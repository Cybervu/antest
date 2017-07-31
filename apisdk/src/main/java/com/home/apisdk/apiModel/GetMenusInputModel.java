package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class GetMenusInputModel {

    String authToken,lang_code;


    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
