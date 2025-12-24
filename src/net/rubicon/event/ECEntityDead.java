package net.rubicon.event;

import net.rubicon.entity.LivingEntity;

public class ECEntityDead implements IEventComponent {

    public LivingEntity deadEntity;

    public ECEntityDead(LivingEntity deadEntity){
        this.deadEntity = deadEntity;
    }

    @Override
    public String getName() {
        return deadEntity.getName() + " is dead !";
    }
}
