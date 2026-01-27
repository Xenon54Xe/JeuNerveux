package com.example.app.event;

import com.example.app.event.component.IEventComponent;

public interface IEvent{

    void addListener(Listener listener);

    boolean removeListener(Listener listener);

    void trigger(IEventComponent component);
}
