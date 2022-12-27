package ru.vsu.cs.oop.lygina_p_s.logic;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.vsu.cs.oop.lygina_p_s.Drawer;
import ru.vsu.cs.oop.lygina_p_s.FieldView;

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
        Stage stage = drawer.getStage();
        setGameState(state);
        controller.setDrawingState(TypeOfCell.EMPTY_CELL);
        setActionConfirmed(false);
        setTurnFinished(false);
        stage.setScene(drawer.getScene());
    }

    public void changeOrientation(Orientation orientation){
        controller.setOrientation(orientation);
    }

    public void confirmAction(){
        setActionConfirmed(true);
        drawer.getStage().setScene(drawer.getScene());
    }

    private void setCellCreatingFieldAction(){
        FieldView fieldView = (getGameState() == CREATING_FIELD_1)?drawer.getFieldView1():drawer.getFieldView2();
        Player player = (getGameState() == CREATING_FIELD_1)?player1:player2;
        for (int i = 0; i < Game.TABLE_SIZE; i++) {
            for (int j = 0; j < Game.TABLE_SIZE; j++) {
                Node node = drawer.getNodeFromGridPane(fieldView, i, j);
                int finalI = i;
                int finalJ = j;
                node.setOnMouseClicked(e -> {
                    if(controller.createComponent(player, finalI, finalJ)) {
                        if (controller.getDrawingState() == TypeOfCell.SHIP){
                            int start = (controller.getOrientation() == Orientation.VERTICAL)?finalJ:finalI;
                            for (int ind = start; ind < start + controller.getDeckCount(); ind++){
                                int row = (controller.getOrientation() == Orientation.VERTICAL)?finalI:ind;
                                int col = (controller.getOrientation() == Orientation.VERTICAL)?ind:finalJ;
                                drawer.drawComponent((StackPane) drawer.getNodeFromGridPane(fieldView, row, col), controller.getDrawingState());
                            }
                        } else
                            drawer.drawComponent((StackPane) node, controller.getDrawingState());
                    }
                });
            }
        }
    }

    private void setCellTurnAction(){
        FieldView fieldViewVictim = (getGameState()==TURN_1)? drawer.getFieldViewVictim2():drawer.getFieldViewVictim1();
        FieldView fieldViewAttackingVictim = (getGameState()==TURN_1)? drawer.getFieldView2():drawer.getFieldView1();
        Player victim = (getGameState()==TURN_1)?player2:player1;
        for (int i = 0; i < Game.TABLE_SIZE; i++) {
            for (int j = 0; j < Game.TABLE_SIZE; j++) {
                Node node = drawer.getNodeFromGridPane(fieldViewVictim, i, j);
                int finalI = i;
                int finalJ = j;
                node.setOnMouseClicked(e -> {
                    if (isTurnFinished())
                        return;
                    turn(finalI, finalJ);
                    drawer.drawHitCell(
                            (StackPane) drawer.getNodeFromGridPane(fieldViewVictim, finalI, finalJ),
                            victim.getCell(finalI, finalJ).getType()
                    );
                    drawer.drawHitCell(
                            (StackPane) drawer.getNodeFromGridPane(fieldViewAttackingVictim, finalI, finalJ),
                            victim.getCell(finalI, finalJ).getType()
                    );
                });
            }
        }
    }

    public void setCellAction(){
        if (getGameState() == CREATING_FIELD_1 || getGameState() == CREATING_FIELD_2) {
            setCellCreatingFieldAction();
        } else if (getGameState() == TURN_1 || getGameState() == TURN_2){
            setCellTurnAction();
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

    public void decreaseAliveShipsCount(Player player){
        player.setAliveShipsCount(player.getAliveShipsCount() - 1);
    }
}
