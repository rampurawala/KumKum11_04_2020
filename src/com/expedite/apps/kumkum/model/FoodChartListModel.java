
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodChartListModel {

    @SerializedName("arrays")
    @Expose
    private List<Array> arrays = null;

    public List<Array> getArrays() {
        return arrays;
    }

    @SerializedName("isTeacherDisplay")
    @Expose
    private boolean isTeacherDisplay = true;

    @SerializedName("isSubjectDisplay")
    @Expose
    private boolean isSubjectDisplay = true;

    @SerializedName("isTimeDisplay")
    @Expose
    private boolean isTimeDisplay = true;


    public void setIsTeacherDisplay(boolean TeacherDisplay) {
        this.isTeacherDisplay = TeacherDisplay;
    }

    public boolean getIsTeacherDisplay() {
        return isTeacherDisplay;
    }

    public void setIsSubjectDisplay(boolean SubjectDisplay) {
        this.isSubjectDisplay = SubjectDisplay;
    }

    public boolean getIsSubjectDisplay() {
        return isSubjectDisplay;
    }

    public void setIsTimeDisplay(boolean TimeDisplay) {
        this.isTimeDisplay = TimeDisplay;
    }

    public boolean getIsTimeDisplay() {
        return isTimeDisplay;
    }

    public void setArrays(List<Array> arrays) {
        this.arrays = arrays;
    }

    public class Array {

        @SerializedName("response")
        @Expose
        private String response;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("dayId")
        @Expose
        private String dayId;
        @SerializedName("timeTableList")
        @Expose
        private List<TimeTableList> timeTableList = null;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDayId() {
            return dayId;
        }

        public void setDayId(String dayId) {
            this.dayId = dayId;
        }

        public List<TimeTableList> getTimeTableList() {
            return timeTableList;
        }

        public void setTimeTableList(List<TimeTableList> timeTableList) {
            this.timeTableList = timeTableList;
        }

    }

    public class TimeTableList {

        @SerializedName("ttime")
        @Expose
        private String ttime;
        @SerializedName("lectuername")
        @Expose
        private String lectuername;
        @SerializedName("teachername")
        @Expose
        private String teachername;

        public String getTtime() {
            return ttime;
        }

        public void setTtime(String ttime) {
            this.ttime = ttime;
        }

        public String getLectuername() {
            return lectuername;
        }

        public void setLectuername(String lectuername) {
            this.lectuername = lectuername;
        }

        public String getTeachername() {
            return teachername;
        }

        public void setTeachername(String teachername) {
            this.teachername = teachername;
        }

    }

}
