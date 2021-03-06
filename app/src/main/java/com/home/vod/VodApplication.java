package com.home.vod;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.home.apisdk.*;
import com.crashlytics.android.Crashlytics;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.vod.util.LogUtil;

import io.fabric.sdk.android.Fabric;

/**
 * Created by muvi on 12/6/17.
 */

public class VodApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.showLog("MUVI PCKG NAME", SDKInitializer.user_Package_Name_At_Api+"::::"+getPackageName());
       // SDKInitializer.hashKey="nn";
       // SDKInitializer.user_Package_Name_At_Api=getPackageName();
        Fabric.with(this, new Crashlytics());
        APIUrlConstant.BASE_URl=BuildConfig.SERVICE_BASE_PATH;
        LogUtil.showLog("MUVI PCKG NAME", SDKInitializer.user_Package_Name_At_Api+"::::"+getPackageName());

        //Allowing Strict mode policy for Nougat support
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
