package org.allesoft.simple_scheduler.scheduler.cache.low;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo.cmp;

public class Gra {
    public static void main(String[] args) {
        LinkedSimplex<MultiPointImplTwo> forLayer = SimplexLinkedGraphTwoReal.createForLayer(0);

        JFrame window = new JFrame();
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        AtomicReference<LinkedSimplex<MultiPointImplTwo>> simplex = forLayer.getSelf();

        JComponent canvas = new JComponent() {
            int centerY = getHeight() / 4;
            int centerX = getWidth() / 4;

            @Override
            protected void paintComponent(Graphics g) {
                LinkedSimplex<MultiPointImplTwo> initial = simplex.get();
                Set<LinkedSimplex<MultiPointImplTwo>> passed = new HashSet<>();
                draw(g, initial, passed);
                centerX *= 2;
                passed = new HashSet<>();
                draw(g, initial.getNextLayer().get(), passed);
                centerY *= 2;
                passed = new HashSet<>();
                draw(g, initial.getNextLayer().get().getNextLayer().get(), passed);
                centerY = getHeight() / 4;
                centerX = getWidth() / 4;
            }

            private void draw(Graphics g, LinkedSimplex<MultiPointImplTwo> simplex, Set<LinkedSimplex<MultiPointImplTwo>> passed) {
                passed.add(simplex);
                List<MultiPointImplTwo> boundaries = new ArrayList<>(simplex.getBoundaries());
                int size = boundaries.size();
                for (int i = 0; i < size; i++) {
                    MultiPointImplTwo curr = (MultiPointImplTwo) boundaries.get(i);
                    MultiPointImplTwo next = (MultiPointImplTwo) boundaries.get((i + 1) % size);
                    double scale = 5;
                    g.drawLine(centerX + (int) (curr.getX() * scale), centerY + (int) (curr.getY() * scale), centerX + (int) (next.getX() * scale), centerY + (int) (next.getY() * scale));
                }
                for (AtomicReference<LinkedSimplex<MultiPointImplTwo>> next : simplex.getNeighbours()) {
                    if (next != null) {
                        LinkedSimplex<MultiPointImplTwo> simplex1 = next.get();
                        if (!passed.contains(simplex1)) {
                            draw(g, simplex1, passed);
                        }
                    }
                }
            }
        };
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int centerY = canvas.getHeight() / 4;
                int centerX = canvas.getWidth() / 4;
                double scale = 5;
                simplex.get().insert(cmp((e.getX() - centerX) / scale, (e.getY() - centerY) / scale)).get();
                System.out.println(simplex.get());
                canvas.repaint();
            }
        });
        window.add(canvas);


        for (int i = 1; i < 100; i += 10) {
            for (int j = 1; j < 100 - i; j += 10) {
                try {
                    simplex.get().insert(MultiPointImplTwo.cmp(i, j));
                } catch (NullPointerException e) {

                }
                canvas.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
