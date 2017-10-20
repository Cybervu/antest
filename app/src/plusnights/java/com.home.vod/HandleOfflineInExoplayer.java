package com.home.vod;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by MUVI on 10/20/2017.
 */

public class HandleOfflineInExoplayer {
    private Activity context;
    public HandleOfflineInExoplayer(Activity context){
        this.context=context;
    }
    public void handleVisibelUnvisibleDownload(RelativeLayout download_layout){

        download_layout.setVisibility(View.GONE);
    }
}
