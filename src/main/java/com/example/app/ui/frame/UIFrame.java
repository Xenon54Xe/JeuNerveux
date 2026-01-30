package com.example.app.ui.frame;

import com.example.app.DrawOther;
import com.example.app.GameCanvas;
import com.example.app.Updatable;
import com.example.app.ui.UIObject;
import com.example.app.utils.Vector2D;
import com.example.app.utils.collections.LinkedList;
import com.example.app.utils.collections.List;

import java.awt.*;
import java.util.ArrayList;

public class UIFrame extends UIObject implements Updatable, DrawOther {

    final GameCanvas gc;

    // MASTER
    private UIFrame parentFrame = null;

    // CLASS VARIABLES
    private final List<FrameCase> frameCases = new LinkedList<>();
    private final List<FrameCase> toAddBuffer = new LinkedList<>();
    private final List<FrameCase> toRemoveBuffer = new LinkedList<>();
    private int maxCol, maxRow; // To place ui

    // DRAW OPTIONS
    public final static String DRAW_EVENLY = "draw-evenly";
    public final static String DRAW_STEP_BETWEEN_CENTER = "draw-step-center";
    public final static String DRAW_STEP_BETWEEN_EDGES = "draw-step-edges";
    private String drawOption = DRAW_EVENLY;
    private final int updatePositionDelay = 60;
    private int updatePositionTimer = 0;

    // DRAW CHARACTERISTICS
    private int stepX, stepY;
    private int maxWidth, maxHeight;
    private int cumulatedWidth, cumulatedHeight; // max cumulated ui size

    private int firstTime = 1;

    public UIFrame(GameCanvas gc, String name, int drawReference) {
        super(name, 0, 0);

        this.gc = gc;

        setDrawRule(drawReference);

        setDrawEvenly();
        gc.uiM.safeAddFrame(this);

        // Initial size
        updateSize();
    }

    public UIFrame(GameCanvas gc, String name, int drawRule, int col, int row) {
        super(name, 0, 0);

        this.gc = gc;

        setDrawRule(drawRule);
        setShape(col, row);

        setDrawEvenly();
        gc.uiM.safeAddFrame(this);

        // Initial size
        updateSize();
    }

