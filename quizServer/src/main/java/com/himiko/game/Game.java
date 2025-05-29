package com.himiko.game;

import com.himiko.game.elements.Question;
import com.himiko.game.utils.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private int maxUserSize;
    private long gameID;
    private Integer code;
    private boolean privateGame = false;
    private boolean autoStart = true;

    private List<WebSocketSession> currentUserList = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private User gameCreator;
    private Question currentQuestion;
    private GameState gameState = GameState.WAITING;

    // Session, Answer
    private Map<WebSocketSession, String> currentAnswers = new HashMap<>();
    // Session, Points
    private Map<WebSocketSession, Integer> points = new HashMap<>();

    // Default constructor
    public Game(final long gameID) {
        this.maxUserSize = 4; // Default user size
        this.gameID = gameID;
    }

    // Custom game constructor (Private game)
    public Game(int maxPlayerSize, final long gameID, final User gameCreator, boolean privateGame, boolean autoStart, final int code) {
        this.maxUserSize = maxPlayerSize;
        this.gameID = gameID;
        this.gameCreator = gameCreator;
        this.privateGame = privateGame;
        this.autoStart = autoStart;
        this.code = code;
    }

    public void addUser(WebSocketSession session) {
        if (this.isUserInGame(session)) return;
        this.currentUserList.add(session);
        this.points.put(session, 0);
    }

    public void removeUser(WebSocketSession session) {
        if (!this.isUserInGame(session)) return;
        this.currentUserList.remove(session);
        this.currentAnswers.remove(session);
        this.points.remove(session);
    }

    public void awardPoints(WebSocketSession session, int pts) {
        this.points.merge(session, pts, Integer::sum);
    }

    public int getPoints(WebSocketSession session) {
        return points.getOrDefault(session, 0);
    }

    public void storeAnswer(WebSocketSession session, String answer) {
        this.currentAnswers.put(session, answer);
    }

    public void clearAnswers() {
        this.currentAnswers.clear();
    }

    public void pullNextQuestion() {
        if (!this.questionAvailable()) return;

        this.clearAnswers();
        Question tmp = this.getRandomQuestion();
        if (tmp.isUsed()) {
            this.pullNextQuestion();
            return;
        }
        this.currentQuestion = tmp;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }

    public boolean questionAvailable() {
        return this.questions.stream().anyMatch(question -> !question.isUsed());
    }

    public boolean canGameStart() {
        return !this.autoStart || this.currentUserList.size() < (this.maxUserSize / 2) || this.gameState != GameState.RUNNING;
    }

    private Question getRandomQuestion() {
        int index = (int) (Math.random() * this.questions.size());
        return this.questions.get(index);
    }

    public Map<WebSocketSession, String> getCurrentAnswers() {
        return currentAnswers;
    }

    public boolean isUserInGame(WebSocketSession session) {
        return this.currentUserList.contains(session);
    }

    public boolean isPrivateGame() {
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

    public List<WebSocketSession> getCurrentUserList() {
        return currentUserList;
    }

    public void setCurrentUserList(List<WebSocketSession> currentUserList) {
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

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Map<WebSocketSession, Integer> getPoints() {
        return points;
    }

    public void setPoints(Map<WebSocketSession, Integer> points) {
        this.points = points;
    }
}
