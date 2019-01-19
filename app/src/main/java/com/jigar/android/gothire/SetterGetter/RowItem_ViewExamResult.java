package com.jigar.android.gothire.SetterGetter;

import java.util.List;

/**
 * Created by COMP11 on 14-Mar-18.
 */

public class RowItem_ViewExamResult {

        String ComplitionTime,CorrectQueCount,Examstartid,IsStillIntrasted,JobId,NonAttendQueCount,NonObjectiveQueCount,Note;
        String StageID;

    public String getComplitionTime() {
        return ComplitionTime;
    }

    public void setComplitionTime(String complitionTime) {
        ComplitionTime = complitionTime;
    }

    public String getCorrectQueCount() {
        return CorrectQueCount;
    }

    public void setCorrectQueCount(String correctQueCount) {
        CorrectQueCount = correctQueCount;
    }

    public String getExamstartid() {
        return Examstartid;
    }

    public void setExamstartid(String examstartid) {
        Examstartid = examstartid;
    }

    public String getIsStillIntrasted() {
        return IsStillIntrasted;
    }

    public void setIsStillIntrasted(String isStillIntrasted) {
        IsStillIntrasted = isStillIntrasted;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
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

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getStageID() {
        return StageID;
    }

    public void setStageID(String stageID) {
        StageID = stageID;
    }

    public String getStageName() {
        return StageName;
    }

    public void setStageName(String stageName) {
        StageName = stageName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getTotalMandQue() {
        return TotalMandQue;
    }

    public void setTotalMandQue(String totalMandQue) {
        TotalMandQue = totalMandQue;
    }

    public String getTotalQue() {
        return TotalQue;
    }

    public void setTotalQue(String totalQue) {
        TotalQue = totalQue;
    }

    public String getWrongQueCount() {
        return WrongQueCount;
    }

    public void setWrongQueCount(String wrongQueCount) {
        WrongQueCount = wrongQueCount;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

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

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getStrt_id() {
        return strt_id;
    }

    public void setStrt_id(String strt_id) {
        this.strt_id = strt_id;
    }

    public List<RowItem_ListOFQuestion> getListOfQuestion() {
        return ListOfQuestion;
    }

    public void setListOfQuestion(List<RowItem_ListOFQuestion> listOfQuestion) {
        ListOfQuestion = listOfQuestion;
    }

    String StageName;
    String Status;
    String StatusName;
    String TotalMandQue;
    String TotalQue;
    String WrongQueCount;
    String Question;
    String Answer;
    String AnswerType;
    String jobname;
    String strt_id;
        List<RowItem_ListOFQuestion>ListOfQuestion;


}
