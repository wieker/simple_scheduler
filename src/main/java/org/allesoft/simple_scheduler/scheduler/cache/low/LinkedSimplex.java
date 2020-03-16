package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class LinkedSimplex {
    static final int DIMENSIONS = 1;
    static final int LAYERS = 3;
    private Collection<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    private LinkedSimplex nextLayer = null;
    private Collection<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
    private MultiPoint value;

    public LinkedSimplex(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPoint value) {
        this.neighbours = neighbours;
        this.nextLayer = nextLayer;
        this.boundaries = boundaries;
        this.value = value;
    }

    LinkedSimplex search(MultiPoint point, int layer) {
        return search(point, layer, (x, y) -> y, x -> x);
    }

    LinkedSimplex search(MultiPoint point, int layer,
                         BiFunction<LinkedSimplex, LinkedSimplex, LinkedSimplex> processor,
                         Function<LinkedSimplex, LinkedSimplex> finalProcesor) {
        check2(this, "before check");
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

    private static LinkedSimplex split(MultiPoint point, LinkedSimplex parentSimplex, LinkedSimplex next) {
        MultiPoint median = median(point, parentSimplex.value);

        System.out.println("insert " + point.getPos() + " into " + parentSimplex.value);
        check2(parentSimplex, "broken parent");
        neiCheck(parentSimplex);

        Collection<LinkedSimplex> newSimplexes = new ArrayList<>(DIMENSIONS + 1);

        List<MultiPoint> border = new ArrayList<>(parentSimplex.boundaries);
        List<MultiPoint> oldBorder = new ArrayList<>(parentSimplex.boundaries);
        List<Optional<LinkedSimplex>> oldNeighs = new ArrayList<>(border.size());
        oldBorder.forEach(vertex -> {
            border.remove(vertex);
            Optional<LinkedSimplex> nearestNeigh = neighbourForThisHyperWall(parentSimplex, border);
            nearestNeigh.ifPresent(n -> n.getNeighbours().remove(parentSimplex));
            oldNeighs.add(nearestNeigh);
            border.add(vertex);
        });
        int i = 0;
        for (MultiPoint vertex : oldBorder) {
            border.remove(vertex);
            Optional<LinkedSimplex> nearestNei = oldNeighs.get(i);
            border.add(median);

            LinkedSimplex temporarySimplex = new LinkedSimplex(null, null, new ArrayList<>(border), null);
            LinkedSimplex newSimplex = temporarySimplex.inSimplex(parentSimplex.getValue()) ? parentSimplex : temporarySimplex;
            newSimplex.setBoundaries(new ArrayList<>(border));
            List<LinkedSimplex> newNeis = new ArrayList<>(DIMENSIONS + 1);
            nearestNei.ifPresent(newNeis::add);
            newSimplex.setNeighbours(newNeis);
            newSimplexes.add(newSimplex);

            nearestNei.ifPresent(n -> n.getNeighbours().add(newSimplex));

            border.remove(median);
            border.add(vertex);

            i++;
        }
        newSimplexes.forEach(s -> {
            ArrayList<LinkedSimplex> collection = new ArrayList<>(newSimplexes);
            collection.remove(s);
            s.getNeighbours().addAll(collection);
            neiCheck(s);
        });
        AtomicReference<LinkedSimplex> withValue = new AtomicReference<>();
        newSimplexes.forEach(s -> {
            if (s.inSimplex(point)) {
                s.setValue(point);
                s.setNextLayer(next);
                withValue.set(s);
            }
        });

        return withValue.get();
    }

    private static void neiCheck(LinkedSimplex simplex) {
        if (simplex.getNeighbours().size() != 2 - (simplex.getBoundaries().stream().map(MultiPoint::getPos).filter(List.of(0, 100)::contains).count())) {
            throw new RuntimeException("neigh broken");
        }
    }

    private static Optional<LinkedSimplex> neighbourForThisHyperWall(LinkedSimplex simplex, List<MultiPoint> border) {
        return simplex.neighbours.stream()
                        .filter(nei -> nei.boundaries.containsAll(border))
                        .findFirst();
    }

    private static void check2(LinkedSimplex simplex, String s2) {
        simplex.getBoundaries().forEach(b -> {
            if (b.getPos() != 0 && b.getPos() != 100 && simplex.neighbours.stream().flatMap(n -> n.getBoundaries().stream()).noneMatch(b::equals)) {
                System.out.println("list");
                print(simplex, new HashSet<>());
                throw new RuntimeException(s2);
            }
        });
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

    public static void main(String[] args) {
        for (int i = 0; i < 100; i ++) {
            LinkedSimplex linkedSimplex = createForLayer(0);

            linkedSimplex.insert(new MultiPoint(20));
            linkedSimplex.insert(new MultiPoint(28));
            linkedSimplex.insert(new MultiPoint(24));
            linkedSimplex.insert(new MultiPoint(9));
            linkedSimplex.insert(new MultiPoint(2));
            linkedSimplex.insert(new MultiPoint(22));
            linkedSimplex.insert(new MultiPoint(48));
            linkedSimplex.insert(new MultiPoint(49));

            print(linkedSimplex.search(new MultiPoint(1), 0), new HashSet<>());
            print(linkedSimplex.search(new MultiPoint(1), 1), new HashSet<>());
            print(linkedSimplex.search(new MultiPoint(1), 2), new HashSet<>());
        }
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
        LinkedSimplex linkedSimplex = new LinkedSimplex(new ArrayList<>(), null, new ArrayList<>(), null);
        linkedSimplex.value = new MultiPoint(10);
        linkedSimplex.boundaries.add(new MultiPoint(0));
        linkedSimplex.boundaries.add(new MultiPoint(100));
        linkedSimplex.nextLayer = createForLayer(layer + 1);
        return linkedSimplex;
    }

    @Override
    public String toString() {
        return " [ " + boundaries.stream().map(MultiPoint::getPos).min(Integer::compare).orElseThrow() + " " + ((value != null) ? value.getPos() : -1) + " " + boundaries.stream().map(MultiPoint::getPos).max(Integer::compare).orElseThrow() + " ] ";
    }

    public Collection<LinkedSimplex> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Collection<LinkedSimplex> neighbours) {
        this.neighbours = neighbours;
    }

    public LinkedSimplex getNextLayer() {
        return nextLayer;
    }

    public void setNextLayer(LinkedSimplex nextLayer) {
        this.nextLayer = nextLayer;
    }

    public Collection<MultiPoint> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(Collection<MultiPoint> boundaries) {
        this.boundaries = boundaries;
    }

    public MultiPoint getValue() {
        return value;
    }

    public void setValue(MultiPoint value) {
        this.value = value;
    }
}
