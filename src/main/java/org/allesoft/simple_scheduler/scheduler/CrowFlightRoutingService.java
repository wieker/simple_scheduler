package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.GeoPoint;

import java.util.Date;

class CrowFlightRoutingService implements Route {
    private final GeoPoint from;
    private final GeoPoint to;

    public CrowFlightRoutingService(GeoPoint from, GeoPoint to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public double distance() {
        return Math.sqrt(
                SchedUtils.sqr(from.lat() - to.lat()) + SchedUtils.sqr(from.lon() - to.lon()));
    }

    @Override
    public double time() {
        return 0;
    }

    @Override
    public GeoPoint from() {
        return from;
    }

    @Override
    public GeoPoint to() {
        return to;
    }

    @Override
    public Date date() {
        return null;
    }
}
