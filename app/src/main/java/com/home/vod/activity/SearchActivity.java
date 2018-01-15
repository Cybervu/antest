package com.home.vod.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.home.apisdk.apiController.SearchDataAsynTask;
import com.home.apisdk.apiModel.Search_Data_input;
import com.home.apisdk.apiModel.Search_Data_otput;
import com.home.vod.R;
import com.home.vod.adapter.VideoFilterAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URL;
import java.util.ArrayList;
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
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_RESULT_FOUND_REFINE_YOUR_SEARCH;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEARCH_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.NO_RESULT_FOUND_REFINE_YOUR_SEARCH;
import static com.home.vod.preferences.LanguagePreference.SEARCH_ALERT;
import static com.home.vod.preferences.LanguagePreference.SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.TEXT_SEARCH_PLACEHOLDER;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

/*
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;
*/

/**
 * Created by user on 28-06-2015.
 */

public class SearchActivity extends AppCompatActivity implements SearchDataAsynTask.SearchDataListener {
    ProgressBarHandler videoPDialog;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    ProgressBarHandler pDialog;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    int scrolledPosition = 0;
    boolean scrolling;

    String videoImageStrToHeight;
    int videoHeight = 185;
    int videoWidth = 256;
    // SharedPreferences pref;
    GridItem itemToPlay;
    LanguagePreference languagePreference;

    private static int firstVisibleInListview;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    //for no internet

    private RelativeLayout noInternetConnectionLayout;

    //firsttime load
    boolean firstTime = false;

    //data to load videourl
    private String movieUniqueId;
    private String movieStreamUniqueId;
    // String videoUrlStr;
    String videoResolution = "BEST";


    //search
    String searchTextStr;
    boolean isSearched = false;
    private SearchView.SearchAutoComplete theTextArea;



    /* Handling GridView Scrolling*/


    // private int mCurrentTransitionEffect = JazzyHelper.HELIX;

    //no data
    RelativeLayout noDataLayout;

    /*The Data to be posted*/
    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;

    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    //Set Context

    //Adapter for GridView
    private VideoFilterAdapter customGridAdapter;
    Toolbar mActionBarToolbar;

    //Model for GridView
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl;

    // UI
    private GridView gridView;
    RelativeLayout footerView;
    private String movieVideoUrlStr = "";
    //private String movieThirdPartyUrl = "";
    TextView noDataTextView;
    TextView noInternetTextView;
    PreferenceManager preferenceManager;

    public SearchActivity() {
        // Required empty public constructor

    }

