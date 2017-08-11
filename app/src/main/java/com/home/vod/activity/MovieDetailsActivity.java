package com.home.vod.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiController.ViewContentRatingAsynTask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Get_Video_Details_Output;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.apisdk.apiModel.ViewContentRatingInputModel;
import com.home.apisdk.apiModel.ViewContentRatingOutputModel;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.model.DataModel;
import com.home.vod.preferences.PreferenceManager;
import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.WebImage;
import com.home.vod.R;
import com.home.vod.util.ExpandableTextView;
import com.home.vod.util.LogUtil;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class MovieDetailsActivity extends AppCompatActivity implements LogoutAsynctask.Logout,
        GetValidateUserAsynTask.GetValidateUser, VideoDetailsAsynctask.VideoDetails, GetLanguageListAsynTask.GetLanguageList,
        GetContentDetailsAsynTask.GetContentDetails ,AddToFavAsync.AddToFavListener,ViewContentRatingAsynTask.ViewContentRatingListner
        ,DeleteFavAsync.DeleteFavListener,GetTranslateLanguageAsync.GetTranslateLanguageInfoListner{
    public static ProgressBarHandler progressBarHandler;
    int  prevPosition = 0;
    PreferenceManager preferenceManager;
    private static final int MAX_LINES = 2;
    ProgressBarHandler loadMovieDetailspDialog;
    ProgressBarHandler pDialog;
    int ratingAddedByUser = 1;
    String ipAddressStr="";
    String filename = "";
    static File mediaStorageDir;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String>ResolutionUrl = new ArrayList<>();
    ArrayList<String> SubTitleLanguage = new ArrayList<>();


    VideoDetailsAsynctask asynLoadVideoUrls;
    GetValidateUserAsynTask asynValidateUserDetails;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    ViewContentRatingAsynTask asynGetReviewDetails;
    String loggedInIdStr;
    AddToFavAsync asynFavoriteAdd;
    Toolbar mActionBarToolbar;
    ImageView moviePoster;
    PPVModel ppvmodel ;
    APVModel advmodel ;
    CurrencyModel currencymodel ;
    ImageView playButton,favorite_view;;
    String PlanId = "";
    ImageButton offlineImageButton;
    Button watchTrailerButton;
    Button preorderButton;
    int loginresultcode =0;

    private boolean isThirdPartyTrailer = false;
    String Default_Language = "";
    String Previous_Selected_Language="";

    RelativeLayout viewStoryLayout;

    //Add By Bibhu Later.
    ExpandableTextView videoStoryTextView;
    Button storyViewMoreButton;

    boolean isExpanded = false;

    TextView videoTitle, videoGenreTextView, videoDurationTextView,videoCensorRatingTextView,videoCensorRatingTextView1, videoReleaseDateTextView, videoCastCrewTitleTextView;
    String movieNameStr;
    String movieTypeStr = "";
    boolean castStr = false;
    String censorRatingStr = "";
    String videoduration = "";
    String movieDetailsStr ="";
    String Video_Url = "";
    String movieThirdPartyUrl = "";





//     ///****rating****///

    RatingBar ratingBar;
    TextView viewRatingTextView;
    String movieIdStr;

    /*** rating***///
    String rating ;
    String reviews;




    Intent DataIntent;
    String permalinkStr;
    String movieTrailerUrlStr ,movieReleaseDateStr= "";
    String movieStreamUniqueId,bannerImageId,posterImageId
            ,priceForUnsubscribedStr,priceFosubscribedStr,currencyIdStr,currencyCountryCodeStr,
            currencySymbolStr;
    String movieUniqueId="" ,isEpisode= "";
    int isFreeContent,isPPV,isConverted,contentTypesId,isAPV;

    RelativeLayout noInternetConnectionLayout,noDataLayout,iconImageRelativeLayout,bannerImageRelativeLayout;
    LinearLayout story_layout;
    String sucessMsg;
    int isFavorite ;
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
    String isMemberSubscribed,loggedInStr;

    // Added For The Voucher

    int isVoucher = 0;
    String VoucherCode = "";

    TextView content_label,content_name,voucher_success;
    EditText voucher_code;
    Button apply,watch_now;
    boolean watch_status = false;
    String ContentName="";
    AlertDialog voucher_alert;
    Player playerModel;
    Get_Video_Details_Output get_video_details_output;


    // voucher ends here //

    @Override
    protected void onResume() {
        super.onResume();


        /**FAVORITE*/
        if(Util.favorite_clicked == true){
            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
            contentDetailsInput.setAuthToken(Util.authTokenStr);
            contentDetailsInput.setPermalink(permalinkStr);

            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput,MovieDetailsActivity.this,MovieDetailsActivity.this);
            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);
        }
        // **************chromecast*********************//
       /* if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }
*/


      /*  mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
*/
        //**************chromecast*********************//
        invalidateOptionsMenu();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        /***************chromecast**********************/

