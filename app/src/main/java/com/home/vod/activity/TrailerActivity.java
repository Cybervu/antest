
package com.home.vod.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.home.apisdk.apiController.GetFFVideoLogDetailsAsync;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetVideoLogsAsynTask;
import com.home.apisdk.apiController.ResumeVideoLogDetailsAsync;
import com.home.apisdk.apiModel.FFVideoLogDetailsInput;
import com.home.apisdk.apiModel.ResumeVideoLogDetailsInput;
import com.home.apisdk.apiModel.VideoLogsInputModel;
import com.home.apisdk.apiModel.Video_Log_Output_Model;
import com.home.vod.R;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ExpandableTextView;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ResizableCustomView;
import com.home.vod.util.SensorOrientationChangeNotifier;
import com.home.vod.util.Util;

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
import static com.home.vod.preferences.LanguagePreference.CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.util.Constant.authTokenStr;


/*enum ContentTypes1 {
    DASH("application/dash+xml"), HLS("application/vnd.apple.mpegurl"), PDCF(
            "video/mp4"), M4F("video/mp4"), DCF("application/vnd.oma.drm.dcf"), BBTS(
            "video/mp2t");
    String mediaSourceParamsContentType = null;

    private ContentTypes1(String mediaSourceParamsContentType) {
        this.mediaSourceParamsContentType = mediaSourceParamsContentType;
    }

    public String getMediaSourceParamsContentType() {
        return mediaSourceParamsContentType;
    }
}*/

