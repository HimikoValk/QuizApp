package com.himiko.server.protocol.data;


import com.himiko.game.utils.User;

import java.util.Map;

/**
 * @author Valk on 28.03.2025
 * @project quizClient
 */
public class ScoreData {
    private Map<User, Integer> userScoreMap;
    private int score;

    public ScoreData(Map<User, Integer> userScoreMap, int score) {
        this.userScoreMap = userScoreMap;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public Map<User, Integer> getUserScoreMap() {
        return this.userScoreMap;
    }
}
