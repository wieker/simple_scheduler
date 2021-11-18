package org.allesoft.enterprise_db.queue.service.impl;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleBinarySemaphore {
    private Semaphore semaphore = new Semaphore(0);
    private AtomicInteger fired = new AtomicInteger(0);

    void signal() {
        fired.set(1);
        semaphore.release();
    }

    void await() {
        while (!fired.compareAndSet(1, 0)) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                // retry
            }
        }
    }
}
