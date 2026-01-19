package com.example.app.utils;

public class LinkedList<E> implements ILinkedList<E> {

    private Node<E> root;
    private int size;
    private int currentIndex;

    public LinkedList(){
        root = null;

        size = 0;
        currentIndex = -1;
    }

    public LinkedList(E value){
        root = new Node<>(value);
        root.next = root;
        root.prev = root;

        size = 1;
        currentIndex = 0;
    }

    @Override
    public void clear() {
        root = null;

        size = 0;
        currentIndex = -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        assert 0 <= i && i < size;

        while (currentIndex != i){
            shift();
        }

        return getFirstValue();
    }

    @Override
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
        currentIndex = size - 1;
    }

    @Override
    public boolean remove(){
        // Remove the first element
        if (size == 0){
            return false;
        }

        if (size == 1){
            root = null;

            size = 0;
            currentIndex = -1;
        }
        else if (size > 1){
            root.next.prev = root.prev;
            root.prev.next = root.next;
            root = root.prev;

            size--;
            currentIndex = size - 1;
        }
        return true;
    }

    @Override
    public boolean remove(E value){
        if (size == 0){
            return false;
        }

        for (int i = 0; i < size; i++){
            if (root.value.equals(value)){
                return remove();
            }
            shift();
        }

        return false;
    }

    @Override
    public boolean contains(E value) {
        for (int i = 0; i < size(); i++){
            if (getFirstValue().equals(value)){
                return true;
            }
            shift();
        }
        return false;
    }

    public void shift(){
        root = root.prev;

        currentIndex++;
        if (currentIndex == size){
            currentIndex = 0;
        }
    }

    public void shift(boolean reverse){
        if (!reverse) {
            root = root.prev;

            currentIndex++;
            if (currentIndex == size){
                currentIndex = 0;
            }
        }
        else {
            root = root.next;

            currentIndex--;
            if (currentIndex == 0){
                currentIndex = size - 1;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(IList<E> other) {
        if ((other instanceof ILinkedList<E> linkedOther)){
            if (size() != linkedOther.size()){
                return false;
            }

            E val1 = getFirstValue();
            E val2;
            boolean in = linkedOther.contains(val1);
            if (!in){
                return false;
            }

            for (int i = 0; i < size(); i++){
                val1 = getFirstValueNShift();
                val2 = linkedOther.getFirstValueNShift();
                if (!val1.equals(val2)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public E getFirstValue(){
        return root.value;
    }

    @Override
    public String toString() {
        if (size() == 0){
            return "[]";
        }

        StringBuilder text = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            E value = getFirstValueNShift();
            text.append(value.toString()).append(", ");
        }
        text.delete(text.length() - 2, text.length());
        text.append("]");

        return text.toString();
    }
}
