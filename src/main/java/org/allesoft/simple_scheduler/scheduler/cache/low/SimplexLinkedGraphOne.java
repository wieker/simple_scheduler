package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class SimplexLinkedGraphOne extends LinkedSimplex<MultiPointImplOne> {
    public SimplexLinkedGraphOne(Collection<AtomicReference<LinkedSimplex<MultiPointImplOne>>> neighbours, LinkedSimplex<MultiPointImplOne> nextLayer, Collection<MultiPointImplOne> boundaries, MultiPointImplOne value) {
        super(neighbours, nextLayer, boundaries, value, new Splitter<>());
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).min(Integer::compare).orElseThrow(RuntimeException::new) + " " + getValue() + " " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).max(Integer::compare).orElseThrow(RuntimeException::new) + " ] ";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i ++) {
            LinkedSimplex<MultiPointImplOne> linkedSimplex = createForLayer(0);

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

    private static void print(LinkedSimplex<MultiPointImplOne> linkedSimplex, Set<LinkedSimplex<MultiPointImplOne>> e) {
        System.out.println(linkedSimplex);
        e.add(linkedSimplex);
        for (AtomicReference<LinkedSimplex<MultiPointImplOne>> simplex : linkedSimplex.getNeighbours()) {
            if (!e.contains(simplex)) {
                print(simplex.get(), e);
            }
        }
    }

    private static LinkedSimplex<MultiPointImplOne> createForLayer(int layer) {
        if (layer == LAYERS) {
            return null;
        }
        LinkedSimplex<MultiPointImplOne> linkedSimplex = new SimplexLinkedGraphOne(null, null, new ArrayList<>(), null);
        linkedSimplex.setValue(new MultiPointImplOne(10));
        linkedSimplex.setBoundaries(Arrays.asList(new MultiPointImplOne(0), new MultiPointImplOne(100)));
        linkedSimplex.setNextLayer(createForLayer(layer + 1));
        return linkedSimplex;
    }
}
