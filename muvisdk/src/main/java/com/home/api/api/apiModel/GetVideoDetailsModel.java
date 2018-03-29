package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Video Details
 *
 * @author Abhishek
 */

public class GetVideoDetailsModel {

    @SerializedName("is_watermark")
    @Expose
    private IsWatermark isWatermark;
    @SerializedName("videoResolution")
    @Expose
    private String videoResolution = "";
    @SerializedName("videoDetails")
    @Expose
    private ArrayList<VideoDetail> videoDetails = null;
    @SerializedName("newvideoUrl")
    @Expose
    private String newvideoUrl = "";
    @SerializedName("is_offline")
    @Expose
    private String isOffline = "";
    @SerializedName("studio_approved_url")
    @Expose
    private String studioApprovedUrl = "";
    @SerializedName("licenseUrl")
    @Expose
    private String licenseUrl = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("videoUrl")
    @Expose
    private String videoUrl = "";
    @SerializedName("thirdparty_url")
    @Expose
    private String thirdpartyUrl = "";
    @SerializedName("emed_url")
    @Expose
    private String emedUrl = "";
    @SerializedName("trailerUrl")
    @Expose
    private String trailerUrl = "";
    @SerializedName("trailerThirdpartyUrl")
    @Expose
    private String trailerThirdpartyUrl = "";
    @SerializedName("embedTrailerUrl")
    @Expose
    private String embedTrailerUrl = "";
    @SerializedName("played_length")
    @Expose
    private String playedLength = "";
    @SerializedName("subTitle")
    @Expose
    private ArrayList<Subtitle> subTitle = null;
    @SerializedName("streaming_restriction")
    @Expose
    private String streamingRestriction = "";
    @SerializedName("no_streaming_device")
    @Expose
    private Integer noStreamingDevice = 0;
    @SerializedName("no_of_views")
    @Expose
    private String noOfViews = "";
    @SerializedName("download_status")
    @Expose
    private String downloadStatus = "";
    @SerializedName("adDetails")
    @Expose
    private AdDetails adDetails;

    public AdDetails getAdDetails() {
        return adDetails;
    }

    public void setAdDetails(AdDetails adDetails) {
        this.adDetails = adDetails;
    }

    /**
     * This method is used to get the watermark details
     *
     * @return isWatermark
     */
    public IsWatermark getIsWatermark() {
        return isWatermark;
    }

    /**
     * This method is used to set the watermark details
     *
     * @param isWatermark For setting the watermark
     */
    public void setIsWatermark(IsWatermark isWatermark) {
        this.isWatermark = isWatermark;
    }

    /**
     * This method is used to get the video resolution
     *
     * @return videoResolution
     */
    public String getVideoResolution() {
        return videoResolution;
    }

    /**
     * This method is used to set the video resolution
     *
     * @param videoResolution For setting the video resolution
     */
    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    /**
     * This method is used to get the array list of video details
     *
     * @return videoDetails
     */
    public ArrayList<VideoDetail> getVideoDetails() {
        return videoDetails;
    }

    /**
     * This method is used to set the array list of video details
     *
     * @param videoDetails For setting the video details
     */
    public void setVideoDetails(ArrayList<VideoDetail> videoDetails) {
        this.videoDetails = videoDetails;
    }

    /**
     * This method is used to get the new video url
     *
     * @return newvideoUrl
     */
    public String getNewvideoUrl() {
        return newvideoUrl;
    }

    /**
     * This method is used to set the new video url
     *
     * @param newvideoUrl For setting the new video url
     */
    public void setNewvideoUrl(String newvideoUrl) {
        this.newvideoUrl = newvideoUrl;
    }

    /**
     * This method is used to get the offline details
     *
     * @return isOffline
     */
    public String getIsOffline() {
        return isOffline;
    }

    /**
     * This method is used to set the offline details
     *
     * @param isOffline For setting the offline details
     */
    public void setIsOffline(String isOffline) {
        this.isOffline = isOffline;
    }

    /**
     * This method is used to get the studio approval url
     *
     * @return studioApprovedUrl
     */
    public String getStudioApprovedUrl() {
        return studioApprovedUrl;
    }

    /**
     * This method is used to set the studio approval url
     *
     * @param studioApprovedUrl For setting the studio approval url
     */
    public void setStudioApprovedUrl(String studioApprovedUrl) {
        this.studioApprovedUrl = studioApprovedUrl;
    }

    /**
     * This method is used to get the licence url
     *
     * @return licenseUrl
     */
    public String getLicenseUrl() {
        return licenseUrl;
    }

