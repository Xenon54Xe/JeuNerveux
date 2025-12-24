package net.rubicon.entity;

import net.rubicon.event.ECEntityDead;
import net.rubicon.event.IListener;
import net.rubicon.main.GameCanvas;
import net.rubicon.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager implements IListener<ECEntityDead> {

    final GameCanvas gc;

    public ITrackable tracked;

    public ArrayList<LivingEntity> livingEntities = new ArrayList<>();
    public LivingEntity player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 100, 2 * gc.tileSize, 20);
        addEntity(player);

        // MOUSE
        Mouse mouse = new Mouse(gc, playerSolidArea, "mouse", 50, gc.tileSize, gc.tileSize, 25);
        mouse.setWorldPosition(new Vector2D(gc.tileSize * 11, gc.tileSize * 12));
        addEntity(mouse);

        // TRACKED
        setTracked(player);

        // EVENT
        gc.eventEntityDead.addListener(this);
    }

    public void setTracked(ITrackable tracked){
        this.tracked = tracked;
    }

    public void addEntity(LivingEntity entity){
        livingEntities.add(entity);
    }

    public void removeEntity(LivingEntity entity){
        livingEntities.remove(entity);
    }

    public void update(double dt){
        for (LivingEntity entity : livingEntities){
            entity.update(dt);
        }
    }

    public void draw(Graphics2D g2){
        for (LivingEntity entity : livingEntities){
            entity.draw(g2);
        }
    }

    @Override
    public void onTrigger(ECEntityDead component) {
        LivingEntity deadEntity = component.deadEntity;

        removeEntity(deadEntity);
    }
}
