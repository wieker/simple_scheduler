package org.allesoft.simple_scheduler.nrf52.mesh;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLocalArrayBackedQueue<T> {

    final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr;
    ThreadLocal<Integer> local = new ThreadLocal<>();

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Object take(int time) throws InterruptedException {
        lock.lock();
        try {
            Integer position;
            if (local.get() == null) {
                position = takeptr;
                local.set(position);
            } else {
                position = local.get();
            }
            while (position == putptr)
                notEmpty.await(time, TimeUnit.MILLISECONDS);
            Object x = items[position];
            int oldPosition = position;
            if (++position == items.length) {
                takeptr = 0;
            }
            if (oldPosition == takeptr) {
                takeptr = position;
            }
            local.set(position);
            return x;
        } finally {
            lock.unlock();
        }
    }
}
