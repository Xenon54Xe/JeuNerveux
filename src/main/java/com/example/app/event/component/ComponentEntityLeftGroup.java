package com.example.app.event.component;

import com.example.app.entity.Entity;

public record ComponentEntityLeftGroup(Entity entity) implements IEventComponent {

    public ComponentEntityLeftGroup {
        assert entity != null : "Entity cannot be null";
    }

    @Override
    public String getName() {
        return entity.getName();
    }
}
