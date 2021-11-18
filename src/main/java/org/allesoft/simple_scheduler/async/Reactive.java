package org.allesoft.simple_scheduler.async;

public class Reactive {
    public static void main(String[] args) {
        int[] code = {1, 1, 0, 0};
        int i = 0;
        int q = 1;

        int counter_i = 0;
        int counter_q = 0;
        for (int j = 0; j < 1024; j ++) {
            i = (i + 1) % 4;
            q = (q + 1) % 4;

            int sig = (int)(Math.random() * 2);

            counter_i += (sig == code[i]) ? 1 : 0;
            counter_q += (sig == code[q]) ? 1 : 0;
        }

        System.out.println("counter i: " + counter_i);
        System.out.println("counter q: " + counter_q);
    }
}
