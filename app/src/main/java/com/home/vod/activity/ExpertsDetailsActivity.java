package com.home.vod.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
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
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetRelatedContentAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.HeaderConstants;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.RelatedContentInput;
import com.home.apisdk.apiModel.RelatedContentOutput;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.vod.BuildConfig;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.LoginRegistrationOnContentClickHandler;
import com.home.vod.MyDownloadIntentHandler;
import com.home.vod.R;
import com.home.vod.SearchIntentHandler;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.model.LanguageModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.SensorOrientationChangeNotifier;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
import static com.home.vod.preferences.LanguagePreference.BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.CROSSED_MAXIMUM_LIMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ALREADY_PURCHASE_THIS_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_ON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CROSSED_MAXIMUM_LIMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FOLLOWED_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FOLLOW_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.FOLLOWED_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.FOLLOW_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.util.Constant.DAYS_DATA;
import static com.home.vod.util.Constant.PERMALINK_INTENT_ARRAY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.SEASON_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.is_followed;
import static com.home.vod.util.Util.languageModel;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.timer;

/**
 * Created by MUVI on 10/6/2017.
 *
 * @author Abhishek
 */

public class ExpertsDetailsActivity extends AppCompatActivity implements SensorOrientationChangeNotifier.Listener,GetRelatedContentAsynTask.GetRelatedContentListener, GetContentDetailsAsynTask.GetContentDetailsListener, DeleteFavAsync.DeleteFavListener, AddToFavAsync.AddToFavListener,GetValidateUserAsynTask.GetValidateUserListener,
        GetIpAddressAsynTask.IpAddressListener, GetLanguageListAsynTask.GetLanguageListListener,GetEpisodeDeatailsAsynTask.GetEpisodeDetailsListener, VideoDetailsAsynctask.VideoDetailsListener {


    TextView videoStoryTextView,colortitle,categoryTitle,categoryTitle1,line_divider;
    ImageView moviePoster;
    LinearLayout line_layout;
    String PlanId = "";
    int Played_Length = 0;
    String watch_status_String = "start";
    String seek_status = "";
    GetValidateUserAsynTask asynValidateUserDetails;
    String programType = "";
    ImageView bannerImageView, playButton;
    ProgressBarHandler pDialog;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout, image_logo;
    String movieUniqueId = "";
    String movieTrailerUrlStr = "", isEpisode = "";
    String duration = "";
    String videoduration = "";
    String[] season;
    String name;
    int selectedPurchaseType = 0;
    AlertDialog alert;
    String difficulty_level;
    String repetition;
    String preparation_time;
    String cooking_time;
    String calories;
    String email, id;
    public int totaldays;
    String ipAddres = "";
    String movieDetailsStr = "";
    String benefits;
    int isLogin = 0;
    String useridStr;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    VideoDetailsAsynctask asynLoadVideoUrls;
    String movieReleaseDateStr = "";
    PreferenceManager preferenceManager;
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
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ArrayList<String> SubTitleLanguage = new ArrayList<>();
    Player playerModel;
    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    String movieStreamUniqueId, bannerImageId, posterImageId, permalinkStr;
    boolean castStr = false;
    int isFavorite;

    TimerTask timerTask;
    long cast_disconnected_position = 0;
    int player_start_time = 0;
    int player_end_time = 0;
    String log_temp_id = "0";
    int playerPosition = 0;
    boolean video_prepared = false;
    RemoteMediaClient remoteMediaClient;
    boolean video_completed_at_chromecast = false;
    String priceForUnsubscribedStr, priceFosubscribedStr;

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

    EpisodesListModel itemToPlay;
    int ItemClickedPosition = 0;
    public static final int VIDEO_PLAY_BUTTON_CLICK_LOGIN_REG_REQUESTCODE = 8888;
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

        pDialog = new ProgressBarHandler(ExpertsDetailsActivity.this);
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

        Log.v("Subhalaxmi","video done");

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
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                if(ExpertsDetailsActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*35)/100);

                }
                else
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
                }
            }
            else
            {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

                }
                else
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*35)/100);

                }
                else
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*35)/100);
                }
            }
            else
            {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

                }
                else
                {
                    params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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
    public void onGetEpisodeDetailsPreExecuteStarted() {



        pDialog.show();
    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(Episode_Details_output episode_details_output, int status, int i, String message, String movieUniqueId) {

        /*try {
            if (progressBarHandler != null && progressBarHandler.isShowing()) {
                progressBarHandler.hide();

            }
        } catch (IllegalArgumentException ex) {

        }*/


        itemData = new ArrayList<EpisodesListModel>();

        if(itemData != null && itemData.size() >0){
            itemData.clear();
        }

        Log.v("SUBHASS","data called === "+status);
        if (status == 200) {

            Log.v("SUBHASS","episodede ");
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


                itemData.add(new EpisodesListModel(episodeNoStr, episodeStoryStr, episodeDateStr, episodeImageStr, episodeTitleStr, "", episodeSeriesNoStr, movieUniqueId, episodeMovieStreamUniqueIdStr, episodeThirdParty, videodurationStr));

            }

            if(itemData!= null && itemData.size() >0){
                clickItem(itemData.get(0), 0);
            }
            LogUtil.showLog("MUVI", "episode show...1");

            LogUtil.showLog("BISHAL", "itemdata==" + itemData);

        }else{


            try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {

        }
            Toast.makeText(ExpertsDetailsActivity.this,"No Content Available",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int code, String status, String message) {


        // _video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // _video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/

        boolean play_video = true;


        Log.v("SUBHA"," video called startworkout button");

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

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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

//            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if (_video_details_output.getPlayed_length() != null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(_video_details_output.getPlayed_length())));


            //dependency for datamodel

            SubTitleName = _video_details_output.getSubTitleName();
            SubTitleLanguage = _video_details_output.getSubTitleLanguage();

           /* Util.dataModel.setVideoUrl(_video_details_output.getVideoUrl());
            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());
            Util.dataModel.setThirdPartyUrl(_video_details_output.getThirdparty_url());
            Util.dataModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            Util.dataModel.setChannel_id(_video_details_output.getChannel_id());
            Util.dataModel.setPreRoll(_video_details_output.getPreRoll());
            Util.dataModel.setPostRoll(_video_details_output.getPostRoll());
            Util.dataModel.setMidRoll(_video_details_output.getMidRoll());
            Util.dataModel.setAdDetails(_video_details_output.getAdDetails());
            Util.dataModel.setPlayPos(Util.isDouble(_video_details_output.getPlayed_length()));

*/
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

                Util.showNoDataAlert(ExpertsDetailsActivity.this);
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

                    Log.v("pratik","tymg=="+playerModel.getPlayPos());

                    if (mCastSession != null &&
                            mCastSession.isConnected()) {

                        ///Added for resume cast watch
                        Log.v("pratik","tym=="+playerModel.getPlayPos()*1000);
                        if ((playerModel.getPlayPos() * 1000) > 0) {
                            playerModel.setPlayPos(playerModel.getPlayPos());
                            Intent resumeIntent = new Intent(ExpertsDetailsActivity.this, ResumePopupActivity.class);
                            startActivityForResult(resumeIntent, 1001);
                            Log.v("pratik","tym==>0");

                        } else {
//                            Played_Length = 0;
                            watch_status_String = "start";

                            Log.v("pratik","timee elsee");
                            PlayThroughChromeCast();
                        }

                    } else {


                        playerModel.setThirdPartyPlayer(false);

                        final Intent playVideoIntent;
                        if (playerModel.getAdNetworkId() == 3) {
                            LogUtil.showLog("responseStr", "playVideoIntent" + playerModel.getAdNetworkId());

                            playVideoIntent = new Intent(ExpertsDetailsActivity.this, ProgramPlayerActivity.class);
//                            playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                        } else if (playerModel.getAdNetworkId() == 1 && playerModel.getPreRoll() == 1) {
                            if (playerModel.getPlayPos() <= 0) {
                                playVideoIntent = new Intent(ExpertsDetailsActivity.this, AdPlayerActivity.class);
                            } else {
                                playVideoIntent = new Intent(ExpertsDetailsActivity.this, ProgramPlayerActivity.class);
//                                playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);

                            }


                        } else {
                            playVideoIntent = new Intent(ExpertsDetailsActivity.this, ProgramPlayerActivity.class);
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

                                    progressBarHandler = new ProgressBarHandler(ExpertsDetailsActivity.this);
                                    progressBarHandler.show();
//                                    Download_SubTitle(FakeSubTitlePath.get(0).trim());
                                } else {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                    playVideoIntent.putExtra("PlayerModel", playerModel);
                                    playVideoIntent.putExtra("PLAY_LIST", itemData);
                                    playVideoIntent.putExtra("SEASON", 0);
                                    playVideoIntent.putExtra("PERMALINK", _permalink);
                                    playVideoIntent.putExtra("Index", "0");
                                    playVideoIntent.putExtra("TAG", ItemClickedPosition);
                                    playVideoIntent.putExtra("Current_SEASON", 0);
                                    startActivity(playVideoIntent);
                                }

                            }
                        });
                    }
                } else {
                    final Intent playVideoIntent = new Intent(ExpertsDetailsActivity.this, ProgramPlayerActivity.class);
//                    final Intent   playVideoIntent = new Intent(ProgramDetailsActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    playVideoIntent.putExtra("PlayerModel", playerModel);
                    playVideoIntent.putExtra("PLAY_LIST", itemData);
                    playVideoIntent.putExtra("SEASON", 0);
                    playVideoIntent.putExtra("PERMALINK", _permalink);

                    playVideoIntent.putExtra("TAG", ItemClickedPosition);
                    playVideoIntent.putExtra("Index", "0");
                    playVideoIntent.putExtra("Current_SEASON", 0);
                    startActivity(playVideoIntent);
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            Util.showNoDataAlert(ExpertsDetailsActivity.this);
        }

    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {

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

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ExpertsDetailsActivity.this, MainActivity.class);
                            startActivity(in);

                        }
                    });
            dlgAlert.create().show();
        } else if (status <= 0) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ExpertsDetailsActivity.this, MainActivity.class);
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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this);
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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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

            }else if (status == 429 || status == 430) {

                if (validUserStr != null) {


                    if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
                        if (NetworkStatus.getInstance().isConnected(this)) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            getVideoDetailsInput.setContent_uniq_id(itemData.get(0).getEpisodeMuviUniqueId());
                            getVideoDetailsInput.setStream_uniq_id(itemData.get(0).getEpisodeStreamUniqueId());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ExpertsDetailsActivity.this, ExpertsDetailsActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(ExpertsDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (isAPV == 1 || isPPV == 1) {
//                                ShowPpvPopUp();

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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

                            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
                                /*Intent intent = new Intent(ProgrammeActivity.this, SubscriptionActivity.class);
                                intent.putExtra("PlayerModel", playerModel);
                                intent.putExtra("PERMALINK", _permalink);
                                intent.putExtra("SEASON", 0);
                                intent.putExtra("Index", "0");
                                intent.putExtra("Current_SEASON", 0);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);*/

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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

                            } else {
//                                ShowPpvPopUp();

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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

                            }
                        }

                    }
                }


            } else if (isAPV == 1 || isPPV == 1) {
//                ShowPpvPopUp();

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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

            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
              /*  Intent intent = new Intent(ProgrammeActivity.this, SubscriptionActivity.class);
                intent.putExtra("PlayerModel", playerModel);
                intent.putExtra("PERMALINK", _permalink);
                intent.putExtra("SEASON", 0);
                intent.putExtra("Index", "0");
                intent.putExtra("Current_SEASON", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);*/


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
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


            } else if (Util.dataModel.getIsConverted() == 0) {
                Util.showNoDataAlert(ExpertsDetailsActivity.this);
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
                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ExpertsDetailsActivity.this, ExpertsDetailsActivity.this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(ExpertsDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                }
            }

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

            cast_disconnected_position = session.getRemoteMediaClient().getApproximateStreamPosition();


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
    ProgressBarHandler progressBarHandler;
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
    Timer center_pause_paly_timer;
    boolean center_pause_paly_timer_is_running = false;
    public boolean isFastForward = false;
    boolean video_completed = false;
    int screenWidth, screenHeight;
    LinearLayout primary_ll, last_ll;
    boolean compressed = true;
    ArrayList<EpisodesListModel> itemData;
    /* Added for trailer player ---------*/
        ///by nihar
    LinearLayout view_below;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_expertsdetails);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(ExpertsDetailsActivity.this);
        playButton = (ImageView) findViewById(R.id.playButton);
        playButton.setVisibility(View.GONE);
        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        videoStoryTextView = (TextView) findViewById(R.id.videoStoryTextView);
        colortitle = (TextView) findViewById(R.id.colortitle);
        line_divider = (TextView) findViewById(R.id.line_divider);
        categoryTitle = (TextView) findViewById(R.id.categoryTitle);
        categoryTitle1 = (TextView) findViewById(R.id.categoryTitle1);
        line_layout = (LinearLayout) findViewById(R.id.line_layout);
        PlanId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();

        isLogin = preferenceManager.getLoginFeatureFromPref();
        image_logo = (RelativeLayout) findViewById(R.id.logo_image);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        player_layout = (RelativeLayout) findViewById(R.id.player_layout);
        episodeListOptionMenuHandler = new EpisodeListOptionMenuHandler(this);

        progressBarHandler = new ProgressBarHandler(ExpertsDetailsActivity.this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.hide();
                onBackPressed();
            }
        });



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
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
            if(ExpertsDetailsActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*35)/100);

            }
            else
            {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            }
        }
        else
        {
            if(ExpertsDetailsActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

            }
            else
            {
                params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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
                    if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                        if(ExpertsDetailsActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                        {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*35)/100);

                        }
                        else
                        {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*35)/100);
                        }
                    }
                    else
                    {
                        if(ExpertsDetailsActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                        {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

                        }
                        else
                        {
                            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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
        Util.dataModel = dbModel;


        image_logo.bringToFront();



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerImageRelativeLayout.setVisibility(View.GONE);
                player_layout.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.VISIBLE);

//                Toast.makeText(ProgrammeActivity.this,"play button clicked",Toast.LENGTH_SHORT).show();
                //added condition for check movieTrailerurl null or not .....by nihar #30-10-2017

                if(movieTrailerUrlStr != null){



                    if (mCastSession != null && mCastSession.isConnected()) {
                        progressView.setVisibility(View.GONE);

                        progressView.setVisibility(View.GONE);
                        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieReleaseDateStr);
                        movieMetadata.putString(MediaMetadata.KEY_TITLE, name + " - Trailer");
                        movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                        movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject();
                            jsonObj.put("description", name);


                            //  This Code Is Added For Video Log By Bibhu..

                            jsonObj.put("authToken", authTokenStr.trim());
                            jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                            jsonObj.put("ip_address", ipAddres.trim());
                            jsonObj.put("movie_id", movieUniqueId);
                            jsonObj.put("episode_id", "");


                            jsonObj.put("played_length", "0");


                            jsonObj.put("watch_status", "start");
                            jsonObj.put("device_type", "2");
                            jsonObj.put("log_id", "0");

                            // restrict_stream_id is always set to be "0" , bcoz it's a triler.
                            jsonObj.put("restrict_stream_id", "0");

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
                            // This is added only to identify, the videolog is called for trailer
                            jsonObj.put("content_type", "2");

                            //====================End=====================//

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

                    }


                    else {
                        emVideoView.setVideoURI(Uri.parse(movieTrailerUrlStr));
                    }



                }

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
                video_completed = false;
                video_prepared = true;
                progressView.setVisibility(View.VISIBLE);
                center_play_pause.setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.GONE);
               /* try {

                   // mHandler.removeCallbacks(updateTimeTask);

                    current_time.setVisibility(View.VISIBLE);
                    SensorOrientationChangeNotifier.getInstance(ProgrammeActivity.this).addListener(ProgrammeActivity.this);
                    emVideoView.start();
                    seekBar.setProgress(emVideoView.getCurrentPosition());
                    updateProgressBar();

                    ////nihar
                } catch (Exception e) {
                }*/
            }
        });






        ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
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
                final Intent searchIntent = new SearchIntentHandler(ExpertsDetailsActivity.this).handleSearchIntent();
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(ExpertsDetailsActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(ExpertsDetailsActivity.this, RegisterActivity.class);
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

                final Intent mydownload = new MyDownloadIntentHandler(ExpertsDetailsActivity.this).handleDownloadIntent();
                startActivity(mydownload);
                // Not implemented here
                return false;

            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(ExpertsDetailsActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(ExpertsDetailsActivity.this, PurchaseHistoryActivity.class);
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
        pDialog = new ProgressBarHandler(ExpertsDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {

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
            calories = contentDetailsOutput.getCalories();
            preparation_time = contentDetailsOutput.getPreparation_time();
            cooking_time = contentDetailsOutput.getCooking_time();
            name = contentDetailsOutput.getName();
            contentId = contentDetailsOutput.getId();
            muviStreamId = contentDetailsOutput.getMovieStreamId();
            movieTrailerUrlStr = contentDetailsOutput.getTrailerUrl();
            contentTypesId = contentDetailsOutput.getContentTypesId();


            Log.v("SUBHASHREE","movieTrailer === "+ movieTrailerUrlStr);




            if (name.matches("") || name.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                categoryTitle.setVisibility(View.GONE);
                categoryTitle1.setVisibility(View.GONE);
                line_layout.setVisibility(View.GONE);
                colortitle.setVisibility(View.GONE);

            } else {
                FontUtls.loadFont(ExpertsDetailsActivity.this, getResources().getString(R.string.medium_fonts), categoryTitle);
                FontUtls.loadFont(ExpertsDetailsActivity.this, getResources().getString(R.string.medium_fonts), categoryTitle1);

                if (name.contains("-")) {
                    String Data[] = name.split("-");
                    categoryTitle.setVisibility(View.VISIBLE);
                    categoryTitle1.setVisibility(View.VISIBLE);

                    categoryTitle.setText(Data[0].toString() + " - ");
                    categoryTitle1.setText( Data[1].toString());
                    colortitle.setVisibility(View.VISIBLE);

                } else {
                    categoryTitle.setVisibility(View.VISIBLE);
                    categoryTitle1.setVisibility(View.GONE);
                    colortitle.setVisibility(View.VISIBLE);
                    categoryTitle.setText(name.toString());
                }



                line_layout.setVisibility(View.VISIBLE);

                colortitle.setVisibility(View.GONE);
            }


            if (movieTrailerUrlStr.matches("") || (movieTrailerUrlStr != null && movieTrailerUrlStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA)))) {
                playButton.setVisibility(View.GONE);
                Log.v("SUBHA", "trailer url no === ");
            } else {
                playButton.setVisibility(View.VISIBLE);
                Log.v("SUBHA", "trailer url yes === ");
            }




            if (movieDetailsStr.matches("") || movieDetailsStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoStoryTextView.setVisibility(View.GONE);

            } else {
                //  videoStoryTextView.setMaxLines(3);
                videoStoryTextView.setVisibility(View.VISIBLE);

                FontUtls.loadFont(ExpertsDetailsActivity.this, getResources().getString(R.string.light_fonts), videoStoryTextView);

                videoStoryTextView.setText(movieDetailsStr.trim());

            }


            if (TextUtils.isEmpty(bannerImageId)) {

                if (TextUtils.isEmpty(posterImageId)) {

                    moviePoster.setImageResource(R.drawable.logo);
                } else {


                    /*ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.logo)
                            .showImageOnLoading(R.drawable.logo).build();
                    imageLoader.displayImage(posterImageId, moviePoster, options);*/

                    Picasso.with(ExpertsDetailsActivity.this)
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

                Picasso.with(ExpertsDetailsActivity.this)
                        .load(bannerImageId)
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .into(moviePoster);


            }


            if (contentDetailsOutput.getSeason() != null) {
                totaldays = contentDetailsOutput.getSeason().length;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        SensorOrientationChangeNotifier.getInstance(ProgrammeActivity.this).addListener(this);

       /* if (Util.favorite_clicked == true) {

            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
            contentDetailsInput.setAuthToken(authTokenStr);
            contentDetailsInput.setPermalink(permalinkStr);

            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, ProgrammeActivity.this, ProgrammeActivity.this);
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
        if(is_followed == true){
            is_followed = false;
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
        /***************chromecast**********************/
        invalidateOptionsMenu();


    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ExpertsDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        ExpertsDetailsActivity.this.sucessMsg = sucessMsg;
//        favorite_view_episode.setImageResource(R.drawable.favorite);


//        showToast();
        isFavorite = 0;
        if (pDialog.isShowing() && pDialog != null) {
            LogUtil.showLog("PINTU", "delete fav pdlog hide");
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
        pDialog = new ProgressBarHandler(ExpertsDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        if (status == 200) {


            //pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            ExpertsDetailsActivity.this.sucessMsg = sucessMsg;
            String loggedInStr = preferenceManager.getLoginStatusFromPref();

//            favorite_view_episode.setImageResource(R.drawable.favorite_red);
            isFavorite = 1;

//            showToast();
            if (pDialog.isShowing() && pDialog != null) {
                LogUtil.showLog("PINTU", "addd fav pdlog hide");
                pDialog.hide();
            }
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
                Log.v("ANU","cast_disconnected_position===="+cast_disconnected_position);

            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {





                try {
                    castSession.setMessageReceivedCallbacks("urn:x-cast:muvi.mcrt.final", new Cast.MessageReceivedCallback() {
                        @Override
                        public void onMessageReceived(CastDevice castDevice, String s, String s1) {
                            Log.v("bibhu", "onMessageReceived Message from receiver=" + s1);


                            if (s1.contains("completed")) {
                                video_completed_at_chromecast = true;
                                Log.v("bibhu", "video completed at chromecast");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        emVideoView.reset();
                                    }
                                });
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


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

                if (video_prepared){

                    emVideoView.setEnabled(false);
                    MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                    movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieReleaseDateStr);
                    movieMetadata.putString(MediaMetadata.KEY_TITLE, name + " - Trailer");
                    movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                    movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject();
                        jsonObj.put("description", name);


                        //  This Code Is Added For Video Log By Bibhu..

                        jsonObj.put("authToken", authTokenStr.trim());
                        jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                        jsonObj.put("ip_address", ipAddres.trim());
                        jsonObj.put("movie_id", movieUniqueId);
                        jsonObj.put("episode_id", "");


                        jsonObj.put("played_length", "0");


                        jsonObj.put("watch_status", "start");
                        jsonObj.put("device_type", "2");
                        jsonObj.put("log_id", "0");

                        // restrict_stream_id is always set to be "0" , bcoz it's a triler.
                        jsonObj.put("restrict_stream_id", "0");

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
                        // This is added only to identify, the videolog is called for trailer
                        jsonObj.put("content_type", "2");

                        //====================End=====================//

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
                        if (mCastSession != null && mCastSession.isConnected()) {
                            loadRemoteMedia(emVideoView.getCurrentPosition(), true);

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

                /*Intent intent = new Intent(ProgrammeActivity.this, ExpandedControlsActivity.class);
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
            String permalinkStr = relatedContentOutput.getPermalink();
            String storyStr = relatedContentOutput.getStory();
            String titleStr = relatedContentOutput.getTitle();
            String trailer_urlStr = relatedContentOutput.getTrailer_url();
            String posterStr = relatedContentOutput.getPoster();
            String content_bannerStr = relatedContentOutput.getContent_banner();

            if(relatedContentOutput.getIs_downloadable().equalsIgnoreCase("1") || relatedContentOutput.getIs_downloadable().equalsIgnoreCase("2")) {


                Intent intent = new Intent(ExpertsDetailsActivity.this, DietDetailsActivity.class);
                intent.putExtra(HeaderConstants.VLINK, permalinkStr);

                LogUtil.showLog("SUBHA", "getPermalink()" + permalinkStr);

               /* intent.putExtra("STORY", storyStr);
                intent.putExtra("TITLE", titleStr);
                intent.putExtra("TRAILER_URL", trailer_urlStr);
                intent.putExtra("POSTER", posterStr);
                intent.putExtra("CONTENT_BANNER", content_bannerStr);*/
                startActivity(intent);
            }

        } else
            Toast.makeText(ExpertsDetailsActivity.this, "There is No Diet Plan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetRelatedContentPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ExpertsDetailsActivity.this);
        pDialog.show();
        LogUtil.showLog("SUBHA", "onGetRelatedContentPreExecuteStarted");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("BKS", "elseclickedddddd");

        if (resultCode == RESULT_OK && requestCode == 1001) {
            if (data.getStringExtra("yes").equals("1002")) {
                watch_status_String = "halfplay";
                seek_status = "first_time";
                Played_Length =playerModel.getPlayPos() * 1000;
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

                Intent resumeIntent = new Intent(ExpertsDetailsActivity.this, ResumePopupActivity.class);
                startActivityForResult(resumeIntent, 1007);

            }
        } else if (requestCode == 2001) {


            Log.v("pratik", "else conditn called");
            watch_status_String = "start";
            Played_Length = 0;
            PlayThroughChromeCast();
        }else if (requestCode == 30060 && resultCode == RESULT_OK) {
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
        } else if(requestCode == VIDEO_PLAY_BUTTON_CLICK_LOGIN_REG_REQUESTCODE && resultCode == RESULT_OK){
//            new CheckVoucherOrPpvPaymentHandler(MovieDetailsActivity.this).handleVoucherPaymentOrPpvPayment();
            // callValidateUserAPI();


            callForValidateUser();

        }

    }


    /* ------ Added for Trailer ------*/


    private Runnable updateTimeTask = new Runnable() {
        public void run() {
          /*  if (played_length > 0) {
                emVideoView.seekTo(34000);
                seekBar.setProgress(34000);
            }else {*/

            Log.v("Subhalaxmi","video done not complted");
//            Toast.makeText(ProgrammeActivity.this,"video done not complted",Toast.LENGTH_SHORT).show();
            seekBar.setProgress(emVideoView.getCurrentPosition());
//            }
            seekBar.setMax(emVideoView.getDuration());
            Calcute_Currenttime_With_TotalTime();
            mHandler.postDelayed(this, 1000);

            if (contentTypesId != 4) {
//                seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
            }

            current_matching_time = emVideoView.getCurrentPosition();


            if ((previous_matching_time == current_matching_time) && (current_matching_time < emVideoView.getDuration())) {
                Log.v("Subhalaxmi","video done not started");
//                Toast.makeText(ProgrammeActivity.this,"video done not started",Toast.LENGTH_SHORT).show();
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


                        Log.v("Subhalaxmi","video done complted");
//                        Toast.makeText(ProgrammeActivity.this,"video done complted",Toast.LENGTH_SHORT).show();
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
            Log.v("Subhalaxmi","video_completed ===== "+video_completed);

            if (video_completed) {

              /*  if (content_types_id != 4) {
                    // onBackPressed();
//                    backCalled();
                }*/
                Log.v("Subhalaxmi","video done");
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
            if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
            if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

        }
        else
        {
            params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
        }

    player_layout.setLayoutParams(params1);
    compressed = true;
    compress_expand.setImageResource(R.drawable.ic_media_fullscreen_stretch);
        view_below.setVisibility(View.VISIBLE);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );


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


    public void DynamicLayout(LinearLayout layout, String Header, String Details) {
        Log.v("Nihar", "" + Header + Details);
        //masterlayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        /////View
        View view = new View(this);
        //int paddingleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        //  view.setPadding(paddingleft, 0, 0, 0);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height + 2);
        view.setLayoutParams(parms);
      //  int marginleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        parms.setMargins(0, 0, 0, 0);
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ///Title Textview
        TextView textView = new TextView(this);
        textView.setText(Header);
        textView.setAllCaps(true);
        textView.setTextColor(getResources().getColor(R.color.videotextColor));
        //  int titleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.story_title_text_size) , getResources().getDisplayMetrics());

        textView.setTextSize(20);

        FontUtls.loadFont(ExpertsDetailsActivity.this, getResources().getString(R.string.regular_fonts), textView);
        LinearLayout.LayoutParams TextViewParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
      //  int textview = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int topmargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        TextViewParams.setMargins(0, topmargin, 0, 2);
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
        FontUtls.loadFont(ExpertsDetailsActivity.this, getResources().getString(R.string.light_fonts), detail_text);

        detail_text.setText(Details);

        LinearLayout.LayoutParams detailsParam = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        detailsParam.setMargins(0, 0, 0,topmargin);

        detail_text.setLayoutParams(detailsParam);



        ///main layout view set
        layout.addView(linearLayout);
        layout.addView(detail_text);

    }

   private void loadEpisodes(){
       Episode_Details_input episode_details_input = new Episode_Details_input();

       episode_details_input.setAuthtoken(authTokenStr);
       episode_details_input.setPermalink(_permalink);
       episode_details_input.setSeries_number("");
       episode_details_input.setLimit("10");
       episode_details_input.setOffset("1");
       // episode_details_input.setSeries_number("1");
       episode_details_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
       GetEpisodeDeatailsAsynTask getEpisodeDeatailsAsynTask = new GetEpisodeDeatailsAsynTask(episode_details_input, this, this);
       getEpisodeDeatailsAsynTask.executeOnExecutor(threadPoolExecutor);

       Log.v("SUBHASS","episode days");
   }



    public void clickItem(EpisodesListModel item, int position) {

       /* Intent intent=new Intent(ProgramDetailsActivity.this,ProgramPlayerActivity.class);
        startActivity(intent);*/
        Log.v("SUBHASS","called after startbutton clicked == ");

        itemToPlay = item;
        ItemClickedPosition = position;



        SubTitleName.clear();
        SubTitlePath.clear();
        ResolutionUrl.clear();
        ResolutionFormat.clear();
        SubTitleLanguage.clear();
      /*  Util.offline_url.clear();
        Util.offline_language.clear();*/


        Log.v("SUBHA", "stream id === " + item.getEpisodeStreamUniqueId());

        playerModel = new Player();
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

        if(contentTypesId == 3){
            playerModel.setPosterImageId(item.getEpisodeThumbnailImageView());

        }else {
            playerModel.setPosterImageId(posterImageId);
        }

        LogUtil.showLog("MUVI", "content typesid = " + contentTypesId);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if (isLogin == 1) {
            if (loggedInStr != null) {
                if (NetworkStatus.getInstance().isConnected(this)) {

                    callForValidateUser();

                } else {
                    Util.showToast(ExpertsDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                    //  Toast.makeText(ProgramDetailsActivity.this,Util.getTextofLanguage(ProgramDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
                }

            } else {



                Intent registerActivity = new LoginRegistrationOnContentClickHandler(ExpertsDetailsActivity.this).handleClickOnContent();
                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Util.check_for_subscription = 1;
                registerActivity.putExtra("PLAY_LIST", itemData);
                registerActivity.putExtra("TAG", ItemClickedPosition);
                registerActivity.putExtra("PlayerModel", playerModel);
                registerActivity.putExtra("PERMALINK", _permalink);
                registerActivity.putExtra("SEASON", 0);
//                registerActivity.putExtra("Current_SEASON", getIntent().getStringExtra(SEASON_INTENT_KEY));
//                registerActivity.putExtra(PERMALINK_INTENT_ARRAY, getIntent().getSerializableExtra(PERMALINK_INTENT_ARRAY));
                registerActivity.putExtra("Index", 0);
                registerActivity.putExtra("Current_SEASON", 0);
                startActivityForResult(registerActivity,VIDEO_PLAY_BUTTON_CLICK_LOGIN_REG_REQUESTCODE);
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


                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ExpertsDetailsActivity.this, ExpertsDetailsActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(ExpertsDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                //Toast.makeText(ProgramDetailsActivity.this,Util.getTextofLanguage(ProgramDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
            }
        }


    }


    public  void callForValidateUser()
    {
       Util.dataModel.setPurchase_type("episode");
       Util.dataModel.setSeason_id(itemData.get(0).getEpisodeSeriesNo());

        ValidateUserInput validateUserInput = new ValidateUserInput();
        validateUserInput.setAuthToken(authTokenStr);
        validateUserInput.setUserId(preferenceManager.getUseridFromPref());
        validateUserInput.setMuviUniqueId(movieUniqueId.trim());
        validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
        validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
        validateUserInput.setEpisodeStreamUniqueId(itemData.get(0).getEpisodeStreamUniqueId());
        validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, ExpertsDetailsActivity.this, ExpertsDetailsActivity.this);
        asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
    }

    private void ShowPpvPopUp(){
        {


            try {
                if (Util.currencyModel.getCurrencySymbol() == null) {
                    Util.showToast(ExpertsDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));

                    // Toast.makeText(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Util.showToast(ExpertsDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));

                // Toast.makeText(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExpertsDetailsActivity.this, R.style.MyAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) ExpertsDetailsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

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
                    final Intent showPaymentIntent = new Intent(ExpertsDetailsActivity.this, PPvPaymentInfoActivity.class);
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
                    showPaymentIntent.putExtra("PERMALINK", _permalink);
                    showPaymentIntent.putExtra("SEASON",0);
                    showPaymentIntent.putExtra("Current_SEASON", 0);
                    showPaymentIntent.putExtra("Index","0");

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


    private void PlayThroughChromeCast(){
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

                if (contentTypesId == 3){
                    jsonObj.put("episode_id", playerModel.getStreamUniqueId());
                }
                else{
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


                Log.v("SUBHASHREE","authTokenStr" + authTokenStr);
                Log.v("SUBHASHREE","user_id" + preferenceManager.getUseridFromPref());
                Log.v("SUBHASHREE","ipAddres " + ipAddres.trim());
                Log.v("SUBHASHREE","playerModel.getMovieUniqueId()" + playerModel.getMovieUniqueId());
                Log.v("SUBHASHREE","watch_status_String" + watch_status_String);
                Log.v("SUBHASHREE","seek_status" + seek_status);
                Log.v("SUBHASHREE","domain_name" + BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));


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


}



