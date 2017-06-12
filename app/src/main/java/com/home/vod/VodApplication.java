package com.home.vod;

import android.app.Application;

import com.home.apisdk.*;

/**
 * Created by muvi on 12/6/17.
 */

public class VodApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        APIUrlConstant.BASE_URl= BuildConfig.SERVICE_BASE_PATH;
    }
}
