package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *This Model Class Holds All The Attributes For Featured Content
 *
 * @author Abhishek
 */

public class GetFeaturedContentModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("section")
    @Expose
    private ArrayList<Section> section = null;
    @SerializedName("title")
    @Expose
    private String title = "";

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Section> getSection() {
        return section;
    }

    public void setSection(ArrayList<Section> section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public class Section {

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
        private String isConverted = "";
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

        public String getIsEpisode() {
            return isEpisode;
        }

        public void setIsEpisode(String isEpisode) {
            this.isEpisode = isEpisode;
        }

        public String getMovieStreamUniqId() {
            return movieStreamUniqId;
        }

        public void setMovieStreamUniqId(String movieStreamUniqId) {
            this.movieStreamUniqId = movieStreamUniqId;
        }

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getMovieStreamId() {
            return movieStreamId;
        }

        public void setMovieStreamId(String movieStreamId) {
            this.movieStreamId = movieStreamId;
        }

        public String getMuviUniqId() {
            return muviUniqId;
        }

        public void setMuviUniqId(String muviUniqId) {
            this.muviUniqId = muviUniqId;
        }

        public String getContentTypeId() {
            return contentTypeId;
        }

        public void setContentTypeId(String contentTypeId) {
            this.contentTypeId = contentTypeId;
        }

        public String getPpvPlanId() {
            return ppvPlanId;
        }

        public void setPpvPlanId(String ppvPlanId) {
            this.ppvPlanId = ppvPlanId;
        }

        public String getPermalink() {
            return permalink;
        }

        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullMovie() {
            return fullMovie;
        }

        public void setFullMovie(String fullMovie) {
            this.fullMovie = fullMovie;
        }

        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

        public ArrayList<String> getGenre() {
            return genre;
        }

        public void setGenre(ArrayList<String> genre) {
            this.genre = genre;
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

        public String getContentTypesId() {
            return contentTypesId;
        }

        public void setContentTypesId(String contentTypesId) {
            this.contentTypesId = contentTypesId;
        }

        public String getIsConverted() {
            return isConverted;
        }

        public void setIsConverted(String isConverted) {
            this.isConverted = isConverted;
        }

        public String getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        public void setLastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        public String getMovieid() {
            return movieid;
        }

        public void setMovieid(String movieid) {
            this.movieid = movieid;
        }

        public String getGeocategoryId() {
            return geocategoryId;
        }

        public void setGeocategoryId(String geocategoryId) {
            this.geocategoryId = geocategoryId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getStudioId() {
            return studioId;
        }

        public void setStudioId(String studioId) {
            this.studioId = studioId;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public Integer getIsFreeContent() {
            return isFreeContent;
        }

        public void setIsFreeContent(Integer isFreeContent) {
            this.isFreeContent = isFreeContent;
        }

        public String getEmbeddedUrl() {
            return embeddedUrl;
        }

        public void setEmbeddedUrl(String embeddedUrl) {
            this.embeddedUrl = embeddedUrl;
        }

        public ViewStatus getViewStatus() {
            return viewStatus;
        }

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

        public String getViewcount() {
            return viewcount;
        }

        public void setViewcount(String viewcount) {
            this.viewcount = viewcount;
        }

        public String getUniqViewCount() {
            return uniqViewCount;
        }

        public void setUniqViewCount(String uniqViewCount) {
            this.uniqViewCount = uniqViewCount;
        }

    }
}
