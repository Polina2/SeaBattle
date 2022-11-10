package ru.vsu.cs.oop.lygina_p_s;

public class Ship {
    private final int deckCount;
    private final Orientation orientation;
    private int aliveDeckCount;
    private boolean isAlive;

    public Ship(int deckCount, Orientation orientation) {
        this.deckCount = deckCount;
        this.orientation = orientation;
        aliveDeckCount = deckCount;
        isAlive = true;
    }

    public void decreaseAliveDeckCount(){
        aliveDeckCount--;
        if (aliveDeckCount == 0)
            isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
