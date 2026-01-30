package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.event.component.ComponentEntityLeftGroup;
import com.example.app.utils.collections.List;
import com.example.app.utils.collections.LinkedList;

import java.awt.*;

public class EntityGroup implements IEntityGroup {

    final GameCanvas gc;

    // STATIC
    public static final int NULL_GROUP_ID = -1;
    private static int NEXT_ID = 0;
    public final int id;

    // CLASS VARIABLES
    public final List<Entity> entities = new LinkedList<>();
    private final List<Entity> toAddBuffer = new LinkedList<>();
    private final List<Entity> toRemoveBuffer = new LinkedList<>();
    private boolean show = true;
    private boolean active = true;

    public EntityGroup(GameCanvas gc){
        id = NEXT_ID++;

        this.gc = gc;

        gc.entityM.safeAddGroup(this);
    }

    public int getID() {
        return id;
    }

    void addEntity(Entity entity){
        assert entity.getGroupID() == -1 : "Entity already belongs to a group";

        entity.setGroupID(id);
        entities.add(entity);
    }

    public void safeAddEntity(Entity entity){
        toAddBuffer.add(entity);
    }

    void removeEntity(Entity entity){
        assert entities.remove(entity);

        entity.setGroupID(-1);
        gc.eventEntityLeavedGroup.trigger(new ComponentEntityLeftGroup(entity));
    }

    public void safeRemoveEntity(Entity entity){
        toRemoveBuffer.add(entity);
    }

    public void safeRemoveAllEntities(){
        for(Entity entity : entities){
            if (!toRemoveBuffer.contains(entity)){
                toRemoveBuffer.add(entity);
            }
        }
    }

    public boolean contains(Entity entity){
        return entities.contains(entity);
    }

    public boolean toRemoveBufferContains(Entity entity){
        return toRemoveBuffer.contains(entity);
    }

    public int size(){
        return entities.size();
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update() {
        if (isActive()) {
            // Update buffers
            updateBuffers();

            // Update entities
            updateEntities();
        }
    }

    void updateBuffers(){
        // Remove entities
        if (!toRemoveBuffer.isEmpty()){
            for (Entity entity : toRemoveBuffer){
                removeEntity(entity);
            }
            toRemoveBuffer.clear();
        }

        // Add entities
        if (!toAddBuffer.isEmpty()) {
            for (Entity entity : toAddBuffer) {
                addEntity(entity);
            }
            toAddBuffer.clear();
        }
    }

    void updateEntities(){
        for (Entity entity : entities){
            entity.update();
        }
    }

    @Override
    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public boolean isShow() {
        return show;
    }

    @Override
    public void draw(Graphics2D g2){
        if (isShow()) {
            for (Entity entity : entities) {
                entity.draw(g2);
            }
        }
    }
}
