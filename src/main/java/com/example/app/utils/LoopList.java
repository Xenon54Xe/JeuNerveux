package com.example.app.utils;

import com.example.app.utils.Node.BiNode;

public class LoopList<E> implements ILoopList<E> {

    private BiNode<E> root;
    private int size;
    private int currentIndex;

    public LoopList(){
        root = null;

        size = 0;
        currentIndex = -1;
    }

    public LoopList(E value){
        root = new BiNode<>(value);
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
    public E get(boolean shift, boolean reverse) {
        E value = root.value;
        if (shift){
            shift(reverse);
        }
        return value;
    }

    @Override
    public void add(E value){
        if (size == 0){
            root = new BiNode<>(value);
            root.next = root;
            root.prev = root;
            size = 1;
            currentIndex = 0;
        }
        else {
            BiNode<E> newBiNode = new BiNode<>(root.value);
            root.value = value;

            newBiNode.next = root.next;
            newBiNode.prev = root;
            root.next.prev = newBiNode;
            root.next = newBiNode;
            size++;
            currentIndex++;
        }
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
        else{
            root.next.prev = root.prev;
            root.prev.next = root.next;
            root = root.prev;

            size--;
            currentIndex--;
            if (currentIndex < 0){
                currentIndex = size - 1;
            }
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
            if (get().equals(value)){
                return true;
            }
            shift();
        }
        return false;
    }

    @Override
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
    public void shift(){
        shift(false);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size() == 0){
            return "[]";
        }

        StringBuilder text = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            E value = get(true);
            text.append(value.toString()).append(", ");
        }
        text.delete(text.length() - 2, text.length());
        text.append("]");

        return text.toString();
    }
}
