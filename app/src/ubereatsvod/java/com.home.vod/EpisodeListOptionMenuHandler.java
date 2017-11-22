package com.home.vod;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.FavoriteActivity;
import com.home.vod.activity.LoginActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.MyDownloads;
import com.home.vod.activity.ProfileActivity;
import com.home.vod.activity.PurchaseHistoryActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.SearchActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.CustomTypefaceSpan;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.Util;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.YES;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.languageModel;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.DEFAULT_IS_CHROMECAST;
import static player.utils.Util.DEFAULT_IS_OFFLINE;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.IS_CHROMECAST;
import static player.utils.Util.IS_OFFLINE;


/**
 * Created by MUVI on 9/25/2017.
 */

public class EpisodeListOptionMenuHandler{

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
        filter_menu.setVisible(true);
        menu_language = menu.findItem(R.id.menu_item_language);
        login_menu = menu.findItem(R.id.action_login);
        profile_menu = menu.findItem(R.id.menu_item_profile);
        purchage_menu = menu.findItem(R.id.action_purchage);
        logout_menu = menu.findItem(R.id.action_logout);
        register_menu = menu.findItem(R.id.action_register);
        mydownload_menu = menu.findItem(R.id.action_mydownload);
        favorite_menu = menu.findItem(R.id.menu_item_favorite);
        action_searchmenu=menu.findItem(R.id.action_search);
        applyFontToMenuItem(login_menu);
        applyFontToMenuItem(logout_menu);
        applyFontToMenuItem(register_menu);
        applyFontToMenuItem(menu_language);

        /***************chromecast**********************/

        CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu, R.id.media_route_menu_item);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(activity.getApplicationContext(), menu,
                R.id.media_route_menu_item);
        /***************chromecast**********************/

        login_menu.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));
        register_menu.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
        profile_menu.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
        purchage_menu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
        logout_menu.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
        mydownload_menu.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
        purchage_menu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
        favorite_menu.setTitle(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));

        filter_menu.setVisible(false);
        if ((languagePreference.getTextofLanguage(IS_CHROMECAST, DEFAULT_IS_CHROMECAST).trim()).equals("1"))
            mediaRouteMenuItem.setVisible(false);
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
                favorite_menu.setVisible(false);
            else
                favorite_menu.setVisible(false);

            //  purchage_menu.setVisible(true);

            logout_menu.setVisible(true);

            if ((languagePreference.getTextofLanguage(IS_OFFLINE, DEFAULT_IS_OFFLINE)
                    .trim()).equals("1"))
                mydownload_menu.setVisible(false);
            else
                mydownload_menu.setVisible(false);


        } else if (loggedInStr == null) {

            if (isLogin == 1) {

                login_menu.setVisible(true);
                //register_menu.setVisible(true);

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

    private void applyFontToMenuItem(MenuItem mi) {
       // FontUtls.loadFont(activity, activity.getResources().getString(R.string.regular_fonts), videoCastCrewTitleTextView);
        Typeface externalFont = Typeface.createFromAsset(activity.getAssets(),activity.getResources().getString(R.string.fonts));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#da241a")), 0, mNewTitle.length(), 0);
       // mNewTitle.setSpan(new CustomTypefaceSpan("" , externalFont), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
}