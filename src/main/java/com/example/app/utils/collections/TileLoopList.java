package com.example.app.utils.collections;

import com.example.app.tile.Tile;

public class TileLoopList extends LoopList<Tile> {

    public TileLoopList(){
        super();
    }

    public TileLoopList(Tile value){
        super(value);

        value.setID(size() - 1);
    }

    public Tile getTile(int ID){
        return getTile(ID, false);
    }

    public Tile getTile(int ID, boolean reverse){
        Tile curTile = get();
        while (curTile.getID() != ID){
            shift(reverse);
            curTile = get();
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
