package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class LanguageListInputModel {

    String authToken;

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    String langCode;

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
