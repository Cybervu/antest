package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For ContactUsAsynTask
 *
 * @author MUVI
 */

public class AppHomeFeatureInputModel {
    String authToken = "";
    String featureSectionLimit = "";
    String getFeatureSectionOffset = "";
    String lang_code = "";
    String userId = "";




    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFeatureSectionLimit() {
        return featureSectionLimit;
    }

    public void setFeatureSectionLimit(String featureSectionLimit) {
        this.featureSectionLimit = featureSectionLimit;
    }

    public String getGetFeatureSectionOffset() {
        return getFeatureSectionOffset;
    }

    public void setGetFeatureSectionOffset(String getFeatureSectionOffset) {
        this.getFeatureSectionOffset = getFeatureSectionOffset;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }





}
