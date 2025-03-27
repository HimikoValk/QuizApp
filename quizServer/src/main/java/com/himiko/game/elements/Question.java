package com.himiko.game.elements;

public class Question {
    private final String question;
    private final String answer;
    private final String[] options;
    private final QuestionCategory category;
    private boolean used = false;

    public Question(String question, String answer, String[] options, QuestionCategory category)
    {
        this.question = question;
        this.answer = answer;
        this.options = options;
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

    public String[] getOptions() {
        return this.options;
    }

    public QuestionCategory getCategory() {
        return this.category;
    }

    public boolean isUsed() {
        return this.used;
    }
}
