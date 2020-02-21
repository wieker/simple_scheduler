package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchedulerImpl implements Scheduler {
    @Autowired
    AlgorithmService algorithmService;
    @Autowired
    RoutingService routingService;
    @Autowired
    TaskExecutorService taskExecutorService;
    @Autowired
    OptionCalculationCache optionCalculationCache;

    Snapshot snapshot;

    @Override
    public void run() {
        optionCalculationCache.setRoutingService(routingService);
        List<Double> matrix = snapshot.getJobs().parallelStream()
                .flatMap(job -> snapshot.getDrivers().parallelStream()
                        .map(driver -> optionCalculationCache.getOption(job, driver)))
                .map(Option::calculate).collect(Collectors.toList());
        // algorithmService.allocate();
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }
}
