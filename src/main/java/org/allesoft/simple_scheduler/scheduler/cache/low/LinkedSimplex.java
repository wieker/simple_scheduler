package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class LinkedSimplex {
    static final int DIMENSIONS = 2;
    static final int LAYERS = 3;
    private Collection<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    private LinkedSimplex nextLayer = null;
    private Collection<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
    private MultiPoint value;

    public LinkedSimplex(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPoint value) {
        this.neighbours = neighbours;
        this.nextLayer = nextLayer;
        this.boundaries = Collections.unmodifiableCollection(boundaries);
        this.value = value;
    }

    private LinkedSimplex split(MultiPoint point, LinkedSimplex next) {

        System.out.println("insert " + point + " into " + value);
        if (getValue() == null) {
            setValue(point);
            return null; //or it's better to return this?
        }

        if (getValue().equals(point)) {
            return null;
        }

        MultiPoint median = median(point, value);

        Collection<LinkedSimplex> newSimplexes = new ArrayList<>(DIMENSIONS + 1);

        List<MultiPoint> border = new ArrayList<>(boundaries);
        List<MultiPoint> oldBorder = new ArrayList<>(boundaries);
        List<Optional<LinkedSimplex>> oldNeighs = new ArrayList<>(border.size());
        oldBorder.forEach(vertex -> {
            border.remove(vertex);
            Optional<LinkedSimplex> nearestNeigh = neighbourForThisHyperWall(border);
            System.out.println(border.stream().map(Objects::toString).collect(Collectors.joining()));
            System.out.println(nearestNeigh.orElse(null));
            oldNeighs.add(nearestNeigh);
            border.add(vertex);
        });
        int i = 0;
        Map<LinkedSimplex, LinkedSimplex> mapping = new HashMap<>();
        boolean used = false;
        for (MultiPoint vertex : oldBorder) {
            border.remove(vertex);
            Optional<LinkedSimplex> nearestNei = oldNeighs.get(i);
            border.add(median);
            System.out.println(border.stream().map(Objects::toString).collect(Collectors.joining()));
            System.out.println(nearestNei.orElse(null));

            LinkedSimplex temporarySimplex = newInstance(border);
            LinkedSimplex newSimplex = temporarySimplex.inSimplex(getValue()) && !used ? this : temporarySimplex;
            used = true;
            newSimplex.setBoundaries(new ArrayList<>(border));
            nearestNei.ifPresent(nei -> mapping.put(newSimplex, nei));
            newSimplexes.add(newSimplex);

            border.remove(median);
            border.add(vertex);

            i++;
        }
        mapping.forEach((linkedSimplex, linkedSimplex2) -> System.out.println(linkedSimplex.toString() + " => " + linkedSimplex2));
        newSimplexes.forEach(s -> {
            ArrayList<LinkedSimplex> collection = new ArrayList<>(newSimplexes);
            collection.remove(s);
            LinkedSimplex oldNei = mapping.get(s);
            if (oldNei != null) {
                collection.add(oldNei);
                oldNei.replaceNeighbour(this, s);
            }
            s.setNeighbours(collection);
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
            return bestNeighbour(point).map(s -> s.search(point, layer, processor, finalProcesor))
                    .orElseThrow(() -> {
                        System.out.println(getNeighbours().stream().map(Objects::toString).collect(Collectors.joining()));
                        return new RuntimeException("now neighbour for " + this + " and " + point);
                    });
        }
    }

    void insert(MultiPoint point) {
        search(point, 0,
                (currentSimplex, lowerSimplex) -> currentSimplex.splitUpperLevelSimplex(point, lowerSimplex),
                currentSimplex -> currentSimplex.split(point, null));
    }

    protected abstract LinkedSimplex newInstance(List<MultiPoint> border);

    protected abstract MultiPoint median(MultiPoint a, MultiPoint b);

    protected abstract boolean inSimplex(MultiPoint point);

    protected abstract Optional<LinkedSimplex> bestNeighbour(MultiPoint point);

    public Collection<LinkedSimplex> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Collection<LinkedSimplex> neighbours) {
        this.neighbours = neighbours != null ? Collections.unmodifiableCollection(neighbours) : null;
    }

    public void replaceNeighbour(LinkedSimplex old, LinkedSimplex newSimplex) {
        neighbours.remove(old);
        neighbours.add(newSimplex);
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
        this.boundaries = Collections.unmodifiableCollection(boundaries);
    }

    public MultiPoint getValue() {
        return value;
    }

    public void setValue(MultiPoint value) {
        this.value = value;
    }

    protected Optional<LinkedSimplex> neighbourForThisHyperWall(List<MultiPoint> border) {
        return this.getNeighbours().stream()
                        .filter(nei -> nei.getBoundaries().containsAll(border))
                        .findFirst();
    }

    private LinkedSimplex splitUpperLevelSimplex(MultiPoint point, LinkedSimplex newSimplex) {
        if (newSimplex != null && new Random().nextBoolean()) {
            return split(point, newSimplex);
        }
        return null;
    }
}
