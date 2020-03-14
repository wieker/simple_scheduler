package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.Scheduler;
import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SchedulerTest {
    @InjectMocks
    Scheduler scheduler;
    AlgorithmService algorithmService;
    RoutingService routingService;
    TaskExecutorService taskExecutorService;
    OptionCalculationCache optionCalculationCache;

    @Test
    public void checkAllocation() {
        scheduler.run();
    }

}
