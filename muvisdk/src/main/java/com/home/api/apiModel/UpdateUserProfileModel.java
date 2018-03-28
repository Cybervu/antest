package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Update User Profile
 *
 * @author Abhishek
 */

public class UpdateUserProfileModel {

    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("nick_name")
    @Expose
    private String nickName = "";
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber = "";
    @SerializedName("profile_image")
    @Expose
    private String profileImage = "";

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

    /**
     * This method is used to get the name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to set the name
     *
     * @param name For setting the name
     */
    public void setName(String name) {
        this.name = name;
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
     * This method is used to get the mobile number
     *
     * @return mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * This method is used to set the mobile number
     *
     * @param mobileNumber For setting the mobile number
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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
}
