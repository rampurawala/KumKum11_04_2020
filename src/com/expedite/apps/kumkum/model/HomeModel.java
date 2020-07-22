package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeModel {
    @SerializedName("resultflag")
    @Expose
    private String resultflag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("HomeList")
    @Expose
    private List<HomeList> homeList = null;
    @SerializedName("HomeWork")
    @Expose
    private HomeWork homeWork;
    @SerializedName("Attendance")
    @Expose
    private Attendance attendance;

    public String getResultflag() {
        return resultflag;
    }

    public void setResultflag(String resultflag) {
        this.resultflag = resultflag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HomeList> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<HomeList> homeList) {
        this.homeList = homeList;
    }

    public HomeWork getHomeWork() {
        return homeWork;
    }

    public void setHomeWork(HomeWork homeWork) {
        this.homeWork = homeWork;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public class HomeList {

        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("ImageUrl")
        @Expose
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }

    public class HomeWork {

        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("ImageUrl")
        @Expose
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public class Attendance {

        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("ImageUrl")
        @Expose
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }
}
