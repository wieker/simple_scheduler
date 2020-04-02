package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimplexLinkedGraphTwoReal extends LinkedSimplex<MultiPointImplTwo> {
    public SimplexLinkedGraphTwoReal(Collection<LinkedSimplex<MultiPointImplTwo>> neighbours, LinkedSimplex<MultiPointImplTwo> nextLayer, Collection<MultiPointImplTwo> boundaries, MultiPointImplTwo value) {
        super(neighbours, nextLayer, boundaries, value);
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(Object::toString).collect(Collectors.joining()) + " / " + getValue()  + " ] ";
    }

    public static void main(String[] args) {
        LinkedSimplex<MultiPointImplTwo> linkedSimplex = createForLayer(0);

        for (int i = 1; i < 100; i ++) {
            for (int j = 1; j < 100 - i; j ++) {
                linkedSimplex = linkedSimplex.insert(MultiPointImplTwo.cmp(i, j));
            }
        }

        System.out.println("level 0");
        print(linkedSimplex.search(MultiPointImplTwo.cmp(1, 1), 0), new HashSet<>());
        System.out.println("level 1");
        print(linkedSimplex.search(MultiPointImplTwo.cmp(1, 1), 1), new HashSet<>());
        System.out.println("level 2");
        print(linkedSimplex.search(MultiPointImplTwo.cmp(1, 1), 2), new HashSet<>());
    }

    private static void print(LinkedSimplex<MultiPointImplTwo> linkedSimplex, Set<LinkedSimplex<MultiPointImplTwo>> e) {
        System.out.println(linkedSimplex);
        e.add(linkedSimplex);
        for (LinkedSimplex<MultiPointImplTwo> simplex : linkedSimplex.getNeighbours()) {
            if (!e.contains(simplex)) {
                print(simplex, e);
            }
        }
    }

    public static LinkedSimplex<MultiPointImplTwo> createForLayer(int layer) {
        if (layer == LAYERS) {
            return null;
        }
        LinkedSimplex<MultiPointImplTwo> linkedSimplex = new SimplexLinkedGraphTwoReal(new ArrayList<>(), null, new ArrayList<>(), null);
        linkedSimplex.setValue(MultiPointImplTwo.cmp(30, 30));
        linkedSimplex.setBoundaries(Arrays.asList(MultiPointImplTwo.cmp(0, 100), MultiPointImplTwo.cmp(0, 0), MultiPointImplTwo.cmp(100, 0)));
        linkedSimplex.setNextLayer(createForLayer(layer + 1));
        return linkedSimplex;
    }
}
