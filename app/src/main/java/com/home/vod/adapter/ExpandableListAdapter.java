package com.home.vod.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.apisdk.apiModel.MenusOutputModel;
import com.home.vod.R;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;
import com.home.vod.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

import static com.home.vod.preferences.LanguagePreference.ABOUT_US;
import static com.home.vod.preferences.LanguagePreference.CONTACT_US;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ABOUT_US;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTACT_US;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_HOME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_WATCH_HISTORY;
import static com.home.vod.preferences.LanguagePreference.HOME;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.WATCH_HISTORY;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> expandableListTitle;
    private ArrayList<String> idArrayList;
    private HashMap<String, ArrayList<String>> expandableListDetail;
    private ArrayList<MenusOutputModel.MainMenu> mainMenuModelArrayList;
    // add by subha
    private ArrayList<MenusOutputModel.FooterMenu> footerMenuModelArrayList;


    public ExpandableListAdapter(Context context, ArrayList<String> idArrayList,ArrayList<String> expandableListTitle,
                                 HashMap<String, ArrayList<String>> expandableListDetail, ArrayList<MenusOutputModel.MainMenu> mainMenuModelArrayList, ArrayList<MenusOutputModel.FooterMenu> footerMenuModelArrayList) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.mainMenuModelArrayList = mainMenuModelArrayList;
        this.footerMenuModelArrayList = footerMenuModelArrayList;
        this.idArrayList=idArrayList;

    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.idArrayList.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item1, null);

        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), expandedListTextView);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.idArrayList.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.idArrayList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView1, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        final ImageView iconimage, iconimage1;
        int totalposition = listPosition;
//        Util.drawer_collapse_expand_imageview.clear();

        View convertView = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
            //convertView = layoutInflater.inflate(R.layout.nav_drawer_row, null);
        }




        TextView textViewLine = (TextView) convertView.findViewById(R.id.textViewLine);

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.NORMAL);
        listTitleTextView.setText(Html.fromHtml(listTitle));

        // Kushal - set id to the layout
        LanguagePreference languagePreference = LanguagePreference.getLanguagePreference(context);

        if(convertView instanceof LinearLayout){
            LinearLayout l = (LinearLayout) convertView;
            if (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(CONTACT_US,DEFAULT_CONTACT_US)))
                l.setId(R.id.contact_us);
            else  if (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(HOME,DEFAULT_HOME)))
                l.setId(R.id.home);
            else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(ABOUT_US,DEFAULT_ABOUT_US))){
                l.setId(R.id.about_us);
            }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(WATCH_HISTORY,DEFAULT_WATCH_HISTORY))){
                l.setId(R.id.watch_history);
            }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(MY_DOWNLOAD,DEFAULT_MY_DOWNLOAD))){
                l.setId(R.id.my_download);
            }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(MY_FAVOURITE,DEFAULT_MY_FAVOURITE))){
                l.setId(R.id.my_favourite);
            }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(MY_LIBRARY,DEFAULT_MY_LIBRARY))){
                l.setId(R.id.my_library);
            }
        }




        /*LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.layout);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View v = ll.getChildAt(i);
            if (v instanceof LinearLayout) {
                LinearLayout l = (LinearLayout) v;
                if (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(CONTACT_US,DEFAULT_CONTACT_US)))
                    l.setId(R.id.contact_us);
                else  if (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(HOME,DEFAULT_HOME)))
                    l.setId(R.id.home);
                else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(ABOUT_US,DEFAULT_ABOUT_US))){
                    l.setId(R.id.about_us);
                }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(WATCH_HISTORY,DEFAULT_WATCH_HISTORY))){
                    l.setId(R.id.watch_history);
                }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(MY_DOWNLOAD,DEFAULT_MY_DOWNLOAD))){
                    l.setId(R.id.my_download);
                }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(MY_FAVOURITE,DEFAULT_MY_FAVOURITE))){
                    l.setId(R.id.my_favourite);
                }else if  (listTitleTextView.getText().toString().equals(languagePreference.getTextofLanguage(MY_LIBRARY,DEFAULT_MY_LIBRARY))){
                    l.setId(R.id.my_library);
                }
            }
        }*/

        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), listTitleTextView);

        iconimage = (ImageView) convertView.findViewById(R.id.submenu);
        iconimage1 = (ImageView) convertView.findViewById(R.id.iconimage1);
        //listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, isExpanded ? 0 : android.R.drawable.ic_menu_more, 0);


        if (expandableListDetail.get(this.idArrayList.get(listPosition)).size() > 0) {
            iconimage.setVisibility(View.VISIBLE);
            Log.v("SUBHA", "iconimage visible Position ===== " + listPosition);
        }

        //for expand less and expand the child content
        //***for this we have clear drawer_collapse in splashscreen and create a arraylist for imageview which is declare in Util
        //****in NavigationdrawerFragment we have two method ongroupcollapse and ongroupexpand ther we written logic( expnad and less)
        boolean add_to_array = true;


        for (int k = 0; k < Util.drawer_collapse_expand_imageview.size(); k++) {
            String Data[] = Util.drawer_collapse_expand_imageview.get(k).split(",");
            if (listPosition == Integer.parseInt(Data[0])) {
                add_to_array = false;
            }
        }
        if (add_to_array) {
            Util.drawer_collapse_expand_imageview.add(listPosition + "," + Util.image_compressed);
        }

/*

            String expand_collapse_image_info1[] = Util.drawer_collapse_expand_imageview.get(listPosition).split(",");
            Log.v("SUBHA1","inside adapter===Data=========="+expand_collapse_image_info1[0]+","+expand_collapse_image_info1[1]);

*/

        try {
            String expand_collapse_image_info[] = Util.drawer_collapse_expand_imageview.get(listPosition).split(",");
            if (listPosition == Integer.parseInt(expand_collapse_image_info[0]) && Integer.parseInt(expand_collapse_image_info[1]) == 1) {
                iconimage.setImageResource(R.drawable.ic_remove_black_24dp);

                Log.v("SUBHA1", "image expanded =" + expand_collapse_image_info[0] + "," + expand_collapse_image_info[1]);
            } else if (listPosition == Integer.parseInt(expand_collapse_image_info[0]) && Integer.parseInt(expand_collapse_image_info[1]) == 0) {
                iconimage.setImageResource(R.drawable.ic_add_black_24dp);

                Log.v("SUBHA1", "image collapsed =" + expand_collapse_image_info[0] + "," + expand_collapse_image_info[1]);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


//*************underline where the mainmenu arraylist data finish*************

        if (footerMenuModelArrayList.size() > 0) {
            if (listPosition == mainMenuModelArrayList.size()) {
                textViewLine.setVisibility(View.VISIBLE);
            }
        }

        return convertView;

    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}