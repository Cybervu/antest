package com.home.vod.model;

import java.io.Serializable;

/**
 * Created by MUVI on 10/6/2017.
 */

public class SeasonModel implements Serializable {
    private String seasonId = "";
    private int seasonImage ;
    private String seasonName = "";
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public SeasonModel(String seasonId, int seasonImage, String seasonName,boolean isSelected) {
        this.seasonId = seasonId;
        this.seasonImage = seasonImage;
        this.seasonName = seasonName;
        this.isSelected = isSelected;

    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public int getSeasonImage() {
        return seasonImage;
    }

    public void setSeasonImage(int seasonImage) {
        this.seasonImage = seasonImage;
    }
}

