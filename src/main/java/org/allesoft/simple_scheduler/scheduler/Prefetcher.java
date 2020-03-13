package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.RoutingService;

import java.util.Collection;

public interface Prefetcher {
    void prefetch(RoutingService routingService, Collection<Option> options);
}
