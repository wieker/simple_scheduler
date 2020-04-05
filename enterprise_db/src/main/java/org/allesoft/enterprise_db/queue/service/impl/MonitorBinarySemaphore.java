package org.allesoft.enterprise_db.queue.service.impl;

public class MonitorBinarySemaphore {
    private Object object = new Object();
    private Integer lock = Integer.valueOf(0);

    public void signal() {
        synchronized (object) {
            lock = 1;
            object.notify();
        }
    }

    public void await() {
        synchronized (object) {
            while (lock == 0) {
                try {
                    object.wait();
                } catch (InterruptedException e) {

                }
            }
            lock = 0;
        }
    }
}
