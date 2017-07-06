package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 7/4/2017.
 */

public class SimultaneousLogoutInput {

    String authToken;
    String device_type;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    String email_id;
}
