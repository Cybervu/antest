package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Application Menu
 *
 * @author Abhishek
 */

public class GetAppMenuModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("menu_items")
    @Expose
    private ArrayList<MenuItem> menuItems = null;
    @SerializedName("footer_menu")
    @Expose
    private ArrayList<FooterMenu> footerMenu = null;

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
     * This method is used to get the array list of menu items
     *
     * @return menuItems
     */
    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * This method is used to set the array list of menu items
     *
     * @param menuItems For setting the Menu items
     */

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    /**
     * This method is used to get the array list of Footer menu items
     *
     * @return footerMenu
     */
    public ArrayList<FooterMenu> getFooterMenu() {
        return footerMenu;
    }

    /**
     * This method is used to set the array list of Footer menu items
     *
     * @param footerMenu For setting the footer menu items
     */

    public void setFooterMenu(ArrayList<FooterMenu> footerMenu) {
        this.footerMenu = footerMenu;
    }

    public class Child {

        @SerializedName("id")
        @Expose
        private Integer id = 0;
        @SerializedName("title")
        @Expose
        private String title = "";
        @SerializedName("parent_id")
        @Expose
        private Integer parentId = 0;
        @SerializedName("link_type")
        @Expose
        private String linkType = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("value")
        @Expose
        private String value = "";
        @SerializedName("id_seq")
        @Expose
        private Integer idSeq = 0;
        @SerializedName("language_id")
        @Expose
        private Integer languageId = 0;
        @SerializedName("language_parent_id")
        @Expose
        private Integer languageParentId = 0;

        /**
         * This method is used to get the ID
         *
         * @return id
         */

        public Integer getId() {
            return id;
        }

        /**
         * This method is used to set the ID
         *
         * @param id For setting the ID
         */

        public void setId(Integer id) {
            this.id = id;
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
         * This method is used to get the parent id
         *
         * @return parentId
         */

        public Integer getParentId() {
            return parentId;
        }

        /**
         * This method is used to set the parent id
         *
         * @param parentId For setting the parent id
         */

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
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
         * This method is used to get the value
         *
         * @return value
         */

        public String getValue() {
            return value;
        }

        /**
         * This method is used to set the value
         *
         * @param value For setting the value
         */

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * This method is used to get the ID sequence
         *
         * @return idSeq
         */

        public Integer getIdSeq() {
            return idSeq;
        }

        /**
         * This method is used to set the ID sequence
         *
         * @param idSeq For setting the ID sequence
         */

        public void setIdSeq(Integer idSeq) {
            this.idSeq = idSeq;
        }

        /**
         * This method is used to get the Language ID
         *
         * @return languageId
         */

        public Integer getLanguageId() {
            return languageId;
        }

        /**
         * This method is used to set the Language ID
         *
         * @param languageId For setting the Language ID
         */

        public void setLanguageId(Integer languageId) {
            this.languageId = languageId;
        }

        /**
         * This method is used to get the Language parent ID
         *
         * @return languageParentId
         */

        public Integer getLanguageParentId() {
            return languageParentId;
        }

        /**
         * This method is used to set the Language parent ID
         *
         * @param languageParentId For setting the language
         */

        public void setLanguageParentId(Integer languageParentId) {
            this.languageParentId = languageParentId;
        }
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
         * This method is used to set the link type
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

    public class MenuItem {

        @SerializedName("id")
        @Expose
        private Integer id = 0;
        @SerializedName("title")
        @Expose
        private String title = "";
        @SerializedName("parent_id")
        @Expose
        private Integer parentId = 0;
        @SerializedName("link_type")
        @Expose
        private String linkType = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("value")
        @Expose
        private String value = "";
        @SerializedName("id_seq")
        @Expose
        private Integer idSeq = 0;
        @SerializedName("language_id")
        @Expose
        private Integer languageId = 0;
        @SerializedName("language_parent_id")
        @Expose
        private Integer languageParentId = 0;
        @SerializedName("child")
        @Expose
        private ArrayList<Child> child = null;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId = 0;
        @SerializedName("isSubcategoryPresent")
        @Expose
        private Integer isSubcategoryPresent = 0;

        /**
         * This method is used to get the id
         *
         * @return id
         */

        public Integer getId() {
            return id;
        }

        /**
         * This method is used to set the id
         *
         * @param id For setting the url
         */

        public void setId(Integer id) {
            this.id = id;
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
         * This method is used to get the parent id
         *
         * @return parentId
         */

        public Integer getParentId() {
            return parentId;
        }

        /**
         * This method is used to set the parent id
         *
         * @param parentId For setting the parent id
         */

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
        }

        /**
         * This method is used to get the parent link type
         *
         * @return linkType
         */
        public String getLinkType() {
            return linkType;
        }

        /**
         * This method is used to set the parent link type
         *
         * @param linkType For setting the link type
         */

        public void setLinkType(String linkType) {
            this.linkType = linkType;
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
         * This method is used to get the value
         *
         * @return value
         */

        public String getValue() {
            return value;
        }

        /**
         * This method is used to set the value
         *
         * @param value For setting the value
         */

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * This method is used to get the Id sequence
         *
         * @return idSeq
         */
        public Integer getIdSeq() {
            return idSeq;
        }

        /**
         * This method is used to set the Id sequence
         *
         * @param idSeq For setting the sequence id
         */
        public void setIdSeq(Integer idSeq) {
            this.idSeq = idSeq;
        }

        /**
         * This method is used to get the language id
         *
         * @return languageId
         */
        public Integer getLanguageId() {
            return languageId;
        }

        /**
         * This method is used to set the language id
         *
         * @param languageId For setting the language id
         */
        public void setLanguageId(Integer languageId) {
            this.languageId = languageId;
        }

        /**
         * This method is used to get the language parent id
         *
         * @return languageParentId
         */

        public Integer getLanguageParentId() {
            return languageParentId;
        }

        /**
         * This method is used to set the language parent id
         *
         * @param languageParentId For setting the language id
         */

        public void setLanguageParentId(Integer languageParentId) {
            this.languageParentId = languageParentId;
        }

        /**
         * This method is used to get the array list of child menu
         *
         * @return child
         */
        public ArrayList<Child> getChild() {
            return child;
        }

        /**
         * This method is used to set the array list of child menu
         *
         * @param child For setting the child menu
         */

        public void setChild(ArrayList<Child> child) {
            this.child = child;
        }

        /**
         * This method is used to get the category id
         *
         * @return categoryId
         */

        public Integer getCategoryId() {
            return categoryId;
        }

        /**
         * This method is used to set the category id
         *
         * @param categoryId For setting the category id
         */
        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        /**
         * This method is used to get the check if sub category is present or not
         *
         * @return isSubcategoryPresent
         */
        public Integer getIsSubcategoryPresent() {
            return isSubcategoryPresent;
        }

        /**
         * This method is used to set the check if sub category is present or not
         *
         * @param isSubcategoryPresent For setting the sub category
         */

        public void setIsSubcategoryPresent(Integer isSubcategoryPresent) {
            this.isSubcategoryPresent = isSubcategoryPresent;
        }

    }
}
