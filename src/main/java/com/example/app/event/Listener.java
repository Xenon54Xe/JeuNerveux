package com.example.app.event;

import com.example.app.event.component.IEventComponent;

public interface Listener {

    void onTrigger(IEventComponent component);

    void register();
}
