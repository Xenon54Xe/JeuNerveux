package net.rubicon.entity;

public interface ITrackable {
    /// Try to keep the tracked entity in the center of the screen, except when it is at the edge of the map
    ///
    /// The camera position is the position of the top left corner of the screen in the world

    double getWorldX();

    double getWorldY();

    int getScreenX();

    int getScreenY();

    int getCameraWorldX();

    int getCameraWorldY();

    default double calcCameraWorldX(int screenWidth, int worldWidth){
        // Top left corner of the screen
        if (getWorldX() >= screenWidth / 2.0 && getWorldX() <= worldWidth - screenWidth / 2.0){
            return getWorldX() - screenWidth / 2.0;
        } else if (getWorldX() < screenWidth / 2.0) {
            return 0;
        }else {
            return worldWidth - screenWidth;
        }
    }

    default double calcCameraWorldY(int screenHeight, int worldHeight){
        // Top left corner of the screen
        if (getWorldY() >= screenHeight / 2.0 && getWorldY() <= worldHeight - screenHeight / 2.0){
            return getWorldY() - screenHeight / 2.0;
        } else if (getWorldY() < screenHeight / 2.0) {
            return 0;
        }else {
            return worldHeight - screenHeight;
        }
    }

    default double calcTrackedEntityScreenX(int screenWidth, int worldWidth) {
        // Top left corner of the entity
        if (getWorldX() >= screenWidth / 2.0 && getWorldX() <= worldWidth - screenWidth / 2.0){
            return screenWidth / 2.0;
        } else if (getWorldX() < screenWidth / 2.0) {
            return getWorldX();
        }else {
            return screenWidth - worldWidth + getWorldX();
        }
    }

    default double calcTrackedEntityScreenY(int screenHeight, int worldHeight){
        // Top left corner of the entity
        if (getWorldY() >= screenHeight / 2.0 && getWorldY() <= worldHeight - screenHeight / 2.0){
            return screenHeight / 2.0;
        } else if (getWorldY() < screenHeight / 2.0) {
            return getWorldY();
        }else {
            return screenHeight - worldHeight + getWorldY();
        }
    }
}
