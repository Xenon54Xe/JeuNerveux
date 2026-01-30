package com.example.app.event.component;

public record ComponentRequestUpdate(int frameID) implements IEventComponent{
    @Override
    public String getName() {
        return "NeededUpdateUIFrame: " + frameID;
    }
}
