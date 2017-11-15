package com.home.vod.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.crashlytics.android.Crashlytics;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
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
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetRelatedContentAsynTask;
import com.home.apisdk.apiController.GetRelatedContentListAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.GetVideoLogsAsynTask;
import com.home.apisdk.apiController.HeaderConstants;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.RelatedContentInput;
import com.home.apisdk.apiModel.RelatedContentListInput;
import com.home.apisdk.apiModel.RelatedContentListOutput;
import com.home.apisdk.apiModel.RelatedContentOutput;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.apisdk.apiModel.VideoLogsInputModel;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.vod.BuildConfig;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.MyDownloadIntentHandler;
import com.home.vod.R;
import com.home.vod.SearchIntentHandler;
import com.home.vod.adapter.ProgramDetailsAdapter;
import com.home.vod.adapter.RelatedContentListDataAdapter;
import com.home.vod.adapter.VideoFilterAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.model.GridItem;
import com.home.vod.model.LanguageModel;
import com.home.vod.model.RelatedContentListItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.ResizableCustomView;
import com.home.vod.util.SensorOrientationChangeNotifier;
import com.home.vod.util.Util;
import com.intertrust.wasabi.ErrorCodeException;
import com.intertrust.wasabi.Runtime;
import com.intertrust.wasabi.media.PlaylistProxy;
import com.intertrust.wasabi.media.PlaylistProxyListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.Fabric;
import player.activity.ExoPlayerActivity;
import player.activity.Player;
import player.adapter.DownloadOptionAdapter;
import player.model.ContactModel1;
import player.model.SubtitleModel;
import player.service.PopUpService;
import player.utils.DBHelper;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.vod.preferences.LanguagePreference.BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MORE_VIDEOS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_REPETITION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DETAILS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.MORE_VIDEOS;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.REPETITION_TITLE;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.languageModel;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.timer;

/**
 * Created by MUVI on 10/6/2017.
 *
 * @author Abhishek
 */
enum ContentTypesYoga {
    DASH("application/dash+xml"), HLS("application/vnd.apple.mpegurl"), PDCF(
            "video/mp4"), M4F("video/mp4"), DCF("application/vnd.oma.drm.dcf"), BBTS(
            "video/mp2t");
    String mediaSourceParamsContentType = null;

    private ContentTypesYoga(String mediaSourceParamsContentType) {
        this.mediaSourceParamsContentType = mediaSourceParamsContentType;
    }

    public String getMediaSourceParamsContentType() {
        return mediaSourceParamsContentType;
    }
}

