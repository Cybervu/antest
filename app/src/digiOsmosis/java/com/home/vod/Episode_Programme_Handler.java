package com.home.vod;

import android.app.Activity;
import android.content.Intent;

import com.home.vod.activity.ProgrammeActivity;

import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;

/**
 * Created by Abhishek on 10/6/2017.
 */

public class Episode_Programme_Handler {

    Activity activity;

    public Episode_Programme_Handler(Activity activity){
        this.activity=activity;
    }

    public void handleIntent(String key,String permalink){

        Intent intent=new Intent(activity, ProgrammeActivity.class);
        intent.putExtra(key,permalink);
        activity.startActivity(intent);

    }

}
