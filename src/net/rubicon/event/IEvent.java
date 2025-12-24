package net.rubicon.event;

public interface IEvent<E extends EventComponent> {

    void trigger(E object);
}
