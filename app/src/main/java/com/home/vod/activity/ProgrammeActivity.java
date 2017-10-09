package com.home.vod.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.vod.R;
import com.home.vod.model.DataModel;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.ResizableCustomView;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BENEFIT_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEASON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DETAILS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DIFFICULTY_TITLE;
import static com.home.vod.preferences.LanguagePreference.DURATION_TITLE;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.SEASON;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.HAS_FAVORITE;

/**
 * Created by MUVI on 10/6/2017.
 */

public class ProgrammeActivity extends AppCompatActivity implements GetContentDetailsAsynTask.GetContentDetailsListener, DeleteFavAsync.DeleteFavListener, AddToFavAsync.AddToFavListener {

    TextView detailsTextView, videoStoryTextView, benefitsTitleTextView, benefitsStoryTextView, durationTitleTextView, diffcultyTitleTextView;
    ImageView bannerImageView, playButton, moviePoster;
    Button startProgramButton, dietPlanButton;
    ProgressBarHandler pDialog;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout;
    LinearLayout story_layout;
    String movieUniqueId = "";
    String movieTrailerUrlStr, isEpisode = "";
    String movieNameStr;
    String videoduration = "";
    String movieTypeStr = "";
    String movieIdStr;
    String movieDetailsStr = "";
    String story;
    String useridStr;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    String movieReleaseDateStr = "";
    PreferenceManager preferenceManager;
    ImageView favorite_view_episode;
    Toolbar mActionBarToolbar;
    static String _permalink;
    String sucessMsg;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    String loggedInStr;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    LanguagePreference languagePreference;
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);


    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    String movieStreamUniqueId, bannerImageId, posterImageId, permalinkStr;
    boolean castStr = false;
    int isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(ProgrammeActivity.this);
        playButton = (ImageView) findViewById(R.id.playButton);
        detailsTextView = (TextView) findViewById(R.id.detailsTextView);
        videoStoryTextView = (TextView) findViewById(R.id.videoStory);
        benefitsTitleTextView = (TextView) findViewById(R.id.benefitsTitleTextView);
        benefitsStoryTextView = (TextView) findViewById(R.id.benefitsStoryTextView);
        startProgramButton = (Button) findViewById(R.id.startProgramButton);
        dietPlanButton = (Button) findViewById(R.id.dietPlanButton);
        durationTitleTextView = (TextView) findViewById(R.id.durationTitleTextView);
        diffcultyTitleTextView = (TextView) findViewById(R.id.diffcultyTitleTextView);
        favorite_view_episode = (ImageView) findViewById(R.id.favoriteImageView);
        moviePoster = (ImageView) findViewById(R.id.bannerImageView);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DataModel dbModel = new DataModel();
        movieUniqueId = dbModel.getMovieUniqueId();
        isEpisode = dbModel.getEpisode_id();


        startProgramButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), SeasonActivity.class);
                i.putExtra(PERMALINK_INTENT_KEY, permalinkStr);
                startActivity(i);
            }
        });


        ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
        useridStr = preferenceManager.getUseridFromPref();

        contentDetailsInput.setAuthToken(authTokenStr);
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
        asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);


        /***favorite *****/
        favorite_view_episode.setVisibility(View.GONE);

        favorite_view_episode.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (preferenceManager != null) {
                    loggedInStr = preferenceManager.getUseridFromPref();
                }

                if (loggedInStr != null) {
                    if (isFavorite == 1) {

                        DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
                        deleteFavInputModel.setAuthTokenStr(authTokenStr);
                        deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                        deleteFavInputModel.setMovieUniqueId(movieUniqueId);
                        deleteFavInputModel.setIsEpisode(isEpisode);

                        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, ProgrammeActivity.this, ProgrammeActivity.this);
                        deleteFavAsync.executeOnExecutor(threadPoolExecutor);

                       /* AsynFavoriteDelete asynFavoriteDelete=new AsynFavoriteDelete();
                        asynFavoriteDelete.execute();*/
                    } else {
                        LogUtil.showLog("MUVI", "favorite");
                        AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                        addToFavInputModel.setAuthToken(authTokenStr);
                        addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                        addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                        addToFavInputModel.setIsEpisodeStr(isEpisode);

                        AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ProgrammeActivity.this, ProgrammeActivity.this);
                        asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);


                    }
                } else {
                    Util.favorite_clicked = true;
                    final Intent registerActivity = new Intent(ProgrammeActivity.this, RegisterActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            registerActivity.putExtra("from", this.getClass().getName());
                            startActivity(registerActivity);

                        }
                    });

                }

            }
        });

        /***favorite *****/


    }


    @Override
    public void onGetContentDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
        pDialog.show();
    }
    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "contentdetails pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }


        if (status == 200) {

            movieDetailsStr = contentDetailsOutput.getStory();
            _permalink = contentDetailsOutput.getPermalink();
            isFavorite = contentDetailsOutput.getIs_favorite();
            bannerImageId = contentDetailsOutput.getBanner();


            detailsTextView.setText(languagePreference.getTextofLanguage(DETAILS_TITLE, DEFAULT_DETAILS_TITLE));
            benefitsTitleTextView.setText(languagePreference.getTextofLanguage(BENEFIT_TITLE, DEFAULT_BENEFIT_TITLE));
            durationTitleTextView.setText(languagePreference.getTextofLanguage(DURATION_TITLE, DEFAULT_DURATION_TITLE));
            diffcultyTitleTextView.setText(languagePreference.getTextofLanguage(DIFFICULTY_TITLE, DEFAULT_DIFFICULTY_TITLE));


            /***favorite *****/
            if (movieDetailsStr.matches("") || movieDetailsStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoStoryTextView.setVisibility(View.GONE);

            } else {
                //  videoStoryTextView.setMaxLines(3);
                videoStoryTextView.setVisibility(View.VISIBLE);

                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.light_fonts), videoStoryTextView);

                videoStoryTextView.setText(movieDetailsStr.trim());

            }

            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                favorite_view_episode.setVisibility(View.VISIBLE);
            } else {
                favorite_view_episode.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(bannerImageId)) {

                if (TextUtils.isEmpty(posterImageId)) {

                    moviePoster.setImageResource(R.drawable.logo);
                } else {


                    /*ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.logo)
                            .showImageOnFail(R.drawable.logo)
                            .showImageOnLoading(R.drawable.logo).build();
                    imageLoader.displayImage(posterImageId, moviePoster, options);*/

                    Picasso.with(ProgrammeActivity.this)
                            .load(posterImageId)
                            .error(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .into(moviePoster);

                }

            } else {


                /*ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(ShowWithEpisodesActivity.this));

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.logo)
                        .showImageOnFail(R.drawable.logo)
                        .showImageOnLoading(R.drawable.logo).build();
                imageLoader.displayImage(bannerImageId.trim(), moviePoster, options);*/

                Picasso.with(ProgrammeActivity.this)
                        .load(bannerImageId.trim())
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .into(moviePoster);


            }

        }
    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
        pDialog.show();
    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        ProgrammeActivity.this.sucessMsg = sucessMsg;
        favorite_view_episode.setImageResource(R.drawable.favorite_unselected);
        showToast();
        isFavorite = 0;
        if (pDialog.isShowing() && pDialog != null) {
            LogUtil.showLog("PINTU", "delete fav pdlog hide");
            pDialog.hide();
        }
    }

    public void showToast() {
        LogUtil.showLog("ANU", "SHOWTOASTisepisode" + isEpisode);
        LogUtil.showLog("ANU", "Value" + movieUniqueId);

        Context context = getApplicationContext();
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

    @Override
    public void onAddToFavPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ProgrammeActivity.this);
        pDialog.show();
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {
        if (status == 200) {


            //pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            ProgrammeActivity.this.sucessMsg = sucessMsg;
            String loggedInStr = preferenceManager.getLoginStatusFromPref();

            favorite_view_episode.setImageResource(R.drawable.favorite_red);
            isFavorite = 1;

            showToast();
            if (pDialog.isShowing() && pDialog != null) {
                LogUtil.showLog("PINTU", "addd fav pdlog hide");
                pDialog.hide();
            }
        }
    }
}