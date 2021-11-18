package org.allesoft.enterprise_db.queue.service.impl;

import java.util.concurrent.ArrayBlockingQueue;

public class BlockingQueueBinarySemaphore {
    private ArrayBlockingQueue<Boolean> queue = new ArrayBlockingQueue<>(1);

    public void signal() {
        queue.offer(true);
    }

    public void await() {
        boolean retry = false;
        do {
            try {
                queue.take();
            } catch (InterruptedException e) {
                retry = true;
            }
        } while (retry);
    }
}
