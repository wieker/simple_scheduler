package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Splitter<T extends MultiPoint<T>> {
    public LinkedSimplex<T> split(T point, LinkedSimplex<T> next, LinkedSimplex<T> simplex) {
        // 1. neighbours
        // 2. border
        // 3. value
        // 4. nei border - read only
        // 5. nei neighs - replace only
        System.out.println("insert " + point + " into " + simplex.getValue());
        System.out.println(this);
        for (LinkedSimplex<T> s : simplex.getNeighbours()) {
            System.out.println(s);
        }

        T median = point.median(point, simplex.getValue());
        System.out.println(median);

        Collection<LinkedSimplex<T>> newSimplexes = new ArrayList<>(simplex.DIMENSIONS + 1);

        AtomicReference<LinkedSimplex<T>> withValue = new AtomicReference<>();
        List<T> border = new ArrayList<>(simplex.getBoundaries());
        List<T> oldBorder = Collections.unmodifiableList(new ArrayList<>(simplex.getBoundaries()));
        Map<LinkedSimplex<T>, Optional<LinkedSimplex<T>>> neighs = new HashMap<>();
        oldBorder.forEach(vertex -> {
            border.remove(vertex);
            border.add(median);
            LinkedSimplex<T> newInstance = simplex.newInstance(Collections.unmodifiableList(border));
            System.out.println("newInstance: " + newInstance);
            if (simplex.getValue().inSimplex(newInstance)) {
                System.out.println("old");
                newInstance.setValue(simplex.getValue());
                newInstance.setNextLayer(simplex.getNextLayer());
            } else if (point.inSimplex(newInstance)) {
                System.out.println("new");
                newInstance.setValue(point);
                newInstance.setNextLayer(next);
                withValue.set(newInstance);
            }
            newSimplexes.add(newInstance);
            border.remove(median);
            neighs.put(newInstance, simplex.neighbourForThisHyperWall(border));
            border.add(vertex);
        });
        newSimplexes.forEach(s -> {
            List<LinkedSimplex<T>> newNighs = new ArrayList<>(newSimplexes);
            newNighs.remove(s);
            neighs.get(s).ifPresent(newNighs::add);
            neighs.get(s).ifPresent(t -> t.replaceNeighbour(simplex, s));
            s.setNeighbours(newNighs);
        });

        size += 2;
        int i = deepSearch(0, new HashSet<>(), withValue.get());
        if (i != size) {
            throw new RuntimeException("deep search fail");
        }
        return withValue.get();
    }

    static int size = 1;

    int deepSearch(int d, Set<LinkedSimplex<T>> passed, LinkedSimplex<T> simplexA) {
        d ++;
        passed.add(simplexA);
        for (LinkedSimplex<T> simplex : simplexA.getNeighbours()) {
            if (!simplex.getNeighbours().contains(this)) {
                throw new RuntimeException("unconnected");
            }
            if (!passed.contains(simplex)) {
                d = deepSearch(d, passed, simplexA);
            }
        }
        return d;
    }
}
