package net.rubicon.event;

public interface IEvent<E> {

    void trigger(E object);
}
