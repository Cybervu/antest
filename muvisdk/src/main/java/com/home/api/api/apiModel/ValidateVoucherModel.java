package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public class ValidateVoucherModel {


    @SerializedName("movie_id")
    @Expose
    private String movieId = "";
    @SerializedName("season")
    @Expose
    private Integer season = 0;
    @SerializedName("stream_id")
    @Expose
    private String streamId = "";
    @SerializedName("content_types_id")
    @Expose
    private String contentTypesId = "";
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";

    /**
     * This method is used to get the movie id
     *
     * @return movieId
     */

    public String getMovieId() {
        return movieId;
    }

    /**
     * This method is used to set the movie id
     *
     * @param movieId For setting the movie id
     */
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    /**
     * This method is used to get the season
     *
     * @return season
     */
    public Integer getSeason() {
        return season;
    }

    /**
     * This method is used to set the season
     *
     * @param season For setting the season
     */
    public void setSeason(Integer season) {
        this.season = season;
    }

    /**
     * This method is used to get the stream id
     *
     * @return streamId
     */
    public String getStreamId() {
        return streamId;
    }

    /**
     * This method is used to set the stream id
     *
     * @param streamId For setting the stream id
     */
    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    /**
     * This method is used to get the content type id
     *
     * @return contentTypesId
     */
    public String getContentTypesId() {
        return contentTypesId;
    }

    /**
     * This method is used to set the content type id
     *
     * @param contentTypesId For setting the content type id
     */
    public void setContentTypesId(String contentTypesId) {
        this.contentTypesId = contentTypesId;
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

}
