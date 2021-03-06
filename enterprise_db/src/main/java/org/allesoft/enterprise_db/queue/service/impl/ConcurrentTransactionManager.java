package org.allesoft.enterprise_db.queue.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentTransactionManager {
    public void commit(List<Lockable> readLocks, List<Lockable> writeLocks, List<AtomicReference<Object>> listToUpdate, List<Object> newValues) {
        List<Lockable> orderedList = new ArrayList<>(readLocks);
        orderedList.addAll(writeLocks);
        orderedList.sort(Comparator.naturalOrder()); // and filter duplicates
        for (int i = 0; i < orderedList.size(); i ++) {
            if (!orderedList.get(i).getLock().compareAndSet(0, 1)) {
                cleanup(orderedList, i);
                throw new RuntimeException("locked -> retry");
            }
        }
        for (int i = 0; i < listToUpdate.size(); i ++) {
            listToUpdate.get(i).set(newValues.get(i));
        }
        cleanup(readLocks, writeLocks.size());
    }

    private void cleanup(List<Lockable> orderedList, int width) {
        for (int j = 0; j < width; j ++) {
            orderedList.get(j).getLock().set(0);
        }
    }
}
