package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/20/2018.
 */

public class SocialAuthDetailsModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("display_name")
    @Expose
    private String display_name;
    @SerializedName("profile_image")
    @Expose
    private String profile_image;
    @SerializedName("isSubscribed")
    @Expose
    private String isSubscribed;
    @SerializedName("nick_name")
    @Expose
    private String nick_name;
    @SerializedName("studio_id")
    @Expose
    private String studio_id;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLogin_history_id() {
        return login_history_id;
    }

    public void setLogin_history_id(String login_history_id) {
        this.login_history_id = login_history_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("login_history_id")
    @Expose
    private String login_history_id;
    @SerializedName("id")
    @Expose
    private String id;

}

