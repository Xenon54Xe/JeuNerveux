package com.example.app.event;

import com.example.app.event.component.IEventComponent;

import java.util.ArrayList;

public class Event implements IEvent{
    // E = what will get the listener and what we send

    private final ArrayList<Listener> listeners = new ArrayList<>();

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeListener(Listener listener) {
        return listeners.remove(listener);
    }

    @Override
    public void trigger(IEventComponent component) {
        for (Listener listener : listeners){
            listener.onTrigger(component);
        }
    }
}
