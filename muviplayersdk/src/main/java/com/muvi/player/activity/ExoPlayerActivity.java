
package com.muvi.player.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import com.example.muviplayersdk.R;
import com.intertrust.wasabi.ErrorCodeException;
import com.intertrust.wasabi.Runtime;
import com.intertrust.wasabi.media.PlaylistProxy;
import com.intertrust.wasabi.media.PlaylistProxyListener;
import com.muvi.player.subtitle_support.Caption;
import com.muvi.player.subtitle_support.FormatSRT;
import com.muvi.player.subtitle_support.FormatSRT_WithoutCaption;
import com.muvi.player.subtitle_support.TimedTextObject;
import com.muvi.player.utils.ExpandableTextView;
import com.muvi.player.utils.SensorOrientationChangeNotifier;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;



//====================for thirdparty player(DRM) it use======================
enum ContentTypes1 {
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
}

//========================DRM code end===============================

public class ExoPlayerActivity extends AppCompatActivity implements SensorOrientationChangeNotifier.Listener, PlaylistProxyListener {
    PlaylistProxy playerProxy;
    Player playerModel;
    int played_length = 0;
    int playerStartPosition = 0;

    Timer timer;
    private Handler threadHandler = new Handler();
    String videoLogId = "0";
    String watchStatus = "start";
    int playerPosition = 0;
    public boolean isFastForward = false;
    public int playerPreviousPosition = 0;
    TimerTask timerTask;
    String emailIdStr = "";
    String userIdStr = "";
    String movieId = "";
    String episodeId = "0";

    String videoBufferLogId = "0";
    String videoBufferLogUniqueId = "0";
    String Location = "0";

    AsyncVideoLogDetails asyncVideoLogDetails;
    AsyncFFVideoLogDetails asyncFFVideoLogDetails;
    AsynGetIpAddress asynGetIpAddress;
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
    // SharedPreferences pref;
    //Toolbar mActionBarToolbar;
    LinearLayout linearLayout1;

    TextView videoTitle, GenreTextView, videoDurationTextView, videoCensorRatingTextView, videoCensorRatingTextView1, videoReleaseDateTextView,
            videoCastCrewTitleTextView;
    ExpandableTextView story;
    private EMVideoView emVideoView;
    int seek_label_pos = 0;
    boolean isLiveStream = false;
    boolean isLandScape = false;
    private SubtitleProcessingTask subsFetchTask;
    public TimedTextObject srt;
    TextView subtitleText;
    public Handler subtitleDisplayHandler;
    ImageView subtitle_change_btn;

    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    boolean callWithoutCaption = true;
    boolean censor_layout = true;

    // This added for resolution Change.

    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    int seekBarProgress = 0;
    boolean change_resolution = false;
    boolean is_paused = false;

    //======end========//

    // This is added for the movable water mark //

    Timer MovableTimer;


    //===================end===================//


