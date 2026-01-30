package com.example.app.entity.group;

import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;
import com.example.app.event.Listener;

public interface LivingEntityGroup extends IEntityGroup, Listener {

    default LivingEntity getWeakest(){
        LivingEntity weekest = null;

        for (Entity entity : getEntities()){
            if (entity instanceof LivingEntity livingEntity) {

                if (weekest == null || livingEntity.getHealth() < weekest.getHealth()) {
                    weekest = livingEntity;
                }
            }
        }

        return weekest;
    }
}
