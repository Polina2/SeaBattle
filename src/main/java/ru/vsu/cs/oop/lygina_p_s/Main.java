package ru.vsu.cs.oop.lygina_p_s;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Game game = new Game();
        game.run();
        Drawer drawer = new Drawer(stage, game);
        stage.setScene(drawer.getScene());
        stage.show();
    }
}

