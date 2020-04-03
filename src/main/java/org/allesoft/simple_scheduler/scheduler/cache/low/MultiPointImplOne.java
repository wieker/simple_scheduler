package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class MultiPointImplOne extends MultiPoint<MultiPointImplOne> {
    final private int pos;

    public MultiPointImplOne(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object obj) {
        return pos == ((MultiPointImplOne) obj).pos;
    }

    @Override
    public String toString() {
        return " { " + pos + " } ";
    }

    @Override
    public boolean inSimplex(Collection<MultiPointImplOne> boundaries) {
        MultiPointImplOne point = (MultiPointImplOne) this;
        Iterator<MultiPointImplOne> iterator = boundaries.iterator();
        int a = ((MultiPointImplOne)iterator.next()).getPos();
        int b = ((MultiPointImplOne)iterator.next()).getPos();
        return point.getPos() >= min(a, b) && point.getPos() < max(a, b);
    }

    @Override
    public Optional<AtomicReference<LinkedSimplex<MultiPointImplOne>>> bestNeighbour(LinkedSimplex<MultiPointImplOne> simplexLinkedGraphOne) {
        MultiPointImplOne point = (MultiPointImplOne) this;
        return point.getPos() < simplexLinkedGraphOne.getValue().getPos() ?
                simplexLinkedGraphOne.getNeighbours().stream().filter(n -> ((MultiPointImplOne) n.get().getValue()).getPos() < simplexLinkedGraphOne.getValue().getPos()).min(Comparator.comparingInt(n -> ((MultiPointImplOne) n.get().getValue()).getPos())) :
                simplexLinkedGraphOne.getNeighbours().stream().filter(n -> ((MultiPointImplOne) n.get().getValue()).getPos() > simplexLinkedGraphOne.getValue().getPos()).max(Comparator.comparingInt(n -> ((MultiPointImplOne) n.get().getValue()).getPos()));
    }

    @Override
    public MultiPointImplOne median(MultiPointImplOne aPoint, MultiPointImplOne bPoint, LinkedSimplex<MultiPointImplOne> simplex, Splitter<MultiPointImplOne> splitter) {
        MultiPointImplOne a = (MultiPointImplOne) aPoint;
        MultiPointImplOne b = (MultiPointImplOne) bPoint;
        return new MultiPointImplOne(Math.max(a.getPos(), b.getPos()));
    }

    @Override
    public MultiPointImplOne getSimplexMedian(LinkedSimplex<MultiPointImplOne> simplex) {
        Iterator<MultiPointImplOne> iterator = simplex.getBoundaries().iterator();
        MultiPointImplOne a = iterator.next();
        MultiPointImplOne b = iterator.next();
        MultiPointImplOne value = median(a, b, null, null);
        return value;
    }
}
