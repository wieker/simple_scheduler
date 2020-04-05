package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GraphNode implements Lockable {
    private static int counter = 0;
    private int position;
    private ArrayList<AtomicReference<GraphNode>> nodes;
    private AtomicInteger lock = new AtomicInteger();

    public GraphNode() {
        position = counter ++;
        nodes = new ArrayList<>();
    }

    public int getPosition() {
        return position;
    }

    List<AtomicReference<GraphNode>> getNeighbours() {
        return nodes;
    }

    @Override
    public AtomicInteger getLock() {
        return lock;
    }

    @Override
    public int compareTo(Lockable lockable) {
        return Integer.compare(position, lockable.getPosition());
    }
}
