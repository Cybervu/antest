package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Video_Details_Output {

    String videoResolution;
    String videoUrl;
    String emed_url;
    ArrayList<String> SubTitleName = new ArrayList<>();
    String studio_approved_url,licenseUrl;
    String is_offline;
    String channel_id;
    int midRoll;

    public String getDownload_status() {
        return download_status;
    }

    public void setDownload_status(String download_status) {
        this.download_status = download_status;
    }

    String download_status;

    public int getMidRoll() {
        return midRoll;
    }

    public void setMidRoll(int midRoll) {
        this.midRoll = midRoll;
    }

    public int getPostRoll() {
        return postRoll;
    }

    public void setPostRoll(int postRoll) {
        this.postRoll = postRoll;
    }

    public int getPreRoll() {
        return preRoll;
    }

    public void setPreRoll(int preRoll) {
        this.preRoll = preRoll;
    }

    int postRoll;
    int preRoll;

    public int getAdNetworkId() {
        return adNetworkId;
    }

    public void setAdNetworkId(int adNetworkId) {
        this.adNetworkId = adNetworkId;
    }

    int adNetworkId = 1;
    String no_streaming_device;

    public String getNo_streaming_device() {
        return no_streaming_device;
    }

    public void setNo_streaming_device(String no_streaming_device) {
        this.no_streaming_device = no_streaming_device;
    }

    public String getNo_of_views() {
        return no_of_views;
    }

    public void setNo_of_views(String no_of_views) {
        this.no_of_views = no_of_views;
    }

    public String getTrailerThirdpartyUrl() {
        return trailerThirdpartyUrl;
    }

    public void setTrailerThirdpartyUrl(String trailerThirdpartyUrl) {
        this.trailerThirdpartyUrl = trailerThirdpartyUrl;
    }

    public String getEmbedTrailerUrl() {
        return embedTrailerUrl;
    }

    public void setEmbedTrailerUrl(String embedTrailerUrl) {
        this.embedTrailerUrl = embedTrailerUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    String no_of_views;
    String trailerThirdpartyUrl;
    String embedTrailerUrl;
    String trailerUrl;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }


    public String getAdDetails() {
        return adDetails;
    }

    public void setAdDetails(String adDetails) {
        this.adDetails = adDetails;
    }

    public String getStreaming_restriction() {
        return streaming_restriction;
    }

    public void setStreaming_restriction(String streaming_restriction) {
        this.streaming_restriction = streaming_restriction;
    }


    String adDetails="";
    String streaming_restriction;
    public String getIs_offline() {
        return is_offline;
    }

    public void setIs_offline(String is_offline) {
        this.is_offline = is_offline;
    }

    public String getStudio_approved_url() {
        return studio_approved_url;
    }

    public void setStudio_approved_url(String studio_approved_url) {
        this.studio_approved_url = studio_approved_url;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public ArrayList<String> getSubTitleName() {
        return SubTitleName;
    }

    public void setSubTitleName(ArrayList<String> subTitleName) {
        SubTitleName = subTitleName;
    }

    public ArrayList<String> getSubTitlePath() {
        return SubTitlePath;
    }

    public void setSubTitlePath(ArrayList<String> subTitlePath) {
        SubTitlePath = subTitlePath;
    }

    public ArrayList<String> getFakeSubTitlePath() {
        return FakeSubTitlePath;
    }

    public void setFakeSubTitlePath(ArrayList<String> fakeSubTitlePath) {
        FakeSubTitlePath = fakeSubTitlePath;
    }

    public ArrayList<String> getResolutionFormat() {
        return ResolutionFormat;
    }

    public void setResolutionFormat(ArrayList<String> resolutionFormat) {
        ResolutionFormat = resolutionFormat;
    }

    public ArrayList<String> getResolutionUrl() {
        return ResolutionUrl;
    }

    public void setResolutionUrl(ArrayList<String> resolutionUrl) {
        ResolutionUrl = resolutionUrl;
    }

    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ArrayList<String> offlineUrl = new ArrayList<>();
    ArrayList<String> offlineLanguage = new ArrayList<>();
    ArrayList<String> SubTitleLanguage = new ArrayList<>();

    public ArrayList<String> getOfflineUrl() {
        return offlineUrl;
    }

    public void setOfflineUrl(ArrayList<String> offlineUrl) {
        this.offlineUrl = offlineUrl;
    }

    public ArrayList<String> getOfflineLanguage() {
        return offlineLanguage;
    }

    public void setOfflineLanguage(ArrayList<String> offlineLanguage) {
        this.offlineLanguage = offlineLanguage;
    }

    public ArrayList<String> getSubTitleLanguage() {
        return SubTitleLanguage;
    }

    public void setSubTitleLanguage(ArrayList<String> subTitleLanguage) {
        SubTitleLanguage = subTitleLanguage;
    }

    public String getThirdparty_url() {
        return thirdparty_url;
    }

    public void setThirdparty_url(String thirdparty_url) {
        this.thirdparty_url = thirdparty_url;
    }

    public String getPlayed_length() {
        return played_length;
    }

    public void setPlayed_length(String played_length) {
        this.played_length = played_length;
    }

    String thirdparty_url;
    String played_length="";
    public void setVideoResolution(String videoResolution){
        this.videoResolution = videoResolution;
    }
    public String getVideoResolution(){
        return videoResolution;
    }

    public void setVideoUrl(String videoUrl){this.videoUrl = videoUrl;}
    public String getVideoUrl(){return videoUrl;}

    public void setEmed_url(String emed_url){this.emed_url = emed_url;}
    public String getEmed_url(){return emed_url;}
}
