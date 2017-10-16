package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.home.vod.activity.ProgramPlayerActivity;

import player.activity.Player;

/**
 * Created by MUVI on 10/13/2017.
 */

public class ProgramPlayerIntentHandler {

    Activity activity;

    public ProgramPlayerIntentHandler(Activity activity){
        this.activity=activity;
    }

    public Intent handlePlayerIntent(){
        return new Intent(activity,ProgramPlayerActivity.class);
    }
}
