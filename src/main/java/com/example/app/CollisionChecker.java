package com.example.app;

import com.example.app.entity.Entity;
import com.example.app.tile.Tile;
import com.example.app.ui.UIDrawVector;
import com.example.app.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    /// Set the movement toward a wall to zero to allow the com.example.app.entity to slide on the wall

    private final GameCanvas gc;

    public final static int TOP_LEFT = 0;
    public final static int TOP_RIGHT = 1;
    public final static int BOTTOM_LEFT = 2;
    public final static int BOTTOM_RIGHT = 3;

    // DEBUG
    UIDrawVector vPos1 = new UIDrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);
    UIDrawVector vPos2 = new UIDrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);
    UIDrawVector vPos3 = new UIDrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);
    UIDrawVector vPos4 = new UIDrawVector(Vector2D.ZERO, Vector2D.ZERO, Color.GREEN);

    public CollisionChecker(GameCanvas gc){
        this.gc = gc;
        
        gc.uiM.addUIObject(vPos1);
        gc.uiM.addUIObject(vPos2);
        gc.uiM.addUIObject(vPos3);
        gc.uiM.addUIObject(vPos4);
    }

    public void checkEntity(Entity entity, ArrayList<Entity> others){
        //Vector2D total = Vector2D.ZERO;
        for (Entity other : others){
            Vector2D diff = entity.getWorldPosition().sub(other.getWorldPosition());
            if (diff.getLength() < gc.tileSize){
                entity.setMoveDirectionVector(entity
                        .getMoveDirectionVector()
                        .absProjectTo(diff.getOrthogonal().getNormalized()).getNormalized());
            }
        }
        //entity.setMoveDirectionVector(entity.getMoveDirectionVector().add(total.getNormalized()).getNormalized());
    }

    private Vector2D[] getNextBoxPositions(Vector2D topLeftCornerPosition, Vector2D moveVector, Rectangle solidArea){
        // Top left, top right, bottom left, bottom right
        Vector2D[] nextPositions = new Vector2D[4];

        Vector2D nextTopLeftCornerPosition = topLeftCornerPosition.add(moveVector);
        nextPositions[TOP_LEFT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x, solidArea.y));
        nextPositions[TOP_RIGHT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y));
        nextPositions[BOTTOM_LEFT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x, solidArea.y + solidArea.height));
        nextPositions[BOTTOM_RIGHT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y + solidArea.height));

        return nextPositions;
    }

    private boolean checkIfTileIsCollision(Vector2D boxPos) {

        int[] tilePos = Vector2D.getTile(gc.tileSize, boxPos);


        for (int layer = 0; layer < gc.tileM.getLayerCount(); layer++) {
            // Test if on the edge of the world
            if (tilePos[0] < 0 || tilePos[0] >= gc.tileM.getMaxWorldCol()
                    || tilePos[1] < 0 || tilePos[1] >= gc.tileM.getMaxWorldRow())
            {
                return true;
            }
            else {

                // Test if the entity is heading toward a collision tile
                int tileNum = gc.tileM.tileMap.getTileNum(tilePos[0], tilePos[1], layer);
                Tile tile = gc.tileM.tiles.getTile(tileNum);

                if (tile.isCollision()){
                    return true;
                }
            }
        }

        return false;
    }

    public void checkTile(Entity entity){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<String> directions = entity.getMoveDirectionVector().getDirections();

        if (directions == null){
            return;
        }

        // GET POSITIONS OF SOLID BOX IN WORLD
        Vector2D[] nextBoxPositions = getNextBoxPositions(entity.getWorldTopLeftPosition(), entity.getNextMoveVector(gc.dt), entity.getSolidArea());

        // Check if after the movement the box will collide with a wall
        for (String direction : directions){

            Vector2D boxPos1 = Vector2D.ZERO, boxPos2 = Vector2D.ZERO;
            if (direction.equals(Vector2D.S_UP)){

                boxPos1 = nextBoxPositions[TOP_LEFT];
                boxPos2 = nextBoxPositions[TOP_RIGHT];
            } else if (direction.equals(Vector2D.S_DOWN)) {

                boxPos1 = nextBoxPositions[BOTTOM_LEFT];
                boxPos2 = nextBoxPositions[BOTTOM_RIGHT];
            } else if (direction.equals(Vector2D.S_LEFT)) {

                boxPos1 = nextBoxPositions[TOP_LEFT];
                boxPos2 = nextBoxPositions[BOTTOM_LEFT];
            }else if (direction.equals(Vector2D.S_RIGHT)){

                boxPos1 = nextBoxPositions[TOP_RIGHT];
                boxPos2 = nextBoxPositions[BOTTOM_RIGHT];
            }

            if (checkIfTileIsCollision(boxPos1) || checkIfTileIsCollision(boxPos2)){
                entity.setOnCollision(true);
                return;
            }
        }

        //        if (collision){
//            if (entity.isAvoidWall()){
//                entity.setMoveDirectionVector(moveVector.add(targetDirection.mul(-2)).getNormalized());
//            }
//            else {
//                entity.setMoveDirectionVector(moveVector.absProjectTo(orthogonal).getNormalized());
//            }
//        }
    }


    private void debugRays(Vector2D boxPos1, Vector2D boxPos2, Vector2D targetDirection, Vector2D boxPos1AfterMovement, Vector2D boxPos2AfterMovement) {
        if (targetDirection.equals(Vector2D.UP) || targetDirection.equals(Vector2D.DOWN)) {
            vPos1.setScreenPosition(boxPos1.sub(gc.tracked.getCameraWorldPosition()));
            vPos1.setVector2D(boxPos1AfterMovement.sub(boxPos1).mul(100));
            vPos2.setScreenPosition(boxPos2.sub(gc.tracked.getCameraWorldPosition()));
            vPos2.setVector2D(boxPos2AfterMovement.sub(boxPos2).mul(100));
        } else if (targetDirection.equals(Vector2D.LEFT) || targetDirection.equals(Vector2D.RIGHT)) {
            vPos3.setScreenPosition(boxPos1.sub(gc.tracked.getCameraWorldPosition()));
            vPos3.setVector2D(boxPos1AfterMovement.sub(boxPos1).mul(100));
            vPos4.setScreenPosition(boxPos2.sub(gc.tracked.getCameraWorldPosition()));
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
                        tileNum1 = gc.tileM.tileMap.getTileNum(entityLeftCol, entityTopRow, layer);
                        tileNum2 = gc.tileM.tileMap.getTileNum(entityRightCol, entityTopRow, layer);
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
                        tileNum1 = gc.tileM.tileMap.getTileNum(entityLeftCol, entityBottomRow, layer);
                        tileNum2 = gc.tileM.tileMap.getTileNum(entityRightCol, entityBottomRow, layer);
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
                        tileNum1 = gc.tileM.tileMap.getTileNum(entityLeftCol, entityTopRow, layer);
                        tileNum2 = gc.tileM.tileMap.getTileNum(entityLeftCol, entityBottomRow, layer);
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
                        tileNum1 = gc.tileM.tileMap.getTileNum(entityRightCol, entityTopRow, layer);
                        tileNum2 = gc.tileM.tileMap.getTileNum(entityRightCol, entityBottomRow, layer);
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
