package com.home.vod;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.home.apisdk.*;
import com.crashlytics.android.Crashlytics;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.vod.activity.CastAndCrewActivity;
import com.home.vod.util.LogUtil;
import com.muvi.muviplayersdk.activity.CastCrew;
import com.muvi.muviplayersdk.chromecast.ChromeCastApplicationId;

import io.fabric.sdk.android.Fabric;

/**
 * Created by muvi on 12/6/17.
 */

public class VodApplication extends Application implements CastCrew.AppInterface{

    @Override
    public void onCreate() {
        super.onCreate();
        ChromeCastApplicationId.chromeCastID = getString(R.string.app_id);
        LogUtil.showLog("MUVI PCKG NAME", SDKInitializer.user_Package_Name_At_Api+"::::"+getPackageName());
       // SDKInitializer.hashKey="nn";
       // SDKInitializer.user_Package_Name_At_Api=getPackageName();
        Fabric.with(this, new Crashlytics());
        APIUrlConstant.BASE_URl=BuildConfig.SERVICE_BASE_PATH;
        LogUtil.showLog("MUVI PCKG NAME", SDKInitializer.user_Package_Name_At_Api+"::::"+getPackageName());

    }



    VodApplication() {
        CastCrew.registerApp(this);
    }

    @Override
    public void getCastCrewDetails(String movie_id) {
        // Call Cast & Crew Activity.

        Log.v("BIBHU11","APPLICATION CALLED--"+movie_id);
        Intent intent = new Intent(getApplicationContext(), CastAndCrewActivity.class);
        intent.putExtra("cast_movie_id", movie_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getApplicationContext().startActivity(intent);

    }
}
