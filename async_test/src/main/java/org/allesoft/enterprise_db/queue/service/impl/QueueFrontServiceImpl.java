package org.allesoft.enterprise_db.queue.service.impl;

import org.allesoft.enterprise_db.queue.service.QueueEntry;
import org.allesoft.enterprise_db.queue.service.QueueFrontService;

import java.util.Queue;

public class QueueFrontServiceImpl implements QueueFrontService {
    Queue<QueueEntry> dataQueue;
    Queue<Boolean> controlQueue;

    public QueueFrontServiceImpl(Queue<QueueEntry> dataQueue, Queue<Boolean> controlQueue) {
        this.dataQueue = dataQueue;
        this.controlQueue = controlQueue;
    }

    @Override
    public void save(QueueEntry logEntry) {
        dataQueue.offer(logEntry);
        controlQueue.offer(true);
    }
}
