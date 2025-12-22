package net.rubicon.event;

import java.util.LinkedList;

public abstract class Event<E> implements IEvent<E>{
    // E = what will get the listener, F = what we put in the trigger

    LinkedList<IListener<E>> listeners = new LinkedList<>();

    public void addListener(IListener<E> listener) {
        listeners.add(listener);
    }

    public boolean removeListener(IListener<E> listener) {
        return listeners.remove(listener);
    }

    @Override
    public void trigger(E payload) {
        for (IListener<E> listener : listeners){
            listener.onTrigger(payload);
        }
    }
}
