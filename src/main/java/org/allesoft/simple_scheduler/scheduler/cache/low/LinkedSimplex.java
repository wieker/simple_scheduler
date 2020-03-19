package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class LinkedSimplex {
    static final int DIMENSIONS = 2;
    static final int LAYERS = 1;
    private Collection<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    private LinkedSimplex nextLayer = null;
    private Collection<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
    private MultiPoint value;

    public LinkedSimplex(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPoint value) {
        this.neighbours = neighbours != null ? Collections.unmodifiableList(new ArrayList<>(neighbours)) : null;
        this.nextLayer = nextLayer;
        this.boundaries = Collections.unmodifiableCollection(boundaries);
        this.value = value;
    }

    public LinkedSimplex split(MultiPoint point, LinkedSimplex next) {
        // 1. neighbours
        // 2. border
        // 3. value
        // 4. nei border - read only
        // 5. nei neighs - replace only
        System.out.println("insert " + point + " into " + value);
        System.out.println(this);
        for (LinkedSimplex s : getNeighbours()) {
            System.out.println(s);
        }

        MultiPoint median = median(point, value);
        System.out.println(median);

        Collection<LinkedSimplex> newSimplexes = new ArrayList<>(DIMENSIONS + 1);

        AtomicReference<LinkedSimplex> withValue = new AtomicReference<>();
        List<MultiPoint> border = new ArrayList<>(boundaries);
        List<MultiPoint> oldBorder = Collections.unmodifiableList(new ArrayList<>(boundaries));
        Map<LinkedSimplex, Optional<LinkedSimplex>> neighs = new HashMap<>();
        oldBorder.forEach(vertex -> {
            border.remove(vertex);
            border.add(median);
            LinkedSimplex newInstance = newInstance(Collections.unmodifiableList(border));
            System.out.println("newInstance: " + newInstance);
            if (newInstance.inSimplex(value)) {
                System.out.println("old");
                newInstance.setValue(value);
                newInstance.setNextLayer(nextLayer);
            } else if (newInstance.inSimplex(point)) {
                System.out.println("new");
                newInstance.setValue(point);
                newInstance.setNextLayer(next);
                withValue.set(newInstance);
            }
            newSimplexes.add(newInstance);
            border.remove(median);
            neighs.put(newInstance, neighbourForThisHyperWall(border));
            border.add(vertex);
        });
        newSimplexes.forEach(s -> {
            List<LinkedSimplex> newNighs = new ArrayList<>(newSimplexes);
            newNighs.remove(s);
            neighs.get(s).ifPresent(newNighs::add);
            neighs.get(s).ifPresent(t -> t.replaceNeighbour(this, s));
            s.setNeighbours(newNighs);
        });

        size += 2;
        int i = withValue.get().deepSearch(0, new HashSet<>());
        if (i != size) {
            throw new RuntimeException("deep search fail");
        }
        return withValue.get();
    }

    static int size = 1;

    int deepSearch(int d, Set<LinkedSimplex> passed) {
        d ++;
        passed.add(this);
        for (LinkedSimplex simplex : getNeighbours()) {
            if (!simplex.getNeighbours().contains(this)) {
                throw new RuntimeException("unconnected");
            }
            if (!passed.contains(simplex)) {
                d = simplex.deepSearch(d, passed);
            }
        }
        return d;
    }

    LinkedSimplex search(MultiPoint point, int layer) {
        return search(point, layer, (x, y) -> y, x -> x, new HashSet<LinkedSimplex>());
    }

    LinkedSimplex search(MultiPoint point, int layer,
                         BiFunction<LinkedSimplex, LinkedSimplex, LinkedSimplex> processor,
                         Function<LinkedSimplex, LinkedSimplex> finalProcesor, Set<LinkedSimplex> visited) {
        System.out.println("enter " + this);
        visited.add(this);
        if (inSimplex(point)) {
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
            return bestNeighbour(point, visited).map(s -> s.search(point, layer, processor, finalProcesor, visited)).orElse(null);
        }
    }

    LinkedSimplex insert(MultiPoint point) {
        AtomicBoolean grow = new AtomicBoolean(true);
        System.out.println("insert enter " + point);
        return search(point, 0,
                (currentSimplex, lowerSimplex) -> currentSimplex.splitUpperLevelSimplex(point, lowerSimplex, grow),
                currentSimplex -> currentSimplex.splitBase(point, null, grow), new HashSet<LinkedSimplex>());
    }

    protected abstract LinkedSimplex newInstance(List<MultiPoint> border);

    protected abstract MultiPoint median(MultiPoint a, MultiPoint b);

    protected abstract boolean inSimplex(MultiPoint point);

    protected abstract Optional<LinkedSimplex> bestNeighbour(MultiPoint point, Set<LinkedSimplex> visited);

    public Collection<LinkedSimplex> getNeighbours() {
        return Collections.unmodifiableCollection(neighbours);
    }

    public void setNeighbours(Collection<LinkedSimplex> neighbours) {
        this.neighbours = Collections.unmodifiableList(new ArrayList<>(neighbours));
        if (neighbours.size() > DIMENSIONS + 1) {
            throw new RuntimeException("too many neighbours");
        }
    }

    public void replaceNeighbour(LinkedSimplex old, LinkedSimplex newSimplex) {
        System.out.println("replace " + old + " => " + newSimplex);
        List<LinkedSimplex> newNeighbours = new ArrayList<>(neighbours);
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
        Optional<LinkedSimplex> first = this.getNeighbours().stream()
                .filter(nei -> nei.getBoundaries().containsAll(border))
                .findFirst();
        if (!first.isPresent()) {
            for (MultiPoint point : border) {
                System.out.println(point);
            }
        }
        return first;
    }

    private LinkedSimplex splitUpperLevelSimplex(MultiPoint point, LinkedSimplex newSimplex, AtomicBoolean grow) {
        if (grow.get() && new Random().nextBoolean()) {
            return split(point, newSimplex);
        }
        grow.set(false);
        return this;
    }

    private LinkedSimplex splitBase(MultiPoint point, LinkedSimplex newSimplex, AtomicBoolean grow) {
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
        return split(point, newSimplex);
    }
}
