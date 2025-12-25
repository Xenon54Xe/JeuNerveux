package net.rubicon.utils;

import net.rubicon.main.ITrackable;

import java.util.ArrayList;

public class Vector2D {

    // CLASS VARIABLES
    private double x;
    private double y;

    // STATIC
    public static Vector2D ZERO = new Vector2D(0, 0);
    public static Vector2D UP = new Vector2D(0, -1);
    public static Vector2D DOWN = new Vector2D(0, 1);
    public static Vector2D LEFT = new Vector2D(-1, 0);
    public static Vector2D RIGHT = new Vector2D(1, 0);

    public static String S_ZERO = "zero";
    public static String S_UP = "up";
    public static String S_DOWN = "down";
    public static String S_LEFT = "left";
    public static String S_RIGHT = "right";


    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector){
        x = vector.x;
        y = vector.y;
    }

    public static Vector2D getRandomVectorNormalized(){
        double x = Math.random();
        double y = Math.random();

        return new Vector2D(x, y).getNormalized();
    }

    public static Vector2D getScreenPosition(Vector2D reference, Vector2D worldPos){
        return worldPos.sub(reference);
    }

    public static int getTileX(int tileSize, double worldX){
        return (int)(worldX / tileSize);
    }

    public static int getTileY(int tileSize, double worldY){
        return (int)(worldY / tileSize);
    }

    public static int[] getTile(int tileSize, Vector2D position){
        int[] tile = new int[2];

        tile[0] = getTileX(tileSize, position.getX());
        tile[1] = getTileY(tileSize, position.getY());

        return tile;
    }

    public static double getDistance(Vector2D one, Vector2D two){
        return two.sub(one).getLength();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength(){
        return Math.sqrt(x * x + y * y);
    }

    public double getDistance(Vector2D other){
        return Vector2D.getDistance(this, other);
    }

    public Vector2D getNormalized(){
        Vector2D newVector = new Vector2D(this);
        newVector.normalize();
        return newVector;
    }

    public String getMainDirection(){
        if (x == 0 && y == 0){
            return S_ZERO;
        }

        if (Math.abs(x) > Math.abs(y)){
            if (x > 0){
                return S_RIGHT;
            }
            else {
                return S_LEFT;
            }
        }
        else {
            if (y > 0){
                return S_DOWN;
            }
            else {
                return S_UP;
            }
        }
    }

    public ArrayList<String> getDirections(){
        if (x == 0 && y == 0){
            return null;
        }

        ArrayList<String> directions = new ArrayList<>();
        if (x > 0){
            directions.add(S_RIGHT);
        }
        else if(x < 0){
            directions.add(S_LEFT);
        }

        if (y > 0){
            directions.add(S_DOWN);
        } else if (y < 0) {
            directions.add(S_UP);
        }

        return directions;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2D add(Vector2D other){
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D sub(Vector2D other){
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D mul(double other){
        return new Vector2D(x * other, y * other);
    }

    public double mul(Vector2D other){
        return x * other.x + y * other.y;
    }

    public Vector2D mask(Vector2D other){
        // Return the coordinates multiplied by the coordinates of the mask
        return new Vector2D(x * other.x, y * other.y);
    }

    public Vector2D absMask(Vector2D other){
        // Return the coordinates multiplied by the absolutes coordinates of the mask
        return new Vector2D(x * Math.abs(other.x), y * Math.abs(other.y));
    }

    public Vector2D div(double other){
        assert other != 0;
        return new Vector2D(x / other, y / other);
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
}
