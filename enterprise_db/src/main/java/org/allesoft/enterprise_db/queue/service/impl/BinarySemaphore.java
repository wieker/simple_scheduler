package org.allesoft.enterprise_db.queue.service.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class BinarySemaphore {
    private volatile Thread workThread;
    private AtomicInteger fired = new AtomicInteger(0);

    void signal() {
        fired.set(1);
        LockSupport.unpark(workThread);
    }

    void wait(Thread workThread) {
        this.workThread = workThread;
        while (!fired.compareAndSet(1, 0)) {
            LockSupport.park();
        }
    }
}
