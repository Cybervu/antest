package com.home.vod.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import com.home.vod.R;
import com.home.vod.adapter.FavoriteAdapter;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.model.LanguageModel;
import com.home.vod.util.ProgressBarHandler;
import com.muvi.player.utils.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;

public class FavoriteActivity extends AppCompatActivity {
    public static ProgressBarHandler progressBarHandler;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;
    String isEpisodeStr;
    AlertDialog alert;

    int index;
    String sucessMsg;


    ProgressBarHandler videoPDialog;
    String videoImageStrToHeight;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;

    int videoHeight = 185;
    int videoWidth = 256;
    SharedPreferences pref;
    GridItem itemToPlay;
    Toolbar mActionBarToolbar;
    GridLayoutManager mLayoutManager;

    private TextView sectionTitle;
    //Register Dialog
    ////////
    String movieUniqueId, FavMoviePoster = "";
    String FebMoviename = "";

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    //for no internet

    private RelativeLayout noInternetConnectionLayout;

    //firsttime load
    boolean firstTime = false;


    /* Handling GridView Scrolling*/

    int scrolledPosition = 0;
    boolean scrolling;
    private static final String KEY_TRANSITION_EFFECT = "transition_effect";

    private Map<String, Integer> mEffectMap;
    // private int mCurrentTransitionEffect = JazzyHelper.HELIX;

    //no data
    RelativeLayout noDataLayout;

    /*The Data to be posted*/
    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;

    GridItem data_send;

    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    TextView noDataTextView;
    TextView noInternetTextView;
    //Set Context
    int isLogin = 0;

    //Adapter for GridView
    private FavoriteAdapter customGridAdapter;
    boolean a=false;

    //Model for GridView
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    String posterUrl, loggedInStr;
    String sectionName;
    String sectionId;
    // UI
    AsynLoadVideos asyncLoadVideos;
    private GridView gridView;
    // private JazzyGridView gridView;
    RelativeLayout footerView;
    ///

    //////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        /////


