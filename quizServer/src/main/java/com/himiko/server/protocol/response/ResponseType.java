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
    GAME_INFO,
    SCORE_INFO,

    GAMES,
    SERVER_INFORMATION,
    QUESTION_INFO,
    ANSWER_RESULT
}
