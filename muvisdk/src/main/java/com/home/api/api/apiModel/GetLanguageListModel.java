package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public class GetLanguageListModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("lang_list")
    @Expose
    private ArrayList<LangList> langList = null;
    @SerializedName("default_lang")
    @Expose
    private String defaultLang = "";

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
     * This method is used to get the array list of language
     *
     * @return langList
     */

    public ArrayList<LangList> getLangList() {
        return langList;
    }

    /**
     * This method is used to set the array list of language
     *
     * @param langList For setting the array list of language
     */

    public void setLangList(ArrayList<LangList> langList) {
        this.langList = langList;
    }

    /**
     * This method is used to get the default language
     *
     * @return defaultLang
     */

    public String getDefaultLang() {
        return defaultLang;
    }

    /**
     * This method is used to set the default language
     *
     * @param defaultLang For setting the default language
     */

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }

    public class LangList {

        @SerializedName("code")
        @Expose
        private String code = "";
        @SerializedName("language")
        @Expose
        private String language = "";

        /**
         * This method is used to get the code
         *
         * @return code
         */
        public String getCode() {
            return code;
        }

        /**
         * This method is used to set the code
         *
         * @param code For setting the code
         */
        public void setCode(String code) {
            this.code = code;
        }

        /**
         * This method is used to get the language
         *
         * @return language
         */

        public String getLanguage() {
            return language;
        }

        /**
         * This method is used to set the language
         *
         * @param language For setting the language
         */

        public void setLanguage(String language) {
            this.language = language;
        }

    }
}
