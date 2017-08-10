package com.home.vod.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Get_Video_Details_Output;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.vod.BuildConfig;
import com.home.vod.R;
import com.home.vod.adapter.EpisodesListAdapter;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
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
import org.json.JSONArray;
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
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.BUTTON_APPLY;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_APPLY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEASON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_TRAILER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.SEASON;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.VIEW_TRAILER;
import static com.home.vod.preferences.LanguagePreference.YES;
import static com.home.vod.util.Constant.CAST_INTENT_KEY;
import static com.home.vod.util.Constant.CENSOR_RATING_INTENT_KEY;
import static com.home.vod.util.Constant.GENRE_INTENT_KEY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.SEASON_INTENT_KEY;
import static com.home.vod.util.Constant.STORY_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;
import static com.muvi.player.utils.Util.DEFAULT_HAS_FAVORITE;
import static com.muvi.player.utils.Util.HAS_FAVORITE;

public class ShowWithEpisodesActivity extends AppCompatActivity implements VideoDetailsAsynctask.VideoDetails, GetValidateUserAsynTask.GetValidateUser,
        LogoutAsynctask.Logout, GetLanguageListAsynTask.GetLanguageList, GetContentDetailsAsynTask.GetContentDetails ,GetTranslateLanguageAsync.GetTranslateLanguageInfoListner {
    String movieDetailsStr = "";
    String priceForUnsubscribedStr, priceFosubscribedStr;
    PPVModel ppvmodel;
    APVModel advmodel;
    CurrencyModel currencymodel;
    String PlanId = "";
    LanguagePreference languagePreference;

    /*subtitle-------------------------------------*/

    String filename = "";
    static File mediaStorageDir;

    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
        /*subtitle-------------------------------------*/



    public static ProgressBarHandler progressBarHandler;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;

    GetContentDetailsAsynTask asynLoadMovieDetails;

    VideoDetailsAsynctask asynLoadVideoUrls;
    GetValidateUserAsynTask asynValidateUserDetails;

    Toolbar mActionBarToolbar;
    ProgressBarHandler pDialog;
    int isLogin = 0;
    String movieUniqueId = "";

    Get_Video_Details_Output get_video_details_output;

    Player playerModel;
    String censorRatingStr = "";
    private boolean isThirdPartyTrailer = false;
    AsynEpisodeDetails asynEpisodeDetails;
    int spinnerPosition = 0;
    ImageView moviePoster;
    ImageView playButton;
    ImageButton offlineImageButton;
    Button viewTrailerButton, btnmore, watchTrailerButton;
    TextView videoTitle, videoGenreTextView, videoDurationTextView, videoCensorRatingTextView, videoCensorRatingTextView1, videoReleaseDateTextView, videoCastCrewTitleTextView;
    RatingBar ratingBar;
    //SharedPreferences pref;
    Spinner season_spinner;
    ArrayList<String> season;
    EpisodesListModel itemToPlay;
    String videoResolution = "BEST";
    boolean castStr = false;
    String Season_Value = "";

    AlertDialog alert;


    RecyclerView.LayoutManager mLayoutManager;
    String movieStreamUniqueId, bannerImageId, posterImageId, movieReleaseDateStr, permalinkStr, movieTrailerUrlStr;
    String name, loggedInStr, planid;
    static String _permalink;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout;
    LinearLayout story_layout;
    int isFreeContent, isPPV, isConverted, contentTypesId, isAPV;
    RecyclerView seasontiveLayout;
    int status = 0;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    int isSubscribedDataStr = 0;
    ExpandableTextView videoStoryTextView;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ArrayList<EpisodesListModel> itemData = new ArrayList<EpisodesListModel>();
    String movieNameStr;
    // TextView seasonTitleTextView;
    String episodeVideoUrlStr;
    TextView noDataTextView;
    TextView noInternetTextView;
    String isMemberSubscribed;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_with_episodes);
        Util.goToLibraryplayer = false;
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(ShowWithEpisodesActivity.this);
        isLogin = preferenceManager.getLoginFeatureFromPref();
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        btnmore = (Button) findViewById(R.id.btnMore);
// *************for exoplayer *************
        playerModel=new Player();
        get_video_details_output = new Get_Video_Details_Output();

        Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        btnmore.setTypeface(videoGenreTextViewTypeface);

        btnmore.setText(languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE));

        btnmore.setVisibility(View.GONE);
        playButton = (ImageView) findViewById(R.id.playButton);
        watchTrailerButton = (Button) findViewById(R.id.viewTrailerButton);
        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        watchTrailerButton.setTypeface(submitButtonTypeface);
        watchTrailerButton.setText(languagePreference.getTextofLanguage(VIEW_TRAILER, DEFAULT_VIEW_TRAILER));

        playButton.setVisibility(View.GONE);

        offlineImageButton = (ImageButton) findViewById(R.id.offlineImageButton);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoGenreTextView = (TextView) findViewById(R.id.videoGenreTextView);
        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        videoStoryTextView = (ExpandableTextView) findViewById(R.id.videoStoryTextView);
        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        playButton.setVisibility(View.GONE);

        videoCastCrewTitleTextView.setVisibility(View.GONE);
        ppvmodel = new PPVModel();
        advmodel = new APVModel();
        currencymodel = new CurrencyModel();
        PlanId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();
        season_spinner = (Spinner) findViewById(R.id.seasonSpinner);

        if (Build.VERSION.SDK_INT < 23) {
            season_spinner.setBackgroundResource(R.drawable.lollipop_spinner_theme);
        }

        season_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerPosition = position;
                if (asynEpisodeDetails != null) {
                    asynEpisodeDetails.cancel(true);
                }
                btnmore.setVisibility(View.VISIBLE);
                asynEpisodeDetails = new AsynEpisodeDetails();
                asynEpisodeDetails.executeOnExecutor(threadPoolExecutor);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
      /*  seasonTitleTextView = (TextView) findViewById(R.id.seasonTitleTextView);
        seasonTitleTextView.setText(languagePreference.getTextofLanguage(SEASON, Util.DEFAULT_SEASON));
*/
        seasontiveLayout = (RecyclerView) findViewById(R.id.featureContent);
        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));

        mLayoutManager = new LinearLayoutManager(ShowWithEpisodesActivity.this, LinearLayoutManager.HORIZONTAL, false);
        iconImageRelativeLayout = (RelativeLayout) findViewById(R.id.iconImageRelativeLayout);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        story_layout = (LinearLayout) findViewById(R.id.story_layout);
        seasontiveLayout.setVisibility(View.GONE);
        episodeVideoUrlStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        seasontiveLayout.addItemDecoration(new HorizotalSpaceItemDecoration(20));

        watchTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// *****************set data into playermdel for play in exoplayer************


                playerModel.setStreamUniqueId(movieStreamUniqueId);
                playerModel.setMovieUniqueId(movieUniqueId);
                playerModel.setUserId(preferenceManager.getUseridFromPref());
                playerModel.setEmailId(preferenceManager.getEmailIdFromPref());
                playerModel.setAuthTokenStr( authTokenStr.trim());
                playerModel.setRootUrl(BuildConfig.SERVICE_BASE_PATH);
                playerModel.setEpisode_id("0");
                playerModel.setIsFreeContent(isFreeContent);
                playerModel.setVideoTitle(movieNameStr);
                playerModel.setVideoStory(movieDetailsStr);
                playerModel.setVideoGenre(videoGenreTextView.getText().toString());
                playerModel.setVideoDuration(videoDurationTextView.getText().toString());
                playerModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                playerModel.setCensorRating(censorRatingStr);






                DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl("");
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


                if ((movieTrailerUrlStr.matches("")) || (movieTrailerUrlStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA)))) {
                    Util.showNoDataAlert(ShowWithEpisodesActivity.this);
                   /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();*/
                    return;
                } else if (isThirdPartyTrailer == false) {
                    /*if (mCastSession != null && mCastSession.isConnected()) {


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
                       // Util.showQueuePopup(ShowWithEpisodesActivity.this, view, mediaInfo);

                        togglePlayback();

                    }else {*/
                    final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, TrailerActivity.class);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(playVideoIntent);

                        }
                    });
                } else {
                    if (movieTrailerUrlStr.contains("://www.youtube") || movieTrailerUrlStr.contains("://www.youtu.be")) {
                        if (movieTrailerUrlStr.contains("live_stream?channel")) {
                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });
                        } else {

                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, YouTubeAPIActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);


                                }
                            });

                        }
                    } else {
                        final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(playVideoIntent);

                            }
                        });
                    }

                }

            }
        });

        loggedInStr = preferenceManager.getLoginStatusFromPref();
        planid = preferenceManager.getUseridFromPref();

        if (planid == null)
            planid = "0";

        if (loggedInStr == null)
            loggedInStr = "0";

        try {
            isSubscribedDataStr = Integer.parseInt(preferenceManager.getIsSubscribedFromPref());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLogin == 1) {
                    if (preferenceManager != null) {
                        String loggedInStr = preferenceManager.getLoginStatusFromPref();

                        if (loggedInStr == null) {
                            final Intent registerActivity = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    Util.check_for_subscription = 1;

                                    startActivity(registerActivity);


                                }
                            });

                            //showLoginDialog();
                        } else {
                            if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {

                                if (isFreeContent == 1) {
                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                    getVideoDetailsInput.setAuthToken(authTokenStr);
                                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                                } else {
                                    ValidateUserInput validateUserInput = new ValidateUserInput();
                                    validateUserInput.setAuthToken(authTokenStr);
                                    validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                                    validateUserInput.setMuviUniqueId(movieUniqueId.trim());
                                    validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                                    validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                                    validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                                    validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                    asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                                    asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                                }
                            } else {

                                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {

                        final Intent registerActivity = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                Util.check_for_subscription = 1;

                                startActivity(registerActivity);


                            }
                        });
                    }
                } else {
                    if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {
                        // MUVIlaxmi

                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(authTokenStr);
                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);


                    } else {
                        Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent episode = new Intent(ShowWithEpisodesActivity.this, Episode_list_Activity.class);
                episode.putExtra(PERMALINK_INTENT_KEY, _permalink);
                episode.putExtra(GENRE_INTENT_KEY, videoGenreTextView.getText().toString());
                episode.putExtra(STORY_INTENT_KEY, videoStoryTextView.getText().toString());
                episode.putExtra(CENSOR_RATING_INTENT_KEY, censorRatingStr);
                episode.putExtra(CAST_INTENT_KEY, castStr);
                episode.putExtra(SEASON_INTENT_KEY, Season_Value);
                episode.putExtra("content_types_id", "" + contentTypesId);

                Log.v("MUVI", "season intent = " + Season_Value);

                runOnUiThread(new Runnable() {
                    public void run() {
                        episode.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(episode);


                    }
                });

            }
        });
        videoCastCrewTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent episode = new Intent(ShowWithEpisodesActivity.this, CastAndCrewActivity.class);
                episode.putExtra("cast_movie_id", movieUniqueId.trim());
                runOnUiThread(new Runnable() {
                    public void run() {
                        episode.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(episode);


                    }
                });

            }
        });

           /*subtitle-------------------------------------*/


        if (ContextCompat.checkSelfPermission(ShowWithEpisodesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowWithEpisodesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(ShowWithEpisodesActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        111);
            } else {
                ActivityCompat.requestPermissions(ShowWithEpisodesActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);

            }
        } else {
            //Call whatever you want
            if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {

                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                contentDetailsInput.setAuthToken(authTokenStr);
                contentDetailsInput.setPermalink(permalinkStr);

                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput,ShowWithEpisodesActivity.this,ShowWithEpisodesActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);


            } else {
                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }
      /*  AsynLoadMovieDetails asyncLoadVideos = new AsynLoadMovieDetails();
        asyncLoadVideos.executeOnExecutor(threadPoolExecutor);*/
           /*subtitle-------------------------------------*/


         /*  *//*chromecast-------------------------------------*//*

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
*//***************chromecast**********************//*
*/
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Get_Video_Details_Output get_video_details_output, int statusCode, String stus, String message) {
        // get_video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // get_video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/


        if (statusCode == 200) {
            if (get_video_details_output.getThirdparty_url() == null || get_video_details_output.getThirdparty_url().matches("")) {
                if (get_video_details_output.getVideoUrl() != null || !get_video_details_output.getVideoUrl().matches("")) {
                    playerModel.setVideoUrl(get_video_details_output.getVideoUrl());
                    Log.v("BISHAL", "videourl===" + playerModel.getVideoUrl());
                    playerModel.setThirdPartyPlayer(false);
                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                }
            } else {
                if (get_video_details_output.getThirdparty_url() != null || !get_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(get_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                }
            }

            Util.dataModel.setVideoResolution(get_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(get_video_details_output.getVideoResolution());
            if(get_video_details_output.getPlayed_length()!=null && !get_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(get_video_details_output.getPlayed_length())));




            //dependency for datamodel
            Util.dataModel.setVideoUrl(get_video_details_output.getVideoUrl());
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
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                }
                Util.showNoDataAlert(ShowWithEpisodesActivity.this);
               /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                }


                // condition for checking if the response has third party url or not.
                if (get_video_details_output.getThirdparty_url()==null ||
                        get_video_details_output.getThirdparty_url().matches("")
                        ) {


                    playerModel.setThirdPartyPlayer(false);
                    final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
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

                                progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
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
                    final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
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

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }
            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();*/
            Util.showNoDataAlert(ShowWithEpisodesActivity.this);
        }




    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        String Subscription_Str = preferenceManager.getIsSubscribedFromPref();


        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {
            status = 0;
        }

        if (validateUserOutput == null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                            startActivity(in);

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
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                            startActivity(in);

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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
                if (message != null && message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                }
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            } else if (status == 429) {

                if (validateUserOutput.getValiduser_str() != null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }

                    if ((validateUserOutput.getValiduser_str().trim().equalsIgnoreCase("OK")) || (validateUserOutput.getValiduser_str().trim().matches("OK")) || (validateUserOutput.getValiduser_str().trim().equals("OK"))) {
                        if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                                ShowPpvPopUp();
                            } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                                Intent intent = new Intent(ShowWithEpisodesActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            } else {
                                ShowPpvPopUp();
                            }
                        }

                    }
                }

            } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                ShowPpvPopUp();
            } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                Intent intent = new Intent(ShowWithEpisodesActivity.this, SubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else if (Util.dataModel.getIsConverted() == 0) {
                Util.showNoDataAlert(ShowWithEpisodesActivity.this);
                /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();*/
            } else {
                if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {
                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                    getVideoDetailsInput.setAuthToken(authTokenStr);
                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
        pDialog.show();

    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        if (status == null) {
            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(ShowWithEpisodesActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {
        if (progressBarHandler.isShowing()) {
            progressBarHandler.hide();
            progressBarHandler = null;

        }
    }

    @Override
    public void onGetContentDetailsPreExecuteStarted() {

    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {


        movieUniqueId = contentDetailsOutput.getMuviUniqId();
        //  castValue = contentDetailsOutput.getCastStr();


        Log.v("SUBHA","movieUniqueId====== kjdsvnib"+movieUniqueId);
        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        if (contentDetailsOutput.getIsApv() == 1) {
            playButton.setVisibility(View.INVISIBLE);

        } else if (contentDetailsOutput.getIsApv() == 0 && contentDetailsOutput.getIsPpv() == 0 && contentDetailsOutput.getIsConverted() == 0) {
            if (contentDetailsOutput.getContentTypesId().equals(4)) {
                playButton.setVisibility(View.VISIBLE);

            } else {
                playButton.setVisibility(View.INVISIBLE);

            }

        } else if (contentDetailsOutput.getIsApv() == 0 && contentDetailsOutput.getIsPpv() == 0 && contentDetailsOutput.getIsConverted() == 1) {
            playButton.setVisibility(View.VISIBLE);


        }
        videoTitle.setVisibility(View.VISIBLE);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        videoTitle.setTypeface(castDescriptionTypeface);
        videoTitle.setText(contentDetailsOutput.getName());

        if (contentDetailsOutput.getTrailerUrl().matches("") || contentDetailsOutput.getTrailerUrl().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
            watchTrailerButton.setVisibility(View.INVISIBLE);
        } else {
            watchTrailerButton.setText(languagePreference.getTextofLanguage(VIEW_TRAILER, DEFAULT_VIEW_TRAILER));

            watchTrailerButton.setVisibility(View.VISIBLE);
        }

        if (contentDetailsOutput.getGenre() != null && contentDetailsOutput.getGenre().matches("") || contentDetailsOutput.getGenre().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
            videoGenreTextView.setVisibility(View.GONE);

        } else {
            videoGenreTextView.setVisibility(View.VISIBLE);
            Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
            videoGenreTextView.setTypeface(videoGenreTextViewTypeface);
            videoGenreTextView.setText(contentDetailsOutput.getGenre());

        }
        if(contentDetailsOutput.getVideoDuration().matches("") ||
                contentDetailsOutput.getVideoDuration().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))){
            videoDurationTextView.setVisibility(View.GONE);

        }else{

            videoDurationTextView.setVisibility(View.VISIBLE);
            Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
            videoDurationTextView.setTypeface(videoGenreTextViewTypeface);
            videoDurationTextView.setText(contentDetailsOutput.getVideoDuration());
        }


        if(contentDetailsOutput.getReleaseDate().matches("") || contentDetailsOutput.getReleaseDate().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))){
            videoReleaseDateTextView.setVisibility(View.GONE);
        }else{
            videoReleaseDateTextView.setVisibility(View.VISIBLE);
            Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
            videoReleaseDateTextView.setTypeface(videoGenreTextViewTypeface);
            movieReleaseDateStr = Util.formateDateFromstring("yyyy-mm-dd", "yyyy", contentDetailsOutput.getReleaseDate());
            videoReleaseDateTextView.setText(movieReleaseDateStr);

        }

        if(contentDetailsOutput.getStory().matches("") || contentDetailsOutput.getStory().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))){
            videoStoryTextView.setVisibility(View.GONE);

        }else{
            videoStoryTextView.setVisibility(View.VISIBLE);
            Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
            videoStoryTextView.setTypeface(videoGenreTextViewTypeface);
            videoStoryTextView.setText(contentDetailsOutput.getStory());

        }

        if (contentDetailsOutput.getCensorRating().matches("") || contentDetailsOutput.getCensorRating().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
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
        if (contentDetailsOutput.getBanner().trim().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {

            if (contentDetailsOutput.getPoster().trim().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {

                moviePoster.setImageResource(R.drawable.logo);
            } else {


                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.logo)
                        .showImageOnFail(R.drawable.logo)
                        .showImageOnLoading(R.drawable.logo).build();
                imageLoader.displayImage(contentDetailsOutput.getPoster(), moviePoster, options);

            }

        } else {


            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.logo)
                    .showImageOnFail(R.drawable.logo)
                    .showImageOnLoading(R.drawable.logo).build();
            imageLoader.displayImage(contentDetailsOutput.getPoster().trim(), moviePoster, options);


        }
    }


    //Load Video Details Like VideoUrl,Release Date,Details,BannerUrl,rating,popularity etc.

    private class AsynLoadMovieDetails extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        String movieTypeStr;
        String videoduration = "";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getContentDetailsUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", authTokenStr.trim());
                httppost.addHeader("permalink", permalinkStr);
                //  httppost.addHeader("country",((Global)getApplicationContext()).getCountryCode());
                String countryCodeStr = preferenceManager.getCountryCodeFromPref();

                if (countryCodeStr != null) {

                    httppost.addHeader("country", countryCodeStr);
                } else {
                    httppost.addHeader("country", "IN");

                }
                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);

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

                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();


                        }

                    });

                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
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

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                   /* JSONArray jsonArray=myJson.getJSONArray("seasons");
                    season=new ArrayList<>();
                    for(int j=0;j<jsonArray.length();j++){

                        season.add(languagePreference.getTextofLanguage(SEASON, Util.DEFAULT_SEASON)+" "+jsonArray.getString(j));


                    }*/
                    if ((myJson.has("epDetails")) && myJson.getString("epDetails").trim() != null && !myJson.getString("epDetails").trim().isEmpty() && !myJson.getString("epDetails").trim().equals("null") && !myJson.getString("epDetails").trim().matches("")) {
                        JSONObject epDetailsJson = myJson.getJSONObject("epDetails");
                        if ((epDetailsJson.has("series_number")) && epDetailsJson.getString("series_number").trim() != null && !epDetailsJson.getString("series_number").trim().isEmpty() && !epDetailsJson.getString("series_number").trim().equals("null") && !epDetailsJson.getString("series_number").trim().matches("")) {
                            String s[] = epDetailsJson.getString("series_number").split(",");
                            Arrays.sort(s);
                            if (season != null && season.size() > 0) {
                                season.clear();
                            }
                            season = new ArrayList<>();
                            for (int j = 0; j < s.length; j++) {

                                season.add(languagePreference.getTextofLanguage(SEASON, DEFAULT_SEASON) + " " + s[j]);


                            }
                          /*  for(int j=0;j<s.length;j++){

                                season.add(languagePreference.getTextofLanguage(SEASON, Util.DEFAULT_SEASON)+" "+s.get);


                            }*/
                        }

                    }


                }

                if (status > 0) {

                    if (status == 200) {

                        JSONObject mainJson = myJson.getJSONObject("movie");
                        if ((mainJson.has("name")) && mainJson.getString("name").trim() != null && !mainJson.getString("name").trim().isEmpty() && !mainJson.getString("name").trim().equals("null") && !mainJson.getString("name").trim().matches("")) {
                            movieNameStr = mainJson.getString("name");
                        } else {
                            movieNameStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("video_duration")) && mainJson.getString("video_duration").trim() != null && !mainJson.getString("video_duration").trim().isEmpty() && !mainJson.getString("video_duration").trim().equals("null") && !mainJson.getString("video_duration").trim().matches("")) {
                            videoduration = mainJson.getString("video_duration");
                            playerModel.setVideoDuration(videoduration);


                        } else {
                            videoduration = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("trailerThirdpartyUrl")) && mainJson.getString("trailerThirdpartyUrl").trim() != null && !mainJson.getString("trailerThirdpartyUrl").trim().isEmpty() && !mainJson.getString("trailerThirdpartyUrl").trim().equals("null") && !mainJson.getString("trailerThirdpartyUrl").trim().matches("")) {
                            movieTrailerUrlStr = mainJson.getString("trailerThirdpartyUrl");
                            isThirdPartyTrailer = true;
                        } else {

                            if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                                movieTrailerUrlStr = mainJson.getString("trailerUrl");

                            } else {
                                movieTrailerUrlStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
                            }
                            isThirdPartyTrailer = false;
                        }
                        if ((mainJson.has("genre")) && mainJson.getString("genre").trim() != null && !mainJson.getString("genre").trim().isEmpty() && !mainJson.getString("genre").trim().equals("null") && !mainJson.getString("genre").trim().matches("")) {
                            movieTypeStr = mainJson.getString("genre");
                            movieTypeStr = movieTypeStr.replaceAll("\\[", "");
                            movieTypeStr = movieTypeStr.replaceAll("\\]", "");
                            movieTypeStr = movieTypeStr.replaceAll(",", " , ");
                            movieTypeStr = movieTypeStr.replaceAll("\"", "");


                        } else {
                            movieTypeStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("censor_rating")) && mainJson.getString("censor_rating").trim() != null && !mainJson.getString("censor_rating").trim().isEmpty() && !mainJson.getString("censor_rating").trim().equals("null") && !mainJson.getString("censor_rating").trim().matches("")) {
                            censorRatingStr = mainJson.getString("censor_rating");
                            censorRatingStr = censorRatingStr.replaceAll("\\[", "");
                            censorRatingStr = censorRatingStr.replaceAll("\\]", "");
                            censorRatingStr = censorRatingStr.replaceAll(",", " ");
                            censorRatingStr = censorRatingStr.replaceAll("\"", "");


                        } else {
                            censorRatingStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("story")) && mainJson.getString("story").trim() != null && !mainJson.getString("story").trim().isEmpty() && !mainJson.getString("story").trim().equals("null") && !mainJson.getString("story").trim().matches("")) {
                            movieDetailsStr = mainJson.getString("story");
                        } else {
                            movieDetailsStr = "";

                        }
                        if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                            movieTrailerUrlStr = mainJson.getString("trailerUrl");

                        } else {
                            movieTrailerUrlStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }
                        if ((mainJson.has("movie_stream_uniq_id")) && mainJson.getString("movie_stream_uniq_id").trim() != null && !mainJson.getString("movie_stream_uniq_id").trim().isEmpty() && !mainJson.getString("movie_stream_uniq_id").trim().equals("null") && !mainJson.getString("movie_stream_uniq_id").trim().matches("")) {
                            movieStreamUniqueId = mainJson.getString("movie_stream_uniq_id");
                        } else {
                            movieStreamUniqueId = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("muvi_uniq_id")) && mainJson.getString("muvi_uniq_id").trim() != null && !mainJson.getString("muvi_uniq_id").trim().isEmpty() && !mainJson.getString("muvi_uniq_id").trim().equals("null") && !mainJson.getString("muvi_uniq_id").trim().matches("")) {
                            movieUniqueId = mainJson.getString("muvi_uniq_id");
                        } else {
                            movieUniqueId = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }

                       /* if ((mainJson.has("movieUrl")) && mainJson.getString("movieUrl").trim() != null && !mainJson.getString("movieUrl").trim().isEmpty() && !mainJson.getString("movieUrl").trim().equals("null") && !mainJson.getString("movieUrl").trim().matches("")) {
                            movieVideoUrlStr = mainJson.getString("movieUrl");

                        }
                        else{
                            movieVideoUrlStr = getResources().getString(R.string.no_data_str);

                        }*/

                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
                            bannerImageId = mainJson.getString("banner");
                            bannerImageId = bannerImageId.replace("episode", "original");
                        } else {
                            bannerImageId = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                        }

                        if ((mainJson.has("poster")) && mainJson.getString("poster").trim() != null && !mainJson.getString("poster").trim().isEmpty() && !mainJson.getString("poster").trim().equals("null") && !mainJson.getString("poster").trim().matches("")) {
                            posterImageId = mainJson.getString("poster");
                            posterImageId = posterImageId.replace("episode", "original");

                        } else {
                            posterImageId = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

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
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
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
                responseStr = "0";
                e1.printStackTrace();
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);

                        //Commented By Me

                       /* movieDescription.setVisibility(View.GONE);
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
                responseStr = "0";
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

         /*   try{
                if(pDialog.isShowing())
                    pDialog.dismiss();
            }
            catch(IllegalArgumentException ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);

                        //Commented By Me

                       *//* movieDescription.setVisibility(View.GONE);
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
                        watchTrailerButton.setVisibility(View.GONE);*//*


                    }

                });
                responseStr = "0";
            }*/
            if (responseStr == null) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseStr = "0";
            }
            if ((responseStr.trim().equals("0"))) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);

                        story_layout.setVisibility(View.GONE);
                        bannerImageRelativeLayout.setVisibility(View.GONE);
                        iconImageRelativeLayout.setVisibility(View.GONE);

                        //Commented By Me

                       /* movieDescription.setVisibility(View.GONE);
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

                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();


            } else {

                noInternetConnectionLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);
                if (isAPV == 1) {
                    playButton.setVisibility(View.GONE);
                } else if (isAPV == 0 && isPPV == 0 && isConverted == 0) {
                    if (contentTypesId == 4) {
                        playButton.setVisibility(View.GONE);
                    } else {
                        playButton.setVisibility(View.GONE);
                    }

                } else if (isAPV == 0 && isPPV == 0 && isConverted == 1) {
                    playButton.setVisibility(View.GONE);

                }
                videoTitle.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
                videoTitle.setTypeface(videoGenreTextViewTypeface);
                videoTitle.setText(movieNameStr);

                if (movieTrailerUrlStr.matches("") || movieTrailerUrlStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    watchTrailerButton.setVisibility(View.INVISIBLE);
                } else {

                    watchTrailerButton.setVisibility(View.VISIBLE);
                }

                if (movieTypeStr != null && movieTypeStr.matches("") || movieTypeStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    videoGenreTextView.setVisibility(View.GONE);

                } else {
                    videoGenreTextView.setVisibility(View.VISIBLE);
                    Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoGenreTextView.setTypeface(watchTrailerButtonTypeface);
                    videoGenreTextView.setText(movieTypeStr);

                }
                if (videoduration.matches("") || videoduration.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    videoDurationTextView.setVisibility(View.GONE);

                } else {
                    videoDurationTextView.setVisibility(View.VISIBLE);
                    Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoDurationTextView.setTypeface(watchTrailerButtonTypeface);
                    videoDurationTextView.setText(videoduration);
                    iconImageRelativeLayout.setVisibility(View.VISIBLE);
                }


                if (movieReleaseDateStr.matches("") || movieReleaseDateStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    videoReleaseDateTextView.setVisibility(View.GONE);
                } else {
                    videoReleaseDateTextView.setVisibility(View.VISIBLE);
                    Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoReleaseDateTextView.setTypeface(watchTrailerButtonTypeface);
                    movieReleaseDateStr = Util.formateDateFromstring("yyyy-mm-dd", "mm-dd-yyyy", movieReleaseDateStr);
                    videoReleaseDateTextView.setText(movieReleaseDateStr);

                }

                if (movieDetailsStr.matches("") || movieDetailsStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    videoStoryTextView.setVisibility(View.GONE);

                } else {
                    //  videoStoryTextView.setMaxLines(3);
                    videoStoryTextView.setVisibility(View.VISIBLE);
                    Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoStoryTextView.setTypeface(watchTrailerButtonTypeface);
                    // makeTextViewResizable(videoStoryTextView,3,movieDetailsStr,true);
                    videoStoryTextView.setText(movieDetailsStr);
                    //customTextView(movieDetailsStr);
                    //makeTextViewResizable(videoStoryTextView, 3, "See More", true);
                    // customTextView(movieDetailsStr);
                    // videoStoryTextView.setText(movieDetailsStr);
                  /*  String tempStr = movieDetailsStr+"View More";
                    String word = "View More";

                    SpannableString ss = new SpannableString(tempStr);
                    ClickableSpan span1 = new ClickableSpan() {
                        @Override
                        public void onClick(View textView) {
                            // do some thing
                        }
                    };


                    ss.setSpan(span1, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

                    //videoStoryTextView.setText(movieDetailsStr);
                    //videoStoryTextView.setMovementMethod(LinkMovementMethod.getInstance());


                }

                if (castStr == true) {
                    Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoCastCrewTitleTextView.setTypeface(watchTrailerButtonTypeface);
                    videoCastCrewTitleTextView.setText(languagePreference.getTextofLanguage(CAST_CREW_BUTTON_TITLE, DEFAULT_CAST_CREW_BUTTON_TITLE));

                    videoCastCrewTitleTextView.setVisibility(View.VISIBLE);
                }
               /* if(censorRatingStr.matches("") || censorRatingStr.matches(getResources().getString(R.string.no_data_str))){
                    videoCensorRatingTextView.setVisibility(View.GONE);
                }else{
                    videoCensorRatingTextView.setVisibility(View.VISIBLE);
                    videoCensorRatingTextView.setText(censorRatingStr);
                }
*/
                if (censorRatingStr.matches("") || censorRatingStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    videoCensorRatingTextView.setVisibility(View.GONE);
                    videoCensorRatingTextView1.setVisibility(View.GONE);

                } else {

                    if (censorRatingStr.contains("-")) {
                        String Data[] = censorRatingStr.split("-");
                        videoCensorRatingTextView.setVisibility(View.VISIBLE);
                        videoCensorRatingTextView1.setVisibility(View.VISIBLE);
                        Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                        videoCensorRatingTextView.setTypeface(watchTrailerButtonTypeface);
                        videoCensorRatingTextView1.setTypeface(watchTrailerButtonTypeface);
                        videoCensorRatingTextView.setText(Data[0]);
                        videoCensorRatingTextView1.setText(Data[1]);

                    } else {
                        videoCensorRatingTextView.setVisibility(View.VISIBLE);
                        videoCensorRatingTextView1.setVisibility(View.GONE);
                        Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                        videoCensorRatingTextView.setTypeface(watchTrailerButtonTypeface);
                        videoCensorRatingTextView.setText(censorRatingStr);
                    }


                }
                season_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter adapter = new ArrayAdapter(ShowWithEpisodesActivity.this, R.layout.dropdownlist, season);
                season_spinner.setAdapter(adapter);

