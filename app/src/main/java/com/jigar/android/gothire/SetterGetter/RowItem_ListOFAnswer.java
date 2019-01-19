package com.jigar.android.gothire.SetterGetter;

/**
 * Created by COMP11 on 14-Mar-18.
 */

public class RowItem_ListOFAnswer {

//    public string Answer { get; set; }
//    public string AnswerType { get; set; }
//    public int QueId { get; set; }
//    public int examStartid { get; set; }
//    public int? istrue { get; set; }
    String Answer;
    String AnswerType;
    String QueId;

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getAnswerType() {
        return AnswerType;
    }

    public void setAnswerType(String answerType) {
        AnswerType = answerType;
    }

    public String getQueId() {
        return QueId;
    }

    public void setQueId(String queId) {
        QueId = queId;
    }

    public String getExamStartId() {
        return examStartId;
    }

    public void setExamStartId(String examStartId) {
        this.examStartId = examStartId;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    String examStartId;
    String isTrue;
}