//        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        /***************chromecast**********************/

        MenuItem item,item1,item2,item3,item4,item5,item6;
        item= menu.findItem(R.id.action_filter);
        item.setVisible(false);
        MenuItem item7 =  menu.findItem(R.id.menu_item_favorite);
        item= menu.findItem(R.id.action_filter);
        item.setVisible(false);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();

        id = preferenceManager.getUseridFromPref();
        email=preferenceManager.getEmailIdFromPref();


        if(preferenceManager.getLanguageListFromPref().equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);


        if(loggedInStr!=null){
            item4= menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.LANGUAGE_POPUP_LOGIN,Util.DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5= menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BTN_REGISTER,Util.DEFAULT_BTN_REGISTER));
            item5.setVisible(false);

            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.PROFILE,Util.DEFAULT_PROFILE));

            item1.setVisible(true);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.PURCHASE_HISTORY,Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(true);

            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.LOGOUT,Util.DEFAULT_LOGOUT));
            item3.setVisible(true);

            if ((Util.getTextofLanguage(MovieDetailsActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                item7.setVisible(true);
            }else{
                item7.setVisible(false);

            }

        }else if(loggedInStr==null){
            item4= menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.LANGUAGE_POPUP_LOGIN,Util.DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5= menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BTN_REGISTER,Util.DEFAULT_BTN_REGISTER));
            if(isLogin == 1)
            {
                item4.setVisible(true);
                item5.setVisible(true);

            }else{
                item4.setVisible(false);
                item5.setVisible(false);

            }

            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.PROFILE,Util.DEFAULT_PROFILE));
            item1.setVisible(false);
            item2= menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.PURCHASE_HISTORY,Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3= menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this,Util.LOGOUT,Util.DEFAULT_LOGOUT));
            item3.setVisible(false);

            item7 = menu.findItem(R.id.menu_item_favorite);
            item7.setTitle(Util.getTextofLanguage(this,Util.MY_FAVOURITE,Util.DEFAULT_MY_FAVOURITE));
            item7.setVisible(false);


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

                Intent loginIntent = new Intent(MovieDetailsActivity.this, LoginActivity.class);
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
            case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
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
                profileIntent.putExtra("EMAIL",email);
                profileIntent.putExtra("LOGID",id);
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
                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this,Util.SIGN_OUT_WARNING,Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.YES,Util.DEFAULT_YES) ,new DialogInterface.OnClickListener() {

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

                dlgAlert.setNegativeButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO,Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

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



    /*chromecast-------------------------------------*/
    View view;


    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    /**
     * List of various states that we can be in
     */
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

    /*chromecast-------------------------------------*/
    RelativeLayout relativeOverlayLayout;
    private BroadcastReceiver DELETE_ACTION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String movieUniqId = intent.getStringExtra("movie_uniq_id").trim();
            if (movieUniqId.equals(movieUniqueId.trim())){
                isFavorite=0;
                favorite_view.setImageResource(R.drawable.favorite_unselected);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.details_layout);


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(DELETE_ACTION, new IntentFilter("ITEM_STATUS"));

        playerModel=new Player();
        get_video_details_output = new Get_Video_Details_Output();
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

        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        watchTrailerButton = (Button) findViewById(R.id.viewTrailerButton);
        preorderButton= (Button) findViewById(R.id.preOrderButton);
        favorite_view = (ImageView) findViewById(R.id.favorite_view);
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
        viewStoryLayout = (RelativeLayout)findViewById(R.id.viewStoryLayout);

        videoStoryTextView = (ExpandableTextView) findViewById(R.id.videoStoryTextView);
        storyViewMoreButton = (Button) findViewById(R.id.storyViewMoreButton);




        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        videoCastCrewTitleTextView.setVisibility(View.GONE);


        // *** rating***////
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setFocusable(false);
        ratingBar.setVisibility(View.GONE);

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewRatingTextView = (TextView) findViewById(R.id.viewRatingTextView);
       // loggedInStr = preferenceManager.getLoginStatusFromPref();
        //pref = getSharedPreferences(Util.LOGIN_PREF, 0);

        relativeOverlayLayout = (RelativeLayout)findViewById(R.id.relativeOverlayLayout);

        noInternetConnectionLayout = (RelativeLayout)findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout)findViewById(R.id.noData);
        noInternetTextView =(TextView)findViewById(R.id.noInternetTextView);
        noDataTextView =(TextView)findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_CONTENT,Util.DEFAULT_NO_CONTENT));
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        iconImageRelativeLayout = (RelativeLayout) findViewById(R.id.iconImageRelativeLayout);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        story_layout = (LinearLayout) findViewById(R.id.story_layout);
       // pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        permalinkStr = getIntent().getStringExtra(Util.PERMALINK_INTENT_KEY);
        // isLogin = ((Global) getApplicationContext()).getIsLogin();
      //  SharedPreferences isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode

        isLogin = preferenceManager.getLoginFeatureFromPref();


        ppvmodel = new PPVModel();
        advmodel = new APVModel();
        currencymodel = new CurrencyModel();
        PlanId = (Util.getTextofLanguage(MovieDetailsActivity.this, Util.PLAN_ID, Util.DEFAULT_PLAN_ID)).trim();


        // *****rating********///
        viewRatingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(MovieDetailsActivity.this,ReviewActivity.class);
                reviewIntent.putExtra("muviId",movieIdStr);
                startActivityForResult(reviewIntent, 30060);
            }
        });
        /***favorite *****/

        favorite_view.setVisibility(View.GONE);


        favorite_view.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                String loggedInStr = "";
                if (preferenceManager.getLoginFeatureFromPref()== 1) {
                     loggedInStr = preferenceManager.getUseridFromPref();
                }

                if (loggedInStr != null){
                    if (isFavorite==1){
                        Log.v("goofy","Item deleted");

                        DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
                        deleteFavInputModel.setAuthTokenStr(Util.authTokenStr);
                        deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                        deleteFavInputModel.setMovieUniqueId(movieUniqueId);
                        deleteFavInputModel.setIsEpisode(isEpisode);

                        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, MovieDetailsActivity.this,MovieDetailsActivity.this);
                        deleteFavAsync.executeOnExecutor(threadPoolExecutor);


                    }else{

                        AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                        addToFavInputModel.setAuthToken(Util.authTokenStr);
                        addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                        addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                        addToFavInputModel.setIsEpisodeStr(isEpisode);

                        asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, MovieDetailsActivity.this,MovieDetailsActivity.this);
                        asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);

                    }
                }
                else {
                    Util.favorite_clicked=true;
                    final Intent registerActivity = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            registerActivity.putExtra("from", this.getClass().getName());
                            startActivity(registerActivity);

                        }
                    });

                }

            }
        });
        /***favorite *****/

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

                if(preferenceManager.getLoginFeatureFromPref() == 1) {
                    if (preferenceManager != null) {
                        String loggedInStr = preferenceManager.getUseridFromPref();

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

                                    Log.v("SUBHA","video details");
                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                    getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                    Log.v("BKS","contentid"+getVideoDetailsInput.getContent_uniq_id());
                                } else {


                                    Log.v("SUBHA","validate user details");

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

                        Log.v("SUBHA","VV");
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
                    if (preferenceManager.getLoginFeatureFromPref() == 1) {
                        String loggedInStr = preferenceManager.getUseridFromPref();

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
                            //String loggedinDateStr = pref.getString("date", null);
                            String loggedinDateStr = preferenceManager.getLoginDateFromPref();
                            if (loggedinDateStr != null) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date loggedInDate = null;
                                try {
                                    loggedInDate = formatter.parse(loggedinDateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Date today = new Date();
                                long differenceInDays = (int) Util.calculateDays(loggedInDate, today) + 1;
                                if (differenceInDays >= 7) {
                                   /* SharedPreferences.Editor editor = pref.edit();
                                    editor.clear();
                                    editor.commit();*/

                                    final Intent registerActivity = new Intent(MovieDetailsActivity.this, RegisterActivity.class);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            Util.check_for_subscription = 1;

                                            startActivity(registerActivity);

                                        }
                                    });
                                } else {

                                    if (Util.checkNetwork(MovieDetailsActivity.this) == true) {

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
                                    } else {
                                        Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                                        // Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                                    }


                                }
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
                        // subhalaxmi
                        Log.v("SUBHA","VV VV");
                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                    } else {
                        Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                        //Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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


                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this,R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                    return;
                }
                else  if ((movieTrailerUrlStr.matches("")) || (movieTrailerUrlStr.matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA)))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this,R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                    return;

                }else {
                    /*chromecast-------------------------------------*/
                    if (isThirdPartyTrailer == false) {
                        if (mCastSession != null && mCastSession.isConnected()) {


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

                           // togglePlayback();

                        } else {
                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, TrailerActivity.class);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });
                        }
                    }
                    else {
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



                //Will Add Some Data to send
                final Intent registerActivity = new Intent(MovieDetailsActivity.this, CastAndCrewActivity.class);
                runOnUiThread(new Runnable() {
                    public void run() {

                        registerActivity.putExtra("cast_movie_id",movieUniqueId);
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

                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                contentDetailsInput.setAuthToken(Util.authTokenStr);
                contentDetailsInput.setPermalink(permalinkStr);

                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput,MovieDetailsActivity.this,MovieDetailsActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                // Toast.makeText(getApplicationContext(), Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }





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

                            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                            contentDetailsInput.setAuthToken(Util.authTokenStr);
                            contentDetailsInput.setPermalink(permalinkStr);

                            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput,MovieDetailsActivity.this,MovieDetailsActivity.this);
                            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                            // Toast.makeText(getApplicationContext(), Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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














///////////////////////////////////--------------------------------------//////////////////////////////



    /*private class AsynValidateUserDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;

        int status;
        String validUserStr;
        String userMessage;
        String responseStr;
        String loggedInIdStr;

        @Override
        protected Void doInBackground(Void... params) {

            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }



            String urlRouteList = Util.rootUrl().trim()+Util.userValidationUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_id", Util.dataModel.getMovieUniqueId().trim());
                httppost.addHeader("purchase_type", Util.dataModel.getPurchase_type());
                httppost.addHeader("season_id", Util.dataModel.getSeason_id());
                httppost.addHeader("episode_id", Util.dataModel.getEpisode_id());
            *//*    SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                if (countryPref != null) {
                    String countryCodeStr = countryPref.getString("countryCode", null);
                    httppost.addHeader("country", countryCodeStr);
                }else{
                    httppost.addHeader("country", "IN");

                }         *//*

                httppost.addHeader("lang_code",Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    StringBuilder sb = new StringBuilder();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    responseStr = sb.toString();


                } catch (final org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            status = 0;
                            Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            // Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    status = 0;

                    e.printStackTrace();
                }
                Log.v("SUBHA","response data = "+ responseStr);
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    validUserStr = myJson.optString("status");
                    userMessage = myJson.optString("msg");
                    if ((myJson.has("member_subscribed")) && myJson.getString("member_subscribed").trim() != null && !myJson.getString("member_subscribed").trim().isEmpty() && !myJson.getString("member_subscribed").trim().equals("null") && !myJson.getString("member_subscribed").trim().matches("")) {

                        isMemberSubscribed = myJson.optString("member_subscribed");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY",isMemberSubscribed);
                        editor.commit();
                    }

                }

            }
            catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {

            String Subscription_Str = pref.getString("PREFS_LOGIN_ISSUBSCRIBED_KEY", "0");


            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }

            if (responseStr == null) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
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
                    if (userMessage != null && !userMessage.equalsIgnoreCase("")) {
                        dlgAlert.setMessage(userMessage);
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
                } else if (status == 426 || status == 425) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }

                    if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                        Intent intent = new Intent(MovieDetailsActivity.this, SubscriptionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                        if (userMessage != null && !userMessage.equalsIgnoreCase("")) {
                            dlgAlert.setMessage(userMessage);
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

                                    }
                                });
                        dlgAlert.create().show();
                    }
                } else if (status == 429 || status == 430 || status == 428) {


                    Log.v("SUBHA", "430");
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

                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid"))) {
                                if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                                    Log.v("SUBHA", "430 ppv");
                                    // Go to ppv Payment
                                    payment_for_single_part();
                                } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                                    Log.v("SUBHA", "430 subscription");
                                    Intent intent = new Intent(MovieDetailsActivity.this, SubscriptionActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                } else {
                                    // Go to ppv Payment
                                    //ShowVoucherPopUp(movieNameStr);
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
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this);
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
                    dlgAlert.create().show();
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
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            pDialog.show();

        }


    }*/

    private void payment_for_single_part() {

        {

            try {
                if (Util.currencyModel.getCurrencySymbol() == null) {
                    Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));

                    // Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));

                //  Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
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
            showPaymentIntent.putExtra("currencyCountryCode",Util.currencyModel.getCurrencyCode());
            showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
            showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle());
            showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
            showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
            showPaymentIntent.putExtra("PlayerModel",playerModel);
            if (Util.dataModel.getIsAPV() == 1) {
                showPaymentIntent.putExtra("isConverted", 0);
            } else {
                showPaymentIntent.putExtra("isConverted", 1);

            }
            showPaymentIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(showPaymentIntent);

        }
    }


   /* private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int statusCode;
        // This is added because of change in simultaneous login feature
        String message;
        boolean play_video = true;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.loadVideoUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
                httppost.addHeader("user_id",pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
                httppost.addHeader("lang_code",Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            responseStr = "0";
                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                            Util.showToast(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //  Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    responseStr = "0";
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    e.printStackTrace();
                }

                JSONObject myJson =null;
                JSONArray SubtitleJosnArray = null;
                JSONArray ResolutionJosnArray = null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
                    ResolutionJosnArray = myJson.optJSONArray("videoDetails");
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    // This is added because of change in simultaneous login feature
                    message = myJson.optString("msg");

                    Log.v("BIBHU","video stream msg"+message);
                    // ================================== End ====================================//
                }

                if (statusCode >= 0) {
                    if (statusCode == 200) {
                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                            if ((myJson.has("studio_approved_url")) && myJson.getString("studio_approved_url").trim() != null && !myJson.getString("studio_approved_url").trim().isEmpty() && !myJson.getString("studio_approved_url").trim().equals("null") && !myJson.getString("studio_approved_url").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("studio_approved_url"));
                                if ((myJson.has("licenseUrl")) && myJson.getString("licenseUrl").trim() != null && !myJson.getString("licenseUrl").trim().isEmpty() && !myJson.getString("licenseUrl").trim().equals("null") && !myJson.getString("licenseUrl").trim().matches("")) {
                                    //Util.dataModel.setLicenseUrl(myJson.getString("licenseUrl"));
                                }
                                if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                    Util.dataModel.setMpdVideoUrl(myJson.getString("videoUrl"));

                                }else {
//                                    Util.dataModel.setMpdVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                                }
                            }

                           *//* if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
                            }*//*

                            else{
                                if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                    Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));

                                }else {
                                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                                }
                            }

                        } else {
                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));

                            } else {
                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                            }
                        }
                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));

                        }
                        if ((myJson.has("played_length")) && myJson.getString("played_length").trim() != null && !myJson.getString("played_length").trim().isEmpty() && !myJson.getString("played_length").trim().equals("null") && !myJson.getString("played_length").trim().matches("")) {
                            Util.dataModel.setPlayPos(Util.isDouble(myJson.getString("played_length")));
                        } else {

                        }

                        if((myJson.has("is_offline")) && myJson.getString("is_offline").trim() != null && !myJson.getString("is_offline").trim().isEmpty() && !myJson.getString("is_offline").trim().equals("null") && !myJson.getString("is_offline").trim().matches("")){

                            //offline = myJson.getString("is_offline");
//                            Util.dataModel.setIsOffline(Util.isOffline=myJson.getString("is_offline"));

                        }else {


                        }

                        if (SubtitleJosnArray != null) {
                            if (SubtitleJosnArray.length() > 0) {
                                for (int i = 0; i < SubtitleJosnArray.length(); i++) {
                                    SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
                                    FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
                                    SubTitleLanguage.add(SubtitleJosnArray.getJSONObject(i).optString("code").trim());
                                 *//*   Util.offline_url.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
                                    Util.offline_language.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
*//*
                                }
                            }
                        }



                        // ================================== End ====================================//
                    }

                }
                else {

                    responseStr = "0";
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }
            } catch (JSONException e1) {

                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                e1.printStackTrace();
            }

            catch (Exception e)
            {

                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

            // This is added because of change in simultaneous login feature //

            if (!play_video) {

                try {
                    if (pDialog.isShowing())
                        pDialog.hide();
                } catch (IllegalArgumentException ex) {
                }

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(message);
                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();

                return;
            }

            //=====================End========================================//
            if (responseStr == null) {
                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
                }
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else {

                if (Util.dataModel.getVideoUrl() == null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                } else {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }

                    if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {



                            if (Util.dataModel.getVideoUrl().contains("rtmp://") || Util.dataModel.getVideoUrl().contains("rtmp://")) {
                                Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this, Util.VIDEO_ISSUE, Util.DEFAULT_VIDEO_ISSUE), Toast.LENGTH_SHORT).show();
                            } else {
                                final Intent playVideoIntent;

                                playVideoIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);


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
                                            playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                            playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                            playVideoIntent.putExtra("ResolutionFormat",ResolutionFormat);
                                            playVideoIntent.putExtra("ResolutionUrl",ResolutionUrl);
                                            startActivity(playVideoIntent);
                                        }


                                    }
                                });

                        }
                    } else {
                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")) {
                            if (Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
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
        }


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            pDialog.show();

        }


    }*/


    @Override
    public void onBackPressed()
    {
        if (asynValidateUserDetails!=null){
            asynValidateUserDetails.cancel(true);
        }
        if (asynLoadVideoUrls!=null){
            asynLoadVideoUrls.cancel(true);
        }
        if (asynLoadMovieDetails!=null){
            asynLoadMovieDetails.cancel(true);
        }
        if (asynGetReviewDetails!=null){
            asynGetReviewDetails.cancel(true);
        }
        if(loadMovieDetailspDialog!=null && loadMovieDetailspDialog.isShowing())
        {
            loadMovieDetailspDialog.hide();
            loadMovieDetailspDialog = null;

        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    public void ShowLanguagePopup()
    {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MovieDetailsActivity.this,R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater)getSystemService(MovieDetailsActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this,Util.APP_SELECT_LANGUAGE,Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_APPLY,Util.DEFAULT_BUTTON_APPLY));

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



                Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.languageModel.get(position).getLanguageId());
                languageCustomAdapter.notifyDataSetChanged();

                // Default_Language = Util.languageModel.get(position).getLanguageId();
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

                LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                languageListInputModel.setAuthToken(Util.authTokenStr);
                languageListInputModel.setLangCode(Default_Language);

                GetTranslateLanguageAsync getTranslateLanguageAsync = new GetTranslateLanguageAsync(languageListInputModel,MovieDetailsActivity.this,MovieDetailsActivity.this);
                getTranslateLanguageAsync.executeOnExecutor(threadPoolExecutor);

                /*AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);*/


            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Previous_Selected_Language);
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


    /*private class AsynGetLanguageList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList =Util.rootUrl().trim()+Util.LanguageList.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());


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
                        Default_Language = json.optString("default_lang");
                        if(!Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,"").equals(""))
                        {
                            Default_Language = Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                        }

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

            if(progressBarHandler.isShowing())
            {
                progressBarHandler.hide();
                progressBarHandler = null;

            }

            if (responseStr == null) {
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject json = new JSONObject(responseStr);
                        JSONArray jsonArray = json.getJSONArray("lang_list");
                        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String language_id = jsonArray.getJSONObject(i).optString("code").trim();
                            String language_name = jsonArray.getJSONObject(i).optString("language").trim();


                            LanguageModel languageModel = new LanguageModel();
                            languageModel.setLanguageId(language_id);
                            languageModel.setLanguageName(language_name);

                            if(Default_Language.equalsIgnoreCase(language_id))
                            {
                                languageModel.setIsSelected(true);
                            }
                            else
                            {
                                languageModel.setIsSelected(false);
                            }
                            languageModels.add(languageModel);
                        }

                        Util.languageModel = languageModels;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                 *//*   if(!Default_Language.equals("en")) {
                        //                  Call For Language Translation.
                        AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);

                    }else{

                    }*//*

                } else {
                }
            }
            ShowLanguagePopup();


        }

        protected void onPreExecute() {

            progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
            progressBarHandler.show();

        }
    }*/



    /*private class AsynGetTransalatedLanguage extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList =Util.rootUrl().trim()+Util.LanguageTranslation.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken",Util.authTokenStr);
                httppost.addHeader("lang_code",Default_Language);



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

            if(progressBarHandler!=null && progressBarHandler.isShowing())
            {
                progressBarHandler.hide();
                progressBarHandler = null;

            }

            if (responseStr == null) {
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject parent_json = new JSONObject(responseStr);
                        JSONObject json = parent_json.getJSONObject("translation");


                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ENTER_EMPTY_FIELD,json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ALREADY_MEMBER,json.optString("already_member").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ACTIAVTE_PLAN_TITLE,json.optString("activate_plan_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_STATUS_ACTIVE,json.optString("transaction_status_active").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ADD_TO_FAV,json.optString("add_to_fav").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ADDED_TO_FAV,json.optString("added_to_fav").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.HOME,json.optString("home").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ADVANCE_PURCHASE,json.optString("advance_purchase").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ALERT,json.optString("alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.EPISODE_TITLE,json.optString("episodes_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SORT_ALPHA_A_Z,json.optString("sort_alpha_a_z").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SORT_ALPHA_Z_A,json.optString("sort_alpha_z_a").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.AMOUNT,json.optString("amount").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.COUPON_CANCELLED,json.optString("coupon_cancelled").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BUTTON_APPLY,json.optString("btn_apply").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SIGN_OUT_WARNING,json.optString("sign_out_warning").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DISCOUNT_ON_COUPON,json.optString("discount_on_coupon").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CREDIT_CARD_CVV_HINT,json.optString("credit_card_cvv_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CAST,json.optString("cast").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CAST_CREW_BUTTON_TITLE,json.optString("cast_crew_button_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CENSOR_RATING,json.optString("censor_rating").trim());


                        if(json.optString("change_password").trim()==null || json.optString("change_password").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
                        }
                        if(json.optString("add_a_review").trim()==null || json.optString("add_a_review").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ADD_A_REVIEW, json.optString("add_a_review").trim());
                        }
                        if(json.optString("submit_your_rating_title").trim()==null || json.optString("submit_rating").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SUBMIT_YOUR_RATING_TITLE, Util.DEFAULT_SUBMIT_YOUR_RATING_TITLE);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SUBMIT_YOUR_RATING_TITLE, json.optString("submit_rating").trim());
                        }
                        if(json.optString("reviews").trim()==null || json.optString("reviews").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.REVIEWS, Util.DEFAULT_REVIEWS);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.REVIEWS, json.optString("reviews").trim());
                        }
                        if(json.optString("click_here").trim()==null || json.optString("click_here").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CLICK_HERE, Util.DEFAULT_CLICK_HERE);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CLICK_HERE, json.optString("click_here").trim());
                        }
                        if(json.optString("to_login").trim()==null || json.optString("to_login").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TO_LOGIN, Util.DEFAULT_TO_LOGIN);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TO_LOGIN, json.optString("to_login").trim());
                        }
                        if(json.optString("need_login_to_review").trim()==null || json.optString("need_login_to_review").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NEED_LOGIN_TO_REVIEW, Util.DEFAULT_NEED_LOGIN_TO_REVIEW);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.NEED_LOGIN_TO_REVIEW, json.optString("need_login_to_review").trim());
                        }
                        if(json.optString("btn_post_review").trim()==null || json.optString("btn_post_review").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BTN_POST_REVIEW, Util.DEFAULT_BTN_POST_REVIEW);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.BTN_POST_REVIEW, json.optString("btn_post_review").trim());
                        }
                        if(json.optString("login_facebook").trim()==null || json.optString("login_facebook").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGIN_FACEBOOK, Util.DEFAULT_LOGIN_FACEBOOK);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGIN_FACEBOOK, json.optString("login_facebook").trim());
                        }
                        if(json.optString("enter_review_here").trim()==null || json.optString("enter_review_here").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ENTER_REVIEW_HERE, Util.DEFAULT_ENTER_REVIEW_HERE);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.ENTER_REVIEW_HERE, json.optString("enter_review_here").trim());
                        }

                        if(json.optString("register_facebook").trim()==null || json.optString("register_facebook").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.REGISTER_FACEBOOK, Util.DEFAULT_REGISTER_FACEBOOK);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.REGISTER_FACEBOOK, json.optString("register_facebook").trim());
                        }

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CONFIRM_PASSWORD,json.optString("confirm_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CREDIT_CARD_DETAILS,json.optString("credit_card_detail").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DIRECTOR,json.optString("director").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DOWNLOAD_BUTTON_TITLE,json.optString("download_button_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DESCRIPTION,json.optString("description").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.EMAIL_EXISTS,json.optString("email_exists").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.EMAIL_DOESNOT_EXISTS,json.optString("email_does_not_exist").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.EMAIL_PASSWORD_INVALID,json.optString("email_password_invalid").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.COUPON_CODE_HINT,json.optString("coupon_code_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SEARCH_ALERT,json.optString("search_alert").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CREDIT_CARD_NUMBER_HINT,json.optString("credit_card_number_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TEXT_EMIAL,json.optString("text_email").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NAME_HINT,json.optString("name_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CREDIT_CARD_NAME_HINT,json.optString("credit_card_name_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TEXT_PASSWORD,json.optString("text_password").trim());


                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ERROR_IN_PAYMENT_VALIDATION,json.optString("error_in_payment_validation").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ERROR_IN_REGISTRATION,json.optString("error_in_registration").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_STATUS_EXPIRED,json.optString("transaction_status_expired").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DETAILS_NOT_FOUND_ALERT,json.optString("details_not_found_alert").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.FAILURE,json.optString("failure").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.FILTER_BY,json.optString("filter_by").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.FORGOT_PASSWORD,json.optString("forgot_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.GENRE,json.optString("genre").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ENTER_REGISTER_FIELDS_DATA,json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.AGREE_TERMS,json.optString("agree_terms").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.INVALID_COUPON,json.optString("invalid_coupon").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.INVOICE,json.optString("invoice").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,json.optString("language_popup_language").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SORT_LAST_UPLOADED,json.optString("sort_last_uploaded").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.LANGUAGE_POPUP_LOGIN,json.optString("language_popup_login").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.LOGIN,json.optString("login").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.LOGOUT,json.optString("logout").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.LOGOUT_SUCCESS,json.optString("logout_success").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.MY_FAVOURITE,json.optString("my_favourite").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NEW_PASSWORD,json.optString("new_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NEW_HERE_TITLE,json.optString("new_here_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO,json.optString("no").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO_DATA,json.optString("no_data").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO_INTERNET_CONNECTION,json.optString("no_internet_connection").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO_INTERNET_NO_DATA,json.optString("no_internet_no_data").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO_DETAILS_AVAILABLE,json.optString("no_details_available").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BUTTON_OK,json.optString("btn_ok").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.OLD_PASSWORD,json.optString("old_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.OOPS_INVALID_EMAIL,json.optString("oops_invalid_email").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ORDER,json.optString("order").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_DETAILS_ORDER_ID,json.optString("transaction_detail_order_id").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PASSWORD_RESET_LINK,json.optString("password_reset_link").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PASSWORDS_DO_NOT_MATCH,json.optString("password_donot_match").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PAY_BY_PAYPAL,json.optString("pay_by_paypal").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BTN_PAYNOW,json.optString("btn_paynow").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PAY_WITH_CREDIT_CARD,json.optString("pay_with_credit_card").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PAYMENT_OPTIONS_TITLE,json.optString("payment_options_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PLAN_NAME,json.optString("plan_name").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,json.optString("activate_subscription_watch_video").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.COUPON_ALERT,json.optString("coupon_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VALID_CONFIRM_PASSWORD,json.optString("valid_confirm_password").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PROFILE,json.optString("profile").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PROFILE_UPDATED,json.optString("profile_updated").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PURCHASE,json.optString("purchase").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_DETAIL_PURCHASE_DATE,json.optString("transaction_detail_purchase_date").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PURCHASE_HISTORY,json.optString("purchase_history").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BTN_REGISTER,json.optString("btn_register").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SORT_RELEASE_DATE,json.optString("sort_release_date").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SAVE_THIS_CARD,json.optString("save_this_card").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TEXT_SEARCH_PLACEHOLDER,json.optString("text_search_placeholder").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SEASON,json.optString("season").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SELECT_OPTION_TITLE,json.optString("select_option_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SELECT_PLAN,json.optString("select_plan").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SIGN_UP_TITLE,json.optString("signup_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,json.optString("slow_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SLOW_ISSUE_INTERNET_CONNECTION,json.optString("slow_issue_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SORRY,json.optString("sorry").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.GEO_BLOCKED_ALERT,json.optString("geo_blocked_alert").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SIGN_OUT_ERROR,json.optString("sign_out_error").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ALREADY_PURCHASE_THIS_CONTENT,json.optString("already_purchase_this_content").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CROSSED_MAXIMUM_LIMIT,json.optString("crossed_max_limit_of_watching").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SORT_BY,json.optString("sort_by").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.STORY_TITLE,json.optString("story_title").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BTN_SUBMIT,json.optString("btn_submit").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_STATUS,json.optString("transaction_success").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VIDEO_ISSUE,json.optString("video_issue").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO_CONTENT,json.optString("no_content").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.NO_VIDEO_AVAILABLE,json.optString("no_video_available").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY,json.optString("content_not_available_in_your_country").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_DATE,json.optString("transaction_date").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANASCTION_DETAIL,json.optString("transaction_detail").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION_STATUS,json.optString("transaction_status").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRANSACTION,json.optString("transaction").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.TRY_AGAIN,json.optString("try_again").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.UNPAID,json.optString("unpaid").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.USE_NEW_CARD,json.optString("use_new_card").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VIEW_MORE,json.optString("view_more").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VIEW_TRAILER,json.optString("view_trailer").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.WATCH,json.optString("watch").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.WATCH_NOW,json.optString("watch_now").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SIGN_OUT_ALERT,json.optString("sign_out_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.UPDATE_PROFILE_ALERT,json.optString("update_profile_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.YES,json.optString("yes").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.PURCHASE_SUCCESS_ALERT,json.optString("purchase_success_alert").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.CARD_WILL_CHARGE,json.optString("card_will_charge").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SEARCH_HINT,json.optString("search_hint").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.TERMS, json.optString("terms").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.APP_ON, json.optString("app_on").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.MESSAGE, json.optString("text_message").trim());
                        Util.getTextofLanguage(MovieDetailsActivity.this, Util.PURCHASE, Util.DEFAULT_PURCHASE);
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Default_Language);
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.MESSAGE, json.optString("text_message").trim());
                        //Call For Language PopUp Dialog

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE,json.optString("logged_out_from_all_devices").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.ANDROID_VERSION,json.optString("android_version").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.MANAGE_DEVICE,json.optString("manage_device").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.YOUR_DEVICE,json.optString("your_device").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DEREGISTER,json.optString("deregister").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.LOGIN_STATUS_MESSAGE,json.optString("oops_you_have_no_access").trim());
                  *//*      Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.UPADTE_TITLE,json.optString("update_title").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.UPADTE_MESSAGE,json.optString("update_message").trim());

                        //Language for offline viewing

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.MY_DOWNLOAD,json.optString("my_download").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DELETE_BTN,json.optString("delete_btn").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.STOP_SAVING_THIS_VIDEO,json.optString("stop_saving_this_video").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.YOUR_VIDEO_WONT_BE_SAVED,json.optString("your_video_can_not_be_saved").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BTN_KEEP,json.optString("btn_keep").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BTN_DISCARD,json.optString("btn_discard").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.DOWNLOAD_CANCELLED,json.optString("download_cancelled").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.WANT_TO_DOWNLOAD,json.optString("want_to_download").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.WANT_TO_DELETE,json.optString("want_to_delete").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VIEW_LESS,json.optString("view_less").trim());

                        //Language for voucher

                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this, Util.VOUCHER_CODE, json.optString("voucher_code").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VOUCHER_BLANK_MESSAGE,json.optString("voucher_vaildate_message").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.VOUCHER_SUCCESS,json.optString("voucher_applied_success").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.BTN_NEXT,json.optString("btn_next").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.FREE_FOR_COUPON,json.optString("free_for_coupon").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.SELECT_PURCHASE_TYPE,json.optString("select_purchase_type").trim());
                        Util.setLanguageSharedPrefernce(MovieDetailsActivity.this,Util.COMPLETE_SEASON,json.optString("complete_season").trim());
*//*

                        languageCustomAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(MovieDetailsActivity.this,MainActivity.class);
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
*/

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

                SubTitlePath.add(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis()+".vtt");
                OutputStream output = new FileOutputStream(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis()+".vtt");

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
            if(FakeSubTitlePath.size()>0)
            {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            }
            else
            {
                if(progressBarHandler!=null && progressBarHandler.isShowing())
                {
                    progressBarHandler.hide();
                }
                Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ExoPlayerActivity.class);
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                playVideoIntent.putExtra("ResolutionFormat",ResolutionFormat);
                playVideoIntent.putExtra("ResolutionUrl",ResolutionUrl);
                startActivity(playVideoIntent);
            }
        }
    }


   /* public void ShowVoucherPopUp(String ContentName)
    {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MovieDetailsActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) MovieDetailsActivity.this.getSystemService(MovieDetailsActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.voucher_popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        content_label = (TextView) convertView.findViewById(R.id.content_label);
        content_name = (TextView) convertView.findViewById(R.id.content_name);
        voucher_success = (TextView) convertView.findViewById(R.id.voucher_success);
        voucher_code = (EditText) convertView.findViewById(R.id.voucher_code);
        apply = (Button) convertView.findViewById(R.id.apply);
        watch_now = (Button) convertView.findViewById(R.id.watch_now);

        apply.setEnabled(true);
        apply.setBackgroundResource(R.drawable.button_radious);
        apply.setTextColor(getResources().getColor(R.color.pageTitleColor));


        // Font implemented Here//

        Typeface typeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        content_label.setTypeface(typeface);
        content_name.setTypeface(typeface);
        voucher_success.setTypeface(typeface);
        apply.setTypeface(typeface);
        watch_now.setTypeface(typeface);
        voucher_code.setTypeface(typeface);

        //==============end===============//

        // Language Implemented Here //

        content_label.setText(" "+ Util.getTextofLanguage(MovieDetailsActivity.this,Util.PURCHASE,Util.DEFAULT_PURCHASE)+" :");
        voucher_success.setText(" "+ Util.getTextofLanguage(MovieDetailsActivity.this,Util.VOUCHER_SUCCESS,Util.DEFAULT_VOUCHER_SUCCESS)+" ");
        apply.setText(" "+ Util.getTextofLanguage(MovieDetailsActivity.this,Util.BUTTON_APPLY,Util.DEFAULT_BUTTON_APPLY)+" ");
        watch_now.setText(" "+ Util.getTextofLanguage(MovieDetailsActivity.this,Util.WATCH_NOW,Util.DEFAULT_WATCH_NOW)+" ");
        voucher_code.setHint(" "+ Util.getTextofLanguage(MovieDetailsActivity.this,Util.VOUCHER_CODE,Util.DEFAULT_VOUCHER_CODE)+" ");



        //==============End===============//


        voucher_code.setText("");
        watch_now.setBackgroundResource(R.drawable.voucher_inactive_button);
        watch_now.setTextColor(Color.parseColor("#7f7f7f"));

        voucher_success.setVisibility(View.INVISIBLE);

        content_name.setText(" "+ContentName);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VoucherCode = voucher_code.getText().toString().trim();
                if(!VoucherCode.equals(""))
                {
                    voucher_alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    ValidateVoucher_And_VoucherSubscription();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),Util.getTextofLanguage(MovieDetailsActivity.this,Util.VOUCHER_BLANK_MESSAGE,Util.DEFAULT_VOUCHER_BLANK_MESSAGE), Toast.LENGTH_SHORT).show();

                }
            }
        });

        watch_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(watch_status)
                {
                    voucher_alert.dismiss();

                    // Calling Voucher Subscription Api

                    AsynVoucherSubscription asynVoucherSubscription = new AsynVoucherSubscription();
                    asynVoucherSubscription.executeOnExecutor(threadPoolExecutor);



                }
            }
        });
        voucher_alert = alertDialog.show();
        voucher_alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
*/
    public void ValidateVoucher_And_VoucherSubscription()
    {
        // Calling Validate Voucher Api

       /* AsynValidateVoucher asynValidateVoucher = new AsynValidateVoucher();
        asynValidateVoucher.executeOnExecutor(threadPoolExecutor);*/
    }

   /* private class AsynValidateVoucher extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1;

        int status;
        String responseStr;
        String loggedInIdStr;
        String message = "Invalid Voucher.";



        @Override
        protected Void doInBackground(Void... params) {

            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }

            try {

                String urlRouteList = Util.rootUrl().trim()+Util.ValidateVoucher.trim();
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("movie_id", movieUniqueId.trim());
                httppost.addHeader("stream_id",movieStreamUniqueId);
                // httppost.addHeader("season", Util.dataModel.getSeason_id());  // This is optional,so don't need to send here
                httppost.addHeader("voucher_code", VoucherCode);
                httppost.addHeader("purchase_type","show");
                httppost.addHeader("lang_code",Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));


              *//*  String urlRouteList = "http://www.idogic.com/rest/ValidateVoucher";
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken","b7fbac553a14c99adcf079be6b48fd9f");
                httppost.addHeader("user_id","5148");
                httppost.addHeader("movie_id","5b24dfaf49a996b04ef92c272bde21f0");
                httppost.addHeader("stream_id","de4fcc9ffcc0b7d3ae1468765290685f");
//                httppost.addHeader("season","1");
                httppost.addHeader("voucher_code", VoucherCode);
                httppost.addHeader("purchase_type","show");*//*



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("BIBHU" , "Response Of validate voucher  = "+responseStr);


                } catch (final org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog1 != null && progressDialog1.isShowing()) {
                                progressDialog1.hide();
                                progressDialog1 = null;
                            }
                            status = 0;
                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    if (progressDialog1 != null && progressDialog1.isShowing()) {
                        progressDialog1.hide();
                        progressDialog1 = null;
                    }
                    status = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                }
                else
                {
                    status = 0;
                }
            }
            catch (Exception e) {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
                status = 0;
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }

            if(status == 200)
            {


                voucher_success.setVisibility(View.VISIBLE);
                watch_now.setBackgroundResource(R.drawable.button_radious);
                watch_now.setTextColor(Color.parseColor("#ffffff"));
                watch_status = true;

                apply.setEnabled(false);
                apply.setBackgroundResource(R.drawable.voucher_inactive_button);
                apply.setTextColor(Color.parseColor("#7f7f7f"));

            }
            else
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog1 = new ProgressDialog(MovieDetailsActivity.this,R.style.MyTheme);
            progressDialog1.setCancelable(false);
            progressDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog1.setIndeterminate(false);
            progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_rawable));
            progressDialog1.show();
        }
    }

    private class AsynVoucherSubscription extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1;

        int status;
        String responseStr;
        String loggedInIdStr;
        String message = "";

        @Override
        protected Void doInBackground(Void... params) {

            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }

            try {

                String urlRouteList = Util.rootUrl().trim()+Util.VoucherSubscription.trim();
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("movie_id", movieUniqueId.trim());
                httppost.addHeader("stream_id", Util.dataModel.getStreamUniqueId());
                // httppost.addHeader("season", Util.dataModel.getSeason_id()); // This is optional here
                httppost.addHeader("voucher_code", VoucherCode);
                httppost.addHeader("purchase_type","show");
                httppost.addHeader("is_preorder",""+Util.dataModel.getIsAPV());
                httppost.addHeader("lang_code",Util.getTextofLanguage(MovieDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));


        *//*        String urlRouteList = "http://www.idogic.com/rest/VoucherSubscription";
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken","b7fbac553a14c99adcf079be6b48fd9f");
                httppost.addHeader("user_id","5148");
                httppost.addHeader("movie_id","5b24dfaf49a996b04ef92c272bde21f0");
                httppost.addHeader("stream_id","de4fcc9ffcc0b7d3ae1468765290685f");
            //    httppost.addHeader("season","1");
                httppost.addHeader("voucher_code", VoucherCode);
                httppost.addHeader("purchase_type","show");
                httppost.addHeader("is_preorder",""+Util.dataModel.getIsAPV());
*//*


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("BIBHU" , "Response Of validate voucher  = "+responseStr);


                } catch (final org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog1 != null && progressDialog1.isShowing()) {
                                progressDialog1.hide();
                                progressDialog1 = null;
                            }
                            status = 0;
                            Toast.makeText(MovieDetailsActivity.this, Util.getTextofLanguage(MovieDetailsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    if (progressDialog1 != null && progressDialog1.isShowing()) {
                        progressDialog1.hide();
                        progressDialog1 = null;
                    }
                    status = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                }
                else
                {
                    status = 0;
                }
            }
            catch (Exception e) {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
                status = 0;
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }

            if(status == 200)
            {
               *//* voucher_success.setVisibility(View.VISIBLE);
                watch_now.setBackgroundResource(R.drawable.button_radious);
                watch_now.setTextColor(Color.parseColor("#ffffff"));
                watch_status = true;

                apply.setEnabled(false);
                apply.setBackgroundResource(R.drawable.voucher_inactive_button);
                apply.setTextColor(Color.parseColor("#7f7f7f"));*//*

                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            }
            else
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog1 = new ProgressDialog(MovieDetailsActivity.this,R.style.MyTheme);
            progressDialog1.setCancelable(false);
            progressDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog1.setIndeterminate(false);
            progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_rawable));
            progressDialog1.show();
        }
    }
*/

