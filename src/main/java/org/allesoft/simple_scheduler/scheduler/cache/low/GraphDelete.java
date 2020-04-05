package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GraphDelete {
    public void delete(GraphNode node) {
        List<GraphNode> writeLocks = node.getNeighbours().stream().map(AtomicReference::get).collect(Collectors.toList());
        List<AtomicReference<GraphNode>> nodesToUpdate = writeLocks.stream().flatMap(n -> n.getNeighbours().stream()).filter(n -> n.get().equals(this)).collect(Collectors.toList());
        List<GraphNode> newValues = new ArrayList<>();
        for (int i = 0; i < nodesToUpdate.size(); i ++) {
            newValues.add(null);
        }
        new ConcurrentTransactionManager().commit(writeLocks, new ArrayList<>(), nodesToUpdate, newValues);
    }
}
