package com.home.vod;

import android.app.Activity;
import android.view.Display;
import android.widget.ImageView;

import com.home.vod.util.Util;

import static com.home.vod.util.Util.decodeSampledBitmapFromResource;

/**
 * Created by Android on 12/19/2017.
 */

public class SplashScreenHandler {

    private Activity context;
    public SplashScreenHandler(Activity context){
        this.context=context;
    }
    public void handleSplashscreen(ImageView imageResize) {


        if ( Util.isTablet(context)){
            imageResize.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            try {
                handlePhoneSplashUI(imageResize);
            } catch (Exception e) {

            }
        }


    }
    public void handlePhoneSplashUI(ImageView imageResize){
        imageResize.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Display display = context.getWindowManager().getDefaultDisplay();
        float dpHeight = display.getHeight();
        float dpWidth = display.getWidth();
        imageResize.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.drawable.splash1, dpWidth, dpHeight));

    }
}
