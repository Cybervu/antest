package com.home.vod.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.home.vod.R;
import com.home.vod.adapter.MyDownloadAdapter;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ProgressBarHandler;


import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import player.model.ContactModel1;
import player.utils.DBHelper;
import player.utils.Util;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static java.lang.Thread.sleep;


public class MyDownloads extends AppCompatActivity {



    //public static ProgressDialog  pDialog;
    Context context;
    ListView list;
    TextView noDataTextView;
    RelativeLayout nodata;
    /*ArrayList<Detail> mylist;
    ArrayList<Developer> jj;*/
    int islogin=0;
    MyDownloadAdapter adapter;
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
    // MydownloadModel mydownloadModel;
    static String path,filename,_filename,token,title,poster,genre,duration,rdate,movieid,user,uniqid;
    ArrayList<ContactModel1> download;
    ProgressBarHandler pDialog;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    String download_id_from_watch_access_table = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_mydownload);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        dbHelper=new DBHelper(MyDownloads.this);


        Toolbar mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD,DEFAULT_MY_DOWNLOAD));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();

                finish();
            }
        });

        list= (ListView)findViewById(R.id.listView);
        nodata= (RelativeLayout) findViewById(R.id.noData);
        noDataTextView= (TextView) findViewById(R.id.noDataTextView);
        //  islogin = preferenceManager.getLoginFeatureFromPref();
        if (preferenceManager!=null){
            emailIdStr= preferenceManager.getEmailIdFromPref();


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


        download=dbHelper.getContactt(emailIdStr,1);
        //download=dbHelper.getDownloadcontent(emailIdStr);
        if(download.size()>0) {
            adapter = new MyDownloadAdapter(MyDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
            list.setAdapter(adapter);
        }else {
            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT,DEFAULT_NO_CONTENT));
        }



       /* ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.listitem,mylist);
        list.setAdapter(adapter);*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                SubTitleName.clear();
                SubTitlePath.clear();

//                try {
//
//
//
//                    String path1 = Environment.getExternalStorageDirectory() + "/Android/data/"+getApplicationContext().getPackageName().trim()+"/WITHDRM/" + download.get(position).getMUVIID().trim() + "-1." + "mlv";
//                    File file = new File(path1);
//                    if (file != null && file.exists()) {
//
//                        file.delete();
//
//                    }
//
//                }catch (Exception e){
//
//
//
//                }

                pDialog = new ProgressBarHandler(MyDownloads.this);
                pDialog.show();

                new Thread(new Runnable(){
                    public void run(){

                        SQLiteDatabase DB = MyDownloads.this.openOrCreateDatabase("DOWNLOADMANAGER_ONDEMAND.db", MODE_PRIVATE, null);
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

                                    Intent in=new Intent(MyDownloads.this,MarlinBroadbandExample.class);
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

        if(download.size()>0) {
            adapter = new MyDownloadAdapter(MyDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
            list.setAdapter(adapter);

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
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MyDownloads.this, R.style.MyAlertDialogStyle);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Util.getTextofLanguage(MyDownloads.this, Util.SORRY, Util.DEFAULT_SORRY));
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(MyDownloads.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }
}
