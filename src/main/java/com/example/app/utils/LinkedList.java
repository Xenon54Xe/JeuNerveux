package com.example.app.utils;

public class LinkedList<E> implements IList<E>{

    private Node<E> root;
    private int size;

    public LinkedList(){
        size = 0;
        root = null;
    }

    public LinkedList(E value){
        root = new Node<>(value);
        root.next = root;
        root.prev = root;

        size = 1;
    }

    public void add(E value){
        if (size == 0){
            root = new Node<>(value);
            root.next = root;
            root.prev = root;
        }
        else {
            Node<E> newNode = new Node<>(root.value);
            root.value = value;

            newNode.next = root.next;
            newNode.prev = root;
            root.next.prev = newNode;
            root.next = newNode;
        }
        size++;
    }

    public boolean remove(){
        // Remove the first element
        if (size == 0){
            return false;
        }
        if (size == 1){
            root = null;
            size = 0;
            return true;
        }
        else {
            root.next.prev = root.prev;
            root.prev.next = root.next;
            size--;
            return true;
        }
    }

    public boolean remove(E value){
        if (size == 0){
            return false;
        }

        for (int i = 0; i < size; i++){
            if (root.value.equals(value)){
                return remove();
            }
            shift(false);
        }

        return false;
    }

    public void shift(boolean reverse){
        if (!reverse) {
            root = root.prev;
        }
        else {
            root = root.next;
        }
    }

    public int size() {
        return size;
    }

    public E getFirstValue(){
        return root.value;
    }

    public E getFirstValueNShift(){
        E value = root.value;
        shift(false);
        return value;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < size; i++) {
            E value = getFirstValueNShift();
            text.append(value.toString());
        }

        return text.toString();
    }
}
