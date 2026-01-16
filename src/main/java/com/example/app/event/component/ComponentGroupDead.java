package com.example.app.event.component;

import com.example.app.entity.group.EntityGroup;

public record ComponentGroupDead(EntityGroup group) implements IEventComponent{

    public ComponentGroupDead(EntityGroup group){
        this.group = group;
    }

    @Override
    public String getName() {
        return "Group: " + group.id;
    }
}
