package ru.vsu.cs.oop.lygina_p_s;

import javafx.scene.layout.GridPane;
import ru.vsu.cs.oop.lygina_p_s.logic.Game;
import ru.vsu.cs.oop.lygina_p_s.logic.Player;

public class Field extends GridPane {
    private final Player player;

    public Field(Player player){
        this.player = player;
        for (int i = 0; i < Game.TABLE_SIZE; i++){
            for (int j = 0; j < Game.TABLE_SIZE; j++){
                CellView cellView = new CellView(player.getCell(i, j));
                this.add(cellView, i, j);
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
