package com.home.vod.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
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
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.MyLibraryAsynTask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Get_Video_Details_Output;
import com.home.apisdk.apiModel.MyLibraryInputModel;
import com.home.apisdk.apiModel.MyLibraryOutputModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.vod.R;
import com.home.vod.activity.Episode_list_Activity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.MyLibraryPlayer;
import com.home.vod.activity.ShowWithEpisodesActivity;
import com.home.vod.adapter.GenreFilterAdapter;
import com.home.vod.adapter.VideoFilterAdapter;
import com.home.vod.model.DataModel;
import com.home.vod.model.GridItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.muvi.player.activity.ThirdPartyPlayer;
import com.muvi.player.activity.YouTubeAPIActivity;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
import static com.home.vod.preferences.LanguagePreference.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.APP_ON;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.CROSSED_MAXIMUM_LIMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_ON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_VIDEO_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.NO_VIDEO_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.SEASON_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

/*
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;
*/

/**
 * Created by user on 28-06-2015.
 */
public class MyLibraryFragment extends Fragment implements VideoDetailsAsynctask.VideoDetails, GetValidateUserAsynTask.GetValidateUser,
        MyLibraryAsynTask.MyLibrary {
    ArrayList<String> url_maps;
    private ProgressBarHandler videoPDialog;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    private ProgressBarHandler gDialog;
    private SliderLayout mDemoSlider;
    String videoImageStrToHeight;
    int videoHeight = 185;
    int videoWidth = 256;
    private PreferenceManager preferenceManager;
    GridItem itemToPlay;
    private ProgressBarHandler pDialog;
    private static int firstVisibleInListview;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    LanguagePreference languagePreference;

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


    int season_id = 0;
    int isFreeContent = 0;

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
    ArrayList<String> genreArray;
    String filterOrderByStr = "lastupload";
    GenreFilterAdapter genreAdapter;
    MenuItem filterMenuItem;
    int prevPosition = 5;
    String filterPermalink = "";
    int scrolledPosition = 0;
    boolean scrolling;
    boolean isSearched = false;
    RecyclerView genreListData;
    RelativeLayout footerView;
    TextView sectionTitle;
    String titleListName;

    public static ProgressBarHandler progressBarHandler;
    String filename = "";
    static File mediaStorageDir;

    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();

    public MyLibraryFragment() {
        // Required empty public constructor

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mylibrary_videos, container, false);
        context = getActivity();
        //for search for each activity
        setHasOptionsMenu(true);
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());
        genreListData = (RecyclerView) rootView.findViewById(R.id.demoListView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        genreListData.setLayoutManager(linearLayout);
        genreListData.setItemAnimator(new DefaultItemAnimator());
        preferenceManager = PreferenceManager.getPreferenceManager(getActivity());// 0 - for private mode
        sectionTitle = (TextView) rootView.findViewById(R.id.sectionTitle);
        posterUrl = languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA);

        gridView = (GridView) rootView.findViewById(R.id.imagesGridView);
       /* gridView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(context,2);
        gridView.setLayoutManager(mLayoutManager);
        gridView.setItemAnimator(new DefaultItemAnimator());*/
        footerView = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout) rootView.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) rootView.findViewById(R.id.noData);
        noInternetTextView = (TextView) rootView.findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) rootView.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage( NO_CONTENT, DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

        if (getArguments().getString("title") != null) {
            titleListName = getArguments().getString("title");
            sectionTitle.setText(titleListName);
        } else {
            sectionTitle.setText("");

        }
        Typeface sectionTitleTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.regular_fonts));
        sectionTitle.setTypeface(sectionTitleTypeface);

        gridView.setAdapter(customGridAdapter);

        //Detect Network Connection


        if (!NetworkStatus.getInstance().isConnected(getActivity())) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }
        resetData();


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
            }
        } else {
            //Call whatever you want
            if (NetworkStatus.getInstance().isConnected(getActivity())) {

                MyLibraryInputModel myLibraryInputModel=new MyLibraryInputModel();
                myLibraryInputModel.setAuthToken(authTokenStr);
                myLibraryInputModel.setUser_id(preferenceManager.getUseridFromPref());
                myLibraryInputModel.setLimit(String.valueOf(limit));
                myLibraryInputModel.setOffset(String.valueOf(offset));
                String countryPref=preferenceManager.getCountryCodeFromPref();
                if (countryPref != null) {
                    myLibraryInputModel.setCountry(countryPref);
                }else {
                    myLibraryInputModel.setCountry("IN");
                }
                myLibraryInputModel.setLang_code(languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                MyLibraryAsynTask asyncLoadVideos = new MyLibraryAsynTask(myLibraryInputModel,MyLibraryFragment.this,context);
                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(getActivity(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }


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

                        if (NetworkStatus.getInstance().isConnected(getActivity())) {

                            // default data
                            MyLibraryInputModel myLibraryInputModel=new MyLibraryInputModel();
                            myLibraryInputModel.setAuthToken(authTokenStr);
                            myLibraryInputModel.setUser_id(preferenceManager.getUseridFromPref());
                            myLibraryInputModel.setLimit(String.valueOf(limit));
                            myLibraryInputModel.setOffset(String.valueOf(offset));
                            String countryPref=preferenceManager.getCountryCodeFromPref();
                            if (countryPref != null) {
                                myLibraryInputModel.setCountry(countryPref);
                            }else {
                                myLibraryInputModel.setCountry("IN");
                            }
                            myLibraryInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            MyLibraryAsynTask asyncLoadVideos = new MyLibraryAsynTask(myLibraryInputModel,MyLibraryFragment.this,context);
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
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
                season_id = item.getIsAPV();
                isFreeContent = item.getIsPPV();

                SubTitleName.clear();
                SubTitlePath.clear();

                DataModel dbModel = new DataModel();
                Util.dataModel = dbModel;


                if (moviePermalink.matches(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
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


                    } else if ((movieTypeId.trim().equalsIgnoreCase("3")) && isEpisode.equals("1")) {
                        // Call Load Videos Url to play the Video


                        if (isFreeContent == 1) {
                            /*AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);*/
                            ValidateUserInput validateUserInput = new ValidateUserInput();
                            validateUserInput.setAuthToken(authTokenStr);
                            validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                            validateUserInput.setMuviUniqueId(movieUniqueId.trim());
                            validateUserInput.setPurchaseType("episode");
                            validateUserInput.setSeasonId("" + season_id);
                            validateUserInput.setEpisodeStreamUniqueId(movieStreamUniqueId);
                            validateUserInput.setLanguageCode(languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, MyLibraryFragment.this, context);
                            asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);

                        } else {
                            ValidateUserInput validateUserInput = new ValidateUserInput();
                            validateUserInput.setAuthToken(authTokenStr);
                            validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                            validateUserInput.setMuviUniqueId(movieUniqueId.trim());
                            validateUserInput.setPurchaseType("episode");
                            validateUserInput.setSeasonId("" + season_id);
                            validateUserInput.setEpisodeStreamUniqueId(movieStreamUniqueId);
                            validateUserInput.setLanguageCode(languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, MyLibraryFragment.this, context);
                            asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                        }


                    } else if ((movieTypeId.trim().equalsIgnoreCase("3")) && isEpisode.equals("0") && season_id == 0) {
                        final Intent detailsIntent = new Intent(context, ShowWithEpisodesActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(detailsIntent);
                            }
                        });
                    } else if ((movieTypeId.trim().equalsIgnoreCase("3")) && isEpisode.equals("0") && season_id != 0) {


                        final Intent detailsIntent = new Intent(context, Episode_list_Activity.class);
                        Util.goToLibraryplayer = true;

                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        detailsIntent.putExtra(SEASON_INTENT_KEY, "" + season_id);
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

        return rootView;
    }


    public void onResume() {


        if (url_maps != null && url_maps.size() > 0) {
            url_maps.clear();
        }
        getActivity().invalidateOptionsMenu();
        super.onResume();
        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(getActivity());
        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Get_Video_Details_Output get_video_details_output, int code, String status, String message) {
        if (status == null) {
            status = "0";
            Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
            //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
        }

        if ((status.trim().equalsIgnoreCase("0"))) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }
            Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
            //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_VIDEO_AVAILABLE, DEFAULT_NO_VIDEO_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        } else {

            if (Util.dataModel.getVideoUrl() == null) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_VIDEO_AVAILABLE, DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA))) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_VIDEO_AVAILABLE, DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                }
                if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA))) {
                    final Intent playVideoIntent = new Intent(getActivity(), MyLibraryPlayer.class);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

//                            if(FakeSubTitlePath.size()>0)
//                            {
//                                // This Portion Will Be changed Later.
//
//                                File dir = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + getActivity().getApplicationContext().getPackageName().trim() + "/SubTitleList/");
//                                if (dir.isDirectory())
//                                {
//                                    String[] children = dir.list();
//                                    for (int i = 0; i < children.length; i++)
//                                    {
//                                        new File(dir, children[i]).delete();
//                                    }
//                                }
//
//                                progressBarHandler = new ProgressBarHandler(getActivity());
//                                progressBarHandler.show();
//                                Download_SubTitle(FakeSubTitlePath.get(0).trim());
//                            }
//                            else
                            {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                context.startActivity(playVideoIntent);
                            }

                        }
                    });
                } else {
                    if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")) {
                        if (Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
                            final Intent playVideoIntent = new Intent(getActivity(), ThirdPartyPlayer.class);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    context.startActivity(playVideoIntent);
                                }
                            });
                        } else {

                            final Intent playVideoIntent = new Intent(getActivity(), YouTubeAPIActivity.class);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    context.startActivity(playVideoIntent);
                                }
                            });

                        }
                    } else {
                        final Intent playVideoIntent = new Intent(getActivity(), ThirdPartyPlayer.class);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(playVideoIntent);

                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {
        pDialog = new ProgressBarHandler(getActivity());
        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        if (validateUserOutput == null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        } else if (status <= 0) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }

        if (status > 0) {
            if (status == 425) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);

                dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));

                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                            }
                        });
                dlgAlert.create().show();
            } else if (status == 426) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                            }
                        });
                dlgAlert.create().show();
            } else if (status == 428) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( CROSSED_MAXIMUM_LIMIT, CROSSED_MAXIMUM_LIMIT) + " " + languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
           /*     else if (Util.dataModel.getIsAPV() == 1 && status == 431){

                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }

                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                    if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
                        dlgAlert.setMessage(userMessage);
                    }else{
                        dlgAlert.setMessage(languagePreference.getTextofLanguage(Util.ALREADY_PURCHASE_THIS_CONTENT,Util.DEFAULT_ALREADY_PURCHASE_THIS_CONTENT));

                    }
                    //dlgAlert.setMessage(languagePreference.getTextofLanguage(Util.ALREADY_PURCHASE_THIS_CONTENT,Util.DEFAULT_ALREADY_PURCHASE_THIS_CONTENT)+ " " +getResources().getString(R.string.studio_site));

                    dlgAlert.setTitle(languagePreference.getTextofLanguage(Util.SORRY,Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                    overridePendingTransition(0,0);
                                }
                            });
                    dlgAlert.create().show();

                }*/
            else if (status == 430) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }

                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);

                dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                            }
                        });
                dlgAlert.create().show();


            } else if (status == 427) {


                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                if (message != null && message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(languagePreference.getTextofLanguage( CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                }
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else if (status == 429) {


                if (validateUserOutput.getValiduser_str() == null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);

                    dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                }
                            });
                    dlgAlert.create().show();


                }
                if (validateUserOutput.getValiduser_str() != null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }

                    if ((validateUserOutput.getValiduser_str().trim().equalsIgnoreCase("OK")) || (validateUserOutput.getValiduser_str().trim().matches("OK")) || (validateUserOutput.getValiduser_str().trim().equals("OK")))

                    {
                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(authTokenStr);
                        getVideoDetailsInput.setContent_uniq_id(movieUniqueId);
                        getVideoDetailsInput.setStream_uniq_id(movieStreamUniqueId);
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                        VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MyLibraryFragment.this, context);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                    } else {
                        try {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                        } catch (IllegalArgumentException ex) {
                            status = 0;
                        }
                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);

                            dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));

                            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();


                                        }
                                    });
                            dlgAlert.create().show();
                        }

                    }
                }

            }
        }
    }

    @Override
    public void onMyLibraryPreExecuteStarted() {

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
                videoPDialog.hide();
                footerView.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onMyLibraryPostExecuteCompleted(ArrayList<MyLibraryOutputModel> myLibraryOutputModelArray, int status, String totalItems, String message) {

        String movieImageStr = "";

        for (int i = 0; i < myLibraryOutputModelArray.size(); i++) {
            movieImageStr = myLibraryOutputModelArray.get(i).getPosterUrl();
            String movieName = myLibraryOutputModelArray.get(i).getName();
            String videoTypeIdStr = myLibraryOutputModelArray.get(i).getContentTypesId();
            String movieGenreStr=myLibraryOutputModelArray.get(i).getGenre();
            String moviePermalinkStr=myLibraryOutputModelArray.get(i).getPermalink();
            String isEpisodeStr=myLibraryOutputModelArray.get(i).getIs_episode();
            movieStreamUniqueId=myLibraryOutputModelArray.get(i).getMovie_stream_uniq_id();
            movieUniqueId=myLibraryOutputModelArray.get(i).getMovieId();
            int isConverted=myLibraryOutputModelArray.get(i).getIsConverted();
            int isFreeContent=myLibraryOutputModelArray.get(i).getIsfreeContent();
            int season_id=myLibraryOutputModelArray.get(i).getSeason_id();

            itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, movieUniqueId, movieStreamUniqueId, isConverted, isFreeContent, season_id));
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


                    new RetrieveFeedTask().execute(videoImageStrToHeight);
                    /*    Picasso.with(context).load(videoImageStrToHeight
                        ).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                AsynLOADUI loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {

                            }
                        });*/

                } else {
                    AsynLOADUI loadUI = new AsynLOADUI();
                    loadUI.executeOnExecutor(threadPoolExecutor);
                }
            }
        }
    }

    //Load Films Videos
