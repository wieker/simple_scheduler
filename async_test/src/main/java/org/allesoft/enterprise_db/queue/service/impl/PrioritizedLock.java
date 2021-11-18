package org.allesoft.enterprise_db.queue.service.impl;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PrioritizedLock {
    private Queue<Thread> threadQueue = new ConcurrentLinkedQueue<>();
    private volatile Thread owningThread;
    private static Map<Thread, ThreadInfo> threadInfoMap = new ConcurrentHashMap<>();
    private AtomicInteger lock;

    public void lock() {
        if (!lock.compareAndSet(0, 1)) {
            ThreadInfo running = threadInfoMap.get(owningThread);
            ThreadInfo currentInfo = threadInfoMap.get(Thread.currentThread());
            if (running.getPriority() < currentInfo.getPriority()) {
                running.setKilled(true);
                running.getLocked().forceUnlock();
            }
        } else {
            owningThread = Thread.currentThread();
        }
    }

    public void unlock() {
        owningThread = null;
        lock.set(0);
    }

    public void forceUnlock() {

    }

    static class ThreadInfo {
        private volatile boolean killed;
        private volatile PrioritizedLock locked;
        private int priority;

        public boolean isKilled() {
            return killed;
        }

        public void setKilled(boolean killed) {
            this.killed = killed;
        }

        public PrioritizedLock getLocked() {
            return locked;
        }

        public void setLocked(PrioritizedLock locked) {
            this.locked = locked;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }
}
