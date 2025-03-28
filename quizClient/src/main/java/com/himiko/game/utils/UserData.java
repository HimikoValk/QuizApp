package com.himiko.game.utils;

public class UserData {
    private String name;
    private long id;

    public UserData(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }
}
