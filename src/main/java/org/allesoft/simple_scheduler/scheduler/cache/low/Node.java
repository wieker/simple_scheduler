package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.concurrent.ConcurrentSkipListSet;

public class Node {
    ConcurrentSkipListSet<Node> neighbours = new ConcurrentSkipListSet<>(Node::compareByAngleThenDistance);

    private static int compareByAngleThenDistance(Object o, Object t1) {
        return 0;
    }

    public Node getNearest() {
        return null;
    }
}
