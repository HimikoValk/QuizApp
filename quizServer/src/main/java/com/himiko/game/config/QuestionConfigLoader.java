package com.himiko.game.config;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.himiko.game.elements.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Valk on 28.03.2025
 * @project quizServer
 */
public class QuestionConfigLoader {
    public static List<Question> loadQuestionData(String path)
    {
        Gson gson = new Gson();
        try {
             BufferedReader reader = new BufferedReader(new FileReader(path));
            Type configType = new TypeToken<QuestionConfig>(){}.getType();
            QuestionConfig config = gson.fromJson(reader, configType);

            return config.getQuestions().stream()
                    .map(qd -> new Question(qd.getQuestion(), qd.getAnswer(), qd.getOptions(), qd.getCategory()))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class QuestionConfig
    {
        private List<Question> questions;

        public List<Question> getQuestions() {
            return this.questions;
        }
    }

}
