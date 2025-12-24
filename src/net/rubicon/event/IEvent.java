package net.rubicon.event;

public interface IEvent<E extends IEventComponent>{

    void trigger(E component);
}
