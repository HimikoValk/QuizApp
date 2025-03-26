package com.himiko.game.elements;

public class Question {
    private final String question;
    private final String answer;
    private final QuestionCategory category;
    private boolean used = false;

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

    public void setUsed(boolean used) {
        this.used = used;
    }

    public QuestionCategory getCategory() {
        return this.category;
    }

    public boolean isUsed() {
        return this.used;
    }
}
