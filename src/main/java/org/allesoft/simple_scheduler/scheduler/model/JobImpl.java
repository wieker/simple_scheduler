package org.allesoft.simple_scheduler.scheduler.model;

public class JobImpl implements Job {
    private final int lat;
    private final int lon;
    private final long id;
    private Worker assignedWorker;

    public JobImpl(int lat, int lon, long id) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Worker getAssignedWorker() {
        return assignedWorker;
    }

    @Override
    public void setAssignedWorker(Worker assignedWorker) {
        this.assignedWorker = assignedWorker;
    }
}
