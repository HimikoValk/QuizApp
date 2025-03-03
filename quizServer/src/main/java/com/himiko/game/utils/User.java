package com.himiko.game.utils;


/**
 * @author Valk on 16.02.2025
 * @project quizServer
 */
public class User {
    private String name;
    private int totalGames;
    private int wins;
    private long id;
    
    //Default Constructor
    public User()
    {
    }

    public User(String name, int totalGames, int wins, long id)
    {
        this.name = name;
        this.totalGames = totalGames;
        this.wins = wins;
        this.id = id;
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

    public void setId(long id){
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
