package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.Option;

public interface OptionCalculationCache {
    Option getOption(Job job, Worker driver);

}
