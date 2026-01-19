package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.LivingEntity;
import com.example.app.entity.Player;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.utils.Vector2D;

public class PlayerEntityGroup extends EntityGroup{

    // CLASS VARIABLES
    private final int enemyFindInterval = 20;
    private int enemyFindCount = 0;
    private LivingEntity foundEnemy = null;

    public PlayerEntityGroup(GameCanvas gc, LivingEntity player) {
        super(gc);

        setMaster(player);
    }

    @Override
    public void update() {
        super.update();

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

        if (gc.sceneryM.groups.isEmpty()){
            return;
        }

        // Find new enemy
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < gc.sceneryM.groups.size(); i++) {

            LivingEntity entity = gc.sceneryM.groups.getFirstValueNShift().getMaster();
            if (entity == null){
                continue;
            }

            double distance = entity.getWorldPosition().getDistance(getMaster().getWorldPosition());
            if (!(entity.getGroupID() == id) && distance < gc.tileSize * 4){

                if (foundEnemy == null || distance < minDistance) {
                    minDistance = distance;
                    foundEnemy = entity;
                }
            }
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity killed, LivingEntity killer)){
            if (killed == getMaster()){
                killGroup();
            }
        }
    }
}