/*
    private class AsynFavoriteAdd extends AsyncTask<String, Void, Void> {


        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {

            pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            if (pref != null) {
                loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }

            String urlRouteList = Util.rootUrl().trim() + Util.AddtoFavlist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", movieUniqueId);
                httppost.addHeader("content_type", isEpisode);
                httppost.addHeader("user_id", loggedInStr);
                Log.v("ANU","Value"+movieUniqueId);

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("ANU","LOFDJBSJHBSJ======"+loggedInStr);
            Log.v("ANU","response======"+responseStr);

            favorite_view.setImageResource(R.drawable.favorite_red);
            isFavorite=1;
            showToast();
            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }

        }

    }
*/
    public void showToast()
    {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg=(TextView) toastRoot.findViewById(R.id.toastMsg) ;
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
//        toast.setText("Added to Favorites");
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }
/*
    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {


        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;

        String contMessage;
        String responseStr;

//    @Override
//    protected void onPreExecute() {
//        pDialog = new ProgressBarHandler(getActivity().getBaseContext());
//        pDialog.show();
//        Log.v("NIhar","onpreExecution");
//    }

        @Override
        protected Void doInBackground(String... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", movieUniqueId);
                httppost.addHeader("content_type", isEpisode);
                httppost.addHeader("user_id", loggedInStr);

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.v("AMU","delete======"+loggedInStr);
            Log.v("AMU","response======"+responseStr);
            favorite_view.setImageResource(R.drawable.favorite_unselected);
            showToast();
            isFavorite = 0;
            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }






        }
        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            pDialog.show();

        }
    }
*/


  /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Util.favorite_clicked=false;
