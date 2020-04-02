package org.allesoft.simple_scheduler.cache.low;

import org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo;
import org.allesoft.simple_scheduler.scheduler.cache.low.SimplexLinkedGraphTwoReal;
import org.junit.Test;

import java.util.ArrayList;

import static org.allesoft.simple_scheduler.scheduler.cache.low.MultiPointImplTwo.cmp;

public class TestTwoDTest {
    @Test
    public void checkTwoDHelpers() {
        SimplexLinkedGraphTwoReal simplexWithTests = new SimplexLinkedGraphTwoReal(new ArrayList<>(), null, new ArrayList<>(), cmp(0, 0)) {
            public void test() {

            }
        };
    }
}