    @Override
    protected void onResume() {
        super.onResume();
        SensorOrientationChangeNotifier.getInstance(ExoPlayerActivity.this).addListener(this);
        // Added For FCM
        // Call Api to Check User's Login Status;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        player_layout = (RelativeLayout) findViewById(R.id.player_layout);
      /*  player_layout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);*/

        player_layout_height = player_layout.getHeight();
        player_layout_width = player_layout.getWidth();

        primary_ll = (LinearLayout) findViewById(R.id.primary_ll);
        last_ll = (LinearLayout) findViewById(R.id.last_ll);
        last_ll = (LinearLayout) findViewById(R.id.last_ll);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        ipAddressTextView = (TextView) findViewById(R.id.emailAddressTextView);
        emailAddressTextView = (TextView) findViewById(R.id.ipAddressTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        playerModel = new Player();
        playerModel = (Player) getIntent().getSerializableExtra("PlayerModel");
      /*  playerModel.getVideoTitleColor();
        Log.v("BISHAL", "colorExo=" + playerModel.getVideoTitleColor());*/
    /*    playerModel.getStoryColor();
        playerModel.getVideoReleaseDateColor();
        playerModel.getCensorRatingColor();*/


       isLiveStream = playerModel.isLiveStream();
        played_length = playerModel.getPlayPos() * 1000;
        Log.v("BKS","exo video url==="+playerModel.getVideoUrl());
        if (!playerModel.getVideoUrl().trim().equals("")) {
            Log.v("BKS","video match and enter the if loop and backcalled===");
            Log.v("BKS","thirdpartyurl==="+playerModel.isThirdPartyPlayer());
           if (playerModel.isThirdPartyPlayer()) {
                Log.v("BKS","enter thirdparty condition==");


                if (playerModel.getVideoUrl().contains("://www.youtube") || playerModel.getVideoUrl().contains("://www.youtu.be")) {
                    if (playerModel.getVideoUrl().contains("live_stream?channel")) {
                        final Intent playVideoIntent = new Intent(ExoPlayerActivity.this, ThirdPartyPlayer.class);
                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        playVideoIntent.putExtra("PlayerModel", playerModel);
                        startActivity(playVideoIntent);
                        finish();
                        return;
                    } else {
                        final Intent playVideoIntent = new Intent(ExoPlayerActivity.this, YouTubeAPIActivity.class);
                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        playVideoIntent.putExtra("PlayerModel", playerModel);
                        startActivity(playVideoIntent);
                        finish();
                        return;
                    }
                } else {
                    final Intent playVideoIntent = new Intent(ExoPlayerActivity.this, ThirdPartyPlayer.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    playVideoIntent.putExtra("PlayerModel", playerModel);
                    startActivity(playVideoIntent);
                    finish();
                    return;

                }
            }
           else {

               backCalled();
           }

            //onBackPressed();
        }
        //for backpress condition start
       /* else {

          backCalled();
        }*/

        //end condition


        movieId = playerModel.getMovieUniqueId();
        Log.v("BISHAL", "MovieId==" + movieId);
        episodeId = playerModel.getEpisode_id();
        Log.v("BISHAL", "Url=" + playerModel.getVideoUrl());

        if (playerModel.getUserId() != null && !playerModel.getUserId().trim().matches("")) {
            userIdStr = playerModel.getUserId();
        }
        if (playerModel.getEmailId() != null && !playerModel.getEmailId().trim().matches("")) {
            emailIdStr = playerModel.getEmailId();
        }

        emVideoView = (EMVideoView) findViewById(R.id.emVideoView);
        subtitleText = (TextView) findViewById(R.id.offLine_subtitleText);
        subtitle_change_btn = (ImageView) findViewById(R.id.subtitle_change_btn);


        latest_center_play_pause = (ImageButton) findViewById(R.id.latest_center_play_pause);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        //videoTitle.setTextColor(Color.parseColor(playerModel.getVideoTitleColor()));
        //videoTitle.setTextColor(playerModel.getVideoTitleColor());
        Typeface videoTitleface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoTitle.setTypeface(videoTitleface);
        GenreTextView = (TextView) findViewById(R.id.GenreTextView);
        Typeface GenreTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        GenreTextView.setTypeface(GenreTextViewface);
        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        Typeface videoDurationTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoDurationTextView.setTypeface(videoDurationTextViewface);
        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        Typeface videoCensorRatingTextViewface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        videoCensorRatingTextView.setTypeface(videoCensorRatingTextViewface);
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
        Typeface videoCensorRatingTextView1face = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoCensorRatingTextView1.setTypeface(videoCensorRatingTextView1face);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        Typeface videoReleaseDateTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoReleaseDateTextView.setTypeface(videoReleaseDateTextViewface);
        story = (ExpandableTextView) findViewById(R.id.story);
        // story.setTextColor(playerModel.getStoryColor());
        Typeface storyTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        story.setTypeface(storyTypeface);
        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        videoCastCrewTitleTextView.setTypeface(watchTrailerButtonTypeface);
        //translatedLanguage=new TranslatedLanguage(ExoPlayerActivity.this);
        //videoCastCrewTitleTextView.setText(translatedLanguage.getCastCrewButtonTitle());

        MovableTimer = new Timer();
        MovableTimer.schedule(new TimerTask() {

            @Override
            public void run() {
//               MoveWaterMark();
            }
        }, 2000, 2000);

        //===============This is used for subtitle ================================//
        playerModel.DefaultSubtitle = "Off";

        if (playerModel.getSubTitleName() != null) {
            SubTitleName = playerModel.getSubTitleName();
        } else {
            SubTitleName.clear();
        }

        if (playerModel.getSubTitlePath() != null) {
            SubTitlePath = playerModel.getSubTitlePath() ;
        } else {
            SubTitlePath.clear();
        }

        subtitle_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

                playerModel.call_finish_at_onUserLeaveHint = false;
                Intent intent = new Intent(ExoPlayerActivity.this, Subtitle_Resolution.class);
                intent.putExtra("ResolutionFormat", ResolutionFormat);
                intent.putExtra("ResolutionUrl", ResolutionUrl);
                intent.putExtra("SubTitleName", SubTitleName);
                intent.putExtra("SubTitlePath", SubTitlePath);
                startActivityForResult(intent, 3333);


            }
        });

        //=========================End=================================//


        //===============================This is used for Resolution Change ===================================//


        playerModel.VideoResolution = "Auto";

        if (playerModel.getResolutionFormat()  != null) {
            ResolutionFormat = playerModel.getResolutionFormat() ;
        } else {
            ResolutionFormat.clear();

        }

        if ( playerModel.getResolutionUrl() != null) {
            ResolutionUrl = playerModel.getResolutionUrl() ;
        } else {
            ResolutionUrl.clear();
        }

        if (ResolutionUrl.size() < 1)

