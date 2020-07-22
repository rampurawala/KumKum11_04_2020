
package com.expedite.apps.kumkum.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyDiaryCalendarModel {

    @SerializedName("response")
    @Expose
    public Object response;
    @SerializedName("strArray")
    @Expose
    public Object strArray;
    @SerializedName("strArrayLable")
    @Expose
    public Object strArrayLable;
    @SerializedName("strResult")
    @Expose
    public String strResult;
    @SerializedName("strlist")
    @Expose
    public List<Strlist> strlist = null;
    @SerializedName("listArray")
    @Expose
    public List<ListArray> listArray = null;

    public class ListArray {

        @SerializedName("first")
        @Expose
        public String first;
        @SerializedName("second")
        @Expose
        public String second;
        @SerializedName("third")
        @Expose
        public String third;
        @SerializedName("fourth")
        @Expose
        public Object fourth;
        @SerializedName("fifth")
        @Expose
        public Object fifth;
        @SerializedName("sixth")
        @Expose
        public Object sixth;
        @SerializedName("seventh")
        @Expose
        public Object seventh;
        @SerializedName("eighth")
        @Expose
        public Object eighth;
        @SerializedName("nineth")
        @Expose
        public Object nineth;
        @SerializedName("tenth")
        @Expose
        public Object tenth;
        @SerializedName("eleventh")
        @Expose
        public Object eleventh;
        @SerializedName("twelth")
        @Expose
        public Object twelth;

    }
    public class Strlist {

        @SerializedName("first")
        @Expose
        public String first;
        @SerializedName("second")
        @Expose
        public String second;
        @SerializedName("third")
        @Expose
        public String third;
        @SerializedName("fourth")
        @Expose
        public Object fourth;
        @SerializedName("fifth")
        @Expose
        public String fifth;
        @SerializedName("sixth")
        @Expose
        public String sixth;
        @SerializedName("seventh")
        @Expose
        public String seventh;
        @SerializedName("eighth")
        @Expose
        public String eighth;
        @SerializedName("nineth")
        @Expose
        public String nineth;
        @SerializedName("tenth")
        @Expose
        public Object tenth;
        @SerializedName("eleventh")
        @Expose
        public Object eleventh;
        @SerializedName("twelth")
        @Expose
        public Object twelth;

    }


}
