package ru.vsu.cs.oop.lygina_p_s;

public class Cell {
    private boolean isHit = false;
    private TypeOfCell type = TypeOfCell.EMPTY_CELL;

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
