package org.allesoft.enterprise_db.queue.service;

public interface AsyncPersistService {
    void flush() throws InterruptedException;
}
