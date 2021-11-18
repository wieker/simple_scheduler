package org.allesoft.enterprise_db.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class GreenLight<T extends Object> {
    private int size;
    private int current;
    Consumer<GreenLocal<T>> consumer;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<T>(100);
    static List<GreenThread> threads = new ArrayList<>();
    static GreenThread ctx;

    static GreenLocal greenLocal = new GreenLocal<>(null);

    public GreenLight(int size) {
        this.size = size;
        current = 0;
    }

    public int limit() {
        return size - current;
    }

    public void execute(T work) {
        queue.add(work);
    }

    public void setConsumer(Consumer<GreenLocal<T>> consumer) {
        this.consumer = consumer;
        executorService.execute(() -> {
            for (;;) {
                T poll = queue.poll();
                GreenThread<Object> objectGreenThread = new GreenThread<>();
                objectGreenThread.value = poll;
                threads.add(objectGreenThread);
                greenLocal.setVar(poll);
                ctx = objectGreenThread;
                consumer.accept(greenLocal);
                threads.remove(ctx);
                ctx = null;
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static <E> GreenLocal<E> asyncAwait(Future<E> await) {
        try {
            if (await != null) {
                ctx.future = await;
                ctx = null;
            }
            for (GreenThread th : threads) {
                if (th.future != null && th.future.isDone()) {
                    ctx = th;
                    greenLocal.setVar(ctx.value);
                    return new GreenLocal(th.future.get());
                }
            }
            return new GreenLocal<>(null);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static class GreenThread<T> {
        T value;
        Future future;
    }
}