        {
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

        //=========================End=================================//


        if ((SubTitlePath.size() < 1) && (ResolutionUrl.size() < 1)) {
            subtitle_change_btn.setVisibility(View.INVISIBLE);
            Log.v("SUBHA", "subtitle_image button Invisible called");
        }

        //=============================== End Resolution Change ===================================//

        if (playerModel.getVideoTitle().trim() != null && !playerModel.getVideoTitle().trim().matches("")) {
            videoTitle.setText(playerModel.getVideoTitle().trim());
            //videoTitle.setTextColor(playerModel.getVideoTitleColor());
            videoTitle.setVisibility(View.VISIBLE);
        } else {
            videoTitle.setVisibility(View.GONE);
        }

        if (playerModel.getVideoGenre().trim() != null && !playerModel.getVideoGenre().trim().matches(""))

        {
            GenreTextView.setText(playerModel.getVideoGenre().trim());
            // GenreTextView.setTextColor(playerModel.getVideoGenreColor());
            GenreTextView.setVisibility(View.VISIBLE);
        } else {
            GenreTextView.setVisibility(View.GONE);
        }


        if (playerModel.getVideoDuration().trim() != null && !playerModel.getVideoDuration().trim().matches(""))

        {
            videoDurationTextView.setText(playerModel.getVideoDuration().trim());
            //videoDurationTextView.setTextColor(playerModel.getVideoDurationTextColor());
            videoDurationTextView.setVisibility(View.VISIBLE);
            censor_layout = false;
        } else {
            videoDurationTextView.setVisibility(View.GONE);
        }
        if (playerModel.getCensorRating().trim() != null && !playerModel.getCensorRating().trim().matches("")) {
            if ((playerModel.getCensorRating().trim()).contains("_")) {
                String Data[] = (playerModel.getCensorRating().trim()).split("-");
                videoCensorRatingTextView.setVisibility(View.VISIBLE);
                videoCensorRatingTextView1.setVisibility(View.VISIBLE);
                videoCensorRatingTextView.setText(Data[0]);
                videoCensorRatingTextView1.setText(Data[1]);
                censor_layout = false;
            } else {
                censor_layout = false;
                videoCensorRatingTextView.setVisibility(View.VISIBLE);
                videoCensorRatingTextView1.setVisibility(View.GONE);
                videoCensorRatingTextView.setText(playerModel.getCensorRating().trim());
                // videoCensorRatingTextView.setTextColor(playerModel.getCensorRatingColor());
            }
        } else {

            videoCensorRatingTextView.setVisibility(View.GONE);
            videoCensorRatingTextView1.setVisibility(View.GONE);
        }
        if (playerModel.getCensorRating().trim() != null && playerModel.getCensorRating().trim().equalsIgnoreCase("No_DATA")) {
            videoCensorRatingTextView.setVisibility(View.GONE);
            videoCensorRatingTextView1.setVisibility(View.GONE);
        }

        if (playerModel.getVideoReleaseDate().trim() != null && !playerModel.getVideoReleaseDate().trim().matches(""))

        {
            videoReleaseDateTextView.setText(playerModel.getVideoReleaseDate().trim());
            videoReleaseDateTextView.setVisibility(View.VISIBLE);
            censor_layout = false;
        } else {
            videoReleaseDateTextView.setVisibility(View.GONE);
        }

        if (censor_layout) {

            ((LinearLayout) findViewById(R.id.durationratingLiearLayout)).setVisibility(View.GONE);
        }
        if (playerModel.getVideoStory().trim() != null && !playerModel.getVideoStory().trim().matches(""))

        {
            story.setText(playerModel.getVideoStory());
           // story.setTextColor(Color.parseColor(playerModel.getStoryColor()));
            story.setVisibility(View.VISIBLE);
        } else {
            story.setVisibility(View.GONE);
        }

        if (playerModel.isCastCrew() == true)

        {
            videoCastCrewTitleTextView.setVisibility(View.VISIBLE);
        } else {
            videoCastCrewTitleTextView.setVisibility(View.GONE);
        }

        videoCastCrewTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerModel.checkNetwork(ExoPlayerActivity.this)) {
                    //Will Add Some Data to send
                    playerModel.call_finish_at_onUserLeaveHint = false;
                    playerModel.hide_pause = true;
                    ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.GONE);
                    latest_center_play_pause.setVisibility(View.VISIBLE);

                    if (emVideoView.isPlaying()) {
                        emVideoView.pause();
                        latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
                        center_play_pause.setImageResource(R.drawable.ic_media_play);
                        mHandler.removeCallbacks(updateTimeTask);
                    }


