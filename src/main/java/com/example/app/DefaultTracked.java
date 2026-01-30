package com.example.app;

public class DefaultTracked implements Trackable{

    @Override
    public double getWorldX() {
        return 0;
    }

    @Override
    public double getWorldY() {
        return 0;
    }

    @Override
    public int getCameraWorldX() {
        return (int)getWorldX();
    }

    @Override
    public int getCameraWorldY() {
        return (int)getWorldY();
    }
}
