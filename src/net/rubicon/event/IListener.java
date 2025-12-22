package net.rubicon.event;

public interface IListener<E> {

    public void onTrigger(E uiObject);
}