                    final Intent detailsIntent = new Intent(ExoPlayerActivity.this, CastAndCrewActivity.class);
                    detailsIntent.putExtra("cast_movie_id", movieId.trim());
                    detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(detailsIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "NO_INTERNET_CONNECTION", Toast.LENGTH_LONG).show();
                }
            }
        });



        /*Typeface Tf = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        ipAddressTextView.setTypeface(Tf);
        emailAddressTextView.setTypeface(Tf);
        dateTextView.setTypeface(Tf);*/

        emailAddressTextView.setText(emailIdStr);
        dateTextView.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        compress_expand = (ImageView) findViewById(R.id.compress_expand);
        back = (ImageButton) findViewById(R.id.back);
        seekBar = (SeekBar) findViewById(R.id.progress);
        center_play_pause = (ImageButton) findViewById(R.id.center_play_pause);

        current_time = (TextView) findViewById(R.id.current_time);
        total_time = (TextView) findViewById(R.id.total_time);
        progressView = (ProgressBar) findViewById(R.id.progress_view);


        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        playerModel.player_description = true;

        LinearLayout.LayoutParams params1 = null;

        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);

            } else {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);
            }
        } else {
            if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

            } else {
                params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
            }
        }
        player_layout.setLayoutParams(params1);

        if (isLiveStream == true) {
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
                if (((ProgressBar) findViewById(R.id.progress_view)).getVisibility() == View.VISIBLE) {
                    primary_ll.setVisibility(View.VISIBLE);
                    center_play_pause.setVisibility(View.GONE);
                    latest_center_play_pause.setVisibility(View.GONE);
                    current_time.setVisibility(View.GONE);
                    subtitle_change_btn.setVisibility(View.INVISIBLE);


                } else {
                    if (primary_ll.getVisibility() == View.VISIBLE) {
                        primary_ll.setVisibility(View.GONE);
                        last_ll.setVisibility(View.GONE);
                        center_play_pause.setVisibility(View.GONE);
                        latest_center_play_pause.setVisibility(View.GONE);
                        current_time.setVisibility(View.GONE);
                        subtitle_change_btn.setVisibility(View.INVISIBLE);

                        End_Timer();
                    } else {


                        primary_ll.setVisibility(View.VISIBLE);

                        if (SubTitlePath.size() > 0 || ResolutionUrl.size() > 0) {
                            subtitle_change_btn.setVisibility(View.VISIBLE);
                        }

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
                    playerModel.player_description = false;
                    playerModel.landscape = true;
                    hideSystemUI();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {

                    playerModel.player_description = true;
                    LinearLayout.LayoutParams params1 = null;
                    if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);

                        } else {
                            params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);
                        }
                    } else {
                        if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

                        } else {
                            params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
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
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    showSystemUI();
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

                if (playerModel.hide_pause) {
                    playerModel.hide_pause = false;
                    Start_Timer();
                }

                Execute_Pause_Play();
            }
        });

        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {

                if (change_resolution) {

                    change_resolution = false;
                    emVideoView.start();
                    emVideoView.seekTo(seekBarProgress);
                    seekBar.setProgress(emVideoView.getCurrentPosition());

                    if (is_paused) {
                        is_paused = false;
                        emVideoView.pause();
                        progressView.setVisibility(View.GONE);
                    } else {
                        updateProgressBar();
                    }

                } else {

                    if (playerModel.getPlayPos() >= emVideoView.getDuration() / 1000) {
                        played_length = 0;
                    }

                    video_completed = false;
                    progressView.setVisibility(View.VISIBLE);
                    center_play_pause.setVisibility(View.GONE);
                    latest_center_play_pause.setVisibility(View.GONE);

                    try {
                        //video log
                        if (isLiveStream == true) {

                            if (SubTitlePath.size() > 0) {
                                CheckSubTitleParsingType("1");
                                subtitleDisplayHandler = new Handler();
                                subsFetchTask = new SubtitleProcessingTask("1");
                                subsFetchTask.execute();
                            } else {
                                if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                                    asyncVideoLogDetails = new AsyncVideoLogDetails();
                                    asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                }
                            }

                            emVideoView.start();
                            updateProgressBar();
                        } else {
                            startTimer();

                            if (played_length > 0) {
                                playerModel.call_finish_at_onUserLeaveHint = false;
                                ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.GONE);
                                Intent resumeIntent = new Intent(ExoPlayerActivity.this, ResumePopupActivity.class);
                                startActivityForResult(resumeIntent, 1001);
                            } else {


                                emVideoView.start();
                                seekBar.setProgress(emVideoView.getCurrentPosition());
                                updateProgressBar();

                                if (SubTitlePath.size() > 0) {
                                    CheckSubTitleParsingType("1");
                                    subtitleDisplayHandler = new Handler();
                                    subsFetchTask = new SubtitleProcessingTask("1");
                                    subsFetchTask.execute();
                                } else {
                                    if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                                        asyncVideoLogDetails = new AsyncVideoLogDetails();
                                        asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                    }

                                }

                            }

                        }
                    } catch (Exception e) {
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCalled();

            }
        });


        //emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));


        // ================================This is added for the DRM video Play=========================

        try {
            Runtime.initialize(getDir("wasabi", MODE_PRIVATE).getAbsolutePath());

            if (!Runtime.isPersonalized())
                Runtime.personalize();

        } catch (NullPointerException e) {

            backCalled();
            return;
        } catch (ErrorCodeException e) {
            backCalled();
            return;
        }

        try {
            EnumSet<PlaylistProxy.Flags> flags = EnumSet.noneOf(PlaylistProxy.Flags.class);
            playerProxy = new PlaylistProxy(flags, this, new Handler());
            playerProxy.start();
        } catch (ErrorCodeException e) {

            backCalled();
            return;
        }

        try {


            ContentTypes1 contentType = ContentTypes1.DASH;
            PlaylistProxy.MediaSourceParams params = new PlaylistProxy.MediaSourceParams();
            params.sourceContentType = contentType
                    .getMediaSourceParamsContentType();

            String contentTypeValue = contentType.toString();
            if (playerModel.getVideoUrl().contains(".mpd")) {
                String url = playerProxy.makeUrl(playerModel.getVideoUrl(), PlaylistProxy.MediaSourceType.valueOf((contentTypeValue == "MP4" || contentTypeValue == "HLS" || contentTypeValue == "DASH") ? contentTypeValue : "SINGLE_FILE"), params);
                emVideoView.setVideoURI(Uri.parse(url));
            } else {
                emVideoView.setVideoURI(Uri.parse(playerModel.getVideoUrl()));
            }


        } catch (ErrorCodeException e) {
            backCalled();
            return;
        } catch (IllegalArgumentException e) {
            backCalled();
            e.printStackTrace();
        } catch (SecurityException e) {
            backCalled();
            e.printStackTrace();
        } catch (IllegalStateException e) {
            backCalled();
            e.printStackTrace();
        }
        //========================end of drm code====================================

        AsynGetIpAddress asynGetIpAddress = new AsynGetIpAddress();
        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);
    }

    @Override
    public void onErrorNotification(int i, String s) {

    }


    private class AsyncVideoLogDetails extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("BKS", "urlRouteList==" + playerModel.getRootUrl());

            String urlRouteList = playerModel.getRootUrl().trim() + "videoLogs";
            Log.v("BKS", "urlRouteList==" + urlRouteList + movieId);
            Log.v("BKS", "userIdStr==" + userIdStr + userIdStr);
            Log.v("BKS", "ip_address==" + ipAddressStr);
            Log.v("BKS", "movie_id==" + movieId);
            Log.v("BKS", "episodeId==" + episodeId);
            Log.v("BKS", "played_length==" + played_length);

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
                httppost.addHeader("user_id", userIdStr.trim());
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());
                httppost.addHeader("played_length", String.valueOf(playerPosition));
                httppost.addHeader("watch_status", watchStatus);
                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoLogId);
                Log.v("BISHALS", "movie_id=" + movieId.trim());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA", "PLAY responseStr" + responseStr);


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoLogId = "0";

                        }

                    });

                } catch (Exception e) {
                    videoLogId = "0";
                    e.printStackTrace();

                    Log.v("SUBHA", "Exception of videoplayer" + e.toString());
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    if (statusCode == 200) {
                        videoLogId = myJson.optString("log_id");
                    } else {
                        videoLogId = "0";
                    }

                }

            } catch (Exception e) {
                videoLogId = "0";
                Log.v("SUBHA", "Exception" + e);

            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {
                videoLogId = "0";
            }
            AsyncVideoBufferLogDetails asyncVideoBufferLogDetails = new AsyncVideoBufferLogDetails();
            asyncVideoBufferLogDetails.executeOnExecutor(threadPoolExecutor);

            return;
        }

        @Override
        protected void onPreExecute() {
            Log.v("SUBHA", "onPreExecute");
            stoptimertask();
            Log.v("SUBHA", "onPreExecute1");
        }
    }


    private class AsyncVideoBufferLogDetails extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = playerModel.getRootUrl().trim() + "bufferLogs";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
                httppost.addHeader("user_id", userIdStr);
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());
                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoBufferLogId);
                httppost.addHeader("resolution", resolution.trim());
                httppost.addHeader("start_time", "0");
                httppost.addHeader("end_time", String.valueOf(playerPosition));
                httppost.addHeader("log_unique_id", videoBufferLogUniqueId);
                httppost.addHeader("location", Location);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoBufferLogId = "0";
                            videoBufferLogUniqueId = "0";
                            Location = "0";
                        }

                    });

                } catch (IOException e) {
                    videoBufferLogId = "0";
                    videoBufferLogUniqueId = "0";
                    Location = "0";
                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    if (statusCode == 200) {
                        videoBufferLogId = myJson.optString("log_id");
                        videoBufferLogUniqueId = myJson.optString("log_unique_id");
                        Location = myJson.optString("location");
                        ;
                    } else {
                        videoBufferLogId = "0";
                        videoBufferLogUniqueId = "0";
                        Location = "0";
                    }
                }
            } catch (Exception e) {
                videoBufferLogId = "0";
                videoBufferLogUniqueId = "0";
                Location = "0";
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {

                videoBufferLogId = "0";
                videoBufferLogUniqueId = "0";
                Location = "0";
            }
            startTimer();

            return;
        }

        @Override
        protected void onPreExecute() {

        }
    }


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
                            Log.v("BIBHU1" +
                                    "", "TimerTask called=" + currentPositionStr + "  =====  " + playerPreviousPosition + "======" + isFastForward);

                            if (isFastForward == true) {
                                isFastForward = false;


                                int duration = emVideoView.getDuration() / 1000;
                                if (currentPositionStr > 0 && currentPositionStr == duration) {

                                    Log.v("BIBHU1" + "", "Complete FF Log Called");
                                    if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                                        asyncFFVideoLogDetails = new AsyncFFVideoLogDetails();
                                        watchStatus = "complete";
                                        asyncFFVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                    }

                                } else {

                                    Log.v("BIBHU1", "halfplay FF Log Called");
                                    if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                                        asyncFFVideoLogDetails = new AsyncFFVideoLogDetails();
                                        watchStatus = "halfplay";
                                        asyncFFVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                    }

                                }

                            } else if (isFastForward == false && currentPositionStr >= millisecondsToString(playerPreviousPosition)) {

                                playerPreviousPosition = 0;

                                int duration = emVideoView.getDuration() / 1000;
                                if (currentPositionStr > 0 && currentPositionStr == duration) {

                                    Log.v("BIBHU1", "Complete Video Log Called");
                                    if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                                        asyncVideoLogDetails = new AsyncVideoLogDetails();
                                        watchStatus = "complete";
                                        asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                    }

                                } else if (currentPositionStr > 0 && currentPositionStr % 60 == 0) {

                                    Log.v("BIBHU1", "Halfplay video Log Called");
                                    if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                                        asyncVideoLogDetails = new AsyncVideoLogDetails();
                                        watchStatus = "halfplay";
                                        asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                    }


                                }
                            }
                        }
                        //get the current timeStamp
                    }
                });
            }
        };
    }

    private class AsyncFFVideoLogDetails extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int statusCode = 0;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = playerModel.getRootUrl().trim() + "videoLogs";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
                httppost.addHeader("user_id", userIdStr);
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());

                httppost.addHeader("played_length", String.valueOf(playerPosition));
                httppost.addHeader("watch_status", watchStatus);

                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoLogId);

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoLogId = "0";

                        }

                    });

                } catch (IOException e) {
                    videoLogId = "0";

                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    if (statusCode == 200) {
                        videoLogId = myJson.optString("log_id");
                    } else {
                        videoLogId = "0";
                    }
                }

            } catch (Exception e) {
                videoLogId = "0";

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            if (responseStr == null) {
                videoLogId = "0";

            }
            if (statusCode == 200) {
                AsyncFFVideoBufferLogDetails asyncFFVideoBufferLogDetails = new AsyncFFVideoBufferLogDetails();
                asyncFFVideoBufferLogDetails.executeOnExecutor(threadPoolExecutor);

            }
            return;

        }

        @Override
        protected void onPreExecute() {
            // updateSeekBarThread.stop();
            stoptimertask();
        }
    }


    private class AsyncFFVideoBufferLogDetails extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = playerModel.getRootUrl().trim() + "bufferLogs";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
                httppost.addHeader("user_id", userIdStr.trim());
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());
                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoBufferLogId);
                httppost.addHeader("resolution", resolution.trim());
                httppost.addHeader("start_time", "0");
                httppost.addHeader("end_time", String.valueOf(millisecondsToString(playerPreviousPosition)));
                httppost.addHeader("log_unique_id", videoBufferLogUniqueId);
                httppost.addHeader("location", Location);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoBufferLogId = "0";
                            videoBufferLogUniqueId = "0";
                            Location = "0";
                        }

                    });

                } catch (IOException e) {
                    videoBufferLogId = "0";
                    videoBufferLogUniqueId = "0";
                    Location = "0";
                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    if (statusCode == 200) {
                        videoBufferLogId = myJson.optString("log_id");
                        videoBufferLogUniqueId = myJson.optString("log_unique_id");
                        Location = myJson.optString("location");
                        ;
                    } else {
                        videoBufferLogId = "0";
                        videoBufferLogUniqueId = "0";
                        Location = "0";
                    }

                }

            } catch (Exception e) {
                videoBufferLogId = "0";
                videoBufferLogUniqueId = "0";
                Location = "0";
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {

                videoBufferLogId = "0";
                videoBufferLogUniqueId = "0";
                Location = "0";
            }
            if (statusCode == 200) {

                AsyncUpdateVideoBufferLogDetails asyncUpdateVideoBufferLogDetails = new AsyncUpdateVideoBufferLogDetails();
                asyncUpdateVideoBufferLogDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                return;
            }
            return;
        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class AsyncUpdateVideoBufferLogDetails extends AsyncTask<Void, Void, Void> {
        //ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = playerModel.getRootUrl().trim() + "updateBufferLogs";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
                httppost.addHeader("user_id", userIdStr);
                httppost.addHeader("ip_address", ipAddressStr);
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());
                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoBufferLogId);
                httppost.addHeader("resolution", resolution.trim());

                httppost.addHeader("start_time", String.valueOf(playerPosition));
                httppost.addHeader("end_time", String.valueOf(playerPosition));
                httppost.addHeader("log_unique_id", videoBufferLogUniqueId);
                httppost.addHeader("location", Location);

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoBufferLogId = "0";
                            videoBufferLogUniqueId = "0";
                            Location = "0";
                        }

                    });

                } catch (IOException e) {
                    videoBufferLogId = "0";
                    videoBufferLogUniqueId = "0";
                    Location = "0";
                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    if (statusCode == 200) {
                        videoBufferLogId = myJson.optString("log_id");
                        videoBufferLogUniqueId = myJson.optString("log_unique_id");
                        Location = myJson.optString("location");

                    } else {
                        videoBufferLogId = "0";
                        videoBufferLogUniqueId = "0";
                        Location = "0";
                    }

                }

            } catch (Exception e) {
                videoBufferLogId = "0";
                videoBufferLogUniqueId = "0";
                Location = "0";
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {
                videoBufferLogId = "0";
                videoBufferLogUniqueId = "0";
                Location = "0";
            }

            startTimer();
            return;
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private int millisecondsToString(int milliseconds) {
        // int seconds = (int) (milliseconds / 1000) % 60 ;
        int seconds = (int) (milliseconds / 1000);
        return seconds;
    }

    @Override
    public void onOrientationChange(int orientation) {


        if (orientation == 90) {

            playerModel.player_description = false;
            isLandScape = false;

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
            playerModel.player_description = false;
            isLandScape = true;

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

            playerModel.player_description = true;

            LinearLayout.LayoutParams params1 = null;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);

                } else {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);
                }
            } else {
                if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

                } else {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            showSystemUI();

            //current_time.setVisibility(View.GONE);

        } else if (orientation == 0) {

            playerModel.player_description = true;
            LinearLayout.LayoutParams params1 = null;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);

                } else {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 45) / 100);
                }
            } else {
                if (ExoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);

                } else {
                    params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (screenHeight * 40) / 100);
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            showSystemUI();

            //current_time.setVisibility(View.GONE);
        }

        current_time_position_timer();

    }


    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
        String responseStr;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                // Execute HTTP Post Request
                try {
                    URL myurl = new URL(playerModel.loadIPUrl);
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
                    Log.v("BISHAL", "ipstr==" + ipAddressStr);
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

            ipAddressTextView.setText(ipAddressStr);
            Log.v("BISHAl", "ipstr==" + ipAddressStr);

            if (responseStr == null) {
                ipAddressStr = "";
            }
            return;
        }

        protected void onPreExecute() {

        }
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
            seekBarProgress = emVideoView.getCurrentPosition();
