package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Search Data
 *
 * @author Abhishek
 */

public class SearchDataModel {
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("search")
    @Expose
    private ArrayList<Search> search = null;
    @SerializedName("limit")
    @Expose
    private String limit = "";
    @SerializedName("offset")
    @Expose
    private Integer offset = 0;
    @SerializedName("item_count")
    @Expose
    private Integer itemCount = 0;
    @SerializedName("Ads")
    @Expose
    private ArrayList<String> ads = null;

    /**
     * This method is used to get the code
     *
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * This method is used to set the code
     *
     * @param code For setting the code
     */
    public void setCode(Integer code) {
        this.code = code;
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
     * This method is used to get the array list of search
     *
     * @return search
     */
    public ArrayList<Search> getSearch() {
        return search;
    }

    /**
     * This method is used to set the array list of search
     *
     * @param search For setting the search result
     */
    public void setSearch(ArrayList<Search> search) {
        this.search = search;
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
     * This method is used to get the Off set
     *
     * @return offset
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * This method is used to set the Off set
     *
     * @param offset For setting the off set
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * This method is used to get the item count
     *
     * @return itemCount
     */
    public Integer getItemCount() {
        return itemCount;
    }

    /**
     * This method is used to set the item count
     *
     * @param itemCount For setting the item count
     */
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
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
     * @param ads For setting the ads
     */
    public void setAds(ArrayList<String> ads) {
        this.ads = ads;
    }

    public class ContentDetails {

        @SerializedName("total_seasons")
        @Expose
        private Integer totalSeasons = 0;
        /* @SerializedName("total_episodes_per_season")
         @Expose
         private TotalEpisodesPerSeason totalEpisodesPerSeason;*/
        @SerializedName("total_episodes")
        @Expose
        private Integer totalEpisodes = 0;

        /**
         * This method is used to get the total season
         *
         * @return totalSeasons
         */
        public Integer getTotalSeasons() {
            return totalSeasons;
        }

        /**
         * This method is used to set the total season
         *
         * @param totalSeasons For setting the total season
         */
        public void setTotalSeasons(Integer totalSeasons) {
            this.totalSeasons = totalSeasons;
        }

        /**
         * This method is used to get the total episode per season
         *
         * @return totalEpisodesPerSeason
         */
      /*  public TotalEpisodesPerSeason getTotalEpisodesPerSeason() {
            return totalEpisodesPerSeason;
        }

        *//**
         * This method is used to set the total episode per season
         *
         * @param totalEpisodesPerSeason For setting the total episode per season
         *//*
        public void setTotalEpisodesPerSeason(TotalEpisodesPerSeason totalEpisodesPerSeason) {
            this.totalEpisodesPerSeason = totalEpisodesPerSeason;
        }
*/

        /**
         * This method is used to get the total episode
         *
         * @return totalEpisodes
         */
        public Integer getTotalEpisodes() {
            return totalEpisodes;
        }

        /**
         * This method is used to set the total episode
         *
         * @param totalEpisodes For setting the total episode
         */
        public void setTotalEpisodes(Integer totalEpisodes) {
            this.totalEpisodes = totalEpisodes;
        }

    }

    /*public class Hdsd {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class IkRaja {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class KestoMin {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class Kieli {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class Ohjaaja {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class POsissa {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }*/

    public class Search {

        public Integer getIs_ppv() {
            return is_ppv;
        }

        public void setIs_ppv(Integer is_ppv) {
            this.is_ppv = is_ppv;
        }

        public Integer getIs_advance() {
            return is_advance;
        }

        public void setIs_advance(Integer is_advance) {
            this.is_advance = is_advance;
        }

        @SerializedName("is_ppv")
        @Expose
        private Integer is_ppv;

        @SerializedName("is_advance")
        @Expose
        private Integer is_advance;

        @SerializedName("movie_id")
        @Expose
        private String movieId = "";
        @SerializedName("content_category_value")
        @Expose
        private String contentCategoryValue = "";
        @SerializedName("is_episode")
        @Expose
        private String isEpisode = "";
        @SerializedName("episode_number")
        @Expose
        private Integer episodeNumber = 0;
        @SerializedName("season_number")
        @Expose
        private Integer seasonNumber = 0;
        @SerializedName("title")
        @Expose
        private String title = "";
        @SerializedName("parent_content_title")
        @Expose
        private String parentContentTitle = "";
        @SerializedName("content_details")
        @Expose
        private ContentDetails contentDetails;
        @SerializedName("content_title")
        @Expose
        private String contentTitle = "";
        @SerializedName("play_btn")
        @Expose
        private String playBtn = "";
        @SerializedName("buy_btn")
        @Expose
        private String buyBtn = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("c_permalink")
        @Expose
        private String cPermalink = "";
        @SerializedName("poster")
        @Expose
        private String poster = "";
        @SerializedName("data_type")
        @Expose
        private Integer dataType = 0;
        @SerializedName("is_landscape")
        @Expose
        private Integer isLandscape = 0;
        @SerializedName("release_date")
        @Expose
        private String releaseDate = "";
        @SerializedName("full_release_date")
        @Expose
        private String fullReleaseDate = "";
        @SerializedName("censor_rating")
        @Expose
        private String censorRating = "";
        @SerializedName("movie_uniq_id")
        @Expose
        private String movieUniqId = "";
        @SerializedName("stream_uniq_id")
        @Expose
        private Integer streamUniqId = 0;
        @SerializedName("video_duration")
        @Expose
        private String videoDuration = "";
        @SerializedName("watch_duration")
        @Expose
        private String watchDuration = "";
        @SerializedName("video_duration_text")
        @Expose
        private String videoDurationText = "";
        @SerializedName("ppv")
        @Expose
        private String ppv = "";
        @SerializedName("payment_type")
        @Expose
        private String paymentType = "";
        @SerializedName("is_converted")
        @Expose
        private Integer isConverted = 0;
        @SerializedName("movie_stream_id")
        @Expose
        private String movieStreamId = "";
        @SerializedName("uniq_id")
        @Expose
        private String uniqId = "";
        @SerializedName("content_type_id")
        @Expose
        private String contentTypeId = "";
        @SerializedName("content_types_id")
        @Expose
        private String contentTypesId = "";
        @SerializedName("ppv_plan_id")
        @Expose
        private String ppvPlanId = "";
        @SerializedName("full_movie")
        @Expose
        private String fullMovie = "";
        @SerializedName("story")
        @Expose
        private String story = "";
        @SerializedName("short_story")
        @Expose
        private String shortStory = "";
        @SerializedName("genre")
        @Expose
        private ArrayList<String> genres = null;
        @SerializedName("display_name")
        @Expose
        private String displayName = "";
        @SerializedName("content_permalink")
        @Expose
        private String contentPermalink = "";
        @SerializedName("trailer_url")
        @Expose
        private String trailerUrl = "";
        @SerializedName("trailer_is_converted")
        @Expose
        private String trailerIsConverted = "";
        @SerializedName("trailer_player")
        @Expose
        private String trailerPlayer = "";
        @SerializedName("casts")
        @Expose
        private ArrayList<String> casts = null;
        @SerializedName("casting")
        @Expose
        private String casting = "";
        @SerializedName("content_banner")
        @Expose
        private String contentBanner = "";
        @SerializedName("reviewformonly")
        @Expose
        private String reviewformonly = "";
        @SerializedName("reviewsummary")
        @Expose
        private String reviewsummary = "";
        @SerializedName("reviews")
        @Expose
        private String reviews = "";
        @SerializedName("myreview")
        @Expose
        private String myreview = "";
        @SerializedName("defaultResolution")
        @Expose
        private Integer defaultResolution = 0;
        @SerializedName("multipleVideo")
        @Expose
        private ArrayList<String> multipleVideo = null;
        @SerializedName("start_time")
        @Expose
        private String startTime = "";
        @SerializedName("duration")
        @Expose
        private String duration = "";
        @SerializedName("custom")
        @Expose
        private ArrayList<String> custom = null;
        @SerializedName("is_downloadable")
        @Expose
        private String isDownloadable = "";
        @SerializedName("download_btn")
        @Expose
        private String downloadBtn = "";
        @SerializedName("content_language")
        @Expose
        private String contentLanguage = "";
        @SerializedName("is_fav_status")
        @Expose
        private Integer isFavStatus = 0;
        @SerializedName("movie_stream_uniq_id")
        @Expose
        private String movieStreamUniqId = "";
        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("booking_status")
        @Expose
        private Integer bookingStatus = 0;
        @SerializedName("show_booking_button")
        @Expose
        private Integer showBookingButton = 0;
        @SerializedName("booking_time")
        @Expose
        private String bookingTime = "";
        /* @SerializedName("ohjaaja")
         @Expose
         private Ohjaaja ohjaaja;
         @SerializedName("kesto_(min)")
         @Expose
         private KestoMin kestoMin;
         @SerializedName("ik\u00e4raja")
         @Expose
         private IkRaja ikRaja;
         @SerializedName("p\u00e4\u00e4osissa")
         @Expose
         private POsissa pOsissa;*/
        /*@SerializedName("valmistusmaa")
        @Expose
        private Valmistusmaa valmistusmaa;*/
        /*@SerializedName("kieli")
        @Expose
        private Kieli kieli;
        @SerializedName("hdsd")
        @Expose
        private Hdsd hdsd;*/
       /* @SerializedName("valmistumisvuosi")
        @Expose
        private Valmistumisvuosi valmistumisvuosi;
        @SerializedName("tekstitys1")
        @Expose
        private Tekstitys1 tekstitys1;*/

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
         * This method is used to get the content category value
         *
         * @return contentCategoryValue
         */
        public String getContentCategoryValue() {
            return contentCategoryValue;
        }

        /**
         * This method is used to set the content category value
         *
         * @param contentCategoryValue For setting the content category value
         */
        public void setContentCategoryValue(String contentCategoryValue) {
            this.contentCategoryValue = contentCategoryValue;
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
         * This method is used to get the episode number
         *
         * @return episodeNumber
         */
        public Integer getEpisodeNumber() {
            return episodeNumber;
        }

        /**
         * This method is used to set the episode number
         *
         * @param episodeNumber For setting the episode number
         */
        public void setEpisodeNumber(Integer episodeNumber) {
            this.episodeNumber = episodeNumber;
        }

        /**
         * This method is used to get the season number
         *
         * @return seasonNumber
         */
        public Integer getSeasonNumber() {
            return seasonNumber;
        }

        /**
         * This method is used to set the season number
         *
         * @param seasonNumber For setting the season number
         */
        public void setSeasonNumber(Integer seasonNumber) {
            this.seasonNumber = seasonNumber;
        }

        /**
         * This method is used to get the title
         *
         * @return title
         */
        public String getTitle() {
            return title;
        }

        /**
         * This method is used to set the title
         *
         * @param title For setting the title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * This method is used to get the parent content title
         *
         * @return parentContentTitle
         */
        public String getParentContentTitle() {
            return parentContentTitle;
        }

        /**
         * This method is used to set the parent content title
         *
         * @param parentContentTitle For setting the parent content title
         */
        public void setParentContentTitle(String parentContentTitle) {
            this.parentContentTitle = parentContentTitle;
        }

        /**
         * This method is used to get the content details
         *
         * @return contentDetails
         */
        public ContentDetails getContentDetails() {
            return contentDetails;
        }

        /**
         * This method is used to set the content details
         *
         * @param contentDetails For setting the content details
         */
        public void setContentDetails(ContentDetails contentDetails) {
            this.contentDetails = contentDetails;
        }

        /**
         * This method is used to get the content title
         *
         * @return contentTitle
         */
        public String getContentTitle() {
            return contentTitle;
        }

        /**
         * This method is used to set the content title
         *
         * @param contentTitle For setting the content details
         */
        public void setContentTitle(String contentTitle) {
            this.contentTitle = contentTitle;
        }

        /**
         * This method is used to get the play button
         *
         * @return playBtn
         */
        public String getPlayBtn() {
            return playBtn;
        }

        /**
         * This method is used to set the play button
         *
         * @param playBtn For setting the play button
         */
        public void setPlayBtn(String playBtn) {
            this.playBtn = playBtn;
        }

        /**
         * This method is used to get the buy button
         *
         * @return buyBtn
         */
        public String getBuyBtn() {
            return buyBtn;
        }

        /**
         * This method is used to set the buy button
         *
         * @param buyBtn For setting the buy button
         */
        public void setBuyBtn(String buyBtn) {
            this.buyBtn = buyBtn;
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

        public String getCPermalink() {
            return cPermalink;
        }

        public void setCPermalink(String cPermalink) {
            this.cPermalink = cPermalink;
        }

        /**
         * This method is used to get the poster
         *
         * @return poster
         */
        public String getPoster() {
            return poster;
        }

        /**
         * This method is used to set the poster
         *
         * @param poster For setting the poster
         */
        public void setPoster(String poster) {
            this.poster = poster;
        }

        /**
         * This method is used to get the data type
         *
         * @return dataType
         */
        public Integer getDataType() {
            return dataType;
        }

        /**
         * This method is used to set the data type
         *
         * @param dataType For setting the data type
         */
        public void setDataType(Integer dataType) {
            this.dataType = dataType;
        }

        /**
         * This method is used to get the landscape mode
         *
         * @return isLandscape
         */
        public Integer getIsLandscape() {
            return isLandscape;
        }

        /**
         * This method is used to set the landscape mode
         *
         * @param isLandscape For setting the landscape mode
         */
        public void setIsLandscape(Integer isLandscape) {
            this.isLandscape = isLandscape;
        }

        /**
         * This method is used to get the Release date
         *
         * @return releaseDate
         */
        public String getReleaseDate() {
            return releaseDate;
        }

        /**
         * This method is used to set the Release date
         *
         * @param releaseDate For setting the release date
         */
        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        /**
         * This method is used to get the full release date
         *
         * @return fullReleaseDate
         */
        public String getFullReleaseDate() {
            return fullReleaseDate;
        }

        /**
         * This method is used to set the full release date
         *
         * @param fullReleaseDate For setting the full release date
         */
        public void setFullReleaseDate(String fullReleaseDate) {
            this.fullReleaseDate = fullReleaseDate;
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
         * @param censorRating For setting the censor rating
         */
        public void setCensorRating(String censorRating) {
            this.censorRating = censorRating;
        }

        /**
         * This method is used to get the movie unique id
         *
         * @return movieUniqId
         */
        public String getMovieUniqId() {
            return movieUniqId;
        }

        /**
         * This method is used to set the movie unique id
         *
         * @param movieUniqId For setting the movie unique id
         */
        public void setMovieUniqId(String movieUniqId) {
            this.movieUniqId = movieUniqId;
        }

        /**
         * This method is used to get the stream unique id
         *
         * @return streamUniqId
         */
        public Integer getStreamUniqId() {
            return streamUniqId;
        }

        /**
         * This method is used to set the stream unique id
         *
         * @param streamUniqId For setting the stream unique id
         */
        public void setStreamUniqId(Integer streamUniqId) {
            this.streamUniqId = streamUniqId;
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
         * This method is used to get the video duration text
         *
         * @return videoDurationText
         */
        public String getVideoDurationText() {
            return videoDurationText;
        }

        /**
         * This method is used to set the video duration text
         *
         * @param videoDurationText For setting the video duration text
         */
        public void setVideoDurationText(String videoDurationText) {
            this.videoDurationText = videoDurationText;
        }

        /**
         * This method is used to get the ppv details
         *
         * @return ppv
         */
        public String getPpv() {
            return ppv;
        }

        /**
         * This method is used to set the ppv details
         *
         * @param ppv For setting the ppv details
         */
        public void setPpv(String ppv) {
            this.ppv = ppv;
        }

        /**
         * This method is used to get the payment type
         *
         * @return paymentType
         */
        public String getPaymentType() {
            return paymentType;
        }

        /**
         * This method is used to set the payment type
         *
         * @param paymentType For setting the payment
         */
        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
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
         * This method is used to get the unique id
         *
         * @return uniqId
         */
        public String getUniqId() {
            return uniqId;
        }

        /**
         * This method is used to set the unique id
         *
         * @param uniqId For setting the unique id
         */
        public void setUniqId(String uniqId) {
            this.uniqId = uniqId;
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
         * This method is used to get the content types id
         *
         * @return contentTypesId
         */
        public String getContentTypesId() {
            return contentTypesId;
        }

        /**
         * This method is used to set the content types id
         *
         * @param contentTypesId For setting the content types id
         */
        public void setContentTypesId(String contentTypesId) {
            this.contentTypesId = contentTypesId;
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
         * This method is used to get the short story
         *
         * @return shortStory
         */
        public String getShortStory() {
            return shortStory;
        }

        /**
         * This method is used to set the short story
         *
         * @param shortStory For setting the short story
         */
        public void setShortStory(String shortStory) {
            this.shortStory = shortStory;
        }

        /**
         * This method is used to get the array list of genres
         *
         * @return genres
         */
        public ArrayList<String> getGenres() {
            return genres;
        }

        /**
         * This method is used to set the array list of genres
         *
         * @param genres For setting the genres
         */
        public void setGenres(ArrayList<String> genres) {
            this.genres = genres;
        }

        /**
         * This method is used to get the display name
         *
         * @return displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * This method is used to set the display name
         *
         * @param displayName For setting the display name
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        /**
         * This method is used to get the content permalink
         *
         * @return contentPermalink
         */
        public String getContentPermalink() {
            return contentPermalink;
        }

        /**
         * This method is used to set the content permalink
         *
         * @param contentPermalink For setting the content permalink
         */
        public void setContentPermalink(String contentPermalink) {
            this.contentPermalink = contentPermalink;
        }

        /**
         * This method is used to get the trailer url
         *
         * @return trailerUrl
         */
        public String getTrailerUrl() {
            return trailerUrl;
        }

        /**
         * This method is used to set the trailer url
         *
         * @param trailerUrl For setting the trailer url
         */
        public void setTrailerUrl(String trailerUrl) {
            this.trailerUrl = trailerUrl;
        }

        /**
         * This method is used to get the trailer converted details
         *
         * @return trailerIsConverted
         */
        public String getTrailerIsConverted() {
            return trailerIsConverted;
        }

        /**
         * This method is used to set the trailer converted details
         *
         * @param trailerIsConverted For setting the trailer converted details
         */
        public void setTrailerIsConverted(String trailerIsConverted) {
            this.trailerIsConverted = trailerIsConverted;
        }

        /**
         * This method is used to get the trailer player
         *
         * @return trailerPlayer
         */
        public String getTrailerPlayer() {
            return trailerPlayer;
        }

        /**
         * This method is used to set the trailer player
         *
         * @param trailerPlayer For setting the trailer player
         */
        public void setTrailerPlayer(String trailerPlayer) {
            this.trailerPlayer = trailerPlayer;
        }

        /**
         * This method is used to get the casts
         *
         * @return casts
         */
        public ArrayList<String> getCasts() {
            return casts;
        }

        /**
         * This method is used to set the casts
         *
         * @param casts For setting the casts
         */
        public void setCasts(ArrayList<String> casts) {
            this.casts = casts;
        }

        /**
         * This method is used to get the casting
         *
         * @return casting
         */
        public String getCasting() {
            return casting;
        }

        /**
         * This method is used to set the casting
         *
         * @param casting For setting the casting
         */
        public void setCasting(String casting) {
            this.casting = casting;
        }

        /**
         * This method is used to get the content banner
         *
         * @return contentBanner
         */
        public String getContentBanner() {
            return contentBanner;
        }

        /**
         * This method is used to set the content banner
         *
         * @param contentBanner For setting the content banner
         */
        public void setContentBanner(String contentBanner) {
            this.contentBanner = contentBanner;
        }

        /**
         * This method is used to get the review form only
         *
         * @return reviewformonly
         */
        public String getReviewformonly() {
            return reviewformonly;
        }

        /**
         * This method is used to set the review form only
         *
         * @param reviewformonly For setting the review form only
         */
        public void setReviewformonly(String reviewformonly) {
            this.reviewformonly = reviewformonly;
        }

        /**
         * This method is used to get the review summary
         *
         * @return reviewsummary
         */
        public String getReviewsummary() {
            return reviewsummary;
        }

        /**
         * This method is used to set the review summary
         *
         * @param reviewsummary For setting the review summary
         */
        public void setReviewsummary(String reviewsummary) {
            this.reviewsummary = reviewsummary;
        }

        /**
         * This method is used to get the reviews
         *
         * @return reviews
         */
        public String getReviews() {
            return reviews;
        }

        /**
         * This method is used to set the reviews
         *
         * @param reviews For setting the review
         */
        public void setReviews(String reviews) {
            this.reviews = reviews;
        }

        /**
         * This method is used to get the my reviews
         *
         * @return myreview
         */
        public String getMyreview() {
            return myreview;
        }

        /**
         * This method is used to set the my reviews
         *
         * @param myreview For setting the my review
         */
        public void setMyreview(String myreview) {
            this.myreview = myreview;
        }

        /**
         * This method is used to get the default resolution
         *
         * @return defaultResolution
         */
        public Integer getDefaultResolution() {
            return defaultResolution;
        }

        /**
         * This method is used to set the default resolution
         *
         * @param defaultResolution For setting the default resolution
         */
        public void setDefaultResolution(Integer defaultResolution) {
            this.defaultResolution = defaultResolution;
        }

        /**
         * This method is used to get the array list of multiple video
         *
         * @return multipleVideo
         */
        public ArrayList<String> getMultipleVideo() {
            return multipleVideo;
        }

        /**
         * This method is used to set the array list of multiple video
         *
         * @param multipleVideo For setting the multiple video
         */
        public void setMultipleVideo(ArrayList<String> multipleVideo) {
            this.multipleVideo = multipleVideo;
        }

        /**
         * This method is used to get the start time
         *
         * @return startTime
         */
        public String getStartTime() {
            return startTime;
        }

        /**
         * '
         * This method is used to set the start time
         *
         * @param startTime For setting the start time
         */
        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        /**
         * This method is used to get the duration
         *
         * @return duration
         */
        public String getDuration() {
            return duration;
        }

        /**
         * This method is used to set the duration
         *
         * @param duration For setting the duration
         */
        public void setDuration(String duration) {
            this.duration = duration;
        }

        /**
         * This method is used to get the array list of custom
         *
         * @return custom
         */
        public ArrayList<String> getCustom() {
            return custom;
        }

        /**
         * This method is used to \set the array list of custom
         *
         * @param custom For setting the custom
         */
        public void setCustom(ArrayList<String> custom) {
            this.custom = custom;
        }

        /**
         * This method is used to get the downloadable details
         *
         * @return isDownloadable
         */
        public String getIsDownloadable() {
            return isDownloadable;
        }

        /**
         * This method is used to set the downloadable details
         *
         * @param isDownloadable For setting the download details
         */
        public void setIsDownloadable(String isDownloadable) {
            this.isDownloadable = isDownloadable;
        }

        /**
         * This method is used to get the downloadable button
         *
         * @return downloadBtn
         */
        public String getDownloadBtn() {
            return downloadBtn;
        }

        /**
         * This method is used to set the downloadable button
         *
         * @param downloadBtn For setting the download buttons
         */
        public void setDownloadBtn(String downloadBtn) {
            this.downloadBtn = downloadBtn;
        }

        /**
         * This method is used to get the content language
         *
         * @return contentLanguage
         */
        public String getContentLanguage() {
            return contentLanguage;
        }

        /**
         * This method is used to set the content language
         *
         * @param contentLanguage For setting the content language
         */
        public void setContentLanguage(String contentLanguage) {
            this.contentLanguage = contentLanguage;
        }

        /**
         * This method is used to get the favorite status
         *
         * @return isFavStatus
         */
        public Integer getIsFavStatus() {
            return isFavStatus;
        }

        /**
         * This method is used to set the favorite status
         *
         * @param isFavStatus For setting the favorite status
         */
        public void setIsFavStatus(Integer isFavStatus) {
            this.isFavStatus = isFavStatus;
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
         * This method is used to get the booking status
         *
         * @return bookingStatus
         */
        public Integer getBookingStatus() {
            return bookingStatus;
        }

        /**
         * This method is used to set the booking status
         *
         * @param bookingStatus For setting the booking status
         */
        public void setBookingStatus(Integer bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        /**
         * This method is used to get the booking button
         *
         * @return showBookingButton
         */
        public Integer getShowBookingButton() {
            return showBookingButton;
        }

        /**
         * This method is used to set the booking button
         *
         * @param showBookingButton For setting the booking button
         */
        public void setShowBookingButton(Integer showBookingButton) {
            this.showBookingButton = showBookingButton;
        }

        /**
         * This method is used to get the booking time
         *
         * @return bookingTime
         */

        public String getBookingTime() {
            return bookingTime;
        }

        /**
         * This method is used to set the booking time
         *
         * @param bookingTime For setting the booking time
         */
        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

      /*  public Ohjaaja getOhjaaja() {
            return ohjaaja;
        }

        public void setOhjaaja(Ohjaaja ohjaaja) {
            this.ohjaaja = ohjaaja;
        }

        public KestoMin getKestoMin() {
            return kestoMin;
        }

        public void setKestoMin(KestoMin kestoMin) {
            this.kestoMin = kestoMin;
        }

        public IkRaja getIkRaja() {
            return ikRaja;
        }

        public void setIkRaja(IkRaja ikRaja) {
            this.ikRaja = ikRaja;
        }

        public POsissa getPOsissa() {
            return pOsissa;
        }

        public void setPOsissa(POsissa pOsissa) {
            this.pOsissa = pOsissa;
        }*/

      /*  public Valmistusmaa getValmistusmaa() {
            return valmistusmaa;
        }

        public void setValmistusmaa(Valmistusmaa valmistusmaa) {
            this.valmistusmaa = valmistusmaa;
        }

        *//*public Kieli getKieli() {
            return kieli;
        }

        public void setKieli(Kieli kieli) {
            this.kieli = kieli;
        }

        public Hdsd getHdsd() {
            return hdsd;
        }

        public void setHdsd(Hdsd hdsd) {
            this.hdsd = hdsd;
        }*//*

        public Valmistumisvuosi getValmistumisvuosi() {
            return valmistumisvuosi;
        }

        public void setValmistumisvuosi(Valmistumisvuosi valmistumisvuosi) {
            this.valmistumisvuosi = valmistumisvuosi;
        }

        public Tekstitys1 getTekstitys1() {
            return tekstitys1;
        }

        public void setTekstitys1(Tekstitys1 tekstitys1) {
            this.tekstitys1 = tekstitys1;
        }

    }*/

  /*  public class Tekstitys1 {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class TotalEpisodesPerSeason {

        @SerializedName("1")
        @Expose
        private String _1 = "";

        public String get1() {
            return _1;
        }

        public void set1(String _1) {
            this._1 = _1;
        }

    }

    public class Valmistumisvuosi {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }

    public class Valmistusmaa {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName = "";
        @SerializedName("field_value")
        @Expose
        private String fieldValue = "";

        public String getFieldDisplayName() {
            return fieldDisplayName;
        }

        public void setFieldDisplayName(String fieldDisplayName) {
            this.fieldDisplayName = fieldDisplayName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

    }*/
    }
}
