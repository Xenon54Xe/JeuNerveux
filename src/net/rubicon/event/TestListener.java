package net.rubicon.event;

public class TestListener implements IListener{

    @Override
    public void onTrigger(IEventComponent component) {
        System.out.println(component.getName());
    }
}
