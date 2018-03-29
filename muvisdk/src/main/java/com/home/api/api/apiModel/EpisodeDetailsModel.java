package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Episode Details
 *
 * @author Abhishek
 */

public class EpisodeDetailsModel {

    public Integer getIs_advance() {
        return is_advance;
    }

    public void setIs_advance(Integer is_advance) {
        this.is_advance = is_advance;
    }

    @SerializedName("is_advance")
    @Expose
    private Integer is_advance=0;
    @SerializedName("code")
    @Expose
    private Integer code=0;
    @SerializedName("msg")
    @Expose
    private String msg="";
    @SerializedName("name")
    @Expose
    private String name="";
    @SerializedName("muvi_uniq_id")
    @Expose
    private String muviUniqId="";
   /* @SerializedName("custom_fields")
    @Expose
    private CustomFields customFields=null;*/
    @SerializedName("is_ppv")
    @Expose
    private Integer isPpv=0;
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
    @SerializedName("currency")
    @Expose
    private Currency currency=null;
    @SerializedName("permalink")
    @Expose
    private String permalink="";
    @SerializedName("item_count")
    @Expose
    private String itemCount="";
    @SerializedName("limit")
    @Expose
    private String limit="";
    @SerializedName("offset")
    @Expose
    private Integer offset=0;
    @SerializedName("comments")
    @Expose
    private ArrayList<String> comments = null;
    @SerializedName("episode")
    @Expose
    private ArrayList<Episode> episode = null;
    @SerializedName("cast")
    @Expose
    private ArrayList<String> cast = null;
    @SerializedName("season_total_duration")
    @Expose
    private String seasonTotalDuration="";

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuviUniqId() {
        return muviUniqId;
    }

    public void setMuviUniqId(String muviUniqId) {
        this.muviUniqId = muviUniqId;
    }

    /*public CustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }*/

    public Integer getIsPpv() {
        return isPpv;
    }

    public void setIsPpv(Integer isPpv) {
        this.isPpv = isPpv;
    }

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

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<Episode> getEpisode() {
        return episode;
    }

    public void setEpisode(ArrayList<Episode> episode) {
        this.episode = episode;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = cast;
    }

    public String getSeasonTotalDuration() {
        return seasonTotalDuration;
    }

    public void setSeasonTotalDuration(String seasonTotalDuration) {
        this.seasonTotalDuration = seasonTotalDuration;
    }

    /*public class Genre1 {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName="";
        @SerializedName("field_value")
        @Expose
        private String fieldValue="";

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

    public class Hdsd {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName="";
        @SerializedName("field_value")
        @Expose
        private String fieldValue="";

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
        private String fieldDisplayName="";
        @SerializedName("field_value")
        @Expose
        private String fieldValue="";

        public String getFieldDisplayName() {
            return fieldDisplayName="";
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
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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

    /*public class AdvPricing {
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

    }*/

    public class Resolution {

        @SerializedName("resolution")
        @Expose
        private Integer resolution;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getResolution() {
            return resolution;
        }

