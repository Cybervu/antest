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
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.DEFAULT_IS_OFFLINE;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.IS_OFFLINE;

/**
 * Created by Android on 10/5/2017.
 */

public class SideMenuHandler {


    Activity activity;
    MainActivity mainActivity;
    boolean value = true;
    boolean login_value = false;

    String login_menu,register_menu,profile_menu,mydownload_menu,purchase_menu,logout_menu,login_menuPermalink,register_menuPermalink,profile_menuPermalink,mydownload_menuPermalink,purchase_menuPermalink,logout_menuPermalink;


    public SideMenuHandler(Activity activity) {
        this.activity = activity;

    }
//    login_menu= (languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));




    public void staticSideMenu(LanguagePreference languagePreference, ArrayList<NavDrawerItem> menuList, PreferenceManager preferenceManager) {

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        int isLogin = preferenceManager.getLoginFeatureFromPref();
        Log.v("ANU","loggedInStr"+loggedInStr);
        Log.v("ANU","isLogin"+isLogin);

        login_menuPermalink = "login_permalink";
        register_menuPermalink = "register_permalink";
        profile_menuPermalink = "profile_Permalink";
        mydownload_menuPermalink = "mydownload_Permalink";
        purchase_menuPermalink = "purchase_Permalink";

        login_menu = languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN);
        register_menu =languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER);
        profile_menu = languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE);

        mydownload_menu = languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD);
        purchase_menu = languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY);


            if (loggedInStr != null) {


                    menuList.add(new NavDrawerItem(profile_menu, profile_menuPermalink, true, "internal"));
                    menuList.add(new NavDrawerItem(purchase_menu, purchase_menuPermalink, true, "internal"));
                    menuList.add(new NavDrawerItem(mydownload_menu, mydownload_menuPermalink, true, "internal"));


            }

        else{

            if (isLogin == 1) {

                    menuList.add(new NavDrawerItem(login_menu, login_menuPermalink, true, "internal"));
                    menuList.add(new NavDrawerItem(register_menu, register_menuPermalink, true, "internal"));


            }

        }



    }

    public void addLogoutMenu( LanguagePreference languagePreference, ArrayList<NavDrawerItem> menuList, PreferenceManager preferenceManager) {

        String loggedInStr = preferenceManager.getLoginStatusFromPref();

        logout_menuPermalink = "logout_Permalink";
        logout_menu = languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT);
        if (loggedInStr != null) {
                menuList.add(new NavDrawerItem(logout_menu, logout_menuPermalink, true, "internal"));
        }


    }

}
