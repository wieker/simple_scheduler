package org.allesoft.simple_scheduler.scheduler.model;

public interface Job extends GeoPoint, Identified {
    Worker getAssignedWorker();

    void setAssignedWorker(Worker assignedWorker);
}
