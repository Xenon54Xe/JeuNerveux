package com.example.app.utils.collections;

public class Node<E> {

    public Node<E> next;
    public E data;

    public Node(E data){
        this.data = data;
    }
}
