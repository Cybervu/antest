package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public class GetContentDetailsList {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("movie")
    @Expose
    private Movie movie;
    @SerializedName("comments")
    @Expose
    private ArrayList<String> comments = null;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("epDetails")
    @Expose
    private EpDetails epDetails;
    @SerializedName("seasons")
    @Expose
    private ArrayList<String> seasons = null;

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    @SerializedName("item_count")
    @Expose
    private String item_count;

    /**
     * This method is used for getting the code
     *
     * @return code
     */

    public Integer getCode() {
        return code;
    }

    /**
     * This method is used for setting the code
     *
     * @param code Server request code
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
     * This method is used to get the movie
     *
     * @return movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * This method is used to set the movie
     *
     * @param movie For setting the movie
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * This method is used to get the array list of comments
     *
     * @return comments
     */
    public ArrayList<String> getComments() {
        return comments;
    }

    /**
     * This method is used to set the array list of comments
     *
     * @param comments For setting the comment list
     */
    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
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
     * This method is used to get the episode details
     *
     * @return epDetails
     */
    public EpDetails getEpDetails() {
        return epDetails;
    }

    /**
     * This method is used to set the episode details
     *
     * @param epDetails For setting the episode details
     */

    public void setEpDetails(EpDetails epDetails) {
        this.epDetails = epDetails;
    }

    /**
     * This method is used to get the array list of seasons
     *
     * @return seasons
     */
    public ArrayList<String> getSeasons() {
        return seasons;
    }

    /**
     * This method is used to set the array list of seasons
     *
     * @param seasons For setting the season list
     */
    public void setSeasons(ArrayList<String> seasons) {
        this.seasons = seasons;
    }

    public class Movie {

        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("content_types_id")
        @Expose
        private String contentTypesId = "";
        @SerializedName("movie_stream_id")
        @Expose
        private String movieStreamId = "";
        @SerializedName("content_publish_date")
        @Expose
        private String contentPublishDate = "";
        @SerializedName("full_movie")
        @Expose
        private String fullMovie = "";
        @SerializedName("is_converted")
        @Expose
        private String isConverted = "";
        @SerializedName("video_resolution")
        @Expose
        private String videoResolution = "";
        @SerializedName("movie_stream_uniq_id")
        @Expose
        private String movieStreamUniqId = "";
        @SerializedName("muvi_uniq_id")
        @Expose
        private String muviUniqId = "";
        @SerializedName("ppv_plan_id")
        @Expose
        private String ppvPlanId = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("content_type_id")
        @Expose
        private String contentTypeId = "";
        @SerializedName("genre")
        @Expose
        private String genre = "";
        @SerializedName("release_date")
        @Expose
        private String releaseDate = "";
        @SerializedName("censor_rating")
        @Expose
        private String censorRating = "";
        @SerializedName("story")
        @Expose
        private String story = "";
        @SerializedName("rolltype")
        @Expose
        private String rolltype = "";
        @SerializedName("roll_after")
        @Expose
        private String rollAfter = "";
        @SerializedName("video_duration")
        @Expose
        private String videoDuration = "";
        @SerializedName("thirdparty_url")
        @Expose
        private String thirdpartyUrl = "";
        @SerializedName("is_episode")
        @Expose
        private String isEpisode = "";
        @SerializedName("custom1")
        @Expose
        private String custom1 = "";
        @SerializedName("custom2")
        @Expose
        private String custom2 = "";
        @SerializedName("custom3")
        @Expose
        private String custom3 = "";
        @SerializedName("custom4")
        @Expose
        private String custom4 = "";
        @SerializedName("custom5")
        @Expose
        private String custom5 = "";
        @SerializedName("custom6")
        @Expose
        private String custom6 = "";
        @SerializedName("custom7")
        @Expose
        private String custom7 = "";
        @SerializedName("custom8")
        @Expose
        private String custom8 = "";
        @SerializedName("custom9")
        @Expose
        private String custom9 = "";
        @SerializedName("custom10")
        @Expose
        private String custom10 = "";
        @SerializedName("content_language")
        @Expose
        private String contentLanguage = "";
        @SerializedName("is_downloadable")
        @Expose
        private String isDownloadable = "";
        @SerializedName("custom_metadata_form_id")
        @Expose
        private String customMetadataFormId = "";
        @SerializedName("isFreeContent")
        @Expose
        private Integer isFreeContent = 0;
        @SerializedName("is_ppv")
        @Expose
        private Integer isPpv = 0;
        @SerializedName("is_advance")
        @Expose
        private Integer isAdvance = 0;
        @SerializedName("actor")
        @Expose
        private String actor = "";
        @SerializedName("director")
        @Expose
        private String director = "";
        @SerializedName("cast_detail")
        @Expose
        private ArrayList<CastDetail> castDetail = null;
        @SerializedName("embeddedUrl")
        @Expose
        private String embeddedUrl = "";
        @SerializedName("viewStatus")
        @Expose
        private ViewStatus viewStatus;
        @SerializedName("movieUrlForTv")
        @Expose
        private String movieUrlForTv = "";
        @SerializedName("movieUrl")
        @Expose
        private String movieUrl = "";
        /*@SerializedName("resolution")
        @Expose
        private String resolution = "";*/
        @SerializedName("banner")
        @Expose
        private String banner = "";
        @SerializedName("poster")
        @Expose
        private String poster = "";
        @SerializedName("adDetails")
        @Expose
        private ArrayList<String> adDetails = null;
        @SerializedName("trailerThirdpartyUrl")
        @Expose
        private String trailerThirdpartyUrl = "";
        @SerializedName("trailerUrl")
        @Expose
        private String trailerUrl = "";
        @SerializedName("embedTrailerUrl")
        @Expose
        private String embedTrailerUrl = "";
        /*@SerializedName("custom_meta_data")
        @Expose
        private ArrayList<String> customMetaData = null;*/

        @SerializedName("ppv_pricing")
        @Expose
        private PpvPricing ppvPricing=null;

        public AdvPricing getAdv_pricing() {
            return adv_pricing;
        }

        public void setAdv_pricing(AdvPricing adv_pricing) {
            this.adv_pricing = adv_pricing;
        }



        @SerializedName("adv_pricing")
        @Expose
        private AdvPricing adv_pricing=null;

        public PpvPricing getPpvPricing() {
            return ppvPricing;
        }

        public void setPpvPricing(PpvPricing ppvPricing) {
            this.ppvPricing = ppvPricing;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        @SerializedName("currency")
        @Expose
        private Currency currency=null;

        public String getIs_favorite() {
            return is_favorite;
        }

        public void setIs_favorite(String is_favorite) {
            this.is_favorite = is_favorite;
        }

        @SerializedName("is_favorite")
        @Expose
        private String is_favorite;

        /**
         * This method is used to get the id
         *
         * @return id
         */
        public String getId() {
            return id;
        }

        /**
         * This method is used to set the id
         *
         * @param id For setting the id
         */
        public void setId(String id) {
            this.id = id;
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
         * This method is used to get the content publish date
         *
         * @return contentPublishDate
         */
        public String getContentPublishDate() {
            return contentPublishDate;
        }

        /**
         * This method is used to set the content publish date
         *
         * @param contentPublishDate For setting the content publish date
         */
        public void setContentPublishDate(String contentPublishDate) {
            this.contentPublishDate = contentPublishDate;
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
         * This method is used to get the converted details
         *
         * @return isConverted
         */
        public String getIsConverted() {
            return isConverted;
        }

        /**
         * This method is used to set the converted details
         *
         * @param isConverted For setting the converted details
         */
        public void setIsConverted(String isConverted) {
            this.isConverted = isConverted;
        }

        /**
         * This method is used to get the video resolution
         *
         * @return videoResolution
         */
        public String getVideoResolution() {
            return videoResolution;
        }

        /**
         * This method is used to set the video resolution
         *
         * @param videoResolution For setting the video resolution
         */

        public void setVideoResolution(String videoResolution) {
            this.videoResolution = videoResolution;
        }

        /**
         * This method is used to get the movie unique stream id
         *
         * @return movieStreamUniqId
         */

        public String getMovieStreamUniqId() {
            return movieStreamUniqId;
        }

        /**
         * This method is used to set the movie unique stream id
         *
         * @param movieStreamUniqId For setting the movie unique id
         */

        public void setMovieStreamUniqId(String movieStreamUniqId) {
            this.movieStreamUniqId = movieStreamUniqId;
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
         * This method is used to get the genre
         *
         * @return genre
         */
        public String getGenre() {
            return genre;
        }

        /**
         * This method is used to set the genre
         *
         * @param genre For setting the genre
         */
        public void setGenre(String genre) {
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
         * This method is used to get the roll type
         *
         * @return rolltype
         */

        public String getRolltype() {
            return rolltype;
        }

        /**
         * This method is used to set the roll type
         *
         * @param rolltype For setting the roll type
         */

        public void setRolltype(String rolltype) {
            this.rolltype = rolltype;
        }

        /**
         * This method is used to get the roll after
         *
         * @return rollAfter
         */
        public String getRollAfter() {
            return rollAfter;
        }

        /**
         * This method is used to set the roll after
         *
         * @param rollAfter For setting the roll after
         */
        public void setRollAfter(String rollAfter) {
            this.rollAfter = rollAfter;
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
         * This method is used to get the third party url
         *
         * @return thirdpartyUrl
         */
        public String getThirdpartyUrl() {
            return thirdpartyUrl;
        }

        /**
         * This method is used to set the third party url
         *
         * @param thirdpartyUrl For setting the thord party url
         */
        public void setThirdpartyUrl(String thirdpartyUrl) {
            this.thirdpartyUrl = thirdpartyUrl;
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

        /*public String getCustom1() {
            return custom1;
        }

        public void setCustom1(String custom1) {
            this.custom1 = custom1;
        }

        public String getCustom2() {
            return custom2;
        }

        public void setCustom2(String custom2) {
            this.custom2 = custom2;
        }

        public String getCustom3() {
            return custom3;
        }

        public void setCustom3(String custom3) {
            this.custom3 = custom3;
        }

        public String getCustom4() {
            return custom4;
        }

        public void setCustom4(String custom4) {
            this.custom4 = custom4;
        }

        public String getCustom5() {
            return custom5;
        }

        public void setCustom5(String custom5) {
            this.custom5 = custom5;
        }

        public String getCustom6() {
            return custom6;
        }

        public void setCustom6(String custom6) {
            this.custom6 = custom6;
        }

        public String getCustom7() {
            return custom7;
        }

        public void setCustom7(String custom7) {
            this.custom7 = custom7;
        }

        public String getCustom8() {
            return custom8;
        }

        public void setCustom8(String custom8) {
            this.custom8 = custom8;
        }

        public String getCustom9() {
            return custom9;
        }

        public void setCustom9(String custom9) {
            this.custom9 = custom9;
        }

        public String getCustom10() {
            return custom10;
        }

        public void setCustom10(String custom10) {
            this.custom10 = custom10;
        }*/

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
         * @param isDownloadable For setting the downloadable details
         */
        public void setIsDownloadable(String isDownloadable) {
            this.isDownloadable = isDownloadable;
        }

        /**
         * This method is used to get the custom meta data
         *
         * @return customMetadataFormId
         */
        public String getCustomMetadataFormId() {
            return customMetadataFormId;
        }

        /**
         * This method is used to set the custom meta data
         *
         * @param customMetadataFormId For setting the custom meta data
         */

        public void setCustomMetadataFormId(String customMetadataFormId) {
            this.customMetadataFormId = customMetadataFormId;
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
         * This method is used to get the ppv details
         *
         * @return isPpv
         */
        public Integer getIsPpv() {
            return isPpv;
        }

        /**
         * This method is used to set the ppv details
         *
         * @param isPpv For setting the ppv details
         */

        public void setIsPpv(Integer isPpv) {
            this.isPpv = isPpv;
        }

        /**
         * This method is used to get the advance details
         *
         * @return isAdvance
         */
        public Integer getIsAdvance() {
            return isAdvance;
        }

        /**
         * This method is used to set the advance details
         *
         * @param isAdvance For setting the advance details
         */
        public void setIsAdvance(Integer isAdvance) {
            this.isAdvance = isAdvance;
        }

        /**
         * This method is used to get the actors
         *
         * @return actor
         */
        public String getActor() {
            return actor;
        }

        /**
         * This method is used to set the actors
         *
         * @param actor For setting the actors
         */
        public void setActor(String actor) {
            this.actor = actor;
        }

        /**
         * This method is used to get the director
         *
         * @return director
         */
        public String getDirector() {
            return director;
        }

        /**
         * This method is used to set the director
         *
         * @param director For setting the director
         */
        public void setDirector(String director) {
            this.director = director;
        }

        /**
         * This method is used to get the array list of cast details
         *
         * @return castDetail
         */
        public ArrayList<CastDetail> getCastDetail() {
            return castDetail;
        }

        /**
         * This method is used to set the array list of cast details
         *
         * @param castDetail For setting the cast details
         */

        public void setCastDetail(ArrayList<CastDetail> castDetail) {
            this.castDetail = castDetail;
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
         * @param embeddedUrl For setting the embedded url
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

        /**
         * This method is used to get the movie url for tv
         *
         * @return movieUrlForTv
         */
        public String getMovieUrlForTv() {
            return movieUrlForTv;
        }

        /**
         * This method is used to set the movie url for tv
         *
         * @param movieUrlForTv For etting the movie url for tv
         */

        public void setMovieUrlForTv(String movieUrlForTv) {
            this.movieUrlForTv = movieUrlForTv;
        }

        /**
         * This method is used to get the movie url
         *
         * @return movieUrl
         */
        public String getMovieUrl() {
            return movieUrl;
        }

        /**
         * This method is used to set the movie url
         *
         * @param movieUrl For setting the movie url
         */

        public void setMovieUrl(String movieUrl) {
            this.movieUrl = movieUrl;
        }

       /* *//**
         * This method is used to get the resolution
         *
         * @return resolution
         *//*
        public String getResolution() {
            return resolution;
        }

        *//**
         * This method is used to set the resolution
         *
         * @param resolution For setting the resolution
         *//*

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }*/

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
         * This method is used to get the array list of ad details
         *
         * @return adDetails
         */
        public ArrayList<String> getAdDetails() {
            return adDetails;
        }

        /**
         * This method is used to set the array list of ad details
         *
         * @param adDetails For setting the array list of ad details
         */

        public void setAdDetails(ArrayList<String> adDetails) {
            this.adDetails = adDetails;
        }

        /**
         * This method is used to get the trailer third party url
         *
         * @return trailerThirdpartyUrl
         */
        public String getTrailerThirdpartyUrl() {
            return trailerThirdpartyUrl;
        }

        /**
         * This method is used to set the trailer third party url
         *
         * @param trailerThirdpartyUrl For setting the trailer third party url
         */

        public void setTrailerThirdpartyUrl(String trailerThirdpartyUrl) {
            this.trailerThirdpartyUrl = trailerThirdpartyUrl;
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
         * This method is used to get the embedded trailer url
         *
         * @return embedTrailerUrl
         */
        public String getEmbedTrailerUrl() {
            return embedTrailerUrl;
        }

        /**
         * This method is used to set the embedded trailer url
         *
         * @param embedTrailerUrl For setting the embedded trailer url
         */
        public void setEmbedTrailerUrl(String embedTrailerUrl) {
            this.embedTrailerUrl = embedTrailerUrl;
        }

        /**
         * This method is used to get the array list of custom meta data
         *
         * @return customMetaData
         */
       /* public ArrayList<String> getCustomMetaData() {
            return customMetaData;
        }

        *//**
         * This method is used to set the array list of custom meta data
         *
         * @param customMetaData For setting the custom meta data
         *//*
        public void setCustomMetaData(ArrayList<String> customMetaData) {
            this.customMetaData = customMetaData;
        }*/
    }

    public class CastDetail {

        @SerializedName("celeb_image")
        @Expose
        private String celebImage = "";
        @SerializedName("celeb_name")
        @Expose
        private String celebName = "";
        @SerializedName("celeb_id")
        @Expose
        private String celebId = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("cast_type")
        @Expose
        private String castType = "";

        /**
         * This method is used to get the celebrity image
         *
         * @return celebImage
         */
        public String getCelebImage() {
            return celebImage;
        }

        /**
         * This method is used to set the celebrity image
         *
         * @param celebImage For setting the celebrity image
         */

        public void setCelebImage(String celebImage) {
            this.celebImage = celebImage;
        }

        /**
         * This method is used to get the celebrity name
         *
         * @return celebName
         */

        public String getCelebName() {
            return celebName;
        }

        /**
         * This method is used to set the celebrity name
         *
         * @param celebName For setting the celebrity name
         */

        public void setCelebName(String celebName) {
            this.celebName = celebName;
        }

        /**
         * This method is used to get the celebrity id
         *
         * @return celebId
         */
        public String getCelebId() {
            return celebId;
        }

        /**
         * This method is used to set the celebrity id
         *
         * @param celebId For setting the celebrity id
         */
        public void setCelebId(String celebId) {
            this.celebId = celebId;
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
         * This method is used to get the cast type
         *
         * @return castType
         */
        public String getCastType() {
            return castType;
        }

        /**
         * This method is used to set the cast type
         *
         * @param castType For setting the cast type
         */
        public void setCastType(String castType) {
            this.castType = castType;
        }

    }

    public class EpDetails {

        @SerializedName("total_series")
        @Expose
        private String totalSeries = "";
        @SerializedName("series_number")
        @Expose
        private String seriesNumber = "";

        /**
         * This method is used to get the total series
         *
         * @return totalSeries
         */
        public String getTotalSeries() {
            return totalSeries;
        }

        /**
         * This method is used to set the total series
         *
         * @param totalSeries For setting the total series
         */
        public void setTotalSeries(String totalSeries) {
            this.totalSeries = totalSeries;
        }

        /**
         * This method is used to get the series number
         *
         * @return seriesNumber
         */

        public String getSeriesNumber() {
            return seriesNumber;
        }

        /**
         * This method is used to get the series number
         *
         * @param seriesNumber For setting the series number
         */

        public void setSeriesNumber(String seriesNumber) {
            this.seriesNumber = seriesNumber;
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
         * This method is used to get the unique view count
         *
         * @return uniqViewCount
         */
        public String getUniqViewCount() {
            return uniqViewCount;
        }

        /**
         * This method is used to set the unique view count
         *
         * @param uniqViewCount For setting the unique view count
         */

        public void setUniqViewCount(String uniqViewCount) {
            this.uniqViewCount = uniqViewCount;
        }

    }

}