public class YogaPlayerActivity extends AppCompatActivity implements PlaylistProxyListener, SensorOrientationChangeNotifier.Listener, GetRelatedContentAsynTask.GetRelatedContentListener, GetContentDetailsAsynTask.GetContentDetailsListener, DeleteFavAsync.DeleteFavListener, AddToFavAsync.AddToFavListener,
        GetIpAddressAsynTask.IpAddressListener, GetLanguageListAsynTask.GetLanguageListListener, GetRelatedContentListAsynTask.GetRelatedContentListListener, GetValidateUserAsynTask.GetValidateUserListener, VideoDetailsAsynctask.VideoDetailsListener {
    PlaylistProxy playerProxy;
    GetValidateUserAsynTask asynValidateUserDetails = null;
    VideoDetailsAsynctask asynLoadVideoUrls = null;

    TextView detailsTextView, colortitle, colortitle1, benefitsTitleTextView, benefitsStoryTextView, durationTitleTextView, diffcultyTitleTextView, difficulty, days, lineTextview, repetitionTitleTextView, repetitionTextView, lineTextview1;
    ImageView bannerImageView, playButton, moviePoster, share;

    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout, image_logo;
    LinearLayout story_layout;
    String movieUniqueId = "", movieGenre = "";
    String movieTrailerUrlStr = "", isEpisode = "";
    String duration = "";
    String[] season;
    String name;
    String difficulty_level;
    String repetition;
    String email, id;
    String ipAddres = "";
    String movieDetailsStr = "";
    String benefits = "";
    String useridStr;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    String movieReleaseDateStr = "";
    PreferenceManager preferenceManager;
    ImageView favorite_view_episode;
    Toolbar mActionBarToolbar;
    static String _permalink;
    String sucessMsg;
    String Default_Language = "";
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    String loggedInStr;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    LanguagePreference languagePreference;
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    String bannerImageId, posterImageId, permalinkStr, contentIdStr, contentStreamIdStr;
    String videoDurationStr = "";
    boolean castStr = false;
    int isFavorite;

    ///////////////////////////////////////////////////////////////////
    /*
    @params-for player by nihar

     */
    ContentDetailsOutput contentDetailsOutputModel;
    DataModel dbModel = new DataModel();
    Player playerModel;
    ArrayList<String> SubTitleName;
    ArrayList<String> SubTitlePath;
    ArrayList<String> ResolutionFormat;
    ArrayList<String> ResolutionUrl;
    ArrayList<String> SubTitleLanguage = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ProgressBarHandler pDialog;
    ProgressBarHandler progressBarHandler;
    String licensetoken;
    ImageView downloadImageView;
    int player_start_time = 0;
    int player_end_time = 0;
    String log_temp_id = "0";

    //////////download///////

    ArrayList<String> List_Of_FileSize = new ArrayList<>();
    ArrayList<String> List_Of_Resolution_Format = new ArrayList<>();
    ArrayList<String> List_Of_Resolution_Url = new ArrayList<>();
    ArrayList<String> List_Of_Resolution_Url_Used_For_Download = new ArrayList<>();

    ProgressBarHandler pDialog_for_gettig_filesize;
    AlertDialog alert;
    int selected_download_format = 0;
    String Dwonload_Complete_Msg = "";
    int seekBarProgress = 0;

    boolean isDrm = false;


    /*****
     * offline
     *****/
    DownloadManager downloadManager;
    RelativeLayout download_layout;
    public boolean downloading;
    //Handler mHandler;
    static String filename, path;
    ArrayList<ContactModel1> dmanager;
    YogaPlayerActivity.AsynWithdrm asynWithdrm;
    ContactModel1 audio, audio_1;
    DBHelper dbHelper;
    public Handler exoplayerdownloadhandler;
    public long enqueue;
    ImageView download;
    ProgressBar Progress;
    TextView percentg;
    private static final int REQUEST_STORAGE = 1;
    File mediaStorageDir, mediaStorageDir1;

    String mlvfile = "";
    String token = "";
    String fname;
    String fileExtenstion;
    int lenghtOfFile;
    int lengthfile;
    /*****
     * offline
     *****/
    ///////////////////////////////////////////////////////////////////

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
    private ImageButton mPlayCircle;
    private String movieUrl;
    private String emailIdStr = "";
    private String userIdStr;
    private int content_types_id;
    private String ipAddressStr;


    int playerPosition = 0;
    TimerTask timerTask;

    int current_played_length = 0;
    long cast_disconnected_position = 0;
    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        ipAddres = ipAddressStr;
        return;
    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {

        pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();

        for (int i = 0; i < languageListOutputArray.size(); i++) {
            String language_id = languageListOutputArray.get(i).getLanguageCode();
            String language_name = languageListOutputArray.get(i).getLanguageName();


            LanguageModel languageModel = new LanguageModel();
            languageModel.setLanguageId(language_id);
            languageModel.setLanguageName(language_name);

            if (Default_Language.equalsIgnoreCase(language_id)) {
                languageModel.setIsSelected(true);
            } else {
                languageModel.setIsSelected(false);
            }
            languageModels.add(languageModel);
        }

        languageModel = languageModels;
    }

    @Override
    public void onOrientationChange(int orientation) {


        if (orientation == 90) {
            compressed = false;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            player_layout.setLayoutParams(params);
            compress_expand.setImageResource(R.drawable.ic_media_fullscreen_shrink);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            hideSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            //current_time.setVisibility(View.GONE);
        } else if (orientation == 270) {


            compressed = false;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            player_layout.setLayoutParams(params);
            compress_expand.setImageResource(R.drawable.ic_media_fullscreen_shrink);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            hideSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //current_time.setVisibility(View.GONE);

            // Do some landscape stuff
        } else if (orientation == 180) {

            RelativeLayout.LayoutParams params1 = null;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);

                } else {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                }
            } else {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

                } else {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
                }
            }
            player_layout.setLayoutParams(params1);
            compressed = true;
            compress_expand.setImageResource(R.drawable.ic_media_fullscreen_stretch);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            showSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //current_time.setVisibility(View.GONE);

        } else if (orientation == 0) {


            RelativeLayout.LayoutParams params1 = null;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);

                } else {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);
                }
            } else {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

                } else {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
                }
            }
            player_layout.setLayoutParams(params1);
            compressed = true;
            compress_expand.setImageResource(R.drawable.ic_media_fullscreen_stretch);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            showSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //current_time.setVisibility(View.GONE);
        }

        current_time_position_timer();

    }

    @Override
    public void onGetRelatedContentListPreExecuteStarted() {

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
                relatedContentList.setVisibility(View.VISIBLE);
                itemData.clear();


                if (relatedContentListOutputArray.size() > 0) {

                    relatedContentList.setVisibility(View.VISIBLE);
                    tutorialRelativeLayout.setVisibility(View.VISIBLE);


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
                        itemData.add(new RelatedContentListItem(videoImageStr, videoName, "", videoTypeIdStr, videoGenreStr, "", videoPermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV, "", relatedContentListOutputArray.get(i).getContentId(), relatedContentListOutputArray.get(i).getContentStreamId()));
                    }


                    if (itemData.size() <= 0) {


                        //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
                    } else {


                        LogUtil.showLog("BISHAL", "data show...");
                        relatedContentList.setVisibility(View.VISIBLE);
                        relatedContentList.setLayoutManager(mLayoutManager);
                        relatedContentList.setItemAnimator(new DefaultItemAnimator());
                        RelatedContentListDataAdapter mAdapter = new RelatedContentListDataAdapter(YogaPlayerActivity.this, R.layout.list_card_program_details, itemData);


                        relatedContentList.setAdapter(mAdapter);

                    }

                } else {

                    relatedContentList.setVisibility(View.GONE);
                    tutorialRelativeLayout.setVisibility(View.GONE);


                }
            } else {

                relatedContentList.setVisibility(View.GONE);
                tutorialRelativeLayout.setVisibility(View.GONE);


            }
        } else {


            relatedContentList.setVisibility(View.GONE);
            tutorialRelativeLayout.setVisibility(View.GONE);


        }


    }

    @Override
    public void onErrorNotification(int i, String s) {

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

    RemoteMediaClient remoteMediaClient;

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
    String contentId, muviStreamId;

    /*--------- Added for trailer player */

    private EMVideoView emVideoView;
    ImageView subtitle_change_btn;
    RelativeLayout player_layout;
    ImageButton latest_center_play_pause;
    ProgressBar progressView;
    ImageButton back, center_play_pause;
    ImageView compress_expand;
    SeekBar seekBar;
    String Current_Time, TotalTime;
    TextView current_time, total_time;
    TextView ipAddressTextView;
    TextView emailAddressTextView;
    TextView dateTextView;
    long previous_matching_time = 0, current_matching_time = 0;
    public int playerPreviousPosition = 0;
    int playerStartPosition = 0;
    int seek_label_pos = 0;
    RecyclerView relatedContentList;
    RelativeLayout tutorialRelativeLayout;
    ArrayList<RelatedContentListItem> itemData = new ArrayList<RelatedContentListItem>();
    Timer center_pause_paly_timer;
    boolean center_pause_paly_timer_is_running = false;
    public boolean isFastForward = false;
    boolean video_completed = false;
    boolean video_prepared = false;
    int screenWidth, screenHeight;
    LinearLayout primary_ll, last_ll;
    boolean compressed = true;
    TextView clocktime;
    LinearLayout benefitsLinearLayout;
    RelativeLayout durationRelativeLayout;

    TextView moreVideosTextView;

    /* Added for trailer player ---------*/
    ///by nihar
    LinearLayout view_below;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_yoga_player);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pDialog != null) {
                    pDialog.hide();

                }
                onBackPressed();
            }
        });

        playerModel = new Player();
        dbHelper = new DBHelper(YogaPlayerActivity.this);
        dbHelper.getWritableDatabase();
        if (playerModel != null && playerModel.getEmailId() != null && !playerModel.getEmailId().trim().matches("")) {
            emailIdStr = playerModel.getEmailId();
        }
        //start-----
        SubTitleName = new ArrayList<>();
        SubTitlePath = new ArrayList<>();
        ResolutionFormat = new ArrayList<>();
        ResolutionUrl = new ArrayList<>();
        //------end


        preferenceManager = PreferenceManager.getPreferenceManager(this);


        if (preferenceManager != null) {
            emailIdStr = preferenceManager.getEmailIdFromPref();

        } else {
            emailIdStr = "";


        }

        contentIdStr = getIntent().getStringExtra("CONTENT_ID");
        contentStreamIdStr = getIntent().getStringExtra("CONTENT_STREAM_ID");
        languagePreference = LanguagePreference.getLanguagePreference(YogaPlayerActivity.this);
        playButton = (ImageView) findViewById(R.id.playButton);
        detailsTextView = (TextView) findViewById(R.id.detailsTextView);
        difficulty = (TextView) findViewById(R.id.difficulty);
        repetitionTextView = (TextView) findViewById(R.id.repetition);
        lineTextview = (TextView) findViewById(R.id.lineTextview);
        lineTextview1 = (TextView) findViewById(R.id.lineTextview1);
        lineTextview1.setVisibility(View.GONE);

        moreVideosTextView = (TextView) findViewById(R.id.tutorialTextView);
        clocktime = (TextView) findViewById(R.id.clocktime);
        durationRelativeLayout = (RelativeLayout) findViewById(R.id.durationRelativeLayout);

        days = (TextView) findViewById(R.id.days);
        tutorialRelativeLayout = (RelativeLayout) findViewById(R.id.tutorialRelativeLayout);

        benefitsTitleTextView = (TextView) findViewById(R.id.benefitsTitleTextView);
        colortitle = (TextView) findViewById(R.id.colortitle);
        colortitle1 = (TextView) findViewById(R.id.colortitle1);
        benefitsStoryTextView = (TextView) findViewById(R.id.benefitsStoryTextView);
        durationTitleTextView = (TextView) findViewById(R.id.durationTitleTextView);
        diffcultyTitleTextView = (TextView) findViewById(R.id.diffcultyTitleTextView);
        repetitionTitleTextView = (TextView) findViewById(R.id.repetitionTitleTextView);
        favorite_view_episode = (ImageView) findViewById(R.id.favoriteImageView);
        relatedContentList = (RecyclerView) findViewById(R.id.featureContent);
        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        share = (ImageView) findViewById(R.id.share);
        image_logo = (RelativeLayout) findViewById(R.id.logo_image);
        benefitsLinearLayout = (LinearLayout) findViewById(R.id.benefitsLinearLayout);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        player_layout = (RelativeLayout) findViewById(R.id.player_layout);
        episodeListOptionMenuHandler = new EpisodeListOptionMenuHandler(this);
        colortitle.setVisibility(View.GONE);
        colortitle1.setVisibility(View.GONE);
        lineTextview.setVisibility(View.GONE);


        FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), moreVideosTextView);
        moreVideosTextView.setText(languagePreference.getTextofLanguage(MORE_VIDEOS, DEFAULT_MORE_VIDEOS));

        mLayoutManager = new LinearLayoutManager(YogaPlayerActivity.this, LinearLayoutManager.HORIZONTAL, false);
        if (((YogaPlayerActivity.this.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((YogaPlayerActivity.this.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {


            relatedContentList.addItemDecoration(new SpacesItemDecoration(30));

        } else {

            relatedContentList.addItemDecoration(new SpacesItemDecoration(50));


        }
        relatedContentList.addOnItemTouchListener(new RecyclerTouchListener(YogaPlayerActivity.this,
                relatedContentList, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (asynValidateUserDetails != null) {
                    asynValidateUserDetails.cancel(true);
                }
                if (asynLoadVideoUrls != null) {
                    asynLoadVideoUrls.cancel(true);
                }


                if (emVideoView != null && emVideoView.isPlaying()) {
                    emVideoView.stopPlayback();

                }
            /*    End_Timer();
               *//* if (emVideoView.isPlaying()){
                    emVideoView.stopPlayback();

                }*//*
                if(emVideoView!=null) {
                    emVideoView.stopPlayback();
                }*/
                mHandler.removeCallbacks(updateTimeTask);

                seekBar.setProgress(0);
//                    emVideoView.seekTo(0);
                current_time.setText("00:00:00");
                total_time.setText("00:00:00");
                previous_matching_time = 0;
                current_matching_time = 0;
                bannerImageRelativeLayout.setVisibility(View.VISIBLE);
                player_layout.setVisibility(View.GONE);


//                Log.v("Subhalaxmi", "video done complted");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                });
                showSystemUI();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                RelatedContentListItem item = itemData.get(position);
                //Values are passing to activity & to fragment as well
               /* EpisodesListModel item = itemData.get(position);
                clickItem(item, position);*/
                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                permalinkStr = item.getPermalink();
                contentIdStr = item.getContentId();
                contentStreamIdStr = item.getContentStreamId();
                useridStr = preferenceManager.getUseridFromPref();

                contentDetailsInput.setAuthToken(authTokenStr);

                Log.v("SUBHA", "authToken === " + authTokenStr);
                if (preferenceManager != null) {
                    String countryPref = preferenceManager.getCountryCodeFromPref();
                    contentDetailsInput.setCountry(countryPref);
                } else {
                    contentDetailsInput.setCountry("IN");
                }
                contentDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                contentDetailsInput.setPermalink(permalinkStr);
                contentDetailsInput.setUser_id(useridStr);
                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, YogaPlayerActivity.this, YogaPlayerActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            }

            @Override
            public void onLongClick(View view, int position) {

                return;
            }
        }));

        /********* Offline********/


        if (playerModel != null && playerModel.getUserId() != null && !playerModel.getUserId().trim().matches("")) {
            userIdStr = playerModel.getUserId();
        }
        if (playerModel != null && playerModel.getEmailId() != null && !playerModel.getEmailId().trim().matches("")) {
            emailIdStr = playerModel.getEmailId();
        }

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


        download = (ImageView) findViewById(R.id.downloadImageView);
        Progress = (ProgressBar) findViewById(R.id.progressBar);
        percentg = (TextView) findViewById(R.id.percentage);

        //  content_types_id = playerModel.getContentTypesId();
        //Check for offline content // Added By sanjay
//        mediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        download_layout = (RelativeLayout) findViewById(R.id.downloadRelativeLayout);
       /* if (content_types_id!=4 && playerModel.getIsOffline().equals("1") && playerModel.getDownloadStatus().equals("1")) {
            download_layout.setVisibility(View.VISIBLE);
        }*/
        download_layout.setVisibility(View.GONE);

        /*if (content_types_id != 4) {
            download_layout.setVisibility(View.VISIBLE);
        }*/


        /********* Offline ********/
        //added for player functionality
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(YogaPlayerActivity.this)) {
                    final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    try {
                        player.utils.Util.call_finish_at_onUserLeaveHint = false;
                        startActivityForResult(intent, 22222);
                    } catch (ActivityNotFoundException e) {
                    }
                } else {

                    if (isDrm) {
                        // This is applicable for DRM content.

                        List_Of_Resolution_Format.clear();
                        List_Of_FileSize.clear();
                        List_Of_Resolution_Url.clear();
                        List_Of_Resolution_Url_Used_For_Download.clear();


                        asynWithdrm = new AsynWithdrm();
                        asynWithdrm.executeOnExecutor(threadPoolExecutor);
                    } else {
                        // This is applicable for NON-DRM contnet.

                        List_Of_Resolution_Url.clear();
                        List_Of_FileSize.clear();
                        if (List_Of_Resolution_Url.size() > 0) {
                            for (int i = 1; i < List_Of_Resolution_Url.size(); i++) {
                                List_Of_Resolution_Url.add(playerModel.ResolutionUrl.get(i));
                            }

                            pDialog_for_gettig_filesize = new ProgressBarHandler(YogaPlayerActivity.this);
                            pDialog_for_gettig_filesize.show();

                            new DetectDownloadingFileSize().execute();
                        } else {
                            new DownloadFileFromURL().execute(playerModel.getVideoUrl());
                        }

                    }


                    if (playerModel.getOfflineUrl().size() > 0) {
                        Download_SubTitle(playerModel.getOfflineUrl().get(0));
                    }
                }
            }
        });



        /*relatedContentList.addOnItemTouchListener(new ProgramDetailsActivity.RecyclerTouchListener(this,
                relatedContentList, new ProgramDetailsActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                EpisodesListModel item = itemData.get(position);
                clickItem(item, position);
            }

            @Override
            public void onLongClick(View view, int position) {

                return;
            }
        }));*/

         /*-------------Added for trailer ---------------*/
        bannerImageRelativeLayout.setVisibility(View.VISIBLE);

        emVideoView = (EMVideoView) findViewById(R.id.emVideoView);
        subtitle_change_btn = (ImageView) findViewById(R.id.subtitle_change_btn);
        subtitle_change_btn.setVisibility(View.GONE);
        latest_center_play_pause = (ImageButton) findViewById(R.id.latest_center_play_pause);
        progressView = (ProgressBar) findViewById(R.id.progress_view);
        primary_ll = (LinearLayout) findViewById(R.id.primary_ll);
        view_below = (LinearLayout) findViewById(R.id.view_below);
        last_ll = (LinearLayout) findViewById(R.id.last_ll);
        seekBar = (SeekBar) findViewById(R.id.progress);
        center_play_pause = (ImageButton) findViewById(R.id.center_play_pause);

        current_time = (TextView) findViewById(R.id.current_time);
        total_time = (TextView) findViewById(R.id.total_time);

        ipAddressTextView = (TextView) findViewById(R.id.emailAddressTextView);
        emailAddressTextView = (TextView) findViewById(R.id.ipAddressTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        ipAddressTextView.setVisibility(View.GONE);
        emailAddressTextView.setVisibility(View.GONE);
        dateTextView.setVisibility(View.GONE);

        compress_expand = (ImageView) findViewById(R.id.compress_expand);
        back = (ImageButton) findViewById(R.id.back);


        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        RelativeLayout.LayoutParams params1 = null;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);

            } else {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
        } else {
            if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

            } else {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
            }
        }

        player_layout.setLayoutParams(params1);
        bannerImageRelativeLayout.setLayoutParams(params1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Toast.makeText(getApplicationContext(),""+seekBar.getProgress(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTimeTask);
                playerStartPosition = emVideoView.getCurrentPosition();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTimeTask);
                emVideoView.seekTo(seekBar.getProgress());
                current_time.setVisibility(View.VISIBLE);
                current_time.setVisibility(View.GONE);
                showCurrentTime();
                current_time.setVisibility(View.VISIBLE);
                updateProgressBar();
                if (playerPreviousPosition == 0) {
                    if (playerStartPosition < emVideoView.getCurrentPosition()) {
                        isFastForward = true;
                        playerPreviousPosition = playerStartPosition;

                    } else {
                        playerPreviousPosition = playerStartPosition;
                        isFastForward = false;

                    }
                }
            }
        });
        durationRelativeLayout.setVisibility(View.GONE);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Instant_End_Timer();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Start_Timer();
                }
                return false;
            }
        });

        emVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.progress_view).getVisibility() == View.VISIBLE) {
                    primary_ll.setVisibility(View.GONE);
                    center_play_pause.setVisibility(View.GONE);
                    latest_center_play_pause.setVisibility(View.GONE);
                    current_time.setVisibility(View.GONE);


                } else {
                    if (primary_ll.getVisibility() == View.VISIBLE) {
                        primary_ll.setVisibility(View.GONE);
                        last_ll.setVisibility(View.GONE);
                        center_play_pause.setVisibility(View.GONE);
                        latest_center_play_pause.setVisibility(View.GONE);
                        current_time.setVisibility(View.GONE);

                        End_Timer();
                    } else {


                        primary_ll.setVisibility(View.GONE);


                        last_ll.setVisibility(View.VISIBLE);
                        center_play_pause.setVisibility(View.VISIBLE);
                        latest_center_play_pause.setVisibility(View.VISIBLE);
                        current_time.setVisibility(View.VISIBLE);
                        current_time.setVisibility(View.GONE);
                        showCurrentTime();
                        current_time.setVisibility(View.VISIBLE);
                        Start_Timer();
                    }

                }


            }
        });

        compress_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Write code here

                if (compressed) {
                    compressed = false;
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    player_layout.setLayoutParams(params);
                    compress_expand.setImageResource(R.drawable.ic_media_fullscreen_shrink);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                    });
                    hideSystemUI();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {

                    RelativeLayout.LayoutParams params1 = null;
                    if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);

                        } else {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);
                        }
                    } else {
                        if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

                        } else {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
                        }

                    }
                    player_layout.setLayoutParams(params1);
                    compressed = true;
                    compress_expand.setImageResource(R.drawable.ic_media_fullscreen_stretch);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                    });
                    showSystemUI();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }


            }
        });


          /*-------------Added for trailer ---------------*/


        DataModel dbModel = new DataModel();
        movieUniqueId = dbModel.getMovieUniqueId();
        isEpisode = dbModel.getEpisode_id();
        lineTextview.setVisibility(View.GONE);


        image_logo.bringToFront();

