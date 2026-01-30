package com.example.app.utils.collections;

public interface Collection<E> extends Iterable<E> {

    int size();

    void clear();

    boolean isEmpty();

    boolean contains(E element);
}
