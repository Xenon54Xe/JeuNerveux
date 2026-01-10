package com.example.app.entity;

public interface IEntityGroup {
    
    void addEntity(LivingEntity entity);
    
    boolean removeEntity(LivingEntity entity);
    
    boolean contains(LivingEntity entity);

    void killGroup();

    void update();
}
