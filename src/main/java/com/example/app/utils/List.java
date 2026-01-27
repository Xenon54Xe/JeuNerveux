package com.example.app.utils;

public interface List<E> {

    void clear();

    boolean isEmpty();

    E get(int i);

    void add(E value);

    default void addAll(Object[] values) {
        for (Object value : values){
            add((E) value);
        }
    }

    boolean remove(E value);

    boolean contains(E value);

    int size();

    boolean equals(List<E> other);

    String toString();
}
