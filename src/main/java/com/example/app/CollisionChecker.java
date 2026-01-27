package com.example.app;

import com.example.app.entity.Entity;
import com.example.app.tile.Tile;
import com.example.app.ui.UIDrawVector;
import com.example.app.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    /// Set the movement toward a wall to zero to allow the com.example.app.entity to slide on the wall

    final GameCanvas gc;

    // STATIC
    public final static int TOP_LEFT = 0;
    public final static int TOP_RIGHT = 1;
    public final static int BOTTOM_LEFT = 2;
    public final static int BOTTOM_RIGHT = 3;

    public CollisionChecker(GameCanvas gc){
        this.gc = gc;
    }

    public Vector2D checkEntity(ArrayList<Entity> others, Entity entity, Vector2D moveVector){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<Integer> directions = moveVector.getDirections();

        if (directions == null){
            return Vector2D.ZERO;
        }

        Vector2D correctedMoveVector = moveVector.copy();
        // Check if after the movement the box will collide with a wall
        for (int direction : directions){

            for (Entity other : others) {
                correctedMoveVector = correctVectorWithEntity(entity, other, correctedMoveVector, direction);
            }
        }

        return correctedMoveVector;
    }
    
    private Vector2D correctVectorWithEntity(Entity entity, Entity other, Vector2D moveVector, int direction){

        int[] targetPoints = getTargetPoints(direction);
        int targetPointA = targetPoints[0];
        int targetPointB = targetPoints[1];
        Vector2D topLeftPos = entity.getWorldTopLeftPosition();
        Rectangle solidArea = entity.getSolidArea();

        Vector2D boxPos1 = getNextBoxPositions(topLeftPos, moveVector, solidArea, targetPointA);
        Vector2D boxPos2 = getNextBoxPositions(topLeftPos, moveVector, solidArea, targetPointB);

        if (Vector2D.isPointInArea(boxPos1, other.getWorldTopLeftPosition(), other.getSolidArea())
                || Vector2D.isPointInArea(boxPos2, other.getWorldTopLeftPosition(), other.getSolidArea())){
            return moveVector.sub(moveVector.projectTo(Vector2D.getRelatedVector(direction)));
        }
        return moveVector;
    }

    public Vector2D checkTile(Entity entity, Vector2D moveVector){
        // GET DIRECTIONS WHERE ENTITY GO
        ArrayList<Integer> directions = moveVector.getDirections();

        if (directions == null){
            return Vector2D.ZERO;
        }

        Vector2D correctedMoveVector = moveVector.copy();
        // Check if after the movement the box will collide with a wall
        for (int direction : directions){

            correctedMoveVector = correctVectorWithTile(entity, correctedMoveVector, direction);
        }

        return correctedMoveVector;
    }

    private Vector2D correctVectorWithTile(Entity entity, Vector2D moveVector, int direction){
        int[] targetPoints = getTargetPoints(direction);
        int targetPointA = targetPoints[0];
        int targetPointB = targetPoints[1];
        Vector2D topLeftPos = entity.getWorldTopLeftPosition();
        Rectangle solidArea = entity.getSolidArea();

        Vector2D boxPos1 = getNextBoxPositions(topLeftPos, moveVector, solidArea, targetPointA);
        Vector2D boxPos2 = getNextBoxPositions(topLeftPos, moveVector, solidArea, targetPointB);

        if (isTileCollision(boxPos1) || isTileCollision(boxPos2)){
            return moveVector.sub(moveVector.projectTo(Vector2D.getRelatedVector(direction)));
        }
        return moveVector;
    }

    private boolean isTileCollision(Vector2D boxPos) {
        assert boxPos != null;

        int[] tilePos = Vector2D.getTile(gc.TILE_SIZE, boxPos);


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

    public static int[] getTargetPoints(int direction) {
        int targetPointA, targetPointB;
        switch (direction){
            case Vector2D.S_UP -> {
                targetPointA = TOP_LEFT;
                targetPointB = TOP_RIGHT;
            }
            case Vector2D.S_DOWN -> {
                targetPointA = BOTTOM_LEFT;
                targetPointB = BOTTOM_RIGHT;
            }
            case Vector2D.S_LEFT -> {
                targetPointA = TOP_LEFT;
                targetPointB = BOTTOM_LEFT;
            }
            case Vector2D.S_RIGHT -> {
                targetPointA = TOP_RIGHT;
                targetPointB = BOTTOM_RIGHT;
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        return new int[]{targetPointA, targetPointB};
    }

    public static Vector2D getNextBoxPositions(Vector2D topLeftCornerPos, Vector2D moveVector, Rectangle solidArea, int pointToCheck){
        // Choose two of top left, top right, bottom left, bottom right
        Vector2D nextTopLeftCornerPos = topLeftCornerPos.add(moveVector);

        switch (pointToCheck){
            case TOP_LEFT -> {
                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x, solidArea.y));
            }
            case TOP_RIGHT -> {
                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y));
            }
            case BOTTOM_LEFT -> {
                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x, solidArea.y + solidArea.height));
            }
            case BOTTOM_RIGHT -> {
                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y + solidArea.height));
            }
            default -> {
                return Vector2D.ZERO;
            }
        }
    }
}
