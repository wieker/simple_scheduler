package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;

public class LinkedSimplex<T> {
    static final int DIMENSIONS = 2;
    static final int LAYERS = 3;
    List<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    List<LinkedSimplex> layers = new ArrayList<>(LAYERS);
    T value;

    LinkedSimplex search(MultiPoint point, int layer) {
        if (inSimplex()) {
            if (layer < LAYERS - 1) {
                return layers.get(layer + 1).search(point, layer + 1);
            } else {
                return this;
            }
        } else {
            return bestNeighbour(point).search(point, layer);
        }
    }

    void insert(MultiPoint point) {
        search(point, 0).doInsert(point);
    }

    void doInsert(MultiPoint point) {
        search(point, 0);
    }

    boolean inSimplex() {
        return true;
    }

    LinkedSimplex bestNeighbour(MultiPoint point) {
        return null;
    }
}
