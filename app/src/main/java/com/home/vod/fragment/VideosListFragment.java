package com.home.vod.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.GetContentListAsynTask;
import com.home.apisdk.apiModel.ContentListInput;
import com.home.apisdk.apiModel.ContentListOutput;
import com.home.vod.R;
import com.home.vod.activity.FilterActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ShowWithEpisodesActivity;
import com.home.vod.adapter.GenreFilterAdapter;
import com.home.vod.adapter.VideoFilterAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.model.ListItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.ProgressBarHandler;
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
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

/*
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;
*/

/**
 * Created by user on 28-06-2015.
 */
public class VideosListFragment extends Fragment implements GetContentListAsynTask.GetContentListListener  {
    public static boolean clearClicked = false;
   /* *//***************chromecast**********************//*
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
    */
    /***************chromecast**********************//*

*/


    AsynLOADUI loadUI;
    GetContentListAsynTask asynLoadVideos;

    ArrayList<String> url_maps;
    private ProgressBarHandler videoPDialog;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    private ProgressBarHandler gDialog;
    private SliderLayout mDemoSlider;
    String videoImageStrToHeight;
    int  videoHeight = 185;
    int  videoWidth = 256;
    PreferenceManager preferenceManager;
    GridItem itemToPlay;
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
    private VideoFilterAdapter customGridAdapter;

    //Model for GridView
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl;

    // UI
    private GridView gridView;

    ImageView img;
    //data to load videourl
    private String movieUniqueId;
    private String movieStreamUniqueId;
    String videoUrlStr;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;

    RelativeLayout filterView;
    public static ArrayList<String> genreArray;
    public static String filterOrderByStr = "lastupload";
    GenreFilterAdapter genreAdapter;
    MenuItem filterMenuItem;
    int prevPosition = 5;
    String filterPermalink = "";
    int scrolledPosition = 0;
    boolean scrolling;
    boolean isSearched = false;
    RecyclerView genreListData;


    RelativeLayout footerView;

    public VideosListFragment() {
        // Required empty public constructor

    }

    View header;
    public static boolean isLoading = false;
    LanguagePreference languagePreference;


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        if (mDemoSlider != null) {
            mDemoSlider.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        context = getActivity();
        preferenceManager = PreferenceManager.getPreferenceManager(context);
        //for search for each activity
        setHasOptionsMenu(true);
/*

**************chromecast*********************

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

**************chromecast*********************
*/

        TextView categoryTitle = (TextView) rootView.findViewById(R.id.categoryTitle);
        FontUtls.loadFont(context,context.getResources().getString(R.string.regular_fonts),categoryTitle);

        /*Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);*/
        categoryTitle.setText(getArguments().getString("title"));
        genreListData = (RecyclerView) rootView.findViewById(R.id.demoListView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        genreListData.setLayoutManager(linearLayout);
        genreListData.setItemAnimator(new DefaultItemAnimator());
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());

        posterUrl = languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA);

