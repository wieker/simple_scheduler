package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class SimplexLinkedGraphTwo extends LinkedSimplex {
    public SimplexLinkedGraphTwo(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPointImplOne value) {
        super(neighbours, nextLayer, boundaries, value);
    }

    @Override
    protected boolean inSimplex(MultiPointImplOne point) {
        Iterator<MultiPoint> iterator = getBoundaries().iterator();
        int a = ((MultiPointImplOne)iterator.next()).getPos();
        int b = ((MultiPointImplOne)iterator.next()).getPos();
        return point.getPos() >= min(a, b) && point.getPos() < max(a, b);
    }

    @Override
    protected Optional<LinkedSimplex> bestNeighbour(MultiPointImplOne point) {
        return point.getPos() < getValue().getPos() ?
                getNeighbours().stream().filter(n -> n.getValue().getPos() < getValue().getPos()).min(Comparator.comparingInt(n -> n.getValue().getPos())) :
                getNeighbours().stream().filter(n -> n.getValue().getPos() > getValue().getPos()).max(Comparator.comparingInt(n -> n.getValue().getPos()));
    }

    @Override
    protected MultiPoint median(MultiPointImplOne a, MultiPointImplOne b) {
        return new MultiPointImplOne(Math.max(a.getPos(), b.getPos()));
    }

    @Override
    protected LinkedSimplex newInstance(List<MultiPoint> border) {
        return new SimplexLinkedGraphTwo(null, null, new ArrayList<>(border), null);
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).min(Integer::compare).orElseThrow() + " " + getValue() + " " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).max(Integer::compare).orElseThrow() + " ] ";
    }
}
