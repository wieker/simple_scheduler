package org.allesoft.simple_scheduler.scheduler.cache.low;

public class MultiPointImplOne extends MultiPoint {
    final private int pos;

    public MultiPointImplOne(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object obj) {
        return pos == ((MultiPointImplOne) obj).pos;
    }

    @Override
    public String toString() {
        return " { " + pos + " } ";
    }
}
