package com.example.app.utils;

import com.example.app.tile.Tile;

public class TileLinkedList extends LinkedList<Tile> {

    public TileLinkedList(){
        super();
    }

    public TileLinkedList(Tile value){
        super(value);

        value.setID(size() - 1);
    }

    public Tile getTile(int ID){
        return getTile(ID, false);
    }

    public Tile getTile(int ID, boolean reverse){
        Tile curTile = getFirstValue();
        while (curTile.getID() != ID){
            shift(reverse);
            curTile = getFirstValue();
        }
        return curTile;
    }

    public Tile getNextTile(int ID){
        return getTile(ID, false);
    }

    public Tile getPreviousTile(int ID){
        return getTile(ID, true);
    }

    @Override
    public void add(Tile value) {
        super.add(value);

        value.setID(size() - 1);
    }
}