public class TrailerActivity extends AppCompatActivity implements
        SensorOrientationChangeNotifier.Listener,
        GetVideoLogsAsynTask.GetVideoLogsListener,GetIpAddressAsynTask.IpAddressListener,
        GetFFVideoLogDetailsAsync.GetFFVideoLogsListener,ResumeVideoLogDetailsAsync.ResumeVideoLogDetailsListener{
   // int played_length = 0;
    int playerStartPosition = 0;
    ImageView subtitle_change_btn;
    private static final int MAX_LINES = 2;

    Timer timer;
    private Handler threadHandler = new Handler();
    String videoLogId = "0";
    String watchStatus = "start";
    int playerPosition = 0;
    String watchSt = "halfplay";
    public boolean isFastForward = false;
    public int playerPreviousPosition = 0;
    TimerTask timerTask;
    String emailIdStr = "";
    String userIdStr = "";
    String movieId = "";
    String episodeId = "0";
    GetVideoLogsAsynTask asyncVideoLogDetails;
    GetFFVideoLogDetailsAsync asyncFFVideoLogDetails;

    GetIpAddressAsynTask asynGetIpAddress;

    ImageButton back, center_play_pause;
    ImageView compress_expand;
    SeekBar seekBar;
    private Handler mHandler = new Handler();
    Timer center_pause_paly_timer;
    String Current_Time, TotalTime;
    TextView current_time, total_time;
    ProgressBar progressView;
    LinearLayout primary_ll, last_ll;
    boolean video_completed = false;
    // TextView detais_text;
    TextView ipAddressTextView;
    TextView emailAddressTextView;
    TextView dateTextView;
    long previous_matching_time = 0, current_matching_time = 0;
    boolean center_pause_paly_timer_is_running = false;
    RelativeLayout player_layout;


    boolean compressed = true;
    int player_layout_height, player_layout_width;
    int screenWidth, screenHeight;
    ImageButton latest_center_play_pause;


    String resolution = "BEST";

    String ipAddressStr = "";
    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    PreferenceManager preferenceManager;
    //Toolbar mActionBarToolbar;
    LinearLayout linearLayout1;

    TextView videoTitle, GenreTextView, videoDurationTextView, videoCensorRatingTextView, videoCensorRatingTextView1, videoReleaseDateTextView,
             videoCastCrewTitleTextView;
    TextView story;
    private EMVideoView emVideoView;
    int seek_label_pos = 0;
    int content_types_id = 0;
    boolean censor_layout = true;
    LanguagePreference languagePreference;
    
    @Override
    protected void onResume() {
        super.onResume();
        SensorOrientationChangeNotifier.getInstance(TrailerActivity.this).addListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_video_player);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        content_types_id = Util.dataModel.getContentTypesId();
       // played_length = Util.dataModel.getPlayPos() * 1000;


        if (Util.dataModel.getVideoUrl().matches("")) {
            backCalled();
            //onBackPressed();
        }
        movieId = Util.dataModel.getMovieUniqueId();
        episodeId = Util.dataModel.getEpisode_id();

        if (preferenceManager != null) {
            emailIdStr = preferenceManager.getEmailIdFromPref();
            userIdStr = preferenceManager.getUseridFromPref();
            LogUtil.showLog("BKS","userid trailer=="+userIdStr);
            if (emailIdStr==null){
                emailIdStr = "";
            }
            if (userIdStr==null){
                userIdStr = "";
            }

        } else {
            emailIdStr = "";
            userIdStr = "";
        }


        emVideoView = (EMVideoView) findViewById(R.id.emVideoView);
        subtitle_change_btn = (ImageView) findViewById(R.id.subtitle_change_btn);
        subtitle_change_btn.setVisibility(View.GONE);

        latest_center_play_pause = (ImageButton) findViewById(R.id.latest_center_play_pause);
        videoTitle = (TextView) findViewById(R.id.videoTitle);

        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.regular_fonts),videoTitle);
        GenreTextView = (TextView) findViewById(R.id.GenreTextView);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),GenreTextView);

        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),videoDurationTextView);

        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),videoCensorRatingTextView);
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),videoCensorRatingTextView1);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),videoReleaseDateTextView);

        story = (TextView) findViewById(R.id.videoStoryTextView);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),story);
        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        FontUtls.loadFont(TrailerActivity.this, getResources().getString(R.string.light_fonts),videoCastCrewTitleTextView);

        videoCastCrewTitleTextView.setText(languagePreference.getTextofLanguage(CAST_CREW_BUTTON_TITLE,DEFAULT_CAST_CREW_BUTTON_TITLE));

        //Call For Subtitle Loading // Added By Bibhu





        if (Util.dataModel.getVideoTitle().trim() != null)

        {
            videoTitle.setText(Util.dataModel.getVideoTitle().trim());
            videoTitle.setVisibility(View.VISIBLE);
        } else {
            videoTitle.setVisibility(View.GONE);
        }


        if (Util.dataModel.getVideoGenre().trim() != null && !Util.dataModel.getVideoGenre().trim().matches(""))

        {
            GenreTextView.setText(Util.dataModel.getVideoGenre().trim());
            GenreTextView.setVisibility(View.VISIBLE);
        } else {
            GenreTextView.setVisibility(View.GONE);
        }


        if (Util.dataModel.getVideoDuration().trim() != null && !Util.dataModel.getVideoDuration().trim().matches(""))

        {
            videoDurationTextView.setText(Util.dataModel.getVideoDuration().trim());
            videoDurationTextView.setVisibility(View.VISIBLE);
            censor_layout = false;
        } else {

            videoDurationTextView.setVisibility(View.GONE);
        }

        if (Util.dataModel.getCensorRating().trim() != null && !Util.dataModel.getCensorRating().trim().matches("")) {
            if ((Util.dataModel.getCensorRating().trim()).contains("_")) {
                String Data[] = (Util.dataModel.getCensorRating().trim()).split("-");
                videoCensorRatingTextView.setVisibility(View.VISIBLE);
                videoCensorRatingTextView1.setVisibility(View.VISIBLE);
                videoCensorRatingTextView.setText(Data[0]);
                videoCensorRatingTextView1.setText(Data[-1]);
                censor_layout = false;
            } else {
                censor_layout = false;

                videoCensorRatingTextView.setVisibility(View.VISIBLE);
                videoCensorRatingTextView1.setVisibility(View.GONE);
                videoCensorRatingTextView.setText(Util.dataModel.getCensorRating().trim());
            }
        } else {

            videoCensorRatingTextView.setVisibility(View.GONE);
            videoCensorRatingTextView1.setVisibility(View.GONE);
        }
        if (Util.dataModel.getCensorRating().trim() != null && Util.dataModel.getCensorRating().trim()
                .equalsIgnoreCase(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA))) {
            videoCensorRatingTextView.setVisibility(View.GONE);
            videoCensorRatingTextView1.setVisibility(View.GONE);
        }

        if (Util.dataModel.getVideoReleaseDate().trim() != null && !Util.dataModel.getVideoReleaseDate().trim().matches(""))

        {
            videoReleaseDateTextView.setText(Util.dataModel.getVideoReleaseDate().trim());
            videoReleaseDateTextView.setVisibility(View.VISIBLE);
            censor_layout = false;
        } else {
            videoReleaseDateTextView.setVisibility(View.GONE);
        }

        if(censor_layout) {

            findViewById(R.id.durationratingLiearLayout).setVisibility(View.GONE);
        }

        if (Util.dataModel.getVideoStory().trim() != null && !Util.dataModel.getVideoStory().trim().matches(""))

        {
            story.setText(Util.dataModel.getVideoStory());
            story.setVisibility(View.VISIBLE);
            ResizableCustomView.doResizeTextView(TrailerActivity.this,story,MAX_LINES, languagePreference.getTextofLanguage(VIEW_MORE,DEFAULT_VIEW_MORE), true);

        } else {
            story.setVisibility(View.GONE);
        }

        if (Util.dataModel.isCastCrew() == true)

        {
            videoCastCrewTitleTextView.setVisibility(View.VISIBLE);
        } else {
            videoCastCrewTitleTextView.setVisibility(View.GONE);
        }


        videoCastCrewTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkStatus.getInstance().isConnected(TrailerActivity.this)) {
                    //Will Add Some Data to send
                    Util.call_finish_at_onUserLeaveHint = false;
                    Util.hide_pause = true;
                    findViewById(R.id.progress_view).setVisibility(View.GONE);
                    latest_center_play_pause.setVisibility(View.VISIBLE);

                    if (emVideoView.isPlaying()) {
                        emVideoView.pause();
                        latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
                        center_play_pause.setImageResource(R.drawable.ic_media_play);
                        mHandler.removeCallbacks(updateTimeTask);
                    }


                    final Intent detailsIntent = new Intent(TrailerActivity.this, CastAndCrewActivity.class);
                    detailsIntent.putExtra("cast_movie_id", movieId.trim());
                    detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(detailsIntent);
                } else {
                    Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION,DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }
            }
        });


        player_layout = (RelativeLayout) findViewById(R.id.player_layout);
        player_layout_height = player_layout.getHeight();
        player_layout_width = player_layout.getWidth();

        primary_ll = (LinearLayout) findViewById(R.id.primary_ll);
        last_ll = (LinearLayout) findViewById(R.id.last_ll);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        ipAddressTextView = (TextView) findViewById(R.id.emailAddressTextView);
        emailAddressTextView = (TextView) findViewById(R.id.ipAddressTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        ipAddressTextView.setVisibility(View.GONE);
        emailAddressTextView.setVisibility(View.GONE);
        dateTextView.setVisibility(View.GONE);

        compress_expand = (ImageView) findViewById(R.id.compress_expand);
        back = (ImageButton) findViewById(R.id.back);
       /* back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExoPlayerActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });*/
        //   back.setOnClickListener(ExoPlayerActivity.this);
      /*  back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.showLog("MUVI","CHFHFH");
               // onBackPressed();
                backCalled();
            }
        });*/
        // pause_play = (ImageButton) findViewById(R.id.pause_play);
        seekBar = (SeekBar) findViewById(R.id.progress);
        center_play_pause = (ImageButton) findViewById(R.id.center_play_pause);

        current_time = (TextView) findViewById(R.id.current_time);
        total_time = (TextView) findViewById(R.id.total_time);
        progressView = (ProgressBar) findViewById(R.id.progress_view);


        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        LinearLayout.LayoutParams params1 = null;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
            if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
            if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

            }
            else
            {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
            }
        }
        player_layout.setLayoutParams(params1);

        if (content_types_id == 4) {
            seekBar.setEnabled(false);
            seekBar.setProgress(0);
        } else {
            seekBar.setEnabled(true);
            seekBar.setProgress(0);
        }

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
                    primary_ll.setVisibility(View.VISIBLE);
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


                        primary_ll.setVisibility(View.VISIBLE);




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
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
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

                    LinearLayout.LayoutParams params1 = null;
                    if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                        if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
                        if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                        {
                            params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

                        }
                        else
                        {
                            params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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

       /* back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    back.setImageResource(R.drawable.ic_back);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                }
                return false;
            }
        });*/

        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {



               /* LogUtil.showLog("MUVI","played_length"+played_length);
                LogUtil.showLog("MUVI","emVideoView.getDuration()"+emVideoView.getDuration());
                int duration=emVideoView.getDuration()/1000;
                int hours = duration / 3600;
                int minutes = (duration / 60) - (hours * 60);
                int seconds = duration - (hours * 3600) - (minutes * 60) ;
                String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
                Toast.makeText(getApplicationContext(), "duration is " + duration ,  Toast.LENGTH_LONG).show();*/

             /*   if (Util.dataModel.getPlayPos() >= emVideoView.getDuration() / 1000) {
                    played_length = 0;
                }
*/
                video_completed = false;
                progressView.setVisibility(View.VISIBLE);
                center_play_pause.setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.GONE);


                try {
                  /*  if (emailIdStr != null && !emailIdStr.equalsIgnoreCase("")) {
                        emailAddressTextView.setVisibility(View.VISIBLE);
                        emailAddressTextView.setText(emailIdStr);
                    } else {
                        emailAddressTextView.setVisibility(View.GONE);
                    }
                    if (ipAddres!=null){
                        ipAddressTextView.setVisibility(View.VISIBLE);
                        ipAddressTextView.setText(ipAddres);
                    }else{
                        ipAddressTextView.setVisibility(View.GONE);
                    }
                    String date = new SimpleDateFormat("MMMM dd , yyyy").format(new Date());
                    if (date != null && !date.equalsIgnoreCase("")) {
                        dateTextView.setVisibility(View.VISIBLE);
                        dateTextView.setText(date);
                    } else {
                        dateTextView.setVisibility(View.GONE);
                    }*/

                    //video log
                    if (content_types_id == 4) {


                        VideoLogsInputModel videoLogsInputModel = new VideoLogsInputModel();
                        videoLogsInputModel.setAuthToken(authTokenStr);
                        videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                        videoLogsInputModel.setMuviUniqueId(movieId.trim());
                        videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                        videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                        videoLogsInputModel.setWatchStatus(watchStatus);
                        videoLogsInputModel.setDeviceType("2");
                        videoLogsInputModel.setVideoLogId(videoLogId);
                        videoLogsInputModel.setUserId(userIdStr.trim());
                        asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,TrailerActivity.this,TrailerActivity.this);
                        asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);

                        emVideoView.start();
                        updateProgressBar();
                    } else {
                        startTimer();

                      /*  if (played_length > 0) {
                            ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.GONE);
                            Intent resumeIntent = new Intent(ExoPlayerActivity.this, ResumePopupActivity.class);
                            startActivityForResult(resumeIntent, 1001);
                        } else {*/


                            emVideoView.start();
                            seekBar.setProgress(emVideoView.getCurrentPosition());
                            updateProgressBar();


                        VideoLogsInputModel videoLogsInputModel = new VideoLogsInputModel();
                        videoLogsInputModel.setAuthToken(authTokenStr);
                        videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                        videoLogsInputModel.setMuviUniqueId(movieId.trim());
                        videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                        videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                        videoLogsInputModel.setWatchStatus(watchStatus);
                        videoLogsInputModel.setDeviceType("2");
                        videoLogsInputModel.setVideoLogId(videoLogId);
                        videoLogsInputModel.setUserId(userIdStr.trim());
                        asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,TrailerActivity.this,TrailerActivity.this);
                        asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);



                       // }
                      /*  emVideoView.start();
                        seekBar.setProgress(emVideoView.getCurrentPosition());
                        updateProgressBar();*/

                       /* if (played_length > 0) {
                            emVideoView.seekTo(played_length);
                            seekBar.setProgress(played_length);
                        }else {
                            seekBar.setProgress(emVideoView.getCurrentPosition());
                        }
                        updateProgressBar();*/
                    }
                } catch (Exception e) {
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCalled();
               /* Toast.makeText(ExoPlayerActivity.this, "test", Toast.LENGTH_SHORT).show();
                mHandler.removeCallbacks(updateTimeTask);
                emVideoView.release();
                finish();*/
            }
        });


        emVideoView.setVideoURI(Uri.parse(Util.dataModel.getVideoUrl()));

    }

    @Override
    public void onGetVideoLogsPreExecuteStarted() {
        stoptimertask();
    }

    @Override
    public void onGetVideoLogsPostExecuteCompleted(Video_Log_Output_Model video_log_output_model, int status, String message) {
        startTimer();
        return;
    }


