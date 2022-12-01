package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;
import ru.vsu.cs.oop.lygina_p_s.logic.GameState;
import ru.vsu.cs.oop.lygina_p_s.logic.Orientation;
import ru.vsu.cs.oop.lygina_p_s.logic.TypeOfCell;

import static ru.vsu.cs.oop.lygina_p_s.logic.GameState.*;

public class Drawer {
    private Field field1;
    private Field field2;
    private final Stage stage;

    public Drawer(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public Field getField1() {
        return field1;
    }

    public Field getField2() {
        return field2;
    }

    private TypeOfCell drawingState = TypeOfCell.EMPTY_CELL;
//    private int deckCount = 1;
//    private Orientation orientation = Orientation.HORIZONTAL;
//
    public void setDrawingState(TypeOfCell drawingState) {
        this.drawingState = drawingState;
    }

    public TypeOfCell getDrawingState() {
        return drawingState;
    }
//
//    public int getDeckCount() {
//        return deckCount;
//    }
//
//    public Orientation getOrientation() {
//        return orientation;
//    }

    public Scene getScene(Game game, Stage stage) {
        GameState state = game.getGameState();
        if (state == CREATING_FIELD_1 || state == CREATING_FIELD_2){
            Group group = new Group();
            HBox hBoxRoot = new HBox();
            if (state == CREATING_FIELD_1) {
                field1 = new Field(game.getPlayer1());
                hBoxRoot.getChildren().add(field1);
            } else {
                field2 = new Field(game.getPlayer2());
                hBoxRoot.getChildren().add(field2);
            }
            VBox vBoxButtons = new VBox();
            Button buttonCreateShip1 = new Button("1 deck ship");
            Button buttonCreateShip2 = new Button("2 deck ship");
            Button buttonCreateShip3 = new Button("3 deck ship");
            Button buttonCreateShip4 = new Button("4 deck ship");
            Button buttonMine = new Button("Mine");
            Button buttonMinesweeper = new Button("Minesweeper");
            Button buttonSubmarine = new Button("Submarine");
            Button buttonOK = new Button("OK");

            InputHandler.setButtonChangeGameState(buttonOK, game, this);

            InputHandler.setButtonChangeDrawingState(buttonMine, TypeOfCell.MINE, this);
            InputHandler.setButtonChangeDrawingState(buttonMinesweeper, TypeOfCell.MINESWEEPER, this);
            InputHandler.setButtonChangeDrawingState(buttonSubmarine, TypeOfCell.SUBMARINE, this);
            InputHandler.setButtonChangeDrawingState(buttonCreateShip1, TypeOfCell.SHIP, this);
            InputHandler.setButtonChangeDrawingState(buttonCreateShip2, TypeOfCell.SHIP, this);
            InputHandler.setButtonChangeDrawingState(buttonCreateShip3, TypeOfCell.SHIP, this);
            InputHandler.setButtonChangeDrawingState(buttonCreateShip4, TypeOfCell.SHIP, this);

            InputHandler.setCellAction(game, this);

            vBoxButtons.getChildren().addAll(buttonCreateShip1, buttonCreateShip2, buttonCreateShip3,
                    buttonCreateShip4, buttonMine, buttonMinesweeper, buttonSubmarine, buttonOK);
            hBoxRoot.getChildren().add(vBoxButtons);
            group.getChildren().add(hBoxRoot);
            return new Scene(group, 800, 600);
        }
        return null;
    }

    public void drawComponent(StackPane pane){
        Rectangle rectangle = (Rectangle) pane.getChildren().get(0);
        double halfSquare = rectangle.getWidth() / 2 - 1;
        if (drawingState == TypeOfCell.MINE){
            Circle circle = new Circle(rectangle.getX(), rectangle.getY(), rectangle.getHeight()/2, Color.WHITE);
            circle.setStroke(Color.BLACK);
            pane.getChildren().add(circle);
        } else if (drawingState == TypeOfCell.SHIP){
            Line line1 = new Line(rectangle.getX()-halfSquare, rectangle.getY()-halfSquare,
                    rectangle.getX()+halfSquare, rectangle.getY()+halfSquare);
            Line line2 = new Line(rectangle.getX()-halfSquare, rectangle.getY()+halfSquare,
                    rectangle.getX()+halfSquare, rectangle.getY()-halfSquare);
            line1.setStroke(Color.BLACK);
            line2.setStroke(Color.BLACK);
            pane.getChildren().addAll(line1, line2);
        } else if (drawingState == TypeOfCell.MINESWEEPER){
            Polygon polygon = new Polygon(rectangle.getX()-halfSquare, rectangle.getY()+halfSquare,
                    rectangle.getX()+halfSquare, rectangle.getY()+halfSquare,
                    rectangle.getX(), rectangle.getY()-halfSquare);
            polygon.setFill(Color.WHITE);
            polygon.setStroke(Color.BLACK);
            pane.getChildren().add(polygon);
        } else if (drawingState == TypeOfCell.SUBMARINE){
            Polygon polygon = new Polygon(rectangle.getX(), rectangle.getY()-halfSquare,
                    rectangle.getX()+halfSquare, rectangle.getY(),
                    rectangle.getX(), rectangle.getY()+halfSquare,
                    rectangle.getX()-halfSquare, rectangle.getY());
            polygon.setFill(Color.WHITE);
            polygon.setStroke(Color.BLACK);
            pane.getChildren().add(polygon);
        }
    }
}