//            call feb Api
            loggedInStr=pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

            AsynFavoriteAdd asynFavoriteAdd =new AsynFavoriteAdd();
            asynFavoriteAdd.execute();
        }
        else {Util.favorite_clicked=false;}

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("BKS", "elseclickedddddd");
        /*loginresultcode = requestCode;
        if (requestCode == 40500) {
            if (resultCode == RESULT_OK) {
                Util.favorite_clicked = false;

                loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

                AsynLoadMovieDetails asyncViewFavorite = new AsynLoadMovieDetails();
                asyncViewFavorite.executeOnExecutor(threadPoolExecutor);
            } else {
                Log.v("BKS", "elseclickedddddd");
                Util.favorite_clicked = false;
            }

        }*/
//
        if(requestCode==30060)
        {
            if (Util.checkNetwork(MovieDetailsActivity.this)) {
                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                contentDetailsInput.setAuthToken(Util.authTokenStr);
                contentDetailsInput.setPermalink(permalinkStr);

                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput,MovieDetailsActivity.this,MovieDetailsActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(getApplicationContext(), Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }

        }




    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //aunregisterReceiver(DELETE_ACTION);
    }



    public void GetReviewDetails()
    {


        ViewContentRatingInputModel viewContentRatingInputModel = new ViewContentRatingInputModel();
        viewContentRatingInputModel.setAuthToken(Util.authTokenStr);
        viewContentRatingInputModel.setUser_id(preferenceManager.getUseridFromPref());
//        viewContentRatingInputModel.setUser_id("142026");
        viewContentRatingInputModel.setContent_id(movieIdStr.trim());
        viewContentRatingInputModel.setLang_code(preferenceManager.getLanguageListFromPref());
        asynGetReviewDetails = new ViewContentRatingAsynTask(viewContentRatingInputModel,MovieDetailsActivity.this,MovieDetailsActivity.this);
        asynGetReviewDetails.executeOnExecutor(threadPoolExecutor);


        Log.v("SUBHA2","user id"+ preferenceManager.getUseridFromPref());
        Log.v("SUBHA2","Movie  id"+ movieIdStr.trim());
        Log.v("SUBHA2","View Content Rating Call");

    }
    //Asyntask for getDetails of the csat and crew members.

   /* private class AsynGetReviewDetails extends AsyncTask<Void, Void, Void> {
        // ProgressBarHandler pDialog;
        String responseStr = "";
        int status;
        String msg;
        @Override
        protected Void doInBackground(Void... params) {

            try {


                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.ViewContentRating.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("content_id",movieIdStr.trim());
                if (pref != null) {
                    loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
                    httppost.addHeader("user_id",loggedInStr);

                }
                httppost.addHeader("lang_code", Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA","responseStr AsynGetReviewDetails"+responseStr);


                } catch (Exception e){
                    responseStr = "0";

                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    msg = myJson.optString("msg");
                    if ((myJson.has("showrating")) && myJson.optString("showrating").trim() != null && !myJson.optString("showrating").trim().isEmpty() && !myJson.optString("showrating").trim().equals("null") && !myJson.optString("showrating").trim().matches("")) {
                        ratingAddedByUser = Integer.parseInt(myJson.optString("showrating"));
                        Log.v("SUBHA","HFFH"+ratingAddedByUser);
                    }

                }


            } catch (final JSONException e1) {
                responseStr = "0";
            }
            catch (Exception e)
            {
                responseStr = "0";

            }
            return null;

        }

        protected void onPostExecute(Void result) {

            try{
                if (loadMovieDetailspDialog != null && loadMovieDetailspDialog.isShowing()) {
                    loadMovieDetailspDialog.hide();
                    loadMovieDetailspDialog = null;
                }

            }
            catch(IllegalArgumentException ex)
            {

                responseStr = "0";
            }
            if(responseStr == null) {
                responseStr = "0";
            }
            *//****rating ******//*
            if (isReviewThere == 0) {
                viewRatingTextView.setVisibility(View.GONE);

            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(Float.parseFloat(rating));
            }
            if (isRatingThere == 0) {
                ratingBar.setVisibility(View.GONE);
            } else {
                viewRatingTextView.setVisibility(View.VISIBLE);

                if (pref != null) {
                    String loggedInStr = pref.getString("PREFS_LOGGEDIN_KEY", null);
                    if (loggedInStr == null) {
                        viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW));

                    } else {
                        if (ratingAddedByUser == 1) {
                            viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW));

                        } else {
                            viewRatingTextView.setText("reviews (" + reviews + ")");
                            viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.REVIEWS, Util.DEFAULT_REVIEWS) + " (" + reviews + ") ");

                        }

                    }
                }else{
                    viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW));

                }
            }

            *//***favorite *****//*

            if(loggedInStr != null && isFavorite== 0 && Util.favorite_clicked == true){

                Util.favorite_clicked = false;
                AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                addToFavInputModel.setAuthToken(Util.authTokenStr);
                addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                addToFavInputModel.setIsEpisodeStr(isEpisode);

                asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, MovieDetailsActivity.this,MovieDetailsActivity.this);
                asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);
            }
            else if (loggedInStr != null && isFavorite==1){

                favorite_view.setImageResource(R.drawable.favorite_red);
            }
            *//***favorite *****//*
        }

        @Override
        protected void onPreExecute() {

            loadMovieDetailspDialog = new ProgressBarHandler(MovieDetailsActivity.this);
            loadMovieDetailspDialog.show();
            Log.v("SUBHA","onPreExecute");


        }
    }*/

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
                Log.v("SUBHA","validate post execute 11" + status);
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
            } else if (status == 429 || status == 430) {

                Log.v("SUBHA","validate post execute" + status);

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
                            Log.v("SUBHA","VV VV VV");

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

                                Log.v("SUBHA","unpaid msg");
                                payment_for_single_part();
                            } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                                Intent intent = new Intent(MovieDetailsActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            } else {
                                // Go to ppv Payment
                                Log.v("SUBHA","unpaid msg");
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

                    Log.v("SUBHA","VV vv vv vvvv");
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
        if (status > 0 && status == 200) {
            ShowLanguagePopup();
        }
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Get_Video_Details_Output get_video_details_output, int code, String status, String message) {
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

        if (code == 200) {
            if (get_video_details_output.getThirdparty_url() == null || get_video_details_output.getThirdparty_url().matches("")) {

                /**@bishal
                 * for drm player below condition added
                 * if studio_approved_url is there in api then set the videourl from this other wise goto 2nd one
                 */

                if (get_video_details_output.getStudio_approved_url() != null &&
                        !get_video_details_output.getStudio_approved_url().isEmpty() &&
                        !get_video_details_output.getStudio_approved_url().equals("null") &&
                        !get_video_details_output.getStudio_approved_url().matches("")) {
                    LogUtil.showLog("BISHAL","if called means  studioapproved");
                    playerModel.setVideoUrl(get_video_details_output.getStudio_approved_url());
                    LogUtil.showLog("BS","studipapprovedurl===="+playerModel.getVideoUrl());


                    if ( get_video_details_output.getLicenseUrl().trim() != null && !get_video_details_output.getLicenseUrl().trim().isEmpty() && !get_video_details_output.getLicenseUrl().trim().equals("null") && !get_video_details_output.getLicenseUrl().trim().matches("")) {
                        playerModel.setLicenseUrl(get_video_details_output.getLicenseUrl());
                    }
                    if ( get_video_details_output.getVideoUrl().trim() != null && !get_video_details_output.getVideoUrl().isEmpty() && !get_video_details_output.getVideoUrl().equals("null") && !get_video_details_output.getVideoUrl().trim().matches("")) {
                        playerModel.setMpdVideoUrl(get_video_details_output.getVideoUrl());

                    }else {
                        playerModel.setMpdVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                }

                else {
                    if (get_video_details_output.getVideoUrl() != null || !get_video_details_output.getVideoUrl().matches("")) {
                        playerModel.setVideoUrl(get_video_details_output.getVideoUrl());
                        Log.v("BISHAL", "videourl===" + playerModel.getVideoUrl());
                        playerModel.setThirdPartyPlayer(false);
                    } else {
                        //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                        playerModel.setVideoUrl(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                    }
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
    public void onAddToFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        favorite_view.setImageResource(R.drawable.favorite_red);
        isFavorite=1;
        showToast();
        if(pDialog.isShowing()&& pDialog!=null)
        {
            pDialog.hide();
        }
    }

    @Override
    public void onGetContentDetailsPreExecuteStarted() {
        loadMovieDetailspDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        loadMovieDetailspDialog.show();
    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {

        try{
            if (loadMovieDetailspDialog != null && loadMovieDetailspDialog.isShowing()) {
                loadMovieDetailspDialog.hide();
                loadMovieDetailspDialog = null;
            }
        }
        catch(IllegalArgumentException ex)
        {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }


        if(status==200){
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);

            movieUniqueId = contentDetailsOutput.getMuviUniqId();
            isEpisode = contentDetailsOutput.getIsEpisode();
            movieStreamUniqueId=contentDetailsOutput.getMovieStreamUniqId();
            movieNameStr=contentDetailsOutput.getName();
            movieTrailerUrlStr=contentDetailsOutput.getTrailerUrl();
            videoduration=contentDetailsOutput.getVideoDuration();
            censorRatingStr=contentDetailsOutput.getCensorRating();
            movieDetailsStr=contentDetailsOutput.getStory();
            movieTypeStr=contentDetailsOutput.getGenre();
            isFavorite = contentDetailsOutput.getIs_favorite();
            reviews = contentDetailsOutput.getReview();
            rating = contentDetailsOutput.getRating();
            movieIdStr = contentDetailsOutput.getId();
            Util.currencyModel = contentDetailsOutput.getCurrencyDetails();
            Util.apvModel = contentDetailsOutput.getApvDetails();
            Util.ppvModel = contentDetailsOutput.getPpvDetails();

            Log.v("SUBHA","rattting === "+ rating);
            Log.v("SUBHA","reviewwww === "+ reviews);


            //  castValue = contentDetailsOutput.getCastStr();

//        Log.v("SUBHA2","cast value" +castValue);

            Log.v("SUBHA","movieUniqueId====== "+movieUniqueId);

            /***favorite *****/

            if ((Util.getTextofLanguage(MovieDetailsActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE).trim()).equals("1")) {
                favorite_view.setVisibility(View.VISIBLE);
            }
            /***favorite *****/

            if (contentDetailsOutput.getIsApv() == 1) {
                playButton.setVisibility(View.INVISIBLE);
                preorderButton.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADVANCE_PURCHASE, Util.DEFAULT_ADVANCE_PURCHASE));
                preorderButton.setVisibility(View.VISIBLE);
            } else if (contentDetailsOutput.getIsApv() == 0 && contentDetailsOutput.getIsPpv() == 0 && contentDetailsOutput.getIsConverted() == 0) {
                if (contentDetailsOutput.getContentTypesId() == 4) {
                    playButton.setVisibility(View.VISIBLE);
                    preorderButton.setVisibility(View.GONE);

                } else {
                    playButton.setVisibility(View.INVISIBLE);
                    preorderButton.setVisibility(View.GONE);

                }

            } else if (contentDetailsOutput.getIsApv() == 0 && contentDetailsOutput.getIsPpv() == 0 && contentDetailsOutput.getIsConverted() == 1) {
                playButton.setVisibility(View.VISIBLE);
                preorderButton.setVisibility(View.GONE);


            }
            videoTitle.setVisibility(View.VISIBLE);
            Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
            videoTitle.setTypeface(castDescriptionTypeface);
            videoTitle.setText(contentDetailsOutput.getName());

            if (contentDetailsOutput.getTrailerUrl().matches("") || contentDetailsOutput.getTrailerUrl().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                watchTrailerButton.setVisibility(View.INVISIBLE);
            } else {
                watchTrailerButton.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.VIEW_TRAILER, Util.DEFAULT_VIEW_TRAILER));

                watchTrailerButton.setVisibility(View.VISIBLE);
            }

            if (contentDetailsOutput.getGenre() != null && contentDetailsOutput.getGenre().matches("") || contentDetailsOutput.getGenre().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                videoGenreTextView.setVisibility(View.GONE);

            } else {
                videoGenreTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                videoGenreTextView.setTypeface(videoGenreTextViewTypeface);
                videoGenreTextView.setText(contentDetailsOutput.getGenre());

            }
            if(contentDetailsOutput.getVideoDuration().matches("") || contentDetailsOutput.getVideoDuration().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
                videoDurationTextView.setVisibility(View.GONE);

            }else{

                videoDurationTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
                videoDurationTextView.setTypeface(videoGenreTextViewTypeface);
                videoDurationTextView.setText(contentDetailsOutput.getVideoDuration());
            }


            if(contentDetailsOutput.getReleaseDate().matches("") || contentDetailsOutput.getReleaseDate().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
                videoReleaseDateTextView.setVisibility(View.GONE);
            }else{
                videoReleaseDateTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
                videoReleaseDateTextView.setTypeface(videoGenreTextViewTypeface);
                movieReleaseDateStr = Util.formateDateFromstring("yyyy-mm-dd", "yyyy", contentDetailsOutput.getReleaseDate());
                videoReleaseDateTextView.setText(movieReleaseDateStr);

            }

            if(contentDetailsOutput.getStory().matches("") || contentDetailsOutput.getStory().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
                videoStoryTextView.setVisibility(View.GONE);

            }else{
                videoStoryTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
                videoStoryTextView.setTypeface(videoGenreTextViewTypeface);
                videoStoryTextView.setText(contentDetailsOutput.getStory());

            }

            if (contentDetailsOutput.getCensorRating().matches("") || contentDetailsOutput.getCensorRating().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                videoCensorRatingTextView.setVisibility(View.GONE);
                videoCensorRatingTextView1.setVisibility(View.GONE);

            } else {

                if (contentDetailsOutput.getCensorRating().contains("-")) {
                    String Data[] = contentDetailsOutput.getCensorRating().split("-");
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
                    videoCensorRatingTextView.setText(contentDetailsOutput.getCensorRating());
                }


            }

       /* if (castValue == true){
            videoCastCrewTitleTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.CAST_CREW_BUTTON_TITLE, Util.DEFAULT_CAST_CREW_BUTTON_TITLE));
            Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
            videoCastCrewTitleTextView.setTypeface(videoGenreTextViewTypeface);
            videoCastCrewTitleTextView.setVisibility(View.VISIBLE);
        }
*/
            if (contentDetailsOutput.getBanner().trim().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {

                if (contentDetailsOutput.getPoster().trim().matches(Util.getTextofLanguage(MovieDetailsActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {

                    moviePoster.setImageResource(R.drawable.logo);
                } else {


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(MovieDetailsActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.logo)
                            .showImageOnLoading(R.drawable.logo).build();
                    imageLoader.displayImage(contentDetailsOutput.getPoster(), moviePoster, options);

                }

            } else {


                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(MovieDetailsActivity.this));

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.logo)
                        .showImageOnFail(R.drawable.logo)
                        .showImageOnLoading(R.drawable.logo).build();
                imageLoader.displayImage(contentDetailsOutput.getPoster().trim(), moviePoster, options);


            }

        }else {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }

        Log.v("SUBHA","call review details");
        GetReviewDetails();


    }

    @Override
    public void onViewContentRatingPreExecuteStarted() {
        loadMovieDetailspDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        loadMovieDetailspDialog.show();
    }

    @Override
    public void onViewContentRatingPostExecuteCompleted(ViewContentRatingOutputModel viewContentRatingOutputModel,
                                                        int status, String message) {
        try{
            if (loadMovieDetailspDialog != null && loadMovieDetailspDialog.isShowing()) {
                loadMovieDetailspDialog.hide();
                loadMovieDetailspDialog = null;
            }
        }
        catch(IllegalArgumentException ex)
        {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }

        if(status==200) {

            String loggedInStr = preferenceManager.getUseridFromPref();
            Log.v("SUBHA","review data"+ reviews);
            Log.v("SUBHA","rating data"+ rating);

            if (reviews.equalsIgnoreCase("0")) {
                viewRatingTextView.setVisibility(View.GONE);

            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(Float.parseFloat(rating));
            }
            if (rating.equalsIgnoreCase("0")) {
                ratingBar.setVisibility(View.GONE);
            } else {
                Log.v("SUBHA","rating ==== "+ rating);
                viewRatingTextView.setVisibility(View.VISIBLE);

                if (preferenceManager.getLoginFeatureFromPref() == 1) {

                    if (loggedInStr == null) {
                        viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW));
                        Log.v("SUBHA","rating 0 ==== "+ viewContentRatingOutputModel.getShowrating());
                    } else {
                        if (viewContentRatingOutputModel.getShowrating() == 1) {
                            viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW));
                            Log.v("SUBHA","rating 1 ==== "+ viewContentRatingOutputModel.getShowrating());
                        } else {
                            Log.v("SUBHA","rating 2 ==== "+ viewContentRatingOutputModel.getShowrating());
                            viewRatingTextView.setText("reviews (" + reviews + ")");
                            viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.REVIEWS, Util.DEFAULT_REVIEWS) + " (" + reviews + ") ");

                        }

                    }
                } else {
                    viewRatingTextView.setText(Util.getTextofLanguage(MovieDetailsActivity.this, Util.ADD_A_REVIEW, Util.DEFAULT_ADD_A_REVIEW));

                }
            }

            /***favorite *****/

            if (loggedInStr != null && isFavorite == 0 && Util.favorite_clicked == true) {

                Util.favorite_clicked = false;
                AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                addToFavInputModel.setAuthToken(Util.authTokenStr);
                addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                addToFavInputModel.setIsEpisodeStr(isEpisode);

                asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, MovieDetailsActivity.this, MovieDetailsActivity.this);
                asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);
            } else if (loggedInStr != null && isFavorite == 1) {

                favorite_view.setImageResource(R.drawable.favorite_red);
            }
            /***favorite *****/
        }else{

        }
    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
        pDialog.show();

    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {
        favorite_view.setImageResource(R.drawable.favorite_unselected);
        showToast();
        isFavorite = 0;
        if(pDialog.isShowing()&& pDialog!=null)
        {
            pDialog.hide();
        }
    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(MovieDetailsActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

    }




}