//    private class AsyncVideoLogDetails extends AsyncTask<Void, Void, Void> {
//        //  ProgressDialog pDialog;
//        String responseStr;
//        int statusCode = 0;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.videoLogUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("user_id", userIdStr.trim());
//                httppost.addHeader("ip_address", ipAddres.trim());
//                httppost.addHeader("movie_id", movieId.trim());
//                httppost.addHeader("episode_id", episodeId.trim());
//                httppost.addHeader("played_length", String.valueOf(playerPosition));
//                httppost.addHeader("watch_status", watchStatus);
//                httppost.addHeader("device_type", "2");
//                httppost.addHeader("log_id", videoLogId);
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            videoLogId = "0";
//
//                        }
//
//                    });
//
//                } catch (Exception e) {
//                    videoLogId = "0";
//                    e.printStackTrace();
//
//                }
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                    if (statusCode == 200) {
//                        videoLogId = myJson.optString("log_id");
//                    } else {
//                        videoLogId = "0";
//                    }
//
//                }
//
//            } catch (Exception e) {
//                videoLogId = "0";
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//         /*   try {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
//            } catch (IllegalArgumentException ex) {
//                videoLogId = "0";
//            }*/
//            if (responseStr == null) {
//                videoLogId = "0";
//
//            }
//            startTimer();
//            return;
//
//
//        }

//        @Override
//        protected void onPreExecute() {
//            stoptimertask();
//        }
//
//
//    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                threadHandler.post(new Runnable() {
                    public void run() {
                        if (emVideoView != null) {
                            int currentPositionStr = millisecondsToString(emVideoView.getCurrentPosition());
                            playerPosition = currentPositionStr;


                            if (isFastForward == true) {
                                isFastForward = false;


                                int duration = emVideoView.getDuration() / 1000;
                                if (currentPositionStr > 0 && currentPositionStr == duration) {
                                    FFVideoLogDetailsInput ffVideoLogDetailsInput=new FFVideoLogDetailsInput();
                                    ffVideoLogDetailsInput.setAuthToken(authTokenStr);
                                    ffVideoLogDetailsInput.setUser_id(userIdStr);
                                    ffVideoLogDetailsInput.setIp_address(ipAddressStr.trim());
                                    ffVideoLogDetailsInput.setMovie_id(movieId.trim());
                                    ffVideoLogDetailsInput.setEpisode_id(episodeId.trim());
                                    ffVideoLogDetailsInput.setPlayed_length(String.valueOf(playerPosition));
                                    ffVideoLogDetailsInput.setWatch_status(watchStatus);
                                    ffVideoLogDetailsInput.setDevice_type("2");
                                    ffVideoLogDetailsInput.setLog_id(videoLogId);
                                    asyncFFVideoLogDetails = new GetFFVideoLogDetailsAsync(ffVideoLogDetailsInput,TrailerActivity.this,TrailerActivity.this);
                                    watchStatus = "complete";
                                    asyncFFVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    FFVideoLogDetailsInput ffVideoLogDetailsInput=new FFVideoLogDetailsInput();
                                    ffVideoLogDetailsInput.setAuthToken(authTokenStr);
                                    ffVideoLogDetailsInput.setUser_id(userIdStr);
                                    ffVideoLogDetailsInput.setIp_address(ipAddressStr.trim());
                                    ffVideoLogDetailsInput.setMovie_id(movieId.trim());
                                    ffVideoLogDetailsInput.setEpisode_id(episodeId.trim());
                                    ffVideoLogDetailsInput.setPlayed_length(String.valueOf(playerPosition));
                                    ffVideoLogDetailsInput.setWatch_status(watchStatus);
                                    ffVideoLogDetailsInput.setDevice_type("2");
                                    ffVideoLogDetailsInput.setLog_id(videoLogId);
                                    asyncFFVideoLogDetails = new GetFFVideoLogDetailsAsync(ffVideoLogDetailsInput,TrailerActivity.this,TrailerActivity.this);
                                    watchStatus = "halfplay";
                                    asyncFFVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                }

                            } else if (isFastForward == false && currentPositionStr >= millisecondsToString(playerPreviousPosition)) {

                                playerPreviousPosition = 0;

                                int duration = emVideoView.getDuration() / 1000;
                                if (currentPositionStr > 0 && currentPositionStr == duration) {
                                    VideoLogsInputModel videoLogsInputModel = new VideoLogsInputModel();
                                    videoLogsInputModel.setAuthToken(authTokenStr);
                                    videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                                    videoLogsInputModel.setMuviUniqueId(movieId.trim());
                                    videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                                    videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                                    videoLogsInputModel.setWatchStatus(watchStatus);
                                    videoLogsInputModel.setDeviceType("2");
                                    videoLogsInputModel.setVideoLogId(videoLogId);
                                    videoLogsInputModel.setUserId(userIdStr.trim());
                                    asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,TrailerActivity.this,TrailerActivity.this);
                                    watchStatus = "complete";
                                    asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                } else if (currentPositionStr > 0 && currentPositionStr % 60 == 0) {
                                    VideoLogsInputModel videoLogsInputModel = new VideoLogsInputModel();
                                    videoLogsInputModel.setAuthToken(authTokenStr);
                                    videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                                    videoLogsInputModel.setMuviUniqueId(movieId.trim());
                                    videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                                    videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                                    videoLogsInputModel.setWatchStatus(watchStatus);
                                    videoLogsInputModel.setDeviceType("2");
                                    videoLogsInputModel.setVideoLogId(videoLogId);
                                    videoLogsInputModel.setUserId(userIdStr.trim());
                                    asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,TrailerActivity.this,TrailerActivity.this);
                                    watchStatus = "halfplay";
                                    asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);

                                }
                            }
                        }
                        //get the current timeStamp
                    }
                });
            }
        };
    }

    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        return;
    }

    @Override
    public void onGetFFVideoLogsPreExecuteStarted() {
        stoptimertask();
    }

    @Override
    public void onGetFFVideoLogsPostExecuteCompleted(int code, String status, String videoLogId) {
        startTimer();
        return;
    }

