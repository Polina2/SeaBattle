package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.layout.GridPane;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;

public class FieldView extends GridPane {
    public FieldView(){
        for (int i = 0; i < Game.TABLE_SIZE; i++){
            for (int j = 0; j < Game.TABLE_SIZE; j++){
                CellView cellView = new CellView();
                this.add(cellView, i, j);
            }
        }
    }
}
