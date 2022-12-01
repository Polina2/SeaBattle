package ru.vsu.cs.oop.lygina_p_s;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Game game = new Game();
        Drawer drawer = new Drawer(stage);
        game.run();
        stage.setScene(drawer.getScene(game, stage));
        stage.show();
    }
}

