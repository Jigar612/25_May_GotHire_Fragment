package com.jigar.android.gothire.SetterGetter;

import java.util.List;

/**
 * Created by COMP11 on 19-Mar-18.
 */

public class RowItem_Notification {


    String msg,OpenActivityID,RoundID,JobID,job_nm,cpy_nm,job_desc,interv_accept_dt;

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    String createdOn;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOpenActivityID() {
        return OpenActivityID;
    }

    public void setOpenActivityID(String openActivityID) {
        OpenActivityID = openActivityID;
    }

    public String getRoundID() {
        return RoundID;
    }

    public void setRoundID(String roundID) {
        RoundID = roundID;
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public String getJob_nm() {
        return job_nm;
    }

    public void setJob_nm(String job_nm) {
        this.job_nm = job_nm;
    }

    public String getCpy_nm() {
        return cpy_nm;
    }

    public void setCpy_nm(String cpy_nm) {
        this.cpy_nm = cpy_nm;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getInterv_accept_dt() {
        return interv_accept_dt;
    }

    public void setInterv_accept_dt(String interv_accept_dt) {
        this.interv_accept_dt = interv_accept_dt;
    }

    public String getCandidate_id() {
        return Candidate_id;
    }

    public void setCandidate_id(String candidate_id) {
        Candidate_id = candidate_id;
    }

    String Candidate_id;

    public String getExamstartid() {
        return Examstartid;
    }

    public void setExamstartid(String examstartid) {
        Examstartid = examstartid;
    }

    String Examstartid;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String Title,days,time;

    public List<RowItems_Interviews> getList_jobNameData() {
        return list_jobNameData;
    }

    public void setList_jobNameData(List<RowItems_Interviews> list_jobNameDate) {
        this.list_jobNameData = list_jobNameData;
    }

    List<RowItems_Interviews>list_jobNameData;

}
