package com.home.vod.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.home.apisdk.apiController.GetVideoLogsAsynTask;
import com.home.apisdk.apiController.HeaderConstants;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.RelatedContentInput;
import com.home.apisdk.apiModel.RelatedContentOutput;
import com.home.apisdk.apiModel.VideoLogsInputModel;
import com.home.vod.BuildConfig;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.MyDownloadIntentHandler;
import com.home.vod.R;
import com.home.vod.SearchIntentHandler;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.LanguageModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.ResizableCustomView;
import com.home.vod.util.SensorOrientationChangeNotifier;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.vod.preferences.LanguagePreference.BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DETAILS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PROGRAM_BUTTON;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
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

public class ProgrammeActivity extends AppCompatActivity implements SensorOrientationChangeNotifier.Listener,GetRelatedContentAsynTask.GetRelatedContentListener, GetContentDetailsAsynTask.GetContentDetailsListener, DeleteFavAsync.DeleteFavListener, AddToFavAsync.AddToFavListener,
        GetIpAddressAsynTask.IpAddressListener, GetLanguageListAsynTask.GetLanguageListListener {

    TextView detailsTextView, videoStoryTextView, colortitle, colortitle1, benefitsTitleTextView, benefitsStoryTextView, durationTitleTextView, diffcultyTitleTextView, difficulty, days, lineTextview;
    ImageView bannerImageView, playButton, moviePoster, share;
    Button startProgramButton, dietPlanButton;
    ProgressBarHandler pDialog;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout, image_logo;
    LinearLayout story_layout;
    String movieUniqueId = "";
    String movieTrailerUrlStr = "", isEpisode = "";
    String duration = "";
    String videoduration = "";
    String[] season;
    String name;
    String difficulty_level;
    String repetition;
    String email, id;
    String ipAddres = "";
    String movieDetailsStr = "";
    String benefits;
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

        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
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
        Toast.makeText(ProgrammeActivity.this,"video done",Toast.LENGTH_SHORT).show();

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
                if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
    Timer center_pause_paly_timer;
    boolean center_pause_paly_timer_is_running = false;
    public boolean isFastForward = false;
    boolean video_completed = false;
    int screenWidth, screenHeight;
    LinearLayout primary_ll, last_ll;
    boolean compressed = true;
    /* Added for trailer player ---------*/
        ///by nihar
    LinearLayout view_below;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_programme);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(ProgrammeActivity.this);
        playButton = (ImageView) findViewById(R.id.playButton);
        detailsTextView = (TextView) findViewById(R.id.detailsTextView);
        difficulty = (TextView) findViewById(R.id.difficulty);
        lineTextview = (TextView) findViewById(R.id.lineTextview);
        days = (TextView) findViewById(R.id.days);
        videoStoryTextView = (TextView) findViewById(R.id.videoStoryTextView);
        benefitsTitleTextView = (TextView) findViewById(R.id.benefitsTitleTextView);
        colortitle = (TextView) findViewById(R.id.colortitle);
        colortitle1 = (TextView) findViewById(R.id.colortitle1);
        benefitsStoryTextView = (TextView) findViewById(R.id.benefitsStoryTextView);
        startProgramButton = (Button) findViewById(R.id.startProgramButton);
        dietPlanButton = (Button) findViewById(R.id.dietPlanButton);
        durationTitleTextView = (TextView) findViewById(R.id.durationTitleTextView);
        diffcultyTitleTextView = (TextView) findViewById(R.id.diffcultyTitleTextView);
        favorite_view_episode = (ImageView) findViewById(R.id.favoriteImageView);
        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        share = (ImageView) findViewById(R.id.share);
        image_logo = (RelativeLayout) findViewById(R.id.logo_image);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        player_layout = (RelativeLayout) findViewById(R.id.player_layout);
        episodeListOptionMenuHandler = new EpisodeListOptionMenuHandler(this);
        colortitle.setVisibility(View.GONE);
        colortitle1.setVisibility(View.GONE);
        lineTextview.setVisibility(View.GONE);

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
            if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
            if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
                        if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
                        if(ProgrammeActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
        lineTextview.setVisibility(View.GONE);


        image_logo.bringToFront();

        FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.regular_fonts), startProgramButton);
        FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.regular_fonts), dietPlanButton);

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
                try {

                   // mHandler.removeCallbacks(updateTimeTask);

                    current_time.setVisibility(View.VISIBLE);
                    SensorOrientationChangeNotifier.getInstance(ProgrammeActivity.this).addListener(ProgrammeActivity.this);
                    emVideoView.start();
                    seekBar.setProgress(emVideoView.getCurrentPosition());
                    updateProgressBar();

                    ////nihar
                } catch (Exception e) {
                }
            }
        });


        startProgramButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), SeasonActivity.class);
                i.putExtra(PERMALINK_INTENT_KEY, permalinkStr);
                startActivity(i);
                bannerImageRelativeLayout.setVisibility(View.VISIBLE);
                player_layout.setVisibility(View.GONE);
            }
        });

        dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelatedContentInput relatedContentInput = new RelatedContentInput();
                LogUtil.showLog("SUBHA", "conten" + contentId + "hf" + muviStreamId);

                relatedContentInput.setAuthToken(authTokenStr);
                relatedContentInput.setContentId(contentId);
                relatedContentInput.setContent_stream_id(muviStreamId);
                relatedContentInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                GetRelatedContentAsynTask asyngetRelatedContent = new GetRelatedContentAsynTask(relatedContentInput, ProgrammeActivity.this, ProgrammeActivity.this);
                asyngetRelatedContent.executeOnExecutor(threadPoolExecutor);

            }
        });

       /* dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgrammeActivity.this, DietPlanActivity.class);
                startActivity(intent);
            }
        });*/

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.shareIt(ProgrammeActivity.this);
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

                        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, ProgrammeActivity.this, ProgrammeActivity.this);
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

                        AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ProgrammeActivity.this, ProgrammeActivity.this);
                        asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);


                    }
                } else {
                    Util.favorite_clicked = true;

                    Intent registerActivity = new Intent(ProgrammeActivity.this, RegisterActivity.class);
                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    registerActivity.putExtra("from", this.getClass().getName());
                    startActivityForResult(registerActivity, 30060);

                    bannerImageRelativeLayout.setVisibility(View.VISIBLE);
                    player_layout.setVisibility(View.GONE);

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
                final Intent searchIntent = new SearchIntentHandler(ProgrammeActivity.this).handleSearchIntent();
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(ProgrammeActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(ProgrammeActivity.this, RegisterActivity.class);
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

                final Intent mydownload = new MyDownloadIntentHandler(ProgrammeActivity.this).handleDownloadIntent();
                startActivity(mydownload);
                // Not implemented here
                return false;

            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(ProgrammeActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(ProgrammeActivity.this, PurchaseHistoryActivity.class);
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
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
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
            name = contentDetailsOutput.getName();
            contentId = contentDetailsOutput.getId();
            muviStreamId = contentDetailsOutput.getMovieStreamId();
            movieTrailerUrlStr = contentDetailsOutput.getTrailerUrl();
            contentTypesId = contentDetailsOutput.getContentTypesId();

            lineTextview.setVisibility(View.VISIBLE);

            benefitsTitleTextView.setText(languagePreference.getTextofLanguage(BENEFIT_TITLE, DEFAULT_BENEFIT_TITLE));
            durationTitleTextView.setText(languagePreference.getTextofLanguage(DURATION_TITLE, DEFAULT_DURATION_TITLE));
            diffcultyTitleTextView.setText(languagePreference.getTextofLanguage(DIFFICULTY_TITLE, DEFAULT_DIFFICULTY_TITLE));
            startProgramButton.setText(languagePreference.getTextofLanguage(PROGRAM_BUTTON, DEFAULT_PROGRAM_BUTTON));
            dietPlanButton.setText(languagePreference.getTextofLanguage(DIET_BUTTON, DEFAULT_DIET_BUTTON));


            if (benefits.matches("") || benefits.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                benefitsTitleTextView.setVisibility(View.GONE);
                colortitle1.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.regular_fonts), benefitsTitleTextView);
                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.light_fonts), benefitsStoryTextView);
                benefitsStoryTextView.setText(benefits.trim());
                colortitle1.setVisibility(View.VISIBLE);
            }

            if (name.matches("") || name.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                detailsTextView.setVisibility(View.GONE);
                colortitle.setVisibility(View.GONE);

            } else {


                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.regular_fonts), detailsTextView);

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
            if (season != null && season.length > 0) {
                startProgramButton.setVisibility(View.VISIBLE);
            } else {
                startProgramButton.setVisibility(View.GONE);
            }
            if (duration.matches("")) {
                durationTitleTextView.setVisibility(View.GONE);
                lineTextview.setVisibility(View.GONE);

            } else {

                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.regular_fonts), durationTitleTextView);
                days.setText(duration);
            }
            if (difficulty_level.matches("")) {
                diffcultyTitleTextView.setVisibility(View.GONE);
                lineTextview.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.regular_fonts), diffcultyTitleTextView);
                difficulty.setText(difficulty_level);
            }

            if (difficulty_level.matches("") && duration != null) {
                lineTextview.setVisibility(View.GONE);
                days.setGravity(Gravity.CENTER);
            } else if (duration.matches("") && difficulty_level != null) {
                lineTextview.setVisibility(View.GONE);
                difficulty.setGravity(Gravity.CENTER);
            }
            // Util.favorite_clicked = false;


            /***favorite *****/
            if (movieDetailsStr.matches("") || movieDetailsStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoStoryTextView.setVisibility(View.GONE);

            } else {
                //  videoStoryTextView.setMaxLines(3);
                videoStoryTextView.setVisibility(View.VISIBLE);

                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.light_fonts), videoStoryTextView);

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

                    Picasso.with(ProgrammeActivity.this)
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

                Picasso.with(ProgrammeActivity.this)
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
                    Log.v("SUBHA", "favorite ----");

                    AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                    addToFavInputModel.setAuthToken(authTokenStr);
                    addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                    addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                    addToFavInputModel.setIsEpisodeStr(isEpisode);

                    AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ProgrammeActivity.this, ProgrammeActivity.this);
                    asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);
                } else if (loggedInStr != null && isFavorite == 1) {

                    favorite_view_episode.setImageResource(R.drawable.favorite_red);
                }

            } else {
                favorite_view_episode.setVisibility(View.GONE);
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
        /***************chromecast**********************/
        invalidateOptionsMenu();


    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        ProgrammeActivity.this.sucessMsg = sucessMsg;
        favorite_view_episode.setImageResource(R.drawable.favorite);
        showToast();
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
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
        pDialog.show();
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        if (status == 200) {


            //pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            ProgrammeActivity.this.sucessMsg = sucessMsg;
            String loggedInStr = preferenceManager.getLoginStatusFromPref();

            favorite_view_episode.setImageResource(R.drawable.favorite_red);
            isFavorite = 1;

            showToast();
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
            String permalinkStr = relatedContentOutput.getPermalink().substring(relatedContentOutput.getPermalink().lastIndexOf("/") + 1);
            LogUtil.showLog("SUBHA", "getPermalink()" + permalinkStr);


            Intent intent = new Intent(ProgrammeActivity.this, DietPlanActivity.class);
            intent.putExtra(HeaderConstants.VLINK, permalinkStr);
            startActivity(intent);

        } else
            Toast.makeText(ProgrammeActivity.this, "There is No Diet Plan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetRelatedContentPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
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
                Toast.makeText(ProgrammeActivity.this,"video done 1",Toast.LENGTH_SHORT).show();
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


}



