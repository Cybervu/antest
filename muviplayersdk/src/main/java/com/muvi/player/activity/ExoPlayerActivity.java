
package com.muvi.player.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
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

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.muviplayersdk.R;
import com.intertrust.wasabi.ErrorCodeException;
import com.intertrust.wasabi.Runtime;
import com.intertrust.wasabi.media.PlaylistProxy;
import com.intertrust.wasabi.media.PlaylistProxyListener;
import com.muvi.player.adapter.DownloadOptionAdapter;
import com.muvi.player.model.ContactModel1;
import com.muvi.player.model.SubtitleModel;
import com.muvi.player.subtitle_support.Caption;
import com.muvi.player.subtitle_support.FormatSRT;
import com.muvi.player.subtitle_support.FormatSRT_WithoutCaption;
import com.muvi.player.subtitle_support.TimedTextObject;
import com.muvi.player.utils.DBHelper;
import com.muvi.player.utils.ExpandableTextView;
import com.muvi.player.utils.ProgressBarHandler;
import com.muvi.player.utils.SensorOrientationChangeNotifier;
import com.muvi.player.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
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
import static java.lang.Math.round;


//====================for thirdparty player(DRM) it use======================
enum ContentTypes1 {
    DASH("application/dash+xml"), HLS("application/vnd.apple.mpegurl"), PDCF(
            "video/mp4"), M4F("video/mp4"), DCF("application/vnd.oma.drm.dcf"), BBTS(
            "video/mp2t");
    String mediaSourceParamsContentType = null;

    ContentTypes1(String mediaSourceParamsContentType) {
        this.mediaSourceParamsContentType = mediaSourceParamsContentType;
    }

    public String getMediaSourceParamsContentType() {
        return mediaSourceParamsContentType;
    }
}

//========================DRM code end===============================

public class ExoPlayerActivity extends AppCompatActivity implements SensorOrientationChangeNotifier.Listener, PlaylistProxyListener {

    //for check drm play or non drm play
    private static boolean isDrm=false;
    PlaylistProxy playerProxy;
    Player playerModel;
    int player_start_time = 0;
    DownloadManager.Request request;
    String timestamp;
    int played_length = 0;
    int playerStartPosition = 0;
    String restrict_stream_id = "0";
    /***** offline *****/
    DownloadManager downloadManager;
    RelativeLayout download_layout;
    public boolean downloading;
    //Handler mHandler;
    static String filename, path;
    ArrayList<ContactModel1> dmanager;
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
    float lenghtOfFile;
    float lengthfile;
    /***** offline *****/

    AsynWithdrm asynWithdrm;
    // This is changed for the new requirement of Offline Viewing.

    ArrayList<String> List_Of_DownloadFile_Url = new ArrayList<>();
    // This is only applicable for multiple download feature

    ArrayList<String> List_Of_FileSize = new ArrayList<>();
    ArrayList<String> List_Of_Resolution_Format = new ArrayList<>();
    ArrayList<String> List_Of_Resolution_Url = new ArrayList<>();
    ArrayList<String> List_Of_Resolution_Url_Used_For_Download = new ArrayList<>();

    ProgressBarHandler pDialog_for_gettig_filesize;
    AlertDialog alert;
    int selected_download_format = 0;


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
    long PreviousUsedData = 0;
    long CurrentUsedData = 0;

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
    long PreviousUsedData_By_DownloadContent = 0;
    String licensetoken;

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
        Log.v("MUVI","app name=="+getApplicationName(getApplicationContext()));

        PreviousUsedDataByApp(true);
        PreviousUsedData_By_DownloadContent = DataUsedByDownloadContent();
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
        //playerModel = new Player();
        playerModel = (Player) getIntent().getSerializableExtra("PlayerModel");
        //to check the videourl is drm or non drm

        if(playerModel.getVideoUrl().contains(".mpd")){
            isDrm=true;
        }else {
            isDrm=false;
        }

            if (playerModel != null && playerModel.getUserId() != null && !playerModel.getUserId().trim().matches("")) {
                userIdStr = playerModel.getUserId();
            }
            if (playerModel != null && playerModel.getEmailId() != null && !playerModel.getEmailId().trim().matches("")) {
                emailIdStr = playerModel.getEmailId();
            }
        // This is changed for the new requirement of Offline Viewing.
        startService(new Intent(this, DataConsumptionService.class));
        registerReceiver(SelectedUrl, new IntentFilter("UrlPosition"));

            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            exoplayerdownloadhandler = new Handler();
            dbHelper = new DBHelper(ExoPlayerActivity.this);
            dbHelper.getWritableDatabase();
            audio_1 = dbHelper.getContact(playerModel.getStreamUniqueId() + emailIdStr);


