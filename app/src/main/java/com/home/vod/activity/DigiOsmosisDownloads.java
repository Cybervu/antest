package com.home.vod.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ProgressBarHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import player.model.ContactModel1;
import player.utils.DBHelper;
import player.utils.Util;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.util.Constant.authTokenStr;
import static java.lang.Thread.sleep;


public class DigiOsmosisDownloads extends AppCompatActivity implements GetLanguageListAsynTask.GetLanguageListListener ,LogoutAsynctask.LogoutListener, GetTranslateLanguageAsync.GetTranslateLanguageInfoListener{

    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
    int prevPosition = 0;
    String isEpisodeStr;
    AlertDialog alert;
    public static ProgressBarHandler progressBarHandler;
    //public static ProgressDialog  pDialog;
    Context context;
    GridView gridView;
    TextView noDataTextView;
    RelativeLayout nodata,logo_image;
    /*ArrayList<Detail> mylist;
    ArrayList<Developer> jj;*/
    int islogin=0;
    DigiOsmosisDownloadAdapter adapter;
    List<String[]> allElements;
    // CSVReader readers = null;
    String[] nextLine=null;
    SharedPreferences pref;
    String emailIdStr = "";
    ProgressDialog progressDialog;
    DBHelper dbHelper;
    LanguagePreference languagePreference;
    PreferenceManager preferenceManager;
    ArrayList<ContactModel1> databaseupdate;
    DownloadManager downloadManager;
    public boolean downloading;
    RelativeLayout image_logo;
    LinearLayout titleLayout;
    TextView titleTextView;
    // MydownloadModel mydownloadModel;
    static String path,filename,_filename,token,title,poster,genre,duration,rdate,movieid,user,uniqid;
    ArrayList<ContactModel1> download;
    ProgressBarHandler pDialog;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    String download_id_from_watch_access_table = "";

    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);



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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_mydownload);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        dbHelper=new DBHelper(DigiOsmosisDownloads.this);
        dbHelper.getWritableDatabase();


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
        image_logo = (RelativeLayout) findViewById(R.id.logo_image);
        image_logo.bringToFront();

        gridView= (GridView)findViewById(R.id.gridView);
        nodata= (RelativeLayout) findViewById(R.id.noData);
        noDataTextView= (TextView) findViewById(R.id.noDataTextView);
        //  islogin = preferenceManager.getLoginFeatureFromPref();
        if (preferenceManager!=null){
            emailIdStr= preferenceManager.getEmailIdFromPref();

            Toast.makeText(getApplicationContext(),emailIdStr,Toast.LENGTH_LONG).show();


        }else {
            emailIdStr = "";


        }


