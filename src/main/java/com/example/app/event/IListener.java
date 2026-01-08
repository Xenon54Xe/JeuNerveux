package com.example.app.event;

public interface IListener {

    void onTrigger(IEventComponent component);

    void register(Event event);
}
