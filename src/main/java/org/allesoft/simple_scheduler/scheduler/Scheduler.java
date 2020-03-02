package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.Snapshot;

public interface Scheduler {
    void run();

    void setSnapshot(Snapshot snapshot);
}
