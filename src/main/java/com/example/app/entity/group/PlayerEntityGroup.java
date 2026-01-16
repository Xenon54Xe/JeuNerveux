package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.LivingEntity;
import com.example.app.entity.Player;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.utils.Vector2D;

public class PlayerEntityGroup extends EntityGroup{

    // CLASS VARIABLES
    private final Player player;
    private final int enemyFindInterval = 20;
    private int enemyFindCount = 0;
    private LivingEntity foundEnemy = null;

    public PlayerEntityGroup(GameCanvas gc, Player player) {
        super(gc);

        this.player = player;
    }


    @Override
    public void update() {
        // Look for nearby enemies
        enemyFindCount--;
        if (enemyFindCount <= 0){

            enemyFindCount = enemyFindInterval;
            findNewEnemy();
        }

        if (foundEnemy != null){
            makeEntitiesMove(foundEnemy.getWorldPosition(), false);
        }else {
            makeEntitiesMove(player.getWorldPosition(), true);
        }
    }

    private void findNewEnemy() {
        // Find new enemy
        for (int i = 0; i < gc.entityM.livingEntities.size(); i++) {
            LivingEntity entity = gc.entityM.livingEntities.getFirstValueNShift();
            if (!(entity.getGroupID() == id) &&
                    entity.getWorldPosition().getDistance(player.getWorldPosition()) < player.getReach() * 2){
                foundEnemy = entity;
                return;
            }
        }
        foundEnemy = null;
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity killed, LivingEntity killer)){
            if (killed == player){
                killGroup();
            }
        }
    }
}
