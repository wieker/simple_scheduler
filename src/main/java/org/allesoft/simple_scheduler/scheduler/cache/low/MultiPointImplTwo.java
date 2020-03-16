package org.allesoft.simple_scheduler.scheduler.cache.low;

public class MultiPointImplTwo extends MultiPoint {
    final private int x;
    final private int y;

    public MultiPointImplTwo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
