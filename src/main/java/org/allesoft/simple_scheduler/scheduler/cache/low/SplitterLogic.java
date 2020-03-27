package org.allesoft.simple_scheduler.scheduler.cache.low;

public class SplitterLogic {
    private final MultiPoint point;
    private final LinkedSimplex next;

    public SplitterLogic(MultiPoint point, LinkedSimplex next) {
        this.point = point;
        this.next = next;
    }

    public MultiPoint getPoint() {
        return point;
    }

    public LinkedSimplex getNext() {
        return next;
    }
}
