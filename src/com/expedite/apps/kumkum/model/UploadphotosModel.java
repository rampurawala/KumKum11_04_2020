
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadphotosModel {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("host")
    @Expose
    private String host;

    @SerializedName("userID")
    @Expose
    private String userID;

    @SerializedName("pwd")
    @Expose
    private String pwd;

    @SerializedName("folder")
    @Expose
    private String folder;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("bucket")
    @Expose
    private String bucket;

    @SerializedName("secretKey")
    @Expose
    private String secretKey;

    @SerializedName("classList")
    @Expose
    private List<ClassList> classList = null;



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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserID() {
        return userID;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public List<ClassList> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassList> classList) {
        this.classList = classList;
    }


    public class ClassList {

        @SerializedName("yearId")
        @Expose
        private String yearId;
        @SerializedName("className")
        @Expose
        private String className;
        @SerializedName("classId")
        @Expose
        private String classId;
        @SerializedName("studCount")
        @Expose
        private String studCount;

        @SerializedName("schoolName")
        @Expose
        private String schoolName;
        @SerializedName("schoolId")
        @Expose
        private String schoolId;
        private String isSelected = "false";
        private String StudentIds;
        private Boolean isAll;
        public String getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(String isSelected) {
            this.isSelected = isSelected;
        }

        public String getYearId() {
            return yearId;
        }

        public void setYearId(String yearId) {
            this.yearId = yearId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getStudCount() {
            return studCount;
        }

        public void setStudCount(String studCount) {
            this.studCount = studCount;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getStudentIds() {
            return StudentIds;
        }

        public void setStudentIds(String StudentIds) {
            this.StudentIds = StudentIds;
        }

        public boolean getIsAll() {
            return isAll;
        }

        public void setIsAll(boolean isAll) {
            this.isAll = isAll;
        }

    }


}
