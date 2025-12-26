package net.rubicon.main;

import net.rubicon.ui.DrawVector;
import net.rubicon.entity.Entity;
import net.rubicon.tile.Tile;
import net.rubicon.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    /// Set the movement toward a wall to zero to allow the entity to slide on the wall

    private final GameCanvas gc;

    // DEBUG
    DrawVector vPos1 = new DrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);
    DrawVector vPos2 = new DrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);
    DrawVector vPos3 = new DrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);
    DrawVector vPos4 = new DrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);

    public CollisionChecker(GameCanvas gc){
        this.gc = gc;
        
        gc.uiM.addUIObject(vPos1);
        gc.uiM.addUIObject(vPos2);
        gc.uiM.addUIObject(vPos3);
        gc.uiM.addUIObject(vPos4);
    }

    public void checkTile(Entity entity){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<String> directions = entity.getMoveDirectionVector().getDirections();

        if (directions == null){
            return;
        }

        // GET POSITIONS OF SOLID BOX IN WORLD
        int entityBoxLeftWorldX = (int)(entity.getWorldX() - entity.getWidth() / 2.0 + entity.getSolidArea().x);
        int entityBoxRightWorldX = (int)(entity.getWorldX() - entity.getWidth() / 2.0 + entity.getSolidArea().x + entity.getSolidArea().width);
        int entityBoxTopWorldY = (int)(entity.getWorldY() - entity.getHeight() / 2.0 + entity.getSolidArea().y);
        int entityBoxBottomWorldY = (int)(entity.getWorldY() - entity.getHeight() / 2.0 + entity.getSolidArea().y + entity.getSolidArea().height);


        // Check if after the movement the box will collide with a wall
        for (String direction : directions){

            Vector2D moveVector = entity.getMoveDirectionVector();
            if (direction.equals(Vector2D.S_UP)){

                Vector2D boxPos1 = new Vector2D(entityBoxLeftWorldX, entityBoxTopWorldY);
                Vector2D boxPos2 = new Vector2D(entityBoxRightWorldX, entityBoxTopWorldY);
                checkIfCollision(entity, boxPos1, boxPos2, moveVector, Vector2D.UP);

            } else if (direction.equals(Vector2D.S_DOWN)) {

                Vector2D boxPos1 = new Vector2D(entityBoxLeftWorldX, entityBoxBottomWorldY);
                Vector2D boxPos2 = new Vector2D(entityBoxRightWorldX, entityBoxBottomWorldY);
                checkIfCollision(entity, boxPos1, boxPos2, moveVector, Vector2D.DOWN);

            } else if (direction.equals(Vector2D.S_LEFT)) {

                Vector2D boxPos1 = new Vector2D(entityBoxLeftWorldX, entityBoxTopWorldY);
                Vector2D boxPos2 = new Vector2D(entityBoxLeftWorldX, entityBoxBottomWorldY);
                checkIfCollision(entity, boxPos1, boxPos2, moveVector, Vector2D.LEFT);

            }else if (direction.equals(Vector2D.S_RIGHT)){

                Vector2D boxPos1 = new Vector2D(entityBoxRightWorldX, entityBoxTopWorldY);
                Vector2D boxPos2 = new Vector2D(entityBoxRightWorldX, entityBoxBottomWorldY);
                checkIfCollision(entity, boxPos1, boxPos2, moveVector, Vector2D.RIGHT);
            }
        }
    }


    private void checkIfCollision(Entity entity, Vector2D boxPos1, Vector2D boxPos2, Vector2D moveVector, Vector2D targetDirection) {
        Vector2D boxPos1AfterMovement = boxPos1.add(moveVector.absMask(targetDirection).mul(entity.getSpeed() * gc.dt * 1.1));
        Vector2D boxPos2AfterMovement = boxPos2.add(moveVector.absMask(targetDirection).mul(entity.getSpeed() * gc.dt * 1.1));
        Vector2D orthogonal = new Vector2D(targetDirection.getY(), - targetDirection.getX()).getNormalized();

        // debugRays(boxPos1, boxPos2, targetDirection, boxPos1AfterMovement, boxPos2AfterMovement);

        int[] tile1TilePos = Vector2D.getTile(gc.tileSize, boxPos1AfterMovement);
        int[] tile2TilePos = Vector2D.getTile(gc.tileSize, boxPos2AfterMovement);


        boolean collision = false;
        for (int layer = 0; layer < gc.tileM.getLayerCount(); layer++) {
            // Test if on the edge of the world
            if (tile1TilePos[0] < 0 || tile1TilePos[0] >= gc.tileM.getMaxWorldCol()
                    || tile1TilePos[1] < 0 || tile1TilePos[1] >= gc.tileM.getMaxWorldRow()
                    || tile2TilePos[0] < 0 || tile2TilePos[0] >= gc.tileM.getMaxWorldCol()
                    || tile2TilePos[1] < 0 || tile2TilePos[1] >= gc.tileM.getMaxWorldRow() )
            {
                collision = true;
            }
            else {

                // Test if the entity is heading toward a collision tile
                int tileNum1 = gc.tileM.tileMapNum[tile1TilePos[0]][tile1TilePos[1]][layer];
                int tileNum2 = gc.tileM.tileMapNum[tile2TilePos[0]][tile2TilePos[1]][layer];
                Tile tile1 = gc.tileM.tiles.getTile(tileNum1);
                Tile tile2 = gc.tileM.tiles.getTile(tileNum2);

                if(tile1.isCollision() || tile2.isCollision()){
                    collision = true;
                }
            }
        }

        if (collision){
            if (entity.isAvoidWall()){
                entity.setMoveDirectionVector(moveVector.add(targetDirection.mul(-2)).getNormalized());
            }
            else {
                entity.setMoveDirectionVector(moveVector.absMask(orthogonal).getNormalized());
            }
        }
    }

    private void debugRays(Vector2D boxPos1, Vector2D boxPos2, Vector2D targetDirection, Vector2D boxPos1AfterMovement, Vector2D boxPos2AfterMovement) {
        if (targetDirection.equals(Vector2D.UP) || targetDirection.equals(Vector2D.DOWN)) {
            vPos1.setScreenPosition(Vector2D.getScreenPosition(gc.tracked.getCameraWorldPosition(), boxPos1));
            vPos1.setVector2D(boxPos1AfterMovement.sub(boxPos1).mul(100));
            vPos2.setScreenPosition(Vector2D.getScreenPosition(gc.tracked.getCameraWorldPosition(), boxPos2));
            vPos2.setVector2D(boxPos2AfterMovement.sub(boxPos2).mul(100));
        } else if (targetDirection.equals(Vector2D.LEFT) || targetDirection.equals(Vector2D.RIGHT)) {
            vPos3.setScreenPosition(Vector2D.getScreenPosition(gc.tracked.getCameraWorldPosition(), boxPos1));
            vPos3.setVector2D(boxPos1AfterMovement.sub(boxPos1).mul(100));
            vPos4.setScreenPosition(Vector2D.getScreenPosition(gc.tracked.getCameraWorldPosition(), boxPos2));
            vPos4.setVector2D(boxPos2AfterMovement.sub(boxPos2).mul(100));
        }
    }

    @Deprecated
    public void checkTile2(Entity entity){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<String> directions = entity.getMoveDirectionVector().getDirections();

        if (directions == null){
            return;
        }

        // GET POSITIONS OF SOLID BOX IN WORLD
        int entityLeftWorldX = (int)(entity.getWorldX() - entity.getWidth() / 2.0 + entity.getSolidArea().x);
        int entityRightWorldX = (int)(entity.getWorldX() - entity.getWidth() / 2.0 + entity.getSolidArea().x + entity.getSolidArea().width);
        int entityTopWorldY = (int)(entity.getWorldY() - entity.getHeight() / 2.0 + entity.getSolidArea().y);
        int entityBottomWorldY = (int)(entity.getWorldY() - entity.getHeight() / 2.0 + entity.getSolidArea().y + entity.getSolidArea().height);

        // GET TILE POSITIONS OF SOLID BOX
        int tileNum1;
        int tileNum2;

        for (String direction : directions){
            int layer = 0;
            Vector2D moveVector = entity.getMoveDirectionVector();
            int entityLeftCol = entityLeftWorldX / gc.tileSize;
            int entityRightCol = entityRightWorldX / gc.tileSize;
            int entityTopRow = entityTopWorldY / gc.tileSize;
            int entityBottomRow = entityBottomWorldY / gc.tileSize;

            // CHECK FOR EVERY DIRECTION
            switch (direction){
                case "up":
                    entityTopRow = (int)((entityTopWorldY - entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.tileM.getLayerCount()){
                        tileNum1 = gc.tileM.tileMapNum[entityLeftCol][entityTopRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityRightCol][entityTopRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).isCollision() || gc.tileM.tiles.getTile(tileNum2).isCollision()){
                            entity.setMoveDirectionVector(moveVector.mask(Vector2D.RIGHT));
                            layer = gc.tileM.getLayerCount();
                        }
                        layer++;
                    }
                    break;
                case "down":
                    entityBottomRow = (int)((entityBottomWorldY + entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.tileM.getLayerCount()){
                        tileNum1 = gc.tileM.tileMapNum[entityLeftCol][entityBottomRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityRightCol][entityBottomRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).isCollision() || gc.tileM.tiles.getTile(tileNum2).isCollision()){
                            entity.setMoveDirectionVector(moveVector.mask(Vector2D.RIGHT));
                            layer = gc.tileM.getLayerCount();
                        }
                        layer++;
                    }
                    break;
                case "left":
                    entityLeftCol = (int)((entityLeftWorldX - entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.tileM.getLayerCount()){
                        tileNum1 = gc.tileM.tileMapNum[entityLeftCol][entityTopRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityLeftCol][entityBottomRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).isCollision() || gc.tileM.tiles.getTile(tileNum2).isCollision()){
                            entity.setMoveDirectionVector(moveVector.mask(Vector2D.DOWN));
                            layer = gc.tileM.getLayerCount();
                        }
                        layer++;
                    }
                    break;
                case "right":
                    entityRightCol = (int)((entityRightWorldX + entity.getSpeed() * gc.dt) / gc.tileSize);

                    while (layer < gc.tileM.getLayerCount()){
                        tileNum1 = gc.tileM.tileMapNum[entityRightCol][entityTopRow][layer];
                        tileNum2 = gc.tileM.tileMapNum[entityRightCol][entityBottomRow][layer];
                        if (gc.tileM.tiles.getTile(tileNum1).isCollision() || gc.tileM.tiles.getTile(tileNum2).isCollision()){
                            entity.setMoveDirectionVector(moveVector.mask(Vector2D.DOWN));
                            layer = gc.tileM.getLayerCount();
                        }
                        layer++;
                    }
                    break;
            }
        }
    }
}
