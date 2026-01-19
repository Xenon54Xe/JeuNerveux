package com.example.app;

import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;
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

    public Vector2D checkEntity(ArrayList<Entity> others, Entity entity, Vector2D moveVector){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<String> directions = moveVector.getDirections();

        if (directions == null){
            return Vector2D.ZERO;
        }

        Vector2D correctedMoveVector = moveVector.copy();
        // Check if after the movement the box will collide with a wall
        for (String direction : directions){

            for (Entity other : others) {
                correctedMoveVector = correctVectorWithEntity(entity, other, correctedMoveVector, direction);
            }
        }

        return correctedMoveVector;
    }
    
    private Vector2D correctVectorWithEntity(Entity entity, Entity other, Vector2D moveVector, String direction){
        Vector2D boxPos1 = null, boxPos2 = null;
        Vector2D[] boxPos = getBoxPos(entity, moveVector, direction);
        boxPos1 = boxPos[0];
        boxPos2 = boxPos[1];

        if (Vector2D.isPointInArea(boxPos1, other.getWorldTopLeftPosition(), other.getSolidArea())
                || Vector2D.isPointInArea(boxPos2, other.getWorldTopLeftPosition(), other.getSolidArea())){
            return moveVector.sub(moveVector.projectTo(Vector2D.getRelatedVector(direction)));
        }
        return moveVector;
    }

    public static Vector2D[] getNextBoxPositions(Vector2D topLeftCornerPosition, Vector2D moveVector, Rectangle solidArea){
        // Top left, top right, bottom left, bottom right
        Vector2D[] nextPositions = new Vector2D[4];

        Vector2D nextTopLeftCornerPosition = topLeftCornerPosition.add(moveVector);
        nextPositions[TOP_LEFT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x, solidArea.y));
        nextPositions[TOP_RIGHT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y));
        nextPositions[BOTTOM_LEFT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x, solidArea.y + solidArea.height));
        nextPositions[BOTTOM_RIGHT] = nextTopLeftCornerPosition.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y + solidArea.height));

        return nextPositions;
    }

    private boolean isTileCollision(Vector2D boxPos) {
        assert boxPos != null;

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

    public Vector2D checkTile(Entity entity, Vector2D moveVector){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<String> directions = moveVector.getDirections();

        if (directions == null){
            return Vector2D.ZERO;
        }

        Vector2D correctedMoveVector = moveVector.copy();
        // Check if after the movement the box will collide with a wall
        for (String direction : directions){

            correctedMoveVector = correctVectorWithTile(entity, correctedMoveVector, direction);
        }

        return correctedMoveVector;
    }

    private Vector2D correctVectorWithTile(Entity entity, Vector2D moveVector, String direction){
        Vector2D[] boxPos = getBoxPos(entity, moveVector, direction);
        Vector2D boxPos1 = boxPos[0];
        Vector2D boxPos2 = boxPos[1];

        if (isTileCollision(boxPos1) || isTileCollision(boxPos2)){
            return moveVector.sub(moveVector.projectTo(Vector2D.getRelatedVector(direction)));
        }
        return moveVector;
    }

    public static Vector2D[] getBoxPos(Entity entity, Vector2D moveVector, String direction) {
        Vector2D[] nextBoxPositions = getNextBoxPositions(
                entity.getWorldTopLeftPosition(),
                moveVector.projectTo(Vector2D.getRelatedVector(direction)),
                entity.getSolidArea());

        Vector2D boxPos1 = null, boxPos2 = null;
        switch (direction){
            case Vector2D.S_UP -> {
                boxPos1 = nextBoxPositions[TOP_LEFT];
                boxPos2 = nextBoxPositions[TOP_RIGHT];
            }
            case Vector2D.S_DOWN -> {
                boxPos1 = nextBoxPositions[BOTTOM_LEFT];
                boxPos2 = nextBoxPositions[BOTTOM_RIGHT];
            }
            case Vector2D.S_LEFT -> {
                boxPos1 = nextBoxPositions[TOP_LEFT];
                boxPos2 = nextBoxPositions[BOTTOM_LEFT];
            }
            case Vector2D.S_RIGHT -> {
                boxPos1 = nextBoxPositions[TOP_RIGHT];
                boxPos2 = nextBoxPositions[BOTTOM_RIGHT];
            }
        };

        return new Vector2D[]{boxPos1, boxPos2};
    }

    private record BoxPos(Vector2D boxPos1, Vector2D boxPos2) {
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
}
