package net.rubicon.event;

import java.util.ArrayList;

public record ComponentChangeMap(String mapName, ArrayList<Integer> spawnableTiles) implements IEventComponent{

    public ComponentChangeMap(String mapName, ArrayList<Integer> spawnableTiles){
        this.mapName = mapName;
        this.spawnableTiles = spawnableTiles;
    }

    @Override
    public String getName() {
        return mapName;
    }
}
