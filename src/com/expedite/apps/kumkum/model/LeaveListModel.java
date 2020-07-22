
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LeaveListModel {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("strResult")
    @Expose
    private String message;
    @SerializedName("strArrayLable")
    @Expose
    private List<String> acedemicYear = null;
    @SerializedName("strArray")
    @Expose
    private List<String> totalleave = null;
    @SerializedName("listArray")
    @Expose
    private List<LeaveHistory> leaveHistory = null;
    @SerializedName("strlist")
    @Expose
    private List<LeaveTypeList> leaveTypeList = null;

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

    public List<String> getAcedemicYear() {
        return acedemicYear;
    }

    public List<String> getTotalleave() {
        return totalleave;
    }

    public void setAcedemicYear(List<String> acedemicYear) {
        this.acedemicYear = acedemicYear;
    }

    public void setTotalleave(List<String> totalleave) {
        this.totalleave = totalleave;
    }

    public List<LeaveTypeList> getLeaveTypeList() {
        return leaveTypeList;
    }

    public void setLeaveTypeList(List<LeaveTypeList> leaveTypeList) {
        this.leaveTypeList = leaveTypeList;
    }

    public List<LeaveHistory> getLeaveHistory() {
        return leaveHistory;
    }

    public void setLeaveHistory(List<LeaveHistory> leaveHistory) {
        this.leaveHistory = leaveHistory;
    }


    public class LeaveHistory implements Serializable {
        @SerializedName("first")
        @Expose
        private String leaveDate;
        @SerializedName("second")
        @Expose
        private String leaverason;
        @SerializedName("third")
        @Expose
        private String leaveCount;
        @SerializedName("fourth")
        @Expose
        private String Document;
        @SerializedName("fifth")
        @Expose
        private String leaveStatus;
        @SerializedName("sixth")
        @Expose
        private String cordinatorRemarks;
        @SerializedName("seventh")
        @Expose
        private String managerRemarks;
        @SerializedName("eighth")
        @Expose
        private String approvedBy;
        @SerializedName("nineth")
        @Expose
        private String approvedDate;
        @SerializedName("tenth")
        @Expose
        private String leaveType;
        @SerializedName("eleventh")
        @Expose
        private String ApplicationDate;
        //        @SerializedName("twelth")
//        @Expose
//        private String employeeName;
//        @SerializedName("thirteen")
//        @Expose
//        private String employeeId;
//        @SerializedName("fourteen")
//        @Expose
//        private String IsDisplayAcceptReject;
//        @SerializedName("fifteen")
//        @Expose
//        private String leaveid;
        @SerializedName("sixteen")
        @Expose
        private String documentType;
//        @SerializedName("seventeen")
//        @SerializedName("eighteen")
//        @SerializedName("nineteen")
//        @SerializedName("twenty")


        public String getLeaveDate() {
            return leaveDate;
        }

        public void setLeaveDate(String leaveDate) {
            this.leaveDate = leaveDate;
        }

        public String getLeaverason() {
            return leaverason;
        }

        public void setLeaverason(String leaverason) {
            this.leaverason = leaverason;
        }

        public String getLeaveCount() {
            return leaveCount;
        }

        public void setLeaveCount(String leaveCount) {
            this.leaveCount = leaveCount;
        }

        public String getDocument() {
            return Document;
        }

        public void setDocument(String document) {
            Document = document;
        }

        public String getLeaveStatus() {
            return leaveStatus;
        }

        public void setLeaveStatus(String leaveStatus) {
            this.leaveStatus = leaveStatus;
        }

        public String getCordinatorRemarks() {
            return cordinatorRemarks;
        }

        public void setCordinatorRemarks(String cordinatorRemarks) {
            this.cordinatorRemarks = cordinatorRemarks;
        }

        public String getManagerRemarks() {
            return managerRemarks;
        }

        public void setManagerRemarks(String managerRemarks) {
            this.managerRemarks = managerRemarks;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }


        public String getApprovedDate() {
            return approvedDate;
        }

        public void setApprovedDate(String approvedDate) {
            this.approvedDate = approvedDate;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(String leaveType) {
            this.leaveType = leaveType;
        }

        public String getApplicationDate() {
            return ApplicationDate;
        }

        public void setApplicationDate(String applicationDate) {
            ApplicationDate = applicationDate;
        }

//        public String getEmployeeName() {
//            return employeeName;
//        }
//
//        public void setEmployeeName(String employeeName) {
//            this.employeeName = employeeName;
//        }
//
//        public String getEmployeeId() {
//            return employeeId;
//        }
//
//        public void setEmployeeId(String employeeId) {
//            this.employeeId = employeeId;
//        }
//
//        public String getIsDisplayAcceptReject() {
//            return IsDisplayAcceptReject;
//        }
//
//        public void setIsDisplayAcceptReject(String isDisplayAcceptReject) {
//            IsDisplayAcceptReject = isDisplayAcceptReject;
//        }
//
//        public String getLeaveid() {
//            return leaveid;
//        }
//
//        public void setLeaveid(String leaveid) {
//            this.leaveid = leaveid;
//        }

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

    }

    public class LeaveTypeList {
        @SerializedName("first")
        @Expose
        private String title;
        @SerializedName("second")
        @Expose
        private String count;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

}