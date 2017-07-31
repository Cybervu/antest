package com.home.apisdk.apiModel.apiModel;

/**
 * Created by Muvi on 9/28/2016.
 */
public class ContentDetailsInput {
String permalink,authtoken;

    public ContentDetailsInput() {

    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getAuthToken() {
        return authtoken;
    }

    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }
}
