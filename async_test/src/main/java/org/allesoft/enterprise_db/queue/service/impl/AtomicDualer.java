package org.allesoft.enterprise_db.queue.service.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDualer {
    private volatile int value;
    private AtomicInteger lock = new AtomicInteger(0);
    private Queue<Op> ops = new ConcurrentLinkedQueue<>();
    private static TransactionManager transactionManager = new TransactionManager();

    public void addition(Op op) {
        ops.add(op);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static class Op {

    }

    public static class Instance {

    }

    private static class TransactionManager {
        boolean isBefore(Op op) {
            return true;
        }
    }
}
