package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public class GetCastDetailsModel {

    @SerializedName("status")
    @Expose
    private Integer status = 0;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("summary")
    @Expose
    private String summary = "";
    @SerializedName("cast_image")
    @Expose
    private String castImage = "";
    @SerializedName("custom_data")
    @Expose
    private ArrayList<String> customData = null;
    @SerializedName("movieList")
    @Expose
    private ArrayList<MovieList> movieList = null;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("orderby")
    @Expose
    private String orderby = "";
    @SerializedName("item_count")
    @Expose
    private String itemCount = "";
    @SerializedName("limit")
    @Expose
    private String limit = "";
    @SerializedName("Ads")
    @Expose
    private ArrayList<String> ads = null;

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
     * This method is used to get the summary
     *
     * @return summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * This method is used to set the summary
     *
     * @param summary For setting the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * This method is used to get the cast image
     *
     * @return castImage
     */
    public String getCastImage() {
        return castImage;
    }

    /**
     * This method is used to set the cast image
     *
     * @param castImage For setting the cast image
     */

    public void setCastImage(String castImage) {
        this.castImage = castImage;
    }

    /**
     * This method is used to get the array list of custom data
     *
     * @return customData
     */

    public ArrayList<String> getCustomData() {
        return customData;
    }

    /**
     * This method is used to set the array list of custom data
     *
     * @param customData For setting the custom data
     */

    public void setCustomData(ArrayList<String> customData) {
        this.customData = customData;
    }

    /**
     * This method is used to get the array list of movies
     *
     * @return movieList
     */
    public ArrayList<MovieList> getMovieList() {
        return movieList;
    }

    /**
     * This method is used to set the array list of movies
     *
     * @param movieList For setting the movie list
     */
    public void setMovieList(ArrayList<MovieList> movieList) {
        this.movieList = movieList;
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
     * This method is used to get the order by details
     *
     * @return orderby
     */

    public String getOrderby() {
        return orderby;
    }

    /**
     * This method is used to set the order by details
     *
     * @param orderby For setting the order by details
     */

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /**
     * This method is used to get the item count
     *
     * @return itemCount
     */
    public String getItemCount() {
        return itemCount;
    }

    /**
     * This method is used to set the item count
     *
     * @param itemCount For setting the item count
     */

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * This method is used to get the limit
     *
     * @return limit
     */

    public String getLimit() {
        return limit;
    }

    /**
     * This method is used to set the limit
     *
     * @param limit For setting the limit
     */

    public void setLimit(String limit) {
        this.limit = limit;
    }

    /**
     * This method is used to get the array list of ads
     *
     * @return ads
     */
    public ArrayList<String> getAds() {
        return ads;
    }

    /**
     * This method is used to set the array list of ads
     *
     * @param ads For setting the ad list
     */

    public void setAds(ArrayList<String> ads) {
        this.ads = ads;
    }

    public class MovieList {

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
        @SerializedName("content_type_id")
        @Expose
        private String contentTypeId = "";
        @SerializedName("ppv_plan_id")
        @Expose
        private String ppvPlanId = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("full_movie")
        @Expose
        private String fullMovie = "";
        @SerializedName("story")
        @Expose
        private String story = "";
        @SerializedName("genre")
        @Expose
        private ArrayList<String> genre = null;
        @SerializedName("release_date")
        @Expose
        private String releaseDate = "";
        @SerializedName("content_types_id")
        @Expose
        private String contentTypesId = "";
        @SerializedName("is_converted")
        @Expose
        private Integer isConverted = 0;
        @SerializedName("last_updated_date")
        @Expose
        private String lastUpdatedDate = "";
        @SerializedName("movieid")
        @Expose
        private String movieid = "";
        @SerializedName("geocategory_id")
        @Expose
        private String geocategoryId = "";
        @SerializedName("studio_id")
        @Expose
        private String studioId = "";
        @SerializedName("category_id")
        @Expose
        private String categoryId = "";
        @SerializedName("country_code")
        @Expose
        private String countryCode = "";
        @SerializedName("ip")
        @Expose
        private String ip = "";
        @SerializedName("poster_url")
        @Expose
        private String posterUrl = "";
        @SerializedName("isFreeContent")
        @Expose
        private Integer isFreeContent = 0;
        @SerializedName("embeddedUrl")
        @Expose
        private String embeddedUrl = "";
        @SerializedName("viewStatus")
        @Expose
        private ViewStatus viewStatus;

        public int getIs_ppv() {
            return is_ppv;
        }

        public void setIs_ppv(int is_ppv) {
            this.is_ppv = is_ppv;
        }

        public int getIs_advance() {
            return is_advance;
        }

        public void setIs_advance(int is_advance) {
            this.is_advance = is_advance;
        }

        @SerializedName("is_ppv")
        @Expose
        private int is_ppv = 0;
        @SerializedName("is_advance")
        @Expose
        private int is_advance=0;

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
         * @param movieStreamUniqId For setting the movie stream id
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
         * This method is used to get the Movie unique id
         *
         * @return muviUniqId
         */
        public String getMuviUniqId() {
            return muviUniqId;
        }

        /**
         * This method is used to set the Movie unique id
         *
         * @param muviUniqId For setting the movie unique id
         */

        public void setMuviUniqId(String muviUniqId) {
            this.muviUniqId = muviUniqId;
        }

        /**
         * This method is used to get the content type id
         *
         * @return contentTypeId
         */
        public Object getContentTypeId() {
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
         * This method is used to get the full movie
         *
         * @return fullMovie
         */
        public Object getFullMovie() {
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
         * This method is used to get the genre
         *
         * @return genre
         */

        public ArrayList<String> getGenre() {
            return genre;
        }

        /**
         * This method is used to set the genre
         *
         * @param genre For setting the genre
         */

        public void setGenre(ArrayList<String> genre) {
            this.genre = genre;
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
         * This method is used to get the last updated date
         *
         * @return lastUpdatedDate
         */
        public String getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        /**
         * This method is used to set the last updated date
         *
         * @param lastUpdatedDate For setting the last updated date
         */
        public void setLastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        /**
         * This method is used to get the Movie id
         *
         * @return movieid
         */

        public String getMovieid() {
            return movieid;
        }

        /**
         * This method is used to set the Movie id
         *
         * @param movieid For setting the movie id
         */

        public void setMovieid(String movieid) {
            this.movieid = movieid;
        }

        /**
         * This method is used to get the Geo category id
         *
         * @return geocategoryId
         */

        public Object getGeocategoryId() {
            return geocategoryId;
        }

        /**
         * This method is used to set the Geo category id
         *
         * @param geocategoryId For setting the category id
         */

        public void setGeocategoryId(String geocategoryId) {
            this.geocategoryId = geocategoryId;
        }

        /**
         * This method is used to get the studio id
         *
         * @return studioId
         */

        public Object getStudioId() {
            return studioId;
        }

        /**
         * This method is used to set the studio id
         *
         * @param studioId For setting the studio id
         */

        public void setStudioId(String studioId) {
            this.studioId = studioId;
        }

        /**
         * This method is used to get the category id
         *
         * @return categoryId
         */

        public String getCategoryId() {
            return categoryId;
        }

        /**
         * This method is used to set the category id
         *
         * @param categoryId For setting the category id
         */

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        /**
         * This method is used to get the country code
         *
         * @return countryCode
         */

        public String getCountryCode() {
            return countryCode;
        }

        /**
         * This method is used to set the country code
         *
         * @param countryCode For setting the country code
         */

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
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
         * This method is used to get the embedded url
         *
         * @return embeddedUrl
         */

        public String getEmbeddedUrl() {
            return embeddedUrl;
        }

        /**
         * This method is used to set the embedded url
         *
         * @param embeddedUrl For setting the embedded url details
         */

        public void setEmbeddedUrl(String embeddedUrl) {
            this.embeddedUrl = embeddedUrl;
        }

        /**
         * This method is used to get the view status
         *
         * @return viewStatus
         */
        public ViewStatus getViewStatus() {
            return viewStatus;
        }

        /**
         * This method is used to set the view status
         *
         * @param viewStatus For setting the view status
         */

        public void setViewStatus(ViewStatus viewStatus) {
            this.viewStatus = viewStatus;
        }

    }

    public class ViewStatus {

        @SerializedName("viewcount")
        @Expose
        private String viewcount = "";
        @SerializedName("uniq_view_count")
        @Expose
        private String uniqViewCount = "";

        /**
         * This method is used to get the view count
         *
         * @return viewcount
         */
        public String getViewcount() {
            return viewcount;
        }

        /**
         * This method is used to set the view count
         *
         * @param viewcount For setting the view count
         */

        public void setViewcount(String viewcount) {
            this.viewcount = viewcount;
        }

        /**
         * This method is used to get the Unique view count
         *
         * @return uniqViewCount
         */
        public String getUniqViewCount() {
            return uniqViewCount;
        }

        /**
         * This method is used to set the Unique view count
         *
         * @param uniqViewCount For setting the unique view count
         */
        public void setUniqViewCount(String uniqViewCount) {
            this.uniqViewCount = uniqViewCount;
        }

    }
}
