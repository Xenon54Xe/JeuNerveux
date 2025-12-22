package net.rubicon.utils;

public class LinkedList<E> {

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
}