    /**
     * This method is used to set the licence url
     *
     * @param licenseUrl For setting the licence url
     */
    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
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
     * This method is used to get the video url
     *
     * @return videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * This method is used to set the video url
     *
     * @param videoUrl For setting the video url
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * This method is used to get the third party url
     *
     * @return thirdpartyUrl
     */
    public String getThirdpartyUrl() {
        return thirdpartyUrl;
    }

    /**
     * This method is used to set the third party url
     *
     * @param thirdpartyUrl For setting the third party url
     */
    public void setThirdpartyUrl(String thirdpartyUrl) {
        this.thirdpartyUrl = thirdpartyUrl;
    }

    /**
     * This method is used to get the embedded url
     *
     * @return emedUrl
     */
    public String getEmedUrl() {
        return emedUrl;
    }

    /**
     * This method is used to set the embedded url
     *
     * @param emedUrl For setting the embedded url
     */
    public void setEmedUrl(String emedUrl) {
        this.emedUrl = emedUrl;
    }

    /**
     * This method is used to get the trailer url
     *
     * @return trailerUrl
     */
    public String getTrailerUrl() {
        return trailerUrl;
    }

    /**
     * This method is used to set the trailer url
     *
     * @param trailerUrl For setting the trailer url
     */
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    /**
     * This method is used to get the trailer third party url
     *
     * @return trailerThirdpartyUrl
     */
    public String getTrailerThirdpartyUrl() {
        return trailerThirdpartyUrl;
    }

    /**
     * This method is used to set the trailer third party url
     *
     * @param trailerThirdpartyUrl For setting the trailer third party url
     */
    public void setTrailerThirdpartyUrl(String trailerThirdpartyUrl) {
        this.trailerThirdpartyUrl = trailerThirdpartyUrl;
    }

    /**
     * This method is used to get the embedded trailer url
     *
     * @return embedTrailerUrl
     */
    public String getEmbedTrailerUrl() {
        return embedTrailerUrl;
    }

    /**
     * This method is used to set the embedded trailer url
     *
     * @param embedTrailerUrl For setting the embedded trailer url
     */
    public void setEmbedTrailerUrl(String embedTrailerUrl) {
        this.embedTrailerUrl = embedTrailerUrl;
    }

    /**
     * This method is used to get the played length
     *
     * @return playedLength
     */
    public String getPlayedLength() {
        return playedLength;
    }

    /**
     * This method is used to set the played length
     *
     * @param playedLength For setting the played length
     */
    public void setPlayedLength(String playedLength) {
        this.playedLength = playedLength;
    }

    /**
     * This method is used to get the array list of subtitle
     *
     * @return subTitle
     */
    public ArrayList<Subtitle> getSubTitle() {
        return subTitle;
    }

