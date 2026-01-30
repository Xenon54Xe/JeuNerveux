package com.example.app.entity;

import com.example.app.DrawOther;
import com.example.app.GameCanvas;
import com.example.app.Manager;
import com.example.app.Updatable;
import com.example.app.entity.group.EntityGroup;
import com.example.app.entity.group.IEntityGroup;
import com.example.app.entity.group.LeaderEntityGroup;
import com.example.app.utils.Vector2D;
import com.example.app.utils.collections.List;
import com.example.app.utils.collections.LinkedList;

import java.awt.*;

public class EntityManager implements Manager, DrawOther, Updatable {

    final GameCanvas gc;

    // CLASS VARIABLES
    // Entity group managment
    private IEntityGroup defaultGroup;
    private final List<IEntityGroup> groups;
    private final List<IEntityGroup> toAddBuffer = new LinkedList<>();
    private final List<IEntityGroup> toRemoveBuffer = new LinkedList<>();

    // Gather empty groups
    private final int gatherEmptyGroupsDelay = 180;
    private int gatherEmptyGroupsCounter = 0;

    public Player player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        groups = new LinkedList<>();
    }

    public void init(){
        // Groups MANAGMENT
        defaultGroup = new EntityGroup(gc);
        groups.add(defaultGroup);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Entity> getAloneEntities() {
        return defaultGroup.getEntities();
    }

    public void addEntity(Entity entity){
        defaultGroup.safeAddEntity(entity);
    }

    public void removeEntity(Entity entity){
        defaultGroup.safeRemoveEntity(entity);
    }

    void addGroup(EntityGroup group){
        assert !groups.contains(group);
        groups.add(group);
    }

    public void safeAddGroup(EntityGroup group){
        toAddBuffer.add(group);
    }

    void removeGroup(EntityGroup group){
        assert groups.remove(group);
    }

    public void safeRemoveGroup(EntityGroup group){
        toRemoveBuffer.add(group);
    }

    public void safeRemoveAllEntities(){
        for (IEntityGroup group : groups){
            group.safeRemoveAllEntities();
        }
    }

    public List<IEntityGroup> getGroups(){
        return groups;
    }

    public void trackFirstFound() {
        for (IEntityGroup group : groups){
            if (group instanceof LeaderEntityGroup leaderEntityGroup){
                LivingEntity master = leaderEntityGroup.getMaster();
                if (master != null){
                    gc.setTracked(master);
                    return;
                }
            }
        }
    }

    public void regenerateAllEntities(){
        for (IEntityGroup group : groups){
            for (Entity entity : group.getEntities()){
                if (entity instanceof LivingEntity livingEntity){
                    livingEntity.setHealth(livingEntity.getMaxHealth());
                }
            }
        }
    }

    public LivingEntity getNearestLivingEntity(Entity entity, Vector2D position){
        if (groups.isEmpty()){
            return null;
        }

        // Search for nearest group
        double lowestDistance = Double.POSITIVE_INFINITY;
        LeaderEntityGroup nearestGroup = null;
        for (IEntityGroup group : groups) {
            if (group.getID() == entity.getGroupID()) {
                continue;
            }

            if (group instanceof LeaderEntityGroup leaderEntityGroup) {
                if (leaderEntityGroup.getMaster() == null) {
                    continue;
                }
                double distance = position.getDistance(leaderEntityGroup.getMaster().getWorldPosition());
                if (distance < lowestDistance) {
                    lowestDistance = distance;
                    nearestGroup = leaderEntityGroup;
                }
            }
        }

        // Search for nearest entity in the nearest group
        LivingEntity nearestEntity = null;
        if (nearestGroup != null) {
            lowestDistance = Double.POSITIVE_INFINITY;
            for (Entity other : nearestGroup.getEntities()) {
                if (other instanceof LivingEntity livingEntity) {
                    double distance = position.getDistance(livingEntity.getWorldPosition());
                    if (distance < lowestDistance) {
                        lowestDistance = distance;
                        nearestEntity = livingEntity;
                    }
                }
            }
        }

        return nearestEntity;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public boolean isShow() {
        return true;
    }

    @Override
    public void setShow(boolean show) {

    }

    @Override
    public void update(){

        if (!isActive()){
            return;
        }

        if(gc.keyH.rClicked && gc.editorMode){
            regenerateAllEntities();
        }

        // Update groups
        if (gc.gameState == GameCanvas.PLAY_STATE) {
            for (IEntityGroup group : groups) {
                group.update();
            }
        }

        // Update ui map
        //gc.uiM.uiMap.setEntitiesPositions(livingEntities);
    }

    public void draw(Graphics2D g2){
        if (isShow()) {
            for (IEntityGroup group : groups) {
                group.draw(g2);
            }
        }
    }
}
