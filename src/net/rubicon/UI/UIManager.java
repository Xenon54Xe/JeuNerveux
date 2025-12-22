package net.rubicon.UI;

import net.rubicon.main.GameCanvas;
import net.rubicon.utils.LinkedList;

import java.awt.*;

public class UIManager {

    private boolean mouseOverUI;

    private final GameCanvas gc;
    private final Font arial_tile_size;

    // Elements
    private final LinkedList<UIObject> uiObjects = new LinkedList<>();
    private final LinkedList<UIObject> clickableUIObjects = new LinkedList<>();

    // Others elements
    private final UIText positionText;

    public UIManager(GameCanvas gc){

        this.gc = gc;
        arial_tile_size = new Font("Arial", Font.PLAIN, gc.tileSize / 2);

        // REGISTER UI OBJECTS
        positionText = new UIText(Color.BLACK, "Position", 25, 50);
        addUIObject(positionText);
    }

    public boolean isMouseOverUI() {
        return mouseOverUI;
    }

    public void addUIObject(UIObject uiObject){
        if (uiObject instanceof IClickable){
            clickableUIObjects.add(uiObject);
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

        // For changing ui
        positionText.setText("X, Y = " + (int)(gc.player.getWorldX() / gc.tileSize + 0.5) + ", " + (int)(gc.player.getWorldY() / gc.tileSize + 1));
    }

    private void drawAllInArray(Graphics2D g2, LinkedList<UIObject> uiObjects){
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
        drawAllInArray(g2, uiObjects); // Test also if mouse over ui
        drawAllInArray(g2, clickableUIObjects);
    }
}
