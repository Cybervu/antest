
package com.home.vod.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.home.apisdk.apiController.GetFFVideoLogDetailsAsync;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetVideoLogsAsynTask;
import com.home.apisdk.apiController.MyLibraryAsynTask;
import com.home.apisdk.apiController.ResumeVideoLogDetailsAsync;
import com.home.apisdk.apiModel.FFVideoLogDetailsInput;
import com.home.apisdk.apiModel.ResumeVideoLogDetailsInput;
import com.home.apisdk.apiModel.VideoLogsInputModel;
import com.home.vod.R;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.subtitle_support.Caption;
import com.home.vod.subtitle_support.FormatSRT;
import com.home.vod.subtitle_support.FormatSRT_WithoutCaption;
import com.home.vod.subtitle_support.TimedTextObject;
import com.home.vod.util.ExpandableTextView;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.SensorOrientationChangeNotifier;
import com.home.vod.util.Util;

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

public class MyLibraryPlayer extends AppCompatActivity implements SensorOrientationChangeNotifier.Listener,GetVideoLogsAsynTask.GetVideoLogs,
        GetIpAddressAsynTask.IpAddress,ResumeVideoLogDetailsAsync.ResumeVideoLogDetails,GetFFVideoLogDetailsAsync.GetFFVideoLogs{
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
    String watchSt = "halfplay";
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
    ExpandableTextView story;
    private EMVideoView emVideoView;
    int seek_label_pos = 0;
    int content_types_id = 0;


    // Adder Later // By MUVI

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
        SensorOrientationChangeNotifier.getInstance(MyLibraryPlayer.this).addListener(this);


        // Added For FCM
        // Call Api to Check User's Login Status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        content_types_id = Util.dataModel.getContentTypesId();
        played_length = Util.dataModel.getPlayPos() * 1000;

        Util.goToLibraryplayer = true;

        if (Util.dataModel.getVideoUrl().matches("")) {
            backCalled();
            //onBackPressed();
        }
        movieId = Util.dataModel.getMovieUniqueId();
        episodeId = Util.dataModel.getEpisode_id();

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        emailIdStr = preferenceManager.getEmailIdFromPref();
        userIdStr = preferenceManager.getUseridFromPref();


        if (emailIdStr == null) {
            emailIdStr = "";

        } if (userIdStr == null) {
            userIdStr = "";

        }

        emVideoView = (EMVideoView) findViewById(R.id.emVideoView);
        subtitleText = (TextView) findViewById(R.id.offLine_subtitleText);
        subtitle_change_btn = (ImageView) findViewById(R.id.subtitle_change_btn);

        latest_center_play_pause = (ImageButton) findViewById(R.id.latest_center_play_pause);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        Typeface videoTitleface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoTitle.setTypeface(videoTitleface);
        GenreTextView = (TextView) findViewById(R.id.GenreTextView);
        Typeface GenreTextViewface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        GenreTextView.setTypeface(GenreTextViewface);
        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        Typeface videoDurationTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoDurationTextView.setTypeface(videoDurationTextViewface);
        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        Typeface videoCensorRatingTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoCensorRatingTextView.setTypeface(videoCensorRatingTextViewface);
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
        Typeface videoCensorRatingTextView1face = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoCensorRatingTextView1.setTypeface(videoCensorRatingTextView1face);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        Typeface videoReleaseDateTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoReleaseDateTextView.setTypeface(videoReleaseDateTextViewface);
        story = (ExpandableTextView) findViewById(R.id.story);
        Typeface storyTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        story.setTypeface(storyTypeface);
        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        videoCastCrewTitleTextView.setTypeface(watchTrailerButtonTypeface);
        videoCastCrewTitleTextView.setText(Util.getTextofLanguage(MyLibraryPlayer.this, Util.CAST_CREW_BUTTON_TITLE, Util.DEFAULT_CAST_CREW_BUTTON_TITLE));


        MovableTimer = new Timer();
        MovableTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                MoveWaterMark();
            }
        },1000,1000);

        //Call For Subtitle Loading // Added By MUVI


        if (getIntent().getStringArrayListExtra("subTitleName") != null) {
            SubTitleName = getIntent().getStringArrayListExtra("subTitleName");
        } else {
            SubTitleName.clear();
        }

        if (getIntent().getStringArrayListExtra("subTitlePath") != null) {
            SubTitlePath = getIntent().getStringArrayListExtra("subTitlePath");
        } else {
            SubTitlePath.clear();
        }

       /* if(subTitlePath.size()<1)
        {
            subtitle_change_btn.setVisibility(View.INVISIBLE);
            Log.v("MUVI","CC Invisible called");
        }*/

        subtitle_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* try{
                    Util.call_finish_at_onUserLeaveHint = false;
                    Intent intent = new Intent(MyLibraryPlayer.this,SubtitleList.class);
                    intent.putExtra("subTitleName",subTitleName);
                    intent.putExtra("subTitlePath",subTitlePath);
                    startActivityForResult(intent,222);
                }catch (Exception e){Log.v("MUVI","Exception of subtitle change click ="+e.toString());}*/


                Util.call_finish_at_onUserLeaveHint = false;
                Intent intent = new Intent(MyLibraryPlayer.this,Subtitle_Resolution.class);
                intent.putExtra("resolutionFormat",ResolutionFormat);
                intent.putExtra("resolutionUrl",ResolutionUrl);
                intent.putExtra("subTitleName",SubTitleName);
                intent.putExtra("subTitlePath",SubTitlePath);
                startActivityForResult(intent,3333);


            }
        });

        //=========================End=================================//


        //=============================== Resolution Change ===================================//

      /*  if (getIntent().getStringArrayListExtra("resolutionFormat") != null) {
            resolutionFormat = getIntent().getStringArrayListExtra("resolutionFormat");
        } else {
            resolutionFormat.clear();
        }

        if (getIntent().getStringArrayListExtra("resolutionUrl") != null) {
            resolutionUrl = getIntent().getStringArrayListExtra("resolutionUrl");
        } else {
            resolutionUrl.clear();
        }

        if(resolutionUrl.size()<1)

        {
            // Add your code
            Log.v("MUVI","resolution image Invisible called");
        }
        else
        {
            resolutionUrl.add(Util.dataModel.getVideoUrl().trim());
            resolutionFormat.add("Auto");
        }
*/
        Util.VideoResolution = "Auto";
        Util.DefaultSubtitle = "Off";

        if (getIntent().getStringArrayListExtra("resolutionFormat") != null) {
            ResolutionFormat = getIntent().getStringArrayListExtra("resolutionFormat");
        } else {
            ResolutionFormat.clear();

        }

        if (getIntent().getStringArrayListExtra("resolutionUrl") != null) {
            ResolutionUrl = getIntent().getStringArrayListExtra("resolutionUrl");
        } else {
            ResolutionUrl.clear();
        }

        if(ResolutionUrl.size()<1)

        {
            // Add your code
            LogUtil.showLog("MUVI","resolution image Invisible called");
        }
        else
        {
            ResolutionUrl.add(Util.dataModel.getVideoUrl().trim());
            ResolutionFormat.add("Auto");
        }

        if(ResolutionFormat.size()>0)
        {
            Collections.reverse(ResolutionFormat);
            for(int m=0;m<ResolutionFormat.size();m++)
            {
                LogUtil.showLog("MUVI","RESOLUTION FORMAT======"+ResolutionFormat.get(m));
            }
        }
        if(ResolutionUrl.size()>0)
        {
            Collections.reverse(ResolutionUrl);
            for(int n=0;n<ResolutionUrl.size();n++)
            {
                LogUtil.showLog("MUVI","RESOLUTION URL======"+ResolutionUrl.get(n));
            }
        }





        //=========================End=================================//



        if((SubTitlePath.size()<1) && (ResolutionUrl.size()<1))
        {
            subtitle_change_btn.setVisibility(View.INVISIBLE);
            LogUtil.showLog("MUVI","CC Invisible called");
        }


   /*     emVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                try{

                  *//*  resolutionFormat.clear();
                    resolutionUrl.clear();

                    String url1 = "https://r2---sn-p5qs7n7s.googlevideo.com/videoplayback?ms=au&clen=9643006&mv=m&mt=1490847859&expire=1490869548&ei=zIjcWNiIB4--1gK12J2wDg&requiressl=yes&ipbits=0&mn=sn-p5qs7n7s&mm=31&id=o-AOBNENDOg3dWResPJrDOrzcdkVLJzIAYZWhQU5ZbrB4w&itag=17&key=yt6&ip=159.253.144.86&dur=965.578&lmt=1476270027971356&upn=CmLw7ROk5F8&mime=video%2F3gpp&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cupn%2Cexpire&initcwndbps=3532500&gir=yes&source=youtube&pl=24&signature=D4A534C89E9CF57FCA86E4F48F551DAE17C35A42.1BED6199D2C1CBC51FE759376BA25E3CA16B6592&title=8+Future+Trucks+%26+Buses+YOU+MUST+SEE.mp4";
                    String url2 = "https://r2---sn-p5qs7n7s.googlevideo.com/videoplayback?ms=au&ei=zIjcWNiIB4--1gK12J2wDg&mv=m&mt=1490847859&expire=1490869548&requiressl=yes&ipbits=0&mn=sn-p5qs7n7s&mm=31&id=o-AOBNENDOg3dWResPJrDOrzcdkVLJzIAYZWhQU5ZbrB4w&itag=22&key=yt6&ip=159.253.144.86&dur=965.508&lmt=1476447036908539&upn=CmLw7ROk5F8&mime=video%2Fmp4&sparams=dur%2Cei%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&initcwndbps=3532500&ratebypass=yes&source=youtube&pl=24&signature=03EA48DAB77BB926A74DE6071C1166B96E95D247.62FF4140E2C639E6E4F5EE1CDEEE174B37C65739&title=8+Future+Trucks+%26+Buses+YOU+MUST+SEE.mp4";
                    resolutionFormat.add("144p");
                    resolutionFormat.add("720p");
                    resolutionFormat.add("Auto");

                    resolutionUrl.add(url1);
                    resolutionUrl.add(url2);
                    resolutionUrl.add(url1);*//*

                    Util.call_finish_at_onUserLeaveHint = false;
                    Intent intent = new Intent(MyLibraryPlayer.this,Subtitle_Resolution.class);
                    intent.putExtra("resolutionFormat",resolutionFormat);
                    intent.putExtra("resolutionUrl",resolutionUrl);
                    intent.putExtra("subTitleName",subTitleName);
                    intent.putExtra("subTitlePath",subTitlePath);
                    startActivityForResult(intent,3333);


                *//*    Util.call_finish_at_onUserLeaveHint = false;
                    Intent intent = new Intent(MyLibraryPlayer.this,ResolutionChangeActivity.class);
                    intent.putExtra("resolutionFormat",resolutionFormat);
                    intent.putExtra("resolutionUrl",resolutionUrl);
                    startActivityForResult(intent,3333);*//*
                }catch (Exception e){Log.v("MUVI","Exception of subtitle change click ="+e.toString());}

                return false;
            }
        });*/

        //=============================== End Resolution Change ===================================//




        player_layout = (RelativeLayout) findViewById(R.id.player_layout);
        player_layout_height = player_layout.getHeight();
        player_layout_width = player_layout.getWidth();

        primary_ll = (LinearLayout) findViewById(R.id.primary_ll);
        last_ll = (LinearLayout) findViewById(R.id.last_ll);
        last_ll = (LinearLayout) findViewById(R.id.last_ll);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        ipAddressTextView = (TextView) findViewById(R.id.emailAddressTextView);
        emailAddressTextView = (TextView) findViewById(R.id.ipAddressTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        Typeface Tf = Typeface.createFromAsset(getAssets(), "fonts/SOURCESANSPRO-REGULAR.OTF");
        ipAddressTextView.setTypeface(Tf);
        emailAddressTextView.setTypeface(Tf);
        dateTextView.setTypeface(Tf);


        emailAddressTextView.setText(emailIdStr);
        dateTextView.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));




       /* ipAddressTextView.setVisibility(View.GONE);
        emailAddressTextView.setVisibility(View.GONE);
        dateTextView.setVisibility(View.GONE);*/

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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        player_layout.setLayoutParams(params);
        compress_expand.setImageResource(R.drawable.ic_media_fullscreen_shrink);
        compress_expand.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });

        hideSystemUI();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

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

                        if(SubTitlePath.size()>0 || ResolutionUrl.size()>0)
                        {
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
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {

                    LinearLayout.LayoutParams params1 = null;
                    if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)){
                        if(MyLibraryPlayer.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
                        if(MyLibraryPlayer.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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

                LogUtil.showLog("MUVI","This is the first calling point");
                LogUtil.showLog("MUVI","Played Length ="+Util.dataModel.getPlayPos());

                if(change_resolution)
                {

                    change_resolution = false;
                    emVideoView.start();
                    emVideoView.seekTo(seekBarProgress);
                    seekBar.setProgress(emVideoView.getCurrentPosition());

                    if(is_paused)
                    {
                        is_paused = false;
                        emVideoView.pause();
                        progressView.setVisibility(View.GONE);
                    }
                    else {
                        updateProgressBar();
                    }

                }
                else
                {

                    // have to delete

                   /* CheckSubTitleParsingType("1");
                    subtitleDisplayHandler = new Handler();
                    subsFetchTask = new SubtitleProcessingTask("1");
                    subsFetchTask.execute();*/

                    // end here//

                    if (Util.dataModel.getPlayPos() >= emVideoView.getDuration() / 1000) {
                        played_length = 0;
                    }

                    video_completed = false;
                    progressView.setVisibility(View.VISIBLE);
                    center_play_pause.setVisibility(View.GONE);
                    latest_center_play_pause.setVisibility(View.GONE);


                    try {

                        //video log
                        if (content_types_id == 4) {


                            if(SubTitlePath.size()>0)
                            {
                                CheckSubTitleParsingType("1");
                                subtitleDisplayHandler = new Handler();
                                subsFetchTask = new SubtitleProcessingTask("1");
                                subsFetchTask.execute();
                            }
                            else {
                                VideoLogsInputModel videoLogsInputModel=new VideoLogsInputModel();
                                videoLogsInputModel.setAuthToken(Util.authTokenStr);
                                videoLogsInputModel.setUserId(userIdStr.trim());
                                videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                                videoLogsInputModel.setMuviUniqueId(movieId.trim());
                                videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                                videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                                videoLogsInputModel.setWatchStatus(watchStatus);
                                videoLogsInputModel.setDeviceType("2");
                                videoLogsInputModel.setVideoLogId(videoLogId);
                                asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel, MyLibraryPlayer.this,MyLibraryPlayer.this);
                                asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                            }

                            emVideoView.start();
                            updateProgressBar();
                        } else {
                            startTimer();

                            if (played_length > 0) {
                                Util.call_finish_at_onUserLeaveHint = false;
                                ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.GONE);
                                Intent resumeIntent = new Intent(MyLibraryPlayer.this, MyLibraryResumePopupActivity.class);
                                startActivityForResult(resumeIntent, 1001);
                            } else {


                                emVideoView.start();
                                seekBar.setProgress(emVideoView.getCurrentPosition());
                                updateProgressBar();

                                if(SubTitlePath.size()>0)
                                {
                                    CheckSubTitleParsingType("1");
                                    subtitleDisplayHandler = new Handler();
                                    subsFetchTask = new SubtitleProcessingTask("1");
                                    subsFetchTask.execute();
                                }
                                else {
                                    VideoLogsInputModel videoLogsInputModel=new VideoLogsInputModel();
                                    videoLogsInputModel.setAuthToken(Util.authTokenStr);
                                    videoLogsInputModel.setUserId(userIdStr.trim());
                                    videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                                    videoLogsInputModel.setMuviUniqueId(movieId.trim());
                                    videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                                    videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                                    videoLogsInputModel.setWatchStatus(watchStatus);
                                    videoLogsInputModel.setDeviceType("2");
                                    videoLogsInputModel.setVideoLogId(videoLogId);
                                    asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,MyLibraryPlayer.this,MyLibraryPlayer.this);
                                    asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
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


        emVideoView.setVideoURI(Uri.parse(Util.dataModel.getVideoUrl()));

    }

    @Override
    public void onGetVideoLogsPreExecuteStarted() {
        LogUtil.showLog("MUVI", "onPreExecute");
        stoptimertask();
        LogUtil.showLog("MUVI", "onPreExecute1");
    }

    @Override
    public void onGetVideoLogsPostExecuteCompleted(int status, String message, String videoLogId) {

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
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("user_id", userIdStr.trim());
//                httppost.addHeader("ip_address", ipAddressStr.trim());
//                httppost.addHeader("movie_id", movieId.trim());
//                httppost.addHeader("episode_id", episodeId.trim());
//                httppost.addHeader("played_length", String.valueOf(playerPosition));
//                httppost.addHeader("watch_status", watchStatus);
//                httppost.addHeader("device_type", "2");
//                httppost.addHeader("log_id", videoLogId);
//
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//                    Log.v("MUVI", "PLAY responseStr" + responseStr);
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
//                    Log.v("MUVI", "Exception of videoplayer" + e.toString());
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
//
//        @Override
//        protected void onPreExecute() {
//            Log.v("MUVI", "onPreExecute");
//            stoptimertask();
//            Log.v("MUVI", "onPreExecute1");
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
                                    FFVideoLogDetailsInput ffVideoLogDetailsInput= new FFVideoLogDetailsInput();
                                    ffVideoLogDetailsInput.setAuthToken(Util.authTokenStr);
                                    ffVideoLogDetailsInput.setUser_id(userIdStr);
                                    ffVideoLogDetailsInput.setIp_address(ipAddressStr.trim());
                                    ffVideoLogDetailsInput.setMovie_id(movieId.trim());
                                    ffVideoLogDetailsInput.setEpisode_id(episodeId.trim());
                                    ffVideoLogDetailsInput.setPlayed_length(String.valueOf(playerPosition));
                                    ffVideoLogDetailsInput.setWatch_status(watchStatus);
                                    ffVideoLogDetailsInput.setDevice_type("2");
                                    ffVideoLogDetailsInput.setLog_id(videoLogId);
                                    asyncFFVideoLogDetails = new GetFFVideoLogDetailsAsync(ffVideoLogDetailsInput,MyLibraryPlayer.this,MyLibraryPlayer.this);
                                    watchStatus = "complete";
                                    asyncFFVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    FFVideoLogDetailsInput ffVideoLogDetailsInput= new FFVideoLogDetailsInput();
                                    ffVideoLogDetailsInput.setAuthToken(Util.authTokenStr);
                                    ffVideoLogDetailsInput.setUser_id(userIdStr);
                                    ffVideoLogDetailsInput.setIp_address(ipAddressStr.trim());
                                    ffVideoLogDetailsInput.setMovie_id(movieId.trim());
                                    ffVideoLogDetailsInput.setEpisode_id(episodeId.trim());
                                    ffVideoLogDetailsInput.setPlayed_length(String.valueOf(playerPosition));
                                    ffVideoLogDetailsInput.setWatch_status(watchStatus);
                                    ffVideoLogDetailsInput.setDevice_type("2");
                                    ffVideoLogDetailsInput.setLog_id(videoLogId);
                                    asyncFFVideoLogDetails = new GetFFVideoLogDetailsAsync(ffVideoLogDetailsInput,MyLibraryPlayer.this,MyLibraryPlayer.this);
                                    watchStatus = "halfplay";
                                    asyncFFVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                }

                            } else if (isFastForward == false && currentPositionStr >= millisecondsToString(playerPreviousPosition)) {

                                playerPreviousPosition = 0;

                                int duration = emVideoView.getDuration() / 1000;
                                if (currentPositionStr > 0 && currentPositionStr == duration) {
                                    VideoLogsInputModel videoLogsInputModel=new VideoLogsInputModel();
                                    videoLogsInputModel.setAuthToken(Util.authTokenStr);
                                    videoLogsInputModel.setUserId(userIdStr.trim());
                                    videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                                    videoLogsInputModel.setMuviUniqueId(movieId.trim());
                                    videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                                    videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                                    videoLogsInputModel.setWatchStatus(watchStatus);
                                    videoLogsInputModel.setDeviceType("2");
                                    videoLogsInputModel.setVideoLogId(videoLogId);
                                    asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,MyLibraryPlayer.this,MyLibraryPlayer.this);
                                    watchStatus = "complete";
                                    asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                                } else if (currentPositionStr > 0 && currentPositionStr % 60 == 0) {
                                    VideoLogsInputModel videoLogsInputModel=new VideoLogsInputModel();
                                    videoLogsInputModel.setAuthToken(Util.authTokenStr);
                                    videoLogsInputModel.setUserId(userIdStr.trim());
                                    videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                                    videoLogsInputModel.setMuviUniqueId(movieId.trim());
                                    videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                                    videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                                    videoLogsInputModel.setWatchStatus(watchStatus);
                                    videoLogsInputModel.setDeviceType("2");
                                    videoLogsInputModel.setVideoLogId(videoLogId);
                                    asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,MyLibraryPlayer.this,MyLibraryPlayer.this);
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
        ipAddressTextView.setText(ipAddressStr);
    }

    @Override
    public void onGetFFVideoLogsPreExecuteStarted() {
        stoptimertask();
    }

    @Override
    public void onGetFFVideoLogsPostExecuteCompleted(int code, String status, String videoLogId) {
        if (status == null) {
            videoLogId = "0";

        }
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
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("user_id", userIdStr);
//                httppost.addHeader("ip_address", ipAddressStr.trim());
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
        int seconds = (int) (milliseconds / 1000);

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
        }

        current_time_position_timer();

    }


//    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//
//                // Execute HTTP Post Request
//                try {
//                    URL myurl = new URL(Util.loadIPUrl);
//                    HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
//                    InputStream ins = con.getInputStream();
//                    InputStreamReader isr = new InputStreamReader(ins);
//                    BufferedReader in = new BufferedReader(isr);
//
//                    String inputLine;
//
//                    while ((inputLine = in.readLine()) != null) {
//                        System.out.println(inputLine);
//                        responseStr = inputLine;
//                    }
//
//                    in.close();
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    ipAddressStr = "";
//
//                } catch (UnsupportedEncodingException e) {
//
//                    ipAddressStr = "";
//
//                } catch (IOException e) {
//                    ipAddressStr = "";
//
//                }
//                if (responseStr != null) {
//                    Object json = new JSONTokener(responseStr).nextValue();
//                    if (json instanceof JSONObject) {
//                        ipAddressStr = ((JSONObject) json).getString("ip");
//                    }
//                }
//
//            } catch (Exception e) {
//                ipAddressStr = "";
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            ipAddressTextView.setText(ipAddressStr);
//
//            if (responseStr == null) {
//                ipAddressStr = "";
//            }
//            return;
//        }
//
//        protected void onPreExecute() {
//
//        }
//    }


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

            if (content_types_id != 4) {
                try{
                    seek_label_pos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();
                }catch (Exception e){}
            }

            current_matching_time = emVideoView.getCurrentPosition();


            if ((previous_matching_time == current_matching_time) && (current_matching_time < emVideoView.getDuration())) {
                ((ProgressBar) findViewById(R.id.progress_view)).setVisibility(View.VISIBLE);
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

            ResumeVideoLogDetailsInput resumeVideoLogDetailsInput=new ResumeVideoLogDetailsInput();
            resumeVideoLogDetailsInput.setAuthToken(Util.authTokenStr);
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
        resumeVideoLogDetailsInput.setAuthToken(Util.authTokenStr);
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
         Log.v("MUVI","HHVID"+videoLogId);
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
            Log.v("MUVI","FHFHFHCALLED");
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
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("user_id", userIdStr.trim());
//                httppost.addHeader("ip_address", ipAddressStr.trim());
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
//
//    }

    public void ShowResumeDialog(String Title, String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MyLibraryPlayer.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(MyLibraryPlayer.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(MyLibraryPlayer.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {

                Util.call_finish_at_onUserLeaveHint = true;

                if (data.getStringExtra("yes").equals("1002")) {

                    watchStatus = "halfplay";
                    playerPosition = Util.dataModel.getPlayPos();
                    emVideoView.start();
                    emVideoView.seekTo(played_length);
                    seekBar.setProgress(played_length);
                    updateProgressBar();

                } else {
                    emVideoView.start();
                    seekBar.setProgress(emVideoView.getCurrentPosition());
                    updateProgressBar();
                }


                if(SubTitlePath.size()>0)
                {

                    CheckSubTitleParsingType("1");

                    subtitleDisplayHandler = new Handler();
                    subsFetchTask = new SubtitleProcessingTask("1");
                    subsFetchTask.execute();
                }
                else {
                    VideoLogsInputModel videoLogsInputModel=new VideoLogsInputModel();
                    videoLogsInputModel.setAuthToken(Util.authTokenStr);
                    videoLogsInputModel.setUserId(userIdStr.trim());
                    videoLogsInputModel.setIpAddress(ipAddressStr.trim());
                    videoLogsInputModel.setMuviUniqueId(movieId.trim());
                    videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
                    videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
                    videoLogsInputModel.setWatchStatus(watchStatus);
                    videoLogsInputModel.setDeviceType("2");
                    videoLogsInputModel.setVideoLogId(videoLogId);
                    asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,this,this);
                    asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
                }

            }
            if(requestCode == 3333)
            {
                // This is for Subtitle feature

                if (data.getStringExtra("type").equals("subtitle")) {

//                    Toast.makeText(getApplicationContext(),"subtitle == "+data.getStringExtra("position"),Toast.LENGTH_SHORT).show();
                    if (!data.getStringExtra("position").equals("nothing")) {

                        if(data.getStringExtra("position").equals("0"))
                        {
                            // Stop Showing Subtitle
                            if(subtitleDisplayHandler!=null)
                                subtitleDisplayHandler.removeCallbacks(subtitleProcessesor);
                            subtitleText.setText("");
                        }
                        else
                        {
                            try{

                                CheckSubTitleParsingType(data.getStringExtra("position"));

                                subtitleDisplayHandler = new Handler();
                                subsFetchTask = new SubtitleProcessingTask(data.getStringExtra("position"));
                                subsFetchTask.execute();
                            }catch (Exception e){
                                LogUtil.showLog("MUVI","Exception of subtitle change process ="+e.toString());}

                        }

                    }
                }

                // This is for Resolution feature

                if (data.getStringExtra("type").equals("resolution")) {

//                Toast.makeText(getApplicationContext(),"resolution == "+data.getStringExtra("position"),Toast.LENGTH_SHORT).show();
                    mHandler.removeCallbacks(updateTimeTask);
                    if (!data.getStringExtra("position").equals("nothing")) {

                        if (!emVideoView.isPlaying())
                        {
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
        Util.hide_pause = false;

        if(MovableTimer!=null)
            MovableTimer.cancel();
    }

    // Added Later By MUVI For Subtitle Feature.

    public class SubtitleProcessingTask extends AsyncTask<Void, Void, Void> {



        String Subtitle_Path = "";
        public SubtitleProcessingTask(String path) {
//            Log.v("MUVI","subTitlePath size ==="+subTitlePath.size());
//             Subtitle_Path = Environment.getExternalStorageDirectory().toString()+"/"+"sub.vtt";
            Subtitle_Path = SubTitlePath.get((Integer.parseInt(path)-1));
        }

        @Override
        protected void onPreExecute() {
//            subtitleText.setText("Loading subtitles..");
            super.onPreExecute();
            LogUtil.showLog("MUVI","subTitlePath size at pre execute==="+SubTitlePath.size());
        }

        @Override
        protected Void doInBackground(Void... params) {
            // int count;
            try {

                LogUtil.showLog("MUVI","Subtitle_Path ========"+Subtitle_Path);

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
                InputStream fIn = new FileInputStream( String.valueOf( myFile ) );


              /* InputStream stream = getResources().openRawResource(
                        R.raw.subtitle);*/

                if(callWithoutCaption)
                {
                    LogUtil.showLog("MUVI","Without Caption Called");
                    FormatSRT_WithoutCaption formatSRT = new FormatSRT_WithoutCaption();
                    srt = formatSRT.parseFile("sample", fIn);
                }
                else
                {
                    LogUtil.showLog("MUVI","With Caption Called");
                    FormatSRT formatSRT = new FormatSRT();
                    srt = formatSRT.parseFile("sample", fIn);
                }



            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.showLog("MUVI", "error in downloadinf subs");
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

            VideoLogsInputModel videoLogsInputModel=new VideoLogsInputModel();
            videoLogsInputModel.setAuthToken(Util.authTokenStr);
            videoLogsInputModel.setUserId(userIdStr.trim());
            videoLogsInputModel.setIpAddress(ipAddressStr.trim());
            videoLogsInputModel.setMuviUniqueId(movieId.trim());
            videoLogsInputModel.setEpisodeStreamUniqueId(episodeId.trim());
            videoLogsInputModel.setPlayedLength(String.valueOf(playerPosition));
            videoLogsInputModel.setWatchStatus(watchStatus);
            videoLogsInputModel.setDeviceType("2");
            videoLogsInputModel.setVideoLogId(videoLogId);
            asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,MyLibraryPlayer.this,MyLibraryPlayer.this);
            asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);

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

    public void CheckSubTitleParsingType(String path)
    {

        String Subtitle_Path = SubTitlePath.get((Integer.parseInt(path)-1));

//        String Subtitle_Path = Environment.getExternalStorageDirectory().toString()+"/"+"sub.vtt";

        LogUtil.showLog("MUVI","Subtitle_Path at CheckSubTitleParsingType = "+Subtitle_Path);
        LogUtil.showLog("MUVI","Subtitle_Path at CheckSubTitleParsingType size = "+SubTitlePath.size());

        callWithoutCaption = true;

        File myFile = new File(Subtitle_Path);
        BufferedReader test_br = null;
        InputStream stream = null;
        InputStreamReader in = null;
        try {
            stream = new FileInputStream( String.valueOf( myFile ));
            in= new InputStreamReader(stream);
            test_br =  new BufferedReader(in);

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
        while(testinglinecounter<6)
        {
            try
            {
                LogUtil.showLog("MUVI","Testing Liane at Mainactivity = "+TestingLine.toString());

                if(Integer.parseInt(TestingLine.toString().trim())==captionNumber)
                {
                    callWithoutCaption = false;
                    testinglinecounter = 6;
                }
            }
            catch (Exception e){
                try {
                    TestingLine = test_br.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                testinglinecounter++;
                LogUtil.showLog("MUVI","Total no of line at Mainactivity = "+testinglinecounter);
            }
        }
    }


    // This is added for the movable water mark //

    public void MoveWaterMark()
    {
        Rect rectf = new Rect();
        emVideoView.getLocalVisibleRect(rectf);
        int mainLayout_width = rectf.width()-50;
        int mainLayout_height = rectf.height() - 120;


        // Child layout Lyout details

        Rect rectf1 = new Rect();
        linearLayout1.getLocalVisibleRect(rectf1);
        int childLayout_width = rectf1.width();
        int childLayout_height = rectf1.height();

        boolean show = true;

        while (show)
        {

            Random r = new Random();
            final int xLeft =r.nextInt(mainLayout_width - 10) + 10;

            final int min = 10;
            final int max = mainLayout_height;
            final int yUp =  new Random().nextInt((max - min) + 1) + min;


            LogUtil.showLog("MUVI" ,"=========================================="+"\n");

            LogUtil.showLog("MUVI" ,"mainLayout_width  ==="+mainLayout_width);
            LogUtil.showLog("MUVI" ,"mainLayout_height  ==="+mainLayout_height);

            LogUtil.showLog("MUVI" ,"childLayout_width  ==="+childLayout_width);
            LogUtil.showLog("MUVI" ,"childLayout_height  ==="+childLayout_height);


            LogUtil.showLog("MUVI" ,"xLeft  ==="+xLeft);
            LogUtil.showLog("MUVI" ,"yUp  ==="+yUp);

            LogUtil.showLog("MUVI" ,"width addition  ==="+(childLayout_width+xLeft));
            LogUtil.showLog("MUVI" ,"height addition   ==="+(childLayout_height+yUp));

            if((mainLayout_width>(childLayout_width+xLeft)) && (mainLayout_height>(childLayout_height+yUp)))
            {
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


    // This API is called for cecking the Login status
}
