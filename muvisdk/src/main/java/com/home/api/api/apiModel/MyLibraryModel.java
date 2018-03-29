package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *This Model Class Holds All The Attributes For My Library
 *
 * @author Abhishek
 */

public class MyLibraryModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("mylibrary")
    @Expose
    private ArrayList<Mylibrary> mylibrary = null;

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
     * This method is used to get the array list of my library
     *
     * @return mylibrary
     */
    public ArrayList<Mylibrary> getMylibrary() {
        return mylibrary;
    }

    /**
     * This method is used to set the array list of my library
     *
     * @param mylibrary
     */
    public void setMylibrary(ArrayList<Mylibrary> mylibrary) {
        this.mylibrary = mylibrary;
    }

    public class Mylibrary {

        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("movie_stream_uniq_id")
        @Expose
        private String movieStreamUniqId = "";
        @SerializedName("movie_id")
        @Expose
        private String movieId = "";
        @SerializedName("movie_stream_id")
        @Expose
        private String movieStreamId = "";
        @SerializedName("is_episode")
        @Expose
        private String isEpisode = "";
        @SerializedName("muvi_uniq_id")
        @Expose
        private String muviUniqId = "";
        @SerializedName("content_types_id")
        @Expose
        private String contentTypesId = "";
        @SerializedName("content_type_id")
        @Expose
        private String contentTypeId = "";
        @SerializedName("baseurl")
        @Expose
        private String baseurl = "";
        @SerializedName("poster_url")
        @Expose
        private String posterUrl = "";
        @SerializedName("video_duration")
        @Expose
        private String videoDuration = "";
        @SerializedName("watch_duration")
        @Expose
        private String watchDuration = "";
        @SerializedName("genres")
        @Expose
        private ArrayList<String> genres = null;
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("ppv_plan_id")
        @Expose
        private String ppvPlanId = "";
        @SerializedName("full_movie")
        @Expose
        private String fullMovie = "";
        @SerializedName("story")
        @Expose
        private String story = "";
        @SerializedName("release_date")
        @Expose
        private String releaseDate = "";
        @SerializedName("is_converted")
        @Expose
        private Integer isConverted = 0;
        @SerializedName("isFreeContent")
        @Expose
        private Integer isFreeContent = 0;
        @SerializedName("season_id")
        @Expose
        private Integer seasonId = 0;

        /**
         * This method is used to get the name
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * This method is used to set the name
         *
         * @param name For setting the name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * This method is used to get the movie stream unique id
         *
         * @return movieStreamUniqId
         */
        public String getMovieStreamUniqId() {
            return movieStreamUniqId;
        }

        /**
         * This method is used to set the movie stream unique id
         *
         * @param movieStreamUniqId For setting the movie stream unique id
         */
        public void setMovieStreamUniqId(String movieStreamUniqId) {
            this.movieStreamUniqId = movieStreamUniqId;
        }

        /**
         * This method is used to get the movie id
         *
         * @return movieId
         */
        public String getMovieId() {
            return movieId;
        }

        /**
         * This method is used to set the movie id
         *
         * @param movieId For setting the movie id
         */
        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        /**
         * This method is used to get the movie stream id
         *
         * @return movieStreamId
         */
        public String getMovieStreamId() {
            return movieStreamId;
        }

        /**
         * This method is used to set the movie stream id
         *
         * @param movieStreamId For setting the movie stream id
         */
        public void setMovieStreamId(String movieStreamId) {
            this.movieStreamId = movieStreamId;
        }

        /**
         * This method is used to get the episode details
         *
         * @return isEpisode
         */
        public String getIsEpisode() {
            return isEpisode;
        }

        /**
         * This method is used to set the episode details
         *
         * @param isEpisode For setting the episode details
         */
        public void setIsEpisode(String isEpisode) {
            this.isEpisode = isEpisode;
        }

        /**
         * This method is used to get the movie unique id
         *
         * @return muviUniqId
         */
        public String getMuviUniqId() {
            return muviUniqId;
        }

        /**
         * This method is used to set the movie unique id
         *
         * @param muviUniqId For setting the movie unique id
         */
        public void setMuviUniqId(String muviUniqId) {
            this.muviUniqId = muviUniqId;
        }

        /**
         * This method is used to get the content type id
         *
         * @return contentTypesId
         */
        public String getContentTypesId() {
            return contentTypesId;
        }

        /**
         * This method is used to set the content type id
         *
         * @param contentTypesId For setting the content type id
         */
        public void setContentTypesId(String contentTypesId) {
            this.contentTypesId = contentTypesId;
        }

        /**
         * This method is used to get the content type id
         *
         * @return contentTypeId
         */
        public String getContentTypeId() {
            return contentTypeId;
        }

        /**
         * This method is used to set the content type id
         *
         * @param contentTypeId For setting the content type id
         */
        public void setContentTypeId(String contentTypeId) {
            this.contentTypeId = contentTypeId;
        }

        /**
         * This method is used to get the base url
         *
         * @return baseurl
         */
        public String getBaseurl() {
            return baseurl;
        }

        /**
         * This method is used to set the base url
         *
         * @param baseurl For setting the base url
         */
        public void setBaseurl(String baseurl) {
            this.baseurl = baseurl;
        }

        /**
         * This method is used to get the poster url
         *
         * @return posterUrl
         */
        public String getPosterUrl() {
            return posterUrl;
        }

        /**
         * This method is used to set the poster url
         *
         * @param posterUrl For setting the poster url
         */
        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        /**
         * This method is used to get the video duration
         *
         * @return videoDuration
         */
        public String getVideoDuration() {
            return videoDuration;
        }

        /**
         * This method is used to set the video duration
         *
         * @param videoDuration For setting the video duration
         */
        public void setVideoDuration(String videoDuration) {
            this.videoDuration = videoDuration;
        }

        /**
         * This method is used to get the watch duration
         *
         * @return watchDuration
         */
        public String getWatchDuration() {
            return watchDuration;
        }

        /**
         * This method is used to set the watch duration
         *
         * @param watchDuration For setting the watch duration
         */
        public void setWatchDuration(String watchDuration) {
            this.watchDuration = watchDuration;
        }

        /**
         * This method is used to get the genres
         *
         * @return genres
         */
        public ArrayList<String> getGenres() {
            return genres;
        }

        /**
         * This method is used to set the genres
         *
         * @param genres For setting the genres
         */
        public void setGenres(ArrayList<String> genres) {
            this.genres = genres;
        }

        /**
         * This method is used to get the permalink
         *
         * @return permalink
         */
        public String getPermalink() {
            return permalink;
        }

        /**
         * This method is used to set the permalink
         *
         * @param permalink For setting the permalink
         */
        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        /**
         * This method is used to get the ppv plan id
         *
         * @return ppvPlanId
         */
        public String getPpvPlanId() {
            return ppvPlanId;
        }

        /**
         * This method is used to set the ppv plan id
         *
         * @param ppvPlanId For setting the ppv plan id
         */
        public void setPpvPlanId(String ppvPlanId) {
            this.ppvPlanId = ppvPlanId;
        }

        /**
         * This method is used to get the full movie
         *
         * @return fullMovie
         */
        public String getFullMovie() {
            return fullMovie;
        }

        /**
         * This method is used to set the full movie
         *
         * @param fullMovie For setting the full movie
         */
        public void setFullMovie(String fullMovie) {
            this.fullMovie = fullMovie;
        }

        /**
         * This method is used to get the story
         *
         * @return story
         */
        public String getStory() {
            return story;
        }

        /**
         * This method is used to set the story
         *
         * @param story For setting the story
         */
        public void setStory(String story) {
            this.story = story;
        }

        /**
         * This method is used to get the release date
         *
         * @return releaseDate
         */
        public String getReleaseDate() {
            return releaseDate;
        }

        /**
         * This method is used to set the release date
         *
         * @param releaseDate For setting the release date
         */
        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        /**
         * This method is used to get the converted details
         *
         * @return isConverted
         */
        public Integer getIsConverted() {
            return isConverted;
        }

        /**
         * This method is used to set the converted details
         *
         * @param isConverted For setting the converted details
         */
        public void setIsConverted(Integer isConverted) {
            this.isConverted = isConverted;
        }

        /**
         * This method is used to get the free content details
         *
         * @return isFreeContent
         */
        public Integer getIsFreeContent() {
            return isFreeContent;
        }

        /**
         * This method is used to set the free content details
         *
         * @param isFreeContent For setting the free content details
         */
        public void setIsFreeContent(Integer isFreeContent) {
            this.isFreeContent = isFreeContent;
        }

        /**
         * This method is used to get the season id
         *
         * @return seasonId
         */
        public Integer getSeasonId() {
            return seasonId;
        }

        /**
         * This method is used to set the season id
         *
         * @param seasonId For setting the season id
         */
        public void setSeasonId(Integer seasonId) {
            this.seasonId = seasonId;
        }

    }
}
