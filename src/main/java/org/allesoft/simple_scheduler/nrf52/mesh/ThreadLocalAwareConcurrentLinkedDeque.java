package org.allesoft.simple_scheduler.nrf52.mesh;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadLocalAwareConcurrentLinkedDeque<T> {
    AtomicInteger threadsCount = new AtomicInteger(0);
    ThreadLocal<Long> position = new ThreadLocal<>();
    AtomicLong headC = new AtomicLong(0);
    AtomicLong tailC = new AtomicLong(0);
    ConcurrentLinkedQueue<T> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

    public ThreadLocalAwareConcurrentLinkedDeque() {
        new Thread(() -> {

        }).start();
    }

    void add(T obj) {
        concurrentLinkedQueue.offer(obj);
    }

    T get() {
        return concurrentLinkedQueue.poll();
    }

    @Override
    protected void finalize() throws Throwable {
        threadsCount.decrementAndGet();
        super.finalize();
    }
}
