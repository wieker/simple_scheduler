package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class LinkedSimplex {
    static final int DIMENSIONS = 1;
    static final int LAYERS = 3;
    Collection<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    LinkedSimplex nextLayer = null;
    Collection<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
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
            return bestNeighbour(point).map(s -> s.search(point, layer, processor, finalProcesor)).orElse(null);
        }
    }

    void insert(MultiPoint point) {
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
        LinkedSimplex linkedSimplex = new LinkedSimplex();
        linkedSimplex.value = point;

        MultiPoint median = median(point, simplex.value);
        Optional<LinkedSimplex> movedNeighbour = simplex.bestNeighbour(point);
        Optional<MultiPoint> movedBoundary = simplex.bestBoundary(point);

        movedNeighbour.ifPresent(n -> {
                    linkedSimplex.neighbours.add(n);
                    simplex.neighbours.remove(n);
                });
        movedBoundary.ifPresent(b -> {
            linkedSimplex.boundaries.add(b);
            simplex.boundaries.remove(b);
        });
        linkedSimplex.neighbours.add(simplex);
        linkedSimplex.boundaries.add(median);

        simplex.boundaries.add(median);
        simplex.neighbours.add(linkedSimplex);

        linkedSimplex.nextLayer = next;
        return linkedSimplex;
    }

    static MultiPoint median(MultiPoint a, MultiPoint b) {
        return new MultiPoint(Math.max(a.getPos(), b.getPos()));
    }

    boolean inSimplex(MultiPoint point) {
        Iterator<MultiPoint> iterator = boundaries.iterator();
        int a = iterator.next().getPos();
        int b = iterator.next().getPos();
        return point.getPos() >= min(a, b) && point.getPos() < max(a, b);
    }

    Optional<LinkedSimplex> bestNeighbour(MultiPoint point) {
        return point.getPos() < value.getPos() ?
                neighbours.stream().filter(n -> n.value.getPos() < value.getPos()).min(Comparator.comparingInt(n -> n.value.getPos())) :
                neighbours.stream().filter(n -> n.value.getPos() > value.getPos()).max(Comparator.comparingInt(n -> n.value.getPos()));
    }

    Optional<MultiPoint> bestBoundary(MultiPoint point) {
        return point.getPos() < value.getPos() ? boundaries.stream().min(Comparator.comparingInt(n -> n.getPos())) : boundaries.stream().max(Comparator.comparingInt(n -> n.getPos()));
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

        print(linkedSimplex.search(new MultiPoint(1), 0), new HashSet<>());
        print(linkedSimplex.search(new MultiPoint(1), 1), new HashSet<>());
        print(linkedSimplex.search(new MultiPoint(1), 2), new HashSet<>());
    }

    private static void print(LinkedSimplex linkedSimplex, Set<LinkedSimplex> e) {
        System.out.println(linkedSimplex);
        e.add(linkedSimplex);
        for (LinkedSimplex simplex : linkedSimplex.neighbours) {
            if (!e.contains(simplex)) {
                print(simplex, e);
            }
        }
    }

    private static LinkedSimplex createForLayer(int layer) {
        if (layer == LAYERS) {
            return null;
        }
        LinkedSimplex linkedSimplex = new LinkedSimplex();
        linkedSimplex.value = new MultiPoint(10);
        linkedSimplex.boundaries.add(new MultiPoint(0));
        linkedSimplex.boundaries.add(new MultiPoint(100));
        linkedSimplex.nextLayer = createForLayer(layer + 1);
        return linkedSimplex;
    }

    @Override
    public String toString() {
        return " [ " + boundaries.stream().map(MultiPoint::getPos).min(Integer::compare).orElseThrow() + " " + value.getPos() + " " + boundaries.stream().map(MultiPoint::getPos).max(Integer::compare).orElseThrow() + " ] ";
    }
}
