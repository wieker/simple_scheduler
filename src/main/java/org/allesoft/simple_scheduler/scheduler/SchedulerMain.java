package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.allesoft.simple_scheduler.scheduler.core.SimplePrefetcher;
import org.allesoft.simple_scheduler.scheduler.service.DbSnapshotProvider;
import org.allesoft.simple_scheduler.scheduler.service.SnapshotProvider;
import org.allesoft.simple_scheduler.scheduler.structure.CalculatedByCrowFlightRoute;
import org.allesoft.simple_scheduler.scheduler.tools.SingleThreadExecutionService;

public class SchedulerMain {
    public static void main(String[] args) {
        RoutingService routingService = (from, to, date) -> new CalculatedByCrowFlightRoute(from, to);
        SnapshotProvider snapshotSupplier = new DbSnapshotProvider();
        Scheduler scheduler = new SchedulerImpl(matrix -> new Algorithm().allocateMatrix(matrix),
                routingService,
                new SingleThreadExecutionService(),
                new OptionCalculationCacheImpl(),
                new SimplePrefetcher(),
                (from, to, date) -> new CalculatedByCrowFlightRoute(from, to),
                (from1, to1, date) -> new CalculatedByCrowFlightRoute(from1, to1)
        );
        scheduler.setSnapshot(snapshotSupplier.loadSnapshot());
        scheduler.run();
    }

}
