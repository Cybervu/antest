package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Static page details
 *
 * @author Abhishek
 */

public class GetStaticPagedetailsModel {

    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("page_details")
    @Expose
    private PageDetails pageDetails;

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
     * This method is used to get the Page details
     *
     * @return pageDetails
     */
    public PageDetails getPageDetails() {
        return pageDetails;
    }

    /**
     * This method is used to set the Page details
     *
     * @param pageDetails For setting the page details
     */

    public void setPageDetails(PageDetails pageDetails) {
        this.pageDetails = pageDetails;
    }

    public class PageDetails {

        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("link_type")
        @Expose
        private String linkType = "";
        @SerializedName("external_url")
        @Expose
        private String externalUrl = "";
        @SerializedName("title")
        @Expose
        private String title = "";
        @SerializedName("reference")
        @Expose
        private String reference = "";
        @SerializedName("content")
        @Expose
        private String content = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("studio_id")
        @Expose
        private String studioId = "";
        @SerializedName("meta_title")
        @Expose
        private String metaTitle = "";
        @SerializedName("meta_description")
        @Expose
        private String metaDescription = "";
        @SerializedName("meta_keywords")
        @Expose
        private String metaKeywords = "";
        @SerializedName("ip")
        @Expose
        private String ip = "";
        @SerializedName("created_by")
        @Expose
        private String createdBy = "";
        @SerializedName("created_date")
        @Expose
        private String createdDate = "";
        @SerializedName("last_updated_by")
        @Expose
        private String lastUpdatedBy = "";
        @SerializedName("last_updated_date")
        @Expose
        private String lastUpdatedDate = "";
        @SerializedName("status")
        @Expose
        private String status = "";
        @SerializedName("id_seq")
        @Expose
        private String idSeq = "";
        @SerializedName("language_id")
        @Expose
        private String languageId = "";
        @SerializedName("parent_id")
        @Expose
        private String parentId = "";

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
         * This method is used to get the link type
         *
         * @return linkType
         */
        public String getLinkType() {
            return linkType;
        }

        /**
         * This method is used to set the link type
         *
         * @param linkType For setting the link type
         */

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        /**
         * This method is used to get the external url
         *
         * @return externalUrl
         */
        public String getExternalUrl() {
            return externalUrl;
        }

        /**
         * This method is used to set the external url
         *
         * @param externalUrl For setting the external url
         */

        public void setExternalUrl(String externalUrl) {
            this.externalUrl = externalUrl;
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
         * This method is used to get the reference
         *
         * @return reference
         */
        public String getReference() {
            return reference;
        }

        /**
         * This method is used to set the reference
         *
         * @param reference For setting the reference
         */

        public void setReference(String reference) {
            this.reference = reference;
        }

        /**
         * This method is used to get the content
         *
         * @return content
         */
        public String getContent() {
            return content;
        }

        /**
         * This method is used to set the content
         *
         * @param content For setting the content
         */

        public void setContent(String content) {
            this.content = content;
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
         * This method is used to get the meta title
         *
         * @return metaTitle
         */
        public String getMetaTitle() {
            return metaTitle;
        }

        /**
         * This method is used to set the meta title
         *
         * @param metaTitle For setting the meta title
         */
        public void setMetaTitle(String metaTitle) {
            this.metaTitle = metaTitle;
        }

        /**
         * This method is used to get the meta description
         *
         * @return metaDescription
         */
        public String getMetaDescription() {
            return metaDescription;
        }

        /**
         * This method is used to set the meta description
         *
         * @param metaDescription For setting the meta description
         */
        public void setMetaDescription(String metaDescription) {
            this.metaDescription = metaDescription;
        }

        /**
         * This method is used to get the meta keyword
         *
         * @return metaKeywords
         */
        public String getMetaKeywords() {
            return metaKeywords;
        }

        /**
         * This method is used to set the meta keyword
         *
         * @param metaKeywords For setting the meta keyword
         */

        public void setMetaKeywords(String metaKeywords) {
            this.metaKeywords = metaKeywords;
        }

        /**
         * This method is used to get the ip
         *
         * @return ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * This method is used to set the meta keyword
         *
         * @param ip For setting the ip
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * This method is used to get the created by details
         *
         * @return createdBy
         */
        public String getCreatedBy() {
            return createdBy;
        }

        /**
         * This method is used to set the created by details
         *
         * @param createdBy For setting the created by details
         */
        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        /**
         * This method is used to get the created date
         *
         * @return createdDate
         */
        public String getCreatedDate() {
            return createdDate;
        }

        /**
         * This method is used to set the created date
         *
         * @param createdDate For setting the created date
         */
        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        /**
         * This method is used to get the last update details
         *
         * @return lastUpdatedBy
         */
        public String getLastUpdatedBy() {
            return lastUpdatedBy;
        }

        /**
         * This method is used to set the last update details
         *
         * @param lastUpdatedBy For setting the last update details
         */
        public void setLastUpdatedBy(String lastUpdatedBy) {
            this.lastUpdatedBy = lastUpdatedBy;
        }

        /**
         * This method is used to get the last update date
         *
         * @return lastUpdatedDate
         */
        public String getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        /**
         * This method is used to set the last update date
         *
         * @param lastUpdatedDate For setting the last update date
         */
        public void setLastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
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
         * This method is used to get the id sequence
         *
         * @return idSeq
         */
        public String getIdSeq() {
            return idSeq;
        }

        /**
         * This method is used to set the id sequence
         *
         * @param idSeq For setting the id sequence
         */
        public void setIdSeq(String idSeq) {
            this.idSeq = idSeq;
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
         * This method is used to get the parent id
         *
         * @return parentId
         */
        public String getParentId() {
            return parentId;
        }

        /**
         * This method is used to set the parent id
         *
         * @param parentId For setting the parent id
         */

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

    }
}
