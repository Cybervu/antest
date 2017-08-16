package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 9/28/2016.
 */
public class ContentDetailsOutput{
String movieStreamUniqId = "";
    String muviUniqId = "";
    String censorRating = "";
    String releaseDate = "";
    String story = "";
    String videoDuration = "";
    String isFreeContent = "";
    String isAdvance = "";
    String actor = "";
    String director;
    String cast_detail;
    String trailerUrl;
    String movieUrlForTv;
    String movieUrl;
    String embeddedUrl;
    String banner;
    String poster;
    String comments;
    String epDetails;
    String name;
    String permalink;
    String genre = "";
    String rating = "";
    String review = "";
    String id = "";


    public String[] getSeason() {
        return season;
    }

    public void setSeason(String[] season) {
        this.season = season;
    }

    String[] season;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    int is_favorite = 0;


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getIsEpisode() {
        return isEpisode;
    }

    public void setIsEpisode(String isEpisode) {
        this.isEpisode = isEpisode;
    }

    String isEpisode = "";
    int isConverted;

    public Boolean getCastStr() {
        return castStr;
    }

    public void setCastStr(Boolean castStr) {
        this.castStr = castStr;
    }

    Boolean castStr;

    public int getIsPpv() {
        return isPpv;
    }

    public int getIsConverted() {
        return isConverted;
    }

    int isPpv;
    int isApv;

    public void setContentTypesId(int contentTypesId) {
        this.contentTypesId = contentTypesId;
    }

    public int getContentTypesId() {
        return contentTypesId;
    }

    int contentTypesId;
    APVModel apvDetails;
    PPVModel ppvDetails;
    CurrencyModel currencyDetails;



    public APVModel getApvDetails() {
        return apvDetails;
    }

    public void setApvDetails(APVModel apvDetails) {
        this.apvDetails = apvDetails;
    }

    public PPVModel getPpvDetails() {
        return ppvDetails;
    }

    public void setPpvDetails(PPVModel ppvDetails) {
        this.ppvDetails = ppvDetails;
    }

    public CurrencyModel getCurrencyDetails() {
        return currencyDetails;
    }

    public void setCurrencyDetails(CurrencyModel currencyDetails) {
        this.currencyDetails = currencyDetails;
    }

    public void setIsConverted(int isConverted) {
        this.isConverted = isConverted;
    }

    public void setIsPpv(int isPpv) {
        this.isPpv = isPpv;
    }

    public int getIsApv() {
        return isApv;
    }

    public void setIsApv(int isApv) {
        this.isApv = isApv;
    }

    public String getMovieStreamUniqId() {
        return movieStreamUniqId;
    }

    public void setMovieStreamUniqId(String movieStreamUniqId) {
        this.movieStreamUniqId = movieStreamUniqId;
    }

    public String getMuviUniqId() {
        return muviUniqId;
    }

    public void setMuviUniqId(String muviUniqId) {
        this.muviUniqId = muviUniqId;
    }

    public String getCensorRating() {
        return censorRating;
    }

    public void setCensorRating(String censorRating) {
        this.censorRating = censorRating;
    }



    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getIsFreeContent() {
        return isFreeContent;
    }

    public void setIsFreeContent(String isFreeContent) {
        this.isFreeContent = isFreeContent;
    }


    public String getIsAdvance() {
        return isAdvance;
    }

    public void setIsAdvance(String isAdvance) {
        this.isAdvance = isAdvance;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast_detail() {
        return cast_detail;
    }

    public void setCast_detail(String cast_detail) {
        this.cast_detail = cast_detail;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getMovieUrlForTv() {
        return movieUrlForTv;
    }

    public void setMovieUrlForTv(String movieUrlForTv) {
        this.movieUrlForTv = movieUrlForTv;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getEmbeddedUrl() {
        return embeddedUrl;
    }

    public void setEmbeddedUrl(String embeddedUrl) {
        this.embeddedUrl = embeddedUrl;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEpDetails() {
        return epDetails;
    }

    public void setEpDetails(String epDetails) {
        this.epDetails = epDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


}
