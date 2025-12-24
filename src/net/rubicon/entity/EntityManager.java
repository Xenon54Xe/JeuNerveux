package net.rubicon.entity;

import net.rubicon.main.GameCanvas;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager {

    GameCanvas gc;

    public ITrackable tracked;

    public ArrayList<LivingEntity> livingEntities = new ArrayList<>();
    public LivingEntity player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 100, 2 * gc.tileSize, 20);
        livingEntities.add(player);

        tracked = player;
        // MOUSE

    }

    public void addEntity(LivingEntity entity){
        livingEntities.add(entity);
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
}
