package com.expedite.apps.kumkum.model;

public class ActivityVisitedModel {
    String StudiD;
    String SchoolId;
    String ActivityName;
    String Count;
    String DateVisited;

    public ActivityVisitedModel(String studiD, String schoolId, String activityName, String count
            , String date) {
        StudiD = studiD;
        SchoolId = schoolId;
        ActivityName = activityName;
        Count = count;
        DateVisited = date;
    }

    public ActivityVisitedModel() {
    }

    public String getDateVisited() {
        return DateVisited;
    }

    public void setDateVisited(String dateVisited) {
        DateVisited = dateVisited;
    }

    public String getStudiD() {
        return StudiD;
    }

    public void setStudiD(String studiD) {
        StudiD = studiD;
    }

    public String getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(String schoolId) {
        SchoolId = schoolId;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

}
