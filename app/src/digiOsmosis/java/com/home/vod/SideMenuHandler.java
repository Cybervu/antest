package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.vod.activity.DigiOsmosisProfileActivity;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.vod.activity.DigiOsmosisProfileActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.ProfileActivity;
import com.home.vod.activity.Notification;
import com.home.vod.activity.ProfileActivity;
import com.home.vod.activity.ProgramDetailsActivity;
import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.model.NavDrawerItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.home.vod.util.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NOTIFICATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.NOTIFICATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.HAS_FAVORITE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;

/**
 * Created by Android on 10/5/2017.
 */

public class SideMenuHandler {

    int screenSize;
    Activity context;
    MainActivity mainActivity;
    boolean value = true;
    boolean login_value = false;
    int adding_position ;
    public ArrayList<NavDrawerItem> originalMenuList = new ArrayList<>();

    String favourite_menu,favourite_menuPermalink, login_menu,register_menu,profile_menu,mydownload_menu,purchase_menu,logout_menu,login_menuPermalink,register_menuPermalink,profile_menuPermalink,mydownload_menuPermalink,purchase_menuPermalink,logout_menuPermalink;
    LanguagePreference languagePreference;

    TextView nameText,badge,badge1,logoutTextView,notificationTextView,noLoginNotificationTextView;
    ImageView editPen,profile_image,bannerImageView;
    LinearLayout layout1,layout2,notification,logout;

    public SideMenuHandler(Activity context) {
        this.context = context;
    }

