package ru.vsu.cs.oop.lygina_p_s;

import javafx.stage.Stage;
import ru.vsu.cs.oop.lygina_p_s.logic.*;

public class Controller {

    public static void changeGameState(Game game, Drawer drawer, GameState state){
        Stage stage = drawer.getStage();
        game.setGameState(state);
        drawer.setDrawingState(TypeOfCell.EMPTY_CELL);
        stage.setScene(drawer.getScene(game));
    }

    public static void createComponent(Player player, TypeOfCell type, int i, int j){
        switch (type){
            case MINE -> player.createMine(i, j);
            case MINESWEEPER -> player.createMinesweeper(i, j);
            case SUBMARINE -> player.createSubmarine(i, j);
     //       case SHIP -> player.createShip(i, j, new Ship(drawer.getDeckCount(), drawer.getOrientation()));
        }
    }
}
