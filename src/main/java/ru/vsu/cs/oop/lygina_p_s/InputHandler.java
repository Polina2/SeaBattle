package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;
import ru.vsu.cs.oop.lygina_p_s.logic.Orientation;
import ru.vsu.cs.oop.lygina_p_s.logic.TypeOfCell;

import static ru.vsu.cs.oop.lygina_p_s.logic.GameState.*;

public class InputHandler {
    private Controller controller = new Controller();

    public void setButtonChangeDrawingState(Button button, TypeOfCell type, Drawer drawer){
        button.setOnAction(e -> {
            drawer.setDrawingState(type);
        });
    }

    public void setButtonChangeDrawingState(Button button, TypeOfCell type, Drawer drawer, int deckCount){
        button.setOnAction(e -> {
            drawer.setDrawingState(type);
            drawer.setDeckCount(deckCount);
        });
    }

    public void setButtonChangeGameState(Button button, Game game, Drawer drawer){
        button.setOnAction(e -> {
            if (game.getGameState() == CREATING_FIELD_1)
                controller.changeGameState(game, drawer, CREATING_FIELD_2);
            else if (game.getGameState() == TURN_1)
                controller.changeGameState(game, drawer, TURN_2);
            else
                controller.changeGameState(game, drawer, TURN_1);
        });
    }

    public void setCellAction(Game game, Drawer drawer){
        if (game.getGameState() == CREATING_FIELD_1 || game.getGameState() == CREATING_FIELD_2) {
            FieldView fieldView = (game.getGameState() == CREATING_FIELD_1)?drawer.getField1(): drawer.getField2();
            for (int i = 0; i < Game.TABLE_SIZE; i++) {
                for (int j = 0; j < Game.TABLE_SIZE; j++) {
                    Node node = getNodeFromGridPane(fieldView, i, j);
                    int finalI = i;
                    int finalJ = j;
                    node.setOnMouseClicked(e -> {
                        if(controller.createComponent(fieldView.getPlayer(), drawer, finalI, finalJ)) {
                            if (drawer.getDrawingState() == TypeOfCell.SHIP){
                                int start = (drawer.getOrientation() == Orientation.VERTICAL)?finalJ:finalI;
                                for (int ind = start; ind < start + drawer.getDeckCount(); ind++){
                                    int row = (drawer.getOrientation() == Orientation.VERTICAL)?finalI:ind;
                                    int col = (drawer.getOrientation() == Orientation.VERTICAL)?ind:finalJ;
                                    drawer.drawComponent((StackPane) getNodeFromGridPane(fieldView, row, col));
                                }
                            } else
                                drawer.drawComponent((StackPane) node);
                        }
                    });
                }
            }
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
