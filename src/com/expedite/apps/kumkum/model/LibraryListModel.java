
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibraryListModel {

    @SerializedName("bookarrays")
    @Expose
    private List<Bookarray> bookarrays = null;

    public List<Bookarray> getBookarrays() {
        return bookarrays;
    }

    public void setBookarrays(List<Bookarray> bookarrays) {
        this.bookarrays = bookarrays;
    }

    public class Bookarray {

        @SerializedName("response")
        @Expose
        private String response;
        @SerializedName("message")
        @Expose
        private String message;
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

        public List<BookIssueList> getBookIssueList() {
            return bookIssueList;
        }

        public void setBookIssueList(List<BookIssueList> bookIssueList) {
            this.bookIssueList = bookIssueList;
        }

    }

    public class BookIssueList {

        @SerializedName("issueId")
        @Expose
        private String issueId;
        @SerializedName("bookName")
        @Expose
        private String bookName;
        @SerializedName("issueDate")
        @Expose
        private String issueDate;
        @SerializedName("returnDate")
        @Expose
        private String returnDate;
        @SerializedName("statusId")
        @Expose
        private String statusId;
        @SerializedName("statusName")
        @Expose
        private String statusName;

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(String returnDate) {
            this.returnDate = returnDate;
        }

        public String getStatusId() {
            return statusId;
        }

        public void setStatusId(String statusId) {
            this.statusId = statusId;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

    }

}
