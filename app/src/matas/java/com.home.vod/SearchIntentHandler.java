package com.home.vod;

import android.app.Activity;
import android.content.Intent;


/**
 * Created by MUVI on 10/13/2017.
 */

public class SearchIntentHandler {

    Activity activity;

    public SearchIntentHandler(Activity activity){
        this.activity=activity;
    }

    public Intent handleSearchIntent(){
        return new Intent(activity,SearchActivity.class);
    }
}
