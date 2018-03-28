package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Registration Enable
 *
 * @author Abhishek
 */

public class IsRegistrationEnableModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("isMylibrary")
    @Expose
    private Integer isMylibrary = 0;
    @SerializedName("isRating")
    @Expose
    private Integer isRating = 0;
    @SerializedName("is_login")
    @Expose
    private Integer isLogin = 0;
    @SerializedName("has_favourite")
    @Expose
    private String hasFavourite = "";
    @SerializedName("signup_step")
    @Expose
    private Integer signupStep = 0;
    @SerializedName("isRestrictDevice")
    @Expose
    private String isRestrictDevice = "";
    @SerializedName("is_streaming_restriction")
    @Expose
    private Integer isStreamingRestriction = 0;
    @SerializedName("facebook")
    @Expose
    private Facebook facebook;
    @SerializedName("google")
    @Expose
    private Google google;
    @SerializedName("chromecast")
    @Expose
    private Integer chromecast = 0;
    @SerializedName("is_offline")
    @Expose
    private Integer isOffline = 0;
    @SerializedName("isPlayList")
    @Expose
    private String isPlayList = "";
    @SerializedName("isQueue")
    @Expose
    private String isQueue = "";

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
     * This method is used to get the my library details
     *
     * @return isMylibrary
     */
    public Integer getIsMylibrary() {
        return isMylibrary;
    }

    /**
     * This method is used to set the my library details
     *
     * @param isMylibrary For setting the my library details
     */
    public void setIsMylibrary(Integer isMylibrary) {
        this.isMylibrary = isMylibrary;
    }

    /**
     * This method is used to get the my rating details
     *
     * @return isRating
     */
    public Integer getIsRating() {
        return isRating;
    }

    /**
     * This method is used to set the my rating details
     *
     * @param isRating For setting the rating details
     */
    public void setIsRating(Integer isRating) {
        this.isRating = isRating;
    }

    /**
     * This method is used to get the my login details
     *
     * @return isLogin
     */
    public Integer getIsLogin() {
        return isLogin;
    }

    /**
     * This method is used to set the my login details
     *
     * @param isLogin For setting the login details
     */
    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * This method is used to get the favorite details
     *
     * @return hasFavourite
     */
    public String getHasFavourite() {
        return hasFavourite;
    }

    /**
     * This method is used to set the favorite details
     *
     * @param hasFavourite For setting the favorite details
     */
    public void setHasFavourite(String hasFavourite) {
        this.hasFavourite = hasFavourite;
    }

    /**
     * This method is used to get the sign up step
     *
     * @return signupStep
     */
    public Integer getSignupStep() {
        return signupStep;
    }

    /**
     * This method is used to set the sign up step
     *
     * @param signupStep For setting the sign up step
     */
    public void setSignupStep(Integer signupStep) {
        this.signupStep = signupStep;
    }

    /**
     * This method is used to get the restrict device details
     *
     * @return isRestrictDevice
     */
    public String getIsRestrictDevice() {
        return isRestrictDevice;
    }

    /**
     * This method is used to set the restrict device details
     *
     * @param isRestrictDevice For setting the restrict device details
     */
    public void setIsRestrictDevice(String isRestrictDevice) {
        this.isRestrictDevice = isRestrictDevice;
    }

    /**
     * This method is used to get the streaming restriction details
     *
     * @return isStreamingRestriction
     */
    public Integer getIsStreamingRestriction() {
        return isStreamingRestriction;
    }

    /**
     * This method is used to set the streaming restriction details
     *
     * @param isStreamingRestriction For setting the streaming restriction details
     */
    public void setIsStreamingRestriction(Integer isStreamingRestriction) {
        this.isStreamingRestriction = isStreamingRestriction;
    }

    /**
     * This method is used to get the facebook details
     *
     * @return facebook
     */
    public Facebook getFacebook() {
        return facebook;
    }

    /**
     * This method is used to set the facebook details
     *
     * @param facebook For setting the facebook details
     */
    public void setFacebook(Facebook facebook) {
        this.facebook = facebook;
    }

    /**
     * This method is used to get the google details
     *
     * @return google
     */
    public Google getGoogle() {
        return google;
    }

    /**
     * This method is used to set the google details
     *
     * @param google For setting the google details
     */
    public void setGoogle(Google google) {
        this.google = google;
    }

    /**
     * This method is used to get the chromecast details
     *
     * @return chromecast
     */
    public Integer getChromecast() {
        return chromecast;
    }

    /**
     * This method is used to set the chromecast details
     *
     * @param chromecast For setting the chromecast details
     */
    public void setChromecast(Integer chromecast) {
        this.chromecast = chromecast;
    }

    /**
     * This method is used to get the offline details
     *
     * @return isOffline
     */
    public Integer getIsOffline() {
        return isOffline;
    }

    /**
     * This method is used to set the offline details
     *
     * @param isOffline For setting the offline details
     */
    public void setIsOffline(Integer isOffline) {
        this.isOffline = isOffline;
    }

    /**
     * This method is used to get the playlist details
     *
     * @return isPlayList
     */
    public String getIsPlayList() {
        return isPlayList;
    }

    /**
     * This method is used to set the playlist details
     *
     * @param isPlayList For setting the playlist details
     */
    public void setIsPlayList(String isPlayList) {
        this.isPlayList = isPlayList;
    }

    /**
     * This method is used to get the queue details
     *
     * @return isQueue
     */
    public String getIsQueue() {
        return isQueue;
    }

    /**
     * This method is used to set the queue details
     *
     * @param isQueue For setting the queue details
     */
    public void setIsQueue(String isQueue) {
        this.isQueue = isQueue;
    }

    public class Facebook {

        @SerializedName("status")
        @Expose
        private Integer status = 0;

        /**
         * This method is used to get the status
         *
         * @return status
         */
        public Integer getStatus() {
            return status;
        }

        /**
         * This method is used to set the status
         *
         * @param status For setting the status
         */

        public void setStatus(Integer status) {
            this.status = status;
        }

    }

    public class Google {

        @SerializedName("status")
        @Expose
        private Integer status = 0;

        /**
         * This method is used to get the status
         *
         * @return status
         */
        public Integer getStatus() {
            return status;
        }

        /**
         * This method is used to set the status
         *
         * @param status For setting the status
         */

        public void setStatus(Integer status) {
            this.status = status;
        }

    }


}