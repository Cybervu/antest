package com.home.vod.activity;

/**
 * Created by MUVI on 10/10/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetEpisodeDeatailsAsynTask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetRelatedContentListAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.RelatedContentListInput;
import com.home.apisdk.apiModel.RelatedContentListOutput;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.vod.BuildConfig;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.LoginRegistrationOnContentClickHandler;
import com.home.vod.MonetizationHandler;
import com.home.vod.MyDownloadIntentHandler;
import com.home.vod.R;
import com.home.vod.SearchIntentHandler;
import com.home.vod.adapter.ProgramDetailsAdapter;
import com.home.vod.adapter.RelatedContentListDataAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.model.RelatedContentListItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import player.activity.AdPlayerActivity;
import player.activity.Player;
import player.activity.ResumePopupActivity;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.vod.preferences.LanguagePreference.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.ALREADY_PURCHASE_THIS_CONTENT;
import static com.home.vod.preferences.LanguagePreference.APP_ON;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.CROSSED_MAXIMUM_LIMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ALREADY_PURCHASE_THIS_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_ON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CROSSED_MAXIMUM_LIMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FOLLOWED_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEASON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TUTORIAL_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_WORKOUT_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.FOLLOWED_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SEASON;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.TUTORIAL_TITLE;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.WORKOUT_BUTTON;
import static com.home.vod.util.Constant.PERMALINK_INTENT_ARRAY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.SEASON_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.is_followed;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.HAS_FAVORITE;

public class ProgramDetailsActivity extends AppCompatActivity implements GetContentDetailsAsynTask.GetContentDetailsListener, GetEpisodeDeatailsAsynTask.GetEpisodeDetailsListener, GetIpAddressAsynTask.IpAddressListener, GetValidateUserAsynTask.GetValidateUserListener,
        VideoDetailsAsynctask.VideoDetailsListener, GetRelatedContentListAsynTask.GetRelatedContentListListener, AddToFavAsync.AddToFavListener, DeleteFavAsync.DeleteFavListener {

    ImageView bannerImageView, playButton, share, favorite_view_episode;
    TextView detailsTextView, tutorialTextView;
    String durationText = "";
    Button startWorkoutButton, dietPlanButton, stretchWorkoutButton;
    RelativeLayout letsWorkoutButton;
    LinearLayout tutorialTitle, equipmentRelativeLayout, equipmentTitle, buttonLinearlayout;
    TextView equipmentTitleTextView, equipmentStoryTextView;
    ProgressBarHandler progressBarHandler;
    GetValidateUserAsynTask asynValidateUserDetails;
    VideoDetailsAsynctask asynLoadVideoUrls;
    ArrayList<EpisodesListModel> itemData;
    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    PreferenceManager preferenceManager;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout, logo_image;
    RecyclerView relatedContentList;
    Player playerModel;
    int isFavorite;
    String movieUniqueId = "", isEpisode = "";
    RelativeLayout titleRelativeLayout;
    DataModel dbModel = new DataModel();
    int isLogin = 0;
    public static final int VIDEO_PLAY_BUTTON_CLICK_LOGIN_REG_REQUESTCODE = 8888;
    RecyclerView.LayoutManager mLayoutManager;
    Toolbar mActionBarToolbar;
    String episodeVideoUrlStr;
    String email, id;
    String filename = "";
    int ItemClickedPosition = 0;
    int position;
    static File mediaStorageDir;
    ArrayList<RelatedContentListItem> relatedContentItemData = new ArrayList<RelatedContentListItem>();
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ArrayList<String> SubTitleLanguage = new ArrayList<>();
    EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    String loggedInStr;
    int keepAliveTime = 10;
    int selectedPurchaseType = 0;
    AlertDialog alert;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    String permalinkStr;
    String useridStr;
    EpisodesListModel itemToPlay;
    String ipAddres;
    String bannerImageId, posterImageId, duration, contentStreamIdStr, contentIdStr;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    LanguagePreference languagePreference;
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    /////by nihar
    String[] season;
    String sucessMsg;
    /*chromecast-------------------------------------*/
    private VideoView mVideoView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mStartText;
    private TextView mEndText;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private ProgressBar mLoading;
    private View mControllers;
    private View mContainer;
    private ImageView mCoverArt;
    private Timer mSeekbarTimer;
    private Timer mControllersTimer;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final Handler mHandler = new Handler();
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;
    private boolean mControllersVisible;
    private int mDuration;
    private TextView mAuthorView;
    // ProgressBarHandler pDialog;
    String priceForUnsubscribedStr, priceFosubscribedStr;
    PPVModel ppvmodel;
    APVModel advmodel;
    CurrencyModel currencymodel;
    String PlanId = "";
    ProgressBarHandler pDialog;
    private ImageButton mPlayCircle;

    //Added for resume cast
    int Played_Length = 0;
    String watch_status_String = "start";
    String seek_status = "";

    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {
        ipAddres = ipAddressStr;
        return;
    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {
        LogUtil.showLog("PINTU", "validateuser pdlog show");
        if (pDialog == null) {
            pDialog = new ProgressBarHandler(ProgramDetailsActivity.this);
        }
        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {

        String subscription_Str = validateUserOutput.getIsMemberSubscribed();
        preferenceManager.setIsSubscribedToPref(subscription_Str);
        String validUserStr = validateUserOutput.getValiduser_str();

        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "validate user pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            status = 0;
        }

        if (validateUserOutput == null) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ProgramDetailsActivity.this, MainActivity.class);
                            startActivity(in);

                        }
                    });
            dlgAlert.create().show();
        } else if (status <= 0) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ProgramDetailsActivity.this, MainActivity.class);
                            startActivity(in);

                        }
                    });
            dlgAlert.create().show();
        }

        if (status > 0) {

            if (status == 425) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                   /* if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                    }*/
                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

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
            } else if (status == 426) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                  /*  if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                    }*/
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
            } else if (status == 427) {


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this);
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
            } else if (status == 428) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(CROSSED_MAXIMUM_LIMIT, DEFAULT_CROSSED_MAXIMUM_LIMIT) + " " + languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                 /*   if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.CROSSED_MAXIMUM_LIMIT,Util.DEFAULT_CROSSED_MAXIMUM_LIMIT));

                    }*/
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
            } else if (Util.dataModel.getIsAPV() == 1 && status == 431) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }

                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                if (message != null && !message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(ALREADY_PURCHASE_THIS_CONTENT, DEFAULT_ALREADY_PURCHASE_THIS_CONTENT));

                }
                //dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.ALREADY_PURCHASE_THIS_CONTENT,Util.DEFAULT_ALREADY_PURCHASE_THIS_CONTENT)+ " " +getResources().getString(R.string.studio_site));

                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        });
                dlgAlert.create().show();

            } else if (status == 429 || status == 430) {

                if (validUserStr != null) {


                    if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
                        if (NetworkStatus.getInstance().isConnected(this)) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
//                                ShowPpvPopUp();

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

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
                            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
                               /* Intent intent = new Intent(ProgramDetailsActivity.this, SubscriptionActivity.class);
                                intent.putExtra("PlayerModel", playerModel);
                                intent.putExtra("PERMALINK", permalinkStr);
                                intent.putExtra("SEASON", season.length);
                                intent.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
                                intent.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                                intent.putExtra("Index", getIntent().getStringExtra("Index"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);*/


                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

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
                            } else {
//                                ShowPpvPopUp();


                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

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
                            }
                        }

                    }
                }


            } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
