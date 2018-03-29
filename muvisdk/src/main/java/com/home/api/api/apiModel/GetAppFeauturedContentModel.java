package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Feature Content
 *
 * @author Abhishek
 */

public class GetAppFeauturedContentModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("section")
    @Expose
    private ArrayList<Section> section = null;

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
     * This method is used to get the array list of section
     *
     * @return section
     */

    public ArrayList<Section> getSection() {
        return section;
    }

    /**
     * This method is used to set the array list of section
     *
     * @param section For setting the array list
     */

    public void setSection(ArrayList<Section> section) {
        this.section = section;
    }

    public class Section {

        @SerializedName("is_ppv")
        @Expose
        private int is_ppv = 0;

        /**
         * This method is used to get the PPV details
         *
         * @return is_ppv
         */

        public int getIs_ppv() {
            return is_ppv;
        }

        /**
         * This method is used to set the PPV details
         *
         * @param is_ppv For setting the ppv
         */

        public void setIs_ppv(int is_ppv) {
            this.is_ppv = is_ppv;
        }

        /**
         * This method is used to get the advance details
         *
         * @return is_advance
         */

        public int getIs_advance() {
            return is_advance;
        }

        /**
         * This method is used to set the advance details
         *
         * @param is_advance For setting the advance details
         */

        public void setIs_advance(int is_advance) {
            this.is_advance = is_advance;
        }

        @SerializedName("is_advance")
        @Expose
        private int is_advance = 0;

        @SerializedName("is_episode")
        @Expose
        private String isEpisode = "";
        @SerializedName("movie_stream_uniq_id")
        @Expose
        private String movieStreamUniqId = "";
        @SerializedName("movie_id")
        @Expose
        private String movieId = "";
        @SerializedName("movie_stream_id")
        @Expose
        private String movieStreamId = "";
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
        @SerializedName("censor_rating")
        @Expose
        private String censorRating = "";
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
        @SerializedName("category_id")
        @Expose
        private String categoryId = "";
        @SerializedName("studio_id")
        @Expose
        private String studioId = "";
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
        @SerializedName("rating")
        @Expose
        private String rating = "";
        @SerializedName("review")
        @Expose
        private String review = "";
        @SerializedName("posterForTv")
        @Expose
        private String posterForTv = "";
        @SerializedName("banner")
        @Expose
        private String banner = "";

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
         * This method is used to get the array list of genre
         *
         * @return genre
         */

        public ArrayList<String> getGenre() {
            return genre;
        }

        /**
         * This method is used to set the array list of genre
         *
         * @param genre For setting the genre list
         */

        public void setGenre(ArrayList<String> genre) {
            this.genre = genre;
        }

        /**
         * This method is used to get the censor rating
         *
         * @return censorRating
         */

        public String getCensorRating() {
            return censorRating;
        }

        /**
         * This method is used to set the censor rating
         *
         * @param censorRating For setting the sensor rating
         */

        public void setCensorRating(String censorRating) {
            this.censorRating = censorRating;
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
         * This method is used to get the movie id
         *
         * @return movieid
         */

        public String getMovieid() {
            return movieid;
        }

        /**
         * This method is used to set the movie id
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

        public String getGeocategoryId() {
            return geocategoryId;
        }

        /**
         * This method is used to set the Geo category id
         *
         * @param geocategoryId For setting the geo category id
         */

        public void setGeocategoryId(String geocategoryId) {
            this.geocategoryId = geocategoryId;
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
         * This method is used to get the studio id
         *
         * @return studioId
         */

        public String getStudioId() {
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
         * This method is used to get the IP
         *
         * @return ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * This method is used to set the IP
         *
         * @param ip For setting the IP
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * This method is used to get the poster URL
         *
         * @return posterUrl
         */

        public String getPosterUrl() {
            return posterUrl;
        }

        /**
         * This method is used to set the poster URL
         *
         * @param posterUrl For setting the poster URL
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
         * This method is used to get the Embedded Url
         *
         * @return embeddedUrl
         */

        public String getEmbeddedUrl() {
            return embeddedUrl;
        }

        /**
         * This method is used to set the Embedded Url
         *
         * @param embeddedUrl For setting the Embedded Url
         */

        public void setEmbeddedUrl(String embeddedUrl) {
            this.embeddedUrl = embeddedUrl;
        }

        /**
         * This method is used to get the View status
         *
         * @return viewStatus
         */

        public ViewStatus getViewStatus() {
            return viewStatus;
        }

        /**
         * This method is used to set the View status
         *
         * @param viewStatus For setting the view status
         */

        public void setViewStatus(ViewStatus viewStatus) {
            this.viewStatus = viewStatus;
        }

        /**
         * This method is used to get the rating
         *
         * @return rating
         */

        public String getRating() {
            return rating;
        }

        /**
         * This method is used to set the rating
         *
         * @param rating For setting the rating
         */

        public void setRating(String rating) {
            this.rating = rating;
        }

        /**
         * This method is used to get the review
         *
         * @return review
         */
        public String getReview() {
            return review;
        }

        /**
         * This method is used to set the review
         *
         * @param review For setting the review
         */

        public void setReview(String review) {
            this.review = review;
        }

        /**
         * This method is used to get the poster for tv
         *
         * @return posterForTv
         */
        public String getPosterForTv() {
            return posterForTv;
        }

        /**
         * This method is used to set the poster for tv
         *
         * @param posterForTv For setting the poster for tv
         */
        public void setPosterForTv(String posterForTv) {
            this.posterForTv = posterForTv;
        }

        /**
         * This method is used to get the banner
         *
         * @return banner
         */
        public String getBanner() {
            return banner;
        }

        /**
         * This method is used to set the banner
         *
         * @param banner For setting the banner
         */

        public void setBanner(String banner) {
            this.banner = banner;
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
         * @param uniqViewCount For setting the Unique View Count
         */

        public void setUniqViewCount(String uniqViewCount) {
            this.uniqViewCount = uniqViewCount;
        }

    }
}