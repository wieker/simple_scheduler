package org.allesoft.enterprise_db;

import java.util.Queue;

public class QueueFillerProvider {
    public Runnable filler(Queue<String> dataQueue, Queue<Boolean> controlQueue, Integer pushSize) {
        return () -> {
            for (int i = 0; i < pushSize; i ++) {
                dataQueue.offer("str" + System.currentTimeMillis());
                controlQueue.offer(true);
            }
        };
    }
}
