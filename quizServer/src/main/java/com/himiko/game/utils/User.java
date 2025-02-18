package com.himiko.game.utils;


/**
 * @author Valk on 16.02.2025
 * @project quizServer
 */
public class User {
    private String name;
    private int totalGames;
    private int wins;

    //Default Constructor
    public User()
    {
    }

    public User(String name, int totalGames, int wins)
    {
        this.name = name;
        this.totalGames = totalGames;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
