package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.*;

import java.util.*;

public class SchedulerImpl implements Scheduler {
    AlgorithmService algorithmService;
    RoutingService routingService;
    TaskExecutorService taskExecutorService;
    OptionCalculationCache optionCalculationCache;
    Snapshot snapshot;

    @Override
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public SchedulerImpl(AlgorithmService algorithmService,
                         RoutingService routingService,
                         TaskExecutorService taskExecutorService,
                         OptionCalculationCache optionCalculationCache) {
        this.algorithmService = algorithmService;
        this.routingService = routingService;
        this.taskExecutorService = taskExecutorService;
        this.optionCalculationCache = optionCalculationCache;
    }

    @Override
    public void run() {
        optionCalculationCache.setRoutingService(routingService);
        Collection<Job> jobs = snapshot.getJobs();
        Collection<Worker> drivers = snapshot.getDrivers();
        jobs.forEach(job ->
                drivers.forEach(driver ->
                        optionCalculationCache.getOption(job, driver)));
        double[][] matrix = new double[jobs.size()][drivers.size()];
        int i = 0;
        int j = 0;
        for (Job job : jobs) {
            for (Worker driver : drivers) {
                matrix[i][j] = optionCalculationCache.getOption(job, driver).calculate();
                j ++;
            }
            i ++;
        }
        algorithmService.allocateMatrix(matrix);
    }

}
