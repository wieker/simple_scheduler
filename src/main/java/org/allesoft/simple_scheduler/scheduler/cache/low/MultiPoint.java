package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MultiPoint<T extends MultiPoint<T>> {
    public abstract Optional<AtomicReference<LinkedSimplex<T>>> bestNeighbour(LinkedSimplex<T> simplexLinkedGraphTwoReal);

    public abstract T median(T a, T b, LinkedSimplex<T> simplex, Splitter<T> splitter);

    public abstract boolean inSimplex(Collection<T> boundaries);
}
