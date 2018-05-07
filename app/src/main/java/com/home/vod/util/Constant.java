package com.home.vod.util;


import com.home.vod.BuildConfig;

/**
 * Created by MUVI on 1/18/2017.
 */

public class Constant {

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
    public static final int IMAGE_PORTAIT_CONST = 1;
    public static final int IMAGE_LANDSCAPE_CONST = 0;


    public static final String[] PUBLIC_IPS_FOR_DISABLE_GEOBLOCK = {"111.93.166.194","111.93.165.17","111.93.165.18","111.93.165.19","111.93.165.20",
            "111.93.165.22","182.74.0.134"};

}
