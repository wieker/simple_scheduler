package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LinkedSimplex<T extends MultiPoint<T>> {
    static final int DIMENSIONS = 2;
    public static final int LAYERS = 1;
    private Collection<LinkedSimplex<T>> neighbours = new ArrayList<>(DIMENSIONS + 1);
    private LinkedSimplex<T> nextLayer = null;
    private Collection<T> boundaries = new ArrayList<>(DIMENSIONS + 1);
    private T value;
    private Splitter<T> splitter;

    public LinkedSimplex(Collection<LinkedSimplex<T>> neighbours, LinkedSimplex<T> nextLayer, Collection<T> boundaries, T value, Splitter<T> splitter) {
        this.neighbours = neighbours != null ? Collections.unmodifiableList(new ArrayList<>(neighbours)) : null;
        this.nextLayer = nextLayer;
        this.boundaries = Collections.unmodifiableCollection(boundaries);
        this.value = value;
        this.splitter = splitter;
    }

    public LinkedSimplex<T> split(T point, LinkedSimplex<T> next, Splitter<T> splitter) {
        return splitter.split(point, next, this);
    }

    LinkedSimplex<T> search(T point, int layer) {
        return search(point, layer, (x, y) -> y, x -> x, new HashSet<LinkedSimplex<T>>());
    }

    LinkedSimplex<T> search(T point, int layer,
                         BiFunction<LinkedSimplex<T>, LinkedSimplex<T>, LinkedSimplex<T>> processor,
                         Function<LinkedSimplex<T>, LinkedSimplex<T>> finalProcesor, Set<LinkedSimplex<T>> visited) {
        System.out.println("enter " + this);
        visited.add(this);
        if (point.inSimplex(this.getBoundaries())) {
            if (layer < LAYERS - 1) {
                try {
                    return processor.apply(this, nextLayer.search(point, layer + 1, processor, finalProcesor, visited));
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return finalProcesor.apply(this);
            }
        } else {
            return point.bestNeighbour(visited, this).map(s -> s.search(point, layer, processor, finalProcesor, visited)).orElse(null);
        }
    }

    LinkedSimplex<T> insert(T point) {
        AtomicBoolean grow = new AtomicBoolean(true);
        System.out.println("insert enter " + point);
        return search(point, 0,
                (currentSimplex, lowerSimplex) -> currentSimplex.splitUpperLevelSimplex(point, lowerSimplex, grow),
                currentSimplex -> currentSimplex.splitBase(point, null, grow), new HashSet<LinkedSimplex<T>>());
    }

    protected static <E extends MultiPoint<E>> LinkedSimplex<E> newInstance(List<E> border, Splitter<E> splitter) {
        return new LinkedSimplex<E>(null, null, new ArrayList<>(border), null, splitter);
    }

    public Collection<LinkedSimplex<T>> getNeighbours() {
        return Collections.unmodifiableCollection(neighbours);
    }

    public void setNeighbours(Collection<LinkedSimplex<T>> neighbours) {
        this.neighbours = Collections.unmodifiableList(new ArrayList<>(neighbours));
        if (neighbours.size() > DIMENSIONS + 1) {
            throw new RuntimeException("too many neighbours");
        }
    }

    public void replaceNeighbour(LinkedSimplex<T> old, LinkedSimplex<T> newSimplex) {
        System.out.println("replace " + old + " => " + newSimplex);
        List<LinkedSimplex<T>> newNeighbours = new ArrayList<>(neighbours);
        int size = newNeighbours.size();
        newNeighbours.remove(old);
        if (size == newNeighbours.size()) {
            throw new RuntimeException("miss neighbours " + this + old + newSimplex);
        }
        newNeighbours.add(newSimplex);
        if (newNeighbours.size() > DIMENSIONS + 1) {
            throw new RuntimeException("too many neighbours");
        }
        setNeighbours(newNeighbours);
    }

    public LinkedSimplex<T> getNextLayer() {
        return nextLayer;
    }

    public void setNextLayer(LinkedSimplex<T> nextLayer) {
        this.nextLayer = nextLayer;
    }

    public Collection<T> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(Collection<T> boundaries) {
        this.boundaries = Collections.unmodifiableCollection(boundaries);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    protected Optional<LinkedSimplex<T>> neighbourForThisHyperWall(List<T> border) {
        Optional<LinkedSimplex<T>> first = this.getNeighbours().stream()
                .filter(nei -> nei.getBoundaries().containsAll(border))
                .findFirst();
        if (!first.isPresent()) {
            for (T point : border) {
                System.out.println(point);
            }
        }
        return first;
    }

    private LinkedSimplex<T> splitUpperLevelSimplex(T point, LinkedSimplex<T> newSimplex, AtomicBoolean grow) {
        if (grow.get() && new Random().nextBoolean()) {
            return split(point, newSimplex, splitter);
        }
        grow.set(false);
        return this;
    }

    private LinkedSimplex<T> splitBase(T point, LinkedSimplex<T> newSimplex, AtomicBoolean grow) {
        if (getValue() == null) {
            setValue(point);
            grow.set(false);
            System.out.println("found empty" + this);
            return this; //or it's better to return this?
        }

        if (getValue().equals(point)) {
            grow.set(false);
            System.out.println("found same");
            return this;
        }
        return split(point, newSimplex, splitter);
    }
}