//                ShowPpvPopUp();

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

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

            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
                /*Intent intent = new Intent(ProgramDetailsActivity.this, SubscriptionActivity.class);
                intent.putExtra("PlayerModel", playerModel);
                intent.putExtra("PERMALINK", permalinkStr);
                intent.putExtra("SEASON", season.length);
                intent.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
                intent.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                intent.putExtra("Index", getIntent().getStringExtra("Index"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);*/


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage(APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

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

            } else if (Util.dataModel.getIsConverted() == 0) {
                Util.showNoDataAlert(ProgramDetailsActivity.this);
                /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this);
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
                if (NetworkStatus.getInstance().isConnected(this)) {

                    Log.v("SUBHA", "Video PLayer Called 1");

                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                    getVideoDetailsInput.setAuthToken(authTokenStr);
                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {

        LogUtil.showLog("PINTU", "videodetails pdlog show");

        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.show();
        }
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int code, String status, String message) {


        // _video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // _video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/

        boolean play_video = true;


        Log.v("SUBHA", " video called startworkout button");

        if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {

            if (_video_details_output.getStreaming_restriction().trim().equals("0")) {

                play_video = false;
            } else {
                play_video = true;
            }
        } else {
            play_video = true;
        }

        Log.v("SUBHA", "play video ==== " + play_video);
        if (!play_video) {

            try {
                if (pDialog.isShowing())
                    pDialog.hide();
            } catch (IllegalArgumentException ex) {
            }

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(message);
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

            return;
        }


        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "videodetails pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
        }

        Log.v("SUBHA", "code == player == " + code);
        if (code == 200) {
            playerModel.setIsOffline(_video_details_output.getIs_offline());
            playerModel.setDownloadStatus(_video_details_output.getDownload_status());
            if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {

                /**@bishal
                 * for drm player below condition added
                 * if studio_approved_url is there in api then set the videourl from this other wise goto 2nd one
                 */

                if (_video_details_output.getStudio_approved_url() != null &&
                        !_video_details_output.getStudio_approved_url().isEmpty() &&
                        !_video_details_output.getStudio_approved_url().equals("null") &&
                        !_video_details_output.getStudio_approved_url().matches("")) {
                    LogUtil.showLog("BISHAL", "if called means  studioapproved");
                    playerModel.setVideoUrl(_video_details_output.getStudio_approved_url());
                    LogUtil.showLog("BKS", "studipapprovedurl====" + playerModel.getVideoUrl());


                    if (_video_details_output.getLicenseUrl().trim() != null && !_video_details_output.getLicenseUrl().trim().isEmpty() && !_video_details_output.getLicenseUrl().trim().equals("null") && !_video_details_output.getLicenseUrl().trim().matches("")) {
                        playerModel.setLicenseUrl(_video_details_output.getLicenseUrl());
                    }
                    if (_video_details_output.getVideoUrl().trim() != null && !_video_details_output.getVideoUrl().isEmpty() && !_video_details_output.getVideoUrl().equals("null") && !_video_details_output.getVideoUrl().trim().matches("")) {
                        playerModel.setMpdVideoUrl(_video_details_output.getVideoUrl());

                    } else {
                        playerModel.setMpdVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                    }
                } else {
                    if (_video_details_output.getVideoUrl() != null || !_video_details_output.getVideoUrl().matches("")) {
                        playerModel.setVideoUrl(_video_details_output.getVideoUrl());
                        LogUtil.showLog("BISHAL", "videourl===" + playerModel.getVideoUrl());
                        playerModel.setThirdPartyPlayer(false);
                    } else {
                        //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                        playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                    }
                }
            } else {
                if (_video_details_output.getThirdparty_url() != null || !_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                }
            }

            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if (_video_details_output.getPlayed_length() != null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(_video_details_output.getPlayed_length())));


            //dependency for datamodel

            SubTitleName = _video_details_output.getSubTitleName();
            SubTitleLanguage = _video_details_output.getSubTitleLanguage();

            Util.dataModel.setVideoUrl(_video_details_output.getVideoUrl());
            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());
            Util.dataModel.setThirdPartyUrl(_video_details_output.getThirdparty_url());
            Util.dataModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            Util.dataModel.setChannel_id(_video_details_output.getChannel_id());
            Util.dataModel.setPreRoll(_video_details_output.getPreRoll());
            Util.dataModel.setPostRoll(_video_details_output.getPostRoll());
            Util.dataModel.setMidRoll(_video_details_output.getMidRoll());
            Util.dataModel.setAdDetails(_video_details_output.getAdDetails());
            Util.dataModel.setPlayPos(Util.isDouble(_video_details_output.getPlayed_length()));


            //player model set
            playerModel.setAdDetails(_video_details_output.getAdDetails());
            playerModel.setMidRoll(_video_details_output.getMidRoll());
            playerModel.setPostRoll(_video_details_output.getPostRoll());
            playerModel.setChannel_id(_video_details_output.getChannel_id());
            playerModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            playerModel.setPreRoll(_video_details_output.getPreRoll());
            playerModel.setSubTitleName(_video_details_output.getSubTitleName());
            playerModel.setSubTitlePath(_video_details_output.getSubTitlePath());
            playerModel.setResolutionFormat(_video_details_output.getResolutionFormat());
            playerModel.setResolutionUrl(_video_details_output.getResolutionUrl());
            playerModel.setFakeSubTitlePath(_video_details_output.getFakeSubTitlePath());
            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            FakeSubTitlePath = _video_details_output.getFakeSubTitlePath();
            playerModel.setSubTitleLanguage(_video_details_output.getSubTitleLanguage());
            playerModel.setOfflineUrl(_video_details_output.getOfflineUrl());
            playerModel.setOfflineLanguage(_video_details_output.getOfflineLanguage());
            playerModel.setPlayPos(Util.isDouble(_video_details_output.getPlayed_length()));


            if (playerModel.getVideoUrl() == null ||
                    playerModel.getVideoUrl().matches("")) {

                Util.showNoDataAlert(ProgramDetailsActivity.this);
               /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
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


                // condition for checking if the response has third party url or not.
                if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {

                    Log.v("pratik", "tymg==" + playerModel.getPlayPos());
                    Log.v("pratik", "tym=h=" + Util.dataModel.getPlayPos());

                    if (mCastSession != null &&
                            mCastSession.isConnected()) {

                        ///Added for resume cast watch
                        Log.v("pratik", "tym==" + playerModel.getPlayPos() * 1000);
                        if ((Util.dataModel.getPlayPos() * 1000) > 0) {
                            Util.dataModel.setPlayPos(playerModel.getPlayPos());
                            Intent resumeIntent = new Intent(ProgramDetailsActivity.this, ResumePopupActivity.class);
                            startActivityForResult(resumeIntent, 1001);
                            Log.v("pratik", "tym==>0");

                        } else {
                            Played_Length = 0;
                            watch_status_String = "start";

                            Log.v("pratik", "timee elsee");
                            PlayThroughChromeCast();
                        }

                    } else {


                        playerModel.setThirdPartyPlayer(false);

                        final Intent playVideoIntent;
                        if (Util.dataModel.getAdNetworkId() == 3) {
                            LogUtil.showLog("responseStr", "playVideoIntent" + Util.dataModel.getAdNetworkId());

                            playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                            playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                        } else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1) {
                            if (Util.dataModel.getPlayPos() <= 0) {
                                playVideoIntent = new Intent(ProgramDetailsActivity.this, AdPlayerActivity.class);
                            } else {
                                playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                                playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                            }


                        } else {
                            playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                            playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);
                        }
                        /***ad **/
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

                                    progressBarHandler = new ProgressBarHandler(ProgramDetailsActivity.this);
                                    progressBarHandler.show();
                                    Download_SubTitle(FakeSubTitlePath.get(0).trim());
                                } else {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                    playVideoIntent.putExtra("PlayerModel", playerModel);
                                    playVideoIntent.putExtra("PLAY_LIST", itemData);
                                    playVideoIntent.putExtra("SEASON", season.length);
                                    playVideoIntent.putExtra("PERMALINK", permalinkStr);
                                    playVideoIntent.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
                                    playVideoIntent.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                                    playVideoIntent.putExtra("Index", getIntent().getStringExtra("Index"));
                                    Log.v("SUBHA", "current season ==== " + getIntent().getStringExtra(SEASON_INTENT_KEY));
                                    playVideoIntent.putExtra("TAG", ItemClickedPosition);
                                    startActivity(playVideoIntent);
                                }

                            }
                        });
                    }
                } else {
                    final Intent playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                    final Intent   playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    playVideoIntent.putExtra("PlayerModel", playerModel);
                    playVideoIntent.putExtra("PLAY_LIST", itemData);
                    playVideoIntent.putExtra("SEASON", season.length);
                    playVideoIntent.putExtra("PERMALINK", permalinkStr);
                    playVideoIntent.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));

                    Log.v("SUBHA", "current season ==== " + getIntent().getStringExtra(SEASON_INTENT_KEY));
                    playVideoIntent.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                    playVideoIntent.putExtra("TAG", ItemClickedPosition);
                    playVideoIntent.putExtra("Index", getIntent().getStringExtra("Index"));

                    startActivity(playVideoIntent);
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            Util.showNoDataAlert(ProgramDetailsActivity.this);
        }

    }

    @Override
    public void onGetRelatedContentListPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgramDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetRelatedContentListPostExecuteCompleted(ArrayList<RelatedContentListOutput> relatedContentListOutputArray, int status, int totalItems, String message) {

        String videoGenreStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoName = "";
        String videoImageStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoPermalinkStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoTypeStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoTypeIdStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoUrlStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        String movieUniqueIdStr = "";
        String movieStreamUniqueIdStr = "";
        int isConverted = 0;
        int isAPV = 0;
        int isPPV = 0;
        String movieThirdPartyUrl = "";

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        Log.v("SUBHA", "data called here 273ry    " + status);

        if (status > 0) {
            if (status == 200) {
                tutorialTitle.setVisibility(View.VISIBLE);
                tutorialTextView.setVisibility(View.VISIBLE);

                relatedContentList.setVisibility(View.VISIBLE);
                relatedContentItemData.clear();
                Log.v("SUBHASS", "program type value == relatedcontent details ");

                if (relatedContentListOutputArray.size() > 0) {

                    relatedContentList.setVisibility(View.VISIBLE);
                    tutorialTitle.setVisibility(View.VISIBLE);
                    tutorialTextView.setVisibility(View.VISIBLE);


                    for (int i = 0; i < relatedContentListOutputArray.size(); i++) {


                        videoImageStr = relatedContentListOutputArray.get(i).getPosterUrl();

                        videoName = relatedContentListOutputArray.get(i).getName();


                        videoTypeIdStr = relatedContentListOutputArray.get(i).getContentTypesId();
                        //videoGenreStr = relatedContentListOutputArray.get(i).getGenre();
                        videoPermalinkStr = relatedContentListOutputArray.get(i).getPermalink();
                        //isEpisodeStr = relatedContentListOutputArray.get(i).getIsEpisodeStr();
                        //isConverted = relatedContentListOutputArray.get(i).getIsConverted();
                        // isPPV = relatedContentListOutputArray.get(i).getIsPPV();
                        // isAPV = relatedContentListOutputArray.get(i).getIsAPV();
                        relatedContentItemData.add(new RelatedContentListItem(videoImageStr, videoName, "", videoTypeIdStr, videoGenreStr, "", videoPermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV, "", relatedContentListOutputArray.get(i).getContentId(), relatedContentListOutputArray.get(i).getContentStreamId()));
                    }


                    if (relatedContentItemData.size() <= 0) {


                        //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
                    } else {

                        tutorialTitle.setVisibility(View.VISIBLE);
                        tutorialTextView.setVisibility(View.VISIBLE);

                        LogUtil.showLog("BISHAL", "data show...");
                        relatedContentList.setVisibility(View.VISIBLE);
                        relatedContentList.setLayoutManager(mLayoutManager);
                        relatedContentList.setItemAnimator(new DefaultItemAnimator());
                        RelatedContentListDataAdapter mAdapter = new RelatedContentListDataAdapter(ProgramDetailsActivity.this, R.layout.related_content_listing, relatedContentItemData);


                        relatedContentList.setAdapter(mAdapter);

                    }

                } else {

                    relatedContentList.setVisibility(View.GONE);
                    tutorialTitle.setVisibility(View.GONE);


                }
            } else {

                relatedContentList.setVisibility(View.GONE);
                tutorialTitle.setVisibility(View.GONE);


            }
        } else {


            relatedContentList.setVisibility(View.GONE);
            tutorialTitle.setVisibility(View.GONE);


        }


    }

    @Override
    public void onAddToFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgramDetailsActivity.this);
        pDialog.show();

    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        if (status == 200) {


            //pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            ProgramDetailsActivity.this.sucessMsg = sucessMsg;
            String loggedInStr = preferenceManager.getLoginStatusFromPref();

            favorite_view_episode.setImageResource(R.drawable.favorite_red);
//            favorite_view_episode.setText(languagePreference.getTextofLanguage(FOLLOWED_PROGRAM_BUTTON,DEFAULT_FOLLOWED_PROGRAM_BUTTON));
            isFavorite = 1;

//            showToast();
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "addd fav pdlog hide");
                pDialog.hide();
            }
        }
    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgramDetailsActivity.this);
        pDialog.show();

    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        ProgramDetailsActivity.this.sucessMsg = sucessMsg;
        favorite_view_episode.setImageResource(R.drawable.favorite);
