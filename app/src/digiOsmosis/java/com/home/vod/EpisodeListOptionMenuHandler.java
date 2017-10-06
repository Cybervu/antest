package com.home.vod;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.DEFAULT_IS_CHROMECAST;
import static player.utils.Util.DEFAULT_IS_OFFLINE;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.IS_CHROMECAST;
import static player.utils.Util.IS_OFFLINE;


/**
 * Created by MUVI on 9/25/2017.
 */

public class EpisodeListOptionMenuHandler {

    Activity activity;

    public EpisodeListOptionMenuHandler(Activity activity) {
        this.activity = activity;

    }


    public void createOptionMenu(Menu menu, PreferenceManager preferenceManager, LanguagePreference languagePreference) {

        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        int isLogin = preferenceManager.getLoginFeatureFromPref();

        MenuItem filter_menu, profile_menu, purchage_menu, logout_menu,
                login_menu, register_menu, mydownload_menu, favorite_menu, mediaRouteMenuItem, menu_language,action_searchmenu;

        filter_menu = menu.findItem(R.id.action_filter);
        menu_language = menu.findItem(R.id.menu_item_language);
        login_menu = menu.findItem(R.id.action_login);
        profile_menu = menu.findItem(R.id.menu_item_profile);
        purchage_menu = menu.findItem(R.id.action_purchage);
        logout_menu = menu.findItem(R.id.action_logout);
        register_menu = menu.findItem(R.id.action_register);
        mydownload_menu = menu.findItem(R.id.action_mydownload);
        favorite_menu = menu.findItem(R.id.menu_item_favorite);
        action_searchmenu=menu.findItem(R.id.action_search);

        /***************chromecast**********************/

        CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu, R.id.media_route_menu_item);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu,
                R.id.media_route_menu_item);
        /***************chromecast**********************/

        login_menu.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));
        register_menu.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
        profile_menu.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
        logout_menu.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
        mydownload_menu.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
        purchage_menu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
        favorite_menu.setTitle(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));

        filter_menu.setVisible(false);
        if ((languagePreference.getTextofLanguage(IS_CHROMECAST, DEFAULT_IS_CHROMECAST).trim()).equals("1"))
            mediaRouteMenuItem.setVisible(true);
        else
            mediaRouteMenuItem.setVisible(false);

        if (preferenceManager.getLanguageListFromPref().equals("1"))
            menu_language.setVisible(false);
        else
            menu_language.setVisible(true);


        if (loggedInStr != null) {

            login_menu.setVisible(false);
            register_menu.setVisible(false);
            profile_menu.setVisible(true);


            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE).trim()).equals("1"))
                favorite_menu.setVisible(true);
            else
                favorite_menu.setVisible(false);

            purchage_menu.setVisible(true);

            logout_menu.setVisible(true);

            if ((languagePreference.getTextofLanguage(IS_OFFLINE, DEFAULT_IS_OFFLINE)
                    .trim()).equals("1"))
                mydownload_menu.setVisible(true);
            else
                mydownload_menu.setVisible(false);


        } else if (loggedInStr == null) {


            if (isLogin == 1) {

                login_menu.setVisible(true);
                register_menu.setVisible(true);

            } else {
                login_menu.setVisible(false);
                register_menu.setVisible(false);

            }

            profile_menu.setVisible(false);
            purchage_menu.setVisible(false);
            logout_menu = menu.findItem(R.id.action_logout);
            logout_menu.setVisible(false);
            mydownload_menu.setVisible(false);
            favorite_menu.setVisible(false);

        }
    }
}