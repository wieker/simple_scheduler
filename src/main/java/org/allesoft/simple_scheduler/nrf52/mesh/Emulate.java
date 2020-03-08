package org.allesoft.simple_scheduler.nrf52.mesh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Emulate implements Runnable {
    public static ThreadLocalArrayBackedQueue<Integer> queue = new ThreadLocalArrayBackedQueue<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i ++) {
                    try {
                        queue.put(i);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        for (int i = 0; i < 3; i ++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (;;) {
                            System.out.println(queue.take());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void run() {

    }
}
