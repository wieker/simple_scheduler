package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.model.Job;
import org.allesoft.simple_scheduler.scheduler.model.Worker;

import java.util.Collection;
import java.util.List;

public interface Snapshot {
    Collection<Worker> getDrivers();

    Collection<Job> getJobs();

    List<Penalty> getPenalties();
}
