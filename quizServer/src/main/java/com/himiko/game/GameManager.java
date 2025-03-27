package com.himiko.game;

import com.himiko.Main;
import com.himiko.game.elements.Question;
import com.himiko.game.elements.QuestionCategory;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.data.GameInfo;
import com.himiko.server.protocol.data.QuestionInfo;
import com.himiko.server.protocol.response.Response;
import com.himiko.server.protocol.response.ResponseType;
import com.himiko.server.utils.NetworkClient;

import javax.print.attribute.standard.MediaSize;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Valk on 16.02.2025
 * @project quizServer
 */
public class GameManager {
    private Logger logger;
    private static Map<Long, Game> games = new HashMap<>();
    private static final long maxID = 99999999999L;
    private Thread gameCheckThread; //Thread check if game can be started..

    public GameManager()
    {
        this.logger = Main.logger;
        //Just for a Test
        for(int i = 0; i < 10; i++) {
            //Create 10 Games
            this.createGame();
        }
        //Check thread for games
        this.createCheckThread();
    }


    public void startGame(long gameID) {
        if (!this.doesGameExist(gameID)) return;

        Game game = games.get(gameID);

        if (game.getCurrentUserList().size() < (game.getMaxUserSize() / 2)) {
           // this.logger.warning("Not enough players to start the game.. (Game ID:{})", game.getGameID());
            return;
        }

        if (game.getGameState() == GameState.RUNNING){
            this.logger.warning("Game is already running.. (Game ID:{})", game.getGameID());
            return;
        }else if(game.getGameState() == GameState.FINISHED)
        {
            //Remove game..
            this.logger.warning("Game finished... Removing game(Game ID:{})", game.getGameID());
            games.remove(game.getGameID());
            return;
        }

        game.setGameState(GameState.RUNNING);
        this.logger.info("Starting game with id :{}", game.getGameID());

        new Thread(() -> {
            this.logger.debug("Started game thread for Game-ID:{}", game.getGameID());

            while(game.questionAvailable()) {
                if(game.getCurrentUserList().isEmpty()) break;
                if (game.getCurrentQuestion() == null || game.getCurrentQuestion().isUsed()) {
                    game.pullNextQuestion();
                    this.logger.debug("Pulled next question! Question:{}", game.getCurrentQuestion().getQuestion());
                }

                this.sendQuestionToPlayers(game, this.getQuestionInfo(game.getGameID()));

                try {
                    Thread.sleep(20000); // 20 Sekunden warten
                } catch (InterruptedException e) {
                    logger.error("Game thread interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }

                this.evaluateQuestions(game, game.getCurrentQuestion());
                game.getCurrentQuestion().setUsed(true);
            }
            this.logger.debug("Finished Game!");
            game.setGameState(GameState.FINISHED);
            this.logger.debug("Terminating thread:{}",Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }).start();
    }

    private void sendQuestionToPlayers(Game game, QuestionInfo question)
    {
        for(NetworkClient client : game.getCurrentUserList())
        {
            Main.server.packageHandler.sendResponse(new Response<>(question, ResponseType.QUESTION_INFO), client);
        }
    }

    private void evaluateQuestions(Game game, Question question)
    {
        Map<NetworkClient, String> answers = game.getCurrentAnswers();

        for(Map.Entry<NetworkClient, String> entry : answers.entrySet())
        {
            String answer = entry.getValue();
            if(answer.equalsIgnoreCase(question.getAnswer()))
            {
                this.logger.info("User:{} choose the right answer! Current Points:{}",entry.getKey().getClient().getRemoteSocketAddress(), game.getPoints(entry.getKey()) + 10);
                game.awardPoints(entry.getKey(), 10);
            }
        }
        game.clearAnswers();
    }

    //Default
    public void createGame()
    {
        long gameID = this.createID(maxID);
        Game game = new Game(gameID);
        //For test
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Test1", "ANSWERRIGHTHTHTHH", new String[]{"option2", "option3", "option4"}, QuestionCategory.OTHER));
        questions.add(new Question("Test2", "ANSWERRIGHTHTHTHH", new String[]{"option2", "option3", "option4"}, QuestionCategory.OTHER));
        questions.add(new Question("Test3", "ANSWERRIGHTHTHTHH", new String[]{"option2", "option3", "option4"}, QuestionCategory.OTHER));
        questions.add(new Question("Test4", "ANSWERRIGHTHTHTHH", new String[]{"option2", "option3", "option4"}, QuestionCategory.OTHER));
        questions.add(new Question("Test5", "ANSWERRIGHTHTHTHH", new String[]{"option2", "option3", "option4"}, QuestionCategory.OTHER));
        game.setQuestions(questions);
        game.setMaxUserSize(2);

        games.put(gameID,game);
        this.logger.debug("Created game with id:{}", gameID);
    }

    public GameInfo createGame(NetworkClient client, int maxPlayerSize, boolean privateGame)
    {
        User creator = SessionManager.getUser(client);

        if(creator == null) {
            this.logger.error("No session found for client:{}", client.getClient().getRemoteSocketAddress());
            return null;
        }

        long gameID = createID(maxID);
        games.put(gameID, new Game(maxPlayerSize, gameID, creator, privateGame, this.createCode((int)maxID)));
        this.logger.debug("Created game with id:{}", gameID);
        return this.getGameInfo(gameID);
    }

    public GameInfo createGame(NetworkClient client, int maxPlayerSize, boolean privateGame, Question[] questions)
    {
        User creator = SessionManager.getUser(client);

        if(creator == null) {
            this.logger.error("No session found for client:{}", client.getClient().getRemoteSocketAddress());
            return null;
        }

        long gameID = this.createID(maxID);
        int code = this.createCode((int)maxID);
        Game game = new Game(maxPlayerSize, gameID, creator, privateGame, code);
        //Add questions to game
        Arrays.stream(questions).forEach(game::addQuestion);
        games.put(gameID, game);
        this.logger.debug("Created game with id:{} User creator:{}", gameID, game.getGameCreator().getName());
        return this.getGameInfo(gameID);
    }

    public QuestionInfo getQuestionInfo(long gameID)
    {
        if(!this.doesGameExist(gameID)) return null;
        Game game = games.get(gameID);

        if(game.getGameState() != GameState.RUNNING) return null;
        List<String> options = new ArrayList<>(Arrays.asList(game.getCurrentQuestion().getOptions()));
        options.add(game.getCurrentQuestion().getAnswer());

        Collections.shuffle(options);
        return new QuestionInfo(game.getCurrentQuestion().getQuestion(), options.toArray(new String[0]));
    }

    public boolean addUserToGame(long gameID, NetworkClient client)
    {
        if(!doesGameExist(gameID)) return false;
        Game game = games.get(gameID);

        if(game.getCurrentUserList().size() >= game.getMaxUserSize())
        {
            Main.server.packageHandler.sendResponse(new Response<>("Game is already full!", ResponseType.FAILURE), client);
            return false;
        }else if(game.isUserInGame(client))
        {
            Main.server.packageHandler.sendResponse(new Response<>("You are already in game!", ResponseType.FAILURE), client);
            return false;
        }else if(game.getGameState() == GameState.RUNNING)
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

    public boolean isUserCreator(long gameID, NetworkClient client)
    {
        if(!doesGameExist(gameID)) throw new RuntimeException("Game does not exist");
        return SessionManager.getUser(client).getId() == games.get(gameID).getGameCreator().getId();
    }

    public boolean isUserInGame(NetworkClient client)
    {
        return games.values().stream().anyMatch(game -> {
            return game.isUserInGame(client);
        });
    }

    public Game getUserGame(NetworkClient client)
    {
        return games.values().stream().filter(g -> {
            return g.isUserInGame(client);
        }).findFirst().get();
    }

    public GameInfo getGameInfo(long gameID)
    {
        Game game = games.get(gameID);
        if(game == null) return null;
        return new GameInfo(
                game.getGameID(),
                game.getMaxUserSize(),
                game.getCurrentUserList().size(),
                this.getPlayerNames(game),
                game.isPrivateGame(),
                game.isPrivateGame() ? null : game.getCode(),
                game.getGameCreator() == null ? "Unknown" : game.getGameCreator().getName(),
                game.getGameState());
    }

    public List<GameInfo> getGameInfos()
    {
        return games.values().stream()
                .map(game -> new GameInfo(
                        game.getGameID(),
                        game.getMaxUserSize(),
                        game.getCurrentUserList().size(),
                        getPlayerNames(game),
                        game.isPrivateGame(),
                        game.isPrivateGame() ? null : game.getCode(),
                        game.getGameCreator() == null ? "Unknown" : game.getGameCreator().getName(),
                        game.getGameState()
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

    private void createCheckThread()
    {
        this.gameCheckThread = new Thread(() -> {
            while (Main.server.isAlive())
            {
               if (games.isEmpty()) break;
               games.keySet().stream().forEach(id -> this.startGame(id));
                try {
                    //Check every 5 seconds if a Game can be started...
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
           });
        gameCheckThread.start();
    }

    private long createID(long maxID)
    {
        long id = (long) (Math.random() * maxID);

        if(games.get(id) != null) {
            this.logger.warning("SAME ID! CREATE A NEW ONE!");
            return this.createID(maxID);
        }

        return id;
    }

    private int createCode(int maxCodeRange)
    {
        int code = (int) (Math.random() * maxCodeRange);

        if(this.getPrivateGames().stream().anyMatch(game -> this.isCodeCorrect(game.getGameID(), code)))
        {
            return this.createCode(maxCodeRange);
        }

        return code;
    }

    private boolean doesGameExist(long id)
    {
        return games.containsKey(id);
    }
}