//jgvfhfj
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerImageRelativeLayout.setVisibility(View.GONE);
                player_layout.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.VISIBLE);


                ////
                preLoadVideos(contentDetailsOutputModel);
                //added condition for check movieTrailerurl null or not .....by nihar #30-10-2017
               /* movieUrl.replace("\\", "");
                if (movieUrl != null) {
                    Log.v("Niihar_url", movieUrl);
                    emVideoView.setVideoURI(Uri.parse(movieUrl));

                }*/

//                emVideoView.setVideoURI(Uri.parse("https://d16wkdkbh7je0c.cloudfront.net/uploads/trailers/28506/Yoga_for_Weight_Loss.mp4?Expires=1509108246&Signature=IlwLU1x8mWyuE9LZaq1SdHsXG31sJzNcUB6902WnFIM3iswG589u2~syrZ138yYRHIh4SFfKOs7pDqljNWO8BLvsVrux09StsUuBOYyCuBuKTPvzeRj57E73SjS8mwFw-OD9AaQ~sdQ8n0175ghOyEdfyQl7A5dcGYZHD38wInWEYVm70X5YHvdzOqOtf5hf~XOOZ5a~7eM0So~pomwuF~LDvrEY1~2EBGwyiDQ-YnkLv6l2sSjxPGwQ0IFSSOnFUhxiHqbX4vgfmjAZpbZrGHLfawUlsxDV5rRsSy~Pw19jMFzCRVmGvAbQnCZ43acEXyEbgYUb8RXP2EtstM5WkQ__&Key-Pair-Id=APKAJYIDWFG3D6CNOYVA"));
            }
        });




        center_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Execute_Pause_Play();
            }
        });
        latest_center_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCastSession != null && mCastSession.isConnected()) {
                    if (player.utils.Util.hide_pause) {
                        player.utils.Util.hide_pause = false;
                        latest_center_play_pause.setVisibility(View.GONE);
                    }
                    Execute_Pause_Play();

                } else {
                    Execute_Pause_Play();

                }

            }
        });



        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                Log.v("ANU","PREPARED CALLED===");

                video_completed = false;
                video_prepared = true;
                progressView.setVisibility(View.VISIBLE);
                center_play_pause.setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.GONE);
                try {

                    // mHandler.removeCallbacks(updateTimeTask);

                    current_time.setVisibility(View.VISIBLE);
                    SensorOrientationChangeNotifier.getInstance(YogaPlayerActivity.this).addListener(YogaPlayerActivity.this);
                    emVideoView.start();
                    seekBar.setProgress(emVideoView.getCurrentPosition());
                    updateProgressBar();

                    ////nihar
                } catch (Exception e) {
                }
            }
        });



       /* dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YogaPlayerActivity.this, DietPlanActivity.class);
                startActivity(intent);
            }
        });*/

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.shareIt(YogaPlayerActivity.this);
            }
        });

        if (ContextCompat.checkSelfPermission(YogaPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(YogaPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(YogaPlayerActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        111);
            } else {
                ActivityCompat.requestPermissions(YogaPlayerActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);

            }
        } else {
            //Call whatever you want
            if (NetworkStatus.getInstance().isConnected(YogaPlayerActivity.this)) {

                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
                contentIdStr = getIntent().getStringExtra("CONTENT_ID");
                contentStreamIdStr = getIntent().getStringExtra("CONTENT_STREAM_ID");
                useridStr = preferenceManager.getUseridFromPref();

                contentDetailsInput.setAuthToken(authTokenStr);

                Log.v("SUBHA", "authToken === " + authTokenStr);
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
                Util.showToast(YogaPlayerActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                // Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }


        ///////added by nihar
        center_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Execute_Pause_Play();
            }
        });
        latest_center_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.hide_pause) {
                    Util.hide_pause = false;
                    Start_Timer();
                }

                Execute_Pause_Play();
            }
        });


        /***favorite *****/
        favorite_view_episode.setVisibility(View.GONE);

        favorite_view_episode.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

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

                        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, YogaPlayerActivity.this, YogaPlayerActivity.this);
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

                        AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, YogaPlayerActivity.this, YogaPlayerActivity.this);
                        asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);


                    }
                } else {
                    Util.favorite_clicked = true;

                    Intent registerActivity = new Intent(YogaPlayerActivity.this, RegisterActivity.class);
                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    registerActivity.putExtra("from", this.getClass().getName());
                    startActivityForResult(registerActivity, 30060);

                    bannerImageRelativeLayout.setVisibility(View.VISIBLE);
                    player_layout.setVisibility(View.GONE);

                }

            }
        });


        /***favorite *****/



        /*if (ContextCompat.checkSelfPermission(YogaPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(YogaPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(YogaPlayerActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        111);
            } else {
                ActivityCompat.requestPermissions(YogaPlayerActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);

            }
        } else {
            //Call whatever you want
            if (NetworkStatus.getInstance().isConnected(YogaPlayerActivity.this)) {

                ContentDetailsInput contentDetailsInput1 = new ContentDetailsInput();
                contentDetailsInput1.setAuthToken(authTokenStr);
                contentDetailsInput1.setPermalink(permalinkStr);
                contentDetailsInput1.setUser_id(preferenceManager.getUseridFromPref());
                contentDetailsInput1.setCountry(preferenceManager.getCountryCodeFromPref());
                contentDetailsInput1.setLanguage(preferenceManager.getLanguageListFromPref());

                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput1, YogaPlayerActivity.this, YogaPlayerActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(YogaPlayerActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                // Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }*/




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



        percentg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(YogaPlayerActivity.this, R.style.MyAlertDialogStyle);
                                            dlgAlert.setTitle(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.STOP_SAVING_THIS_VIDEO, player.utils.Util.DEFAULT_STOP_SAVING_THIS_VIDEO));
                                            dlgAlert.setMessage(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.YOUR_VIDEO_WONT_BE_SAVED, player.utils.Util.DEFAULT_YOUR_VIDEO_WONT_BE_SAVED));
                                            dlgAlert.setPositiveButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.BTN_KEEP, player.utils.Util.DEFAULT_BTN_KEEP), null);
                                            dlgAlert.setCancelable(false);
                                            dlgAlert.setPositiveButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.BTN_KEEP, player.utils.Util.DEFAULT_BTN_KEEP),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();

                                                        }
                                                    });
                                            dlgAlert.setNegativeButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.BTN_DISCARD, player.utils.Util.DEFAULT_BTN_DISCARD), null);
                                            dlgAlert.setCancelable(false);
                                            dlgAlert.setNegativeButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.BTN_DISCARD, player.utils.Util.DEFAULT_BTN_DISCARD),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            downloading = false;
                                                            audio = dbHelper.getContact(playerModel.getStreamUniqueId() + emailIdStr);

                                                            if (audio != null) {


                                                                String k = String.valueOf(audio.getDOWNLOADID());

                                                                downloadManager.remove(audio.getDOWNLOADID());
                                                                dbHelper.deleteRecord(audio);

                                                                SQLiteDatabase DB = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                                                                String query = "DELETE FROM " + DBHelper.DOWNLOAD_CONTENT_INFO + " WHERE download_contnet_id = '" + enqueue + "'";
                                                                DB.execSQL(query);

                                                            }


                                                            exoplayerdownloadhandler.post(new Runnable() {
                                                                @Override
                                                                public void run() {


                                                                    Progress.setProgress((int) 0);
                                                                    //percentg.setText(0+"%");
                                                                    percentg.setVisibility(View.GONE);
                                                                    download.setVisibility(View.VISIBLE);


                                                                }
                                                            });

                                                            Toast.makeText(getApplicationContext(), player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.DOWNLOAD_CANCELLED, player.utils.Util.DEFAULT_DOWNLOAD_CANCELLED), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                            dlgAlert.create().show();

                                        }
                                    }
        );





    }

/*chromecast-------------------------------------*/

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
                final Intent searchIntent = new SearchIntentHandler(YogaPlayerActivity.this).handleSearchIntent();
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(YogaPlayerActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(YogaPlayerActivity.this, RegisterActivity.class);
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
            case R.id.action_mydownload:

                final Intent mydownload = new MyDownloadIntentHandler(YogaPlayerActivity.this).handleDownloadIntent();
                startActivity(mydownload);
                // Not implemented here
                return false;

            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(YogaPlayerActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(YogaPlayerActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;

            default:
                break;
        }

        return false;
    }


    @Override
    public void onGetContentDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {
        this.contentDetailsOutputModel = contentDetailsOutput;
        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "contentdetails pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }


        if (status == 200) {

            movieUniqueId = contentDetailsOutput.getMuviUniqId();
            movieGenre = contentDetailsOutput.getGenre();
            benefits = contentDetailsOutput.getBenefit();
            season = contentDetailsOutput.getSeason();
            movieDetailsStr = contentDetailsOutput.getStory();
            _permalink = contentDetailsOutput.getPermalink();
            isFavorite = contentDetailsOutput.getIs_favorite();
            bannerImageId = contentDetailsOutput.getBanner();
            posterImageId = contentDetailsOutput.getPoster();
            duration = contentDetailsOutput.getDuration();
            repetition = contentDetailsOutput.getRepetition();
            difficulty_level = contentDetailsOutput.getDifficulty_level();
            name = contentDetailsOutput.getName();
            contentId = contentDetailsOutput.getId();
            muviStreamId = contentDetailsOutput.getMovieStreamUniqId();
            movieTrailerUrlStr = contentDetailsOutput.getTrailerUrl();
            contentTypesId = contentDetailsOutput.getContentTypesId();
            videoDurationStr = contentDetailsOutput.getVideoDuration();
            movieUrl = contentDetailsOutput.getMovieUrl();
            lineTextview.setVisibility(View.VISIBLE);

           /* benefitsTitleTextView.setText(benefits);
            durationTitleTextView.setText(duration);
            diffcultyTitleTextView.setText(difficulty_level);
            repetitionTitleTextView.setText(repetition);*/

            benefitsTitleTextView.setText(languagePreference.getTextofLanguage(BENEFIT_TITLE, DEFAULT_BENEFIT_TITLE));
            durationTitleTextView.setText(languagePreference.getTextofLanguage(DURATION_TITLE, DEFAULT_DURATION_TITLE));
            diffcultyTitleTextView.setText(languagePreference.getTextofLanguage(DIFFICULTY_TITLE, DEFAULT_DIFFICULTY_TITLE));
            repetitionTitleTextView.setText(languagePreference.getTextofLanguage(REPETITION_TITLE, DEFAULT_REPETITION_TITLE));
            if (contentDetailsOutput.getMetadata() != null || contentDetailsOutput.getMetadata().size() > 0) {
               /* benefitsTitleTextView.setVisibility(View.GONE);
                colortitle1.setVisibility(View.GONE);*/
                benefitsLinearLayout.removeAllViews();
                for (Map.Entry<String, String> entry : contentDetailsOutput.getMetadata().entrySet()) {

                    if (entry.getValue() != null && !entry.getValue().matches("")) {

                        DynamicLayout(benefitsLinearLayout, entry.getKey().toUpperCase(), entry.getValue());
                    }


                }
              /*  RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.benefitsLinearLayout);
                relatedContentList.setLayoutParams(params);*/

            } else {
               /* FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), benefitsTitleTextView);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.light_fonts), benefitsStoryTextView);
                benefitsStoryTextView.setText(benefits.trim());
                colortitle1.setVisibility(View.VISIBLE);*/
                benefitsLinearLayout.setVisibility(View.GONE);
            }

          /*  if (benefits.matches("") || benefits.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                benefitsTitleTextView.setVisibility(View.GONE);
                colortitle1.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), benefitsTitleTextView);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.light_fonts), benefitsStoryTextView);
                benefitsStoryTextView.setText(benefits.trim());
                colortitle1.setVisibility(View.VISIBLE);
            }*/

            if (duration.matches("")) {
                durationTitleTextView.setVisibility(View.GONE);
                lineTextview.setVisibility(View.GONE);

            } else {

                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.fonts), durationTitleTextView);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), days);
                days.setText(duration);
            }
            if (difficulty_level.matches("")) {
                diffcultyTitleTextView.setVisibility(View.GONE);
                lineTextview.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.fonts), diffcultyTitleTextView);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), difficulty);
                difficulty.setText(difficulty_level);
            }
            if (videoDurationStr.matches("")) {
                durationRelativeLayout.setVisibility(View.GONE);
            } else {
                durationRelativeLayout.setVisibility(View.VISIBLE);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), clocktime);
                clocktime.setText(videoDurationStr);
            }
            if (repetition.matches("")) {
                repetitionTitleTextView.setVisibility(View.GONE);
                lineTextview1.setVisibility(View.GONE);
            } else {
                lineTextview1.setVisibility(View.VISIBLE);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.fonts), repetitionTitleTextView);
                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), repetitionTextView);
                repetitionTextView.setText(repetition);
            }


            if (difficulty_level.matches("") && duration != null) {
                lineTextview.setVisibility(View.GONE);
                days.setGravity(Gravity.CENTER);
            } else if (duration.matches("") && difficulty_level != null) {
                lineTextview.setVisibility(View.GONE);
                difficulty.setGravity(Gravity.CENTER);
            }


            if (name.matches("") || name.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                detailsTextView.setVisibility(View.GONE);
                colortitle.setVisibility(View.GONE);

            } else {


                FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), detailsTextView);

                detailsTextView.setText(name);
                colortitle.setVisibility(View.VISIBLE);
            }


            if (movieTrailerUrlStr.equals("") && movieTrailerUrlStr != null && movieTrailerUrlStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                playButton.setVisibility(View.GONE);
                Log.v("SUBHA", "trailer url no === ");
            } else {
                playButton.setVisibility(View.VISIBLE);
                Log.v("SUBHA", "trailer url yes === ");
            }


            if (TextUtils.isEmpty(bannerImageId)) {

                if (TextUtils.isEmpty(posterImageId)) {

                    moviePoster.setImageResource(R.drawable.logo);
                } else {


                    Picasso.with(YogaPlayerActivity.this)
                            .load(posterImageId)
                            .error(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .into(moviePoster);

                }

            } else {


                /*ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.logo)
                        .showImageOnFail(R.drawable.logo)
                        .showImageOnLoading(R.drawable.logo).build();
                imageLoader.displayImage(bannerImageId.trim(), moviePoster, options);*/

                Picasso.with(YogaPlayerActivity.this)
                        .load(posterImageId)
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .into(moviePoster);


            }
            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                favorite_view_episode.setVisibility(View.VISIBLE);
                if (preferenceManager != null) {
                    loggedInStr = preferenceManager.getUseridFromPref();
                }
                if (loggedInStr != null && isFavorite == 0 && Util.favorite_clicked == true) {

                    Util.favorite_clicked = false;


                    AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                    addToFavInputModel.setAuthToken(authTokenStr);
                    addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                    addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                    addToFavInputModel.setIsEpisodeStr(isEpisode);

                    AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, YogaPlayerActivity.this, YogaPlayerActivity.this);
                    asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);
                } else if (loggedInStr != null && isFavorite == 1) {

                    favorite_view_episode.setImageResource(R.drawable.favorite_red);
                } else {
                    favorite_view_episode.setImageResource(R.drawable.favorite);

                }

            } else {
                favorite_view_episode.setVisibility(View.GONE);
            }


            RelatedContentListInput relatedContentListInput = new RelatedContentListInput();
