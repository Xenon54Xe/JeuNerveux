package net.rubicon.main;

import net.rubicon.entity.ITrackable;

import java.awt.*;

public interface IPrintable {

    default int getScreenX(ITrackable tracked, int worldX){
        assert tracked != null;
        return worldX - tracked.getCameraWorldX();
    }

    default int getScreenY(ITrackable tracked, int worldY){
        assert tracked != null;
        return worldY - tracked.getCameraWorldY();
    }

    void draw(Graphics2D g2);
}
