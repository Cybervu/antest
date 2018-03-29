package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Check Country
 *
 * @author Abhishek
 */

public class CheckGeoBlockModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("country")
    @Expose
    private String country = "";

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
     * This method is used to get the country
     *
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * This method is used to set the country
     *
     * @param country For setting the country
     */

    public void setCountry(String country) {
        this.country = country;
    }
}
