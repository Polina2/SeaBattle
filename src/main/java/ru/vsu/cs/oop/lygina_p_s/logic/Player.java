package ru.vsu.cs.oop.lygina_p_s.logic;

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

    public Cell getCell(int i, int j){
        if (i >= 0 && i < Game.TABLE_SIZE && j >= 0 && j < Game.TABLE_SIZE)
            return cells[i][j];
        else
            return null;
    }

    public void setCell(int i, int j, Cell cell){
        if (i >= 0 && i < Game.TABLE_SIZE && j >= 0 && j < Game.TABLE_SIZE)
            cells[i][j] = cell;
    }

    public int getAliveShipsCount() {
        return aliveShipsCount;
    }

    public void setAliveShipsCount(int aliveShipsCount) {
        this.aliveShipsCount = aliveShipsCount;
    }

    public void setCellType(int i, int j, TypeOfCell type){
        getCell(i, j).setType(type);
    }

    public void setCellHit(int i, int j){
        getCell(i, j).setHit(true);
    }
}
