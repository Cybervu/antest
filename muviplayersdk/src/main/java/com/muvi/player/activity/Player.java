package com.muvi.player.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MUVI on 15-05-2017.
 */

public class Player implements Serializable{


    boolean isLiveStream = false;
    boolean isLandScape = false;

    boolean isThirdPartyPlayer=false;
    public static boolean player_description = true;
    public static boolean landscape = true;
    public static boolean hide_pause = false;
    public static boolean call_finish_at_onUserLeaveHint = true;
    public static String DefaultSubtitle = "Off";
    public static String VideoResolution = "Auto";
    public static final String loadIPUrl = "https://api.ipify.org/?format=json";

    public static boolean checkNetwork(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected==false){
            return false;
        }
        return true;
    }



    public boolean isLandScape() {
        return isLandScape;
    }

    public void setLandScape(boolean landScape) {
        isLandScape = landScape;
    }

    public boolean isLiveStream() {
        return isLiveStream;
    }

    public void setLiveStream(boolean liveStream) {
        isLiveStream = liveStream;
    }


    boolean waterMark=true;


    int playPos = 0;

    String movieUniqueId,streamUniqueId;
    String videoUrl = "";
    String rootUrl="";

    String authTokenStr;
    String userId;
    String emailId;
    String movieId;
    String Episode_id;
    int isFreeContent;
    String videoResolution;

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    public int getIsFreeContent() {
        return isFreeContent;
    }

    public void setIsFreeContent(int isFreeContent) {
        this.isFreeContent = isFreeContent;
    }

    public String getEpisode_id() {
        return Episode_id;
    }

    public void setEpisode_id(String episode_id) {
        Episode_id = episode_id;
    }


    ArrayList<String> SubTitleName = new ArrayList<>();

    public static boolean isPlayer_description() {
        return player_description;
    }

    public static void setPlayer_description(boolean player_description) {
        Player.player_description = player_description;
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

    public ArrayList<String> getFakeSubTitlePath() {
        return FakeSubTitlePath;
    }

    public void setFakeSubTitlePath(ArrayList<String> fakeSubTitlePath) {
        FakeSubTitlePath = fakeSubTitlePath;
    }

    private String videoTitle = "";
    private String videoGenre = "";
   /* private String videoDuration = "00:00:00";
    private String videoReleaseDate = "00/00/0000";*/
   private String videoDuration = "";
    private String videoReleaseDate = "";
    private String videoStory = "";
    private boolean castCrew = false;
    private String censorRating = "";
    private String posterImageId = "https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png";

    public boolean isThirdPartyPlayer() {
        return isThirdPartyPlayer;
    }

    public void setThirdPartyPlayer(boolean thirdPartyPlayer) {
        isThirdPartyPlayer = thirdPartyPlayer;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }


    public String getAuthTokenStr() {
        return authTokenStr;
    }

    public void setAuthTokenStr(String authTokenStr) {
        this.authTokenStr = authTokenStr;
    }

    public boolean isWaterMark() {
        return waterMark;
    }

    public void setWaterMark(boolean waterMark) {
        this.waterMark = waterMark;
    }


    public int getPlayPos() {
        return playPos;
    }

    public void setPlayPos(int playPos) {
        this.playPos = playPos;
    }


    public String getMovieUniqueId() {
        return movieUniqueId;
    }

    public void setMovieUniqueId(String movieUniqueId) {
        this.movieUniqueId = movieUniqueId;
    }

    public String getStreamUniqueId() {
        return streamUniqueId;
    }

    public void setStreamUniqueId(String streamUniqueId) {
        this.streamUniqueId = streamUniqueId;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoGenre() {
        return videoGenre;
    }

    public void setVideoGenre(String videoGenre) {
        this.videoGenre = videoGenre;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoReleaseDate() {
        return videoReleaseDate;
    }

    public void setVideoReleaseDate(String videoReleaseDate) {
        this.videoReleaseDate = videoReleaseDate;
    }

    public String getVideoStory() {
        return videoStory;
    }

    public void setVideoStory(String videoStory) {
        this.videoStory = videoStory;
    }

    public boolean isCastCrew() {
        return castCrew;
    }

    public void setCastCrew(boolean castCrew) {
        this.castCrew = castCrew;
    }

    public String getCensorRating() {
        return censorRating;
    }

    public void setCensorRating(String censorRating) {
        this.censorRating = censorRating;
    }

    public String getPosterImageId() {
        return posterImageId;
    }

    public void setPosterImageId(String posterImageId) {
        this.posterImageId = posterImageId;
    }

  /*  public int getContentTypesId() {
        return ContentTypesId;
    }

    public void setContentTypesId(int contentTypesId) {
        ContentTypesId = contentTypesId;
    }
*/


    private String StoryColor;

    public String getStoryColor() {
        return StoryColor;
    }

    public void setStoryColor(String storyColor) {
        StoryColor = storyColor;
    }




}
