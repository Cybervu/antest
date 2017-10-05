package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by MUVI on 10/5/2017.
 */

public class AppHomePageOutput {

    String banner_text;

    public String getBanner_text() {
        return banner_text;
    }

    public void setBanner_text(String banner_text) {
        this.banner_text = banner_text;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    String is_featured;


    public ArrayList<HomePageSectionModel> getHomePageSectionModel() {
        return homePageSectionModel;
    }

    public void setHomePageSectionModel(ArrayList<HomePageSectionModel> homePageSectionModel) {
        this.homePageSectionModel = homePageSectionModel;
    }

    ArrayList<HomePageSectionModel> homePageSectionModel;

    public ArrayList<HomePageBannerModel> getHomePageBannerModels() {
        return homePageBannerModels;
    }

    public void setHomePageBannerModels(ArrayList<HomePageBannerModel> homePageBannerModels) {
        this.homePageBannerModels = homePageBannerModels;
    }

    ArrayList<HomePageBannerModel> homePageBannerModels;


}
