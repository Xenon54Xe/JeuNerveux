package com.example.app.utils;

public interface ILinkedList<E> extends IList<E>{

    default void addAll(ILinkedList<E> values){
        for (int i = 0; i < values.size(); i++) {
            add(values.getFirstValueNShift());
        }
    }

    boolean remove();

    void shift();

    void shift(boolean reverse);

    E getFirstValue();

    default E getFirstValueNShift(){
        E value = getFirstValue();
        shift();
        return value;
    }

    default void setRoot(E value){
        assert contains(value);

        while (!getFirstValue().equals(value)){
            shift();
        }
    }

    default Object[] toArray(){
        if (size() == 0){
            return null;
        }

        Object[] values = new Object[size()];
        for (int i = 0; i < size(); i++){
            values[i] = getFirstValueNShift();
        }
        return values;
    }
}
