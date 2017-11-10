package com.home.vod;

import com.home.vod.activity.MainActivity;

/**
 * Created by MUVI on 9/25/2017.
 */

public class MainActivityHeaderHandler {
    MainActivity activity;

    public MainActivityHeaderHandler(MainActivity activity) {
        this.activity = activity;

    }


    public void handleTitle() {
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);

    }
}
