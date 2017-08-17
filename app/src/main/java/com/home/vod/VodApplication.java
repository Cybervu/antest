package com.home.vod;

import android.app.Application;

import com.home.apisdk.*;
import com.crashlytics.android.Crashlytics;
import com.home.vod.util.LogUtil;

import io.fabric.sdk.android.Fabric;

/**
 * Created by muvi on 12/6/17.
 */

public class VodApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
      //  HeaderConstants.user_Package_Name_At_Api = getPackageName();
      //  LogUtil.showLog("MUVI PCKG NAME", HeaderConstants.user_Package_Name_At_Api+"::::"+getPackageName());
        APIUrlConstant.BASE_URl=BuildConfig.SERVICE_BASE_PATH;

    }
}
