package com.himiko.game;

import com.himiko.Main;
import com.himiko.game.elements.Question;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.data.GameInfo;
import com.himiko.server.protocol.response.Response;
import com.himiko.server.protocol.response.ResponseType;
import com.himiko.server.utils.NetworkClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Valk on 16.02.2025
 * @project quizServer
 */
public class GameManager {
    private Logger logger;
    private static Map<Long, Game> games = new HashMap<>();
    private static final long maxID = 99999999999L;

    public GameManager()
    {
        this.logger = Main.logger;
        for(int i = 0; i < 10; i++) {
            //Create 10 Games
            this.createGame();
        }
    }

    public void startGame(long gameID)
    {
        if(!this.doesGameExist(gameID)) return;

        Game game = games.get(gameID);

        if(game.getCurrentUserList().size() < 2)
        {
            this.logger.warning("Not enough players to start the game.. (Game ID:{})", game.getGameID());
            return;
        }

        game.setGameState(GameState.RUNNING);
        this.logger.info("Starting game with id :{}", game.getGameID());

        new Thread(() -> {
            //TODO:
        }).start();
    }

    public void sendQuestionToPlayers(Game game, Question question)
    {
        for(NetworkClient client : game.getCurrentUserList())
        {
            Main.server.packageHandler.sendResponse(new Response<>(question, ResponseType.QUESTION), client);
        }
    }

    //Default
    public void createGame()
    {
        long gameID = this.createID(maxID);
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
        long gameID = createID(maxID);
        games.put(gameID, new Game(maxPlayerSize, gameID, creator, privateGame, code));
        this.logger.debug("Created game with id:{}", gameID);
    }

    public boolean addUserToGame(long gameID, NetworkClient client)
    {
        if(!doesGameExist(gameID)) return false;
        Game game = games.get(gameID);

        if(game.getCurrentUserList().size() >= game.getMaxUserSize())
        {
            Main.server.packageHandler.sendResponse(new Response<>("Game is already full!", ResponseType.FAILURE), client);
            return false;
        }

        if(game.isUserInGame(client))
        {
            Main.server.packageHandler.sendResponse(new Response<>("You are already in game!", ResponseType.FAILURE), client);
            return false;
        }

        if(game.getGameState() == GameState.RUNNING)
        {
            Main.server.packageHandler.sendResponse(new Response<>("Game is already running!", ResponseType.FAILURE), client);
            return false;
        }

        game.addUser(client);

        return true;
    }

    public void removeUser(NetworkClient client)
    {
        games.values().forEach(g ->{if(g.isUserInGame(client)) g.removeUser(client);});
    }

    public void removeUser(long gameID, NetworkClient client)
    {
        if(this.doesGameExist(gameID) && this.isUserInGame(gameID, client))
        {
            games.get(gameID).removeUser(client);
        }
    }

    public boolean isPrivateGame(long gameID)
    {
        if(!doesGameExist(gameID)) throw new RuntimeException("Game does not exist");
        return games.get(gameID).isPrivateGame();
    }

    public boolean isCodeCorrect(long gameID,int code)
    {
        if(!doesGameExist(gameID)) throw new RuntimeException("Game does not exist");
        return games.get(gameID).getCode() == code;
    }

    public boolean isUserInGame(long gameID, NetworkClient client)
    {
        if(!doesGameExist(gameID)) throw new RuntimeException("Game does not exist");
        return games.get(gameID).isUserInGame(client);
    }

    public List<GameInfo> getGameInfos() {
        return games.values().stream()
                .map(game -> new GameInfo(
                        game.getGameID(),
                        game.getMaxUserSize(),
                        game.getCurrentUserList().size(),
                        getPlayerNames(game),
                        game.isPrivateGame(),
                        game.isPrivateGame() ? null : game.getCode(),
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