package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Collection;
import java.util.Optional;

public class SimplexLinkedGraphTwo extends LinkedSimplex {
    public SimplexLinkedGraphTwo(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPoint value) {
        super(neighbours, nextLayer, boundaries, value);
    }

    @Override
    boolean inSimplex(MultiPoint point) {
        return super.inSimplex(point);
    }

    @Override
    Optional<LinkedSimplex> bestNeighbour(MultiPoint point) {
        return super.bestNeighbour(point);
    }
}
