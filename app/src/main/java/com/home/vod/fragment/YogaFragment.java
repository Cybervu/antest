package com.home.vod.fragment;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
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
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.CategoryListInput;
import com.home.apisdk.apiModel.CategoryListOutputModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.vod.R;
import com.home.vod.Episode_Programme_Handler;
import com.home.vod.activity.FilterActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.YogaPlayerActivity;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
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
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

/*
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;
*/

/**
 * Created by user on 28-06-2015.
 */
public class YogaFragment extends Fragment implements DeleteFavAsync.DeleteFavListener, AddToFavAsync.AddToFavListener, GetCategoryListAsynTask.GetCategoryListListener {

    public static boolean clearClicked = false;
    private int isFavorite = 0;
    Context adaptorContext;
    private String content_types_id;
    private String content_id;
    private String content_stream_id;
    private boolean backfromactivity = false;

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
    AsynLoadVideos asynLoadVideos;

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
    YogaItem itemToPlay;
    private ProgressBarHandler pDialog;
    private static int firstVisibleInListview;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    //for no internet

    private RelativeLayout noInternetConnectionLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;


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
    Context context;

    //Adapter for GridView
    private YogaFilterAdapter customGridAdapter;

    //Model for GridView
    ArrayList<YogaItem> itemData = new ArrayList<YogaItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl;

    // UI
    private ListView listView;

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


    RelativeLayout footerView;

    public YogaFragment() {
        // Required empty public constructor

    }

