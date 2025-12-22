package net.rubicon.event;

public class TestListener<E> implements IListener<E>{

    @Override
    public void onTrigger(E payload) {
        System.out.println(payload);
    }
}
