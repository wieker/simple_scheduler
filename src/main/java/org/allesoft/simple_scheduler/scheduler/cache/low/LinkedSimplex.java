package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class LinkedSimplex {
    static final int DIMENSIONS = 1;
    static final int LAYERS = 3;
    private Collection<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    private LinkedSimplex nextLayer = null;
    private Collection<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
    private MultiPoint value;

    public LinkedSimplex(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPointImplOne value) {
        this.neighbours = neighbours;
        this.nextLayer = nextLayer;
        this.boundaries = boundaries;
        this.value = value;
    }

    private LinkedSimplex split(MultiPoint point, LinkedSimplex next) {
        MultiPoint median = median(point, value);

        System.out.println("insert " + point + " into " + value);

        Collection<LinkedSimplex> newSimplexes = new ArrayList<>(DIMENSIONS + 1);

        List<MultiPoint> border = new ArrayList<>(boundaries);
        List<MultiPoint> oldBorder = new ArrayList<>(boundaries);
        List<Optional<LinkedSimplex>> oldNeighs = new ArrayList<>(border.size());
        oldBorder.forEach(vertex -> {
            border.remove(vertex);
            Optional<LinkedSimplex> nearestNeigh = neighbourForThisHyperWall(border);
            nearestNeigh.ifPresent(n -> n.getNeighbours().remove(this));
            oldNeighs.add(nearestNeigh);
            border.add(vertex);
        });
        int i = 0;
        for (MultiPoint vertex : oldBorder) {
            border.remove(vertex);
            Optional<LinkedSimplex> nearestNei = oldNeighs.get(i);
            border.add(median);

            LinkedSimplex temporarySimplex = newInstance(border);
            LinkedSimplex newSimplex = temporarySimplex.inSimplex(getValue()) ? this : temporarySimplex;
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

    LinkedSimplex search(MultiPointImplOne point, int layer) {
        return search(point, layer, (x, y) -> y, x -> x);
    }

    LinkedSimplex search(MultiPointImplOne point, int layer,
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

    void insert(MultiPointImplOne point) {
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

    private Optional<LinkedSimplex> neighbourForThisHyperWall(List<MultiPoint> border) {
        return this.neighbours.stream()
                        .filter(nei -> nei.boundaries.containsAll(border))
                        .findFirst();
    }

    private LinkedSimplex splitUpperLevelSimplex(MultiPoint point, LinkedSimplex newSimplex) {
        if (newSimplex != null && new Random().nextBoolean()) {
            return split(point, newSimplex);
        }
        return null;
    }
}
