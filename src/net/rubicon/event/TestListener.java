package net.rubicon.event;

public class TestListener<E extends IEventComponent> implements IListener<E>{

    @Override
    public void onTrigger(E component) {
        System.out.println(component.getName());
    }
}
