package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SimplexLinkedGraphTwoReal extends LinkedSimplex {
    public SimplexLinkedGraphTwoReal(Collection<LinkedSimplex> neighbours, LinkedSimplex nextLayer, Collection<MultiPoint> boundaries, MultiPointImplTwo value) {
        super(neighbours, nextLayer, boundaries, value);
    }

    class Triangle {
        Triangle(double x1, double y1, double x2, double y2, double x3,
                 double y3) {
            this.x3 = x3;
            this.y3 = y3;
            y23 = y2 - y3;
            x32 = x3 - x2;
            y31 = y3 - y1;
            x13 = x1 - x3;
            det = y23 * x13 - x32 * y31;
            minD = Math.min(det, 0);
            maxD = Math.max(det, 0);
        }

        boolean contains(double x, double y) {
            double dx = x - x3;
            double dy = y - y3;
            double a = y23 * dx + x32 * dy;
            if (a < minD || a > maxD)
                return false;
            double b = y31 * dx + x13 * dy;
            if (b < minD || b > maxD)
                return false;
            double c = det - a - b;
            if (c < minD || c > maxD)
                return false;
            return true;
        }

        private final double x3, y3;
        private final double y23, x32, y31, x13;
        private final double det, minD, maxD;
    }

    @Override
    protected boolean inSimplex(MultiPoint gPoint) {
        MultiPointImplTwo point = (MultiPointImplTwo) gPoint;
        Iterator<MultiPoint> iterator = getBoundaries().iterator();
        MultiPointImplTwo a = (MultiPointImplTwo) iterator.next();
        MultiPointImplTwo b = ((MultiPointImplTwo)iterator.next());
        MultiPointImplTwo c = ((MultiPointImplTwo)iterator.next());
        return new Triangle(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY()).contains(point.getX(), point.getY());
    }

    @Override
    public MultiPointImplTwo getValue() {
        return (MultiPointImplTwo) super.getValue();
    }

    public double isLeft(MultiPointImplTwo a, MultiPointImplTwo b, MultiPointImplTwo c){
        try {
            return ((b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()));
        } catch (NullPointerException e) {
            System.out.println(c);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Optional<LinkedSimplex> bestNeighbour(MultiPoint gPoint) {
        MultiPointImplTwo point = (MultiPointImplTwo) gPoint;
        if (getBoundaries().size() < 3) {
            throw new RuntimeException("wrong boundary");
        }
        Iterator<MultiPoint> iterator = getBoundaries().iterator();
        MultiPointImplTwo a = (MultiPointImplTwo) iterator.next();
        MultiPointImplTwo b = ((MultiPointImplTwo)iterator.next());
        MultiPointImplTwo c = ((MultiPointImplTwo)iterator.next());
        MultiPointImplTwo value = getValue();
        if (value == null) {
            value = (MultiPointImplTwo) median(a, b);
            value = (MultiPointImplTwo) median(value, c);
        }
        boolean leftAB = isLeft(a, b, point) * isLeft(a, b, value) < 0;
        boolean leftBC = isLeft(c, b, point) * isLeft(c, b, value) < 0;
        boolean leftCA = isLeft(a, c, point) * isLeft(a, c, value) < 0;
        if (leftAB) {
            return neighbourForThisHyperWall(List.of(a, b));
        }
        if (leftBC) {
            return neighbourForThisHyperWall(List.of(c, b));
        }
        if (leftCA) {
            return neighbourForThisHyperWall(List.of(a, c));
        }
        return Optional.empty();
    }

    @Override
    protected MultiPoint median(MultiPoint aPoint, MultiPoint bPoint) {
        MultiPointImplTwo a = (MultiPointImplTwo) aPoint;
        MultiPointImplTwo b = (MultiPointImplTwo) bPoint;
        return MultiPointImplTwo.cmp((a.getX() + b.getX()) / 2, (a.getY() + b.getX()) / 2);
    }

    @Override
    protected LinkedSimplex newInstance(List<MultiPoint> border) {
        return new SimplexLinkedGraphTwoReal(null, null, new ArrayList<>(border), null);
    }

    @Override
    public String toString() {
        return " [ " + getBoundaries().stream().map(Object::toString).collect(Collectors.joining()) + " / " + getValue()  + " ] ";
    }

    public static void main(String[] args) {
        LinkedSimplex linkedSimplex = createForLayer(0);

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
        LinkedSimplex linkedSimplex = new SimplexLinkedGraphTwoReal(new ArrayList<>(), null, new ArrayList<>(), null);
        linkedSimplex.setValue(MultiPointImplTwo.cmp(30, 30));
        linkedSimplex.setBoundaries(List.of(MultiPointImplTwo.cmp(0, 100), MultiPointImplTwo.cmp(0, 0), MultiPointImplTwo.cmp(100, 0)));
        linkedSimplex.setNextLayer(createForLayer(layer + 1));
        return linkedSimplex;
    }
}
