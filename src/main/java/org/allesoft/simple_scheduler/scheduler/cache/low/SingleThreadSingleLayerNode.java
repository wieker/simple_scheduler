package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.allesoft.simple_scheduler.scheduler.SchedUtils.sqr;

public class SingleThreadSingleLayerNode {
    static final int dimension = 2;
    List<SingleThreadSingleLayerNode> neighbours = new ArrayList<>(dimension + 1);
    List<SingleThreadSingleLayerNode> layers;
    int x;
    int y;

    private int compareByDistanceAndAngle(SingleThreadSingleLayerNode node, SingleThreadSingleLayerNode t1, SingleThreadSingleLayerNode base) {
        return Double.compare(distance(node, base),
                distance(t1, base));
    }

    private static double distance(SingleThreadSingleLayerNode node, SingleThreadSingleLayerNode t) {
        return Math.sqrt(sqr(node.x - t.x) + sqr(node.y - t.y));
    }

    SingleThreadSingleLayerNode search(Set<SingleThreadSingleLayerNode> passed, SingleThreadSingleLayerNode find) {
        neighbours.sort((node, t1) -> compareByDistanceAndAngle(node, t1, find));
        return neighbours.stream().filter(passed::contains).findFirst().orElse(null);
    }

    boolean inSimplex() {
        return false;
    }

    void searchSimplex() {

    }

    void insertToLayer() {

    }

    void removeFromLayer() {

    }
}
