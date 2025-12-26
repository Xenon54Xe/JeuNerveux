package net.rubicon.event;

import net.rubicon.entity.LivingEntity;

public record ComponentEntityDead(LivingEntity deadEntity, LivingEntity killer) implements IEventComponent {

    public ComponentEntityDead(LivingEntity deadEntity) {
        this(deadEntity, null);
    }

    @Override
    public String getName() {
        return deadEntity.getName() + " is dead !";
    }
}
