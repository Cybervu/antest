package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ShowWithEpisodesActivity;

import static com.home.vod.util.Util.showActivateSubscriptionWatchVideoAleart;

/**
 * Created by Abhishek on 10/3/2017.
 */

//the purpose of create this handler the differnce of purcase brtweeen sony user and muvi user in sony,
// there is no payment in application so by this method we can handle both the things like for sony we show pop up and for other customer we redirect to payment page

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
               if (activity instanceof MovieDetailsActivity)
                   ((MovieDetailsActivity) activity).handleActionForValidateUserPayment(validUserStr, message, subscription_Str);
               if (activity instanceof RegisterActivity)
                   ((RegisterActivity) activity).handleActionForValidateUserPayment(validUserStr, message, subscription_Str);
               if (activity instanceof LoginActivity)
                   ((LoginActivity) activity).handleActionForValidateUserPayment(validUserStr, message, subscription_Str);

           } catch (ClassCastException e){
               e.printStackTrace();
           } catch (Exception e){
               e.printStackTrace();
           }
        }
    public void handle428Error(String subscription_Str){

        try {

            if (activity instanceof ShowWithEpisodesActivity)
                ((ShowWithEpisodesActivity) activity).handleFor428Status( subscription_Str);
            if (activity instanceof Episode_list_Activity)
                ((Episode_list_Activity) activity).handleFor428Status(subscription_Str);
            if (activity instanceof MovieDetailsActivity)
                ((MovieDetailsActivity) activity).handleFor428Status(subscription_Str);
            if (activity instanceof RegisterActivity)
                ((RegisterActivity) activity).handleFor428Status(subscription_Str);
            if (activity instanceof LoginActivity)
                ((LoginActivity) activity).handleFor428Status(subscription_Str);

        } catch (ClassCastException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    }