package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.AlgorithmService;

import java.util.Arrays;

public class Algorithm implements AlgorithmService {
    @Override
    public int[] allocateMatrix(double[][] matrix) {
        int[] result = new int[matrix.length];

        decrement_rows(matrix);

        decrement_columns(matrix);

        int[] assigned_row = new int[matrix.length];
        find_all_zeros(matrix, assigned_row);

        real_allocate(matrix, result);

        return result;
    }

    // is it N!??? something is going wrong
    private void real_allocate(double[][] matrix, int[] possible_allocations) {
        int[] allocated = new int[matrix.length];
        int pos = 0;
        Arrays.fill(possible_allocations, -1);
        while (pos < matrix.length) {
            boolean restart = true;
            for (int i = possible_allocations[pos] + 1; i < matrix[pos].length; i ++) {
                if (possible_allocations[pos] > -1) {
                    allocated[possible_allocations[pos]] = 0;
                }
                if (matrix[pos][i] == 0 && allocated[i] != 1) {
                    possible_allocations[pos] = i;
                    allocated[i] = 1;
                    pos ++;
                    restart = false;
                    break;
                }
            }

            if (restart) {
                possible_allocations[pos] = -1;
                pos--;
            }
        }
        show_marked_rows(matrix, possible_allocations);
    }

    private void find_all_zeros(double[][] matrix, int[] assigned_row) {
        int[] crossed_column = new int[matrix.length];
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
    }

    private void show_marked_rows(double[][] matrix, int[] mark_row_without_assign) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(mark_row_without_assign[i] + " ");
        }
        System.out.println();
    }

    private void decrement_columns(double[][] matrix) {
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

    private void decrement_rows(double[][] matrix) {
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
