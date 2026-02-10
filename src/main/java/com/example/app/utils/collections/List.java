package com.example.app.utils.collections;

public interface List<E> extends Collection<E> {

    void add(E element);

    void add(int index, E element);

    E get(int index);

    E remove(int index);

    boolean remove(E element);
}
