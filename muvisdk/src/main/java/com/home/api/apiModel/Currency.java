package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MUVI on 3/20/2018.
 */

public class Currency {

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
