package com.himiko.server.protocol.response;

public enum ResponseType {
    LOGIN_SUCCESS,   // Erfolgreicher Login
    LOGIN_FAILED,    // Fehlgeschlagener Login
    USER_DATA,       // Antwort mit User-Daten
    PLAYER_COUNT,    // Anzahl aktiver Spieler
    GAME_CREATED,    // Spiel erfolgreich erstellt
    GAME_JOINED,     // Erfolgreich beigetreten
    GAME_LEFT,       // Erfolgreich verlassen
    GAME_EDITED,     // Spiel erfolgreich bearbeitet
    SERVER_INFORMATION,//
    ERROR            // Allgemeine Fehlermeldung
}
