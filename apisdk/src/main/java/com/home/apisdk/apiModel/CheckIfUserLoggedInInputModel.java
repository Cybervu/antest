package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class CheckIfUserLoggedInInputModel {

    String authToken;
    String user_id;
    String device_id;
    String device_type;

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }



    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
