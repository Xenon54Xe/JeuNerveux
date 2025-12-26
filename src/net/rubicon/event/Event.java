package net.rubicon.event;

import java.util.LinkedList;

public class Event implements IEvent{
    // E = what will get the listener and what we send

    LinkedList<IListener> listeners = new LinkedList<>();

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