//        databaseupdate=dbHelper.getContactt(emailIdStr,2);
//        if (databaseupdate.size()>0) {
//
//
//            Toast.makeText(getApplicationContext(),"Downloading",Toast.LENGTH_LONG).show();
//            int i = 0;
//
//            for (i = 0; i < databaseupdate.size(); i++) {
//
//                checkDownLoadStatusFromDownloadManager1(databaseupdate.get(i));
//
//
//            }
//        }else {
//
//
//        }





        if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {

            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_large_vertical) : (int) getResources().getInteger(R.integer.configuration_large_vertical));


        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_normal_vertical) : (int) getResources().getInteger(R.integer.configuration_normal_vertical));


        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_small_vertical) : (int) getResources().getInteger(R.integer.configuration_small_vertical));


        } else {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_xlarge_vertical) : (int) getResources().getInteger(R.integer.configuration_xlarge_vertical));

        }
        //download=dbHelper.getDownloadcontent(emailIdStr);
        download=dbHelper.getContactt(emailIdStr,1);

        if(download.size()>0) {


            adapter = new DigiOsmosisDownloadAdapter(DigiOsmosisDownloads.this, R.layout.custom_offlist, download);
            gridView.setAdapter(adapter);
        }else {


            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT,DEFAULT_NO_CONTENT));
        }



       /* ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.listitem,mylist);
        list.setAdapter(adapter);*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                SubTitleName.clear();
                SubTitlePath.clear();



                pDialog = new ProgressBarHandler(DigiOsmosisDownloads.this);
                pDialog.show();

                new Thread(new Runnable(){
                    public void run(){

                        SQLiteDatabase DB = DigiOsmosisDownloads.this.openOrCreateDatabase("DOWNLOADMANAGER_ONDEMAND.db", MODE_PRIVATE, null);
                        Cursor cursor = DB.rawQuery("SELECT LANGUAGE,PATH FROM SUBTITLE_ONDEMAND WHERE UID = '"+download.get(position).getUniqueId()+"'", null);
                        int count = cursor.getCount();

                        if (count > 0) {
                            if (cursor.moveToFirst()) {
                                do {
                                    SubTitleName.add(cursor.getString(0).trim());
                                    SubTitlePath.add(cursor.getString(1).trim());


                                    Log.v("BIBHU3","SubTitleName============"+cursor.getString(0).trim());
                                    Log.v("BIBHU3","SubTitlePath============"+cursor.getString(1).trim());

                                } while (cursor.moveToNext());
                            }
                        }



                        // following block is responsible for restriction on download content .....


                        long server_current_time = 0;
                        long watch_period = -1;
                        long access_period = -1;
                        long initial_played_time = 0;
                        long updated_server_current_time = 0;

                        Cursor cursor1 = DB.rawQuery("SELECT server_current_time , watch_period , access_period , initial_played_time , updated_server_current_time,download_id FROM "+DBHelper.WATCH_ACCESS_INFO+" WHERE download_id = '"+download.get(position).getDOWNLOADID()+"'", null);
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
                            return;
                        }

                        if(initial_played_time == 0)
                        {
                            if(((server_current_time < System.currentTimeMillis()) && (access_period > System.currentTimeMillis())) || (access_period == -1))
                            {
                                String Qry = "UPDATE " +DBHelper.WATCH_ACCESS_INFO+ " SET initial_played_time = '"+System.currentTimeMillis()+"'" +
                                        " WHERE download_id = '"+download.get(position).getDOWNLOADID()+"' ";

                                DB.execSQL(Qry);


                            }
                            else
                            {
                                // Show Restriction Message

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowRestrictionMsg("You don't have access to play this video.");
                                        return;
                                    }
                                });



                            }
                        }
                        else
                        {
                            if(updated_server_current_time < System.currentTimeMillis())
                            {
                                if(access_period == -1 || (System.currentTimeMillis() < access_period)) // && (((System.currentTimeMillis() - initial_played_time) < watch_period)) || watch_period == -1)
                                {
                                    String Qry = "UPDATE " +DBHelper.WATCH_ACCESS_INFO+ " SET updated_server_current_time = '"+System.currentTimeMillis()+"'" +
                                            " WHERE download_id = '"+download.get(position).getDOWNLOADID()+"' ";
                                    DB.execSQL(Qry);
                                }
                                else
                                {
                                    // Show Restriction Meassge
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowRestrictionMsg("You don't have access to play this video.");
                                            return;
                                        }
                                    });


                                }
                            }
                            else
                            {
                                // Show Restriction Message
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowRestrictionMsg("You don't have access to play this video.");
                                        return;
                                    }
                                });


                            }
                        }


                        //=====================================END================================================//



                        final String pathh=download.get(position).getPath();
                        final String titles=download.get(position).getMUVIID();
                        final String gen=download.get(position).getGenere();
                        final String tok=download.get(position).getToken();
                        final String contentid=download.get(position).getContentid();
                        final String muviid=download.get(position).getMuviid();
                        String post=download.get(position).getPoster();
                        final String vidduration=download.get(position).getDuration();
                        final String filename=pathh.substring(pathh.lastIndexOf("/") + 1);


                        try {


                            sleep(1200);


                            runOnUiThread(new Runnable() {
                                //
                                @Override
                                public void run() {

                                    Intent in=new Intent(DigiOsmosisDownloads.this,MarlinBroadbandExample.class);
                                    Log.v("BKS", "PATH==" + pathh);
                                    Log.v("BKS", "Title==" + titles);
                                    Log.v("BKS", "TOK=" + tok);

                                    in.putExtra("SubTitleName", SubTitleName);
                                    in.putExtra("SubTitlePath", SubTitlePath);

                                    in.putExtra("FILE", pathh);
                                    in.putExtra("Title", titles);
                                    //in.putExtra("GENRE", gen);
                                    in.putExtra("TOK", tok);

                                    in.putExtra("contid", contentid);
                                    in.putExtra("gen", gen);
                                    in.putExtra("muvid", muviid);
                                    in.putExtra("vid", vidduration);
                                    in.putExtra("FNAME", filename);
                                    in.putExtra("download_id_from_watch_access_table", download_id_from_watch_access_table);

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
        });



    }

    public void visible(){
        if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_large_vertical) : (int) getResources().getInteger(R.integer.configuration_large_vertical));


        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_normal_vertical) : (int) getResources().getInteger(R.integer.configuration_normal_vertical));


        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_small_vertical) : (int) getResources().getInteger(R.integer.configuration_small_vertical));


        } else {
            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_xlarge_vertical) : (int) getResources().getInteger(R.integer.configuration_xlarge_vertical));

        }
        if(download.size()>0) {

            adapter = new DigiOsmosisDownloadAdapter(DigiOsmosisDownloads.this, R.layout.custom_offlist, download);
            gridView.setAdapter(adapter);

        }else {

            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT,DEFAULT_NO_CONTENT));
        }


    }

    public void checkDownLoadStatusFromDownloadManager1(final ContactModel1 model) {


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

                                } else if (status == DownloadManager.STATUS_FAILED) {
                                    // 1. process for download fail.
                                    model.setDSTATUS(0);

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
                                //Util.downloadprogress=(int) progress;

                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                    //downloading = false;
//                                download_layout.setVisibility(View.GONE);
                                    //writefilepath();
                                    //String path=Environment.getExternalStorageDirectory() + "/WITHDRM/"+fname;

//                                    String path1 = Environment.getExternalStorageDirectory() + "/Android/data/"+getApplicationContext().getPackageName().trim()+"/WITHDRM/" + Util.dataModel.getVideoTitle().trim() + "-1." + "mlv";
//                                    File file = new File(path1);
//                                    if (file != null && file.exists()) {
//
//                                        file.delete();
//
//                                    }

                                }


                            }
                        } else {
                            // model.setDSTATUS(3);
                        }

//
                        // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                        cursor.close();
                    }


                }
            }).start();

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





}
