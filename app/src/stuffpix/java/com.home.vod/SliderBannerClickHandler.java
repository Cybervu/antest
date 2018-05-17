package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.home.vod.activity.LoginActivity;

/**
 * Created by BISHAL on 17-05-2018.
 */

public class SliderBannerClickHandler {
    Context context;
    public SliderBannerClickHandler(Context context){
        this.context=context;
    }
    public void handleClickOnBanner(){
        Intent bannerviewintent =new Intent("android.intent.action.VIEW",
                Uri.parse("https://www.stuffpix.co.nz/new-pix "));
        context.startActivity(bannerviewintent);
    }
}
