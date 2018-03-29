package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/16/2018.
 */

public class VoucherPlanModel {

    @SerializedName("code")
    @Expose
    private Integer code=0;
    @SerializedName("status")
    @Expose
    private String status="";
    @SerializedName("is_show")
    @Expose
    private String isShow="0";
    @SerializedName("is_season")
    @Expose
    private String isSeason="0";
    @SerializedName("is_episode")
    @Expose
    private String isEpisode="0";

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsSeason() {
        return isSeason;
    }

    public void setIsSeason(String isSeason) {
        this.isSeason = isSeason;
    }

    public String getIsEpisode() {
        return isEpisode;
    }

    public void setIsEpisode(String isEpisode) {
        this.isEpisode = isEpisode;
    }
}
