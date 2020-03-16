package org.allesoft.simple_scheduler.cache.low;

import org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex;
import org.allesoft.simple_scheduler.scheduler.cache.low.MultiPoint;
import org.allesoft.simple_scheduler.scheduler.cache.low.SimplexLinkedGraphTwoReal;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo.cmp;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class AlgTest {
    @Test
    public void check1() {
        List<LinkedSimplex> neighbours = new ArrayList<>();
        List<MultiPoint> boundaries = List.of(cmp(0, 0), cmp(50, 100), cmp(100, 0));
        LinkedSimplex simplex = new SimplexLinkedGraphTwoReal(neighbours, null, boundaries, cmp(30, 30));

        LinkedSimplex onePart = simplex.split(cmp(15, 15), null);

        assertNotEquals(simplex, onePart);
        assertThat(simplex.getNeighbours().size(), is(2));
        assertThat(onePart.getNeighbours().size(), is(2));
        assertFalse(onePart.getNeighbours().contains(onePart));
        assertFalse(simplex.getNeighbours().contains(simplex));
    }

    @Test
    public void check2() {
        List<LinkedSimplex> neighbours = new ArrayList<>();
        List<MultiPoint> boundaries = List.of(cmp(0, 0), cmp(50, 100), cmp(100, 0));
        LinkedSimplex simplex = new SimplexLinkedGraphTwoReal(neighbours, null, boundaries, cmp(30, 30));

        LinkedSimplex onePart = simplex.split(cmp(15, 15), null);

        assertNotEquals(simplex, onePart);
        assertThat(simplex.getNeighbours().size(), is(2));
        assertThat(onePart.getNeighbours().size(), is(2));
    }
}
