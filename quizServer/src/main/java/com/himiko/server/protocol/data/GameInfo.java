package com.himiko.server.protocol.data;

import com.himiko.game.GameState;

import java.util.List;

public class GameInfo {
    private long gameID;
    private int maxUserSize;
    private int currentPlayers;
    private List<String> playerNames;
    private boolean privateGame;
    private Integer code;
    private String creatorName;
    private GameState gameState;

    public GameInfo(long gameID, int maxUserSize, int currentPlayers, List<String> playerNames, boolean privateGame, Integer code, String creatorName, GameState gameState) {
        this.gameID = gameID;
        this.maxUserSize = maxUserSize;
        this.currentPlayers = currentPlayers;
        this.playerNames = playerNames;
        this.privateGame = privateGame;
        this.code = code;
        this.creatorName = creatorName;
        this.gameState = gameState;
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public int getMaxUserSize() {
        return maxUserSize;
    }

    public void setMaxUserSize(int maxUserSize) {
        this.maxUserSize = maxUserSize;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public boolean isPrivateGame() {
        return privateGame;
    }

    public void setPrivateGame(boolean privateGame) {
        this.privateGame = privateGame;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}