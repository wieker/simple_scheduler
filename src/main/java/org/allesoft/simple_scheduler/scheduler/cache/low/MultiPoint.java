package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public abstract class MultiPoint<T extends MultiPoint<T>> {
    public abstract Optional<LinkedSimplex<T>> bestNeighbour(Set<LinkedSimplex<T>> visited, LinkedSimplex<T> simplexLinkedGraphTwoReal);

    public abstract T median(T a, T b);

    public abstract boolean inSimplex(Collection<T> boundaries);
}