//                season_spinner.setBackgroundResource(R.drawable.spinner_theme);

                if (bannerImageId.trim().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {

                    if (posterImageId.trim().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {

                        moviePoster.setImageResource(R.drawable.logo);
                    } else {


                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                .cacheOnDisc(true).resetViewBeforeLoading(true)
                                .showImageForEmptyUri(R.drawable.logo)
                                .showImageOnFail(R.drawable.logo)
                                .showImageOnLoading(R.drawable.logo).build();
                        imageLoader.displayImage(posterImageId, moviePoster, options);

                    }

                } else {


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.logo)
                            .showImageOnLoading(R.drawable.logo).build();
                    imageLoader.displayImage(bannerImageId.trim(), moviePoster, options);


                }
                if (contentTypesId == 3) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
//                    AsynEpisodeDetails asynEpisodeDetails=new AsynEpisodeDetails();
//                    asynEpisodeDetails.executeOnExecutor(threadPoolExecutor);
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
            pDialog.show();

        }


    }

    private class AsynEpisodeDetails extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pDialog;
        String responseStr;
        String movieThirdPartyUrl = "";
        int episodeContenTTypesId = 0;

        int status;

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = APIUrlConstant.getGetEpisodeDetailsUrl();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", authTokenStr.trim());
                httppost.addHeader("permalink", permalinkStr.trim());
                httppost.addHeader("limit", "4");
                httppost.addHeader("offset", "1");
                String countryCodeStr = preferenceManager.getCountryCodeFromPref();

                if (countryCodeStr != null) {

                    httppost.addHeader("country", countryCodeStr);
                } else {
                    httppost.addHeader("country", "IN");

                }
                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

                String data = season.get(spinnerPosition);
                String[] data1 = data.split(" ");

                if (data1.length > 0) {

                    httppost.addHeader("series_number", data1[1].trim());
                    Season_Value = data1[1].trim();

                } else {

                }
                //httppost.addHeader("deviceType", "roku");
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {


                } catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    String items = myJson.optString("item_count");
                    movieUniqueId = myJson.optString("muvi_uniq_id");
                    _permalink = myJson.optString("permalink");

                }


                if (status > 0) {

                    if (status == 200) {

                        itemData.clear();

                        if ((myJson.has("is_ppv")) && myJson.getString("is_ppv").trim() != null && !myJson.getString("is_ppv").trim().isEmpty() && !myJson.getString("is_ppv").trim().equals("null") && !myJson.getString("is_ppv").trim().matches("")) {

                            isPPV = Integer.parseInt(myJson.getString("is_ppv"));
                        } else {
                            isPPV = 0;


                        }
                        if ((myJson.has("is_advance")) && myJson.getString("is_advance").trim() != null && !myJson.getString("is_advance").trim().isEmpty() && !myJson.getString("is_advance").trim().equals("null") && !myJson.getString("is_advance").trim().matches("")) {

                            isAPV = Integer.parseInt(myJson.getString("is_advance"));
                        } else {
                            isAPV = 0;


                        }

                        if (isPPV == 1) {
                            JSONObject ppvJson = null;
                            if ((myJson.has("ppv_pricing"))) {


                                ppvJson = myJson.getJSONObject("ppv_pricing");
                                if ((ppvJson.has("price_for_unsubscribed")) && ppvJson.getString("price_for_unsubscribed").trim() != null && !ppvJson.getString("price_for_unsubscribed").trim().isEmpty() && !ppvJson.getString("price_for_unsubscribed").trim().equals("null") && !ppvJson.getString("price_for_unsubscribed").trim().matches("")) {
                                    // priceForUnsubscribedStr = ppvJson.getString("price_for_unsubscribed");
                                    ppvmodel.setPPVPriceForUnsubscribedStr(ppvJson.getString("price_for_unsubscribed"));
                                } else {
                                    // priceForUnsubscribedStr = "0.0";
                                    ppvmodel.setPPVPriceForUnsubscribedStr("0.0");


                                }
                                if ((ppvJson.has("price_for_subscribed")) && ppvJson.getString("price_for_subscribed").trim() != null && !ppvJson.getString("price_for_subscribed").trim().isEmpty() && !ppvJson.getString("price_for_subscribed").trim().equals("null") && !ppvJson.getString("price_for_subscribed").trim().matches("")) {
                                    //priceFosubscribedStr = ppvJson.getString("price_for_subscribed");
                                    ppvmodel.setPPVPriceForUnsubscribedStr(ppvJson.getString("price_for_subscribed"));

                                } else {
                                    // priceFosubscribedStr = "0.0";
                                    ppvmodel.setPPVPriceForUnsubscribedStr("0.0");

                                }
                                if ((ppvJson.has("id")) && ppvJson.getString("id").trim() != null && !ppvJson.getString("id").trim().isEmpty() && !ppvJson.getString("id").trim().equals("null") && !ppvJson.getString("id").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvPlanId(ppvJson.getString("id"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvPlanId("0");

                                }
                                //MUVIShree
                                if ((ppvJson.has("show_unsubscribed")) && ppvJson.getString("show_unsubscribed").trim() != null && !ppvJson.getString("show_unsubscribed").trim().isEmpty() && !ppvJson.getString("show_unsubscribed").trim().equals("null") && !ppvJson.getString("show_unsubscribed").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvShowUnsubscribedStr(ppvJson.getString("show_unsubscribed"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvShowUnsubscribedStr("0.0");


                                }
                                if ((ppvJson.has("show_subscribed")) && ppvJson.getString("show_subscribed").trim() != null && !ppvJson.getString("show_subscribed").trim().isEmpty() && !ppvJson.getString("show_subscribed").trim().equals("null") && !ppvJson.getString("show_subscribed").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvShowSubscribedStr(ppvJson.getString("show_subscribed"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvShowSubscribedStr("0.0");


                                }

                                if ((ppvJson.has("season_unsubscribed")) && ppvJson.getString("season_unsubscribed").trim() != null && !ppvJson.getString("season_unsubscribed").trim().isEmpty() && !ppvJson.getString("season_unsubscribed").trim().equals("null") && !ppvJson.getString("season_unsubscribed").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvSeasonUnsubscribedStr(ppvJson.getString("season_unsubscribed"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvSeasonUnsubscribedStr("0.0");


                                }
                                if ((ppvJson.has("season_subscribed")) && ppvJson.getString("season_subscribed").trim() != null && !ppvJson.getString("season_subscribed").trim().isEmpty() && !ppvJson.getString("season_subscribed").trim().equals("null") && !ppvJson.getString("season_subscribed").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvSeasonSubscribedStr(ppvJson.getString("season_subscribed"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvSeasonSubscribedStr("0.0");


                                }
                                if ((ppvJson.has("episode_unsubscribed")) && ppvJson.getString("episode_unsubscribed").trim() != null && !ppvJson.getString("episode_unsubscribed").trim().isEmpty() && !ppvJson.getString("episode_unsubscribed").trim().equals("null") && !ppvJson.getString("episode_unsubscribed").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvEpisodeUnsubscribedStr(ppvJson.getString("episode_unsubscribed"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvEpisodeUnsubscribedStr("0.0");


                                }
                                if ((ppvJson.has("episode_subscribed")) && ppvJson.getString("episode_subscribed").trim() != null && !ppvJson.getString("episode_subscribed").trim().isEmpty() && !ppvJson.getString("episode_subscribed").trim().equals("null") && !ppvJson.getString("episode_subscribed").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setPpvEpisodeSubscribedStr(ppvJson.getString("episode_subscribed"));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setPpvEpisodeSubscribedStr("0.0");


                                }
                                if ((ppvJson.has("is_show")) && ppvJson.getString("is_show").trim() != null && !ppvJson.getString("is_show").trim().isEmpty() && !ppvJson.getString("is_show").trim().equals("null") && !ppvJson.getString("is_show").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setIsShow(Integer.parseInt(ppvJson.getString("is_show")));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setIsShow(0);


                                }
                                if ((ppvJson.has("is_season")) && ppvJson.getString("is_season").trim() != null && !ppvJson.getString("is_season").trim().isEmpty() && !ppvJson.getString("is_season").trim().equals("null") && !ppvJson.getString("is_season").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setIsSeason(Integer.parseInt(ppvJson.getString("is_season")));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setIsSeason(0);


                                }
                                if ((ppvJson.has("is_episode")) && ppvJson.getString("is_episode").trim() != null && !ppvJson.getString("is_episode").trim().isEmpty() && !ppvJson.getString("is_episode").trim().equals("null") && !ppvJson.getString("is_episode").trim().matches("")) {
                                    //  planIdStr = ppvJson.getString("id");
                                    ppvmodel.setIsEpisode(Integer.parseInt(ppvJson.getString("is_episode")));

                                } else {
                                    //  planIdStr = "0";
                                    ppvmodel.setIsEpisode(0);
                                }

                                Util.ppvModel = ppvmodel;
                            }
                        }
                        if (isAPV == 1) {
                            JSONObject advJson = null;
                            if ((myJson.has("adv_pricing"))) {


                                advJson = myJson.getJSONObject("adv_pricing");
                                if ((advJson.has("price_for_unsubscribed")) && advJson.getString("price_for_unsubscribed").trim() != null && !advJson.getString("price_for_unsubscribed").trim().isEmpty() && !advJson.getString("price_for_unsubscribed").trim().equals("null") && !advJson.getString("price_for_unsubscribed").trim().matches("")) {
                                    //priceForUnsubscribedStr = advJson.getString("price_for_unsubscribed");
                                    advmodel.setAPVPriceForUnsubscribedStr(advJson.getString("price_for_unsubscribed"));
                                } else {
                                    //priceForUnsubscribedStr = "0.0";
                                    advmodel.setAPVPriceForUnsubscribedStr("0.0");


                                }
                                if ((advJson.has("price_for_subscribed")) && advJson.getString("price_for_subscribed").trim() != null && !advJson.getString("price_for_subscribed").trim().isEmpty() && !advJson.getString("price_for_subscribed").trim().equals("null") && !advJson.getString("price_for_subscribed").trim().matches("")) {
                                    //priceFosubscribedStr = advJson.getString("price_for_subscribed");
                                    advmodel.setAPVPriceForsubscribedStr(advJson.getString("price_for_subscribed"));
                                } else {
                                    //priceFosubscribedStr = "0.0";
                                    advmodel.setAPVPriceForsubscribedStr("0.0");

                                }
                                if ((advJson.has("id")) && advJson.getString("id").trim() != null && !advJson.getString("id").trim().isEmpty() && !advJson.getString("id").trim().equals("null") && !advJson.getString("id").trim().matches("")) {
                                    //planIdStr = advJson.getString("id");
                                    advmodel.setApvPlanId(advJson.getString("id"));
                                } else {
                                    // planIdStr = "0";
                                    advmodel.setApvPlanId("0");

                                }

                                Util.apvModel = advmodel;
                            }

                        }

                        if (isPPV == 1 || isAPV == 1) {
                            Log.v("MUVI", "currency");

                            JSONObject currencyJson = null;
                            if (myJson.has("currency") && myJson.getString("currency") != null && !myJson.getString("currency").equals("null")) {
                                currencyJson = myJson.getJSONObject("currency");


                                if (currencyJson.has("id") && currencyJson.getString("id").trim() != null && !currencyJson.getString("id").trim().isEmpty() && !currencyJson.getString("id").trim().equals("null") && !currencyJson.getString("id").trim().matches("")) {
                                    // currencyIdStr = currencyJson.getString("id");
                                    currencymodel.setCurrencyId(currencyJson.getString("id"));
                                    Log.v("MUVI", "currency id" + currencymodel.getCurrencyId());

                                } else {
                                    // currencyIdStr = "";
                                    currencymodel.setCurrencyId("");
                                }

                                if (currencyJson.has("country_code") && currencyJson.getString("country_code").trim() != null && !currencyJson.getString("country_code").trim().isEmpty() && !currencyJson.getString("country_code").trim().equals("null") && !currencyJson.getString("country_code").trim().matches("")) {
                                    //currencyCountryCodeStr = currencyJson.getString("country_code");
                                    currencymodel.setCurrencyCode(currencyJson.getString("country_code"));
                                } else {
                                    //currencyCountryCodeStr = "";
                                    currencymodel.setCurrencyCode("153");
                                }
                                if (currencyJson.has("symbol") && currencyJson.getString("symbol").trim() != null && !currencyJson.getString("symbol").trim().isEmpty() && !currencyJson.getString("symbol").trim().equals("null") && !currencyJson.getString("symbol").trim().matches("")) {
                                    //currencySymbolStr = currencyJson.getString("symbol");
                                    currencymodel.setCurrencySymbol(currencyJson.getString("symbol"));
                                } else {
                                    //currencySymbolStr = "";
                                    currencymodel.setCurrencySymbol("$");
                                }

                                Util.currencyModel = currencymodel;
                            }
                        }


                        JSONArray jsonMainNode = myJson.getJSONArray("episode");
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;

                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);
                                if ((jsonChildNode.has("thirdparty_url")) && jsonChildNode.getString("thirdparty_url").trim() != null && !jsonChildNode.getString("thirdparty_url").trim().isEmpty() && !jsonChildNode.getString("thirdparty_url").trim().equals("null") && !jsonChildNode.getString("thirdparty_url").trim().matches("")) {
                                    movieThirdPartyUrl = jsonChildNode.getString("thirdparty_url");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    episodeContenTTypesId = Integer.parseInt(jsonChildNode.getString("content_types_id"));

                                }

                                String episodeTitleStr = "";
                                if ((jsonChildNode.has("episode_title")) && jsonChildNode.getString("episode_title").trim() != null && !jsonChildNode.getString("episode_title").trim().isEmpty() && !jsonChildNode.getString("episode_title").trim().equals("null") && !jsonChildNode.getString("episode_title").trim().matches("")) {
                                    episodeTitleStr = jsonChildNode.getString("episode_title");

                                }
                                String episodeNoStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                                if ((jsonChildNode.has("episode_number")) && jsonChildNode.getString("episode_number").trim() != null && !jsonChildNode.getString("episode_number").trim().isEmpty() && !jsonChildNode.getString("episode_number").trim().equals("null") && !jsonChildNode.getString("episode_number").trim().matches("")) {
                                    episodeNoStr = jsonChildNode.getString("episode_number");

                                }
                                String episodeThirdParty = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

                                if ((jsonChildNode.has("thirdparty_url")) && jsonChildNode.getString("thirdparty_url").trim() != null && !jsonChildNode.getString("thirdparty_url").trim().isEmpty() && !jsonChildNode.getString("thirdparty_url").trim().equals("null") && !jsonChildNode.getString("thirdparty_url").trim().matches("")) {
                                    episodeThirdParty = jsonChildNode.getString("thirdparty_url");

                                }
                                String videodurationStr = "";

                                if ((jsonChildNode.has("video_duration")) && jsonChildNode.getString("video_duration").trim() != null && !jsonChildNode.getString("video_duration").trim().isEmpty() && !jsonChildNode.getString("video_duration").trim().equals("null") && !jsonChildNode.getString("video_duration").trim().matches("")) {
                                    videodurationStr = jsonChildNode.getString("video_duration");

                                }
                            /*    String episodeVideoUrlStr = getResources().getString(R.string.no_data_str);

                                if ((jsonChildNode.has("embeddedUrl")) && jsonChildNode.getString("embeddedUrl").trim() != null && !jsonChildNode.getString("embeddedUrl").trim().isEmpty() && !jsonChildNode.getString("embeddedUrl").trim().equals("null") && !jsonChildNode.getString("embeddedUrl").trim().matches("")) {
                                    episodeVideoUrlStr = jsonChildNode.getString("embeddedUrl");

                                }
*/

                                String episodeImageStr = "";

                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    episodeImageStr = jsonChildNode.getString("poster_url");

                                }
                                String episodeDateStr = "";

                                if ((jsonChildNode.has("episode_date")) && jsonChildNode.getString("episode_date").trim() != null && !jsonChildNode.getString("episode_date").trim().isEmpty() && !jsonChildNode.getString("episode_date").trim().equals("null") && !jsonChildNode.getString("episode_date").trim().matches("")) {
                                    episodeDateStr = jsonChildNode.getString("episode_date");
                                    episodeDateStr = Util.formateDateFromstring("yyyy-mm-dd", "mm-dd-yyyy", episodeDateStr);
                                }
                                String episodeStoryStr = "";

                                if ((jsonChildNode.has("episode_story")) && jsonChildNode.getString("episode_story").trim() != null && !jsonChildNode.getString("episode_story").trim().isEmpty() && !jsonChildNode.getString("episode_story").trim().equals("null") && !jsonChildNode.getString("episode_story").trim().matches("")) {
                                    episodeStoryStr = jsonChildNode.getString("episode_story");

                                }
                                String episodeSeriesNoStr = "";

                                if ((jsonChildNode.has("series_number")) && jsonChildNode.getString("series_number").trim() != null && !jsonChildNode.getString("series_number").trim().isEmpty() && !jsonChildNode.getString("series_number").trim().equals("null") && !jsonChildNode.getString("series_number").trim().matches("")) {
                                    episodeSeriesNoStr = jsonChildNode.getString("series_number");

                                }


                                String episodeMovieStreamUniqueIdStr = "";

                                if ((jsonChildNode.has("movie_stream_uniq_id")) && jsonChildNode.getString("movie_stream_uniq_id").trim() != null && !jsonChildNode.getString("movie_stream_uniq_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_uniq_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_uniq_id").trim().matches("")) {
                                    episodeMovieStreamUniqueIdStr = jsonChildNode.getString("movie_stream_uniq_id");

                                }


                                itemData.add(new EpisodesListModel(episodeNoStr, episodeStoryStr, episodeDateStr, episodeImageStr, episodeTitleStr, episodeVideoUrlStr, episodeSeriesNoStr, movieUniqueId, episodeMovieStreamUniqueIdStr, episodeThirdParty, episodeContenTTypesId, videodurationStr));
                            } catch (Exception e) {

                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                            }

                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

            if (responseStr == null) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }

            }
            if ((responseStr.trim().equals("0"))) {

            } else {
                if (itemData.size() <= 0) {

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }

                    //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    seasontiveLayout.setVisibility(View.VISIBLE);
                    seasontiveLayout.setLayoutManager(mLayoutManager);
                    seasontiveLayout.setItemAnimator(new DefaultItemAnimator());
                    EpisodesListAdapter mAdapter = new EpisodesListAdapter(ShowWithEpisodesActivity.this, R.layout.list_card_multipart, itemData, new EpisodesListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(EpisodesListModel item) {
                            clickItem(item);

                        }
                    });
                    seasontiveLayout.setAdapter(mAdapter);

                }
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
            pDialog.show();


        }


    }

    @Override
    public void onBackPressed() {
        if (asynValidateUserDetails != null) {
            asynValidateUserDetails.cancel(true);
        }
        if (asynLoadVideoUrls != null) {
            asynLoadVideoUrls.cancel(true);
        }

        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }


    public void clickItem(EpisodesListModel item) {

        itemToPlay = item;

        DataModel dbModel = new DataModel();
        dbModel.setIsFreeContent(isFreeContent);
        dbModel.setIsAPV(isAPV);
        dbModel.setIsPPV(isPPV);
        dbModel.setIsConverted(1);
        dbModel.setMovieUniqueId(movieUniqueId);
        dbModel.setStreamUniqueId(item.getEpisodeStreamUniqueId());
        dbModel.setThirdPartyUrl(item.getEpisodeThirdPartyUrl());
        dbModel.setVideoTitle(item.getEpisodeTitle());
        dbModel.setVideoStory(item.getEpisodeDescription());
        dbModel.setVideoGenre(videoGenreTextView.getText().toString());
        dbModel.setVideoDuration(item.getEpisodeDuration());
        // dbModel.setVideoReleaseDate(item.getEpisodeTelecastOn());
        dbModel.setVideoReleaseDate("");

        dbModel.setCensorRating(censorRatingStr);
        dbModel.setCastCrew(castStr);
        dbModel.setEpisode_id(item.getEpisodeStreamUniqueId());
        dbModel.setSeason_id(Season_Value);
        dbModel.setPurchase_type("episode");
        dbModel.setPosterImageId(item.getEpisodeThumbnailImageView());
        dbModel.setContentTypesId(contentTypesId);

        dbModel.setEpisode_series_no(item.getEpisodeSeriesNo());
        dbModel.setEpisode_no(item.getEpisodeNumber());
        dbModel.setEpisode_title(item.getEpisodeTitle());

        Util.dataModel = dbModel;
        SubTitleName.clear();
        SubTitlePath.clear();
        ResolutionUrl.clear();
        ResolutionFormat.clear();

        Log.v("MUVI", "content typesid = " + contentTypesId);

        if (isLogin == 1) {
            if (preferenceManager != null) {
                String loggedInStr = preferenceManager.getLoginStatusFromPref();

                if (loggedInStr == null) {
                    final Intent register = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            register.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            Util.check_for_subscription = 1;
                            startActivity(register);


                        }
                    });
                    //showLoginDialog();
                } else {
                    if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {

                        if (isFreeContent == 1) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            ValidateUserInput validateUserInput = new ValidateUserInput();
                            validateUserInput.setAuthToken(authTokenStr);
                            validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                            validateUserInput.setMuviUniqueId(movieUniqueId.trim());
                            validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                            validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                            validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                            validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, this, this);
                            asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                        }
                    } else {
                        Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }
            } else {

                final Intent register = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);

                runOnUiThread(new Runnable() {
                    public void run() {
                        register.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Util.check_for_subscription = 1;

                        startActivity(register);


                    }
                });
            }
        } else {
            if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {
                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(authTokenStr);
                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
            }
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
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
//                httppost.addHeader("internet_speed", MainActivity.internetSpeed.trim());
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
//                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                            Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    e.printStackTrace();
//                }
//
//              /*  JSONObject myJson =null;
//
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//*/
//
//                /**** subtitles************/
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
//                /**** subtitles************/
//
//                if (statusCode >= 0) {
//                    if (statusCode == 200) {
//                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA))) {
//                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
//
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//
//                            }
//                        }else{
//                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//
//
//                        }
//
//                        /**** subtitles************/
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
//                        /**** subtitles************/
//
//                    /******Resolution****/
//
//
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
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                }
//            } catch (JSONException e1) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                }
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_VIDEO_AVAILABLE,Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SORRY,Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_VIDEO_AVAILABLE,Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SORRY,Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA))) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_VIDEO_AVAILABLE,Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SORRY,Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    }
//                    if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA))) {
//
//                       /* if (mCastSession != null && mCastSession.isConnected()) {
//
//
//                            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
//
//                            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, Util.dataModel.getVideoStory());
//                            movieMetadata.putString(MediaMetadata.KEY_TITLE, Util.dataModel.getVideoTitle());
//                            movieMetadata.addImage(new WebImage(Uri.parse(itemToPlay.getEpisodeThumbnailImageView())));
//                            movieMetadata.addImage(new WebImage(Uri.parse(itemToPlay.getEpisodeThumbnailImageView())));
//                            JSONObject jsonObj = null;
//                            try {
//                                jsonObj = new JSONObject();
//                                jsonObj.put("description", Util.dataModel.getVideoTitle()
//                                );
//                            } catch (JSONException e) {
//                            }
//
//                            mediaInfo = new MediaInfo.Builder(Util.dataModel.getVideoUrl().trim())
//                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//                                    .setContentType("videos/mp4")
//                                    .setMetadata(movieMetadata)
//                                    .setStreamDuration(15 * 1000)
//                                    .setCustomData(jsonObj)
//                                    .build();
//                            mSelectedMedia = mediaInfo;
//
//
//                            togglePlayback();
//                        }else {*/
//                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
//                           /* runOnUiThread(new Runnable() {
//                                public void run() {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(playVideoIntent);
//
//                                }
//                            });*/
//
//                            /**subtitle**/
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    if(FakeSubTitlePath.size()>0)
//                                    {
//                                        // This Portion Will Be changed Later.
//
//                                        File dir = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
//                                        if (dir.isDirectory())
//                                        {
//                                            String[] children = dir.list();
//                                            for (int i = 0; i < children.length; i++)
//                                            {
//                                                new File(dir, children[i]).delete();
//                                            }
//                                        }
//
//                                        progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
//                                        progressBarHandler.show();
//                                        Download_SubTitle(FakeSubTitlePath.get(0).trim());
//                                    }
//                                    else
//                                    {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        playVideoIntent.putExtra("SubTitleName", SubTitleName);
//                                        playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
//                                        playVideoIntent.putExtra("ResolutionFormat",ResolutionFormat);
//                                        playVideoIntent.putExtra("ResolutionUrl",ResolutionUrl);
//                                        startActivity(playVideoIntent);
//                                    }
//
//                                }
//                            });
//                        }
//                    else{
//                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")){
//                            if(Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
//                                final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(playVideoIntent);
//
//                                    }
//                                });
//                            }else{
//
//                                final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, YouTubeAPIActivity.class);
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
//                        }else{
//                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);
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
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
//            pDialog.show();
//
//
//        }
//
//
//    }

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
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("movie_id", movieUniqueId.trim());
//                httppost.addHeader("purchase_type", Util.dataModel.getPurchase_type());
//                httppost.addHeader("season_id", Util.dataModel.getSeason_id());
//                httppost.addHeader("episode_id", Util.dataModel.getEpisode_id());
//             /*   SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
//                if (countryPref != null) {
//                    String countryCodeStr = countryPref.getString("countryCode", null);
//                    httppost.addHeader("country", countryCodeStr);
//                }else{
//                    httppost.addHeader("country", "IN");
//
//                }*/
//                httppost.addHeader("lang_code",Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
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
//                            Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
//                                startActivity(in);
//
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
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
//                                startActivity(in);
//
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
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
//                    if (userMessage != null && userMessage.equalsIgnoreCase("")) {
//                        dlgAlert.setMessage(userMessage);
//                    } else {
//                        dlgAlert.setMessage(languagePreference.getTextofLanguage(CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
//
//                    }
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
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
//                            if (Util.checkNetwork(ShowWithEpisodesActivity.this) == true) {
//                                GetVideoDetailsInput getVideoDetailsInput=new GetVideoDetailsInput();
//                                getVideoDetailsInput.setAuthToken(authTokenStr);
//                                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
//                                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
//                                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
//                                getVideoDetailsInput.setUser_id(pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//                                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput,ShowWithEpisodesActivity.this,ShowWithEpisodesActivity.this);
//                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                            } else {
//                                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                            }
//                        }
//                        else {
//
//                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid")))
//                            {
//                                if(Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1)
//                                {
//                                    ShowPpvPopUp();
//                                }
//                                else if(PlanId.equals("1") && Subscription_Str.equals("0"))
//                                {
//                                    Intent intent = new Intent(ShowWithEpisodesActivity.this,SubscriptionActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(intent);
//                                }
//                                else
//                                {
//                                    ShowPpvPopUp();
//                                }
//                            }
//
//                        }
//                    }
//
//                }
//                else if(Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1)
//                {
//                    ShowPpvPopUp();
//                }
//                else if(PlanId.equals("1") && Subscription_Str.equals("0"))
//                {
//                    Intent intent = new Intent(ShowWithEpisodesActivity.this,SubscriptionActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//                }
//                else if(Util.dataModel.getIsConverted() == 0)
//                {
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//                else
//                {
//                    if (Util.checkNetwork(ShowWithEpisodesActivity.this) == true) {
//                        GetVideoDetailsInput getVideoDetailsInput=new GetVideoDetailsInput();
//                        getVideoDetailsInput.setAuthToken(authTokenStr);
//                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
//                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
//                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
//                        getVideoDetailsInput.setUser_id(pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//                        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput,ShowWithEpisodesActivity.this,ShowWithEpisodesActivity.this);
//                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                    } else {
//                        Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                    }
//                }
//
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
//            pDialog.show();
//
//
//        }
//
//
//    }

    private void ShowPpvPopUp() {
        {


            try {
                if (Util.currencyModel.getCurrencySymbol() == null) {
                    Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) ShowWithEpisodesActivity.this.getSystemService(ShowWithEpisodesActivity.this.LAYOUT_INFLATER_SERVICE);

            View convertView = (View) inflater.inflate(R.layout.activity_ppv_popup, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("");

            final RadioButton completeRadioButton = (RadioButton) convertView.findViewById(R.id.completeRadioButton);
            final RadioButton seasonRadioButton = (RadioButton) convertView.findViewById(R.id.seasonRadioButton);
            final RadioButton episodeRadioButton = (RadioButton) convertView.findViewById(R.id.episodeRadioButton);
            TextView episodePriceTextView = (TextView) convertView.findViewById(R.id.episodePriceTextView);
            TextView seasonPriceTextView = (TextView) convertView.findViewById(R.id.seasonPriceTextView);
            TextView completePriceTextView = (TextView) convertView.findViewById(R.id.completePriceTextView);
            Button payNowButton = (Button) convertView.findViewById(R.id.payNowButton);


            if (Util.dataModel.getIsAPV() == 1) {
                if (Util.apvModel.getIsEpisode() == 1) {
                    episodeRadioButton.setVisibility(View.VISIBLE);
                    episodePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    episodeRadioButton.setVisibility(View.GONE);
                    episodePriceTextView.setVisibility(View.GONE);
                }
                if (Util.apvModel.getIsSeason() == 1) {
                    seasonRadioButton.setVisibility(View.VISIBLE);
                    seasonPriceTextView.setVisibility(View.VISIBLE);
                } else {
                    seasonRadioButton.setVisibility(View.GONE);
                    seasonPriceTextView.setVisibility(View.GONE);
                }
                if (Util.apvModel.getIsShow() == 1) {
                    completeRadioButton.setVisibility(View.VISIBLE);
                    completePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    completeRadioButton.setVisibility(View.GONE);
                    completePriceTextView.setVisibility(View.GONE);
                }
            } else {
                if (Util.ppvModel.getIsEpisode() == 1) {
                    episodeRadioButton.setVisibility(View.VISIBLE);
                    episodePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    episodeRadioButton.setVisibility(View.GONE);
                    episodePriceTextView.setVisibility(View.GONE);
                }
                if (Util.ppvModel.getIsSeason() == 1) {
                    seasonRadioButton.setVisibility(View.VISIBLE);
                    seasonPriceTextView.setVisibility(View.VISIBLE);
                } else {
                    seasonRadioButton.setVisibility(View.GONE);
                    seasonPriceTextView.setVisibility(View.GONE);
                }
                if (Util.ppvModel.getIsShow() == 1) {
                    completeRadioButton.setVisibility(View.VISIBLE);
                    completePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    completeRadioButton.setVisibility(View.GONE);
                    completePriceTextView.setVisibility(View.GONE);
                }
            }


            completeRadioButton.setText("  " + Util.dataModel.getEpisode_title().trim() + " Complete Season ");
            seasonRadioButton.setText("  " + Util.dataModel.getEpisode_title().trim() + " Season " + Util.dataModel.getEpisode_series_no().trim() + " ");
            episodeRadioButton.setText("  " + Util.dataModel.getEpisode_title().trim() + " S" + Util.dataModel.getEpisode_series_no().trim() + " E " + Util.dataModel.getEpisode_no().trim() + " ");

            String subscriptionStr = preferenceManager.getIsSubscribedFromPref();

            if (subscriptionStr.trim().equals("1")) {
                if (Util.dataModel.getIsAPV() == 1) {

                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeSubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvSeasonSubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowSubscribedStr());
                } else {
                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeSubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonSubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvShowSubscribedStr());
                }
            } else {
                if (Util.dataModel.getIsAPV() == 1) {

                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeUnsubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvSeasonUnsubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowUnsubscribedStr());
                } else {
                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeUnsubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonUnsubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvShowUnsubscribedStr());
                }
            }


            alert = alertDialog.show();
            completeRadioButton.setChecked(true);


        /*if (completeRadioButton.isChecked() == true) {
            if (Util.dataModel.getIsAPV() == 1) {
                priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

            } else {
                priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
            }
        }*/


            // Changed later

            if (completeRadioButton.getVisibility() == View.VISIBLE) {

                completeRadioButton.setChecked(true);
                episodeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);

                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
                }


            } else {
                if (seasonRadioButton.getVisibility() == View.VISIBLE) {

                    completeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(true);
                    episodeRadioButton.setChecked(false);

                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvSeasonSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvSeasonSubscribedStr();
                    }


                } else {
                    completeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(false);
                    episodeRadioButton.setChecked(true);

                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvEpisodeSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvEpisodeSubscribedStr();
                    }


                }
            }

            ///////////////////////=====================////////////////////////

            completeRadioButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    episodeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(false);
                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
                    }


                }

            });


            episodeRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(false);

                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvEpisodeSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvEpisodeSubscribedStr();
                    }

                }
            });
            seasonRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    episodeRadioButton.setChecked(false);
                    completeRadioButton.setChecked(false);
                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvSeasonSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvSeasonSubscribedStr();
                    }

                }
            });

            payNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completeRadioButton.isChecked()) {
                        Util.selected_episode_id = "0";
                        Util.selected_season_id = "0";
                        Log.v("MUVI", "called 1");
                    } else if (seasonRadioButton.isChecked()) {
                        Util.selected_episode_id = "0";
                        Util.selected_season_id = "" + spinnerPosition;
                        Log.v("MUVI", "called 2");
                    } else {
                        Util.selected_episode_id = Util.dataModel.getStreamUniqueId();
                        Util.selected_season_id = Util.dataModel.getEpisode_series_no();
                        Log.v("MUVI", "called 3");
                    }


                    Log.v("MUVI", "Show withepisode  Activity Season Id =" + Util.selected_season_id);
                    Log.v("MUVI", "Show withepisode Activity episode Id =" + Util.selected_episode_id);

                    alert.dismiss();
                    final Intent showPaymentIntent = new Intent(ShowWithEpisodesActivity.this, PPvPaymentInfoActivity.class);
                    showPaymentIntent.putExtra("muviuniqueid", Util.dataModel.getMovieUniqueId().trim());
                    showPaymentIntent.putExtra("episodeStreamId", Util.dataModel.getStreamUniqueId().trim());
                    showPaymentIntent.putExtra("content_types_id", Util.dataModel.getContentTypesId());
                    showPaymentIntent.putExtra("movieThirdPartyUrl", Util.dataModel.getThirdPartyUrl());
                    showPaymentIntent.putExtra("planUnSubscribedPrice", priceForUnsubscribedStr);
                    showPaymentIntent.putExtra("planSubscribedPrice", priceFosubscribedStr);
                    showPaymentIntent.putExtra("currencyId", Util.currencyModel.getCurrencyId());
                    showPaymentIntent.putExtra("currencyCountryCode", Util.currencyModel.getCurrencyCode());
                    showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
                    showPaymentIntent.putExtra("showName", Util.dataModel.getEpisode_title());
                    showPaymentIntent.putExtra("seriesNumber", Util.dataModel.getEpisode_series_no());
                    showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
                    showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
                    if (Util.dataModel.getIsAPV() == 1) {
                        showPaymentIntent.putExtra("isConverted", 0);
                    } else {
                        showPaymentIntent.putExtra("isConverted", 1);

                    }

                    showPaymentIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(showPaymentIntent);

                }
            });
        }
    }

    public class HorizotalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int horizontalSpaceWidth;

        public HorizotalSpaceItemDecoration(int horizontalSpaceWidth) {
            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.right = horizontalSpaceWidth;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

     /*   *//***************chromecast**********************//*

        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        *//***************chromecast**********************//*

*/
        MenuItem item, item1, item2, item3, item4, item5, item6;
        MenuItem item7 = null;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        if (preferenceManager.getLanguageListFromPref().equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);

        if (loggedInStr != null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            item5.setVisible(false);
           /* item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            item1.setVisible(true);

            if ((languagePreference.getTextofLanguage(HAS_FAVORITE,DEFAULT_HAS_FAVORITE).trim()).equals("1")) {
                item7.setVisible(true);
            }else{
                item7.setVisible(false);

            }
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(true);

            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            item3.setVisible(true);

        } else if (loggedInStr == null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            if (isLogin == 1) {
                item4.setVisible(true);
                item5.setVisible(true);

            } else {
                item4.setVisible(false);
                item5.setVisible(false);

            }
           /* item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            item1.setVisible(false);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            item3.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(ShowWithEpisodesActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(ShowWithEpisodesActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);

                if (Util.languageModel != null && Util.languageModel.size() > 0) {


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(ShowWithEpisodesActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(ShowWithEpisodesActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(SIGN_OUT_WARNING, DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(YES, DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(languagePreference.getTextofLanguage(NO, DEFAULT_NO), new DialogInterface.OnClickListener() {

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
//            String urlRouteList = Util.rootUrl().trim() + Util.logoutUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("login_history_id", loginHistoryIdStr);
//                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseCode = 0;
//                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                    responseCode = 0;
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    responseCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//            } catch (Exception e) {
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
//                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseStr == null) {
//                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode == 0) {
//                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode > 0) {
//                if (responseCode == 200) {
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.clear();
//                    editor.commit();
//                    SharedPreferences loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
//                    if (loginPref != null) {
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
//                    if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
//                            .trim()).equals("1")) {
//                        final Intent startIntent = new Intent(ShowWithEpisodesActivity.this, SplashScreen.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(startIntent);
//                                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
//                                finish();
//
//                            }
//                        });
//                    } else {
//                        final Intent startIntent = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(startIntent);
//                                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
//                                finish();
//
//                            }
//                        });
//                    }
//
//                } else {
//                    Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
//            pDialog.show();
//        }
//    }


    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(ShowWithEpisodesActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(languagePreference.getTextofLanguage(APP_SELECT_LANGUAGE, DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(languagePreference.getTextofLanguage(BUTTON_APPLY, DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        languageCustomAdapter = new LanguageCustomAdapter(ShowWithEpisodesActivity.this, Util.languageModel);
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
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(ShowWithEpisodesActivity.this, recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                languagePreference.setLanguageSharedPrefernce( SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
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

                    LanguageListInputModel languageListInputModel=new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    languageListInputModel.setLangCode(Default_Language);
                    GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel,ShowWithEpisodesActivity.this,ShowWithEpisodesActivity.this);
                    asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                languagePreference.setLanguageSharedPrefernce( SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
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

    @Override
    protected void onResume() {
        super.onResume();
    /*    *//***************chromecast**********************//*
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }



        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        *//***************chromecast**********************/
        invalidateOptionsMenu();

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
//                httppost.addHeader("authToken", authTokenStr.trim());
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
//                        default_Language = json.optString("default_lang");
//                        if (!languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, "").equals("")) {
//                            default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
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
//                            if (default_Language.equalsIgnoreCase(language_id)) {
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
//            progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
//            progressBarHandler.show();
//
//        }
//    }
    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {
        if (status > 0 && status == 200) {

            try {


                Util.parseLanguage(languagePreference,jsonResponse,Default_Language);
                languageCustomAdapter.notifyDataSetChanged();

                Intent intent = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Call For Other Methods.


        } else {
        }
    }

//    private class AsynGetTransalatedLanguage extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//        int status;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.LanguageTranslation.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", authTokenStr);
//                httppost.addHeader("lang_code", default_Language);
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
//                } catch (Exception e) {
//                }
//                if (responseStr != null) {
//                    JSONObject json = new JSONObject(responseStr);
//                    try {
//                        status = Integer.parseInt(json.optString("code"));
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
//            if (progressBarHandler != null && progressBarHandler.isShowing()) {
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
//                        JSONObject parent_json = new JSONObject(responseStr);
//                        JSONObject json = parent_json.getJSONObject("translation");
//
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ALREADY_MEMBER, json.optString("already_member").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ACTIAVTE_PLAN_TITLE, json.optString("activate_plan_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_STATUS_ACTIVE, json.optString("transaction_status_active").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ADD_TO_FAV, json.optString("add_to_fav").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ADDED_TO_FAV, json.optString("added_to_fav").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ADVANCE_PURCHASE, json.optString("advance_purchase").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ALERT, json.optString("alert").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.EPISODE_TITLE, json.optString("episodes_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SORT_ALPHA_A_Z, json.optString("sort_alpha_a_z").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SORT_ALPHA_Z_A, json.optString("sort_alpha_z_a").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.AMOUNT, json.optString("amount").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.COUPON_CANCELLED, json.optString("coupon_cancelled").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.BUTTON_APPLY, json.optString("btn_apply").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SIGN_OUT_WARNING, json.optString("sign_out_warning").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.DISCOUNT_ON_COUPON, json.optString("discount_on_coupon").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CREDIT_CARD_CVV_HINT, json.optString("credit_card_cvv_hint").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CAST, json.optString("cast").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CAST_CREW_BUTTON_TITLE, json.optString("cast_crew_button_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CENSOR_RATING, json.optString("censor_rating").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ENTER_EMPTY_FIELD, json.optString("enter_register_fields_data").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.HOME, json.optString("home").trim());
//
//
//                        if (json.optString("change_password").trim() == null || json.optString("change_password").trim().equals("")) {
//                            Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
//                        } else {
//                            Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
//                        }
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());
//
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CONFIRM_PASSWORD, json.optString("confirm_password").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.DIRECTOR, json.optString("director").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.DESCRIPTION, json.optString("description").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.EMAIL_EXISTS, json.optString("email_exists").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.EMAIL_DOESNOT_EXISTS, json.optString("email_does_not_exist").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.EMAIL_PASSWORD_INVALID, json.optString("email_password_invalid").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.COUPON_CODE_HINT, json.optString("coupon_code_hint").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SEARCH_ALERT, json.optString("search_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CREDIT_CARD_NUMBER_HINT, json.optString("credit_card_number_hint").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TEXT_EMIAL, json.optString("text_email").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NAME_HINT, json.optString("name_hint").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CREDIT_CARD_NAME_HINT, json.optString("credit_card_name_hint").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TEXT_PASSWORD, json.optString("text_password").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ERROR_IN_PAYMENT_VALIDATION, json.optString("error_in_payment_validation").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ERROR_IN_REGISTRATION, json.optString("error_in_registration").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_STATUS_EXPIRED, json.optString("transaction_status_expired").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.DETAILS_NOT_FOUND_ALERT, json.optString("details_not_found_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.FAILURE, json.optString("failure").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.FILTER_BY, json.optString("filter_by").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.FORGOT_PASSWORD, json.optString("forgot_password").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.GENRE, json.optString("genre").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.AGREE_TERMS, json.optString("agree_terms").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.INVALID_COUPON, json.optString("invalid_coupon").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.INVOICE, json.optString("invoice").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.LANGUAGE_POPUP_LANGUAGE, json.optString("language_popup_language").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SORT_LAST_UPLOADED, json.optString("sort_last_uploaded").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.LANGUAGE_POPUP_LOGIN, json.optString("language_popup_login").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.LOGIN, json.optString("login").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.LOGOUT, json.optString("logout").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.LOGOUT_SUCCESS, json.optString("logout_success").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.MY_FAVOURITE, json.optString("my_favourite").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NEW_PASSWORD, json.optString("new_password").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NEW_HERE_TITLE, json.optString("new_here_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO, json.optString("no").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO_DATA, json.optString("no_data").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO_INTERNET_CONNECTION, json.optString("no_internet_connection").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO_INTERNET_NO_DATA, json.optString("no_internet_no_data").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO_DETAILS_AVAILABLE, json.optString("no_details_available").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.BUTTON_OK, json.optString("btn_ok").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.OLD_PASSWORD, json.optString("old_password").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.OOPS_INVALID_EMAIL, json.optString("oops_invalid_email").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ORDER, json.optString("order").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_DETAILS_ORDER_ID, json.optString("transaction_detail_order_id").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PASSWORD_RESET_LINK, json.optString("password_reset_link").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PASSWORDS_DO_NOT_MATCH, json.optString("password_donot_match").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PAY_BY_PAYPAL, json.optString("pay_by_paypal").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.BTN_PAYNOW, json.optString("btn_paynow").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PAY_WITH_CREDIT_CARD, json.optString("pay_with_credit_card").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PAYMENT_OPTIONS_TITLE, json.optString("payment_options_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PLAN_NAME, json.optString("plan_name").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, json.optString("activate_subscription_watch_video").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.COUPON_ALERT, json.optString("coupon_alert").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.VALID_CONFIRM_PASSWORD, json.optString("valid_confirm_password").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PROFILE, json.optString("profile").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PROFILE_UPDATED, json.optString("profile_updated").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PURCHASE, json.optString("purchase").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_DETAIL_PURCHASE_DATE, json.optString("transaction_detail_purchase_date").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PURCHASE_HISTORY, json.optString("purchase_history").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.BTN_REGISTER, json.optString("btn_register").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SORT_RELEASE_DATE, json.optString("sort_release_date").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SAVE_THIS_CARD, json.optString("save_this_card").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TEXT_SEARCH_PLACEHOLDER, json.optString("text_search_placeholder").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SEASON, json.optString("season").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SELECT_OPTION_TITLE, json.optString("select_option_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SELECT_PLAN, json.optString("select_plan").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SIGN_UP_TITLE, json.optString("signup_title").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SLOW_INTERNET_CONNECTION, json.optString("slow_internet_connection").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SLOW_ISSUE_INTERNET_CONNECTION, json.optString("slow_issue_internet_connection").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SORRY, json.optString("sorry").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.GEO_BLOCKED_ALERT, json.optString("geo_blocked_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SIGN_OUT_ERROR, json.optString("sign_out_error").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.ALREADY_PURCHASE_THIS_CONTENT, json.optString("already_purchase_this_content").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CROSSED_MAXIMUM_LIMIT, json.optString("crossed_max_limit_of_watching").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SORT_BY, json.optString("sort_by").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.STORY_TITLE, json.optString("story_title").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.BTN_SUBMIT, json.optString("btn_submit").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_STATUS, json.optString("transaction_success").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.VIDEO_ISSUE, json.optString("video_issue").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO_CONTENT, json.optString("no_content").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.NO_VIDEO_AVAILABLE, json.optString("no_video_available").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, json.optString("content_not_available_in_your_country").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_DATE, json.optString("transaction_date").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANASCTION_DETAIL, json.optString("transaction_detail").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION_STATUS, json.optString("transaction_status").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRANSACTION, json.optString("transaction").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TRY_AGAIN, json.optString("try_again").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.UNPAID, json.optString("unpaid").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.USE_NEW_CARD, json.optString("use_new_card").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.VIEW_MORE, json.optString("view_more").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.VIEW_TRAILER, json.optString("view_trailer").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.WATCH, json.optString("watch").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.WATCH_NOW, json.optString("watch_now").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SIGN_OUT_ALERT, json.optString("sign_out_alert").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.UPDATE_PROFILE_ALERT, json.optString("update_profile_alert").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.YES, json.optString("yes").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.PURCHASE_SUCCESS_ALERT, json.optString("purchase_success_alert").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.CARD_WILL_CHARGE, json.optString("card_will_charge").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SEARCH_HINT, json.optString("search_hint").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.TERMS, json.optString("terms").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.APP_ON, json.optString("app_on").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.FILL_FORM_BELOW, json.optString("Fill_form_below").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.MESSAGE, json.optString("text_message").trim());
//
//                        languagePreference.getTextofLanguage(PURCHASE, Util.DEFAULT_PURCHASE);
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SELECTED_LANGUAGE_CODE, default_Language);
//
//
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
//                        Util.setLanguageSharedPrefernce(ShowWithEpisodesActivity.this, Util.MESSAGE, json.optString("text_message").trim());
//                        //Call For Language PopUp Dialog
//
//                        languageCustomAdapter.notifyDataSetChanged();
//
//                        Intent intent = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        startActivity(intent);
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    // Call For Other Methods.
//
//
//                } else {
//                }
//            }
//
//
//        }
//
//        protected void onPreExecute() {
//            progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
//            progressBarHandler.show();
//        }
//    }

    private void customTextView(String str) {

        String textStr = " " + str;
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                textStr);

        String tempStr = " " + languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE);
        spanTxt.append(tempStr);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                videoStoryTextView.setMaxLines(Integer.MAX_VALUE);


            }
        }, spanTxt.length() - tempStr.length(), spanTxt.length(), 0);
        // spanTxt.append(" and");
        // setColor(videoStoryTextView, str, tempStr, Color.RED);
        //  spanTxt.setSpan(new ForegroundColorSpan(Color.BLUE), 15, spanTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanTxt.setSpan(new ForegroundColorSpan(ResourcesCompat.getColor(getResources(), R.color.button_background, null)), spanTxt.length() - tempStr.length(), spanTxt.length(), 0);
      /*  spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getApplicationContext(), "Privacy Policy Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);*/
        videoStoryTextView.setMovementMethod(LinkMovementMethod.getInstance());
        videoStoryTextView.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    private void setColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i + 1, i + 1 + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
/*
    *//*****************chromecvast*-------------------------------------*//*

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

               // mVideoView.pause();
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

                Intent intent = new Intent(ShowWithEpisodesActivity.this, ExpandedControlsActivity.class);
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
                Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               /* playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl)*/;
                playVideoIntent.putExtra("PlayerModel",playerModel);
                startActivity(playVideoIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want

                        if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {

                            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                            contentDetailsInput.setAuthToken(authTokenStr);
                            contentDetailsInput.setPermalink(permalinkStr);

                            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput,ShowWithEpisodesActivity.this,ShowWithEpisodesActivity.this);
                            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);


                        } else {
                            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
    /*subtitles***********/
}
