package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.entity.LivingEntity;
import com.example.app.tile.Tile;
import com.example.app.tile.TileMap;
import com.example.app.utils.collections.ILoopList;

import java.awt.*;
import java.util.ArrayList;

public class UIMap extends UIBox{

    final GameCanvas gc;

    public int[][] colorMapNum;
    public ArrayList<Integer[]> entitiesPositions = new ArrayList<>();
    public int squareWidth, squareHeight;
    public int nbCol, nbRow;

    public UIMap(GameCanvas gc, String name, int screenX, int screenY, int maxWidth, int maxHeight) {
        super(gc.mouseMH, null, name, screenX, screenY, maxWidth, maxHeight);

        this.gc = gc;

        setRoundCorner(false);
    }

    public int chooseColorNum(TileMap tileMap, int col, int row){
        Tile choosedTile = null;
        for (int layer = 0; layer < tileMap.getLayerCount(); layer++) {
            Tile tile = gc.tileM.tiles.getTile(tileMap.getTileNum(col, row, layer));
            if (choosedTile == null){
                choosedTile = tile;
            } else if (tile.isCollision()) {
                choosedTile = tile;
            } else if (!choosedTile.isCollision() && tile.getColorNum() != Tile.TRANSPARENT) {
                choosedTile = tile;
            }
        }

        if (choosedTile == null){
            return Tile.TRANSPARENT;
        }
        return choosedTile.getColorNum();
    }

    public void initMap(TileMap tileMap){
        colorMapNum = new int[tileMap.getMaxCol()][tileMap.getMaxRow()];
        for (int col = 0; col < tileMap.getMaxCol(); col++) {
            for (int row = 0; row < tileMap.getMaxRow(); row++) {
                colorMapNum[col][row] = chooseColorNum(tileMap, col, row);
            }
        }

        nbCol = tileMap.getMaxCol();
        nbRow = tileMap.getMaxRow();

        squareWidth = getWidth() / nbCol;
        squareHeight = getHeight() / nbRow;
    }

    public void setEntitiesPositions(ArrayList<Integer[]> entitiesPositions){
        this.entitiesPositions = entitiesPositions;
    }

    public void setEntitiesPositions(ILoopList<LivingEntity> entities){
        ArrayList<Integer[]> arrayList = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            LivingEntity entity = entities.get(true);
            Integer[] integers = new Integer[2];
            integers[0] = entity.getTileX();
            integers[1] = entity.getTileY();
            arrayList.add(integers);
        }

        setEntitiesPositions(arrayList);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()) {
            super.draw(g2);

            drawFullMap(g2);

            // Entities
            drawEntitiesPositions(g2);
        }
    }

    private void drawEntitiesPositions(Graphics2D g2){
        if (entitiesPositions.isEmpty()){
            return;
        }

        int startDrawX = getDrawRuleScreenX();
        int startDrawY = getDrawRuleScreenY();
        g2.setColor(Color.RED);

        for (Integer[] pos : entitiesPositions){
            int x = pos[0], y = pos[1];
            int drawX = startDrawX + getNextX(x, nbCol) * squareWidth * getDrawReferenceMultiplier()[0];
            int drawY = startDrawY + getNextY(y, nbRow) * squareHeight * getDrawReferenceMultiplier()[1];

            g2.fillRect(drawX, drawY, squareWidth, squareHeight);
        }
    }

    private void drawFullMap(Graphics2D g2) {
        int startDrawX = getDrawRuleScreenX();
        int startDrawY = getDrawRuleScreenY();

        for (int col = 0; col < nbCol; col++) {
            for (int row = 0; row < nbRow; row++) {

                int colorNum = colorMapNum[col][row];
                if (!(colorNum == Tile.TRANSPARENT)) {
                    int drawX = startDrawX + getNextX(col, nbCol) * squareWidth * getDrawReferenceMultiplier()[0];
                    int drawY = startDrawY + getNextY(row, nbRow) * squareHeight * getDrawReferenceMultiplier()[1];

                    g2.setColor(Tile.getRelatedColor(colorMapNum[col][row]));
                    g2.fillRect(drawX, drawY, squareWidth, squareHeight);
                }
            }
        }
    }
}
