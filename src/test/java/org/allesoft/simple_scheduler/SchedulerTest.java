package org.allesoft.simple_scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.allesoft.simple_scheduler.scheduler.Prefetcher;
import org.allesoft.simple_scheduler.scheduler.Scheduler;
import org.allesoft.simple_scheduler.scheduler.SchedulerImpl;
import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {
    @InjectMocks
    Scheduler scheduler = new SchedulerImpl(null, null, null, null, null, null, null);
    @Mock
    AlgorithmService algorithmService;
    @Mock
    RoutingService simplestRoutingService;
    @Mock
    RoutingService lowCostRoutingService;
    @Mock
    RoutingService finalRoutingService;
    @Mock
    TaskExecutorService taskExecutorService;
    @Mock
    OptionCalculationCache optionCalculationCache;
    @Mock
    Snapshot snapshot;
    @Mock
    Prefetcher prefetcher;

    @Test
    public void checkAllocation() {
        scheduler.run();

        verify(algorithmService).allocateMatrix(any());
    }

}
