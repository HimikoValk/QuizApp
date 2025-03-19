package com.himiko.server.protocol.data;

public class GameJoinData {
    private long gameID;
    private Integer code; //Koennte null sein deswegen Integer und nicht int

    public GameJoinData(long gameID, Integer code) {
        this.gameID = gameID;
        this.code = code;
    }

    public long getGameID() {
        return gameID;
    }

    public Integer getCode() {
        return code;
    }
}
