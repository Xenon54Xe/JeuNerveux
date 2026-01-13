package com.example.app.event.component;


import com.example.app.utils.ILinkedList;

public record ComponentChangeMap(String mapName, ILinkedList<Integer> spawnableTiles) implements IEventComponent{

    public ComponentChangeMap(String mapName, ILinkedList<Integer> spawnableTiles){
        this.mapName = mapName;
        this.spawnableTiles = spawnableTiles;
    }

    @Override
    public String getName() {
        return mapName;
    }
}
