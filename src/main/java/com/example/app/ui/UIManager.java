package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.utils.ILinkedList;
import com.example.app.utils.LinkedList;

import java.awt.*;

/**
 * If an ui object should be updatable, then implements IUpdatable
 * If an ui object should be clickable, then implements IClickable
 */
public class UIManager {

    // UTILS
    private final Font arial_tile_size;

    // CLASS VARIABLES
    private boolean mouseOverUI;

    // Elements
    private final ILinkedList<UIObject> uiObjects = new LinkedList<>();
    private final ILinkedList<IClickable> clickableUIObjects = new LinkedList<>();
    private final ILinkedList<IUpdatable> updatableUIObjects = new LinkedList<>();

    public UIManager(GameCanvas gc){

        arial_tile_size = new Font("Arial", Font.PLAIN, gc.tileSize / 2);
    }

    public boolean isMouseOverUI() {
        return mouseOverUI;
    }

    public void addUIObject(UIObject uiObject){

        if (uiObject instanceof IClickable clickable) {
            clickableUIObjects.add(clickable);
        }
        if (uiObject instanceof IUpdatable updatable) {
            updatableUIObjects.add(updatable);
        }
        uiObjects.add(uiObject);
    }

    public void update(){
        // For clickable objects
        for (int i = 0; i < clickableUIObjects.size(); i++) {
            IClickable clickable = clickableUIObjects.getFirstValueNShift();
            clickable.isClicked();
        }

        // For updatable objects
        for (int i = 0; i < updatableUIObjects.size(); i++){
            IUpdatable updatable = updatableUIObjects.getFirstValueNShift();
            updatable.update();
        }
    }

    private void drawAllUI(Graphics2D g2, ILinkedList<UIObject> uiObjects){
        for (int i = 0; i < uiObjects.size(); i++) {
            UIObject uiObject = uiObjects.getFirstValueNShift();
            uiObject.draw(g2);
            if (!mouseOverUI && uiObject.mouseOver()){
                mouseOverUI = true;
            }
        }
    }

    public void draw(Graphics2D g2){

        g2.setFont(arial_tile_size);
        g2.setColor(Color.WHITE);

        mouseOverUI = false;
        drawAllUI(g2, uiObjects); // Test also if mouse over ui
    }
}
