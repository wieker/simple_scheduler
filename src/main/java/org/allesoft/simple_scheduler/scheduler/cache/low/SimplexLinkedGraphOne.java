package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class SimplexLinkedGraphOne extends LinkedSimplex {
    public SimplexLinkedGraphOne(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPointImplOne value) {
        super(neighbours, nextLayer, boundaries, value);
    }

    @Override
    protected boolean inSimplex(MultiPoint gPoint) {
        MultiPointImplOne point = (MultiPointImplOne) gPoint;
        Iterator<MultiPoint> iterator = getBoundaries().iterator();
        int a = ((MultiPointImplOne)iterator.next()).getPos();
        int b = ((MultiPointImplOne)iterator.next()).getPos();
        return point.getPos() >= min(a, b) && point.getPos() < max(a, b);
    }

    @Override
    public MultiPointImplOne getValue() {
        return (MultiPointImplOne) super.getValue();
    }

    @Override
    protected Optional<LinkedSimplex> bestNeighbour(MultiPoint gPoint, Set<LinkedSimplex> visited) {
        MultiPointImplOne point = (MultiPointImplOne) gPoint;
        return point.getPos() < getValue().getPos() ?
                getNeighbours().stream().filter(n -> ((MultiPointImplOne) n.getValue()).getPos() < getValue().getPos()).min(Comparator.comparingInt(n -> ((MultiPointImplOne) n.getValue()).getPos())) :
                getNeighbours().stream().filter(n -> ((MultiPointImplOne) n.getValue()).getPos() > getValue().getPos()).max(Comparator.comparingInt(n -> ((MultiPointImplOne) n.getValue()).getPos()));
    }

    @Override
    protected MultiPoint median(MultiPoint aPoint, MultiPoint bPoint) {
        MultiPointImplOne a = (MultiPointImplOne) aPoint;
        MultiPointImplOne b = (MultiPointImplOne) bPoint;
        return new MultiPointImplOne(Math.max(a.getPos(), b.getPos()));
    }

    @Override
    protected LinkedSimplex newInstance(List<MultiPoint> border) {
        return new SimplexLinkedGraphOne(null, null, new ArrayList<>(border), null);
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).min(Integer::compare).orElseThrow(RuntimeException::new) + " " + getValue() + " " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).max(Integer::compare).orElseThrow(RuntimeException::new) + " ] ";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i ++) {
            LinkedSimplex linkedSimplex = createForLayer(0);

            linkedSimplex.insert(new MultiPointImplOne(20));
            linkedSimplex.insert(new MultiPointImplOne(28));
            linkedSimplex.insert(new MultiPointImplOne(24));
            linkedSimplex.insert(new MultiPointImplOne(9));
            linkedSimplex.insert(new MultiPointImplOne(2));
            linkedSimplex.insert(new MultiPointImplOne(22));
            linkedSimplex.insert(new MultiPointImplOne(48));
            linkedSimplex.insert(new MultiPointImplOne(49));

            print(linkedSimplex.search(new MultiPointImplOne(1), 0), new HashSet<>());
            print(linkedSimplex.search(new MultiPointImplOne(1), 1), new HashSet<>());
            print(linkedSimplex.search(new MultiPointImplOne(1), 2), new HashSet<>());
        }
    }

    private static void print(LinkedSimplex linkedSimplex, Set<LinkedSimplex> e) {
        System.out.println(linkedSimplex);
        e.add(linkedSimplex);
        for (LinkedSimplex simplex : linkedSimplex.getNeighbours()) {
            if (!e.contains(simplex)) {
                print(simplex, e);
            }
        }
    }

    private static LinkedSimplex createForLayer(int layer) {
        if (layer == LAYERS) {
            return null;
        }
        LinkedSimplex linkedSimplex = new SimplexLinkedGraphOne(new ArrayList<>(), null, new ArrayList<>(), null);
        linkedSimplex.setValue(new MultiPointImplOne(10));
        linkedSimplex.setBoundaries(Arrays.asList(new MultiPointImplOne(0), new MultiPointImplOne(100)));
        linkedSimplex.setNextLayer(createForLayer(layer + 1));
        return linkedSimplex;
    }
}
