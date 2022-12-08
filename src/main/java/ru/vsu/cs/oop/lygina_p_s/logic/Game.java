package ru.vsu.cs.oop.lygina_p_s.logic;

public class Game {
    public static final int TABLE_SIZE = 10;

    private GameState gameState;
    private Player player1;
    private Player player2;
    private Controller controller;
    private boolean actionConfirmed = false;

    public boolean isActionConfirmed() {
        return actionConfirmed;
    }

    public void setActionConfirmed(boolean actionConfirmed) {
        this.actionConfirmed = actionConfirmed;
    }

    public Game(){

    }

    public void run(){
        player1 = new Player();
        player2 = new Player();
        controller = new Controller();
        gameState = GameState.CREATING_FIELD_1;
        //update view
    }

    private void turn(int row, int col){
        Player victim, attacker;
        if (gameState == GameState.TURN_1) {
            victim = player2;
            attacker = player1;
        } else {
            victim = player1;
            attacker = player2;
        }
        fire(victim, attacker, row, col);
    }

    private void fire(Player victim, Player attacker, int row, int col){
        Cell cell = victim.getCell(row, col);
        cell.setHit(true);
        if (cell.getType() == TypeOfCell.SHIP){
            cell.getShip().decreaseAliveDeckCount();
            if (!cell.getShip().isAlive())
                controller.decreaseAliveShipsCount(victim);
            if (victim.getAliveShipsCount() == 0)
                gameState = GameState.END;
        } else {
            if (cell.getType() == TypeOfCell.SUBMARINE){
                fire(attacker, victim, row, col);
            } else if (cell.getType() == TypeOfCell.MINE){
                //ship coordinates
            }else if (cell.getType() == TypeOfCell.MINESWEEPER){
                //mine coordinates
            }
            if (gameState == GameState.TURN_1)
                gameState = GameState.TURN_2;
            else
                gameState = GameState.TURN_1;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
