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
    }

    //Custom game constructor (Private game)
    public Game(int maxPlayerSize, final long gameID, final User gameCreator, boolean privateGame)
    {
        this.maxUserSize = maxPlayerSize;
        this.gameID = gameID;
        this.gameCreator = gameCreator;
        this.privateGame = privateGame;
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

    public boolean isUserInGame(NetworkClient client)
    {
        return this.currentUserList.stream().anyMatch(c -> c.getClient().getRemoteSocketAddress().equals(client.getClient().getRemoteSocketAddress()));
    }

    public boolean isPrivateGame()
    {
        return this.privateGame;
    }
}
