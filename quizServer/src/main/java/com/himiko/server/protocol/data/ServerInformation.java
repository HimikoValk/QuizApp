package com.himiko.server.protocol.data;

public class ServerInformation {
    private int playerCount;
    private int openGames;
    private String serverVersion;

    public ServerInformation(int playerCount, int openGames, String serverVersion) {
        this.playerCount = playerCount;
        this.openGames = openGames;
        this.serverVersion = serverVersion;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getOpenGames() {
        return openGames;
    }

    public String getServerVersion() {
        return serverVersion;
    }
}
