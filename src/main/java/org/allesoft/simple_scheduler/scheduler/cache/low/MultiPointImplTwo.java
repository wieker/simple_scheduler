package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

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
    public Optional<AtomicReference<LinkedSimplex<MultiPointImplTwo>>> bestNeighbour(LinkedSimplex<MultiPointImplTwo> simplex) {
        MultiPointImplTwo point = this;
        if (simplex.getBoundaries().size() < 3) {
            throw new RuntimeException("wrong boundary");
        }
        Iterator<MultiPointImplTwo> iterator = simplex.getBoundaries().iterator();
        MultiPointImplTwo a = iterator.next();
        MultiPointImplTwo b = iterator.next();
        MultiPointImplTwo c = iterator.next();
        MultiPointImplTwo value = simplex.getValue();
        if (value == null) {
            value = stdMedian(a, b);
            value = stdMedian(value, c);
        }
        boolean leftAB = isLeft(a, b, point) * isLeft(a, b, value) <= 0;
        boolean leftBC = isLeft(c, b, point) * isLeft(c, b, value) <= 0;
        boolean leftCA = isLeft(a, c, point) * isLeft(a, c, value) <= 0;
        if (leftAB) {
            return simplex.neighbourForThisHyperWall(Arrays.asList(a, b));
        }
        if (leftBC) {
            return simplex.neighbourForThisHyperWall(Arrays.asList(c, b));
        }
        if (leftCA) {
            return simplex.neighbourForThisHyperWall(Arrays.asList(a, c));
        }
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

    public boolean collinear(MultiPointImplTwo a, MultiPointImplTwo b, MultiPointImplTwo c) {
        return collinear(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());
    }

    public boolean collinear(double x1, double y1, double x2,
                             double y2, double x3, double y3)
    {
        // Calculation the area of
        // triangle. We have skipped
        // multiplication with 0.5
        // to avoid floating point
        // computations
        double a = x1 * (y2 - y3) +
                x2 * (y3 - y1) +
                x3 * (y1 - y2);

        return a == 0;
    }

    @Override
    public boolean inSimplex(Collection<MultiPointImplTwo> boundaries) {
        MultiPointImplTwo point = (MultiPointImplTwo) this;
        Iterator<MultiPointImplTwo> iterator = boundaries.iterator();
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
    public MultiPointImplTwo median(MultiPointImplTwo a, MultiPointImplTwo b, LinkedSimplex<MultiPointImplTwo> simplex, Splitter<MultiPointImplTwo> splitter) {
        ArrayList<MultiPointImplTwo> boundary = new ArrayList<>(simplex.getBoundaries());
        for (int i = 0; i < boundary.size(); i ++) {
            MultiPointImplTwo z = boundary.get(i);
            if (collinear(a, b, z)) {
                System.out.println("collinear exception " + a + " " + b + " " + z);
            }
        }
        for (int i = 0; i < boundary.size(); i ++) {
            MultiPointImplTwo z = boundary.get(i);
            for (int j = i + 1; j < boundary.size(); j ++) {
                MultiPointImplTwo y = boundary.get(j);
                if (collinear(a, y, z)) {
                    return handle(a, z, y);
                }
            }
        }
        for (int i = 0; i < boundary.size(); i ++) {
            MultiPointImplTwo z = boundary.get(i);
            for (int j = i + 1; j < boundary.size(); j ++) {
                MultiPointImplTwo y = boundary.get(j);
                if (collinear(b, y, z)) {
                    handle(y, z, b);
                }
            }
        }
        MultiPointImplTwo stdMedian = stdMedian(a, b);
        for (int i = 0; i < boundary.size(); i ++) {
            MultiPointImplTwo z = boundary.get(i);
            while (collinear(stdMedian, b, z)) {
                Random random = new Random();
                stdMedian = cmp(stdMedian.getX() + ((double)random.nextInt(100)) / 100_000, stdMedian.getY() + ((double)random.nextInt(100)) / 100_000);
            }
        }
        for (int i = 0; i < boundary.size(); i ++) {
            MultiPointImplTwo z = boundary.get(i);
            if (collinear(a, stdMedian, z)) {
                Random random = new Random();
                stdMedian = cmp(stdMedian.getX() + ((double)random.nextInt(100)) / 100_000, stdMedian.getY() + ((double)random.nextInt(100)) / 100_000);
            }
        }
        return stdMedian;
    }

    public MultiPointImplTwo handle(MultiPointImplTwo a, MultiPointImplTwo z, MultiPointImplTwo y) {
        throw new RuntimeException("collinear exception " + a + " " + y + " " + z);
    }

    public MultiPointImplTwo stdMedian(MultiPointImplTwo a, MultiPointImplTwo b) {
        return MultiPointImplTwo.cmp((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }
}
