package com.example.app.entity;

import com.example.app.utils.ILoopList;

public interface IAttackEntity {

    void attack();

    default boolean attackNearestEntity(LivingEntity self, ILoopList<LivingEntity> entities, int reach, int damage) {

        if (self.isDead()){
            return false;
        }

        double lowestDistance = Double.POSITIVE_INFINITY;
        LivingEntity nearestEntity = entities.get();

        for (int i = 0; i < entities.size(); i++) {
            LivingEntity entity = entities.get(true);
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
            return true;
        }
        return false;
    }


    default boolean attackFirstNearEnoughEntity(LivingEntity self, ILoopList<LivingEntity> entities, int reach, int damage) {

        if (self.isDead()){
            return false;
        }

        for (int i = 0; i < entities.size(); i++) {
            LivingEntity entity = entities.get(true);
            if (self == entity
                    || (self.getGroupID() != -1 && entity.getGroupID() == self.getGroupID())){
                // Skip this entity
                continue;
            }

            double distance = self.getWorldPosition().getDistance(entity.getWorldPosition());
            if (distance < reach) {
                entity.damage(damage, self);
                return true;
            }
        }
        return false;
    }
}
