package org.allesoft.enterprise_db.queue.service.impl;

import java.util.ArrayList;
import java.util.List;

public class FilterPrimeNumbers {
    public static int[] numbers = {1, 5, 7, 10, 3, 11, 33, 34, 39, 77};
    public static List<Integer> result = new ArrayList<>();

    public static void main(String[] args) {
        int i;
        for (i = 0; i < numbers.length; i ++) {
            int number = numbers[i];
            if (filter(number)) {
                result.add(number);
            }
        }
        System.out.println(result.toString());
    }

    private static boolean filter(int number) {
        for (int i = 2; i < number; i ++) {
            System.out.println("check: " + number + " div " + i);
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
