package org.allesoft.enterprise_db.queue.service.impl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBinarySemaphore {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void signal() {
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    public void await() {
        lock.lock();
        boolean retry = false;
        do {
            try {
                condition.await();
            } catch (InterruptedException e) {
                retry = true;
            }
        } while (retry);
        lock.unlock();
    }
}
