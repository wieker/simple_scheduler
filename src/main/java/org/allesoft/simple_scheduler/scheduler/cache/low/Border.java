package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.List;

public class Border {
    final List<MultiPoint> border;

    public Border(List<MultiPoint> border) {
        this.border = border;
    }

    public List<MultiPoint> getBorder() {
        return border;
    }
}
