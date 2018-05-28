package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

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
       /* Intent bannerviewintent =new Intent("android.intent.action.VIEW",
                Uri.parse("https://www.muvinow.com.au/new-releases"));
        context.startActivity(bannerviewintent);*/

        Fragment fragment = new VideosListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","New Releases");
        bundle.putString("item", "new-releases");
        fragment.setArguments(bundle);
        ((MainActivity) context ).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

    }
}
