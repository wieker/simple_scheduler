package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scheduler.Algorithm;
import org.junit.Assert;
import org.junit.Test;

public class AlgorithmTest {
    @Test
    public void check0() {
        double[][] matrix = {};

        int[] result = new Algorithm().allocateMatrix(matrix);

        Assert.assertArrayEquals(result, new int[] {});
    }

    @Test
    public void check1() {
        double[][] matrix = {{1}};

        int[] result = new Algorithm().allocateMatrix(matrix);

        Assert.assertArrayEquals(result, new int[] {0});
    }
}
