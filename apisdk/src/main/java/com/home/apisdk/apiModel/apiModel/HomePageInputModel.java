package com.home.apisdk.apiModel.apiModel;

/**
 * Created by Muvi on 08-May-17.
 */

public class HomePageInputModel {

    private String authToken;

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    String lang_code;
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



}
