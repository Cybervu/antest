package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Attributes For HomePageAsynTask
 *
 * @author MUVI
 */

public class CategoryListOutputModel {

    private String category_id;
    private String category_name;
    private String permalink;
    private String category_img_url;
    private String cat_img_size;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getCategory_img_url() {
        return category_img_url;
    }

    public void setCategory_img_url(String category_img_url) {
        this.category_img_url = category_img_url;
    }

    public String getCat_img_size() {
        return cat_img_size;
    }

    public void setCat_img_size(String cat_img_size) {
        this.cat_img_size = cat_img_size;
    }
}
