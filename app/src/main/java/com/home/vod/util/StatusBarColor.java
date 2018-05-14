package com.home.vod.util;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.home.vod.R;

public class StatusBarColor {

    public static void changeColor(Activity activity,int color){
        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(activity,color));
    }


}
