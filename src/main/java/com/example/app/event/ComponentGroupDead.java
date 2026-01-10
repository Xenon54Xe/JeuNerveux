package com.example.app.event;

import com.example.app.entity.EntityGroup;

public record ComponentGroupDead(EntityGroup group) implements IEventComponent{

    public ComponentGroupDead(EntityGroup group){
        this.group = group;
    }

    @Override
    public String getName() {
        return "Group: " + group.ID;
    }
}
