package com.himiko.game.manager;

import com.himiko.game.Game;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    public static int currentPlayerCount = 0;
    //User get games which are visible for him
    public static Map<Long, Game> gameMap = new HashMap<>();

    public GameManager()
    {

    }

    public void addGame(long id, Game game)
    {
        //TODO:
        //Replace game?
        if(gameMap.containsKey(id)) {
            gameMap.remove(id);
        }
        gameMap.put(id, game);
    }
}
