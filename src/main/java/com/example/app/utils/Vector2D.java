package com.example.app.utils;

import com.example.app.GameCanvas;
import com.example.app.utils.collections.LinkedList;
import com.example.app.utils.collections.List;

import java.awt.Rectangle;

public class Vector2D {

    // CLASS VARIABLES
    private double x;
    private double y;

    // STATIC
    public static final Vector2D ZERO = new Vector2D(0, 0);
    public static final Vector2D UP = new Vector2D(0, -1);
    public static final Vector2D DOWN = new Vector2D(0, 1);
    public static final Vector2D LEFT = new Vector2D(-1, 0);
    public static final Vector2D RIGHT = new Vector2D(1, 0);

    public static final int INT_ZERO = 0;
    public static final int INT_UP = 1;
    public static final int INT_DOWN = 2;
    public static final int INT_LEFT = 3;
    public static final int INT_RIGHT = 4;

    public static final int MUL_UP =  -1;
    public static final int MUL_DOWN = 1;
    public static final int MUL_LEFT = -1;
    public static final int MUL_RIGHT = 1;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector) {
        x = vector.x;
        y = vector.y;
    }

    public static long random(int min, int max) {
        return Math.round(Math.random() * (max - min)) + min;
    }

    public static Vector2D chooseRandomWorldPosition(GameCanvas gc, List<Integer> choiceTiles) {
        return tileToWorldPosition(gc, chooseRandomTile(gc, choiceTiles));
    }

    public static Vector2D tileToWorldPosition(GameCanvas gc, int col, int row) {
        return new Vector2D(col * gc.TILE_SIZE + gc.TILE_SIZE / 2.0, row * gc.TILE_SIZE + gc.TILE_SIZE / 2.0);
    }

    public static Vector2D tileToWorldPosition(GameCanvas gc, int[] position) {
        return tileToWorldPosition(gc, position[0], position[1]);
    }

    public static int[] chooseRandomTile(GameCanvas gc, List<Integer> choiceTiles) {
        int randomIndex = (int) random(0, choiceTiles.size() - 1);
        int randomNumber = choiceTiles.get(randomIndex);

        int col, row;
        col = randomNumber % gc.tileM.getMaxWorldCol();
        row = randomNumber / gc.tileM.getMaxWorldRow();

        return new int[]{col, row};
    }

    public static Vector2D getRandomVectorNormalized() {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;

        assert x != 0 || y != 0 : "Its your lucky day !!!";
        return new Vector2D(x, y).getNormalized();
    }

    public static Vector2D getRelatedVector(int direction){
        return switch (direction){
            case INT_ZERO -> ZERO;
            case INT_UP -> UP;
            case INT_DOWN -> DOWN;
            case INT_LEFT -> LEFT;
            case INT_RIGHT -> RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    public static int getTileX(int tileSize, double worldX) {
        return (int) Math.floor(worldX / tileSize);
    }

    public static int getTileY(int tileSize, double worldY) {
        return (int) Math.floor(worldY / tileSize);
    }

    public static List<Integer> getTile(int tileSize, Vector2D position) {
        List<Integer> tile = new LinkedList<>();

        tile.add(getTileX(tileSize, position.getX()));
        tile.add(getTileY(tileSize, position.getY()));

        return tile;
    }

    public static double getDistance(Vector2D one, Vector2D two) {
        double dx = one.getX() - two.getX();
        double dy = one.getY() - two.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static boolean isPointInArea(double x, double y, double minX, double minY, double maxX, double maxY){
        return (x >= minX
                && x <= maxX
                && y >= minY
                && y <= maxY);
    }

    public static boolean isPointInArea(Vector2D point, Vector2D topLeftAreaPoint, int width, int height){
        double minX, maxX, minY, maxY;
        minX = topLeftAreaPoint.getX();
        maxX = topLeftAreaPoint.getX() + width;
        minY = topLeftAreaPoint.getY();
        maxY = topLeftAreaPoint.getY() + height;

        return isPointInArea(point.getX(), point.getY(), minX, minY, maxX, maxY);
    }

    public static boolean isPointInArea(Vector2D point, Vector2D referencePoint, Rectangle area){
        return isPointInArea(point.getX(), point.getY(),
                referencePoint.getX() + area.x,
                referencePoint.getY() + area.y,
                referencePoint.getX() + area.width,
                referencePoint.getY() + area.height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public double getDistance(Vector2D other) {
        return Vector2D.getDistance(this, other);
    }

    public Vector2D getNormalized() {
        Vector2D newVector = copy();
        newVector.normalize();
        return newVector;
    }

    public Vector2D getOrthogonal(){
        return new Vector2D(-getY(), getX());
    }

    public boolean isNormalized() {
        return Math.abs(getLength() - 1) < 0.0001;
    }

    public int getMainDirection() {
        if (x == 0 && y == 0) {
            return INT_ZERO;
        }

        if (Math.abs(x) > Math.abs(y)) {
            if (x > 0) {
                return INT_RIGHT;
            } else {
                return INT_LEFT;
            }
        } else {
            if (y > 0) {
                return INT_DOWN;
            } else {
                return INT_UP;
            }
        }
    }

    public int[] getDirections() {
        if (x == 0 && y == 0) {
            return null;
        }

        int[] directions = new int[2];
        if (x > 0) {
            directions[0] = INT_RIGHT;
        } else if (x < 0) {
            directions[0] = INT_LEFT;
        }

        if (y > 0) {
            directions[1] = INT_DOWN;
        } else if (y < 0) {
            directions[1] = INT_UP;
        }

        return directions;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void add(Vector2D other) {
        x += other.x;
        y += other.y;
    }

    public void sub(double x, double y){
        this.x -= x;
        this.y -= y;
    }

    public void sub(Vector2D other) {
        sub(other.x, other.y);
    }

    public void mul(double other) {
        x *= other;
        y *= other;
    }

    public double dotProduct(Vector2D other) {
        return x * other.getX() + y * other.getY();
    }

    public Vector2D getProjectionOn(Vector2D targetVector) {
        double dotP = dotProduct(targetVector);

        Vector2D newVector = targetVector.copy();
        newVector.mul(dotP);
        return newVector;
    }

    public Vector2D absProjectTo(Vector2D targetVector){
        // Keep the main orientation of this (self) vector when projecting on targetVector
        double dotP = dotProduct(targetVector);
        Vector2D newVector = getProjectionOn(targetVector);
        if (dotP < 0){
            newVector.mul(-1);
        }
        return newVector;
    }

    public void div(double other){
        assert other != 0;
        x /= other;
        y /= other;
    }

    public void normalize(){
        if (x == 0 && y == 0){
            return;
        }
        double length = getLength();
        x /= length;
        y /= length;
    }

    public Vector2D copy() {
        return new Vector2D(this);
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + ")";
    }

    public boolean equals(Vector2D vector2D) {
        return vector2D.x == x && vector2D.y == y;
    }
}
