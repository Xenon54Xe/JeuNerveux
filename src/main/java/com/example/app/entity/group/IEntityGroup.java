package com.example.app.entity.group;

import com.example.app.entity.LivingEntity;

public interface IEntityGroup {

    int getID();

    LivingEntity getMaster();

    void setMaster(LivingEntity entity);

    void addEntity(LivingEntity entity);
    
    void safeRemoveEntity(LivingEntity entity);
    
    boolean contains(LivingEntity entity);

    int size();

    boolean isEmpty();

    void killGroup();

    void update();
}
