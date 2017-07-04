package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 7/4/2017.
 */

public class AboutUsInput {
    String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPermalink() {
        return Permalink;
    }

    public void setPermalink(String permalink) {
        Permalink = permalink;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    String Permalink;
    String lang_code;
}
