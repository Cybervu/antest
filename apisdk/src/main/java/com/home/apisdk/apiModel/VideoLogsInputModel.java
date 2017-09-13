package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For GetVideoLogsAsynTask
 *
 * @author MUVI
 */

public class VideoLogsInputModel {

    String authToken;
    String userId;
    String ipAddress;
    String muviUniqueId;
    String episodeStreamUniqueId;
    String playedLength;
    String watchStatus;
    String deviceType;
    String videoLogId;
    String is_streaming_restriction;

    public String getIs_streaming_restriction() {
        return is_streaming_restriction;
    }

    public void setIs_streaming_restriction(String is_streaming_restriction) {
        this.is_streaming_restriction = is_streaming_restriction;
    }

    public String getRestrict_stream_id() {
        return restrict_stream_id;
    }

    public void setRestrict_stream_id(String restrict_stream_id) {
        this.restrict_stream_id = restrict_stream_id;
    }

    String restrict_stream_id;

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

    public String getPlayedLength() {
        return playedLength;
    }

    public void setPlayedLength(String playedLength) {
        this.playedLength = playedLength;
    }

    public String getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVideoLogId() {
        return videoLogId;
    }

    public void setVideoLogId(String videoLogId) {
        this.videoLogId = videoLogId;
    }
}