    View header;
    private boolean isLoading = false;
    private int lastVisibleItem, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        languagePreference = LanguagePreference.getLanguagePreference(SearchActivity.this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        preferenceManager = PreferenceManager.getPreferenceManager(this);

        posterUrl = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

        gridView = (GridView) findViewById(R.id.imagesGridView);
       /* gridView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(SearchActivity.this,2);
        gridView.setLayoutManager(mLayoutManager);
        gridView.setItemAnimator(new DefaultItemAnimator());*/

        footerView = (RelativeLayout) findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_RESULT_FOUND_REFINE_YOUR_SEARCH, DEFAULT_NO_RESULT_FOUND_REFINE_YOUR_SEARCH));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);

        //MUVIlaxmi

        //Detect Network Connection


        if (!NetworkStatus.getInstance().isConnected(SearchActivity.this)) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
        gridView.setLayoutParams(layoutParams);
       /* gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setGravity(Gravity.CENTER_HORIZONTAL);*/
        resetData();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                itemToPlay = itemData.get(position);
                String moviePermalink = itemToPlay.getPermalink();
                String movieTypeId = itemToPlay.getVideoTypeId();
                // if searched

                // for tv shows navigate to episodes
                if ((movieTypeId.equalsIgnoreCase("3"))) {
                    if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this, R.style.MyAlertDialogStyle);
                        dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
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
                    } else {

                        final Intent detailsIntent = new Intent(SearchActivity.this, ShowWithEpisodesActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
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
                    final Intent detailsIntent = new Intent(SearchActivity.this, MovieDetailsActivity.class);

                    if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this, R.style.MyAlertDialogStyle);
                        dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
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
                    } else {
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(detailsIntent);
                            }
                        });
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

                        if (NetworkStatus.getInstance().isConnected(SearchActivity.this)) {


                            // default data
                            Search_Data_input search_data_input = new Search_Data_input();
                            search_data_input.setAuthToken(authTokenStr);
                            search_data_input.setLimit(String.valueOf(limit));
                            search_data_input.setOffset(String.valueOf(offset));
                            search_data_input.setQ(searchTextStr.trim());
                            String countryCodeStr = preferenceManager.getCountryCodeFromPref();
                            if (countryCodeStr != null) {
                                search_data_input.setCountry(countryCodeStr);
                            } else {
                                search_data_input.setCountry("IN");
                            }
                            search_data_input.setLanguage_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            SearchDataAsynTask asyncLoadVideos = new SearchDataAsynTask(search_data_input, SearchActivity.this, SearchActivity.this);
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);


                            scrolling = false;

                        }

                    }

                }

            }
        });




       /* AsynLoadVideos asyncViewFavorite = new AsynLoadVideos();
        asyncViewFavorite.executeOnExecutor(threadPoolExecutor);*/
    /*    gridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem)) {
                    // End has been reached
                    listSize = itemData.size();
                    if (mLayoutManager.findLastVisibleItemPosition() >= itemsInServer - 1) {
                        footerView.setVisibility(View.GONE);
                        return;

                    }
                    offset += 1;
                    boolean isNetwork = Util.checkNetwork(SearchActivity.this);
                    if (isNetwork == true) {


                        // searched data
                        AsynLoadSearchVideos asyncViewFavorite = new AsynLoadSearchVideos();
                        asyncViewFavorite.executeOnExecutor(threadPoolExecutor);

                    }
                    //isLoading = true;
                }
            }
        });
*/

    }

    @Override
    public void onBackPressed() {
     /*   if (asyncViewFavorite!=null){
            asyncViewFavorite.cancel(true);
        }
        if (asynLoadVideoUrls!=null){
            asynLoadVideoUrls.cancel(true);
        }*/
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    @Override
    public void onSearchDataPreexecute() {
        {
            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
                pDialog = MainActivity.internetSpeedDialog;
            } else {
                pDialog = new ProgressBarHandler(SearchActivity.this);

                if (listSize == 0) {

                    pDialog.show();
                    footerView.setVisibility(View.GONE);
                } else {
                    pDialog.hide();
                    footerView.setVisibility(View.VISIBLE);
                }
            }


        }
    }

    @Override
    public void onSearchDataPostExecuteCompleted(ArrayList<Search_Data_otput> contentListOutputArray, int status, int totalItems, String message) {

        itemsInServer=totalItems;
        String videoGenreStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoName = "";
        String videoImageStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoPermalinkStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoTypeStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoTypeIdStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoUrlStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        String movieUniqueIdStr = "";
        String movieStreamUniqueIdStr = "";
        int isConverted = 0;
        int isAPV = 0;
        int isPPV = 0;
        String movieThirdPartyUrl = "";

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        if (status > 0) {
            if (status == 200) {
                gridView.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);

                if (contentListOutputArray.size() > 0) {

                    gridView.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);



                    for (int i = 0; i < contentListOutputArray.size(); i++) {


                        videoImageStr = contentListOutputArray.get(i).getPoster_url();
                        videoName = contentListOutputArray.get(i).getEpisode_title();
                        videoTypeIdStr = contentListOutputArray.get(i).getContent_types_id();
                        videoGenreStr = contentListOutputArray.get(i).getGenre();
                        videoPermalinkStr = contentListOutputArray.get(i).getPermalink();
                        isEpisodeStr = contentListOutputArray.get(i).getIs_episode();
                        isConverted = contentListOutputArray.get(i).getIs_converted();
                        isPPV = contentListOutputArray.get(i).getIs_ppv();
                        isAPV = contentListOutputArray.get(i).getIs_advance();
                        itemData.add(new GridItem(videoImageStr, videoName, "", videoTypeIdStr, videoGenreStr, "", videoPermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV));
                    }

                    videoImageStrToHeight = videoImageStr;

                    if (firstTime == true){

                        new RetrieveFeedTask().execute(videoImageStrToHeight);

                        /*Picasso.with(SearchActivity.this).load(videoImageStrToHeight
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
                        });*/

                    }else {
                        AsynLOADUI loadUI = new AsynLOADUI();
                        loadUI.executeOnExecutor(threadPoolExecutor);
                    }

                } else {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);

                }

            } else {

                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);

            }
        } else {

            noDataLayout.setVisibility(View.VISIBLE);
            noInternetConnectionLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);

        }

    }
    //load searched videos
