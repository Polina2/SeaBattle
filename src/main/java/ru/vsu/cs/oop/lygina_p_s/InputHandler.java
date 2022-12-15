package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ru.vsu.cs.oop.lygina_p_s.logic.*;

import static ru.vsu.cs.oop.lygina_p_s.logic.GameState.*;

public class InputHandler {
    private Controller controller;

    public InputHandler(Controller controller) {
        this.controller = controller;
    }

    public void setButtonChangeDrawingState(Button button, TypeOfCell type, Drawer drawer){
        button.setOnAction(e -> {
            controller.setDrawingState(type);
        });
    }

    public void setButtonChangeDrawingState(Button button, TypeOfCell type, int deckCount){
        button.setOnAction(e -> {
            controller.setDrawingState(type);
            controller.setDeckCount(deckCount);
        });
    }

    public void setButtonChangeGameState(Button button, Game game, Drawer drawer){
        button.setOnAction(e -> {
            if (game.getGameState() == CREATING_FIELD_1)
                controller.changeGameState(game, drawer, CREATING_FIELD_2);
            else if (game.getGameState() == TURN_1 && game.isTurnFinished())
                controller.changeGameState(game, drawer, TURN_2);
            else if (game.getGameState()==CREATING_FIELD_2 || (game.getGameState()==TURN_2 && game.isTurnFinished()))
                controller.changeGameState(game, drawer, TURN_1);
        });
    }

    public void setRadioButtonChangeOrientation(RadioButton radioButton, Orientation orientation){
        radioButton.setOnAction(e -> {
            controller.setOrientation(orientation);
        });
    }

    public void setButtonConfirmAction(Button button, Game game, Drawer drawer){
        button.setOnAction(e -> {
            game.setActionConfirmed(true);
            drawer.getStage().setScene(drawer.getScene(game));
        });
    }

    private void setCellCreatingFieldAction(Game game, Drawer drawer){
        FieldView fieldView = (game.getGameState() == CREATING_FIELD_1)?drawer.getFieldView1():drawer.getFieldView2();
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

    private void setCellTurnAction(Game game, Drawer drawer){
        FieldView fieldView = (game.getGameState()==TURN_1)? drawer.getFieldViewVictim2():drawer.getFieldViewVictim1();
        for (int i = 0; i < Game.TABLE_SIZE; i++) {
            for (int j = 0; j < Game.TABLE_SIZE; j++) {
                Node node = getNodeFromGridPane(fieldView, i, j);
                int finalI = i;
                int finalJ = j;
                node.setOnMouseClicked(e -> {
                    if (game.isTurnFinished())
                        return;
                    game.turn(finalI, finalJ);
                    drawer.drawHitCell(
                            (StackPane) getNodeFromGridPane(fieldView, finalI, finalJ),
                            fieldView.getPlayer().getCell(finalI, finalJ).getType()
                    );
                });
            }
        }
    }

    public void setCellAction(Game game, Drawer drawer){
        if (game.getGameState() == CREATING_FIELD_1 || game.getGameState() == CREATING_FIELD_2) {
            setCellCreatingFieldAction(game, drawer);
        } else if (game.getGameState() == TURN_1 || game.getGameState() == TURN_2){
            setCellTurnAction(game, drawer);
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
}
