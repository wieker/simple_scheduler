package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.Route;

public interface RoutingService {
    Route getRoute(GeoPoint from, GeoPoint to);
}
