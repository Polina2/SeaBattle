package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
    private FieldView fieldView1;
    private FieldView fieldView2;
    private final Stage stage;
    private InputHandler inputHandler = new InputHandler();

    public Drawer(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public FieldView getField1() {
        return fieldView1;
    }

    public FieldView getField2() {
        return fieldView2;
    }

    private TypeOfCell drawingState = TypeOfCell.EMPTY_CELL;
    private int deckCount = 1;
    private Orientation orientation = Orientation.HORIZONTAL;

    public void setDrawingState(TypeOfCell drawingState) {
        this.drawingState = drawingState;
    }

    public TypeOfCell getDrawingState() {
        return drawingState;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setDeckCount(int deckCount) {
        this.deckCount = deckCount;
    }

    public Scene getScene(Game game) {
        GameState state = game.getGameState();
        if (state == CREATING_FIELD_1 || state == CREATING_FIELD_2){
            Group group = new Group();
            HBox hBoxRoot = new HBox();
            if (state == CREATING_FIELD_1) {
                fieldView1 = new FieldView(game.getPlayer1());
                hBoxRoot.getChildren().add(fieldView1);
            } else {
                fieldView2 = new FieldView(game.getPlayer2());
                hBoxRoot.getChildren().add(fieldView2);
            }
            VBox vBoxButtons = new VBox();

            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton radioButtonH = new RadioButton("Horizontal");
            RadioButton radioButtonV = new RadioButton("Vertical");
            radioButtonH.setToggleGroup(toggleGroup);
            radioButtonV.setToggleGroup(toggleGroup);
            radioButtonH.setSelected(true);
            radioButtonH.setOnAction(e -> {
                orientation = Orientation.HORIZONTAL;
            });
            radioButtonV.setOnAction(e -> {
                orientation = Orientation.VERTICAL;
            });

            Button buttonCreateShip1 = new Button("1 deck ship");
            Button buttonCreateShip2 = new Button("2 deck ship");
            Button buttonCreateShip3 = new Button("3 deck ship");
            Button buttonCreateShip4 = new Button("4 deck ship");
            Button buttonMine = new Button("Mine");
            Button buttonMinesweeper = new Button("Minesweeper");
            Button buttonSubmarine = new Button("Submarine");
            Button buttonOK = new Button("OK");

            inputHandler.setButtonChangeGameState(buttonOK, game, this);

            inputHandler.setButtonChangeDrawingState(buttonMine, TypeOfCell.MINE, this);
            inputHandler.setButtonChangeDrawingState(buttonMinesweeper, TypeOfCell.MINESWEEPER, this);
            inputHandler.setButtonChangeDrawingState(buttonSubmarine, TypeOfCell.SUBMARINE, this);
            inputHandler.setButtonChangeDrawingState(buttonCreateShip1, TypeOfCell.SHIP, this, 1);
            inputHandler.setButtonChangeDrawingState(buttonCreateShip2, TypeOfCell.SHIP, this, 2);
            inputHandler.setButtonChangeDrawingState(buttonCreateShip3, TypeOfCell.SHIP, this, 3);
            inputHandler.setButtonChangeDrawingState(buttonCreateShip4, TypeOfCell.SHIP, this, 4);

            inputHandler.setCellAction(game, this);

            vBoxButtons.getChildren().addAll(radioButtonH, radioButtonV,
                    buttonCreateShip1, buttonCreateShip2, buttonCreateShip3,
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
