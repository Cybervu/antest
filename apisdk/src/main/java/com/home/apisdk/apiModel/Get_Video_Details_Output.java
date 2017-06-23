package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_Video_Details_Output {

    String videoResolution;
    String videoUrl;
    String emed_url;

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
