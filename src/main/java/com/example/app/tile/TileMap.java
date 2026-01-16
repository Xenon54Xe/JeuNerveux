package com.example.app.tile;

public class TileMap {

    private final int maxCol, maxRow, layerCount;
    private final int[][][] tileMapNum;

    public TileMap(int[][][] tileMapNum){
        this.tileMapNum = tileMapNum;

        maxCol = tileMapNum.length;
        maxRow = tileMapNum[0].length;
        layerCount = tileMapNum[0][0].length;
    }

    public TileMap(int col, int row, int layer){
        this.tileMapNum = new int[col][row][layer];

        maxCol = col;
        maxRow = row;
        layerCount = layer;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public int getTileNum(int col, int row, int layer){
        return tileMapNum[col][row][layer];
    }

    public void setTileNum(int value, int col, int row, int layer){
        tileMapNum[col][row][layer] = value;
    }
}
