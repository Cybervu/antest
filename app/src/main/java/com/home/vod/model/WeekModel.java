package com.home.vod.model;

import java.io.Serializable;

/**
 * Created by MUVI on 10/6/2017.
 */

public class WeekModel  {
    private String weekId = "";
    private String weekName = "";

    public WeekModel(String weekId, String weekName, int weekDays) {
        this.weekId = weekId;
        this.weekName = weekName;
        this.weekDays = weekDays;
    }

    private int weekDays = 7;


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

