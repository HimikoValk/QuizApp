package com.himiko.game;


import com.himiko.game.elemtents.Question;
import com.himiko.game.utils.UserData;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private long gameID;
    private int code;
    private Question currentQuestion;
    private List<UserData> userList;

    public Game(long gameID) {
        this.gameID = gameID;
        this.currentQuestion = null;
        this.userList = new ArrayList<>();
    }

    public Game(long gameID, int code) {
        this.gameID = gameID;
        this.code = code;
    }

    public void setCurrentQuestion(Question question)
    {
        this.currentQuestion = question;
    }

    public void setUserList(List<UserData> pUserList)
    {
        this.userList = pUserList;
    }

    public Question getCurrentQuestion() {
        return this.currentQuestion;
    }

    public int getCode() {
        return this.code;
    }

    public long getGameID() {
        return this.gameID;
    }

    public List<UserData> getUserList() {
        return this.userList;
    }
}