//            permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);

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

    @Override
    public void onResume() {
        super.onResume();

//        SensorOrientationChangeNotifier.getInstance(YogaPlayerActivity.this).addListener(this);

       /* if (Util.favorite_clicked == true) {

            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
            contentDetailsInput.setAuthToken(authTokenStr);
            contentDetailsInput.setPermalink(permalinkStr);

            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, YogaPlayerActivity.this, YogaPlayerActivity.this);
            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);
        }*/
// **************chromecast*********************//
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
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        YogaPlayerActivity.this.sucessMsg = sucessMsg;
        favorite_view_episode.setImageResource(R.drawable.favorite);
        showToast();
        isFavorite = 0;
        Util.favorite_clicked = true;
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
        }
    }

    public void showToast() {
        LogUtil.showLog("ANU", "SHOWTOASTisepisode" + isEpisode);
        LogUtil.showLog("ANU", "Value" + movieUniqueId);

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg = (TextView) toastRoot.findViewById(R.id.toastMsg);
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
//        toast.setText("Added to Favorites");
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    public void onAddToFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
        pDialog.show();
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        if (status == 200) {


            //pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            YogaPlayerActivity.this.sucessMsg = sucessMsg;
            String loggedInStr = preferenceManager.getLoginStatusFromPref();

            favorite_view_episode.setImageResource(R.drawable.favorite_red);
            isFavorite = 1;
            Util.favorite_clicked = true;
            showToast();
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "addd fav pdlog hide");
                pDialog.hide();
            }

          /*  RelatedContentListInput relatedContentListInput = new RelatedContentListInput();
            permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
            contentIdStr = getIntent().getStringExtra("CONTENT_ID");
            contentStreamIdStr = getIntent().getStringExtra("CONTENT_STREAM_ID");
            useridStr = preferenceManager.getUseridFromPref();

            relatedContentListInput.setAuthToken(authTokenStr);

            Log.v("SUBHA", "authToken ===  1234566" + authTokenStr);
            if (preferenceManager != null) {
                String countryPref = preferenceManager.getCountryCodeFromPref();
                relatedContentListInput.setCountry(countryPref);
            } else {
                relatedContentListInput.setCountry("IN");
            }
            relatedContentListInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            relatedContentListInput.setPermalink(permalinkStr);
            GetRelatedContentListAsynTask asynLoadMovieDetails = new GetRelatedContentListAsynTask(relatedContentListInput, this, this);
            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);
*/


        }
    }


    /*****************
     * chromecast*-------------------------------------
     */

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

                player.utils.Util.call_finish_at_onUserLeaveHint = true;
                latest_center_play_pause.setEnabled(true);
                emVideoView.setEnabled(true);

                startTimer();
                emVideoView.start();

                if (cast_disconnected_position != 0) {

                    emVideoView.seekTo((int) cast_disconnected_position);
                    log_temp_id = "0";
                    player_start_time = millisecondsToString((int) cast_disconnected_position);
                    playerPosition = player_start_time;

                    // Call video log here
                }

                latest_center_play_pause.setImageResource(R.drawable.center_ic_media_pause);
                center_play_pause.setImageResource(R.drawable.ic_media_pause);
                latest_center_play_pause.setVisibility(View.GONE);
                mHandler.removeCallbacks(updateTimeTask);
                updateProgressBar();



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


                cast_disconnected_position = session.getRemoteMediaClient().getApproximateStreamPosition();

            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {



                stoptimertask();
                player.utils.Util.call_finish_at_onUserLeaveHint = false;
                player.utils.Util.hide_pause = true;
                ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.VISIBLE);
                emVideoView.setEnabled(false);
//                latest_center_play_pause.setEnabled(true);

                if (emVideoView.isPlaying()) {
                    emVideoView.pause();
                    latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
                    center_play_pause.setImageResource(R.drawable.ic_media_play);
                    mHandler.removeCallbacks(updateTimeTask);
                }


                if (center_pause_paly_timer_is_running) {
                    center_pause_paly_timer.cancel();
                    center_pause_paly_timer_is_running = false;
                    Log.v("BIBHU11", "CastAndCrewActivity End_Timer cancel called");


                    last_ll.setVisibility(View.GONE);
                    center_play_pause.setVisibility(View.GONE);
                    current_time.setVisibility(View.GONE);
                }

                if (SubTitlePath.size() > 0)
