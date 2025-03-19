package com.himiko.server.protocol.response;

public enum ResponseType {
    SUCCESS,
    FAILURE,
    ERROR,            // Allgemeine Fehlermeldung

    GAME_JOINED,
    GAME_CREATED,
    GAME_EDITED,
    GAME_STARTED,
    GAME_ENDED,

    GAMES,
    SERVER_INFORMATION,
    QUESTION,
    ANSWER_RESULT
}
