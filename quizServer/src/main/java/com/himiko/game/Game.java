package com.himiko.game;

import com.himiko.game.elements.Question;
import com.himiko.game.elements.QuestionCategory;
import com.himiko.game.utils.User;
import com.himiko.server.utils.NetworkClient;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int maxUserSize;
    private long gameID;
    private Integer code;
    private boolean privateGame = false;

    private List<NetworkClient> currentUserList = new ArrayList<>();
    private List<Question> quetsionPool = new ArrayList<>();
    private User gameCreator;

    private Question currentQuestion;

    //Default constructor
    public Game(final long gameID)
    {
        this.maxUserSize = 4; //Default user size
        this.gameID = gameID;
        this.privateGame = false;
        this.code = null;
    }

    //Custom game constructor (Private game)
    public Game(int maxPlayerSize, final long gameID, final User gameCreator, boolean privateGame, final int code)
    {
        this.maxUserSize = maxPlayerSize;
        this.gameID = gameID;
        this.gameCreator = gameCreator;
        this.privateGame = privateGame;
        this.code = code;
    }

    public void addUser(NetworkClient client) {
        if(this.isUserInGame(client)) return;
        this.currentUserList.add(client);
    }

    public void removeUser(NetworkClient client)
    {
        if(!this.isUserInGame(client)) return;
        this.currentUserList.remove(client);
    }

    public void addQuestion(Question question)
    {
        this.quetsionPool.add(question);
    }

    //TODO:??
    public void removeQuestion(Question question)
    {
        this.quetsionPool.remove(question);
    }

    public boolean isUserInGame(NetworkClient client)
    {
        return this.currentUserList.stream().anyMatch(c -> c.getClient().getRemoteSocketAddress().equals(client.getClient().getRemoteSocketAddress()));
    }

    public boolean isPrivateGame()
    {
        return this.privateGame;
    }

    public int getMaxUserSize() {
        return maxUserSize;
    }

    public void setMaxUserSize(int maxUserSize) {
        this.maxUserSize = maxUserSize;
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setPrivateGame(boolean privateGame) {
        this.privateGame = privateGame;
    }

    public List<NetworkClient> getCurrentUserList() {
        return currentUserList;
    }

    public void setCurrentUserList(List<NetworkClient> currentUserList) {
        this.currentUserList = currentUserList;
    }

    public List<Question> getQuetsionPool() {
        return quetsionPool;
    }

    public void setQuetsionPool(List<Question> quetsionPool) {
        this.quetsionPool = quetsionPool;
    }

    public User getGameCreator() {
        return gameCreator;
    }

    public void setGameCreator(User gameCreator) {
        this.gameCreator = gameCreator;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
