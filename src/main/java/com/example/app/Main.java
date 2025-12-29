package com.example.app;

public class Main {
    public static void main(String[] args) {
        GameCanvas canvas = new GameCanvas();
        new GameWindow(canvas);
        canvas.startGameThread();
    }
}
