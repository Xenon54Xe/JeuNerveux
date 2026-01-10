package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.utils.Vector2D;

public class EnemyEntityGroup extends EntityGroup{

    private int changeTargetCounter = 0;
    private Vector2D targetPosition = Vector2D.ZERO;

    public EnemyEntityGroup(GameCanvas gc) {
        super(gc);
    }

    @Override
    public void update() {
        if (changeTargetCounter <= 0){
            changeTargetCounter = 4 * 60;
            targetPosition = Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles);
        }
        changeTargetCounter--;

        makeEntitiesMove(gc, targetPosition);
    }
}
