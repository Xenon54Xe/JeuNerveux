package com.example.app;

import com.example.app.entity.Entity;
import com.example.app.tile.Tile;
import com.example.app.utils.Vector2D;
import com.example.app.utils.collections.List;

import java.awt.Rectangle;

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

    public void checkEntity(List<Entity> others, Entity entity, Vector2D moveVector){
        // GET DIRECTIONS WHERE ENTITY GO
        int[] directions = moveVector.getDirections();

        if (directions == null){
            return;
        }

        // Check if after the movement the box will collide with a wall
        for (int direction : directions){
            for (Entity other : others){
                boolean modified = correctVectorWithEntity(entity, other, moveVector, direction);
                if(modified){
                    break;
                }
            }
        }
    }
    
    private boolean correctVectorWithEntity(Entity entity, Entity other, Vector2D moveVector, int direction){
        double posAX, posBX, posAY, posBY;
        switch (direction){
            case Vector2D.INT_LEFT -> {
                posAX = entity.getLeft() + moveVector.getX();
                posAY = entity.getTop() + moveVector.getY();
                posBX = entity.getLeft() + moveVector.getX();
                posBY = entity.getBottom() + moveVector.getY();
                if (rectangleContains(other.getSolidArea(), posAX, posAY) || rectangleContains(other.getSolidArea(), posBX, posBY)){
                    moveVector.sub(moveVector.getX(), 0);
                    return true;
                }
            }
            case Vector2D.INT_RIGHT -> {
                posAX = entity.getRight() + moveVector.getX();
                posAY = entity.getTop() + moveVector.getY();
                posBX = entity.getRight() + moveVector.getX();
                posBY = entity.getBottom() + moveVector.getY();
                if (rectangleContains(other.getSolidArea(), posAX, posAY) || rectangleContains(other.getSolidArea(), posBX, posBY)){
                    moveVector.sub(moveVector.getX(), 0);
                    return true;
                }
            }
            case Vector2D.INT_UP -> {
                posAX = entity.getLeft() + moveVector.getX();
                posAY = entity.getTop() + moveVector.getY();
                posBX = entity.getRight() + moveVector.getX();
                posBY = entity.getTop() + moveVector.getY();
                if (rectangleContains(other.getSolidArea(), posAX, posAY) || rectangleContains(other.getSolidArea(), posBX, posBY)){
                    moveVector.sub(0, moveVector.getY());
                    return true;
                }
            }
            case Vector2D.INT_DOWN -> {
                posAX = entity.getLeft() + moveVector.getX();
                posAY = entity.getBottom() + moveVector.getY();
                posBX = entity.getRight() + moveVector.getX();
                posBY = entity.getBottom() + moveVector.getY();
                if (rectangleContains(other.getSolidArea(), posAX, posAY) || rectangleContains(other.getSolidArea(), posBX, posBY)) {
                    moveVector.sub(0, moveVector.getY());
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean rectangleContains(Rectangle rectangle, double x, double y){
        return rectangle.x <= x && x <= rectangle.x + rectangle.width
                && rectangle.y <= y && y <= rectangle.y + rectangle.height;
    }

    public void checkTile(Entity entity, Vector2D moveVector){
        // Modify directly moveVector to allow sliding on walls
        // GET DIRECTIONS WHERE ENTITY GO
        int[] directions = moveVector.getDirections();

        if (directions == null){
            return;
        }

        // Check if after the movement the box will collide with a wall
        for (int direction : directions){
            correctVectorWithTile(entity, moveVector, direction);
        }
    }

    private void correctVectorWithTile(Entity entity, Vector2D moveVector, int direction){
        double posAX, posBX, posAY, posBY;
        switch (direction){
            case Vector2D.INT_LEFT -> {
                posAX = entity.getLeft() + moveVector.getX();
                posAY = entity.getTop() + moveVector.getY();
                posBX = entity.getLeft() + moveVector.getX();
                posBY = entity.getBottom() + moveVector.getY();
                if (isTileCollision(posAX, posAY) || isTileCollision(posBX, posBY)){
                    moveVector.sub(moveVector.getX(), 0);
                }
            }
            case Vector2D.INT_RIGHT -> {
                posAX = entity.getRight() + moveVector.getX();
                posAY = entity.getTop() + moveVector.getY();
                posBX = entity.getRight() + moveVector.getX();
                posBY = entity.getBottom() + moveVector.getY();
                if (isTileCollision(posAX, posAY) || isTileCollision(posBX, posBY)){
                    moveVector.sub(moveVector.getX(), 0);
                }
            }
            case Vector2D.INT_UP -> {
                posAX = entity.getLeft() + moveVector.getX();
                posAY = entity.getTop() + moveVector.getY();
                posBX = entity.getRight() + moveVector.getX();
                posBY = entity.getTop() + moveVector.getY();
                if (isTileCollision(posAX, posAY) || isTileCollision(posBX, posBY)){
                    moveVector.sub(0, moveVector.getY());
                }
            }
            case Vector2D.INT_DOWN -> {
                posAX = entity.getLeft() + moveVector.getX();
                posAY = entity.getBottom() + moveVector.getY();
                posBX = entity.getRight() + moveVector.getX();
                posBY = entity.getBottom() + moveVector.getY();
                if (isTileCollision(posAX, posAY) || isTileCollision(posBX, posBY)){
                    moveVector.sub(0, moveVector.getY());
                }
            }
            default -> {
            }
        }
    }

    private boolean isTileCollision(double posX, double posY) {
        int tileX, tileY;
        tileX = Vector2D.getTileX(gc.TILE_SIZE, posX);
        tileY = Vector2D.getTileY(gc.TILE_SIZE, posY);

        for (int layer = 0; layer < gc.tileM.getLayerCount(); layer++) {
            // Test if on the edge of the world
            if (tileX < 0 || tileX >= gc.tileM.getMaxWorldCol()
                    || tileY < 0 || tileY >= gc.tileM.getMaxWorldRow()) {
                return true;
            }
            else {
                // Test if the entity is heading toward a collision tile
                int tileNum = gc.tileM.tileMap.getTileNum(tileX, tileY, layer);
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
            case Vector2D.INT_UP -> {
                targetPointA = TOP_LEFT;
                targetPointB = TOP_RIGHT;
            }
            case Vector2D.INT_DOWN -> {
                targetPointA = BOTTOM_LEFT;
                targetPointB = BOTTOM_RIGHT;
            }
            case Vector2D.INT_LEFT -> {
                targetPointA = TOP_LEFT;
                targetPointB = BOTTOM_LEFT;
            }
            case Vector2D.INT_RIGHT -> {
                targetPointA = TOP_RIGHT;
                targetPointB = BOTTOM_RIGHT;
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        return new int[]{targetPointA, targetPointB};
    }

    public static double getNextPointPosition(double topLeftCornerCoordinate, double moveVectorCoordinate, Rectangle solidArea, int pointToCheck){
        double nextCoordinate = topLeftCornerCoordinate + moveVectorCoordinate;
        switch (pointToCheck){
            case TOP_LEFT, BOTTOM_LEFT -> {
                return nextCoordinate + solidArea.x;
            }
            case TOP_RIGHT, BOTTOM_RIGHT -> {
                return nextCoordinate + solidArea.x + solidArea.width;
            }
            default -> {
                return 0;
            }
        }
    }

//    public static double[] getNextPositions(double topLeftX, double topLeftY, Vector2D moveVector , Rectangle solidArea, int pointToCheck){
//        double[] nextPositions = new double[2];
//        switch (pointToCheck){
//            case Vector2D.INT_LEFT -> {
//
//            }
//        }
//    }
//
//    public static Vector2D getNextBoxPositions(Vector2D topLeftCornerPos, Vector2D moveVector, Rectangle solidArea, int pointToCheck){
//        // Choose two of top left, top right, bottom left, bottom right
//        Vector2D nextTopLeftCornerPos = topLeftCornerPos.add(moveVector);
//
//        switch (pointToCheck){
//            case TOP_LEFT -> {
//                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x, solidArea.y));
//            }
//            case TOP_RIGHT -> {
//                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y));
//            }
//            case BOTTOM_LEFT -> {
//                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x, solidArea.y + solidArea.height));
//            }
//            case BOTTOM_RIGHT -> {
//                return nextTopLeftCornerPos.add(new Vector2D(solidArea.x + solidArea.width, solidArea.y + solidArea.height));
//            }
//            default -> {
//                return Vector2D.ZERO;
//            }
//        }
//    }
}