        gridView = (GridView) rootView.findViewById(R.id.imagesGridView);
       /* gridView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(context,2);
        gridView.setLayoutManager(mLayoutManager);
        gridView.setItemAnimator(new DefaultItemAnimator());*/
        footerView = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout)rootView.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout)rootView.findViewById(R.id.noData);
        noInternetTextView =(TextView)rootView.findViewById(R.id.noInternetTextView);
        noDataTextView =(TextView)rootView.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION,DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT,DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

        gridView.setAdapter(customGridAdapter);

        //Detect Network Connection

        if (!NetworkStatus.getInstance().isConnected(context)) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }
        resetData();
        ContentListInput contentListInput = new ContentListInput();
        contentListInput.setAuthToken(authTokenStr);
        contentListInput.setOffset(String.valueOf(offset));
        contentListInput.setLimit(String.valueOf(limit));
        String strtext = getArguments().getString("item");
        contentListInput.setPermalink(strtext.trim());
        filterPermalink = strtext.trim();
        contentListInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

        String countryCodeStr = preferenceManager.getCountryCodeFromPref();
        if (countryCodeStr != null) {

            contentListInput.setCountry(countryCodeStr);
        } else {
            contentListInput.setCountry("IN");
        }
        asynLoadVideos = new GetContentListAsynTask(contentListInput, this, context);
        asynLoadVideos.executeOnExecutor(threadPoolExecutor);

        /*gridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        isLoading = true;
                        footerView.setVisibility(View.GONE);
                        return;

                    }
                    offset += 1;
                    boolean isNetwork = Util.checkNetwork(context);
                    if (isNetwork == true) {
                        // default data
                        AsynLoadVideos asyncViewFavorite = new AsynLoadVideos();
                        asyncViewFavorite.executeOnExecutor(threadPoolExecutor);
                    }

                }

            }
        });
*/


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
                        if (NetworkStatus.getInstance().isConnected(context)) {

                            // default data
                            ContentListInput contentListInput = new ContentListInput();
                            contentListInput.setAuthToken(authTokenStr);
                            contentListInput.setOffset(String.valueOf(offset));
                            contentListInput.setLimit(String.valueOf(limit));
                            String strtext = getArguments().getString("item");
                            contentListInput.setPermalink(strtext.trim());
                            contentListInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            String countryCodeStr = preferenceManager.getCountryCodeFromPref();
                            if (countryCodeStr != null) {
                                contentListInput.setCountry(countryCodeStr);
                            }else{
                                contentListInput.setCountry("IN");
                            }

                            asynLoadVideos = new GetContentListAsynTask(contentListInput, VideosListFragment.this, context);
                            asynLoadVideos.executeOnExecutor(threadPoolExecutor);


                            scrolling = false;

                        }

                    }

                }

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                img = (ImageView) view.findViewById(R.id.movieImageView);

                GridItem item = itemData.get(position);
                itemToPlay = item;
                String posterUrl = item.getImage();
                String movieName = item.getTitle();
                String movieGenre = item.getMovieGenre();
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();
                videoUrlStr = item.getVideoUrl();
                String isEpisode = item.getIsEpisode();
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

                    if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
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
                        final Intent detailsIntent = new Intent(context, ShowWithEpisodesActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(detailsIntent);
                            }
                        });
                    }
                }

            }
        });


        filterView = (RelativeLayout) rootView.findViewById(R.id.filterBg);

        filterView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                filterView.setVisibility(View.GONE);
                gridView.setEnabled(true);

                if ((filterOrderByStr != null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray != null && genreArray.size() > 0)) {
                    firstTime = true;


                    offset = 1;
                    scrolledPosition = 0;
                    listSize = 0;
                    if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        limit = 20;
                    } else {
                        limit = 15;
                    }
                    itemsInServer = 0;
                    scrolling = false;
                    if (itemData != null && itemData.size() > 0) {
                        itemData.clear();
                    }
                    isSearched = false;

                    if (!NetworkStatus.getInstance().isConnected(context)) {
                        noInternetConnectionLayout.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                        if (filterMenuItem != null) {

                            filterMenuItem.setVisible(false);
                        }


                    } else {
                        if (videoPDialog != null && videoPDialog.isShowing()) {
                            videoPDialog.hide();
                            videoPDialog = null;
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
       // SharedPreferences isLoginPref = context.getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode

        String genreString = preferenceManager.getGenreArrayFromPref();
        String genreValuesString = preferenceManager.getGenreValuesArrayFromPref();
        final String[] genreTempArr = genreString.split(",");
        String[] genreValuesTempArr = genreValuesString.split(",");

        for (int i = 0; i < genreTempArr.length; i++) {

            mdata.add(new ListItem(genreTempArr[i], genreValuesTempArr[i]));
        }


        genreAdapter = new GenreFilterAdapter(mdata, getActivity());
        genreListData.setAdapter(genreAdapter);
        if (mdata.size() > 0) {
            prevPosition = mdata.size() - 4;
        }
        mdata.get(prevPosition).setSelected(true);
        genreListData.addOnItemTouchListener(new RecyclerTouchListener(context, genreListData, new VideosListFragment.ClickListener() {
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

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (itemData!=null){
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);


                            }else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);


                            }

                            footerView.setVisibility(View.GONE);
                            Toast.makeText(context,languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
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

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        for(int i=0; i < lengthJsonArr; i++) {
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

                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV));
                            } catch (Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
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
                    }
                    else{
                        responseStr = "0";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                getActivity().runOnUiThread(new Runnable() {
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
        // if (genreArray!=null && genreArray.size() > 0) {
        if ((filterOrderByStr != null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray != null && genreArray.size() > 0)) {
            firstTime = true;
            LogUtil.showLog("SUBHAA", "hgdjhdgjhbj" + clearClicked);

            offset = 1;
            scrolledPosition = 0;
            listSize = 0;
            if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                limit = 20;
            } else {
                limit = 15;
            }
            itemsInServer = 0;
            scrolling = false;
            if (itemData != null && itemData.size() > 0) {
                itemData.clear();
            }
            boolean isNetwork = NetworkStatus.getInstance().isConnected(context);
            isSearched = false;

            if (isNetwork == false) {
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
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
        // }

        if (pDialog != null) {
            pDialog.hide();
            pDialog = null;
        }
//        LogUtil.showLog("SUBHA","JFJFJCLEA"+clearClicked);
        if (clearClicked == true) {
            boolean isNetwork = NetworkStatus.getInstance().isConnected(context);
            if (isNetwork == false) {
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }
            resetData();
            LogUtil.showLog("SUBHAA", "JFJFJCLEA" + clearClicked);

            clearClicked = false;

            ContentListInput contentListInput = new ContentListInput();
            contentListInput.setAuthToken(authTokenStr);
            contentListInput.setOffset(String.valueOf(offset));
            contentListInput.setLimit(String.valueOf(limit));
            String strtext = getArguments().getString("item");
            contentListInput.setPermalink(strtext.trim());
            contentListInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            String countryCodeStr = preferenceManager.getCountryCodeFromPref();
            if (countryCodeStr != null) {
                contentListInput.setCountry(countryCodeStr);
            }else{
                contentListInput.setCountry("IN");
            }

            asynLoadVideos = new GetContentListAsynTask(contentListInput, VideosListFragment.this, context);
            asynLoadVideos.executeOnExecutor(threadPoolExecutor);


           /* asynLoadVideos = new AsynLoadVideos();
            asynLoadVideos.executeOnExecutor(threadPoolExecutor);*/
        }

/*
*//***************chromecast**********************//*
        mCastContext.addCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();
        }
*//***************chromecast**********************/


        if (url_maps != null && url_maps.size() > 0) {
            url_maps.clear();
        }
        getActivity().invalidateOptionsMenu();
        super.onResume();
       /* if (videoPDialog != null && videoPDialog.isShowing()) {
            videoPDialog.hide();
            videoPDialog = null;
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;
        }*/
        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }



    /*public void onResume() {
        if (pDialog != null) {
            pDialog.hide();
            pDialog = null;
        }
*//*
*//**//***************chromecast**********************//**//*
        mCastContext.addCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();
        }
*//**//***************chromecast**********************//*

        if (filterView != null && filterView.getVisibility() == View.VISIBLE) {
            filterView.setVisibility(View.GONE);
            genreListData.setVisibility(View.GONE);
        }


        if (url_maps != null && url_maps.size() > 0) {
            url_maps.clear();
        }
        getActivity().invalidateOptionsMenu();
        super.onResume();
       *//* if (videoPDialog != null && videoPDialog.isShowing()) {
            videoPDialog.hide();
            videoPDialog = null;
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;
        }*//*
        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }*/

    @Override
    public void onGetContentListPreExecuteStarted() {

        if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
            videoPDialog = MainActivity.internetSpeedDialog;
            footerView.setVisibility(View.GONE);

        } else {
            videoPDialog = new ProgressBarHandler(context);

            if (listSize == 0) {
                // hide loader for first time

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

    @Override
    public void onGetContentListPostExecuteCompleted(ArrayList<ContentListOutput> contentListOutputArray, int status, int totalItems, String message) {


        if (status>0){
            if (status==200){
                String movieImageStr="";

                for (int i = 0; i < contentListOutputArray.size(); i++) {

                    String movieName = contentListOutputArray.get(i).getName();
                    String contentTypesId = contentListOutputArray.get(i).getContentTypesId();
                    String movieGenreStr = contentListOutputArray.get(i).getGenre();
                    movieImageStr = contentListOutputArray.get(i).getPosterUrl();
                    String moviePermalinkStr = contentListOutputArray.get(i).getPermalink();
                    String isEpisodeStr = contentListOutputArray.get(i).getIsEpisodeStr();
                    int isConverted = contentListOutputArray.get(i).getIsConverted();
                    int isPPV = contentListOutputArray.get(i).getIsPPV();
                    int isAPV = contentListOutputArray.get(i).getIsAPV();

                    itemData.add(new GridItem(movieImageStr, movieName, "", contentTypesId, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV));
                }
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
                        Picasso.with(context).load(videoImageStrToHeight
                        ).error(R.drawable.no_image).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            //
                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {
                                LogUtil.showLog("MUVI", "videoImageStrToHeight = " + videoImageStrToHeight);
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
        }else {

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
        }



          /*try {
            if (videoPDialog != null && videoPDialog.isShowing()) {
                videoPDialog.hide();
                videoPDialog = null;
            }
        }catch (IllegalArgumentException ex) {

        }*/

      /*  String movieImageStr="";

        for (int i = 0; i < contentListOutputArray.size(); i++) {

            String movieName = contentListOutputArray.get(i).getName();
            String contentTypesId = contentListOutputArray.get(i).getContentTypesId();
            String movieGenreStr = contentListOutputArray.get(i).getGenre();
            movieImageStr = contentListOutputArray.get(i).getPosterUrl();
            String moviePermalinkStr = contentListOutputArray.get(i).getPermalink();
            String isEpisodeStr = contentListOutputArray.get(i).getIsEpisodeStr();
            int isConverted = contentListOutputArray.get(i).getIsConverted();
            int isPPV = contentListOutputArray.get(i).getIsPPV();
            int isAPV = contentListOutputArray.get(i).getIsAPV();

            itemData.add(new GridItem(movieImageStr, movieName, "", contentTypesId, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV));
        }
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
                    Picasso.with(context).load(videoImageStrToHeight
                    ).error(R.drawable.no_image).into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            videoWidth = bitmap.getWidth();
                            videoHeight = bitmap.getHeight();
                            loadUI = new AsynLOADUI();
                            loadUI.executeOnExecutor(threadPoolExecutor);
                        }

                        //
                        @Override
                        public void onBitmapFailed(final Drawable errorDrawable) {
                            LogUtil.showLog("MUVI", "videoImageStrToHeight = " + videoImageStrToHeight);
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
            }*/
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

     /*   InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
*/

        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
        gridView.setLayoutParams(layoutParams);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setGravity(Gravity.CENTER_HORIZONTAL);

        if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            } else {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            }

        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
            } else {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            }

        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

            gridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);


        } else {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            } else {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 4);
            }


        }

        super.onConfigurationChanged(newConfig);
    }

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

        MenuItem item;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(true);

     /*   *//***************chromecast**********************//*
        showIntroductoryOverlay();

        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getActivity(), menu, R.id.media_route_menu_item);

        *//***************chromecast**********************//*
*/

        super.onCreateOptionsMenu(menu, inflater);

    }


    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            float density = context.getResources().getDisplayMetrics().density;
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
                    if (filterMenuItem != null) {

                        filterMenuItem.setVisible(false);
                    }

                    footerView.setVisibility(View.GONE);
                }

                gridView.smoothScrollToPosition(0);
                firstTime = false;
                ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
                gridView.setLayoutParams(layoutParams);
                gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                gridView.setGravity(Gravity.CENTER_HORIZONTAL);

                if (getActivity() != null && (getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    }

                } else if (getActivity() != null && (getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    }

                } else if (getActivity() != null && (context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                } else {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                    }

                }

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = gridView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);

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




