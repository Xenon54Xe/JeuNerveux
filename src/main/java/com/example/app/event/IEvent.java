package com.example.app.event;

import com.example.app.event.component.IEventComponent;

public interface IEvent{

    void addListener(IListener listener);

    boolean removeListener(IListener listener);

    void trigger(IEventComponent component);
}