//            }
            seekBar.setMax(emVideoView.getDuration());
            Calcute_Currenttime_With_TotalTime();
            mHandler.postDelayed(this, 1000);

            if (isLiveStream != true) {
                showCurrentTime();//this condition for check the cuurent time AND THAT SEEKBAR SIMULTINOUSLY RUN
            }

            current_matching_time = emVideoView.getCurrentPosition();


            if ((previous_matching_time == current_matching_time) && (current_matching_time < emVideoView.getDuration())) {
                //====start this condition for check the cuurent time AND THAT SEEKBAR SIMULTINOUSLY RUN====
                ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.VISIBLE);

                primary_ll.setVisibility(View.GONE);
                last_ll.setVisibility(View.GONE);
                center_play_pause.setVisibility(View.GONE);
                latest_center_play_pause.setVisibility(View.GONE);
                current_time.setVisibility(View.GONE);
                subtitle_change_btn.setVisibility(View.INVISIBLE);

                previous_matching_time = current_matching_time;
                //=============end up part============
            } else {

                if (isLiveStream == false) {


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
                ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.GONE);
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
            if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                AsyncResumeVideoLogDetails asyncResumeVideoLogDetails = new AsyncResumeVideoLogDetails();
                asyncResumeVideoLogDetails.executeOnExecutor(threadPoolExecutor);
            }

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
        Log.v("BKS","enter in the back called===");

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
        if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
            AsyncResumeVideoLogDetails asyncResumeVideoLogDetails = new AsyncResumeVideoLogDetails();
            asyncResumeVideoLogDetails.executeOnExecutor(threadPoolExecutor);
        }

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
         Log.v("SUBHA","HHVID"+videoLogId);
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

        //if (played_length <= 0) {
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


        if (playerModel.call_finish_at_onUserLeaveHint) {

            playerModel.call_finish_at_onUserLeaveHint = true;

            mHandler.removeCallbacks(updateTimeTask);
            if (emVideoView != null) {
                emVideoView.release();
            }

            finish();
            overridePendingTransition(0, 0);
            super.onUserLeaveHint();
        }

    }


    public void Execute_Pause_Play() {
        if (emVideoView.isPlaying()) {
            emVideoView.pause();
            latest_center_play_pause.setImageResource(R.drawable.center_ic_media_play);
            center_play_pause.setImageResource(R.drawable.ic_media_play);
            mHandler.removeCallbacks(updateTimeTask);
        } else {
            if (video_completed) {

                if (isLiveStream != true) {
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

            subtitle_change_btn.setVisibility(View.INVISIBLE);
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
                        if (isLiveStream != true) {

                            try{
                                Log.v("BKS","seekright=="+seekBar.getRight());
                                Log.v("BKS","seekleft=="+seekBar.getLeft());
                                Log.v("BKS","seekprogress=="+seekBar.getProgress());
                                Log.v("BKS","seekmax=="+seekBar.getMax());


                            seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
                            current_time.setX(seek_label_pos - current_time.getWidth() / 2);
                            timer.cancel();
                            }catch (ArithmeticException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
                        }
                    }
                });
            }
        }, 0, 100);
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.v("SUBHA","FHFHFHCALLED");
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }
*/


    private class AsyncResumeVideoLogDetails extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;
        String watchSt = "halfplay";

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = playerModel.getRootUrl().trim() + "videoLogs";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
                httppost.addHeader("user_id", userIdStr.trim());
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (current_matching_time >= emVideoView.getDuration()) {
                            watchSt = "complete";
                        }

                    }

                });
                httppost.addHeader("played_length", String.valueOf(playerPosition));
                httppost.addHeader("watch_status", watchSt);
              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (current_matching_time >= emVideoView.getDuration()) {

                            httppost.addHeader("watch_status", "complete");
                        }else{
                            httppost.addHeader("watch_status", "halfplay");

                        }

                    }

                });*/

                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoLogId);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoLogId = "0";

                        }

                    });

                } catch (IOException e) {
                    videoLogId = "0";

                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    if (statusCode == 200) {
                        videoLogId = myJson.optString("log_id");
                    } else {
                        videoLogId = "0";
                    }

                }

            } catch (Exception e) {
                videoLogId = "0";

            }

            return null;
        }


        protected void onPostExecute(Void result) {
         /*   try {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                videoLogId = "0";
            }*/
            if (responseStr == null) {
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

        @Override
        protected void onPreExecute() {
            stoptimertask();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {

                playerModel.call_finish_at_onUserLeaveHint = false;

                if (data.getStringExtra("yes").equals("1002")) {

                    watchStatus = "halfplay";
                    playerPosition = playerModel.getPlayPos();
                    emVideoView.start();
                    emVideoView.seekTo(played_length);
                    seekBar.setProgress(played_length);
                    updateProgressBar();

                } else {
                    emVideoView.start();
                    seekBar.setProgress(emVideoView.getCurrentPosition());
                    updateProgressBar();
                }


                if (SubTitlePath.size() > 0) {

                    CheckSubTitleParsingType("1");

                    subtitleDisplayHandler = new Handler();
                    subsFetchTask = new SubtitleProcessingTask("1");
                    subsFetchTask.execute();
                } else {
                    if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                        asyncVideoLogDetails = new AsyncVideoLogDetails();
                        asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                    }

                }

            }
            if (requestCode == 3333) {
                // This is for Subtitle feature

                if (data.getStringExtra("type").equals("subtitle")) {

//                    Toast.makeText(getApplicationContext(),"subtitle == "+data.getStringExtra("position"),Toast.LENGTH_SHORT).show();
                    if (!data.getStringExtra("position").equals("nothing")) {

                        if (data.getStringExtra("position").equals("0")) {
                            // Stop Showing Subtitle
                            if (subtitleDisplayHandler != null)
                                subtitleDisplayHandler.removeCallbacks(subtitleProcessesor);
                            subtitleText.setText("");
                        } else {
                            try {

                                CheckSubTitleParsingType(data.getStringExtra("position"));

                                subtitleDisplayHandler = new Handler();
                                subsFetchTask = new SubtitleProcessingTask(data.getStringExtra("position"));
                                subsFetchTask.execute();
                            } catch (Exception e) {
                                Log.v("SUBHA", "Exception of subtitle change process =" + e.toString());
                            }
                        }
                    }
                }

                // This is for Resolution feature

                if (data.getStringExtra("type").equals("resolution")) {

//                Toast.makeText(getApplicationContext(),"resolution == "+data.getStringExtra("position"),Toast.LENGTH_SHORT).show();
                    mHandler.removeCallbacks(updateTimeTask);
                    if (!data.getStringExtra("position").equals("nothing")) {

                        if (!emVideoView.isPlaying()) {
                            is_paused = true;
                        }

                        change_resolution = true;
                        ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.VISIBLE);
                        emVideoView.setVideoURI(Uri.parse(ResolutionUrl.get(Integer.parseInt(data.getStringExtra("position")))));

                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerModel.hide_pause = false;

        if (MovableTimer != null)
            MovableTimer.cancel();
    }

    // Added Later By Bibhu For Subtitle Feature.

    public class SubtitleProcessingTask extends AsyncTask<Void, Void, Void> {


        String Subtitle_Path = "";

        public SubtitleProcessingTask(String path) {
//            Log.v("SUBHA","SubTitlePath size ==="+SubTitlePath.size());
//             Subtitle_Path = Environment.getExternalStorageDirectory().toString()+"/"+"sub.vtt";
            Subtitle_Path = SubTitlePath.get((Integer.parseInt(path) - 1));
        }

        @Override
        protected void onPreExecute() {
//            subtitleText.setText("Loading subtitles..");
            super.onPreExecute();
            Log.v("SUBHA", "SubTitlePath size at pre execute===" + SubTitlePath.size());
        }

        @Override
        protected Void doInBackground(Void... params) {
            // int count;
            try {

                Log.v("SUBHA", "Subtitle_Path ========" + Subtitle_Path);

				/*
                 * if you want to download file from Internet, use commented
				 * code.
				 */
                // URL url = new URL(
                // "https://dozeu380nojz8.cloudfront.net/uploads/video/subtitle_file/3533/srt_919a069ace_boss.srt");
                // InputStream is = url.openStream();
                // File f = getExternalFile();
                // FileOutputStream fos = new FileOutputStream(f);
                // byte data[] = new byte[1024];
                // while ((count = is.read(data)) != -1) {
                // fos.write(data, 0, count);
                // }
                // is.close();
//                // fos.close();
//                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/sintel.vtt");
////

//                File root = android.os.Environment.getExternalStorageDirectory();
//                mediaStorageDir = new File(root+"/Android/data/"+getApplicationContext().getPackageName().trim()+"/SubTitleList/", "");


//                String path = Environment.getExternalStorageDirectory().toString()+"/sub.vtt";
//                File myFile = new File(path);
                File myFile = new File(Subtitle_Path);
                InputStream fIn = new FileInputStream(String.valueOf(myFile));


              /* InputStream stream = getResources().openRawResource(
                        R.raw.subtitle);*/

                if (callWithoutCaption) {
                    Log.v("BIBHU", "Without Caption Called");
                    FormatSRT_WithoutCaption formatSRT = new FormatSRT_WithoutCaption();
                    srt = formatSRT.parseFile("sample", fIn);
                } else {
                    Log.v("BIBHU", "With Caption Called");
                    FormatSRT formatSRT = new FormatSRT();
                    srt = formatSRT.parseFile("sample", fIn);
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("SUBHA", "error in downloadinf subs");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (null != srt) {
                subtitleText.setText("");
                subtitleDisplayHandler.post(subtitleProcessesor);
//                Toast.makeText(getApplicationContext(), "subtitles loaded!!",Toast.LENGTH_SHORT).show();
            }
            if (!userIdStr.matches("") && !movieId.matches("") && !playerModel.getAuthTokenStr().matches("") && !episodeId.matches("") && !ipAddressStr.matches("")) {
                asyncVideoLogDetails = new AsyncVideoLogDetails();
                asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
            }


            super.onPostExecute(result);
        }
    }

    public void onTimedText(Caption text) {
        if (text == null) {
            subtitleText.setVisibility(View.INVISIBLE);
            return;
        }
        subtitleText.setText(Html.fromHtml(text.content));
        subtitleText.setVisibility(View.VISIBLE);

    }

    @Override
    public void finish() {
        cleanUp();
        super.finish();
    }

    private void cleanUp() {
        if (subtitleDisplayHandler != null) {
            subtitleDisplayHandler.removeCallbacks(subtitleProcessesor);
        }

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

    private Runnable subtitleProcessesor = new Runnable() {

        @Override
        public void run() {
            if (emVideoView != null && emVideoView.isPlaying()) {
                int currentPos = emVideoView.getCurrentPosition();
                Collection<Caption> subtitles = srt.captions.values();
                for (Caption caption : subtitles) {
                    if (currentPos >= caption.start.mseconds
                            && currentPos <= caption.end.mseconds) {
                        onTimedText(caption);
                        break;
                    } else if (currentPos > caption.end.mseconds) {
                        onTimedText(null);
                    }
                }
            }
            subtitleDisplayHandler.postDelayed(this, 100);
        }
    };

    public void CheckSubTitleParsingType(String path) {

        String Subtitle_Path = SubTitlePath.get((Integer.parseInt(path) - 1));

//        String Subtitle_Path = Environment.getExternalStorageDirectory().toString()+"/"+"sub.vtt";

        Log.v("BIBHU", "Subtitle_Path at CheckSubTitleParsingType = " + Subtitle_Path);
        Log.v("BIBHU", "Subtitle_Path at CheckSubTitleParsingType size = " + SubTitlePath.size());

        callWithoutCaption = true;

        File myFile = new File(Subtitle_Path);
        BufferedReader test_br = null;
        InputStream stream = null;
        InputStreamReader in = null;
        try {
            stream = new FileInputStream(String.valueOf(myFile));
            in = new InputStreamReader(stream);
            test_br = new BufferedReader(in);

        } catch (Exception e) {
            e.printStackTrace();
        }

        int testinglinecounter = 1;
        int captionNumber = 1;


        String TestingLine = null;
        try {
            TestingLine = test_br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (testinglinecounter < 6) {
            try {
                Log.v("BIBHU", "Testing Liane at Mainactivity = " + TestingLine.toString());

                if (Integer.parseInt(TestingLine.toString().trim()) == captionNumber) {
                    callWithoutCaption = false;
                    testinglinecounter = 6;
                }
            } catch (Exception e) {
                try {
                    TestingLine = test_br.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                testinglinecounter++;
                Log.v("BIBHU", "Total no of line at Mainactivity = " + testinglinecounter);
            }
        }
    }


    // This is added for the movable water mark //

    public void MoveWaterMark() {

        Rect rectf = new Rect();
        emVideoView.getLocalVisibleRect(rectf);
        int mainLayout_width = rectf.width() - 50;
        int mainLayout_height = rectf.height() - 120;


        // Child layout Lyout details

        Rect rectf1 = new Rect();
        linearLayout1.getLocalVisibleRect(rectf1);
        int childLayout_width = rectf1.width();
        int childLayout_height = rectf1.height();

        boolean show = true;

        while (show) {

            Random r = new Random();
            final int xLeft = r.nextInt(mainLayout_width - 10) + 10;

            final int min = 10;
            final int max = mainLayout_height;
            final int yUp = new Random().nextInt((max - min) + 1) + min;


            Log.v("BIBHU", "==========================================" + "\n");

            Log.v("BIBHU", "mainLayout_width  ===" + mainLayout_width);
            Log.v("BIBHU", "mainLayout_height  ===" + mainLayout_height);

            Log.v("BIBHU", "childLayout_width  ===" + childLayout_width);
            Log.v("BIBHU", "childLayout_height  ===" + childLayout_height);


            Log.v("BIBHU", "xLeft  ===" + xLeft);
            Log.v("BIBHU", "yUp  ===" + yUp);

            Log.v("BIBHU", "width addition  ===" + (childLayout_width + xLeft));
            Log.v("BIBHU", "height addition   ===" + (childLayout_height + yUp));

            if ((mainLayout_width > (childLayout_width + xLeft)) && (mainLayout_height > (childLayout_height + yUp))) {
                show = false;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    linearLayout1.setX(xLeft);
                    linearLayout1.setY(yUp);
                }
            });


        }
    }


    private void hideSystemUI() {
        Log.v("BKS","hidesystem");
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        story.setText("");
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showSystemUI() {
        Log.v("BKS","showsystem");
        story.setText(playerModel.getVideoStory());
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        );
    }


}
