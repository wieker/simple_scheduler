package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.Option;

import java.util.Collection;

public interface TaskExecutorService {
    void execute(RoutingService routingService, Collection<Option> option);
}
