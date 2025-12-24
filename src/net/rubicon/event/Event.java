package net.rubicon.event;

import java.util.LinkedList;

public class Event<E extends IEventComponent> implements IEvent<E>{
    // E = what will get the listener and what we send

    LinkedList<IListener<E>> listeners = new LinkedList<>();

    public void addListener(IListener<E> listener) {
        listeners.add(listener);
    }

    public boolean removeListener(IListener<E> listener) {
        return listeners.remove(listener);
    }

    @Override
    public void trigger(E component) {
        for (IListener<E> listener : listeners){
            listener.onTrigger(component);
        }
    }
}
