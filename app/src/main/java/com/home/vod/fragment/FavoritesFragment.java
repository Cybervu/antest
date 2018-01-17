package com.home.vod.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.ViewFavouriteAsynTask;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.ViewFavouriteInputModel;
import com.home.apisdk.apiModel.ViewFavouriteOutputModel;
import com.home.vod.Episode_Programme_Handler;
import com.home.vod.R;
import com.home.vod.Single_Part_Programme_Handler;
import com.home.vod.adapter.DigiOsmosisFavoriteAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import player.utils.Util;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class FavoritesFragment extends Fragment implements ViewFavouriteAsynTask.ViewFavouriteListener,DeleteFavAsync.DeleteFavListener{

    String isEpisodeStr = "";
    int videoHeight = 185;
    int videoWidth = 256;
    Bundle bundle;
    private ArrayList<String> url;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    Context context;
    String videoUrlStr;
    TextView nocontent;
    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;
    int itemcount = 0;
    boolean firstTime = false;
    boolean a = false;
    GridItem data_send;
    String sucessMsg;
    String loggedInStr;
    SharedPreferences pref;
    int index;
    //data to load videourl
    private String movieUniqueId;
    private String movieStreamUniqueId;
    // String videoUrlStr;
    String videoResolution = "BEST";
    ProgressBarHandler pDialog,videoPDialog;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    int scrolledPosition = 0;
    boolean scrolling;
    String videoImageStrToHeight;
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    PreferenceManager preferenceManager;
    ViewFavouriteAsynTask asyncViewFavorite;
    //search
    String searchTextStr;
    boolean isSearched = false;
    RelativeLayout noDataLayout;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    TextView noDataTextView;
    TextView noInternetTextView;
    RelativeLayout footerView;
    private RelativeLayout noInternetConnectionLayout;
    GridItem itemToPlay;
//    private DigiOsmosisFavoriteHandler favoriteLayoutHanlder;
    private DigiOsmosisFavoriteAdapter customGridAdapter;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favorite_tablayout, container, false);
        mGridView = (GridView) layout.findViewById(R.id.imagesGridView);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        url = new ArrayList<>();
        context = getActivity();
        preferenceManager = PreferenceManager.getPreferenceManager(getActivity());
        pref = context.getSharedPreferences(Util.LOGIN_PREF, 0);
        loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
        bundle = getArguments();
        footerView = (RelativeLayout) layout.findViewById(R.id.loadingPanel);
       /* favoriteLayoutHanlder = new DigiOsmosisFavoriteHandler(this);
        favoriteLayoutHanlder.handleTitle();*/
        noInternetConnectionLayout = (RelativeLayout) layout.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) layout.findViewById(R.id.noData);
        noInternetTextView = (TextView) layout.findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) layout.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(context, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(context, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);

        LogUtil.showLog("MUVI","favorite calling");
        ViewFavouriteInputModel viewFavouriteInputModel = new ViewFavouriteInputModel();
        viewFavouriteInputModel.setAuthToken(authTokenStr);
        viewFavouriteInputModel.setUser_id(preferenceManager.getUseridFromPref());

        asyncViewFavorite = new ViewFavouriteAsynTask(viewFavouriteInputModel,this,context);
        asyncViewFavorite.execute();

        LogUtil.showLog("MUVI","authtokenn = "+Util.authTokenStr);
        LogUtil.showLog("MUVI","user id = "+preferenceManager.getUseridFromPref());




        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (mGridView.getLastVisiblePosition() >= itemsInServer - 1) {
                    return;

                }

                if (view.getId() == mGridView.getId()) {
                    final int currentFirstVisibleItem = mGridView.getFirstVisiblePosition();

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
                        if (mGridView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;
                        boolean isNetwork = Util.checkNetwork(context);
                        if (isNetwork == true) {

                            // default data
                          /*  ViewFavouriteInputModel viewFavouriteInputModel = new ViewFavouriteInputModel();
                            viewFavouriteInputModel.setAuthToken(authTokenStr);
                            viewFavouriteInputModel.setUser_id(preferenceManager.getUseridFromPref());

                            asyncViewFavorite = new ViewFavouriteAsynTask(viewFavouriteInputModel,this,context);
                            asyncViewFavorite.execute();

*/
                            scrolling = false;

                        }

                    }

                }

            }
        });


        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                a = true;
                index = i;

                Log.v("SUBHALAXMI","setOnItemLongClickListener"+ a);

                if (customGridAdapter.getItem(i).isSelected() == false) {

                    customGridAdapter.getItem(i).setSelected(true);
                    customGridAdapter.getItem(i).setClicked(true);
                    Log.v("SUBHALAXMI","data_send === "+ data_send);

                    data_send = itemData.get(i);

                    String url = data_send.getImage();


                } else {
                    customGridAdapter.getItem(i).setSelected(false);

                }
                customGridAdapter.notifyDataSetChanged();
                return true;
            }
        });


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridItem item = itemData.get(position);
                itemToPlay = item;
                String posterUrl = item.getImage();
                String movieName = item.getTitle();
                String movieGenre = item.getMovieGenre();
                String moviePermalink = item.getPermalink();


                LogUtil.showLog("bibhu","moviePermalink ="+moviePermalink);
                String movieTypeId = item.getVideoTypeId();
                if (a){
                    a=false;
                    return;
                }
                else{

                    if (moviePermalink.matches(Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                        dlgAlert.setMessage(Util.getTextofLanguage(context, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                        dlgAlert.setTitle(Util.getTextofLanguage(context, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(context, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(context, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();

                    } else {

                        if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                           /* final Intent movieDetailsIntent = new Intent(FavoriteActivity.this, MovieDetailsActivity.class);
                            movieDetailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(movieDetailsIntent);
                                }
                            });*/
                            new Single_Part_Programme_Handler(context).handleIntent(PERMALINK_INTENT_KEY,moviePermalink);


                        } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                            new Episode_Programme_Handler(context).handleIntent(PERMALINK_INTENT_KEY,moviePermalink);
                        }
                    }
                }



            }
        });

        return layout;

    }


    public  void removeFavorite(GridItem gridItem,int pos){
        movieUniqueId = gridItem.getMovieUniqueId();
        index = pos;
        DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
        deleteFavInputModel.setAuthTokenStr(authTokenStr);
        deleteFavInputModel.setIsEpisode(isEpisodeStr);
        deleteFavInputModel.setMovieUniqueId(movieUniqueId);
        deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());

        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel,this,context);
        deleteFavAsync.execute();


    }
    @Override
    public void onViewFavouritePreExecuteStarted() {
        pDialog = new ProgressBarHandler(context);
        pDialog.show();

    }

    @Override
    public void onViewFavouritePostExecuteCompleted(ArrayList<ViewFavouriteOutputModel> viewFavouriteOutputModelArray, int status, int totalItems, String message) {

        LogUtil.showLog("MUVI","item data =="+ itemData);

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
            movieImageStr = viewFavouriteOutputModelArray.get(i).getPoster();
            String moviePermalinkStr = viewFavouriteOutputModelArray.get(i).getPermalink();
            isEpisodeStr = viewFavouriteOutputModelArray.get(i).getIsEpisodeStr();
            movieUniqueId = viewFavouriteOutputModelArray.get(i).getMovieId();
            if(contentTypesId.trim().equalsIgnoreCase("1") || contentTypesId.trim().equalsIgnoreCase("2") || contentTypesId.trim().equalsIgnoreCase("4") ) {
                itemData.add(new GridItem(movieImageStr, movieName, "", contentTypesId, "", "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", 0, 0, 0, viewFavouriteOutputModelArray.get(i).getStory()));
                LogUtil.showLog("MUVI", "item data ==" + itemData);
            }
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
                mGridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }
            noDataLayout.setVisibility(View.VISIBLE);
            noInternetConnectionLayout.setVisibility(View.GONE);
            mGridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);
            videoImageStrToHeight = movieImageStr;
            if (firstTime == true) {
                Picasso.with(getActivity()).load(videoImageStrToHeight
                ).error(R.drawable.no_image).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        videoWidth = bitmap.getWidth();
                        videoHeight = bitmap.getHeight();
                        AsynLOADUI loadUI = new AsynLOADUI();
                        loadUI.execute();
                    }

                    //
                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        LogUtil.showLog("MUVI", "videoImageStrToHeight = " + videoImageStrToHeight);
                        videoImageStrToHeight = "https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png";
                        videoWidth = errorDrawable.getIntrinsicWidth();
                        videoHeight = errorDrawable.getIntrinsicHeight();
                        AsynLOADUI loadUI = new AsynLOADUI();
                        loadUI.execute();

                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {

                    }
                });

            } else {
                AsynLOADUI loadUI = new AsynLOADUI();
                loadUI.execute();
            }
        }
    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(context);
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        if (pDialog.isShowing() && pDialog != null) {
            pDialog.hide();
        }
        if (status==200) {

            this.sucessMsg=sucessMsg;
            showToast();

            LogUtil.showLog("ANU", "REMOVED"+index);
            // gridView.setAdapter(customGridAdapter);


            itemData.remove(index);
            mGridView.invalidateViews();
            customGridAdapter.notifyDataSetChanged();
            mGridView.setAdapter(customGridAdapter);



            Intent Sintent = new Intent("ITEM_STATUS");
            Sintent.putExtra("movie_uniq_id", movieUniqueId);

            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(Sintent);
        }
    }



    public void showToast() {

        Context context = getActivity();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater(getArguments());

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg = (TextView) toastRoot.findViewById(R.id.toastMsg);
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

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
                    mGridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }

                mGridView.smoothScrollToPosition(0);
                firstTime = false;
                ViewGroup.LayoutParams layoutParams = mGridView.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
                mGridView.setLayoutParams(layoutParams);
                mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                mGridView.setGravity(Gravity.CENTER_HORIZONTAL);

                if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    if (videoWidth > videoHeight) {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    } else {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);
                    } else {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                } else {
                    if (videoWidth > videoHeight) {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    } else {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                    }

                } if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    if (videoWidth > videoHeight) {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    } else {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);
                    } else {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    }

                } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                } else {
                    if (videoWidth > videoHeight) {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    } else {
                        mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                    }

                }
                /*customGridAdapter = favoriteLayoutHanlder.handleLayout(customGridAdapter, itemData, mGridView, videoWidth,videoHeight);
                mGridView.setAdapter(customGridAdapter);*/

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(context, R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });
                    } else {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData , new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });

                    }
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });
                    } else {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                }
                mGridView.setAdapter(customGridAdapter);



            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = mGridView.onSaveInstanceState();
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
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });
                    } else {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });

                    }
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });
                    } else {
                        customGridAdapter = new DigiOsmosisFavoriteAdapter(getActivity(), R.layout.favorite_tablisting, itemData, new DigiOsmosisFavoriteAdapter.closelistener() {
                            @Override
                            public void onCloseItemClick(int pos) {
                                removeFavorite(data_send,pos);
                            }
                        });

                    }
                    // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                }
                mGridView.setAdapter(customGridAdapter);
              /*  customGridAdapter = favoriteLayoutHanlder.handleLayout(customGridAdapter, itemData, mGridView, videoWidth,videoHeight);
                mGridView.setAdapter(customGridAdapter);*/

                if (mBundleRecyclerViewState != null) {
                    mGridView.onRestoreInstanceState(listState);
                }

            }
        }


    }



}