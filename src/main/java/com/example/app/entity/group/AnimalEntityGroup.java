package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.LivingEntity;
import com.example.app.utils.Vector2D;

public class AnimalEntityGroup extends LeaderEntityGroup{

    private int changeTargetCounter = 0;

    public AnimalEntityGroup(GameCanvas gc) {
        super(gc);
    }

    @Override
    public void update() {
        if (isActive()) {
            super.update();

            if (!entities.isEmpty()) {
                if (getMaster() != null) {
                    if (changeTargetCounter <= 0) {
                        changeTargetCounter = 4 * 60;
                        Vector2D targetPosition = Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles);
                        double diffX = targetPosition.getX() - getMaster().getWorldPosition().getX();
                        double diffY = targetPosition.getY() - getMaster().getWorldPosition().getY();
                        double length = Math.sqrt(diffX * diffX + diffY * diffY);
                        getMaster().setMoveDirectionVector(diffX / length, diffY / length);
                    }
                    changeTargetCounter--;

                    makeEntitiesMove(getMaster().getWorldPosition(), false);
                }
            }
        }
    }
}
