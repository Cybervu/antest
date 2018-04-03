package com.home.vod;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.FavoriteActivity;
import com.home.vod.activity.LoginActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MyDownloads;
import com.home.vod.activity.ProfileActivity;
import com.home.vod.activity.PurchaseHistoryActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.SearchActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.LogUtil;
import com.home.vod.util.Util;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.YES;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.languageModel;
import static playerOld.utils.Util.DEFAULT_IS_CHROMECAST;
import static playerOld.utils.Util.DEFAULT_IS_OFFLINE;
import static playerOld.utils.Util.IS_CHROMECAST;
import static playerOld.utils.Util.IS_OFFLINE;


/**
 * Created by Abhishek on 9/25/2017.
 */

public class EpisodeListOptionMenuHandler {

    Activity activity;

    public EpisodeListOptionMenuHandler(Activity activity) {
        this.activity = activity;

    }


    public void createOptionMenu(Menu menu, PreferenceManager preferenceManager, LanguagePreference languagePreference, FeatureHandler featureHandler) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        int isLogin = preferenceManager.getLoginFeatureFromPref();

        MenuItem filter_menu, profile_menu, purchage_menu, logout_menu,
                login_menu, register_menu, mydownload_menu, favorite_menu, mediaRouteMenuItem, menu_language, action_searchmenu, submenu;
        filter_menu = menu.findItem(R.id.action_filter);
        filter_menu.setVisible(false);
        login_menu = menu.findItem(R.id.action_login);
        (menu.findItem(R.id.menu_item_language)).setVisible(false);
        profile_menu = menu.findItem(R.id.menu_item_profile);
        purchage_menu = menu.findItem(R.id.action_purchage);
        logout_menu = menu.findItem(R.id.action_logout);
        register_menu = menu.findItem(R.id.action_register);
        action_searchmenu = menu.findItem(R.id.action_search);
        submenu = menu.findItem(R.id.submenu);

        /***************chromecast**********************/

        CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu, R.id.media_route_menu_item);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu,
                R.id.media_route_menu_item);
        /***************chromecast**********************/

        login_menu.setTitle(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN));
        register_menu.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
        profile_menu.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
        purchage_menu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
        logout_menu.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
        submenu.setVisible(true);
        action_searchmenu.setVisible(true);
        purchage_menu.setVisible(false);
        filter_menu.setVisible(false);
        if ((featureHandler.getFeatureStatus(FeatureHandler.CHROMECAST, FeatureHandler.DEFAULT_CHROMECAST)))
            mediaRouteMenuItem.setVisible(true);
        else
            mediaRouteMenuItem.setVisible(false);


        if (loggedInStr != null) {

            login_menu.setVisible(false);
            register_menu.setVisible(false);
            profile_menu.setVisible(true);


            logout_menu.setVisible(true);


        } else if (loggedInStr == null) {

            if (isLogin == 1) {

                login_menu.setVisible(true);
                register_menu.setVisible(true);

            } else {
                login_menu.setVisible(false);
                register_menu.setVisible(false);

            }

            profile_menu.setVisible(false);

            logout_menu = menu.findItem(R.id.action_logout);
            logout_menu.setVisible(false);

        }
    }
}
