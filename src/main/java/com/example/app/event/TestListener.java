package com.example.app.event;

import com.example.app.event.component.IEventComponent;

public class TestListener implements Listener {

    public void register(IEvent event) {
        event.addListener(this);
    }

    @Override
    public void onTrigger(IEventComponent component) {
        System.out.println(component.getName());
    }

    @Override
    public void register(){}
}
