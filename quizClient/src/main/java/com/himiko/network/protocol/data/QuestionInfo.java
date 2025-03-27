package com.himiko.network.protocol.data;


/**
 * @author Valk on 27.03.2025
 * @project quizClient
 */
public class QuestionInfo {
    private String question;
    private String[] options;

    public QuestionInfo(String question, String[] options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
