package net.rubicon.event;

public interface IListener<E extends EventComponent> {

    public void onTrigger(E eventComponent);
}
