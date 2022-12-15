package ru.vsu.cs.oop.lygina_p_s.logic;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ru.vsu.cs.oop.lygina_p_s.Drawer;
import ru.vsu.cs.oop.lygina_p_s.FieldView;

import static ru.vsu.cs.oop.lygina_p_s.logic.GameState.*;

public class Game {
    public static final int TABLE_SIZE = 10;

    private GameState gameState;
    private Player player1;
    private Player player2;
    private Controller controller;
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

    public void run(){
        player1 = new Player();
        player2 = new Player();
        controller = new Controller();
        gameState = GameState.CREATING_FIELD_1;
    }

    public void changeDrawingStateAction(TypeOfCell type){
        controller.setDrawingState(type);
    }

    public void changeDrawingStateAction(TypeOfCell type, int deckCount){
        controller.setDrawingState(type);
        controller.setDeckCount(deckCount);
    }

    public void changeGameStateAction(Drawer drawer){
        if (getGameState() == CREATING_FIELD_1)
            controller.changeGameState(this, drawer, CREATING_FIELD_2);
        else if (getGameState() == TURN_1 && isTurnFinished())
            controller.changeGameState(this, drawer, TURN_2);
        else if (getGameState()==CREATING_FIELD_2 || (getGameState()==TURN_2 && isTurnFinished()))
            controller.changeGameState(this, drawer, TURN_1);
    }

    public void changeOrientation(Orientation orientation){
        controller.setOrientation(orientation);
    }

    public void confirmAction(Drawer drawer){
        setActionConfirmed(true);
        drawer.getStage().setScene(drawer.getScene());
    }
    //maybe i'll correct it
    private void setCellCreatingFieldAction(Drawer drawer){
        FieldView fieldView = (getGameState() == CREATING_FIELD_1)?drawer.getFieldView1():drawer.getFieldView2();
        for (int i = 0; i < Game.TABLE_SIZE; i++) {
            for (int j = 0; j < Game.TABLE_SIZE; j++) {
                Node node = getNodeFromGridPane(fieldView, i, j);
                int finalI = i;
                int finalJ = j;
                node.setOnMouseClicked(e -> {
                    if(controller.createComponent(fieldView.getPlayer(), finalI, finalJ)) {
                        if (controller.getDrawingState() == TypeOfCell.SHIP){
                            int start = (controller.getOrientation() == Orientation.VERTICAL)?finalJ:finalI;
                            for (int ind = start; ind < start + controller.getDeckCount(); ind++){
                                int row = (controller.getOrientation() == Orientation.VERTICAL)?finalI:ind;
                                int col = (controller.getOrientation() == Orientation.VERTICAL)?ind:finalJ;
                                drawer.drawComponent((StackPane) getNodeFromGridPane(fieldView, row, col), controller.getDrawingState());
                            }
                        } else
                            drawer.drawComponent((StackPane) node, controller.getDrawingState());
                    }
                });
            }
        }
    }

    private void setCellTurnAction(Drawer drawer){
        FieldView fieldView = (getGameState()==TURN_1)? drawer.getFieldViewVictim2():drawer.getFieldViewVictim1();
        for (int i = 0; i < Game.TABLE_SIZE; i++) {
            for (int j = 0; j < Game.TABLE_SIZE; j++) {
                Node node = getNodeFromGridPane(fieldView, i, j);
                int finalI = i;
                int finalJ = j;
                node.setOnMouseClicked(e -> {
                    if (isTurnFinished())
                        return;
                    turn(finalI, finalJ);
                    drawer.drawHitCell(
                            (StackPane) getNodeFromGridPane(fieldView, finalI, finalJ),
                            fieldView.getPlayer().getCell(finalI, finalJ).getType()
                    );
                });
            }
        }
    }

    public void setCellAction(Drawer drawer){
        if (getGameState() == CREATING_FIELD_1 || getGameState() == CREATING_FIELD_2) {
            setCellCreatingFieldAction(drawer);
        } else if (getGameState() == TURN_1 || getGameState() == TURN_2){
            setCellTurnAction(drawer);
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
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
            turnFinished = true;
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
