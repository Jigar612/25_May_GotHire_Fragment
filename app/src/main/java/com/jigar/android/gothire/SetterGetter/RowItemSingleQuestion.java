package com.jigar.android.gothire.SetterGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMP11 on 08-Mar-18.
 */

public class RowItemSingleQuestion {
    String QuestionRecord;
    String Second_from_single;
    String VideoValidation;
    String Question;
    String QuestionDesc;

    String CandidateID,responsetype_id,RoundID,JobId,MsaterQuestionID,ExamstartId,QueId,jobprocesstime,startdate;
    String video_ans,job_name,seconds_video_valida,QuestionDisplay,totalprocesstime,DateTimeValidations,stage_name;
    String  company_name,job_desc,accepted_on,FilesizeType,Filesize,DescriptionValidation,fileformateType,from_date,to_date;

    String ansList_Id;
    String ansList_option;
    String ansList_responseType;
    int ansListCount;
    String ansList_IsTrue;
    String ansList_QueId;

    public String getLeftLabel() {
        return LeftLabel;
    }

    public void setLeftLabel(String leftLabel) {
        LeftLabel = leftLabel;
    }

    public String getRighLabel() {
        return RighLabel;
    }

    public void setRighLabel(String righLabel) {
        RighLabel = righLabel;
    }

    public String getCenterLabel() {
        return CenterLabel;
    }

    public void setCenterLabel(String centerLabel) {
        CenterLabel = centerLabel;
    }

    String LeftLabel;
    String RighLabel;
    String CenterLabel;

    public ArrayList<String> getArrayList_question_id() {
        return arrayList_question_id;
    }

    public void setArrayList_question_id(ArrayList<String> arrayList_question_id) {
        this.arrayList_question_id = arrayList_question_id;
    }

    public ArrayList<String> getArrayList_question_mst_id() {
        return arrayList_question_mst_id;
    }

    public void setArrayList_question_mst_id(ArrayList<String> arrayList_question_mst_id) {
        this.arrayList_question_mst_id = arrayList_question_mst_id;
    }

    public ArrayList<String>arrayList_question_id;
    public ArrayList<String>arrayList_question_mst_id;

    public List<RowItemAnsList> getAnsList() {
        return AnsList;
    }

    public void setAnsList(List<RowItemAnsList> ansList) {
        AnsList = ansList;
    }

    List<RowItemAnsList>AnsList;




    public int getAnsListCount() {
        return ansListCount;
    }

    public void setAnsListCount(int ansListCount) {
        this.ansListCount = ansListCount;
    }



    public String getAnsList_responseType() {
        return ansList_responseType;
    }

    public void setAnsList_responseType(String ansList_responseType) {
        this.ansList_responseType = ansList_responseType;
    }

    public String getAnsList_IsTrue() {
        return ansList_IsTrue;
    }

    public void setAnsList_IsTrue(String ansList_IsTrue) {
        this.ansList_IsTrue = ansList_IsTrue;
    }



    public String getAnsList_QueId() {
        return ansList_QueId;
    }

    public void setAnsList_QueId(String ansList_QueId) {
        this.ansList_QueId = ansList_QueId;
    }








    public String getQuestionRecord() {
        return QuestionRecord;
    }

    public void setQuestionRecord(String questionRecord) {
        QuestionRecord = questionRecord;
    }

    public String getSecond_from_single() {
        return Second_from_single;
    }

    public void setSecond_from_single(String second_from_single) {
        Second_from_single = second_from_single;
    }

    public String getVideoValidation() {
        return VideoValidation;
    }

    public void setVideoValidation(String videoValidation) {
        VideoValidation = videoValidation;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getQuestionDesc() {
        return QuestionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        QuestionDesc = questionDesc;
    }

    public String getCandidateID() {
        return CandidateID;
    }

    public void setCandidateID(String candidateID) {
        CandidateID = candidateID;
    }

    public String getResponsetype_id() {
        return responsetype_id;
    }

    public void setResponsetype_id(String responsetype_id) {
        this.responsetype_id = responsetype_id;
    }

    public String getRoundID() {
        return RoundID;
    }

    public void setRoundID(String roundID) {
        RoundID = roundID;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getMsaterQuestionID() {
        return MsaterQuestionID;
    }

    public void setMsaterQuestionID(String msaterQuestionID) {
        MsaterQuestionID = msaterQuestionID;
    }

    public String getExamstartId() {
        return ExamstartId;
    }

    public void setExamstartId(String examstartId) {
        ExamstartId = examstartId;
    }

    public String getQueId() {
        return QueId;
    }

    public void setQueId(String queId) {
        QueId = queId;
    }

    public String getJobprocesstime() {
        return jobprocesstime;
    }

    public void setJobprocesstime(String jobprocesstime) {
        this.jobprocesstime = jobprocesstime;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getVideo_ans() {
        return video_ans;
    }

    public void setVideo_ans(String video_ans) {
        this.video_ans = video_ans;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getSeconds_video_valida() {
        return seconds_video_valida;
    }

    public void setSeconds_video_valida(String seconds_video_valida) {
        this.seconds_video_valida = seconds_video_valida;
    }

    public String getQuestionDisplay() {
        return QuestionDisplay;
    }

    public void setQuestionDisplay(String questionDisplay) {
        QuestionDisplay = questionDisplay;
    }

    public String getTotalprocesstime() {
        return totalprocesstime;
    }

    public void setTotalprocesstime(String totalprocesstime) {
        this.totalprocesstime = totalprocesstime;
    }

    public String getDateTimeValidations() {
        return DateTimeValidations;
    }

    public void setDateTimeValidations(String dateTimeValidations) {
        DateTimeValidations = dateTimeValidations;
    }

    public String getStage_name() {
        return stage_name;
    }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getAccepted_on() {
        return accepted_on;
    }

    public void setAccepted_on(String accepted_on) {
        this.accepted_on = accepted_on;
    }

    public String getFilesizeType() {
        return FilesizeType;
    }

    public void setFilesizeType(String filesizeType) {
        FilesizeType = filesizeType;
    }

    public String getFilesize() {
        return Filesize;
    }

    public void setFilesize(String filesize) {
        Filesize = filesize;
    }

    public String getDescriptionValidation() {
        return DescriptionValidation;
    }

    public void setDescriptionValidation(String descriptionValidation) {
        DescriptionValidation = descriptionValidation;
    }

    public String getFileformateType() {
        return fileformateType;
    }

    public void setFileformateType(String fileformateType) {
        this.fileformateType = fileformateType;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getAnsList_Id() {
        return ansList_Id;
    }

    public void setAnsList_Id(String ansList_Id) {
        this.ansList_Id = ansList_Id;
    }

    public String getAnsList_option() {
        return ansList_option;
    }

    public void setAnsList_option(String ansList_option) {
        this.ansList_option = ansList_option;
    }
}
