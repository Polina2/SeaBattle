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
        if (i > 0 && i < Game.TABLE_SIZE && j > 0 && j < Game.TABLE_SIZE)
            return cells[i][j];
        else
            return null;
    }

    public int getAliveShipsCount() {
        return aliveShipsCount;
    }

    public void decreaseAliveShipsCount(){
        aliveShipsCount--;
    }

    public void setCellType(int i, int j, TypeOfCell type){
        getCell(i, j).setType(type);
    }

    public void setCellHit(int i, int j){
        getCell(i, j).setHit(true);
    }

    private boolean isCellEmpty(int i, int j){
        return getCell(i, j).getType() == TypeOfCell.EMPTY_CELL;
    }

    private boolean hasCellNoNeighbours(int i, int j){
        for (int r = i-1; r <= i+1; r++){
            for (int c = j-1; c <= j+1; c++){
                if (r != i || c != j){
                    if (!isCellEmpty(r, c))
                        return false;
                }
            }
        }
        return true;
    }

    public void createMine(int i, int j){
        if (isCellEmpty(i, j) && hasCellNoNeighbours(i, j))
            setCellType(i, j, TypeOfCell.MINE);
    }

    public void createMinesweeper(int i, int j) {
        if (isCellEmpty(i, j) && hasCellNoNeighbours(i, j))
            setCellType(i, j, TypeOfCell.MINESWEEPER);
    }

    public void createSubmarine(int i, int j) {
        if (isCellEmpty(i, j))
            setCellType(i, j, TypeOfCell.SUBMARINE);
    }

    public void createShip(int i, int j, Ship ship){
        int start = (ship.getOrientation() == Orientation.HORIZONTAL)?j:i;
        for (int ind = start; ind < start + ship.getDeckCount(); ind++){
            int row = (ship.getOrientation() == Orientation.HORIZONTAL)?i:ind;
            int col = (ship.getOrientation() == Orientation.HORIZONTAL)?ind:j;
            if (!isCellEmpty(row, col) || !hasCellNoNeighbours(row, col))
                return;
        }
        for (int ind = start; ind < start + ship.getDeckCount(); ind++){
            int row = (ship.getOrientation() == Orientation.HORIZONTAL)?i:ind;
            int col = (ship.getOrientation() == Orientation.HORIZONTAL)?ind:j;
            cells[row][col] = new Cell(ship);
        }
    }
}
