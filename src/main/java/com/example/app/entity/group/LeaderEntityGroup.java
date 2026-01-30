package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.utils.Vector2D;

public class LeaderEntityGroup extends EntityGroup implements LivingEntityGroup{

    // CLASS VARIABLES
    public LivingEntity master;

    public LeaderEntityGroup(GameCanvas gc) {
        super(gc);

        register();
    }

    @Override
    void addEntity(Entity entity) {
        super.addEntity(entity);
        entity.setMoveSpontaneously(false);

        if (master == null){
            setMaster((LivingEntity) entity);
        }
    }

    @Override
    void removeEntity(Entity entity) {
        super.removeEntity(entity);
        entity.setMoveSpontaneously(true);

        if (entity.equals(master)){
            randomChooseMaster();
        }
    }

    public LivingEntity getMaster(){
        return master;
    }

    public void setMaster(LivingEntity master) {
        this.master = master;
        master.setGroupID(id);
        master.setMoveSpontaneously(true);
        if (!contains(master)){
            safeAddEntity(master);
        }
    }

    public void randomChooseMaster(){
        if (entities.isEmpty()){
            master = null;
            return;
        }

        for (Entity entity : entities){
            if (!toRemoveBufferContains(entity) && entity instanceof LivingEntity livingEntity){
                setMaster(livingEntity);
                return;
            }
        }
    }

    public void makeEntitiesMove(Vector2D targetPosition, boolean canTeleporte){
        makeEntitiesMove(targetPosition, canTeleporte, false);
    }

    public void makeEntitiesMove(Vector2D targetPosition, boolean canTeleporte, boolean notTooClose){
        for (int i = 0; i < entities.size(); i++){
            LivingEntity entity = (LivingEntity) entities.get(i);

            if (!entity.equals(master)){
                Vector2D position = entity.getWorldPosition();
                double diffX, diffY;
                diffX = targetPosition.getX() - position.getX();
                diffY = targetPosition.getY() - position.getY();
                double distance = Math.sqrt(diffX * diffX + diffY * diffY);
                
                if (notTooClose && distance < 2 * gc.TILE_SIZE){
                    continue;
                }

                if (canTeleporte && distance > gc.SCREEN_WIDTH){
                    entity.setWorldPosition(targetPosition.getX(), targetPosition.getY());
                }
                else {
                    entity.setMoveDirectionVector(diffX / distance, diffY / distance);
                    entity.move(entities, gc.dt);
                }
            }
        }
    }

    @Override
    void updateEntities() {
        master.update();

        makeEntitiesMove(master.getWorldPosition(), true, false);
        for (Entity entity : entities){
            if (!entity.equals(master)){
                entity.update();
            }
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead deadComponent){
            LivingEntity deadEntity = deadComponent.deadEntity();
            if (contains(deadEntity)){
                safeRemoveEntity(deadEntity);
            }

            if (deadEntity.equals(master)){
                randomChooseMaster();
            }
        }
    }

    @Override
    public void register() {
        gc.eventEntityDead.addListener(this);
    }
}
