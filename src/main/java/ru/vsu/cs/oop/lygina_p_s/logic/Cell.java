package ru.vsu.cs.oop.lygina_p_s.logic;

public class Cell {
    private boolean isHit = false;
    private TypeOfCell type = TypeOfCell.EMPTY_CELL;
    private Ship ship = null;
    private int row;
    private int col;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
    }

    public Cell(Ship ship, int row, int col) {
        this.row = row;
        this.col = col;
        this.ship = ship;
        this.setType(TypeOfCell.SHIP);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
