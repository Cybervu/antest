package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_UserProfile_Input {

    String authToken,user_id,email;

    public void setAuthToken(String authToken){this.authToken = authToken;}
    public String getAuthToken(){return authToken;}

    public void setUser_id(String user_id){this.user_id = user_id;}
    public String getUser_id(){return user_id;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

}
