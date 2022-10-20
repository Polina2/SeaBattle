package ru.vsu.cs.oop.lygina_p_s;

public class Player {
    private Cell[][] cells = new Cell[Game.TABLE_SIZE][Game.TABLE_SIZE];
    private int aliveShipsCount = 10;

    public Player() {
        for (int i = 0; i < Game.TABLE_SIZE; i++){
            for (int j = 0; j < Game.TABLE_SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell getCage(int i, int j){
        return cells[i][j];
    }

    public void setCageType(int i, int j, TypeOfCell type){
        cells[i][j].setType(type);
    }

    public void setCageHit(int i, int j){
        cells[i][j].setHit(true);
    }
}
