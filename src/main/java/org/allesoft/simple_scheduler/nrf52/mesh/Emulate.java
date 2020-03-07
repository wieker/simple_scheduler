package org.allesoft.simple_scheduler.nrf52.mesh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Emulate implements Runnable {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i ++) {
            executorService.submit(new Emulate());
        }
    }

    @Override
    public void run() {

    }
}
