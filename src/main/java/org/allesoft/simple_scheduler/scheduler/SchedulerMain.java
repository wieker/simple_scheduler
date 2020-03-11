package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.RoutingService;

public class SchedulerMain {
    public static void main(String[] args) {
        RoutingService routingService = CrowFlightRoutingService::new;
        SnapshotProvider snapshotSupplier = new DbSnapshotProvider();
        Scheduler scheduler = new SchedulerImpl(matrix -> new Algorithm().allocateMatrix(matrix),
                routingService,
                null,
                new OptionCalculationCacheImpl(routingService)
        );
        scheduler.setSnapshot(snapshotSupplier.loadSnapshot());
        scheduler.run();
    }
}
