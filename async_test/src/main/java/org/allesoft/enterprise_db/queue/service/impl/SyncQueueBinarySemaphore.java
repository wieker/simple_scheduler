package org.allesoft.enterprise_db.queue.service.impl;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class SyncQueueBinarySemaphore {
    public class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 0;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(1, 0);
        }

        @Override
        protected boolean tryRelease(int arg) {
            setState(1);
            return true;
        }
    }

    private final Sync sync = new Sync();

    public void signal() {
        sync.release(0);
    }

    public void await() {
        sync.acquire(1);
    }
}
