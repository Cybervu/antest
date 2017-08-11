package com.home.vod.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.ViewFavouriteAsynTask;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.ViewFavouriteInputModel;
import com.home.apisdk.apiModel.ViewFavouriteOutputModel;
import com.home.vod.R;
import com.home.vod.adapter.FavoriteAdapter;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.model.LanguageModel;
import com.home.vod.preferences.PreferenceManager;
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

public class FavoriteActivity extends AppCompatActivity implements GetLanguageListAsynTask.GetLanguageList,ViewFavouriteAsynTask.ViewFavouriteListener,
        LogoutAsynctask.Logout, GetTranslateLanguageAsync.GetTranslateLanguageInfoListner,DeleteFavAsync.DeleteFavListener{
    public static ProgressBarHandler progressBarHandler;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;
    String isEpisodeStr;
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
PreferenceManager preferenceManager;
    //Adapter for GridView
    private FavoriteAdapter customGridAdapter;
    boolean a=false;


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
    ///

    //////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        /////


        /////
        preferenceManager = PreferenceManager.getPreferenceManager(this);
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

        loggedInStr =preferenceManager.getUseridFromPref();

        isLogin = preferenceManager.getLoginFeatureFromPref();

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

                            ViewFavouriteInputModel viewFavouriteInputModel = new ViewFavouriteInputModel();
                            viewFavouriteInputModel.setAuthToken(Util.authTokenStr);
                            viewFavouriteInputModel.setUser_id(preferenceManager.getUseridFromPref());

                            asyncViewFavorite = new ViewFavouriteAsynTask(viewFavouriteInputModel,FavoriteActivity.this,FavoriteActivity.this);
                            asyncViewFavorite.executeOnExecutor(threadPoolExecutor);




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


        Log.v("SUBHA","favorite calling");
        ViewFavouriteInputModel viewFavouriteInputModel = new ViewFavouriteInputModel();
        viewFavouriteInputModel.setAuthToken(Util.authTokenStr);
        viewFavouriteInputModel.setUser_id(preferenceManager.getUseridFromPref());

        asyncViewFavorite = new ViewFavouriteAsynTask(viewFavouriteInputModel,FavoriteActivity.this,FavoriteActivity.this);
        asyncViewFavorite.executeOnExecutor(threadPoolExecutor);

        Log.v("SUBHA","authtokenn = "+Util.authTokenStr);
        Log.v("SUBHA","user id = "+preferenceManager.getUseridFromPref());




    }




    @Override
    public void onBackPressed() {
        if (asyncViewFavorite != null) {
            asyncViewFavorite.cancel(true);
        }

        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {

    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

    }



    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(FavoriteActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {

        if (status == null) {
            Toast.makeText(FavoriteActivity.this, com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.SIGN_OUT_ERROR, com.home.vod.util.Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(FavoriteActivity.this, com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.SIGN_OUT_ERROR, com.home.vod.util.Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.IS_ONE_STEP_REGISTRATION, com.home.vod.util.Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(FavoriteActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(FavoriteActivity.this, com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.LOGOUT_SUCCESS, com.home.vod.util.Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(FavoriteActivity.this, com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.LOGOUT_SUCCESS, com.home.vod.util.Util.DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(FavoriteActivity.this, com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.SIGN_OUT_ERROR, com.home.vod.util.Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(FavoriteActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

    }



    @Override
    public void onViewFavouritePreExecuteStarted() {
        pDialog = new ProgressBarHandler(FavoriteActivity.this);
        pDialog.show();
    }

    @Override
    public void onViewFavouritePostExecuteCompleted(ArrayList<ViewFavouriteOutputModel> viewFavouriteOutputModelArray, int status, int totalItems, String message) {

        Log.v("SUBHA","item data =="+ itemData);

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        }catch (IllegalArgumentException ex) {

        }

        String movieImageStr="";

        for (int i = 0; i < viewFavouriteOutputModelArray.size(); i++) {

            String movieName = viewFavouriteOutputModelArray.get(i).getTitle();
            String contentTypesId = viewFavouriteOutputModelArray.get(i).getContentTypesId();
//            String movieGenreStr = viewFavouriteOutputModelArray.get(i).get();
            movieImageStr = viewFavouriteOutputModelArray.get(i).getPoster();
            String moviePermalinkStr = viewFavouriteOutputModelArray.get(i).getPermalink();
            String isEpisodeStr = viewFavouriteOutputModelArray.get(i).getIsEpisodeStr();
//            int isConverted = viewFavouriteOutputModelArray.get(i).getIsConverted();
//            int isPPV = viewFavouriteOutputModelArray.get(i).getIsPPV();
//            int isAPV = viewFavouriteOutputModelArray.get(i).getIsAPV();

            itemData.add(new GridItem(movieImageStr, movieName, "", contentTypesId, "", "", moviePermalinkStr, isEpisodeStr, "", "", 0, 0, 0));
         Log.v("SUBHA","item data =="+ itemData);

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
                Picasso.with(this).load(videoImageStrToHeight
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
                        Log.v("MUVI", "videoImageStrToHeight = " + videoImageStrToHeight);
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

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(FavoriteActivity.this);
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {
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
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        String id = preferenceManager.getUseridFromPref();
        String email = preferenceManager.getEmailIdFromPref();
        SharedPreferences language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        if(preferenceManager.getLanguageListFromPref().equals("1"))
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
                Default_Language = com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.SELECTED_LANGUAGE_CODE, com.home.vod.util.Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = com.home.vod.util.Util.getTextofLanguage(FavoriteActivity.this, com.home.vod.util.Util.SELECTED_LANGUAGE_CODE, com.home.vod.util.Util.DEFAULT_SELECTED_LANGUAGE_CODE);

                if (com.home.vod.util.Util.languageModel!=null && com.home.vod.util.Util.languageModel.size() > 0){


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(com.home.vod.util.Util.authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
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
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(com.home.vod.util.Util.authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(Util.getTextofLanguage(FavoriteActivity.this, Util.SELECTED_LANGUAGE_CODE, com.home.vod.util.Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, FavoriteActivity.this, FavoriteActivity.this);
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



                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(com.home.vod.util.Util.authTokenStr);
                    languageListInputModel.setLangCode(Default_Language);

                    GetTranslateLanguageAsync getTranslateLanguageAsync = new GetTranslateLanguageAsync(languageListInputModel,FavoriteActivity.this,FavoriteActivity.this);
                    getTranslateLanguageAsync.executeOnExecutor(threadPoolExecutor);

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



    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }





    public  void removeFavorite(GridItem gridItem){
        movieUniqueId = gridItem.getMovieUniqueId();

        DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
        deleteFavInputModel.setAuthTokenStr(Util.authTokenStr);
        deleteFavInputModel.setIsEpisode(isEpisodeStr);
        deleteFavInputModel.setMovieUniqueId(movieUniqueId);
        deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());

        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel,FavoriteActivity.this,FavoriteActivity.this);
        deleteFavAsync.executeOnExecutor(threadPoolExecutor);


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

