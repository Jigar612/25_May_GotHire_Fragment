package com.jigar.android.gothire.SetterGetter;

/**
 * Created by COMP11 on 06-Mar-18.
 */

public class RowItemGetAcceptedJobResult {
    int id_fetch;

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

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    String JobType;

    public int getId_fetch() {
        return id_fetch;
    }

    public void setId_fetch(int id_fetch) {
        this.id_fetch = id_fetch;
    }

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
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    String image_path;

}
