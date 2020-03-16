package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        passed.add(this);
        double curDist = distance(find, this);
        if (inSimplex(new MultiPointImplOne(0))) {
            if (emptySimplex()) {
                acquire();
            } else {
                splitSimplex();
            }
        }
        // have to select by angle and compare by distance
        List<Double> neiDist = neighbours.stream().map(nei -> distance(nei, find)).collect(Collectors.toList());
        int i = 0;
        for (Double dist : neiDist) {
            if (dist < curDist) {
                return neighbours.get(i).search(passed, find);
            }
            i++;
        }
        if (goDown() == null) {

        }
        neighbours.sort((node, t1) -> compareByDistanceAndAngle(node, t1, find));
        return neighbours.stream().filter(passed::contains).findFirst().orElse(null);
    }

    void findSimplex() {

    }

    boolean emptySimplex() {
        return false;
    }

    void splitSimplex() {

    }

    void acquire() {

    }

    SingleThreadSingleLayerNode goDown() {
        return null;
    }

    boolean inSimplex(MultiPoint multiPoint) {
        return false;
    }

    void searchSimplex() {

    }

    void insertToLayer() {

    }

    void removeFromLayer() {

    }
}
