package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For GetFFVideoBufferLogDetailsAsync
 *
 * @author MUVI
 */

public class VideoBufferLogsInputModel {

    String authToken;
    String userId;
    String ipAddress = "";
    String muviUniqueId;
    String episodeStreamUniqueId;
    String deviceType;
    String bufferLogId;

    /**
     * This Method is use to Get the Buffer Log Unique Id
     *
     * @return bufferLogUniqueId
     */

    public String getBufferLogUniqueId() {
        return bufferLogUniqueId;
    }

    /**
     * This Method is use to Set the Buffer Log Unique Id
     *
     * @param bufferLogUniqueId For Setting The Buffer Log Unique Id
     */
    public void setBufferLogUniqueId(String bufferLogUniqueId) {
        this.bufferLogUniqueId = bufferLogUniqueId;
    }

    String bufferLogUniqueId;
    String location;
    String bufferStartTime;
    String bufferEndTime;
    String videoResolution;

    /**
     * This Method is use to Get the Buffer Log Id
     *
     * @return bufferLogId
     */

    public String getBufferLogId() {
        return bufferLogId;
    }

    /**
     * This Method is use to Set the Buffer Log Id
     *
     * @param bufferLogId For Setting The Buffer Log Id
     */

    public void setBufferLogId(String bufferLogId) {
        this.bufferLogId = bufferLogId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBufferStartTime() {
        return bufferStartTime;
    }

    public void setBufferStartTime(String bufferStartTime) {
        this.bufferStartTime = bufferStartTime;
    }

    public String getBufferEndTime() {
        return bufferEndTime;
    }

    public void setBufferEndTime(String bufferEndTime) {
        this.bufferEndTime = bufferEndTime;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    /**
     * This Method is use to Get the Auth Token
     *
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * This Method is use to Set the Auth Token
     *
     * @param authToken For Setting The Auth Token
     */

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMuviUniqueId() {
        return muviUniqueId;
    }

    public void setMuviUniqueId(String muviUniqueId) {
        this.muviUniqueId = muviUniqueId;
    }

    public String getEpisodeStreamUniqueId() {
        return episodeStreamUniqueId;
    }

    public void setEpisodeStreamUniqueId(String episodeStreamUniqueId) {
        this.episodeStreamUniqueId = episodeStreamUniqueId;
    }


    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


}
