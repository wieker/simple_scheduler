package org.allesoft.simple_scheduler.cache.low;

import org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex;
import org.allesoft.simple_scheduler.scheduler.cache.low.MultiPoint;
import org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo;
import org.allesoft.simple_scheduler.scheduler.cache.low.SimplexLinkedGraphTwoReal;
import org.allesoft.simple_scheduler.scheduler.cache.low.Splitter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.allesoft.simple_scheduler.scheduler.cache.low.LinkedSimplex.LAYERS;
import static org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo.cmp;
import static org.allesoft.simple_scheduler.scheduler.cache.low.SimplexLinkedGraphTwoReal.createForLayer;
import static org.mockito.Mockito.mock;

public class SplitterTest {
    @Test
    public void test() {
        LinkedSimplex<MultiPointImplTwo> simplex = new LinkedSimplex<MultiPointImplTwo>(new ArrayList<>(),
                null, Arrays.asList(cmp(0, 100), cmp(0, 0), cmp(100, 0)), cmp(30, 30), null);
        Splitter<MultiPointImplTwo> splitter = new Splitter<>();
        MultiPointImplTwo pointImplTwo = cmp(10, 10);

        splitter.split(pointImplTwo, null, simplex);
    }
}
