package net.rubicon.utils;

public class Node<E> {

    E value;
    Node<E> prev;
    Node<E> next;

    public Node(E value){
        this.value = value;
    }
}
