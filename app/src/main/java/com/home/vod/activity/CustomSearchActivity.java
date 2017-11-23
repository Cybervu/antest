package com.home.vod.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.SearchDataAsynTask;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.Search_Data_input;
import com.home.apisdk.apiModel.Search_Data_otput;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.Episode_Programme_Handler;
import com.home.vod.R;
import com.home.vod.Single_Part_Programme_Handler;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.adapter.VideoFilterAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ProgressBarHandler;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import player.utils.Util;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEARCH_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEARCH_RESULTS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SEARCH_ALERT;
import static com.home.vod.preferences.LanguagePreference.SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.SEARCH_RESULTS;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
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
public class CustomSearchActivity extends AppCompatActivity implements SearchDataAsynTask.SearchDataListener,GetLanguageListAsynTask.GetLanguageListListener , LogoutAsynctask.LogoutListener, GetTranslateLanguageAsync.GetTranslateLanguageInfoListener {
    ProgressBarHandler videoPDialog;
    public static ProgressBarHandler progressBarHandler;
    int prevPosition = 0;

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    ProgressBarHandler pDialog;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    int scrolledPosition = 0;
    boolean scrolling;
    AlertDialog alert;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    String videoImageStrToHeight;
    int videoHeight = 185;
    int videoWidth = 256;
    // SharedPreferences pref;
    GridItem itemToPlay;
    LanguagePreference languagePreference;
    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;

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
    RelativeLayout noDataLayout,logo_image;

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

    public CustomSearchActivity() {
        // Required empty public constructor

    }

