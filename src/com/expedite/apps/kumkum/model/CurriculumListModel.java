
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurriculumListModel {

    @SerializedName("array")
    @Expose
    private List<Array> array = null;

    public List<Array> getArray() {
        return array;
    }

    public void setArray(List<Array> array) {
        this.array = array;
    }

    public class Array {

        @SerializedName("response")
        @Expose
        private String response;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("parentId")
        @Expose
        private String parentId;
        @SerializedName("folderList")
        @Expose
        private List<FolderList> folderList = null;
        @SerializedName("fileList")
        @Expose
        private List<FileList> fileList = null;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public List<FolderList> getFolderList() {
            return folderList;
        }

        public void setFolderList(List<FolderList> folderList) {
            this.folderList = folderList;
        }

        public List<FileList> getFileList() {
            return fileList;
        }

        public void setFileList(List<FileList> fileList) {
            this.fileList = fileList;
        }

    }

    public class FileList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("Instructions")
        @Expose
        private String Instructions;
        @SerializedName("DueDate")
        @Expose
        private String DueDate;
        @SerializedName("TimeLeft")
        @Expose
        private String TimeLeft;
        @SerializedName("FileStatus")
        @Expose
        private String FileStatus;

        public String getTimeLeft() {
            return TimeLeft;
        }

        public void setTimeLeft(String timeLeft) {
            TimeLeft = timeLeft;
        }

        public String getFileStatus() {
            return FileStatus;
        }

        public void setFileStatus(String fileStatus) {
            FileStatus = fileStatus;
        }

        public String getFolderName() {
            return FolderName;
        }

        public void setFolderName(String folderName) {
            FolderName = folderName;
        }

        @SerializedName("FolderName")
        @Expose
        private String FolderName;
// "id": "64",
//          "name": "pdf",
//          "type": "1",
//          "url": "https://s3.ap-south-1.amazonaws.com/espschools/CLASSROOM/pdf/1588728708.pdf",
//          "DueDate": "06/05/2020 07:00 AM",
//          "Instructions": "\u001e!�",
//          "TimeLeft": "",
//          "FileStatus": 2,
//          "FolderName": "Sem-1/"
        public String getIsChecked() {
            return isChecked;
        }

        public void setIsChecked(String isChecked) {
            this.isChecked = isChecked;
        }

        @SerializedName("isChecked")
        @Expose
        private String isChecked;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInstructions() {
            return Instructions;
        }

        public void setInstructions(String instructions) {
            Instructions = instructions;
        }

        public String getDueDate() {
            return DueDate;
        }

        public void setDueDate(String dueDate) {
            DueDate = dueDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public class FolderList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

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

    }

}
