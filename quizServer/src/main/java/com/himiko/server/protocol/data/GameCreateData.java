package com.himiko.server.protocol.data;


import com.himiko.game.elements.Question;
import com.himiko.game.utils.User;

/**
 * @author Valk on 21.03.2025
 * @project quizServer
 */
public class GameCreateData {
    private int maxUserSize;
    private boolean privateGame;
    private boolean autoStart;
    private Question[] questions;

    public GameCreateData(int maxUserSize, boolean privateGame,boolean autoStart, Question[] questions) {
        this.maxUserSize = maxUserSize;
        this.privateGame = privateGame;
        this.autoStart = autoStart;
        this.questions = questions;
    }

    public int getMaxUserSize() {
        return this.maxUserSize;
    }

    public void setMaxUserSize(int maxUserSize) {
        this.maxUserSize = maxUserSize;
    }

    public boolean isPrivateGame() {
        return this.privateGame;
    }

    public void setPrivateGame(boolean privateGame) {
        this.privateGame = privateGame;
    }

    public boolean isAutoStart() {
        return this.autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Question[] getQuestions() {
        return this.questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }
}
