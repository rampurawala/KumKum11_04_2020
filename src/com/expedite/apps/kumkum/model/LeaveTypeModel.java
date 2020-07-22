package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 8/1/2018.
 */

public class LeaveTypeModel {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("strResult")
    @Expose
    private String message;
    @SerializedName("strlist")
    @Expose
    private List<Leavedetails> leavedetails = null;


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

    public List<Leavedetails> getLeavedetails() {
        return leavedetails;
    }

    public void setLeavedetails(List<Leavedetails> leavedetails) {
        this.leavedetails = leavedetails;
    }


    public class Leavedetails {
        @SerializedName("first")
        @Expose
        private String name;
        @SerializedName("second")
        @Expose
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
