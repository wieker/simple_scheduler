package org.allesoft.simple_scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

public class TaskTestMain {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("execution_context.xml");
        context.refresh();
        TaskExecutor taskExecutor = (TaskExecutor) context.getBean("theBestTaskExecutor");
        AtomicInteger counter = new AtomicInteger(0);
        AtomicInteger finished = new AtomicInteger(10000);
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i ++) {
            taskExecutor.execute(() -> {
                System.out.println("Started " + counter.incrementAndGet());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                finished.decrementAndGet();
            });
        }
        System.out.println("scheduled");
        latch.await();
        System.out.println(finished.get());
    }
}
