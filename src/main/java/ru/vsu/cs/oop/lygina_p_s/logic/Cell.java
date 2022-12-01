package ru.vsu.cs.oop.lygina_p_s.logic;

public class Cell {
    private boolean isHit = false;
    private TypeOfCell type = TypeOfCell.EMPTY_CELL;
    private Ship ship = null;

    public Cell(){

    }

    public Cell(Ship ship) {
        this.ship = ship;
        this.setType(TypeOfCell.SHIP);
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public TypeOfCell getType() {
        return type;
    }

    public void setType(TypeOfCell type) {
        this.type = type;
    }
}
