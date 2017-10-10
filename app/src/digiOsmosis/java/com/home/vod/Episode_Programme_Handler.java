package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.home.vod.activity.ProgrammeActivity;

import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;

/**
 * Created by MUVI on 10/6/2017.
 */

public class Episode_Programme_Handler {

    Context context;

    public Episode_Programme_Handler(Context context){
        this.context=context;
    }

    public void handleIntent(String key,String permalink){

        Intent intent=new Intent(context, ProgrammeActivity.class);
        intent.putExtra(key,permalink);
        context.startActivity(intent);

    }

}
