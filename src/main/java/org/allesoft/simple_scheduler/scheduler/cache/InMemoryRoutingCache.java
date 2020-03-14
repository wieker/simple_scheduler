package org.allesoft.simple_scheduler.scheduler.cache;

import org.allesoft.simple_scheduler.scheduler.structure.Route;

import java.util.Date;

public interface InMemoryRoutingCache extends FeaturedRoutingService {
    Route findCachedRoute(Route from, Route to, Date date);
}
