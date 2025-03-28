package com.himiko.network.protocol.request;

public enum RequestType {
    USER_LOGIN,
    USER_LOGOUT,

    GET_GAMES,
    GET_GAME_INFO,
    GAME_CREATE,
    GAME_START,
    GAME_JOIN,
    GAME_LEAVE,
    GAME_EDIT,
    GET_QUESTION_INFO,
    SCORE_INFO,

    SERVER_INFORMATION,
    ANSWER,
}
