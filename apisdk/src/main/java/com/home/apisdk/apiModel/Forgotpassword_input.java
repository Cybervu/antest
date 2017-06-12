package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 10/4/2016.
 */
public class Forgotpassword_input {
    String authToken,email,pakagename;

    public String getPakagename() {
        return pakagename;
    }

    public void setPakagename(String pakagename) {
        this.pakagename = pakagename;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
