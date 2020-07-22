package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 7/21/2018.
 */

public class ExamListModel {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("strResult")
    @Expose
    private String strResult;
    @SerializedName("listArray")
    @Expose
    private List<ListArray> listArray = null;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStrResult() {
        return strResult;
    }

    public void setStrResult(String strResult) {
        this.strResult = strResult;
    }

    public List<ListArray> getListArray() {
        return listArray;
    }

    public void setListArray(List<ListArray> listArray) {
        this.listArray = listArray;
    }

    public class ListArray {

        @SerializedName("first")
        @Expose
        private String first;
        @SerializedName("second")
        @Expose
        private String second;
        @SerializedName("third")
        @Expose
        private String third;
        @SerializedName("fourth")
        @Expose
        private String fourth;
        @SerializedName("fifth")
        @Expose
        private String fifth;

        @SerializedName("sixth")
        @Expose
        private String sixth;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public String getThird() {
            return third;
        }

        public void setThird(String third) {
            this.third = third;
        }

        public String getFourth() {
            return fourth;
        }

        public void setFourth(String fourth) {
            this.fourth = fourth;
        }

        public String getFifth() {
            return fifth;
        }

        public void setFifth(String fifth) {
            this.fifth = fifth;
        }

        public String getSixth() {
            return sixth;
        }

        public void setSixth(String sixth) {
            this.sixth = sixth;
        }
    }
}
