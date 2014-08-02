package sample.point;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alex on 02.08.2014.
 */
public class SynchronizedList<T> {
    private AtomicInteger pointer = new AtomicInteger();
    private Object[] container;

    public SynchronizedList(int size) {
        container = new Object[size];
    }

    public void add(T item) {
        int index = pointer.getAndIncrement();
        container[index] = item;
    }

    public T get(int index) {
        return (T) container[index];
    }

    public int size() {
        return pointer.get();
    }

    public boolean isEmpty() {
        return pointer.get() == 0;
    }
}
