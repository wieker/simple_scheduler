package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex.DIMENSIONS;
import static org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex.newInstance;

public class Splitter<T extends MultiPoint<T>> {
    public LinkedSimplex<T> split(T point, LinkedSimplex<T> next, LinkedSimplex<T> simplex) {
        // 1. neighbours
        // 2. border
        // 3. value
        // 4. nei border - read only
        // 5. nei neighs - replace only
        T median = point.median(point, simplex.getValue(), simplex);

        List<T> oldBorder = oldBorders(simplex);
        List<List<T>> oldNeighBorders = getOldNeighBorders(oldBorder);
        List<Optional<LinkedSimplex<T>>> orderedOldNeighbours = orderedOldNeighbours(simplex, oldNeighBorders);
        List<List<T>> newBorders = newBorders(median, oldNeighBorders);
        List<T> newValues = newValues(point, simplex, newBorders);
        List<LinkedSimplex<T>> newNexts = newNexts(point, next, simplex, newBorders);
        List<LinkedSimplex<T>> newSimplexes = newSimplexes(newBorders, newValues, newNexts);
        List<List<LinkedSimplex<T>>> newNeighbours = newNeighbours(orderedOldNeighbours, newSimplexes);
        setNeighbours(newSimplexes, newNeighbours);
        adjustNeighbours(simplex, orderedOldNeighbours, newSimplexes);

        return newSimplexes.get(0);
    }

    public List<T> oldBorders(LinkedSimplex<T> simplex) {
        return Collections.unmodifiableList(new ArrayList<>(simplex.getBoundaries()));
    }

    public void adjustNeighbours(LinkedSimplex<T> simplex, List<Optional<LinkedSimplex<T>>> orderedOldNeighbours, List<LinkedSimplex<T>> newSimplexes) {
        for (int i = 0; i < orderedOldNeighbours.size(); i ++) {
            if (orderedOldNeighbours.get(i).isPresent()) {
                orderedOldNeighbours.get(i).get().replaceNeighbour(simplex, newSimplexes.get(i));
            }
        }
    }

    public void setNeighbours(List<LinkedSimplex<T>> newSimplexes, List<List<LinkedSimplex<T>>> newNeighbours) {
        for (int i = 0; i < newSimplexes.size(); i ++) {
            newSimplexes.get(i).setNeighbours(newNeighbours.get(i));
        }
    }

    public List<List<LinkedSimplex<T>>> newNeighbours(List<Optional<LinkedSimplex<T>>> orderedOldNeighbours, List<LinkedSimplex<T>> newSimplexes) {
        List<List<LinkedSimplex<T>>> newNeighbours = new ArrayList<>(DIMENSIONS + 1);
        for (int i = 0; i < newSimplexes.size(); i ++) {
            ArrayList<LinkedSimplex<T>> newList = new ArrayList<>(newSimplexes);
            orderedOldNeighbours.get(i).ifPresent(newList::add);
            newList.remove(newSimplexes.get(i));
            newNeighbours.add(newList);
        }
        return newNeighbours;
    }

    public List<LinkedSimplex<T>> newSimplexes(List<List<T>> newBorders, List<T> newValues, List<LinkedSimplex<T>> newNexts) {
        List<LinkedSimplex<T>> newSimplexes = new ArrayList<>(DIMENSIONS + 1);
        for (int i = 0; i < newBorders.size(); i ++) {
            LinkedSimplex<T> newInstance = newInstance(newBorders.get(i), this);
            newInstance.setValue(newValues.get(i));
            newInstance.setNextLayer(newNexts.get(i));
            newSimplexes.add(newInstance);
        }
        return newSimplexes;
    }

    public List<LinkedSimplex<T>> newNexts(T point, LinkedSimplex<T> next, LinkedSimplex<T> simplex, List<List<T>> newBorders) {
        AtomicReference<LinkedSimplex<T>> newValue = new AtomicReference<>(simplex.getNextLayer());
        AtomicReference<LinkedSimplex<T>> pointValue = new AtomicReference<>(next);
        return newBorders.stream().map(newBoundaries -> {
                return simplex.getValue().inSimplex(newBoundaries) ? newValue.getAndSet(null) :
                        point.inSimplex(newBoundaries) ? pointValue.getAndSet(null) : null;
            }).collect(Collectors.toList());
    }

    public List<T> newValues(T point, LinkedSimplex<T> simplex, List<List<T>> newBorders) {
        AtomicReference<T> newValue = new AtomicReference<>(simplex.getValue());
        AtomicReference<T> pointValue = new AtomicReference<>(point);
        return newBorders.stream().map(newBoundaries -> {
                return simplex.getValue().inSimplex(newBoundaries) ? newValue.getAndSet(null) :
                        point.inSimplex(newBoundaries) ? pointValue.getAndSet(null) : null;
            }).collect(Collectors.toList());
    }

    public List<List<T>> newBorders(T median, List<List<T>> oldNeighBorders) {
        return oldNeighBorders.stream().map(list -> {
                ArrayList<T> newList = new ArrayList<>(list);
                newList.add(median);
                return newList;
            }).collect(Collectors.toList());
    }

    public List<Optional<LinkedSimplex<T>>> orderedOldNeighbours(LinkedSimplex<T> simplex, List<List<T>> oldNeighBorders) {
        return oldNeighBorders.stream().map(simplex::neighbourForThisHyperWall).collect(Collectors.toList());
    }

    public List<List<T>> getOldNeighBorders(List<T> oldBorder) {
        List<List<T>> neighBorders = new ArrayList<>();
        oldBorder.forEach(vertex -> {
            List<T> neighBorder = new ArrayList<>(oldBorder);
            neighBorder.remove(vertex);
            neighBorders.add(neighBorder);
        });
        return neighBorders;
    }
}
