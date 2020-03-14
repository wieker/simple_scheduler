package org.allesoft.simple_scheduler.scheduler.structure;

import org.allesoft.simple_scheduler.scheduler.SchedUtils;
import org.allesoft.simple_scheduler.scheduler.model.GeoPoint;

import java.util.Date;

public class CalculatedByCrowFlightRoute implements Route {
    private final GeoPoint from;
    private final GeoPoint to;

    public CalculatedByCrowFlightRoute(GeoPoint from, GeoPoint to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public double distance() {
        return Math.sqrt(
                SchedUtils.sqr(from.getLat() - to.getLat()) + SchedUtils.sqr(from.getLon() - to.getLon()));
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
