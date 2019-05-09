package com.example.quiztaker.models;

public class QuizData {
    private String quizName;
    private String creatorName;
    public QuizData(String quizName, String creatorName)
    {
        this.quizName = quizName;
        this.creatorName = creatorName;
    }
    public String getQuizName()
    {
        return quizName;
    }

    public String getCreatorName()
    {
        return creatorName;
    }
}
