package org.allesoft.enterprise_db;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StorageQueueProvider {
    public Queue<String> createQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    public Queue<Boolean> controlQueue() {
        return new ArrayBlockingQueue<>(1);
    }
}
