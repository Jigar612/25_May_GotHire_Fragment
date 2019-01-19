package com.jigar.android.gothire.SetterGetter;

/**
 * Created by COMP11 on 06-Mar-18.
 */

public class RowItemGetRoundWiseJob {

    String Candidate_id;
    String Roundname;
    String ExamStartdate;
    String ExamEnddate;
    String IsStart;
    String Roundstatus;
    String Color;
    String RoundID;
    String ProcessTIme;
    String TotalProcessTime;
    String status;
    String FinalStage;
    String IsOffer;

    public String getIsOffer() {
        return IsOffer;
    }

    public void setIsOffer(String isOffer) {
        IsOffer = isOffer;
    }

    public String getFinalStage() {
        return FinalStage;
    }

    public void setFinalStage(String finalStage) {
        FinalStage = finalStage;
    }

    public String getTotalReamingTime() {
        return TotalReamingTime;
    }

    public void setTotalReamingTime(String totalReamingTime) {
        TotalReamingTime = totalReamingTime;
    }

    String TotalReamingTime;
    int jobPost_id;


    String CpyName;
    String JobName;
    String IntervAcceptDt;
    String ExamStartID;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    String Desc;
    public String getCpyName() {
        return CpyName;
    }

    public void setCpyName(String cpyName) {
        CpyName = cpyName;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getIntervAcceptDt() {
        return IntervAcceptDt;
    }

    public void setIntervAcceptDt(String intervAcceptDt) {
        IntervAcceptDt = intervAcceptDt;
    }

    public String getExamStartID() {
        return ExamStartID;
    }

    public void setExamStartID(String examStartID) {
        ExamStartID = examStartID;
    }



    public String getCandidate_id() {
        return Candidate_id;
    }

    public void setCandidate_id(String candidate_id) {
        Candidate_id = candidate_id;
    }

    public String getRoundname() {
        return Roundname;
    }

    public void setRoundname(String roundname) {
        Roundname = roundname;
    }

    public String getExamStartdate() {
        return ExamStartdate;
    }

    public void setExamStartdate(String examStartdate) {
        ExamStartdate = examStartdate;
    }

    public String getExamEnddate() {
        return ExamEnddate;
    }

    public void setExamEnddate(String examEnddate) {
        ExamEnddate = examEnddate;
    }

    public String getIsStart() {
        return IsStart;
    }

    public void setIsStart(String isStart) {
        IsStart = isStart;
    }

    public String getRoundstatus() {
        return Roundstatus;
    }

    public void setRoundstatus(String roundstatus) {
        Roundstatus = roundstatus;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getRoundID() {
        return RoundID;
    }

    public void setRoundID(String roundID) {
        RoundID = roundID;
    }

    public String getProcessTIme() {
        return ProcessTIme;
    }

    public void setProcessTIme(String processTIme) {
        ProcessTIme = processTIme;
    }

    public String getTotalProcessTime() {
        return TotalProcessTime;
    }

    public void setTotalProcessTime(String totalProcessTime) {
        TotalProcessTime = totalProcessTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getJobPost_id() {
        return jobPost_id;
    }

    public void setJobPost_id(int jobPost_id) {
        this.jobPost_id = jobPost_id;
    }



    //New for JobRound

//    getRoundJob.Desc = jobdesc;
//    getRoundJob.CpyName = companyname;
//    getRoundJob.JobName = jobname;
//    getRoundJob.IntervAcceptDt = accepted_date;
//    getRoundJob.ExamStartID = abc.ExamStartID;
}
