package org.allesoft.simple_scheduler.scheduler.service;

import org.allesoft.simple_scheduler.GeoPoint;
import org.allesoft.simple_scheduler.scheduler.Route;

public interface RoutingService {
    Route getRoute(GeoPoint from, GeoPoint to);
}
