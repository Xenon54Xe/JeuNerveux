package com.example.app.ui.frame;

import com.example.app.GameCanvas;
import com.example.app.ui.UIObject;
import com.example.app.utils.ILinkedList;
import com.example.app.utils.LinkedList;
import com.example.app.utils.Vector2D;

import java.awt.*;

public class UIFrame extends UIObject {

    final GameCanvas gc;

    // MASTER
    private UIFrame parentFrame = null;

    // CLASS VARIABLES
    private final ILinkedList<FrameCase> frameCases = new LinkedList<>();
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

    public UIFrame(GameCanvas gc, String name, String drawReference) {
        super(name, 0, 0);

        this.gc = gc;

        setDrawReference(drawReference);

        setDrawEvenly();
        gc.uiM.addUIObject(this);

        // Initial size
        updateSize();
    }

    public UIFrame(GameCanvas gc, String name, String drawReference, int col, int row) {
        super(name, 0, 0);

        this.gc = gc;

        setDrawReference(drawReference);
        setShape(col, row);

        setDrawEvenly();
        gc.uiM.addUIObject(this);

        // Initial size
        updateSize();
    }

    public UIFrame(GameCanvas gc, String name, String drawReference,
                   int screenX, int screenY, int width, int height, int col, int row) {
        super(name, screenX, screenY);

        this.gc = gc;

        setWidth(width);
        setHeight(height);
        setDrawReference(drawReference);
        setShape(col, row);

        setDrawEvenly();
        gc.uiM.addUIObject(this);

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

    @Override
    public void setDrawReference(String drawReference) {
        for (int i = 0; i < frameCases.size(); i++){
            frameCases.getFirstValueNShift().getObject().setDrawReference(drawReference);
        }

        super.setDrawReference(drawReference);
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
        for (int i = 0; i < frameCases.size(); i++) {

            FrameCase frameCase = frameCases.getFirstValueNShift();
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

        for (int i = 0; i < frameCases.size(); i++){
            frameCases.getFirstValueNShift().getObject().setShow(show);
        }
    }

    public void expand(){
        // Set the drawReference before calling this
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

    public void addUIObject(UIObject object, int col, int row){
        assert col < maxCol;
        assert row < maxRow;
        object.setDrawReference(getDrawReference());
        FrameCase newFrameCase = new FrameCase(object, col, row);
        frameCases.add(newFrameCase);
        gc.uiM.addUIObject(object);
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

                        for (int i = 0; i < frameCases.size(); i++) {
                            FrameCase frameCase = frameCases.getFirstValueNShift();
                            UIObject object = frameCase.getObject();
                            int col = frameCase.getCol();
                            int row = frameCase.getRow();

                            object.setScreenX(getDrawTopLeftScreenX() + curStepX * (col + 1));
                            object.setScreenY(getDrawTopLeftScreenY() + curStepY * (row + 1));
                        }
                    }
                    case DRAW_STEP_BETWEEN_CENTER -> {

                        int startScreenX = getDrawReferenceScreenX();
                        int startScreenY = getDrawReferenceScreenY();
                        if (getDrawReference().equals(DRAW_CENTER)){
                            startScreenX -= cumulatedWidth / 2;
                            startScreenY -= cumulatedHeight / 2;
                        }

                        int[] mul = getDrawReferenceMultiplier();
                        int widthMul = mul[0];
                        int heightMul = mul[1];

                        for (int i = 0; i < frameCases.size(); i++) {
                            FrameCase frameCase = frameCases.getFirstValueNShift();
                            UIObject object = frameCase.getObject();
                            int col = frameCase.getCol();
                            int row = frameCase.getRow();

                            object.setScreenX(startScreenX + col * stepX * widthMul);
                            object.setScreenY(startScreenY + row * stepY * heightMul);
                        }
                    }
                    case DRAW_STEP_BETWEEN_EDGES -> {

                        int startScreenX = getDrawReferenceScreenX();
                        int startScreenY = getDrawReferenceScreenY();
                        if (getDrawReference().equals(DRAW_CENTER)){
                            startScreenX -= cumulatedWidth / 2;
                            startScreenY -= cumulatedHeight / 2;
                        }

                        System.out.println("/////////////////");
                        System.out.println(maxWidth + "  " + maxHeight);

                        int[] mul = getDrawReferenceMultiplier();
                        int widthMul = mul[0];
                        int heightMul = mul[1];

                        for (int i = 0; i < frameCases.size(); i++) {
                            FrameCase frameCase = frameCases.getFirstValueNShift();
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
}
