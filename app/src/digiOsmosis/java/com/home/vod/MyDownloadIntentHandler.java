package com.home.vod;

import android.app.Activity;
import android.content.Intent;

import com.home.vod.activity.DigiOsmosisDownloads;

/**
 * Created by MUVI on 10/13/2017.
 */

public class MyDownloadIntentHandler {

    Activity activity;

    public MyDownloadIntentHandler(Activity activity){
        this.activity=activity;
    }

    public Intent handleDownloadIntent(){
        return new Intent(activity,DigiOsmosisDownloads.class);
    }
}
