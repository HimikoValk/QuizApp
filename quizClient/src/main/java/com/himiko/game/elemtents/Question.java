package com.himiko.game.elemtents;

public class Question {
    private final String question;
    private final QuestionCategory category;

    public Question(String question, QuestionCategory category)
    {
        this.question = question;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }
}
