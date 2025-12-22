package net.rubicon.event;

import java.util.LinkedList;

public abstract class Event<E, F> implements IEvent<E, F>{
    // E = what will get the listener, F = what we put in the trigger

    LinkedList<IListener<E>> listeners = new LinkedList<>();

    @Override
    public void addListener(IListener<E> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeListener(IListener<E> listener) {
        return listeners.remove(listener);
    }
}
