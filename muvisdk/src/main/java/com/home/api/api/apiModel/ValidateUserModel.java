package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Validate user
 *
 * @author Abhishek
 */

public class ValidateUserModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("member_subscribed")
    @Expose
    private String member_subscribed = "";
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";

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
     * This method is used to get the server code
     *
     * @return stringCode
     */
    public String getMember_subscribed() {
        return member_subscribed;
    }

    /**
     * This method is used to set the server code
     *
     * @param stringCode For setting the server code
     */
    public void setMember_subscribed(String stringCode) {
        this.member_subscribed = stringCode;
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
}
