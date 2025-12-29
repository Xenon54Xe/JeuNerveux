package com.example.app;

import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow(GameCanvas canvas) {
        setTitle("JeuNerveux");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(canvas);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
