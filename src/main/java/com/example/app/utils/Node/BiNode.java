package com.example.app.utils.Node;

public class BiNode<E> extends Node<E> {

    public BiNode<E> prev;
    public BiNode<E> next;

    public BiNode(E value){
        super(value);
    }
}