        /////

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().getStringExtra("SectionId") != null) {
            sectionId = getIntent().getStringExtra("SectionId");

        }
        SharedPreferences isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode
        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
        Log.v("nihar_log",loggedInStr);
        isLogin = isLoginPref.getInt(Util.IS_LOGIN_PREF_KEY, 0);
        Log.v("nihar_log",""+isLogin);
        sectionTitle = (TextView) findViewById(R.id.sectionTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        sectionTitle.setTypeface(castDescriptionTypeface);
        if (getIntent().getStringExtra("sectionName") != null) {
            sectionName = getIntent().getStringExtra("sectionName");
            sectionTitle.setText(sectionName);
        } else {
            sectionTitle.setText("");

        }

        posterUrl = Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

        gridView = (GridView) findViewById(R.id.imagesGridView);
        footerView = (RelativeLayout) findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);

        //subhalaxmi
        gridView.setVisibility(View.VISIBLE);
      /*  ArrayList<GridItem> tempData = new ArrayList<GridItem>();


        for (int i = 0; i <= 10; i ++){
            tempData.add(new GridItem("","Loading","","","","","","","","",0,0,0));
            float density = getResources().getDisplayMetrics().density;

            if (density >= 3.5 && density <= 4.0){
                customGridAdapter = new GridViewAdapter(FavoriteActivity.this, R.layout.nexus_videos_grid_layout, itemData, new GridViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(GridItem item) {
                        clickItem(item);

                    }
                });
            }else{
                customGridAdapter = new GridViewAdapter(FavoriteActivity.this, R.layout.videos_280_grid_layout, itemData, new GridViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(GridItem item) {
                        clickItem(item);

                    }
                });

            }
        }*/

        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                a=true;
                index = i  ;
                if (customGridAdapter.getItem(i).isSelected() == false) {

                    customGridAdapter.getItem(i).setSelected(true);
                    customGridAdapter.getItem(i).setClicked(true);

                    data_send = itemData.get(i);

                    String url =data_send.getImage();


                }else{
                    customGridAdapter.getItem(i).setSelected(false);

                }
                customGridAdapter.notifyDataSetChanged();
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridItem item = itemData.get(position);
                itemToPlay = item;
                String posterUrl = item.getImage();
                String movieName = item.getTitle();
                String movieGenre = item.getMovieGenre();
                String moviePermalink = item.getPermalink();


                Log.v("bibhu","moviePermalink ="+moviePermalink);
                String movieTypeId = item.getVideoTypeId();
                if (a){
                    a=false;
                    return;
                }
                else{

                    if (moviePermalink.matches(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FavoriteActivity.this);
                        dlgAlert.setMessage(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                        dlgAlert.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();

                    } else {

                        if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                            final Intent movieDetailsIntent = new Intent(FavoriteActivity.this, MovieDetailsActivity.class);
                            movieDetailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(movieDetailsIntent);
                                }
                            });


                        } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                            final Intent detailsIntent = new Intent(FavoriteActivity.this, ShowWithEpisodesActivity.class);
                            detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(detailsIntent);
                                }
                            });
                        }
                    }
                }



            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (gridView.getLastVisiblePosition() >= itemsInServer - 1) {
                    footerView.setVisibility(View.GONE);
                    return;

                }

                if (view.getId() == gridView.getId()) {
                    final int currentFirstVisibleItem = gridView.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        mIsScrollingUp = false;

                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mIsScrollingUp = true;

                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    scrolling = false;

                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    scrolling = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (scrolling == true && mIsScrollingUp == false) {

                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                        listSize = itemData.size();
                        if (gridView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;
                        boolean isNetwork = Util.checkNetwork(FavoriteActivity.this);
                        if (isNetwork == true) {

                            // default data
                            AsynLoadVideos asyncLoadVideos = new AsynLoadVideos();
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);


                            scrolling = false;

                        }

                    }

                }

            }
        });


        //Detect Network Connection

        boolean isNetwork = Util.checkNetwork(FavoriteActivity.this);
        if (isNetwork == false) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
        gridView.setLayoutParams(layoutParams);


        firstTime = true;


        //Load first 10 data items

        if (itemData != null && itemData.size() > 0) {
            itemData.clear();
        }
        offset = 1;
        scrolledPosition = 0;
        listSize = 0;
        itemsInServer = 0;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            limit = 20;
        } else {
            limit = 15;
        }
        scrolling = false;

        asyncLoadVideos = new AsynLoadVideos();
        asyncLoadVideos.executeOnExecutor(threadPoolExecutor);




    }


    public void clickItem(GridItem item) {
        String moviePermalink = item.getPermalink();
        String movieTypeId = item.getVideoTypeId();
        // if searched

        // for tv shows navigate to episodes
        if ((movieTypeId.equalsIgnoreCase("3"))) {
            if (moviePermalink.matches(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FavoriteActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else {

                final Intent detailsIntent = new Intent(FavoriteActivity.this, ShowWithEpisodesActivity.class);
                detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                runOnUiThread(new Runnable() {
                    public void run() {
                        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(detailsIntent);
                    }
                });
            }

        }

        // for single clips and movies
        else if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
            final Intent detailsIntent = new Intent(FavoriteActivity.this, MovieDetailsActivity.class);

            if (moviePermalink.matches(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FavoriteActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else {
                detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                runOnUiThread(new Runnable() {
                    public void run() {
                        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(detailsIntent);
                    }
                });
            }
        }

    }


    @Override
    public void onBackPressed() {
        if (asyncLoadVideos != null) {
            asyncLoadVideos.cancel(true);
        }

        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }


    private class AsynLoadVideos extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String movieGenreStr = "";
        String movieTrailer = "";
        String movieName = Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String movieImageStr = Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String moviePermalinkStr = Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoTypeIdStr = Util.getTextofLanguage(FavoriteActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.ViewFavorite.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", loggedInStr);

           /*     httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                httppost.addHeader("orderby", "lastupload");*/

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (itemData != null) {
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                            } else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);
                            }

                            footerView.setVisibility(View.GONE);
                            Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            footerView.setVisibility(View.GONE);
                            gridView.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    int  itemcount = Integer.parseInt(myJson.optString("item_count"));

                    Log.v("status_nihar","itemcount"+itemcount+""+status+"user id: "+loggedInStr+"size:"+itemData.size());
                   /* String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);*/
                }
                Log.v("Nihar", "length " + status);
                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);
                                movieUniqueId = jsonChildNode.getString("movie_uniq_id");

                                if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {
                                    movieName = jsonChildNode.getString("title");
                                    Log.v("mainActivity",movieName);
                                }
                                if ((jsonChildNode.has("poster")) && jsonChildNode.getString("poster").trim() != null && !jsonChildNode.getString("poster").trim().isEmpty() && !jsonChildNode.getString("poster").trim().equals("null") && !jsonChildNode.getString("poster").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster");
                                    //movieImageStr = movieImageStr.replace("episode", "original");
                                    Log.v("mainActivity",movieImageStr);

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");
//                                    moviePermalinkStr = "chit-kwint-she-the";
                                    Log.v("bibhu","Api permalink==="+moviePermalinkStr);

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }

                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }
                                //videoTypeIdStr = "1";
