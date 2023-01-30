package ru.vsu.cs.oop.lygina_p_s.logic;

public class InitializationController {
    private TypeOfCell drawingState = TypeOfCell.EMPTY_CELL;
    private int deckCount = 1;
    private Orientation orientation = Orientation.HORIZONTAL;

    public void setDrawingState(TypeOfCell drawingState) {
        this.drawingState = drawingState;
    }

    public TypeOfCell getDrawingState() {
        return drawingState;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setDeckCount(int deckCount) {
        this.deckCount = deckCount;
    }

    private boolean isCellEmpty(Player player, int i, int j){
        return player.getCell(i, j).getType() == TypeOfCell.EMPTY_CELL;
    }

    private boolean isCellSubmarine(Player player, int i, int j){
        return player.getCell(i, j).getType() == TypeOfCell.SUBMARINE;
    }

    private boolean hasCellNoNeighbours(Player player, int i, int j){
        for (int r = i-1; r <= i+1; r++){
            for (int c = j-1; c <= j+1; c++){
                if (r != i || c != j){
                    if (r >= 0 && r < Game.TABLE_SIZE && c >= 0 && c < Game.TABLE_SIZE)
                        if (!isCellEmpty(player, r, c) && !isCellSubmarine(player, r, c))
                            return false;
                }
            }
        }
        return true;
    }

    public boolean createMine(Player player, int i, int j){
        if (isCellEmpty(player, i, j) && hasCellNoNeighbours(player, i, j)) {
            player.setCellType(i, j, TypeOfCell.MINE);
            return true;
        }
        return false;
    }

    public boolean createMinesweeper(Player player, int i, int j) {
        if (isCellEmpty(player, i, j) && hasCellNoNeighbours(player, i, j)) {
            player.setCellType(i, j, TypeOfCell.MINESWEEPER);
            return true;
        }
        return false;
    }

    public boolean createSubmarine(Player player, int i, int j) {
        if (isCellEmpty(player, i, j)) {
            player.setCellType(i, j, TypeOfCell.SUBMARINE);
            return true;
        }
        return false;
    }

    public boolean createShip(Player player, int i, int j, Ship ship){
        int start = (ship.getOrientation() == Orientation.VERTICAL)?j:i;
        for (int ind = start; ind < start + ship.getDeckCount(); ind++){
            int row = (ship.getOrientation() == Orientation.VERTICAL)?i:ind;
            int col = (ship.getOrientation() == Orientation.VERTICAL)?ind:j;
            if (!isCellEmpty(player, row, col) || !hasCellNoNeighbours(player, row, col))
                return false;
        }
        for (int ind = start; ind < start + ship.getDeckCount(); ind++){
            int row = (ship.getOrientation() == Orientation.VERTICAL)?i:ind;
            int col = (ship.getOrientation() == Orientation.VERTICAL)?ind:j;
            player.setCell(row, col, new Cell(ship, row, col));
        }
        player.setAliveShipsCount(player.getAliveShipsCount()+1);
        return true;
    }

    public boolean createComponent(Player player, int i, int j){
        return switch (getDrawingState()) {
            case MINE -> createMine(player, i, j);
            case MINESWEEPER -> createMinesweeper(player, i, j);
            case SUBMARINE -> createSubmarine(player, i, j);
            case SHIP -> createShip(player, i, j, new Ship(getDeckCount(), getOrientation()));
            default -> false;
        };
    }
}
