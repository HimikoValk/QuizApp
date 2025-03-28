package com.himiko.network.protocol.data;


import com.himiko.game.utils.UserData;

import java.util.Map;

/**
 * @author Valk on 28.03.2025
 * @project quizClient
 */
public class ScoreData {
    private Map<UserData, Integer> userScoreMap;
    private int score;

    public ScoreData(Map<UserData, Integer> userScoreMap, int score) {
        this.userScoreMap = userScoreMap;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public Map<UserData, Integer> getUserScoreMap() {
        return this.userScoreMap;
    }
}
