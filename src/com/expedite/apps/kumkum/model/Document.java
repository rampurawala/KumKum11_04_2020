package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Document {
    @SerializedName("ResultFlag")
    @Expose
    private String resultFlag;

    @SerializedName("Message")
    @Expose
    private Object message;

    @SerializedName("ShareTitle")
    @Expose
    private String shareTitle;

    @SerializedName("ShareMessage")
    @Expose
    private Object shareMessage;
    @SerializedName("DocumentItemList")
    @Expose
    private List<DocumentItemList> documentItemLists = null;

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getMessage() {
        return (String) message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public Object getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(Object shareMessage) {
        this.shareMessage = shareMessage;
    }

    public List<DocumentItemList> getDocumentItemLists() {
        return documentItemLists;
    }

    public void setDocumentItemLists(List<DocumentItemList> documentItemLists) {
        this.documentItemLists = documentItemLists;
    }


    public static class DocumentItemList {

        @SerializedName("DocumentId")
        @Expose
        private String documentId;

        @SerializedName("DocumentName")
        @Expose
        private String documentName;

        @SerializedName("DocumentType")
        @Expose
        private String documentType;

        @SerializedName("DocumentDate")
        @Expose
        private String documentDate;

        @SerializedName("ShareUrl")
        @Expose
        private String shareUrl;

        @SerializedName("DisplayUrl")
        @Expose
        private String displayUrl;

        @SerializedName("Year")
        @Expose
        private String Year;
        private String yearDisplayFlag;

        public DocumentItemList() {

        }

        public DocumentItemList(String json, Class<Document> documentClass) {
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getDocumentDate() {
            return documentDate;
        }

        public void setDocumentDate(String documentDate) {
            this.documentDate = documentDate;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getDisplayUrl() {
            return displayUrl;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }

        public String getYearFlag() {
            return yearDisplayFlag;
        }

        public void setYearFlag(String yearDisplayFlag) {
            this.yearDisplayFlag = yearDisplayFlag;
        }

        public void setDisplayUrl(String displayUrl) {
            this.displayUrl = displayUrl;
        }

        public String toString() {
            return String.format("DocumentId:%s,DocumentName:%d,DocumentType:%s,DocumentDate:%s,ShareUrl:%s,DisplayUrl:%s,", documentId, documentName, documentType, documentDate, shareUrl, displayUrl);
        }
    }
}
