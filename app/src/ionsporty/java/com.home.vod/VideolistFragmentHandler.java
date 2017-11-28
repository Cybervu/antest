package com.home.vod;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by MUVI on 10/27/2017.
 */

public class VideolistFragmentHandler {
    private Activity context;

    public VideolistFragmentHandler(Activity context){
        this.context=context;

    }

    public void handleMenuFilter(Menu menu){

        MenuItem item,itemsearch;
        item= menu.findItem(R.id.action_filter);
       // itemsearch=menu.findItem(R.id.action_search);
         item.setVisible(true);
        //itemsearch.setVisible(true);


    }
}
