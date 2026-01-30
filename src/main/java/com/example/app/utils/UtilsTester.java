package com.example.app.utils;

import com.example.app.utils.collections.LinkedList;
import com.example.app.utils.collections.List;

public class UtilsTester {

    public static void main(String[] args) {

        System.out.println("UtilsTester is running...");

        // Test my LinkedList implementation
        testLinkedList();

        // Test my LoopList implementation
        testLoopList();
    }

    // LOOP LIST TEST
    private static void testLoopList() {
        System.out.println("Testing LoopList...");

        com.example.app.utils.collections.LoopList<String> loopList = new com.example.app.utils.collections.LoopList<>();

        loopList.add("First");
        loopList.add("Second");
        loopList.add("Third");

        System.out.println("LoopList size after additions: " + loopList.size());

        for (int i = 0; i < loopList.size() * 2; i++) {
            System.out.println("Element at iteration " + i + ": " + loopList.get(true, false));
        }

        loopList.clear();
        System.out.println("LoopList size after clearing: " + loopList.size());
    }


    private static void testLinkedList() {
        System.out.println("Testing LinkedList...");

        List<String> list = new LinkedList<>();

        list.add("First");
        list.add("Second");
        list.add(1, "Inserted at index 1");

        System.out.println("List size after additions: " + list.size());

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Element at index " + i + ": " + list.get(i));
        }

        list.remove("Second");
        System.out.println("List size after removal: " + list.size());

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Element at index " + i + ": " + list.get(i));
        }

        list.clear();
        System.out.println("List size after clearing: " + list.size());
    }
}
