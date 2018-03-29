package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Add to favorite
 *
 * @author Abhishek
 */

public class AddToFavModel {

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
     * This method is used for getting the code
     *
     * @return code
     */

    public Integer getCode() {
        return code;
    }

    /**
     * This method is used for setting the code
     *
     * @param code Server request code
     */

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * This method is used for getting the status
     *
     * @return status
     */

    public String getStatus() {
        return status;
    }

    /**
     * This method is used for setting the status
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
