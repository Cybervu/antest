package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Menu List
 *
 * @author Abhishek
 */

public class MenuListModel {

    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("total_menu")
    @Expose
    private Integer totalMenu = 0;
    @SerializedName("menu")
    @Expose
    private ArrayList<Menu> menu = null;
    @SerializedName("footer_menu")
    @Expose
    private ArrayList<FooterMenu> footerMenu = null;

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
     * This method is used to get the total menu
     *
     * @return totalMenu
     */
    public Integer getTotalMenu() {
        return totalMenu;
    }

    /**
     * This method is used to set the total menu
     *
     * @param totalMenu For setting the total menu
     */
    public void setTotalMenu(Integer totalMenu) {
        this.totalMenu = totalMenu;
    }

    /**
     * This method is used to get the array list of menu
     *
     * @return menu
     */
    public ArrayList<Menu> getMenu() {
        return menu;
    }

    /**
     * This method is used to set the array list of menu
     *
     * @param menu For setting the menu
     */
    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }

    /**
     * This method is used to get the array list of footer menu
     *
     * @return footerMenu
     */
    public ArrayList<FooterMenu> getFooterMenu() {
        return footerMenu;
    }

    /**
     * This method is used to set the array list of footer menu
     *
     * @param footerMenu For setting the footer menu
     */
    public void setFooterMenu(ArrayList<FooterMenu> footerMenu) {
        this.footerMenu = footerMenu;
    }

    public class FooterMenu {

        @SerializedName("domain")
        @Expose
        private String domain = "";
        @SerializedName("link_type")
        @Expose
        private String linkType = "";
        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("display_name")
        @Expose
        private String displayName = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("url")
        @Expose
        private String url = "";
        @SerializedName("0")
        @Expose
        private Integer _0 = 0;

        /**
         * This method is used to get the domain
         *
         * @return domain
         */
        public String getDomain() {
            return domain;
        }

        /**
         * This method is used to set the domain
         *
         * @param domain For setting the domain
         */
        public void setDomain(String domain) {
            this.domain = domain;
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
         * This method is used to get she link type
         *
         * @param linkType For setting the link type
         */
        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

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
         * This method is used to get the display name
         *
         * @return displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * This method is used to set the display name
         *
         * @param displayName For setting the display name
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
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
         * This method is used to get the url
         *
         * @return url
         */
        public String getUrl() {
            return url;
        }

        /**
         * This method is used to set the url
         *
         * @param url For setting the url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        public Integer get0() {
            return _0;
        }

        public void set0(Integer _0) {
            this._0 = _0;
        }

    }

    public class Menu {

        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("link_type")
        @Expose
        private String linkType = "";
        @SerializedName("parent_id")
        @Expose
        private String parentId = "";
        @SerializedName("display_name")
        @Expose
        private String displayName = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("web_url")
        @Expose
        private String webUrl = "";

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

        /**
         * This method is used to get the display name
         *
         * @return displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * This method is used to set the display name
         *
         * @param displayName For setting the display name
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
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
         * This method is used to get the permalink
         *
         * @param permalink For setting the permalink
         */
        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        /**
         * This method is used to get the web url
         *
         * @return webUrl
         */
        public String getWebUrl() {
            return webUrl;
        }

        /**
         * This method is used to set the web url
         *
         * @param webUrl For setting the web url
         */
        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

    }
}
