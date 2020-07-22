
package com.expedite.apps.kumkum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollTaskListModel {

    @SerializedName("pollarrays")
    @Expose
    private List<Pollarray> pollarrays = null;

    public List<Pollarray> getPollarrays() {
        return pollarrays;
    }

    public void setPollarrays(List<Pollarray> pollarrays) {
        this.pollarrays = pollarrays;
    }

    public class Pollarray {

        @SerializedName("response")
        @Expose
        private String response;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("dayId")
        @Expose
        private String dayId;
        @SerializedName("surveyList")
        @Expose
        private List<SurveyList> surveyList = null;

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

        public String getDayId() {
            return dayId;
        }

        public void setDayId(String dayId) {
            this.dayId = dayId;
        }

        public List<SurveyList> getSurveyList() {
            return surveyList;
        }

        public void setSurveyList(List<SurveyList> surveyList) {
            this.surveyList = surveyList;
        }

    }

    public class SurveyList {

        @SerializedName("surveyId")
        @Expose
        private String surveyId;
        @SerializedName("surveyQue")
        @Expose
        private String surveyQue;
        @SerializedName("allowToSkip")
        @Expose
        private String allowToSkip;
        @SerializedName("allowToAvoid")
        @Expose
        private String allowToAvoid;
        @SerializedName("allowToAvoidText")
        @Expose
        private String allowToAvoidText;
        @SerializedName("Description")
        @Expose
        private String Description;
        @SerializedName("Url")
        @Expose
        private String Url;
        @SerializedName("surveyOptions")
        @Expose
        private List<SurveyOption> surveyOptions = null;

        public String getSurveyId() {
            return surveyId;
        }

        public void setSurveyId(String surveyId) {
            this.surveyId = surveyId;
        }

        public String getSurveyQue() {
            return surveyQue;
        }

        public void setSurveyQue(String surveyQue) {
            this.surveyQue = surveyQue;
        }

        public String getAllowToSkip() {
            return allowToSkip;
        }

        public void setAllowToSkip(String allowToSkip) {
            this.allowToSkip = allowToSkip;
        }

        public String getAllowToAvoid() {
            return allowToAvoid;
        }

        public void setAllowToAvoid(String allowToAvoid) {
            this.allowToAvoid = allowToAvoid;
        }

        public String getAllowToAvoidText() {
            return allowToAvoidText;
        }

        public void setAllowToAvoidText(String allowToAvoidText) {
            this.allowToAvoidText = allowToAvoidText;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

        public List<SurveyOption> getSurveyOptions() {
            return surveyOptions;
        }

        public void setSurveyOptions(List<SurveyOption> surveyOptions) {
            this.surveyOptions = surveyOptions;
        }

    }

    public class SurveyOption {

        @SerializedName("optionId")
        @Expose
        private String optionId;
        @SerializedName("optionAns")
        @Expose
        private String optionAns;
        @SerializedName("Url")
        @Expose
        private String url;

        public String getOptionId() {
            return optionId;
        }

        public void setOptionId(String optionId) {
            this.optionId = optionId;
        }

        public String getOptionAns() {
            return optionAns;
        }

        public void setOptionAns(String optionAns) {
            this.optionAns = optionAns;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

}
