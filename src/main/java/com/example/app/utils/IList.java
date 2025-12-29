package com.example.app.utils;

public interface IList<E> {

    void add(E value);

    boolean remove(E value);

    int size();
}
