package org.allesoft.simple_scheduler.scheduler.structure;

import org.allesoft.simple_scheduler.Job;
import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.allesoft.simple_scheduler.Worker;
import org.allesoft.simple_scheduler.scheduler.Option;

public class OptionImpl implements Option {
    private final Job job;
    private final Worker driver;

    private double result;

    public OptionImpl(Job job, Worker driver) {
        this.job = job;
        this.driver = driver;
    }

    @Override
    public double calculate(RoutingService routingService) {
        return result = routingService.getRoute(job, driver).distance() * 1.0f;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public Worker getWorker() {
        return driver;
    }

    @Override
    public double getResult() {
        return result;
    }
}
