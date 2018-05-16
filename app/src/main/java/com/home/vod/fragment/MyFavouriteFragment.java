package com.home.vod.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.ViewFavouriteAsynTask;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.R;
import com.home.vod.activity.FavoriteActivity;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ShowWithEpisodesActivity;
import com.home.vod.adapter.FavoriteFragmentAdapter1;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.Constant;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.preferences.LanguagePreference.APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.BUTTON_APPLY;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_APPLY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.languageModel;

import com.home.apisdk.apiModel.ViewFavouriteOutputModel;
import com.home.apisdk.apiModel.ViewFavouriteInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.squareup.picasso.Picasso;

/**
 * Created by user on 28-06-2015.
 */
public class MyFavouriteFragment extends Fragment implements
        ViewFavouriteAsynTask.ViewFavouriteListener, DeleteFavAsync.DeleteFavListener {

    public static ProgressBarHandler progressBarHandler;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    LanguagePreference languagePreference;
    FeatureHandler featureHandler;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;
    public static String isEpisodeStr;
    AlertDialog alert;
    ProgressBarHandler pDialog;

    int index;
    String sucessMsg;


    ProgressBarHandler videoPDialog;
    String videoImageStrToHeight;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;

    int videoHeight = 185;
    int videoWidth = 256;
    GridItem itemToPlay;
    Toolbar mActionBarToolbar;
    GridLayoutManager mLayoutManager;

    // private TextView sectionTitle;
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
    public static PreferenceManager preferenceManager;
    //Adapter for GridView
    private FavoriteFragmentAdapter customGridAdapter;
    boolean a = false;


    //Model for GridView
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    String posterUrl, loggedInStr;
    String sectionName;
    String sectionId;
    // UI
    ViewFavouriteAsynTask asyncViewFavorite;
    private GridView gridView;
    // private JazzyGridView gridView;
    RelativeLayout footerView;
    public EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
///

    //////
    Context context;
    public static DeleteFavAsync.DeleteFavListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_favourite_fragment, container, false);
        context = getActivity();
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        final Intent startIntent = new Intent(getActivity(), MainActivity.class);

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                getActivity().startActivity(startIntent);

                                getActivity().finish();

                            }
                        });
                    }
                }
                return false;
            }
        });
        setHasOptionsMenu(true);
        preferenceManager = PreferenceManager.getPreferenceManager(getActivity());
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());
        featureHandler = FeatureHandler.getFeaturePreference(getActivity());
        //((MainActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("sectionName"));
       // ((MainActivity) getActivity()).getSupportActionBar();
        // Kushal - set Id to back button and text in Toolabr
        Toolbar toolbar = ((MainActivity) getActivity()).mToolbar;
        setIdToActionBarBackButton(toolbar);

        TextView sectionTitle = (TextView) rootView.findViewById(R.id.sectionTitle);
        if (getArguments().getString("sectionName") != null) {
            sectionTitle.setText(getArguments().getString("sectionName"));
            // sectionTitle.setText(titleListName);
        }


    /*if (getArguments().getString("title") != null) {
        titleListName = getArguments().getString("title");
        // sectionTitle.setText(titleListName);
    } else {
        //   sectionTitle.setText("");

    }*/
        /*mActionBarToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getIntent().getStringExtra("sectionName"));
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitleColor));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        onBackPressed();
        }
        });

        // Kushal - To set Id to action bar back button
        setIdToActionBarBackButton(mActionBarToolbar);
        if (getIntent().getStringExtra("SectionId") != null) {
        sectionId = getIntent().getStringExtra("SectionId");

        }*/

        loggedInStr = preferenceManager.getUseridFromPref();
        episodeListOptionMenuHandler = new EpisodeListOptionMenuHandler(getActivity());

        isLogin = preferenceManager.getLoginFeatureFromPref();

        // sectionTitle = (TextView) findViewById(R.id.sectionTitle);
        //  FontUtls.loadFont(getActivity(), getResources().getString(R.string.regular_fonts), sectionTitle);
        /*if (getIntent().getStringExtra("sectionName") != null) {
            mActionBarToolbar.setTitle(getIntent().getStringExtra("sectionName"));
        } else {
            mActionBarToolbar.setTitle(languagePreference.getTextofLanguage(MY_FAVOURITE,DEFAULT_MY_FAVOURITE));

        }*/
        posterUrl = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);

        gridView = (GridView) rootView.findViewById(R.id.imagesGridView);
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

        //MUVIlaxmi
        gridView.setVisibility(View.VISIBLE);
      /*  ArrayList<GridItem> tempData = new ArrayList<GridItem>();


        for (int i = 0; i <= 10; i ++){
            tempData.add(new GridItem("","Loading","","","","","","","","",0,0,0));
            float density = getResources().getDisplayMetrics().density;

            if (density >= 3.5 && density <= 4.0){
                customGridAdapter = new GridViewAdapter(getActivity(), R.layout.nexus_videos_grid_layout, itemData, new GridViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(GridItem item) {
                        clickItem(item);

                    }
                });
            }else{
                customGridAdapter = new GridViewAdapter(getActivity(), R.layout.videos_280_grid_layout, itemData, new GridViewAdapter.OnItemClickListener() {
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
                a = true;
                index = i;
                if (customGridAdapter.getItem(i).isSelected() == false) {

                    customGridAdapter.getItem(i).setSelected(true);
                    customGridAdapter.getItem(i).setClicked(true);

                    data_send = itemData.get(i);

                    String url = data_send.getImage();


                } else {
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


                LogUtil.showLog("bibhu", "moviePermalink =" + moviePermalink);
                String movieTypeId = item.getVideoTypeId();
                if (a) {
                    a = false;
                    return;
                } else {

                    if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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
                            final Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                            movieDetailsIntent.putExtra(Constant.PERMALINK_INTENT_KEY, moviePermalink);
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(movieDetailsIntent);
                                }
                            });


                        } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                            final Intent detailsIntent = new Intent(getActivity(), ShowWithEpisodesActivity.class);
                            detailsIntent.putExtra(Constant.PERMALINK_INTENT_KEY, moviePermalink);
                            getActivity().runOnUiThread(new Runnable() {
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

                        if (NetworkStatus.getInstance().isConnected(getActivity())) {

                            // default data

                            ViewFavouriteInputModel viewFavouriteInputModel = new ViewFavouriteInputModel();
                            viewFavouriteInputModel.setAuthToken(authTokenStr);
                            viewFavouriteInputModel.setUser_id(preferenceManager.getUseridFromPref());

                            asyncViewFavorite = new ViewFavouriteAsynTask(viewFavouriteInputModel, MyFavouriteFragment.this, context);
                            asyncViewFavorite.executeOnExecutor(threadPoolExecutor);


                            scrolling = false;

                        }

                    }

                }

            }
        });


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

        if (NetworkStatus.getInstance().isConnected(getActivity())) {

            ViewFavouriteInputModel viewFavouriteInputModel = new ViewFavouriteInputModel();
            viewFavouriteInputModel.setAuthToken(authTokenStr);
            viewFavouriteInputModel.setUser_id(preferenceManager.getUseridFromPref());

            asyncViewFavorite = new ViewFavouriteAsynTask(viewFavouriteInputModel, this, getActivity());
            asyncViewFavorite.executeOnExecutor(threadPoolExecutor);
        } else {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }
        LogUtil.showLog("MUVI", "authtokenn = " + authTokenStr);
        LogUtil.showLog("MUVI", "user id = " + preferenceManager.getUseridFromPref());

        return rootView;
    }

/*

@Override
public void onBackPressed() {
        if (asyncViewFavorite != null) {
        asyncViewFavorite.cancel(true);
        }

        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
        }
*/


    @Override
    public void onViewFavouritePreExecuteStarted() {
        pDialog = new ProgressBarHandler(getActivity());
        pDialog.show();
    }

    @Override
    public void onViewFavouritePostExecuteCompleted(ArrayList<ViewFavouriteOutputModel> viewFavouriteOutputModelArray, int status, int totalItems, String message) {

        LogUtil.showLog("MUVI", "item data ==" + itemData);

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        String movieImageStr = "";

        for (int i = 0; i < viewFavouriteOutputModelArray.size(); i++) {

            String movieName = viewFavouriteOutputModelArray.get(i).getTitle();
            String contentTypesId = viewFavouriteOutputModelArray.get(i).getContentTypesId();
            movieImageStr = viewFavouriteOutputModelArray.get(i).getPoster();
            String moviePermalinkStr = viewFavouriteOutputModelArray.get(i).getPermalink();
            isEpisodeStr = viewFavouriteOutputModelArray.get(i).getIsEpisodeStr();
            movieUniqueId = viewFavouriteOutputModelArray.get(i).getMovieId();
            itemData.add(new GridItem(movieImageStr, movieName, "", contentTypesId, "", "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", 0, 0, 0));
            LogUtil.showLog("MUVI", "item data ==" + itemData);

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

                new RetrieveFeedTask().execute(videoImageStrToHeight);

               /* Picasso.with(this).load(videoImageStrToHeight
                ).error(R.drawable.no_image).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        videoWidth = bitmap.getWidth();
                        videoHeight = bitmap.getHeight();
                        AsynLOADUI loadUI = new AsynLOADUI();
                        loadUI.executeOnExecutor(threadPoolExecutor);
                    }

                    //
                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        LogUtil.showLog("MUVI", "videoImageStrToHeight = " + videoImageStrToHeight);
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

            } else {
                AsynLOADUI loadUI = new AsynLOADUI();
                loadUI.executeOnExecutor(threadPoolExecutor);
            }
        }
    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(getActivity());
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        if (pDialog.isShowing() && pDialog != null) {
            pDialog.hide();
        }
        if (status == 200) {

            this.sucessMsg = sucessMsg;
            showToast();

            LogUtil.showLog("ANU", "REMOVED");
            if (itemData != null && itemData.size() > 0)
                itemData.remove(index);
            gridView.invalidateViews();
            customGridAdapter.notifyDataSetChanged();
            gridView.setAdapter(customGridAdapter);

            Intent Sintent = new Intent("ITEM_STATUS");
            Sintent.putExtra("movie_uniq_id", movieUniqueId);

            LocalBroadcastManager.getInstance(context).sendBroadcast(Sintent);
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
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.videos_grid_layout, itemData);

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }

              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(getActivity(), R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(getActivity(), R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = gridView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


              /*  if (videoWidth > videoHeight) {
                    customGridAdapter = new VideoFilterAdapter(getActivity(), R.layout.videos_280_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                } else {
                    customGridAdapter = new VideoFilterAdapter(getActivity(), R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }*/

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.nexus_videos_grid_layout_land, itemData);
                    } else {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.nexus_videos_grid_layout, itemData);
                    } else {
                        customGridAdapter = new FavoriteFragmentAdapter(getActivity(), R.layout.videos_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                }

                if (mBundleRecyclerViewState != null) {
                    gridView.onRestoreInstanceState(listState);
                }

            }
        }


    }
 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item, item1, item2, item3, item4, item5, item6, item7, item8;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);



        (menu.findItem(R.id.menu_item_language)).setVisible(false);

        item1 = menu.findItem(R.id.menu_item_profile);
        item1.setVisible(true);
        item2 = menu.findItem(R.id.action_purchage);
        item2.setVisible(false);
        item3 = menu.findItem(R.id.action_logout);
        item3.setVisible(false);
        item4 = menu.findItem(R.id.action_login);
        item4.setVisible(false);
        item5 = menu.findItem(R.id.action_register);
        item5.setVisible(false);
        item6 = menu.findItem(R.id.action_mydownload);
        item6.setVisible(false);
        item7 = menu.findItem(R.id.search);
        item7.setVisible(true);
        item8 = menu.findItem(R.id.option);
        item8.setVisible(false);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);

        menu.findItem(R.id.media_route_menu_item).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                final Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:
                // Not implemented here
                return false;

            default:
                break;
        }
        return false;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