//    private class AsynLoadVideos extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//        int status;
//        String movieGenreStr = "";
//        String movieName = languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA);
//        String movieImageStr = languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA);
//        String moviePermalinkStr = languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA);
//        String videoTypeIdStr = languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA);
//        String isEpisodeStr = "";
//
//
//        int isConverted = 0;
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            try {
//               /* String urlRouteList ="http://www.idogic.com/rest/MyLibrary";
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken","445882348316089103b8729dcb397c51");
//                httppost.addHeader("user_id","5029");
//
//                httppost.addHeader("limit", String.valueOf(limit));
//                httppost.addHeader("offset", String.valueOf(offset));
//
//                SharedPreferences countryPref = context.getSharedPreferences(COUNTRY_PREF, 0);
//
//                if (countryPref != null) {
//                    String countryCodeStr = countryPref.getString("countryCode", null);
//                    httppost.addHeader("country", countryCodeStr);
//                } else {
//                    httppost.addHeader("country", "IN");
//                }
//                httppost.addHeader("lang_code", languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
//*/
//
//                String urlRouteList = rootUrl().trim() + myLibrary.trim();
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("user_id", preferenceManager.getUseridFromPref());
//
//
//                httppost.addHeader("limit", String.valueOf(limit));
//                httppost.addHeader("offset", String.valueOf(offset));
//
//                String countryCodeStr = preferenceManager.getCountryCodeFromPref();
//
//                if (countryCodeStr != null) {
//                    httppost.addHeader("country", countryCodeStr);
//                } else {
//                    httppost.addHeader("country", "IN");
//                }
//                httppost.addHeader("lang_code", languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (itemData != null) {
//                                noInternetConnectionLayout.setVisibility(View.GONE);
//                                gridView.setVisibility(View.VISIBLE);
//                                noDataLayout.setVisibility(View.GONE);
//                            } else {
//                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
//                                noDataLayout.setVisibility(View.GONE);
//                                gridView.setVisibility(View.GONE);
//                            }
//
//                            footerView.setVisibility(View.GONE);
//                            Toast.makeText(context, languagePreference.getTextofLanguage( SLOW_INTERNET_CONNECTION, DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            noInternetConnectionLayout.setVisibility(View.GONE);
//                            noDataLayout.setVisibility(View.VISIBLE);
//                            footerView.setVisibility(View.GONE);
//                            gridView.setVisibility(View.GONE);
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
////                    itemsInServer = Integer.parseInt(items);
//                }
//
//                if (status > 0) {
//                    if (status == 200) {
//
//                        JSONArray jsonMainNode = myJson.getJSONArray("mylibrary");
//
//                        int lengthJsonArr = jsonMainNode.length();
//                        for (int i = 0; i < lengthJsonArr; i++) {
//                            JSONObject jsonChildNode;
//                            try {
//                                jsonChildNode = jsonMainNode.getJSONObject(i);
//
//                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
//                                    movieGenreStr = jsonChildNode.getString("genre");
//
//                                }
//                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
//                                    movieName = jsonChildNode.getString("name");
//
//                                }
//                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
//                                    movieImageStr = jsonChildNode.getString("poster_url");
//                                    //movieImageStr = movieImageStr.replace("episode", "original");
//
//                                }
//                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
//                                    moviePermalinkStr = jsonChildNode.getString("permalink");
//
//                                }
//                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
//                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");
//
//                                }
//                                //videoTypeIdStr = "1";
//
//                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
//                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));
//
//                                }
//                                if ((jsonChildNode.has("season_id")) && jsonChildNode.getString("season_id").trim() != null && !jsonChildNode.getString("season_id").trim().isEmpty() && !jsonChildNode.getString("season_id").trim().equals("null") && !jsonChildNode.getString("season_id").trim().matches("")) {
//                                    season_id = Integer.parseInt(jsonChildNode.getString("season_id"));
//
//
//                                }
//                                if ((jsonChildNode.has("isFreeContent")) && jsonChildNode.getString("isFreeContent").trim() != null && !jsonChildNode.getString("isFreeContent").trim().isEmpty() && !jsonChildNode.getString("isFreeContent").trim().equals("null") && !jsonChildNode.getString("isFreeContent").trim().matches("")) {
//                                    isFreeContent = Integer.parseInt(jsonChildNode.getString("isFreeContent"));
//
//                                }
//                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
//                                    isEpisodeStr = jsonChildNode.getString("is_episode");
//
//                                }
//
//                                if ((jsonChildNode.has("muvi_uniq_id")) && jsonChildNode.getString("muvi_uniq_id").trim() != null && !jsonChildNode.getString("muvi_uniq_id").trim().isEmpty() && !jsonChildNode.getString("muvi_uniq_id").trim().equals("null") && !jsonChildNode.getString("muvi_uniq_id").trim().matches("")) {
//                                    movieUniqueId = jsonChildNode.getString("muvi_uniq_id");
//                                }
//
//                                if ((jsonChildNode.has("movie_stream_uniq_id")) && jsonChildNode.getString("movie_stream_uniq_id").trim() != null && !jsonChildNode.getString("movie_stream_uniq_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_uniq_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_uniq_id").trim().matches("")) {
//                                    movieStreamUniqueId = jsonChildNode.getString("movie_stream_uniq_id");
//                                }
//
//
//                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, movieUniqueId, movieStreamUniqueId, isConverted, isFreeContent, season_id));
//                            } catch (Exception e) {
//                                getActivity().runOnUiThread(new Runnable() {
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
//                        getActivity().runOnUiThread(new Runnable() {
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
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            noDataLayout.setVisibility(View.VISIBLE);
//                            noInternetConnectionLayout.setVisibility(View.GONE);
//                            gridView.setVisibility(View.GONE);
//                            footerView.setVisibility(View.GONE);
//                        }
//                    });
//                }
//
//                e.printStackTrace();
//
//            }
//            return null;
//
//        }
//
//        protected void onPostExecute(Void result) {
//
//            if (responseStr == null)
//                responseStr = "0";
//            if ((responseStr.trim().equals("0"))) {
//                try {
//                    if (videoPDialog != null && videoPDialog.isShowing()) {
//                        videoPDialog.hide();
//                        videoPDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//
//                    noDataLayout.setVisibility(View.VISIBLE);
//                    noInternetConnectionLayout.setVisibility(View.GONE);
//                    gridView.setVisibility(View.GONE);
//                    footerView.setVisibility(View.GONE);
//                }
//                noDataLayout.setVisibility(View.VISIBLE);
//                noInternetConnectionLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
//                footerView.setVisibility(View.GONE);
//            } else {
//                if (itemData.size() <= 0) {
//                    try {
//                        if (videoPDialog != null && videoPDialog.isShowing()) {
//                            videoPDialog.hide();
//                            videoPDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//
//                        noDataLayout.setVisibility(View.VISIBLE);
//                        noInternetConnectionLayout.setVisibility(View.GONE);
//                        gridView.setVisibility(View.GONE);
//                        footerView.setVisibility(View.GONE);
//                    }
//                    noDataLayout.setVisibility(View.VISIBLE);
//                    noInternetConnectionLayout.setVisibility(View.GONE);
//                    gridView.setVisibility(View.GONE);
//                    footerView.setVisibility(View.GONE);
//                } else {
//                    footerView.setVisibility(View.GONE);
//                    gridView.setVisibility(View.VISIBLE);
//                    noInternetConnectionLayout.setVisibility(View.GONE);
//                    noDataLayout.setVisibility(View.GONE);
//                    videoImageStrToHeight = movieImageStr;
//                    if (firstTime == true) {
//
//
//                        new RetrieveFeedTask().execute(videoImageStrToHeight);
//                    /*    Picasso.with(context).load(videoImageStrToHeight
//                        ).into(new Target() {
//
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                videoWidth = bitmap.getWidth();
//                                videoHeight = bitmap.getHeight();
//                                AsynLOADUI loadUI = new AsynLOADUI();
//                                loadUI.executeOnExecutor(threadPoolExecutor);
//                            }
//
//                            @Override
//                            public void onBitmapFailed(final Drawable errorDrawable) {
//
//                            }
//
//                            @Override
//                            public void onPrepareLoad(final Drawable placeHolderDrawable) {
//
//                            }
//                        });*/
//
//                    } else {
//                        AsynLOADUI loadUI = new AsynLOADUI();
//                        loadUI.executeOnExecutor(threadPoolExecutor);
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()) {
//                videoPDialog = MainActivity.internetSpeedDialog;
//                footerView.setVisibility(View.GONE);
//
//            } else {
//                videoPDialog = new ProgressBarHandler(context);
//
//                if (listSize == 0) {
//                    // hide loader for first time
//
//                    videoPDialog.show();
//                    footerView.setVisibility(View.GONE);
//                } else {
//                    // show loader for first time
//                    videoPDialog.hide();
//                    footerView.setVisibility(View.VISIBLE);
//
//                }
//            }
//        }
//
//
//    }


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

        if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            } else {
                gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            }

        } else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
            } else {
                gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
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
        item.setVisible(false);
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
                        if (density <= 1.5) {
                            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 5);

                        } else {
                            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                        }
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
                        if (density <= 1.5) {
                            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 6);

                        } else {
                            gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                        }
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
        switch (item.getItemId()) {
            case R.id.action_filter:

                // add feature for search activity

                return false;

            default:
                break;
        }

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want
                        if (NetworkStatus.getInstance().isConnected(getActivity())) {

                            MyLibraryInputModel myLibraryInputModel=new MyLibraryInputModel();
                            myLibraryInputModel.setAuthToken(authTokenStr);
                            myLibraryInputModel.setUser_id(preferenceManager.getUseridFromPref());
                            myLibraryInputModel.setLimit(String.valueOf(limit));
                            myLibraryInputModel.setOffset(String.valueOf(offset));
                            String countryPref=preferenceManager.getCountryCodeFromPref();
                            if (countryPref != null) {
                               myLibraryInputModel.setCountry(countryPref);
                            }else {
                                myLibraryInputModel.setCountry("IN");
                            }
                            myLibraryInputModel.setLang_code(languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            MyLibraryAsynTask asyncLoadVideos = new MyLibraryAsynTask(myLibraryInputModel,MyLibraryFragment.this,context);
                            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(getActivity(), languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }
                    } else {
                        getActivity().finish();
                    }
                } else {
                    getActivity().finish();
                }

                return;
            }
        }
    }