    /**
     * This method is used to set the array list of subtitle
     *
     * @param subTitle For setting the subtitle
     */
    public void setSubTitle(ArrayList<Subtitle> subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * This method is used to get the streaming restriction
     *
     * @return streamingRestriction
     */
    public String getStreamingRestriction() {
        return streamingRestriction;
    }

    /**
     * This method is used to set the streaming restriction
     *
     * @param streamingRestriction For setting the streaming restriction
     */
    public void setStreamingRestriction(String streamingRestriction) {
        this.streamingRestriction = streamingRestriction;
    }

    /**
     * This method is used to get the number of streaming device
     *
     * @return noStreamingDevice
     */
    public Integer getNoStreamingDevice() {
        return noStreamingDevice;
    }

    /**
     * This method is used to set the number of streaming device
     *
     * @param noStreamingDevice For setting the number of streaming device
     */
    public void setNoStreamingDevice(Integer noStreamingDevice) {
        this.noStreamingDevice = noStreamingDevice;
    }

    /**
     * This method is used to get the number of views
     *
     * @return noOfViews
     */
    public String getNoOfViews() {
        return noOfViews;
    }

    /**
     * This method is used to set the number of views
     *
     * @param noOfViews For setting the number of views
     */
    public void setNoOfViews(String noOfViews) {
        this.noOfViews = noOfViews;
    }

    /**
     * This method is used to get the download status
     *
     * @return downloadStatus
     */
    public String getDownloadStatus() {
        return downloadStatus;
    }

    /**
     * This method is used to set the download status
     *
     * @param downloadStatus For setting the download status
     */
    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public class IsWatermark {

        @SerializedName("status")
        @Expose
        private Integer status = 0;
        @SerializedName("email")
        @Expose
        private String email = "";
        @SerializedName("ip")
        @Expose
        private String ip = "";
        @SerializedName("date")
        @Expose
        private String date = "";

        /**
         * This method is used to get the status
         *
         * @return status
         */
        public Integer getStatus() {
            return status;
        }

        /**
         * This method is used to set the status
         *
         * @param status For setting the status
         */
        public void setStatus(Integer status) {
            this.status = status;
        }

        /**
         * This method is used to get the email
         *
         * @return email
         */
        public String getEmail() {
            return email;
        }

        /**
         * This method is used to set the email
         *
         * @param email For setting the email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * This method is used to get the ip
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

        /**
         * This method is used to get the date
         *
         * @return date
         */
        public String getDate() {
            return date;
        }

        /**
         * This method is used to set the date
         *
         * @param date For setting the date
         */
        public void setDate(String date) {
            this.date = date;
        }

    }

    public class VideoDetail {

        /*@SerializedName("resolution")
        @Expose
        private ArrayList<String> resolution = null;*/
        private ArrayList<String> resolution;
        @SerializedName("url")
        @Expose
        private ArrayList<String> url = null;

        /**
         * This method is used to get the resolution
         *
         * @return resolution
         */
        public ArrayList getResolution() {
            resolution.add("ewffkjbw");
            return resolution;
        }

        /**
         * This method is used to set the resolution
         *
         * @param resolution For setting the resolution
         */
        public void setResolution(ArrayList resolution) {
            this.resolution = resolution;
        }

        /**
         * This method is used to get the url
         *
         * @return url
         */
        public ArrayList getUrl() {
            return url;
        }

        /**
         * This method is used to set the url
         *
         * @param url For setting the url
         */
        public void setUrl(ArrayList url) {
            this.url = url;
        }

    }

    public class Subtitle {
        @SerializedName("language")
        @Expose
        private ArrayList<String> SubTitleName = new ArrayList<>();

        @SerializedName("url")
        @Expose
        private ArrayList<String> FakeSubTitlePath = new ArrayList<>();

        public ArrayList<String> getSubTitleName() {
            return SubTitleName;
        }

        public void setSubTitleName(ArrayList<String> subTitleName) {
            SubTitleName = subTitleName;
        }

        public ArrayList<String> getFakeSubTitlePath() {
            return FakeSubTitlePath;
        }

        public void setFakeSubTitlePath(ArrayList<String> fakeSubTitlePath) {
            FakeSubTitlePath = fakeSubTitlePath;
        }

        public ArrayList<String> getSubtitle_code() {
            return subtitle_code;
        }

        public void setSubtitle_code(ArrayList<String> subtitle_code) {
            this.subtitle_code = subtitle_code;
        }

       /* public ArrayList<String> getOffline_url() {
            return offline_url;
        }

        public void setOffline_url(ArrayList<String> offline_url) {
            this.offline_url = offline_url;
        }
*/
       /* public ArrayList<String> getOffline_language() {
            return offline_language;
        }

        public void setOffline_language(ArrayList<String> offline_language) {
            this.offline_language = offline_language;
        }*/

        @SerializedName("code")
        @Expose
        private ArrayList<String> subtitle_code = new ArrayList<>();
        /*@SerializedName("url")
        @Expose
        private ArrayList<String> offline_url = new ArrayList<>();*/
       /* @SerializedName("language")
        @Expose
        private ArrayList<String> offline_language = new ArrayList<>();*/
    }

    public class AdsTime {

        @SerializedName("mid")
        @Expose
        private Integer mid=0;
        @SerializedName("start")
        @Expose
        private Integer start=0;
        @SerializedName("end")
        @Expose
        private Integer end=0;
        @SerializedName("midroll_values")
        @Expose
        private String midrollValues="";

        public Integer getMid() {
            return mid;
        }

        public void setMid(Integer mid) {
            this.mid = mid;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getMidrollValues() {
            return midrollValues;
        }

        public void setMidrollValues(String midrollValues) {
            this.midrollValues = midrollValues;
        }

    }

    public class AdDetails {

        @SerializedName("adNetwork")
        @Expose
        private ArrayList<AdNetwork> adNetwork = null;
        @SerializedName("adsTime")
        @Expose
        private AdsTime adsTime=null;

        public ArrayList<AdNetwork> getAdNetwork() {
            return adNetwork;
        }

        public void setAdNetwork(ArrayList<AdNetwork> adNetwork) {
            this.adNetwork = adNetwork;
        }

        public AdsTime getAdsTime() {
            return adsTime;
        }

        public void setAdsTime(AdsTime adsTime) {
            this.adsTime = adsTime;
        }

    }

    public class AdNetwork {

        @SerializedName("channel_id")
        @Expose
        private String channelId="";
        @SerializedName("ad_network_id")
        @Expose
        private Integer adNetworkId=0;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public Integer getAdNetworkId() {
            return adNetworkId;
        }

        public void setAdNetworkId(Integer adNetworkId) {
            this.adNetworkId = adNetworkId;
        }

    }
}
