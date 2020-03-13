package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.*;

import java.util.*;

public class SchedulerImpl implements Scheduler {
    AlgorithmService algorithmService;
    RoutingService routingService;
    TaskExecutorService taskExecutorService;
    OptionCalculationCache optionCalculationCache;
    Snapshot snapshot;
    Prefetcher prefetcher;

    @Override
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public SchedulerImpl(AlgorithmService algorithmService,
                         RoutingService routingService,
                         TaskExecutorService taskExecutorService,
                         OptionCalculationCache optionCalculationCache, Prefetcher prefetcher) {
        this.algorithmService = algorithmService;
        this.routingService = routingService;
        this.taskExecutorService = taskExecutorService;
        this.optionCalculationCache = optionCalculationCache;
        this.prefetcher = prefetcher;
    }

    @Override
    public void run() {
        Collection<Job> jobs = snapshot.getJobs();
        Collection<Worker> drivers = snapshot.getDrivers();
        prepareOptions(jobs, drivers);
        calculateAllocation(jobs, drivers);
    }

    private void calculateAllocation(Collection<Job> jobs, Collection<Worker> drivers) {
        double[][] matrix = new double[jobs.size()][drivers.size()];
        int i = 0;
        int j = 0;
        for (Job job : jobs) {
            for (Worker driver : drivers) {
                matrix[i][j] = optionCalculationCache.getOption(job, driver).getResult();
                j ++;
            }
            i ++;
        }
        int[] allocation = algorithmService.allocateMatrix(matrix);
    }

    private void prepareOptions(Collection<Job> jobs, Collection<Worker> drivers) {
        Collection<Option> options = new ArrayList<>();
        jobs.forEach(job ->
                drivers.stream()
                        .map(driver -> optionCalculationCache.getOption(job, driver))
                        .forEach(options::add));
        prefetcher.prefetch(routingService, options);
        taskExecutorService.execute(routingService, options);
    }

}
