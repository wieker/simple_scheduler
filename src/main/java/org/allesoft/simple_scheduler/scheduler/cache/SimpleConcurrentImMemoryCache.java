package org.allesoft.simple_scheduler.scheduler.cache;

import org.allesoft.simple_scheduler.scheduler.model.GeoPoint;
import org.allesoft.simple_scheduler.scheduler.structure.Route;

import java.util.Date;
import java.util.List;

public class SimpleConcurrentImMemoryCache implements InMemoryRoutingCache {


    @Override
    public Route getRoute(GeoPoint from, GeoPoint to, Date date) {
        return null;
    }

    @Override
    public Route findCachedRoute(Route from, Route to, Date date) {
        return null;
    }

    @Override
    public List<Route> getRoutes(List<Route> from, Route to) {
        return null;
    }

    @Override
    public List<Route> getRoutes(Route from, List<Route> to) {
        return null;
    }

    @Override
    public List<Route> getRoutesMatrix(List<Route> from, Route to) {
        return null;
    }
}
