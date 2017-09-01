package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_Video_Details_Output {

    String videoResolution;
    String videoUrl;
    String emed_url;
    ArrayList<String> SubTitleName = new ArrayList<>();
    String studio_approved_url,licenseUrl;
    String is_offline;

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
    String played_length;
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
