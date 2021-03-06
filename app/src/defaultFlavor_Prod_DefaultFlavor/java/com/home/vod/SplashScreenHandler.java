package com.home.vod;

import android.app.Activity;
import android.widget.ImageView;

import com.home.vod.util.FeatureHandler;


/**
 * Created by Android on 12/19/2017.
 */

public class SplashScreenHandler {

    private Activity context;
    public SplashScreenHandler(Activity context){
        this.context=context;
    }
    public void handleSplashscreen(ImageView imageResize) {
        imageResize.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void changeFeatureProperties(FeatureHandler featureHandler){
        featureHandler.setFeatureFlag(FeatureHandler.FACEBOOK,"1");
        featureHandler.setFeatureFlag(FeatureHandler.GOOGLE,"1");
    }

}
