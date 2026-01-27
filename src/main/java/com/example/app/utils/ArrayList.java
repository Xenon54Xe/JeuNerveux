package com.example.app.utils;

import java.util.Objects;

public class ArrayList<E> implements List<E> {

    private final OneNode<E> root; // sentinel dummy node; root never changes
    private int size;

    public ArrayList() {
        root = new OneNode<>(null);
        size = 0;
    }

    public ArrayList(E value) {
        root = new OneNode<>(null);
        root.next = new OneNode<>(value);
        size = 1;
    }

    @Override
    public void clear() {
        root.next = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }
        OneNode<E> cur = root.next;
        for (int j = 0; j < i; j++) {
            cur = cur.next;
        }
        return cur.value;
    }

    @Override
    public void add(E value) {
        OneNode<E> cur = root;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = new OneNode<>(value);
        size++;
    }

    @Override
    public boolean remove(E value) {
        if (isEmpty()) {
            return false;
        }
        OneNode<E> prev = root;
        while (prev.next != null && !Objects.equals(prev.next.value, value)) {
            prev = prev.next;
        }
        if (prev.next == null) {
            return false;
        }
        prev.next = prev.next.next;
        size--;
        return true;
    }

    @Override
    public boolean contains(E value) {
        OneNode<E> cur = root.next;
        while (cur != null) {
            if (Objects.equals(cur.value, value)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(List<E> other) {
        if (other == null || size != other.size()) {
            return false;
        }
        OneNode<E> cur = root.next;
        for (int i = 0; i < size; i++) {
            if (!Objects.equals(cur.value, other.get(i))) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        OneNode<E> cur = root.next;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) {
                sb.append(", ");
            }
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // Simple node class included for clarity; if project already has OneNode, remove this.
    private static class OneNode<T> {
        T value;
        OneNode<T> next;
        OneNode(T value) { this.value = value; }
    }
}