    View header;
    private boolean isLoading = false;
    private int lastVisibleItem, totalItemCount;
    LinearLayout titleLayout;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_search);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(CustomSearchActivity.this);
        episodeListOptionMenuHandler=new EpisodeListOptionMenuHandler(this);
        titleLayout = (LinearLayout) findViewById(R.id.line_layout);
        titleTextView = (TextView) findViewById(R.id.categoryTitle);
        titleTextView.setText(languagePreference.getTextofLanguage(SEARCH_RESULTS, DEFAULT_SEARCH_RESULTS));
        titleLayout.setVisibility(View.GONE);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        logo_image = (RelativeLayout) findViewById(R.id.logo_image);
        logo_image.bringToFront();
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(languagePreference.getTextofLanguage(SEARCH_PLACEHOLDER, DEFAULT_SEARCH_PLACEHOLDER));
        searchView.requestFocus();
        int searchImgId = getResources().getIdentifier(String.valueOf(R.id.search_button), null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
       // v.setImageResource(R.drawable.ic_audiotrack);

        searchView.setMaxWidth(10000);
        ImageView imgView = (ImageView)searchView.findViewById(R.id.search_mag_icon);
        imgView.setImageResource(R.drawable.search_bg);
        final SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
       // theTextArea.setBackgroundResource(R.drawable.edit);

        theTextArea.setHint(languagePreference.getTextofLanguage(TEXT_SEARCH_PLACEHOLDER, DEFAULT_TEXT_SEARCH_PLACEHOLDER));
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
        theTextArea.setTextColor(getResources().getColor(R.color.textColor));

        theTextArea.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    String query = theTextArea.getText().toString().trim();
                    if (query.equalsIgnoreCase("") || query == null) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CustomSearchActivity.this, R.style.MyAlertDialogStyle);
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
                        if (!NetworkStatus.getInstance().isConnected(CustomSearchActivity.this)) {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);
                            titleLayout.setVisibility(View.GONE);


                        } else {
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
                            SearchDataAsynTask asyncLoadVideos = new SearchDataAsynTask(search_data_input, CustomSearchActivity.this, CustomSearchActivity.this);
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
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);

        //MUVIlaxmi

        //Detect Network Connection


        if (!NetworkStatus.getInstance().isConnected(CustomSearchActivity.this)) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            titleLayout.setVisibility(View.GONE);

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
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CustomSearchActivity.this, R.style.MyAlertDialogStyle);
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

                        new Episode_Programme_Handler(CustomSearchActivity.this).handleIntent(PERMALINK_INTENT_KEY,moviePermalink);
                    }

                }

                // for single clips and movies
                else if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                    if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CustomSearchActivity.this, R.style.MyAlertDialogStyle);
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

                        new Single_Part_Programme_Handler(CustomSearchActivity.this).handleIntent(PERMALINK_INTENT_KEY,moviePermalink);
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

                        if (NetworkStatus.getInstance().isConnected(CustomSearchActivity.this)) {

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
                            SearchDataAsynTask asyncLoadVideos = new SearchDataAsynTask(search_data_input, CustomSearchActivity.this, CustomSearchActivity.this);
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
                pDialog = new ProgressBarHandler(CustomSearchActivity.this);

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

        String videoGenreStr = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);
        String videoName = "";
        String videoImageStr = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);
        String videoPermalinkStr = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);
        String videoTypeStr = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);
        String videoTypeIdStr = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);
        String videoUrlStr = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);
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
        }catch (IllegalArgumentException ex) {

        }

        if (status>0){
            if (status==200){
                gridView.setVisibility(View.VISIBLE);

                titleLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);

                if (contentListOutputArray.size()> 0) {

                    gridView.setVisibility(View.VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);

                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);


                for (int i=0;i<contentListOutputArray.size();i++) {


                    videoImageStr = contentListOutputArray.get(i).getPoster_url();
                    if (contentListOutputArray.get(i).getIs_episode().matches("1")) {
                        videoName = contentListOutputArray.get(i).getEpisode_title();
                    }else{
                        videoName = contentListOutputArray.get(i).getName();

                    }
                    videoTypeIdStr = contentListOutputArray.get(i).getContent_types_id();
                    videoGenreStr = contentListOutputArray.get(i).getGenre();
                    videoPermalinkStr = contentListOutputArray.get(i).getPermalink();
                    isEpisodeStr = contentListOutputArray.get(i).getIs_episode();
                    isConverted = contentListOutputArray.get(i).getIs_converted();
                    isPPV = contentListOutputArray.get(i).getIs_ppv();
                    isAPV = contentListOutputArray.get(i).getIs_advance();
                    itemData.add(new GridItem(videoImageStr, videoName, "", videoTypeIdStr, videoGenreStr, "", videoPermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV,""));
                }

                    videoImageStrToHeight = videoImageStr;


                    AsynLOADUI loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);

                }else {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.GONE);

                    footerView.setVisibility(View.GONE);

                }
            }else {

                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                titleLayout.setVisibility(View.GONE);

                footerView.setVisibility(View.GONE);

            }
        }else {

            noDataLayout.setVisibility(View.VISIBLE);
            noInternetConnectionLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            titleLayout.setVisibility(View.GONE);

            footerView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {

        progressBarHandler = new ProgressBarHandler(CustomSearchActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

        if (progressBarHandler.isShowing()) {
            progressBarHandler.hide();
            progressBarHandler = null;

        }
        if (status > 0 && status == 200) {
            ShowLanguagePopup();
        }
    }



    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(CustomSearchActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {

        if (status == null) {
            Toast.makeText(CustomSearchActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(CustomSearchActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION,DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(CustomSearchActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(CustomSearchActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(CustomSearchActivity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(CustomSearchActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS,DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(CustomSearchActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(CustomSearchActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

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
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_large_horizontal) : (int) getResources().getInteger(R.integer.configuration_large_horizontal));
                    } else {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_large_vertical) : (int) getResources().getInteger(R.integer.configuration_large_vertical));
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {

                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_normal_horizontal) : (int) getResources().getInteger(R.integer.configuration_normal_horizontal));
                    } else {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_normal_vertical) : (int) getResources().getInteger(R.integer.configuration_normal_vertical));
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_small_horizontal) : (int) getResources().getInteger(R.integer.configuration_small_horizontal));


                } else {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_xlarge_horizontal) : (int) getResources().getInteger(R.integer.configuration_xlarge_horizontal));
                    } else {
                        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) getResources().getInteger(R.integer.configuration_xlarge_vertical) : (int) getResources().getInteger(R.integer.configuration_xlarge_vertical));
                    }

                }
                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.videos_grid_layout, itemData);

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }

              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(ViewMoreActivity.this, R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(ViewMoreActivity.this, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = gridView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(ViewMoreActivity.this, R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(ViewMoreActivity.this, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(CustomSearchActivity.this, R.layout.videos_grid_layout, itemData);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = gridView.onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }
   /* @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            gridView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }*/

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }*/
  /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();

        //Parcelable listState = gridView.onSaveInstanceState();
        //mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, gridView.getLayoutManager().onSaveInstanceState());
    }*/


    //load video urls as per resolution

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



 /*   void showRegistrationDialog(){
        registerDialogListener = new RegisterDialog.NewUserButtonOnClickListener() {
            @Override
            public void onNewUserButtonClick(EditText registerFullNameStr, EditText registerEmailStr, EditText registerPasswordStr) {
                regEmailIdEditText = registerEmailStr;
                regPasswordEditText = registerPasswordStr;
                regFullNameEditText = registerFullNameStr;
                registerOfRegisteredDialogButtonClicked();
            }

            @Override
            public void onCheckboxTextClick() {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://live.levidia.com/page/terms-privacy-policy"));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(browserIntent);
            }
            @Override
            public void onAlreadyLoginButtonClick() {
                if (registerDialog.isShowing()){
                    registerDialog.cancel();
                    registerDialog.cancel();
                }
                showLoginDialog();
            }


        };

        registerDialog = new RegisterDialog(SearchActivity.this, registerDialogListener);
        registerDialog.show();


    }
    public void registerOfRegisteredDialogButtonClicked() {

        String regNameStr =  regFullNameEditText.getText().toString().trim();
        String regEmailStr =  regEmailIdEditText.getText().toString();
        String regPasswordStr = regPasswordEditText.getText().toString();

        if (regNameStr.matches("") || (regEmailStr.matches("")) || (regPasswordStr.matches(""))) {
            Toast.makeText(SearchActivity.this,getResources().getString(R.string.empty_fields_alert), Toast.LENGTH_LONG).show();
        }
        else{
            boolean isValidEmail = Util.isValidMail(regEmailStr);
            if( (regEmailStr!=null && regEmailStr.length() > 0) && (isValidEmail == false)){
                Toast.makeText(SearchActivity.this,getResources().getString(R.string.invalid_email_address_msg), Toast.LENGTH_LONG).show();
            }
            else{
                boolean isNetwork = Util.checkNetwork(SearchActivity.this);
                if (isNetwork==false){
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.no_internet_connection_str), Toast.LENGTH_LONG).show();

                }
                else {
                   *//* AsynRegistrationDetails asyncReg = new AsynRegistrationDetails();
                    asyncReg.executeOnExecutor(threadPoolExecutor);*//*
                }

            }
        }

    }*/
/*
    //Verify the login
    private class AsynLogInDetails extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        int statusCode;
        String loggedInIdStr;
        String responseStr;
        String isSubscribedStr;
        String loginHistoryIdStr;

        JSONObject myJson = null;
        String loginEmailIdStr = loginEmailIdEditText.getText().toString();
        String loginPasswordStr = loginPasswordEditText.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {


            ArrayList<NameValuePair> cred = new ArrayList<NameValuePair>();
            cred.add(new BasicNameValuePair("email", loginEmailIdStr));
            cred.add(new BasicNameValuePair("password", loginPasswordStr));
            cred.add(new BasicNameValuePair("authToken", Util.authTokenStr.trim()));

            String urlRouteList = Util.rootUrl().trim()+Util.loginUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("password", loginPasswordStr);
                httppost.addHeader("email", loginEmailIdStr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(cred, "UTF-8"));
                }

                catch (UnsupportedEncodingException e) {
                    statusCode = 0;
                    e.printStackTrace();
                }

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusCode = 0;
                            //Crouton.showText(ShowWithEpisodesListActivity.this, "Slow Internet Connection", Style.INFO);
                            Toast.makeText(SearchActivity.this, getResources().getString(R.string.slow_internet_connection_str), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    statusCode = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));

                    //userIdStr = myJson.optString("status");
                    loggedInIdStr = myJson.optString("id");
                    isSubscribedStr = myJson.optString("isSubscribed");
                    loginHistoryIdStr = myJson.optString("login_history_id");


                }

            }
            catch (Exception e) {
                statusCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){
                try {
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                } catch (IllegalArgumentException ex) {
                    statusCode = 0;

                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                dlgAlert.setMessage(getResources().getString(R.string.empty_records_alert));
                dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (statusCode > 0) {

                SharedPreferences.Editor editor = pref.edit();

                if (statusCode == 200){
                    String displayNameStr = myJson.optString("display_name");
                    String emailFromApiStr = myJson.optString("email");
                    String profileImageStr = myJson.optString("profile_image");

                    editor.putString("PREFS_LOGGEDIN_KEY","1");
                    editor.putString("PREFS_LOGGEDIN_ID_KEY",loggedInIdStr);
                    editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY","");
                    editor.putString("PREFS_LOGIN_EMAIL_ID_KEY", emailFromApiStr);
                    editor.putString("PREFS_LOGIN_DISPLAY_NAME_KEY", displayNameStr);
                    editor.putString("PREFS_LOGIN_PROFILE_IMAGE_KEY", profileImageStr);
                    editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY",loginPasswordEditText.getText().toString().trim());
                    editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY",isSubscribedStr);
                    editor.putString("PREFS_LOGIN_HISTORYID_KEY",loginHistoryIdStr);


                    Date todayDate = new Date();
                    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
                    editor.putString("date", todayStr.trim());
                    editor.commit();

                    if (loginDialog!=null && loginDialog.isShowing()) {
                        loginDialog.dismiss();
                        loginDialog.cancel();

                    }

                    if (Util.checkNetwork(SearchActivity.this) == true) {
                        if (videoPDialog!=null && videoPDialog.isShowing())
                            videoPDialog.dismiss();
                            AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);


                    } else {
                        Toast.makeText(SearchActivity.this, getResources().getString(R.string.no_internet_connection_str), Toast.LENGTH_LONG).show();
                    }

                }else{
                    try {
                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        statusCode = 0;

                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                    dlgAlert.setMessage(getResources().getString(R.string.email_password_invalid_str));
                    dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }
            }else{
                try {
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                } catch (IllegalArgumentException ex) {

                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                dlgAlert.setMessage(getResources().getString(R.string.empty_records_alert));
                dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

        }

        @Override
        protected void onPreExecute() {
         *//*   pDialog = new ProgressDialog(context);
            pDialog.setMessage(getResources().getString(R.string.loading_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
*//*
            pDialog = new ProgressDialog(SearchActivity.this,R.style.MyTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));

            if (pDialog!=null && !pDialog.isShowing()) {
                pDialog.show();
            }
        }


    }*/

    //Registration
   /* private class AsynRegistrationDetails extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        int status;
        String responseStr;
        String registrationIdStr;
        String isSubscribedStr;
        String loginHistoryIdStr;
        JSONObject myJson = null;
        String regFullNameStr = regFullNameEditText.getText().toString().trim();
        String regEmailStr = regEmailIdEditText.getText().toString();
        String regPasswordStr = regPasswordEditText.getText().toString();
        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim()+Util.registrationUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("name", regFullNameStr);
                httppost.addHeader("email", regEmailStr);
                httppost.addHeader("password", regPasswordStr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            status = 0;
                            Toast.makeText(SearchActivity.this, getResources().getString(R.string.slow_internet_connection_str), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    status = 0;
                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    registrationIdStr = myJson.optString("id");
                    isSubscribedStr = myJson.optString("isSubscribed");
                    loginHistoryIdStr = myJson.optString("login_history_id");

                }

            }
            catch (Exception e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (pDialog!=null && pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                status = 0;

            }
            if(responseStr == null){
                status = 0;

            }
            if (status == 0) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                dlgAlert.setMessage(getResources().getString(R.string.error_in_registration_str));
                dlgAlert.setTitle(getResources().getString(R.string.failure_str));
                dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }
            if (status > 0) {

                if (status == 422) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                    dlgAlert.setMessage(getResources().getString(R.string.email_exists_for_studio_str));
                    dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                }

                else if (status == 200){

                    String displayNameStr = myJson.optString("display_name");
                    String emailFromApiStr = myJson.optString("email");
                    String profileImageStr = myJson.optString("profile_image");

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("PREFS_LOGGEDIN_KEY","1");
                    editor.putString("PREFS_LOGGEDIN_ID_KEY", registrationIdStr);
                    editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY", regPasswordEditText.getText().toString().trim());
                    editor.putString("PREFS_LOGIN_EMAIL_ID_KEY", emailFromApiStr);
                    editor.putString("PREFS_LOGIN_DISPLAY_NAME_KEY", displayNameStr);
                    editor.putString("PREFS_LOGIN_PROFILE_IMAGE_KEY", profileImageStr);
                    editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY",isSubscribedStr);
                    editor.putString("PREFS_LOGIN_HISTORYID_KEY",loginHistoryIdStr);

                    Date todayDate = new Date();
                    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
                    editor.putString("date", todayStr.trim());
                    editor.commit();



                    if (registerDialog.isShowing()) {
                        registerDialog.dismiss();
                        registerDialog.cancel();

                    }

                    if (Util.checkNetwork(SearchActivity.this) == true) {
                        if (pDialog!=null && pDialog.isShowing())
                            pDialog.dismiss();
                        // MUVIlaxmi
                        if (itemToPlay.ge == null) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                            dlgAlert.setMessage(getResources().getString(R.string.no_video_available_str));
                            dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                            dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            dlgAlert.create().show();
                        }
                        else if (videoUrlStr.matches("") || videoUrlStr.equalsIgnoreCase(getResources().getString(R.string.no_data_str))) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                            dlgAlert.setMessage(getResources().getString(R.string.no_video_available_str));
                            dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                            dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            dlgAlert.create().show();
                        } else{
                            final Intent playVideoIntent = new Intent(SearchActivity.this, VideoTrailerPlayerActivity.class);
                            playVideoIntent.putExtra("url", videoUrlStr);
                           *//* String videoPreview;
                            if(bannerImageId.trim().matches(getResources().getString(R.string.no_data_str))){
                                if(posterImageId.trim().matches(getResources().getString(R.string.no_data_str))) {
                                    videoPreview = getResources().getString(R.string.no_data_str);
                                }else{
                                    videoPreview = posterImageId;
                                }

                            }else{
                                videoPreview = bannerImageId;
                            }

                            playVideoIntent.putExtra("videoPreview", videoPreview);*//*
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });


                        }
                    } else {
                        Toast.makeText(SearchActivity.this, getResources().getString(R.string.no_internet_connection_str), Toast.LENGTH_LONG).show();
                    }




                }
            }

        }

        @Override
        protected void onPreExecute() {
          *//*  pDialog = new ProgressDialog(MovieDetailsActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);*//*

            pDialog = new ProgressDialog(SearchActivity.this,R.style.MyTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));

         *//*   pDialog = new ProgressDialog(MovieDetailsActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);*//*

            if (pDialog!=null && !pDialog.isShowing()) {
                pDialog.show();
            }
        }


    }
*/
/*
    public void forgotPasswordButtonClicked() {

        String loginEmailStr =  loginEmailIdEditText.getText().toString().trim();
        if (loginEmailStr.matches("")) {
            Toast.makeText(SearchActivity.this, getResources().getString(R.string.blank_email_address_msg), Toast.LENGTH_LONG).show();

        }else{
            boolean isValidEmail = Util.isValidMail(loginEmailStr);
            if( (loginEmailStr!=null && loginEmailStr.length() > 0) && (isValidEmail == false)){
                Toast.makeText(SearchActivity.this, getResources().getString(R.string.invalid_email_address_msg), Toast.LENGTH_LONG).show();

            }
            else{
                boolean isNetwork = Util.checkNetwork(SearchActivity.this);
                if (isNetwork==false){
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                    dlgAlert.setMessage(getResources().getString(R.string.no_internet_no_data));
                    dlgAlert.setTitle(getResources().getString(R.string.sorry_str));
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                }
                else {
                    AsynForgotPasswordDetails asyncPasswordForgot=new AsynForgotPasswordDetails();
                    asyncPasswordForgot.executeOnExecutor(threadPoolExecutor);
                }

            }
        }

    }
    private class AsynForgotPasswordDetails extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        String loginEmailIdStr = loginEmailIdEditText.getText().toString();
        int responseCode;
        String responseStr;

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> cred = new ArrayList<NameValuePair>();
            cred.add(new BasicNameValuePair("email", loginEmailIdStr));
            cred.add(new BasicNameValuePair("authToken", Util.authTokenStr.trim()));

            String urlRouteList = Util.rootUrl().trim()+Util.forgotpasswordUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("email", loginEmailIdStr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(cred, "UTF-8"));
                }

                catch (UnsupportedEncodingException e) {
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                    responseCode = 0;
                    e.printStackTrace();
                }
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing())
                                pDialog.dismiss();
                            responseCode = 0;
                            validationIndicatorTextView.setVisibility(View.INVISIBLE);
                            Toast.makeText(SearchActivity.this, getResources().getString(R.string.slow_internet_connection_str), Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                    responseCode = 0;
                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    responseCode = Integer.parseInt(myJson.optString("code"));
                }

            }
            catch (Exception e) {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                responseCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                validationIndicatorTextView.setVisibility(View.VISIBLE);
                validationIndicatorTextView.setText(getResources().getString(R.string.email_didnot_exists_str));
            }
            if(responseStr == null){
                validationIndicatorTextView.setVisibility(View.VISIBLE);
                validationIndicatorTextView.setText(getResources().getString(R.string.email_didnot_exists_str));
            }
            if (responseCode == 0) {
                validationIndicatorTextView.setVisibility(View.VISIBLE);
                validationIndicatorTextView.setText(getResources().getString(R.string.email_didnot_exists_str));
            }
            if (responseCode > 0) {
                if (responseCode == 200) {
                    if (forgotPasswordDialog.isShowing()) {
                        forgotPasswordDialog.dismiss();
                        forgotPasswordDialog.cancel();
                    }
                    validationIndicatorTextView.setVisibility(View.INVISIBLE);

                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                    dlgAlert.setMessage(getResources().getString(R.string.check_email_to_reset_password));
                    dlgAlert.setTitle(getResources().getString(R.string.success_str));
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(getResources().getString(R.string.ok_str),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                }
                else {
                    validationIndicatorTextView.setVisibility(View.VISIBLE);
                    validationIndicatorTextView.setText(getResources().getString(R.string.email_didnot_exists_str));
                }
            }

        }

        @Override
        protected void onPreExecute() {
         *//*   pDialog = new ProgressDialog(context);
            pDialog.setMessage(getResources().getString(R.string.loading_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);*//*

            pDialog = new ProgressDialog(SearchActivity.this,R.style.MyTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));

            if (pDialog!=null && !pDialog.isShowing()) {
                pDialog.show();
            }
        }
    }*/
/*
        public void clickItem(GridItem item) {


            String moviePermalink = item.getPermalink();
            String movieTypeId = item.getVideoTypeId();
            // if searched

            // for tv shows navigate to episodes
            if ((movieTypeId.equalsIgnoreCase("3"))) {
                if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                } else {

                    final Intent detailsIntent = new Intent(SearchActivity.this, ShowWithEpisodesActivity.class);
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
                final Intent detailsIntent = new Intent(SearchActivity.this, MovieDetailsActivity.class);

                if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
*/


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        episodeListOptionMenuHandler.createOptionMenu(menu,preferenceManager,languagePreference);

        MenuItem search_menu;
        search_menu = menu.findItem(R.id.action_search);
        search_menu.setVisible(false);
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

    private MenuItem mediaRouteMenuItem;
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
                final Intent searchIntent = new Intent(CustomSearchActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(CustomSearchActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(CustomSearchActivity.this, RegisterActivity.class);
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
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(CustomSearchActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(CustomSearchActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CustomSearchActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(CustomSearchActivity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(CustomSearchActivity.this, Util.YES, Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(Util.getTextofLanguage(CustomSearchActivity.this, Util.SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, CustomSearchActivity.this, CustomSearchActivity.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(Util.getTextofLanguage(CustomSearchActivity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomSearchActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(Util.getTextofLanguage(CustomSearchActivity.this, Util.APP_SELECT_LANGUAGE, Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(Util.getTextofLanguage(CustomSearchActivity.this, Util.BUTTON_APPLY, Util.DEFAULT_BUTTON_APPLY));

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
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(CustomSearchActivity.this, recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                Util.setLanguageSharedPrefernce(CustomSearchActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
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

                    GetTranslateLanguageAsync getTranslateLanguageAsync = new GetTranslateLanguageAsync(languageListInputModel, CustomSearchActivity.this, CustomSearchActivity.this);
                    getTranslateLanguageAsync.executeOnExecutor(threadPoolExecutor);

                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.setLanguageSharedPrefernce(CustomSearchActivity.this, Util.SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

    }


}

