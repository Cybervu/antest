package com.home.apisdk.apiModel;

/**
 * Created by BISHAL on 21-09-2017.
 */

public class GmailLoginInput {
    String name;
    String email;
    String password;
    String authToken;
    String profile_image;

    public String getGmail_userid() {
        return gmail_userid;
    }

    public void setGmail_userid(String gmail_userid) {
        this.gmail_userid = gmail_userid;
    }

    String gmail_userid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

}
