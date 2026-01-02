package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.utils.LinkedList;

import java.awt.*;

public class UIManager {

    // UTILS
    private final GameCanvas gc;
    private final Font arial_tile_size;

    // CLASS VARIABLES
    private boolean mouseOverUI;

    // Elements
    private final LinkedList<UIObject> uiObjects = new LinkedList<>();
    private final LinkedList<UIObject> clickableUIObjects = new LinkedList<>();
    private final LinkedList<UIObject> updatableUIObjects = new LinkedList<>();

    public UIManager(GameCanvas gc){
        this.gc = gc;
        arial_tile_size = new Font("Arial", Font.PLAIN, gc.tileSize / 2);
    }

    public boolean isMouseOverUI() {
        return mouseOverUI;
    }

    public void addUIObject(UIObject uiObject){
        if (uiObject instanceof IClickable || uiObject instanceof IUpdatable){

            if (uiObject instanceof IClickable) {
                clickableUIObjects.add(uiObject);
            }
            if (uiObject instanceof IUpdatable) {
                updatableUIObjects.add(uiObject);
            }
        }else {

            uiObjects.add(uiObject);
        }
    }

    public void update(){
        // For clickable objects
        for (int i = 0; i < clickableUIObjects.size(); i++) {
            IClickable clickable = (IClickable) clickableUIObjects.getFirstValueNShift();
            clickable.isClicked();
        }

        // For updatable objects
        for (int i = 0; i < updatableUIObjects.size(); i++){
            IUpdatable updatable = (IUpdatable) updatableUIObjects.getFirstValueNShift();
            updatable.update();
        }
    }

    private void drawAllUI(Graphics2D g2, LinkedList<UIObject> uiObjects){
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
        drawAllUI(g2, uiObjects); // Test also if mouse over com.example.app.ui
        drawAllUI(g2, clickableUIObjects);
        drawAllUI(g2, updatableUIObjects);
    }
}
