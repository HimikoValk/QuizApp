package com.himiko.game.elemtents;

public class Question {
    private final String question;
    private final String answer;
    private final String[] options;
    private final QuestionCategory category;

    public Question(String question,QuestionCategory category)
    {
        this.question = question;
        this.answer = null;
        this.options = null;
        this.category = category;
    }

    public Question(String question,String answer,String[] options,QuestionCategory category)
    {
        this.question = question;
        this.answer = answer;
        this.options = options;
        this.category = category;
    }

    public Question(String question,String answer,QuestionCategory category)
    {
        this.question = question;
        this.answer = answer;
        this.options = null;
        this.category = category;
    }

    public Question(String question,String[] options,QuestionCategory category)
    {
        this.question = question;
        this.answer = null;
        this.options = options;
        this.category = category;
    }

    public String getAnswer() {
        return answer;
    }

    public String[] getOptions() {
        return this.options;
    }

    public QuestionCategory getCategory() {
        return this.category;
    }

    public String getQuestion() {
        return this.question;
    }
}
