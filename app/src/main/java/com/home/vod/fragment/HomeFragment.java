package com.home.vod.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.release.muvisdk.api.apiController.GetAppHomePageAsync;
import com.release.muvisdk.api.apiController.GetLoadVideosAsync;
import com.release.muvisdk.api.apiModel.AppHomePageOutput;
import com.release.muvisdk.api.apiModel.HomePageBannerModel;
import com.release.muvisdk.api.apiModel.HomePageInputModel;
import com.release.muvisdk.api.apiModel.HomePageSectionModel;
import com.release.muvisdk.api.apiModel.LoadVideoInput;
import com.release.muvisdk.api.apiModel.LoadVideoOutput;
import com.home.vod.R;
import com.home.vod.activity.MainActivity;
import com.home.vod.adapter.RecyclerViewDataAdapter;
import com.home.vod.model.GetMenuItem;
import com.home.vod.model.SectionDataModel;
import com.home.vod.model.SingleItemModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
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
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.util.Constant.authTokenStr;

/**
 * Created by Muvi on 11/24/2016.
 */
public class HomeFragment extends Fragment implements GetLoadVideosAsync.LoadVideosAsyncListener, GetAppHomePageAsync.HomePageListener {

//    int bannerArray[] = {R.drawable.banner1};
    int videoHeight = 185;
    int videoWidth = 256;

    GetLoadVideosAsync asynLoadVideos;
    AsynLOADUI loadui;
    View rootView;
    int item_CountOfSections = 0;
    boolean isFirstTime = false;
    int counter = 0;
    ArrayList<GetMenuItem> menuList;
    ArrayList<String> url_maps;

    private ProgressBarHandler mProgressBarHandler = null;

    //private ProgressDialog videoPDialog = null;
    private RelativeLayout noInternetLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;
    LanguagePreference languagePreference;

    RecyclerView my_recycler_view;
    Context context;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;


    ArrayList<SectionDataModel> allSampleData;
    ArrayList<SingleItemModel> singleItem;

    //AsynLoadImageUrls as = null;
    GetAppHomePageAsync asynLoadMenuItems = null;
    /* int bannerArray[] = {R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
     int bannerL[] = {R.drawable.banner1_l,R.drawable.banner2_l,R.drawable.banner3_l};*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    private String imageUrlStr;
    private int listSize = 0;
    RecyclerView recycler_view_list;
    private boolean firstTime = false;
    RecyclerViewDataAdapter adapter;
    LinearLayoutManager mLayoutManager;
    RelativeLayout footerView;
    int bannerLoaded = 0;
    RelativeLayout sliderRelativeLayout;
    private SliderLayout mDemoSlider;
    String videoImageStrToHeight;
    int ui_completed = 0;
    int loading_completed = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rootView = v;
        context = getActivity();
        setHasOptionsMenu(true);
        Util.image_orentiation.clear();
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());
        LogUtil.showLog("MUVI", "device_id already created =" + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        String GOOGLE_FCM_TOKEN;
        // LogUtil.showLog("MUVI", "google_id already created =" + languagePreference.getTextofLanguage( GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));



 /*       *//***************chromecast**********************//*

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

*//***************chromecast**********************//*
*/
        allSampleData = new ArrayList<SectionDataModel>();
        // createDummyData();
        footerView = (RelativeLayout) v.findViewById(R.id.loadingPanel);
        my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        sliderRelativeLayout = (RelativeLayout) v.findViewById(R.id.sliderRelativeLayout);
        mDemoSlider = (SliderLayout) v.findViewById(R.id.sliderLayout);

