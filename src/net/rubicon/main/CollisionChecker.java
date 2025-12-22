package net.rubicon.main;

import net.rubicon.entity.Entity;
import net.rubicon.utils.Vector2D;

import java.util.ArrayList;

public class CollisionChecker {

    private final GameCanvas gc;

    public CollisionChecker(GameCanvas gc){
        this.gc = gc;
    }

    public void checkTile(Entity entity){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<String> directions = entity.getMoveVector().getDirections();

        if (directions == null){
            return;
        }

        // GET POSITIONS OF SOLID BOX IN WORLD
        int entityLeftWorldX = (int)(entity.getWorldX() + entity.solidArea.x);
        int entityRightWorldX = (int)(entity.getWorldX() + entity.solidArea.x + entity.solidArea.width);
        int entityTopWorldY = (int)(entity.getWorldY() + entity.solidArea.y);
        int entityBottomWorldY = (int)(entity.getWorldY() + entity.solidArea.y + entity.solidArea.height);

        // GET TILE POSITIONS OF SOLID BOX
        int tileNum1;
        int tileNum2;

        for (String direction : directions){
            int layer = 0;
            Vector2D moveVector = entity.getMoveVector();
            int entityLeftCol = entityLeftWorldX / gc.tileSize;
            int entityRightCol = entityRightWorldX / gc.tileSize;
            int entityTopRow = entityTopWorldY / gc.tileSize;
            int entityBottomRow = entityBottomWorldY / gc.tileSize;

            // CHECK FOR EVERY DIRECTION
            switch (direction){
                case "up":
                    entityTopRow = (int)((entityTopWorldY - entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.layerCount){
                        tileNum1 = gc.tileM.tileMapNum[entityLeftCol][entityTopRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityRightCol][entityTopRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).collision || gc.tileM.tiles.getTile(tileNum2).collision){
                            entity.setMoveVector(moveVector.mask(Vector2D.RIGHT));
                            layer = gc.layerCount;
                        }
                        layer++;
                    }
                    break;
                case "down":
                    entityBottomRow = (int)((entityBottomWorldY + entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.layerCount){
                        tileNum1 = gc.tileM.tileMapNum[entityLeftCol][entityBottomRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityRightCol][entityBottomRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).collision || gc.tileM.tiles.getTile(tileNum2).collision){
                            entity.setMoveVector(moveVector.mask(Vector2D.RIGHT));
                            layer = gc.layerCount;
                        }
                        layer++;
                    }
                    break;
                case "left":
                    entityLeftCol = (int)((entityLeftWorldX - entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.layerCount){
                        tileNum1 = gc.tileM.tileMapNum[entityLeftCol][entityTopRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityLeftCol][entityBottomRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).collision || gc.tileM.tiles.getTile(tileNum2).collision){
                            entity.setMoveVector(moveVector.mask(Vector2D.DOWN));
                            layer = gc.layerCount;
                        }
                        layer++;
                    }
                    break;
                case "right":
                    entityRightCol = (int)((entityRightWorldX + entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.layerCount){
                        tileNum1 = gc.tileM.tileMapNum[entityRightCol][entityTopRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityRightCol][entityBottomRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).collision || gc.tileM.tiles.getTile(tileNum2).collision){
                            entity.setMoveVector(moveVector.mask(Vector2D.DOWN));
                            layer = gc.layerCount;
                        }
                        layer++;
                    }
                    break;
            }
        }
    }
}
