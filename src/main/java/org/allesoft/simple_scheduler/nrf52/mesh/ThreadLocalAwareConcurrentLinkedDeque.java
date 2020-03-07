package org.allesoft.simple_scheduler.nrf52.mesh;

import java.lang.invoke.VarHandle;
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

        this.head = this.tail = new ThreadLocalAwareConcurrentLinkedDeque.Node();
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

    transient volatile ThreadLocalAwareConcurrentLinkedDeque.Node<T> head;
    private transient volatile ThreadLocalAwareConcurrentLinkedDeque.Node<T> tail;
    private static final int MAX_HOPS = 8;
    private static final VarHandle HEAD;
    private static final VarHandle TAIL;
    static final VarHandle ITEM;
    static final VarHandle NEXT;

    public boolean offer(T e) {
        ThreadLocalAwareConcurrentLinkedDeque.Node<T> newNode = new ThreadLocalAwareConcurrentLinkedDeque.Node(Objects.requireNonNull(e));
        ThreadLocalAwareConcurrentLinkedDeque.Node<T> t = this.tail;
        ThreadLocalAwareConcurrentLinkedDeque.Node p = t;

        do {
            while(true) {
                ThreadLocalAwareConcurrentLinkedDeque.Node<T> q = p.next;
                if (q == null) {
                    break;
                }

                if (p == q) {
                    p = t != (t = this.tail) ? t : this.head;
                } else {
                    p = p != t && t != (t = this.tail) ? t : q;
                }
            }
        } while(!NEXT.compareAndSet(p, (Void)null, newNode));

        if (p != t) {
            TAIL.weakCompareAndSet(this, t, newNode);
        }

        return true;
    }

    public T poll() {
        label33:
        while(true) {
            ThreadLocalAwareConcurrentLinkedDeque.Node<T> h = this.head;

            ThreadLocalAwareConcurrentLinkedDeque.Node p;
            ThreadLocalAwareConcurrentLinkedDeque.Node q;
            Object item;
            for(p = h; (item = p.item) == null || !p.casItem(item, (Object)null); p = (ThreadLocalAwareConcurrentLinkedDeque.Node) q) {
                if ((q = p.next) == null) {
                    this.updateHead(h, p);
                    return null;
                }

                if (p == q) {
                    continue label33;
                }
            }

            if (p != h) {
                this.updateHead(h, (q = p.next) != null ? q : p);
            }

            return (T) item;
        }
    }

    static final class Node<T> {
        volatile T item;
        volatile ThreadLocalAwareConcurrentLinkedDeque.Node<T> next;

        Node(T item) {
            ThreadLocalAwareConcurrentLinkedDeque.ITEM.set(this, item);
        }

        Node() {
        }

        void appendRelaxed(ThreadLocalAwareConcurrentLinkedDeque.Node<T> next) {
            ThreadLocalAwareConcurrentLinkedDeque.NEXT.set(this, next);
        }

        boolean casItem(T cmp, T val) {
            return ThreadLocalAwareConcurrentLinkedDeque.ITEM.compareAndSet(this, cmp, val);
        }
    }

    final void updateHead(ThreadLocalAwareConcurrentLinkedDeque.Node<T> h, ThreadLocalAwareConcurrentLinkedDeque.Node<T> p) {
        if (h != p && HEAD.compareAndSet(this, h, p)) {
            NEXT.setRelease(h, h);
        }

    }
}
