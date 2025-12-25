package net.rubicon.event;

import net.rubicon.entity.LivingEntity;

public record ECEntityDead(LivingEntity deadEntity, LivingEntity killer) implements IEventComponent {

    public ECEntityDead(LivingEntity deadEntity) {
        this(deadEntity, null);
    }

    @Override
    public String getName() {
        return deadEntity.getName() + " is dead !";
    }
}
