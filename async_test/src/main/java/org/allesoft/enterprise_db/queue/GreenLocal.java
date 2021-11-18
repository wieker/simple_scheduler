package org.allesoft.enterprise_db.queue;

import java.util.concurrent.Future;
import java.util.function.Predicate;

public class GreenLocal<T extends Object> {
    private T var;

    public GreenLocal(T var) {
        this.var = var;
    }

    public interface GreenConsumer<T> {
        void accept(T t) throws GreenException;
    }

    public interface GreenAsync<T, V> {
        Future<V> accept(T t) throws GreenException;
    }

    public void sync(GreenConsumer<T> consumer) throws GreenException {
        if (var != null) {
            consumer.accept(var);
        }
    }

    public boolean deadOrEquals(Predicate<T> predicate) {
        return var == null || predicate.test(var);
    }

    public <V> Future<V> syncMap(GreenAsync<T, V> async) throws GreenException {
        if (var != null) {
            return async.accept(var);
        }
        return null;
    }

    public T getVar() {
        return var;
    }

    public void setVar(T var) {
        this.var = var;
    }
}
