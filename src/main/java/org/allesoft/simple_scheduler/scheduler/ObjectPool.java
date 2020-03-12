package org.allesoft.simple_scheduler.scheduler;

public interface ObjectPool<T> {
    T getObject();

    void freeObject();
}
