package com.example.app;

public class Main {
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        System.out.println("Assertions enabled: " + assertionsEnabled);

        GameCanvas canvas = new GameCanvas();
        new GameWindow(canvas);
        canvas.startGameThread();
    }
}
