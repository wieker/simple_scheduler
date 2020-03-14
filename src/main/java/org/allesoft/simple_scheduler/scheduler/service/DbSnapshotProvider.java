package org.allesoft.simple_scheduler.scheduler.service;

import org.allesoft.simple_scheduler.Job;
import org.allesoft.simple_scheduler.Penalty;
import org.allesoft.simple_scheduler.Snapshot;
import org.allesoft.simple_scheduler.Worker;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DbSnapshotProvider implements SnapshotProvider {
    @Override
    public Snapshot loadSnapshot() {
        return new Snapshot() {
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
        };
    }
}
