package com.home.apisdk.apiModel.apiModel;

import java.util.ArrayList;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_Video_Details_Output {

    String videoResolution;
    String videoUrl;
    String emed_url;
    ArrayList<String> SubTitleName = new ArrayList<>();

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
