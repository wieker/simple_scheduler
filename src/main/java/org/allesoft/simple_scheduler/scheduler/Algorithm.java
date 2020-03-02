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
        show_marked_rows(matrix, assigned_row);

        int[] mark_row_without_assign = new int[matrix.length];
        for (int i = 0; i < matrix.length; i ++) {
            if (assigned_row[i] == -1) {
                mark_row_without_assign[i] = 1;
            }
        }
        show_marked_rows(matrix, mark_row_without_assign);
        boolean marked;
        int[] mark_col_having_zero_in_marked_rows = new int[matrix[0].length];
        do {
            marked = false;
            for (int i = 0; i < matrix.length; i ++) {
                for (int j = 0; j < matrix[0].length; j ++) {
                    if (mark_col_having_zero_in_marked_rows[j] == 0 && mark_row_without_assign[i] == 1 && matrix[i][j] == 0) {
                        mark_col_having_zero_in_marked_rows[j] = 1;
                        marked = true;
                    }
                }
            }
            show_marked_rows(matrix, mark_col_having_zero_in_marked_rows);
            for (int i = 0; i < matrix.length; i ++) {
                if (assigned_row[i] != -1 && mark_col_having_zero_in_marked_rows[assigned_row[i]] == 1 &&
                        mark_row_without_assign[i] == 0) {
                    mark_row_without_assign[i] = 1;
                    marked = true;
                }
            }
            show_marked_rows(matrix, mark_row_without_assign);
        } while (marked);


        return result;
    }

    private void show_marked_rows(double[][] matrix, int[] mark_row_without_assign) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(mark_row_without_assign[i] + " ");
        }
        System.out.println();
    }

    private void step2(double[][] matrix) {
        for (int i = 0; i < matrix[0].length; i ++) {
            double min = matrix[0][i];
            for (double[] doubles : matrix) {
                if (min > doubles[i]) {
                    min = doubles[i];
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
                if (min > matrix[i][j]) {
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
                {2, 3},
                {2, 4},
        });
    }
}
