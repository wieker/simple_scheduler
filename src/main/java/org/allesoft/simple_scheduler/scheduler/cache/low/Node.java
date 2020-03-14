package org.allesoft.simple_scheduler.scheduler.cache.low;

import org.allesoft.simple_scheduler.scheduler.structure.Route;

import java.util.concurrent.ConcurrentSkipListMap;

public class Node {
    ConcurrentSkipListMap<Route, Node> neighbours = new ConcurrentSkipListMap<>(Node::compareByAngleThenDistance);

    private static int compareByAngleThenDistance(Object o, Object t1) {
        return 0;
    }
}