        sliderRelativeLayout.setVisibility(View.GONE);
        noInternetLayout = (RelativeLayout) rootView.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) rootView.findViewById(R.id.noData);
        noInternetTextView = (TextView) rootView.findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) rootView.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));

        footerView.setVisibility(View.GONE);

        my_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        if (NetworkStatus.getInstance().isConnected(getActivity())) {
            // default data
            menuList = new ArrayList<GetMenuItem>();

            url_maps = new ArrayList<String>();

            HomePageInputModel homePageInputModel = new HomePageInputModel();
            homePageInputModel.setAuthToken(authTokenStr);
            homePageInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            asynLoadMenuItems = new GetAppHomePageAsync(homePageInputModel, this, context);
            asynLoadMenuItems.executeOnExecutor(threadPoolExecutor);


        }
        return v;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        MenuItem item;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);
      /*  *//***************chromecast**********************//*


        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getActivity(), menu, R.id.media_route_menu_item);
        showIntroductoryOverlay();

        *//***************chromecast**********************/

        super.onCreateOptionsMenu(menu, inflater);

    }


  /*  private void StartAsyncTaskInParallel(AsynLoadMenuItems task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }*/


    @Override
    public void onLoadVideosAsyncPreExecuteStarted() {

        if (firstTime == false) {


            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler = new ProgressBarHandler(context);
                        mProgressBarHandler.show();
                    }
                });

            }

        } else {
               /* if (counter >= 0 && counter >= menuList.size()-1) {
                    LogUtil.showLog("MUVI","COUNTER");
                    loading_completed = true;
                }*/
        }
    }

    @Override
    public void onLoadVideosAsyncPostExecuteCompleted(ArrayList<LoadVideoOutput> loadVideoOutputs, int code, String status) {

        try {
            if (mProgressBarHandler != null && mProgressBarHandler.isShowing()) {
                mProgressBarHandler.hide();
                mProgressBarHandler = null;
            }
        } catch (IllegalArgumentException ex) {

        }
        String movieImageStr = "";

        singleItem = new ArrayList<SingleItemModel>();

        for (int i = 0; i < loadVideoOutputs.size(); i++) {
            movieImageStr = loadVideoOutputs.get(i).getPoster_url();
            String movieName = loadVideoOutputs.get(i).getName();
            String videoTypeIdStr = loadVideoOutputs.get(i).getContent_types_id();
            String movieGenreStr = loadVideoOutputs.get(i).getGenre();
            String moviePermalinkStr = loadVideoOutputs.get(i).getPermalink();
            String isEpisodeStr = loadVideoOutputs.get(i).getIs_episode();
            int isConverted = loadVideoOutputs.get(i).getIs_converted();
            int isPPV = loadVideoOutputs.get(i).getIs_ppv();
            int isAPV = loadVideoOutputs.get(i).getIs_advance();


            singleItem.add(new SingleItemModel(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV));
        }

        allSampleData.add(new SectionDataModel(menuList.get(counter).getName(), menuList.get(counter).getSectionId(), singleItem));


        if (NetworkStatus.getInstance().isConnected(getActivity())) {

            if (getActivity() != null) {
                new RetrieveFeedTask().execute(movieImageStr);

            }


        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
        }


        return;

    }


    private class AsynLOADPicasso extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Picasso.with(context).load(videoImageStrToHeight).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    videoWidth = bitmap.getWidth();
                    videoHeight = bitmap.getHeight();
                    loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            return null;
        }

        protected void onPostExecute(Void result) {

        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            //ui_completed = ui_completed + 1;

            if (videoWidth > videoHeight) {

                Util.image_orentiation.add(0);

            } else {
                Util.image_orentiation.add(1);

            }

            LogUtil.showLog("MUVI", "HHH" + videoWidth + videoHeight);
            LogUtil.showLog("MUVI", "vertical" + MainActivity.vertical);

            if (getView() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            if (firstTime == false) {

                if (mProgressBarHandler != null) {
                    mProgressBarHandler.hide();
                    mProgressBarHandler = null;
                }
                firstTime = true;

                if (adapter != null) {

                    adapter.notifyDataSetChanged();
                } else { // it works first time
                    adapter = new RecyclerViewDataAdapter(context, allSampleData, url_maps, firstTime, MainActivity.vertical);
                    //   adapter = new AdapterClass(context,list);
                    my_recycler_view.setAdapter(adapter);
                }

            } else {
                if (mProgressBarHandler != null) {
                    mProgressBarHandler.hide();
                    mProgressBarHandler = null;
                }


                if (counter >= 0 && counter >= menuList.size() - 1) {
                    footerView.setVisibility(View.GONE);
                }

                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = my_recycler_view.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
                if (mBundleRecyclerViewState != null) {
                    my_recycler_view.getLayoutManager().onRestoreInstanceState(listState);
                }
            }

      /*      my_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LogUtil.showLog("MUVI", "onScrollStateChanged");
                   *//* if (counter >= 0 && counter >= menuList.size()-1) {
                        adapter.swapItems();
                    }*//*

                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                       LogUtil.showLog("MUVI","ONSCROLL");
                  *//*  if (counter >= 0 && counter >= menuList.size()-1) {
                        adapter.swapItems();
                    }*//*

                }
            });*/
            if (counter >= 0 && counter < menuList.size() - 1) {
                counter = counter + 1;
                if (NetworkStatus.getInstance().isConnected(getActivity())) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                footerView.setVisibility(View.VISIBLE);

                            }
                        });
                    }




                /*    loading_completed = loading_completed + 1;
                    LogUtil.showLog("MUVI","loading_completed"+loading_completed);
                    LogUtil.showLog("MUVI","ui_completed"+ui_completed);*/

                    // default data
                    LoadVideoInput loadVideoInput = new LoadVideoInput();
                    loadVideoInput.setAuthToken(authTokenStr);
                    loadVideoInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    loadVideoInput.setSection_id(menuList.get(counter).getSectionId());
                    asynLoadVideos = new GetLoadVideosAsync(loadVideoInput, HomeFragment.this, context);
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor);

                } else {
                    noInternetLayout.setVisibility(View.VISIBLE);
                }
            }


        }

        @Override
        protected void onPreExecute() {

        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save RecyclerView state
        if (my_recycler_view != null) {
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mLayoutManager.onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

        }
        super.onSaveInstanceState(outState);

       /* if (my_recycler_view!=null && my_recycler_view.getLayoutManager().onSaveInstanceState()!=nu) {
            Parcelable listState = my_recycler_view.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }*/
    }

    @Override
    public void onHomePagePreExecuteStarted() {
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        mProgressBarHandler.show();
    }

    @Override
    public void onHomePagePostExecuteCompleted(AppHomePageOutput appHomePageOutput, int status, String message) {

        if (mProgressBarHandler != null) {
            mProgressBarHandler.hide();
            mProgressBarHandler = null;
        }

        if (status == 200) {

            if (singleItem != null && singleItem.size() > 0) {
                singleItem.clear();
            }

            if (allSampleData != null && allSampleData.size() > 0) {
                allSampleData.clear();
            }
            for (HomePageBannerModel model : appHomePageOutput.getHomePageBannerModels()) {
                url_maps.add(model.getImage_path());
            }

            for (HomePageSectionModel section : appHomePageOutput.getHomePageSectionModel()) {
                menuList.add(new GetMenuItem(section.getTitle(), section.getSection_id(), section.getStudio_id(), section.getLanguage_id()));
            }


            if (NetworkStatus.getInstance().isConnected(getActivity())) {

                my_recycler_view.setLayoutManager(mLayoutManager);
                adapter = new RecyclerViewDataAdapter(context, allSampleData, url_maps, firstTime, MainActivity.vertical);
                my_recycler_view.setAdapter(adapter);
                my_recycler_view.setVisibility(View.VISIBLE);


                LoadVideoInput loadVideoInput = new LoadVideoInput();
                loadVideoInput.setAuthToken(authTokenStr);
                loadVideoInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                loadVideoInput.setSection_id(menuList.get(counter).getSectionId());
                asynLoadVideos = new GetLoadVideosAsync(loadVideoInput, HomeFragment.this, context);
                asynLoadVideos.executeOnExecutor(threadPoolExecutor);
                // default data
                    /*asynLoadVideos = new AsynLoadVideos();
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor,menuList.get(counter).getSectionId());*/

            } else {
                noInternetLayout.setVisibility(View.VISIBLE);
            }

        } else {
//            url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");

            for (HomePageBannerModel model : appHomePageOutput.getHomePageBannerModels()) {
                url_maps.add(model.getImage_path());
            }

            if (firstTime == false) {
                firstTime = true;

                if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                    for (int j = 0; j < url_maps.size(); j++) {
                        DefaultSliderView textSliderView = new DefaultSliderView(context);
                        textSliderView
                                .description("")
                                .image(url_maps.get(j))
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        // .setOnSliderClickListener(this);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra", "");

                        mDemoSlider.addSlider(textSliderView);
                    }
                } else {
                    for (int j = 0; j < url_maps.size(); j++) {
                        DefaultSliderView textSliderView = new DefaultSliderView(context);
                        textSliderView
                                .description("")
                                .image(url_maps.get(j))
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        // .setOnSliderClickListener(this);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra", "");

                        mDemoSlider.addSlider(textSliderView);
                    }
                }
            }
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(10000);
            //   mDemoSlider.addOnPageChangeListener(this);
            mDemoSlider.getPagerIndicator().setVisibility(View.INVISIBLE);

            sliderRelativeLayout.setVisibility(View.VISIBLE);

        }
        return;
    }

  /* class AsynLoadMenuItems extends AsyncTask<Void, Void, Void> implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


        String responseStr;
        int statusCode;
        String title;
        String studio_id;
        String language_id;
        String section_id;
        JSONObject bannerJson;
        JSONObject sectionJson;

        private ProgressBarHandler progressBarHandler = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getHomepageUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", authTokenStr.trim());
                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

              *//*  httppost.addHeader("limit", "1");
                httppost.addHeader("offset", String.valueOf(counter));*//*

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    if (singleItem != null && singleItem.size() > 0) {
                        singleItem.clear();
                    }

                    if (allSampleData != null && allSampleData.size() > 0) {
                        allSampleData.clear();
                    }


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressBarHandler != null) {
                                progressBarHandler.hide();
                                progressBarHandler = null;
                            }

                            responseStr = "0";
                            allSampleData = null;

                        }

                    });

                } catch (IOException e) {
                    if (progressBarHandler != null) {
                        progressBarHandler.hide();
                        progressBarHandler = null;
                    }

                    responseStr = "0";
                    allSampleData = null;

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    if (myJson.has("BannerSectionList")) {
                        bannerJson = new JSONObject(myJson.optString("BannerSectionList"));
                        if (Integer.parseInt(bannerJson.optString("code")) == 200) {
                            JSONArray jsonBannerImageNode = null;
                            try {
                                jsonBannerImageNode = bannerJson.getJSONArray("banners");
                                int lengthBannerImagesArray = jsonBannerImageNode.length();

                                if (lengthBannerImagesArray > 0) {
                                    for (int i = 0; i < lengthBannerImagesArray; i++) {
                                        url_maps.add(jsonBannerImageNode.getJSONObject(i).getString("original"));

                                    }
                                } else {
                                    url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");

                                }

                            } catch (JSONException e2) {
                                url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");
                                e2.printStackTrace();
                            }
                        } else {
                            url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");

                        }
                    }
                    if (myJson.has("SectionName")) {
                        sectionJson = new JSONObject(myJson.optString("SectionName"));
                        if (Integer.parseInt(sectionJson.optString("code")) == 200) {

                            JSONArray jsonMainNode = sectionJson.getJSONArray("section");
                            int lengthJsonArr = jsonMainNode.length();
                            for (int i = 0; i < lengthJsonArr; i++) {

                                JSONObject jsonChildNode;
                                try {
                                    jsonChildNode = jsonMainNode.getJSONObject(i);

                                    if ((jsonChildNode.has("studio_id")) && jsonChildNode.getString("studio_id").trim() != null && !jsonChildNode.getString("studio_id").trim().isEmpty() && !jsonChildNode.getString("studio_id").trim().equals("null") && !jsonChildNode.getString("studio_id").trim().matches("")) {
                                        studio_id = jsonChildNode.getString("studio_id");

                                    }
                                    if ((jsonChildNode.has("language_id")) && jsonChildNode.getString("language_id").trim() != null && !jsonChildNode.getString("language_id").trim().isEmpty() && !jsonChildNode.getString("language_id").trim().equals("null") && !jsonChildNode.getString("language_id").trim().matches("")) {
                                        language_id = jsonChildNode.getString("language_id");

                                    }
                                    if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {
                                        title = jsonChildNode.getString("title");

                                    }
                                    if ((jsonChildNode.has("section_id")) && jsonChildNode.getString("section_id").trim() != null && !jsonChildNode.getString("section_id").trim().isEmpty() && !jsonChildNode.getString("section_id").trim().equals("null") && !jsonChildNode.getString("section_id").trim().matches("")) {
                                        section_id = jsonChildNode.getString("section_id");

                                    }

                                    menuList.add(new GetMenuItem(title, section_id, studio_id, language_id));
                                } catch (Exception e) {
                                    responseStr = "0";
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            responseStr = "0";


                        }
                    }
                }
            } catch (Exception e) {
                if (progressBarHandler != null) {
                    progressBarHandler.hide();
                    progressBarHandler = null;
                }

                responseStr = "0";
                allSampleData = null;
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {


            if (progressBarHandler != null) {
                progressBarHandler.hide();
                progressBarHandler = null;
            }

            if (responseStr == null) {
                if (progressBarHandler != null) {
                    progressBarHandler.hide();
                    progressBarHandler = null;
                }
                responseStr = "0";
            } else if ((responseStr.trim().equals("0"))) {


                if (url_maps.size() >= 0 && firstTime == false) {
                    firstTime = true;

                    if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        for (int j = 0; j < url_maps.size(); j++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(context);
                            textSliderView
                                    .description("")
                                    .image(url_maps.get(j))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }
                    } else {
                        for (int j = 0; j < url_maps.size(); j++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(context);
                            textSliderView
                                    .description("")
                                    .image(url_maps.get(j))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }
                    }

                *//*    for (int i = 0; i < url_maps.size(); i++) {

                        DefaultSliderView textSliderView = new DefaultSliderView(context);

                        textSliderView
                                .description("")
                                .image(R.drawable.slider1)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra", "");

                        mDemoSlider.addSlider(textSliderView);*//*

                      *//*  DefaultSliderView textSliderView = new DefaultSliderView(context);

                        textSliderView
                                .description("")
                                .image(url_maps.get(i))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra", "");

                        mDemoSlider.addSlider(textSliderView);*//*
                } else {
                    // DefaultSliderView textSliderView = new DefaultSliderView(context);
                  *//*  textSliderView
                            .description("")
                            .image(R.drawable.slider1)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);
                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", "");

                    mDemoSlider.addSlider(textSliderView);*//*

                    if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        for (int j = 0; j < url_maps.size(); j++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(context);
                            textSliderView
                                    .description("")
                                    .image(url_maps.get(j))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }
                    } else {
                        for (int j = 0; j < url_maps.size(); j++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(context);
                            textSliderView
                                    .description("")
                                    .image(url_maps.get(j))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }
                    }

                }
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(10000);
                mDemoSlider.addOnPageChangeListener(this);
                sliderRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                if (mProgressBarHandler != null) {
                    mProgressBarHandler.hide();
                    mProgressBarHandler = null;
                }


                if (NetworkStatus.getInstance().isConnected(getActivity())) {

                    my_recycler_view.setLayoutManager(mLayoutManager);
                    adapter = new RecyclerViewDataAdapter(context, allSampleData, url_maps, firstTime, MainActivity.vertical);
                    my_recycler_view.setAdapter(adapter);
                    my_recycler_view.setVisibility(View.VISIBLE);

                    LoadVideoInput loadVideoInput = new LoadVideoInput();
                    loadVideoInput.setAuthToken(authTokenStr);
                    loadVideoInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    loadVideoInput.setSection_id(menuList.get(counter).getSectionId());
                    asynLoadVideos = new GetLoadVideosAsync(loadVideoInput, HomeFragment.this, context);
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor);

                    // default data
                    *//*asynLoadVideos = new AsynLoadVideos();
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor,menuList.get(counter).getSectionId());*//*

                } else {
                    noInternetLayout.setVisibility(View.VISIBLE);
                }
            }
            return;
        }

        @Override
        protected void onPreExecute() {

            progressBarHandler = new ProgressBarHandler(getActivity());
            progressBarHandler.show();

        }

        @Override
        public void onSliderClick(BaseSliderView slider) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }*/

    /*private class AsynLoadImageUrls extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int statusCode;
        ProgressDialog pDialog;
        public ProgressDialog internetSpeedDialog = null;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl()+Util.downloadImageUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
                                    internetSpeedDialog.dismiss();
                                }
                            } catch (IllegalArgumentException ex) {
                                responseStr = "0";
                            }

                        }

                    });

                }catch (IOException e) {
                    try {
                        if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
                            internetSpeedDialog.dismiss();
                        }
                    }
                    catch(IllegalArgumentException ex)
                    {
                        responseStr = "0";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        e.printStackTrace();
                    }
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));

                }

                if (statusCode > 0) {
                    if (statusCode == 200) {
                        if ((myJson.has("image_url")) && myJson.getString("image_url").trim() != null && !myJson.getString("image_url").trim().isEmpty() && !myJson.getString("image_url").trim().equals("null") && !myJson.getString("image_url").trim().matches("")) {
                            imageUrlStr = myJson.getString("image_url");
                        }
                        else{

                            responseStr = "0";

                        }
                    }
                }
                else {
                    responseStr = "0";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            } catch (JSONException e1) {
                try {
                    if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
                        internetSpeedDialog.dismiss();
                    }
                }
                catch(IllegalArgumentException ex)
                {
                    responseStr = "0";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    e1.printStackTrace();
                }
            }

            catch (Exception e)
            {
                try {
                    if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
                        internetSpeedDialog.dismiss();
                    }
                }
                catch(IllegalArgumentException ex)
                {
                    responseStr = "0";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });
                    e.printStackTrace();
                }

            }
            return null;

        }

        protected void onPostExecute(Void result) {
            try {

            }catch (IllegalArgumentException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // createDummyData();

                    }
                });
            }

        }

        @Override
        protected void onPreExecute() {
            if (internetSpeedDialog!=null && internetSpeedDialog.isShowing()){
                pDialog = internetSpeedDialog;
            }else{
              *//*  pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage(getResources().getString(R.string.loading_str));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();*//*
                pDialog = new ProgressDialog(context,R.style.MyTheme);
                pDialog.setCancelable(false);
                pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
                pDialog.setIndeterminate(false);
                pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
                pDialog.show();
            }

        }


    }*/
    public void myOnKeyDown() {
        //do whatever you want here
        if (asynLoadMenuItems != null) {
            asynLoadMenuItems.cancel(true);
        }

        if (asynLoadVideos != null) {
            asynLoadVideos.cancel(true);
        }
       /* ActivityCompat.finishAffinity(getActivity());
        getActivity().finish();
        System.exit(0);*/

    }
   /* *//***************chromecast**********************//*

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



    */

    /***************chromecast**********************/

    @Override
    public void onResume() {
        super.onResume();

        if (mProgressBarHandler != null) {
            mProgressBarHandler.hide();
            mProgressBarHandler = null;
        }
     /*   *//***************chromecast**********************//*
        mCastContext.addCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();
        }
        *//***************chromecast**********************//*
*/
        getActivity().invalidateOptionsMenu();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.media_route_menu_item:
                // Not implemented here
                return false;
            default:
                break;
        }

        return false;
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

            LogUtil.showLog("MUVI", "HHH");
            loadui = new AsynLOADUI();
            loadui.executeOnExecutor(threadPoolExecutor);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  phandler = new ProgressBarHandler(getActivity());
            phandler.show();*/

        }
    }
}





