package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentTransactionManager {
    public void commit(List<? extends Lockable> readLocks, List<? extends Lockable> writeLocks, List<? extends AtomicReference<? extends Object>> listToUpdate, List<? extends Object> newValues) {
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
            ((AtomicReference<Object>) listToUpdate.get(i)).set(newValues.get(i));
        }
        cleanup(readLocks, writeLocks.size());
    }

    private void cleanup(List<? extends Lockable> orderedList, int width) {
        for (int j = 0; j < width; j ++) {
            orderedList.get(j).getLock().set(0);
        }
    }
}
