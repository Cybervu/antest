package com.home.vod;

import android.content.Context;
import android.content.Intent;

import com.home.vod.activity.ProgrammeActivity;

/**
 * Created by MUVI on 10/6/2017.
 */

public class Single_Part_Programme_Handler {

    Context context;

    public Single_Part_Programme_Handler(Context context){
        this.context=context;
    }

    public void handleIntent(String key,String permalink){

        Intent intent=new Intent(context, ProgrammeActivity.class);
        intent.putExtra(key,permalink);
        context.startActivity(intent);

    }

}
