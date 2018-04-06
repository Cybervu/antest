package com.home.vod;

import android.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FeatureHandler;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_HAS_FAVORITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.HAS_FAVORITE;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static player.utils.Util.DEFAULT_IS_CHROMECAST;
import static player.utils.Util.DEFAULT_IS_OFFLINE;
import static player.utils.Util.IS_CHROMECAST;
import static player.utils.Util.IS_OFFLINE;


/**
 * Created by Abhishek on 9/25/2017.
 */

public class EpisodeListOptionMenuHandler {

    Activity activity;
    FeatureHandler featureHandler;

    // Kushal
    boolean[] visibility;
    private int LOGIN_INDEX = 0;
    private int REGISTER_INDEX = 1;
    private int LANGUAGE_INDEX = 2;
    private int PROFILE_INDEX = 3;
    private int PURCHASE_INDEX = 4;
    private int LOGOUT_INDEX = 5;
    MenuItem filter_menu, profile_menu, purchage_menu, logout_menu,
            login_menu, register_menu, mydownload_menu, favorite_menu, mediaRouteMenuItem, menu_language, action_searchmenu, submenu;
//


    public EpisodeListOptionMenuHandler(Activity activity) {
        this.activity = activity;

    }


    public boolean[] createOptionMenu(Menu menu, PreferenceManager preferenceManager, LanguagePreference languagePreference, FeatureHandler featureHandler) {

        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        int isLogin = preferenceManager.getLoginFeatureFromPref();


        filter_menu = menu.findItem(R.id.action_filter);
        menu_language = menu.findItem(R.id.menu_item_language);
        login_menu = menu.findItem(R.id.action_login);
        menu_language= (menu.findItem(R.id.menu_item_language)).setVisible(false);
        profile_menu = menu.findItem(R.id.menu_item_profile);
        purchage_menu = menu.findItem(R.id.action_purchage);
        logout_menu = menu.findItem(R.id.action_logout);
        register_menu = menu.findItem(R.id.action_register);
        mydownload_menu = menu.findItem(R.id.action_mydownload);
        favorite_menu = menu.findItem(R.id.menu_item_favorite);
        action_searchmenu = menu.findItem(R.id.action_search);
        submenu = menu.findItem(R.id.submenu);

        /***************chromecast**********************/

        CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu, R.id.media_route_menu_item);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu,
                R.id.media_route_menu_item);
        /***************chromecast**********************/

        menu_language.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LANGUAGE, DEFAULT_LANGUAGE_POPUP_LANGUAGE));
        login_menu.setTitle(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN));
        register_menu.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
        profile_menu.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
        mydownload_menu.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
        purchage_menu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
        logout_menu.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
        favorite_menu.setTitle(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
        submenu.setVisible(true);
        // Kushal
        visibility = new boolean[6];

        action_searchmenu.setVisible(true);
        filter_menu.setVisible(false);
        if ((featureHandler.getFeatureStatus(FeatureHandler.CHROMECAST, FeatureHandler.DEFAULT_CHROMECAST)))
            mediaRouteMenuItem.setVisible(true);
        else
            mediaRouteMenuItem.setVisible(false);

        // Kushal
        if (preferenceManager.getLanguageListFromPref().equals("1")) {
            menu_language.setVisible(false);
            visibility[LANGUAGE_INDEX] = false;
        } else {
            menu_language.setVisible(true);
            visibility[LANGUAGE_INDEX] = true;
        }


        if (loggedInStr != null) {

            login_menu.setVisible(false);
            register_menu.setVisible(false);
            profile_menu.setVisible(true);
            logout_menu.setVisible(true);

            // Kushal
            visibility[LOGIN_INDEX] = false;
            visibility[REGISTER_INDEX] = false;
            visibility[PROFILE_INDEX] = true;

            /**
             * This has been modified.
             */
            if ((featureHandler.getFeatureStatus(FeatureHandler.HAS_FAVOURITE, FeatureHandler.DEFAULT_HAS_FAVOURITE)))
                favorite_menu.setVisible(true);
            else
                favorite_menu.setVisible(false);

            purchage_menu.setVisible(true);
            logout_menu.setVisible(true);
            // Kushal
            visibility[PURCHASE_INDEX] = true;
            visibility[LOGOUT_INDEX] = true;

            if ((featureHandler.getFeatureStatus(FeatureHandler.IS_OFFLINE, FeatureHandler.DEFAULT_IS_OFFLINE)))
                mydownload_menu.setVisible(true);
            else
                mydownload_menu.setVisible(false);


        } else if (loggedInStr == null) {

            if (isLogin == 1) {

                login_menu.setVisible(true);
                register_menu.setVisible(true);
                // Kushal
                visibility[LOGIN_INDEX] = true;
                visibility[REGISTER_INDEX] = true;

            } else {
                login_menu.setVisible(false);
                register_menu.setVisible(false);
                //Kushal
                visibility[LOGIN_INDEX] = false;
                visibility[REGISTER_INDEX] = false;

            }

            profile_menu.setVisible(false);
            purchage_menu.setVisible(false);
            logout_menu.setVisible(false);
            mydownload_menu.setVisible(false);
            favorite_menu.setVisible(false);
            //Kushal
            visibility[PROFILE_INDEX] = false;
            visibility[PURCHASE_INDEX] = false;
            visibility[LOGOUT_INDEX] = false;

        }
        makeOldMenuInvisible();
        return visibility;
    }
    private void makeOldMenuInvisible() {
        login_menu.setVisible(false);
        register_menu.setVisible(false);
        profile_menu.setVisible(false);
        purchage_menu.setVisible(false);
        logout_menu.setVisible(false);
        mydownload_menu.setVisible(false);
        favorite_menu.setVisible(false);
        menu_language.setVisible(false);
    }
}
