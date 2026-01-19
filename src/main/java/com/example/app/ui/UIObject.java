package com.example.app.ui;

import com.example.app.IDrawable;
import com.example.app.utils.Vector2D;

public abstract class UIObject implements IDrawable {

    // Management
    public static int NEXT_ID = 0;
    public final int id;

    // CLASS VARIABLES
    private int screenX, screenY;
    private String name;
    
    // DRAW LOGIC
    private boolean show = false;
    private int width, height;

    // DRAW REFERENCE
    public static final String DRAW_TOP_LEFT_CORNER = "top-left";
    public static final String DRAW_TOP_RIGHT_CORNER = "tpo-right";
    public static final String DRAW_BOTTOM_LEFT_CORNER = "bottom_left";
    public static final String DRAW_BOTTOM_RIGHT_CORNER = "bottom_right";
    public static final String DRAW_CENTER = "center";
    private String drawReference = DRAW_CENTER;

    public UIObject(String name, int screenX, int screenY){
        this.name = name;
        this.screenX = screenX;
        this.screenY = screenY;

        id = NEXT_ID++;
    }

    public int getNextX(int x, int maxX){
        return switch (drawReference){
            case DRAW_TOP_LEFT_CORNER, DRAW_BOTTOM_LEFT_CORNER -> x;
            case DRAW_TOP_RIGHT_CORNER, DRAW_BOTTOM_RIGHT_CORNER -> maxX - x;
            default -> throw new IllegalStateException("Unexpected value: " + drawReference);
        };
    }

    public int getNextY(int y, int maxY){
        return switch (drawReference){
            case DRAW_TOP_LEFT_CORNER, DRAW_TOP_RIGHT_CORNER -> y;
            case DRAW_BOTTOM_LEFT_CORNER, DRAW_BOTTOM_RIGHT_CORNER -> maxY - y;
            default -> throw new IllegalStateException("Unexpected value: " + drawReference);
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height){
        setWidth(width);
        setHeight(height);
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getDrawReference() {
        return drawReference;
    }

    public void setDrawReference(String drawReference) {
        assert drawReference.equals(DRAW_CENTER)
                || drawReference.equals(DRAW_TOP_LEFT_CORNER)
                || drawReference.equals(DRAW_BOTTOM_LEFT_CORNER)
                || drawReference.equals(DRAW_TOP_RIGHT_CORNER)
                | drawReference.equals(DRAW_BOTTOM_RIGHT_CORNER);
        this.drawReference = drawReference;
    }

    public int getScreenX() {
        // Return the coordinates of the top left corner
        return screenX;
    }

    public void setScreenX(int screenX) {
        // Set the coordinates of the top left corner
        this.screenX = screenX;
    }

    public int getScreenY() {
        // Return the coordinates of the top left corner
        return screenY;
    }

    public void setScreenY(int screenY) {
        // Set the coordinates of the top left corner
        this.screenY = screenY;
    }

    public int getDrawTopLeftScreenX(){
        switch (drawReference) {
            case DRAW_CENTER -> {
                return getScreenX() - width / 2;
            }
            case DRAW_TOP_LEFT_CORNER, DRAW_BOTTOM_LEFT_CORNER -> {
                return getScreenX();
            }
            case DRAW_TOP_RIGHT_CORNER, DRAW_BOTTOM_RIGHT_CORNER -> {
                return getScreenX() - width;
            }
            default -> {
                return -1;
            }
        }
    }

    public int getDrawTopLeftScreenY(){
        switch (drawReference) {
            case DRAW_CENTER -> {
                return getScreenY() - height / 2;
            }
            case DRAW_TOP_LEFT_CORNER, DRAW_TOP_RIGHT_CORNER -> {
                return getScreenY();
            }
            case DRAW_BOTTOM_LEFT_CORNER, DRAW_BOTTOM_RIGHT_CORNER -> {
                return getScreenY() - height;
            }
            default -> {
                return -1;
            }
        }
    }

    public int getDrawBottomLeftScreenX(){
        return getDrawTopLeftScreenX();
    }

    public int getDrawBottomLeftScreenY(){
        return getDrawTopLeftScreenY() + height;
    }

    public int getDrawTopRightScreenX(){
        return getDrawTopLeftScreenX() + width;
    }

    public int getDrawTopRightScreenY(){
        return getDrawTopLeftScreenY();
    }

    public int getDrawBottomRightScreenX(){
        return getDrawTopLeftScreenX() + width;
    }

    public int getDrawBottomRightScreenY(){
        return getDrawTopLeftScreenY() + height;
    }

    public int getDrawCenterScreenX(){
        return getDrawTopLeftScreenX() + width / 2;
    }

    public int getDrawCenterScreenY(){
        return getDrawTopLeftScreenY() + height / 2;
    }

    public int getDrawReferenceScreenX(){
        return switch (drawReference){
            case DRAW_TOP_LEFT_CORNER -> getDrawTopLeftScreenX();
            case DRAW_BOTTOM_LEFT_CORNER -> getDrawBottomLeftScreenX();
            case DRAW_TOP_RIGHT_CORNER -> getDrawTopRightScreenX();
            case DRAW_BOTTOM_RIGHT_CORNER -> getDrawBottomRightScreenX();
            case DRAW_CENTER -> getDrawCenterScreenX();
            default -> throw new IllegalStateException("Unexpected value: " + drawReference);
        };
    }

    public int getDrawReferenceScreenY(){
        return switch (drawReference){
            case DRAW_TOP_LEFT_CORNER -> getDrawTopLeftScreenY();
            case DRAW_BOTTOM_LEFT_CORNER -> getDrawBottomLeftScreenY();
            case DRAW_TOP_RIGHT_CORNER -> getDrawTopRightScreenY();
            case DRAW_BOTTOM_RIGHT_CORNER -> getDrawBottomRightScreenY();
            case DRAW_CENTER -> getDrawCenterScreenY();
            default -> throw new IllegalStateException("Unexpected value: " + drawReference);
        };
    }

    public int[] getDrawReferenceMultiplier(){
        return switch (drawReference){
            case DRAW_TOP_LEFT_CORNER, DRAW_CENTER -> new int[]{1, 1};
            case DRAW_BOTTOM_LEFT_CORNER -> new int[]{1, -1};
            case DRAW_TOP_RIGHT_CORNER -> new int[]{-1, 1};
            case DRAW_BOTTOM_RIGHT_CORNER -> new int[]{-1, -1};
            default -> throw new IllegalStateException("Unexpected value: " + drawReference);
        };
    }

    public void setScreenPosition(Vector2D vector2D){
        setScreenPosition((int)Math.round(vector2D.getX()), (int)Math.round(vector2D.getY()));
    }

    public void setScreenPosition(int screenX, int screenY){
        setScreenX(screenX);
        setScreenY(screenY);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean mouseOver() {
        return false;
    }

    public boolean equals(UIObject obj) {
    return id == obj.id;
    }
}
