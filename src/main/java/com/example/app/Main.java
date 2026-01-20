package com.example.app;

public class Main {
    public static void main(String[] args) {
        System.out.println("Working dir: " + System.getProperty("user.dir"));
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("Java version: " + System.getProperty("java.version"));

        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        System.out.println("Assertions enabled: " + assertionsEnabled);

        GameCanvas canvas = new GameCanvas();
        new GameWindow(canvas);
        canvas.startGameThread();
    }
}
