package org.allesoft.simple_scheduler.scheduler.cache.low;

public class MultiPointImplTwo extends MultiPoint {
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
}