    View header;
    public static boolean isLoading = false;
    ImageView bannerimageView;

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        if (mDemoSlider != null) {
            mDemoSlider.stopAutoCycle();
        }
        super.onStop();
    }
    TextView categoryTitle;
    View headerView;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.yoga_videos, container, false);
        context = getActivity();
        //for search for each activity
        setHasOptionsMenu(true);
        filterOrderByStr = "";
        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {

                    showIntroductoryOverlay();
                }
            }
        };
        mCastContext = CastContext.getSharedInstance(getActivity());
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(getActivity(), savedInstanceState);


        // int startPosition = getInt("startPosition", 0);
        // mVideoView.setVideoURI(Uri.parse(item.getContentId()));


        setupCastListener();
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        genreListData = (RecyclerView) rootView.findViewById(R.id.demoListView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        genreListData.setLayoutManager(linearLayout);
        genreListData.setItemAnimator(new DefaultItemAnimator());
        preferenceManager = PreferenceManager.getPreferenceManager(getActivity());
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());

        posterUrl = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

        listView = (ListView) rootView.findViewById(R.id.imagesGridView);
 titleStr = getArguments().getString("title");
        footerView = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout) rootView.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) rootView.findViewById(R.id.noData);
        noInternetTextView = (TextView) rootView.findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) rootView.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
      /*  View headerView = ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.yoga_listing, listView, false);
        listView.addHeaderView(headerView);

        listView.setAdapter(customGridAdapter);*/

        headerView = ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.yoga_listing, listView, false);
        listView.addHeaderView(headerView);

        listView.setAdapter(customGridAdapter);

        categoryTitle = (TextView) rootView.findViewById(R.id.categoryTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText(titleStr);
        //Detect Network Connection


        if (!NetworkStatus.getInstance().isConnected(getActivity())) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }
        resetData();
        asynLoadVideos = new AsynLoadVideos();
        asynLoadVideos.execute();

        bannerimageView = (ImageView) rootView.findViewById(R.id.bannerImageView);
        CategoryListInput categoryListInput = new CategoryListInput();
        categoryListInput.setAuthToken(authTokenStr);
        GetCategoryListAsynTask getCategoryListAsynTask = new GetCategoryListAsynTask(categoryListInput, YogaFragment.this, getActivity());
        getCategoryListAsynTask.execute();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

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


                if (scrolling == true && mIsScrollingUp == false) {


                    Log.v("SUBHA", "on scroll data");

                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                        listSize = itemData.size();
                        if (listView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;
                        if (NetworkStatus.getInstance().isConnected(getActivity())) {

                            if ((filterOrderByStr != null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray != null && genreArray.size() > 0)) {

                                Log.v("SUBHA", "filterOrderByStr " + filterOrderByStr);

                                AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

                            } else {
                                Log.v("SUBHA", "asynLoadVideos " + filterOrderByStr);


                                asynLoadVideos = new AsynLoadVideos();
                                asynLoadVideos.executeOnExecutor(threadPoolExecutor);
                            }

                            scrolling = false;

                        }

                    }

                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position <= 0){
                    return;
                }
                img = (ImageView) view.findViewById(R.id.movieImageView);

                Log.v("SUBHA", "grid view item called");


                YogaItem item = itemData.get(position - 1);
                itemToPlay = item;
                String moviePermalink = item.getPermalink();
                videoUrlStr = item.getVideoUrl();
                movieUniqueId = item.getMovieUniqueId();
                movieStreamUniqueId = item.getMovieStreamUniqueId();

                if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
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

                    final Intent movieDetailsIntent = new Intent(context, YogaPlayerActivity.class);
                    movieDetailsIntent.putExtra(PERMALINK_INTENT_KEY, item.getPermalink());
                    movieDetailsIntent.putExtra("CONTENT_ID", item.getContent_id());
                    movieDetailsIntent.putExtra("CONTENT_STREAM_ID", item.getContent_stream_id());
                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(movieDetailsIntent);
                        }
                    });


                   /* if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                        final Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                        movieDetailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(movieDetailsIntent);
                            }
                        });


                    } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                        new Episode_Programme_Handler(getActivity()).handleIntent(PERMALINK_INTENT_KEY, moviePermalink);
                    }*/
                }

            }
        });


        filterView = (RelativeLayout) rootView.findViewById(R.id.filterBg);

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
                    if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
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

                    if (!NetworkStatus.getInstance().isConnected(getActivity())) {
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
                        if (asynLoadVideos != null) {
                            asynLoadVideos.cancel(true);
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


        genreAdapter = new GenreFilterAdapter(mdata, getActivity());
        genreListData.setAdapter(genreAdapter);
        if (mdata.size() > 0) {
            prevPosition = mdata.size() - 4;
        }
        mdata.get(prevPosition).setSelected(true);
        genreListData.addOnItemTouchListener(new RecyclerTouchListener(context, genreListData, new ClickListener() {
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


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        //drawer_collapse_expand_imageview.clear();

                        final Intent startIntent = new Intent(context, MainActivity.class);

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);

                                getActivity().finish();

                            }
                        });
                    }
                }
                return false;
            }
        });

        return rootView;
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
                    getActivity().runOnUiThread(new Runnable() {
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

                            //  Toast.makeText(context,languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
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
                                itemData.add(new YogaItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", isConverted, isPPV, isAPV, "", content_types_id, isFavorite, content_id, content_stream_id, false));
                            } catch (Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
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
                        getActivity().runOnUiThread(new Runnable() {
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
                getActivity().runOnUiThread(new Runnable() {
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
                        Picasso.with(getActivity()).load(videoImageStrToHeight
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
                videoPDialog = new ProgressBarHandler(context);
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


        getActivity().invalidateOptionsMenu();

        super.onResume();
        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
        Log.v("SUBHAA", "onResume" + Util.favorite_clicked);


        // if (genreArray!=null && genreArray.size() > 0) {
      /*  if ((filterOrderByStr != null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray != null && genreArray.size() > 0)) {
            firstTime = true;
            Log.v("SUBHAA", "hgdjhdgjhbj" + clearClicked);

            if (itemData != null && itemData.size() > 0) {
                itemData.clear();
            }

            isLoading = false;

            offset = 1;
            scrolledPosition = 0;
            listSize = 0;
            if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                limit = 30;
            } else {
                limit = 15;
            }
            itemsInServer = 0;
            // scrolling = false;

            isSearched = false;

            if (!NetworkStatus.getInstance().isConnected(getActivity())) {
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                if (filterMenuItem != null) {

                    filterMenuItem.setVisible(false);
                }


            } else {

               *//* if (itemData != null && itemData.size() > 0) {
                    itemData.clear();
                }*//*
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                if (videoPDialog != null && videoPDialog.isShowing()) {
                    videoPDialog.hide();
                    videoPDialog = null;
                }
                if (asynLoadVideos != null) {
                    asynLoadVideos.cancel(true);
                }
                if (loadUI != null) {
                    loadUI.cancel(true);
                }
                AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

            }
        }*/
        // }



        if (Util.favorite_clicked == true) {

            Util.favorite_clicked = false;
            listView.removeHeaderView(headerView);
            listView.addHeaderView(headerView);

        /*    Log.v("SUBHA","title "+ titleStr);
            listView.setAdapter(customGridAdapter);
            Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts));
            categoryTitle.setTypeface(castDescriptionTypeface);
            categoryTitle.setText(titleStr);
            //Detect Network Connection
            listView.addHeaderView(headerView);*/


            if (!NetworkStatus.getInstance().isConnected(getActivity())) {
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }
            resetData();


         /*   CategoryListInput categoryListInput = new CategoryListInput();
            categoryListInput.setAuthToken(authTokenStr);
            GetCategoryListAsynTask getCategoryListAsynTask = new GetCategoryListAsynTask(categoryListInput, YogaFragment.this, getActivity());
            getCategoryListAsynTask.execute();*/

            asynLoadVideos = new AsynLoadVideos();
            asynLoadVideos.execute();
        }








    }

    //Load Films Videos
    private class AsynLoadVideos extends AsyncTask<Void, Void, Void> {
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
                String strtext = getArguments().getString("item");
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
                    getActivity().runOnUiThread(new Runnable() {
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

                    getActivity().runOnUiThread(new Runnable() {
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
                            } catch (Exception e) {
                                Log.v("SUBHAA", "Exception 1"+e.toString());
                                getActivity().runOnUiThread(new Runnable() {
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
                        getActivity().runOnUiThread(new Runnable() {
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

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
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


   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

     *//*   InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
*//*

        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
        listView.setLayoutParams(layoutParams);
        listView.setStretchMode(listView.STRETCH_COLUMN_WIDTH);
        listView.setGravity(Gravity.CENTER_HORIZONTAL);

        if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            if (videoWidth > videoHeight) {
                listView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            } else {
                listView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            }

        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            if (videoWidth > videoHeight) {
                listView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
            } else {
                listView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            }

        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

            listView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);


        } else {
            if (videoWidth > videoHeight) {
                listView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            } else {
                listView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 4);
            }


        }

        super.onConfigurationChanged(newConfig);
    }
*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        MenuItem item, item1;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);

    /*    item1= menu.findItem(R.id.action_notifications);
        item1.setVisible(false);*/

        /***************chromecast**********************/
        showIntroductoryOverlay();

//        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getActivity(), menu, R.id.media_route_menu_item);

        /***************chromecast**********************/


        super.onCreateOptionsMenu(menu, inflater);

    }


    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            float density = context.getResources().getDisplayMetrics().density;
            Log.v("ANU", "density=====" + density);
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
               /* listView.setStretchMode(listView.STRETCH_COLUMN_WIDTH);
                listView.setGravity(Gravity.CENTER_HORIZONTAL);*/

                /*if (getActivity()!=null && (getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    if (videoWidth > videoHeight) {
                        listView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_large_3) : (int) context.getResources().getDimension(R.dimen.configuration_large_3));
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_xlarge_4) : (int) context.getResources().getDimension(R.dimen.configuration_xlarge_4));
                    }

                } else if (getActivity() !=null && (getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_normal_2) : (int) context.getResources().getDimension(R.dimen.configuration_normal_2));
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_large_3) : (int) context.getResources().getDimension(R.dimen.configuration_large_3));
                    }

                } else if (getActivity()!=null && (context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_normal_2) : (int) context.getResources().getDimension(R.dimen.configuration_normal_2));


                } else {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_xlarge_4) : (int) context.getResources().getDimension(R.dimen.configuration_xlarge_4));
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? (int) context.getResources().getDimension(R.dimen.configuration_xlarge_5) : (int) context.getResources().getDimension(R.dimen.configuration_xlarge_5));
                    }*/

//                }

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);
                    } else {
                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);

                    }
                    listView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);
                    } else {

                        Log.v("SUBHA", "DATA here 1");

                    }
                    customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);
                    listView.setAdapter(customGridAdapter);
                }


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = listView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {

                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);
                    } else {
                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);

                    }
                    listView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);
                    } else {
                        customGridAdapter = new YogaFilterAdapter(context, R.layout.list_card_item_layout, itemData);

                    }
                    listView.setAdapter(customGridAdapter);
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
        if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
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
                startActivity(new Intent(getActivity(), FilterActivity.class));
                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
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
                    /*mIntroductoryOverlay =
                            new IntroductoryOverlay.Builder(
                            getActivity(), mediaRouteMenuItem)
                            .setTitleText(getActivity().getString(R.string.introducing_cast))
                            .setOverlayColor(R.color.primary)
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();


                    mIntroductoryOverlay.show();*/
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
                Intent intent = new Intent(context, ExpandedControlsActivity.class);
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
            if (!Util.isOrientationPortrait(context)) {
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

///Add to favorite
    public void AddToFev(Context mContext, String mMovieUniqueId) {
        adaptorContext = mContext;
        videoPDialog = new ProgressBarHandler(mContext);
        if (preferenceManager != null) {
            loggedInStr = preferenceManager.getUseridFromPref();
        }

        if (loggedInStr != null) {
            if (isFavorite == 1) {

                DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
                deleteFavInputModel.setAuthTokenStr(authTokenStr);
                deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                deleteFavInputModel.setMovieUniqueId(mMovieUniqueId);
                deleteFavInputModel.setIsEpisode("1");

                DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, YogaFragment.this, mContext);
                deleteFavAsync.execute();

                       /* AsynFavoriteDelete asynFavoriteDelete=new AsynFavoriteDelete();
                        asynFavoriteDelete.execute();*/
            } else {

                LogUtil.showLog("MUVI", "favorite");
                AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                addToFavInputModel.setAuthToken(authTokenStr);
                addToFavInputModel.setMovie_uniq_id(mMovieUniqueId);
                addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                addToFavInputModel.setIsEpisodeStr("1");

                AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, YogaFragment.this, mContext);
                asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);


            }
        } else {
            Util.favorite_clicked = true;
            final Intent registerActivity = new Intent(getActivity(), RegisterActivity.class);
            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {
                    registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    registerActivity.putExtra("from", this.getClass().getName());
                    context.startActivity(registerActivity);

                }
            });

        }
    }


    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {
        pDialog.hide();
//        Toast.makeText(adaptorContext, ""+sucessMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddToFavPreExecuteStarted() {
        pDialog.show();
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        pDialog.hide();
        if (sucessMsg == null) {
            Toast.makeText(adaptorContext, "error", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(adaptorContext, "" + sucessMsg, Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public void onGetCategoryListPreExecuteStarted() {

    }

    @Override
    public void onGetCategoryListPostExecuteCompleted(ArrayList<CategoryListOutputModel> categoryListOutputArray, int status, int totalItems, String message) {

        String searchdata = getArguments().getString("item");
        Log.v("Nihar_sdk", "searchdata =" + categoryListOutputArray.size());
        int searchListLength = categoryListOutputArray.size();
        Log.v("Nihar_sdk", "searchListLength =" + searchListLength);
        for (int i = 0; i < searchListLength - 1; i++) {
            Log.v("Nihar_sdk", "Enter for" + searchdata);
            if (categoryListOutputArray.get(i).getPermalink().contains(searchdata)) {
                Log.v("SUBHA", "image banner === " + categoryListOutputArray.get(i).getCategory_img_url());

                Picasso.with(getActivity())
                        .load(categoryListOutputArray.get(i).getCategory_img_url())
                        .into(bannerimageView);

//                Picasso.with(getActivity()).load(categoryListOutputArray.get(i).getCategory_img_url()).into(bannerimageView);
                return;
            } else {
//                Toast.makeText(getActivity(), "Image denied", Toast.LENGTH_SHORT).show();
            }
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("BKS", "elseclickedddddd");

        if (requestCode == 30060 && resultCode == RESULT_OK) {
            if (NetworkStatus.getInstance().isConnected(getActivity())) {


                asynLoadVideos = new AsynLoadVideos();
                asynLoadVideos.executeOnExecutor(threadPoolExecutor);


               /* ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
                useridStr = preferenceManager.getUseridFromPref();

                contentDetailsInput.setAuthToken(authTokenStr);

                Log.v("SUBHA", "authToken1243442554 === " + authTokenStr);
                if (preferenceManager != null) {
                    String countryPref = preferenceManager.getCountryCodeFromPref();
                    contentDetailsInput.setCountry(countryPref);
                } else {
                    contentDetailsInput.setCountry("IN");
                }
                contentDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                contentDetailsInput.setPermalink(permalinkStr);
                contentDetailsInput.setUser_id(useridStr);
                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, this, this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);*/

            } else {
                Toast.makeText(getActivity(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }
}