package com.umut.myduolingo.model;

public class Question {
    String Question;
    String Answer1;
    String Answer2;

    public Question() {

    }

    public Question(String question, String answer1, String answer2) {
        Question = question;
        Answer1 = answer1;
        Answer2 = answer2;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer1() {
        return Answer1;
    }

    public void setAnswer1(String answer1) {
        Answer1 = answer1;
    }

    public String getAnswer2() {
        return Answer2;
    }

    public void setAnswer2(String answer2) {
        Answer2 = answer2;
    }
}
