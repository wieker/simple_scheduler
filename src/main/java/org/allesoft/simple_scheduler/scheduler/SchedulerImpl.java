package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.AlgorithmService;
import org.allesoft.simple_scheduler.scheduler.model.Job;
import org.allesoft.simple_scheduler.OptionCalculationCache;
import org.allesoft.simple_scheduler.Snapshot;
import org.allesoft.simple_scheduler.TaskExecutorService;
import org.allesoft.simple_scheduler.scheduler.model.Worker;
import org.allesoft.simple_scheduler.scheduler.service.RoutingService;

import java.util.*;

public class SchedulerImpl implements Scheduler {
    AlgorithmService algorithmService;
    RoutingService simplestRoutingService;
    RoutingService lowCostRoutingService;
    RoutingService finalRoutingService;
    TaskExecutorService taskExecutorService;
    OptionCalculationCache optionCalculationCache;
    Snapshot snapshot;
    Prefetcher prefetcher;

    @Override
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public SchedulerImpl(AlgorithmService algorithmService,
                         RoutingService finalRoutingService,
                         TaskExecutorService taskExecutorService,
                         OptionCalculationCache optionCalculationCache, Prefetcher prefetcher,
                         RoutingService simplestRoutingService, RoutingService lowCostRoutingService) {
        this.algorithmService = algorithmService;
        this.finalRoutingService = finalRoutingService;
        this.taskExecutorService = taskExecutorService;
        this.optionCalculationCache = optionCalculationCache;
        this.prefetcher = prefetcher;
        this.simplestRoutingService = simplestRoutingService;
        this.lowCostRoutingService = lowCostRoutingService;
    }

    @Override
    public void run() {
        List<Job> jobs = Collections.unmodifiableList(new ArrayList<>(snapshot.getJobs()));
        List<Worker> drivers = Collections.unmodifiableList(new ArrayList<>(snapshot.getDrivers()));
        prepareOptions(jobs, drivers);
        calculateAllocation(jobs, drivers);
    }

    private void calculateAllocation(List<Job> jobs, List<Worker> drivers) {
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
        int jobIndex = 0;
        for (int driverIndex : allocation) {
            Worker assignedWorker = drivers.get(driverIndex);
            Job job = jobs.get(jobIndex);
            job.setAssignedWorker(assignedWorker);
            assignedWorker.setAssignedJob(job);
            jobIndex ++;
        }
    }

    private void prepareOptions(Collection<Job> jobs, Collection<Worker> drivers) {
        Collection<Option> options = new ArrayList<>();
        jobs.forEach(job ->
                drivers.stream()
                        .map(driver -> optionCalculationCache.getOption(job, driver))
                        .forEach(options::add));
        prefetcher.prefetch(finalRoutingService, options);
        taskExecutorService.execute(finalRoutingService, options);
    }

}
