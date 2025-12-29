package com.example.app.event;

public record ComponentCreateMap(String name, int nbCol, int nbRow, int nbLayer) implements IEventComponent {

    public ComponentCreateMap(String name, int nbCol, int nbRow, int nbLayer){
        this.name = name;
        this.nbCol = nbCol;
        this.nbRow = nbRow;
        this.nbLayer = nbLayer;
    }

    @Override
    public String getName() {
        return name + " : " + nbCol + ", " + nbRow + ", " + nbLayer;
    }
}
