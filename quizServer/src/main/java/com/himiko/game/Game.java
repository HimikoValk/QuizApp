package com.himiko.game;

import com.himiko.Main;
import com.himiko.game.elements.Question;
import com.himiko.game.utils.User;
import com.himiko.server.utils.NetworkClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private int maxUserSize;
    private long gameID;
    private Integer code;
    private boolean privateGame = false;

    private List<NetworkClient> currentUserList = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();;
    private User gameCreator;
    private Question currentQuestion;
    private GameState gameState = GameState.WAITING;

    //Client, Answer
    private Map<NetworkClient, String> currentAnswers = new HashMap<>();
    //Client, Points
    private Map<NetworkClient, Integer> points = new HashMap<>();

    //Default constructor
    public Game(final long gameID)
    {
        this.maxUserSize = 4; //Default user size
        this.gameID = gameID;
        this.gameCreator = null;
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

    public void awardPoints(NetworkClient client, int pts) {
        if(this.points.get(client) == null){ this.points.put(client, pts); return;}
        this.points.put(client, this.points.get(client) + pts);
    }
    public int getPoints(NetworkClient client) {
        return points.getOrDefault(client, 0);
    }

    public void storeAnswer(NetworkClient client,String answer){
        Main.logger.debug("Saved answer!Answer:{}",answer);
        if(this.currentAnswers.get(client) != null) this.currentAnswers.remove(client);
        this.currentAnswers.put(client,answer);
    }

    public void clearAnswers()
    {
        this.currentAnswers.clear();
    }

    public void pullNextQuestion()
    {
        this.clearAnswers();
        Question tmp = this.getRandomQuestion();
        if(tmp.isUsed()) this.pullNextQuestion();
        this.currentQuestion = tmp;
    }

    public void addQuestion(Question question)
    {
        Main.logger.debug("Add Question:{} With Options Size:{}", question.getQuestion(), question.getQuestion().length());
        this.questions.add(question);
    }

    public void removeQuestion(Question question)
    {
        this.questions.remove(question);
    }

    public boolean questionAvailable()
    {
        return this.questions.stream().anyMatch(question -> !question.isUsed());
    }

    private Question getRandomQuestion()
    {
        int index = (int)(Math.random() * this.questions.size());
        return this.questions.get(index);
    }

    public Map<NetworkClient, String> getCurrentAnswers() {
        return currentAnswers;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public User getGameCreator() {
        return gameCreator;
    }

    public void setGameCreator(User gameCreator) {
        this.gameCreator = gameCreator;
    }

    public Question getCurrentQuestion() {
        return this.currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
