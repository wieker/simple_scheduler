package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.AlgorithmService;

import java.util.Arrays;

public class Algorithm implements AlgorithmService {
    @Override
    public int[] allocateMatrix(double[][] matrix) {
        int[] result = new int[matrix.length];

        step1(matrix);

        step2(matrix);

        int[] crossed_column = new int[matrix.length];
        int[] assigned_row = new int[matrix.length];
        Arrays.fill(assigned_row, -1);
        for (int i = 0; i < matrix.length; i ++) {
            for (int j = 0; j < matrix.length; j ++) {
                if (matrix[i][j] == 0 && crossed_column[j] == 0) {
                    assigned_row[i] = j;
                    crossed_column[j] = 1;
                    break;
                }
            }
        }

        int[] mark_row = new int[matrix.length];
        for (int i = 0; i < matrix.length; i ++) {
            if (assigned_row[i] == -1) {
                mark_row[i] = 1;
            }
        }
        boolean marked;
        int[] mark_col = new int[matrix[0].length];
        do {
            marked = false;
            for (int i = 0; i < matrix.length; i ++) {
                for (int j = 0; j < matrix[0].length; j ++) {
                    if (mark_col[j] == 0 && mark_row[i] == 1 && matrix[i][j] == 0) {
                        mark_col[j] = 1;
                        marked = true;
                    }
                }
            }
            for (int i = 0; i < matrix.length; i ++) {
                if (assigned_row[i] != -1 && mark_col[assigned_row[i]] == 1 &&
                        mark_row[i] == 0) {
                    mark_row[i] = 1;
                    marked = true;
                }
            }
        } while (marked);

        for (int i = 0; i < matrix.length; i ++) {
            System.out.print(assigned_row[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i ++) {
            System.out.print(mark_row[i] + " ");
        }
        System.out.println();

        return result;
    }

    private void step2(double[][] matrix) {
        for (int i = 0; i < matrix[0].length; i ++) {
            double min = matrix[0][i];
            for (int j = 0; j < matrix.length; j ++) {
                if (min < matrix[j][i]) {
                    min = matrix[j][i];
                }
            }
            for (int j = 0; j < matrix.length; j ++) {
                matrix[j][i] -= min;
            }
        }
    }

    private void step1(double[][] matrix) {
        for (int i = 0; i < matrix.length; i ++) {
            double min = matrix[i][0];
            for (int j = 0; j < matrix[i].length; j ++) {
                if (min < matrix[i][j]) {
                    min = matrix[i][j];
                }
            }
            for (int j = 0; j < matrix[i].length; j ++) {
                matrix[i][j] -= min;
            }
        }
    }

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        algorithm.allocateMatrix(new double[][] {
                {2, 2},
                {2, 1},
        });
    }
}
