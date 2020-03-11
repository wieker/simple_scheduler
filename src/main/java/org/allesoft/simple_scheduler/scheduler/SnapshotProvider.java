package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.Snapshot;

public interface SnapshotProvider {
    Snapshot loadSnapshot();
}
