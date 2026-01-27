package com.example.app.event.component;


import com.example.app.utils.ILoopList;

public record ComponentChangeMap(String mapName, ILoopList<Integer> spawnableTiles) implements IEventComponent{

    public ComponentChangeMap(String mapName, ILoopList<Integer> spawnableTiles){
        this.mapName = mapName;
        this.spawnableTiles = spawnableTiles;
    }

    @Override
    public String getName() {
        return mapName;
    }
}
