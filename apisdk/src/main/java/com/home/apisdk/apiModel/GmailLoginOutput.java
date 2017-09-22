package com.home.apisdk.apiModel;

/**
 * Created by BISHAL on 21-09-2017.
 */

public class GmailLoginOutput {
    String code, id,login_history_id,email,display_name,studio_id,profile_image,status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_history_id() {
        return login_history_id;
    }

    public void setLogin_history_id(String login_history_id) {
        this.login_history_id = login_history_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIs_newuser() {
        return is_newuser;
    }

    public void setIs_newuser(int is_newuser) {
        this.is_newuser = is_newuser;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(int isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    int is_newuser,isSubscribed;

}
