package com.example.app.entity;

import java.util.ArrayList;

public interface IAttackEntity {

    void attack();

    default void attackNearestEntity(LivingEntity self, ArrayList<LivingEntity> entities, int reach, int damage) {

        if (self.isDead()){
            return;
        }

        double lowestDistance = Double.POSITIVE_INFINITY;
        LivingEntity nearestEntity = entities.getFirst();

        for (LivingEntity entity : entities) {

            if (self == entity
                    || (self.getGroupID() != -1 && entity.getGroupID() == self.getGroupID())){
                // Skip this entity
                continue;
            }

            double distance = self.getWorldPosition().getDistance(entity.getWorldPosition());
            if (distance < lowestDistance) {
                lowestDistance = distance;
                nearestEntity = entity;
            }
        }

        if (lowestDistance <= reach) {
            nearestEntity.damage(damage, self);
        }
    }


    default void attackFirstNearEnoughEntity(LivingEntity self, ArrayList<LivingEntity> entities, int reach, int damage) {

        if (self.isDead()){
            return;
        }

        for (LivingEntity entity : entities) {

            if (self == entity
                    || (self.getGroupID() != -1 && entity.getGroupID() == self.getGroupID())){
                // Skip this entity
                continue;
            }

            double distance = self.getWorldPosition().getDistance(entity.getWorldPosition());
            if (distance < reach) {
                entity.damage(damage, self);
                return;
            }
        }
    }
}
