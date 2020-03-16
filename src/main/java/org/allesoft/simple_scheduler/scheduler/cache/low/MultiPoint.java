package org.allesoft.simple_scheduler.scheduler.cache.low;

public class MultiPoint {
    final private int pos;

    public MultiPoint(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object obj) {
        return pos == ((MultiPoint) obj).pos;
    }

    @Override
    public String toString() {
        return " { " + pos + " } ";
    }
}