        public void setResolution(Integer resolution) {
            this.resolution = resolution;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    /*public class Season {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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

    public class Tekstitys1 {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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

    public class Valmistumisvuosi {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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

    /*public class Currency {

        @SerializedName("id")
        @Expose
        private String id="";
        @SerializedName("country_code")
        @Expose
        private String countryCode="";
        @SerializedName("code")
        @Expose
        private String code="";
        @SerializedName("title")
        @Expose
        private String title="";
        @SerializedName("symbol")
        @Expose
        private String symbol="$";
        @SerializedName("iso_num")
        @Expose
        private String isoNum="";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getIsoNum() {
            return isoNum;
        }

        public void setIsoNum(String isoNum) {
            this.isoNum = isoNum;
        }

    }
*/
   /* public class CustomField {

        @SerializedName("season#")
        @Expose
        private Season season;
        @SerializedName("episode#")
        @Expose
        private Episode_ episode;
        @SerializedName("ohjaaja")
        @Expose
        private Ohjaaja ohjaaja;
        @SerializedName("kesto_(min)")
        @Expose
        private KestoMin kestoMin;
        @SerializedName("ik\u00e4raja")
        @Expose
        private IkRaja ikRaja;
        @SerializedName("genre1")
        @Expose
        private Genre1 genre1;
        @SerializedName("valmistumisvuosi")
        @Expose
        private Valmistumisvuosi valmistumisvuosi;
        @SerializedName("p\u00e4\u00e4osissa")
        @Expose
        private POsissa pOsissa;
        @SerializedName("valmistusmaa")
        @Expose
        private Valmistusmaa valmistusmaa;
        @SerializedName("kieli")
        @Expose
        private Kieli kieli;
        @SerializedName("tekstitys1")
        @Expose
        private Tekstitys1 tekstitys1;
        @SerializedName("hdsd")
        @Expose
        private Hdsd hdsd;

        public Season getSeason() {
            return season;
        }

        public void setSeason(Season season) {
            this.season = season;
        }

        public Episode_ getEpisode() {
            return episode;
        }

        public void setEpisode(Episode_ episode) {
            this.episode = episode;
        }

        public Ohjaaja getOhjaaja() {
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

        public Genre1 getGenre1() {
            return genre1;
        }

        public void setGenre1(Genre1 genre1) {
            this.genre1 = genre1;
        }

        public Valmistumisvuosi getValmistumisvuosi() {
            return valmistumisvuosi;
        }

        public void setValmistumisvuosi(Valmistumisvuosi valmistumisvuosi) {
            this.valmistumisvuosi = valmistumisvuosi;
        }

        public POsissa getPOsissa() {
            return pOsissa;
        }

        public void setPOsissa(POsissa pOsissa) {
            this.pOsissa = pOsissa;
        }

        public Valmistusmaa getValmistusmaa() {
            return valmistusmaa;
        }

        public void setValmistusmaa(Valmistusmaa valmistusmaa) {
            this.valmistusmaa = valmistusmaa;
        }

        public Kieli getKieli() {
            return kieli;
        }

        public void setKieli(Kieli kieli) {
            this.kieli = kieli;
        }

        public Tekstitys1 getTekstitys1() {
            return tekstitys1;
        }

        public void setTekstitys1(Tekstitys1 tekstitys1) {
            this.tekstitys1 = tekstitys1;
        }

        public Hdsd getHdsd() {
            return hdsd;
        }

        public void setHdsd(Hdsd hdsd) {
            this.hdsd = hdsd;
        }

    }

    public class CustomFields {

        @SerializedName("season#")
        @Expose
        private String season;
        @SerializedName("episode#")
        @Expose
        private String episode;
        @SerializedName("ohjaaja")
        @Expose
        private String ohjaaja;
        @SerializedName("kesto_(min)")
        @Expose
        private String kestoMin;
        @SerializedName("ik\u00e4raja")
        @Expose
        private String ikRaja;

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getEpisode() {
            return episode;
        }

        public void setEpisode(String episode) {
            this.episode = episode;
        }

        public String getOhjaaja() {
            return ohjaaja;
        }

        public void setOhjaaja(String ohjaaja) {
            this.ohjaaja = ohjaaja;
        }

        public String getKestoMin() {
            return kestoMin;
        }

        public void setKestoMin(String kestoMin) {
            this.kestoMin = kestoMin;
        }

        public String getIkRaja() {
            return ikRaja;
        }

        public void setIkRaja(String ikRaja) {
            this.ikRaja = ikRaja;
        }

    }*/

    public class Episode {
        public Integer getContent_types_id() {
            return content_types_id;
        }

        public void setContent_types_id(Integer content_types_id) {
            this.content_types_id = content_types_id;
        }

        @SerializedName("content_types_id")
        @Expose
        private Integer content_types_id;
        /*@SerializedName("0")
        @Expose
        private String _0;*/
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("movie_stream_uniq_id")
        @Expose
        private String movieStreamUniqId;
        @SerializedName("full_movie")
        @Expose
        private String fullMovie;
        @SerializedName("episode_number")
        @Expose
        private String episodeNumber;
        /*@SerializedName("video_resolution")
        @Expose
        private String videoResolution;*/
        @SerializedName("episode_title")
        @Expose
        private String episodeTitle;
        @SerializedName("series_number")
        @Expose
        private String seriesNumber;
        @SerializedName("episode_date")
        @Expose
        private String episodeDate;
        @SerializedName("episode_story")
        @Expose
        private String episodeStory;
        @SerializedName("video_url")
        @Expose
        private String videoUrl;
        @SerializedName("thirdparty_url")
        @Expose
        private String thirdpartyUrl;
        @SerializedName("rolltype")
        @Expose
        private String rolltype;
        @SerializedName("roll_after")
        @Expose
        private String rollAfter;
        @SerializedName("video_duration")
        @Expose
        private String videoDuration;
        /*@SerializedName("custom_metadata_form_id")
        @Expose
        private String customMetadataFormId;
        @SerializedName("custom1")
        @Expose
        private String custom1;
        @SerializedName("custom2")
        @Expose
        private String custom2;
        @SerializedName("custom3")
        @Expose
        private String custom3;
        @SerializedName("custom4")
        @Expose
        private String custom4;
        @SerializedName("custom5")
        @Expose
        private String custom5;
        @SerializedName("custom6")
        @Expose
        private String custom6;*/
        /*@SerializedName("custom_field")
        @Expose*/
       // private CustomField customField;
        @SerializedName("embeddedUrl")
        @Expose
        private String embeddedUrl;
        @SerializedName("poster_url")
        @Expose
        private String posterUrl;
        @SerializedName("movieUrlForTv")
        @Expose
        private String movieUrlForTv;
       /* @SerializedName("resolution")
        @Expose
        private ArrayList<Resolution> resolution = null;*/
        @SerializedName("adDetails")
        @Expose
        private ArrayList<String> adDetails = null;

       /* public String get0() {
            return _0;
        }

        public void set0(String _0) {
            this._0 = _0;
        }*/

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMovieStreamUniqId() {
            return movieStreamUniqId;
        }

        public void setMovieStreamUniqId(String movieStreamUniqId) {
            this.movieStreamUniqId = movieStreamUniqId;
        }

        public String getFullMovie() {
            return fullMovie;
        }

        public void setFullMovie(String fullMovie) {
            this.fullMovie = fullMovie;
        }

        public String getEpisodeNumber() {
            return episodeNumber;
        }

        public void setEpisodeNumber(String episodeNumber) {
            this.episodeNumber = episodeNumber;
        }

       /* public String getVideoResolution() {
            return videoResolution;
        }

        public void setVideoResolution(String videoResolution) {
            this.videoResolution = videoResolution;
        }*/

        public String getEpisodeTitle() {
            return episodeTitle;
        }

        public void setEpisodeTitle(String episodeTitle) {
            this.episodeTitle = episodeTitle;
        }

        public String getSeriesNumber() {
            return seriesNumber;
        }

        public void setSeriesNumber(String seriesNumber) {
            this.seriesNumber = seriesNumber;
        }

        public String getEpisodeDate() {
            return episodeDate;
        }

        public void setEpisodeDate(String episodeDate) {
            this.episodeDate = episodeDate;
        }

        public String getEpisodeStory() {
            return episodeStory;
        }

        public void setEpisodeStory(String episodeStory) {
            this.episodeStory = episodeStory;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThirdpartyUrl() {
            return thirdpartyUrl;
        }

        public void setThirdpartyUrl(String thirdpartyUrl) {
            this.thirdpartyUrl = thirdpartyUrl;
        }

        public String getRolltype() {
            return rolltype;
        }

        public void setRolltype(String rolltype) {
            this.rolltype = rolltype;
        }

        public String getRollAfter() {
            return rollAfter;
        }

        public void setRollAfter(String rollAfter) {
            this.rollAfter = rollAfter;
        }

        public String getVideoDuration() {
            return videoDuration;
        }

        public void setVideoDuration(String videoDuration) {
            this.videoDuration = videoDuration;
        }

        /*public String getCustomMetadataFormId() {
            return customMetadataFormId;
        }

        public void setCustomMetadataFormId(String customMetadataFormId) {
            this.customMetadataFormId = customMetadataFormId;
        }

        public String getCustom1() {
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
        }*/

        /*public void setCustom6(String custom6) {
            this.custom6 = custom6;
        }*/

       /* public CustomField getCustomField() {
            return customField;
        }

        public void setCustomField(CustomField customField) {
            this.customField = customField;
        }*/

        public String getEmbeddedUrl() {
            return embeddedUrl;
        }

        public void setEmbeddedUrl(String embeddedUrl) {
            this.embeddedUrl = embeddedUrl;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public String getMovieUrlForTv() {
            return movieUrlForTv;
        }

        public void setMovieUrlForTv(String movieUrlForTv) {
            this.movieUrlForTv = movieUrlForTv;
        }

       /* public ArrayList<Resolution> getResolution() {
            return resolution;
        }

        public void setResolution(ArrayList<Resolution> resolution) {
            this.resolution = resolution;
        }*/

        public ArrayList<String> getAdDetails() {
            return adDetails;
        }

        public void setAdDetails(ArrayList<String> adDetails) {
            this.adDetails = adDetails;
        }

    }

    public class Episode_ {

        @SerializedName("field_display_name")
        @Expose
        private String fieldDisplayName;
        @SerializedName("field_value")
        @Expose
        private String fieldValue;

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
}
