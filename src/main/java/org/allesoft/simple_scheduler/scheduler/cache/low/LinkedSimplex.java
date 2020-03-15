package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LinkedSimplex {
    static final int DIMENSIONS = 1;
    static final int LAYERS = 3;
    List<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    LinkedSimplex nextLayer = null;
    List<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
    MultiPoint value;

    LinkedSimplex search(MultiPoint point, int layer) {
        return search(point, layer, (x, y) -> y, x -> x);
    }

    LinkedSimplex search(MultiPoint point, int layer,
                         BiFunction<LinkedSimplex, LinkedSimplex, LinkedSimplex> processor,
                         Function<LinkedSimplex, LinkedSimplex> finalProcesor) {
        if (inSimplex(point)) {
            if (layer < LAYERS - 1) {
                return processor.apply(this, nextLayer.search(point, layer + 1, processor, finalProcesor));
            } else {
                return finalProcesor.apply(this);
            }
        } else {
            LinkedSimplex linkedSimplex = bestNeighbour(point);
            if (linkedSimplex != null) {
                return linkedSimplex.search(point, layer, processor, finalProcesor);
            } else {
                return null;
            }
        }
    }

    void insert(MultiPoint point) {
        ArrayList<LinkedSimplex> path = new ArrayList<>();
        search(point, 0,
                (currentSimplex, lowerSimplex) -> splitUpperLevelSimplex(point, currentSimplex, lowerSimplex),
                currentSimplex -> split(point, currentSimplex));
    }

    private LinkedSimplex splitUpperLevelSimplex(MultiPoint point, LinkedSimplex topSimplex, LinkedSimplex newSimplex) {
        if (newSimplex != null && new Random().nextBoolean()) {
            return split(point, topSimplex, newSimplex);
        }
        return null;
    }

    private static LinkedSimplex split(MultiPoint point, LinkedSimplex simplex) {
        return split(point, simplex, null);
    }

    private static LinkedSimplex split(MultiPoint point, LinkedSimplex simplex, LinkedSimplex next) {
        LinkedSimplex linkedSimplex;
        linkedSimplex = new LinkedSimplex();
        linkedSimplex.value = point;
        MultiPoint median = median(point, simplex.value);
        if (point.getPos() < simplex.value.getPos()) {
            linkedSimplex.neighbours.add(simplex.neighbours.get(0));
            linkedSimplex.neighbours.add(simplex);
            linkedSimplex.boundaries.add(simplex.boundaries.get(0));
            linkedSimplex.boundaries.add(median);
            simplex.boundaries.set(0, median);
            simplex.neighbours.set(0, linkedSimplex);
        } else {
            linkedSimplex.neighbours.add(simplex);
            linkedSimplex.neighbours.add(simplex.neighbours.get(1));
            linkedSimplex.boundaries.add(median);
            linkedSimplex.boundaries.add(simplex.boundaries.get(1));
            simplex.boundaries.set(1, median);
            simplex.neighbours.set(1, linkedSimplex);
        }
        linkedSimplex.nextLayer = next;
        return linkedSimplex;
    }

    static MultiPoint median(MultiPoint a, MultiPoint b) {
        return new MultiPoint(Math.max(a.getPos(), b.getPos()));
    }

    boolean inSimplex(MultiPoint point) {
        return point.getPos() >= boundaries.get(0).getPos() && point.getPos() < boundaries.get(1).getPos();
    }

    LinkedSimplex bestNeighbour(MultiPoint point) {
        return point.getPos() < boundaries.get(0).getPos() ? neighbours.get(0) : neighbours.get(1);
    }

    public static void main(String[] args) {
        LinkedSimplex linkedSimplex = createForLayer(0);

        linkedSimplex.insert(new MultiPoint(20));
        linkedSimplex.insert(new MultiPoint(28));
        linkedSimplex.insert(new MultiPoint(24));
        linkedSimplex.insert(new MultiPoint(9));
        linkedSimplex.insert(new MultiPoint(2));
        linkedSimplex.insert(new MultiPoint(22));
        linkedSimplex.insert(new MultiPoint(48));

        print(linkedSimplex);
        print(linkedSimplex, 1);
        print(linkedSimplex, 2);
    }

    private static void print(LinkedSimplex linkedSimplex) {
        print(linkedSimplex, 0);
    }

    private static void print(LinkedSimplex linkedSimplex, int layer) {
        linkedSimplex = linkedSimplex.search(new MultiPoint(1), layer);
        while (true) {
            if (linkedSimplex == null) {
                break;
            }
            System.out.print(linkedSimplex);
            if (linkedSimplex.neighbours.size() < 2) {
                break;
            }
            LinkedSimplex nextSimplex = linkedSimplex.neighbours.get(1);
            if (nextSimplex == null) {
                break;
            }
            linkedSimplex = nextSimplex;
        }
        System.out.println();
    }

    private static LinkedSimplex createForLayer(int layer) {
        if (layer == LAYERS) {
            return null;
        }
        LinkedSimplex linkedSimplex = new LinkedSimplex();
        linkedSimplex.value = new MultiPoint(10);
        linkedSimplex.boundaries.add(0, new MultiPoint(0));
        linkedSimplex.boundaries.add(1, new MultiPoint(100));
        linkedSimplex.neighbours.add(null);
        linkedSimplex.neighbours.add(null);
        linkedSimplex.nextLayer = createForLayer(layer + 1);
        return linkedSimplex;
    }

    @Override
    public String toString() {
        return " [ " + boundaries.get(0).getPos() + " " + value.getPos() + " " + boundaries.get(1).getPos() + " ] ";
    }
}
