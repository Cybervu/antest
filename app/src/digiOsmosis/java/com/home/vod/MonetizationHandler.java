package com.home.vod;

import android.app.Activity;

import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.ShowWithEpisodesActivity;

/**
 * Created by MUVI on 10/3/2017.
 */

public class MonetizationHandler {

    Activity activity;

    public MonetizationHandler(Activity activity) {
        this.activity = activity;
    }

    public void handle429OR430statusCod(String validUserStr,String message, String subscription_Str) {
        try {

            if (activity instanceof ShowWithEpisodesActivity)
                ((ShowWithEpisodesActivity) activity).handleActionForValidateUserPayment(validUserStr, message, subscription_Str);
            if (activity instanceof Episode_list_Activity)
                ((Episode_list_Activity) activity).handleActionForValidateUserPayment(validUserStr, message, subscription_Str);


        } catch (ClassCastException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
