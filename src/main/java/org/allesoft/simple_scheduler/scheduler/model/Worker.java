package org.allesoft.simple_scheduler.scheduler.model;

public interface Worker extends GeoPoint, Identified {
    Job getAssignedJob();

    void setAssignedJob(Job assignedJob);
}
