package com.home.vod;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.fragment.VideosListFragment;
import com.home.vod.fragment.YogaFragment;

/**
 * Created by MUVI on 10/6/2017.
 */

public class Content_List_Handler {

    Context context;

    public Content_List_Handler(Context context){
        this.context=context;
    }

    public Fragment handleIntent(String title){
        if (title.equalsIgnoreCase("YOGA")){
            return new YogaFragment();

        }else {
            return new VideosListFragment();
        }
    }

}
