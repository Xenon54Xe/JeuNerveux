package net.rubicon.utils;

import net.rubicon.tile.Tile;

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

    public Tile getTile(int ID, boolean transparent, boolean reverse){
        Tile curTile = getTile(ID);
        if (transparent && curTile.transparent){
            return curTile;
        } else if (!transparent && !curTile.transparent) {
            return curTile;
        }

        for (int i = 0; i < size(); i++) {
            shift(reverse);
            curTile = getFirstValue();
            if (transparent && curTile.transparent) {
                return curTile;
            } else if (!transparent && !curTile.transparent) {
                return curTile;
            }
        }

        return null;
    }

    public Tile getNextComplete(int ID){
        return getTile(ID, false, false);
    }

    public Tile getPreviousComplete(int ID){
        return getTile(ID, false, true);
    }

    public Tile getNextTransparent(int ID){
        return getTile(ID, true, false);
    }

    public Tile getPreviousTransparent(int ID){
        return getTile(ID, true, true);
    }

    @Override
    public void add(Tile value) {
        super.add(value);

        value.setID(size() - 1);
    }
}
