package com.home.apisdk.apiModel.apiModel;

/**
 * Created by Muvi on 9/29/2016.
 */
public class Episode_Details_input {
    String permalink,authtoken,limit="10",offset="0";

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String series_number;
    String lang_code;
    String country;

   /* public Episode_Details_input(String permalink, String authtoken, String limit, String offset) {
        this.permalink = permalink;
        this.authtoken = authtoken;
        this.limit = limit;
        this.offset = offset;
    }*/

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
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
}
