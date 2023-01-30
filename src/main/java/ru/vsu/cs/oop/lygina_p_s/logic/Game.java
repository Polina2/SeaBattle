package ru.vsu.cs.oop.lygina_p_s.logic;

import ru.vsu.cs.oop.lygina_p_s.Drawer;
import ru.vsu.cs.oop.lygina_p_s.FieldView;

import java.util.Random;

import static ru.vsu.cs.oop.lygina_p_s.logic.GameState.*;

public class Game {
    public static final int TABLE_SIZE = 10;

    private GameState gameState;
    private Player player1;
    private Player player2;
    private InitializationController controller;
    private Drawer drawer;
    private boolean actionConfirmed = false;
    private boolean turnFinished = false;

    public boolean isTurnFinished() {
        return turnFinished;
    }

    public void setTurnFinished(boolean turnFinished) {
        this.turnFinished = turnFinished;
    }

    public boolean isActionConfirmed() {
        return actionConfirmed;
    }

    public void setActionConfirmed(boolean actionConfirmed) {
        this.actionConfirmed = actionConfirmed;
    }

    public Game(){

    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    public void run(){
        player1 = new Player();
        player2 = new Player();
        controller = new InitializationController();
        gameState = GameState.CREATING_FIELD_1;
    }

    public void changeDrawingStateAction(TypeOfCell type){
        controller.setDrawingState(type);
    }

    public void changeDrawingStateAction(TypeOfCell type, int deckCount){
        controller.setDrawingState(type);
        controller.setDeckCount(deckCount);
    }

    public void changeGameStateAction(){
        if (getGameState() == CREATING_FIELD_1)
            changeGameState(CREATING_FIELD_2);
        else if (getGameState() == TURN_1 && isTurnFinished())
            changeGameState(TURN_2);
        else if (getGameState()==CREATING_FIELD_2 || (getGameState()==TURN_2 && isTurnFinished()))
            changeGameState(TURN_1);
        else if (getGameState()==END)
            changeGameState(END);
    }

    public void changeGameState(GameState state){
        setGameState(state);
        controller.setDrawingState(TypeOfCell.EMPTY_CELL);
        controller.setOrientation(Orientation.HORIZONTAL);
        setActionConfirmed(false);
        setTurnFinished(false);
        drawer.updateScene();
    }

    public void changeOrientation(Orientation orientation){
        controller.setOrientation(orientation);
    }

    public void confirmAction(){
        setActionConfirmed(true);
        drawer.updateScene();
    }

    public void cellCreatingFieldAction(int i, int j){
        FieldView fieldView = (getGameState() == CREATING_FIELD_1)?drawer.getFieldView1():drawer.getFieldView2();
        Player player = (getGameState() == CREATING_FIELD_1)?player1:player2;
        if(controller.createComponent(player, i, j)) {
            if (controller.getDrawingState() == TypeOfCell.SHIP){
                int start = (controller.getOrientation() == Orientation.VERTICAL)?j:i;
                for (int ind = start; ind < start + controller.getDeckCount(); ind++){
                    int row = (controller.getOrientation() == Orientation.VERTICAL)?i:ind;
                    int col = (controller.getOrientation() == Orientation.VERTICAL)?ind:j;
                    drawer.drawComponent(fieldView, row, col, controller.getDrawingState());
                }
            } else
                drawer.drawComponent(fieldView, i, j, controller.getDrawingState());
        }
    }

    public void cellTurnAction(int i, int j){
        if (isTurnFinished())
            return;
        turn(i, j);
    }

    public void setCellAction(){
        if (getGameState() == CREATING_FIELD_1 || getGameState() == CREATING_FIELD_2) {
            drawer.setCellCreatingFieldAction();
        } else if (getGameState() == TURN_1 || getGameState() == TURN_2){
            drawer.setCellTurnAction();
        }
    }

    public void turn(int row, int col){
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
        if (cell.isHit())
            return;
        cell.setHit(true);

        FieldView fieldViewVictim = (victim == player2)? drawer.getFieldViewVictim2():drawer.getFieldViewVictim1();
        FieldView fieldViewAttackingVictim = (victim == player2)? drawer.getFieldView2():drawer.getFieldView1();
        drawer.drawHitCell(
                fieldViewVictim, row, col,
                victim.getCell(row, col).getType()
        );
        drawer.drawHitCell(
                fieldViewAttackingVictim, row, col,
                victim.getCell(row, col).getType()
        );

        if (cell.getType() == TypeOfCell.SHIP){
            cell.getShip().decreaseAliveDeckCount();
            if (!cell.getShip().isAlive())
                decreaseAliveShipsCount(victim);
            if (victim.getAliveShipsCount() == 0)
                gameState = GameState.END;
        } else {
            if (cell.getType() == TypeOfCell.SUBMARINE){
                fire(attacker, victim, row, col);
            } else if (cell.getType() == TypeOfCell.MINE){
                FieldView fieldViewAttacker = (attacker == player1)?drawer.getFieldViewVictim1():drawer.getFieldViewVictim2();
                Cell randomShipCell = getRandomShipCell(attacker);
                if (randomShipCell != null)
                    drawer.drawDiscoveredCell(fieldViewAttacker, randomShipCell.getRow(), randomShipCell.getCol(), TypeOfCell.SHIP);
            }else if (cell.getType() == TypeOfCell.MINESWEEPER){
                FieldView fieldViewAttacker = (attacker == player1)?drawer.getFieldViewVictim1():drawer.getFieldViewVictim2();
                Cell randomMineCell = getRandomMineCell(attacker);
                if (randomMineCell != null)
                    drawer.drawDiscoveredCell(fieldViewAttacker, randomMineCell.getRow(), randomMineCell.getCol(), TypeOfCell.MINE);
            }
            turnFinished = true;
        }
    }

    private Cell getRandomShipCell(Player player){
        Random random = new Random();
        int number = random.nextInt(getShipCellCount(player));
        for (int i = 0; i < TABLE_SIZE; i++){
            for (int j = 0; j < TABLE_SIZE; j++){
                if (player.getCell(i, j).getType() == TypeOfCell.SHIP && !player.getCell(i, j).isHit()){
                    number--;
                }
                if (number == 0){
                    return player.getCell(i, j);
                }
            }
        }
        return null;
    }

    private Cell getRandomMineCell(Player player){
        Random random = new Random();
        int number = random.nextInt(getMineCellCount(player));
        for (int i = 0; i < TABLE_SIZE; i++){
            for (int j = 0; j < TABLE_SIZE; j++){
                if (player.getCell(i, j).getType() == TypeOfCell.MINE && !player.getCell(i, j).isHit()){
                    number--;
                }
                if (number == 0){
                    return player.getCell(i, j);
                }
            }
        }
        return null;
    }

    private int getShipCellCount(Player player){
        int count = 0;
        for (int i = 0; i < TABLE_SIZE; i++){
            for (int j = 0; j < TABLE_SIZE; j++){
                if (player.getCell(i, j).getType() == TypeOfCell.SHIP && !player.getCell(i, j).isHit())
                    count++;
            }
        }
        return count;
    }

    private int getMineCellCount(Player player){
        int count = 0;
        for (int i = 0; i < TABLE_SIZE; i++){
            for (int j = 0; j < TABLE_SIZE; j++){
                if (player.getCell(i, j).getType() == TypeOfCell.MINE && !player.getCell(i, j).isHit())
                    count++;
            }
        }
        return count;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void decreaseAliveShipsCount(Player player){
        player.setAliveShipsCount(player.getAliveShipsCount() - 1);
    }
}
