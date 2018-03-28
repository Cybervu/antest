package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Application Home Page
 *
 * @author Abhishek
 */

public class GetAppHomePageModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("BannerSectionList")
    @Expose
    private ArrayList<BannerSectionList> bannerSectionList = null;
    @SerializedName("banner_text")
    @Expose
    private String bannerText = "";
    @SerializedName("is_featured")
    @Expose
    private Integer isFeatured = 0;
    @SerializedName("SectionName")
    @Expose
    private ArrayList<SectionName> sectionName = null;

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
     * This method is used for getting the status
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method is used for setting the status
     *
     * @param status For setting the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method is used to get the array list of section banner
     *
     * @return bannerSectionList
     */
    public ArrayList<BannerSectionList> getBannerSectionList() {
        return bannerSectionList;
    }

    /**
     * This method is used to set the array list of section banner
     *
     * @param bannerSectionList For setting the banner
     */
    public void setBannerSectionList(ArrayList<BannerSectionList> bannerSectionList) {
        this.bannerSectionList = bannerSectionList;
    }

    /**
     * This method is used to get the banner text
     *
     * @return bannerText
     */

    public String getBannerText() {
        return bannerText;
    }

    /**
     * This method is used to set the banner text
     *
     * @param bannerText For setting the banner text
     */

    public void setBannerText(String bannerText) {
        this.bannerText = bannerText;
    }

    /**
     * This method is used to get the feature details
     *
     * @return isFeatured
     */
    public Integer getIsFeatured() {
        return isFeatured;
    }

    /**
     * This method is used to set the feature details
     *
     * @param isFeatured For setting the feature details
     */

    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }

    /**
     * This method is used to get the array list for section name
     *
     * @return sectionName
     */
    public ArrayList<SectionName> getSectionName() {
        return sectionName;
    }

    /**
     * This method is used to set the array list for section name
     *
     * @param sectionName For setting the array list for section name
     */

    public void setSectionName(ArrayList<SectionName> sectionName) {
        this.sectionName = sectionName;
    }

    public class BannerSectionList {

        @SerializedName("image_path")
        @Expose
        private String imagePath = "";
        @SerializedName("banner_url")
        @Expose
        private String bannerUrl = "";

        /**
         * This method is used to get the image path
         *
         * @return imagePath
         */
        public String getImagePath() {
            return imagePath;
        }

        /**
         * This method is used to set the image path
         *
         * @param imagePath For setting the image path
         */

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        /**
         * This method is used to get the banner url
         *
         * @return bannerUrl
         */

        public String getBannerUrl() {
            return bannerUrl;
        }

        /**
         * This method is used to set the banner url
         *
         * @param bannerUrl For setting the banner url
         */

        public void setBannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
        }

    }

    public class SectionName {

        @SerializedName("studio_id")
        @Expose
        private String studioId = "";
        @SerializedName("language_id")
        @Expose
        private String languageId = "";
        @SerializedName("title")
        @Expose
        private String title = "";
        @SerializedName("section_id")
        @Expose
        private String sectionId = "";
        @SerializedName("section_type")
        @Expose
        private String sectionType = "";
        @SerializedName("total")
        @Expose
        private String total = "";

        /**
         * This method is used to get the studio id
         *
         * @return studioId
         */

        public String getStudioId() {
            return studioId;
        }

        /**
         * This method is used to set the studio id
         *
         * @param studioId For setting the studio id
         */

        public void setStudioId(String studioId) {
            this.studioId = studioId;
        }

        /**
         * This method is used to get the language id
         *
         * @return languageId
         */
        public String getLanguageId() {
            return languageId;
        }

        /**
         * This method is used to set the language id
         *
         * @param languageId For setting the language id
         */
        public void setLanguageId(String languageId) {
            this.languageId = languageId;
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
         * This method is used to get the section id
         *
         * @return sectionId
         */

        public String getSectionId() {
            return sectionId;
        }

        /**
         * This method is used to set the section id
         *
         * @param sectionId For setting the section id
         */

        public void setSectionId(String sectionId) {
            this.sectionId = sectionId;
        }

        /**
         * This method is used to get the section type
         *
         * @return sectionType
         */
        public String getSectionType() {
            return sectionType;
        }

        /**
         * This method is used to set the section type
         *
         * @param sectionType For setting the section type
         */
        public void setSectionType(String sectionType) {
            this.sectionType = sectionType;
        }

        /**
         * This method is used to get the total
         *
         * @return total
         */
        public String getTotal() {
            return total;
        }

        /**
         * This method is used to set the total
         *
         * @param total For setting the total
         */
        public void setTotal(String total) {
            this.total = total;
        }

    }
}