    public SideMenuHandler(Activity activity, PreferenceManager preferenceManager) {
        this.context = activity;

        languagePreference = LanguagePreference.getLanguagePreference(context);
        editPen = (ImageView) context.findViewById(R.id.edit_profile);
        nameText = (TextView) context.findViewById(R.id.edit_name);
        profile_image = (ImageView) context.findViewById(R.id.logo);
        bannerImageView = (ImageView) context.findViewById(R.id.bannerImageView);
       Log.v("BKS","profile=="+preferenceManager.getDispNameFromPref());
        nameText.setText(preferenceManager.getDispNameFromPref());
        layout1 = (LinearLayout) context.findViewById(R.id.layout_1);
        layout2 = (LinearLayout) context.findViewById(R.id.layout_2);
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        notification = (LinearLayout) context.findViewById(R.id.notification);
        logoutTextView = (TextView) context.findViewById(R.id.logoutTextView);
        notificationTextView = (TextView) context.findViewById(R.id.notificationTextView);
        noLoginNotificationTextView = (TextView) context.findViewById(R.id.noLoginNotificationTextView);
        logout = (LinearLayout) context.findViewById(R.id.logout);
        badge = (TextView) context.findViewById(R.id.badge);
        badge1 = (TextView) context.findViewById(R.id.badge);
        final DrawerLayout drawerLayout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
       /* LayerDrawable icon2 = (LayerDrawable) layout2.getIcon();
        // Update LayerDrawable's BadgeDrawable
        BadgeCount.setBadgeCount(this, icon2, preferenceManager.getNOTI_COUNT());
*/

        notificationTextView.setText(languagePreference.getTextofLanguage(NOTIFICATION_TITLE,DEFAULT_NOTIFICATION_TITLE));
        noLoginNotificationTextView.setText(languagePreference.getTextofLanguage(NOTIFICATION_TITLE,DEFAULT_NOTIFICATION_TITLE));
        logoutTextView.setText(languagePreference.getTextofLanguage(LOGOUT,DEFAULT_LOGOUT));

       /* BadgeView badge = new BadgeView(this, layout2);
        badge.setText("1");
        badge.show();*/

        editPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent navIntent = new ProfileHandler(context).handleClickOnEditProfile();
                context.startActivity(navIntent);


            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(context,Notification.class);
                context.startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                    Intent intent = new Intent(context, Notification.class);
                context.startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).logout();
            }
        });



        badge.setText(String.valueOf(preferenceManager.getNOTI_COUNT()));
        badge1.setText(String.valueOf(preferenceManager.getNOTI_COUNT()));



    }





    public void staticSideMenu(LanguagePreference languagePreference, ArrayList<NavDrawerItem> menuList, ArrayList<NavDrawerItem> originalMenuList, PreferenceManager preferenceManager,int adding_position) {

        if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE){
            screenSize=3;
            Log.v("pratik","smh large");
        }else if((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL){
            screenSize=2;
            Log.v("pratik","smh normal");
        }else  if((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL){

            screenSize=1;
            Log.v("pratik","smh small");
        }else{
            screenSize=0;
            Log.v("pratik","smh nthng");
        }



        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        int isLogin = preferenceManager.getLoginFeatureFromPref();
        Log.v("ANU","loggedInStr"+loggedInStr);
        Log.v("ANU","isLogin"+isLogin);

        this.adding_position = adding_position;
        this.originalMenuList = originalMenuList;

        Log.v("BIBHU12","menuList size="+menuList.size());
        Log.v("BIBHU12","originalMenuList size="+this.originalMenuList.size());


        login_menuPermalink = "login_permalink";
        register_menuPermalink = "register_permalink";
        profile_menuPermalink = "profile_Permalink";
        mydownload_menuPermalink = "mydownload_Permalink";
        purchase_menuPermalink = "purchase_Permalink";
        logout_menuPermalink = "logout_Permalink";
        favourite_menuPermalink = "favourite_Permalink";
        logout_menu = languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT);

        login_menu = languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN);
        register_menu =languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER);
        profile_menu = languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE);
        favourite_menu=languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE);
        mydownload_menu = languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD);
        purchase_menu = languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY);


        if(menuList!=null && menuList.size()>0){
            menuList.clear();
            menuList.addAll(originalMenuList);
        }

        Log.v("BIBHU12","menuList size="+menuList.size());
        Log.v("BIBHU12","originalMenuList size="+this.originalMenuList.size());


            if (loggedInStr != null) {

                    menuList.add(adding_position,new NavDrawerItem(profile_menu, profile_menuPermalink, true, "internal"));
                    menuList.add(adding_position+1,new NavDrawerItem(purchase_menu, purchase_menuPermalink, true, "internal"));
                    menuList.add(adding_position+2,new NavDrawerItem(mydownload_menu, mydownload_menuPermalink, true, "internal"));
                if ((screenSize == 1 || screenSize == 2) && ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE).trim()).equals("1"))){
                    menuList.add(adding_position + 3, new NavDrawerItem(favourite_menu, favourite_menuPermalink, true, "internal"));

                }
                    menuList.add(new NavDrawerItem(logout_menu, logout_menuPermalink, true, "internal"));

            }

        else{

            if (isLogin == 1) {
                    menuList.add(adding_position,new NavDrawerItem(login_menu, login_menuPermalink, true, "internal"));
//                    menuList.add(adding_position+1,new NavDrawerItem(register_menu, register_menuPermalink, true, "internal"));
            }

        }



        if (loggedInStr!= null) {
            Log.v("ANU","loggedInStr===="+loggedInStr);
            String PIMG = preferenceManager.getLoginProfImgFromPref();
            Log.v("ANU","getLoginProfImgFromPref===="+PIMG);

            if (preferenceManager.getLoginProfImgFromPref() != null && !(preferenceManager.getLoginProfImgFromPref().equalsIgnoreCase("https://d1yjifjuhwl7lc.cloudfront.net/public/no-user.png"))) {
                Log.v("ANU","sidemenu  if not null====");

                Picasso.with(context)
                        .load(preferenceManager.getLoginProfImgFromPref())
                        .into(profile_image);

            }
            else {
                Log.v("ANU","sidemenu else====");

                Picasso.with(context)
                        .load(R.drawable.profile)
                        .into(profile_image);

            }


            editPen.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
        }

        else {


            Picasso.with(context)
                    .load(R.drawable.profile)
                    .into(profile_image);


            editPen.setVisibility(View.INVISIBLE);
            nameText.setVisibility(View.INVISIBLE);
        }





        if (loggedInStr!= null) {

//            layout1.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            Log.v("ANU","loggedInStr===="+loggedInStr);
            String PIMG = preferenceManager.getLoginProfImgFromPref();
            Log.v("ANU","getLoginProfImgFromPref===="+PIMG);

            if (preferenceManager.getLoginProfImgFromPref() != null && !(preferenceManager.getLoginProfImgFromPref().equalsIgnoreCase("https://d1yjifjuhwl7lc.cloudfront.net/public/no-user.png"))) {
                Log.v("ANU","sidemenu  if not null====");

                Picasso.with(context)
                        .load(preferenceManager.getLoginProfImgFromPref())
                        .into(profile_image);

            }
            else {
                Log.v("ANU","sidemenu else====");

                Picasso.with(context)
                        .load(R.drawable.profile)
                        .into(profile_image);

            }


            editPen.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
        }

        else {

            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
//            layout2.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(R.drawable.profile)
                    .into(profile_image);


            editPen.setVisibility(View.INVISIBLE);
            nameText.setVisibility(View.INVISIBLE);
        }







    }

    public void addLogoutMenu( LanguagePreference languagePreference, ArrayList<NavDrawerItem> menuList, PreferenceManager preferenceManager,int position) {

        String loggedInStr = preferenceManager.getLoginStatusFromPref();


        if (loggedInStr != null) {
                menuList.add(position,new NavDrawerItem(logout_menu, logout_menuPermalink, true, "internal"));
        }


    }


    public void sendBroadCast()
    {
        Intent Sintent = new Intent("LOGIN_SUCCESS");
        context.sendBroadcast(Sintent);
    }

    public void openDrawer(View drawerView) {
    }


}
