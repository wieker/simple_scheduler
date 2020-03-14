package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.Option;
import org.allesoft.simple_scheduler.scheduler.model.Job;
import org.allesoft.simple_scheduler.scheduler.model.Worker;

public interface OptionCalculationCache {
    Option getOption(Job job, Worker driver);

}
