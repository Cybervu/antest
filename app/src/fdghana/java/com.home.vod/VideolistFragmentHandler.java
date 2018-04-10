package com.home.vod;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by MUVI on 10/27/2017.
 */

public class VideolistFragmentHandler {
    private Activity context;

    public VideolistFragmentHandler(Activity context) {
        this.context = context;

    }

    public void handleMenuFilter(Menu menu) {
        MenuItem item;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(true);
    }
}
