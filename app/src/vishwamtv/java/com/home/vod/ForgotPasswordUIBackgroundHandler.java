package com.home.vod;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.net.URL;

/**
 * Created by MUVI on 10/6/2017.
 */

public class ForgotPasswordUIBackgroundHandler {

    Activity activity;
    RelativeLayout activity_forgot_password;
    ScrollView forgotPasswordScrollView;
    LinearLayout forgotPasswordParentLayout;

    public ForgotPasswordUIBackgroundHandler(Activity activity){
        this.activity=activity;
    }

    public void handleBackgroundOfLayout(String url,RelativeLayout activity_forgot_password,ScrollView forgotPasswordScrollView,LinearLayout forgotPasswordParentLayout){
        this.activity_forgot_password = activity_forgot_password;
        this.forgotPasswordScrollView = forgotPasswordScrollView;
        this.forgotPasswordParentLayout = forgotPasswordParentLayout;
        this.activity_forgot_password.setBackgroundColor(activity.getResources().getColor(R.color.appBackgroundColor));
        this.forgotPasswordScrollView.setBackgroundColor(activity.getResources().getColor(R.color.appBackgroundColor));
        this.forgotPasswordParentLayout.setBackgroundColor(activity.getResources().getColor(R.color.appBackgroundColor));


    }

}
