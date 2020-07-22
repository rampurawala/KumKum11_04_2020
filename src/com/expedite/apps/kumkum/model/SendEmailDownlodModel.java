package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 21-12-2018.
 */

public class SendEmailDownlodModel {

        @SerializedName("response")
        @Expose
        private String response;
        @SerializedName("strArray")
        @Expose
        private Object strArray;
        @SerializedName("strArrayLable")
        @Expose
        private Object strArrayLable;
        @SerializedName("strResult")
        @Expose
        private String strResult;
        @SerializedName("strlist")
        @Expose
        private Object strlist;
        @SerializedName("listArray")
        @Expose
        private Object listArray;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public Object getStrArray() {
            return strArray;
        }

        public void setStrArray(Object strArray) {
            this.strArray = strArray;
        }

        public Object getStrArrayLable() {
            return strArrayLable;
        }

        public void setStrArrayLable(Object strArrayLable) {
            this.strArrayLable = strArrayLable;
        }

        public String getStrResult() {
            return strResult;
        }

        public void setStrResult(String strResult) {
            this.strResult = strResult;
        }

        public Object getStrlist() {
            return strlist;
        }

        public void setStrlist(Object strlist) {
            this.strlist = strlist;
        }

        public Object getListArray() {
            return listArray;
        }

        public void setListArray(Object listArray) {
            this.listArray = listArray;
        }


}
