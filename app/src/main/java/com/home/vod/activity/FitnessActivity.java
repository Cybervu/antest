package com.home.vod.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetCategoryListAsynTask;
import com.home.apisdk.apiController.GetFeatureContentAsynTask;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.CategoryListInput;
import com.home.apisdk.apiModel.CategoryListOutputModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.FeatureContentInputModel;
import com.home.apisdk.apiModel.FeatureContentOutputModel;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.R;
import com.home.vod.Episode_Programme_Handler;
import com.home.vod.Single_Part_Programme_Handler;
import com.home.vod.activity.FilterActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.YogaPlayerActivity;
import com.home.vod.adapter.FitnessActivityAdapter;
import com.home.vod.adapter.GenreFilterAdapter;
import com.home.vod.adapter.VideoFilterAdapter;
import com.home.vod.adapter.YogaFilterAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.GridItem;
import com.home.vod.model.ListItem;
import com.home.vod.model.YogaItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static android.app.Activity.RESULT_OK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.vod.R.id.view;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_RELEASE_DATE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_SEARCH_PLACEHOLDER;
import static com.home.vod.preferences.LanguagePreference.FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.SORT_BY;
import static com.home.vod.preferences.LanguagePreference.SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.SORT_RELEASE_DATE;
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
public class FitnessActivity extends AppCompatActivity implements GetCategoryListAsynTask.GetCategoryListListener,GetFeatureContentAsynTask.GetFeatureContentListener {

    public static boolean clearClicked = false;
    private int isFavorite = 0;
    Context adaptorContext;
    private String content_types_id;
    private String content_id = "";
    private String content_stream_id = "";
    private boolean backfromactivity = false;
    private boolean index_clicked = false;

    String titleStr;



    /***************
     * chromecast
     **********************/
    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }



    private VideoView mVideoView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mStartText;
    private TextView mEndText;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private ProgressBar mLoading;
    private View mControllers;
    private View mContainer;
    private ImageView mCoverArt;
    private Timer mSeekbarTimer;
    private Timer mControllersTimer;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final Handler mHandler = new Handler();
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;
    private boolean mControllersVisible;
    private int mDuration;
    private TextView mAuthorView;
    private ImageButton mPlayCircle;
    String loggedInStr;

    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;

    ScrollView scroll_view;
    View insideView;
    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            //invalidateOptionsMenu();
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
    /***************
     * chromecast
     **********************/


    AsynLOADUI loadUI;
//    AsynLoadVideos asynLoadVideos;

    ArrayList<String> url_maps;
    private ProgressBarHandler videoPDialog;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    private ProgressBarHandler gDialog;
    private SliderLayout mDemoSlider;
    String videoImageStrToHeight;
    int videoHeight = 185;
    int videoWidth = 256;
    PreferenceManager preferenceManager;
    LanguagePreference languagePreference;

    private ProgressBarHandler pDialog;
    private static int firstVisibleInListview;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    //for no internet

    private RelativeLayout noInternetConnectionLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;
    Map<String, Integer> mapIndex;
    ArrayList<TextView> listOfTextview = new ArrayList<TextView>();
    LinearLayout indexLayout;
    TextView textView;
    EditText searchEdittext;
    ImageView searchIcon,clearText;
    boolean click = true;
    String searchText;
    List<YogaItem> results = new ArrayList<YogaItem>();

    //firsttime load
    boolean firstTime = false;


    //no data

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

    //Adapter for Listview
    private FitnessActivityAdapter fitnessAdapter;

    //Model for GridView
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    ArrayList<GridItem> searchedItemData = new ArrayList<GridItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl;

    // UI
    private ListView listView;
    int ScrollState = 0;

    ImageView img;
    //data to load videourl
    private String movieUniqueId;
    private String movieStreamUniqueId;
    String videoUrlStr;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;

    RelativeLayout filterView;
    public static ArrayList<String> genreArray;

    public static String filterOrderByStr = "";
    GenreFilterAdapter genreAdapter;
    MenuItem filterMenuItem;
    int prevPosition = 5;
    String filterPermalink = "";
    int scrolledPosition = 0;
    boolean scrolling;
    boolean isSearched = false;
    RecyclerView genreListData;

    YogaItem temp;
    String sectionId="";
    Toolbar mActionBarToolbar;
    String email, id;

    RelativeLayout footerView;


    View header;
    public static boolean isLoading = false;

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        if (mDemoSlider != null) {
            mDemoSlider.stopAutoCycle();
        }
        super.onStop();
    }
    TextView categoryTitle;
    TextView index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.fitness_activity);
        episodeListOptionMenuHandler = new EpisodeListOptionMenuHandler(this);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setTitle("");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //for search for each activity
