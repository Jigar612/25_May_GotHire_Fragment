package com.jigar.android.gothire.SetterGetter;

import java.util.List;

/**
 * Created by COMP11 on 14-Mar-18.
 */

public class RowItem_ListOFQuestion {


    String IsAttended;
    String IsCorrect;

    public String getIsAttended() {
        return IsAttended;
    }

    public void setIsAttended(String isAttended) {
        IsAttended = isAttended;
    }

    public String getIsCorrect() {
        return IsCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        IsCorrect = isCorrect;
    }

    public String getIsNonObjective() {
        return IsNonObjective;
    }

    public void setIsNonObjective(String isNonObjective) {
        IsNonObjective = isNonObjective;
    }

    public String getIsWrong() {
        return IsWrong;
    }

    public void setIsWrong(String isWrong) {
        IsWrong = isWrong;
    }

    public String getQueId() {
        return QueId;
    }

    public void setQueId(String queId) {
        QueId = queId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getRoundId() {
        return RoundId;
    }

    public void setRoundId(String roundId) {
        RoundId = roundId;
    }

    public String getVideoType() {
        return VideoType;
    }

    public void setVideoType(String videoType) {
        VideoType = videoType;
    }

    public String getVideoURl() {
        return VideoURl;
    }

    public void setVideoURl(String videoURl) {
        VideoURl = videoURl;
    }

    public String getNonAttendQueCount() {
        return NonAttendQueCount;
    }

    public void setNonAttendQueCount(String nonAttendQueCount) {
        NonAttendQueCount = nonAttendQueCount;
    }

    public String getNonObjectiveQueCount() {
        return NonObjectiveQueCount;
    }

    public void setNonObjectiveQueCount(String nonObjectiveQueCount) {
        NonObjectiveQueCount = nonObjectiveQueCount;
    }

    public String getCorrectQueCount() {
        return CorrectQueCount;
    }

    public void setCorrectQueCount(String correctQueCount) {
        CorrectQueCount = correctQueCount;
    }

    public String getWrongQueCount() {
        return WrongQueCount;
    }

    public void setWrongQueCount(String wrongQueCount) {
        WrongQueCount = wrongQueCount;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public List<RowItem_ListOFAnswer> getListOfAnswer() {
        return ListOfAnswer;
    }

    public void setListOfAnswer(List<RowItem_ListOFAnswer> listOfAnswer) {
        ListOfAnswer = listOfAnswer;
    }

    String IsNonObjective;
    String IsWrong;
    String QueId;
    String Question;
    String RoundId;
    String VideoType;
    String VideoURl;
    String  NonAttendQueCount,NonObjectiveQueCount,CorrectQueCount,WrongQueCount,Answer;
    List<RowItem_ListOFAnswer>ListOfAnswer;
}
