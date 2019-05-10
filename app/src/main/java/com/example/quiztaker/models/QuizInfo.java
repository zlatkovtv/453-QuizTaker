package com.example.quiztaker.models;

public class QuizInfo {
    private String questionString, optionName1, optionName2, optionName3, optionName4 ;
    //String[] optionName = new String[4];
    public QuizInfo(){

    }
    public QuizInfo(String questionString, String optionName1, String optionName2, String optionName3, String optionName4){
        this.questionString = questionString;
        this.optionName1 = optionName1;
        this.optionName2 = optionName2;
        this.optionName3 = optionName3;
        this.optionName4 = optionName4;
    }

    public String getOptionName1() {
        return optionName1;
    }

    public String getOptionName2() {
        return optionName2;
    }

    public String getOptionName3() {
        return optionName3;
    }

    public String getOptionName4() {
        return optionName4;
    }

    public String getQuestionString() {
        return questionString;
    }


}
