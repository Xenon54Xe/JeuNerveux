package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.utils.Vector2D;

public class AnimalEntityGroup extends EntityGroup{

    private int changeTargetCounter = 0;
    private Vector2D targetPosition = Vector2D.ZERO;

    public AnimalEntityGroup(GameCanvas gc) {
        super(gc);
    }

    @Override
    public void update() {
        if (changeTargetCounter <= 0){
            changeTargetCounter = 4 * 60;
            targetPosition = Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles);
        }
        changeTargetCounter--;

        makeEntitiesMove(targetPosition, false);
    }
}
