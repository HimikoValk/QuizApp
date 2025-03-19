package com.himiko.server.protocol.request;

public enum RequestType {
    USER_LOGIN,   // Login-Request
    USER_LOGOUT,  // Logout-Request

    GAME_CREATE,  // Create game
    GAME_JOIN,    // Join game
    GET_GAMES,
    GAME_LEAVE,   // Leave game
    GAME_EDIT,    // Edit game

    SERVER_INFORMATION
}
