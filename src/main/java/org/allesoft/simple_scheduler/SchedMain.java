package org.allesoft.simple_scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

public class SchedMain {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("scheduling_context.xml");
        context.refresh();
        TaskExecutor taskExecutor = (TaskExecutor) context.getBean("theBestTaskExecutor");
    }
}
