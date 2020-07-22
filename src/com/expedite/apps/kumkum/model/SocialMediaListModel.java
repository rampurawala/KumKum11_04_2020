
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SocialMediaListModel {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("strResult")
    @Expose
    private String message;
    @SerializedName("strlist")
    @Expose
    private List<SocialMediaList> socialMediaList = null;

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

    public List<SocialMediaList> getSocialMediaList() {
        return socialMediaList;
    }

    public void setSocialMediaList(List<SocialMediaList> socialMediaList) {
        this.socialMediaList = socialMediaList;
    }


    public class SocialMediaList implements Serializable {
        @SerializedName("first")
        @Expose
        private String title;
        @SerializedName("second")
        @Expose
        private String link;
        @SerializedName("third")
        @Expose
        private String id;
        @SerializedName("fourth")
        @Expose
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }


    }

}