    public UIFrame(GameCanvas gc, String name, int drawRule,
                   int screenX, int screenY, int width, int height, int col, int row) {
        super(name, screenX, screenY);

        this.gc = gc;

        setWidth(width);
        setHeight(height);
        setDrawRule(drawRule);
        setShape(col, row);

        setDrawEvenly();
        gc.uiM.safeAddFrame(this);

        // Initial size
        updateSize();
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

    public void setDrawRule(int drawRule) {
        for (FrameCase frameCase : frameCases){
            frameCase.getObject().setDrawRule(drawRule);
        }

        super.setDrawRule(drawRule);
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

    public void updateSize(){
        // Get max width & height
        maxWidth = 0;
        maxHeight = 0;

        for (FrameCase frameCase : frameCases){
            UIObject object = frameCase.getObject();
            if (object.getWidth() > maxWidth) {
                maxWidth = object.getWidth();
            }
            if (object.getHeight() > maxHeight) {
                maxHeight = object.getHeight();
            }
        }

        cumulatedWidth = maxWidth * maxCol;
        cumulatedHeight = maxHeight * maxRow;
    }

    @Override
    public void setShow(boolean show) {
        super.setShow(show);

        for (FrameCase frameCase : frameCases){
            frameCase.getObject().setShow(show);
        }
    }

    @Override
    public boolean isMouseOver() {
        return false;
    }

    public void expand(){
        // Set the drawReference before calling this
        if(parentFrame == null){
            switch (getDrawRule()) {
                case DRAW_CENTER -> setScreenPosition(gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2);
                case DRAW_TOP_LEFT -> setScreenPosition(Vector2D.ZERO);
                case DRAW_BOTTOM_LEFT -> setScreenPosition(0, gc.SCREEN_HEIGHT);
                case DRAW_TOP_RIGHT -> setScreenPosition(gc.SCREEN_WIDTH, 0);
                case DRAW_BOTTOM_RIGHT -> setScreenPosition(gc.SCREEN_WIDTH, gc.SCREEN_HEIGHT);
            }
            setSize(gc.SCREEN_WIDTH, gc.SCREEN_HEIGHT);
        }
        else {
            switch (getDrawRule()){
                case DRAW_CENTER -> setScreenPosition(parentFrame.getDrawCenterScreenX(), parentFrame.getDrawCenterScreenY());
                case DRAW_TOP_LEFT -> setScreenPosition(parentFrame.getDrawTopLeftScreenX(), parentFrame.getDrawTopLeftScreenY());
                case DRAW_BOTTOM_LEFT -> setScreenPosition(parentFrame.getDrawBottomLeftScreenX(), parentFrame.getDrawBottomLeftScreenY());
                case DRAW_TOP_RIGHT -> setScreenPosition(parentFrame.getDrawTopRightScreenX(), parentFrame.getDrawTopRightScreenY());
                case DRAW_BOTTOM_RIGHT -> setScreenPosition(parentFrame.getDrawBottomRightScreenX(), parentFrame.getDrawBottomRightScreenY());
            }
            setSize(parentFrame.getWidth(), parentFrame.getHeight());
        }
    }

    public void addUIObject(UIObject object, int col, int row){
        assert col < maxCol;
        assert row < maxRow;
        object.setDrawRule(getDrawRule());
        object.setShow(isShow());
        FrameCase newFrameCase = new FrameCase(object, col, row);
        frameCases.add(newFrameCase);
    }

    @Override
    public void draw(Graphics2D g2) {
        // Used to update positions
        if (isShow()){

            // SET POSITIONS
            updatePositionTimer--;
            if (updatePositionTimer <= 0 || firstTime >= 0) {
                if (firstTime >= 0){
                    firstTime--;
                }
                updateSize();

                updatePositionTimer = updatePositionDelay;
                switch (drawOption) {
                    case DRAW_EVENLY -> {

                        int curStepX = getWidth() / (maxCol + 1);
                        int curStepY = getHeight() / (maxRow + 1);

                        for (FrameCase frameCase : frameCases){
                            UIObject object = frameCase.getObject();
                            int col = frameCase.getCol();
                            int row = frameCase.getRow();

                            object.setScreenX(getDrawTopLeftScreenX() + curStepX * (col + 1));
                            object.setScreenY(getDrawTopLeftScreenY() + curStepY * (row + 1));
                        }
                    }
                    case DRAW_STEP_BETWEEN_CENTER -> {

                        int startScreenX = getDrawRuleScreenX();
                        int startScreenY = getDrawRuleScreenY();
                        if (getDrawRule() == DRAW_CENTER){
                            startScreenX -= cumulatedWidth / 2;
                            startScreenY -= cumulatedHeight / 2;
                        }

                        int[] mul = getDrawReferenceMultiplier();
                        int widthMul = mul[0];
                        int heightMul = mul[1];

                        for (FrameCase frameCase : frameCases){
                            UIObject object = frameCase.getObject();
                            int col = frameCase.getCol();
                            int row = frameCase.getRow();

                            object.setScreenX(startScreenX + col * stepX * widthMul);
                            object.setScreenY(startScreenY + row * stepY * heightMul);
                        }
                    }
                    case DRAW_STEP_BETWEEN_EDGES -> {

                        int startScreenX = getDrawRuleScreenX();
                        int startScreenY = getDrawRuleScreenY();
                        if (getDrawRule() == DRAW_CENTER){
                            startScreenX -= cumulatedWidth / 2;
                            startScreenY -= cumulatedHeight / 2;
                        }

                        System.out.println("/////////////////");
                        System.out.println(maxWidth + "  " + maxHeight);

                        int[] mul = getDrawReferenceMultiplier();
                        int widthMul = mul[0];
                        int heightMul = mul[1];

                        for (FrameCase frameCase : frameCases){
                            UIObject object = frameCase.getObject();
                            int col = frameCase.getCol();
                            int row = frameCase.getRow();

                            object.setScreenX(startScreenX + col * widthMul * (stepX + maxWidth));
                            object.setScreenY(startScreenY + row * heightMul * (stepY + maxHeight));
                        }
                    }
                    default -> {

                        assert false : "Wrong draw option";
                    }
                }
            }
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public void update() {
        if (isActive()){
            // REMOVE BUFFER
            for (FrameCase frameCase : toRemoveBuffer) {
                frameCases.remove(frameCase);
            }
            toRemoveBuffer.clear();

            // ADD BUFFER
            for (FrameCase frameCase : toAddBuffer) {
                frameCases.add(frameCase);
            }
            toAddBuffer.clear();

            // UPDATE OBJECTS
            for (FrameCase frameCase : frameCases){
                UIObject object = frameCase.getObject();
                if (object instanceof Updatable updatable){
                    updatable.update();
                }
                if (object.isMouseOver()) {
                    gc.uiM.setMouseOverUI();
                }
            }
        }
    }
}
