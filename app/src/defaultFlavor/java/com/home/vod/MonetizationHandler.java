package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.LoginActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.ShowWithEpisodesActivity;
import com.home.vod.preferences.LanguagePreference;

import static com.home.vod.preferences.LanguagePreference.ACCESS_PERIOD_EXPIRED;
import static com.home.vod.preferences.LanguagePreference.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.APP_ON;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ACCESS_PERIOD_EXPIRED;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_ON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.util.Util.showActivateSubscriptionWatchVideoAleart;

/**
 * Created by Abhishek on 10/3/2017.
 */

//the purpose of create this handler the differnce of purcase brtweeen sony user and muvi user in sony,
// there is no payment in application so by this method we can handle both the things like for sony we show pop up and for other customer we redirect to payment page

public class MonetizationHandler {

        Activity activity;
    LanguagePreference languagePreference;

        public MonetizationHandler(Activity activity) {
            this.activity = activity;
            languagePreference = LanguagePreference.getLanguagePreference(activity);
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


        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);

        dlgAlert.setMessage(languagePreference.getTextofLanguage(ACCESS_PERIOD_EXPIRED, DEFAULT_ACCESS_PERIOD_EXPIRED) + " " + languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + activity.getResources().getString(R.string.studio_site));


        dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY));
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                    }
                });
        dlgAlert.create().show();



    }

    }