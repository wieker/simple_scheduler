package org.allesoft.enterprise_db.queue.service.impl;

import java.util.concurrent.atomic.AtomicInteger;

public interface Lockable extends Comparable<Lockable> {
    AtomicInteger getLock();
}
