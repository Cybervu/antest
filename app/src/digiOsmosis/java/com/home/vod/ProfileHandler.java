package com.home.vod;

import android.app.Activity;
import android.content.Intent;

import com.home.vod.activity.DigiOsmosisProfileActivity;

/**
 * Created by Android on 10/18/2017.
 */

public class ProfileHandler {
    Activity activity;
    public ProfileHandler(Activity activity){
        this.activity = activity;
    }

    public Intent handleClickOnEditProfile(){
        return new Intent(activity, DigiOsmosisProfileActivity.class);


    }
}
