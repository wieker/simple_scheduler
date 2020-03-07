package org.allesoft.simple_scheduler.nrf52.mesh;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ThreadLocalRandom;

public class RxApp {
    public static void main(String[] args) throws Exception {
        Observable<Integer> vals = Observable.range(1,10);

        vals.flatMap(val -> Observable.just(val)
                .subscribeOn(Schedulers.computation())
                .map(i -> intenseCalculation(i))
        ).subscribe(val -> System.out.println(val));

        System.in.read();
    }

    public static int intenseCalculation(int i) {
        try {
            System.out.println("Calculating " + i +
                    " on " + Thread.currentThread().getName());
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000));
            return i;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
