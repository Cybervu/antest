package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Contact Us
 *
 * @author Abhishek
 */

public class ContactUsModel {
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("success_msg")
    @Expose
    private String successMsg = "";


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
     * This method is used to get the success message
     *
     * @return successMsg
     */

    public String getSuccessMsg() {
        return successMsg;
    }

    /**
     * This method is used to set the success message
     *
     * @param successMsg For setting the success message
     */

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }
}
