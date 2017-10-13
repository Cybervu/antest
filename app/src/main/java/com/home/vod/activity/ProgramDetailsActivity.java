package com.home.vod.activity;

/**
 * Created by MUVI on 10/10/2017.
 *
 */

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetEpisodeDeatailsAsynTask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.vod.BuildConfig;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.MonetizationHandler;
import com.home.vod.R;

import com.home.vod.adapter.ProgramDetailsAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
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
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import player.activity.AdPlayerActivity;
import player.activity.ExoPlayerActivity;
import player.activity.Player;

import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_ALL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.VIEW_ALL;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.SEASON_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

public class ProgramDetailsActivity extends AppCompatActivity implements GetContentDetailsAsynTask.GetContentDetailsListener,GetEpisodeDeatailsAsynTask.GetEpisodeDetailsListener,GetIpAddressAsynTask.IpAddressListener, GetValidateUserAsynTask.GetValidateUserListener,
        VideoDetailsAsynctask.VideoDetailsListener{

    ImageView bannerImageView, playButton, share;
    TextView detailsTextView, durationTitleTextView, durationTextView, tutorialTextView, viewAllTextView;
    Button startWorkoutButton, dietPlanButton;
    RecyclerView featureContent;
    ProgressBarHandler progressBarHandler;
    GetValidateUserAsynTask asynValidateUserDetails;
    VideoDetailsAsynctask asynLoadVideoUrls;
    ArrayList<EpisodesListModel> itemData;
    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    PreferenceManager preferenceManager;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout,logo_image;
    RecyclerView seasontiveLayout;
    Player playerModel;
    String movieUniqueId = "";
    DataModel dbModel = new DataModel();
    int isLogin = 0;
    RecyclerView.LayoutManager mLayoutManager;
    Toolbar mActionBarToolbar;
    String episodeVideoUrlStr;
    String email, id;
    String filename = "";
    int ItemClickedPosition = 0;
    int position;
    static File mediaStorageDir;
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
    String bannerImageId, posterImageId, duration;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    LanguagePreference languagePreference;
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

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
            if (status == 427) {


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
            } else if (status == 429 || status == 430) {
                new MonetizationHandler(ProgramDetailsActivity.this).handle429OR430statusCod(validUserStr, message, subscription_Str);


            } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                ShowPpvPopUp();
            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
                Intent intent = new Intent(ProgramDetailsActivity.this, SubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
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

                    Log.v("SUBHA","Video PLayer Called 1");

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

        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int code, String status, String message) {


        // _video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // _video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

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

        Log.v("SUBHA","play video ==== "+ play_video);
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

Log.v("SUBHA","code == player == "+ code);
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


                    if (mCastSession != null && mCastSession.isConnected()) {
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
                                jsonObj.put("ip_address", ipAddres.trim());
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
                                    playVideoIntent.putExtra("PLAY_LIST",itemData);
                                    playVideoIntent.putExtra("TAG",ItemClickedPosition);
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
                    Log.v("Nihar","==================="+itemData.size());
                    playVideoIntent.putExtra("PLAY_LIST",itemData);
                    playVideoIntent.putExtra("TAG",ItemClickedPosition);
                    startActivity(playVideoIntent);
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            Util.showNoDataAlert(ProgramDetailsActivity.this);
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

        setContentView(R.layout.activity_program_details);
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        detailsTextView = (TextView) findViewById(R.id.titleTextView);
        startWorkoutButton = (Button) findViewById(R.id.startWorkoutButton);
        dietPlanButton = (Button) findViewById(R.id.dietPlanButton);
        durationTitleTextView = (TextView) findViewById(R.id.durationTitleTextView);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        tutorialTextView = (TextView) findViewById(R.id.tutorialTextView);
        viewAllTextView = (TextView) findViewById(R.id.viewAllTextView2);
        seasontiveLayout = (RecyclerView) findViewById(R.id.featureContent);
        share = (ImageView) findViewById(R.id.share);
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
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        logo_image = (RelativeLayout) findViewById(R.id.logo_image);
        logo_image.bringToFront();
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        detailsTextView.setText(languagePreference.getTextofLanguage(SEASON, DEFAULT_SEASON) + " " + getIntent().getStringExtra(SEASON_INTENT_KEY));

        viewAllTextView.setVisibility(View.GONE);
        dietPlanButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mLayoutManager = new LinearLayoutManager(ProgramDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

        viewAllTextView.setText(languagePreference.getTextofLanguage(VIEW_ALL, DEFAULT_VIEW_ALL));



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

        viewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent episode = new Intent(ProgramDetailsActivity.this, Tutorial_List_Activity.class);
                episode.putExtra(PERMALINK_INTENT_KEY, permalinkStr);
                episode.putExtra(SEASON_INTENT_KEY, getIntent().getStringExtra(SEASON_INTENT_KEY));
                startActivity(episode);
            }
        });

        dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgramDetailsActivity.this, DietPlanActivity.class);
                startActivity(intent);
            }
        });


        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(itemData.get(0),0);
               /* Intent  playVideoIntent = new Intent(ProgramDetailsActivity.this, ProgramPlayerActivity.class);
                playVideoIntent.putExtra("PlayerModel", playerModel);
                playVideoIntent.putExtra("PLAY_LIST",itemData);
                Log.v("Nihar","==================="+itemData.size());
                playVideoIntent.putExtra("TAG","0");
                startActivity(playVideoIntent);*/
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
        episode_details_input.setLimit("10");
        episode_details_input.setOffset("1");
        episode_details_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,DEFAULT_SELECTED_LANGUAGE_CODE));
        GetEpisodeDeatailsAsynTask getEpisodeDeatailsAsynTask=new GetEpisodeDeatailsAsynTask(episode_details_input,this,this);
        getEpisodeDeatailsAsynTask.executeOnExecutor(threadPoolExecutor);

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.shareIt(ProgramDetailsActivity.this);
            }
        });


        dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgramDetailsActivity.this, DietPlanActivity.class);
                startActivity(intent);
            }
        });
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
                final Intent searchIntent = new Intent(ProgramDetailsActivity.this, SearchActivity.class);
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

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.action_mydownload:

                Intent mydownload = new Intent(ProgramDetailsActivity.this, MyDownloads.class);
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

            movieUniqueId=contentDetailsOutput.getMuviUniqId();
            bannerImageId = contentDetailsOutput.getBanner();
            posterImageId = contentDetailsOutput.getPoster();
            duration = contentDetailsOutput.getDuration();

           // viewAllTextView.setText(languagePreference.getTextofLanguage(DETAIL_VIEW_MORE,DEFAULT_DETAIL_VIEW_MORE));
            viewAllTextView.setVisibility(View.VISIBLE);
            tutorialTextView.setText(languagePreference.getTextofLanguage(TUTORIAL_TITLE,DEFAULT_TUTORIAL_TITLE));
            durationTitleTextView.setText(languagePreference.getTextofLanguage(DURATION_TITLE, DEFAULT_DURATION_TITLE));

            if (duration.matches("")) {
                durationTitleTextView.setVisibility(View.GONE);
            } else {

                FontUtls.loadFont(ProgramDetailsActivity.this, getResources().getString(R.string.light_fonts), durationTitleTextView);
                durationTitleTextView.setTypeface(null, Typeface.BOLD);
                durationTextView.setText(duration);
            }

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
                        .load(posterImageId)
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .into(bannerImageView);


            }

        }

    }

    @Override
    public void onGetEpisodeDetailsPreExecuteStarted() {

    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(Episode_Details_output episode_details_output, int status, int i, String message, String movieUniqueId) {

        try {
            if (progressBarHandler != null && progressBarHandler.isShowing()) {
                LogUtil.showLog("PINTU", "getepisodedetails pdlog hide");
                progressBarHandler.hide();

            }
        } catch (IllegalArgumentException ex) {

        }


        LogUtil.showLog("MUVI", "episode show...");



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


                //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
            } else {


                LogUtil.showLog("BISHAL", "data show...");
                seasontiveLayout.setVisibility(View.VISIBLE);
                seasontiveLayout.setLayoutManager(mLayoutManager);
                seasontiveLayout.setItemAnimator(new DefaultItemAnimator());
                ProgramDetailsAdapter mAdapter = new ProgramDetailsAdapter(ProgramDetailsActivity.this, R.layout.list_card_program_details, itemData, new ProgramDetailsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EpisodesListModel item,int position) {
                        clickItem(item, position);

                    }
                });
                seasontiveLayout.setAdapter(mAdapter);

            }
        }

    }

    public void clickItem (EpisodesListModel item , int position ){

       /* Intent intent=new Intent(ProgramDetailsActivity.this,ProgramPlayerActivity.class);
        startActivity(intent);*/


        itemToPlay = item;
        ItemClickedPosition = position ;

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


        Log.v("SUBHA","stream id === "+ item.getEpisodeStreamUniqueId());

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
        playerModel.setPosterImageId(posterImageId);

        LogUtil.showLog("MUVI", "content typesid = " + contentTypesId);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if (isLogin == 1) {
            if (loggedInStr != null) {
                if (NetworkStatus.getInstance().isConnected(this)) {

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

                } else {
                    Util.showToast(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                    //  Toast.makeText(ProgramDetailsActivity.this,Util.getTextofLanguage(ProgramDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
                }

            } else {

                final Intent register = new Intent(ProgramDetailsActivity.this, RegisterActivity.class);

                runOnUiThread(new Runnable() {
                    public void run() {
                        register.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Util.check_for_subscription = 1;
                        register.putExtra("PlayerModel", playerModel);
                        startActivity(register);


                    }
                });
            }
        } else {
            if (NetworkStatus.getInstance().isConnected(this)) {
                // MUVIlaxmi

                Log.v("SUBHA","Video PLayer Called 2");

                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(authTokenStr);
                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());


                LogUtil.showLog("SUBHA","Video PLayer Called authTokenStr 2 "+authTokenStr);
                Log.v("SUBHA","Video PLayer Called movie uniqueid 2 "+dbModel.getMovieUniqueId().trim());
                Log.v("SUBHA","Video PLayer Called movie StreamUniqueid 2 "+dbModel.getStreamUniqueId().trim());
                Log.v("SUBHA","Video PLayer Called inyternet speed 2 "+MainActivity.internetSpeed.trim());
                Log.v("SUBHA","Video PLayer Called  user id 2 "+preferenceManager.getUseridFromPref());




                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ProgramDetailsActivity.this, ProgramDetailsActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(ProgramDetailsActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                //Toast.makeText(ProgramDetailsActivity.this,Util.getTextofLanguage(ProgramDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
            }
        }


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
                playVideoIntent.putExtra("PLAY_LIST",itemData);
                Log.v("Nihar","==================="+itemData.size());
                playVideoIntent.putExtra("TAG",ItemClickedPosition);
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
}