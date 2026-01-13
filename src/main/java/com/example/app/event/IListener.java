package com.example.app.event;

import com.example.app.event.component.IEventComponent;

public interface IListener {

    void onTrigger(IEventComponent component);

    void register(IEvent event);
}
