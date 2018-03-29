package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Ip Address
 *
 * @author Abhishek
 */

public class IPAddressModel {

    @SerializedName("ip")
    @Expose
    private String ip = "";

    /**
     * This method is used to get the IP
     *
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method is used to set the ip
     *
     * @param ip For setting the ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
}
