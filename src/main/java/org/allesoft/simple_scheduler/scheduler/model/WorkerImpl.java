package org.allesoft.simple_scheduler.scheduler.model;

public class WorkerImpl implements Worker {

    final private int lat;
    final private int lon;
    final private long id;

    public WorkerImpl(int lat, int lon, long id) {
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
}
