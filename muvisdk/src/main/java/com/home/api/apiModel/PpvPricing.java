package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/20/2018.
 */

public class PpvPricing {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("pricing_id")
    @Expose
    private String pricingId;
    @SerializedName("price_for_unsubscribed")
    @Expose
    private String priceForUnsubscribed;
    @SerializedName("price_for_subscribed")
    @Expose
    private String priceForSubscribed;
    @SerializedName("show_unsubscribed")
    @Expose
    private String showUnsubscribed;
    @SerializedName("season_unsubscribed")
    @Expose
    private String seasonUnsubscribed;
    @SerializedName("episode_unsubscribed")
    @Expose
    private String episodeUnsubscribed;
    @SerializedName("show_subscribed")
    @Expose
    private String showSubscribed;
    @SerializedName("season_subscribed")
    @Expose
    private String seasonSubscribed;
    @SerializedName("episode_subscribed")
    @Expose
    private String episodeSubscribed;
    @SerializedName("validity_period")
    @Expose
    private String validityPeriod;
    @SerializedName("validity_recurrence")
    @Expose
    private String validityRecurrence;
    @SerializedName("is_show")
    @Expose
    private Integer isShow;
    @SerializedName("is_season")
    @Expose
    private Integer isSeason;
    @SerializedName("is_episode")
    @Expose
    private Integer isEpisode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPricingId() {
        return pricingId;
    }

    public void setPricingId(String pricingId) {
        this.pricingId = pricingId;
    }

    public String getPriceForUnsubscribed() {
        return priceForUnsubscribed;
    }

    public void setPriceForUnsubscribed(String priceForUnsubscribed) {
        this.priceForUnsubscribed = priceForUnsubscribed;
    }

    public String getPriceForSubscribed() {
        return priceForSubscribed;
    }

    public void setPriceForSubscribed(String priceForSubscribed) {
        this.priceForSubscribed = priceForSubscribed;
    }

    public String getShowUnsubscribed() {
        return showUnsubscribed;
    }

    public void setShowUnsubscribed(String showUnsubscribed) {
        this.showUnsubscribed = showUnsubscribed;
    }

    public String getSeasonUnsubscribed() {
        return seasonUnsubscribed;
    }

    public void setSeasonUnsubscribed(String seasonUnsubscribed) {
        this.seasonUnsubscribed = seasonUnsubscribed;
    }

    public String getEpisodeUnsubscribed() {
        return episodeUnsubscribed;
    }

    public void setEpisodeUnsubscribed(String episodeUnsubscribed) {
        this.episodeUnsubscribed = episodeUnsubscribed;
    }

    public String getShowSubscribed() {
        return showSubscribed;
    }

    public void setShowSubscribed(String showSubscribed) {
        this.showSubscribed = showSubscribed;
    }

    public String getSeasonSubscribed() {
        return seasonSubscribed;
    }

    public void setSeasonSubscribed(String seasonSubscribed) {
        this.seasonSubscribed = seasonSubscribed;
    }

    public String getEpisodeSubscribed() {
        return episodeSubscribed;
    }

    public void setEpisodeSubscribed(String episodeSubscribed) {
        this.episodeSubscribed = episodeSubscribed;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getValidityRecurrence() {
        return validityRecurrence;
    }

    public void setValidityRecurrence(String validityRecurrence) {
        this.validityRecurrence = validityRecurrence;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsSeason() {
        return isSeason;
    }

    public void setIsSeason(Integer isSeason) {
        this.isSeason = isSeason;
    }

    public Integer getIsEpisode() {
        return isEpisode;
    }

    public void setIsEpisode(Integer isEpisode) {
        this.isEpisode = isEpisode;
    }
}
