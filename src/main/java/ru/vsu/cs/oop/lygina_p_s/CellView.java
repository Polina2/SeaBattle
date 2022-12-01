package ru.vsu.cs.oop.lygina_p_s;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.vsu.cs.oop.lygina_p_s.logic.Cell;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;

public class CellView extends StackPane {
    public static final int SIZE = 400 / Game.TABLE_SIZE;
    private final Cell cell;

    public CellView(Cell cell) {
        this.cell = cell;
        Rectangle border = new Rectangle(SIZE, SIZE);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        setAlignment(Pos.CENTER);
        getChildren().add(border);
    }
}
