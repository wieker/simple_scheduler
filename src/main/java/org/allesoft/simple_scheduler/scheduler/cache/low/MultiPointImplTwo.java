package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class MultiPointImplTwo extends MultiPoint<MultiPointImplTwo> {
    final private double x;
    final private double y;

    private MultiPointImplTwo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static MultiPointImplTwo cmp(double x, double y) {
        return new MultiPointImplTwo(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        return x == ((MultiPointImplTwo) obj).x && y == ((MultiPointImplTwo) obj).y;
    }

    @Override
    public String toString() {
        return " { " + x + ", " + y + " } ";
    }

    @Override
    public Optional<LinkedSimplex<MultiPointImplTwo>> bestNeighbour(Set<LinkedSimplex<MultiPointImplTwo>> visited, LinkedSimplex<MultiPointImplTwo> simplexLinkedGraphTwoReal) {
        MultiPointImplTwo point = this;
        if (simplexLinkedGraphTwoReal.getBoundaries().size() < 3) {
            throw new RuntimeException("wrong boundary");
        }
        Iterator<MultiPointImplTwo> iterator = simplexLinkedGraphTwoReal.getBoundaries().iterator();
        MultiPointImplTwo a = (MultiPointImplTwo) iterator.next();
        MultiPointImplTwo b = ((MultiPointImplTwo)iterator.next());
        MultiPointImplTwo c = ((MultiPointImplTwo)iterator.next());
        MultiPointImplTwo value = simplexLinkedGraphTwoReal.getValue();
        if (value == null) {
            value = (MultiPointImplTwo) median(a, b);
            value = (MultiPointImplTwo) median(value, c);
        }
        boolean leftAB = isLeft(a, b, point) * isLeft(a, b, value) <= 0;
        boolean leftBC = isLeft(c, b, point) * isLeft(c, b, value) <= 0;
        boolean leftCA = isLeft(a, c, point) * isLeft(a, c, value) <= 0;
        if (leftAB) {
            Optional<LinkedSimplex<MultiPointImplTwo>> linkedSimplex = simplexLinkedGraphTwoReal.neighbourForThisHyperWall(Arrays.asList(a, b));
            if (!visited.contains(linkedSimplex.orElse(null))) {
                return linkedSimplex;
            }
            System.out.println("cycle");
        }
        if (leftBC) {
            Optional<LinkedSimplex<MultiPointImplTwo>> linkedSimplex = simplexLinkedGraphTwoReal.neighbourForThisHyperWall(Arrays.asList(c, b));
            if (!visited.contains(linkedSimplex.orElse(null))) {
                return linkedSimplex;
            }
            System.out.println("cycle");
        }
        if (leftCA) {
            Optional<LinkedSimplex<MultiPointImplTwo>> linkedSimplex = simplexLinkedGraphTwoReal.neighbourForThisHyperWall(Arrays.asList(a, c));
            if (!visited.contains(linkedSimplex.orElse(null))) {
                return linkedSimplex;
            }
            System.out.println("cycle");
        }
        System.out.println("this");
        return Optional.empty();
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
    public boolean inSimplex(LinkedSimplex<MultiPointImplTwo> simplexLinkedGraphTwoReal) {
        MultiPointImplTwo point = (MultiPointImplTwo) this;
        Iterator<MultiPointImplTwo> iterator = simplexLinkedGraphTwoReal.getBoundaries().iterator();
        MultiPointImplTwo a = (MultiPointImplTwo) iterator.next();
        MultiPointImplTwo b = ((MultiPointImplTwo)iterator.next());
        MultiPointImplTwo c = ((MultiPointImplTwo)iterator.next());
        return new Triangle(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY()).contains(point.getX(), point.getY());
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
    public MultiPointImplTwo median(MultiPointImplTwo aPoint, MultiPointImplTwo bPoint) {
        MultiPointImplTwo a = (MultiPointImplTwo) aPoint;
        MultiPointImplTwo b = (MultiPointImplTwo) bPoint;
        return MultiPointImplTwo.cmp((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }
}