/*
    public void clickItem(GridItem item){
        itemToPlay = item;
        String moviePermalink = item.getPermalink();
        String movieTypeId = item.getVideoTypeId();



        // for deafult no search
            if (moviePermalink.matches(Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA))) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                dlgAlert.setMessage(Util.getTextofLanguage(context,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(context,Util.SORRY,Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(context,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(context,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();

            } else {

                if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                    final Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                    movieDetailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(movieDetailsIntent);
                        }
                    });


                } else if ((movieTypeId.trim().equalsIgnoreCase("3")) ) {
                    final Intent detailsIntent = new Intent(context, ShowWithEpisodesActivity.class);
                    detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(detailsIntent);
                        }
                    });
                }
            }




    }*/


    public void resetData() {
        if (itemData != null && itemData.size() > 0) {
            itemData.clear();
        }
        firstTime = true;

        offset = 1;
        isLoading = false;
        listSize = 0;
        if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            limit = 20;
        } else {
            limit = 15;
        }
        itemsInServer = 0;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //comment by bishal
       /* switch (item.getItemId()) {
            case R.id.media_route_menu_item:
                // Not implemented here
                return false;
            case R.id.action_filter:
                // Not implemented here


                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setEnabled(true);
                if (filterView != null && filterView.getVisibility() == View.VISIBLE) {
                    filterView.setVisibility(View.GONE);
                    genreListData.setVisibility(View.GONE);
//                    gridView.setEnabled(true);

                    if ((filterOrderByStr != null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray != null && genreArray.size() > 0)) {
                        firstTime = true;


                        offset = 1;
                        scrolledPosition = 0;
                        listSize = 0;
                        if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                            limit = 20;
                        } else {
                            limit = 15;
                        }
                        itemsInServer = 0;
                        scrolling = false;
                        if (itemData != null && itemData.size() > 0) {
                            itemData.clear();
                        }
                        isSearched = false
                        ;
                        if (!NetworkStatus.getInstance().isConnected(context)) {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);
                            if (filterMenuItem != null) {

                                filterMenuItem.setVisible(false);
                            }


                        } else {

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


                } else {
                    filterView.setVisibility(View.VISIBLE);
                    genreListData.setVisibility(View.VISIBLE);
                    gridView.setEnabled(false);

                }

                return false;

            default:
                break;
        }*/

       //addded by bishal
        switch (item.getItemId()) {
            case R.id.media_route_menu_item:
                // Not implemented here
                return false;
            case R.id.action_filter:
                // Not implemented here


                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setEnabled(true);
                startActivity(new Intent(getActivity(), FilterActivity.class));
                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
                filterIntent.putExtra("genreList", genreArray);

              /*  if (filterView!=null && filterView.getVisibility()== View.VISIBLE){
                    filterView.setVisibility(View.GONE);
                    genreListData.setVisibility(View.GONE);
//                    gridView.setEnabled(true);
                    if ((filterOrderByStr!=null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray!=null && genreArray.size() > 0)) {
                        firstTime = true;
                        offset = 1;
                        scrolledPosition = 0;
                        listSize = 0;
                        if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                            limit = 20;
                        } else {
                            limit = 15;
                        }
                        itemsInServer = 0;
                        scrolling = false;
                        if (itemData != null && itemData.size() > 0) {
                            itemData.clear();
                        }
                        boolean isNetwork = Util.checkNetwork(context);
                        isSearched = false
                        ;
                        if (isNetwork == false) {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);
                            if (filterMenuItem != null) {
                                filterMenuItem.setVisible(false);
                            }
                        } else {
                            if (asynLoadVideos!=null){
                                asynLoadVideos.cancel(true);
                            }
                            if (loadUI!=null){
                                loadUI.cancel(true);
                            }
                            AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
                        }
                    }
                }
                else {
                    filterView.setVisibility(View.VISIBLE);
                    genreListData.setVisibility(View.VISIBLE);
                    gridView.setEnabled(false);
                }*/

                return false;

            default:
                break;
        }

        return false;
    }

 /*   *//***************chromecast**********************//*

    private void showIntroductoryOverlay() {


        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }


        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    *//*mIntroductoryOverlay =
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


                    mIntroductoryOverlay.show();*//*
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
                   *//* if (mCastSession != null && mCastSession.isConnected()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            }
                        });

                    }*//*
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
               *//* if (mCastSession != null && mCastSession.isConnected()) {
                    watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                }*//*
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

   *//* private void setCoverArtStatus(String url) {
        if (url != null) {
            mAquery.id(mCoverArt).image(url);
            mCoverArt.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.INVISIBLE);
        } else {
            mCoverArt.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
        }
    }*//*

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
*//***************chromecast**********************//*

*/
}
