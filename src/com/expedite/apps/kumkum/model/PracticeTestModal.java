
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PracticeTestModal {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("practiceTestList")
    @Expose
    private List<PracticeTestModal.PracticeTest> practiceTestList = null;

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
    public List<PracticeTestModal.PracticeTest> getPracticeTestList() {
        return practiceTestList;
    }

    public void setPracticeTestList(List<PracticeTestModal.PracticeTest> practiceTestList) {
        this.practiceTestList = practiceTestList;
    }

    public class PracticeTest {

        @SerializedName("isPending")
        @Expose
        private String isPending;
        @SerializedName("result")
        @Expose
        private String result;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("testId")
        @Expose
        private String testId;

        public String getIsPending() {
            return isPending;
        }

        public void setIsPending(String isPending) {
            this.isPending = isPending;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTestId() {
            return testId;
        }

        public void setTestId(String testId) {
            this.testId = testId;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

    }
}