//    private class AsynLoadSearchVideos extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        String responseStr;
//        int status;
//        String videoGenreStr = languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA);
//        String videoName = "";
//        String videoImageStr = languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA);
//        String videoPermalinkStr = languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA);
//        String videoTypeStr = languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA);
//        String videoTypeIdStr = languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA);
//        String videoUrlStr = languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA);
//        String isEpisodeStr = "";
//        String movieUniqueIdStr = "";
//        String movieStreamUniqueIdStr = "";
//        int isConverted = 0;
//        int isAPV = 0;
//        int isPPV = 0;
//        String movieThirdPartyUrl = "";
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.searchUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("limit", String.valueOf(limit));
//                httppost.addHeader("offset", String.valueOf(offset));
//                httppost.addHeader("q", searchTextStr.trim());
//                //httppost.addHeader("deviceType", "roku");
//
//                SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
//                if (countryPref != null) {
//                    String countryCodeStr = countryPref.getString("countryCode", null);
//                    httppost.addHeader("country", countryCodeStr);
//                }else{
//                    httppost.addHeader("country", "IN");
//
//                }                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (itemData != null) {
//                                noDataLayout.setVisibility(View.GONE);
//                                noInternetConnectionLayout.setVisibility(View.GONE);
//                                gridView.setVisibility(View.VISIBLE);
//                                footerView.setVisibility(View.GONE);
//
//                            } else {
//                                noDataLayout.setVisibility(View.GONE);
//                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
//                                gridView.setVisibility(View.VISIBLE);
//                                footerView.setVisibility(View.GONE);
//                            }
//
//                            Toast.makeText(SearchActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            noDataLayout.setVisibility(View.VISIBLE);
//                            noInternetConnectionLayout.setVisibility(View.GONE);
//                            gridView.setVisibility(View.GONE);
//                            footerView.setVisibility(View.GONE);
//                        }
//                    });
//                    e.printStackTrace();
//                }
//
//                JSONObject myJson = null;
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                    String items = myJson.optString("item_count");
//                    itemsInServer = Integer.parseInt(items);
//                }
//
//                if (status > 0) {
//                    if (status == 200) {
//                        JSONArray jsonMainNode = myJson.getJSONArray("search");
//                        int lengthJsonArr = jsonMainNode.length();
//                        for (int i = 0; i < lengthJsonArr; i++) {
//                            JSONObject jsonChildNode;
//                            try {
//                                jsonChildNode = jsonMainNode.getJSONObject(i);
//                                if ((jsonChildNode.has("thirdparty_url")) && jsonChildNode.getString("thirdparty_url").trim() != null && !jsonChildNode.getString("thirdparty_url").trim().isEmpty() && !jsonChildNode.getString("thirdparty_url").trim().equals("null") && !jsonChildNode.getString("thirdparty_url").trim().matches("")) {
//                                    movieThirdPartyUrl = jsonChildNode.getString("thirdparty_url");
//
//                                }
//                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
//                                    videoGenreStr = jsonChildNode.getString("genre");
//
//                                }
//                                if ((jsonChildNode.has("episode_title")) && jsonChildNode.getString("episode_title").trim() != null && !jsonChildNode.getString("episode_title").trim().isEmpty() && !jsonChildNode.getString("episode_title").trim().equals("null") && !jsonChildNode.getString("episode_title").trim().matches("")) {
//                                    videoName = jsonChildNode.getString("episode_title");
//
//                                } else {
//                                    if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
//                                        videoName = jsonChildNode.getString("name");
//
//                                    }
//                                }
//                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
//                                    videoImageStr = jsonChildNode.getString("poster_url");
//                                    //videoImageStr = videoImageStr.replace("episode", "original");
//
//                                }
//                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
//                                    videoPermalinkStr = jsonChildNode.getString("permalink");
//
//                                }
//                                if ((jsonChildNode.has("display_name")) && jsonChildNode.getString("display_name").trim() != null && !jsonChildNode.getString("display_name").trim().isEmpty() && !jsonChildNode.getString("display_name").trim().equals("null") && !jsonChildNode.getString("display_name").trim().matches("")) {
//                                    videoTypeStr = jsonChildNode.getString("display_name");
//
//                                }
//                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
//                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");
//
//                                }
//                                //videoTypeIdStr = "1";
//
//                                if ((jsonChildNode.has("embeddedUrl")) && jsonChildNode.getString("embeddedUrl").trim() != null && !jsonChildNode.getString("embeddedUrl").trim().isEmpty() && !jsonChildNode.getString("embeddedUrl").trim().equals("null") && !jsonChildNode.getString("embeddedUrl").trim().matches("")) {
//                                    videoUrlStr = jsonChildNode.getString("embeddedUrl");
//
//                                }
//                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
//                                    isEpisodeStr = jsonChildNode.getString("is_episode");
//
//                                }
//                                if ((jsonChildNode.has("muvi_uniq_id")) && jsonChildNode.getString("muvi_uniq_id").trim() != null && !jsonChildNode.getString("muvi_uniq_id").trim().isEmpty() && !jsonChildNode.getString("muvi_uniq_id").trim().equals("null") && !jsonChildNode.getString("muvi_uniq_id").trim().matches("")) {
//                                    movieUniqueIdStr = jsonChildNode.getString("muvi_uniq_id");
//
//                                }
//                                if ((jsonChildNode.has("movie_stream_uniq_id")) && jsonChildNode.getString("movie_stream_uniq_id").trim() != null && !jsonChildNode.getString("movie_stream_uniq_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_uniq_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_uniq_id").trim().matches("")) {
//                                    movieStreamUniqueIdStr = jsonChildNode.getString("movie_stream_uniq_id");
//
//                                }
//                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
//                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));
//
//                                }
//                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
//                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));
//
//                                }
//                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
//                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));
//
//                                }
//
//                                itemData.add(new GridItem(videoImageStr, videoName, "", videoTypeIdStr, videoGenreStr, "", videoPermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV));
//
//                            } catch (Exception e) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        noDataLayout.setVisibility(View.VISIBLE);
//                                        noInternetConnectionLayout.setVisibility(View.GONE);
//                                        gridView.setVisibility(View.GONE);
//                                        footerView.setVisibility(View.GONE);
//                                    }
//                                });
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    } else {
//                        responseStr = "0";
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                noDataLayout.setVisibility(View.VISIBLE);
//                                noInternetConnectionLayout.setVisibility(View.GONE);
//                                gridView.setVisibility(View.GONE);
//                                footerView.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                }
//            } catch (Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        noDataLayout.setVisibility(View.VISIBLE);
//                        noInternetConnectionLayout.setVisibility(View.GONE);
//                        gridView.setVisibility(View.GONE);
//                        footerView.setVisibility(View.GONE);
//                    }
//                });
//                e.printStackTrace();
//
//            }
//            return null;
//
//        }
//
//        protected void onPostExecute(Void result) {
//
//            try {
//
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//            } catch (IllegalArgumentException ex) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        noDataLayout.setVisibility(View.VISIBLE);
//                        noInternetConnectionLayout.setVisibility(View.GONE);
//                        gridView.setVisibility(View.GONE);
//                        footerView.setVisibility(View.GONE);
//                    }
//                });
//            }
//
//            if (responseStr == null) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        noDataLayout.setVisibility(View.VISIBLE);
//                        noInternetConnectionLayout.setVisibility(View.GONE);
//                        gridView.setVisibility(View.GONE);
//                        footerView.setVisibility(View.GONE);
//                    }
//                });
//                responseStr = "0";
//            }
//            if ((responseStr.trim().equals("0"))) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        noDataLayout.setVisibility(View.VISIBLE);
//                        noInternetConnectionLayout.setVisibility(View.GONE);
//                        gridView.setVisibility(View.GONE);
//                        footerView.setVisibility(View.GONE);
//                    }
//                });
//            } else {
//                if (itemData.size() <= 0) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            noDataLayout.setVisibility(View.VISIBLE);
//                            noInternetConnectionLayout.setVisibility(View.GONE);
//                            gridView.setVisibility(View.GONE);
//                            footerView.setVisibility(View.GONE);
//                        }
//                    });
//
//                } else {
//
//                    gridView.setVisibility(View.VISIBLE);
//                    noInternetConnectionLayout.setVisibility(View.GONE);
//                    noDataLayout.setVisibility(View.GONE);
//                    videoWidth = 312;
//                    videoHeight = 560;
//
//                    AsynLOADUI loadui = new AsynLOADUI();
//                    loadui.executeOnExecutor(threadPoolExecutor);
//
//                }
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
//                pDialog = MainActivity.internetSpeedDialog;
//            } else {
//                pDialog = new ProgressBarHandler(SearchActivity.this);
//
//                if (listSize == 0) {
//
//                    pDialog.show();
//                    footerView.setVisibility(View.GONE);
//                } else {
//                    pDialog.hide();
//                    footerView.setVisibility(View.VISIBLE);
//                }
//            }
//
//
//        }
//
//
//    }


    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_search, menu);

         SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
         searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

         searchView.setIconifiedByDefault(false);
         searchView.setQueryHint(getString(R.string.search_hint_str));


         return true;
     }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_search, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(languagePreference.getTextofLanguage(SEARCH_PLACEHOLDER, DEFAULT_SEARCH_PLACEHOLDER));
        searchView.requestFocus();
        int searchImgId = getResources().getIdentifier(String.valueOf(R.id.search_button), null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);

        searchView.setMaxWidth(10000);

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_24dp);
        ImageView imgView = (ImageView) searchView.findViewById(R.id.search_mag_icon);
        imgView.setImageResource(R.drawable.ic_search);

        final SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        theTextArea.setBackgroundResource(R.drawable.edit);

        theTextArea.setHint(languagePreference.getTextofLanguage(TEXT_SEARCH_PLACEHOLDER, DEFAULT_TEXT_SEARCH_PLACEHOLDER));
        theTextArea.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
       /* if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            theTextArea.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search_large, 0, 0, 0);
            v.setImageResource(R.drawable.ic_action_search_xlarge);

        }
        else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            theTextArea.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
            v.setImageResource(R.drawable.ic_action_search_large);

        }
        else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {
            theTextArea.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
            v.setImageResource(R.drawable.ic_action_search_large);

        }
        else {
            theTextArea.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search_xlarge, 0, 0, 0);
            v.setImageResource(R.drawable.ic_action_search_xlarge);

        }*/
        theTextArea.setHintTextColor(Color.parseColor("#dadada"));//or any color that you want
        theTextArea.setTextColor(Color.WHITE);

        theTextArea.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == event.KEYCODE_SEARCH) {
                    // Your piece of code on keyboard search click
                    String query = theTextArea.getText().toString().trim();
                    if (query.equalsIgnoreCase("") || query == null) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this, R.style.MyAlertDialogStyle);
                        dlgAlert.setMessage(languagePreference.getTextofLanguage(SEARCH_ALERT, DEFAULT_SEARCH_ALERT));
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
                    } else {
                        resetData();
                        firstTime = true;

                        offset = 1;
                        listSize = 0;
                        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                            limit = 20;
                        } else {
                            limit = 15;
                        }
                        itemsInServer = 0;
                        isLoading = false;
                        searchTextStr = query;
                        if (itemData != null && itemData.size() > 0) {
                            itemData.clear();
                        }
                        isSearched = true;
                        if (!NetworkStatus.getInstance().isConnected(SearchActivity.this)) {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);

                        } else {
                            Search_Data_input search_data_input = new Search_Data_input();
                            search_data_input.setAuthToken(authTokenStr);
                            search_data_input.setLimit(String.valueOf(limit));
                            search_data_input.setOffset(String.valueOf(offset));
                            search_data_input.setQ(searchTextStr.trim());
                            Log.v("pratik","q===="+searchTextStr.trim());
                            String countryCodeStr = preferenceManager.getCountryCodeFromPref();
                            if (countryCodeStr != null) {
                                search_data_input.setCountry(countryCodeStr);
                            } else {
                                search_data_input.setCountry("IN");
                            }
                            search_data_input.setLanguage_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            SearchDataAsynTask asyncLoadVideos = new SearchDataAsynTask(search_data_input, SearchActivity.this, SearchActivity.this);
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

                        }
                    }
                    return true;
                }

                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return true;
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
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_grid_layout, itemData);

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }
              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }
