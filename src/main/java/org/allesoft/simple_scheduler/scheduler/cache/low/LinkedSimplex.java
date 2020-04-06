package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LinkedSimplex<T extends MultiPoint<T>> {
    static final int DIMENSIONS = 2;
    public static final int LAYERS = 3;
    private List<AtomicReference<LinkedSimplex<T>>> neighbours;
    private AtomicReference<LinkedSimplex<T>>  nextLayer = new AtomicReference<>();
    private Collection<T> boundaries;
    private volatile T value;
    private Splitter<T> splitter;
    private AtomicInteger lock = new AtomicInteger(0);
    private AtomicReference<LinkedSimplex<T>> self = new AtomicReference<>();
    private List<AtomicReference<LinkedSimplex<T>>> fromNeighbours = new ArrayList<>(DIMENSIONS + 1);

    public LinkedSimplex(Collection<AtomicReference<LinkedSimplex<T>>> neighbours, AtomicReference<LinkedSimplex<T>>  nextLayer, Collection<T> boundaries, T value, Splitter<T> splitter) {
        this.neighbours = new ArrayList<>();
        this.nextLayer = nextLayer;
        this.boundaries = Collections.unmodifiableCollection(boundaries);
        this.value = value;
        this.splitter = splitter;
        self.set(this);
    }

    public AtomicReference<LinkedSimplex<T>> split(T point, AtomicReference<LinkedSimplex<T>>  next, Splitter<T> splitter) {
        return splitter.split(point, next, this);
    }

    AtomicReference<LinkedSimplex<T>>  search(T point, int layer) {
        return search(point, layer, (x, y) -> y, x -> x);
    }

    interface TriFunction<A, B, C, D> {
        D apply(A a, B b, C c);
    }

    AtomicReference<LinkedSimplex<T>>  search(T point, int layer,
                            BiFunction<LinkedSimplex<T>, AtomicReference<LinkedSimplex<T>> , AtomicReference<LinkedSimplex<T>> > processor,
                            Function<AtomicReference<LinkedSimplex<T>> , AtomicReference<LinkedSimplex<T>> > finalProcesor) {
        System.out.println("search: " + point + " in " + this.toString());
        System.out.println("level: " + layer);
        if (point.inSimplex(this.getBoundaries())) {
            if (layer < LAYERS - 1) {
                try {
                    return processor.apply(this, nextLayer.get().search(point, layer + 1, processor, finalProcesor));
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return finalProcesor.apply(this.getSelf());
            }
        } else {
            return point.bestNeighbour(this).map(s -> s.get().search(point, layer, processor, finalProcesor)).orElse(null);
        }
    }

    AtomicReference<LinkedSimplex<T>>  insert(T point) {
        AtomicBoolean grow = new AtomicBoolean(true);
        return search(point, 0,
                (currentSimplex, lowerSimplex) -> currentSimplex.splitUpperLevelSimplex(point, lowerSimplex, grow),
                currentSimplex -> currentSimplex.get().splitBase(point, null, grow));
    }

    protected static <E extends MultiPoint<E>> LinkedSimplex<E> newInstance(List<E> border, Splitter<E> splitter) {
        System.out.println("new simplex: " + border);
        return new LinkedSimplex<E>(null, new AtomicReference<>(), new ArrayList<>(border), null, splitter);
    }

    public List<AtomicReference<LinkedSimplex<T>>> getNeighbours() {
        return neighbours;
    }

    public AtomicReference<LinkedSimplex<T>>  getNextLayer() {
        return nextLayer;
    }

    public void setNextLayer(AtomicReference<LinkedSimplex<T>>  nextLayer) {
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

    protected Optional<AtomicReference<LinkedSimplex<T>>> neighbourForThisHyperWall(List<T> border) {
        return this.getNeighbours().stream()
                .filter(nei -> nei != null && nei.get() != null)
                .filter(nei -> nei.get().getBoundaries().containsAll(border))
                .findFirst();
    }

    private AtomicReference<LinkedSimplex<T>>  splitUpperLevelSimplex(T point, AtomicReference<LinkedSimplex<T>>  newSimplex, AtomicBoolean grow) {
        if (grow.get() && new Random().nextBoolean()) {
            System.out.println("split upper");
            return split(point, newSimplex, splitter);
        }
        System.out.println("change upper");
        this.setNextLayer(newSimplex.get().search(point.getSimplexMedian(this), LAYERS));
        grow.set(false);
        return this.getSelf();
    }

    private AtomicReference<LinkedSimplex<T>>  splitBase(T point, AtomicReference<LinkedSimplex<T>>  newSimplex, AtomicBoolean grow) {
        if (getValue() == null) {
            setValue(point);
            grow.set(false);
            System.out.println("found empty" + this);
            return this.getSelf(); //or it's better to return this?
        }

        if (getValue().equals(point)) {
            grow.set(false);
            System.out.println("found same");
            return this.getSelf();
        }
        return split(point, newSimplex, splitter);
    }

    public List<AtomicReference<LinkedSimplex<T>>> getFromNeighbours() {
        return fromNeighbours;
    }

    public AtomicInteger getLock() {
        return lock;
    }

    public AtomicReference<LinkedSimplex<T>> getSelf() {
        return self;
    }

    public void setSelf(AtomicReference<LinkedSimplex<T>> self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(Object::toString).collect(Collectors.joining()) + " / " + getValue()  + " ] ";
    }
}
