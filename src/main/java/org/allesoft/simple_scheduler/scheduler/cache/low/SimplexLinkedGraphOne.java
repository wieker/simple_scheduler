package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class SimplexLinkedGraphOne extends LinkedSimplex<MultiPointImplOne> {
    public SimplexLinkedGraphOne(Collection<AtomicReference<LinkedSimplex<MultiPointImplOne>>> neighbours, AtomicReference<LinkedSimplex<MultiPointImplOne>> nextLayer, Collection<MultiPointImplOne> boundaries, MultiPointImplOne value) {
        super(neighbours, nextLayer, boundaries, value, new Splitter<>());
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).min(Double::compare).orElseThrow(RuntimeException::new) + " " + getValue() + " " + getBoundaries().stream().map(point -> ((MultiPointImplOne) point).getPos()).max(Double::compare).orElseThrow(RuntimeException::new) + " ] ";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i ++) {
            AtomicReference<LinkedSimplex<MultiPointImplOne>> self = createForLayer(0).getSelf();

            self.get().insert(new MultiPointImplOne(20));
            self.get().insert(new MultiPointImplOne(28));
            self.get().insert(new MultiPointImplOne(24));
            self.get().insert(new MultiPointImplOne(9));
            self.get().insert(new MultiPointImplOne(2));
            self.get().insert(new MultiPointImplOne(22));
            self.get().insert(new MultiPointImplOne(48));
            self.get().insert(new MultiPointImplOne(49));

            print(self.get().search(new MultiPointImplOne(1), 0).get(), new HashSet<>());
            print(self.get().search(new MultiPointImplOne(1), 1).get(), new HashSet<>());
            print(self.get().search(new MultiPointImplOne(1), 2).get(), new HashSet<>());
        }
    }

    private static void print(LinkedSimplex<MultiPointImplOne> linkedSimplex, Set<LinkedSimplex<MultiPointImplOne>> e) {
        System.out.println(linkedSimplex);
        e.add(linkedSimplex);
        for (AtomicReference<LinkedSimplex<MultiPointImplOne>> simplex : linkedSimplex.getNeighbours()) {
            if (simplex != null && !e.contains(simplex.get())) {
                print(simplex.get(), e);
            }
        }
    }

    private static LinkedSimplex<MultiPointImplOne> createForLayer(int layer) {
        if (layer == LAYERS) {
            return null;
        }
        LinkedSimplex<MultiPointImplOne> linkedSimplex = new SimplexLinkedGraphOne(null, new AtomicReference<>(), new ArrayList<>(), null);
        linkedSimplex.setValue(new MultiPointImplOne(10));
        linkedSimplex.setBoundaries(Arrays.asList(new MultiPointImplOne(0), new MultiPointImplOne(100)));
        linkedSimplex.getNextLayer().set(createForLayer(layer + 1));
        return linkedSimplex;
    }
}
