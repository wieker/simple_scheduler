package org.allesoft.simple_scheduler;

import java.sql.Driver;
import java.util.Collection;
import java.util.List;

public interface Snapshot {
    Collection<Driver> getDrivers();

    Collection<Job> getJobs();

    List<Penalty> getPenalties();
}
