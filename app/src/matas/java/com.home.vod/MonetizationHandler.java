package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.ShowWithEpisodesActivity;

import static com.home.vod.util.Util.showActivateSubscriptionWatchVideoAleart;

/**
 * Created by MUVI on 10/3/2017.
 */

public class MonetizationHandler {

        Activity activity;

        public MonetizationHandler(Activity activity) {
            this.activity = activity;
        }

        public void handle429OR430statusCod(String validUserStr,String message, String subscription_Str) {
            showActivateSubscriptionWatchVideoAleart(activity);
        }

    }