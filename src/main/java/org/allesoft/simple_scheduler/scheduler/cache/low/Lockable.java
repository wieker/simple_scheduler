package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.concurrent.atomic.AtomicInteger;

public interface Lockable extends Comparable<Lockable> {
    AtomicInteger getLock();

    int getPosition();
}
