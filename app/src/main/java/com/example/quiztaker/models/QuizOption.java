package com.example.quiztaker.models;

public class QuizOption {
    private String optionName;
    private boolean isCorrect;

    public QuizOption(String name, boolean isCorrect) {
        this.optionName = name;
        this.isCorrect = isCorrect;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
