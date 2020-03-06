package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static double sqr(double a) {
        return a * a;
    }

    @Override
    public void run() {
        routingService = (from, to) -> new Route() {
            @Override
            public double distance() {
                return Math.sqrt(
                        sqr(from.lat() - to.lat()) + sqr(from.lon() - to.lon()));
            }

            @Override
            public double time() {
                return 0;
            }

            @Override
            public GeoPoint from() {
                return from;
            }

            @Override
            public GeoPoint to() {
                return to;
            }
        };

        optionCalculationCache = new OptionCalculationCache() {
            Map<Long, Map<Long, Option>> options = new HashMap<>();

            @Override
            public Option getOption(Job job, Worker driver) {
                options.computeIfAbsent(job.getId(), k -> new HashMap<>());
                if (options.get(job.getId()).get(driver.getId()) != null) {
                    return options.get(job.getId()).get(driver.getId());
                }
                Option option = new Option() {
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
                };
                options.get(job.getId()).put(driver.getId(), option);
                return option;
            }

            @Override
            public void setRoutingService(RoutingService routingService) {

            }
        };

        algorithmService = matrix -> new Algorithm().allocateMatrix(matrix);

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

    @Override
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public static void main(String[] args) {
        Scheduler scheduler = new SchedulerImpl();
        scheduler.setSnapshot(new Snapshot() {
            @Override
            public Collection<Worker> getDrivers() {
                return Arrays.asList(new Worker() {
                    @Override
                    public double lat() {
                        return 0;
                    }

                    @Override
                    public double lon() {
                        return 0;
                    }

                    @Override
                    public Long getId() {
                        return 2l;
                    }
                }, new Worker() {
                    @Override
                    public double lat() {
                        return 1;
                    }

                    @Override
                    public double lon() {
                        return 0;
                    }

                    @Override
                    public Long getId() {
                        return 2l;
                    }
                });
            }

            @Override
            public Collection<Job> getJobs() {
                return Arrays.asList(new Job() {
                    @Override
                    public double lat() {
                        return 1;
                    }

                    @Override
                    public double lon() {
                        return 1;
                    }

                    @Override
                    public Long getId() {
                        return 1l;
                    }
                });
            }

            @Override
            public List<Penalty> getPenalties() {
                return null;
            }
        });
    }
}
