package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.Job;
import org.allesoft.simple_scheduler.RoutingService;
import org.allesoft.simple_scheduler.Worker;

public interface Option {
    double calculate(RoutingService routingService);

    Job getJob();

    Worker getWorker();

    double getResult();
}
