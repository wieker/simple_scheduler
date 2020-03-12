package org.allesoft.simple_scheduler.scheduler.structure;

import org.allesoft.simple_scheduler.Job;
import org.allesoft.simple_scheduler.RoutingService;
import org.allesoft.simple_scheduler.Worker;
import org.allesoft.simple_scheduler.scheduler.Option;

public class OptionImpl implements Option {
    private RoutingService routingService;
    private final Job job;
    private final Worker driver;

    public OptionImpl(RoutingService routingService, Job job, Worker driver) {
        this.routingService = routingService;
        this.job = job;
        this.driver = driver;
    }

    @Override
    public double calculate() {
        return routingService.getRoute(job, driver).distance() * 1.0f;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public Worker getWorker() {
        return driver;
    }
}
