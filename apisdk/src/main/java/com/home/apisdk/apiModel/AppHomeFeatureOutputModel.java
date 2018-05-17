package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Input Attributes For ContactUsAsynTask
 *
 * @author MUVI
 */

public class AppHomeFeatureOutputModel {

    ArrayList<HomeFeaturePageSectionModel> homePageSectionModelArrayList = new ArrayList<HomeFeaturePageSectionModel>();
    ArrayList<HomeFeaturePageBannerModel> homePageBannerModelArrayList = new ArrayList<HomeFeaturePageBannerModel>();

    public ArrayList<HomeFeaturePageSectionModel> getHomePageSectionModelArrayList() {
        return homePageSectionModelArrayList;
    }

    public void setHomePageSectionModelArrayList(ArrayList<HomeFeaturePageSectionModel> homePageSectionModelArrayList) {
        this.homePageSectionModelArrayList = homePageSectionModelArrayList;
    }

    public ArrayList<HomeFeaturePageBannerModel> getHomePageBannerModelArrayList() {
        return homePageBannerModelArrayList;
    }

    public void setHomePageBannerModelArrayList(ArrayList<HomeFeaturePageBannerModel> homePageBannerModelArrayList) {
        this.homePageBannerModelArrayList = homePageBannerModelArrayList;
    }



}
