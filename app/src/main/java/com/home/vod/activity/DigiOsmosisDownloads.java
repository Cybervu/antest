package com.home.vod.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.WebImage;
import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.R;
import com.home.vod.SearchIntentHandler;
import com.home.vod.adapter.DigiOsmosisDownloadAdapter;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.adapter.MyDownloadAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ProgressBarHandler;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import player.activity.ResumePopupActivity;
import player.model.ContactModel1;
import player.utils.DBHelper;
import player.utils.Util;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.util.Constant.authTokenStr;
import static java.lang.Thread.sleep;

public class DigiOsmosisDownloads extends AppCompatActivity implements GetLanguageListAsynTask.GetLanguageListListener ,LogoutAsynctask.LogoutListener, GetTranslateLanguageAsync.GetTranslateLanguageInfoListener {
    RelativeLayout logo_image;
TextView titleTextView;
    LinearLayout titleLayout;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;
    LanguageCustomAdapter languageCustomAdapter;
    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
    String download_id_from_watch_access_table = "";
    //public static ProgressDialog  pDialog;
    Context context;
    GridView gridView;
    TextView noDataTextView;
    RelativeLayout nodata;
    DigiOsmosisDownloadAdapter adapter;
    SharedPreferences pref;
    String emailIdStr = "";
    String user_Id = "";
    String StreamId = "";
    DBHelper dbHelper;
    static String path,filename,_filename,token,title,poster,genre,duration,rdate,movieid,user,uniqid;
    ArrayList<ContactModel1> download;
    ProgressBarHandler pDialog;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    String email, id;
    ArrayList<String> Chromecast_Subtitle_Url = new ArrayList<String>();
    ArrayList<String> Chromecast_Subtitle_Language_Name = new ArrayList<String>();
    ArrayList<String> Chromecast_Subtitle_Code = new ArrayList<String>();

    AlertDialog alert;
    int Played_Length = 0;
    String watch_status = "start";

    String PlayedLength = "0";
    String ipAddressStr = "";
    String MpdVideoUrl = "";
    String licenseUrl = "";
    String SubtitleUrl = "";
    String SubtitleLanguage = "";
    String SubtitleCode = "";

    LanguagePreference languagePreference;
    PreferenceManager preferenceManager;

    /*chromecast-------------------------------------*/
    View view;

    private static final int MAX_LINES = 3;


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

    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;


    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =new MySessionManagerListener();
    private CastSession mCastSession;
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

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    int Position = 0;
    public static ProgressBarHandler progressBarHandler;

    AsynLoadVideoUrls asynLoadVideoUrls;
    AsynGetIpAddress asynGetIpAddress;
    MediaInfo mediaInfo;
    /*chromecast-------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydownload);
        dbHelper=new DBHelper(DigiOsmosisDownloads.this);

        languagePreference = LanguagePreference.getLanguagePreference(this);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        dbHelper.getWritableDatabase();
        registerReceiver(UpadateDownloadList, new IntentFilter("NewVodeoAvailable"));

        /*chromecast-------------------------------------*/

        mAquery = new AQuery(this);

        // setupControlsCallbacks();
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        boolean shouldStartPlayback = false;
        int startPosition = 0;



