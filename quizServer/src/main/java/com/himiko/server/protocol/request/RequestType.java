package com.himiko.server.protocol.request;

public enum RequestType {
    USER_LOGIN,
    USER_LOGOUT,

    GAME_CREATE,
    GAME_START,
    GAME_JOIN,
    GET_GAMES,
    GAME_LEAVE,
    GAME_EDIT,
    GET_GAME_INFO,
    GET_QUESTION_INFO,

    SERVER_INFORMATION,
    ANSWER
}
