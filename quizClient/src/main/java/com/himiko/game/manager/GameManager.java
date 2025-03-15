package com.himiko.game.manager;

import com.himiko.game.Game;
import com.himiko.game.utils.UserData;
import com.himiko.network.protocol.data.GameInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    public static int currentPlayerCount = 0;
    //User get games which are visible for him
    private static Map<Long, Game> gameMap = new HashMap<>();

    public static void addGame(long id, Game game)
    {
        //TODO:
        //Replace game?
        if(gameMap.containsKey(id)) {
            gameMap.remove(id);
        }
        gameMap.put(id, game);
    }

    public static void addGame(GameInfo gameInfo)
    {
        Game game = null;

        if(!gameInfo.isPrivateGame())
        {
            game = new Game(gameInfo.getGameID());
        }else{
            game = new Game(gameInfo.getGameID(), gameInfo.getCode());
        }
        game.setCurrentPlayers(gameInfo.getCurrentPlayers());
        game.setMaxUserSize(gameInfo.getMaxUserSize());

        addGame(game.getGameID(), game);
    }

    public static void setUserList(long id,List<UserData> userList)
    {
        Game game = getGame(id);

        if(game == null) return;
        game.setUserList(userList);
    }

    public static Game getGame(long id)
    {
        return gameMap.get(id);
    }

    public static List<Game> getGames()
    {
        return gameMap.values().stream().toList();
    }
}
