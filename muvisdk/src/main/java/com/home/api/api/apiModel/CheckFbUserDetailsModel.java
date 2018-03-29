package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/20/2018.
 */

public class CheckFbUserDetailsModel {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("is_newuser")
    @Expose
    private Integer is_newuser;

    public Integer getIs_newuser() {
        return is_newuser;
    }

    public void setIs_newuser(Integer is_newuser) {
        this.is_newuser = is_newuser;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @SerializedName("code")
    @Expose
    private Integer code;
}
