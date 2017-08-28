package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 9/29/2016.
 */
public class Episode_Details_output {
    ArrayList<Episode> EpisodeArray = new ArrayList<>();
    private String name,code,msg,muvi_uniq_id,permalink,item_count,limit,offset;
    int is_ppv;
    APVModel apvDetails;
    PPVModel ppvDetails;
    CurrencyModel currencyDetails;

    public int getIs_ppv() {
        return is_ppv;
    }

    public void setIs_ppv(int is_ppv) {
        this.is_ppv = is_ppv;
    }



    public ArrayList<Episode> getEpisodeArray() {
        return EpisodeArray;
    }

    public void setEpisodeArray(ArrayList<Episode> episodeArray) {
        EpisodeArray = episodeArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMuvi_uniq_id() {
        return muvi_uniq_id;
    }

    public void setMuvi_uniq_id(String muvi_uniq_id) {
        this.muvi_uniq_id = muvi_uniq_id;
    }



    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

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

    public class Episode{
    String id,movie_stream_uniq_id="",full_movie="",episode_number="",video_resolution="",episode_title="",series_number="",episode_date="",
            episode_story="",video_url="",thirdparty_url="",rolltype="",roll_after="",video_duration="",ikäraja="",embeddedUrl="",poster_url="",movieUrlForTv="";

        public int getContent_types_id() {
            return content_types_id;
        }

        public void setContent_types_id(int content_types_id) {
            this.content_types_id = content_types_id;
        }

        int content_types_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_stream_uniq_id() {
        return movie_stream_uniq_id;
    }

    public void setMovie_stream_uniq_id(String movie_stream_uniq_id) {
        this.movie_stream_uniq_id = movie_stream_uniq_id;
    }

    public String getFull_movie() {
        return full_movie;
    }

    public void setFull_movie(String full_movie) {
        this.full_movie = full_movie;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(String episode_number) {
        this.episode_number = episode_number;
    }

    public String getVideo_resolution() {
        return video_resolution;
    }

    public void setVideo_resolution(String video_resolution) {
        this.video_resolution = video_resolution;
    }

    public String getEpisode_title() {
        return episode_title;
    }

    public void setEpisode_title(String episode_title) {
        this.episode_title = episode_title;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getEpisode_date() {
        return episode_date;
    }

    public void setEpisode_date(String episode_date) {
        this.episode_date = episode_date;
    }

    public String getEpisode_story() {
        return episode_story;
    }

    public void setEpisode_story(String episode_story) {
        this.episode_story = episode_story;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThirdparty_url() {
        return thirdparty_url;
    }

    public void setThirdparty_url(String thirdparty_url) {
        this.thirdparty_url = thirdparty_url;
    }

    public String getRolltype() {
        return rolltype;
    }

    public void setRolltype(String rolltype) {
        this.rolltype = rolltype;
    }

    public String getRoll_after() {
        return roll_after;
    }

    public void setRoll_after(String roll_after) {
        this.roll_after = roll_after;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String getIkäraja() {
        return ikäraja;
    }

    public void setIkäraja(String ikäraja) {
        this.ikäraja = ikäraja;
    }

    public String getEmbeddedUrl() {
        return embeddedUrl;
    }

    public void setEmbeddedUrl(String embeddedUrl) {
        this.embeddedUrl = embeddedUrl;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getMovieUrlForTv() {
        return movieUrlForTv;
    }

    public void setMovieUrlForTv(String movieUrlForTv) {
        this.movieUrlForTv = movieUrlForTv;
    }
        ArrayList<Resolution> resolutionArray = new ArrayList<>();

        public ArrayList<Resolution> getResolutionArray() {
            return resolutionArray;
        }

        public void setResolutionArray(ArrayList<Resolution> resolutionArray) {
            this.resolutionArray = resolutionArray;
        }

        public class Resolution{

        String BEST,secondBest,thirdBest,fourthBest;

        public String getBEST() {
            return BEST;
        }

        public void setBEST(String BEST) {
            this.BEST = BEST;
        }

        public String getSecondBest() {
            return secondBest;
        }

        public void setSecondBest(String secondBest) {
            this.secondBest = secondBest;
        }

        public String getThirdBest() {
            return thirdBest;
        }

        public void setThirdBest(String thirdBest) {
            this.thirdBest = thirdBest;
        }

        public String getFourthBest() {
            return fourthBest;
        }

        public void setFourthBest(String fourthBest) {
            this.fourthBest = fourthBest;
        }
    }

}




  /*  String name;
    String muvi_uniq_id;
    String is_ppv;
    String permalink;
    String item_count;
    String id;
    String movie_stream_uniq_id;
    String full_movie;
    String episode_number;
    String video_resolution;
    String episode_title;
    String series_number;
    String episode_date;
    String episode_story;
    String video_url;
    String rolltype;
    String roll_after;
    String video_duration;
    String embeddedUrl;
    String poster_url;
    String movieUrlForTv;
    String genre;
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


    public int getContent_types_id() {
        return content_types_id;
    }

    public void setContent_types_id(int content_types_id) {
        this.content_types_id = content_types_id;
    }

    int content_types_id;

    public String getThirdparty_url() {
        return thirdparty_url;
    }

    public void setThirdparty_url(String thirdparty_url) {
        this.thirdparty_url = thirdparty_url;
    }

    String thirdparty_url;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuvi_uniq_id() {
        return muvi_uniq_id;
    }

    public void setMuvi_uniq_id(String muvi_uniq_id) {
        this.muvi_uniq_id = muvi_uniq_id;
    }

    public String getIs_ppv() {
        return is_ppv;
    }

    public void setIs_ppv(String is_ppv) {
        this.is_ppv = is_ppv;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_stream_uniq_id() {
        return movie_stream_uniq_id;
    }

    public void setMovie_stream_uniq_id(String movie_stream_uniq_id) {
        this.movie_stream_uniq_id = movie_stream_uniq_id;
    }

    public String getFull_movie() {
        return full_movie;
    }

    public void setFull_movie(String full_movie) {
        this.full_movie = full_movie;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(String episode_number) {
        this.episode_number = episode_number;
    }

    public String getVideo_resolution() {
        return video_resolution;
    }

    public void setVideo_resolution(String video_resolution) {
        this.video_resolution = video_resolution;
    }

    public String getEpisode_title() {
        return episode_title;
    }

    public void setEpisode_title(String episode_title) {
        this.episode_title = episode_title;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getEpisode_date() {
        return episode_date;
    }

    public void setEpisode_date(String episode_date) {
        this.episode_date = episode_date;
    }

    public String getEpisode_story() {
        return episode_story;
    }

    public void setEpisode_story(String episode_story) {
        this.episode_story = episode_story;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getRolltype() {
        return rolltype;
    }

    public void setRolltype(String rolltype) {
        this.rolltype = rolltype;
    }

    public String getRoll_after() {
        return roll_after;
    }

    public void setRoll_after(String roll_after) {
        this.roll_after = roll_after;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String getEmbeddedUrl() {
        return embeddedUrl;
    }

    public void setEmbeddedUrl(String embeddedUrl) {
        this.embeddedUrl = embeddedUrl;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getMovieUrlForTv() {
        return movieUrlForTv;
    }

    public void setMovieUrlForTv(String movieUrlForTv) {
        this.movieUrlForTv = movieUrlForTv;
    }

*/
}
