package net.rubicon.entity;


import java.util.ArrayList;

public interface IAttackEntity {

    void attack();

    default void attackNearestEntity(LivingEntity self, ArrayList<LivingEntity> entities, int reach, int damage) {

        double lowestDistance = Double.POSITIVE_INFINITY;
        LivingEntity nearestEntity = entities.getFirst();

        for (LivingEntity entity : entities) {

            if (entity == self){
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

        for (LivingEntity entity : entities) {

            if (entity == self){
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