//        favorite_view_episode.setText(languagePreference.getTextofLanguage(FOLLOW_PROGRAM_BUTTON, DEFAULT_FOLLOW_PROGRAM_BUTTON));


//        showToast();
        isFavorite = 0;
        if (pDialog != null && pDialog.isShowing()) {
            LogUtil.showLog("PINTU", "delete fav pdlog hide");
            pDialog.hide();
        }
    }

    /*chromecast-------------------------------------*/
     /*chromecast-------------------------------------*/

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

    /*chromecast-------------------------------------*/

    /*chromecast-------------------------------------*/

    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;


    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
     /*chromecast-------------------------------------*/

    /*chromecast-------------------------------------*/
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_program_details);


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        logo_image = (RelativeLayout) findViewById(R.id.logo_image);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        buttonLinearlayout = (LinearLayout) findViewById(R.id.buttonLinearlayout);
        logo_image.bringToFront();
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        detailsTextView = (TextView) findViewById(R.id.titleTextView);
        startWorkoutButton = (Button) findViewById(R.id.startWorkoutButton);
        stretchWorkoutButton = (Button) findViewById(R.id.stretchWorkoutButton);
        letsWorkoutButton = (RelativeLayout) findViewById(R.id.letsWorkoutButton);
        titleRelativeLayout = (RelativeLayout) findViewById(R.id.titleRelativeLayout);
//        dietPlanButton = (Button) findViewById(R.id.dietPlanButton);
//        durationTitleTextView = (TextView) findViewById(R.id.durationTitleTextView);
//        durationTextView = (TextView) findViewById(R.id.durationTextView);
        tutorialTextView = (TextView) findViewById(R.id.tutorialTextView);
//        viewAllTextView = (TextView) findViewById(R.id.viewAllTextView2);
        equipmentTitleTextView = (TextView) findViewById(R.id.equipmentTitleTextView);
        equipmentStoryTextView = (TextView) findViewById(R.id.equipmentStoryTextView);
        relatedContentList = (RecyclerView) findViewById(R.id.featureContent);
        tutorialTitle = (LinearLayout) findViewById(R.id.tutorialTitle);
        equipmentRelativeLayout = (LinearLayout) findViewById(R.id.equipmentRelativeLayout);
        equipmentTitle = (LinearLayout) findViewById(R.id.equipmentTitle);
        share = (ImageView) findViewById(R.id.share);
        favorite_view_episode = (ImageView) findViewById(R.id.favoriteImageView1);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(ProgramDetailsActivity.this);
        episodeListOptionMenuHandler = new EpisodeListOptionMenuHandler(this);
        languagePreference = LanguagePreference.getLanguagePreference(ProgramDetailsActivity.this);
        playerModel = new Player();
        pDialog = new ProgressBarHandler(ProgramDetailsActivity.this);
        PlanId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();
        playerModel.setIsstreaming_restricted(Util.getStreamingRestriction(languagePreference));
        itemData = new ArrayList<EpisodesListModel>();
        progressBarHandler = new ProgressBarHandler(ProgramDetailsActivity.this);

        FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), detailsTextView);
//        FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), viewAllTextView);
        FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), startWorkoutButton);
        FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), stretchWorkoutButton);
        detailsTextView.setText(languagePreference.getTextofLanguage(SEASON, DEFAULT_SEASON) + " " + getIntent().getStringExtra(SEASON_INTENT_KEY));
