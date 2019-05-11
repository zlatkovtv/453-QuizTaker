package com.example.quiztaker.models;

public class QuizCreateData {
    private String quizCreator;
    private String quizTitle;
    public QuizCreateData(String quizCreator, String quizTitle){
        this.quizCreator = quizCreator;
        this.quizTitle = quizTitle;
    }

    public String getQuizCreator() {
        return quizCreator;
    }

    public String getQuizTitle() {
        return quizTitle;
    }
}
