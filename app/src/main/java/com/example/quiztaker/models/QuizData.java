package com.example.quiztaker.models;

public class QuizData {
    private String quizID;
    private String quizName;
    private String creatorName;
    public QuizData(String id, String quizName, String creatorName)
    {
        this.quizID = id;
        this.quizName = quizName;
        this.creatorName = creatorName;
    }

    public String getID() {
        return quizID;
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
