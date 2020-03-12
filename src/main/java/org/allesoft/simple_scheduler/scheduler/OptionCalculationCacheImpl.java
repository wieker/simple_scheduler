package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.Job;
import org.allesoft.simple_scheduler.OptionCalculationCache;
import org.allesoft.simple_scheduler.RoutingService;
import org.allesoft.simple_scheduler.Worker;
import org.allesoft.simple_scheduler.scheduler.structure.OptionImpl;

import java.util.HashMap;
import java.util.Map;

class OptionCalculationCacheImpl implements OptionCalculationCache {
    Map<Long, Map<Long, Option>> options = new HashMap<>();
    private RoutingService routingService;

    public OptionCalculationCacheImpl(RoutingService routingService) {
        this.routingService = routingService;
    }

    @Override
    public Option getOption(Job job, Worker driver) {
        options.computeIfAbsent(job.getId(), k -> new HashMap<>());
        if (options.get(job.getId()).get(driver.getId()) != null) {
            return options.get(job.getId()).get(driver.getId());
        }
        Option option = new OptionImpl(routingService, job, driver);
        options.get(job.getId()).put(driver.getId(), option);
        return option;
    }

    @Override
    public void setRoutingService(RoutingService routingService) {

    }

}