//    private class AsyncFFVideoLogDetails extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//        int statusCode = 0;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.videoLogUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("user_id", userIdStr);
//                httppost.addHeader("ip_address", ipAddres.trim());
//                httppost.addHeader("movie_id", movieId.trim());
//                httppost.addHeader("episode_id", episodeId.trim());
//
//                httppost.addHeader("played_length", String.valueOf(playerPosition));
//                httppost.addHeader("watch_status", watchStatus);
//
//                httppost.addHeader("device_type", "2");
//                httppost.addHeader("log_id", videoLogId);
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            videoLogId = "0";
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    videoLogId = "0";
//
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                    if (statusCode == 200) {
//                        videoLogId = myJson.optString("log_id");
//                    } else {
//                        videoLogId = "0";
//                    }
//
//                }
//
//            } catch (Exception e) {
//                videoLogId = "0";
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//            if (responseStr == null) {
//                videoLogId = "0";
//
//            }
//            startTimer();
//            return;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // updateSeekBarThread.stop();
//            stoptimertask();
//
//        }
//
//
//    }

    private int millisecondsToString(int milliseconds) {
        // int seconds = (int) (milliseconds / 1000) % 60 ;
        int seconds = milliseconds / 1000;

        return seconds;
    }

    @Override
    public void onOrientationChange(int orientation) {


        if (orientation == 90) {


            compressed = false;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            //current_time.setVisibility(View.GONE);
        } else if (orientation == 270) {


            compressed = false;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
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
            //current_time.setVisibility(View.GONE);

            // Do some landscape stuff
        } else if (orientation == 180) {

            LinearLayout.LayoutParams params1 = null;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
                if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

                }
                else
                {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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


            LinearLayout.LayoutParams params1 = null;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
                if(TrailerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);

                }
                else
                {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(screenHeight*40)/100);
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


    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 1000);
    }

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

            if (content_types_id != 4) {
                seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
            }

            current_matching_time = emVideoView.getCurrentPosition();


            if ((previous_matching_time == current_matching_time) && (current_matching_time < emVideoView.getDuration())) {
                findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
                center_play_pause.setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.GONE);
                previous_matching_time = current_matching_time;
            } else {

                if (content_types_id == 4) {


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
                        //onBackPressed();
                        backCalled();
                    }
                }


                previous_matching_time = current_matching_time;
                findViewById(R.id.progress_view).setVisibility(View.GONE);
            }

        }
    };

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
    public void onBackPressed() {
        super.onBackPressed();
        if (asynGetIpAddress != null) {
            asynGetIpAddress.cancel(true);
        }
        if (asyncVideoLogDetails != null) {
            asyncVideoLogDetails.cancel(true);
        }
        if (asyncFFVideoLogDetails != null) {
            asyncFFVideoLogDetails.cancel(true);
        }
        if (progressView != null && progressView.isShown()) {
            progressView = null;
        }
        if (timer != null) {
            stoptimertask();
            timer = null;
        }

        if (video_completed == false) {

            ResumeVideoLogDetailsInput resumeVideoLogDetailsInput=new ResumeVideoLogDetailsInput();
            resumeVideoLogDetailsInput.setAuthToken(authTokenStr);
            resumeVideoLogDetailsInput.setUser_id(userIdStr.trim());
            resumeVideoLogDetailsInput.setIp_address(ipAddressStr.trim());
            resumeVideoLogDetailsInput.setMovie_id(movieId.trim());
            resumeVideoLogDetailsInput.setEpisode_id(episodeId.trim());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (current_matching_time >= emVideoView.getDuration()) {
                        watchSt = "complete";
                    }

                }

            });
            resumeVideoLogDetailsInput.setPlayed_length(String.valueOf(playerPosition));
            resumeVideoLogDetailsInput.setWatch_status(watchSt);
            ResumeVideoLogDetailsAsync asyncResumeVideoLogDetails = new ResumeVideoLogDetailsAsync(resumeVideoLogDetailsInput,this,this);
            asyncResumeVideoLogDetails.executeOnExecutor(threadPoolExecutor);
            return;
        }
        mHandler.removeCallbacks(updateTimeTask);
        if (emVideoView != null) {
            emVideoView.release();
        }
        finish();
        overridePendingTransition(0, 0);
    }

    public void backCalled() {

        if (asynGetIpAddress != null) {
            asynGetIpAddress.cancel(true);
        }
        if (asyncVideoLogDetails != null) {
            asyncVideoLogDetails.cancel(true);
        }
        if (asyncFFVideoLogDetails != null) {
            asyncFFVideoLogDetails.cancel(true);
        }
        if (progressView != null && progressView.isShown()) {
            progressView = null;
        }
        if (timer != null) {
            stoptimertask();
            timer = null;
        }
        ResumeVideoLogDetailsInput resumeVideoLogDetailsInput=new ResumeVideoLogDetailsInput();
        resumeVideoLogDetailsInput.setAuthToken(authTokenStr);
        resumeVideoLogDetailsInput.setUser_id(userIdStr.trim());
        resumeVideoLogDetailsInput.setIp_address(ipAddressStr.trim());
        resumeVideoLogDetailsInput.setMovie_id(movieId.trim());
        resumeVideoLogDetailsInput.setEpisode_id(episodeId.trim());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (current_matching_time >= emVideoView.getDuration()) {
                    watchSt = "complete";
                }

            }

        });
        resumeVideoLogDetailsInput.setPlayed_length(String.valueOf(playerPosition));
        resumeVideoLogDetailsInput.setWatch_status(watchSt);
        ResumeVideoLogDetailsAsync asyncResumeVideoLogDetails = new ResumeVideoLogDetailsAsync(resumeVideoLogDetailsInput,this,this);
        asyncResumeVideoLogDetails.executeOnExecutor(threadPoolExecutor);
        return;
      /*  if (video_completed == false){

            AsyncResumeVideoLogDetails  asyncResumeVideoLogDetails = new AsyncResumeVideoLogDetails();
            asyncResumeVideoLogDetails.executeOnExecutor(threadPoolExecutor);
            return;
        }*//*else{
            watchStatus = "com"
            asyncVideoLogDetails = new AsyncVideoLogDetails();
            asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
        }*//*
        mHandler.removeCallbacks(updateTimeTask);
        if (emVideoView!=null) {
            emVideoView.release();
        }
        finish();
        overridePendingTransition(0, 0);*/
    }

    /* public void onBackPressed() {
         super.onBackPressed();
         LogUtil.showLog("MUVI","HHVID"+videoLogId);
         if (asynGetIpAddress!=null){
             asynGetIpAddress.cancel(true);
         }
         if (asyncVideoLogDetails!=null){
             asyncVideoLogDetails.cancel(true);
         }
         if (asyncFFVideoLogDetails!=null){
             asyncFFVideoLogDetails.cancel(true);
         }
         if (progressView!=null && progressView.isShown()){
             progressView = null;
         }
         if (timer!=null){
             stoptimertask();
             timer = null;
         }
         mHandler.removeCallbacks(updateTimeTask);
         if (emVideoView!=null) {
             emVideoView.release();
         }
         finish();
         overridePendingTransition(0, 0);
     }*/
    @Override
    protected void onUserLeaveHint() {

       // if (played_length <= 0) {
            if (asynGetIpAddress != null) {
                asynGetIpAddress.cancel(true);
            }
            if (asyncVideoLogDetails != null) {
                asyncVideoLogDetails.cancel(true);
            }
            if (asyncFFVideoLogDetails != null) {
                asyncFFVideoLogDetails.cancel(true);
            }
            if (progressView != null && progressView.isShown()) {
                progressView = null;
            }
            if (timer != null) {
                stoptimertask();
                timer = null;
            }




            if(Util.call_finish_at_onUserLeaveHint) {

                Util.call_finish_at_onUserLeaveHint = true;

                mHandler.removeCallbacks(updateTimeTask);
                if (emVideoView != null) {
                    emVideoView.release();
                }

                finish();
                overridePendingTransition(0, 0);
                super.onUserLeaveHint();
            }
       // }
    }


    public void Execute_Pause_Play() {
        if (emVideoView.isPlaying()) {
            emVideoView.pause();
            latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
            center_play_pause.setImageResource(R.drawable.ic_media_play);
            mHandler.removeCallbacks(updateTimeTask);
        } else {
            if (video_completed) {

                if (content_types_id != 4) {
                    // onBackPressed();
                    backCalled();
                }

            } else {
                emVideoView.start();
                latest_center_play_pause.setImageResource(R.drawable.center_ic_media_pause);
                center_play_pause.setImageResource(R.drawable.ic_media_pause);
                mHandler.removeCallbacks(updateTimeTask);
                updateProgressBar();
            }

        }
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
                        current_time.setVisibility(View.GONE);
                        center_play_pause.setVisibility(View.GONE);
                        latest_center_play_pause.setVisibility(View.GONE);
                        End_Timer();
                    }
                });
            }
        };
        center_pause_paly_timer.schedule(timerTaskObj, 2000, 2000);
    }

    public void End_Timer() {
        if (center_pause_paly_timer_is_running) {
            center_pause_paly_timer.cancel();
            center_pause_paly_timer_is_running = false;

            primary_ll.setVisibility(View.GONE);
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

    public void showCurrentTime() {

        current_time.setText(Current_Time);
        current_time_position_timer();

       /* if(seek_label_pos == 0)
        {
            current_time_position_timer();
        }
        else
        {
            seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
            current_time.setX(seek_label_pos - current_time.getWidth() / 2);
        }
       *//* if (progresss <=9)
        {
            current_time.setX(seek_label_pos -6);
        }
        else
        {
            current_time.setX(seek_label_pos - 11);
        }*/


    }

    public void current_time_position_timer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (content_types_id != 4) {

                            seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
                            current_time.setX(seek_label_pos - current_time.getWidth() / 2);
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 100);
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtil.showLog("MUVI","FHFHFHCALLED");
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }
*/
   @Override
   public void onGetResumeVideoLogDetailsPreExecuteStarted() {

       stoptimertask();
   }

    @Override
    public void onGetResumeVideoLogDetailsPostExecuteCompleted(int status, String message, String videoLogId) {

        if (message == null) {
            videoLogId = "0";

        }
        mHandler.removeCallbacks(updateTimeTask);
        if (emVideoView != null) {
            emVideoView.release();
        }
        finish();
        overridePendingTransition(0, 0);
        //startTimer();
        return;
    }

//    private class AsyncResumeVideoLogDetails extends AsyncTask<Void, Void, Void> {
//        //  ProgressDialog pDialog;
//        String responseStr;
//        int statusCode = 0;
//        String watchSt = "halfplay";
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.videoLogUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("user_id", userIdStr.trim());
//                httppost.addHeader("ip_address", ipAddres.trim());
//                httppost.addHeader("movie_id", movieId.trim());
//                httppost.addHeader("episode_id", episodeId.trim());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (current_matching_time >= emVideoView.getDuration()) {
//                            watchSt = "complete";
//                        }
//
//                    }
//
//                });
//                httppost.addHeader("played_length", String.valueOf(playerPosition));
//                httppost.addHeader("watch_status", watchSt);
//              /*  runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (current_matching_time >= emVideoView.getDuration()) {
//
//                            httppost.addHeader("watch_status", "complete");
//                        }else{
//                            httppost.addHeader("watch_status", "halfplay");
//
//                        }
//
//                    }
//
//                });*/
//
//                httppost.addHeader("device_type", "2");
//                httppost.addHeader("log_id", videoLogId);
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            videoLogId = "0";
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    videoLogId = "0";
//
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                    if (statusCode == 200) {
//                        videoLogId = myJson.optString("log_id");
//                    } else {
//                        videoLogId = "0";
//                    }
//
//                }
//
//            } catch (Exception e) {
//                videoLogId = "0";
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//         /*   try {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
//            } catch (IllegalArgumentException ex) {
//                videoLogId = "0";
//            }*/
//            if (responseStr == null) {
//                videoLogId = "0";
//
//            }
//            mHandler.removeCallbacks(updateTimeTask);
//            if (emVideoView != null) {
//                emVideoView.release();
//            }
//            finish();
//            overridePendingTransition(0, 0);
//            //startTimer();
//            return;
//
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            stoptimertask();
//
//        }


  //  }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.hide_pause = false;
    }

    // Added Later By Bibhu For Subtitle Feature.


    @Override
    public void finish() {
        super.finish();
    }



    @Override
    protected void onPause() {
        /*if (subtitleDisplayHandler != null) {
            subtitleDisplayHandler.removeCallbacks(subtitleProcessesor);
            subtitleDisplayHandler = null;
            if (subsFetchTask != null)
                subsFetchTask.cancel(true);
        }*/
        super.onPause();
    }

    private void hideSystemUI() {
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
        if (Util.dataModel.getVideoStory().trim() != null && !Util.dataModel.getVideoStory().trim().matches("")){
            story.setText(Util.dataModel.getVideoStory());
            story.setVisibility(View.VISIBLE);
            ResizableCustomView.doResizeTextView(TrailerActivity.this,story, MAX_LINES, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);

        } else {
            story.setVisibility(View.GONE);
        }
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        );
    }
}
