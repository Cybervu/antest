package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class FeatureContentInputModel {
    String authToken;
    String section_id;

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    String lang_code;

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



}
