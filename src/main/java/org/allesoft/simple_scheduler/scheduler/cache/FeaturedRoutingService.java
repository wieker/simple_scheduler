package org.allesoft.simple_scheduler.scheduler.cache;

import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.allesoft.simple_scheduler.scheduler.structure.Route;

import java.util.List;

public interface FeaturedRoutingService extends RoutingService {
    List<Route> getRoutes(List<Route> from, Route to);

    List<Route> getRoutes(Route from, List<Route> to);

    List<Route> getRoutesMatrix(List<Route> from, Route to);
}
