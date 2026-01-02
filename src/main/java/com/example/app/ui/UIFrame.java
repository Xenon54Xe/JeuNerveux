package com.example.app.ui;

import com.example.app.GameCanvas;

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
        if (isDrawCentered()) {
            if (parentFrame == null) {
                setScreenX(gc.screenWidth / 2);
                setScreenY(gc.screenHeight / 2);
                setWidth(gc.screenWidth);
                setHeight(gc.screenHeight);
            } else {
                setScreenX(parentFrame.getDrawCenterScreenX());
                setScreenY(parentFrame.getDrawCenterScreenY());
                setWidth(parentFrame.getWidth());
                setHeight(parentFrame.getHeight());
            }
        }
        else {
            if (parentFrame == null) {
                setScreenX(0);
                setScreenY(0);
                setWidth(gc.screenWidth);
                setHeight(gc.screenHeight);
            } else {
                setScreenX(parentFrame.getDrawScreenX());
                setScreenY(parentFrame.getDrawScreenY());
                setWidth(parentFrame.getWidth());
                setHeight(parentFrame.getHeight());
            }
        }
    }

    public void addUIObject(UIObject object, int col, int row){
        assert col < maxCol;
        assert row < maxRow;
        object.setDrawCentered(true);
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

                        object.setScreenX(getDrawScreenX() + curStepX * (col + 1));
                        object.setScreenY(getDrawScreenY() + curStepY * (row + 1));
                    }
                } else {
                    assert false : "Wrong draw option";
                }
            }
        }
    }
}
