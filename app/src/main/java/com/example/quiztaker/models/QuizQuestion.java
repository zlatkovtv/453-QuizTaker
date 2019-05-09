package com.example.quiztaker.models;

import java.util.List;

public class QuizQuestion {
    private String id;
    private String questionName;
    private List<QuizOption> options;

    public QuizQuestion(String id, String name, List<QuizOption> options) {
        this.id = id;
        this.questionName = name;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public List<QuizOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuizOption> options) {
        this.options = options;
    }
}