//                    subtitle_change_btn.setVisibility(View.VISIBLE);
//                primary_ll.setVisibility(View.VISIBLE);



                mCastSession = castSession;
                mLocation = PlaybackLocation.REMOTE;
                if (null != mSelectedMedia) {
                    if (mPlaybackState == PlaybackState.PLAYING) {
//                        mVideoView.pause();
//                        loadRemoteMedia(mSeekbar.getProgress(), true);
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();

                Log.v("ANU","video_prepared ==****=="+video_prepared);
                if (video_prepared){

                    emVideoView.setEnabled(false);
                    MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                    movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, playerModel.getVideoStory());
                    movieMetadata.putString(MediaMetadata.KEY_TITLE, playerModel.getVideoTitle());
                    movieMetadata.addImage(new WebImage(Uri.parse(playerModel.getPosterImageId())));
                    movieMetadata.addImage(new WebImage(Uri.parse(playerModel.getPosterImageId())));


                    Log.v("ANU","playerModel.getVideoTitle()===="+playerModel.getVideoTitle());
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
//                                jsonObj.put("ip_address", ipAddres.trim());
                            jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                            jsonObj.put("episode_id", playerModel.getEpisode_id());
                            jsonObj.put("watch_status", "start");
                            jsonObj.put("device_type", "2");
                            jsonObj.put("log_id", "0");
                            jsonObj.put("active_track_index", "0");

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

                            // This code is changed according to new Video log //

                            jsonObj.put("played_length", "0");
                            jsonObj.put("log_temp_id", "0");
                            jsonObj.put("resume_time", "0");
                            jsonObj.put("seek_status", "");
                            // This  Code Is Added For Drm BufferLog By Bibhu ...

                            jsonObj.put("resolution", "BEST");
                            jsonObj.put("start_time", "0");
                            jsonObj.put("end_time", "0");
                            jsonObj.put("log_unique_id", "0");
                            jsonObj.put("location", "0");
                            jsonObj.put("bandwidth_log_id", "0");
                            jsonObj.put("video_type", "mped_dash");
                            jsonObj.put("drm_bandwidth_by_sender", "0");

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
//                                jsonObj.put("ip_address", ipAddres.trim());
                            jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                            jsonObj.put("episode_id", playerModel.getEpisode_id());
                            jsonObj.put("watch_status", "start");
                            jsonObj.put("device_type", "2");
                            jsonObj.put("log_id", "0");
                            jsonObj.put("active_track_index", "0");
                            jsonObj.put("seek_status", "");

                            jsonObj.put("played_length", "0");
                            jsonObj.put("log_temp_id", "0");
                            jsonObj.put("resume_time", "0");
                            jsonObj.put("seek_status", "");


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

                        mediaInfo = new MediaInfo.Builder(com.home.vod.util.Util.dataModel.getVideoUrl().trim())
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
            }

            private void onApplicationDisconnected() {
/*
                    mPlayCircle.setVisibility(View.GONE);
*/
//                emVideoView.setEnabled(true);
//                emVideoView.start();
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

                        break;
                    case REMOTE:
                        // mPlayCircle.setVisibility(View.VISIBLE);
                        if (mCastSession != null && mCastSession.isConnected()) {
                            loadRemoteMedia(emVideoView.getCurrentPosition(), true);


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
        remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }

        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {

            @Override
            public void onStatusUpdated() {

                /*Intent intent = new Intent(YogaPlayerActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.removeListener(this);*/


                if (mCastSession != null && mCastSession.isConnected()) {
                    Log.v("BIBHU222", "======" + remoteMediaClient.isPlaying());

                    if (remoteMediaClient.isPlaying()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                latest_center_play_pause.setImageResource(R.drawable.center_ic_media_pause);
                                latest_center_play_pause.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
                                latest_center_play_pause.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                }

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
    @Override
    public void onGetRelatedContentPostExecuteCompleted(RelatedContentOutput relatedContentOutput, int status, String message) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();

        }
        if (status == 200) {
            String permalinkStr = relatedContentOutput.getPermalink().substring(relatedContentOutput.getPermalink().lastIndexOf("/") + 1);
            LogUtil.showLog("SUBHA", "getPermalink()" + permalinkStr);


            Intent intent = new Intent(YogaPlayerActivity.this, DietPlanActivity.class);
            intent.putExtra(HeaderConstants.VLINK, permalinkStr);
            startActivity(intent);

        } else
            Toast.makeText(YogaPlayerActivity.this, "There is No Diet Plan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetRelatedContentPreExecuteStarted() {
        pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
        pDialog.show();
        LogUtil.showLog("SUBHA", "onGetRelatedContentPreExecuteStarted");

    }

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
        if (requestCode == 30060 && resultCode == RESULT_OK) {
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


    /* ------ Added for Trailer ------*/


    private Runnable updateTimeTask = new Runnable() {
        public void run() {
          /*  if (played_length > 0) {
                emVideoView.seekTo(34000);
                seekBar.setProgress(34000);
            }else {*/

            seekBar.setProgress(emVideoView.getCurrentPosition());
//            }
            seekBar.setMax(emVideoView.getDuration());
            Calcute_Currenttime_With_TotalTime();
            mHandler.postDelayed(this, 1000);

            if (contentTypesId != 4) {
                seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
            }

            current_matching_time = emVideoView.getCurrentPosition();


            if ((previous_matching_time == current_matching_time) && (current_matching_time < emVideoView.getDuration())) {
                findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
                center_play_pause.setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.GONE);
                previous_matching_time = current_matching_time;
            } else {

                if (contentTypesId == 4) {


                } else {
                    if (current_matching_time >= emVideoView.getDuration()) {
                        mHandler.removeCallbacks(updateTimeTask);
                        //  pause_play.setImageResource(R.drawable.ic_media_play);
//                    emVideoView.release();
//                    emVideoView.reset();
                        seekBar.setProgress(0);
//                    emVideoView.seekTo(0);
                        current_time.setText("00:00:00");
                        total_time.setText("00:00:00");
                        previous_matching_time = 0;
                        current_matching_time = 0;
                        video_completed = true;


//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        bannerImageRelativeLayout.setVisibility(View.VISIBLE);
                        player_layout.setVisibility(View.GONE);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            }
                        });
                        showSystemUI();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        //onBackPressed();
//                        backCalled();
                    }
                }


                previous_matching_time = current_matching_time;
                findViewById(R.id.progress_view).setVisibility(View.GONE);

            }

        }
    };


    public void showCurrentTime() {

        current_time.setText(Current_Time);
        current_time_position_timer();


    }







    public void current_time_position_timer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (contentTypesId != 4) {

                            seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
                            current_time.setX(seek_label_pos - current_time.getWidth() / 2);
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 100);
    }


    public void Start_Timer() {

        End_Timer();
        center_pause_paly_timer = new Timer();
        center_pause_paly_timer_is_running = true;
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                //perform your action here

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        current_time.setVisibility(View.VISIBLE);
                        center_play_pause.setVisibility(View.VISIBLE);
                        latest_center_play_pause.setVisibility(View.VISIBLE);
                        End_Timer();
                    }
                });
            }
        };
        center_pause_paly_timer.schedule(timerTaskObj, 1000, 1000);
    }

    public void End_Timer() {
        if (center_pause_paly_timer_is_running) {
            center_pause_paly_timer.cancel();
            center_pause_paly_timer_is_running = false;

            primary_ll.setVisibility(View.GONE);
            current_time.setVisibility(View.GONE);
            last_ll.setVisibility(View.GONE);
            center_play_pause.setVisibility(View.GONE);
            latest_center_play_pause.setVisibility(View.GONE);
        }

    }

    public void Instant_End_Timer() {
        if (center_pause_paly_timer_is_running) {
            center_pause_paly_timer.cancel();
            center_pause_paly_timer_is_running = false;
        }

    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 1000);
    }

    public void Calcute_Currenttime_With_TotalTime() {
        TotalTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(emVideoView.getDuration()),
                TimeUnit.MILLISECONDS.toMinutes(emVideoView.getDuration()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(emVideoView.getDuration())),
                TimeUnit.MILLISECONDS.toSeconds(emVideoView.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(emVideoView.getDuration())));

        Current_Time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(emVideoView.getCurrentPosition()),
                TimeUnit.MILLISECONDS.toMinutes(emVideoView.getCurrentPosition()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(emVideoView.getCurrentPosition())),
                TimeUnit.MILLISECONDS.toSeconds(emVideoView.getCurrentPosition()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(emVideoView.getCurrentPosition())));

        total_time.setText(TotalTime);
        current_time.setText(Current_Time);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        bannerImageRelativeLayout.setVisibility(View.VISIBLE);
        player_layout.setVisibility(View.GONE);

        if(emVideoView!=null)
            emVideoView.release();
    }

    /* ------ Added for Trailer ------*/
    ///added by nihar
    public void Execute_Pause_Play() {


        if (mCastSession != null && mCastSession.isConnected()) {
            if (remoteMediaClient.isPlaying()) {
                remoteMediaClient.pause();
                return;
            }
            if (remoteMediaClient.isPaused()) {
                remoteMediaClient.play();
                return;
            }
        }


        if (emVideoView.isPlaying()) {
            emVideoView.pause();
            latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
            center_play_pause.setImageResource(R.drawable.ic_media_play);
            mHandler.removeCallbacks(updateTimeTask);
        } else {
            Log.v("Subhalaxmi", "video_completed ===== " + video_completed);

            if (video_completed) {

              /*  if (content_types_id != 4) {
                    // onBackPressed();
//                    backCalled();
                }*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                });
                showSystemUI();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            } else {
                emVideoView.start();
                latest_center_play_pause.setImageResource(R.drawable.center_ic_media_pause);
                center_play_pause.setImageResource(R.drawable.ic_media_pause);
                mHandler.removeCallbacks(updateTimeTask);
                updateProgressBar();
            }

        }
    }

    private void hideSystemUI() {
        view_below.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        emVideoView.setLayoutParams(params);
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
       /* LinearLayout.LayoutParams params1 = null;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
            if(YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*45)/100);

            }
            else
            {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*45)/100);
            }
        }
        else
        {
            if(YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

            }
            else
            {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
            }
        }

        emVideoView.setLayoutParams(params1);*/
        /*if (Util.dataModel.getVideoStory().trim() != null && !Util.dataModel.getVideoStory().trim().matches("")){
            story.setText(Util.dataModel.getVideoStory());
            story.setVisibility(View.VISIBLE);
            ResizableCustomView.doResizeTextView(TrailerActivity.this,story, MAX_LINES, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);

        } else {
            story.setVisibility(View.GONE);
        }*/
        RelativeLayout.LayoutParams params1 = null;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 35) / 100);

            } else {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
        } else {
            if (YogaPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

            } else {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
            }
        }

        player_layout.setLayoutParams(params1);
        bannerImageRelativeLayout.setLayoutParams(params1);
     /*   RelativeLayout.LayoutParams params1 = null;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

        } else {
            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
        }*/

        player_layout.setLayoutParams(params1);
        compressed = true;
        compress_expand.setImageResource(R.drawable.ic_media_fullscreen_stretch);
        view_below.setVisibility(View.VISIBLE);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );


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


    //////dynamic layout

    public void DynamicLayout(LinearLayout layout, String Header, String Details) {
        Log.v("Nihar", "" + Header + Details);
        //masterlayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        /////View
        View view = new View(this);
        int paddingleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        //  view.setPadding(paddingleft, 0, 0, 0);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.sideTitle_line_height), getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.sideTitle_line_width), getResources().getDisplayMetrics());
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width - 1, height + 2);
        view.setLayoutParams(parms);
        int marginleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        parms.setMargins(marginleft, paddingleft, 0, 0);
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ///Title Textview
        TextView textView = new TextView(this);
        textView.setText(Header);
        textView.setAllCaps(true);
        textView.setTextColor(getResources().getColor(R.color.videotextColor));
        //  int titleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.story_title_text_size) , getResources().getDisplayMetrics());

        textView.setTextSize(20);

        FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.regular_fonts), textView);
        LinearLayout.LayoutParams TextViewParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        int textview = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int topmargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        TextViewParams.setMargins(textview, topmargin, 0, 2);
        textView.setLayoutParams(TextViewParams);

        linearLayout.addView(view);
        linearLayout.addView(textView);


        TextView detail_text = new TextView(this);
        detail_text.setPadding(paddingleft, 0, paddingleft, 0);
        detail_text.setTextColor(getResources().getColor(R.color.videotextColor));
        //  int detailTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.story_text_size) , getResources().getDisplayMetrics());

        detail_text.setTextSize(13);
        // detail_text.setTextSize(getResources().getDimension(R.dimen.story_text_size));

        int textviewheader = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        FontUtls.loadFont(YogaPlayerActivity.this, getResources().getString(R.string.light_fonts), detail_text);

        detail_text.setText(Details);

        LinearLayout.LayoutParams detailsParam = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        detail_text.setLayoutParams(detailsParam);

        detailsParam.setMargins(textviewheader, textviewheader, textviewheader, textviewheader);


        ///main layout view set
        layout.addView(linearLayout);
        layout.addView(detail_text);

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

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }


    public void preLoadVideos(ContentDetailsOutput contentDetailsOutput) {


        playerModel.setVideoTitle(contentDetailsOutput.getName());
        playerModel.setVideoStory(contentDetailsOutput.getStory());
        playerModel.setPosterImageId(contentDetailsOutput.getPoster());


        dbModel.setIsFreeContent(isFreeContent);
        dbModel.setIsAPV(isAPV);
        dbModel.setIsPPV(isPPV);
        dbModel.setIsConverted(1);
        dbModel.setMovieUniqueId(movieUniqueId);
        dbModel.setStreamUniqueId(contentDetailsOutput.getMovieStreamUniqId());
//        dbModel.setThirdPartyUrl(contentDetailsOutput.getEpisodeThirdPartyUrl());
        dbModel.setVideoTitle(contentDetailsOutput.getName());
        Log.v("ANU","contentDetailsOutput.getName()===="+contentDetailsOutput.getName());
        dbModel.setVideoStory(contentDetailsOutput.getStory());
//        dbModel.setVideoGenre(videoGenreTextView.getText().toString());
        dbModel.setVideoDuration(contentDetailsOutput.getVideoDuration());
        // dbModel.setVideoReleaseDate(item.getEpisodeTelecastOn());
        dbModel.setVideoReleaseDate("");


//        dbModel.setEpisode_id(item.getEpisodeStreamUniqueId());
//        dbModel.setSeason_id(Season_Value);
        dbModel.setPurchase_type("show");
        dbModel.setPosterImageId(contentDetailsOutput.getId());//may be wrong
        dbModel.setContentTypesId(contentTypesId);

      /*  dbModel.setEpisode_series_no(contentDetailsOutput.getEpisodeSeriesNo());
        dbModel.setEpisode_no(contentDetailsOutput.getEpisodeNumber());
        dbModel.setEpisode_title(contentDetailsOutput.getEpisodeTitle());*/

        // dbModel.setParentTitle(movieNameStr);
        dbModel.setContentTypesId(contentDetailsOutput.getContentTypesId());

        Util.dataModel = dbModel;

        //validate user called.
        ValidateUser(getApplicationContext(), useridStr, contentDetailsOutput.getId(), contentDetailsOutput.getMovieStreamId());

    }

    public void ValidateUser(Context context, String UserId, String movieId, String episodeId) {

        ValidateUserInput validateUserInput = new ValidateUserInput();
        validateUserInput.setAuthToken(authTokenStr);
        validateUserInput.setUserId(UserId);
        validateUserInput.setMuviUniqueId(movieId);
        validateUserInput.setPurchaseType(com.home.vod.util.Util.dataModel.getPurchase_type());
        validateUserInput.setSeasonId(com.home.vod.util.Util.dataModel.getSeason_id());
        validateUserInput.setEpisodeStreamUniqueId(episodeId);
        validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

        asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, YogaPlayerActivity.this, context);
        asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);

    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {

    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        Log.v("Niihar_url", "==============++++++====" + status);
        if (status == 429 || status == 430) {
            if (NetworkStatus.getInstance().isConnected(this)) {

                //getVideodetails Api called

                getVideoDetails(getApplicationContext());

            } else {
                Toast.makeText(YogaPlayerActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

            }
        }
    }

    public void getVideoDetails(Context context) {
        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
        getVideoDetailsInput.setAuthToken(authTokenStr);
        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());

        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, YogaPlayerActivity.this, context);
        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {

    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int code, String status, String message) {


     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/

        boolean play_video = true;

        if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {

            if (_video_details_output.getStreaming_restriction().trim().equals("0")) {

                play_video = false;
            } else {
                play_video = true;
            }
        } else {
            play_video = true;
        }


        if (!play_video) {

            try {
                if (pDialog.isShowing())
                    pDialog.hide();
            } catch (IllegalArgumentException ex) {
            }

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(YogaPlayerActivity.this, R.style.MyAlertDialogStyle);
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
            if (playerModel.getVideoUrl().contains(".mpd")) {
                isDrm = true;
            } else {
                isDrm = false;
            }
            com.home.vod.util.Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if (_video_details_output.getPlayed_length() != null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((com.home.vod.util.Util.isDouble(_video_details_output.getPlayed_length())));


            //dependency for datamodel

            SubTitleName = new ArrayList<>(_video_details_output.getSubTitleName());
            SubTitleLanguage = new ArrayList<>(_video_details_output.getSubTitleLanguage());
            ResolutionUrl = new ArrayList<>(_video_details_output.getResolutionUrl());
            ResolutionFormat = new ArrayList<>(_video_details_output.getResolutionFormat());

            player.utils.Util.VideoResolution = "Auto";

            if (ResolutionUrl.size() < 1) {
                Log.v("SUBHA", "resolution image Invisible called");
            } else {
                ResolutionUrl.add(playerModel.getVideoUrl().trim());
                ResolutionFormat.add("Auto");
            }

            if (ResolutionFormat.size() > 0) {
                Collections.reverse(ResolutionFormat);
                for (int m = 0; m < ResolutionFormat.size(); m++) {
                    Log.v("BIBHU", "RESOLUTION FORMAT======" + ResolutionFormat.get(m));
                }
            }
            if (ResolutionUrl.size() > 0) {
                Collections.reverse(ResolutionUrl);
                for (int n = 0; n < ResolutionUrl.size(); n++) {
                    Log.v("BIBHU", "RESOLUTION URL======" + ResolutionUrl.get(n));
                }
            }


//            SubTitleName = _video_details_output.getSubTitleName();
//            SubTitleLanguage = _video_details_output.getSubTitleLanguage();

            if (SubTitlePath.size() > 0 || ResolutionUrl.size() > 0) {
                subtitle_change_btn.setVisibility(View.VISIBLE);
            }


            com.home.vod.util.Util.dataModel.setVideoUrl(_video_details_output.getVideoUrl());
            com.home.vod.util.Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());
            com.home.vod.util.Util.dataModel.setThirdPartyUrl(_video_details_output.getThirdparty_url());
            com.home.vod.util.Util.dataModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            com.home.vod.util.Util.dataModel.setChannel_id(_video_details_output.getChannel_id());
            com.home.vod.util.Util.dataModel.setPreRoll(_video_details_output.getPreRoll());
            com.home.vod.util.Util.dataModel.setPostRoll(_video_details_output.getPostRoll());
            com.home.vod.util.Util.dataModel.setMidRoll(_video_details_output.getMidRoll());
            com.home.vod.util.Util.dataModel.setAdDetails(_video_details_output.getAdDetails());


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


            exoplayerdownloadhandler = new Handler();
            dbHelper = new DBHelper(YogaPlayerActivity.this);
            dbHelper.getWritableDatabase();
            audio_1 = dbHelper.getContact(muviStreamId + emailIdStr);

            if (audio_1 != null) {
                Log.v("SUBHALAXMIPANDA1","data called =============== ");
                if (audio_1.getUSERNAME().trim().equals(emailIdStr.trim())) {
                    checkDownLoadStatusFromDownloadManager1(audio_1, false);
                }
            }
            Log.v("SUBHALAXMIPANDA1","STAUS"+playerModel.getContentTypesId()+"G"+playerModel.getDownloadStatus()+playerModel.getIsOffline());
            if (playerModel.getContentTypesId()!=4 && playerModel.getIsOffline().equals("1") && playerModel.getDownloadStatus().equals("1")) {
                download_layout.setVisibility(View.VISIBLE);
            }


            if (playerModel.getVideoUrl() == null || playerModel.getVideoUrl().matches("")) {

                com.home.vod.util.Util.showNoDataAlert(YogaPlayerActivity.this);


            } else {
                Log.v("ANU","if ELSE=========");
                Log.v("ANU","_video_details_output.getThirdparty_url()========="+_video_details_output.getThirdparty_url());


                // condition for checking if the response has third party url or not.
                if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {
                    Log.v("ANU","if _video_details_output=========");


                    if (mCastSession != null && mCastSession.isConnected()) {
                        Log.v("ANU","if=========");
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
//                                jsonObj.put("ip_address", ipAddres.trim());
                                jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                                jsonObj.put("episode_id", playerModel.getEpisode_id());
                                jsonObj.put("watch_status", "start");
                                jsonObj.put("device_type", "2");
                                jsonObj.put("log_id", "0");
                                jsonObj.put("active_track_index", "0");

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

                                // This code is changed according to new Video log //

                                jsonObj.put("played_length", "0");
                                jsonObj.put("log_temp_id", "0");
                                jsonObj.put("resume_time", "0");
                                jsonObj.put("seek_status", "");
                                // This  Code Is Added For Drm BufferLog By Bibhu ...

                                jsonObj.put("resolution", "BEST");
                                jsonObj.put("start_time", "0");
                                jsonObj.put("end_time", "0");
                                jsonObj.put("log_unique_id", "0");
                                jsonObj.put("location", "0");
                                jsonObj.put("bandwidth_log_id", "0");
                                jsonObj.put("video_type", "mped_dash");
                                jsonObj.put("drm_bandwidth_by_sender", "0");

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

                            Log.v("ANU","playerModel.getMpdVideoUrl().trim()====="+playerModel.getMpdVideoUrl().trim());
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
                            Log.v("ANU","else=========");
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject();
                                jsonObj.put("description", playerModel.getVideoTitle());

                                //  This Code Is Added For Video Log By Bibhu..

                                jsonObj.put("authToken", authTokenStr);
                                jsonObj.put("user_id", preferenceManager.getUseridFromPref());
//                                jsonObj.put("ip_address", ipAddres.trim());
                                jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                                jsonObj.put("episode_id", playerModel.getEpisode_id());
                                jsonObj.put("watch_status", "start");
                                jsonObj.put("device_type", "2");
                                jsonObj.put("log_id", "0");
                                jsonObj.put("active_track_index", "0");
                                jsonObj.put("seek_status", "");

                                jsonObj.put("played_length", "0");
                                jsonObj.put("log_temp_id", "0");
                                jsonObj.put("resume_time", "0");
                                jsonObj.put("seek_status", "");


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

                            mediaInfo = new MediaInfo.Builder(com.home.vod.util.Util.dataModel.getVideoUrl().trim())
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
                    } else {


//                        playerModel.setThirdPartyPlayer(false);
//                        emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));
                        if (FakeSubTitlePath.size() > 0) {
                            final Intent playVideoIntent;

                            playVideoIntent = new Intent(YogaPlayerActivity.this, ProgramPlayerActivity.class);

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

                                        progressBarHandler = new ProgressBarHandler(YogaPlayerActivity.this);
                                        progressBarHandler.show();
                                        Download_SubTitle(FakeSubTitlePath.get(0).trim());
                                    } else {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                        playVideoIntent.putExtra("PlayerModel", playerModel);
//                                    playVideoIntent.putExtra("PLAY_LIST",itemData);
                                        //  startActivity(playVideoIntent);
                                    }

                                }
                            });
                        } else {
                            Log.v("ANU", "==============++++++====" + playerModel.getVideoUrl());

                            playerModel.setThirdPartyPlayer(false);
                            // emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));
                            setVideoPlayer();
                        }
                    }
                } else {
                    final Intent playVideoIntent = new Intent(YogaPlayerActivity.this, YogaPlayerActivity.class);
//                    final Intent   playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    playVideoIntent.putExtra("PlayerModel", playerModel);
//                    playVideoIntent.putExtra("PLAY_LIST",itemData);
                    // startActivity(playVideoIntent);
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            com.home.vod.util.Util.showNoDataAlert(YogaPlayerActivity.this);
        }

    }

    public void Download_SubTitle(String Url) {
        new YogaPlayerActivity.DownloadFileFromURL_Offline().execute(Url);
    }

    class DownloadFileFromURL_Offline extends AsyncTask<String, String, String> {

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
                mediaStorageDir1 = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList_Offline/", "");

                if (!mediaStorageDir1.exists()) {
                    if (!mediaStorageDir1.mkdirs()) {
                        Log.d("App", "failed to create directory");
                    }
                }

                SubtitleModel subtitleModel = new SubtitleModel();
                subtitleModel.setUID(playerModel.getStreamUniqueId() + emailIdStr);
                subtitleModel.setLanguage(playerModel.getOfflineLanguage().get(0));
                String filename = mediaStorageDir1.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt";
                subtitleModel.setPath(filename);

                Log.v("BIBHU3", "SubTitleName============" + filename);

                long rowId = dbHelper.insertRecordSubtittel(subtitleModel);
                Log.v("BIBHU3", "rowId============" + rowId + "sub id ::" + subtitleModel.getUID());

                playerModel.getOfflineLanguage().remove(0);


                OutputStream output = new FileOutputStream(filename);

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
                Log.v("BIBHU3", "error===========" + e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {

            playerModel.getOfflineUrl().remove(0);
            if (playerModel.getOfflineUrl().size() > 0) {
                Download_SubTitle(playerModel.getOfflineUrl().get(0).trim());
            } else {
                playerModel.setThirdPartyPlayer(false);
                // emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));
                setVideoPlayer();
            }

        }
    }

    private void setVideoPlayer() {
        try {
            Log.v("ANU", "==============++++++====" + playerModel.getVideoUrl());

            /*
             * Initialize the Wasabi Runtime (necessary only once for each
			 * instantiation of the application)
			 *
			 * ** Note: Set Runtime Properties as needed for your environment
			 */
            Runtime.initialize(getDir("wasabi", MODE_PRIVATE).getAbsolutePath());
            /*
             * Personalize the application (acquire DRM keys). ExoPlayerActivity.this is only
			 * necessary once each time the application is freshly installed
			 *
			 * ** Note: personalize() is a blocking call and may take long
			 * enough to complete to trigger ANR (Application Not Responding)
			 * errors. In a production application ExoPlayerActivity.this should be called in a
			 * background thread.
			 */
            if (!Runtime.isPersonalized())
                Runtime.personalize();

        } catch (NullPointerException e) {
            //onBackPressed();
            return;
        } catch (ErrorCodeException e) {
            // Consult WasabiErrors.txt for resolution of the error codes
            //onBackPressed();
            return;
        }

        try {
            EnumSet<PlaylistProxy.Flags> flags = EnumSet.noneOf(PlaylistProxy.Flags.class);
            playerProxy = new PlaylistProxy(flags, YogaPlayerActivity.this, new Handler());
            playerProxy.start();
        } catch (ErrorCodeException e) {
            // Consult WasabiErrors.txt for resolution of the error codes
            //  onBackPressed();
            return;
        }



        	/*
         * create a playlist proxy url and pass it to the native player
		 */
        try {
            /*
             * Note that the MediaSourceType must be adapted to the stream type
			 * (DASH or HLS). Similarly,
			 * the MediaSourceParams need to be set according to the media type
			 * if MediaSourceType is SINGLE_FILE
			 */

            ContentTypesYoga contentType = ContentTypesYoga.DASH;
            PlaylistProxy.MediaSourceParams params = new PlaylistProxy.MediaSourceParams();
            params.sourceContentType = contentType
                    .getMediaSourceParamsContentType();
            /*
             * if the content has separate audio tracks (eg languages) you may
			 * select one using MediaSourceParams, eg params.language="es";
			 */
            String contentTypeValue = contentType.toString();
            if (playerModel.getVideoUrl().contains(".mpd")) {
                String url = playerProxy.makeUrl(playerModel.getVideoUrl(), PlaylistProxy.MediaSourceType.valueOf((contentTypeValue == "MP4" || contentTypeValue == "HLS" || contentTypeValue == "DASH") ? contentTypeValue : "SINGLE_FILE"), params);
                emVideoView.setVideoURI(Uri.parse(url));

            } else {
                // emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));
//                setVideoPlayer();
                emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));

            }


        } catch (ErrorCodeException e) {
            // Consult WasabiErrors.txt for resolution of the error codes
            //  onBackPressed();

            return;
        } catch (IllegalArgumentException e) {
            // onBackPressed();
            e.printStackTrace();
        } catch (SecurityException e) {
            // onBackPressed();
            e.printStackTrace();
        } catch (IllegalStateException e) {
            //  onBackPressed();
            e.printStackTrace();
        }


    }

