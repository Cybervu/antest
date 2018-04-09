package com.home.vod;

import android.app.Activity;
import android.support.v7.app.MediaRouteButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.home.vod.util.FeatureHandler;

/**
 * Created by Android on 12/19/2017.
 */

public class SplashScreenHandler {

    private Activity context;

    public SplashScreenHandler(Activity context) {
        this.context = context;
    }


    public void handleSplashscreen(ImageView imageResize) {

        try {
            imageResize.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } catch (Exception e) {
        }
    }


    public void changeFeatureProperties(FeatureHandler featureHandler){
        featureHandler.setFeatureFlag(FeatureHandler.IS_OFFLINE,"0");
    }


}
