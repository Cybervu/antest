package com.home.vod.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Get_Video_Details_Output;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.vod.R;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.model.DataModel;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ExpandableTextView;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.muvi.player.activity.ExoPlayerActivity;
import com.muvi.player.activity.Player;
import com.muvi.player.activity.ThirdPartyPlayer;
import com.muvi.player.activity.YouTubeAPIActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MovieDetailsActivity extends AppCompatActivity implements LogoutAsynctask.Logout,
        GetValidateUserAsynTask.GetValidateUser, VideoDetailsAsynctask.VideoDetails, GetLanguageListAsynTask.GetLanguageList {
    public static ProgressBarHandler progressBarHandler;
    int prevPosition = 0;

    String filename = "";
    static File mediaStorageDir;

    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ProgressBarHandler pDialog;
    VideoDetailsAsynctask asynLoadVideoUrls;
    GetValidateUserAsynTask asynValidateUserDetails;
    AsynLoadMovieDetails asynLoadMovieDetails;
    Toolbar mActionBarToolbar;
    ImageView moviePoster;
    PPVModel ppvmodel;
    String loggedInIdStr;
    APVModel advmodel;
    CurrencyModel currencymodel;
    ImageView playButton;
    String PlanId = "";
    ImageButton offlineImageButton;
    Button watchTrailerButton;
    Button preorderButton;





    String Default_Language = "";
    String Previous_Selected_Language = "";


    //Add By MUVI Later.
    ExpandableTextView videoStoryTextView;
    TextView videoTitle, videoGenreTextView, videoDurationTextView, videoCensorRatingTextView, videoCensorRatingTextView1, videoReleaseDateTextView, videoCastCrewTitleTextView;
    String movieNameStr;
    String movieTypeStr;
    boolean castStr = false;
    String censorRatingStr = "";
    String videoduration = "";
    String movieDetailsStr = "";
    String Video_Url = "";
    String movieThirdPartyUrl = "";
    private boolean isThirdPartyTrailer = false;



    RatingBar ratingBar;

    Intent DataIntent;
    String permalinkStr;
    String movieTrailerUrlStr, movieStreamUniqueId, bannerImageId, posterImageId, movieReleaseDateStr, priceForUnsubscribedStr, priceFosubscribedStr, currencyIdStr, currencyCountryCodeStr,
            currencySymbolStr;
    String movieUniqueId = "";
    int isFreeContent,isPPV,isConverted,contentTypesId,isAPV;
    PreferenceManager preferenceManager;
    RelativeLayout noInternetConnectionLayout,noDataLayout,iconImageRelativeLayout,bannerImageRelativeLayout;
    LinearLayout story_layout;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    int isLogin = 0;
    TextView noDataTextView;
    TextView noInternetTextView;
    String email,id;
    LanguageCustomAdapter languageCustomAdapter;
    AlertDialog alert;
    String isMemberSubscribed;
    Player playerModel;
    Get_Video_Details_Output get_video_details_output;

    @Override
    protected void onResume() {
        super.onResume();

       /* *//***************chromecast**********************//*
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }



        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        *//***************chromecast**********************/
        invalidateOptionsMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
      /*  *//***************chromecast**********************//*

        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        *//***************chromecast**********************//*
*/
        MenuItem item,item1,item2,item3,item4,item5,item6;
        item= menu.findItem(R.id.action_filter);
        item.setVisible(false);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();

        id = preferenceManager.getUseridFromPref();
        email=preferenceManager.getEmailIdFromPref();


        if(preferenceManager.getLanguageListFromPref().equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);

        if(loggedInStr!=null){
            item4= menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5= menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            item5.setVisible(false);
         /*   item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.PROFILE, Util.DEFAULT_PROFILE));

            item1.setVisible(true);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(true);

            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(true);

        }else if(loggedInStr==null){
            item4= menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5= menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            if(isLogin == 1)
            {
                item4.setVisible(true);
                item5.setVisible(true);

            } else {
                item4.setVisible(false);
                item5.setVisible(false);

            }
            /*item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.PROFILE, Util.DEFAULT_PROFILE));
            item1.setVisible(false);
            item2= menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3= menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(MovieDetailsActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                final Intent loginIntent = new Intent(MovieDetailsActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);

                if (Util.languageModel!=null && Util.languageModel.size() > 0){


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(Util.authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(MovieDetailsActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(MovieDetailsActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.YES, Util.DEFAULT_YES) ,new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(Util.authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                // dlgAlert.setPositiveButton(getResources().getString(R.string.yes_str), null);
                dlgAlert.setCancelable(false);

                dlgAlert.create().show();

                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        if (status == null) {
            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((Util.getTextofLanguage(MovieDetailsActivity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(MovieDetailsActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(MovieDetailsActivity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }
    }

//    private class AsynLogoutDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        int responseCode;
//        String loginHistoryIdStr = pref.getString("PREFS_LOGIN_HISTORYID_KEY", null);
//        String responseStr;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            String urlRouteList =Util.rootUrl().trim()+Util.logoutUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("login_history_id",loginHistoryIdStr);
//                httppost.addHeader("lang_code",Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseCode = 0;
//                            Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                }catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    responseCode = 0;
//                    e.printStackTrace();
//                }
//                if(responseStr!=null){
//                    JSONObject myJson = new JSONObject(responseStr);
//                    responseCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//            }
//            catch (Exception e) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseCode = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//
//                }
//            } catch (IllegalArgumentException ex) {
//                Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if(responseStr == null){
//                Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode == 0) {
//                Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode > 0) {
//                if (responseCode == 200) {
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.clear();
//                    editor.commit();
//                    SharedPreferences loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
//                    if (loginPref!=null) {
//                        SharedPreferences.Editor countryEditor = loginPref.edit();
//                        countryEditor.clear();
//                        countryEditor.commit();
//                    }
//                 /*   SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
//                    if (countryPref!=null) {
//                        SharedPreferences.Editor countryEditor = countryPref.edit();
//                        countryEditor.clear();
//                        countryEditor.commit();
//                    }*/
//                    if ((Util.getTextofLanguage(MovieDetailsActivity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
//                            .trim()).equals("1")) {
//                        final Intent startIntent = new Intent(MovieDetailsActivity.this, SplashScreen.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(startIntent);
//                                Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
//                                finish();
//
//                            }
//                        });
//                    }
//                    else
//                    {
//                        final Intent startIntent = new Intent(MovieDetailsActivity.this, MainActivity.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(startIntent);
//                                Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
//                                finish();
//
//                            }
//                        });
//                    }
//
//                }
//                else {
//                    Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
//            pDialog.show();
//        }
//    }

   /* *//*chromecast-------------------------------------*//*
    View view;



    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    */
    /**
     * List of various states that we can be in
     *//*
    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;


    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(CastSession session) {
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionEnding(CastSession session) {
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
        }
    }


    MediaInfo mediaInfo;
    *//*chromecast-------------------------------------*/
    RelativeLayout relativeOverlayLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Util.goToLibraryplayer = false;

        playerModel=new Player();
        get_video_details_output = new Get_Video_Details_Output();

        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        watchTrailerButton = (Button) findViewById(R.id.viewTrailerButton);
        preorderButton= (Button) findViewById(R.id.preOrderButton);
        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        watchTrailerButton.setTypeface(submitButtonTypeface);
        Typeface preorderButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        preorderButton.setTypeface(preorderButtonTypeface);
        preorderButton.setVisibility(View.GONE);

        offlineImageButton = (ImageButton) findViewById(R.id.offlineImageButton);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoGenreTextView = (TextView) findViewById(R.id.videoGenreTextView);
        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        videoStoryTextView = (ExpandableTextView) findViewById(R.id.videoStoryTextView);
        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        videoCastCrewTitleTextView.setVisibility(View.GONE);
        relativeOverlayLayout = (RelativeLayout)findViewById(R.id.relativeOverlayLayout);

        noInternetConnectionLayout = (RelativeLayout)findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout)findViewById(R.id.noData);
        noInternetTextView =(TextView)findViewById(R.id.noInternetTextView);
        noDataTextView =(TextView)findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));

        iconImageRelativeLayout = (RelativeLayout) findViewById(R.id.iconImageRelativeLayout);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        story_layout = (LinearLayout) findViewById(R.id.story_layout);

        preferenceManager = PreferenceManager.getPreferenceManager(this);

        permalinkStr = getIntent().getStringExtra(Util.PERMALINK_INTENT_KEY);
        // isLogin = ((Global) getApplicationContext()).getIsLogin();

        isLogin = preferenceManager.getLoginFeatureFromPref();

        ppvmodel = new PPVModel();
        advmodel = new APVModel();
        currencymodel = new CurrencyModel();
        PlanId = (Util.getTextofLanguage(MovieDetailsActivity.this, Util.PLAN_ID, Util.DEFAULT_PLAN_ID)).trim();



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //playermodel set data
// *****************set data into playermdel for play in exoplayer************

                playerModel.setStreamUniqueId(movieStreamUniqueId);
                playerModel.setMovieUniqueId(movieUniqueId);
                playerModel.setUserId(preferenceManager.getUseridFromPref());
                playerModel.setEmailId(preferenceManager.getEmailIdFromPref());
                playerModel.setAuthTokenStr( Util.authTokenStr.trim());
                playerModel.setRootUrl(Util.rootUrl().trim());
                playerModel.setEpisode_id("0");
                playerModel.setIsFreeContent(isFreeContent);
                playerModel.setVideoTitle(movieNameStr);
                playerModel.setVideoStory(movieDetailsStr);
                playerModel.setVideoGenre(videoGenreTextView.getText().toString());
                playerModel.setVideoDuration(videoDurationTextView.getText().toString());
                playerModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                playerModel.setCensorRating(censorRatingStr);

                Log.v("BKS","stramid="+playerModel.getStreamUniqueId());
                Log.v("BKS","movieID="+playerModel.getMovieUniqueId());
                Log.v("BKS","userid="+preferenceManager.getUseridFromPref());
                Log.v("BKS","emailid="+playerModel.getEmailId());






                DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl(movieThirdPartyUrl);
                dbModel.setVideoTitle(movieNameStr);
                dbModel.setVideoStory(movieDetailsStr);
                dbModel.setVideoGenre(videoGenreTextView.getText().toString());
                dbModel.setVideoDuration(videoDurationTextView.getText().toString());
                dbModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                dbModel.setCensorRating(censorRatingStr);
                dbModel.setCastCrew(castStr);
                dbModel.setEpisode_id("0");
                dbModel.setSeason_id("0");
                dbModel.setPurchase_type("show");
                dbModel.setPosterImageId(posterImageId);
                dbModel.setContentTypesId(contentTypesId);

                Util.dataModel = dbModel;
                SubTitleName.clear();
                SubTitlePath.clear();
                ResolutionUrl.clear();
                ResolutionFormat.clear();

                if(isLogin == 1) {
                    if (preferenceManager != null) {
                        String loggedInStr = preferenceManager.getLoginStatusFromPref();

                        if (loggedInStr == null) {
                            Log.v("BKS","Loginstr value null=="+preferenceManager.getLoginStatusFromPref());

                            final Intent registerActivity = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    Util.check_for_subscription = 1;
                                    startActivity(registerActivity);

                                }
                            });
                            //showLoginDialog();
                        } else {

                            if (Util.checkNetwork(MovieDetailsActivity.this) == true) {


                                if (playerModel.getIsFreeContent() == 1) {
                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                    getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    ValidateUserInput validateUserInput = new ValidateUserInput();
                                    validateUserInput.setAuthToken(Util.authTokenStr);
                                    if (preferenceManager != null) {
                                        loggedInIdStr = preferenceManager.getUseridFromPref();
                                    }
                                    validateUserInput.setUserId(loggedInIdStr.trim());
                                    validateUserInput.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
                                    validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                                    validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                                    validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                                    validateUserInput.setLanguageCode(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                                    asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                                    asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                                }
                            } else {
                                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }

                        }
                    } else {

                        final Intent registerActivity = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                Util.check_for_subscription = 1;

                                startActivity(registerActivity);

                            }
                        });


                    }
                } else {
                    if (Util.checkNetwork(MovieDetailsActivity.this) == true) {
                        // MUVIlaxmi

                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                    } else {
                        Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }


            }


        });

        preorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl(movieThirdPartyUrl);
                dbModel.setVideoTitle(movieNameStr);
                dbModel.setVideoStory(movieDetailsStr);
                dbModel.setVideoGenre(videoGenreTextView.getText().toString());
                dbModel.setVideoDuration(videoDurationTextView.getText().toString());
                dbModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                dbModel.setCensorRating(censorRatingStr);
                dbModel.setCastCrew(castStr);
                dbModel.setEpisode_id("0");
                dbModel.setSeason_id("0");
                dbModel.setPurchase_type("show");
                dbModel.setPosterImageId(posterImageId);
                dbModel.setContentTypesId(contentTypesId);

                Util.dataModel = dbModel;

                if (isLogin == 1) {
                    if (preferenceManager != null) {
                        String loggedInStr = preferenceManager.getLoginStatusFromPref();

                        if (loggedInStr == null) {

                            final Intent registerActivity = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    Util.check_for_subscription = 1;
                                    startActivity(registerActivity);

                                }
                            });
                            //showLoginDialog();
                        } else {
                            if (Util.checkNetwork(MovieDetailsActivity.this) == true) {


                                if (playerModel.getIsFreeContent() == 1) {
                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                    getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    ValidateUserInput validateUserInput = new ValidateUserInput();
                                    validateUserInput.setAuthToken(Util.authTokenStr);
                                    validateUserInput.setUserId(preferenceManager.getUseridFromPref().trim());
                                    validateUserInput.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
                                    validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                                    validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                                    validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                                    validateUserInput.setLanguageCode(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                                    asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                                    asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                                }
                            } else {
                                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }



                        }
                    } else {

                        final Intent registerActivity = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                Util.check_for_subscription = 1;

                                startActivity(registerActivity);

                            }
                        });


                    }
                } else {
                    if (Util.checkNetwork(MovieDetailsActivity.this) == true) {
                        // MUVIlaxmi

                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                    } else {
                        Toast.makeText(MovieDetailsActivity.this,
                                Util.getTextofLanguage(MovieDetailsActivity.this,
                                        Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        watchTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl(movieThirdPartyUrl);
                dbModel.setVideoTitle(movieNameStr);
                dbModel.setVideoStory(movieDetailsStr);
                dbModel.setVideoGenre(videoGenreTextView.getText().toString());
                dbModel.setVideoDuration(videoDurationTextView.getText().toString());
                dbModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                dbModel.setCensorRating(censorRatingStr);
                dbModel.setCastCrew(castStr);
                dbModel.setVideoUrl(movieTrailerUrlStr);
                dbModel.setVideoResolution("BEST");
                dbModel.setContentTypesId(contentTypesId);

                Util.dataModel = dbModel;

                if (movieTrailerUrlStr == null) {

                    Util.showNoDataAlert(MovieDetailsActivity.this);
                    /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();*/
                    return;

                } else if ((movieTrailerUrlStr.matches("")) || (movieTrailerUrlStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA)))) {
                    Util.showNoDataAlert(MovieDetailsActivity.this);
                   /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();*/
                    return;

                } else {
                    /*chromecast-------------------------------------*/
                    if (isThirdPartyTrailer == false) {
                       /* if (mCastSession != null && mCastSession.isConnected()) {


                            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieReleaseDateStr);
                            movieMetadata.putString(MediaMetadata.KEY_TITLE, movieNameStr + " - Trailer");
                            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject();
                                jsonObj.put("description", movieNameStr);
                            } catch (JSONException e) {
                            }

                            mediaInfo = new MediaInfo.Builder(movieTrailerUrlStr.trim())
                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                    .setContentType("videos/mp4")
                                    .setMetadata(movieMetadata)
                                    .setStreamDuration(15 * 1000)
                                    .setCustomData(jsonObj)
                                    .build();
                            mSelectedMedia = mediaInfo;
                           // Util.showQueuePopup(MovieDetailsActivity.this, view, mediaInfo);

                            togglePlayback();

                        }else {*/
                        final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, TrailerActivity.class);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(playVideoIntent);

                            }
                        });
                    } else {
                        if (movieTrailerUrlStr.contains("://www.youtube") || movieTrailerUrlStr.contains("://www.youtu.be")) {
                            if (movieTrailerUrlStr.contains("live_stream?channel")) {
                                final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(playVideoIntent);

                                    }
                                });
                            } else {

                                final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, YouTubeAPIActivity.class);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(playVideoIntent);


                                    }
                                });

                            }
                        } else {
                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });
                        }

                    }
                }
            }
        });

        videoCastCrewTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl(movieThirdPartyUrl);
                dbModel.setVideoTitle(movieNameStr);
                dbModel.setVideoStory(videoStoryTextView.getText().toString());
                dbModel.setVideoGenre(videoGenreTextView.getText().toString());
                dbModel.setVideoDuration(videoDurationTextView.getText().toString());
                dbModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                dbModel.setCensorRating(censorRatingStr);
                dbModel.setCastCrew(castStr);
                Util.dataModel = dbModel;*/


                //Will Add Some Data to send
                final Intent registerActivity = new Intent(MovieDetailsActivity.this, CastAndCrewActivity.class);
                runOnUiThread(new Runnable() {
                    public void run() {

                        registerActivity.putExtra("cast_movie_id", movieUniqueId.trim());
                        registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(registerActivity);

                    }
                });

            }
        });

        /// Subtitle/////

        if (ContextCompat.checkSelfPermission(MovieDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MovieDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MovieDetailsActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        111);
            } else {
                ActivityCompat.requestPermissions(MovieDetailsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);

            }
        } else {
            //Call whatever you want
            if (Util.checkNetwork(MovieDetailsActivity.this)) {

                asynLoadMovieDetails = new AsynLoadMovieDetails();
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(getApplicationContext(), Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }


               /* *//*chromecast-------------------------------------*//*

        mAquery = new AQuery(this);

        // setupControlsCallbacks();
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        boolean shouldStartPlayback = false;
        int startPosition = 0;

         *//*   MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieName.getText().toString());
            movieMetadata.putString(MediaMetadata.KEY_TITLE,  movieName.getText().toString());
            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject();
                jsonObj.put("description", movieName.getText().toString());
            } catch (JSONException e) {
                Log.e(TAG, "Failed to add description to the json object", e);
            }

            mediaInfo = new MediaInfo.Builder(castVideoUrl.trim())
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("videos/mp4")
                    .setMetadata(movieMetadata)
                    .setStreamDuration(15 * 1000)
                    .setCustomData(jsonObj)
                    .build();
            mSelectedMedia = mediaInfo;*//*

        // see what we need to play and where
           *//* Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mSelectedMedia = getIntent().getParcelableExtra("media");
                //setupActionBar();
                boolean shouldStartPlayback = bundle.getBoolean("shouldStart");
                int startPosition = bundle.getInt("startPosition", 0);
                // mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
               // Log.d(TAG, "Setting url of the VideoView to: " + mSelectedMedia.getContentId());
                if (shouldStartPlayback) {
                    // this will be the case only if we are coming from the
                    // CastControllerActivity by disconnecting from a device
                    mPlaybackState = PlaybackState.PLAYING;
                    updatePlaybackLocation(PlaybackLocation.LOCAL);
                    updatePlayButton(mPlaybackState);
                    if (startPosition > 0) {
                        // mVideoView.seekTo(startPosition);
                    }
                    // mVideoView.start();
                    //startControllersTimer();
                } else {
                    // we should load the video but pause it
                    // and show the album art.
                    if (mCastSession != null && mCastSession.isConnected()) {
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    } else {
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                    }
                    mPlaybackState = PlaybackState.IDLE;
                    updatePlayButton(mPlaybackState);
                }
            }*//*


        if (shouldStartPlayback) {
            // this will be the case only if we are coming from the
            // CastControllerActivity by disconnecting from a device
            mPlaybackState = PlaybackState.PLAYING;
            updatePlaybackLocation(PlaybackLocation.LOCAL);
            updatePlayButton(mPlaybackState);
            if (startPosition > 0) {
                // mVideoView.seekTo(startPosition);
            }
            // mVideoView.start();
            //startControllersTimer();
        } else {
            // we should load the video but pause it
            // and show the album art.
            if (mCastSession != null && mCastSession.isConnected()) {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                updatePlaybackLocation(PlaybackLocation.REMOTE);
            } else {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));

                updatePlaybackLocation(PlaybackLocation.LOCAL);
            }
            mPlaybackState = PlaybackState.IDLE;
            updatePlayButton(mPlaybackState);
        }
*//***************chromecast**********************/

    }


    /******* Subtitle*****/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want

                        if (Util.checkNetwork(MovieDetailsActivity.this)) {

                            AsynLoadMovieDetails asyncLoadVideos = new AsynLoadMovieDetails();
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

                        } else {
                            Toast.makeText(getApplicationContext(), Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } else {
                        finish();
                    }
                } else {
                    finish();
                }

                return;
            }
        }
    }


    @Override
    public void onGetValidateUserPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        String Subscription_Str = preferenceManager.getIsSubscribedFromPref();
        String validUserStr=validateUserOutput.getValiduser_str();

        if (validateUserOutput == null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
            dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(MovieDetailsActivity.this, MainActivity.class);
                            startActivity(in);
                            onBackPressed();
                        }
                    });
            dlgAlert.create().show();
        } else if (status <= 0) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
            dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(MovieDetailsActivity.this, MainActivity.class);
                            startActivity(in);
                            onBackPressed();
                        }
                    });
            dlgAlert.create().show();
        }

        if (status > 0) {
            if (status == 427) {

                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
                if (message != null && message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                }
                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                onBackPressed();
                            }
                        });
                dlgAlert.create().show();
            } else if (status == 429) {

                if (validUserStr != null) {

                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }

                    if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
                        if (Util.checkNetwork(MovieDetailsActivity.this) == true) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                                // Go to ppv Payment
                                payment_for_single_part();
                            } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                                Intent intent = new Intent(MovieDetailsActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            } else {
                                // Go to ppv Payment
                                payment_for_single_part();
                            }
                        }

                    }
                }

            } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                // Go to ppv Payment
                payment_for_single_part();
            } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                Intent intent = new Intent(MovieDetailsActivity.this, SubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else if (Util.dataModel.getIsConverted() == 0) {
                Util.showNoDataAlert(MovieDetailsActivity.this);
               /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();*/
            } else {
                if (Util.checkNetwork(MovieDetailsActivity.this) == true) {
                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                    getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Get_Video_Details_Output get_video_details_output, int statusCode, String stus, String message) {


       // get_video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=iWcnxTZMXS4");
       // get_video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {
        }

        if (statusCode == 200) {
            if (get_video_details_output.getThirdparty_url() == null || get_video_details_output.getThirdparty_url().matches("")) {
                if (get_video_details_output.getVideoUrl() != null || !get_video_details_output.getVideoUrl().matches("")) {
                    playerModel.setVideoUrl(get_video_details_output.getVideoUrl());
                    Log.v("BISHAL", "videourl===" + playerModel.getVideoUrl());
                    playerModel.setThirdPartyPlayer(false);
                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                }
            } else {
                if (get_video_details_output.getThirdparty_url() != null || !get_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(get_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                }
            }

            Util.dataModel.setVideoResolution(get_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(get_video_details_output.getVideoResolution());
            if(get_video_details_output.getPlayed_length()!=null && !get_video_details_output.getPlayed_length().equals(""))
            playerModel.setPlayPos((Util.isDouble(get_video_details_output.getPlayed_length())));




            //dependency for datamodel
            Util.dataModel.setVideoUrl(playerModel.getVideoUrl());
            Util.dataModel.setVideoResolution(get_video_details_output.getVideoResolution());
            Util.dataModel.setThirdPartyUrl(get_video_details_output.getThirdparty_url());



            //player model set
            playerModel.setSubTitleName(get_video_details_output.getSubTitleName());
            playerModel.setSubTitlePath(get_video_details_output.getSubTitlePath());
            playerModel.setResolutionFormat(get_video_details_output.getResolutionFormat());
            playerModel.setResolutionUrl(get_video_details_output.getResolutionUrl());
            playerModel.setFakeSubTitlePath(get_video_details_output.getFakeSubTitlePath());
            playerModel.setVideoResolution(get_video_details_output.getVideoResolution());
            FakeSubTitlePath = get_video_details_output.getFakeSubTitlePath();



            if (playerModel.getVideoUrl() == null ||
                    playerModel.getVideoUrl().matches("")) {
                Util.showNoDataAlert(MovieDetailsActivity.this);

                /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();*/
            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    playerModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }


                // condition for checking if the response has third party url or not.
                if (get_video_details_output.getThirdparty_url()==null ||
                        get_video_details_output.getThirdparty_url().matches("")
                        ) {


                    playerModel.setThirdPartyPlayer(false);

                    final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (FakeSubTitlePath.size() > 0) {
                                // This Portion Will Be changed Later.

                                File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
                                if (dir.isDirectory()) {
                                    String[] children = dir.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(dir, children[i]).delete();
                                    }
                                }

                                progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
                                progressBarHandler.show();
                                Download_SubTitle(FakeSubTitlePath.get(0).trim());
                            } else {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                /*playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                                playVideoIntent.putExtra("PlayerModel",playerModel);
                                startActivity(playVideoIntent);
                            }

                        }
                    });
                } else {
                    final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                /*playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                    playVideoIntent.putExtra("PlayerModel",playerModel);
                    startActivity(playVideoIntent);

                    //below part  checked at exoplayer thats why no need of checking here

                   /* playerModel.setThirdPartyPlayer(true);
                    if (playerModel.getVideoUrl().contains("://www.youtube") ||
                            playerModel.getVideoUrl().contains("://www.youtu.be")) {
                        if (playerModel.getVideoUrl().contains("live_stream?channel")) {
                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    playVideoIntent.putExtra("PlayerModel",playerModel);
                                    startActivity(playVideoIntent);

                                }
                            });
                        } else {

                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, YouTubeAPIActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    playVideoIntent.putExtra("PlayerModel",playerModel);
                                    startActivity(playVideoIntent);


                                }
                            });

                        }
                    } else {
                        final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                playVideoIntent.putExtra("PlayerModel",playerModel);
                                startActivity(playVideoIntent);

                            }
                        });
                    }*/
                }
            }

        } else {

            playerModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
            //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            Util.showNoDataAlert(MovieDetailsActivity.this);
           /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
            dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();*/
        }




    }




    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {
        if (progressBarHandler.isShowing()) {
            progressBarHandler.hide();
            progressBarHandler = null;

        }
        ShowLanguagePopup();
    }

    //Load Video Details Like VideoUrl,Release Date,Details,BannerUrl,rating,popularity etc.

    private class AsynLoadMovieDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getContentDetailsUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("permalink", permalinkStr);

                String countryCodeStr = preferenceManager.getCountryCodeFromPref();

                if (countryCodeStr != null) {

                    httppost.addHeader("country", countryCodeStr);
                } else {
                    httppost.addHeader("country", "IN");

                }

                httppost.addHeader("lang_code", Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);

                            story_layout.setVisibility(View.GONE);
                            bannerImageRelativeLayout.setVisibility(View.GONE);
                            iconImageRelativeLayout.setVisibility(View.GONE);


                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();


                        }

                    });

                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);

                            story_layout.setVisibility(View.GONE);
                            bannerImageRelativeLayout.setVisibility(View.GONE);
                            iconImageRelativeLayout.setVisibility(View.GONE);

                            //Commented By Me
                          /*  movieDescription.setVisibility(View.GONE);
                            //movieThumbnailImageView.setVisibility(View.GONE);
                            moviePoster.setVisibility(View.GONE);
                            movieName.setVisibility(View.GONE);
                            movieType.setVisibility(View.GONE);
                            movieDescriptionTitle.setVisibility(View.GONE);
                            castTitleTextView.setVisibility(View.GONE);
                            crewTitleTextView.setVisibility(View.GONE);
                            castTextView.setVisibility(View.GONE);
                            crewTextView.setVisibility(View.GONE);
                            movieCensorRatingTitleTextView.setVisibility(View.GONE);
                            movieCensorRatingTextView.setVisibility(View.GONE);
                            movieGenreTitleTextView.setVisibility(View.GONE);
                            movieReleaseDateTitleTextView.setVisibility(View.GONE);
                            movieReleaseDate.setVisibility(View.GONE);
                            watchTrailerButton.setVisibility(View.GONE);*/

                        }

                    });
                    e.printStackTrace();
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {

                    if (status == 200) {

                        JSONObject mainJson = myJson.getJSONObject("movie");
                        if ((mainJson.has("name")) && mainJson.getString("name").trim() != null && !mainJson.getString("name").trim().isEmpty() && !mainJson.getString("name").trim().equals("null") && !mainJson.getString("name").trim().matches("")) {
                            movieNameStr = mainJson.getString("name");
                        }else{
                            movieNameStr = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("trailerThirdpartyUrl")) && mainJson.getString("trailerThirdpartyUrl").trim() != null && !mainJson.getString("trailerThirdpartyUrl").trim().isEmpty() && !mainJson.getString("trailerThirdpartyUrl").trim().equals("null") && !mainJson.getString("trailerThirdpartyUrl").trim().matches("")) {
                            movieTrailerUrlStr = mainJson.getString("trailerThirdpartyUrl");
                            isThirdPartyTrailer = true;

                        } else {

                            if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                                movieTrailerUrlStr = mainJson.getString("trailerUrl");

                            } else {
                                movieTrailerUrlStr = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
                            }
                            isThirdPartyTrailer = false;
                        }

                        if ((mainJson.has("thirdparty_url")) && mainJson.getString("thirdparty_url").trim() != null && !mainJson.getString("thirdparty_url").trim().isEmpty() && !mainJson.getString("thirdparty_url").trim().equals("null") && !mainJson.getString("thirdparty_url").trim().matches("")) {
                            movieThirdPartyUrl = mainJson.getString("thirdparty_url");

                        } else {
                            movieThirdPartyUrl = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("video_duration")) && mainJson.getString("video_duration").trim() != null && !mainJson.getString("video_duration").trim().isEmpty() && !mainJson.getString("video_duration").trim().equals("null") && !mainJson.getString("video_duration").trim().matches("")) {
                            videoduration = mainJson.getString("video_duration");
                            playerModel.setVideoDuration(videoduration);


                        } else {
                            videoduration = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("genre")) && mainJson.getString("genre").trim() != null && !mainJson.getString("genre").trim().isEmpty() && !mainJson.getString("genre").trim().equals("null") && !mainJson.getString("genre").trim().matches("")) {
                            movieTypeStr = mainJson.getString("genre");
                            movieTypeStr = movieTypeStr.replaceAll("\\[", "");
                            movieTypeStr = movieTypeStr.replaceAll("\\]", "");
                            movieTypeStr = movieTypeStr.replaceAll(",", " , ");
                            movieTypeStr = movieTypeStr.replaceAll("\"", "");


                        } else {
                            movieTypeStr = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("censor_rating")) && mainJson.getString("censor_rating").trim() != null && !mainJson.getString("censor_rating").trim().isEmpty() && !mainJson.getString("censor_rating").trim().equals("null") && !mainJson.getString("censor_rating").trim().matches("")) {
                            censorRatingStr = mainJson.getString("censor_rating");
                            censorRatingStr = censorRatingStr.replaceAll("\\[", "");
                            censorRatingStr = censorRatingStr.replaceAll("\\]", "");
                            censorRatingStr = censorRatingStr.replaceAll(",", " ");
                            censorRatingStr = censorRatingStr.replaceAll("\"", "");


                        } else {
                            censorRatingStr = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("story")) && mainJson.getString("story").trim() != null && !mainJson.getString("story").trim().isEmpty() && !mainJson.getString("story").trim().equals("null") && !mainJson.getString("story").trim().matches("")) {
                            movieDetailsStr = mainJson.getString("story");
                        } else {
                            movieDetailsStr = "";

                        }
                        if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                            movieTrailerUrlStr = mainJson.getString("trailerUrl");

                        }else{
                            movieThirdPartyUrl = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("movie_stream_uniq_id")) && mainJson.getString("movie_stream_uniq_id").trim() != null && !mainJson.getString("movie_stream_uniq_id").trim().isEmpty() && !mainJson.getString("movie_stream_uniq_id").trim().equals("null") && !mainJson.getString("movie_stream_uniq_id").trim().matches("")) {
                            movieStreamUniqueId = mainJson.getString("movie_stream_uniq_id");
                        }else{
                            movieStreamUniqueId = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("muvi_uniq_id")) && mainJson.getString("muvi_uniq_id").trim() != null && !mainJson.getString("muvi_uniq_id").trim().isEmpty() && !mainJson.getString("muvi_uniq_id").trim().equals("null") && !mainJson.getString("muvi_uniq_id").trim().matches("")) {
                            movieUniqueId = mainJson.getString("muvi_uniq_id");
                        }else{
                            movieUniqueId = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                       /* if ((mainJson.has("movieUrl")) && mainJson.getString("movieUrl").trim() != null && !mainJson.getString("movieUrl").trim().isEmpty() && !mainJson.getString("movieUrl").trim().equals("null") && !mainJson.getString("movieUrl").trim().matches("")) {
                            movieVideoUrlStr = mainJson.getString("movieUrl");

                        }
                        else{
                            movieVideoUrlStr = getResources().getString(R.string.no_data_str);

                        }*/

                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")){
                            bannerImageId = mainJson.getString("banner");
                            bannerImageId = bannerImageId.replace("episode", "original");
                        } else {
                            bannerImageId = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("poster")) && mainJson.getString("poster").trim() != null && !mainJson.getString("poster").trim().isEmpty() && !mainJson.getString("poster").trim().equals("null") && !mainJson.getString("poster").trim().matches("")) {
                            posterImageId = mainJson.getString("poster");
                            posterImageId = posterImageId.replace("episode", "original");

                        } else {
                            posterImageId = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("isFreeContent")) && mainJson.getString("isFreeContent").trim() != null && !mainJson.getString("isFreeContent").trim().isEmpty() && !mainJson.getString("isFreeContent").trim().equals("null") && !mainJson.getString("isFreeContent").trim().matches("")) {
                            isFreeContent = Integer.parseInt(mainJson.getString("isFreeContent"));
                        } else {
                            isFreeContent = 0;

                        }
                        if ((mainJson.has("release_date")) && mainJson.getString("release_date").trim() != null && !mainJson.getString("release_date").trim().isEmpty() && !mainJson.getString("release_date").trim().equals("null") && !mainJson.getString("release_date").trim().matches("")) {
                            movieReleaseDateStr = mainJson.getString("release_date");
                        } else {
                            movieReleaseDateStr = "";

                        }
                        if ((mainJson.has("is_ppv")) && mainJson.getString("is_ppv").trim() != null && !mainJson.getString("is_ppv").trim().isEmpty() && !mainJson.getString("is_ppv").trim().equals("null") && !mainJson.getString("is_ppv").trim().matches("")) {
                            isPPV = Integer.parseInt(mainJson.getString("is_ppv"));
                        } else {
                            isPPV = 0;

                        }
                        if ((mainJson.has("is_converted")) && mainJson.getString("is_converted").trim() != null && !mainJson.getString("is_converted").trim().isEmpty() && !mainJson.getString("is_converted").trim().equals("null") && !mainJson.getString("is_converted").trim().matches("")) {
                            isConverted = Integer.parseInt(mainJson.getString("is_converted"));
                        } else {
                            isConverted = 0;

                        }
                        if ((mainJson.has("content_types_id")) && mainJson.getString("content_types_id").trim() != null && !mainJson.getString("content_types_id").trim().isEmpty() && !mainJson.getString("content_types_id").trim().equals("null") && !mainJson.getString("content_types_id").trim().matches("")) {
                            contentTypesId = Integer.parseInt(mainJson.getString("content_types_id"));
                        } else {
                            contentTypesId = 0;

                        }
                        if ((mainJson.has("is_advance")) && mainJson.getString("is_advance").trim() != null && !mainJson.getString("is_advance").trim().isEmpty() && !mainJson.getString("is_advance").trim().equals("null") && !mainJson.getString("is_advance").trim().matches("")) {
                            isAPV = Integer.parseInt(mainJson.getString("is_advance"));
                        } else {
                            isAPV = 0;

                        }
                        if (isPPV == 1) {
                            JSONObject ppvJson = null;
                            if ((myJson.has("ppv_pricing"))) {

                                ppvJson = myJson.getJSONObject("ppv_pricing");
                                if ((ppvJson.has("price_for_unsubscribed")) && ppvJson.getString("price_for_unsubscribed").trim() != null && !ppvJson.getString("price_for_unsubscribed").trim().isEmpty() && !ppvJson.getString("price_for_unsubscribed").trim().equals("null") && !ppvJson.getString("price_for_unsubscribed").trim().matches("")) {
                                    priceForUnsubscribedStr = ppvJson.getString("price_for_unsubscribed");
                                    ppvmodel.setPPVPriceForUnsubscribedStr(priceForUnsubscribedStr);
                                } else {
                                    ppvmodel.setPPVPriceForUnsubscribedStr("0.0");

                                }
                                if ((ppvJson.has("price_for_subscribed")) && ppvJson.getString("price_for_subscribed").trim() != null && !ppvJson.getString("price_for_subscribed").trim().isEmpty() && !ppvJson.getString("price_for_subscribed").trim().equals("null") && !ppvJson.getString("price_for_subscribed").trim().matches("")) {
                                    priceFosubscribedStr = ppvJson.getString("price_for_subscribed");
                                    ppvmodel.setPPVPriceForsubscribedStr(priceFosubscribedStr);
                                } else {
                                    priceFosubscribedStr = "0.0";
                                    ppvmodel.setPPVPriceForsubscribedStr(priceFosubscribedStr);

                                }
                            }

                            Util.ppvModel = ppvmodel;

                        }
                        if (isAPV == 1) {
                            JSONObject advJson = null;
                            if ((myJson.has("adv_pricing"))) {

                                advJson = myJson.getJSONObject("adv_pricing");
                                if ((advJson.has("price_for_unsubscribed")) && advJson.getString("price_for_unsubscribed").trim() != null && !advJson.getString("price_for_unsubscribed").trim().isEmpty() && !advJson.getString("price_for_unsubscribed").trim().equals("null") && !advJson.getString("price_for_unsubscribed").trim().matches("")) {
                                    priceForUnsubscribedStr = advJson.getString("price_for_unsubscribed");
                                    advmodel.setAPVPriceForUnsubscribedStr(priceForUnsubscribedStr);
                                } else {
                                    advmodel.setAPVPriceForUnsubscribedStr("0.0");

                                }
                                if ((advJson.has("price_for_subscribed")) && advJson.getString("price_for_subscribed").trim() != null && !advJson.getString("price_for_subscribed").trim().isEmpty() && !advJson.getString("price_for_subscribed").trim().equals("null") && !advJson.getString("price_for_subscribed").trim().matches("")) {
                                    priceFosubscribedStr = advJson.getString("price_for_subscribed");
                                    advmodel.setAPVPriceForsubscribedStr(priceFosubscribedStr);
                                } else {
                                    advmodel.setAPVPriceForsubscribedStr("0.0");


                                }
                            }

                            Util.apvModel = advmodel;

                        }

                        if (isPPV == 1 || isAPV == 1) {

                            JSONObject currencyJson = null;
                            if (myJson.has("currency") && myJson.getString("currency") != null && !myJson.getString("currency").equals("null")) {
                                currencyJson = myJson.getJSONObject("currency");
                                if (currencyJson.has("id") && currencyJson.getString("id").trim() != null && !currencyJson.getString("id").trim().isEmpty() && !currencyJson.getString("id").trim().equals("null") && !currencyJson.getString("id").trim().matches("")) {
                                    currencyIdStr = currencyJson.getString("id");
                                    currencymodel.setCurrencyId(currencyIdStr);
                                } else {
                                    currencyIdStr = "";
                                    currencymodel.setCurrencyId(currencyIdStr);

                                }
                                if (currencyJson.has("country_code") && currencyJson.getString("country_code").trim() != null && !currencyJson.getString("country_code").trim().isEmpty() && !currencyJson.getString("country_code").trim().equals("null") && !currencyJson.getString("country_code").trim().matches("")) {
                                    currencyCountryCodeStr = currencyJson.getString("country_code");
                                    currencymodel.setCurrencyCode(currencyCountryCodeStr);
                                } else {
                                    currencyCountryCodeStr = "";
                                    currencymodel.setCurrencyCode(currencyCountryCodeStr);
                                }
                                if (currencyJson.has("symbol") && currencyJson.getString("symbol").trim() != null && !currencyJson.getString("symbol").trim().isEmpty() && !currencyJson.getString("symbol").trim().equals("null") && !currencyJson.getString("symbol").trim().matches("")) {
                                    currencySymbolStr = currencyJson.getString("symbol");
                                    currencymodel.setCurrencySymbol(currencySymbolStr);
                                } else {
                                    currencySymbolStr = "";
                                    currencymodel.setCurrencySymbol(currencySymbolStr);
                                }
                            }

                            Util.currencyModel = currencymodel;
                        }


                        if (mainJson.has("cast_detail") && mainJson.has("cast_detail") != false && mainJson.getString("cast_detail").trim() != null && !mainJson.getString("cast_detail").trim().isEmpty() && !mainJson.getString("cast_detail").trim().equals("null") && !mainJson.getString("cast_detail").trim().equals("false")) {
                            castStr = true;

                        }

                    }
                } else {

                    responseStr = "0";

                }
            } catch (final JSONException e1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);


                    }

                });
                responseStr = "0";
                e1.printStackTrace();
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);

                    }

                });
                responseStr = "0";
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

            try{
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);


                    }

                });
                responseStr = "0";
            }
            if (responseStr == null)
                responseStr = "0";

            if ((responseStr.trim().equals("0"))) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);


                    }

                });
                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();


            } else {
                noInternetConnectionLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);
                if (isAPV == 1) {
                    playButton.setVisibility(View.INVISIBLE);
                    preorderButton.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADVANCE_PURCHASE, Util.DEFAULT_ADVANCE_PURCHASE));
                    preorderButton.setVisibility(View.VISIBLE);
                } else if (isAPV == 0 && isPPV == 0 && isConverted == 0) {
                    if (contentTypesId == 4) {
                        playButton.setVisibility(View.VISIBLE);
                        preorderButton.setVisibility(View.GONE);

                    } else {
                        playButton.setVisibility(View.INVISIBLE);
                        preorderButton.setVisibility(View.GONE);

                    }

                } else if (isAPV == 0 && isPPV == 0 && isConverted == 1) {
                    playButton.setVisibility(View.VISIBLE);
                    preorderButton.setVisibility(View.GONE);


                }
                videoTitle.setVisibility(View.VISIBLE);
                Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
                videoTitle.setTypeface(castDescriptionTypeface);
                videoTitle.setText(movieNameStr);

                if (movieTrailerUrlStr.matches("") || movieTrailerUrlStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                    watchTrailerButton.setVisibility(View.INVISIBLE);
                } else {
                    watchTrailerButton.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.VIEW_TRAILER, Util.DEFAULT_VIEW_TRAILER));

                    watchTrailerButton.setVisibility(View.VISIBLE);
                }

                if (movieTypeStr != null && movieTypeStr.matches("") || movieTypeStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                    videoGenreTextView.setVisibility(View.GONE);

                } else {
                    videoGenreTextView.setVisibility(View.VISIBLE);
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoGenreTextView.setTypeface(videoGenreTextViewTypeface);
                    videoGenreTextView.setText(movieTypeStr);

                }
                if(videoduration.matches("") || videoduration.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
                    videoDurationTextView.setVisibility(View.GONE);

                }else{

                    videoDurationTextView.setVisibility(View.VISIBLE);
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
                    videoDurationTextView.setTypeface(videoGenreTextViewTypeface);
                    videoDurationTextView.setText(videoduration);
                }


                if(movieReleaseDateStr.matches("") || movieReleaseDateStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
                    videoReleaseDateTextView.setVisibility(View.GONE);
                }else{
                    videoReleaseDateTextView.setVisibility(View.VISIBLE);
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
                    videoReleaseDateTextView.setTypeface(videoGenreTextViewTypeface);
                    movieReleaseDateStr = Util.formateDateFromstring("yyyy-mm-dd", "yyyy", movieReleaseDateStr);
                    videoReleaseDateTextView.setText(movieReleaseDateStr);

                }

                if(movieDetailsStr.matches("") || movieDetailsStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
                    videoStoryTextView.setVisibility(View.GONE);

                }else{
                    videoStoryTextView.setVisibility(View.VISIBLE);
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
                    videoStoryTextView.setTypeface(videoGenreTextViewTypeface);
                    videoStoryTextView.setText(movieDetailsStr);

                }

                if (censorRatingStr.matches("") || censorRatingStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                    videoCensorRatingTextView.setVisibility(View.GONE);
                    videoCensorRatingTextView1.setVisibility(View.GONE);

                } else {

                    if (censorRatingStr.contains("-")) {
                        String Data[] = censorRatingStr.split("-");
                        videoCensorRatingTextView.setVisibility(View.VISIBLE);
                        videoCensorRatingTextView1.setVisibility(View.VISIBLE);
                        Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                        videoCensorRatingTextView.setTypeface(videoGenreTextViewTypeface);
                        videoCensorRatingTextView1.setTypeface(videoGenreTextViewTypeface);

                        videoCensorRatingTextView.setText(Data[0]);
                        videoCensorRatingTextView1.setText(Data[1]);

                    } else {
                        videoCensorRatingTextView.setVisibility(View.VISIBLE);
                        videoCensorRatingTextView1.setVisibility(View.GONE);
                        Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                        videoCensorRatingTextView.setTypeface(videoGenreTextViewTypeface);
                        videoCensorRatingTextView.setText(censorRatingStr);
                    }


                }

                if (castStr == true){
                    videoCastCrewTitleTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.CAST_CREW_BUTTON_TITLE, Util.DEFAULT_CAST_CREW_BUTTON_TITLE));
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
                    videoCastCrewTitleTextView.setTypeface(videoGenreTextViewTypeface);
                    videoCastCrewTitleTextView.setVisibility(View.VISIBLE);
                }
                //Commented By Me

               /* if(crewStr.matches("") || crewStr.matches(getResources().getString(R.string.no_data_str))){
                    crewTitleTextView.setVisibility(View.GONE);
                    crewTextView.setVisibility(View.GONE);
                }else{
                    crewTitleTextView.setVisibility(View.VISIBLE);
                    crewTextView.setVisibility(View.VISIBLE);
                    crewTextView.setText(crewStr);
                }*/



              /*  if(posterImageId.trim().matches(getResources().getString(R.string.no_data_str))){
                    movieThumbnailImageView.setImageResource(R.drawable.no_thumbnail);
                   *//* Typeface custom_font1 = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-M.ttf");
                    movieName.setTypeface(custom_font1);*//*

                    *//*videoStoryTextView = (TextView)findViewById(R.id.videoStoryTextView);
                    Typeface custom_font2 = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-L.ttf");
                    videoStoryTextView.setTypeface(custom_font2);*//*


                }else{
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(MovieDetailsActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.no_thumbnail)
                            .showImageOnFail(R.drawable.no_thumbnail)
                            .showImageOnLoading(R.drawable.no_thumbnail).build();
                    imageLoader.displayImage(posterImageId, movieThumbnailImageView, options);*/

                  /*  Picasso.with(MovieDetailsActivity.this)
                            .load(posterImageId)
                            .placeholder(R.drawable.no_user_bg).error(R.drawable.no_user_bg).noFade().resize(width, height).into(sc, new Callback() {

                        @Override
                        public void onSuccess() {
                            sc.setAlpha(0.4f);

                        }

                        @Override
                        public void onError() {
                            sc.setImageResource(R.drawable.no_user_bg);
                            sc.setAlpha(0.4f);
                        }

                    });*/
                   /* DisplayImageOptions options1 = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.no_thumbnail)
                            .showImageOnFail(R.drawable.no_thumbnail)
                            .showImageOnLoading(R.drawable.no_thumbnail).build();
                    imageLoader.displayImage(posterImageId, sc, options);*/

                //}
               /* moviePoster.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
                ));*/
                if (bannerImageId.trim().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {

                    if (posterImageId.trim().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {

                        moviePoster.setImageResource(R.drawable.logo);
                    } else {


                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.init(ImageLoaderConfiguration.createDefault(MovieDetailsActivity.this));

                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                .cacheOnDisc(true).resetViewBeforeLoading(true)
                                .showImageForEmptyUri(R.drawable.logo)
                                .showImageOnFail(R.drawable.logo)
                                .showImageOnLoading(R.drawable.logo).build();
                        imageLoader.displayImage(posterImageId, moviePoster, options);

                    }

                } else {


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(MovieDetailsActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.logo)
                            .showImageOnLoading(R.drawable.logo).build();
                    imageLoader.displayImage(bannerImageId.trim(), moviePoster, options);


                }

            }
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            pDialog.show();

        }


    }


    // Get Details Of The Video Url




    //Load Video Details Like VideoUrl,Release Date,Details,BannerUrl,rating,popularity etc.

    private class AsynGetDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getVideoDetailsUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("content_uniq_id", movieUniqueId);
                httppost.addHeader("stream_uniq_id", movieStreamUniqueId);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (Exception e) {

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }
                if (status > 0) {
                    if (status == 200) {
                        if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                            Video_Url = myJson.getString("videoUrl");
                            Log.v("BKS","asyncgetdetails videourl"+myJson.getString("videoUrl"));
                            playerModel.setVideoUrl(get_video_details_output.getVideoUrl());
                            Log.v("BKS","asyncgetdetails videourl ===="+get_video_details_output.getVideoUrl());
                        } else {
                            Video_Url = Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
                        }
                    } else {
                        responseStr = "0";
                    }
                } else {
                    responseStr = "0";

                }
            } catch (final JSONException e1) {
                responseStr = "0";
            } catch (Exception e) {
                responseStr = "0";
            }
            return null;

        }

        protected void onPostExecute(Void result) {

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);

                    }

                });
                responseStr = "0";
            }
            if (responseStr == null)
                responseStr = "0";

            if ((responseStr.trim().equals("0"))) {
                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
            } else {
                final Intent playerIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);
                runOnUiThread(new Runnable() {
                    public void run() {
                        playerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(playerIntent);

                    }
                });

            }
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            pDialog.show();

        }

    }


