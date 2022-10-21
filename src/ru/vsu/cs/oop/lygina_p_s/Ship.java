package ru.vsu.cs.oop.lygina_p_s;

public class Ship {
    private int deckCount;
    private Orientation orientation;
    private int aliveDeckCount;
    private boolean isAlive;

    public Ship(int deckCount, Orientation orientation) {
        this.deckCount = deckCount;
        this.orientation = orientation;
        aliveDeckCount = deckCount;
        isAlive = true;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
