package com.example.app.event.component;


import com.example.app.utils.collections.List;

public record ComponentChangeMap(String mapName, List<Integer> spawnableTiles) implements IEventComponent{

    public ComponentChangeMap(String mapName, List<Integer> spawnableTiles){
        this.mapName = mapName;
        this.spawnableTiles = spawnableTiles;
    }

    @Override
    public String getName() {
        return mapName;
    }
}
