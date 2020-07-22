
package com.expedite.apps.kumkum.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppService {

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
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("value_1")
    @Expose
    private String value_1;
    @SerializedName("strlist")
    @Expose
    private List<ListArray> strlist;
    @SerializedName("listArray")
    @Expose
    private List<ListArray> listArray = null;

    public String getResponse() {
        return response;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue1() {
        return value_1;
    }

    public void setValue1(String value_1) {
        this.value_1 = value_1;
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

    public List<ListArray> getStrlist() {
        return strlist;
    }

    public void setStrlist(List<ListArray> strlist) {
        this.strlist = strlist;
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

        @SerializedName("seventh")
        @Expose
        private String seventh;

        @SerializedName("eighth")
        @Expose
        private String eighth;

        @SerializedName("nineth")
        @Expose
        private String nineth;

        @SerializedName("tenth")
        @Expose
        private String tenth;

        @SerializedName("eleventh")
        @Expose
        private String eleventh;

        @SerializedName("twelth")
        @Expose
        private String twelth;
        @SerializedName("thirteen")
        @Expose
        private String thirteen;

        @SerializedName("fourteen")
        @Expose
        private String fourteen;

        @SerializedName("fifteen")
        @Expose
        private String fifteen;

        @SerializedName("sixteen")
        @Expose
        private String sixteen;
        @SerializedName("seventeen")
        @Expose
        private String seventeen;
        @SerializedName("eighteen")
        @Expose
        private String eighteen;
        @SerializedName("nineteen")
        @Expose
        private String nineteen;
        @SerializedName("twenty")
        @Expose
        private String twenty;
        @SerializedName("twentyone")
        @Expose
        private String twentyone;
        @SerializedName("twentytwo")
        @Expose
        private String twentytwo;
        @SerializedName("twentythree")
        @Expose
        private String twentythree;
        @SerializedName("twentyfour")
        @Expose
        private String twentyfour;
        @SerializedName("twentyfive")
        @Expose
        private String twentyfive;
        @SerializedName("twentysix")
        @Expose
        private String twentysix;
        @SerializedName("twentyseven")
        @Expose
        private String twentyseven;

        private boolean isTestExpired = false;
        private boolean isTestStarted = false;
        private boolean isTestTimeOut = false;
        private boolean isAnsSubmited = false;
        private boolean isShowUploadBtn = false;

        public boolean isShowUploadBtn() {
            return isShowUploadBtn;
        }

        public void setShowUploadBtn(boolean showUploadBtn) {
            isShowUploadBtn = showUploadBtn;
        }

        public boolean isTestStarted() {
            return isTestStarted;
        }

        public void setTestStarted(boolean testStarted) {
            isTestStarted = testStarted;
        }

        public ListArray(String sixth) {
            this.sixth = sixth;
        }

        public boolean isAnsSubmited() {
            return isAnsSubmited;
        }

        public void setAnsSubmited(boolean ansSubmited) {
            isAnsSubmited = ansSubmited;
        }

        public boolean isTestExpired() {
            return isTestExpired;
        }

        public void setTestExpired(boolean testExpired) {
            isTestExpired = testExpired;
        }

        public boolean isTestTimeOut() {
            return isTestTimeOut;
        }

        public void setTestTimeOut(boolean testTimeOut) {
            isTestTimeOut = testTimeOut;
        }


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

        public String getSeventh() {
            return seventh;
        }

        public void setSeventh(String seventh) {
            this.seventh = seventh;
        }

        public String getEighth() {
            return eighth;
        }

        public void setEighth(String eighth) {
            this.eighth = eighth;
        }

        public String getNineth() {
            return nineth;
        }

        public void setNineth(String nineth) {
            this.nineth = nineth;
        }

        public String getTenth() {
            return tenth;
        }

        public void setTenth(String tenth) {
            this.tenth = tenth;
        }

        public String getEleventh() {
            return eleventh;
        }

        public void setEleventh(String eleventh) {
            this.eleventh = eleventh;
        }

        public String getTwelth() {
            return twelth;
        }

        public void setTwelth(String twelth) {
            this.twelth = twelth;
        }

        public String getThirteen() {
            return thirteen;
        }

        public void setThirteen(String thirteen) {
            this.thirteen = thirteen;
        }

        public String getFourteen() {
            return fourteen;
        }

        public void setFourteen(String fourteen) {
            this.fourteen = fourteen;
        }

        public String getFifteen() {
            return fifteen;
        }

        public void setFifteen(String fifteen) {
            this.fifteen = fifteen;
        }

        public String getSixteen() {
            return sixteen;
        }

        public void setSixteen(String sixteen) {
            this.sixteen = sixteen;
        }

        public String getSeventeen() {
            return seventeen;
        }

        public void setSeventeen(String seventeen) {
            this.seventeen = seventeen;
        }

        public String getEighteen() {
            return eighteen;
        }

        public void setEighteen(String eighteen) {
            this.eighteen = eighteen;
        }

        public String getNineteen() {
            return nineteen;
        }

        public void setNineteen(String nineteen) {
            this.nineteen = nineteen;
        }

        public String getTwenty() {
            return twenty;
        }

        public void setTwenty(String twenty) {
            this.twenty = twenty;
        }

        public String getTwentyone() {
            return twentyone;
        }

        public void setTwentyone(String twentyone) {
            this.twentyone = twentyone;
        }

        public String getTwentytwo() {
            return twentytwo;
        }

        public void setTwentytwo(String twentytwo) {
            this.twentytwo = twentytwo;
        }

        public String getTwentythree() {
            return twentythree;
        }

        public void setTwentythree(String twentythree) {
            this.twentythree = twentythree;
        }

        public String getTwentyfour() {
            return twentyfour;
        }

        public void setTwentyfour(String twentyfour) {
            this.twentyfour = twentyfour;
        }

        public String getTwentyfive() {
            return twentyfive;
        }

        public void setTwentyfive(String twentyfive) {
            this.twentyfive = twentyfive;
        }

        public String getTwentysix() {
            return twentysix;
        }

        public void setTwentysix(String twentysix) {
            this.twentysix = twentysix;
        }

        public String getTwentyseven() {
            return twentyseven;
        }

        public void setTwentyseven(String twentyseven) {
            this.twentyseven = twentyseven;
        }


        public String toString() {
            return sixth/* + ":" + testExpireDateAfterStart*/;
        }

        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((sixth == null) ? 0 : sixth.hashCode());
            //  result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AppService.ListArray other = (AppService.ListArray) obj;
       /* if (id != other.id)
            return false;*/
            if (sixth == null) {
                if (other.sixth != null)
                    return false;
            } else if (!sixth.equals(other.sixth))
                return false;
            return true;
        }

    }


}
