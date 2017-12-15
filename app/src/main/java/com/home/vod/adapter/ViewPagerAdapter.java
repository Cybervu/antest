package com.home.vod.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


import com.home.vod.activity.FragmentDynamic;
import com.home.vod.model.GetMenuItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by user on 15-03-2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    //private JSONArray jsonArray;
    ArrayList<GetMenuItem> jsonArray;
    public ViewPagerAdapter(FragmentManager manager, ArrayList<GetMenuItem> jsonArray) {
        super(manager);
        this.jsonArray= jsonArray;
    }

    @Override
    public Fragment getItem(int position) {

        String title=null;
        String sectionid=null;
        String studioid=null;
        String languageid=null;
        try {
            title=jsonArray.get(position).getName();
            sectionid=jsonArray.get(position).getSectionId();
            studioid=jsonArray.get(position).getLanguage_id();
            languageid=jsonArray.get(position).getStudioId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FragmentDynamic.getInstance(position,title,sectionid,studioid,languageid);
    }

    @Override
    public int getCount() {
        return jsonArray.size();
    }


    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String san=null;
        try {
            san=jsonArray.get(position).getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return san;

    }




//
//    public View getTabView(int position) {
//        View tab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        TextView tv = (TextView) tab.findViewById(R.id.custom_text);
//        try {
//            tv.setText(jsonArray.getJSONObject(position).getString("title").trim());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return tab;
//    }
}
