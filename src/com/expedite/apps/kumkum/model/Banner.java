package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Banner {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("isShowAd")
    @Expose
    private String isShowAd;

    public String getAdFrequency() {
        return AdFrequency;
    }

    public void setAdFrequency(String adFrequency) {
        AdFrequency = adFrequency;
    }

    @SerializedName("AdFrequency")
    @Expose
    private String AdFrequency;

    public String getShowActivitys() {
        return showActivitys;
    }

    public void setShowActivitys(String showActivitys) {
        this.showActivitys = showActivitys;
    }

    @SerializedName("showActivitys")
    @Expose
    private String showActivitys;

    public String getIsSaveVisitDetails() {
        return isSaveVisitDetails;
    }

    public void setIsSaveVisitDetails(String isSaveVisitDetails) {
        this.isSaveVisitDetails = isSaveVisitDetails;
    }

    @SerializedName("isSaveVisitDetails")
    @Expose
    private String isSaveVisitDetails;
    @SerializedName("bookIssueList")
    @Expose
    private List<BookIssueList> bookIssueList = null;

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

    public String getIsShowAd() {
        return isShowAd;
    }

    public void setIsShowAd(String isShowAd) {
        this.isShowAd = isShowAd;
    }



    public List<BookIssueList> getBookIssueList() {
        return bookIssueList;
    }

    public void setBookIssueList(List<BookIssueList> bookIssueList) {
        this.bookIssueList = bookIssueList;
    }

    public class BookIssueList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("imgUrl")
        @Expose
        private String imgUrl;
        @SerializedName("RedirectUrl")
        @Expose
        private String redirectUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

    }

}
