package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class UpdateGoogleIdInputModel {

    String authToken;
    String google_id ;
    String device_id;
    String user_id;


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }


    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
