package com.himiko.server.protocol.request;

public enum RequestType {
    USER_LOGIN,   // Login-Request
    USER_LOGOUT,   // Logout-Request
    USER_DATA,    // Request for UserData
    GAME_CREATE,  // Create game
    GAME_JOIN,    // Join game
    GAME_LEAVE,   // Leave game
    GAME_EDIT,     // Edit game
    GAME_INFO,
    SERVER_INFORMATION
}
