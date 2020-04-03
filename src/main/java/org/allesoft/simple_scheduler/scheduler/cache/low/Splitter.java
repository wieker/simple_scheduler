package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex.DIMENSIONS;
import static org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex.LAYERS;
import static org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex.newInstance;

public class Splitter<T extends MultiPoint<T>> {
    public LinkedSimplex<T> split(T point, LinkedSimplex<T> next, LinkedSimplex<T> simplex) {
        // 1. neighbours
        // 2. border
        // 3. value
        // 4. nei border - read only
        // 5. nei neighs - replace only
        T simplexValue = simplex.getValue();
        if (simplexValue == null) {
            simplexValue = point.getSimplexMedian(simplex);
        }
        T median = point.median(point, simplexValue, simplex, this);

        List<T> oldBorder = oldBorders(simplex);
        List<List<T>> oldNeighBorders = getOldNeighBorders(oldBorder);
        List<Optional<AtomicReference<LinkedSimplex<T>>>> orderedOldNeighbours = orderedOldNeighbours(simplex, oldNeighBorders);
        ArrayList<AtomicReference<LinkedSimplex<T>>> fromLinks = new ArrayList<>();
        for (int j = 0; j < orderedOldNeighbours.size(); j ++) {
            AtomicReference<LinkedSimplex<T>> element = null;
            Optional<AtomicReference<LinkedSimplex<T>>> linkedSimplexAtomicReference = orderedOldNeighbours.get(j);
            if (!linkedSimplexAtomicReference.isPresent()) {
                fromLinks.add(null);
            } else {
                for (int i = 0; i < simplex.getNeighbours().size(); i++) {
                    if (linkedSimplexAtomicReference.get() == simplex.getNeighbours().get(i)) {
                        element = simplex.getFromNeighbours().get(i);
                    }
                }
                fromLinks.add(element);
            }
        }
        List<List<T>> newBorders = newBorders(median, oldNeighBorders);
        List<T> newValues = newValues(point, simplex, newBorders);
        List<LinkedSimplex<T>> newSimplexes = newSimplexes(newBorders, newValues);
        newNexts(newSimplexes, next, point);

        for (int i = 0; i < newSimplexes.size(); i ++) {
            for (int j = i + 1; j < newSimplexes.size(); j ++) {
                AtomicReference<LinkedSimplex<T>> refToI = new AtomicReference<>(newSimplexes.get(i));
                AtomicReference<LinkedSimplex<T>> refToJ = new AtomicReference<>(newSimplexes.get(j));
                newSimplexes.get(i).getNeighbours().add(refToJ);
                newSimplexes.get(i).getFromNeighbours().add(refToI);
                newSimplexes.get(j).getNeighbours().add(refToI);
                newSimplexes.get(j).getFromNeighbours().add(refToJ);
            }
            newSimplexes.get(i).getNeighbours().add(orderedOldNeighbours.get(i).orElse(null));
            newSimplexes.get(i).getFromNeighbours().add(fromLinks.get(i));
        }

        for (int i = 0; i < newSimplexes.size(); i ++) {
            newSimplexes.get(i).getLock().set(1);
        }
        if (simplex.getLock().getAndSet(1) == 1) {
            throw new RuntimeException("retry");
        }

        for (int i = 0; i < fromLinks.size(); i ++) {
            if (fromLinks.get(i) != null) {
                fromLinks.get(i).set(newSimplexes.get(i));
            }
        }

        for (int i = 0; i < newSimplexes.size(); i ++) {
            newSimplexes.get(i).getLock().set(0);
        }

        return newSimplexes.get(0);
    }

    public List<List<T>> tryBorders(T median, LinkedSimplex<T> simplex) {
        List<T> oldBorder = oldBorders(simplex);
        List<List<T>> oldNeighBorders = getOldNeighBorders(oldBorder);
        return newBorders(median, oldNeighBorders);
    }

    public List<T> oldBorders(LinkedSimplex<T> simplex) {
        return Collections.unmodifiableList(new ArrayList<>(simplex.getBoundaries()));
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

    public List<LinkedSimplex<T>> newSimplexes(List<List<T>> newBorders, List<T> newValues) {
        List<LinkedSimplex<T>> newSimplexes = new ArrayList<>(DIMENSIONS + 1);
        for (int i = 0; i < newBorders.size(); i ++) {
            LinkedSimplex<T> newInstance = newInstance(newBorders.get(i), this);
            newInstance.setValue(newValues.get(i));
            newInstance.setNextLayer(null);
            newSimplexes.add(newInstance);
        }
        return newSimplexes;
    }

    public void newNexts(List<LinkedSimplex<T>> newSimplexes, LinkedSimplex<T> next, T point) {
        if (next != null) {
            newSimplexes.forEach(simplex -> {
                simplex.setNextLayer(next.search(point.getSimplexMedian(simplex), LAYERS - 1));
            });
        }
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

    public List<Optional<AtomicReference<LinkedSimplex<T>>>> orderedOldNeighbours(LinkedSimplex<T> simplex, List<List<T>> oldNeighBorders) {
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
