package org.allesoft.simple_scheduler.scheduler.service;

import org.allesoft.simple_scheduler.scheduler.model.GeoPoint;
import org.allesoft.simple_scheduler.scheduler.structure.Route;

import java.util.Date;

public interface RoutingService {
    Route getRoute(GeoPoint from, GeoPoint to, Date date);
}