//        setHasOptionsMenu(true);
        filterOrderByStr = "";
        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {

                    showIntroductoryOverlay();
                }
            }
        };
        mCastContext = CastContext.getSharedInstance(FitnessActivity.this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(FitnessActivity.this, savedInstanceState);

        if (getIntent().getStringExtra("SectionId") != null) {
            sectionId = getIntent().getStringExtra("SectionId");

        }
        // int startPosition = getInt("startPosition", 0);
        // mVideoView.setVideoURI(Uri.parse(item.getContentId()));


        setupCastListener();
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        genreListData = (RecyclerView) findViewById(R.id.demoListView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(FitnessActivity.this, LinearLayoutManager.VERTICAL, false);
        genreListData.setLayoutManager(linearLayout);
        genreListData.setItemAnimator(new DefaultItemAnimator());
        preferenceManager = PreferenceManager.getPreferenceManager(FitnessActivity.this);
        languagePreference = LanguagePreference.getLanguagePreference(FitnessActivity.this);

        posterUrl = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

        listView = (ListView) findViewById(R.id.fitness_list);
        titleStr = getIntent().getStringExtra("sectionName");
        footerView = (RelativeLayout) findViewById(R.id.loadingPanel);
        indexLayout = (LinearLayout) findViewById(R.id.side_index);
        searchEdittext = (EditText) findViewById(R.id.search_edittext);
        searchEdittext.addTextChangedListener(new EditTextListener());
        searchIcon = (ImageView) findViewById(R.id.search_icon);
        clearText = (ImageView) findViewById(R.id.clear_text);
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        insideView = (View) findViewById(R.id.side_index);


        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));
        searchEdittext.setHint(languagePreference.getTextofLanguage(TEXT_SEARCH_PLACEHOLDER, DEFAULT_TEXT_SEARCH_PLACEHOLDER));


        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(fitnessAdapter);

        categoryTitle = (TextView) findViewById(R.id.sectionTitle);
        index = (TextView) findViewById(R.id.index);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText(titleStr);
        //Detect Network Connection

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchEdittext.getText().toString().equalsIgnoreCase("")){
                    searchEdittext.setVisibility(View.GONE);
                    clearText.setVisibility(View.GONE);
                }else{
                    searchEdittext.setText("");
                }

            }
        });


        searchIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (click) {
                    searchEdittext.setVisibility(View.VISIBLE);
                    clearText.setVisibility(View.VISIBLE);
                    searchEdittext.requestFocus();
                    click = false;
                }
                else {
                    InputMethodManager inputManager = (InputMethodManager)FitnessActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(FitnessActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    searchEdittext.setVisibility(View.GONE);
                    clearText.setVisibility(View.GONE);
                    click = true;
                }



            }
        });


        if (!NetworkStatus.getInstance().isConnected(FitnessActivity.this)) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }
        resetData();


        FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
        featureContentInputModel.setAuthToken(authTokenStr);
        featureContentInputModel.setSection_id(sectionId.trim());
        featureContentInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        GetFeatureContentAsynTask asyncLoadVideos = new GetFeatureContentAsynTask(featureContentInputModel,FitnessActivity.this,FitnessActivity.this);
        asyncLoadVideos.executeOnExecutor(threadPoolExecutor);



     /*   asynLoadVideos = new AsynLoadVideos();
        asynLoadVideos.execute();

        CategoryListInput categoryListInput = new CategoryListInput();
        categoryListInput.setAuthToken(authTokenStr);
        GetCategoryListAsynTask getCategoryListAsynTask = new GetCategoryListAsynTask(categoryListInput, FitnessActivity.this, FitnessActivity.this);
        getCategoryListAsynTask.execute();*/

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Log.v("Muvi1", "onScrollStateChanged = "+scrollState);
                ScrollState = scrollState;

                if(ScrollState == 1 || ScrollState == 2){
                    index.setVisibility(View.VISIBLE);
                }else {
                    index.setVisibility(View.GONE);
                }

                if (listView.getLastVisiblePosition() >= itemsInServer - 1) {
                    footerView.setVisibility(View.GONE);
                    return;

                }

                if (view.getId() == listView.getId()) {
                    final int currentFirstVisibleItem = listView.getFirstVisiblePosition();

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


                try{

                    Log.v("Muvi1", "on scroll data info = "+itemData.get(firstVisibleItem).getTitle());

                    String title = "";
                    if(listView.getLastVisiblePosition() == itemData.size()-1){
                        title = itemData.get(itemData.size()-1).getTitle().trim();
                    }else{
                        title = itemData.get(firstVisibleItem).getTitle().trim();
                    }

                    title = (title.replaceAll("[0-9]","")).trim();
                    String INDEX = title.substring(0,1);

                    Log.v("Muvi1", "show Index = "+INDEX);
                    index.setText(INDEX.toUpperCase());



                    if(!index_clicked){
                        for (int j = 0;j<listOfTextview.size();j++) {
                            if ((listOfTextview.get(j).getTag().toString().toLowerCase().trim()).equals(INDEX.toLowerCase().trim())){
                                listOfTextview.get(j).setTextColor(getResources().getColor(R.color.colorAccent));

                                // scroll_view.scrollTo(j, (int)insideView.getY());
                                insideView.getParent().requestChildFocus(insideView,insideView);
//                                listOfTextview.get(j).requestFocus();

                            }
                            else {
                                if(mapIndex.get(listOfTextview.get(j).getText()) !=null){
                                    listOfTextview.get(j).setTextColor(getResources().getColor(R.color.sideIndex_color));
                                }else{
                                    listOfTextview.get(j).setTextColor(getResources().getColor(R.color.style_circular_color));
                                }
                            }
                        }
                    }else{
                        index_clicked = false;
                    }






                }catch (Exception e){}

                if (scrolling == true && mIsScrollingUp == false) {




                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                        listSize = itemData.size();
                        if (listView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;
                        if (NetworkStatus.getInstance().isConnected(FitnessActivity.this)) {



                                FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
                                featureContentInputModel.setAuthToken(authTokenStr);
                                featureContentInputModel.setSection_id(sectionId.trim());
                                featureContentInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                GetFeatureContentAsynTask asyncLoadVideos = new GetFeatureContentAsynTask(featureContentInputModel,FitnessActivity.this,FitnessActivity.this);
                                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);


                            scrolling = false;

                        }

                    }

                }

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position<0)
                    return;


                GridItem item = itemData.get(position);
                String posterUrl = item.getImage();
                String movieName = item.getTitle();
                String movieGenre = item.getMovieGenre();
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();

                if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FitnessActivity.this);
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


                    final Intent movieDetailsIntent = new Intent(FitnessActivity.this, YogaPlayerActivity.class);
                    movieDetailsIntent.putExtra(PERMALINK_INTENT_KEY, item.getPermalink());
                    movieDetailsIntent.putExtra("CONTENT_ID", content_id);
                    movieDetailsIntent.putExtra("CONTENT_STREAM_ID", content_stream_id);
                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   runOnUiThread(new Runnable() {
                        public void run() {
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(movieDetailsIntent);
                        }
                    });
                   /* if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {

                        new Single_Part_Programme_Handler(FitnessActivity.this).handleIntent(PERMALINK_INTENT_KEY,moviePermalink);

                    } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                        new Episode_Programme_Handler(FitnessActivity.this).handleIntent(PERMALINK_INTENT_KEY,moviePermalink);
                    }*/
                }

            }
        });


        filterView = (RelativeLayout) findViewById(R.id.filterBg);

        filterView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                filterView.setVisibility(View.GONE);
                listView.setEnabled(true);

                if ((filterOrderByStr != null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray != null && genreArray.size() > 0)) {
                    firstTime = true;


                    offset = 1;
                    scrolledPosition = 0;
                    listSize = 0;
                    if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        limit = 30;
                    } else {
                        limit = 15;
                    }
                    itemsInServer = 0;
                    scrolling = false;
                    if (itemData != null && itemData.size() > 0) {
                        itemData.clear();
                    }
                    isSearched = false;

                    if (!NetworkStatus.getInstance().isConnected(FitnessActivity.this)) {
                        noInternetConnectionLayout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        if (filterMenuItem != null) {

                            filterMenuItem.setVisible(false);
                        }


                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                        if (videoPDialog != null && videoPDialog.isShowing()) {
                            videoPDialog.hide();
                            videoPDialog = null;
                        }

                        if (loadUI != null) {
                            loadUI.cancel(true);
                        }
                        AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                        asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

                    }
                }
                return false;
            }
        });


        final ArrayList<ListItem> mdata = new ArrayList<ListItem>();
        genreArray = new ArrayList<String>();

        String genreString = preferenceManager.getGenreArrayFromPref();
        String genreValuesString = preferenceManager.getGenreValuesArrayFromPref();
        final String[] genreTempArr = genreString.split(",");
        String[] genreValuesTempArr = genreValuesString.split(",");


        for (int i = 0; i < genreTempArr.length; i++) {
            mdata.add(new ListItem(genreTempArr[i], genreValuesTempArr[i]));
            if (i == 0) {
                mdata.set(0, new ListItem(languagePreference.getTextofLanguage(FILTER_BY, DEFAULT_FILTER_BY), genreValuesTempArr[i]));
            }
            if (i == genreTempArr.length - 5) {
                mdata.set(i, new ListItem(languagePreference.getTextofLanguage(SORT_BY, DEFAULT_SORT_BY), genreValuesTempArr[i]));
            }
            if (i == genreTempArr.length - 4) {
                mdata.set(i, new ListItem(languagePreference.getTextofLanguage(SORT_LAST_UPLOADED, DEFAULT_SORT_LAST_UPLOADED), genreValuesTempArr[i]));
            }
            if (i == genreTempArr.length - 3) {
                mdata.set(i, new ListItem(languagePreference.getTextofLanguage(SORT_RELEASE_DATE, DEFAULT_SORT_RELEASE_DATE), genreValuesTempArr[i]));
            }
            if (i == genreTempArr.length - 2) {
                mdata.set(i, new ListItem(languagePreference.getTextofLanguage(SORT_ALPHA_A_Z, DEFAULT_SORT_ALPHA_A_Z), genreValuesTempArr[i]));
            }
            if (i == genreTempArr.length - 1) {
                mdata.set(i, new ListItem(languagePreference.getTextofLanguage(SORT_ALPHA_Z_A, DEFAULT_SORT_ALPHA_Z_A), genreValuesTempArr[i]));
            }
        }


        genreAdapter = new GenreFilterAdapter(mdata, FitnessActivity.this);
        genreListData.setAdapter(genreAdapter);
        if (mdata.size() > 0) {
            prevPosition = mdata.size() - 4;
        }
        mdata.get(prevPosition).setSelected(true);
        genreListData.addOnItemTouchListener(new RecyclerTouchListener(FitnessActivity.this, genreListData, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (position >= 1 && position <= (genreTempArr.length - 6)) {
                    if (mdata.get(position).isSelected() == true) {
                        mdata.get(position).setSelected(false);

                        for (int i = 0; i < genreArray.size(); i++) {
                            if (genreArray.contains(mdata.get(position).getSectionType())) {
                                genreArray.remove(mdata.get(position).getSectionType());
                            }
                        }


                    } else {
                        genreArray.add(mdata.get(position).getSectionType());
                        mdata.get(position).setSelected(true);
                    }
                }

                if (position >= (genreTempArr.length - 4)) {
                    mdata.get(position).setSelected(true);
                    filterOrderByStr = mdata.get(position).getSectionType();
                    if (prevPosition != position) {
                        mdata.get(prevPosition).setSelected(false);
                        prevPosition = position;

                    }

                }

                genreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



    }

    private class AsynLoadFilterVideos extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String movieGenreStr = "";
        String movieName = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String movieImageStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String moviePermalinkStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoTypeIdStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;



        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = APIUrlConstant.getGetContentListUrl();
            if (genreArray != null && genreArray.size() > 0) {
                String[] mStringArray = new String[genreArray.size()];
                mStringArray = genreArray.toArray(mStringArray);
                for (int i = 0; i < mStringArray.length; i++) {
                    if (mStringArray.length <= 1) {
                        urlRouteList = (urlRouteList + "?genre[]=" + mStringArray[i].trim()).replace(" ", "%20");

                    } else {
                        if (i == 0) {
                            urlRouteList = (urlRouteList + "?genre[]=" + mStringArray[i].trim()).replace(" ", "%20");
                        } else {
                            urlRouteList = (urlRouteList + "&genre[]=" + mStringArray[i].trim()).replace(" ", "%20");

                        }

                    }
                }
            }

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("authToken", authTokenStr.trim());
                httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

                if (filterPermalink.trim() != null && !filterPermalink.trim().equalsIgnoreCase("") && !filterPermalink.trim().matches("")) {
                    httppost.addHeader("permalink", filterPermalink.trim());

                }

                if (filterOrderByStr.trim() != null && !filterOrderByStr.trim().equalsIgnoreCase("") && !filterOrderByStr.trim().matches("")) {
                    httppost.addHeader("orderby", filterOrderByStr.trim());

                }

                String countryCodeStr = preferenceManager.getCountryCodeFromPref();

                if (countryCodeStr != null) {

                    httppost.addHeader("country", countryCodeStr);
                } else {
                    httppost.addHeader("country", "IN");

                }


                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    FitnessActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (itemData != null) {
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);


                            } else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);


                            }

                            footerView.setVisibility(View.GONE);
                            Util.showToast(FitnessActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, DEFAULT_SLOW_INTERNET_CONNECTION));

                            //  Toast.makeText(context,languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    FitnessActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            footerView.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);

                        }
                    });
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);
                }

                if (status > 0) {
                    if (status == 200) {


                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                                    movieGenreStr = jsonChildNode.getString("genre");

                                }
                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                    movieName = jsonChildNode.getString("name");

                                }
                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster_url");

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }
                                //videoTypeIdStr = "1";

                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));

                                }
                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));

                                }
                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));

                                }
                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }
                                if ((jsonChildNode.has("muvi_uniq_id")) && jsonChildNode.getString("muvi_uniq_id").trim() != null && !jsonChildNode.getString("muvi_uniq_id").trim().isEmpty() && !jsonChildNode.getString("muvi_uniq_id").trim().equals("null") && !jsonChildNode.getString("muvi_uniq_id").trim().matches("")) {
                                    movieUniqueId = jsonChildNode.getString("muvi_uniq_id");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    content_types_id = jsonChildNode.getString("content_types_id");

                                }
                                if ((jsonChildNode.has("is_favorite")) && jsonChildNode.getString("is_favorite").trim() != null && !jsonChildNode.getString("is_favorite").trim().isEmpty() && !jsonChildNode.getString("is_favorite").trim().equals("null") && !jsonChildNode.getString("is_favorite").trim().matches("")) {
                                    isFavorite = Integer.parseInt(jsonChildNode.getString("is_favorite"));
                                }
                                if ((jsonChildNode.has("movie_id")) && jsonChildNode.getString("movie_id").trim() != null && !jsonChildNode.getString("movie_id").trim().isEmpty() && !jsonChildNode.getString("movie_id").trim().equals("null") && !jsonChildNode.getString("movie_id").trim().matches("")) {
                                    content_id = jsonChildNode.getString("movie_id");
                                }
                                if ((jsonChildNode.has("movie_stream_id")) && jsonChildNode.getString("movie_stream_id").trim() != null && !jsonChildNode.getString("movie_stream_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_id").trim().matches("")) {
                                    content_stream_id = jsonChildNode.getString("movie_stream_id");
                                }
                                Log.v("Nihar_sdk", "" + movieUniqueId);
                                //itemData.add(new YogaItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", isConverted, isPPV, isAPV, "", content_types_id, isFavorite, content_id, content_stream_id, false));
                            } catch (Exception e) {
                                FitnessActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataLayout.setVisibility(View.VISIBLE);
                                        noInternetConnectionLayout.setVisibility(View.GONE);
                                        listView.setVisibility(View.GONE);
                                        footerView.setVisibility(View.GONE);

                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";
                        FitnessActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                footerView.setVisibility(View.GONE);


                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                FitnessActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noDataLayout.setVisibility(View.VISIBLE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);

                    }
                });
            }
            return null;

        }

        protected void onPostExecute(Void result) {


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
                    listView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);

                }
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
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
                        listView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);

                    }
                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);


                } else {
                    footerView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    if (filterMenuItem != null) {

                        filterMenuItem.setVisible(true);
                    }

                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    videoImageStrToHeight = movieImageStr;

                    if (firstTime == true) {
                        Picasso.with(FitnessActivity.this).load(videoImageStrToHeight
                        ).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                                /*AsynLOADUI loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);*/
                            }
                        });
                    } else {
                        loadUI = new AsynLOADUI();
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
                videoPDialog = new ProgressBarHandler(FitnessActivity.this);
                if (listSize == 0) {
                    // hide loader for first time

                  /*  if (videoPDialog!=null && videoPDialog.isShowing()){

                    }else {
                        videoPDialog.show();
                    }*/
                    videoPDialog.show();

                    footerView.setVisibility(View.GONE);
                } else {
                    // show loader for first time
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                    footerView.setVisibility(View.VISIBLE);

                }
            }
        }


    }


    // on device configuration change , the grid numbers need to be changed

    public void onResume() {


        FitnessActivity.this.invalidateOptionsMenu();

        super.onResume();
       /* if (getApplicationContext().getView() != null) {
            InputMethodManager imm = (InputMethodManager) FitnessActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }*/


    }

    //Load Films Videos
   /* private class AsynLoadVideos extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String movieGenreStr = "";
        String movieName = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String movieStory = "";

        String movieImageStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String moviePermalinkStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String videoTypeIdStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = APIUrlConstant.getGetContentListUrl();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", authTokenStr.trim());
                String strtext = getIntent().getStringExtra("item");
                httppost.addHeader("permalink", strtext.trim());
                filterPermalink = strtext.trim();
                httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                httppost.addHeader("user_id", preferenceManager.getUseridFromPref());
                //httppost.addHeader("orderby", "");
                // httppost.addHeader("deviceType", "roku");
                String countryCodeStr = preferenceManager.getCountryCodeFromPref();

                if (countryCodeStr != null) {

                    httppost.addHeader("country", countryCodeStr);
                } else {
                    httppost.addHeader("country", "IN");

                }


                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHAA", "responseStr"+responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    FitnessActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (itemData != null) {
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                            } else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                            }

                            footerView.setVisibility(View.GONE);
                            Util.showToast(context, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, DEFAULT_SLOW_INTERNET_CONNECTION));

//                            Toast.makeText(context,languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    Log.v("SUBHAA", "IOException"+e.toString());

                    FitnessActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            footerView.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);
                }

                if (status > 0) {
                    if (status == 200) {

//                        if (itemData != null && itemData.size() > 0) {
//                            itemData.clear();
//                        }

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        Log.v("SUBHAA", "lengthJsonArr"+lengthJsonArr);

                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                Log.v("SUBHAA", "i"+i);

                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                                    movieGenreStr = jsonChildNode.getString("genre");
                                    Log.v("SUBHAA", "movieGenreStr "+movieGenreStr);
                                }
                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                    movieName = jsonChildNode.getString("name");
                                    Log.v("SUBHAA", "movieName "+movieName);
                                }
                                if ((jsonChildNode.has("story")) && jsonChildNode.getString("story").trim() != null && !jsonChildNode.getString("story").trim().isEmpty() && !jsonChildNode.getString("story").trim().equals("null") && !jsonChildNode.getString("story").trim().matches("")) {
                                    movieStory = jsonChildNode.getString("story");
                                    Log.v("SUBHAA", "movieStory "+movieStory);
                                }
                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster_url");
                                    //movieImageStr = movieImageStr.replace("episode", "original");
                                    Log.v("SUBHAA", "movieImageStr "+movieImageStr);
                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");
                                    Log.v("SUBHAA", "moviePermalinkStr "+moviePermalinkStr);
                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");
                                    Log.v("SUBHAA", "videoTypeIdStr "+videoTypeIdStr);
                                }
                                //videoTypeIdStr = "1";

                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));
                                    Log.v("SUBHAA", "isConverted "+isConverted);
                                }
                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));
                                    Log.v("SUBHAA", "isAPV "+isAPV);
                                }
                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));
                                    Log.v("SUBHAA", "isPPV "+isPPV);
                                }
                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");
                                    Log.v("SUBHAA", "isEpisodeStr "+isEpisodeStr);
                                }
                                if ((jsonChildNode.has("muvi_uniq_id")) && jsonChildNode.getString("muvi_uniq_id").trim() != null && !jsonChildNode.getString("muvi_uniq_id").trim().isEmpty() && !jsonChildNode.getString("muvi_uniq_id").trim().equals("null") && !jsonChildNode.getString("muvi_uniq_id").trim().matches("")) {
                                    movieUniqueId = jsonChildNode.getString("muvi_uniq_id");
                                    Log.v("SUBHAA", "movieUniqueId "+movieUniqueId);
                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    content_types_id = jsonChildNode.getString("content_types_id");
                                    Log.v("SUBHAA", "content_types_id "+content_types_id);
                                }

                                if ((jsonChildNode.has("is_favorite")) && jsonChildNode.getString("is_favorite").trim() != null && !jsonChildNode.getString("is_favorite").trim().isEmpty() && !jsonChildNode.getString("is_favorite").trim().equals("null") && !jsonChildNode.getString("is_favorite").trim().matches("")) {
                                    isFavorite = Integer.parseInt(jsonChildNode.getString("is_favorite"));
                                    Log.v("SUBHAA", "isFavorite "+isFavorite);
                                }
                                if ((jsonChildNode.has("movie_id")) && jsonChildNode.getString("movie_id").trim() != null && !jsonChildNode.getString("movie_id").trim().isEmpty() && !jsonChildNode.getString("movie_id").trim().equals("null") && !jsonChildNode.getString("movie_id").trim().matches("")) {
                                    content_id = jsonChildNode.getString("movie_id");
                                    Log.v("SUBHAA", "content_id "+content_id);
                                }
                                if ((jsonChildNode.has("movie_stream_id")) && jsonChildNode.getString("movie_stream_id").trim() != null && !jsonChildNode.getString("movie_stream_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_id").trim().matches("")) {
                                    content_stream_id = jsonChildNode.getString("movie_stream_id");
                                    Log.v("SUBHAA", "content_stream_id "+content_stream_id);
                                }

                                itemData.add(new YogaItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", isConverted, isPPV, isAPV, movieStory, content_types_id, isFavorite, content_id, content_stream_id, false));
                                searchedItemData.add(new YogaItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", isConverted, isPPV, isAPV, movieStory, content_types_id, isFavorite, content_id, content_stream_id, false));
                            } catch (Exception e) {
                                Log.v("SUBHAA", "Exception 1"+e.toString());
                                FitnessActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataLayout.setVisibility(View.VISIBLE);
                                        noInternetConnectionLayout.setVisibility(View.GONE);
                                        listView.setVisibility(View.GONE);
                                        footerView.setVisibility(View.GONE);
                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.v("SUBHAA", "Exception datavg");
                        responseStr = "0";
                        FitnessActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                footerView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                Log.v("SUBHAA", "Exception"+e.toString());

                if (FitnessActivity.this != null) {
                    FitnessActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noDataLayout.setVisibility(View.VISIBLE);
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            footerView.setVisibility(View.GONE);
                        }
                    });
                }

                e.printStackTrace();

            }
            Log.v("SUBHAA", "size"+responseStr);

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            Log.v("SUBHAA", "onPostExecute");


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
                    listView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
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
                        listView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);
                    }
                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    videoImageStrToHeight = movieImageStr;
                    if (firstTime == true) {
                        Picasso.with(context).load(videoImageStrToHeight
                        ).error(R.drawable.no_image).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {
                                Log.v("SUBHA", "videoImageStrToHeight = " + videoImageStrToHeight);
                                videoImageStrToHeight = "https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png";
                                videoWidth = errorDrawable.getIntrinsicWidth();
                                videoHeight = errorDrawable.getIntrinsicHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);

                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {

                            }
                        });

                    } else {
                        loadUI = new AsynLOADUI();
                        loadUI.executeOnExecutor(threadPoolExecutor);
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {
            Log.v("SUBHAA", "onPreExecute");

            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
                videoPDialog = MainActivity.internetSpeedDialog;
                footerView.setVisibility(View.GONE);
                Log.v("SUBHAA", "internetSpeedDialog");

            } else {
                videoPDialog = new ProgressBarHandler(context);

                if (listSize == 0) {
                    // hide loader for first time
                    Log.v("SUBHAA", "show");

                    videoPDialog.show();
                    footerView.setVisibility(View.GONE);
                } else {
                    // show loader for first time
                    Log.v("SUBHAA", "hide");

                    videoPDialog.hide();
                    footerView.setVisibility(View.VISIBLE);

                }
            }
        }


    }*/




    @Override
    public void onGetFeatureContentPreExecuteStarted() {
        {
            Log.v("SUBHAA", "onPreExecute");

            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
                videoPDialog = MainActivity.internetSpeedDialog;
                footerView.setVisibility(View.GONE);
                Log.v("SUBHAA", "internetSpeedDialog");

            } else {
                videoPDialog = new ProgressBarHandler(FitnessActivity.this);

                if (listSize == 0) {
                    // hide loader for first time
                    Log.v("SUBHAA", "show");

                    videoPDialog.show();
                    footerView.setVisibility(View.GONE);
                } else {
                    // show loader for first time
                    Log.v("SUBHAA", "hide");

                    videoPDialog.hide();
                    footerView.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    @Override
    public void onGetFeatureContentPostExecuteCompleted(ArrayList<FeatureContentOutputModel> featureContentOutputModelArray, int status, String message) {

        String movieImageStr = "";

        for (int i = 0; i < featureContentOutputModelArray.size(); i++) {
            movieImageStr = featureContentOutputModelArray.get(i).getPoster_url();
            String movieName = featureContentOutputModelArray.get(i).getName();
            String videoTypeIdStr = featureContentOutputModelArray.get(i).getContent_types_id();
            String movieGenreStr = featureContentOutputModelArray.get(i).getGenre();
            String moviePermalinkStr = featureContentOutputModelArray.get(i).getPermalink();
            String isEpisodeStr = featureContentOutputModelArray.get(i).getIs_episode();
            int isConverted = featureContentOutputModelArray.get(i).getIs_converted();
            int isPPV = featureContentOutputModelArray.get(i).getIs_ppv();
            int isAPV = featureContentOutputModelArray.get(i).getIs_advance();
            content_id = featureContentOutputModelArray.get(i).getContent_id();
            content_stream_id = featureContentOutputModelArray.get(i).getContent_stream_id();


            itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV,""));
            searchedItemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV,""));

        }
        if (message == null)
            message = "0";
        if ((message.trim().equals("0"))) {
            try {
                if (videoPDialog != null && videoPDialog.isShowing()) {
                    videoPDialog.hide();
                    videoPDialog = null;
                }
            } catch (IllegalArgumentException ex) {

                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }
            noDataLayout.setVisibility(View.VISIBLE);
            noInternetConnectionLayout.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
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
                    listView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            } else {
                footerView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);
                videoImageStrToHeight = movieImageStr;

                if (firstTime == true) {
                    Picasso.with(FitnessActivity.this).load(videoImageStrToHeight
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
    public void onPause() {
        backfromactivity= true;
        super.onPause();
        if (videoPDialog != null && videoPDialog.isShowing()) {
            videoPDialog.hide();
            videoPDialog = null;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        episodeListOptionMenuHandler.createOptionMenu(menu, preferenceManager, languagePreference);

        return true;
    }



    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            float density = getResources().getDisplayMetrics().density;


            Collections.sort(itemData, new Comparator<GridItem>() {
                @Override
                public int compare(GridItem random_list, GridItem new_list) {
//                    return random_list.getTitle().toLowerCase().compareTo(new_list.getTitle().toLowerCase());
                    return ((((random_list.getTitle()).replaceAll("[0-9]","")).trim()).toLowerCase()).compareTo((((new_list.getTitle()).replaceAll("[0-9]","")).trim()).toLowerCase());

                }
            });




            Collections.sort(searchedItemData, new Comparator<GridItem>() {
                @Override
                public int compare(GridItem random_list, GridItem new_list) {
//                    return random_list.getTitle().toLowerCase().compareTo(new_list.getTitle().toLowerCase());
                    return ((((random_list.getTitle()).replaceAll("[0-9]","")).trim()).toLowerCase()).compareTo((((new_list.getTitle()).replaceAll("[0-9]","")).trim()).toLowerCase());
                }
            });

            getIndexList(itemData);
            displayIndex();


            if (firstTime == true) {
                try {
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    if (filterMenuItem != null) {

                        filterMenuItem.setVisible(false);
                    }

                    footerView.setVisibility(View.GONE);
                }

                listView.smoothScrollToPosition(0);
                firstTime = false;
                ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
                listView.setLayoutParams(layoutParams);
                searchIcon.setVisibility(View.VISIBLE);



                Log.v("ANU","IF===");
                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);
                    } else {
                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);

                    }
                    listView.setAdapter(fitnessAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);
                    } else {

                        Log.v("SUBHA", "DATA here 1");

                    }
                    fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);
                    listView.setAdapter(fitnessAdapter);
                }


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = listView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
                Log.v("ANU","ELSE===");

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {

                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);
                    } else {
                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);

                    }
                    listView.setAdapter(fitnessAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);
                    } else {
                        fitnessAdapter = new FitnessActivityAdapter(FitnessActivity.this, R.layout.fitness_adapter_layout, itemData);

                    }
                    listView.setAdapter(fitnessAdapter);
                }

                if (mBundleRecyclerViewState != null) {
                    listView.onRestoreInstanceState(listState);
                }

            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = listView.onSaveInstanceState();
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
            limit = 30;
        } else {
            limit = 15;
        }
        itemsInServer = 0;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.media_route_menu_item:
                // Not implemented here
                return false;
            case R.id.action_filter:
                // Not implemented here


                noInternetConnectionLayout.setVisibility(View.GONE);
                listView.setEnabled(true);
                startActivity(new Intent(FitnessActivity.this, FilterActivity.class));
                Intent filterIntent = new Intent(FitnessActivity.this, FilterActivity.class);
                filterIntent.putExtra("genreList", genreArray);


                return false;

            default:
                break;
        }

        return false;
    }

    /***************
     * chromecast
     **********************/

    private void showIntroductoryOverlay() {


        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }


        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

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

                if (null != mSelectedMedia) {
                   /* if (mCastSession != null && mCastSession.isConnected()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            }
                        });

                    }*/
                    if (mPlaybackState == PlaybackState.PLAYING) {
                        mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);
                        return;
                    } else {

                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                //   updatePlayButton(mPlaybackState);
                //invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
               /* if (mCastSession != null && mCastSession.isConnected()) {
                    watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                }*/
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;

                //invalidateOptionsMenu();
            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                //setCoverArtStatus(null);
                startControllersTimer();
            } else {
                stopControllersTimer();
                //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            stopControllersTimer();
            //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            updateControllersVisibility(false);
        }
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(FitnessActivity.this, ExpandedControlsActivity.class);
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
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
    }

   /* private void setCoverArtStatus(String url) {
        if (url != null) {
            mAquery.id(mCoverArt).image(url);
            mCoverArt.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.INVISIBLE);
        } else {
            mCoverArt.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
        }
    }*/

    private void stopTrickplayTimer() {
        //Log.d(TAG, "Stopped TrickPlay Timer");
        if (mSeekbarTimer != null) {
            mSeekbarTimer.cancel();
        }
    }


    private void stopControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
    }

    private void startControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
        if (mLocation == PlaybackLocation.REMOTE) {
            return;
        }
        mControllersTimer = new Timer();
        mControllersTimer.schedule(new HideControllersTask(), 5000);
    }

    // should be called from the main thread
    private void updateControllersVisibility(boolean show) {
        if (show) {
            //getSupportActionBar().show();
            mControllers.setVisibility(View.VISIBLE);
        } else {
            if (!Util.isOrientationPortrait(FitnessActivity.this)) {
                //getSupportActionBar().hide();
            }
            //  mControllers.setVisibility(View.INVISIBLE);
        }
    }

    private class HideControllersTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // updateControllersVisibility(false);
                    mControllersVisible = false;
                }
            });

        }
    }

    /***************
     * chromecast
     **********************/







    @Override
    public void onGetCategoryListPreExecuteStarted() {

    }

    @Override
    public void onGetCategoryListPostExecuteCompleted(ArrayList<CategoryListOutputModel> categoryListOutputArray, int status, int totalItems, String message) {

        String searchdata = getIntent().getStringExtra("item");
        Log.v("Nihar_sdk", "searchdata =" + categoryListOutputArray.size());
        int searchListLength = categoryListOutputArray.size();
        Log.v("Nihar_sdk", "searchListLength =" + searchListLength);
        for (int i = 0; i < searchListLength - 1; i++) {
            Log.v("Nihar_sdk", "Enter for" + searchdata);
            if (categoryListOutputArray.get(i).getPermalink().contains(searchdata)) {
                Log.v("SUBHA", "image banner === " + categoryListOutputArray.get(i).getCategory_img_url());
                return;
            } else {
            }
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("BKS", "elseclickedddddd");

        if (requestCode == 30060 && resultCode == RESULT_OK) {
            if (NetworkStatus.getInstance().isConnected(FitnessActivity.this)) {


//                asynLoadVideos = new AsynLoadVideos();
//                asynLoadVideos.executeOnExecutor(threadPoolExecutor);

                FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
                featureContentInputModel.setAuthToken(authTokenStr);
                featureContentInputModel.setSection_id(sectionId.trim());
                featureContentInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                GetFeatureContentAsynTask asyncLoadVideos = new GetFeatureContentAsynTask(featureContentInputModel,FitnessActivity.this,FitnessActivity.this);
                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);



            } else {
                Toast.makeText(FitnessActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                FitnessActivity.this.finish();
            }
        }
    }

    private void getIndexList(ArrayList<GridItem> itemArrayList) {
        mapIndex = new LinkedHashMap<String, Integer>();



        for (int i = 0; i < itemArrayList.size(); i++) {
            String title = itemArrayList.get(i).getTitle();
            title = (title.replaceAll("[0-9]","")).trim();
            String index = title.substring(0,1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }

    private void displayIndex() {

        try{

            indexLayout.removeAllViews();
            listOfTextview.clear();

        }catch (Exception e){}

        for(int i=65 ; i<=90 ; i++){
            {
                textView = (TextView) FitnessActivity.this.getLayoutInflater().inflate(R.layout.side_index_item, null);
                textView.setText(""+((char)i));





                textView.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view)
                    {
                        index_clicked = true;
                        final TextView selectedIndex = (TextView) view;

                        if(mapIndex.get(selectedIndex.getText()) !=null){
                            for (int j = 0;j<listOfTextview.size();j++) {
                                if ((listOfTextview.get(j).getTag().toString().trim()).equals(selectedIndex.getTag().toString().trim())){
                                    listOfTextview.get(j).setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                                else {
                                    // listOfTextview.get(j).setTextColor(getResources().getColor(R.color.sideIndex_color));

                                    if(mapIndex.get(listOfTextview.get(j).getText()) !=null){
                                        listOfTextview.get(j).setTextColor(getResources().getColor(R.color.sideIndex_color));
                                    }else{
                                        listOfTextview.get(j).setTextColor(getResources().getColor(R.color.style_circular_color));
                                    }
                                }
                            }

                            try{
                                listView.setSelection(mapIndex.get(selectedIndex.getText()));
                            }catch (Exception e){}
                        }



                    }
                });


                indexLayout.addView(textView);

                textView.setTag(""+((char)i));
                listOfTextview.add(textView);
            }
        }


    }


    private class EditTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            searchText = searchEdittext.getText().toString();
            Log.v("ANU","searchText"+searchText);

            searchInput(searchText);
            if (searchText.length() == 0 ) {
                indexLayout.setVisibility(View.VISIBLE);
            }
            else {
                indexLayout.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public void searchInput(String Text) {
        results.clear();

//        searchedItemData = new ArrayList<YogaItem>();
        itemData.clear();


        for (int i = 0; i < searchedItemData.size(); i++) {
            if (searchedItemData.get(i).getTitle().toLowerCase().contains(Text.toLowerCase())) {
                itemData.add(searchedItemData.get(i));
            }
        }


        fitnessAdapter.notifyDataSetChanged();
    }


}