//        viewAllTextView.setText(languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE));
//        viewAllTextView.setVisibility(View.GONE);
//        dietPlanButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        isLogin = preferenceManager.getLoginFeatureFromPref();

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mLayoutManager = new LinearLayoutManager(ProgramDetailsActivity.this, LinearLayoutManager.VERTICAL, false);

        if (((ProgramDetailsActivity.this.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((ProgramDetailsActivity.this.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {


            relatedContentList.addItemDecoration(new SpacesItemDecoration(30));

        } else {

            relatedContentList.addItemDecoration(new SpacesItemDecoration(50));


        }


        /***favorite *****/
        favorite_view_episode.setVisibility(View.GONE);

        favorite_view_episode.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                is_followed = true;
                if (preferenceManager != null) {
                    loggedInStr = preferenceManager.getUseridFromPref();
                }

                if (loggedInStr != null) {
                    if (isFavorite == 1) {

                        DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
                        deleteFavInputModel.setAuthTokenStr(authTokenStr);
                        deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                        deleteFavInputModel.setMovieUniqueId(movieUniqueId);
                        deleteFavInputModel.setIsEpisode(isEpisode);

                        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                        deleteFavAsync.executeOnExecutor(threadPoolExecutor);

                       /* AsynFavoriteDelete asynFavoriteDelete=new AsynFavoriteDelete();
                        asynFavoriteDelete.execute();*/
                    } else {

                        LogUtil.showLog("MUVI", "favorite");
                        AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                        addToFavInputModel.setAuthToken(authTokenStr);
                        addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                        addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                        addToFavInputModel.setIsEpisodeStr(isEpisode);

                        AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                        asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);


                    }
                } else {
                    Util.favorite_clicked = true;

                    Intent registerActivity = new Intent(ProgramDetailsActivity.this, RegisterActivity.class);
                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    registerActivity.putExtra("from", this.getClass().getName());
                    startActivityForResult(registerActivity, 30060);

                    bannerImageRelativeLayout.setVisibility(View.VISIBLE);

                }

            }
        });


        /***favorite *****/

        /*chromecast-------------------------------------*/

        mAquery = new AQuery(this);

        // setupControlsCallbacks();
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();


        boolean shouldStartPlayback = false;
        int startPosition = 0;

         /*   MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

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
            mSelectedMedia = mediaInfo;*/

        // see what we need to play and where
           /* Bundle bundle = getIntent().getExtras();
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
            }*/

       /* viewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent episode = new Intent(ProgramDetailsActivity.this, Tutorial_List_Activity.class);
                episode.putExtra("movieUniqueId", movieUniqueId);
                episode.putExtra("contentTypesId", contentTypesId);
                episode.putExtra("PLAY_LIST", itemData);
                episode.putExtra("TAG", ItemClickedPosition);
                episode.putExtra("SEASON", season.length);
                episode.putExtra(PERMALINK_INTENT_KEY, permalinkStr);
                episode.putExtra(SEASON_INTENT_KEY, getIntent().getStringExtra(SEASON_INTENT_KEY));
                episode.putExtra("Index", getIntent().getStringExtra("Index"));


                episode.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                startActivity(episode);
            }
        });*/


        relatedContentList.addOnItemTouchListener(new RecyclerTouchListener(this,
                relatedContentList, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(ProgramDetailsActivity.this, "No details Available", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onLongClick(View view, int position) {

                return;
            }
        }));
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(itemData.get(0), 0);

            }
        });

        if (itemData.size() == 1) {
            letsWorkoutButton.setVisibility(View.INVISIBLE);
        }

        letsWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                clickItem(itemData.get(1), 1);
                Log.v("SUBHAS", "item position === ");
            }
        });
        stretchWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int data = itemData.size() - 1;

                EpisodesListModel item = itemData.get(data);
                clickItem(item, data);

                Log.v("SUBHASm", "item position === " + data);

            }
        });
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
/*chromecast-------------------------------------*/

        Episode_Details_input episode_details_input = new Episode_Details_input();
        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
        episode_details_input.setAuthtoken(authTokenStr);
        episode_details_input.setPermalink(permalinkStr);
        episode_details_input.setSeries_number(getIntent().getStringExtra(SEASON_INTENT_KEY));
        Log.v("SUBHA", "season number === " + getIntent().getStringExtra(SEASON_INTENT_KEY));
        episode_details_input.setLimit("10");
        episode_details_input.setOffset("1");
        // episode_details_input.setSeries_number("1");
        episode_details_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        GetEpisodeDeatailsAsynTask getEpisodeDeatailsAsynTask = new GetEpisodeDeatailsAsynTask(episode_details_input, this, this);
        getEpisodeDeatailsAsynTask.executeOnExecutor(threadPoolExecutor);

        share.setVisibility(View.VISIBLE);

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.shareIt(ProgramDetailsActivity.this);
            }
        });


       /* dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgramDetailsActivity.this, DietPlanActivity.class);
                startActivity(intent);
            }
        });*/
        ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
        useridStr = preferenceManager.getUseridFromPref();

        contentDetailsInput.setAuthToken(authTokenStr);
        if (preferenceManager != null) {
            String countryPref = preferenceManager.getCountryCodeFromPref();
            contentDetailsInput.setCountry(countryPref);
        } else {
            contentDetailsInput.setCountry("IN");
        }
        contentDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        contentDetailsInput.setPermalink(permalinkStr);
        contentDetailsInput.setUser_id(useridStr);
        asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, this, this);
        asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int halfSpace;

        public SpacesItemDecoration(int space) {
            this.halfSpace = space / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getPaddingLeft() != halfSpace) {
                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
                parent.setClipToPadding(false);
            }

            outRect.top = halfSpace;
            outRect.bottom = halfSpace;
            outRect.left = halfSpace;
            outRect.right = halfSpace;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        episodeListOptionMenuHandler.createOptionMenu(menu, preferenceManager, languagePreference);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                final Intent searchIntent = new SearchIntentHandler(ProgramDetailsActivity.this).handleSearchIntent();
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(ProgramDetailsActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(ProgramDetailsActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, DigiOsmosisFavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.action_mydownload:

                final Intent mydownload = new MyDownloadIntentHandler(ProgramDetailsActivity.this).handleDownloadIntent();
                startActivity(mydownload);
                // Not implemented here
                return false;

            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(ProgramDetailsActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(ProgramDetailsActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }

        GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

        /***************chromecast**********************/

        /***************chromecast**********************/
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }


        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        /***************chromecast**********************/
        invalidateOptionsMenu();


    }

    @Override
    public void onGetContentDetailsPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(ProgramDetailsActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {
        try {
            if (progressBarHandler != null && progressBarHandler.isShowing()) {
                LogUtil.showLog("PINTU", "contentdetails pdlog hide");
                progressBarHandler.hide();

            }
        } catch (IllegalArgumentException ex) {

        }
        if (status == 200) {
            ///by nihar
            season = contentDetailsOutput.getSeason();

            contentTypesId = contentDetailsOutput.getContentTypesId();
            movieUniqueId = contentDetailsOutput.getMuviUniqId();
            bannerImageId = contentDetailsOutput.getBanner();
            posterImageId = contentDetailsOutput.getPoster();
            duration = contentDetailsOutput.getDuration();
            contentIdStr = contentDetailsOutput.getId();
            contentStreamIdStr = contentDetailsOutput.getMovieStreamId();
            isFavorite = contentDetailsOutput.getIs_favorite();
            isEpisode = contentDetailsOutput.getIsEpisode();

            // viewAllTextView.setText(languagePreference.getTextofLanguage(DETAIL_VIEW_MORE,DEFAULT_DETAIL_VIEW_MORE));
//            viewAllTextView.setVisibility(View.VISIBLE);
            tutorialTextView.setText("EXERCISES");
//            durationTitleTextView.setText(languagePreference.getTextofLanguage(DURATION_TITLE, DEFAULT_DURATION_TITLE));
            startWorkoutButton.setText("WARM UP");
            stretchWorkoutButton.setText("STRETCH");
            FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), tutorialTextView);
//            FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), durationTitleTextView);

          /*  if (duration.matches("")) {
                durationTitleTextView.setVisibility(View.GONE);
            } else {

                FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), durationTitleTextView);
                durationTextView.setText(duration);
            }
*/
            if (TextUtils.isEmpty(bannerImageId)) {

                if (TextUtils.isEmpty(posterImageId)) {

                    bannerImageView.setImageResource(R.drawable.logo);
                } else {


                    /*ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(ProgramDetailsActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.logo)
                            .showImageOnLoading(R.drawable.logo).build();
                    imageLoader.displayImage(posterImageId, moviePoster, options);*/

                    Picasso.with(ProgramDetailsActivity.this)
                            .load(posterImageId)
                            .error(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .into(bannerImageView);

                }

            } else {


                /*ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(ProgramDetailsActivity.this));

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.logo)
                        .showImageOnFail(R.drawable.logo)
                        .showImageOnLoading(R.drawable.logo).build();
                imageLoader.displayImage(bannerImageId.trim(), moviePoster, options);*/

                Picasso.with(ProgramDetailsActivity.this)
                        .load(bannerImageId)
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .into(bannerImageView);


            }

            if (contentDetailsOutput.getMetadata() != null || contentDetailsOutput.getMetadata().size() > 0) {
               /* benefitsTitleTextView.setVisibility(View.GONE);
                colortitle1.setVisibility(View.GONE);*/
                equipmentRelativeLayout.removeAllViews();
                for (Map.Entry<String, String> entry : contentDetailsOutput.getMetadata().entrySet()) {

                    Log.v("SUBHASS", "program type value == content details ");

                    if (entry.getValue() != null && !entry.getValue().matches("")) {


                        if (!entry.getKey().equalsIgnoreCase("programtype")) {

                            DynamicLayout(equipmentRelativeLayout, entry.getKey().toUpperCase(), entry.getValue());
                        }

                    }
                    equipmentRelativeLayout.setVisibility(View.VISIBLE);

                }
              /*  RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.benefitsLinearLayout);
                relatedContentList.setLayoutParams(params);*/

            } else {
               /* FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), benefitsTitleTextView);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.light_fonts), benefitsStoryTextView);
                benefitsStoryTextView.setText(benefits.trim());
                colortitle1.setVisibility(View.VISIBLE);*/
                equipmentRelativeLayout.setVisibility(View.GONE);
            }

            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                favorite_view_episode.setVisibility(View.VISIBLE);
                if (preferenceManager != null) {
                    loggedInStr = preferenceManager.getUseridFromPref();
                }
                if (loggedInStr != null && isFavorite == 0 && Util.favorite_clicked == true) {

                    Util.favorite_clicked = false;
                    Log.v("SUBHA", "favorite ----");

                    AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                    addToFavInputModel.setAuthToken(authTokenStr);
                    addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                    addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                    addToFavInputModel.setIsEpisodeStr(isEpisode);

                    AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                    asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);
                } else if (loggedInStr != null && isFavorite == 1) {

                    favorite_view_episode.setImageResource(R.drawable.favorite_red);
//                    favorite_view_episode.setText(languagePreference.getTextofLanguage(FOLLOWED_PROGRAM_BUTTON,DEFAULT_FOLLOWED_PROGRAM_BUTTON));
                }

            } else {
                favorite_view_episode.setVisibility(View.GONE);
            }


        }

    }

    @Override
    public void onGetEpisodeDetailsPreExecuteStarted() {
        /*progressBarHandler = new ProgressBarHandler(ProgramDetailsActivity.this);
        progressBarHandler.show();*/
    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(Episode_Details_output episode_details_output, int status, int i, String message, String movieUniqueId) {

        try {
            if (progressBarHandler != null && progressBarHandler.isShowing()) {
                progressBarHandler.hide();

            }
        } catch (IllegalArgumentException ex) {

        }


       /* durationText = episode_details_output.getSeason_total_duration();

        Log.v("SUBHA", "duration in time === " + Util.getCountDownStringInMinutes(Integer.parseInt(durationText)));


        if (durationText.matches("") || durationText.equalsIgnoreCase("0") ) {
            durationTitleTextView.setVisibility(View.GONE);
        } else {

            FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), durationTitleTextView);
            durationTextView.setText(Util.getCountDownStringInMinutes(Integer.parseInt(durationText)));
        }
*/

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if (status == 200) {


            isAPV = episode_details_output.getIsAPV();
            isPPV = episode_details_output.getIs_ppv();


            Util.currencyModel = episode_details_output.getCurrencyDetails();
            Util.apvModel = episode_details_output.getApvDetails();
            Util.ppvModel = episode_details_output.getPpvDetails();
//            Util.dataModel.setMovieUniqueId(movieUniqueId);
            for (int a = 0; a < episode_details_output.getEpisodeArray().size(); a++) {

                String episodeNoStr = episode_details_output.getEpisodeArray().get(a).getEpisode_number();
                String episodeStoryStr = episode_details_output.getEpisodeArray().get(a).getEpisode_story();
                String episodeDateStr = episode_details_output.getEpisodeArray().get(a).getEpisode_date();
                String episodeImageStr = episode_details_output.getEpisodeArray().get(a).getPoster_url();
                String episodeTitleStr = episode_details_output.getEpisodeArray().get(a).getEpisode_title();
                String episodeSeriesNoStr = episode_details_output.getEpisodeArray().get(a).getSeries_number();
                String episodeMovieStreamUniqueIdStr = episode_details_output.getEpisodeArray().get(a).getMovie_stream_uniq_id();
                String episodeThirdParty = episode_details_output.getEpisodeArray().get(a).getThirdparty_url();

                //int episodeContenTTypesId=episodeArray.get(a).getContent_types_id();
                String videodurationStr = episode_details_output.getEpisodeArray().get(a).getVideo_duration();


                itemData.add(new EpisodesListModel(episodeNoStr, episodeStoryStr, episodeDateStr, episodeImageStr, episodeTitleStr, episodeVideoUrlStr, episodeSeriesNoStr, movieUniqueId, episodeMovieStreamUniqueIdStr, episodeThirdParty, videodurationStr));

            }
            LogUtil.showLog("MUVI", "episode show...1");

            LogUtil.showLog("BISHAL", "itemdata==" + itemData);
            if (itemData.size() <= 0) {

                buttonLinearlayout.setVisibility(View.GONE);


                //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
            } else {

                buttonLinearlayout.setVisibility(View.VISIBLE);

                if (itemData.size() <= 1) {
                    startWorkoutButton.setVisibility(View.VISIBLE);
                    letsWorkoutButton.setVisibility(View.GONE);
                    stretchWorkoutButton.setVisibility(View.GONE);
                }

                Log.v("SUBHASS", "program type value == episode details ");
                tutorialTitle.setVisibility(View.VISIBLE);
                tutorialTextView.setVisibility(View.VISIBLE);
                LogUtil.showLog("BISHAL", "data show...");
                relatedContentList.setVisibility(View.VISIBLE);
                relatedContentList.setLayoutManager(mLayoutManager);
                relatedContentList.setItemAnimator(new DefaultItemAnimator());
                RelatedContentListDataAdapter mAdapter = new RelatedContentListDataAdapter(ProgramDetailsActivity.this, R.layout.list_card_program_details, relatedContentItemData);


                relatedContentList.setAdapter(mAdapter);

                RelatedContentListInput relatedContentListInput = new RelatedContentListInput();

                useridStr = preferenceManager.getUseridFromPref();

                relatedContentListInput.setAuthToken(authTokenStr);

                if (preferenceManager != null) {
                    String countryPref = preferenceManager.getCountryCodeFromPref();
                    relatedContentListInput.setCountry(countryPref);
                } else {
                    relatedContentListInput.setCountry("IN");
                }
                relatedContentListInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                relatedContentListInput.setContent_id(contentIdStr);
                relatedContentListInput.setContent_stream_id(contentStreamIdStr);
                GetRelatedContentListAsynTask asynLoadMovieDetails = new GetRelatedContentListAsynTask(relatedContentListInput, this, this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            }


        }

    }

    public void clickItem(EpisodesListModel item, int position) {

       /* Intent intent=new Intent(ProgramDetailsActivity.this,ProgramPlayerActivity.class);
        startActivity(intent);*/
        Log.v("SUBHA", "called after startbutton clicked == ");

        itemToPlay = item;
        ItemClickedPosition = position;

        dbModel.setIsFreeContent(isFreeContent);
        dbModel.setIsAPV(isAPV);
        dbModel.setIsPPV(isPPV);
        dbModel.setIsConverted(1);
        dbModel.setMovieUniqueId(movieUniqueId);
        dbModel.setStreamUniqueId(item.getEpisodeStreamUniqueId());
        dbModel.setThirdPartyUrl(item.getEpisodeThirdPartyUrl());
        dbModel.setVideoTitle(item.getEpisodeTitle());
        dbModel.setVideoStory(item.getEpisodeDescription());
//        dbModel.setVideoGenre(videoGenreTextView.getText().toString());
        dbModel.setVideoDuration(item.getEpisodeDuration());
        // dbModel.setVideoReleaseDate(item.getEpisodeTelecastOn());
        dbModel.setVideoReleaseDate("");


//        dbModel.setEpisode_id(item.getEpisodeStreamUniqueId());
//        dbModel.setSeason_id(Season_Value);
        dbModel.setPurchase_type("episode");
        dbModel.setPosterImageId(item.getEpisodeThumbnailImageView());
        dbModel.setContentTypesId(contentTypesId);

        dbModel.setEpisode_series_no(item.getEpisodeSeriesNo());
        dbModel.setEpisode_no(item.getEpisodeNumber());
        dbModel.setEpisode_title(item.getEpisodeTitle());

        // dbModel.setParentTitle(movieNameStr);
        dbModel.setContentTypesId(3);


        Util.dataModel = dbModel;
        SubTitleName.clear();
        SubTitlePath.clear();
        ResolutionUrl.clear();
        ResolutionFormat.clear();
        SubTitleLanguage.clear();
      /*  Util.offline_url.clear();
        Util.offline_language.clear();*/


        Log.v("SUBHA", "stream id === " + item.getEpisodeStreamUniqueId());

        //edit by bishal
        //set the required data in playermodel
        playerModel.setStreamUniqueId(item.getEpisodeStreamUniqueId());
        playerModel.setMovieUniqueId(item.getEpisodeMuviUniqueId());
        playerModel.setUserId(preferenceManager.getUseridFromPref());
        playerModel.setEmailId(preferenceManager.getEmailIdFromPref());
        playerModel.setAuthTokenStr(authTokenStr.trim());
        playerModel.setRootUrl(BuildConfig.SERVICE_BASE_PATH.trim());
//        playerModel.setEpisode_id(item.getEpisodeNumber());
        playerModel.setVideoTitle(item.getEpisodeTitle());
        playerModel.setVideoStory(item.getEpisodeDescription());
//        playerModel.setVideoGenre(videoGenreTextView.getText().toString());
        playerModel.setVideoDuration(item.getEpisodeDuration());
        playerModel.setVideoReleaseDate("");
//        playerModel.setCensorRating(censorRatingStr);
        playerModel.setContentTypesId(contentTypesId);

        if (contentTypesId == 3) {
            playerModel.setPosterImageId(item.getEpisodeThumbnailImageView());

        } else {
            playerModel.setPosterImageId(posterImageId);
        }

        LogUtil.showLog("MUVI", "content typesid = " + contentTypesId);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if (isLogin == 1) {
            if (loggedInStr != null) {
                if (NetworkStatus.getInstance().isConnected(this)) {

                    callForValidateUser();

                } else {
                    Util.showToast(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                    //  Toast.makeText(ProgramDetailsActivity.this,Util.getTextofLanguage(ProgramDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
                }

            } else {


                Intent registerActivity = new LoginRegistrationOnContentClickHandler(ProgramDetailsActivity.this).handleClickOnContent();
                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Util.check_for_subscription = 1;
                registerActivity.putExtra("PLAY_LIST", itemData);
                registerActivity.putExtra("TAG", ItemClickedPosition);
                registerActivity.putExtra("PlayerModel", playerModel);
                registerActivity.putExtra("PERMALINK", permalinkStr);
                registerActivity.putExtra("SEASON", season.length);
                registerActivity.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
                registerActivity.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                registerActivity.putExtra("Index", getIntent().getStringExtra("Index"));
                startActivityForResult(registerActivity, VIDEO_PLAY_BUTTON_CLICK_LOGIN_REG_REQUESTCODE);
                //  startActivity(registerActivity);


            }
        } else {
            if (NetworkStatus.getInstance().isConnected(this)) {
                // MUVIlaxmi

                Log.v("SUBHA", "Video PLayer Called 2");

                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(authTokenStr);
                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());


                LogUtil.showLog("SUBHA", "Video PLayer Called authTokenStr 2 " + authTokenStr);
                Log.v("SUBHA", "Video PLayer Called movie uniqueid 2 " + dbModel.getMovieUniqueId().trim());
                Log.v("SUBHA", "Video PLayer Called movie StreamUniqueid 2 " + dbModel.getStreamUniqueId().trim());
                Log.v("SUBHA", "Video PLayer Called inyternet speed 2 " + MainActivity.internetSpeed.trim());
                Log.v("SUBHA", "Video PLayer Called  user id 2 " + preferenceManager.getUseridFromPref());


                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                //Toast.makeText(ProgramDetailsActivity.this,Util.getTextofLanguage(ProgramDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
            }
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

                    if (mPlaybackState == PlaybackState.PLAYING) {
                        mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);
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
/*
                    mPlayCircle.setVisibility(View.GONE);
*/

                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlayButton(PlaybackState state) {
           /* boolean isConnected = (mCastSession != null)
                    && (mCastSession.isConnected() || mCastSession.isConnecting());*/
        //mControllers.setVisibility(isConnected ? View.GONE : View.VISIBLE);

        switch (state) {
            case PLAYING:

                //mLoading.setVisibility(View.INVISIBLE);
                // mPlayPause.setVisibility(View.VISIBLE);
                //mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_pause_dark));

                break;
            case IDLE:
                if (mLocation == PlaybackLocation.LOCAL) {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                    }*/

                } else {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                    }*/
                }
                //mCon
                // trollers.setVisibility(View.GONE);
                // mCoverArt.setVisibility(View.VISIBLE);
                // mVideoView.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                //mLoading.setVisibility(View.INVISIBLE);
              /*  mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_play_dark));*/

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



                      /* mVideoView.start();
                        Log.d(TAG, "Playing locally...");
                        mPlaybackState = PlaybackState.PLAYING;
                        startControllersTimer();
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*/
                        break;

                    case REMOTE:

                        Log.v("SUBHA", "Toggle play back === played length == " + Played_Length);

                        loadRemoteMedia(Played_Length, true);

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
                       /* mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
                        mVideoView.seekTo(0);
                        mVideoView.start();
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*/
                        break;
                    case REMOTE:
                        // mPlayCircle.setVisibility(View.VISIBLE);
                        if (mCastSession != null && mCastSession.isConnected()) {
                            // watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            loadRemoteMedia(Played_Length, true);
                            Log.v("SUBHA", "Toggle play back  1233 === played length == " + Played_Length);

                            // Utils.showQueuePopup(this, mPlayCircle, mSelectedMedia);
                        } else {
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

                Intent intent = new Intent(ProgramDetailsActivity.this, ExpandedControlsActivity.class);
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

    /*****************
     * chromecast*-------------------------------------
     */


    /***********
     * Subtitle
     ********/

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


                final Intent playVideoIntent;
                if (Util.dataModel.getAdNetworkId() == 3) {
                    LogUtil.showLog("responseStr", "playVideoIntent" + Util.dataModel.getAdNetworkId());

                    playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                    playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                } else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1) {
                    if (Util.dataModel.getPlayPos() <= 0) {
                        playVideoIntent = new Intent(ProgramDetailsActivity.this, AdPlayerActivity.class);
                    } else {
                        playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                        playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                    }
                } else {
                    playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
//                    playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                }
                /***ad **/
                //Intent playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);
                /*playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                playVideoIntent.putExtra("PlayerModel", playerModel);
                playVideoIntent.putExtra("PLAY_LIST", itemData);
                playVideoIntent.putExtra("PERMALINK", permalinkStr);
                playVideoIntent.putExtra("SEASON", season.length);
                playVideoIntent.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
                Log.v("SUBHA", "current season ==== " + getIntent().getStringExtra(SEASON_INTENT_KEY));
                playVideoIntent.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                playVideoIntent.putExtra("Index", getIntent().getStringExtra("Index"));

                // ArrayList<SeasonModel> b =(ArrayList<SeasonModel>) getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY);

                //  playVideoIntent.putExtra("SEASON_ARRAY_MODEL", b );


                // Log.v("Nihar","==================="+b.size());
                playVideoIntent.putExtra("TAG", ItemClickedPosition);
//                playVideoIntent.putExtra("PLAY_LIST",itemData);
                startActivity(playVideoIntent);

            }
        }
    }


    private void ShowPpvPopUp() {
        {


            try {
                if (Util.currencyModel.getCurrencySymbol() == null) {
                    Util.showToast(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));

                    // Toast.makeText(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Util.showToast(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));

                // Toast.makeText(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProgramDetailsActivity.this, R.style.MyAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) ProgramDetailsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View convertView = inflater.inflate(R.layout.activity_ppv_popup, null);
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


            completeRadioButton.setText("  " + Util.dataModel.getVideoTitle().trim() + " Complete Season ");
            seasonRadioButton.setText("  " + Util.dataModel.getVideoTitle().trim() + " Season " + Util.dataModel.getEpisode_series_no().trim() + " ");
            episodeRadioButton.setText("  " + Util.dataModel.getVideoTitle().trim() + " S" + Util.dataModel.getEpisode_series_no().trim() + " E " + Util.dataModel.getEpisode_no().trim() + " ");

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
                        selectedPurchaseType = 1;
                        Util.selected_episode_id = "0";
                        Util.selected_season_id = "0";
                        LogUtil.showLog("MUVI", "called 1");
                    } else if (seasonRadioButton.isChecked()) {
                        selectedPurchaseType = 2;
                        Util.selected_episode_id = "0";
//                        Util.selected_season_id = "" + spinnerPosition;
                        LogUtil.showLog("MUVI", "called 2");
                    } else {
                        selectedPurchaseType = 3;
                        Util.selected_episode_id = Util.dataModel.getStreamUniqueId();
                        Util.selected_season_id = Util.dataModel.getEpisode_series_no();
                        LogUtil.showLog("MUVI", "called 3");
                    }


                    LogUtil.showLog("MUVI", "Show withepisode  Activity Season Id =" + Util.selected_season_id);
                    LogUtil.showLog("MUVI", "Show withepisode Activity episode Id =" + Util.selected_episode_id);

                    alert.dismiss();
                    final Intent showPaymentIntent = new Intent(ProgramDetailsActivity.this, PPvPaymentInfoActivity.class);
                    showPaymentIntent.putExtra("muviuniqueid", Util.dataModel.getMovieUniqueId().trim());
                    showPaymentIntent.putExtra("episodeStreamId", Util.dataModel.getStreamUniqueId().trim());
                    showPaymentIntent.putExtra("contentTypesId", Util.dataModel.getContentTypesId());
                    showPaymentIntent.putExtra("movieThirdPartyUrl", Util.dataModel.getThirdPartyUrl());
                    showPaymentIntent.putExtra("planUnSubscribedPrice", priceForUnsubscribedStr);
                    showPaymentIntent.putExtra("planSubscribedPrice", priceFosubscribedStr);
                    showPaymentIntent.putExtra("currencyId", Util.currencyModel.getCurrencyId());
                    showPaymentIntent.putExtra("currencyCountryCode", Util.currencyModel.getCurrencyCode());
                    showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
                    showPaymentIntent.putExtra("PlayerModel", playerModel);
                    showPaymentIntent.putExtra("PERMALINK", permalinkStr);
                    showPaymentIntent.putExtra("SEASON", season.length);
                    showPaymentIntent.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
                    showPaymentIntent.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                    showPaymentIntent.putExtra("Index", getIntent().getStringExtra("Index"));

                    // showPaymentIntent.putExtra("showName", Util.dataModel.getEpisode_title());


                    if (selectedPurchaseType == 1)
                        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle().trim() + " Complete Season ");
                    if (selectedPurchaseType == 2)
                        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle().trim() + " Season " + Util.dataModel.getEpisode_series_no().trim());
                    if (selectedPurchaseType == 3)
                        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle().trim() + " S" + Util.dataModel.getEpisode_series_no().trim() + " E " + Util.dataModel.getEpisode_no().trim());
                    showPaymentIntent.putExtra("seriesNumber", Util.dataModel.getEpisode_series_no());
                    showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
                    showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
                    showPaymentIntent.putExtra("selectedPurchaseType", selectedPurchaseType);
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

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
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

    private void PlayThroughChromeCast() {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, playerModel.getVideoStory());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, playerModel.getVideoTitle());
        movieMetadata.addImage(new WebImage(Uri.parse(playerModel.getPosterImageId())));
        movieMetadata.addImage(new WebImage(Uri.parse(playerModel.getPosterImageId())));


        String mediaContentType = "videos/mp4";
        if (playerModel.getVideoUrl().contains(".mpd")) {
            mediaContentType = "application/dash+xml";
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject();
                jsonObj.put("description", playerModel.getVideoTitle());
                jsonObj.put("licenseUrl", playerModel.getLicenseUrl());

                //  This Code Is Added For Video Log By Bibhu..

                jsonObj.put("authToken", authTokenStr);
                jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                jsonObj.put("ip_address", ipAddres.trim());
                jsonObj.put("movie_id", playerModel.getMovieUniqueId());

                if (contentTypesId == 3) {
                    jsonObj.put("episode_id", playerModel.getStreamUniqueId());
                } else {
                    jsonObj.put("episode_id", playerModel.getEpisode_id());

                }

                jsonObj.put("watch_status", watch_status_String);

                jsonObj.put("device_type", "2");
                jsonObj.put("log_id", "0");
                jsonObj.put("active_track_index", "0");

                if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {
                    jsonObj.put("restrict_stream_id", "0");
                    jsonObj.put("is_streaming_restriction", "1");
                } else {
                    jsonObj.put("restrict_stream_id", "0");
                    jsonObj.put("is_streaming_restriction", "0");
                }

                jsonObj.put("domain_name", BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));
                jsonObj.put("is_log", "1");

                //=====================End===================//

                // This code is changed according to new Video log //

                jsonObj.put("played_length", "0");
                jsonObj.put("log_temp_id", "0");
                jsonObj.put("resume_time", "0");
                jsonObj.put("seek_status", seek_status);
                // This  Code Is Added For Drm BufferLog By Bibhu ...

                jsonObj.put("resolution", "BEST");
                jsonObj.put("start_time", "0");
                jsonObj.put("end_time", "0");
                jsonObj.put("log_unique_id", "0");
                jsonObj.put("location", "0");
                jsonObj.put("bandwidth_log_id", "0");
                jsonObj.put("video_type", "mped_dash");
                jsonObj.put("drm_bandwidth_by_sender", "0");


                Log.v("SUBHASHREE", "authTokenStr" + authTokenStr);
                Log.v("SUBHASHREE", "user_id" + preferenceManager.getUseridFromPref());
                Log.v("SUBHASHREE", "ipAddres " + ipAddres.trim());
                Log.v("SUBHASHREE", "playerModel.getMovieUniqueId()" + playerModel.getMovieUniqueId());
                Log.v("SUBHASHREE", "watch_status_String" + watch_status_String);
                Log.v("SUBHASHREE", "seek_status" + seek_status);
                Log.v("SUBHASHREE", "domain_name" + BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));


                //====================End=====================//

            } catch (JSONException e) {
            }
            List tracks = new ArrayList();
            for (int i = 0; i < FakeSubTitlePath.size(); i++) {
                MediaTrack englishSubtitle = new MediaTrack.Builder(i,
                        MediaTrack.TYPE_TEXT)
                        .setName(SubTitleName.get(0))
                        .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                        .setContentId(FakeSubTitlePath.get(0))
                        .setLanguage(SubTitleLanguage.get(0))
                        .setContentType("text/vtt")
                        .build();
                tracks.add(englishSubtitle);
            }

            mediaInfo = new MediaInfo.Builder(playerModel.getMpdVideoUrl().trim())
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType(mediaContentType)
                    .setMetadata(movieMetadata)
                    .setCustomData(jsonObj)
                    .setMediaTracks(tracks)
                    .build();
            mSelectedMedia = mediaInfo;


            togglePlayback();
        } else {
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject();
                jsonObj.put("description", playerModel.getVideoTitle());

                //  This Code Is Added For Video Log By Bibhu..

                jsonObj.put("authToken", authTokenStr);
                jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                jsonObj.put("ip_address", ipAddres.trim());
                jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                jsonObj.put("episode_id", playerModel.getStreamUniqueId());
                jsonObj.put("watch_status", watch_status_String);
                jsonObj.put("device_type", "2");
                jsonObj.put("log_id", "0");
                jsonObj.put("active_track_index", "0");
                jsonObj.put("seek_status", "");

                jsonObj.put("played_length", "0");
                jsonObj.put("log_temp_id", "0");
                jsonObj.put("resume_time", "0");
                jsonObj.put("seek_status", seek_status);


                if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {
                    jsonObj.put("restrict_stream_id", "0");
                    jsonObj.put("is_streaming_restriction", "1");
                    Log.v("BIBHU4", "restrict_stream_id============1");
                } else {
                    jsonObj.put("restrict_stream_id", "0");
                    jsonObj.put("is_streaming_restriction", "0");
                    Log.v("BIBHU4", "restrict_stream_id============0");
                }

                jsonObj.put("domain_name", BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));
                jsonObj.put("is_log", "1");

                //=====================End===================//


                // This  Code Is Added For Drm BufferLog By Bibhu ...

                jsonObj.put("resolution", "BEST");
                jsonObj.put("start_time", "0");
                jsonObj.put("end_time", "0");
                jsonObj.put("log_unique_id", "0");
                jsonObj.put("location", "0");
                jsonObj.put("video_type", "");
                jsonObj.put("totalBandwidth", "0");

                //====================End=====================//

            } catch (JSONException e) {
            }

            List tracks = new ArrayList();
            for (int i = 0; i < FakeSubTitlePath.size(); i++) {
                MediaTrack englishSubtitle = new MediaTrack.Builder(i,
                        MediaTrack.TYPE_TEXT)
                        .setName(SubTitleName.get(0))
                        .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                        .setContentId(FakeSubTitlePath.get(0))
                        .setLanguage(SubTitleLanguage.get(0))
                        .setContentType("text/vtt")
                        .build();
                tracks.add(englishSubtitle);
            }

            mediaInfo = new MediaInfo.Builder(Util.dataModel.getVideoUrl().trim())
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType(mediaContentType)
                    .setMetadata(movieMetadata)
                    .setStreamDuration(15 * 1000)
                    .setCustomData(jsonObj)
                    .setMediaTracks(tracks)
                    .build();
            mSelectedMedia = mediaInfo;


            togglePlayback();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1001) {
            if (data.getStringExtra("yes").equals("1002")) {
                watch_status_String = "halfplay";
                seek_status = "first_time";
                Played_Length = playerModel.getPlayPos() * 1000;
                PlayThroughChromeCast();

            } else {
                watch_status_String = "start";
                Played_Length = 0;
                PlayThroughChromeCast();
            }
        } else if (resultCode == RESULT_OK && requestCode == 2001) {
            if (data.getStringExtra("yes").equals("2002")) {

                mSelectedMedia = Util.mSendingMedia;


//                Toast.makeText(this, "Now again in details", Toast.LENGTH_SHORT).show();
             /*   Log.v("bijay",""+mReceivedMedia.getStreamType());
                Log.v("bijay",""+mReceivedMedia.getContentId());
                Log.v("bijay",""+mReceivedMedia.getStreamDuration());*/

                Intent resumeIntent = new Intent(ProgramDetailsActivity.this, ResumePopupActivity.class);
                startActivityForResult(resumeIntent, 1007);

            }
        } else if (requestCode == 2001) {


            Log.v("pratik", "else conditn called");
            watch_status_String = "start";
            Played_Length = 0;
            PlayThroughChromeCast();
        } else if (resultCode == RESULT_OK && requestCode == 1007) {

            if (data.getStringExtra("yes").equals("1002")) {

                Log.v("pratik", "resumed...");
                watch_status_String = "halfplay";
                seek_status = "first_time";
                Played_Length = playerModel.getPlayPos() * 1000;
                togglePlayback();

            } else {
                watch_status_String = "start";
                Played_Length = 0;
                togglePlayback();
            }
        } else if (requestCode == VIDEO_PLAY_BUTTON_CLICK_LOGIN_REG_REQUESTCODE && resultCode == RESULT_OK) {
//            new CheckVoucherOrPpvPaymentHandler(MovieDetailsActivity.this).handleVoucherPaymentOrPpvPayment();
            // callValidateUserAPI();


            callForValidateUser();

        } else if (requestCode == 30060 && resultCode == RESULT_OK) {
            if (NetworkStatus.getInstance().isConnected(this)) {
                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
                useridStr = preferenceManager.getUseridFromPref();

                contentDetailsInput.setAuthToken(authTokenStr);

                Log.v("SUBHA", "authToken1243442554 === " + authTokenStr);
                if (preferenceManager != null) {
                    String countryPref = preferenceManager.getCountryCodeFromPref();
                    contentDetailsInput.setCountry(countryPref);
                } else {
                    contentDetailsInput.setCountry("IN");
                }
                contentDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                contentDetailsInput.setPermalink(permalinkStr);
                contentDetailsInput.setUser_id(useridStr);
                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, this, this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

    public void callForValidateUser() {
        ValidateUserInput validateUserInput = new ValidateUserInput();
        validateUserInput.setAuthToken(authTokenStr);
        validateUserInput.setUserId(preferenceManager.getUseridFromPref());
        validateUserInput.setMuviUniqueId(movieUniqueId.trim());
        validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
        validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
        validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
        validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
        asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
    }

    public void DynamicLayout(LinearLayout layout, String Header, String Details) {
        Log.v("Nihar", "" + Header + Details);
        //masterlayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        /////View
        View view = new View(this);
        //int paddingleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        //  view.setPadding(paddingleft, 0, 0, 0);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height + 2);
        view.setLayoutParams(parms);
        //  int marginleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        parms.setMargins(30, 0, 0, 0);
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ///Title Textview
        TextView textView = new TextView(this);
        textView.setText(Header);
        textView.setAllCaps(true);
        textView.setTextColor(getResources().getColor(R.color.videotextColor));
        //  int titleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.story_title_text_size) , getResources().getDisplayMetrics());

        textView.setTextSize(20);

        FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.regular_fonts), textView);
        LinearLayout.LayoutParams TextViewParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        //  int textview = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int topmargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        TextViewParams.setMargins(30, topmargin, 0, 2);
        textView.setLayoutParams(TextViewParams);

        linearLayout.addView(view);
        linearLayout.addView(textView);


        TextView detail_text = new TextView(this);
        detail_text.setPadding(0, topmargin, 0, topmargin);
        detail_text.setTextColor(getResources().getColor(R.color.videotextColor));
        //  int detailTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.story_text_size) , getResources().getDisplayMetrics());

        detail_text.setTextSize(13);
        // detail_text.setTextSize(getResources().getDimension(R.dimen.story_text_size));

        // int textviewheader = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.light_fonts), detail_text);

        detail_text.setText(Details);

        LinearLayout.LayoutParams detailsParam = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        detailsParam.setMargins(30, 0, 0, topmargin);

        detail_text.setLayoutParams(detailsParam);


        ///main layout view set
        layout.addView(linearLayout);
        layout.addView(detail_text);

    }


}