package com.example.app.utils.collections;

public class Binode<E>{

    public Binode<E> next;
    public Binode<E> prev;
    public E data;

    public Binode(E data) {
        this.data = data;
    }
}
