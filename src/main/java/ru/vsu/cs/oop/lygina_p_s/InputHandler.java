package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;
import ru.vsu.cs.oop.lygina_p_s.logic.TypeOfCell;

import static ru.vsu.cs.oop.lygina_p_s.logic.GameState.*;

public class InputHandler {
    public static void setButtonChangeDrawingState(Button button, TypeOfCell type, Drawer drawer){
        button.setOnAction(e -> {
            drawer.setDrawingState(type);
        });
    }

    public static void setButtonChangeGameState(Button button, Game game, Drawer drawer){
        button.setOnAction(e -> {
            if (game.getGameState() == CREATING_FIELD_1)
                Controller.changeGameState(game, drawer, CREATING_FIELD_2);
            else if (game.getGameState() == TURN_1)
                Controller.changeGameState(game, drawer, TURN_2);
            else
                Controller.changeGameState(game, drawer, TURN_1);
        });
    }

    public static void setCellAction(Game game, Drawer drawer){
        if (game.getGameState() == CREATING_FIELD_1 || game.getGameState() == CREATING_FIELD_2) {
            Field field = (game.getGameState() == CREATING_FIELD_1)?drawer.getField1(): drawer.getField2();
            for (int i = 0; i < Game.TABLE_SIZE; i++) {
                for (int j = 0; j < Game.TABLE_SIZE; j++) {
                    Node node = getNodeFromGridPane(field, i, j);
                    int finalI = i;
                    int finalJ = j;
                    node.setOnMouseClicked(e -> {
                        //risovat korabliki
                        drawer.drawComponent((StackPane) node);
                        Controller.createComponent(field.getPlayer(), drawer.getDrawingState(), finalI, finalJ);
                    });
                }
            }
        }
    }

    private static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
