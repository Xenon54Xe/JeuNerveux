package com.example.app.utils;

public interface ILoopList<E> {
    // A list with looping behavior (the pointer can move from end to start and vice versa)
    // Non-ordered list

    int size();

    boolean isEmpty();

    void clear();

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

    boolean contains(E value);

    default void setRoot(E value){
        assert contains(value);

        while (!get().equals(value)){
            shift();
        }
    }

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

    String toString();
}
