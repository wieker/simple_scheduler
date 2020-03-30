package org.allesoft.enterprise_db.queue.service.impl;

import org.allesoft.enterprise_db.queue.service.AsyncPersistService;
import org.allesoft.enterprise_db.queue.service.QueueConsumer;
import org.allesoft.enterprise_db.queue.service.QueueEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class AsyncPersistServiceImpl implements AsyncPersistService {
    private Queue<QueueEntry> dataQueue;
    private Integer batchSize;
    private List<QueueEntry> batch;
    private QueueConsumer consumer;
    private Queue<Boolean> controlQueue;

    public AsyncPersistServiceImpl(Queue<QueueEntry> dataQueue, Integer batchSize, QueueConsumer consumer, Queue<Boolean> controlQueue) {
        this.dataQueue = dataQueue;
        this.batchSize = batchSize;
        this.consumer = consumer;
        this.controlQueue = controlQueue;
    }

    @Override
    public void flush() throws InterruptedException {
        while (true) {
            int size = 0;
            batch = new ArrayList<>(batchSize);
            Iterator<QueueEntry> iterator = dataQueue.iterator();
            for (int i = 0; i < batchSize; i++) {
                if (!iterator.hasNext()) {
                    break;
                }
                QueueEntry element = iterator.next();
                batch.add(i, element);
                size = i + 1;
            }
            if (size > 0) {
                consumer.save(batch);
            }
            for (int i = 0; i < size; i++) {
                dataQueue.poll();
            }
            if (size == 0) {
                ((ArrayBlockingQueue<Boolean>) controlQueue).take();
            }
        }
    }
}
