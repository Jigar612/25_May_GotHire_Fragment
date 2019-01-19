package com.jigar.android.gothire.SetterGetter;

/**
 * Created by COMP11 on 17-May-18.
 */

public class RowItem_Track_staus {


    String JobTitle;
    String cpyName;
    String JobPostId;

    public String getCandidate_id() {
        return Candidate_id;
    }

    public void setCandidate_id(String candidate_id) {
        Candidate_id = candidate_id;
    }

    String Candidate_id;

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getCpyName() {
        return cpyName;
    }

    public void setCpyName(String cpyName) {
        this.cpyName = cpyName;
    }

    public String getJobPostId() {
        return JobPostId;
    }

    public void setJobPostId(String jobPostId) {
        JobPostId = jobPostId;
    }

    public String getInterviewStatus() {
        return InterviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        InterviewStatus = interviewStatus;
    }

    public String getImage_path() {
        return Image_path;
    }

    public void setImage_path(String image_path) {
        Image_path = image_path;
    }

    String InterviewStatus;
    String Image_path;
}
