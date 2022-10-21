package ru.vsu.cs.oop.lygina_p_s;

public class ShipCell extends Cell{
    private final Ship ship;

    public ShipCell(Ship ship) {
        this.ship = ship;
        this.setType(TypeOfCell.SHIP);
    }

    public Ship getShip() {
        return ship;
    }
}