*/

            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = gridView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/
                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(SearchActivity.this, R.layout.videos_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                }
                if (mBundleRecyclerViewState != null) {
                    gridView.onRestoreInstanceState(listState);
                }

            }
            Util.hideKeyboard(SearchActivity.this);
        }
    }

    public void onResume() {
        super.onResume();
        //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = gridView.onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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


    public void resetData() {
        if (itemData != null && itemData.size() > 0) {
            itemData.clear();
        }
        firstTime = true;

        offset = 1;
        isLoading = false;
        listSize = 0;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            limit = 20;
        } else {
            limit = 15;
        }
        itemsInServer = 0;
        isSearched = false;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressBarHandler phandler;

        protected Void doInBackground(String... urls) {
            try {


                URL url = new URL(urls[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                videoHeight = bmp.getHeight();
                videoWidth = bmp.getWidth();


                LogUtil.showLog("MUVI", "videoHeight==============" + videoHeight);
                LogUtil.showLog("MUVI", "videoWidth==============" + videoWidth);

                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed

           /* if (phandler != null && phandler.isShowing()) {
                phandler.hide();
            }*/

            AsynLOADUI loadUI = new AsynLOADUI();
            loadUI.executeOnExecutor(threadPoolExecutor);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  phandler = new ProgressBarHandler(getActivity());
            phandler.show();*/

        }
    }

}