/////////////////start///////////////
private class AsynWithdrm extends AsyncTask<Void, Void, Void> {
    ProgressBarHandler pDialog;
    int responseCode;
    // String loginHistoryIdStr = loginPref.getString("PREFS_LOGIN_HISTORYID_KEY", null);
    String responseStr;

    @Override
    protected Void doInBackground(Void... params) {


        String urlRouteList = player.utils.Util.rootUrl().trim() + player.utils.Util.morlineBB.trim();
        //String urlRouteList ="https://sonydadc.muvi.com/rest/getMarlinBBOffline";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlRouteList);
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, authTokenStr);
            httppost.addHeader("stream_unique_id",muviStreamId);


            Log.v("SUBHA","authToken == "+ player.utils.Util.authTokenStr);
            Log.v("SUBHA","stream_unique_id == "+ muviStreamId);


            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", " asyncwith drm response === " + responseStr);

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);

                responseCode = Integer.parseInt(myJson.optString("code"));
            }
            JSONObject mainJson = null;
            if (responseCode >= 0) {
                if (responseCode == 200) {

                    mainJson = myJson.getJSONObject("data");

                    if ((mainJson.has("file")) && mainJson.getString("file").trim() != null && !mainJson.getString("file").trim().isEmpty() && !mainJson.getString("file").trim().equals("null") && !mainJson.getString("file").trim().matches("")) {
                        mlvfile = mainJson.getString("file");

                        Log.v("SUBHA", mlvfile);
                    } else {
                        mlvfile = player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.NO_DATA, player.utils.Util.DEFAULT_NO_DATA);
                    }

                    if ((mainJson.has("token")) && mainJson.getString("token").trim() != null && !mainJson.getString("token").trim().isEmpty() && !mainJson.getString("token").trim().equals("null") && !mainJson.getString("token").trim().matches("")) {
                        token = mainJson.getString("token");
                        Log.v("SUBHA", "token" + token);

                    } else {
                        token = player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.NO_DATA, player.utils.Util.DEFAULT_NO_DATA);
                    }


                    if ((mainJson.has("multiple_resolution")) && mainJson.getString("multiple_resolution").trim() != null && !mainJson.getString("multiple_resolution").trim().isEmpty() && !mainJson.getString("multiple_resolution").trim().equals("null") && !mainJson.getString("multiple_resolution").trim().matches("")) {
                        JSONArray jsonArray = mainJson.optJSONArray("multiple_resolution");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            if (jsonArray.getJSONObject(i).optString("resolution").trim().contains("BEST"))
                                List_Of_Resolution_Format.add(jsonArray.getJSONObject(i).optString("resolution"));
                            else
                                List_Of_Resolution_Format.add(jsonArray.getJSONObject(i).optString("resolution") + "p");

                            List_Of_Resolution_Url.add(jsonArray.getJSONObject(i).optString("url"));
                            List_Of_Resolution_Url_Used_For_Download.add(jsonArray.getJSONObject(i).optString("url"));

                            Log.v("BIBHU1", "resolution = " + jsonArray.getJSONObject(i).optString("resolution"));
                            Log.v("BIBHU1", "url = " + jsonArray.getJSONObject(i).optString("url"));
                        }

                        Collections.reverse(List_Of_Resolution_Format);
                        Collections.reverse(List_Of_Resolution_Url);
                        Collections.reverse(List_Of_Resolution_Url_Used_For_Download);

                    }
                    //=======================End====================//

                }
            } else {
                responseStr = "0";
            }

        } catch (Exception e) {
            responseCode = 0;
        }

        int count;
        InputStream is = new ByteArrayInputStream(token.getBytes());
        InputStream inputs = new BufferedInputStream(is, 8192);
        Log.v("SUBHA", "pathh" + token);
        File root = Environment.getExternalStorageDirectory();
        mediaStorageDir = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/TOKEN", "");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }

        licensetoken = mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".xml";
        OutputStream output = null;
        try {
            output = new FileOutputStream(licensetoken);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.v("SUBHA", "pathh" + licensetoken);
        byte data[] = new byte[1024];

        long total = 0;

        try {
            while ((count = inputs.read(data)) != -1) {
                total += count;
                Log.v("SUBHA", "Lrngth" + total);

                output.write(data, 0, count);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            output.flush();
            output.close();


            inputs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    protected void onPostExecute(Void result) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;

            }



            //ExoPlayerActivity.this portion is changed later because of multiple download option.

            if (List_Of_Resolution_Url.size() > 0) {
                pDialog_for_gettig_filesize = new ProgressBarHandler(YogaPlayerActivity.this);
                pDialog_for_gettig_filesize.show();

                new DetectDownloadingFileSize().execute();
            } else {
                new DownloadFileFromURL().execute(mlvfile);

            }


        } catch (IllegalArgumentException ex) {
            Toast.makeText(YogaPlayerActivity.this, player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.SIGN_OUT_ERROR, player.utils.Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }


    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
        pDialog.show();


    }
}
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        ProgressBarHandler pDialog;
        int responseCode;
        String responseStr;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(YogaPlayerActivity.this);
            pDialog.show();

        }

        /**
         * Downloading file in background thread
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(f_url[0]);
                HttpResponse execute = client.execute(httpGet);
                float size = (Float.parseFloat("" + execute.getEntity().getContentLength()) / 1024) / 1024;
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                size = Float.valueOf(decimalFormat.format(size));
                lengthfile = (int) size;




            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //===========

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                }


                String lengh = String.valueOf(lengthfile);

                Log.v("SUBHA","size of file === "+lengh);

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(YogaPlayerActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setTitle(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.WANT_TO_DOWNLOAD, player.utils.Util.DEFAULT_WANT_TO_DOWNLOAD));
                dlgAlert.setMessage(name + " " + "(" + lengh + "MB)");
                dlgAlert.setPositiveButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.DOWNLOAD_BUTTON_TITLE, player.utils.Util.DEFAULT_DOWNLOAD_BUTTON_TITLE), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.DOWNLOAD_BUTTON_TITLE, player.utils.Util.DEFAULT_DOWNLOAD_BUTTON_TITLE),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                downloading = true;

                                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                                if (currentApiVersion >= Build.VERSION_CODES.M) {
                                    requestStoragePermission();
                                } else {
                                    downloadFile(true);
                                }

                            }
                        });
                dlgAlert.setNegativeButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.CANCEL_BUTTON, player.utils.Util.DEFAULT_CANCEL_BUTTON), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.CANCEL_BUTTON, player.utils.Util.DEFAULT_CANCEL_BUTTON),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();


            } catch (IllegalArgumentException ex) {
                Toast.makeText(YogaPlayerActivity.this, player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.SIGN_OUT_ERROR, player.utils.Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }


        }
    }
    private void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(YogaPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(YogaPlayerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {

            if (List_Of_Resolution_Url_Used_For_Download.size() > 0) {
                downloadFile(false);
            } else {
                downloadFile(true);
            }
        }
    }
    private void downloadFile(boolean singlefile) {

        DownloadManager.Request request;
        if (singlefile) {
            selected_download_format = 0;
            if (isDrm)
                request = new DownloadManager.Request(Uri.parse(mlvfile));
            else
                request = new DownloadManager.Request(Uri.parse(playerModel.getVideoUrl()));
        } else {
            if (isDrm)
                request = new DownloadManager.Request(Uri.parse(List_Of_Resolution_Url_Used_For_Download.get(selected_download_format)));
            else
                request = new DownloadManager.Request(Uri.parse(ResolutionUrl.get(selected_download_format + 1)));
            selected_download_format = 0;
        }


        request.setTitle(playerModel.getVideoTitle());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String timestamp = "";
        if (isDrm) {
            //Get download file name
            fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(mlvfile);
            timestamp = System.currentTimeMillis() + ".mlv";
            //Save file to destination folder
            Log.v("SUBHALAXMIPANDA1","isdrm");
            request.setDestinationInExternalPublicDir("Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHDRM", timestamp);
        } else {
            //Get download file name
            fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(playerModel.getVideoUrl());
            timestamp = System.currentTimeMillis() + ".exo";
            //Save file to destination folder
            request.setDestinationInExternalPublicDir("Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM", timestamp);

        }

        enqueue = downloadManager.enqueue(request);

        download.setVisibility(View.GONE);
        percentg.setVisibility(View.VISIBLE);
        Progress.setProgress(0);

        ContactModel1 contactModel1 = new ContactModel1();
        contactModel1.setMUVIID(name);
        contactModel1.setDOWNLOADID((int) enqueue);
        contactModel1.setProgress(0);
        contactModel1.setUSERNAME(emailIdStr);
        contactModel1.setUniqueId(muviStreamId + emailIdStr);

        contactModel1.setDSTATUS(2);
        contactModel1.setPoster(posterImageId);


        if (isDrm) {
            contactModel1.setToken(licensetoken);
            Log.v("SUBHALAXMIPANDA1","licensetoken");

            contactModel1.setPath(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHDRM/" + timestamp);
        } else {
            contactModel1.setToken(fileExtenstion);
            contactModel1.setPath(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM/" + timestamp);
        }

        contactModel1.setContentid(String.valueOf(contentTypesId));
        contactModel1.setGenere(movieGenre);
        contactModel1.setMuviid(movieUniqueId);
        contactModel1.setDuration(videoDurationStr);

        Log.v("SANJAYA1234",playerModel.getPosterImageId().trim());
        dbHelper.insertRecord(contactModel1);

        Log.d("BIBHU", emailIdStr);


        audio = dbHelper.getContact(contactModel1.getUniqueId());
        if (audio != null) {

            if (audio.getUSERNAME().trim().equals(emailIdStr.trim())) {


                checkDownLoadStatusFromDownloadManager1(audio, true);
            }
        }


        SQLiteDatabase DB = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
        String query1 = "INSERT INTO " + DBHelper.DOWNLOAD_CONTENT_INFO + "(download_contnet_id,log_id,authtoken,email," +
                "ipaddress,movie_id,episode_id,device_type,download_status,server_sending_final_status) VALUES" +
                "('" + enqueue + "','0','" + authTokenStr + "','" + emailIdStr.trim() + "','" + ipAddres + "'," +
                "'" + movieUniqueId + "','" + muviStreamId + "'," +
                "'" + 2 + "','2','0')";

        DB.execSQL(query1);

        //---------------------- End ---------------------------------//

        // Send BroadCast to start sending Download content bandwidth .

        Intent intent = new Intent("BnadwidthLog");
        sendBroadcast(intent);

        //---------------------- End ---------------------------------//

        // Have to unComment

        // ExoPlayerActivity.this code is only responsible for Access period and Watch Period feature on Download Contnet
        Cursor cursor = DB.rawQuery("SELECT * FROM " + DBHelper.WATCH_ACCESS_INFO + "" +
                " WHERE email = '" + emailIdStr.trim() + "' AND stream_unique_id = '" + playerModel.getStreamUniqueId() + "'", null);

        if (cursor.getCount() > 0) {
            String query = "UPDATE " + DBHelper.WATCH_ACCESS_INFO + " SET download_id = '" + enqueue + "' , " +
                    "stream_unique_id = '" + playerModel.getStreamUniqueId() + "',initial_played_time = '0'," +
                    "updated_server_current_time = '0' WHERE email = '" + emailIdStr.trim() + "' AND stream_unique_id = '" + playerModel.getStreamUniqueId() + "'";
            DB.execSQL(query);

            Log.v("BIBHU1234", "update called");

        } else {
            String query = "INSERT INTO " + DBHelper.WATCH_ACCESS_INFO + " (download_id , stream_unique_id , initial_played_time , updated_server_current_time,email) VALUES" +
                    " ('" + enqueue + "','" + playerModel.getStreamUniqueId() + "','0','0','" + emailIdStr.trim() + "')";
            DB.execSQL(query);

            Log.v("BIBHU1234", "insert called");

        }


        //=================================End=======================================================//


    }








    public void checkDownLoadStatusFromDownloadManager1(final ContactModel1 model, final boolean CallAccessPeriodApi) {

        if (model.getDOWNLOADID() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    downloading = true;
                    //  Util.downloadprogress=0;
                    int bytes_downloaded = 0;
                    int bytes_total = 0;
                    downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    while (downloading) {


                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(model.getDOWNLOADID()); //filter by id which you have receieved when reqesting download from download manager
                        Cursor cursor = downloadManager.query(q);


                        if (cursor != null && cursor.getCount() > 0) {
                            if (cursor.moveToFirst()) {
                                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int status = cursor.getInt(columnIndex);
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                                    model.setDSTATUS(1);
                                    dbHelper.updateRecord(model);
                                    downloading = false;

                                    Intent intent = new Intent("NewVodeoAvailable");
                                    sendBroadcast(intent);

                                    SQLiteDatabase DB = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                                    String query1 = "UPDATE " + DBHelper.DOWNLOAD_CONTENT_INFO + " SET download_status = '1'" +
                                            " WHERE email = '" + emailIdStr + "' AND download_contnet_id = '" + model.getDOWNLOADID() + "'";
                                    DB.execSQL(query1);

                                    if (isDrm) {
                                        try {
                                            String licenseAcquisitionToken = getActionTokenFromStorage(model.getToken());
                                            com.intertrust.wasabi.jni.Runtime.processServiceToken(licenseAcquisitionToken);

                                            EnumSet<PlaylistProxy.Flags> flags = EnumSet.noneOf(PlaylistProxy.Flags.class);
                                            playerProxy = new PlaylistProxy(flags, YogaPlayerActivity.this, new Handler());
                                            playerProxy.start();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (CallAccessPeriodApi) {
                                        // Call API to get Access Period and Watch period of download content...
                                        new YogaPlayerActivity.AsyncWatchAccessDetails().execute("" + model.getDOWNLOADID());
                                    }
                                    // Have to unComment


                                } else if (status == DownloadManager.STATUS_FAILED) {
                                    // 1. process for download fail.
                                    model.setDSTATUS(0);

                                    SQLiteDatabase DB = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                                    String query1 = "UPDATE " + DBHelper.DOWNLOAD_CONTENT_INFO + " SET download_status = '0'" +
                                            " WHERE email = '" + emailIdStr + "' AND download_contnet_id = '" + model.getDOWNLOADID() + "'";
                                    DB.execSQL(query1);

                                } else if ((status == DownloadManager.STATUS_PAUSED) ||
                                        (status == DownloadManager.STATUS_RUNNING)) {
                                    model.setDSTATUS(2);

                                } else if (status == DownloadManager.STATUS_PENDING) {
                                    //Not handling now
                                }
                                int sizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                                int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                                long size = cursor.getInt(sizeIndex);
                                long downloaded = cursor.getInt(downloadedIndex);
                                double progress = 0.0;
                                if (size != -1) progress = downloaded * 100.0 / size;
                                // At ExoPlayerActivity.this point you have the progress as a percentage.
                                model.setProgress((int) progress);
                                //Util.downloadprogress=(int) progress;

                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
//                                    downloading = false;
//                                    download_layout.setVisibility(View.GONE);
//                                    writefilepath();
//                                    String path=Environment.getExternalStorageDirectory() + "/WITHDRM/"+fname;
//                                    String fileNameWithOutExt = FilenameUtils.removeExtension(fname);
//                                    String path1 = Environment.getExternalStorageDirectory() + "/Android/data/"+getApplicationContext().getPackageName().trim()+"/WITHDRM/" + playerModel.getVideoTitle().trim() + "-1." + "mlv";
//                                    File file = new File(path1);
//                                    if (file != null && file.exists()) {
//                                        file.delete();
//                                    }

                                }


                            }
                        } else {
                            // model.setDSTATUS(3);
                        }


//

                        runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {


                                download.setVisibility(View.GONE);
                                percentg.setVisibility(View.VISIBLE);
                                Progress.setProgress(0);

                                Progress.setProgress((int) model.getProgress());
                                Log.v("SUBHALAXMIPANDA1","model.getProgress()"+model.getProgress());

                                percentg.setText(model.getProgress() + "%");
//
                                if (model.getProgress() == 100) {
                                    Log.v("SUBHALAXMIPANDA1","model.100()"+model.getProgress());

                                    //writefilepath();
//                                dbHelper.deleteRecord(audio);
                                    download_layout.setVisibility(View.GONE);
                                }

                            }
                        });

                        // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                        cursor.close();
                    }


                }
            }).start();


        }

    }

    private String getActionTokenFromStorage(String tokenFileName) {
        String token = null;
        byte[] readBuffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        int bytesRead = 0;

        try {
            is = new FileInputStream(tokenFileName);
            while ((bytesRead = is.read(readBuffer)) != -1) {
                baos.write(readBuffer, 0, bytesRead);
            }
            baos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        token = new String(baos.toByteArray());
        return token;
    }



    class AsyncWatchAccessDetails extends AsyncTask<String, String, String> {

        String responseStr;
        int statusCode = 0;
        String request_data = "";
        String log_id = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {

            Log.v("BIBHU11", "f_url[0]=======" + f_url[0]);

            SQLiteDatabase DB = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = DB.rawQuery("SELECT stream_unique_id FROM " + DBHelper.WATCH_ACCESS_INFO + " WHERE download_id = '" + f_url[0].trim() + "'", null);
            int count = cursor.getCount();
            String Stream_Id = "";

            if (count > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        Stream_Id = cursor.getString(0).trim();

                        Log.v("BIBHU3", "Stream_Id============" + Stream_Id);
                    } while (cursor.moveToNext());
                }
            }


            String urlRouteList = player.utils.Util.rootUrl().trim() + player.utils.Util.GetOfflineViewRemainingTime.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", player.utils.Util.authTokenStr.trim());
                httppost.addHeader("stream_uniq_id", Stream_Id);
                httppost.addHeader("watch_remaining_time", "0");
                httppost.addHeader("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                httppost.addHeader("user_id", userIdStr);
                httppost.addHeader("device_type ", "2");
                httppost.addHeader("request_data ", "");
                httppost.addHeader("lang_code", player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.SELECTED_LANGUAGE_CODE, player.utils.Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (Exception e) {
                    statusCode = 0;
                    e.printStackTrace();
                }


                Log.v("BIBHU11", "response of GetOfflineViewRemainingTime in exoplayer=======" + responseStr);

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    request_data = myJson.optString("request_data");
                    log_id = myJson.optString("log_id");

                    Log.v("BIBHU11", "response of server_current_time in exoplayer=======" + myJson.optLong("created_date"));
                    Log.v("BIBHU11", "response of server_current_time in exoplayer=======" + myJson.optLong("access_expiry_time"));
                    Dwonload_Complete_Msg = "";
                    if (statusCode == 200) {

                        Dwonload_Complete_Msg = myJson.optString("download_complete_msg");

                        if (Dwonload_Complete_Msg.trim().equals(""))
                            Dwonload_Complete_Msg = "Your video has been downloaded successfully.";

                        SQLiteDatabase DB1 = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);


                        String query1 = "UPDATE " + DBHelper.WATCH_ACCESS_INFO + " SET server_current_time = '" + myJson.optLong("created_date") + "' ," +
                                "watch_period = '0',access_period = '" + myJson.optLong("access_expiry_time") + "' WHERE download_id = '" + f_url[0].trim() + "'";


//                        String query1 = "UPDATE "+DBHelper.WATCH_ACCESS_INFO+" SET server_current_time = '"+myJson.optLong("created_date")+"' ," +
//                                "watch_period = '0',access_period = '"+((myJson.optLong("created_date"))+300000)+"' WHERE download_id = '"+f_url[0].trim()+"'";


                        DB1.execSQL(query1);
                    }else{

                        if (Dwonload_Complete_Msg.equals("") || responseStr.contains("html")){
                            Dwonload_Complete_Msg = "Your video has been downloaded successfully.";
                            SQLiteDatabase DB1 = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);


                            String query1 = "UPDATE " + DBHelper.WATCH_ACCESS_INFO + " SET server_current_time = '" + 0 + "' ," +
                                    "watch_period = '0',access_period = '" + -1 + "' WHERE download_id = '" + f_url[0].trim() + "'";


                            DB1.execSQL(query1);
                        }
                    }
                }
            } catch (Exception e) {
                if (Dwonload_Complete_Msg.equals("") || responseStr.contains("html")){
                    Dwonload_Complete_Msg = "Your video has been downloaded successfully.";
                    SQLiteDatabase DB1 = YogaPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);


                    String query1 = "UPDATE " + DBHelper.WATCH_ACCESS_INFO + " SET server_current_time = '" + 0 + "' ," +
                            "watch_period = '0',access_period = '" + -1 + "' WHERE download_id = '" + f_url[0].trim() + "'";


                    DB1.execSQL(query1);
                }
                statusCode = 0;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Intent intent = new Intent(YogaPlayerActivity.this, PopUpService.class);
            intent.putExtra("msg", Dwonload_Complete_Msg);
            startService(intent);
        }
    }
    class DetectDownloadingFileSize extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
        }

        /**
         * Downloading file in background thread
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(List_Of_Resolution_Url.get(0));
                HttpResponse execute = client.execute(httpGet);
//                int contentLength = (int)execute.getEntity().getContentLength();

                float size = (Float.parseFloat("" + execute.getEntity().getContentLength()) / 1024) / 1024;
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                size = Float.valueOf(decimalFormat.format(size));
                List_Of_FileSize.add("(" + size + " MB)");


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {


            List_Of_Resolution_Url.remove(0);
            if (List_Of_Resolution_Url.size() > 0) {
                new DetectDownloadingFileSize().execute();
            } else {
                try {
                    if (pDialog_for_gettig_filesize != null && pDialog_for_gettig_filesize.isShowing()) {
                        pDialog_for_gettig_filesize.hide();
                    }
                } catch (IllegalArgumentException ex) {
                }

                // Show PopUp for Multiple Options for Download .
                ShowDownloadOptionPopUp();
            }

        }
    }
    public void ShowDownloadOptionPopUp() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(YogaPlayerActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) YogaPlayerActivity.this.getSystemService(YogaPlayerActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.activity_download_popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        TextView title_text = (TextView) convertView.findViewById(R.id.title_text);
        ListView resolution_list = (ListView) convertView.findViewById(R.id.resolution_list);
        Button save = (Button) convertView.findViewById(R.id.save);
        Button cancel = (Button) convertView.findViewById(R.id.cancel);

        save.setText(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.SAVE, player.utils.Util.DEFAULT_SAVE));
        cancel.setText(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.CANCEL_BUTTON, player.utils.Util.DEFAULT_CANCEL_BUTTON));
        title_text.setText(player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.SAVE_OFFLINE_VIDEO, player.utils.Util.DEFAULT_SAVE_OFFLINE_VIDEO));

        DownloadOptionAdapter adapter = new DownloadOptionAdapter(YogaPlayerActivity.this, List_Of_FileSize, List_Of_Resolution_Format);
        resolution_list.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //download file here
                downloadFile(false);

                alert.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_download_format = 0;
                alert.cancel();
            }
        });

        alert = alertDialog.show();
        alertDialog.setCancelable(false);
        alert.setCancelable(false);

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                selected_download_format = 0;
                // Toast.makeText(getApplicationContext(),"cancel",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
        String responseStr;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                // Execute HTTP Post Request
                try {
                    URL myurl = new URL(player.utils.Util.loadIPUrl);
                    HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
                    InputStream ins = con.getInputStream();
                    InputStreamReader isr = new InputStreamReader(ins);
                    BufferedReader in = new BufferedReader(isr);

                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        responseStr = inputLine;
                    }

                    in.close();


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    ipAddressStr = "";

                } catch (UnsupportedEncodingException e) {

                    ipAddressStr = "";

                } catch (IOException e) {
                    ipAddressStr = "";

                }
                if (responseStr != null) {
                    Object json = new JSONTokener(responseStr).nextValue();
                    if (json instanceof JSONObject) {
                        ipAddressStr = ((JSONObject) json).getString("ip");

                    }
                }

            } catch (Exception e) {
                ipAddressStr = "";
            }
            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {
                ipAddressStr = "";
            }
            return;
        }

        protected void onPreExecute() {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               /* if(List_Of_Resolution_Url_Used_For_Download.size()>0)
                {
                    downloadFile(false);
                }else
                {
                    downloadFile(true);
                }*/

            } else {
                Toast.makeText(YogaPlayerActivity.this, player.utils.Util.getTextofLanguage(YogaPlayerActivity.this, player.utils.Util.DOWNLOAD_INTERRUPTED, player.utils.Util.DEFAULT_DOWNLOAD_INTERRUPTED), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {

            Log.v("BIBHU", "=======================================stoptimertask caled=================================");

            timer.cancel();
            timer = null;
        }

    }


    private int millisecondsToString(int milliseconds) {
        // int seconds = (int) (milliseconds / 1000) % 60 ;
        int seconds = (int) (milliseconds / 1000);

        return seconds;
    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }



    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

            }
        };
    }

}




