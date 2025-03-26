package com.himiko.game.elemtents;

public class Question {
    private final String question;
    private final String answer;
    private final QuestionCategory category;

    public Question(String question,QuestionCategory category)
    {
        this.question = question;
        this.answer = "";
        this.category = category;
    }

    public Question(String question, String answer,QuestionCategory category)
    {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    public String getAnswer() {
        return answer;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }
}
