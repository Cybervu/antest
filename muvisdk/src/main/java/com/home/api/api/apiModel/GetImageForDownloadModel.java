package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Image for Download
 *
 * @author Abhishek
 */

public class GetImageForDownloadModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("image_url")
    @Expose
    private String imageUrl = "";

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
     * This method is used to get the image url
     * @return imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method is used to set the image url
     * @param imageUrl For setting the image url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
