package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;
import com.example.app.utils.Vector2D;

public class AnimalEntityGroup extends EntityGroup{

    private int changeTargetCounter = 0;

    public AnimalEntityGroup(GameCanvas gc) {
        super(gc);
    }

    @Override
    public void update() {
        super.update();

        if (getMaster() != null) {
            if (changeTargetCounter <= 0) {
                changeTargetCounter = 4 * 60;
                Vector2D targetPosition = Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles);
                Vector2D targetDirection = targetPosition.sub(getMaster().getWorldPosition()).getNormalized();
                getMaster().setMoveDirectionVector(targetDirection);
            }
            changeTargetCounter--;

            makeEntitiesMove(getMaster().getWorldPosition(), false);
        }
    }
}
