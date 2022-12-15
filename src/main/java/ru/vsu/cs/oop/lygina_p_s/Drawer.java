package ru.vsu.cs.oop.lygina_p_s;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
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
    private FieldView fieldViewVictim1;
    private FieldView fieldViewVictim2;
    private final Stage stage;
    private final InputHandler inputHandler;

    public Drawer(Stage stage, Game game) {
        inputHandler = new InputHandler(game.getController());
        fieldView1 = new FieldView(game.getPlayer1());
        fieldView2 = new FieldView(game.getPlayer2());
        fieldViewVictim1 = new FieldView(game.getPlayer1());
        fieldViewVictim2 = new FieldView(game.getPlayer2());
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public FieldView getFieldView1() {
        return fieldView1;
    }

    public FieldView getFieldView2() {
        return fieldView2;
    }

    public FieldView getFieldViewVictim1() {
        return fieldViewVictim1;
    }

    public FieldView getFieldViewVictim2() {
        return fieldViewVictim2;
    }

    private Scene getCreatingFieldScene(Game game){
        GameState state = game.getGameState();
        Group group = new Group();
        HBox hBoxRoot = new HBox();
        if (state == CREATING_FIELD_1) {
            hBoxRoot.getChildren().add(fieldView1);
        } else {
            hBoxRoot.getChildren().add(fieldView2);
        }
        VBox vBoxButtons = new VBox();

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton radioButtonH = new RadioButton("Horizontal");
        RadioButton radioButtonV = new RadioButton("Vertical");
        radioButtonH.setToggleGroup(toggleGroup);
        radioButtonV.setToggleGroup(toggleGroup);
        radioButtonH.setSelected(true);

        inputHandler.setRadioButtonChangeOrientation(radioButtonH, Orientation.HORIZONTAL);
        inputHandler.setRadioButtonChangeOrientation(radioButtonV, Orientation.VERTICAL);

        Button buttonCreateShip1 = new Button("1 deck ship");
        Button buttonCreateShip2 = new Button("2 deck ship");
        Button buttonCreateShip3 = new Button("3 deck ship");
        Button buttonCreateShip4 = new Button("4 deck ship");
        Button buttonMine = new Button("Mine");
        Button buttonMinesweeper = new Button("Minesweeper");
        Button buttonSubmarine = new Button("Submarine");
        Button buttonOK = new Button("OK");

        buttonOK.setOnAction(e -> {
            inputHandler.setButtonChangeGameState(buttonOK, game, this);

        });
        inputHandler.setButtonChangeGameState(buttonOK, game, this);

        inputHandler.setButtonChangeDrawingState(buttonMine, TypeOfCell.MINE, this);
        inputHandler.setButtonChangeDrawingState(buttonMinesweeper, TypeOfCell.MINESWEEPER, this);
        inputHandler.setButtonChangeDrawingState(buttonSubmarine, TypeOfCell.SUBMARINE, this);
        inputHandler.setButtonChangeDrawingState(buttonCreateShip1, TypeOfCell.SHIP, 1);
        inputHandler.setButtonChangeDrawingState(buttonCreateShip2, TypeOfCell.SHIP, 2);
        inputHandler.setButtonChangeDrawingState(buttonCreateShip3, TypeOfCell.SHIP, 3);
        inputHandler.setButtonChangeDrawingState(buttonCreateShip4, TypeOfCell.SHIP, 4);

        inputHandler.setCellAction(game, this);

        vBoxButtons.getChildren().addAll(radioButtonH, radioButtonV,
                buttonCreateShip1, buttonCreateShip2, buttonCreateShip3,
                buttonCreateShip4, buttonMine, buttonMinesweeper, buttonSubmarine, buttonOK);
        hBoxRoot.getChildren().add(vBoxButtons);
        group.getChildren().add(hBoxRoot);
        return new Scene(group, 800, 600);
    }

    private Scene getConfirmationScene(Game game){
        Group group = new Group();
        StackPane stackPane = new StackPane();
        Button button = new Button("Confirm");
        inputHandler.setButtonConfirmAction(button, game, this);
        stackPane.setPrefSize(800, 600);
        stackPane.getChildren().add(button);
        group.getChildren().add(stackPane);
        return new Scene(group, 800, 600);
    }

    private Scene getTurnScene(Game game) {
        Group group = new Group();
        HBox hBox = new HBox();

        FieldView fieldViewVictim = (game.getGameState()==TURN_1)?fieldViewVictim2:fieldViewVictim1;
        FieldView fieldViewAttacker = (game.getGameState()==TURN_1)?fieldView1:fieldView2;
        Button buttonOK = new Button("OK");
        inputHandler.setButtonChangeGameState(buttonOK, game, this);
        inputHandler.setCellAction(game, this);

        hBox.getChildren().addAll(fieldViewAttacker, new Separator(), fieldViewVictim, buttonOK);
        group.getChildren().add(hBox);
        return new Scene(group, 900, 600);
    }

    public Scene getScene(Game game) {
        GameState state = game.getGameState();
        if (state == CREATING_FIELD_1 || state == CREATING_FIELD_2){
            return getCreatingFieldScene(game);
        } else if (state == TURN_1 || state == TURN_2){
            if (game.isActionConfirmed())
                return getTurnScene(game);
            else
                return getConfirmationScene(game);
        }
        return null;
    }

    public void drawHitCell(StackPane pane, TypeOfCell drawingState){
        if (drawingState == TypeOfCell.MINE){
            drawMine(pane, Color.RED);
        } else if (drawingState == TypeOfCell.SHIP){
            drawShip(pane, Color.RED);
        } else if (drawingState == TypeOfCell.MINESWEEPER){
            drawMinesweeper(pane, Color.RED);
        } else if (drawingState == TypeOfCell.SUBMARINE){
            drawSubmarine(pane, Color.RED);
        } else {
            drawEmptyCell(pane);
        }
    }

    private void drawEmptyCell(StackPane pane){
        Rectangle rectangle = (Rectangle) pane.getChildren().get(0);
        rectangle.setFill(Color.GREY);
    }

    private void drawMine(StackPane pane, Color color){
        Rectangle rectangle = (Rectangle) pane.getChildren().get(0);
        Circle circle = new Circle(rectangle.getX(), rectangle.getY(), rectangle.getHeight()/2, Color.WHITE);
        circle.setStroke(color);
        pane.getChildren().add(circle);
    }

    private void drawShip(StackPane pane, Color color){
        Rectangle rectangle = (Rectangle) pane.getChildren().get(0);
        double halfSquare = rectangle.getWidth() / 2 - 1;
        Line line1 = new Line(rectangle.getX()-halfSquare, rectangle.getY()-halfSquare,
                rectangle.getX()+halfSquare, rectangle.getY()+halfSquare);
        Line line2 = new Line(rectangle.getX()-halfSquare, rectangle.getY()+halfSquare,
                rectangle.getX()+halfSquare, rectangle.getY()-halfSquare);
        line1.setStroke(color);
        line2.setStroke(color);
        pane.getChildren().addAll(line1, line2);
    }

    private void drawMinesweeper(StackPane pane, Color color){
        Rectangle rectangle = (Rectangle) pane.getChildren().get(0);
        double halfSquare = rectangle.getWidth() / 2 - 1;
        Polygon polygon = new Polygon(rectangle.getX()-halfSquare, rectangle.getY()+halfSquare,
                rectangle.getX()+halfSquare, rectangle.getY()+halfSquare,
                rectangle.getX(), rectangle.getY()-halfSquare);
        polygon.setFill(Color.WHITE);
        polygon.setStroke(color);
        pane.getChildren().add(polygon);
    }

    private void drawSubmarine(StackPane pane, Color color){
        Rectangle rectangle = (Rectangle) pane.getChildren().get(0);
        double halfSquare = rectangle.getWidth() / 2 - 1;
        Polygon polygon = new Polygon(rectangle.getX(), rectangle.getY()-halfSquare,
                rectangle.getX()+halfSquare, rectangle.getY(),
                rectangle.getX(), rectangle.getY()+halfSquare,
                rectangle.getX()-halfSquare, rectangle.getY());
        polygon.setFill(Color.WHITE);
        polygon.setStroke(color);
        pane.getChildren().add(polygon);
    }

    public void drawComponent(StackPane pane, TypeOfCell drawingState){
        if (drawingState == TypeOfCell.MINE){
            drawMine(pane, Color.BLACK);
        } else if (drawingState == TypeOfCell.SHIP){
            drawShip(pane, Color.BLACK);
        } else if (drawingState == TypeOfCell.MINESWEEPER){
            drawMinesweeper(pane, Color.BLACK);
        } else if (drawingState == TypeOfCell.SUBMARINE){
            drawSubmarine(pane, Color.BLACK);
        }
    }
}
