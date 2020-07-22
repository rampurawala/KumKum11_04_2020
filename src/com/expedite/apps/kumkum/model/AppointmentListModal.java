package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentListModal {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("getappoinment")
    @Expose
    private List<Getappoinment> getappoinment = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Getappoinment> getGetappoinment() {
        return getappoinment;
    }

    public void setGetappoinment(List<Getappoinment> getappoinment) {
        this.getappoinment = getappoinment;
    }

    public class Getappoinment {

        @SerializedName("appointmentid")
        @Expose
        private String appointmentid;
        @SerializedName("Classec")
        @Expose
        private String classec;
        @SerializedName("flg")
        @Expose
        private String flg;
        @SerializedName("visitorname")
        @Expose
        private String visitorname;
        @SerializedName("imgpath")
        @Expose
        private String imgpath;
        @SerializedName("visitoremail")
        @Expose
        private String visitoremail;
        @SerializedName("visitormobile")
        @Expose
        private String visitormobile;
        @SerializedName("purposeofvisit")
        @Expose
        private String purposeofvisit;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("isapproved")
        @Expose
        private Object isapproved;
        @SerializedName("hostname")
        @Expose
        private String hostname;
        @SerializedName("Appointment_taken_Date")
        @Expose
        private String Appointment_taken_Date;
        @SerializedName("Appointment_Approved_By_Name")
        @Expose
        private String Appointment_Approved_By_Name;
        @SerializedName("Appointment_Approved_By_Date")
        @Expose
        private String Appointment_Approved_By_Date;

        @SerializedName("Remarks")
        @Expose
        private String Remarks;


        public String getAppointmentid() {
            return appointmentid;
        }

        public void setAppointmentid(String appointmentid) {
            this.appointmentid = appointmentid;
        }

        public String getClassec() {
            return classec;
        }

        public void setClassec(String classec) {
            this.classec = classec;
        }

        public String getFlg() {
            return flg;
        }

        public void setFlg(String flg) {
            this.flg = flg;
        }

        public String getVisitorname() {
            return visitorname;
        }

        public void setVisitorname(String visitorname) {
            this.visitorname = visitorname;
        }

        public String getImgpath() {
            return imgpath;
        }

        public void setImgpath(String imgpath) {
            this.imgpath = imgpath;
        }

        public String getVisitoremail() {
            return visitoremail;
        }

        public void setVisitoremail(String visitoremail) {
            this.visitoremail = visitoremail;
        }

        public String getVisitormobile() {
            return visitormobile;
        }

        public void setVisitormobile(String visitormobile) {
            this.visitormobile = visitormobile;
        }

        public String getPurposeofvisit() {
            return purposeofvisit;
        }

        public void setPurposeofvisit(String purposeofvisit) {
            this.purposeofvisit = purposeofvisit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Object getIsapproved() {
            return isapproved;
        }

        public void setIsapproved(Object isapproved) {
            this.isapproved = isapproved;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public String getAppointmentTakenDate() {
            return Appointment_taken_Date;
        }

        public void setAppointmentTakenDate(String Appointment_taken_Date) {
            this.Appointment_taken_Date = Appointment_taken_Date;
        }

        public String getAppointmentApprovedByDate() {
            return Appointment_Approved_By_Date;
        }

        public void setAppointmentApprovedByDate(String Appointment_Approved_By_Date) {
            this.Appointment_Approved_By_Date = Appointment_Approved_By_Date;
        }

        public String getAppointmentApprovedByName() {
            return Appointment_Approved_By_Name;
        }

        public void setAppointmentApprovedByName(String Appointment_Approved_By_Name) {
            this.Appointment_Approved_By_Name = Appointment_Approved_By_Name;
        }

        public String getAppointmentRemarks() {
            return Remarks;
        }

        public void setAppointmentRemarks(String Remarks) {
            this.Remarks = Remarks;
        }


    }


}
