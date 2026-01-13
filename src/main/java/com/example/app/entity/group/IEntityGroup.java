package com.example.app.entity.group;

import com.example.app.entity.LivingEntity;

public interface IEntityGroup {
    
    void addEntity(LivingEntity entity);
    
    boolean removeEntity(LivingEntity entity);
    
    boolean contains(LivingEntity entity);

    void killGroup();

    void update();
}
