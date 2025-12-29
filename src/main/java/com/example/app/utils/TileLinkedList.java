package com.example.app.utils;

import com.example.app.tile.Tile;

public class TileLinkedList extends LinkedList<Tile>{

    public TileLinkedList(){
        super();
    }

    public TileLinkedList(Tile value){
        super(value);

        value.setID(size() - 1);
    }

    public Tile getTile(int ID){
        Tile curTile = getFirstValue();
        while (curTile.getID() != ID){
            shift(false);
            curTile = getFirstValue();
        }
        return curTile;
    }

    public Tile getTile(int ID, int layer, boolean reverse){
        Tile curTile = getTile(ID);
        if (curTile.layerContains(layer)){
            return curTile;
        }

        for (int i = 0; i < size(); i++) {
            shift(reverse);
            curTile = getFirstValue();
            if (curTile.layerContains(layer)) {
                return curTile;
            }
        }

        return null;
    }

    public Tile getNextLayerTile(int ID, int layer){
        return getTile(ID, layer, false);
    }

    public Tile getPreviousLayerTile(int ID, int layer){
        return getTile(ID, layer, true);
    }

    @Override
    public void add(Tile value) {
        super.add(value);

        value.setID(size() - 1);
    }
}
