package org.allesoft.simple_scheduler.scheduler.experiment;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {
    @Scheduled(fixedRate = 1000l)
    public void run() throws InterruptedException {
        System.out.println("here 1 " + Thread.currentThread().getName());
        Thread.sleep(2000l);
        System.out.println("there " + Thread.currentThread().getName());
    }
}
