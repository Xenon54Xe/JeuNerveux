package com.example.app.utils;

public interface IList<E> {

    void clear();

    boolean isEmpty();

    E get(int i);

    default E getFirst(){
        return get(0);
    }

    void add(E value);

    default void addAll(E[] values) {
        for (E value : values){
            add(value);
        }
    }

    boolean remove(E value);

    boolean contains(E value);

    int size();

    boolean equals(IList<E> other);

    Object[] toArray();

    String toString();
}
