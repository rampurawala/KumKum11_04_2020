
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TempSubmissionFileListModel {

   String filename="",date="",time="",subjectname="";

    public TempSubmissionFileListModel() {
    }

    public TempSubmissionFileListModel(String filename, String date, String time, String subjectname) {
        this.filename = filename;
        this.date = date;
        this.time = time;
        this.subjectname = subjectname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }
}