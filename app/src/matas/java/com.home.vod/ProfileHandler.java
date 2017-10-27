package com.home.vod;

import android.app.Activity;
import android.content.Intent;

import com.home.vod.activity.ProfileActivity;

/**
 * Created by Android on 10/18/2017.
 */

public class ProfileHandler {
    Activity activity;
    public ProfileHandler(Activity activity){
        this.activity = activity;
    }

    public Intent handleClickOnEditProfile(){
        return new Intent(activity, ProfileActivity.class);


    }
}
