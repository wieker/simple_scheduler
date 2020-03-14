package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.RoutingService;
import org.allesoft.simple_scheduler.scheduler.core.SimplePrefetcher;
import org.allesoft.simple_scheduler.scheduler.tools.SingleThreadExecutionService;

public class SchedulerMain {
    public static void main(String[] args) {
        RoutingService routingService = CrowFlightRoutingService::new;
        SnapshotProvider snapshotSupplier = new DbSnapshotProvider();
        Scheduler scheduler = new SchedulerImpl(matrix -> new Algorithm().allocateMatrix(matrix),
                routingService,
                new SingleThreadExecutionService(),
                new OptionCalculationCacheImpl(),
                new SimplePrefetcher(),
                CrowFlightRoutingService::new,
                CrowFlightRoutingService::new
        );
        scheduler.setSnapshot(snapshotSupplier.loadSnapshot());
        scheduler.run();
    }

}
