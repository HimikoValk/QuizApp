package com.himiko.game.elements;

public class Question {
    private final String question;
    private final String answer;
    private final QuestionCategory category;

    public Question(String question, String answer, QuestionCategory category)
    {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
