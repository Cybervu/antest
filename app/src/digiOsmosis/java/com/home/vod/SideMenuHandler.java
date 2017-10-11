package com.home.vod;

import android.app.Activity;
import android.util.Log;

import com.home.vod.activity.MainActivity;
import com.home.vod.model.NavDrawerItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;

import java.util.ArrayList;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;

/**
 * Created by Android on 10/5/2017.
 */

public class SideMenuHandler {


    Activity activity;
    MainActivity mainActivity;
    boolean value = true;
    boolean login_value = false;
    int adding_position ;
    public ArrayList<NavDrawerItem> originalMenuList = new ArrayList<>();

    String login_menu,register_menu,profile_menu,mydownload_menu,purchase_menu,logout_menu,login_menuPermalink,register_menuPermalink,profile_menuPermalink,mydownload_menuPermalink,purchase_menuPermalink,logout_menuPermalink;


    public SideMenuHandler(Activity activity) {
        this.activity = activity;

    }
//    login_menu= (languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));




    public void staticSideMenu(LanguagePreference languagePreference, ArrayList<NavDrawerItem> menuList, ArrayList<NavDrawerItem> originalMenuList, PreferenceManager preferenceManager,int adding_position) {

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
        logout_menu = languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT);

        login_menu = languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN);
        register_menu =languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER);
        profile_menu = languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE);

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
                    menuList.add(new NavDrawerItem(logout_menu, logout_menuPermalink, true, "internal"));
            }

        else{

            if (isLogin == 1) {
                    menuList.add(adding_position,new NavDrawerItem(login_menu, login_menuPermalink, true, "internal"));
                    menuList.add(adding_position+1,new NavDrawerItem(register_menu, register_menuPermalink, true, "internal"));
            }

        }



    }

    public void addLogoutMenu( LanguagePreference languagePreference, ArrayList<NavDrawerItem> menuList, PreferenceManager preferenceManager,int position) {

        String loggedInStr = preferenceManager.getLoginStatusFromPref();


        if (loggedInStr != null) {
                menuList.add(position,new NavDrawerItem(logout_menu, logout_menuPermalink, true, "internal"));
        }


    }

}
