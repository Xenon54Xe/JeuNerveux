package com.example.app.event;

import com.example.app.entity.LivingEntity;

public record ComponentEntityDead(LivingEntity deadEntity, LivingEntity killer) implements IEventComponent {

    public ComponentEntityDead(LivingEntity deadEntity) {
        this(deadEntity, null);
    }

    @Override
    public String getName() {
        return deadEntity.getName() + " is dead !";
    }
}
