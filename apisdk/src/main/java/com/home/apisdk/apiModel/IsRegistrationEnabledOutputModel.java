package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class IsRegistrationEnabledOutputModel {

    int isMylibrary = 0;
    int is_login =0;
    int signup_step =0;
    int has_favourite=0;
    int rating=0;
    int isRestrictDevice =0;

    public int getIsRestrictDevice() {
        return isRestrictDevice;
    }

    public void setIsRestrictDevice(int isRestrictDevice) {
        this.isRestrictDevice = isRestrictDevice;
    }

    public int getIs_streaming_restriction() {
        return is_streaming_restriction;
    }

    public void setIs_streaming_restriction(int is_streaming_restriction) {
        this.is_streaming_restriction = is_streaming_restriction;
    }

    public int getChromecast() {
        return chromecast;
    }

    public void setChromecast(int chromecast) {
        this.chromecast = chromecast;
    }

    public int getIs_offline() {
        return is_offline;
    }

    public void setIs_offline(int is_offline) {
        this.is_offline = is_offline;
    }

    int is_streaming_restriction=0;
    int chromecast=0;
    int is_offline=0;






    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }





    public int getIsMylibrary() {
        return isMylibrary;
    }

    public void setIsMylibrary(int isMylibrary) {
        this.isMylibrary = isMylibrary;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public int getSignup_step() {
        return signup_step;
    }

    public void setSignup_step(int signup_step) {
        this.signup_step = signup_step;
    }

    public int getHas_favourite() {
        return has_favourite;
    }

    public void setHas_favourite(int has_favourite) {
        this.has_favourite = has_favourite;
    }







}
