package com.example.app.utils;

public class Node<E> {

    public E value;
    public Node<E> prev;
    public Node<E> next;

    public Node(E value){
        this.value = value;
    }
}
