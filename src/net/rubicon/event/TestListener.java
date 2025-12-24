package net.rubicon.event;

public class TestListener<E extends EventComponent> implements IListener<E>{

    @Override
    public void onTrigger(E eventComponent) {
        System.out.println(eventComponent.getName());
    }
}
