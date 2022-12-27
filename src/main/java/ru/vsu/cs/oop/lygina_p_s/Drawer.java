package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
    private final Game game;

    public Drawer(Stage stage, Game game) {
        this.game = game;
        fieldView1 = new FieldView();
        fieldView2 = new FieldView();
        fieldViewVictim1 = new FieldView();
        fieldViewVictim2 = new FieldView();
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

    private Scene getCreatingFieldScene(){
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

        radioButtonH.setOnAction(e -> {
            game.changeOrientation(Orientation.HORIZONTAL);
        });
        radioButtonV.setOnAction(e -> {
            game.changeOrientation(Orientation.VERTICAL);
        });

        Button buttonCreateShip1 = new Button("1 deck ship");
        Button buttonCreateShip2 = new Button("2 deck ship");
        Button buttonCreateShip3 = new Button("3 deck ship");
        Button buttonCreateShip4 = new Button("4 deck ship");
        Button buttonMine = new Button("Mine");
        Button buttonMinesweeper = new Button("Minesweeper");
        Button buttonSubmarine = new Button("Submarine");
        Button buttonOK = new Button("OK");

        buttonOK.setOnAction(e -> {
            game.changeGameStateAction();
        });

        buttonMine.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.MINE);
        });
        buttonMinesweeper.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.MINESWEEPER);
        });
        buttonSubmarine.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.SUBMARINE);
        });
        buttonCreateShip1.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.SHIP, 1);
        });
        buttonCreateShip2.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.SHIP, 2);
        });
        buttonCreateShip3.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.SHIP, 3);
        });
        buttonCreateShip4.setOnAction(e -> {
            game.changeDrawingStateAction(TypeOfCell.SHIP, 4);
        });

        game.setCellAction();

        vBoxButtons.getChildren().addAll(radioButtonH, radioButtonV,
                buttonCreateShip1, buttonCreateShip2, buttonCreateShip3,
                buttonCreateShip4, buttonMine, buttonMinesweeper, buttonSubmarine, buttonOK);
        hBoxRoot.getChildren().add(vBoxButtons);
        group.getChildren().add(hBoxRoot);
        return new Scene(group, 800, 600);
    }

    private Scene getConfirmationScene(){
        Group group = new Group();
        StackPane stackPane = new StackPane();
        Button button = new Button("Confirm");
        button.setOnAction(e -> {
            game.confirmAction();
        });
        stackPane.setPrefSize(800, 600);
        stackPane.getChildren().add(button);
        group.getChildren().add(stackPane);
        return new Scene(group, 800, 600);
    }

    private Scene getTurnScene() {
        Group group = new Group();
        HBox hBox = new HBox();

        FieldView fieldViewVictim = (game.getGameState()==TURN_1)?fieldViewVictim2:fieldViewVictim1;
        FieldView fieldViewAttacker = (game.getGameState()==TURN_1)?fieldView1:fieldView2;
        Button buttonOK = new Button("OK");
        buttonOK.setOnAction(e -> {
            game.changeGameStateAction();
        });

        game.setCellAction();

        hBox.getChildren().addAll(fieldViewAttacker, new Separator(), fieldViewVictim, buttonOK);
        group.getChildren().add(hBox);
        return new Scene(group, 900, 600);
    }

    public Scene getScene() {
        GameState state = game.getGameState();
        if (state == CREATING_FIELD_1 || state == CREATING_FIELD_2){
            return getCreatingFieldScene();
        } else if (state == TURN_1 || state == TURN_2){
            if (game.isActionConfirmed())
                return getTurnScene();
            else
                return getConfirmationScene();
        } else {
            return getEndScene();
        }
    }

    private Scene getEndScene(){
        Group group = new Group();
        StackPane stackPane = new StackPane();
        Text text = new Text("Game over");
        stackPane.setPrefSize(800, 600);
        stackPane.getChildren().add(text);
        group.getChildren().add(stackPane);
        return new Scene(group, 800, 600);
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

    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
