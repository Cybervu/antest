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

public class RegisterUIBackgroundHandler {

    Activity activity;
    RelativeLayout mainLayout;
    ScrollView registerScrollView;
    RelativeLayout registerParentLayout;

    public RegisterUIBackgroundHandler(Activity activity){
        this.activity=activity;
    }

    public void handleBackgroundOfLayout(String url,RelativeLayout mainLayout,ScrollView registerScrollView,RelativeLayout registerParentLayout){
        this.mainLayout = mainLayout;
        this.registerScrollView = registerScrollView;
        this.registerParentLayout = registerParentLayout;
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
            mainLayout.setBackground(d);
//            registerScrollView.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
            registerParentLayout.setBackgroundColor(activity.getResources().getColor(R.color.transparent));

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }
}
