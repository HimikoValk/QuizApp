package com.himiko.game;

import com.himiko.MainApplication;
import com.himiko.game.elements.Question;
import com.himiko.game.utils.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private static Logger logger = LoggerFactory.getLogger(GameManager.class);
    private static Map<Long, Game> games = new HashMap<>();
    private static int DEFAULT_MAX_PLAYER_SIZE = 4;

    public GameManager()
    {

    }

    public void startGame(long gameID)
    {
        if(!this.doesGameExist(gameID) || !this.canGameBeStarted(gameID)) return;

        Game game = games.get(gameID);

        try
        {
            game.setGameState(GameState.RUNNING);
            logger.info("Starting game! GameID:{}", gameID);
            new Thread(() -> {
                while(game.getGameState() == GameState.RUNNING && game.questionAvailable())
                {
                    if(game.getCurrentUserList().isEmpty()) break;
                    Question currentQuestion = game.getCurrentQuestion();
                    if(currentQuestion == null || currentQuestion.isUsed())
                    {
                        game.pullNextQuestion();
                        currentQuestion = game.getCurrentQuestion();
                    }

                    //TODO
                    //Timer for answer time

                    //
                    this.evaluateQuestions(game, currentQuestion);
                    currentQuestion.setUsed(true);
                }
                game.setGameState(GameState.FINISHED);
            }).start();
        }catch (Exception e)
        {
            logger.error("Something went wrong while Starting game... Error:{}", e.getMessage());
        }
    }


    public void createDefaultGame()
    {
        this.createGame(DEFAULT_MAX_PLAYER_SIZE,MainApplication.serverUserProfile, false, true, 0);
    }

    public void createGame(int maxPlayerSize, User gameCreator, boolean privateGame, boolean autoStart,int code)
    {
        //Problem what if a game gets removed? (How to switch game id?)
        long gameID = games.size() + 1;
        games.put((long) (games.size() + 1), new Game(maxPlayerSize, gameID,gameCreator, privateGame, autoStart,code));
    }


    private void evaluateQuestions(Game game, Question question)
    {
        Map<WebSocketSession, String> answers = game.getCurrentAnswers();

        for(Map.Entry<WebSocketSession, String> entry : answers.entrySet())
        {
            String answer = entry.getValue();
            if(answer.equalsIgnoreCase(question.getAnswer()))
            {
                game.awardPoints(entry.getKey(), 10);
            }
        }
        game.clearAnswers();
    }

    /**
     * Checks conditions if game can be started.
     * Conditions could be like state or player size
     * @return
     */
    private boolean canGameBeStarted(long gameID)
    {
        if(doesGameExist(gameID)) return false;

        Game game = games.get(gameID);

        return (game.getGameState() == GameState.WAITING && game.isAutoStart() && (game.getCurrentUserList().size() / 2 == game.getMaxUserSize()));
    }

    private boolean doesGameExist(long gameID)
    {
        return games.get(gameID) != null;
    }
}
