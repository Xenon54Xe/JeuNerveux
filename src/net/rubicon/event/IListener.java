package net.rubicon.event;

public interface IListener<E extends IEventComponent> {

    void onTrigger(E component);
}
