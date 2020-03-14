package org.allesoft.simple_scheduler.scheduler.tools;

import org.allesoft.simple_scheduler.scheduler.service.RoutingService;
import org.allesoft.simple_scheduler.TaskExecutorService;
import org.allesoft.simple_scheduler.scheduler.Option;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SingleThreadExecutionService implements TaskExecutorService {
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void execute(RoutingService routingService, Collection<Option> options) {
        options.forEach(option ->
                executor.execute(() -> option.calculate(routingService)));
    }
}
