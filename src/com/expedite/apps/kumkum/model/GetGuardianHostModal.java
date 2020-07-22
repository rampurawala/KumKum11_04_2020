package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGuardianHostModal {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("appointmenttypelist")
    @Expose
    private List<Appointmenttypelist> appointmenttypelist = null;
    @SerializedName("gethostlist")
    @Expose
    private List<Gethostlist> gethostlist = null;

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

    public List<Appointmenttypelist> getAppointmenttypelist() {
        return appointmenttypelist;
    }

    public void setAppointmenttypelist(List<Appointmenttypelist> appointmenttypelist) {
        this.appointmenttypelist = appointmenttypelist;
    }

    public List<Gethostlist> getGethostlist() {
        return gethostlist;
    }

    public void setGethostlist(List<Gethostlist> gethostlist) {
        this.gethostlist = gethostlist;
    }
    public class Gethostlist {

        @SerializedName("type_id")
        @Expose
        private String typeId;
        @SerializedName("type_name")
        @Expose
        private String typeName;
        @SerializedName("schoolid")
        @Expose
        private Object schoolid;
        @SerializedName("schoolname")
        @Expose
        private Object schoolname;
        @SerializedName("Classsecids")
        @Expose
        private Object classsecids;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("email")
        @Expose
        private String email;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public Object getSchoolid() {
            return schoolid;
        }

        public void setSchoolid(Object schoolid) {
            this.schoolid = schoolid;
        }

        public Object getSchoolname() {
            return schoolname;
        }

        public void setSchoolname(Object schoolname) {
            this.schoolname = schoolname;
        }

        public Object getClasssecids() {
            return classsecids;
        }

        public void setClasssecids(Object classsecids) {
            this.classsecids = classsecids;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
    public class Appointmenttypelist {

        @SerializedName("typeid")
        @Expose
        private String typeid;
        @SerializedName("typename")
        @Expose
        private String typename;

        public String getTypeid() {
            return typeid;
        }

        public void setTypeid(String typeid) {
            this.typeid = typeid;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

    }
}
