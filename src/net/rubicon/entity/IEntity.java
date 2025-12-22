package net.rubicon.entity;

import java.awt.*;

public interface IEntity {
    void update(double dt);

    void draw(Graphics2D g2);
}
