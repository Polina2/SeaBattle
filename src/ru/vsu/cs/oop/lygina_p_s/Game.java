package ru.vsu.cs.oop.lygina_p_s;

public class Game {
    public static final int TABLE_SIZE = 10;

    public enum GameState{
        CREATING_FIELD_1,
        CREATING_FIELD_2,
        TURN_1,
        TURN_2,
        END
    }

    private GameState gameState;
    private Player player1;
    private Player player2;

    public Game(){

    }

    public void run(){
        player1 = new Player();
        player2 = new Player();
        gameState = GameState.CREATING_FIELD_1;
        //update view
    }

    public GameState getGameState() {
        return gameState;
    }
}
