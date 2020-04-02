package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

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
    public boolean inSimplex(LinkedSimplex<MultiPointImplOne> simplexLinkedGraphOne) {
        MultiPointImplOne point = (MultiPointImplOne) this;
        Iterator<MultiPointImplOne> iterator = simplexLinkedGraphOne.getBoundaries().iterator();
        int a = ((MultiPointImplOne)iterator.next()).getPos();
        int b = ((MultiPointImplOne)iterator.next()).getPos();
        return point.getPos() >= min(a, b) && point.getPos() < max(a, b);
    }

    @Override
    public Optional<LinkedSimplex<MultiPointImplOne>> bestNeighbour(Set<LinkedSimplex<MultiPointImplOne>> visited, LinkedSimplex<MultiPointImplOne> simplexLinkedGraphOne) {
        MultiPointImplOne point = (MultiPointImplOne) this;
        return point.getPos() < simplexLinkedGraphOne.getValue().getPos() ?
                simplexLinkedGraphOne.getNeighbours().stream().filter(n -> ((MultiPointImplOne) n.getValue()).getPos() < simplexLinkedGraphOne.getValue().getPos()).min(Comparator.comparingInt(n -> ((MultiPointImplOne) n.getValue()).getPos())) :
                simplexLinkedGraphOne.getNeighbours().stream().filter(n -> ((MultiPointImplOne) n.getValue()).getPos() > simplexLinkedGraphOne.getValue().getPos()).max(Comparator.comparingInt(n -> ((MultiPointImplOne) n.getValue()).getPos()));
    }

    @Override
    public MultiPointImplOne median(MultiPointImplOne aPoint, MultiPointImplOne bPoint) {
        MultiPointImplOne a = (MultiPointImplOne) aPoint;
        MultiPointImplOne b = (MultiPointImplOne) bPoint;
        return new MultiPointImplOne(Math.max(a.getPos(), b.getPos()));
    }
}
