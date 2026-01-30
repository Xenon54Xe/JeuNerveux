package com.example.app.utils.collections;

import java.util.Iterator;

public interface ILoopList<E> extends Collection<E>, Iterable<E> {
    // A list with looping behavior (the pointer can move from end to start and vice versa)
    // Non-ordered list

    void shift(boolean reverse);

    default void shift(){
        shift(false);
    }

    E get(boolean shift, boolean reverse);

    default E get(boolean shift){
        return get(shift, false);
    }

    default E get(){
        return get(false, false);
    }

    void add(E value);

    default void addAll(Object[] values) {
        for (Object value : values){
            add((E) value);
        }
    }

    boolean remove();

    boolean remove(E value);

    default Object[] toArray(){
        if (size() == 0){
            return null;
        }

        Object[] values = new Object[size()];
        for (int i = 0; i < size(); i++){
            values[i] = get(true);
        }
        return values;
    }

    @Override
    default Iterator<E> iterator(){
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public E next() {
                currentIndex++;
                return get(true);
            }
        };
    }
}
