package com.home.vod.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.home.vod.fragment.FavoritesFragment;
import com.home.vod.fragment.FollowedFragment;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class FavoritePagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public FavoritePagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                FavoritesFragment tab1 = new FavoritesFragment();
                return tab1;
            case 1:
                FollowedFragment tab2 = new FollowedFragment();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        //this is where you set the titles
        switch(position) {
            case 0:
                return "FAVORITES";
            case 1:
                return "FOLLOWED";

        }
        return null;
    }
}


