package com.home.vod;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.home.apisdk.apiModel.AppHomePageOutput;
import com.home.vod.fragment.HomeFragment;

import static com.google.android.gms.internal.zzng.fm;


/**
 * Created by BISHAL on 11-12-2017.
 */

public class HomePageHandler extends FragmentActivity {
    Context context;
    TabLayout tabLayout;
     ViewPager viewPager;
    TextView line;
    HomeFragment homeFragment;

    public  HomePageHandler(Context context,View rootView,HomeFragment homeFragment){
        this.context=context;
        this.homeFragment=homeFragment;
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        line = (TextView) rootView.findViewById(R.id.lineTextView2);

       /* homeFragment=new HomeFragment();
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
       *//* FragmentManager f =getSupportFragmentManager();
        FragmentManager fk = MainActivity.this.getSupportFragmentManager();
        String currentFragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();*/
    }
    public void viewpagerOrNormalHomepage(AppHomePageOutput appHomePageOutput,int status){
        homeFragment.ViewpagerAppHomepageLoad(appHomePageOutput,status,tabLayout,viewPager,line);

    }
}
