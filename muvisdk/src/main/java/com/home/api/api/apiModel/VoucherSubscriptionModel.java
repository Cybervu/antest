package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/16/2018.
 */

public class VoucherSubscriptionModel {

    @SerializedName("status")
    @Expose
    private String status = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
}
