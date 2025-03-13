package com.himiko.game;

import com.himiko.Main;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.data.GameInfo;
import com.himiko.server.protocol.request.Request;
import com.himiko.server.protocol.request.RequestType;
import com.himiko.server.utils.NetworkClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Valk on 16.02.2025
 * @project quizServer
 */
public class GameManager {
    private Logger logger;
    //Key:ID (Long), Value:Game
    private static Map<Long, Game> games = new HashMap<>();

    public GameManager()
    {
        this.logger = Main.logger;
        for(int i = 0; i < 10; i++) {
            //Create 10 Games
            this.createGame();
        }
    }

    //Default
    public void createGame()
    {
        long gameID = createID(99999999999L);
        games.put(gameID, new Game(gameID));
        this.logger.debug("Created game with id:{}", gameID);
    }

    //Custom Game
    public void createGame(NetworkClient client, int maxPlayerSize, boolean privateGame, int code)
    {
        User creator = SessionManager.getUser(client);
        if(creator == null) {
            this.logger.error("No session found for client:{}", client.getClient().getRemoteSocketAddress());
            return;
        }
        long gameID = createID(99999999999L);
        games.put(gameID, new Game(maxPlayerSize, gameID, creator, privateGame, code));
        this.logger.debug("Created game with id:{}", gameID);
    }

    public void addUserToGame(long gameID, NetworkClient client)
    {
        if(!doesGameExist(gameID)) return;
        Game game = games.get(gameID);

        if(game.isPrivateGame()){
            //TODO:
        }
    }
    public List<GameInfo> getGameInfos() {
        return games.values().stream()
                .map(game -> new GameInfo(
                        game.getGameID(),
                        game.getMaxUserSize(),
                        game.getCurrentUserList().size(),
                        getPlayerNames(game),
                        game.isPrivateGame(),
                        game.isPrivateGame() ? null : game.getCode(), //null , null???
                        game.getGameCreator() != null ? game.getGameCreator().getName() : "Unknown"
                ))
                .collect(Collectors.toList());
    }

    public List<String> getPlayerNames(Game game)
    {
        return game.getCurrentUserList().stream()
                .map(SessionManager::getUser)
                .filter(Objects::nonNull)
                .map(User::getName)
                .collect(Collectors.toList());
    }

    public List<Game> getGames()
    {
        return games.values().stream().toList();
    }


    public List<Game> getPublicGames()
    {
        return games.values().stream().filter(game -> !game.isPrivateGame()).toList();
    }

    public List<Game> getPrivateGames()
    {
        return games.values().stream().filter(Game::isPrivateGame).toList();
    }

    private long createID(long maxID)
    {
        long id = (long) (Math.random() * maxID);

        if(games.get(id) != null) {
            this.logger.warning("SAME ID! CREATE A NEW ONE!");
            return createID(maxID);
        }

        return id;
    }

    private boolean doesGameExist(long id)
    {
        return games.containsKey(id);
    }
}