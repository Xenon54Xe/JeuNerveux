package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class UIFrame extends UIObject {

    final GameCanvas gc;

    // MASTER
    private UIFrame parentFrame = null;

    // CLASS VARIABLES
    private final ArrayList<FrameCase> frameCases = new ArrayList<>();
    private int maxCol, maxRow; // To place ui
    private int maxWidth, maxHeight; // Cumulated size of ui

    // DRAW OPTIONS
    public final static String DRAW_EVENLY = "draw-evenly";
    public final static String DRAW_STEP_BETWEEN_CENTER = "draw-step-center";
    public final static String DRAW_STEP_BETWEEN_EDGES = "draw-step-edges";
    private String drawOption = DRAW_EVENLY;
    private final int updatePositionDelay = 20;
    private int updatePositionTimer = 0;

    // DRAW CHARACTERISTICS
    private int stepX, stepY;

    public UIFrame(GameCanvas gc, String name) {
        super(name, 0, 0);

        this.gc = gc;

        gc.uiM.addUIObject(this);
    }

    public UIFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(UIFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void setShape(int col, int row){
        this.maxCol = col;
        this.maxRow = row;
    }

    private void setDrawOption(String drawOption) {
        this.drawOption = drawOption;
    }

    public void setDrawEvenly(){
        setDrawOption(DRAW_EVENLY);
    }

    public void setDrawStepBetweenCenter(int stepX, int stepY){
        setStep(stepX, stepY);
        setDrawOption(DRAW_STEP_BETWEEN_CENTER);
    }

    public void setDrawStepBetweenEdges(int stepX, int stepY){
        setStep(stepX, stepY);
        setDrawOption(DRAW_STEP_BETWEEN_EDGES);
    }

    public void setStep(int stepX, int stepY){
        this.stepX = stepX;
        this.stepY = stepY;
    }

    @Override
    public void setShow(boolean show) {
        super.setShow(show);

        for (FrameCase frameCase : frameCases){
            frameCase.getObject().setShow(show);
        }
    }

    public void expand(){
        if(parentFrame == null){
            switch (getDrawReference()) {
                case DRAW_CENTER -> setScreenPosition(gc.screenWidth / 2, gc.screenHeight / 2);
                case DRAW_TOP_LEFT_CORNER -> setScreenPosition(Vector2D.ZERO);
                case DRAW_BOTTOM_LEFT_CORNER -> setScreenPosition(0, gc.screenHeight);
                case DRAW_TOP_RIGHT_CORNER -> setScreenPosition(gc.screenWidth, 0);
                case DRAW_BOTTOM_RIGHT_CORNER -> setScreenPosition(gc.screenWidth, gc.screenHeight);
            }
            setSize(gc.screenWidth, gc.screenHeight);
        }
        else {
            switch (getDrawReference()){
                case DRAW_CENTER -> setScreenPosition(parentFrame.getDrawCenterScreenX(), parentFrame.getDrawCenterScreenY());
                case DRAW_TOP_LEFT_CORNER -> setScreenPosition(parentFrame.getDrawTopLeftScreenX(), parentFrame.getDrawTopLeftScreenY());
                case DRAW_BOTTOM_LEFT_CORNER -> setScreenPosition(parentFrame.getDrawBottomLeftScreenX(), parentFrame.getDrawBottomLeftScreenY());
                case DRAW_TOP_RIGHT_CORNER -> setScreenPosition(parentFrame.getDrawTopRightScreenX(), parentFrame.getDrawTopRightScreenY());
                case DRAW_BOTTOM_RIGHT_CORNER -> setScreenPosition(parentFrame.getDrawBottomRightScreenX(), parentFrame.getDrawBottomRightScreenY());
            }
            setSize(parentFrame.getWidth(), parentFrame.getHeight());
        }
    }

    public void addUIObject(UIObject object, String drawReference, int col, int row){
        assert col < maxCol;
        assert row < maxRow;
        object.setDrawReference(drawReference);
        FrameCase newFrameCase = new FrameCase(object, col, row);
        frameCases.add(newFrameCase);
        gc.uiM.addUIObject(object);
    }

    @Override
    public void draw(Graphics2D g2) {
        // Used to update positions
        if (isShow()){
            // SET POSITIONS
            if (updatePositionTimer <= 0) {
                updatePositionTimer = updatePositionDelay;
                if (drawOption.equals(DRAW_EVENLY)) {
                    int curStepX = getWidth() / (maxCol + 1);
                    int curStepY = getHeight() / (maxRow + 1);

                    for (FrameCase frameCase : frameCases) {
                        UIObject object = frameCase.getObject();
                        int col = frameCase.getCol();
                        int row = frameCase.getRow();

                        object.setScreenX(getDrawTopLeftScreenX() + curStepX * (col + 1));
                        object.setScreenY(getDrawTopLeftScreenY() + curStepY * (row + 1));
                    }
                } else {
                    assert false : "Wrong draw option";
                }
            }
        }
    }
}
