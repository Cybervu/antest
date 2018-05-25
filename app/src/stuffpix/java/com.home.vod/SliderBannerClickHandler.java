package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.home.vod.activity.LoginActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.fragment.VideosListFragment;

/**
 * Created by BISHAL on 17-05-2018.
 */

public class SliderBannerClickHandler {
    Context context;
    public SliderBannerClickHandler(Context context){
        this.context=context;
    }
    public void handleClickOnBanner(){
        /*Intent bannerviewintent =new Intent("android.intent.action.VIEW",
                Uri.parse("https://www.stuffpix.co.nz/new-pix "));
        context.startActivity(bannerviewintent);*/
        Fragment fragment = new VideosListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","New Pix");
        bundle.putString("item", "new-pix");
        fragment.setArguments(bundle);
        ((MainActivity) context ).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }
}
