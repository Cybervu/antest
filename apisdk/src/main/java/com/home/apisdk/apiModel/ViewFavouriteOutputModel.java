package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class ViewFavouriteOutputModel {
    String movieId;
    String permalink;
    String title;
    String contentTypesId;
    String poster;
    String isEpisodeStr;

    public String getIsEpisodeStr() {
        return isEpisodeStr;
    }

    public void setIsEpisodeStr(String isEpisodeStr) {
        this.isEpisodeStr = isEpisodeStr;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentTypesId() {
        return contentTypesId;
    }

    public void setContentTypesId(String contentTypesId) {
        this.contentTypesId = contentTypesId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }





}
