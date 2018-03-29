package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/20/2018.
 */

public class AdvPricing {
    @SerializedName("price_for_unsubscribed")
    @Expose
    private String price_for_unsubscribed;
    @SerializedName("price_for_subscribed")
    @Expose
    private String price_for_subscribed;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("show_unsubscribed")
    @Expose
    private String show_unsubscribed;
    @SerializedName("show_subscribed")
    @Expose
    private String show_subscribed;
    @SerializedName("season_unsubscribed")
    @Expose
    private String season_unsubscribed;
    @SerializedName("season_subscribed")
    @Expose
    private String season_subscribed;
    @SerializedName("episode_unsubscribed")
    @Expose
    private String episode_unsubscribed;
    @SerializedName("episode_subscribed")
    @Expose
    private String episode_subscribed;
    @SerializedName("validity_period")
    @Expose
    private String validity_period;
    @SerializedName("is_show")
    @Expose
    private Integer is_show;
    @SerializedName("is_season")
    @Expose
    private Integer is_season;
    @SerializedName("is_episode")
    @Expose
    private Integer is_episode;

    public String getPrice_for_unsubscribed() {
        return price_for_unsubscribed;
    }

    public void setPrice_for_unsubscribed(String price_for_unsubscribed) {
        this.price_for_unsubscribed = price_for_unsubscribed;
    }

    public String getPrice_for_subscribed() {
        return price_for_subscribed;
    }

    public void setPrice_for_subscribed(String price_for_subscribed) {
        this.price_for_subscribed = price_for_subscribed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShow_unsubscribed() {
        return show_unsubscribed;
    }

    public void setShow_unsubscribed(String show_unsubscribed) {
        this.show_unsubscribed = show_unsubscribed;
    }

    public String getShow_subscribed() {
        return show_subscribed;
    }

    public void setShow_subscribed(String show_subscribed) {
        this.show_subscribed = show_subscribed;
    }

    public String getSeason_unsubscribed() {
        return season_unsubscribed;
    }

    public void setSeason_unsubscribed(String season_unsubscribed) {
        this.season_unsubscribed = season_unsubscribed;
    }

    public String getSeason_subscribed() {
        return season_subscribed;
    }

    public void setSeason_subscribed(String season_subscribed) {
        this.season_subscribed = season_subscribed;
    }

    public String getEpisode_unsubscribed() {
        return episode_unsubscribed;
    }

    public void setEpisode_unsubscribed(String episode_unsubscribed) {
        this.episode_unsubscribed = episode_unsubscribed;
    }

    public String getEpisode_subscribed() {
        return episode_subscribed;
    }

    public void setEpisode_subscribed(String episode_subscribed) {
        this.episode_subscribed = episode_subscribed;
    }

    public String getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(String validity_period) {
        this.validity_period = validity_period;
    }

    public Integer getIs_show() {
        return is_show;
    }

    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }

    public Integer getIs_season() {
        return is_season;
    }

    public void setIs_season(Integer is_season) {
        this.is_season = is_season;
    }

    public Integer getIs_episode() {
        return is_episode;
    }

    public void setIs_episode(Integer is_episode) {
        this.is_episode = is_episode;
    }

    public String getPricing_id() {
        return pricing_id;
    }

    public void setPricing_id(String pricing_id) {
        this.pricing_id = pricing_id;
    }

    public String getValidity_recurrence() {
        return validity_recurrence;
    }

    public void setValidity_recurrence(String validity_recurrence) {
        this.validity_recurrence = validity_recurrence;
    }

    @SerializedName("pricing_id")
    @Expose
    private String pricing_id;
    @SerializedName("validity_recurrence")
    @Expose
    private String validity_recurrence;

}