//    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        String responseStr;
//        int statusCode;
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(rootUrl().trim()+loadVideoUrl.trim());
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("content_uniq_id",movieUniqueId);
//                httppost.addHeader("stream_uniq_id",movieStreamUniqueId);
//                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
//                httppost.addHeader("user_id",pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//
//
//
//
//             /*   HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost("http://www.idogic.com/rest/GetVideoDetails");
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//
//                httppost.addHeader("authToken","445882348316089103b8729dcb397c51");
//                httppost.addHeader("content_uniq_id",movieUniqueId);
//                httppost.addHeader("stream_uniq_id",movieStreamUniqueId);
//
//
//
//
//                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
//                httppost.addHeader("user_id",pref.getString("PREFS_LOGGEDIN_ID_KEY", null));*/
//
//                // Execute HTTP Post Request
//                try {
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseStr = "0";
//                            dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                            Toast.makeText(getActivity(), languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                }catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    responseStr = "0";
//                    dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                    e.printStackTrace();
//                }
//
//                JSONObject myJson =null;
//                JSONArray SubtitleJosnArray = null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//                if (statusCode >= 0) {
//                    if (statusCode == 200) {
//                        if (myJson.optString("thirdparty_url").trim().equals("") || myJson.optString("thirdparty_url").trim()==null ) {
//                            if ((myJson.has("studio_approved_url")) && myJson.getString("studio_approved_url").trim() != null && !myJson.getString("studio_approved_url").trim().isEmpty() && !myJson.getString("studio_approved_url").trim().equals("null") && !myJson.getString("studio_approved_url").trim().matches("")) {
//                                dataModel.setVideoUrl(myJson.getString("studio_approved_url"));
//                            }
//                           /* if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                dataModel.setVideoUrl(myJson.getString("videoUrl"));
//                            }*/
//                            else{
//                                dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                            }
//                        }else{
//                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
//                                dataModel.setVideoUrl(myJson.getString("thirdparty_url"));
//                            }
//                            else{
//                                dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//
//                            }
//                        }
//                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
//                            dataModel.setVideoResolution(myJson.getString("videoResolution"));
//
//                        }
//                        if ((myJson.has("played_length")) && myJson.getString("played_length").trim() != null && !myJson.getString("played_length").trim().isEmpty() && !myJson.getString("played_length").trim().equals("null") && !myJson.getString("played_length").trim().matches("")) {
//                            dataModel.setPlayPos(isDouble(myJson.getString("played_length")));
//                        }
//
//                        if(SubtitleJosnArray!=null)
//                        {
//                            if(SubtitleJosnArray.length()>0)
//                            {
//                                for(int i=0;i<SubtitleJosnArray.length();i++)
//                                {
//                                    SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
//                                    FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
//
//                                }
//                            }
//                        }
//                    }
//
//                }
//                else {
//
//                    responseStr = "0";
//                    dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                }
//            } catch (JSONException e1) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                e1.printStackTrace();
//            }
//
//            catch (Exception e)
//            {
//
//                responseStr = "0";
//                dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Void result) {
//
//            if (responseStr == null) {
//                responseStr = "0";
//                dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//            }
//
//            if ((responseStr.trim().equalsIgnoreCase("0"))) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                }
//                dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE,DEFAULT_NO_VIDEO_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else {
//
//                if (dataModel.getVideoUrl() == null) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE,DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (dataModel.getVideoUrl().matches("") || dataModel.getVideoUrl().equalsIgnoreCase(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA))) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE,DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA));
//                    }
//                    if (dataModel.getThirdPartyUrl().matches("") || dataModel.getThirdPartyUrl().equalsIgnoreCase(languagePreference.getTextofLanguage(NO_DATA,DEFAULT_NO_DATA))) {
//                        final Intent playVideoIntent = new Intent(getActivity(), MyLibraryPlayer.class);
//                        getActivity().runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                if(FakeSubTitlePath.size()>0)
//                                {
//                                    // This Portion Will Be changed Later.
//
//                                    File dir = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + getActivity().getApplicationContext().getPackageName().trim() + "/SubTitleList/");
//                                    if (dir.isDirectory())
//                                    {
//                                        String[] children = dir.list();
//                                        for (int i = 0; i < children.length; i++)
//                                        {
//                                            new File(dir, children[i]).delete();
//                                        }
//                                    }
//
//                                    progressBarHandler = new ProgressBarHandler(getActivity());
//                                    progressBarHandler.show();
//                                    Download_SubTitle(FakeSubTitlePath.get(0).trim());
//                                }
//                                else
//                                {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    playVideoIntent.putExtra("SubTitleName", SubTitleName);
//                                    playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
//                                    context.startActivity(playVideoIntent);
//                                }
//
//                            }
//                        });
//                    }else{
//                        if (dataModel.getVideoUrl().contains("://www.youtube") || dataModel.getVideoUrl().contains("://www.youtu.be")){
//                            if(dataModel.getVideoUrl().contains("live_stream?channel")) {
//                                final Intent playVideoIntent = new Intent(getActivity(), ThirdPartyPlayer.class);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        context.startActivity(playVideoIntent);
//                                    }
//                                });
//                            }else{
//
//                                final Intent playVideoIntent = new Intent(getActivity(), YouTubeAPIActivity.class);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        context.startActivity(playVideoIntent);
//                                    }
//                                });
//
//                            }
//                        }else{
//                            final Intent playVideoIntent = new Intent(getActivity(), ThirdPartyPlayer.class);
//                            getActivity().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    context.startActivity(playVideoIntent);
//
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            pDialog = new ProgressBarHandler(getActivity());
//            pDialog.show();
//        }
//    }


    public void Download_SubTitle(String Url) {
        new DownloadFileFromURL().execute(Url);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

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
                mediaStorageDir = new File(root + "/Android/data/" + getActivity().getApplicationContext().getPackageName().trim() + "/SubTitleList/", "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("App", "failed to create directory");
                    }
                }

                SubTitlePath.add(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");
                OutputStream output = new FileOutputStream(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");

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
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {
            FakeSubTitlePath.remove(0);
            if (FakeSubTitlePath.size() > 0) {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            } else {

                if (progressBarHandler != null && progressBarHandler.isShowing()) {
                    progressBarHandler.hide();
                }
                Intent playVideoIntent = new Intent(getActivity(), MyLibraryPlayer.class);
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                context.startActivity(playVideoIntent);
            }
        }
    }


//    private class AsynValidateUserDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//
//        int status;
//        String validUserStr;
//        String userMessage;
//        String responseStr;
//        String loggedInIdStr;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            if (pref != null) {
//                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
//            }
//
//
//            try {
//                String urlRouteList = rootUrl().trim() + userValidationUrl.trim();
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("user_id", loggedInIdStr.trim());
//                httppost.addHeader("authToken", authTokenStr.trim());
//                httppost.addHeader("movie_id", movieUniqueId.trim());
//                httppost.addHeader("purchase_type", "episode");
//                httppost.addHeader("season_id", "" + season_id);
//                httppost.addHeader("episode_id", movieStreamUniqueId);
//                httppost.addHeader("lang_code", languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
//
//             /*   String urlRouteList ="http://www.idogic.com/rest/isPPVSubscribed";
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("user_id","5029" );
//                httppost.addHeader("authToken","445882348316089103b8729dcb397c51");
//                httppost.addHeader("movie_id", movieUniqueId.trim());
//                httppost.addHeader("purchase_type","episode");
//                httppost.addHeader("season_id",""+season_id);
//                httppost.addHeader("episode_id",movieStreamUniqueId);
//                httppost.addHeader("lang_code",languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,DEFAULT_SELECTED_LANGUAGE_CODE));
//*/
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    StringBuilder sb = new StringBuilder();
//
//                    BufferedReader reader =
//                            new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
//                    String line = null;
//
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//
//                    responseStr = sb.toString();
//
//
//                } catch (final org.apache.http.conn.ConnectTimeoutException e) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            status = 0;
//                            Toast.makeText(getActivity(), languagePreference.getTextofLanguage( SLOW_INTERNET_CONNECTION, DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    status = 0;
//
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                    validUserStr = myJson.optString("status");
//                    userMessage = myJson.optString("msg");
//
//                }
//
//            } catch (Exception e) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                status = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//            } catch (IllegalArgumentException ex) {
//                status = 0;
//            }
//
//            if (responseStr == null) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else if (status <= 0) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//            if (status > 0) {
//                if (status == 425) {
//
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));
//
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (status == 426) {
//
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));
//
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (status == 428) {
//
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( CROSSED_MAXIMUM_LIMIT, CROSSED_MAXIMUM_LIMIT) + " " + languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));
//
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//           /*     else if (dataModel.getIsAPV() == 1 && status == 431){
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
//                    if (userMessage!=null && !userMessage.equalsIgnoreCase("")){
//                        dlgAlert.setMessage(userMessage);
//                    }else{
//                        dlgAlert.setMessage(languagePreference.getTextofLanguage(ALREADY_PURCHASE_THIS_CONTENT,DEFAULT_ALREADY_PURCHASE_THIS_CONTENT));
//
//                    }
//                    //dlgAlert.setMessage(languagePreference.getTextofLanguage(ALREADY_PURCHASE_THIS_CONTENT,DEFAULT_ALREADY_PURCHASE_THIS_CONTENT)+ " " +getResources().getString(R.string.studio_site));
//
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    finish();
//                                    overridePendingTransition(0,0);
//                                }
//                            });
//                    dlgAlert.create().show();
//
//                }*/
//                else if (status == 430) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));
//
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//
//                                }
//                            });
//                    dlgAlert.create().show();
//
//
//                } else if (status == 427) {
//
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//                    if (userMessage != null && userMessage.equalsIgnoreCase("")) {
//                        dlgAlert.setMessage(userMessage);
//                    } else {
//                        dlgAlert.setMessage(languagePreference.getTextofLanguage( CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
//
//                    }
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (status == 429) {
//
//
//                    if (validUserStr == null) {
//                        try {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                        } catch (IllegalArgumentException ex) {
//                            status = 0;
//                        }
//                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//
//                        dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));
//
//                        dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                        dlgAlert.setCancelable(false);
//                        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//
//
//                                    }
//                                });
//                        dlgAlert.create().show();
//
//
//                    }
//                    if (validUserStr != null) {
//                        try {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                        } catch (IllegalArgumentException ex) {
//                            status = 0;
//                        }
//
//                        if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK")))
//
//                        {
//                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
//                            getVideoDetailsInput.setAuthToken(authTokenStr);
//                            getVideoDetailsInput.setContent_uniq_id(movieUniqueId);
//                            getVideoDetailsInput.setStream_uniq_id(movieStreamUniqueId);
//                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
//                            getVideoDetailsInput.setUser_id(pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//                            VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MyLibraryFragment.this, context);
//                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                        } else {
//                            try {
//                                if (pDialog != null && pDialog.isShowing()) {
//                                    pDialog.hide();
//                                    pDialog = null;
//                                }
//                            } catch (IllegalArgumentException ex) {
//                                status = 0;
//                            }
//                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid"))) {
//                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
//
//                                dlgAlert.setMessage(languagePreference.getTextofLanguage( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO) + " " + languagePreference.getTextofLanguage( APP_ON, DEFAULT_APP_ON) + " " + getResources().getString(R.string.studio_site));
//
//                                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
//                                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
//                                dlgAlert.setCancelable(false);
//                                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//
//
//                                            }
//                                        });
//                                dlgAlert.create().show();
//                            }
//
//                        }
//                    }
//
//                }
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(getActivity());
//            pDialog.show();
//
//
//        }
//
//
//    }


    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressBarHandler phandler;

        protected Void doInBackground(String... urls) {
            try {


                URL url = new URL(urls[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                videoHeight = bmp.getHeight();
                videoWidth = bmp.getWidth();

                return null;
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed

            if (phandler != null && phandler.isShowing()) {
                phandler.hide();
            }

            AsynLOADUI loadUI = new AsynLOADUI();
            loadUI.executeOnExecutor(threadPoolExecutor);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            phandler = new ProgressBarHandler(getActivity());
            phandler.show();

        }
    }


}
