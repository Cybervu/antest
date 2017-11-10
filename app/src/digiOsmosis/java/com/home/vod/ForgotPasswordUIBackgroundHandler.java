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
        new RetrieveFeedTask().execute(url);



    }
    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        Bitmap bmp;
        protected Void doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                return null;
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Void feed) {
            Drawable d = new BitmapDrawable(activity.getResources(), bmp);
            activity_forgot_password.setBackground(d);
            forgotPasswordScrollView.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
            forgotPasswordParentLayout.setBackgroundColor(activity.getResources().getColor(R.color.transparent));

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
