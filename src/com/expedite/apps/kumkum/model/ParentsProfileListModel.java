
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ParentsProfileListModel {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("strResult")
    @Expose
    private String message;
    @SerializedName("strlist")
    @Expose
    private List<ParentsProfileList> profileList = null;

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

    public List<ParentsProfileList> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<ParentsProfileList> profileList) {
        this.profileList = profileList;
    }


    public class ParentsProfileList implements Serializable {
        @SerializedName("first")
        @Expose
        private String title;
        @SerializedName("second")
        @Expose
        private String detail;
        @SerializedName("third")
        @Expose
        private String isImageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getIsImageUrl() {
            return isImageUrl;
        }

        public void setIsImageUrl(String isImageUrl) {
            this.isImageUrl = isImageUrl;
        }

    }

}