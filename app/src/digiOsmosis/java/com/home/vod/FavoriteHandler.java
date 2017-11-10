package com.home.vod;

import android.widget.GridView;

import com.home.vod.activity.FavoriteActivity;
import com.home.vod.adapter.FavoriteAdapter;
import com.home.vod.model.GridItem;

import java.util.ArrayList;

/**
 * Created by MUVI on 10/6/2017.
 */

public class FavoriteHandler {

    FavoriteActivity activity;
    FavoriteAdapter customGridAdapter;
    public FavoriteHandler(FavoriteActivity activity){
        this.activity=activity;
    }
    public void handleTitle() {
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
    public FavoriteAdapter handleLayout(FavoriteAdapter customGridAdapter,ArrayList<GridItem> itemData,GridView gridView,int videoWidth,int videoHeight){
        this.customGridAdapter = customGridAdapter;
        float density = this.activity.getResources().getDisplayMetrics().density;
        this.customGridAdapter = new FavoriteAdapter(this.activity, R.layout.favorite_listing, itemData);
        /*if (videoWidth > videoHeight) {
            if (density >= 3.5 && density <= 4.0) {
                customGridAdapter = new FavoriteAdapter(this.activity, R.layout.nexus_videos_grid_layout_land, itemData);
            } else {
                customGridAdapter = new FavoriteAdapter(this.activity, R.layout.videos_280_grid_layout, itemData);

            }
        } else {
            if (density >= 3.5 && density <= 4.0) {
                customGridAdapter = new FavoriteAdapter(this.activity, R.layout.nexus_videos_grid_layout, itemData);
            } else {
                customGridAdapter = new FavoriteAdapter(this.activity, R.layout.videos_grid_layout, itemData);

            }
        }*/
        return this.customGridAdapter;

    }

}
