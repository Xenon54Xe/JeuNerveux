package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;

public class PlayerEntityGroup extends LeaderEntityGroup{

    // CLASS VARIABLES
    private final int enemyFindInterval = 10;
    private int enemyFindCount = 0;
    private LivingEntity foundEnemy = null;

    public PlayerEntityGroup(GameCanvas gc, LivingEntity player) {
        super(gc);

        addEntity(player);
    }

    @Override
    public void update() {
        // Update buffers
        updateBuffers();

        // Look for nearby enemies
        enemyFindCount--;
        if (enemyFindCount <= 0){

            enemyFindCount = enemyFindInterval;
            findNewEnemy();
        }
        if (foundEnemy != null){
            makeEntitiesMove(foundEnemy.getWorldPosition(), false);
        }else {
            makeEntitiesMove(getMaster().getWorldPosition(), true, true);
        }
    }

    private void findNewEnemy() {
        foundEnemy = null;

        // Find new enemy
        double minDistance = Double.POSITIVE_INFINITY;
        // Find the nearest group
        IEntityGroup nearestGroup = null;
        for (IEntityGroup group : gc.entityM.getGroups()) {
            if (group instanceof LeaderEntityGroup leaderEntityGroup){
                if (group.getID() == id){
                    continue;
                }

                LivingEntity master = leaderEntityGroup.getMaster();
                if (master == null){
                    continue;
                }

                double distance = master.getWorldPosition().getDistance(getMaster().getWorldPosition());
                if (distance < minDistance){

                    minDistance = distance;
                    nearestGroup = group;
                }
            }
        }
        // Find the nearest entity in the nearest group
        if (nearestGroup != null) {
            for (Entity entity : nearestGroup.getEntities()) {
                if (entity instanceof LivingEntity livingEntity) {
                    double distance = livingEntity.getWorldPosition().getDistance(getMaster().getWorldPosition());
                    if (distance < gc.TILE_SIZE * 4 && distance < minDistance) {

                        minDistance = distance;
                        foundEnemy = livingEntity;
                    }
                }
            }
        }
    }
}
