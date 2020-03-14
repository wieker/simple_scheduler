package org.allesoft.simple_scheduler.scheduler.service;

import org.allesoft.simple_scheduler.scheduler.model.Job;
import org.allesoft.simple_scheduler.Penalty;
import org.allesoft.simple_scheduler.Snapshot;
import org.allesoft.simple_scheduler.scheduler.model.JobImpl;
import org.allesoft.simple_scheduler.scheduler.model.Worker;
import org.allesoft.simple_scheduler.scheduler.model.WorkerImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleTestSnapshotProvider implements SnapshotProvider {
    @Override
    public Snapshot loadSnapshot() {
        return new Snapshot() {
            @Override
            public Collection<Worker> getDrivers() {
                return Arrays.asList(new WorkerImpl(0, 0, 1l), new WorkerImpl(1, 0, 2l));
            }

            @Override
            public Collection<Job> getJobs() {
                Job job = new JobImpl(1, 1, 1l);
                return Arrays.asList(job);
            }

            @Override
            public List<Penalty> getPenalties() {
                return null;
            }
        };
    }

}