/***************chromecast**********************/

        CastButtonFactory.setUpMediaRouteButton(getActivity(), menu, R.id.media_route_menu_item);
        /***************chromecast**********************/
        MenuItem item, item1, item2, item3;
        item = menu.findItem(R.id.action_filter);
        item1 = menu.findItem(R.id.option);
        item2 = menu.findItem(R.id.search);
        item3 = menu.findItem(R.id.media_route_menu_item);
        item.setVisible(false);
        item1.setVisible(false);
        item2.setVisible(true);
        item3.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);

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

    private FavoriteActivity.PlaybackLocation mLocation;
    private FavoriteActivity.PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;
    private boolean mControllersVisible;

    private MenuItem mediaRouteMenuItem;
    private CastContext mCastContext;
    private CastSession mCastSession;

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            // invalidateOptionsMenu();
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
    /*chromecast-------------------------------------*/

    /*****************chromecvast*-------------------------------------*/


    /***************chromecast**********************/
   /* @Override
    protected void onResume() {
        super.onResume();


        //invalidateOptionsMenu();

    }
*/
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_mydownload:

                Intent mydownload = new Intent(getActivity(), MyDownloads.class);
                startActivity(mydownload);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);

                if (languageModel != null && languageModel.size() > 0) {


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
          *//*  case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;*//*
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(getActivity(), PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(SIGN_OUT_WARNING, DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(YES, DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, getActivity(), getActivity());
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(languagePreference.getTextofLanguage(NO, DEFAULT_NO), new DialogInterface.OnClickListener() {

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
    }*/
    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(languagePreference.getTextofLanguage(APP_SELECT_LANGUAGE, DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(languagePreference.getTextofLanguage(BUTTON_APPLY, DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        languageCustomAdapter = new LanguageCustomAdapter(getActivity(), languageModel);
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
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(getActivity(), recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = languageModel.get(position).getLanguageId();


                languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, languageModel.get(position).getLanguageId());
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

                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

    }

    public static class RecyclerTouchListener1 implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FavoriteActivity.ClickListener1 clickListener;

        public RecyclerTouchListener1(Context context, final RecyclerView recyclerView, final FavoriteActivity.ClickListener1 clickListener) {
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


    public void removeFavorite(GridItem gridItem, int pos) {
        index = pos;
        movieUniqueId = gridItem.getMovieUniqueId();

        DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
        deleteFavInputModel.setAuthTokenStr(authTokenStr);
        deleteFavInputModel.setIsEpisode(isEpisodeStr);
        deleteFavInputModel.setMovieUniqueId(movieUniqueId);
        deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, MyFavouriteFragment.this, context);
        deleteFavAsync.executeOnExecutor(threadPoolExecutor);


    }


    public void showToast() {
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg = (TextView) toastRoot.findViewById(R.id.toastMsg);
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
//        toast.setText("Added to Favorites");
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

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

    private void setIdToActionBarBackButton(Toolbar mActionBarToolbar) {
        for (int i = 0; i < mActionBarToolbar.getChildCount(); i++) {
            View v = mActionBarToolbar.getChildAt(i);
            if (v instanceof ImageButton) {
                ImageButton b = (ImageButton) v;
                b.setId(R.id.menu);
                /*try {
                    if (b.getContentDescription().equals("Open")) {
                        b.setId(R.id.drawer_menu);
                    } else {
                        b.setId(R.id.back_btn);
                    }
                }catch (Exception e){
                    b.setId(R.id.back_btn);
                }*/
            } else if (v instanceof TextView) {
                TextView t = (TextView) v;
                if (t.getText().toString().equals(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE))) {
                    t.setId(R.id.page_title_my_favourite);
                }
            }
        }
    }


    ///


    public class FavoriteFragmentAdapter extends ArrayAdapter<GridItem> {
        private int layoutResourceId;
        boolean close = false;
        private ArrayList<GridItem> data = new ArrayList<GridItem>();
        private Activity mActivity;
        LanguagePreference languagePreference;

        public FavoriteFragmentAdapter(Activity mActivity, int layoutResourceId, ArrayList<GridItem> data) {
            super(mActivity, layoutResourceId, data);
            this.mActivity = mActivity;
            this.layoutResourceId = layoutResourceId;
            this.data = data;
            languagePreference = LanguagePreference.getLanguagePreference(mActivity);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            FavoriteFragmentAdapter1.ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = (mActivity).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new FavoriteFragmentAdapter1.ViewHolder();
                holder.title = (TextView) row.findViewById(R.id.movieTitle);
                holder.videoImageview = (ImageView) row.findViewById(R.id.movieImageView);
                holder.closeAlbumArt = (ImageView) row.findViewById(R.id.close_album_art);


                FontUtls.loadFont(mActivity, mActivity.getResources().getString(R.string.regular_fonts), holder.title);
/*
            Typeface castDescriptionTypeface = Typeface.createFromAsset(mActivity.getAssets(),mActivity.getResources().getString(R.string.regular_fonts));
            holder.title.setTypeface(castDescriptionTypeface);*/
           /* int height = holder.videoImageview.getDrawable().getIntrinsicHeight();
            int width = holder.videoImageview.getDrawable().getIntrinsicWidth();

            holder.videoImageview.getLayoutParams().height = height;
            holder.videoImageview.getLayoutParams().width = width;*/

                if ((mActivity.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));

                } else if ((mActivity.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));


                } else if ((mActivity.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {
                    holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));


                } else {
                    holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));


                }
                row.setTag(holder);

            } else {
                holder = (FavoriteFragmentAdapter1.ViewHolder) row.getTag();
            }

            final GridItem item = data.get(position);
            holder.title.setText(item.getTitle());
            String imageId = item.getImage();
            LogUtil.showLog("Nihar_feb", "" + imageId);


            holder.closeAlbumArt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close = true;
                    if (data.get(position).isClicked()) {

                        LogUtil.showLog("ANU", "movieUniqueId  ========" + item.getMovieUniqueId());

                        removeFavorite(item, position);

                    }

                }
            });


            if (data.get(position).isSelected()) {
                holder.closeAlbumArt.setVisibility(View.VISIBLE);
//            feb_bt.setImageResource(R.drawable.favorite);
            } else {
                holder.closeAlbumArt.setVisibility(View.GONE);
//            feb_bt.setImageResource(R.drawable.favorite_unselected);

            }

            if (imageId.matches("") || imageId.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                holder.videoImageview.setImageResource(R.drawable.logo);


            } else {
                Picasso.with(mActivity)
                        .load(imageId)
                        .into(holder.videoImageview);

//            Picasso.with(mActivity)
//                    .load(imageId).error(R.drawable.logo).placeholder(R.drawable.logo)
//                    .into(holder.videoImageview);


          /*  ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.no_thumbnail)
                    .showImageOnFail(R.drawable.no_thumbnail)
                    .showImageOnLoading(R.drawable.no_thumbnail).build();
            ImageAware imageAware = new ImageViewAware(holder.videoImageview, false);
            imageLoader.displayImage(imageId, imageAware,options);*/
            }


            return row;
        }

        class ViewHolder {
            public TextView title;
            public ImageView videoImageview, closeAlbumArt;


        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
            final BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, opt);
            opt.inSampleSize = calculateInSampleSize(opt, reqWidth, reqHeight);
            opt.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, opt);
        }

        public int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight) {
            final int height = opt.outHeight;
            final int width = opt.outWidth;
            int sampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfWidth = width / 2;
                final int halfHeight = height / 2;
                while ((halfHeight / sampleSize) > reqHeight && (halfWidth / sampleSize) > reqWidth) {
                    sampleSize *= 2;
                }

            }
            return sampleSize;
        }
    }
}