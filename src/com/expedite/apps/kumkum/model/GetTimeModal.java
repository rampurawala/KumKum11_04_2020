package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTimeModal {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("getgrouplist")
    @Expose
    private List<Getgrouplist> getgrouplist = null;

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

    public List<Getgrouplist> getGetgrouplist() {
        return getgrouplist;
    }

    public void setGetgrouplist(List<Getgrouplist> getgrouplist) {
        this.getgrouplist = getgrouplist;
    }

    public class Getgrouplist {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("value")
        @Expose
        private String value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
