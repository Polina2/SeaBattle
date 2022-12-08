package ru.vsu.cs.oop.lygina_p_s;

import javafx.stage.Stage;
import ru.vsu.cs.oop.lygina_p_s.logic.*;

public class Controller {

    public void changeGameState(Game game, Drawer drawer, GameState state){
        Stage stage = drawer.getStage();
        game.setGameState(state);
        drawer.setDrawingState(TypeOfCell.EMPTY_CELL);
        stage.setScene(drawer.getScene(game));
    }

    public boolean createComponent(Player player, Drawer drawer, int i, int j){
        return switch (drawer.getDrawingState()) {
            case MINE -> player.createMine(i, j);
            case MINESWEEPER -> player.createMinesweeper(i, j);
            case SUBMARINE -> player.createSubmarine(i, j);
            case SHIP -> player.createShip(i, j, new Ship(drawer.getDeckCount(), drawer.getOrientation()));
            default -> false;
        };
    }
}
