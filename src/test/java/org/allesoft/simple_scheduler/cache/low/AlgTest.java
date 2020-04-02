package org.allesoft.simple_scheduler.cache.low;

import org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex;
import org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo;
import org.allesoft.simple_scheduler.scheduler.cache.low.SimplexLinkedGraphTwoReal;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo.cmp;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class AlgTest {
    @Test
    public void check1() {
        List<LinkedSimplex<MultiPointImplTwo>> neighbours = new ArrayList<>();
        List<MultiPointImplTwo> boundaries = Arrays.asList(cmp(0, 0), cmp(50, 100), cmp(100, 0));
        LinkedSimplex<MultiPointImplTwo> simplex = new SimplexLinkedGraphTwoReal(neighbours, null, boundaries, cmp(30, 30));

        LinkedSimplex<MultiPointImplTwo> onePart = simplex.split(cmp(15, 15), null, null);

        assertNotEquals(simplex, onePart);
        assertThat(onePart.getNeighbours().size(), is(2));
        assertFalse(onePart.getNeighbours().contains(onePart));
    }

    @Test
    public void check2() {
        List<LinkedSimplex<MultiPointImplTwo>> neighbours = new ArrayList<>();
        List<MultiPointImplTwo> boundaries = Arrays.asList(cmp(0, 0), cmp(50, 100), cmp(100, 0));
        LinkedSimplex<MultiPointImplTwo> simplex = new SimplexLinkedGraphTwoReal(neighbours, null, boundaries, cmp(30, 30));

        LinkedSimplex<MultiPointImplTwo> onePart = simplex.split(cmp(15, 15), null, null);

        assertNotEquals(simplex, onePart);
        assertThat(simplex.getNeighbours().size(), is(2));
        assertThat(onePart.getNeighbours().size(), is(2));
    }
}
