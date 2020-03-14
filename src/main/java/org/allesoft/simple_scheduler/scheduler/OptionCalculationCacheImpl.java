package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.scheduler.model.Job;
import org.allesoft.simple_scheduler.OptionCalculationCache;
import org.allesoft.simple_scheduler.scheduler.model.Worker;
import org.allesoft.simple_scheduler.scheduler.structure.OptionImpl;

import java.util.HashMap;
import java.util.Map;

class OptionCalculationCacheImpl implements OptionCalculationCache {
    Map<Long, Map<Long, Option>> options = new HashMap<>();

    public OptionCalculationCacheImpl() {
    }

    @Override
    public Option getOption(Job job, Worker driver) {
        options.computeIfAbsent(job.getId(), k -> new HashMap<>());
        if (options.get(job.getId()).get(driver.getId()) != null) {
            return options.get(job.getId()).get(driver.getId());
        }
        Option option = new OptionImpl(job, driver);
        options.get(job.getId()).put(driver.getId(), option);
        return option;
    }

}
