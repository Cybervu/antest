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

public class LoginUIBackgroundHandler {

    Activity activity;
    RelativeLayout mainLayout;
    ScrollView loginScrollView;
    LinearLayout loginParentLayout;

    public LoginUIBackgroundHandler(Activity activity){
        this.activity=activity;
    }

    public void handleBackgroundOfLayout(String url,RelativeLayout mainLayout,ScrollView loginScrollView,LinearLayout loginParentLayout){
        this.mainLayout = mainLayout;
        this.loginScrollView = loginScrollView;
        this.loginParentLayout = loginParentLayout;
        this.mainLayout.setBackgroundColor(activity.getResources().getColor(R.color.appBackgroundColor));
        this.loginScrollView.setBackgroundColor(activity.getResources().getColor(R.color.appBackgroundColor));
        this.loginParentLayout.setBackgroundColor(activity.getResources().getColor(R.color.appBackgroundColor));


    }

}
