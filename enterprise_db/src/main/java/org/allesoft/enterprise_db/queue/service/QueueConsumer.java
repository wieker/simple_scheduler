package org.allesoft.enterprise_db.queue.service;

import java.util.List;

public interface QueueConsumer {
    void save(List<QueueEntry> batch);
}