//
                                Log.v("nihar_c",movieImageStr+"/"+movieName+"/"+videoTypeIdStr+"/"+moviePermalinkStr+"/"+isEpisodeStr+"/"+isConverted+"/"+isPPV+"/"+isAPV);

                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, "", "", moviePermalinkStr,isEpisodeStr,movieUniqueId,"",0,0,0));


                            } catch (Exception e) {
                                Log.v("nihar_c","catch_enter"+e.toString());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.v("nihar_c","catch_enter");

                                        noDataLayout.setVisibility(View.VISIBLE);
                                        noInternetConnectionLayout.setVisibility(View.GONE);
                                        gridView.setVisibility(View.GONE);
                                        footerView.setVisibility(View.GONE);
                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("nihar_c","else_enter");
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);
                                footerView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        noDataLayout.setVisibility(View.VISIBLE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        gridView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);
                    }
                });
            }
            return null;

        }

        protected void onPostExecute(Void result) {

            Log.v("AMU","GHHJGBAJ========"+loggedInStr);
            Log.v("AMU","response========"+responseStr);

            if (responseStr == null)
                responseStr = "0";
            if ((responseStr.trim().equals("0"))) {
                try {
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                } catch (IllegalArgumentException ex) {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            } else {
                if (itemData.size() <= 0) {
                    try {
                        if (videoPDialog != null && videoPDialog.isShowing()) {
                            videoPDialog.hide();
                            videoPDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {

                        noDataLayout.setVisibility(View.VISIBLE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        gridView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);
                    }
                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    videoImageStrToHeight = movieImageStr;

                    if (firstTime == true) {
                        Picasso.with(FavoriteActivity.this).load(videoImageStrToHeight
                        ).error(R.drawable.no_image).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                AsynLOADUI loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {
                                videoImageStrToHeight = "https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png";
                                videoWidth = errorDrawable.getIntrinsicWidth();
                                videoHeight = errorDrawable.getIntrinsicHeight();
                                AsynLOADUI loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);

                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {

                            }
                        });

                    } else {
                        AsynLOADUI loadUI = new AsynLOADUI();
                        loadUI.executeOnExecutor(threadPoolExecutor);
                    }


                }
            }
        }

        @Override
        protected void onPreExecute() {
            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
                videoPDialog = MainActivity.internetSpeedDialog;
                footerView.setVisibility(View.GONE);

            } else {
                videoPDialog = new ProgressBarHandler(FavoriteActivity.this);

                if (listSize == 0) {
                    // hide loader for first time

                    videoPDialog.show();
                    footerView.setVisibility(View.GONE);
                } else {
                    // show loader for first time
                    videoPDialog.hide();
                    footerView.setVisibility(View.VISIBLE);

                }
            }
        }


    }

    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            float density = getResources().getDisplayMetrics().density;

            if (firstTime == true) {
                try {
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                } catch (IllegalArgumentException ex) {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }

                gridView.smoothScrollToPosition(0);
                firstTime = false;
                ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
                gridView.setLayoutParams(layoutParams);
                gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                gridView.setGravity(Gravity.CENTER_HORIZONTAL);

                if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    } else {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);
                    } else {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                } else {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    } else {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                    }

                }
                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.videos_grid_layout, itemData);

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }

              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(FavoriteActivity.this, R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(FavoriteActivity.this, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = gridView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(FavoriteActivity.this, R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(FavoriteActivity.this, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.videos_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                }

                if (mBundleRecyclerViewState != null) {
                    gridView.onRestoreInstanceState(listState);
                }

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        /***************chromecast**********************/

        // CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        /***************chromecast**********************/

        MenuItem item, item1, item2, item3, item4, item5, item6;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);
        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        String loggedInStr = pref.getString("PREFS_LOGGEDIN_KEY", null);
        String id = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
        String email = pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
        SharedPreferences language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        if (language_list_pref.getString("total_language", "0").equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);

        if (loggedInStr != null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            item5.setVisible(false);
          /*  item6 = menu.findItem(R.id.menu_item_favorite);
            item6.setTitle(Util.getTextofLanguage(this, Util.MY_FAVOURITE, Util.DEFAULT_MY_FAVOURITE));
            item6.setVisible(true);*/
          /*  item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(FavoriteActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.PROFILE, Util.DEFAULT_PROFILE));

            item1.setVisible(true);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));

            item2.setVisible(true);
            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(true);

        } else if (loggedInStr == null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            if (isLogin == 1) {
                item4.setVisible(true);
                item5.setVisible(true);

            } else {
                item4.setVisible(false);
                item5.setVisible(false);

            }
          /*  item6 = menu.findItem(R.id.menu_item_favorite);
            item6.setTitle(Util.getTextofLanguage(this, Util.MY_FAVOURITE, Util.DEFAULT_MY_FAVOURITE));
            item6.setVisible(false);*/
           /* item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(FavoriteActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.PROFILE, Util.DEFAULT_PROFILE));
            item1.setVisible(false);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(FavoriteActivity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(false);
        }
        return true;
    }
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

    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;
    private boolean mControllersVisible;


    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
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


    MediaInfo mediaInfo;
    /*chromecast-------------------------------------*/

    /*****************chromecvast*-------------------------------------*/











    /***************chromecast**********************/
    @Override
    protected void onResume() {
        super.onResume();


        invalidateOptionsMenu();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(FavoriteActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(FavoriteActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(FavoriteActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = Util.getTextofLanguage(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = Util.getTextofLanguage(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);

                if (Util.languageModel != null && Util.languageModel.size() > 0) {


                    ShowLanguagePopup();

                } else {
                    AsynGetLanguageList asynGetLanguageList = new AsynGetLanguageList();
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
          /*  case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;*/
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(FavoriteActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FavoriteActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(FavoriteActivity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(FavoriteActivity.this, Util.YES, Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        AsynLogoutDetails asynLogoutDetails = new AsynLogoutDetails();
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(Util.getTextofLanguage(FavoriteActivity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

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

    private class AsynLogoutDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        int responseCode;
        String loginHistoryIdStr = pref.getString("PREFS_LOGIN_HISTORYID_KEY", null);
        String responseStr;

        @Override
        protected Void doInBackground(Void... params) {


            String urlRouteList = Util.rootUrl().trim() + Util.logoutUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("login_history_id", loginHistoryIdStr);
                httppost.addHeader("lang_code", Util.getTextofLanguage(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            responseCode = 0;
                            Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    responseCode = 0;
                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    responseCode = Integer.parseInt(myJson.optString("code"));
                }

            } catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;

                }
            } catch (IllegalArgumentException ex) {
                Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
            if (responseStr == null) {
                Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
            if (responseCode == 0) {
                Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
            if (responseCode > 0) {
                if (responseCode == 200) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
                    if (loginPref != null) {
                        SharedPreferences.Editor countryEditor = loginPref.edit();
                        countryEditor.clear();
                        countryEditor.commit();
                    }
                 /*   SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                    if (countryPref!=null) {
                        SharedPreferences.Editor countryEditor = countryPref.edit();
                        countryEditor.clear();
                        countryEditor.commit();
                    }*/
                    if ((Util.getTextofLanguage(FavoriteActivity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                            .trim()).equals("1")) {
                        final Intent startIntent = new Intent(FavoriteActivity.this, SplashScreen.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });
                    } else {
                        final Intent startIntent = new Intent(FavoriteActivity.this, MainActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });
                    }

                } else {
                    Toast.makeText(FavoriteActivity.this, Util.getTextofLanguage(FavoriteActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

                }
            }

        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(FavoriteActivity.this);
            pDialog.show();
        }
    }

    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FavoriteActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(FavoriteActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(Util.getTextofLanguage(FavoriteActivity.this, Util.APP_SELECT_LANGUAGE, Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(Util.getTextofLanguage(FavoriteActivity.this, Util.BUTTON_APPLY, Util.DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

      //  languageCustomAdapter = new LanguageCustomAdapter(FavoriteActivity.this, Util.languageModel);
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
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(FavoriteActivity.this, recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
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


                    AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                    asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

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

  /*  @Override
    protected void onResume() {
        super.onResume();
        *//***************chromecast**********************//*
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }



        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        */

    /***************chromecast**********************//*
        invalidateOptionsMenu();

    }*/

    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    private class AsynGetLanguageList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.LanguageList.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());


                // Execute HTTP Post Request
                try {


                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
                } catch (Exception e) {
                }
                if (responseStr != null) {
                    JSONObject json = new JSONObject(responseStr);
                    try {
                        status = Integer.parseInt(json.optString("code"));
                        Default_Language = json.optString("default_lang");
                        if (!Util.getTextofLanguage(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, "").equals("")) {
                            Default_Language = Util.getTextofLanguage(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                        }

                    } catch (Exception e) {
                        status = 0;
                    }
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (progressBarHandler.isShowing()) {
                progressBarHandler.hide();
                progressBarHandler = null;

            }

            if (responseStr == null) {
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject json = new JSONObject(responseStr);
                        JSONArray jsonArray = json.getJSONArray("lang_list");
                        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String language_id = jsonArray.getJSONObject(i).optString("code").trim();
                            String language_name = jsonArray.getJSONObject(i).optString("language").trim();


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

                        //Util.languageModel = languageModels;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                 /*   if(!Default_Language.equals("en")) {
                        //                  Call For Language Translation.
                        AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);

                    }else{

                    }*/

                } else {
                }
            }
            ShowLanguagePopup();


        }

        protected void onPreExecute() {

            progressBarHandler = new ProgressBarHandler(FavoriteActivity.this);
            progressBarHandler.show();

        }
    }


    private class AsynGetTransalatedLanguage extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.LanguageTranslation.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("lang_code", Default_Language);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
                } catch (Exception e) {
                }
                if (responseStr != null) {
                    JSONObject json = new JSONObject(responseStr);
                    try {
                        status = Integer.parseInt(json.optString("code"));
                    } catch (Exception e) {
                        status = 0;
                    }
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (progressBarHandler != null && progressBarHandler.isShowing()) {
                progressBarHandler.hide();
                progressBarHandler = null;

            }

            if (responseStr == null) {
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject parent_json = new JSONObject(responseStr);
                        JSONObject json = parent_json.getJSONObject("translation");


                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ALREADY_MEMBER, json.optString("already_member").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ACTIAVTE_PLAN_TITLE, json.optString("activate_plan_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_STATUS_ACTIVE, json.optString("transaction_status_active").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ADD_TO_FAV, json.optString("add_to_fav").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ADDED_TO_FAV, json.optString("added_to_fav").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ADVANCE_PURCHASE, json.optString("advance_purchase").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ALERT, json.optString("alert").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.EPISODE_TITLE, json.optString("episodes_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SORT_ALPHA_A_Z, json.optString("sort_alpha_a_z").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SORT_ALPHA_Z_A, json.optString("sort_alpha_z_a").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.AMOUNT, json.optString("amount").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.COUPON_CANCELLED, json.optString("coupon_cancelled").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.BUTTON_APPLY, json.optString("btn_apply").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SIGN_OUT_WARNING, json.optString("sign_out_warning").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.DISCOUNT_ON_COUPON, json.optString("discount_on_coupon").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CREDIT_CARD_CVV_HINT, json.optString("credit_card_cvv_hint").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CAST, json.optString("cast").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CAST_CREW_BUTTON_TITLE, json.optString("cast_crew_button_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CENSOR_RATING, json.optString("censor_rating").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ENTER_EMPTY_FIELD, json.optString("enter_register_fields_data").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.HOME, json.optString("home").trim());

                        if (json.optString("change_password").trim() == null || json.optString("change_password").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
                        } else {
                            Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
                        }
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CONFIRM_PASSWORD, json.optString("confirm_password").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.DIRECTOR, json.optString("director").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.DESCRIPTION, json.optString("description").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.EMAIL_EXISTS, json.optString("email_exists").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.EMAIL_DOESNOT_EXISTS, json.optString("email_does_not_exist").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.EMAIL_PASSWORD_INVALID, json.optString("email_password_invalid").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.COUPON_CODE_HINT, json.optString("coupon_code_hint").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SEARCH_ALERT, json.optString("search_alert").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CREDIT_CARD_NUMBER_HINT, json.optString("credit_card_number_hint").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TEXT_EMIAL, json.optString("text_email").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NAME_HINT, json.optString("name_hint").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CREDIT_CARD_NAME_HINT, json.optString("credit_card_name_hint").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TEXT_PASSWORD, json.optString("text_password").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ERROR_IN_PAYMENT_VALIDATION, json.optString("error_in_payment_validation").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ERROR_IN_REGISTRATION, json.optString("error_in_registration").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_STATUS_EXPIRED, json.optString("transaction_status_expired").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.DETAILS_NOT_FOUND_ALERT, json.optString("details_not_found_alert").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.FAILURE, json.optString("failure").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.FILTER_BY, json.optString("filter_by").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.FORGOT_PASSWORD, json.optString("forgot_password").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.GENRE, json.optString("genre").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.AGREE_TERMS, json.optString("agree_terms").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.INVALID_COUPON, json.optString("invalid_coupon").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.INVOICE, json.optString("invoice").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.LANGUAGE_POPUP_LANGUAGE, json.optString("language_popup_language").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SORT_LAST_UPLOADED, json.optString("sort_last_uploaded").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.LANGUAGE_POPUP_LOGIN, json.optString("language_popup_login").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.LOGIN, json.optString("login").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.LOGOUT, json.optString("logout").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.LOGOUT_SUCCESS, json.optString("logout_success").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.MY_FAVOURITE, json.optString("my_favourite").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NEW_PASSWORD, json.optString("new_password").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NEW_HERE_TITLE, json.optString("new_here_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO, json.optString("no").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO_DATA, json.optString("no_data").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO_INTERNET_CONNECTION, json.optString("no_internet_connection").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO_INTERNET_NO_DATA, json.optString("no_internet_no_data").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO_DETAILS_AVAILABLE, json.optString("no_details_available").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.BUTTON_OK, json.optString("btn_ok").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.OLD_PASSWORD, json.optString("old_password").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.OOPS_INVALID_EMAIL, json.optString("oops_invalid_email").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ORDER, json.optString("order").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_DETAILS_ORDER_ID, json.optString("transaction_detail_order_id").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PASSWORD_RESET_LINK, json.optString("password_reset_link").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PASSWORDS_DO_NOT_MATCH, json.optString("password_donot_match").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PAY_BY_PAYPAL, json.optString("pay_by_paypal").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.BTN_PAYNOW, json.optString("btn_paynow").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PAY_WITH_CREDIT_CARD, json.optString("pay_with_credit_card").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PAYMENT_OPTIONS_TITLE, json.optString("payment_options_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PLAN_NAME, json.optString("plan_name").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, json.optString("activate_subscription_watch_video").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.COUPON_ALERT, json.optString("coupon_alert").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.VALID_CONFIRM_PASSWORD, json.optString("valid_confirm_password").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PROFILE, json.optString("profile").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PROFILE_UPDATED, json.optString("profile_updated").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PURCHASE, json.optString("purchase").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_DETAIL_PURCHASE_DATE, json.optString("transaction_detail_purchase_date").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PURCHASE_HISTORY, json.optString("purchase_history").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.BTN_REGISTER, json.optString("btn_register").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SORT_RELEASE_DATE, json.optString("sort_release_date").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SAVE_THIS_CARD, json.optString("save_this_card").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TEXT_SEARCH_PLACEHOLDER, json.optString("text_search_placeholder").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SEASON, json.optString("season").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SELECT_OPTION_TITLE, json.optString("select_option_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SELECT_PLAN, json.optString("select_plan").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SIGN_UP_TITLE, json.optString("signup_title").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SLOW_INTERNET_CONNECTION, json.optString("slow_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SLOW_ISSUE_INTERNET_CONNECTION, json.optString("slow_issue_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SORRY, json.optString("sorry").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.GEO_BLOCKED_ALERT, json.optString("geo_blocked_alert").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SIGN_OUT_ERROR, json.optString("sign_out_error").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.ALREADY_PURCHASE_THIS_CONTENT, json.optString("already_purchase_this_content").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CROSSED_MAXIMUM_LIMIT, json.optString("crossed_max_limit_of_watching").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SORT_BY, json.optString("sort_by").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.STORY_TITLE, json.optString("story_title").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.BTN_SUBMIT, json.optString("btn_submit").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_STATUS, json.optString("transaction_success").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.VIDEO_ISSUE, json.optString("video_issue").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO_CONTENT, json.optString("no_content").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.NO_VIDEO_AVAILABLE, json.optString("no_video_available").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, json.optString("content_not_available_in_your_country").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_DATE, json.optString("transaction_date").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANASCTION_DETAIL, json.optString("transaction_detail").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION_STATUS, json.optString("transaction_status").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRANSACTION, json.optString("transaction").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TRY_AGAIN, json.optString("try_again").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.UNPAID, json.optString("unpaid").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.USE_NEW_CARD, json.optString("use_new_card").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.VIEW_MORE, json.optString("view_more").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.VIEW_TRAILER, json.optString("view_trailer").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.WATCH, json.optString("watch").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.WATCH_NOW, json.optString("watch_now").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SIGN_OUT_ALERT, json.optString("sign_out_alert").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.UPDATE_PROFILE_ALERT, json.optString("update_profile_alert").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.YES, json.optString("yes").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.PURCHASE_SUCCESS_ALERT, json.optString("purchase_success_alert").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.CARD_WILL_CHARGE, json.optString("card_will_charge").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SEARCH_HINT, json.optString("search_hint").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.TERMS, json.optString("terms").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.APP_ON, json.optString("app_on").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.FILL_FORM_BELOW, json.optString("Fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.MESSAGE, json.optString("text_message").trim());

                        Util.getTextofLanguage(FavoriteActivity.this, Util.PURCHASE, Util.DEFAULT_PURCHASE);
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, Default_Language);
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.MESSAGE, json.optString("text_message").trim());

                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
                        Util.setLanguageSharedPrefernce(FavoriteActivity.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());

                        //Call For Language PopUp Dialog

                        languageCustomAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Call For Other Methods.


                } else {
                }
            }


        }

        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(FavoriteActivity.this);
            progressBarHandler.show();
        }
    }
    public  void removeFavorite(GridItem gridItem){
        movieUniqueId = gridItem.getMovieUniqueId();

        AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
        asynFavoriteDelete.execute();
    }
    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {


        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;

//    @Override
//    protected void onPreExecute() {
//        pDialog = new ProgressBarHandler(getActivity().getBaseContext());
//        pDialog.show();
//        Log.v("NIhar","onpreExecution");
//    }

        @Override
        protected Void doInBackground(String... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", movieUniqueId);
                Log.v("Fav_act","movieUniqueId  ========"+movieUniqueId);
                httppost.addHeader("content_type", isEpisodeStr);
                httppost.addHeader("user_id", loggedInStr);

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                        status = 0;
//
//                    }
//
//                });
                }
            } catch (IOException e) {
//            if (pDialog != null && pDialog.isShowing()) {
//                pDialog.hide();
//                pDialog = null;
//            }
//            status = 0;
                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            showToast();
            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }
            Log.v("ANU","REMOVED");
            itemData.remove(index);
            gridView.invalidateViews();
            customGridAdapter.notifyDataSetChanged();
            gridView.setAdapter(customGridAdapter);

            Intent Sintent = new Intent("ITEM_STATUS");
            Sintent.putExtra("movie_uniq_id", movieUniqueId);

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Sintent);


        }
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(FavoriteActivity.this);
            pDialog.show();


        }
    }
    public void showToast()
    {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg=(TextView) toastRoot.findViewById(R.id.toastMsg) ;
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
//        toast.setText("Added to Favorites");
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }
}

