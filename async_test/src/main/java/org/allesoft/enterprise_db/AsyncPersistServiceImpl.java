package org.allesoft.enterprise_db;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AsyncPersistServiceImpl implements AsyncPersistService {
    private Queue<String> dataQueue;
    private Integer batchSize;
    private List<String> batch;
    private PersistenceDao persistenceDao;
    private Queue<Boolean> controlQueue;

    public AsyncPersistServiceImpl(Queue<String> dataQueue, Integer batchSize, PersistenceDao persistenceDao, Queue<Boolean> controlQueue) {
        this.dataQueue = dataQueue;
        this.batchSize = batchSize;
        this.persistenceDao = persistenceDao;
        this.controlQueue = controlQueue;

        batch = new ArrayList<>(batchSize);
    }

    @Override
    public void flush() {
        for (;;) {
            int size = 0;
            for (int i = 0; i < batchSize; i++) {
                String element = dataQueue.peek();
                if (element == null) {
                    break;
                }
                batch.add(i, element);
                size = i;
            }
            if (size > 0) {
                persistenceDao.save(batch, size);
            }
            for (int i = 0; i < size; i++) {
                dataQueue.poll();
            }
            if (size == 0) {
                controlQueue.poll();
            }
        }
    }
}
