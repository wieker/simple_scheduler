package org.allesoft.simple_scheduler;

import java.util.Collection;
import java.util.List;

public interface Snapshot {
    Collection<Worker> getDrivers();

    Collection<Job> getJobs();

    List<Penalty> getPenalties();
}