///////////////////////////////////--------------------------------------//////////////////////////////


//    private class AsynValidateUserDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//
//        int status;
//        String validUserStr;
//        String userMessage;
//        String responseStr;
//        String loggedInIdStr;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            if (pref != null) {
//                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
//            }
//
//
//
//            String urlRouteList = Util.rootUrl().trim()+Util.userValidationUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("user_id", loggedInIdStr.trim());
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("movie_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("purchase_type", Util.dataModel.getPurchase_type());
//                httppost.addHeader("season_id", Util.dataModel.getSeason_id());
//                httppost.addHeader("episode_id", Util.dataModel.getEpisode_id());
//            /*    SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
//                if (countryPref != null) {
//                    String countryCodeStr = countryPref.getString("countryCode", null);
//                    httppost.addHeader("country", countryCodeStr);
//                }else{
//                    httppost.addHeader("country", "IN");
//
//                }         */
//
//                httppost.addHeader("lang_code",Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    StringBuilder sb = new StringBuilder();
//
//                    BufferedReader reader =
//                            new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
//                    String line = null;
//
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//
//                    responseStr = sb.toString();
//
//
//                } catch (final org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            status = 0;
//                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    status = 0;
//
//                    e.printStackTrace();
//                }
//                if(responseStr!=null){
//                    JSONObject myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                    validUserStr = myJson.optString("status");
//                    userMessage = myJson.optString("msg");
//                    if ((myJson.has("member_subscribed")) && myJson.getString("member_subscribed").trim() != null && !myJson.getString("member_subscribed").trim().isEmpty() && !myJson.getString("member_subscribed").trim().equals("null") && !myJson.getString("member_subscribed").trim().matches("")) {
//
//                        isMemberSubscribed = myJson.optString("member_subscribed");
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY",isMemberSubscribed);
//                        editor.commit();
//                    }
//
//                }
//
//            }
//            catch (Exception e) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                status = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//
//            String Subscription_Str = pref.getString("PREFS_LOGIN_ISSUBSCRIBED_KEY", "0");
//
//
//            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//            } catch (IllegalArgumentException ex) {
//                status = 0;
//            }
//
//            if (responseStr == null) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(MovieDetailsActivity.this, MainActivity.class);
//                                startActivity(in);
//                                onBackPressed();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else if (status <= 0) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(MovieDetailsActivity.this, MainActivity.class);
//                                startActivity(in);
//                                onBackPressed();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//            if (status > 0) {
//                if (status == 427) {
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
//                    if (userMessage != null && userMessage.equalsIgnoreCase("")) {
//                        dlgAlert.setMessage(userMessage);
//                    } else {
//                        dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
//
//                    }
//                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    onBackPressed();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (status == 429) {
//
//                    if (validUserStr != null) {
//                        try {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                        } catch (IllegalArgumentException ex) {
//                            status = 0;
//                        }
//
//                        if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK")))
//                        {
//                            if (Util.checkNetwork(MovieDetailsActivity.this) == true) {
//                                asynLoadVideoUrls = new AsynLoadVideoUrls();
//                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                            } else {
//                                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        else {
//
//                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid")))
//                            {
//                                if(Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1)
//                                {
//                                    // Go to ppv Payment
//                                    payment_for_single_part();
//                                }
//                                else if(PlanId.equals("1") && Subscription_Str.equals("0"))
//                                {
//                                    Intent intent = new Intent(MovieDetailsActivity.this,SubscriptionActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(intent);
//                                }
//                                else
//                                {
//                                    // Go to ppv Payment
//                                    payment_for_single_part();
//                                }
//                            }
//
//                        }
//                    }
//
//                }
//                else if(Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1)
//                {
//                    // Go to ppv Payment
//                    payment_for_single_part();
//                }
//                else if(PlanId.equals("1") && Subscription_Str.equals("0"))
//                {
//                    Intent intent = new Intent(MovieDetailsActivity.this,SubscriptionActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//                }
//                else if(Util.dataModel.getIsConverted() == 0)
//                {
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//                else
//                {
//                    if (Util.checkNetwork(MovieDetailsActivity.this) == true) {
//                        asynLoadVideoUrls = new AsynLoadVideoUrls();
//                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                    } else {
//                        Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
//            pDialog.show();
//
//        }
//
//
//    }

    private void payment_for_single_part() {
        {

            try {
                if (Util.currencyModel.getCurrencySymbol() == null) {
                    Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
                return;

            }


            Util.selected_episode_id = "0";
            Util.selected_season_id = "0";

            if (Util.dataModel.getIsAPV() == 1) {
                priceForUnsubscribedStr = Util.apvModel.getAPVPriceForUnsubscribedStr();
                priceFosubscribedStr = Util.apvModel.getAPVPriceForsubscribedStr();

            } else {
                priceForUnsubscribedStr = Util.ppvModel.getPPVPriceForUnsubscribedStr();
                priceFosubscribedStr = Util.ppvModel.getPPVPriceForsubscribedStr();
            }


            final Intent showPaymentIntent = new Intent(MovieDetailsActivity.this, PPvPaymentInfoActivity.class);
            showPaymentIntent.putExtra("muviuniqueid", Util.dataModel.getMovieUniqueId().trim());
            showPaymentIntent.putExtra("episodeStreamId", Util.dataModel.getStreamUniqueId().trim());
            showPaymentIntent.putExtra("content_types_id", Util.dataModel.getContentTypesId());
            showPaymentIntent.putExtra("movieThirdPartyUrl", Util.dataModel.getThirdPartyUrl());
            showPaymentIntent.putExtra("planUnSubscribedPrice", priceForUnsubscribedStr);
            showPaymentIntent.putExtra("planSubscribedPrice", priceFosubscribedStr);
            showPaymentIntent.putExtra("currencyId", Util.currencyModel.getCurrencyId());
            showPaymentIntent.putExtra("currencyCountryCode", Util.currencyModel.getCurrencyCode());
            showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
            showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle());
            showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
            showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
            if (Util.dataModel.getIsAPV() == 1) {
                showPaymentIntent.putExtra("isConverted", 0);
            } else {
                showPaymentIntent.putExtra("isConverted", 1);

            }
            showPaymentIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(showPaymentIntent);
            finish();
        }
    }


//    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        String responseStr;
//        int statusCode;
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.loadVideoUrl.trim());
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
//                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
//                httppost.addHeader("user_id",pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//
//                // Execute HTTP Post Request
//                try {
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseStr = "0";
//                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                }catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    e.printStackTrace();
//                }
//
//                JSONObject myJson =null;
//                JSONArray SubtitleJosnArray = null;
//                JSONArray ResolutionJosnArray = null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
//                    ResolutionJosnArray = myJson.optJSONArray("videoDetails");
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//                if (statusCode >= 0) {
//                    if (statusCode == 200) {
//                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
//
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                            }
//                        }else{
//                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                            }
//                        }
//                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
//                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));
//
//                        }
//                        if ((myJson.has("played_length")) && myJson.getString("played_length").trim() != null && !myJson.getString("played_length").trim().isEmpty() && !myJson.getString("played_length").trim().equals("null") && !myJson.getString("played_length").trim().matches("")) {
//                            Util.dataModel.setPlayPos(Util.isDouble(myJson.getString("played_length")));
//
//                        }
//                        if(SubtitleJosnArray!=null)
//                        {
//                            if(SubtitleJosnArray.length()>0)
//                            {
//                                for(int i=0;i<SubtitleJosnArray.length();i++)
//                                {
//                                    SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
//                                    FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
//
//
//                                }
//                            }
//                        }
//
//                        /******Resolution****/
//
//                        if(ResolutionJosnArray!=null)
//                        {
//                            if(ResolutionJosnArray.length()>0)
//                            {
//                                for(int i=0;i<ResolutionJosnArray.length();i++)
//                                {
//                                    if((ResolutionJosnArray.getJSONObject(i).optString("resolution").trim()).equals("BEST"))
//                                    {
//                                        ResolutionFormat.add(ResolutionJosnArray.getJSONObject(i).optString("resolution").trim());
//                                    }
//                                    else
//                                    {
//                                        ResolutionFormat.add((ResolutionJosnArray.getJSONObject(i).optString("resolution").trim())+"p");
//                                    }
//
//                                    ResolutionUrl.add(ResolutionJosnArray.getJSONObject(i).optString("url").trim());
//
//                                    Log.v("MUVI","Resolution Format Name ="+ResolutionJosnArray.getJSONObject(i).optString("resolution").trim());
//                                    Log.v("MUVI","Resolution url ="+ResolutionJosnArray.getJSONObject(i).optString("url").trim());
//                                }
//                            }
//                        }
//                        /******Resolution****/
//
//                    }
//
//                }
//                else {
//
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                }
//            } catch (JSONException e1) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                e1.printStackTrace();
//            }
//
//            catch (Exception e)
//            {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                e.printStackTrace();
//
//            }
//            return null;
//
//        }
//
//        protected void onPostExecute(Void result) {
//
//      /*  try{
//            if(pDialog.isShowing())
//                pDialog.dismiss();
//        }
//        catch(IllegalArgumentException ex)
//        {
//            responseStr = "0";
//            movieVideoUrlStr = getResources().getString(R.string.no_data_str);
//        }*/
//            if (responseStr == null) {
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//            }
//
//            if ((responseStr.trim().equalsIgnoreCase("0"))) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                }
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else {
//
//                if (Util.dataModel.getVideoUrl() == null) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//
//                    if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//
//                           /* if (mCastSession != null && mCastSession.isConnected()) {
//
//
//
//                                MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
//
//                                movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieReleaseDateStr);
//                                movieMetadata.putString(MediaMetadata.KEY_TITLE, movieNameStr);
//                                movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
//                                movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
//                                JSONObject jsonObj = null;
//                                try {
//                                    jsonObj = new JSONObject();
//                                    jsonObj.put("description", movieNameStr);
//                                } catch (JSONException e) {
//                                }
//
//                                mediaInfo = new MediaInfo.Builder(Util.dataModel.getVideoUrl().trim())
//                                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//                                        .setContentType("videos/mp4")
//                                        .setMetadata(movieMetadata)
//                                        .setStreamDuration(15 * 1000)
//                                        .setCustomData(jsonObj)
//                                        .build();
//                                mSelectedMedia = mediaInfo;
//
//
//                                togglePlayback();
//
//                            } else {*/
//                        final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                if(FakeSubTitlePath.size()>0)
//                                {
//                                    // This Portion Will Be changed Later.
//
//                                    File dir = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
//                                    if (dir.isDirectory())
//                                    {
//                                        String[] children = dir.list();
//                                        for (int i = 0; i < children.length; i++)
//                                        {
//                                            new File(dir, children[i]).delete();
//                                        }
//                                    }
//
//                                    progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
//                                    progressBarHandler.show();
//                                    Download_SubTitle(FakeSubTitlePath.get(0).trim());
//                                }
//                                else
//                                {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    playVideoIntent.putExtra("SubTitleName", SubTitleName);
//                                    playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
//                                    playVideoIntent.putExtra("ResolutionFormat",ResolutionFormat);
//                                    playVideoIntent.putExtra("ResolutionUrl",ResolutionUrl);
//                                    startActivity(playVideoIntent);
//                                }
//
//                            }
//                        });
//                    }
//                    else {
//                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")) {
//                            if (Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
//                                final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(playVideoIntent);
//
//                                    }
//                                });
//                            } else {
//
//                                final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, YouTubeAPIActivity.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(playVideoIntent);
//
//
//                                    }
//                                });
//
//                            }
//                        } else {
//                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(playVideoIntent);
//
//                                }
//                            });
//                        }
//                    }
//                }
//
//
//
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
//            pDialog.show();
//
//        }
//
//
//    }


    @Override

    public void onBackPressed() {
        if (asynValidateUserDetails != null) {
            asynValidateUserDetails.cancel(true);
        }
        if (asynLoadVideoUrls != null) {
            asynLoadVideoUrls.cancel(true);
        }
        if (asynLoadMovieDetails != null) {
            asynLoadMovieDetails.cancel(true);
        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(MovieDetailsActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.APP_SELECT_LANGUAGE, Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_APPLY, Util.DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        languageCustomAdapter = new LanguageCustomAdapter(MovieDetailsActivity.this, Util.languageModel);
        // Util.languageModel.get(0).setSelected(true);
      /*  if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
            prevPosition = i;
            Util.languageModel.get(i).setSelected(true);

        }
        Util.languageModel.get(0).setSelected(true);*/

        recyclerView.setAdapter(languageCustomAdapter);



    /*    for (int i = 0 ; i < Util.languageModel.size() - 1 ; i ++){
                if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
                    prevPosition = i;
                    Util.languageModel.get(i).setSelected(true);
                    break;

            }else {
                prevPosition = 0;

                Util.languageModel.get(0).setSelected(true);
                break;

            }
        }
*/
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener1(MovieDetailsActivity.this, recyclerView, new ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
                languageCustomAdapter.notifyDataSetChanged();

                // default_Language = Util.languageModel.get(position).getLanguageId();
             /*   AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);*/


                // new LanguageAsyncTask(new Get).executeOnExecutor(threadPoolExecutor);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


                if (!Previous_Selected_Language.equals(Default_Language)) {


                    AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                    asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

    }

    public static class RecyclerTouchListener1 implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener1 clickListener;

        public RecyclerTouchListener1(Context context, final RecyclerView recyclerView, final ClickListener1 clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


//    private class AsynGetLanguageList extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//        int status;
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.LanguageList.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//
//
//                // Execute HTTP Post Request
//                try {
//
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
//                } catch (Exception e) {
//                }
//                if (responseStr != null) {
//                    JSONObject json = new JSONObject(responseStr);
//                    try {
//                        status = Integer.parseInt(json.optString("code"));
//                        Default_Language = json.optString("default_lang");
//                        if (!Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, "").equals("")) {
//                            Default_Language = Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
//                        }
//
//                    } catch (Exception e) {
//                        status = 0;
//                    }
//                }
//
//            } catch (Exception e) {
//                runOnUiThread(new Runnable() {
//                    public void run() {
//
//                    }
//                });
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            if (progressBarHandler.isShowing()) {
//                progressBarHandler.hide();
//                progressBarHandler = null;
//
//            }
//
//            if (responseStr == null) {
//            } else {
//                if (status > 0 && status == 200) {
//
//                    try {
//                        JSONObject json = new JSONObject(responseStr);
//                        JSONArray jsonArray = json.getJSONArray("lang_list");
//                        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String language_id = jsonArray.getJSONObject(i).optString("code").trim();
//                            String language_name = jsonArray.getJSONObject(i).optString("language").trim();
//
//
//                            LanguageModel languageModel = new LanguageModel();
//                            languageModel.setLanguageId(language_id);
//                            languageModel.setLanguageName(language_name);
//
//                            if (Default_Language.equalsIgnoreCase(language_id)) {
//                                languageModel.setIsSelected(true);
//                            } else {
//                                languageModel.setIsSelected(false);
//                            }
//                            languageModels.add(languageModel);
//                        }
//
//                        Util.languageModel = languageModels;
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                 /*   if(!default_Language.equals("en")) {
//                        //                  Call For Language Translation.
//                        AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
//                        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
//
//                    }else{
//
//                    }*/
//
//                } else {
//                }
//            }
//            ShowLanguagePopup();
//
//
//        }
//
//        protected void onPreExecute() {
//
//            progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
//            progressBarHandler.show();
//
//        }
//    }


    private class AsynGetTransalatedLanguage extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = APIUrlConstant.getLanguageTranslation();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("lang_code", Default_Language);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
                } catch (Exception e) {
                }
                if (responseStr != null) {
                    JSONObject json = new JSONObject(responseStr);
                    try {
                        status = Integer.parseInt(json.optString("code"));
                    } catch (Exception e) {
                        status = 0;
                    }
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (progressBarHandler != null && progressBarHandler.isShowing()) {
                progressBarHandler.hide();
                progressBarHandler = null;

            }

            if (responseStr == null) {
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject parent_json = new JSONObject(responseStr);
                        JSONObject json = parent_json.getJSONObject("translation");


                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ENTER_EMPTY_FIELD,json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ALREADY_MEMBER,json.optString("already_member").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ACTIAVTE_PLAN_TITLE,json.optString("activate_plan_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_STATUS_ACTIVE,json.optString("transaction_status_active").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ADD_TO_FAV,json.optString("add_to_fav").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ADDED_TO_FAV,json.optString("added_to_fav").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.HOME,json.optString("home").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ADVANCE_PURCHASE,json.optString("advance_purchase").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ALERT,json.optString("alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.EPISODE_TITLE,json.optString("episodes_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SORT_ALPHA_A_Z,json.optString("sort_alpha_a_z").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SORT_ALPHA_Z_A,json.optString("sort_alpha_z_a").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.AMOUNT,json.optString("amount").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.COUPON_CANCELLED,json.optString("coupon_cancelled").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BUTTON_APPLY,json.optString("btn_apply").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SIGN_OUT_WARNING,json.optString("sign_out_warning").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.DISCOUNT_ON_COUPON,json.optString("discount_on_coupon").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CREDIT_CARD_CVV_HINT,json.optString("credit_card_cvv_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CAST,json.optString("cast").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CAST_CREW_BUTTON_TITLE,json.optString("cast_crew_button_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CENSOR_RATING,json.optString("censor_rating").trim());


                        if(json.optString("change_password").trim()==null || json.optString("change_password").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
                        } else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
                        }


                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CONFIRM_PASSWORD, json.optString("confirm_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.DIRECTOR, json.optString("director").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.DESCRIPTION, json.optString("description").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.EMAIL_EXISTS,json.optString("email_exists").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.EMAIL_DOESNOT_EXISTS,json.optString("email_does_not_exist").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.EMAIL_PASSWORD_INVALID,json.optString("email_password_invalid").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.COUPON_CODE_HINT,json.optString("coupon_code_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SEARCH_ALERT,json.optString("search_alert").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CREDIT_CARD_NUMBER_HINT,json.optString("credit_card_number_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TEXT_EMIAL,json.optString("text_email").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NAME_HINT,json.optString("name_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CREDIT_CARD_NAME_HINT,json.optString("credit_card_name_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TEXT_PASSWORD,json.optString("text_password").trim());


                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ERROR_IN_PAYMENT_VALIDATION,json.optString("error_in_payment_validation").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ERROR_IN_REGISTRATION,json.optString("error_in_registration").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_STATUS_EXPIRED,json.optString("transaction_status_expired").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.DETAILS_NOT_FOUND_ALERT,json.optString("details_not_found_alert").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FAILURE,json.optString("failure").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FILTER_BY,json.optString("filter_by").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FORGOT_PASSWORD,json.optString("forgot_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.GENRE,json.optString("genre").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ENTER_REGISTER_FIELDS_DATA,json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.AGREE_TERMS,json.optString("agree_terms").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.INVALID_COUPON,json.optString("invalid_coupon").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.INVOICE,json.optString("invoice").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LANGUAGE_POPUP_LANGUAGE,json.optString("language_popup_language").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SORT_LAST_UPLOADED,json.optString("sort_last_uploaded").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LANGUAGE_POPUP_LOGIN,json.optString("language_popup_login").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGIN,json.optString("login").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGOUT,json.optString("logout").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGOUT_SUCCESS,json.optString("logout_success").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.MY_FAVOURITE,json.optString("my_favourite").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NEW_PASSWORD,json.optString("new_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NEW_HERE_TITLE,json.optString("new_here_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO,json.optString("no").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO_DATA,json.optString("no_data").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION,json.optString("no_internet_connection").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO_INTERNET_NO_DATA,json.optString("no_internet_no_data").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE,json.optString("no_details_available").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BUTTON_OK,json.optString("btn_ok").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.OLD_PASSWORD,json.optString("old_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.OOPS_INVALID_EMAIL,json.optString("oops_invalid_email").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ORDER,json.optString("order").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_DETAILS_ORDER_ID,json.optString("transaction_detail_order_id").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PASSWORD_RESET_LINK,json.optString("password_reset_link").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PASSWORDS_DO_NOT_MATCH,json.optString("password_donot_match").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PAY_BY_PAYPAL,json.optString("pay_by_paypal").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BTN_PAYNOW,json.optString("btn_paynow").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PAY_WITH_CREDIT_CARD,json.optString("pay_with_credit_card").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PAYMENT_OPTIONS_TITLE,json.optString("payment_options_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PLAN_NAME,json.optString("plan_name").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,json.optString("activate_subscription_watch_video").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.COUPON_ALERT,json.optString("coupon_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.VALID_CONFIRM_PASSWORD,json.optString("valid_confirm_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PROFILE,json.optString("profile").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PROFILE_UPDATED,json.optString("profile_updated").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PURCHASE,json.optString("purchase").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_DETAIL_PURCHASE_DATE,json.optString("transaction_detail_purchase_date").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PURCHASE_HISTORY,json.optString("purchase_history").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BTN_REGISTER,json.optString("btn_register").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SORT_RELEASE_DATE,json.optString("sort_release_date").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SAVE_THIS_CARD,json.optString("save_this_card").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TEXT_SEARCH_PLACEHOLDER,json.optString("text_search_placeholder").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SEASON,json.optString("season").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SELECT_OPTION_TITLE,json.optString("select_option_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SELECT_PLAN,json.optString("select_plan").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SIGN_UP_TITLE,json.optString("signup_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SLOW_INTERNET_CONNECTION,json.optString("slow_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SLOW_ISSUE_INTERNET_CONNECTION,json.optString("slow_issue_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SORRY,json.optString("sorry").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.GEO_BLOCKED_ALERT,json.optString("geo_blocked_alert").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR,json.optString("sign_out_error").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ALREADY_PURCHASE_THIS_CONTENT,json.optString("already_purchase_this_content").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CROSSED_MAXIMUM_LIMIT,json.optString("crossed_max_limit_of_watching").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SORT_BY,json.optString("sort_by").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.STORY_TITLE,json.optString("story_title").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BTN_SUBMIT,json.optString("btn_submit").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_STATUS,json.optString("transaction_success").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.VIDEO_ISSUE,json.optString("video_issue").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO_CONTENT,json.optString("no_content").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE,json.optString("no_video_available").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY,json.optString("content_not_available_in_your_country").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_DATE,json.optString("transaction_date").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANASCTION_DETAIL,json.optString("transaction_detail").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION_STATUS,json.optString("transaction_status").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRANSACTION,json.optString("transaction").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TRY_AGAIN,json.optString("try_again").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.UNPAID,json.optString("unpaid").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.USE_NEW_CARD,json.optString("use_new_card").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.VIEW_MORE,json.optString("view_more").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.VIEW_TRAILER,json.optString("view_trailer").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.WATCH,json.optString("watch").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.WATCH_NOW,json.optString("watch_now").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SIGN_OUT_ALERT,json.optString("sign_out_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.UPDATE_PROFILE_ALERT,json.optString("update_profile_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.YES,json.optString("yes").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.PURCHASE_SUCCESS_ALERT,json.optString("purchase_success_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CARD_WILL_CHARGE,json.optString("card_will_charge").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SEARCH_HINT,json.optString("search_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TERMS, json.optString("terms").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.APP_ON, json.optString("app_on").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FILL_FORM_BELOW, json.optString("Fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.MESSAGE, json.optString("text_message").trim());
                        Util.getTextofLanguage(MovieDetailsActivity.this, Util.PURCHASE, Util.DEFAULT_PURCHASE);
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Default_Language);
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.MESSAGE, json.optString("text_message").trim());
                        //Call For Language PopUp Dialog

                        languageCustomAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(MovieDetailsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Call For Other Methods.


                } else {
                }
            }


        }

        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
            progressBarHandler.show();
        }
    }
   /* *//*****************chromecvast*-------------------------------------*//*

    private void updateMetadata(boolean visible) {
        Point displaySize;
        if (!visible) {
            *//*mDescriptionView.setVisibility(View.GONE);
            mTitleView.setVisibility(View.GONE);
            mAuthorView.setVisibility(View.GONE);*//*
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    displaySize.y + getSupportActionBar().getHeight());
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
        } else {
            //MediaMetadata mm = mSelectedMedia.getMetadata();
          *//*  mDescriptionView.setText(mSelectedMedia.getCustomData().optString(
                    VideoProvider.KEY_DESCRIPTION));
            //mTitleView.setText(mm.getString(MediaMetadata.KEY_TITLE));
            //mAuthorView.setText(mm.getString(MediaMetadata.KEY_SUBTITLE));
            mDescriptionView.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.VISIBLE);
            mAuthorView.setVisibility(View.VISIBLE);*//*
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    (int) (displaySize.x * mAspectRatio));
            lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
        }
    }



    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                mLocation = PlaybackLocation.REMOTE;
                if (null != mSelectedMedia) {

                    if (mPlaybackState ==PlaybackState.PLAYING) {
                       *//* mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);*//*
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
*//*
                    mPlayCircle.setVisibility(View.GONE);
*//*

                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlayButton(PlaybackState state) {
           *//* boolean isConnected = (mCastSession != null)
                    && (mCastSession.isConnected() || mCastSession.isConnecting());*//*
        //mControllers.setVisibility(isConnected ? View.GONE : View.VISIBLE);

        switch (state) {
            case PLAYING:

                //mLoading.setVisibility(View.INVISIBLE);
                // mPlayPause.setVisibility(View.VISIBLE);
                //mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_pause_dark));

                break;
            case IDLE:
                if (mLocation == PlaybackLocation.LOCAL){
                   *//* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                    }*//*

                }else{
                   *//* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                    }*//*
                }
                //mCon
                // trollers.setVisibility(View.GONE);
                // mCoverArt.setVisibility(View.VISIBLE);
                // mVideoView.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                //mLoading.setVisibility(View.INVISIBLE);
              *//*  mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_play_dark));*//*

                break;
            case BUFFERING:
                //mPlayPause.setVisibility(View.INVISIBLE);
                //mLoading.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                //setCoverArtStatus(null);
                //startControllersTimer();
            } else {
                //stopControllersTimer();

                //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            //stopControllersTimer();
            // setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            //updateControllersVisibility(false);
        }
    }


    private void togglePlayback() {
        //stopControllersTimer();
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:



                      *//* mVideoView.start();
                        Log.d(TAG, "Playing locally...");
                        mPlaybackState = PlaybackState.PLAYING;
                        startControllersTimer();
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*//*
                        break;

                    case REMOTE:

                        loadRemoteMedia(0, true);

                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;

              //  mVideoView.pause();
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                        //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));

                        // mPlayCircle.setVisibility(View.GONE);
                       *//* mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
                        mVideoView.seekTo(0);
                        mVideoView.start();
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*//*
                        break;
                    case REMOTE:
                        // mPlayCircle.setVisibility(View.VISIBLE);
                        if (mCastSession != null && mCastSession.isConnected()) {
                            // watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            loadRemoteMedia(0, true);


                            // Utils.showQueuePopup(this, mPlayCircle, mSelectedMedia);
                        }
                        else
                        {
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        updatePlayButton(mPlaybackState);
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {

        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {

            @Override
            public void onStatusUpdated() {

                Intent intent = new Intent(MovieDetailsActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
            }

            @Override
            public void onQueueStatusUpdated() {
            }

            @Override
            public void onPreloadStatusUpdated() {
            }

            @Override
            public void onSendingRemoteMediaRequest() {
            }
        });
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
    }

    *//***************chromecast**********************/

    /***********Subtitle********/

    public void Download_SubTitle(String Url) {
        new DownloadFileFromURL().execute(Url);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;


            try {
                URL url = new URL(f_url[0]);
                String str = f_url[0];
                filename = str.substring(str.lastIndexOf("/") + 1);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File root = Environment.getExternalStorageDirectory();
                mediaStorageDir = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/", "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("App", "failed to create directory");
                    }
                }

                SubTitlePath.add(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");
                OutputStream output = new FileOutputStream(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {
            FakeSubTitlePath.remove(0);
            if (FakeSubTitlePath.size() > 0) {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            } else {
                if (progressBarHandler != null && progressBarHandler.isShowing()) {
                    progressBarHandler.hide();
                }
                playerModel.setSubTitlePath(SubTitlePath);
                Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);
                playVideoIntent.putExtra("PlayerModel",playerModel);
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(playVideoIntent);
            }
        }
    }

}
