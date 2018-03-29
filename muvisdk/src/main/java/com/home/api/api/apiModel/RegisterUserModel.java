package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Register User
 *
 * @author Abhishek
 */

public class RegisterUserModel {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("display_name")
    @Expose
    private String displayName = "";
    @SerializedName("nick_name")
    @Expose
    private String nickName = "";
    @SerializedName("studio_id")
    @Expose
    private String studioId = "";
    @SerializedName("isFree")
    @Expose
    private String isFree = "";
    @SerializedName("profile_image")
    @Expose
    private String profileImage = "";
    @SerializedName("isSubscribed")
    @Expose
    private String isSubscribed = "";
    @SerializedName("is_broadcaster")
    @Expose
    private String isBroadcaster = "";
    @SerializedName("language_list")
    @Expose
    private LanguageList languageList;
    @SerializedName("custom_last_name")
    @Expose
    private String customLastName = "";
    @SerializedName("login_history_id")
    @Expose
    private String loginHistoryId = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";

    /**
     * This method is used to get the id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * This method is used to set the id
     *
     * @param id For setting the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method is used to get the email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method is used to set the email
     *
     * @param email For setting the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method is used to get the display name
     *
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * This method is used to set the display name
     *
     * @param displayName For setting the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * This method is used to get the nick name
     *
     * @return nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * This method is used to set the nick name
     *
     * @param nickName For setting the nick name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * This method is used to get the studio id
     *
     * @return studioId
     */
    public String getStudioId() {
        return studioId;
    }

    /**
     * This method is used to set the studio id
     *
     * @param studioId For setting the studio id
     */
    public void setStudioId(String studioId) {
        this.studioId = studioId;
    }

    /**
     * This method is used to get the free details
     *
     * @return isFree
     */
    public String getIsFree() {
        return isFree;
    }

    /**
     * This method is used to set the free details
     *
     * @param isFree For setting the free details
     */
    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    /**
     * This method is used to get the profile image
     *
     * @return profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * This method is used to set the profile image
     *
     * @param profileImage For setting the profile image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * This method is used to get the subscription details
     *
     * @return isSubscribed
     */
    public String getIsSubscribed() {
        return isSubscribed;
    }

    /**
     * This method is used to set the subscription details
     *
     * @param isSubscribed For setting the subscription details
     */
    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    /**
     * This method is used to get the broadcast details
     *
     * @return isBroadcaster
     */
    public String getIsBroadcaster() {
        return isBroadcaster;
    }

    /**
     * This method is used to set the broadcast details
     *
     * @param isBroadcaster For setting the broadcast details
     */
    public void setIsBroadcaster(String isBroadcaster) {
        this.isBroadcaster = isBroadcaster;
    }

    /**
     * This method is used to get the language list
     *
     * @return languageList
     */
    public LanguageList getLanguageList() {
        return languageList;
    }

    /**
     * This method is used to set the language list
     *
     * @param languageList For setting the language list
     */
    public void setLanguageList(LanguageList languageList) {
        this.languageList = languageList;
    }

    /**
     * This method is used to get the custom last name
     *
     * @return customLastName
     */
    public String getCustomLastName() {
        return customLastName;
    }

    /**
     * This method is used to set the custom last name
     *
     * @param customLastName For setting the custom last name
     */
    public void setCustomLastName(String customLastName) {
        this.customLastName = customLastName;
    }

    /**
     * This method is used to get the login history id
     *
     * @return loginHistoryId
     */
    public String getLoginHistoryId() {
        return loginHistoryId;
    }

    /**
     * This method is used to set the login history id
     *
     * @param loginHistoryId For setting the login history id
     */
    public void setLoginHistoryId(String loginHistoryId) {
        this.loginHistoryId = loginHistoryId;
    }

    /**
     * This method is used to get the server code
     *
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * This method is used to set the server code
     *
     * @param code For setting the server code
     */

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * This method is used to get the status
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method is used to set the status
     *
     * @param status For setting the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method is used to get the message
     *
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * This method is used to set the message
     *
     * @param msg For setting the message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class LanguageList {

        @SerializedName("nl")
        @Expose
        private String nl = "";
        @SerializedName("en")
        @Expose
        private String en = "";

        public String getNl() {
            return nl;
        }

        public void setNl(String nl) {
            this.nl = nl;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

    }
}
