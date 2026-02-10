package com.example.app;

import com.example.app.utils.Vector2D;

import java.awt.*;

public interface Collideable {

    boolean isCollisionEnabled();

    void setCollisionEnabled(boolean collision);

    Rectangle getSolidArea();

    void setSolidArea(Rectangle solidArea);

    default boolean collideWith(Collideable other, Vector2D ownPosition, Vector2D otherPosition){
        double ownX = ownPosition.getX();
        double ownY = ownPosition.getY();
        double otherX = otherPosition.getX();
        double otherY = otherPosition.getY();
        int areaX = other.getSolidArea().x;
        int areaY = other.getSolidArea().y;
        int width = other.getSolidArea().width;
        int height = other.getSolidArea().height;

        return ownX > otherX + areaX &&
                ownX < otherX + areaX + width &&
                ownY > otherY + areaY &&
                ownY < otherY + areaY + height;
    }
}
