package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 7/4/2017.
 */

public class CheckFbUserDetailsInput {

    String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFb_userid() {
        return fb_userid;
    }

    public void setFb_userid(String fb_userid) {
        this.fb_userid = fb_userid;
    }

    String fb_userid;
}
