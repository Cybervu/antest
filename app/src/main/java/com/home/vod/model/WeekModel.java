package com.home.vod.model;

import java.io.Serializable;

/**
 * Created by MUVI on 10/6/2017.
 */

public class WeekModel  {
    private String weekId = "";
    private String weekName = "";
    private String premalink = "";


    public WeekModel(String weekId, String weekName, int weekDays, String premalink) {
        this.weekId = weekId;
        this.weekName = weekName;
        this.weekDays = weekDays;
        this.premalink = premalink;
    }

    private int weekDays = 7;

    public String getPremalink() {
        return premalink;
    }

    public void setPremalink(String premalink) {
        this.premalink = premalink;
    }


    public int getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(int weekDays) {
        this.weekDays = weekDays;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }


}

