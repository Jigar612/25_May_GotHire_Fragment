package com.jigar.android.gothire.SetterGetter;

/**
 * Created by COMP11 on 05-Oct-18.
 */

public class RowItemEducation {

    String School;
    String FiledOfStdy;

    public String getSchool() {
        return School;
    }

    public String getEducation() {
        return Education;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    String Location;

    public void setEducation(String education) {
        Education = education;
    }

    String Education;

    public void setSchool(String school) {
        School = school;
    }

    public String getFiledOfStdy() {
        return FiledOfStdy;
    }

    public void setFiledOfStdy(String filedOfStdy) {
        FiledOfStdy = filedOfStdy;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getTodate() {
        return Todate;
    }

    public void setTodate(String todate) {
        Todate = todate;
    }

    String FromDate;
    String Todate;

}
