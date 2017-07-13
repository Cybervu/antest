package com.home.vod.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.home.apisdk.apiController.GetEpisodeDeatailsAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Get_Video_Details_Output;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.vod.R;
import com.home.vod.adapter.EpisodesListViewMoreAdapter;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;


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
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;

/**
 * Created by Muvi on 2/6/2017.
 */
public class Episode_list_Activity extends AppCompatActivity implements VideoDetailsAsynctask.VideoDetails, GetValidateUserAsynTask.GetValidateUser,
        GetEpisodeDeatailsAsynTask.GetEpisodeDetails, GetLanguageListAsynTask.GetLanguageList, LogoutAsynctask.Logout,
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListner {

    String filename = "";
    static File mediaStorageDir;

    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();

    int previousTotal = 0;
    VideoDetailsAsynctask asynLoadVideoUrls;
    AsynEpisodeDetails asynEpisodeDetails;
    GetValidateUserAsynTask asynValidateUserDetails;
    EpisodesListModel itemToPlay;
    String permalinkStr;
    ArrayList<EpisodesListModel> itemData = new ArrayList<EpisodesListModel>();
    LinearLayoutManager mLayoutManager;
    boolean firstTime = false;
    private EpisodesListViewMoreAdapter customListAdapter;
    public static boolean isLoading = false;
    private String movieVideoUrlStr = "";
    private String videoResolution = "";

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    RecyclerView episodelist;
    private RelativeLayout noInternetConnectionLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;
    SharedPreferences isLoginPref;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;
    int isLogin = 0;

    int isPPV = 0;
    int isAPV = 0;
    int isConverted = 0;
    int isFreeContent = 0;

    ProgressBarHandler pDialog;
    RelativeLayout footerView;
    private PreferenceManager preferenceManager;
    String PlanId = "";
    String videoUrlStr = "";
    String priceForUnsubscribedStr, priceFosubscribedStr;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    PPVModel ppvmodel;
    APVModel advmodel;
    CurrencyModel currencymodel;

    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    AlertDialog alert;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;
    ProgressBarHandler progressBarHandler;


    public void resetData() {
        if (itemData != null && itemData.size() > 0) {
            itemData.clear();
        }
        firstTime = true;

        offset = 1;
        isLoading = false;
        listSize = 0;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            limit = 20;
        } else {
            limit = 15;
        }
        itemsInServer = 0;
    }


    private static final String TAG = "ShowWithEpisodesActivity";

    @Override
    public void onVideoDetailsPreExecuteStarted() {

        pDialog = new ProgressBarHandler(Episode_list_Activity.this);
        pDialog.show();

    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Get_Video_Details_Output get_video_details_output, int code, String status, String message) {
        if (status == null) {
            status = "0";
            Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
            //movieThirdPartyUrl = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        }

        if ((status.trim().equalsIgnoreCase("0"))) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                // movieThirdPartyUrl = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
            }
            Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
            //movieThirdPartyUrl = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
            dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
            dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }


                if (!videoUrlStr.equals("")) {
                        /*if (mCastSession != null && mCastSession.isConnected()) {
                            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE,Util.dataModel.getVideoReleaseDate());
                            movieMetadata.putString(MediaMetadata.KEY_TITLE,  Util.dataModel.getEpisode_title());
                            movieMetadata.addImage(new WebImage(Uri.parse(Util.dataModel.getEpisodePosterUrl())));
                            movieMetadata.addImage(new WebImage(Uri.parse(Util.dataModel.getEpisodePosterUrl())));
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject();
                                jsonObj.put("description", Util.dataModel.getEpisode_title());
                            } catch (JSONException e) {

                            }

                            mediaInfo = new MediaInfo.Builder(videoUrlStr.trim())
                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                    .setContentType("videos/mp4")
                                    .setMetadata(movieMetadata)
                                    .setStreamDuration(15 * 1000)
                                    .setCustomData(jsonObj)
                                    .build();
                            mSelectedMedia = mediaInfo;


                            togglePlayback();
                        }*/

                    final Intent playVideoIntent;
                    if (Util.goToLibraryplayer) {
                        playVideoIntent = new Intent(Episode_list_Activity.this, MyLibraryPlayer.class);

                    } else {
                        playVideoIntent = new Intent(Episode_list_Activity.this, ExoPlayerActivity.class);

                    }
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

                                progressBarHandler = new ProgressBarHandler(Episode_list_Activity.this);
                                progressBarHandler.show();
                                Download_SubTitle(FakeSubTitlePath.get(0).trim());
                            } else {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);
                                startActivity(playVideoIntent);
                            }


                        }
                    });
                } else {
                    if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")) {
                        if (Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
                            final Intent playVideoIntent = new Intent(Episode_list_Activity.this, ThirdPartyPlayer.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });
                        } else {

                            final Intent playVideoIntent = new Intent(Episode_list_Activity.this, YouTubeAPIActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);


                                }
                            });

                        }
                    } else {
                        final Intent playVideoIntent = new Intent(Episode_list_Activity.this, ThirdPartyPlayer.class);
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
    public void onGetValidateUserPreExecuteStarted() {
        pDialog = new ProgressBarHandler(Episode_list_Activity.this);
        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {

        String Subscription_Str = preferenceManager.getIsSubscribedFromPref();
        if (validateUserOutput == null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
            dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(Episode_list_Activity.this, MainActivity.class);
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
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
            dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(Episode_list_Activity.this, MainActivity.class);
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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                if (message != null && message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                }
                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
                        if (Util.checkNetwork(Episode_list_Activity.this) == true) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, Episode_list_Activity.this, this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                                ShowPpvPopUp();
                            } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
                                Intent intent = new Intent(Episode_list_Activity.this, SubscriptionActivity.class);
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
                Intent intent = new Intent(Episode_list_Activity.this, SubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else if (Util.dataModel.getIsConverted() == 0) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            } else {
                if (Util.checkNetwork(Episode_list_Activity.this) == true) {
                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                    getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                    VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, Episode_list_Activity.this, this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(Episode_list_Activity.this);
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
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(Episode_list_Activity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        if (status == null) {
            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                 /*   SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                    if (countryPref!=null) {
                        SharedPreferences.Editor countryEditor = countryPref.edit();
                        countryEditor.clear();
                        countryEditor.commit();
                    }*/
                if ((Util.getTextofLanguage(Episode_list_Activity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(Episode_list_Activity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(Episode_list_Activity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }
    }


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
    private ImageButton mPlayCircle;


    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_listing);

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        isLogin = preferenceManager.getLoginFeatureFromPref();


       /* mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {

                    showIntroductoryOverlay();
                }
            }
        };
        mCastContext = CastContext.getSharedInstance(Episode_list_Activity.this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(Episode_list_Activity.this, savedInstanceState);



        // int startPosition = getInt("startPosition", 0);
        // mVideoView.setVideoURI(Uri.parse(item.getContentId()));

        setupCastListener();
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();*/


        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        mActionBarToolbar.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SEASON, Util.DEFAULT_SEASON).toString() + " " + getIntent().getStringExtra(Util.SEASON_INTENT_KEY));
        mActionBarToolbar.setTitleTextColor(Color.WHITE);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView sectionTitle = (TextView) findViewById(R.id.sectionTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        sectionTitle.setTypeface(castDescriptionTypeface);
        sectionTitle.setText(Util.getTextofLanguage(Episode_list_Activity.this, Util.EPISODE_TITLE, Util.DEFAULT_EPISODE_TITLE));

        episodelist = (RecyclerView) findViewById(R.id.episodelist);
        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));


        mLayoutManager = new LinearLayoutManager(Episode_list_Activity.this, LinearLayoutManager.VERTICAL, false);
        permalinkStr = getIntent().getStringExtra(Util.PERMALINK_INTENT_KEY);

        footerView = (RelativeLayout) findViewById(R.id.loadingPanel);
        footerView.setVisibility(View.GONE);

        ppvmodel = new PPVModel();
        advmodel = new APVModel();
        currencymodel = new CurrencyModel();
        PlanId = (Util.getTextofLanguage(Episode_list_Activity.this, Util.PLAN_ID, Util.DEFAULT_PLAN_ID)).trim();

        resetData();

        boolean isNetwork = Util.checkNetwork(Episode_list_Activity.this);
        if (isNetwork == true) {

            if (ContextCompat.checkSelfPermission(Episode_list_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Episode_list_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(Episode_list_Activity.this,
                            new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                            111);
                } else {
                    ActivityCompat.requestPermissions(Episode_list_Activity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            111);
                }
            } else {
                //Call whatever you want
                if (isNetwork == true) {
                    asynEpisodeDetails = new AsynEpisodeDetails();
                    asynEpisodeDetails.executeOnExecutor(threadPoolExecutor);
                } else {
                    noInternetConnectionLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
        }

        episodelist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem)) {
                    // End has been reached
                    listSize = itemData.size();
                    if (mLayoutManager.findLastVisibleItemPosition() >= itemsInServer - 1) {
                        isLoading = true;
                        footerView.setVisibility(View.GONE);
                        return;

                    }
                    offset += 1;
                    boolean isNetwork = Util.checkNetwork(Episode_list_Activity.this);
                    if (isNetwork == true) {
                        // default data
                        asynEpisodeDetails = new AsynEpisodeDetails();
                        asynEpisodeDetails.executeOnExecutor(threadPoolExecutor);
                    }

                }

            }
        });

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

    }


    public void clickItem(EpisodesListModel item) {


        Util.check_for_subscription = 1;

        itemToPlay = item;
        DataModel dbModel = new DataModel();
        dbModel.setIsFreeContent(isFreeContent);
        dbModel.setIsAPV(isAPV);
        dbModel.setIsPPV(isPPV);
        dbModel.setIsConverted(1);
        dbModel.setMovieUniqueId(item.getEpisodeMuviUniqueId());
        dbModel.setStreamUniqueId(item.getEpisodeStreamUniqueId());
        dbModel.setThirdPartyUrl(item.getEpisodeThirdPartyUrl());
        dbModel.setVideoTitle(item.getEpisodeTitle());
        // dbModel.setVideoStory(getIntent().getStringExtra(Util.STORY_INTENT_KEY));
        dbModel.setVideoStory(item.getEpisodeDescription());

        dbModel.setVideoGenre(getIntent().getStringExtra(Util.GENRE_INTENT_KEY));
        dbModel.setVideoDuration(item.getEpisodeDuration());
        // dbModel.setVideoReleaseDate(item.getEpisodeTelecastOn());
        dbModel.setVideoReleaseDate("");

        dbModel.setCensorRating(getIntent().getStringExtra(Util.CENSOR_RATING_INTENT_KEY));
        dbModel.setCastCrew(getIntent().getBooleanExtra(Util.CAST_INTENT_KEY, false));
        dbModel.setEpisode_id(item.getEpisodeStreamUniqueId());
        dbModel.setPosterImageId(item.getEpisodeThumbnailImageView());

        dbModel.setContentTypesId(Integer.parseInt(getIntent().getStringExtra("content_types_id")));

        dbModel.setEpisode_series_no(item.getEpisodeSeriesNo());
        dbModel.setEpisode_no(item.getEpisodeNumber());
        dbModel.setEpisode_title(item.getEpisodeTitle());

        if (!getIntent().getStringExtra(Util.SEASON_INTENT_KEY).equals("")) {
            dbModel.setSeason_id(getIntent().getStringExtra(Util.SEASON_INTENT_KEY));

        } else {
            dbModel.setSeason_id("0");

        }
        dbModel.setPurchase_type("episode");
        Util.dataModel = dbModel;


        SubTitleName.clear();
        SubTitlePath.clear();
        ResolutionUrl.clear();
        ResolutionFormat.clear();

        if (isLogin == 1) {
            if (preferenceManager != null) {
                String loggedInStr = preferenceManager.getLoginStatusFromPref();

                if (loggedInStr == null) {
                    Intent i = new Intent(Episode_list_Activity.this, RegisterActivity.class);
                    Util.check_for_subscription = 1;
                    startActivity(i);
                    //showLoginDialog();
                } else {
                    if (Util.checkNetwork(Episode_list_Activity.this) == true) {


                        if (isFreeContent == 1) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, this, this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            ValidateUserInput validateUserInput = new ValidateUserInput();
                            validateUserInput.setAuthToken(Util.authTokenStr);
                            validateUserInput.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
                            validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                            validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                            validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                            validateUserInput.setLanguageCode(Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                            GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, this, this);
                            asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                        }
                    } else {
                        Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }
            } else {

                Intent i = new Intent(Episode_list_Activity.this, RegisterActivity.class);
                Util.check_for_subscription = 1;

                startActivity(i);
            }
        } else {
            if (Util.checkNetwork(Episode_list_Activity.this) == true) {
                // MUVIlaxmi

                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(Util.authTokenStr);
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, this, this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
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
//
//                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    e.printStackTrace();
//                }
//
//                JSONArray SubtitleJosnArray = null;
//                JSONArray ResolutionJosnArray = null;
//                JSONObject myJson =null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
//                    ResolutionJosnArray = myJson.optJSONArray("videoDetails");
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//                if (statusCode >= 0) {
//                    if (statusCode == 200) {
//                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA))) {
//                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
//                                videoUrlStr = myJson.getString("videoUrl");
//                            }
//
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//
//                            }
//                        }else{
//                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//
//                        if(SubtitleJosnArray!=null)
//                        {
//                            if(SubtitleJosnArray.length()>0)
//                            {
//                                for(int i=0;i<SubtitleJosnArray.length();i++)
//                                {
//                                    SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
//                                    FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
//
//                                    Log.v("MUVI","SUbtitle Name ="+SubtitleJosnArray.getJSONObject(i).optString("language").trim());
//                                    Log.v("MUVI","SUbtitle FAke Path ="+SubtitleJosnArray.getJSONObject(i).optString("url").trim());
//                                }
//                            }
//                        }
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
//                                }
//                            }
//                        }
//
//
//                    }
//
//                }
//                else {
//
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                }
//            } catch (JSONException e1) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
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
//            movieVideoUrlStr = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
//        }*/
//            if (responseStr == null) {
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
//            }
//
//            if ((responseStr.trim().equalsIgnoreCase("0"))) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    // movieThirdPartyUrl = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
//                }
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = Util.getTextofLanguage(RegisterActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DATA,Util.DEFAULT_NO_DATA));
//                    }
//
//
//
//                    if (!videoUrlStr.equals("")) {
//                        /*if (mCastSession != null && mCastSession.isConnected()) {
//                            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
//
//                            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE,Util.dataModel.getVideoReleaseDate());
//                            movieMetadata.putString(MediaMetadata.KEY_TITLE,  Util.dataModel.getEpisode_title());
//                            movieMetadata.addImage(new WebImage(Uri.parse(Util.dataModel.getEpisodePosterUrl())));
//                            movieMetadata.addImage(new WebImage(Uri.parse(Util.dataModel.getEpisodePosterUrl())));
//                            JSONObject jsonObj = null;
//                            try {
//                                jsonObj = new JSONObject();
//                                jsonObj.put("description", Util.dataModel.getEpisode_title());
//                            } catch (JSONException e) {
//
//                            }
//
//                            mediaInfo = new MediaInfo.Builder(videoUrlStr.trim())
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
//                        }*/
//
//                            final Intent playVideoIntent;
//                            if(Util.goToLibraryplayer)
//                            {
//                                playVideoIntent = new Intent(Episode_list_Activity.this, MyLibraryPlayer.class);
//
//                            }
//                            else
//                            {
//                                playVideoIntent = new Intent(Episode_list_Activity.this, ExoPlayerActivity.class);
//
//                            }
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
//                                        progressBarHandler = new ProgressBarHandler(Episode_list_Activity.this);
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
//
//
//                                }
//                            });
//                        }
//                    else{
//                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")){
//                            if(Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
//                                final Intent playVideoIntent = new Intent(Episode_list_Activity.this, ThirdPartyPlayer.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(playVideoIntent);
//
//                                    }
//                                });
//                            }else{
//
//                                final Intent playVideoIntent = new Intent(Episode_list_Activity.this, YouTubeAPIActivity.class);
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
//                            final Intent playVideoIntent = new Intent(Episode_list_Activity.this, ThirdPartyPlayer.class);
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
//            pDialog = new ProgressBarHandler(Episode_list_Activity.this);
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
//            String urlRouteList = Util.rootUrl().trim() + Util.userValidationUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("user_id", loggedInIdStr.trim());
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("movie_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("purchase_type", Util.dataModel.getPurchase_type());
//                httppost.addHeader("season_id", Util.dataModel.getSeason_id());
//                httppost.addHeader("episode_id", Util.dataModel.getEpisode_id());
//                SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
//             /*   if (countryPref != null) {
//                    String countryCodeStr = countryPref.getString("countryCode", null);
//                    httppost.addHeader("country", countryCodeStr);
//                }else{
//                    httppost.addHeader("country", "IN");
//
//                }    */
//
//                httppost.addHeader("lang_code", Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
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
//                } catch (final org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            status = 0;
//                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                    validUserStr = myJson.optString("status");
//                    userMessage = myJson.optString("msg");
//
//                }
//
//            } catch (Exception e) {
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
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(Episode_list_Activity.this, MainActivity.class);
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
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(Episode_list_Activity.this, MainActivity.class);
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
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                    if (userMessage != null && userMessage.equalsIgnoreCase("")) {
//                        dlgAlert.setMessage(userMessage);
//                    } else {
//                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
//
//                    }
//                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
//                        if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
//                            if (Util.checkNetwork(Episode_list_Activity.this) == true) {
//                                GetVideoDetailsInput getVideoDetailsInput=new GetVideoDetailsInput();
//                                getVideoDetailsInput.setAuthToken(Util.authTokenStr);
//                                getVideoDetailsInput.setUser_id(pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//                                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
//                                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
//                                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
//                                VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput,Episode_list_Activity.this);
//                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                            } else {
//                                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                            }
//                        } else {
//
//                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid"))) {
//                                if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
//                                    ShowPpvPopUp();
//                                } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
//                                    Intent intent = new Intent(Episode_list_Activity.this, SubscriptionActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(intent);
//                                } else {
//                                    ShowPpvPopUp();
//                                }
//                            }
//
//                        }
//                    }
//
//                } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
//                    ShowPpvPopUp();
//                } else if (PlanId.equals("1") && Subscription_Str.equals("0")) {
//                    Intent intent = new Intent(Episode_list_Activity.this, SubscriptionActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//                } else if (Util.dataModel.getIsConverted() == 0) {
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else {
//                    if (Util.checkNetwork(Episode_list_Activity.this) == true) {
//                        GetVideoDetailsInput getVideoDetailsInput=new GetVideoDetailsInput();
//                        getVideoDetailsInput.setAuthToken(Util.authTokenStr);
//                        getVideoDetailsInput.setUser_id(pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
//                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
//                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
//                        VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput,Episode_list_Activity.this);
//                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                    } else {
//                        Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//            pDialog = new ProgressBarHandler(Episode_list_Activity.this);
//            pDialog.show();
//
//        }
//
//
//    }

/*    private class AsynValidateUserDetails extends AsyncTask<Void, Void, Void> {
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
              *//*  SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                if (countryPref != null) {
                    String countryCodeStr = countryPref.getString("countryCode", null);
                    httppost.addHeader("country", countryCodeStr);
                }else{
                    httppost.addHeader("country", "IN");

                }*//*
                httppost.addHeader("lang_code",Util.getTextofLanguage(Episode_list_Activity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

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
                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

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
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    validUserStr = myJson.optString("status");
                    userMessage = myJson.optString("msg");

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
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }

            if(responseStr == null){
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }
            else if (status <= 0) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (status > 0) {
                if (status == 425){


                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                   *//* if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                    }*//*
                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO)+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));

                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                }
                            });
                    dlgAlert.create().show();
                }
                else if (status == 426){


                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                 *//*   if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                    }*//*
                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO)+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));

                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();



                                }
                            });
                    dlgAlert.create().show();
                }
                else if (status == 428){


                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                  *//*  if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.CROSSED_MAXIMUM_LIMIT,Util.DEFAULT_CROSSED_MAXIMUM_LIMIT));

                    }*//*
                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.CROSSED_MAXIMUM_LIMIT,Util.CROSSED_MAXIMUM_LIMIT)+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO)+" "+Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));

                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();






                                }
                            });
                    dlgAlert.create().show();
                }
                else if (Util.dataModel.getIsAPV() == 1 && status == 431){



                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }

                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                    if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ALREADY_PURCHASE_THIS_CONTENT,Util.DEFAULT_ALREADY_PURCHASE_THIS_CONTENT));

                    }
                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                    overridePendingTransition(0,0);
                                }
                            });
                    dlgAlert.create().show();

                }
                else if (status == 430){


                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                 *//*   if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                    }*//*
                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO)+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));

                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                }
                            });
                    dlgAlert.create().show();


                }
                else if (status == 427){


                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                    if (userMessage!=null && userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY,Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                    }
                    dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }
                else if (status == 429){


                    if (validUserStr==null){
                        try {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                        } catch (IllegalArgumentException ex) {
                            status = 0;
                        }
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                      *//*  if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                            dlgAlert.setMessage(userMessage);
                        }else{
                            dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                        }*//*
                        dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO)+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));


                        dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();


                                    }
                                });
                        dlgAlert.create().show();



                    } if (validUserStr!=null) {
                        try {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                        } catch (IllegalArgumentException ex) {
                            status = 0;
                        }

                        if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
                            if (Util.dataModel.getIsPPV() == 0 && Util.dataModel.getIsAPV() == 0 && Util.dataModel.getIsConverted() == 0){
                                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_VIDEO_AVAILABLE,Util.DEFAULT_NO_VIDEO_AVAILABLE), Toast.LENGTH_LONG).show();

                            }
                            else if (Util.dataModel.getIsPPV() == 0 && Util.dataModel.getIsAPV() == 0 && Util.dataModel.getIsConverted() == 1) {
                                if (Util.checkNetwork(Episode_list_Activity.this) == true) {
                                    AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                                }
                            }
                            else if (Util.dataModel.getIsAPV() == 1){
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);

                                if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                                    dlgAlert.setMessage(userMessage+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));

                                }
                                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                dlgAlert.setCancelable(false);
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                dlgAlert.create().show();
                            }
                            else if (Util.dataModel.getIsPPV() == 1) {
                                if (Util.checkNetwork(Episode_list_Activity.this) == true) {
                                    AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                                }
                            }


                        }else{
                            try {
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.hide();
                                    pDialog = null;
                                }
                            } catch (IllegalArgumentException ex) {
                                status = 0;
                            }
                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid"))) {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this);
                              *//*  if (userMessage != null && !userMessage.equalsIgnoreCase("")) {
                                    dlgAlert.setMessage(userMessage);
                                } else {
                                    dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                                }*//*
                                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO)+ " " +Util.getTextofLanguage(Episode_list_Activity.this,Util.APP_ON,Util.DEFAULT_APP_ON)+" "+getResources().getString(R.string.studio_site));

                                dlgAlert.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                dlgAlert.setCancelable(false);
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
            }

        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(Episode_list_Activity.this);
            pDialog.show();


        }


    }*/


    @Override
    public void onGetEpisodeDetailsPreExecuteStarted() {

    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(ArrayList<Episode_Details_output> episode_details_output, int i, int status, String message) {

    }
    private class AsynEpisodeDetails extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pDialog;
        String responseStr;
        String movieThirdPartyUrl = "";
        int episodeContenTTypesId = 0;
        private String movieUniqueId;

        int status;

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.episodesUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("permalink", permalinkStr.trim());
                httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                //httppost.addHeader("deviceType", "roku");
                String countryCodeStr = preferenceManager.getCountryCodeFromPref();
                if (countryCodeStr != null) {

                    httppost.addHeader("country", countryCodeStr);
                } else {
                    httppost.addHeader("country", "IN");

                }
                httppost.addHeader("lang_code", Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                if (!getIntent().getStringExtra(Util.SEASON_INTENT_KEY).equals("")) {

                    httppost.addHeader("series_number", getIntent().getStringExtra(Util.SEASON_INTENT_KEY));

                } else {

                }


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
                            if (itemData != null) {
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                episodelist.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);


                            } else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                episodelist.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.GONE);
                            }
                            footerView.setVisibility(View.GONE);

                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();


                        }

                    });

                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            footerView.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.GONE);
                            episodelist.setVisibility(View.GONE);
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);

                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                        }

                    });
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    String items = myJson.optString("item_count");
                    movieUniqueId = myJson.optString("muvi_uniq_id");
                    itemsInServer = Integer.parseInt(items);
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
                            LogUtil.showLog("MUVI", "currency");

                            JSONObject currencyJson = null;
                            if (myJson.has("currency") && myJson.getString("currency") != null && !myJson.getString("currency").equals("null")) {
                                currencyJson = myJson.getJSONObject("currency");


                                if (currencyJson.has("id") && currencyJson.getString("id").trim() != null && !currencyJson.getString("id").trim().isEmpty() && !currencyJson.getString("id").trim().equals("null") && !currencyJson.getString("id").trim().matches("")) {
                                    // currencyIdStr = currencyJson.getString("id");
                                    currencymodel.setCurrencyId(currencyJson.getString("id"));
                                    LogUtil.showLog("MUVI", "currency id" + currencymodel.getCurrencyId());

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


                                String episodeTitleStr = "";
                                if ((jsonChildNode.has("episode_title")) && jsonChildNode.getString("episode_title").trim() != null && !jsonChildNode.getString("episode_title").trim().isEmpty() && !jsonChildNode.getString("episode_title").trim().equals("null") && !jsonChildNode.getString("episode_title").trim().matches("")) {
                                    episodeTitleStr = jsonChildNode.getString("episode_title");

                                }
                                String episodeNoStr = Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                                if ((jsonChildNode.has("episode_number")) && jsonChildNode.getString("episode_number").trim() != null && !jsonChildNode.getString("episode_number").trim().isEmpty() && !jsonChildNode.getString("episode_number").trim().equals("null") && !jsonChildNode.getString("episode_number").trim().matches("")) {
                                    episodeNoStr = jsonChildNode.getString("episode_number");

                                }


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
                                String episodeVideoUrlStr = Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                                if ((jsonChildNode.has("embeddedUrl")) && jsonChildNode.getString("embeddedUrl").trim() != null && !jsonChildNode.getString("embeddedUrl").trim().isEmpty() && !jsonChildNode.getString("embeddedUrl").trim().equals("null") && !jsonChildNode.getString("embeddedUrl").trim().matches("")) {
                                    episodeVideoUrlStr = jsonChildNode.getString("embeddedUrl");

                                }


                                String episodeImageStr = "";

                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    episodeImageStr = jsonChildNode.getString("poster_url");

                                }
                                String episodeDateStr = "";

                                if ((jsonChildNode.has("episode_date")) && jsonChildNode.getString("episode_date").trim() != null && !jsonChildNode.getString("episode_date").trim().isEmpty() && !jsonChildNode.getString("episode_date").trim().equals("null") && !jsonChildNode.getString("episode_date").trim().matches("")) {
                                    episodeDateStr = jsonChildNode.getString("episode_date");
                                    //episodeDateStr = Util.formateDateFromstring("yyyy-mm-dd", "mm-dd-yyyy", episodeDateStr);
                                    episodeDateStr = "";
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

                                String episodeThirdParty = Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                                if ((jsonChildNode.has("thirdparty_url")) && jsonChildNode.getString("thirdparty_url").trim() != null && !jsonChildNode.getString("thirdparty_url").trim().isEmpty() && !jsonChildNode.getString("thirdparty_url").trim().equals("null") && !jsonChildNode.getString("thirdparty_url").trim().matches("")) {
                                    episodeThirdParty = jsonChildNode.getString("thirdparty_url");

                                }
                                String videodurationStr = "";

                                if ((jsonChildNode.has("video_duration")) && jsonChildNode.getString("video_duration").trim() != null && !jsonChildNode.getString("video_duration").trim().isEmpty() && !jsonChildNode.getString("video_duration").trim().equals("null") && !jsonChildNode.getString("video_duration").trim().matches("")) {
                                    videodurationStr = jsonChildNode.getString("video_duration");

                                }

                                itemData.add(new EpisodesListModel(episodeNoStr, episodeStoryStr, episodeDateStr, episodeImageStr, episodeTitleStr, episodeVideoUrlStr, episodeSeriesNoStr, movieUniqueId, episodeMovieStreamUniqueIdStr, episodeThirdParty, episodeContenTTypesId, videodurationStr));
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (pDialog != null && pDialog.isShowing()) {
                                            pDialog.hide();
                                            pDialog = null;
                                        }
                                        footerView.setVisibility(View.GONE);
                                        noDataLayout.setVisibility(View.VISIBLE);
                                        episodelist.setVisibility(View.GONE);
                                        noInternetConnectionLayout.setVisibility(View.GONE);

                                    }

                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.hide();
                                    pDialog = null;
                                }
                                footerView.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                                episodelist.setVisibility(View.GONE);
                                noInternetConnectionLayout.setVisibility(View.GONE);
                            }

                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                        footerView.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                        episodelist.setVisibility(View.GONE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                    }

                });
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

                        footerView.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                        episodelist.setVisibility(View.GONE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                    }

                });
            }
            if (responseStr == null) {
                responseStr = "0";
                footerView.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                episodelist.setVisibility(View.GONE);
                noInternetConnectionLayout.setVisibility(View.GONE);

            }
            if ((responseStr.trim().equals("0"))) {
                footerView.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                episodelist.setVisibility(View.GONE);
                noInternetConnectionLayout.setVisibility(View.GONE);


                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
            } else {
                if (itemData.size() <= 0) {
                    footerView.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    episodelist.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    AsynLOADUI loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);

                    //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
                } else {
                    footerView.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    episodelist.setVisibility(View.VISIBLE);
                    AsynLOADUI loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);
                }
            }
        }

        @Override
        protected void onPreExecute() {

            if (listSize == 0) {
                // hide loader for first time

                pDialog = new ProgressBarHandler(Episode_list_Activity.this);
                pDialog.show();

                footerView.setVisibility(View.GONE);
            } else {
                // show loader for first time
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                footerView.setVisibility(View.VISIBLE);

            }

        }


    }

    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {


            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {

                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                episodelist.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }

            if (footerView != null && listSize >= itemsInServer - 1) {
                footerView.setVisibility(View.GONE);
            }
            if (firstTime == true) {


                episodelist.smoothScrollToPosition(0);
                firstTime = false;
                ViewGroup.LayoutParams layoutParams = episodelist.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
                episodelist.setLayoutParams(layoutParams);
                if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    mLayoutManager = new GridLayoutManager(Episode_list_Activity.this, 1);
                    episodelist.setLayoutManager(mLayoutManager);

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    mLayoutManager = new GridLayoutManager(Episode_list_Activity.this, 1);
                    episodelist.setLayoutManager(mLayoutManager);

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    mLayoutManager = new GridLayoutManager(Episode_list_Activity.this, 1);
                    episodelist.setLayoutManager(mLayoutManager);

                } else {
                    mLayoutManager = new GridLayoutManager(Episode_list_Activity.this, 1);
                    episodelist.setLayoutManager(mLayoutManager);
                }
                customListAdapter = new EpisodesListViewMoreAdapter(Episode_list_Activity.this, R.layout.episode_list_item, itemData, new EpisodesListViewMoreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EpisodesListModel item) {
                        clickItem(item);
                    }
                });
                episodelist.setAdapter(customListAdapter);

            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = episodelist.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


                customListAdapter = new EpisodesListViewMoreAdapter(Episode_list_Activity.this, R.layout.episode_list_item, itemData, new EpisodesListViewMoreAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(EpisodesListModel item) {
                        clickItem(item);
                    }
                });
                episodelist.setAdapter(customListAdapter);


                if (mBundleRecyclerViewState != null) {
                    episodelist.getLayoutManager().onRestoreInstanceState(listState);
                }
            }

        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = episodelist.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }


    @Override
    public void onBackPressed() {
        if (asynLoadVideoUrls != null) {
            asynLoadVideoUrls.cancel(true);
        }
        if (asynEpisodeDetails != null) {
            asynEpisodeDetails.cancel(true);
        }
        if (asynValidateUserDetails != null) {
            asynValidateUserDetails.cancel(true);
        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }


    public void ShowPpvPopUp() {


        try {
            if (Util.currencyModel.getCurrencySymbol() == null) {
                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
            return;
        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Episode_list_Activity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) Episode_list_Activity.this.getSystemService(Episode_list_Activity.this.LAYOUT_INFLATER_SERVICE);

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


      /*  if (Util.dataModel.getIsAPV() == 1) {
            episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeUnsubscribedStr());
            seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " +Util.apvModel.getApvSeasonUnsubscribedStr());
            completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowUnsubscribedStr());
        } else {
            episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeUnsubscribedStr());
            seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonUnsubscribedStr());
            completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " +Util.ppvModel.getPpvShowUnsubscribedStr());
        }*/

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


       /* if (completeRadioButton.isChecked() == true) {
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
                } else if (seasonRadioButton.isChecked()) {
                    Util.selected_episode_id = "0";
                    Util.selected_season_id = Util.dataModel.getEpisode_series_no();

                } else {
                    Util.selected_episode_id = Util.dataModel.getStreamUniqueId();
                    Util.selected_season_id = Util.dataModel.getEpisode_series_no();
                }

                alert.dismiss();
                final Intent showPaymentIntent = new Intent(Episode_list_Activity.this, PPvPaymentInfoActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item, item1, item2, item3, item4, item5, item6;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);

        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(Episode_list_Activity.this, menu, R.id.media_route_menu_item);

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();

        if (preferenceManager.getLanguageListFromPref().equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);


        if (loggedInStr != null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            item5.setVisible(false);
            /*item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(Episode_list_Activity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.PROFILE, Util.DEFAULT_PROFILE));

            item1.setVisible(true);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));

            item2.setVisible(true);
            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(true);

        } else if (loggedInStr == null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            if (isLogin == 1) {
                item4.setVisible(true);
                item5.setVisible(true);

            } else {
                item4.setVisible(false);
                item5.setVisible(false);

            }
           /* item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(Episode_list_Activity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.PROFILE, Util.DEFAULT_PROFILE));
            item1.setVisible(false);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(Episode_list_Activity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.media_route_menu_item:
                // Not implemented here
                return false;
            case R.id.action_search:
                final Intent searchIntent = new Intent(Episode_list_Activity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                final Intent loginIntent = new Intent(Episode_list_Activity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(Episode_list_Activity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);

                if (Util.languageModel != null && Util.languageModel.size() > 0) {


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputMode = new LanguageListInputModel();
                    languageListInputMode.setAuthToken(Util.authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputMode, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(Episode_list_Activity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

             /*  Intent purchaseintent = new Intent(Episode_list_Activity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);*/
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Episode_list_Activity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.YES, Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(Util.authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, Episode_list_Activity.this, Episode_list_Activity.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(Util.getTextofLanguage(Episode_list_Activity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

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


    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Episode_list_Activity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Episode_list_Activity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(Util.getTextofLanguage(Episode_list_Activity.this, Util.APP_SELECT_LANGUAGE, Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(Util.getTextofLanguage(Episode_list_Activity.this, Util.BUTTON_APPLY, Util.DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        languageCustomAdapter = new LanguageCustomAdapter(Episode_list_Activity.this, Util.languageModel);
        // Util.languageModel.get(0).setSelected(true);
      /*  if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
            prevPosition = i;
            Util.languageModel.get(i).setSelected(true);

        }
        Util.languageModel.get(0).setSelected(true);*/

        recyclerView.setAdapter(languageCustomAdapter);



    /*    for (int i = 0 ; i < Util.languageModel.size() - 1 ; i ++){
                if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
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
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener1(Episode_list_Activity.this, recyclerView, new ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
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
                    languageListInputModel.setAuthToken(Util.authTokenStr);
                    languageListInputModel.setLangCode(Default_Language);
                    GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel,Episode_list_Activity.this,Episode_list_Activity.this);
                    asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

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
//                        if (!Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, "").equals("")) {
//                            Default_Language = Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
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
//            progressBarHandler = new ProgressBarHandler(Episode_list_Activity.this);
//            progressBarHandler.show();
//
//        }
//    }
    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(Episode_list_Activity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {


        if (progressBarHandler != null && progressBarHandler.isShowing()) {
            progressBarHandler.hide();
            progressBarHandler = null;

        }

        if (jsonResponse == null) {
        } else {
            if (status > 0 && status == 200) {

                try {
                    JSONObject json = new JSONObject(jsonResponse);


                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ALREADY_MEMBER, json.optString("already_member").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ACTIAVTE_PLAN_TITLE, json.optString("activate_plan_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS_ACTIVE, json.optString("transaction_status_active").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ADD_TO_FAV, json.optString("add_to_fav").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ADDED_TO_FAV, json.optString("added_to_fav").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ADVANCE_PURCHASE, json.optString("advance_purchase").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ALERT, json.optString("alert").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EPISODE_TITLE, json.optString("episodes_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_ALPHA_A_Z, json.optString("sort_alpha_a_z").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_ALPHA_Z_A, json.optString("sort_alpha_z_a").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.AMOUNT, json.optString("amount").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.COUPON_CANCELLED, json.optString("coupon_cancelled").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BUTTON_APPLY, json.optString("btn_apply").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_OUT_WARNING, json.optString("sign_out_warning").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DISCOUNT_ON_COUPON, json.optString("discount_on_coupon").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_CVV_HINT, json.optString("credit_card_cvv_hint").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CAST, json.optString("cast").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CAST_CREW_BUTTON_TITLE, json.optString("cast_crew_button_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CENSOR_RATING, json.optString("censor_rating").trim());


                    if (json.optString("change_password").trim() == null || json.optString("change_password").trim().equals("")) {
                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
                    } else {
                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
                    }
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CONFIRM_PASSWORD, json.optString("confirm_password").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DIRECTOR, json.optString("director").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DESCRIPTION, json.optString("description").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EMAIL_EXISTS, json.optString("email_exists").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EMAIL_DOESNOT_EXISTS, json.optString("email_does_not_exist").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EMAIL_PASSWORD_INVALID, json.optString("email_password_invalid").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.COUPON_CODE_HINT, json.optString("coupon_code_hint").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SEARCH_ALERT, json.optString("search_alert").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_NUMBER_HINT, json.optString("credit_card_number_hint").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TEXT_EMIAL, json.optString("text_email").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NAME_HINT, json.optString("name_hint").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_NAME_HINT, json.optString("credit_card_name_hint").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TEXT_PASSWORD, json.optString("text_password").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ERROR_IN_SUBSCRIPTION, json.optString("error_in_subscription").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ERROR_IN_PAYMENT_VALIDATION, json.optString("error_in_payment_validation").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ERROR_IN_REGISTRATION, json.optString("error_in_registration").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS_EXPIRED, json.optString("transaction_status_expired").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DETAILS_NOT_FOUND_ALERT, json.optString("details_not_found_alert").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FAILURE, json.optString("failure").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FILTER_BY, json.optString("filter_by").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FORGOT_PASSWORD, json.optString("forgot_password").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.GENRE, json.optString("genre").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.AGREE_TERMS, json.optString("agree_terms").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.INVALID_COUPON, json.optString("invalid_coupon").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.INVOICE, json.optString("invoice").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LANGUAGE_POPUP_LANGUAGE, json.optString("language_popup_language").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_LAST_UPLOADED, json.optString("sort_last_uploaded").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LANGUAGE_POPUP_LOGIN, json.optString("language_popup_login").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGIN, json.optString("login").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGOUT, json.optString("logout").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGOUT_SUCCESS, json.optString("logout_success").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.MY_FAVOURITE, json.optString("my_favourite").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NEW_PASSWORD, json.optString("new_password").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NEW_HERE_TITLE, json.optString("new_here_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO, json.optString("no").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_DATA, json.optString("no_data").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, json.optString("no_internet_connection").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_INTERNET_NO_DATA, json.optString("no_internet_no_data").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, json.optString("no_details_available").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BUTTON_OK, json.optString("btn_ok").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.OLD_PASSWORD, json.optString("old_password").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.OOPS_INVALID_EMAIL, json.optString("oops_invalid_email").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ORDER, json.optString("order").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_DETAILS_ORDER_ID, json.optString("transaction_detail_order_id").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PASSWORD_RESET_LINK, json.optString("password_reset_link").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PASSWORDS_DO_NOT_MATCH, json.optString("password_donot_match").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PAY_BY_PAYPAL, json.optString("pay_by_paypal").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BTN_PAYNOW, json.optString("btn_paynow").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PAY_WITH_CREDIT_CARD, json.optString("pay_with_credit_card").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PAYMENT_OPTIONS_TITLE, json.optString("payment_options_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PLAN_NAME, json.optString("plan_name").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, json.optString("activate_subscription_watch_video").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.COUPON_ALERT, json.optString("coupon_alert").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VALID_CONFIRM_PASSWORD, json.optString("valid_confirm_password").trim());
                    // Util.setLanguageSharedPrefernce(Episode_list_Activity.this,Util.EMAIL_REQUIRED,json.optString("email_required").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PROFILE, json.optString("profile").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PROFILE_UPDATED, json.optString("profile_updated").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PURCHASE, json.optString("purchase").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_DETAIL_PURCHASE_DATE, json.optString("transaction_detail_purchase_date").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PURCHASE_HISTORY, json.optString("purchase_history").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BTN_REGISTER, json.optString("btn_register").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_RELEASE_DATE, json.optString("sort_release_date").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SAVE_THIS_CARD, json.optString("save_this_card").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TEXT_SEARCH_PLACEHOLDER, json.optString("text_search_placeholder").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SEASON, json.optString("season").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECT_OPTION_TITLE, json.optString("select_option_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECT_PLAN, json.optString("select_plan").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_UP_TITLE, json.optString("signup_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SLOW_INTERNET_CONNECTION, json.optString("slow_internet_connection").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SLOW_ISSUE_INTERNET_CONNECTION, json.optString("slow_issue_internet_connection").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORRY, json.optString("sorry").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.GEO_BLOCKED_ALERT, json.optString("geo_blocked_alert").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, json.optString("sign_out_error").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ALREADY_PURCHASE_THIS_CONTENT, json.optString("already_purchase_this_content").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CROSSED_MAXIMUM_LIMIT, json.optString("crossed_max_limit_of_watching").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_BY, json.optString("sort_by").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.STORY_TITLE, json.optString("story_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BTN_SUBMIT, json.optString("btn_submit").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS, json.optString("transaction_success").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VIDEO_ISSUE, json.optString("video_issue").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_CONTENT, json.optString("no_content").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, json.optString("no_video_available").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, json.optString("content_not_available_in_your_country").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_DATE, json.optString("transaction_date").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANASCTION_DETAIL, json.optString("transaction_detail").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS, json.optString("transaction_status").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION, json.optString("transaction").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRY_AGAIN, json.optString("try_again").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.UNPAID, json.optString("unpaid").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.USE_NEW_CARD, json.optString("use_new_card").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VIEW_MORE, json.optString("view_more").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VIEW_TRAILER, json.optString("view_trailer").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.WATCH, json.optString("watch").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.WATCH_NOW, json.optString("watch_now").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_OUT_ALERT, json.optString("sign_out_alert").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.UPDATE_PROFILE_ALERT, json.optString("update_profile_alert").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.YES, json.optString("yes").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PURCHASE_SUCCESS_ALERT, json.optString("purchase_success_alert").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CARD_WILL_CHARGE, json.optString("card_will_charge").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SEARCH_HINT, json.optString("search_hint").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TERMS, json.optString("terms").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.APP_ON, json.optString("app_on").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());


                    // Added Later

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_PDF, json.optString("no_pdf").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DOWNLOAD_INTERRUPTED, json.optString("download_interrupted").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DOWNLOAD_COMPLETED, json.optString("download_completed").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_TITLE, json.optString("transaction_title").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FREE, json.optString("free").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SUBSCRIPTION_COMPLETED, json.optString("subscription completed").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CVV_ALERT, json.optString("cvv_alert").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());

                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.MESSAGE, json.optString("text_message").trim());
                    Util.getTextofLanguage(Episode_list_Activity.this, Util.PURCHASE, Util.DEFAULT_PURCHASE);
                    Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Default_Language);

                    //Call For Language PopUp Dialog

                    languageCustomAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(Episode_list_Activity.this, MainActivity.class);
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
//                httppost.addHeader("authToken", Util.authTokenStr);
//                httppost.addHeader("lang_code", Default_Language);
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
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ALREADY_MEMBER, json.optString("already_member").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ACTIAVTE_PLAN_TITLE, json.optString("activate_plan_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS_ACTIVE, json.optString("transaction_status_active").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ADD_TO_FAV, json.optString("add_to_fav").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ADDED_TO_FAV, json.optString("added_to_fav").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ADVANCE_PURCHASE, json.optString("advance_purchase").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ALERT, json.optString("alert").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EPISODE_TITLE, json.optString("episodes_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_ALPHA_A_Z, json.optString("sort_alpha_a_z").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_ALPHA_Z_A, json.optString("sort_alpha_z_a").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.AMOUNT, json.optString("amount").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.COUPON_CANCELLED, json.optString("coupon_cancelled").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BUTTON_APPLY, json.optString("btn_apply").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_OUT_WARNING, json.optString("sign_out_warning").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DISCOUNT_ON_COUPON, json.optString("discount_on_coupon").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_CVV_HINT, json.optString("credit_card_cvv_hint").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CAST, json.optString("cast").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CAST_CREW_BUTTON_TITLE, json.optString("cast_crew_button_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CENSOR_RATING, json.optString("censor_rating").trim());
//
//
//                        if (json.optString("change_password").trim() == null || json.optString("change_password").trim().equals("")) {
//                            Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
//                        } else {
//                            Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
//                        }
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CONFIRM_PASSWORD, json.optString("confirm_password").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DIRECTOR, json.optString("director").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DESCRIPTION, json.optString("description").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EMAIL_EXISTS, json.optString("email_exists").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EMAIL_DOESNOT_EXISTS, json.optString("email_does_not_exist").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.EMAIL_PASSWORD_INVALID, json.optString("email_password_invalid").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.COUPON_CODE_HINT, json.optString("coupon_code_hint").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SEARCH_ALERT, json.optString("search_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_NUMBER_HINT, json.optString("credit_card_number_hint").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TEXT_EMIAL, json.optString("text_email").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NAME_HINT, json.optString("name_hint").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CREDIT_CARD_NAME_HINT, json.optString("credit_card_name_hint").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TEXT_PASSWORD, json.optString("text_password").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ERROR_IN_SUBSCRIPTION, json.optString("error_in_subscription").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ERROR_IN_PAYMENT_VALIDATION, json.optString("error_in_payment_validation").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ERROR_IN_REGISTRATION, json.optString("error_in_registration").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS_EXPIRED, json.optString("transaction_status_expired").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DETAILS_NOT_FOUND_ALERT, json.optString("details_not_found_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FAILURE, json.optString("failure").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FILTER_BY, json.optString("filter_by").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FORGOT_PASSWORD, json.optString("forgot_password").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.GENRE, json.optString("genre").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.AGREE_TERMS, json.optString("agree_terms").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.INVALID_COUPON, json.optString("invalid_coupon").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.INVOICE, json.optString("invoice").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LANGUAGE_POPUP_LANGUAGE, json.optString("language_popup_language").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_LAST_UPLOADED, json.optString("sort_last_uploaded").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LANGUAGE_POPUP_LOGIN, json.optString("language_popup_login").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGIN, json.optString("login").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGOUT, json.optString("logout").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGOUT_SUCCESS, json.optString("logout_success").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.MY_FAVOURITE, json.optString("my_favourite").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NEW_PASSWORD, json.optString("new_password").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NEW_HERE_TITLE, json.optString("new_here_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO, json.optString("no").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_DATA, json.optString("no_data").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_INTERNET_CONNECTION, json.optString("no_internet_connection").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_INTERNET_NO_DATA, json.optString("no_internet_no_data").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_DETAILS_AVAILABLE, json.optString("no_details_available").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BUTTON_OK, json.optString("btn_ok").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.OLD_PASSWORD, json.optString("old_password").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.OOPS_INVALID_EMAIL, json.optString("oops_invalid_email").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ORDER, json.optString("order").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_DETAILS_ORDER_ID, json.optString("transaction_detail_order_id").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PASSWORD_RESET_LINK, json.optString("password_reset_link").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PASSWORDS_DO_NOT_MATCH, json.optString("password_donot_match").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PAY_BY_PAYPAL, json.optString("pay_by_paypal").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BTN_PAYNOW, json.optString("btn_paynow").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PAY_WITH_CREDIT_CARD, json.optString("pay_with_credit_card").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PAYMENT_OPTIONS_TITLE, json.optString("payment_options_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PLAN_NAME, json.optString("plan_name").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, json.optString("activate_subscription_watch_video").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.COUPON_ALERT, json.optString("coupon_alert").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VALID_CONFIRM_PASSWORD, json.optString("valid_confirm_password").trim());
//                        // Util.setLanguageSharedPrefernce(Episode_list_Activity.this,Util.EMAIL_REQUIRED,json.optString("email_required").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PROFILE, json.optString("profile").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PROFILE_UPDATED, json.optString("profile_updated").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PURCHASE, json.optString("purchase").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_DETAIL_PURCHASE_DATE, json.optString("transaction_detail_purchase_date").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PURCHASE_HISTORY, json.optString("purchase_history").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BTN_REGISTER, json.optString("btn_register").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_RELEASE_DATE, json.optString("sort_release_date").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SAVE_THIS_CARD, json.optString("save_this_card").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TEXT_SEARCH_PLACEHOLDER, json.optString("text_search_placeholder").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SEASON, json.optString("season").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECT_OPTION_TITLE, json.optString("select_option_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECT_PLAN, json.optString("select_plan").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_UP_TITLE, json.optString("signup_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SLOW_INTERNET_CONNECTION, json.optString("slow_internet_connection").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SLOW_ISSUE_INTERNET_CONNECTION, json.optString("slow_issue_internet_connection").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORRY, json.optString("sorry").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.GEO_BLOCKED_ALERT, json.optString("geo_blocked_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, json.optString("sign_out_error").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.ALREADY_PURCHASE_THIS_CONTENT, json.optString("already_purchase_this_content").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CROSSED_MAXIMUM_LIMIT, json.optString("crossed_max_limit_of_watching").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SORT_BY, json.optString("sort_by").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.STORY_TITLE, json.optString("story_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.BTN_SUBMIT, json.optString("btn_submit").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS, json.optString("transaction_success").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VIDEO_ISSUE, json.optString("video_issue").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_CONTENT, json.optString("no_content").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_VIDEO_AVAILABLE, json.optString("no_video_available").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, json.optString("content_not_available_in_your_country").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_DATE, json.optString("transaction_date").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANASCTION_DETAIL, json.optString("transaction_detail").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_STATUS, json.optString("transaction_status").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION, json.optString("transaction").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRY_AGAIN, json.optString("try_again").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.UNPAID, json.optString("unpaid").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.USE_NEW_CARD, json.optString("use_new_card").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VIEW_MORE, json.optString("view_more").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.VIEW_TRAILER, json.optString("view_trailer").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.WATCH, json.optString("watch").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.WATCH_NOW, json.optString("watch_now").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIGN_OUT_ALERT, json.optString("sign_out_alert").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.UPDATE_PROFILE_ALERT, json.optString("update_profile_alert").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.YES, json.optString("yes").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.PURCHASE_SUCCESS_ALERT, json.optString("purchase_success_alert").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CARD_WILL_CHARGE, json.optString("card_will_charge").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SEARCH_HINT, json.optString("search_hint").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TERMS, json.optString("terms").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.APP_ON, json.optString("app_on").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
//
//
//                        // Added Later
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.NO_PDF, json.optString("no_pdf").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DOWNLOAD_INTERRUPTED, json.optString("download_interrupted").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.DOWNLOAD_COMPLETED, json.optString("download_completed").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.TRANSACTION_TITLE, json.optString("transaction_title").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FREE, json.optString("free").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SUBSCRIPTION_COMPLETED, json.optString("subscription completed").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.CVV_ALERT, json.optString("cvv_alert").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
//
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.MESSAGE, json.optString("text_message").trim());
//                        Util.getTextofLanguage(Episode_list_Activity.this, Util.PURCHASE, Util.DEFAULT_PURCHASE);
//                        Util.setLanguageSharedPrefernce(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Default_Language);
//
//                        //Call For Language PopUp Dialog
//
//                        languageCustomAdapter.notifyDataSetChanged();
//
//                        Intent intent = new Intent(Episode_list_Activity.this, MainActivity.class);
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
//            progressBarHandler = new ProgressBarHandler(Episode_list_Activity.this);
//            progressBarHandler.show();
//        }
//    }


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
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("login_history_id", loginHistoryIdStr);
//                httppost.addHeader("lang_code", Util.getTextofLanguage(Episode_list_Activity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
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
//                            Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
//                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseStr == null) {
//                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode == 0) {
//                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
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
//                    if ((Util.getTextofLanguage(Episode_list_Activity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
//                            .trim()).equals("1")) {
//                        final Intent startIntent = new Intent(Episode_list_Activity.this, SplashScreen.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(startIntent);
//                                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
//                                finish();
//
//                            }
//                        });
//                    } else {
//                        final Intent startIntent = new Intent(Episode_list_Activity.this, MainActivity.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(startIntent);
//                                Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
//                                finish();
//
//                            }
//                        });
//                    }
//
//                } else {
//                    Toast.makeText(Episode_list_Activity.this, Util.getTextofLanguage(Episode_list_Activity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            pDialog = new ProgressBarHandler(Episode_list_Activity.this);
//            pDialog.show();
//        }
//    }

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

    @Override
    public void onResume() {

        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }


        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        invalidateOptionsMenu();

        super.onResume();

    }

    private void updateMetadata(boolean visible) {
        Point displaySize;
        if (!visible) {
            /*mDescriptionView.setVisibility(View.GONE);
            mTitleView.setVisibility(View.GONE);
            mAuthorView.setVisibility(View.GONE);*/
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    displaySize.y + getSupportActionBar().getHeight());
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
        } else {
            //MediaMetadata mm = mSelectedMedia.getMetadata();
          /*  mDescriptionView.setText(mSelectedMedia.getCustomData().optString(
                    VideoProvider.KEY_DESCRIPTION));
            //mTitleView.setText(mm.getString(MediaMetadata.KEY_TITLE));
            //mAuthorView.setText(mm.getString(MediaMetadata.KEY_SUBTITLE));
            mDescriptionView.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.VISIBLE);
            mAuthorView.setVisibility(View.VISIBLE);*/
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

                        loadRemoteMedia(0, true);

                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;

                mVideoView.pause();
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
                            loadRemoteMedia(0, true);


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

                Intent intent = new Intent(Episode_list_Activity.this, ExpandedControlsActivity.class);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want

                        boolean isNetwork = Util.checkNetwork(Episode_list_Activity.this);
                        if (isNetwork == true) {
                            asynEpisodeDetails = new AsynEpisodeDetails();
                            asynEpisodeDetails.executeOnExecutor(threadPoolExecutor);
                        } else {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
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
                        LogUtil.showLog("App", "failed to create directory");
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
            LogUtil.showLog("MUVI", "Download Completed");
            FakeSubTitlePath.remove(0);
            if (FakeSubTitlePath.size() > 0) {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            } else {
                if (progressBarHandler != null && progressBarHandler.isShowing()) {
                    progressBarHandler.hide();
                }
                Intent playVideoIntent;
                if (Util.goToLibraryplayer) {
                    playVideoIntent = new Intent(Episode_list_Activity.this, MyLibraryPlayer.class);

                } else {
                    playVideoIntent = new Intent(Episode_list_Activity.this, ExoPlayerActivity.class);

                }
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);
                startActivity(playVideoIntent);
            }
        }
    }

}
