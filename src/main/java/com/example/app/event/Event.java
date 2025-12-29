package com.example.app.event;

import java.util.ArrayList;

public class Event implements IEvent{
    // E = what will get the listener and what we send

    ArrayList<IListener> listeners = new ArrayList<>();

    public void addListener(IListener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(IListener listener) {
        return listeners.remove(listener);
    }

    @Override
    public void trigger(IEventComponent component) {
        for (IListener listener : listeners){
            listener.onTrigger(component);
        }
    }
}
