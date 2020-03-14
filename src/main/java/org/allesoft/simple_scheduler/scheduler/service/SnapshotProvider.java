package org.allesoft.simple_scheduler.scheduler.service;

import org.allesoft.simple_scheduler.Snapshot;

public interface SnapshotProvider {
    Snapshot loadSnapshot();
}
