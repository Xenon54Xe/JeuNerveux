package com.example.app.entity.group;

import com.example.app.DrawOther;
import com.example.app.Updatable;
import com.example.app.entity.Entity;
import com.example.app.utils.collections.List;

public interface IEntityGroup extends Updatable, DrawOther {

    int getID();

    void safeAddEntity(Entity entity);

    void safeRemoveEntity(Entity entity);

    void safeRemoveAllEntities();

    boolean contains(Entity entity);

    int size();

    default boolean isEmpty(){
        return size() == 0;
    }

    List<Entity> getEntities();
}
