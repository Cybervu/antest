package com.home.vod;


import android.app.Activity;
import android.view.View;

import com.home.vod.activity.MainActivity;
import com.home.vod.activity.PPvPaymentInfoActivity;
import com.home.vod.activity.ShowWithEpisodesActivity;

/**
 * Created by Android on 1/10/2018.
 */

public class ToolbarTitleHandler {
    Activity activity;


    public ToolbarTitleHandler(Activity activity){
        this.activity=activity;
        try {
            if (activity instanceof MainActivity){
                ((MainActivity)activity).getSupportActionBar().setTitle("");
                ((MainActivity)activity).toolbarimage.setVisibility(View.VISIBLE);
            }
            if (activity instanceof PPvPaymentInfoActivity){
                ((PPvPaymentInfoActivity)activity).getSupportActionBar().setTitle("");
                ((PPvPaymentInfoActivity)activity).toolbarimage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
