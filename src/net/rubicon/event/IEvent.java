package net.rubicon.event;

public interface IEvent<E, F> {

    public void addListener(IListener<E> target);

    public boolean removeListener(IListener<E> target);

    public void trigger(F object);
}
