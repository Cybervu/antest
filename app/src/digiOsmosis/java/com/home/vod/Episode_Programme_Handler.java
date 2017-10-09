package com.home.vod;

import android.app.Activity;
import android.content.Intent;

import com.home.vod.activity.ProgrammeActivity;

/**
 * Created by MUVI on 10/6/2017.
 */

public class Episode_Programme_Handler {

    Activity activity;

    public Episode_Programme_Handler(Activity activity){
        this.activity=activity;
    }

    public void handleIntent(String key,String permalink){

        Intent intent=new Intent(activity, ProgrammeActivity.class);
        activity.startActivity(intent);

    }

}
