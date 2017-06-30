package com.home.vod;

import android.app.Application;

import com.home.apisdk.*;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by muvi on 12/6/17.
 */

public class VodApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        APIUrlConstant.BASE_URl=BuildConfig.SERVICE_BASE_PATH;

    }
}