            if (audio_1 != null) {
                if (audio_1.getUSERNAME().trim().equals(emailIdStr.trim())) {
                    checkDownLoadStatusFromDownloadManager1(audio_1);
                }
            }

            download = (ImageView) findViewById(R.id.downloadImageView);
            Progress = (ProgressBar) findViewById(R.id.progressBar);
            percentg = (TextView) findViewById(R.id.percentage);


            //Check for offline content // Added By sanjay

            download_layout = (RelativeLayout) findViewById(R.id.downloadRelativeLayout);
            if (playerModel.getContentTypesId() != 4 && playerModel.getIsOffline().equals("1")) {
                download_layout.setVisibility(View.VISIBLE);
            }
            // if (playerModel.getContentTypesId() != 4 && playerModel.getIsOffline().equals("1"))


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List_Of_Resolution_Format.clear();
                    List_Of_FileSize.clear();
                    List_Of_Resolution_Url.clear();
                    List_Of_Resolution_Url_Used_For_Download.clear();


                    asynWithdrm = new AsynWithdrm();
                    asynWithdrm.executeOnExecutor(threadPoolExecutor);

                    // new DownloadFileFromURL().execute(playerModel.getVideoUrl());

                    if (playerModel.getOfflineUrl().size() > 0) {
                        Download_SubTitle(playerModel.getOfflineUrl().get(0));
                    }

                }
            });


            percentg.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExoPlayerActivity.this, R.style.MyAlertDialogStyle);
                                                dlgAlert.setTitle(Util.getTextofLanguage(ExoPlayerActivity.this, Util.STOP_SAVING_THIS_VIDEO, Util.DEFAULT_STOP_SAVING_THIS_VIDEO));
                                                dlgAlert.setMessage(Util.getTextofLanguage(ExoPlayerActivity.this, Util.YOUR_VIDEO_WONT_BE_SAVED, Util.DEFAULT_YOUR_VIDEO_WONT_BE_SAVED));
                                                dlgAlert.setPositiveButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.BTN_KEEP, Util.DEFAULT_BTN_KEEP), null);
                                                dlgAlert.setCancelable(false);
                                                dlgAlert.setPositiveButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.BTN_KEEP, Util.DEFAULT_BTN_KEEP),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();

                                                            }
                                                        });
                                                dlgAlert.setNegativeButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.BTN_DISCARD, Util.DEFAULT_BTN_DISCARD), null);
                                                dlgAlert.setCancelable(false);
                                                dlgAlert.setNegativeButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.BTN_DISCARD, Util.DEFAULT_BTN_DISCARD),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                downloading = false;
                                                                audio = dbHelper.getContact(playerModel.getStreamUniqueId() + emailIdStr);

                                                                if (audio != null) {
                                                                    String k = String.valueOf(audio.getDOWNLOADID());
                                                                    downloadManager.remove(audio.getDOWNLOADID());
                                                                    dbHelper.deleteRecord(audio);

                                                                    SQLiteDatabase DB = ExoPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                                                                    String query = "DELETE FROM " + DBHelper.DOWNLOAD_CONTENT_INFO+ " WHERE download_contnet_id = '"+enqueue+"'";
                                                                    DB.execSQL(query);

                                                                }


                                                                exoplayerdownloadhandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Progress.setProgress((int) 0);
                                                                        percentg.setVisibility(View.GONE);
                                                                        download.setVisibility(View.VISIBLE);
                                                                    }
                                                                });

                                                                Toast.makeText(getApplicationContext(), Util.getTextofLanguage(ExoPlayerActivity.this, Util.DOWNLOAD_CANCELLED, Util.DEFAULT_DOWNLOAD_CANCELLED), Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                                dlgAlert.create().show();

                                            }
                                        }
            );

            /*****Offline*****/


            isLiveStream = playerModel.isLiveStream();
            played_length = playerModel.getPlayPos() * 1000;
            Log.v("BKS", "exo video url===" + playerModel.getVideoUrl());
            if (!playerModel.getVideoUrl().trim().equals("")) {
                Log.v("BKS", "video match and enter the if loop and backcalled===");
                Log.v("BKS", "thirdpartyurl===" + playerModel.isThirdPartyPlayer());
                if (playerModel.isThirdPartyPlayer()) {
                    Log.v("BKS", "enter thirdparty condition==");


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
                } else {

                    backCalled();
                }

                //onBackPressed();
            }

            movieId = playerModel.getMovieUniqueId();
            Log.v("BISHAL", "MovieId==" + movieId);
            episodeId = playerModel.getEpisode_id();
            Log.v("BISHAL", "Url=" + playerModel.getVideoUrl());


            emVideoView = (EMVideoView) findViewById(R.id.emVideoView);
            subtitleText = (TextView) findViewById(R.id.offLine_subtitleText);
            subtitle_change_btn = (ImageView) findViewById(R.id.subtitle_change_btn);


            latest_center_play_pause = (ImageButton) findViewById(R.id.latest_center_play_pause);
            videoTitle = (TextView) findViewById(R.id.videoTitle);
            Typeface videoTitleface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
            videoTitle.setTypeface(videoTitleface);
            GenreTextView = (TextView) findViewById(R.id.GenreTextView);
            Typeface GenreTextViewface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
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
            Typeface watchTrailerButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
            videoCastCrewTitleTextView.setTypeface(watchTrailerButtonTypeface);

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
                SubTitlePath = playerModel.getSubTitlePath();
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

            if (playerModel.getResolutionFormat() != null && !isDrm) {
                ResolutionFormat = playerModel.getResolutionFormat();
            } else {
                ResolutionFormat.clear();

            }

            if (playerModel.getResolutionUrl() != null && !isDrm) {
                ResolutionUrl = playerModel.getResolutionUrl();
            } else {
                ResolutionUrl.clear();
            }

            if (ResolutionUrl.size() < 1)

            {
                Log.v("MUVI", "resolution image Invisible called");
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
                Log.v("MUVI", "subtitle_image button Invisible called");
            }

            //=============================== End Resolution Change ===================================//

            if (playerModel.getVideoTitle().trim() != null && !playerModel.getVideoTitle().trim().matches("")) {
                videoTitle.setText(playerModel.getVideoTitle().trim());
                videoTitle.setVisibility(View.VISIBLE);
            } else {
                videoTitle.setVisibility(View.GONE);
            }

            if (playerModel.getVideoGenre().trim() != null && !playerModel.getVideoGenre().trim().matches(""))

            {
                GenreTextView.setText(playerModel.getVideoGenre().trim());
                GenreTextView.setVisibility(View.VISIBLE);
            } else {
                GenreTextView.setVisibility(View.GONE);
            }


            if (playerModel.getVideoDuration().trim() != null && !playerModel.getVideoDuration().trim().matches(""))

            {
                videoDurationTextView.setText(playerModel.getVideoDuration().trim());
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
                                PreviousUsedDataByApp(false);
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

                                    PreviousUsedDataByApp(false);
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
             /*@MUVI
             * this part is check if url is for drm then plAY if part other wise else part
             * */
                if (isDrm) {
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


    private long DataUsedByDownloadContent() {
        try {

            SQLiteDatabase DB = ExoPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = DB.rawQuery("SELECT " + DBHelper.COLUMN_DOWNLOADID + " FROM " + DBHelper.TABLE_NAME + " ", null);
            int count = cursor.getCount();

            long Total = 0;

            if (count > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        DownloadManager downloadManager1 = (DownloadManager) ExoPlayerActivity.this.getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Query download_id_query = new DownloadManager.Query();
                        download_id_query.setFilterById(Long.parseLong(cursor.getString(0).trim())); //filter by id which you have receieved when reqesting download from download manager
                        Cursor id_cursor = downloadManager1.query(download_id_query);


                        if (id_cursor != null && id_cursor.getCount() > 0) {
                            if (id_cursor.moveToFirst()) {
                                int columnIndex = id_cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int status = id_cursor.getInt(columnIndex);

                                int sizeIndex = id_cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                                int downloadedIndex = id_cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                                long size = id_cursor.getInt(sizeIndex);
                                long downloaded = id_cursor.getInt(downloadedIndex);

                                Total = Total + downloaded / 1024;
                            }
                        }

                        Log.v("BIBHU11", "  TotalUsedData Download size============" + Total +"KB");

                    } while (cursor.moveToNext());
                }
                return Total;
            } else {
                return Total;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public void PreviousUsedDataByApp(boolean status) {
        try{

            long prev_data = 0;
            PackageManager pm = getPackageManager();
            List<PackageInfo> listPackages = pm.getInstalledPackages(0);
            for (PackageInfo pi : listPackages) {
                String appName = (String) pi.applicationInfo.loadLabel(pm);

                if (appName != null && appName.trim().equals(getApplicationName(getApplicationContext()))) {
                    int uid = pi.applicationInfo.uid;
                    prev_data = (TrafficStats.getUidRxBytes(uid) + TrafficStats.getUidTxBytes(uid)) / 1024;
                    PreviousUsedData = prev_data;
                    if(status)
                    {
                        PreviousUsedData = prev_data;
                        Log.v("BIBHU", "PreviousUsedDataByApp  true===========================" + (appName + " : " + PreviousUsedData + "KB"));

                    }
                    else
                    {
                        Log.v("BIBHU", "*************** false===========================prev_data= " + prev_data+" KB==========CurrentUsedData= "+CurrentUsedData+" KB");



                        PreviousUsedData =  ((prev_data - PreviousUsedData) - CurrentUsedData)+ PreviousUsedData;
                        Log.v("BIBHU", "PreviousUsedDataByApp false===========================" + (appName + " : " + PreviousUsedData + "KB"));

                    }

                    Log.v("BKS", "PreviousUsedDataByApp===========================" + (appName + " : " + PreviousUsedData + "KB"));
                }
            }

        }catch (Exception e){

        }
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
                    Log.v("MUVI", "PLAY responseStr" + responseStr);


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

                    Log.v("MUVI", "Exception of videoplayer" + e.toString());
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
                Log.v("MUVI", "Exception" + e);

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
            Log.v("MUVI", "onPreExecute");
            stoptimertask();
            Log.v("MUVI", "onPreExecute1");
        }
    }

    private class AsyncVideoBufferLogDetails extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("BKS","video bufferasynctask called");


            String urlRouteList = Util.rootUrl().trim() + Util.bufferLogUrl.trim();
            try {
                Log.v("BKS","video bufferasynctask try catch");
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", userIdStr);
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", movieId.trim());
                httppost.addHeader("episode_id", episodeId.trim());
                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoBufferLogId);
                httppost.addHeader("resolution", resolution.trim());
                httppost.addHeader("start_time", String.valueOf(playerPosition));
                httppost.addHeader("end_time", String.valueOf(playerPosition));
                httppost.addHeader("log_unique_id", videoBufferLogUniqueId);
                httppost.addHeader("location", Location);

                   Log.v("BKS","video buffer lof videourl"+playerModel.getVideoUrl());
                if (isDrm) {
                    Log.v("BKS","if called and 2 header attached ");

                    httppost.addHeader("video_type", "mped_dash");
                    httppost.addHeader("totalBandwidth", "" + CurrentUsedData);
                    Log.v("BKS","video buffer bandwidth"+CurrentUsedData);
                }

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("BKS", "Response of the bufferlog =" + responseStr);

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


   /* private class AsyncVideoBufferLogDetails extends AsyncTask<Void, Void, Void> {
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
    }*/


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
                if (!isDrm) {

                    AsyncUpdateVideoBufferLogDetails asyncUpdateVideoBufferLogDetails = new AsyncUpdateVideoBufferLogDetails();
                    asyncUpdateVideoBufferLogDetails.executeOnExecutor(threadPoolExecutor);
                }
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
        int seconds = milliseconds / 1000;
        return seconds;
    }

    @Override
    public void onOrientationChange(int orientation) {


        if (orientation == 90) {

            Player.player_description = false;
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
            Player.player_description = false;
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

            Player.player_description = true;

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

            Player.player_description = true;
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
                    URL myurl = new URL(Player.loadIPUrl);
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
            if (isDrm) {
                BufferBandWidth();
            }
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
                findViewById(R.id.progress_view).setVisibility(View.VISIBLE);

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
                findViewById(R.id.progress_view).setVisibility(View.GONE);
            }

        }
    };

    public void BufferBandWidth() {
        DataAsynTask dataAsynTask = new DataAsynTask();
        dataAsynTask.execute();
    }

    private class DataAsynTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try{
                long total = 0;
                PackageManager pm = getPackageManager();
                List<PackageInfo> listPackages = pm.getInstalledPackages(0);
                for (PackageInfo pi : listPackages) {
                    String appName = (String) pi.applicationInfo.loadLabel(pm);
                    if (appName != null && appName.trim().equals(getApplicationName(getApplicationContext()))) {
                        int uid = pi.applicationInfo.uid;
                        total = (TrafficStats.getUidRxBytes(uid) + TrafficStats.getUidTxBytes(uid)) / 1024;

                        CurrentUsedData = total - PreviousUsedData;
                        Log.v("BIBHU", "CurrentUsedData==================" + CurrentUsedData + " KB");
                        CurrentUsedData = CurrentUsedData - (DataUsedByDownloadContent() - PreviousUsedData_By_DownloadContent);

                    }
                }
            }catch (Exception e){

            }


            return null;
        }
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
        if (Util.getTextofLanguage(ExoPlayerActivity.this, Util.IS_STREAMING_RESTRICTION, Util.DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1") && Util.call_finish_at_onUserLeaveHint) {


            AsyncResumeVideoLogDetails_HomeClicked asyncResumeVideoLogDetails_homeClicked = new AsyncResumeVideoLogDetails_HomeClicked();
            asyncResumeVideoLogDetails_homeClicked.executeOnExecutor(threadPoolExecutor);
        }


        if (Player.call_finish_at_onUserLeaveHint) {

            Player.call_finish_at_onUserLeaveHint = true;

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
                PreviousUsedDataByApp(false);
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
            Log.v("MUVI","FHFHFHCALLED");
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

                Player.call_finish_at_onUserLeaveHint = false;

                if (data.getStringExtra("yes").equals("1002")) {

                    watchStatus = "halfplay";
                    playerPosition = playerModel.getPlayPos();
                    player_start_time = playerPosition;
                    PreviousUsedDataByApp(false);
                    emVideoView.start();
                    emVideoView.seekTo(played_length);
                    seekBar.setProgress(played_length);
                    updateProgressBar();

                } else {
                    PreviousUsedDataByApp(false);
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
                                Log.v("MUVI", "Exception of subtitle change process =" + e.toString());
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
                        findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
                        emVideoView.setVideoURI(Uri.parse(ResolutionUrl.get(Integer.parseInt(data.getStringExtra("position")))));

                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Player.hide_pause = false;

        if (MovableTimer != null)
            MovableTimer.cancel();
        //to destroy the registerrecever
        // TODO Auto-generated method stub

        try{
            if(SelectedUrl!=null)
                unregisterReceiver(SelectedUrl);
        }catch(Exception e)
        {

        }
        //super.onDestroy();
    }

    // Added Later By Bibhu For Subtitle Feature.

    public class SubtitleProcessingTask extends AsyncTask<Void, Void, Void> {


        String Subtitle_Path = "";

        public SubtitleProcessingTask(String path) {
//            Log.v("MUVI","SubTitlePath size ==="+SubTitlePath.size());
//             Subtitle_Path = Environment.getExternalStorageDirectory().toString()+"/"+"sub.vtt";
            Subtitle_Path = SubTitlePath.get((Integer.parseInt(path) - 1));
        }

        @Override
        protected void onPreExecute() {
//            subtitleText.setText("Loading subtitles..");
            super.onPreExecute();
            Log.v("MUVI", "SubTitlePath size at pre execute===" + SubTitlePath.size());
        }

        @Override
        protected Void doInBackground(Void... params) {
            // int count;
            try {

                Log.v("MUVI", "Subtitle_Path ========" + Subtitle_Path);

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
                Log.e("MUVI", "error in downloadinf subs");
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


    // Added For Offline Viewing//


    public void checkDownLoadStatusFromDownloadManager1(final ContactModel1 model) {


        if (model.getDOWNLOADID() != 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    downloading = true;
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

                                    SQLiteDatabase DB = ExoPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                                    String query1 = "UPDATE "+DBHelper.DOWNLOAD_CONTENT_INFO+" SET download_status = '1'" +
                                            " WHERE email = '"+emailIdStr+"' AND download_contnet_id = '"+model.getDOWNLOADID()+"'";
                                    DB.execSQL(query1);

                                } else if (status == DownloadManager.STATUS_FAILED) {
                                    // 1. process for download fail.
                                    model.setDSTATUS(0);

                                    SQLiteDatabase DB = ExoPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                                    String query1 = "UPDATE "+DBHelper.DOWNLOAD_CONTENT_INFO+" SET download_status = '0'" +
                                            " WHERE email = '"+emailIdStr+"' AND download_contnet_id = '"+model.getDOWNLOADID()+"'";
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
                                // At this point you have the progress as a percentage.
                                model.setProgress((int) progress);
                            }
                        }


                        runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {

                                download.setVisibility(View.GONE);
                                percentg.setVisibility(View.VISIBLE);
                                Progress.setProgress(0);

                                Progress.setProgress((int) model.getProgress());
                                percentg.setText(model.getProgress() + "%");

                                if (model.getProgress() == 100) {
                                    download_layout.setVisibility(View.GONE);
                                }

                            }
                        });
                        cursor.close();
                    }
                }
            }).start();
        }
    }


    public void Download_SubTitle(String Url) {
        new DownloadFileFromURL_Offline().execute(Url);
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
                mediaStorageDir1 = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList_Offline_WithoutDRM/", "");

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
            }

        }
    }




    /*****offline *****/


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        ProgressBarHandler pDialog;
        String responseStr;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(ExoPlayerActivity.this);
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
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                Calendar d = Calendar.getInstance();
                URL url = new URL(f_url[0]);
                Log.v("SUBHA", "ha ho" + url);
                String str = f_url[0];

                URLConnection conection = url.openConnection();
                conection.connect();

                lenghtOfFile = conection.getContentLength();
                lengthfile = lenghtOfFile / 1024 / 1024;
                //lengthfile = String.format("%.2f", lengthfile);
               // BigDecimal roundlength = new BigDecimal(lengthfile).setScale(1,BigDecimal.ROUND_HALF_UP);
                Log.v("SUBHA4", "" + lengthfile);
               /* lengthofdownloadsize=roundlength;
                lengthfile=roundlength.floatValue(roundlength);*/

                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                lengthfile= Float.valueOf(decimalFormat.format(lengthfile));
             /*   float size = (Float.parseFloat(""+execute.getEntity().getContentLength())/1024)/1024 ;
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                size = Float.valueOf(decimalFormat.format(size));*/



            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                }

                String lengh = String.valueOf(lengthfile);

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ExoPlayerActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setTitle(Util.getTextofLanguage(ExoPlayerActivity.this, Util.WANT_TO_DOWNLOAD, Util.DEFAULT_WANT_TO_DOWNLOAD));
                dlgAlert.setMessage(playerModel.getVideoTitle() + " " + "(" + lengh + "MB)");
                dlgAlert.setPositiveButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.DOWNLOAD_BUTTON_TITLE, Util.DEFAULT_DOWNLOAD_BUTTON_TITLE), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.DOWNLOAD_BUTTON_TITLE, Util.DEFAULT_DOWNLOAD_BUTTON_TITLE),
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
                dlgAlert.setNegativeButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.CANCEL_BUTTON, Util.DEFAULT_CANCEL_BUTTON), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton(Util.getTextofLanguage(ExoPlayerActivity.this, Util.CANCEL_BUTTON, Util.DEFAULT_CANCEL_BUTTON),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();

            } catch (IllegalArgumentException ex) {
                Toast.makeText(ExoPlayerActivity.this, Util.getTextofLanguage(ExoPlayerActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            downloadFile(true);
        }
    }


    private void downloadFile(boolean singlefile) {


        if (isDrm) {
          offlinedrmplay(singlefile);
        }
        else {
            offlinenondrmplay(singlefile);
        }

        audio = dbHelper.getContact(playerModel.getStreamUniqueId() + emailIdStr);
        if (audio != null) {
            if (audio.getUSERNAME().trim().equals(emailIdStr.trim())) {
                checkDownLoadStatusFromDownloadManager1(audio);
            }
        }

        SQLiteDatabase DB = ExoPlayerActivity.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
        String query1 = "INSERT INTO " + DBHelper.DOWNLOAD_CONTENT_INFO+ "(download_contnet_id,log_id,authtoken,email," +
                "ipaddress,movie_id,episode_id,device_type,download_status,server_sending_final_status) VALUES" +
                "('"+enqueue+"','0','"+Util.authTokenStr.trim()+"','"+emailIdStr.trim()+"','"+ipAddressStr+"'," +
                "'"+playerModel.getMovieUniqueId().trim()+"','"+ playerModel.getStreamUniqueId().trim()+"'," +
                "'"+2+"','2','0')";

        DB.execSQL(query1);

        //---------------------- End ---------------------------------//

        // Send BroadCast to start sending Download content bandwidth .

        Intent intent = new Intent("BnadwidthLog");
        sendBroadcast(intent);

        //---------------------- End ---------------------------------//

    }

    private void offlinenondrmplay(boolean singlefile) {
        if (singlefile) {
            selected_download_format = 0;
            request = new DownloadManager.Request(Uri.parse(playerModel.getVideoUrl()));
        } else {
            request = new DownloadManager.Request(Uri.parse(ResolutionUrl.get(selected_download_format + 1)));
            selected_download_format = 0;
        }
        request.setTitle(playerModel.getVideoTitle());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Get download file name
        fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(playerModel.getVideoUrl());

        timestamp = System.currentTimeMillis() + ".exo";
        //Save file to destination folder
        request.setDestinationInExternalPublicDir("Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM", timestamp);
        enqueue = downloadManager.enqueue(request);
        download.setVisibility(View.GONE);
        percentg.setVisibility(View.VISIBLE);
        Progress.setProgress(0);

        ContactModel1 contactModel1 = new ContactModel1();
        contactModel1.setMUVIID(playerModel.getVideoTitle());
        contactModel1.setDOWNLOADID((int) enqueue);
        contactModel1.setProgress(0);
        contactModel1.setUSERNAME(emailIdStr);
        contactModel1.setUniqueId(playerModel.getStreamUniqueId() + emailIdStr);
        contactModel1.setDSTATUS(2);

        contactModel1.setPoster(playerModel.getPosterImageId().trim());
        contactModel1.setToken(fileExtenstion);
        contactModel1.setPath(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM/" + timestamp);
        contactModel1.setContentid(String.valueOf(playerModel.getContentTypesId()));
        contactModel1.setGenere(playerModel.getVideoGenre().trim());
        contactModel1.setMuviid(playerModel.getStreamUniqueId().trim());
        contactModel1.setDuration(playerModel.getVideoDuration().trim());
        dbHelper.insertRecord(contactModel1);
    }

    private void offlinedrmplay(boolean singlefile) {

        if (singlefile) {
            selected_download_format = 0;
            request = new DownloadManager.Request(Uri.parse(mlvfile));
        } else {
            request = new DownloadManager.Request(Uri.parse(List_Of_Resolution_Url_Used_For_Download.get(selected_download_format)));
            selected_download_format = 0;
        }
        request.setTitle(playerModel.getVideoTitle());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Get download file name
        fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(mlvfile);
        fname = URLUtil.guessFileName(mlvfile, null, fileExtenstion);
        timestamp = System.currentTimeMillis() + ".mlv";
        request.setDestinationInExternalPublicDir("Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHDRM", timestamp);
        enqueue = downloadManager.enqueue(request);


        download.setVisibility(View.GONE);
        percentg.setVisibility(View.VISIBLE);
        Progress.setProgress(0);

        ContactModel1 contactModel1 = new ContactModel1();
        contactModel1.setMUVIID(playerModel.getVideoTitle());
        contactModel1.setDOWNLOADID((int) enqueue);
        contactModel1.setProgress(0);
        contactModel1.setUSERNAME(emailIdStr);
        contactModel1.setUniqueId(playerModel.getStreamUniqueId() + emailIdStr);
        contactModel1.setDSTATUS(2);

        contactModel1.setPoster(playerModel.getPosterImageId().trim());
        contactModel1.setToken(licensetoken);
        contactModel1.setPath(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/WITHDRM/" + timestamp);
        contactModel1.setContentid(String.valueOf(playerModel.getContentTypesId()));
        contactModel1.setGenere(playerModel.getVideoGenre().trim());
        contactModel1.setMuviid(playerModel.getStreamUniqueId().trim());
        contactModel1.setDuration(playerModel.getVideoDuration().trim());
        dbHelper.insertRecord(contactModel1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                downloadFile(true);
            } else {
                Toast.makeText(this, Util.getTextofLanguage(ExoPlayerActivity.this, Util.DOWNLOAD_INTERRUPTED, Util.DEFAULT_DOWNLOAD_INTERRUPTED), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncResumeVideoLogDetails_HomeClicked extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog pDialog;
        String responseStr;
        int statusCode = 0;
        String watchSt = "halfplay";

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.videoLogUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
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


                if (Util.getTextofLanguage(ExoPlayerActivity.this, Util.IS_STREAMING_RESTRICTION, Util.DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {
                    Log.v("BIBHU", "sending restrict_stream_id============" + restrict_stream_id);

                    httppost.addHeader("is_streaming_restriction", "1");
                    httppost.addHeader("restrict_stream_id", restrict_stream_id);
                    httppost.addHeader("is_active_stream_closed", "1");
                }


                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", videoLogId);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("BIBHU", "responseStr of responseStr============" + responseStr);


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
                        restrict_stream_id = myJson.optString("restrict_stream_id");
                        Log.v("BIBHU", "responseStr of restrict_stream_id============" + restrict_stream_id);
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
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private class AsynWithdrm extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        int responseCode;
        // String loginHistoryIdStr = loginPref.getString("PREFS_LOGIN_HISTORYID_KEY", null);
        String responseStr;

        @Override
        protected Void doInBackground(Void... params) {


            String urlRouteList = Util.rootUrl().trim() + Util.morlineBB.trim();
            //String urlRouteList ="https://sonydadc.muvi.com/rest/getMarlinBBOffline";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("stream_unique_id", playerModel.getStreamUniqueId());


                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


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
                        Log.v("SUBHA", "" + responseCode);
                        mainJson = myJson.getJSONObject("data");

                        if ((mainJson.has("file")) && mainJson.getString("file").trim() != null && !mainJson.getString("file").trim().isEmpty() && !mainJson.getString("file").trim().equals("null") && !mainJson.getString("file").trim().matches("")) {
                            mlvfile = mainJson.getString("file");

                            Log.v("SUBHA", mlvfile);
                        } else {
                            mlvfile = Util.getTextofLanguage(ExoPlayerActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
                        }

                        if ((mainJson.has("token")) && mainJson.getString("token").trim() != null && !mainJson.getString("token").trim().isEmpty() && !mainJson.getString("token").trim().equals("null") && !mainJson.getString("token").trim().matches("")) {
                            token = mainJson.getString("token");
                            Log.v("SUBHA", "token" + token);

                        } else {
                            token = Util.getTextofLanguage(ExoPlayerActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
                        }



                        // This parsing is responsible for multiple resolution


                        if ((mainJson.has("multiple_resolution")) && mainJson.getString("multiple_resolution").trim() != null && !mainJson.getString("multiple_resolution").trim().isEmpty() && !mainJson.getString("multiple_resolution").trim().equals("null") && !mainJson.getString("multiple_resolution").trim().matches("")) {
                            JSONArray jsonArray = mainJson.optJSONArray("multiple_resolution");

                            for(int i=0 ;i<jsonArray.length();i++)
                            {

                                if(jsonArray.getJSONObject(i).optString("resolution").trim().contains("BEST"))
                                    List_Of_Resolution_Format.add(jsonArray.getJSONObject(i).optString("resolution"));
                                else
                                    List_Of_Resolution_Format.add(jsonArray.getJSONObject(i).optString("resolution")+"p");

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


            }catch (Exception e) {
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

            licensetoken = mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis()+".xml";
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
                Log.d("sanji", playerModel.getStreamUniqueId());


                //This portion is changed later because of multiple download option.

                if(List_Of_Resolution_Url.size()>0)
                {
                    pDialog_for_gettig_filesize = new ProgressBarHandler(ExoPlayerActivity.this);
                    pDialog_for_gettig_filesize.show();

                    new DetectDownloadingFileSize().execute();
                }
                else
                {
                    /*@BISHAL
                    * for drm player the length size is calculated by mlvfile but
                    * in case of non drm it calculate by videourl
                    * */
                    if (isDrm){
                        new DownloadFileFromURL().execute(mlvfile);
                    }else {
                        new DownloadFileFromURL().execute(playerModel.getVideoUrl());
                    }

                }


            } catch (IllegalArgumentException ex) {
                Toast.makeText(ExoPlayerActivity.this, Util.getTextofLanguage(ExoPlayerActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }


        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(ExoPlayerActivity.this);
            pDialog.show();


        }
    }
    // This part is only applicable for multiple download optin feature

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

                float size = (Float.parseFloat(""+execute.getEntity().getContentLength())/1024)/1024 ;
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                size = Float.valueOf(decimalFormat.format(size));
                List_Of_FileSize.add("("+size+" MB)");



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
            if(List_Of_Resolution_Url.size()>0)
            {
                new DetectDownloadingFileSize().execute();
            }
            else
            {
                try {
                    if (pDialog_for_gettig_filesize != null && pDialog_for_gettig_filesize.isShowing()) {
                        pDialog_for_gettig_filesize.hide();
                    }
                } catch (IllegalArgumentException ex) {  }

                // Show PopUp for Multiple Options for Download .
                ShowDownloadOptionPopUp();
            }

        }
    }
    public void ShowDownloadOptionPopUp()
    {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExoPlayerActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) ExoPlayerActivity.this.getSystemService(ExoPlayerActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.activity_download_popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        TextView title_text = (TextView) convertView.findViewById(R.id.title_text);
        ListView resolution_list = (ListView) convertView.findViewById(R.id.resolution_list);
        Button save = (Button) convertView.findViewById(R.id.save);
        Button cancel = (Button) convertView.findViewById(R.id.cancel);

        save.setText(Util.getTextofLanguage(ExoPlayerActivity.this,Util.SAVE,Util.DEFAULT_SAVE));
        cancel.setText(Util.getTextofLanguage(ExoPlayerActivity.this,Util.CANCEL_BUTTON,Util.DEFAULT_CANCEL_BUTTON));
        title_text.setText(Util.getTextofLanguage(ExoPlayerActivity.this,Util.SAVE_OFFLINE_VIDEO,Util.DEFAULT_SAVE_OFFLINE_VIDEO));

        DownloadOptionAdapter adapter = new DownloadOptionAdapter(ExoPlayerActivity.this,List_Of_FileSize,List_Of_Resolution_Format);
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
    private BroadcastReceiver SelectedUrl = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String position = intent.getStringExtra("position");
            Log.v("BIBHU1", "Got position: " + position);
            selected_download_format = Integer.parseInt(position);

        }
    };

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

}
