package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Collection;
import java.util.Optional;

public abstract class MultiPoint<T extends MultiPoint<T>> {
    public abstract Optional<LinkedSimplex<T>> bestNeighbour(LinkedSimplex<T> simplexLinkedGraphTwoReal);

    public abstract T median(T a, T b, LinkedSimplex<T> simplex);

    public abstract boolean inSimplex(Collection<T> boundaries);
}
