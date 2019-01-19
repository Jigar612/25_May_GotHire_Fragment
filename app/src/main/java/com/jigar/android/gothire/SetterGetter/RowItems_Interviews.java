package com.jigar.android.gothire.SetterGetter;

import java.util.ArrayList;

/**
 * Created by COMP11 on 20-Apr-18.
 */

public class RowItems_Interviews {
    public String getCandidateID() {
        return CandidateID;
    }

    public void setCandidateID(String candidateID) {
        CandidateID = candidateID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    String CandidateID;
    String city,salary;


    public String getJopRefNo() {
        return JopRefNo;
    }

    public void setJopRefNo(String jopRefNo) {
        JopRefNo = jopRefNo;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getJobDescription() {
        return JobDescription;
    }

    public void setJobDescription(String jobDescription) {
        JobDescription = jobDescription;
    }

    public String getJobPostId() {
        return JobPostId;
    }

    public void setJobPostId(String jobPostId) {
        JobPostId = jobPostId;
    }

    public String getInterviewStartDate() {
        return InterviewStartDate;
    }

    public void setInterviewStartDate(String interviewStartDate) {
        InterviewStartDate = interviewStartDate;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getTotalStage() {
        return TotalStage;
    }

    public void setTotalStage(String totalStage) {
        TotalStage = totalStage;
    }

    public String getInterviewStatus() {
        return InterviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        InterviewStatus = interviewStatus;
    }

    public String getInterviewStatusName() {
        return InterviewStatusName;
    }

    public void setInterviewStatusName(String interviewStatusName) {
        InterviewStatusName = interviewStatusName;
    }

    public String getInterviewAcceptDateDisplay() {
        return InterviewAcceptDateDisplay;
    }

    public void setInterviewAcceptDateDisplay(String interviewAcceptDateDisplay) {
        InterviewAcceptDateDisplay = interviewAcceptDateDisplay;
    }

    String JopRefNo,CompanyName,JobTitle,JobDescription,JobPostId,InterviewStartDate,Days,TotalStage,InterviewStatus,InterviewStatusName,InterviewAcceptDateDisplay;

    public String getImage_path() {
        return Image_path;
    }

    public void setImage_path(String image_path) {
        Image_path = image_path;
    }

    String Image_path;

    public ArrayList<RowItem_Track_staus> getArrayList_status() {
        return arrayList_status;
    }

    public void setArrayList_status(ArrayList<RowItem_Track_staus> arrayList_status) {
        this.arrayList_status = arrayList_status;
    }

    ArrayList<RowItem_Track_staus>arrayList_status;



}
