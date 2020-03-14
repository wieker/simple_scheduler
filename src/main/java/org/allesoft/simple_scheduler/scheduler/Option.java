package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.scheduler.model.Job;
import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.allesoft.simple_scheduler.scheduler.model.Worker;

public interface Option {
    double calculate(RoutingService routingService);

    Job getJob();

    Worker getWorker();

    double getResult();
}
