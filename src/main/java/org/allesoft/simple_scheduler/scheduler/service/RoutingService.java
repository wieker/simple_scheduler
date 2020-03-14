package org.allesoft.simple_scheduler.scheduler.service;

import org.allesoft.simple_scheduler.scheduler.model.GeoPoint;
import org.allesoft.simple_scheduler.scheduler.structure.Route;

public interface RoutingService {
    Route getRoute(GeoPoint from, GeoPoint to);
}
