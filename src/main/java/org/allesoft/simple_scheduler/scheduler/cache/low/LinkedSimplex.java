package org.allesoft.simple_scheduler.scheduler.cache.low;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkedSimplex {
    static final int DIMENSIONS = 1;
    static final int LAYERS = 3;
    List<LinkedSimplex> neighbours = new ArrayList<>(DIMENSIONS + 1);
    List<LinkedSimplex> layers = new ArrayList<>(LAYERS);
    List<MultiPoint> boundaries = new ArrayList<>(DIMENSIONS + 1);
    MultiPoint value;

    LinkedSimplex search(MultiPoint point, int layer, List<LinkedSimplex> path) {
        if (inSimplex(point)) {
            if (layer < LAYERS - 1) {
                path.add(this);
                return layers.get(layer + 1).search(point, layer + 1, path);
            } else {
                path.add(this);
                return this;
            }
        } else {
            LinkedSimplex linkedSimplex = bestNeighbour(point);
            if (linkedSimplex != null) {
                return linkedSimplex.search(point, layer, path);
            } else {
                return null;
            }
        }
    }

    void insert(MultiPoint point) {
        ArrayList<LinkedSimplex> path = new ArrayList<>();
        search(point, 0, path).doInsert(point, path);
    }

    static void doInsert(MultiPoint point, List<LinkedSimplex> path) {
        System.out.println("Insert: " + point.getPos());
        if (point.getPos() == path.get(0).value.getPos()) {
            return;
        }
        List<LinkedSimplex> layers = new ArrayList<>(LAYERS);
        for (int i = 0; i < LAYERS; i ++) {
            layers.add(new LinkedSimplex());
        }
        int l = LAYERS - 1;
        for (int i = path.size() - 1; i >= 0; i --) {
            LinkedSimplex linkedSimplex;
            linkedSimplex = new LinkedSimplex();
            linkedSimplex.value = point;
            LinkedSimplex currentSimplexForLayer = path.get(i);
            if (point.getPos() < currentSimplexForLayer.value.getPos()) {
                linkedSimplex.neighbours.add(currentSimplexForLayer.neighbours.get(0));
                linkedSimplex.neighbours.add(currentSimplexForLayer);
                linkedSimplex.boundaries.add(new MultiPoint(currentSimplexForLayer.boundaries.get(0).getPos()));
                linkedSimplex.boundaries.add(new MultiPoint(point.getPos() + 1));
                currentSimplexForLayer.boundaries.get(0).setPos(point.getPos() + 1);
                currentSimplexForLayer.neighbours.set(0, linkedSimplex);
            } else {
                linkedSimplex.neighbours.add(currentSimplexForLayer);
                linkedSimplex.neighbours.add(currentSimplexForLayer.neighbours.get(1));
                linkedSimplex.boundaries.add(point);
                linkedSimplex.boundaries.add(new MultiPoint(currentSimplexForLayer.boundaries.get(1).getPos()));
                currentSimplexForLayer.boundaries.get(1).setPos(point.getPos());
                currentSimplexForLayer.neighbours.set(1, linkedSimplex);
            }
            layers.set(l, linkedSimplex);
            int nextInt = new Random().nextInt(2);
            System.out.println("layer: " + l + " final: " + nextInt);
            if (nextInt > 0) {
                break;
            }
            l --;
        }
        for (LinkedSimplex la : layers) {
            la.layers = layers;
        }
    }

    boolean inSimplex(MultiPoint point) {
        return point.getPos() >= boundaries.get(0).getPos() && point.getPos() < boundaries.get(1).getPos();
    }

    LinkedSimplex bestNeighbour(MultiPoint point) {
        return point.getPos() < boundaries.get(0).getPos() ? neighbours.get(0) : neighbours.get(1);
    }

    public static void main(String[] args) {
        List<LinkedSimplex> forLayers = new ArrayList<>(LAYERS);
        for (int i = 0; i < LAYERS; i ++) {
            LinkedSimplex forLayer = createForLayer();
            forLayers.add(forLayer);
            forLayer.layers = forLayers;
        }
        LinkedSimplex linkedSimplex = forLayers.get(0);
        linkedSimplex.insert(new MultiPoint(20));
        linkedSimplex.insert(new MultiPoint(28));
        linkedSimplex.insert(new MultiPoint(24));
        linkedSimplex.insert(new MultiPoint(9));
        linkedSimplex.insert(new MultiPoint(2));
        linkedSimplex.insert(new MultiPoint(22));
        linkedSimplex.insert(new MultiPoint(48));

        linkedSimplex = linkedSimplex.search(new MultiPoint(1), 0, new ArrayList<>());

        print(linkedSimplex);
        print(linkedSimplex.layers.get(2));
        print(linkedSimplex.layers.get(1));
        print(linkedSimplex.layers.get(0));
    }

    private static void print(LinkedSimplex linkedSimplex) {
        while (true) {
            if (linkedSimplex == null) {
                break;
            }
            System.out.print(linkedSimplex.value.getPos() + " ");
            if (linkedSimplex.neighbours.size() < 2) {
                break;
            }
            LinkedSimplex nextSimplex = linkedSimplex.neighbours.get(1);
            if (nextSimplex == null) {
                break;
            }
            linkedSimplex = nextSimplex;
        }
        System.out.println();
    }

    private static LinkedSimplex createForLayer() {
        LinkedSimplex linkedSimplex = new LinkedSimplex();
        linkedSimplex.value = new MultiPoint(10);
        linkedSimplex.boundaries.add(0, new MultiPoint(0));
        linkedSimplex.boundaries.add(1, new MultiPoint(100));
        linkedSimplex.neighbours.add(null);
        linkedSimplex.neighbours.add(null);
        return linkedSimplex;
    }
}
