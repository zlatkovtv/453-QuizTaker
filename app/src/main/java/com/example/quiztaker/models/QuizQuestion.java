package com.example.quiztaker.models;

import java.util.List;

public class QuizQuestion {
    private String questionName;
    private List<QuizOption> options;

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
