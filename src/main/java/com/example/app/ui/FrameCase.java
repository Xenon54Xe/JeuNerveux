package com.example.app.ui;

public class FrameCase {

    private final UIObject object;
    private int col, row;

    public FrameCase(UIObject object, int col, int row){
        this.object = object;
        this.col = col;
        this.row = row;
    }

    public UIObject getObject() {
        return object;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
