package com.home.vod.util;


import android.util.Log;

import com.home.vod.BuildConfig;
import com.home.vod.activity.Login;
import com.home.vod.preferences.PreferenceManager;

/**
 * Created by MUVI on 1/18/2017.
 */

public class Constant {


    public static void constantCalled() {
        Log.v("ANU1","constantCalled====");
    }


    public static final int PRELOAD_TIME_S = 20;
    public static final String IS_CAST_CONNECTED_INTENT_KEY = "IS_CAST_CONNECTED_INTENT_KEY";
    public static final String PERMALINK_INTENT_KEY = "PERMALINK_INTENT_KEY";
    public static final String GENRE_INTENT_KEY = "GENRE";
    public static final String STORY_INTENT_KEY = "STORY";
    public static final String CENSOR_RATING_INTENT_KEY = "CENSORRATING";
    public static final String CAST_INTENT_KEY = "CAST";
    public static final String SEASON_INTENT_KEY = "SEASON";
    public static final String authTokenStr = BuildConfig.AUTH_TOKEN; //new thought channel
    public static final String VIDEO_TITLE_INTENT_KEY = "VIDEO TITLE";


}