        if (shouldStartPlayback) {
            // this will be the case only if we are coming from the
            // CastControllerActivity by disconnecting from a device
            mPlaybackState = PlaybackState.PLAYING;
            updatePlaybackLocation(PlaybackLocation.LOCAL);
            updatePlayButton(mPlaybackState);
            if (startPosition > 0) {
                // mVideoView.seekTo(startPosition);
            }

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
/***************chromecast**********************/





        Toolbar mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        logo_image = (RelativeLayout) findViewById(R.id.logo_image);
        logo_image.bringToFront();
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        titleLayout = (LinearLayout) findViewById(R.id.line_layout);
        titleTextView = (TextView) findViewById(R.id.categoryTitle);
        episodeListOptionMenuHandler=new EpisodeListOptionMenuHandler(this);
        titleTextView.setText(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
        //  mActionBarToolbar.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD,DEFAULT_MY_DOWNLOAD));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();

                finish();
            }
        });

        gridView= (GridView)findViewById(R.id.gridView);
        nodata= (RelativeLayout) findViewById(R.id.noData);
        noDataTextView= (TextView) findViewById(R.id.noDataTextView);
        //  islogin = preferenceManager.getLoginFeatureFromPref();
        if (preferenceManager!=null){
            emailIdStr= preferenceManager.getEmailIdFromPref();
            user_Id= preferenceManager.getUseridFromPref();
        }else {
            emailIdStr = "";
            user_Id = "";
        }

        nodata= (RelativeLayout) findViewById(R.id.noData);
        noDataTextView= (TextView) findViewById(R.id.noDataTextView);

        download=dbHelper.getContactt(emailIdStr,1);
        if(download.size()>0) {


            adapter = new DigiOsmosisDownloadAdapter(DigiOsmosisDownloads.this, R.layout.custom_offlist, download);
            gridView.setAdapter(adapter);
        }else {


            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT,DEFAULT_NO_CONTENT));
        }



        if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {

            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_large_vertical) : (int) getResources().getInteger(R.integer.configuration_large_vertical));


        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_normal_vertical) : (int) getResources().getInteger(R.integer.configuration_normal_vertical));


        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_small_vertical) : (int) getResources().getInteger(R.integer.configuration_small_vertical));


        } else {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_xlarge_vertical) : (int) getResources().getInteger(R.integer.configuration_xlarge_vertical));

        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(DigiOsmosisDownloads.this, " item called " + position, Toast.LENGTH_SHORT).show();
                Position = position;
                SubtitleUrl = "";

                final String data[] = download.get(position).getMUVIID().trim().split("@@@");
                StreamId = data[1];

                Log.v("SUBHA1"," item clicked === ");

                if(Util.checkNetwork(DigiOsmosisDownloads.this))
                {
                    asynGetIpAddress = new AsynGetIpAddress();
                    asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

                }else
                {
                    MoveToOfflinePlayer();
                }
            }
        });

    }

    public void visible(){

        if(download.size()>0) {
            adapter = new DigiOsmosisDownloadAdapter(DigiOsmosisDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
            gridView.setAdapter(adapter);

        }else {

            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(Util.getTextofLanguage(DigiOsmosisDownloads.this,Util.NO_CONTENT,Util.DEFAULT_NO_CONTENT));
        }
    }

    public void ShowRestrictionMsg(String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisDownloads.this, R.style.MyAlertDialogStyle);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.SORRY, Util.DEFAULT_SORRY));
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    protected void onPause() {
        super.onPause();
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(UpadateDownloadList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        episodeListOptionMenuHandler.createOptionMenu(menu,preferenceManager,languagePreference);

     /*   MenuItem favorite_menu;
        favorite_menu = menu.findItem(R.id.menu_item_favorite);
        favorite_menu.setVisible(false);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new SearchIntentHandler(DigiOsmosisDownloads.this).handleSearchIntent();
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(DigiOsmosisDownloads.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(DigiOsmosisDownloads.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);

                if (com.home.vod.util.Util.languageModel!=null && com.home.vod.util.Util.languageModel.size() > 0){


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
            case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(DigiOsmosisDownloads.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(DigiOsmosisDownloads.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisDownloads.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.YES, Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, DigiOsmosisDownloads.this, DigiOsmosisDownloads.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DigiOsmosisDownloads.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.APP_SELECT_LANGUAGE, Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(Util.getTextofLanguage(DigiOsmosisDownloads.this, Util.BUTTON_APPLY, Util.DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        //  languageCustomAdapter = new LanguageCustomAdapter(DigiOsmosisDownloads.this, Util.languageModel);
        // Util.languageModel.get(0).setSelected(true);
      /*  if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
            prevPosition = i;
            Util.languageModel.get(i).setSelected(true);

        }
        Util.languageModel.get(0).setSelected(true);*/

        recyclerView.setAdapter(languageCustomAdapter);



    /*    for (int i = 0 ; i < Util.languageModel.size() - 1 ; i ++){
                if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
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
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(DigiOsmosisDownloads.this, recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                Util.setLanguageSharedPrefernce(DigiOsmosisDownloads.this, Util.SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
                languageCustomAdapter.notifyDataSetChanged();

                // Default_Language = Util.languageModel.get(position).getLanguageId();
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



                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    languageListInputModel.setLangCode(Default_Language);

                    GetTranslateLanguageAsync getTranslateLanguageAsync = new GetTranslateLanguageAsync(languageListInputModel,DigiOsmosisDownloads.this,DigiOsmosisDownloads.this);
                    getTranslateLanguageAsync.executeOnExecutor(threadPoolExecutor);

                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.setLanguageSharedPrefernce(DigiOsmosisDownloads.this, Util.SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(DigiOsmosisDownloads.this);
        progressBarHandler.show();
    }


    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {
        {

            if (progressBarHandler.isShowing()) {
                progressBarHandler.hide();
                progressBarHandler = null;

            }
            if (status > 0 && status == 200) {
                ShowLanguagePopup();
            }
        }
    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(DigiOsmosisDownloads.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(DigiOsmosisDownloads.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {

        if (status == null) {
            Toast.makeText(DigiOsmosisDownloads.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(DigiOsmosisDownloads.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION,DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(DigiOsmosisDownloads.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(DigiOsmosisDownloads.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(DigiOsmosisDownloads.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(DigiOsmosisDownloads.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS,DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(DigiOsmosisDownloads.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }

    }

    private BroadcastReceiver UpadateDownloadList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("BIBHU1","Onreceive called");

            download=dbHelper.getContactt(emailIdStr,1);
            if(download.size()>0) {
                adapter = new DigiOsmosisDownloadAdapter(DigiOsmosisDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
                gridView.setAdapter(adapter);
                nodata.setVisibility(View.GONE);
            }

        }
    };


    /*****************chromecvast*-------------------------------------*/



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

                    if (mPlaybackState ==PlaybackState.PLAYING) {
                       /* mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);*/
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
        switch (state) {
            case PLAYING:
                break;
            case IDLE:
                if (mLocation == PlaybackLocation.LOCAL){
                }else{
                }
                break;
            case PAUSED:
                break;
            case BUFFERING:
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

            } else {
            }
        } else {
        }
    }


    private void togglePlayback() {
        Log.v("BIBHU4", "restrict_stream_id============4");
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:
                        Log.v("BIBHU4", "restrict_stream_id============44");
                        break;
                    case REMOTE:
                        Log.v("BIBHU4", "restrict_stream_id=====2233=======111");
//                        loadRemoteMedia(0, true);
                        loadRemoteMedia(Played_Length, true);
                        break;
                    default:
                        Log.v("BIBHU4", "restrict_stream_id============444");
                        break;
                }
                break;

            case PLAYING:
                Log.v("BIBHU4", "restrict_stream_id============43344");
                mPlaybackState = PlaybackState.PAUSED;
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
//                        loadRemoteMedia(0, true);
                        loadRemoteMedia(Played_Length, true);
                        Log.v("BIBHU4", "restrict_stream_id============44554");
                        break;
                    case REMOTE:
                        Log.v("BIBHU4", "restrict_stream_id============344433");
                        if (mCastSession != null && mCastSession.isConnected()) {
//                            loadRemoteMedia(0, true);
                            loadRemoteMedia(Played_Length, true);
                            Log.v("BIBHU4", "restrict_stream_id=====32223=======111");
                        }
                        else  {
                            Log.v("BIBHU4", "restrict_stream_id======33======344433");
                        }
                        break;
                    default:
                        Log.v("BIBHU4", "restrict_stream_id======33=44=====344433");
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
            Log.v("BIBHU4", "restrict_stream_id==nul===33=======111");
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            Log.v("BIBHU4", "restrict_stream_id====ee=33=======111");
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {

            @Override
            public void onStatusUpdated() {

                Intent intent = new Intent(DigiOsmosisDownloads.this, ExpandedControlsActivity.class);
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
        remoteMediaClient.setActiveMediaTracks(new long[1]).setResultCallback(new ResultCallback<RemoteMediaClient.MediaChannelResult>() {
            @Override
            public void onResult(@NonNull RemoteMediaClient.MediaChannelResult mediaChannelResult) {
                if (!mediaChannelResult.getStatus().isSuccess()) {
                    Log.v("SUBHA", "Failed with status code:" +
                            mediaChannelResult.getStatus().getStatusCode());
                }
            }
        });
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
        Log.v("BIBHU4", "restrict_stream_id====ee=33==posi=====111");

    }

    /***************chromecast**********************/

    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
        String responseStr;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                // Execute HTTP Post Request
                try {
                    URL myurl = new URL(Util.loadIPUrl);
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

            if(progressBarHandler!=null && progressBarHandler.isShowing())
            {
                progressBarHandler.hide();
            }

            // Calling GetVideo Details API to get Latest Url Details.

            asynLoadVideoUrls = new AsynLoadVideoUrls();
            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
        }

        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(DigiOsmosisDownloads.this);
            progressBarHandler.show();
        }
    }


    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int statusCode;

        // This is added because of change in simultaneous login feature
        String message;
        boolean play_video = true;

        // ================================== End ====================================//
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.BASE_URl.trim()+Util.loadVideoUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken",authTokenStr.trim());
                httppost.addHeader("content_uniq_id", download.get(Position).getMuviid());
                httppost.addHeader("stream_uniq_id", StreamId);
                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
                httppost.addHeader("user_id",preferenceManager.getUseridFromPref());
                httppost.addHeader("lang_code",Util.getTextofLanguage(DigiOsmosisDownloads.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));



                Log.v("SUBHA","authToken = "+authTokenStr.trim());
                Log.v("SUBHA","content_uniq_id = "+download.get(Position).getMuviid());
                Log.v("SUBHA","stream_uniq_id = "+StreamId);
                Log.v("SUBHA","user_id = "+preferenceManager.getUseridFromPref());

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (Exception e){
                    responseStr = "0";
                }

                Log.v("SUBHA","response = "+responseStr);
                JSONObject myJson =null;
                JSONArray SubtitleJosnArray = null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                    // ================================== End ====================================//
                }

                if (statusCode >= 0) {
                    if (statusCode == 200) {
                        if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() &&
                                !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                            MpdVideoUrl = myJson.getString("videoUrl").trim();

                            if(MpdVideoUrl.equals(""))
                                responseStr = "0";

                            if ((myJson.has("licenseUrl")) && myJson.getString("licenseUrl").trim() != null && !myJson.getString("licenseUrl").trim().isEmpty() && !myJson.getString("licenseUrl").trim().equals("null") && !myJson.getString("licenseUrl").trim().matches("")) {
                                licenseUrl = myJson.optString("licenseUrl");
                            }

                            SQLiteDatabase DB = DigiOsmosisDownloads.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

                            String Qry1 = "UPDATE " + DBHelper.RESUME_WATCH + " SET LicenceUrl = '"+licenseUrl+"' , Flag = '1' ,LatestMpdUrl = '"+MpdVideoUrl+"'  WHERE UniqueId = '" + download.get(Position).getUniqueId() + "'";
                            DB.execSQL(Qry1);

                            if ((myJson.has("played_length")) && myJson.getString("played_length").trim() != null && !myJson.getString("played_length").trim().isEmpty() && !myJson.getString("played_length").trim().equals("null") && !myJson.getString("played_length").trim().matches("")) {
                                Played_Length = Util.isDouble(myJson.getString("played_length"));
                                Played_Length = Played_Length * 1000;
                            }

                        }
                        else
                        {
                            responseStr = "0";
                        }

                        if(SubtitleJosnArray!=null)
                        {
                            if(SubtitleJosnArray.length()>0)
                            {

                                Chromecast_Subtitle_Url.clear();
                                Chromecast_Subtitle_Language_Name.clear();
                                Chromecast_Subtitle_Code.clear();



                                for(int i=0;i<SubtitleJosnArray.length();i++)
                                {
                                    SubtitleUrl = SubtitleJosnArray.getJSONObject(0).optString("url").trim();
                                    SubtitleLanguage = SubtitleJosnArray.getJSONObject(0).optString("language").trim();
                                    SubtitleCode = SubtitleJosnArray.getJSONObject(0).optString("code").trim();

                                    Log.v("BIBHU","Sutitle name = "+SubtitleJosnArray.getJSONObject(i).optString("language").trim());
                                    Log.v("BIBHU","Sutitle path = "+SubtitleJosnArray.getJSONObject(i).optString("url").trim());
                                    Log.v("BIBHU","Sutitle code = "+SubtitleJosnArray.getJSONObject(i).optString("code").trim());

                                    Chromecast_Subtitle_Url.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
                                    Chromecast_Subtitle_Language_Name.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
                                    Chromecast_Subtitle_Code.add(SubtitleJosnArray.getJSONObject(i).optString("code").trim());

                                }
                            }
                        }
                        // ================================== End ====================================//
                    }

                }
                else {
                    responseStr = "0";
                }
            } catch (Exception e1) {
                Log.v("BIBHU", "Exception e =" + e1.toString());
                responseStr = "0";
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
            }

            // ================================== End ====================================//
            if (responseStr == null) {
                responseStr = "0";
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisDownloads.this,R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(DigiOsmosisDownloads.this,Util.NO_VIDEO_AVAILABLE,Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(DigiOsmosisDownloads.this,Util.SORRY,Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(DigiOsmosisDownloads.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(DigiOsmosisDownloads.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else{

                if (mCastSession != null && mCastSession.isConnected()) {

                    if(!CheckAccessPeriodOfDpwnloadContent())
                    {
                        return;
                    }

                    if((Played_Length)>0)
                    {
                        Intent resumeIntent = new Intent(DigiOsmosisDownloads.this, ResumePopupActivity.class);
                        startActivityForResult(resumeIntent, 1001);
                    }else
                    {
                        Played_Length = 0;
                        watch_status = "start";

                        PlayThroughChromeCast();
                    }

                }
                else
                {
                    MoveToOfflinePlayer();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(DigiOsmosisDownloads.this);
            pDialog.show();
        }
    }


    public void MoveToOfflinePlayer()
    {
        SubTitleName.clear();
        SubTitlePath.clear();

        SQLiteDatabase DB = DigiOsmosisDownloads.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = DB.rawQuery("SELECT LANGUAGE,PATH FROM "+DBHelper.TABLE_NAME_SUBTITLE_LUIMERE+" WHERE UID = '"+download.get(Position).getUniqueId()+"'", null);
        int count = cursor.getCount();

        if (count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    SubTitleName.add(cursor.getString(0).trim());
                    SubTitlePath.add(cursor.getString(1).trim());


                    Log.v("SUBHA1","SubTitleName============"+cursor.getString(0).trim());
                    Log.v("SUBHA1","SubTitlePath============"+cursor.getString(1).trim());

                } while (cursor.moveToNext());
            }
        }


        if(!CheckAccessPeriodOfDpwnloadContent())
        {
            return;
        }

        //This is applicable for resume watch feature on downloaed content

        Cursor cursor11 = DB.rawQuery("SELECT * FROM "+DBHelper.RESUME_WATCH+" WHERE UniqueId = '"+download.get(Position).getUniqueId()+"'", null);
        int count11 = cursor11.getCount();

        if (count11 > 0) {
            if (cursor11.moveToFirst()) {
                do {
                    PlayedLength = cursor11.getString(1).trim();

                    Log.v("BIBHU3","PlayedLength============"+PlayedLength);

                } while (cursor11.moveToNext());
            }
        }

        //==========================================END======================================//



        pDialog = new ProgressBarHandler(DigiOsmosisDownloads.this);
        pDialog.show();

        new Thread(new Runnable(){
            public void run(){

                final String data[] = download.get(Position).getMUVIID().trim().split("@@@");
                final String titles=data[0];

                final String pathh=download.get(Position).getPath();
                final String gen=download.get(Position).getGenere();
                final String tok=download.get(Position).getToken();
                final String contentid=download.get(Position).getContentid();
                final String muviid=download.get(Position).getMuviid();
                final String poster=download.get(Position).getPoster();
                final String story = download.get(Position).getStory();

                Log.v("SUBHASUBHREE"," story ==== "+ download.get(Position).getStory());
                Log.v("SUBHASUBHREE"," story ==== 1111"+story);

                final String vidduration=download.get(Position).getDuration();
                final String filename=pathh.substring(pathh.lastIndexOf("/") + 1);

                try {
                    sleep(1200);

                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {

                            Intent in=new Intent(DigiOsmosisDownloads.this,MarlinBroadbandExample.class);
                            Log.v("SUBHA", "PATH" + pathh);

                            in.putExtra("rootUrl", APIUrlConstant.BASE_URl);
                            in.putExtra("authToken", authTokenStr);
                            in.putExtra("SubTitleName", SubTitleName);
                            in.putExtra("SubTitlePath", SubTitlePath);

                            in.putExtra("FILE", pathh);
                            in.putExtra("Title", titles);
                            in.putExtra("TOK", tok);
                            in.putExtra("poster", poster);
                            in.putExtra("contid", contentid);
                            in.putExtra("gen", gen);
                            in.putExtra("muvid", muviid);
                            in.putExtra("vid", vidduration);
                            in.putExtra("story", story);
                            in.putExtra("FNAME", filename);
                            in.putExtra("download_id_from_watch_access_table", download_id_from_watch_access_table);
                            in.putExtra("PlayedLength", PlayedLength);
                            in.putExtra("UniqueId",""+download.get(Position).getUniqueId());
                            in.putExtra("streamId",data[1]);
//                            in.putExtra("download_content_type",""+download.get(Position).getDownloadContentType());
                            in.putExtra("user_id",user_Id);
                            in.putExtra("email",emailIdStr);

                            in.putExtra("Chromecast_Subtitle_Url",Chromecast_Subtitle_Url);
                            in.putExtra("Chromecast_Subtitle_Language_Name",Chromecast_Subtitle_Language_Name);
                            in.putExtra("Chromecast_Subtitle_Code",Chromecast_Subtitle_Code);

                            Log.v("BIBHU1","PlayedLength="+PlayedLength);

                            //
                            startActivity(in);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    if (pDialog != null && pDialog.isShowing()) {
                                        pDialog.hide();
                                        pDialog = null;
                                    }
                                }
                            });
                        }
                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public boolean CheckAccessPeriodOfDpwnloadContent()
    {

        // following block is responsible for restriction on download content .....

        SQLiteDatabase DB = DigiOsmosisDownloads.this.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

        long server_current_time = 0;
        long watch_period = -1;
        long access_period = -1;
        long initial_played_time = 0;
        long updated_server_current_time = 0;

        Cursor cursor1 = DB.rawQuery("SELECT server_current_time , watch_period , access_period , initial_played_time , updated_server_current_time,download_id FROM "+DBHelper.WATCH_ACCESS_INFO+" WHERE download_id = '"+download.get(Position).getDOWNLOADID()+"'", null);
        int count1 = cursor1.getCount();

        if (count1 > 0) {
            if (cursor1.moveToFirst()) {
                do {
                    server_current_time = cursor1.getLong(0);
                    watch_period = cursor1.getLong(1);
                    access_period = cursor1.getLong(2);
                    initial_played_time = cursor1.getLong(3);
                    updated_server_current_time = cursor1.getLong(4);
                    download_id_from_watch_access_table = cursor1.getString(5);


                    Log.v("BIBHU3","server_current_time============"+server_current_time);
                    Log.v("BIBHU3","watch_period============"+watch_period);
                    Log.v("BIBHU3","access_period============"+access_period);
                    Log.v("BIBHU3","initial_played_time============"+initial_played_time);
                    Log.v("BIBHU3","updated_server_current_time============"+updated_server_current_time);
                    Log.v("BIBHU3","download_id_from_watch_access_table============"+download_id_from_watch_access_table);

                } while (cursor1.moveToNext());
            }
        }else
        {
            return false;
        }

        if(initial_played_time == 0)
        {
            if(((server_current_time < System.currentTimeMillis()) && (access_period > System.currentTimeMillis())) || (access_period == -1))
            {
                String Qry = "UPDATE " +DBHelper.WATCH_ACCESS_INFO+ " SET initial_played_time = '"+System.currentTimeMillis()+"'" +
                        " WHERE download_id = '"+download.get(Position).getDOWNLOADID()+"' ";

                DB.execSQL(Qry);
                return true;
            }
            else
            {
                // Show Restriction Message
                ShowRestrictionMsg("You don't have access to play this video.");
                return false;

            }
        }
        else
        {
            if(updated_server_current_time < System.currentTimeMillis())
            {
                if(access_period == -1 || (System.currentTimeMillis() < access_period)) // && (((System.currentTimeMillis() - initial_played_time) < watch_period)) || watch_period == -1)
                {
                    String Qry = "UPDATE " +DBHelper.WATCH_ACCESS_INFO+ " SET updated_server_current_time = '"+System.currentTimeMillis()+"'" +
                            " WHERE download_id = '"+download.get(Position).getDOWNLOADID()+"' ";
                    DB.execSQL(Qry);
                    return true;
                }
                else
                {
                    // Show Restriction Meassge
                    ShowRestrictionMsg("You don't have access to play this video.");
                    return false;
                }
            }
            else
            {
                // Show Restriction Message
                ShowRestrictionMsg("You don't have access to play this video.");
                return false;
            }
        }



        //=====================================END================================================//
    }

    public void PlayThroughChromeCast()
    {

        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE,download.get(Position).getStory());

        final String data[] = download.get(Position).getMUVIID().trim().split("@@@");
        final String titles=data[0];

        movieMetadata.putString(MediaMetadata.KEY_TITLE, titles);
        movieMetadata.addImage(new WebImage(Uri.parse(download.get(Position).getPoster())));


        String mediaContentType = "videos/mp4";
        if (MpdVideoUrl.contains(".mpd")) {
            mediaContentType = "application/dash+xml";
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject();
                jsonObj.put("description", titles);
                jsonObj.put("licenseUrl",licenseUrl);

                //  This Code Is Added For Video Log By Bibhu..

                jsonObj.put("authToken", authTokenStr.trim());
                jsonObj.put("user_id",preferenceManager.getUseridFromPref());
                jsonObj.put("ip_address", ipAddressStr.trim());
                jsonObj.put("movie_id",download.get(Position).getMuviid());
                jsonObj.put("episode_id",StreamId);
                jsonObj.put("watch_status",watch_status);
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

                jsonObj.put("domain_name", APIUrlConstant.BASE_URl.substring(0, APIUrlConstant.BASE_URl.trim().length() - 6));
                jsonObj.put("is_log", "1");

                //=====================End===================//

                // This code is changed according to new Video log //

                jsonObj.put("played_length", "0");
                jsonObj.put("log_temp_id", "0");
                jsonObj.put("resume_time",Played_Length);
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
            Log.v("BIBHU4", "restrict_stream_id============111");
            if(!SubtitleUrl.equals(""))
            {
                Log.v("BIBHU4", "restrict_stream_id======11======111");
                MediaTrack englishSubtitle = new MediaTrack.Builder(0,
                        MediaTrack.TYPE_TEXT)
                        .setName(SubtitleLanguage)
                        .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                        .setContentId(SubtitleUrl)
                        .setLanguage(SubtitleCode)
                        .setContentType("text/vtt")
                        .build();
                tracks.add(englishSubtitle);
            }


            mediaInfo = new MediaInfo.Builder(MpdVideoUrl)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType(mediaContentType)
                    .setMetadata(movieMetadata)
                    .setCustomData(jsonObj)
                    .setMediaTracks(tracks)
                    .build();
            mSelectedMedia = mediaInfo;

            Log.v("BIBHU4", "restrict_stream_id=====33=======111");

            togglePlayback();
        }
        else{
            {
                mediaContentType = "videos/mp4";
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("description",titles);

                    //  This Code Is Added For Video Log By Bibhu..

                    jsonObj.put("authToken", authTokenStr.trim());
                    jsonObj.put("user_id",preferenceManager.getUseridFromPref());
                    jsonObj.put("ip_address", ipAddressStr.trim());
                    jsonObj.put("movie_id",download.get(Position).getMuviid());
                    jsonObj.put("episode_id",StreamId);
                    jsonObj.put("watch_status",watch_status);
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

                    jsonObj.put("domain_name",APIUrlConstant.BASE_URl.trim().substring(0, APIUrlConstant.BASE_URl.trim().length() - 6));
                    jsonObj.put("is_log", "1");

                    //=====================End===================//

                    // This code is changed according to new Video log //

                    jsonObj.put("played_length", "0");
                    jsonObj.put("log_temp_id", "0");
                    jsonObj.put("resume_time",Played_Length);
                    jsonObj.put("seek_status", "");
                    // This  Code Is Added For Drm BufferLog By Bibhu ...

                    jsonObj.put("resolution", "BEST");
                    jsonObj.put("start_time", "0");
                    jsonObj.put("end_time", "0");
                    jsonObj.put("log_unique_id", "0");
                    jsonObj.put("location", "0");
                    jsonObj.put("bandwidth_log_id", "0");
                    jsonObj.put("video_type", "");
                    jsonObj.put("drm_bandwidth_by_sender", "0");

                    //====================End=====================//

                } catch (JSONException e) {
                }
                List tracks = new ArrayList();
                Log.v("BIBHU4", "restrict_stream_id============111");
                if(!SubtitleUrl.equals(""))
                {
                    Log.v("BIBHU4", "restrict_stream_id======11======111");
                    MediaTrack englishSubtitle = new MediaTrack.Builder(0,
                            MediaTrack.TYPE_TEXT)
                            .setName(SubtitleLanguage)
                            .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                            .setContentId(SubtitleUrl)
                            .setLanguage(SubtitleCode)
                            .setContentType("text/vtt")
                            .build();
                    tracks.add(englishSubtitle);
                }


                mediaInfo = new MediaInfo.Builder(MpdVideoUrl)
                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                        .setContentType(mediaContentType)
                        .setMetadata(movieMetadata)
                        .setCustomData(jsonObj)
                        .setMediaTracks(tracks)
                        .build();
                mSelectedMedia = mediaInfo;

                Log.v("BIBHU4", "restrict_stream_id=====33=======111");

                togglePlayback();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1001)
        {
            if (data.getStringExtra("yes").equals("1002")) {
                watch_status = "halfplay";
                PlayThroughChromeCast();

            } else {
                watch_status = "strat";
                Played_Length = 0;
                PlayThroughChromeCast();
            }
        }
        else
        {
            watch_status = "strat";
            Played_Length = 0;
            PlayThroughChromeCast();
        }
    }

    @Override
    protected void onStop() {
        try {
            if(asynGetIpAddress!=null)
                asynGetIpAddress.cancel(true);
            if(asynLoadVideoUrls!=null)
                asynLoadVideoUrls.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}


