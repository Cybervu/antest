package com.home.vod.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.WebImage;
import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetEpisodeDeatailsAsynTask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiController.ViewContentRatingAsynTask;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.PPVModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.apisdk.apiModel.ViewContentRatingInputModel;
import com.home.apisdk.apiModel.ViewContentRatingOutputModel;
import com.home.vod.R;
import com.home.vod.BuildConfig;
import com.home.vod.adapter.EpisodesListAdapter;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.ResizableCustomView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.BUTTON_APPLY;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_APPLY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEASON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_TRAILER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.SEASON;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.VIEW_TRAILER;
import static com.home.vod.preferences.LanguagePreference.YES;
import static com.home.vod.util.Constant.CAST_INTENT_KEY;
import static com.home.vod.util.Constant.CENSOR_RATING_INTENT_KEY;
import static com.home.vod.util.Constant.GENRE_INTENT_KEY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.SEASON_INTENT_KEY;
import static com.home.vod.util.Constant.STORY_INTENT_KEY;
import static com.home.vod.util.Constant.VIDEO_TITLE_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;
import static player.utils.Util.ADD_A_REVIEW;
import static player.utils.Util.DEFAULT_ADD_A_REVIEW;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.DEFAULT_IS_OFFLINE;
import static player.utils.Util.DEFAULT_REVIEWS;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.IS_OFFLINE;
import static player.utils.Util.REVIEWS;


import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import player.activity.AdPlayerActivity;
import player.activity.ExoPlayerActivity;
import player.activity.Player;
import player.activity.ThirdPartyPlayer;
import player.activity.YouTubeAPIActivity;


public class ShowWithEpisodesActivity extends AppCompatActivity implements
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener,
        LogoutAsynctask.LogoutListener, GetLanguageListAsynTask.GetLanguageListListener,
        GetContentDetailsAsynTask.GetContentDetailsListener,
        GetEpisodeDeatailsAsynTask.GetEpisodeDetailsListener,
        GetValidateUserAsynTask.GetValidateUserListener,
        VideoDetailsAsynctask.VideoDetailsListener,
        AddToFavAsync.AddToFavListener,
        DeleteFavAsync.DeleteFavListener,
        ViewContentRatingAsynTask.ViewContentRatingListener,GetIpAddressAsynTask.IpAddressListener {



    String movieDetailsStr = "";
    String priceForUnsubscribedStr, priceFosubscribedStr;
    PPVModel ppvmodel;
    APVModel advmodel;
    CurrencyModel currencymodel;
    String PlanId = "";
    private static final int MAX_LINES = 2;
    ViewContentRatingAsynTask asynGetReviewDetails;
    int ratingAddedByUser = 1;
    LanguagePreference languagePreference;

    // ProgressBarHandler pDialog;
    ProgressBarHandler pDialog;
    ///****rating****///

    TextView viewRatingTextView;
    String movieIdStr;
    ArrayList<Episode_Details_output.Episode> episodeArray;


    /*** rating***///
    String rating = "0";
    String reviews = "0";
    int isReviewThere = 1;
    int isRatingThere = 1;


    int selectedPurchaseType = 0; // selectedPurchaseType = 1(for show),selectedPurchaseType = 2(for season),selectedPurchaseType = 3(for episode)
    /*subtitle-------------------------------------*/

    String filename = "";
    static File mediaStorageDir;

    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ArrayList<String> SubTitleLanguage = new ArrayList<>();

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        LogUtil.showLog("PINTU", "translate pdlog show");

        pDialog.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "translate pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLogoutPreExecuteStarted() {
        LogUtil.showLog("PINTU", "logout pdlog show");

        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "logoyut pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
        if (status == null) {
            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(ShowWithEpisodesActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }

    }


    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
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
    public void onGetContentDetailsPreExecuteStarted() {
        LogUtil.showLog("PINTU", "contentdetails pdlog show");

        pDialog.show();
        LogUtil.showLog("MUVI", "PREEXECUTE");
    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {

        LogUtil.showLog("MUVI", "onGetContentDetailsPostExecuteCompleted");

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
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);


            movieUniqueId = contentDetailsOutput.getMuviUniqId();
            isEpisode = contentDetailsOutput.getIsEpisode();
            movieStreamUniqueId = contentDetailsOutput.getMovieStreamUniqId();
            movieNameStr = contentDetailsOutput.getName();
            movieTrailerUrlStr = contentDetailsOutput.getTrailerUrl();
            videoduration = contentDetailsOutput.getVideoDuration();
            censorRatingStr = contentDetailsOutput.getCensorRating();
            movieTypeStr = contentDetailsOutput.getGenre();
            isFavorite = contentDetailsOutput.getIs_favorite();
            reviews = contentDetailsOutput.getReview();
            rating = contentDetailsOutput.getRating();
            movieIdStr = contentDetailsOutput.getId();
            movieDetailsStr = contentDetailsOutput.getStory();
            bannerImageId = contentDetailsOutput.getBanner();
            posterImageId = contentDetailsOutput.getPoster();
            movieReleaseDateStr = contentDetailsOutput.getReleaseDate();
            isPPV = contentDetailsOutput.getIsPpv();
            isConverted = contentDetailsOutput.getIsConverted();
            contentTypesId = contentDetailsOutput.getContentTypesId();
            isAPV = contentDetailsOutput.getIsApv();
            castStr = contentDetailsOutput.getCastStr();
            try {
                isFreeContent = Integer.parseInt(contentDetailsOutput.getIsFreeContent());
            } catch (NumberFormatException e) {
            } catch (ArithmeticException e) {
            } catch (Exception e) {
            }

            _permalink = contentDetailsOutput.getPermalink();
            Util.currencyModel = contentDetailsOutput.getCurrencyDetails();
            Util.apvModel = contentDetailsOutput.getApvDetails();
            Util.ppvModel = contentDetailsOutput.getPpvDetails();

            /***favorite *****/

            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                favorite_view_episode.setVisibility(View.VISIBLE);
            } else {
                favorite_view_episode.setVisibility(View.GONE);
            }


            //Enable/Disable Play button

            if (isAPV == 1) {
                playButton.setVisibility(View.GONE);
            } else if (isAPV == 0 && isPPV == 0 && isConverted == 0) {
                if (contentTypesId == 4) {
                    playButton.setVisibility(View.GONE);
                } else {
                    playButton.setVisibility(View.GONE);
                }
            } else if (isAPV == 0 && isPPV == 0 && isConverted == 1) {
                playButton.setVisibility(View.GONE);

            }
            videoTitle.setVisibility(View.VISIBLE);
            FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.regular_fonts), videoTitle);
            videoTitle.setText(movieNameStr);

            if (movieTrailerUrlStr.matches("") || movieTrailerUrlStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                watchTrailerButton.setVisibility(View.INVISIBLE);
            } else {

                watchTrailerButton.setVisibility(View.VISIBLE);
            }

            if (movieTypeStr != null && movieTypeStr.matches("") || movieTypeStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoGenreTextView.setVisibility(View.GONE);

            } else {
                videoGenreTextView.setVisibility(View.VISIBLE);
                FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoGenreTextView);
                videoGenreTextView.setText(movieTypeStr);

            }
            if (videoduration.matches("") || videoduration.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoDurationTextView.setVisibility(View.GONE);

            } else {
                videoDurationTextView.setVisibility(View.VISIBLE);

                FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoDurationTextView);

                videoDurationTextView.setText(videoduration);
                iconImageRelativeLayout.setVisibility(View.VISIBLE);
            }


            if (movieReleaseDateStr.matches("") || movieReleaseDateStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoReleaseDateTextView.setVisibility(View.GONE);
            } else {
                videoReleaseDateTextView.setVisibility(View.VISIBLE);
                FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoReleaseDateTextView);
                movieReleaseDateStr = Util.formateDateFromstring("yyyy-mm-dd", "mm-dd-yyyy", contentDetailsOutput.getReleaseDate());
                videoReleaseDateTextView.setText(movieReleaseDateStr);

            }

            if (movieDetailsStr.matches("") || movieDetailsStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoStoryTextView.setVisibility(View.GONE);

            } else {
                //  videoStoryTextView.setMaxLines(3);
                videoStoryTextView.setVisibility(View.VISIBLE);

                FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoStoryTextView);

                videoStoryTextView.setText(movieDetailsStr.trim());
                ResizableCustomView.doResizeTextView(ShowWithEpisodesActivity.this, videoStoryTextView, MAX_LINES, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);

            }


            if (castStr) {
                FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoCastCrewTitleTextView);
                videoCastCrewTitleTextView.setText(languagePreference.getTextofLanguage(CAST_CREW_BUTTON_TITLE, DEFAULT_CAST_CREW_BUTTON_TITLE));

                videoCastCrewTitleTextView.setVisibility(View.VISIBLE);
            }

            if (censorRatingStr.matches("") ||
                    censorRatingStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoCensorRatingTextView.setVisibility(View.GONE);
                videoCensorRatingTextView1.setVisibility(View.GONE);

            } else {

                if (censorRatingStr.contains("-")) {
                    String Data[] = censorRatingStr.split("-");
                    videoCensorRatingTextView.setVisibility(View.VISIBLE);
                    videoCensorRatingTextView1.setVisibility(View.VISIBLE);

                    FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoCensorRatingTextView);
                    FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoCensorRatingTextView1);

                    videoCensorRatingTextView.setText(Data[0]);
                    videoCensorRatingTextView1.setText(Data[1]);

                } else {
                    videoCensorRatingTextView.setVisibility(View.VISIBLE);
                    videoCensorRatingTextView1.setVisibility(View.GONE);
                    FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.light_fonts), videoCensorRatingTextView);
                    videoCensorRatingTextView.setText(censorRatingStr);
                }


            }


            if (season != null && season.size() > 0) {
                season.clear();
            }
           // LogUtil.showLog("MUVI", "dd====== " + contentDetailsOutput.getSeason().length);

            if (contentDetailsOutput.getSeason() != null && contentDetailsOutput.getSeason().length > 0) {
                for (int j = 0; j < contentDetailsOutput.getSeason().length; j++) {

                    season.add(languagePreference.getTextofLanguage(SEASON, DEFAULT_SEASON) + " " + contentDetailsOutput.getSeason()[j]);


                }
            }
            LogUtil.showLog("MUVI", "season====== " + season.size());

            if (season != null && season.size() > 0) {
                season_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter adapter = new ArrayAdapter(ShowWithEpisodesActivity.this, R.layout.dropdownlist, season);
                season_spinner.setAdapter(adapter);
            }

//                season_spinner.setBackgroundResource(R.drawable.spinner_theme);

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

                    Picasso.with(ShowWithEpisodesActivity.this)
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

                Picasso.with(ShowWithEpisodesActivity.this)
                        .load(bannerImageId.trim())
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .into(moviePoster);


            }

        } else {
            noDataTextView.setText(languagePreference.getTextofLanguage(CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);

            story_layout.setVisibility(View.GONE);
            bannerImageRelativeLayout.setVisibility(View.GONE);
            iconImageRelativeLayout.setVisibility(View.GONE);
            return;
        }


        GetReviewDetails();

        /***favorite *****/
    }

    @Override
    public void onGetEpisodeDetailsPreExecuteStarted() {
        LogUtil.showLog("PINTU", "getepisodedetails pdlog show");

        pDialog.show();
    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(Episode_Details_output episode_details_output, int status, int i, String message, String movieUniqueId) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "getepisodedetails pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }


        LogUtil.showLog("MUVI", "episode show...");

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if (status == 200) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);
            itemData = new ArrayList<EpisodesListModel>();
            isAPV = episode_details_output.getIsAPV();
            isPPV = episode_details_output.getIs_ppv();

            Util.currencyModel = episode_details_output.getCurrencyDetails();
            Util.apvModel = episode_details_output.getApvDetails();
            Util.ppvModel = episode_details_output.getPpvDetails();
            for (int a = 0; a < episode_details_output.getEpisodeArray().size(); a++) {

                String episodeNoStr = episode_details_output.getEpisodeArray().get(a).getEpisode_number();
                String episodeStoryStr = episode_details_output.getEpisodeArray().get(a).getEpisode_story();
                String episodeDateStr = episode_details_output.getEpisodeArray().get(a).getEpisode_date();
                String episodeImageStr = episode_details_output.getEpisodeArray().get(a).getPoster_url();
                String episodeTitleStr = episode_details_output.getEpisodeArray().get(a).getEpisode_title();
                String episodeSeriesNoStr = episode_details_output.getEpisodeArray().get(a).getSeries_number();
                String episodeMovieStreamUniqueIdStr = episode_details_output.getEpisodeArray().get(a).getMovie_stream_uniq_id();
                String episodeThirdParty = episode_details_output.getEpisodeArray().get(a).getThirdparty_url();
                //int episodeContenTTypesId=episodeArray.get(a).getContent_types_id();
                String videodurationStr = episode_details_output.getEpisodeArray().get(a).getVideo_duration();


                itemData.add(new EpisodesListModel(episodeNoStr, episodeStoryStr, episodeDateStr, episodeImageStr, episodeTitleStr, episodeVideoUrlStr, episodeSeriesNoStr, movieUniqueId, episodeMovieStreamUniqueIdStr, episodeThirdParty, videodurationStr));

            }
            LogUtil.showLog("MUVI", "episode show...1");

            LogUtil.showLog("BISHAL", "itemdata==" + itemData);
            if (itemData.size() <= 0) {


                //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
            } else {


                LogUtil.showLog("BISHAL", "data show...");
                seasontiveLayout.setVisibility(View.VISIBLE);
                seasontiveLayout.setLayoutManager(mLayoutManager);
                seasontiveLayout.setItemAnimator(new DefaultItemAnimator());
                EpisodesListAdapter mAdapter = new EpisodesListAdapter(ShowWithEpisodesActivity.this, R.layout.list_card_multipart, itemData, new EpisodesListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EpisodesListModel item) {
                        clickItem(item);

                    }
                });
                seasontiveLayout.setAdapter(mAdapter);

            }
        }

        if (loggedInStr != null && isFavorite == 1) {

            favorite_view_episode.setImageResource(R.drawable.favorite_red);
        }
    }

    /*  @Override
      public void onGetEpisodeDetailsPostExecuteCompleted(ArrayList<Episode_Details_output> episode_details_output, int status, int i, String message,String movieUniqueId) {

          try {
              if (pDialog != null && pDialog.isShowing()) {
                  pDialog.hide();

              }
          }catch (IllegalArgumentException ex) {

          }

          for (int a = 0; a < episode_details_output.size(); a++) {

              String episodeNoStr = episode_details_output.get(a).getEpisode_number();
              String episodeStoryStr = episode_details_output.get(a).getEpisode_story();
              String episodeDateStr = episode_details_output.get(a).getEpisode_date();
              String episodeImageStr = episode_details_output.get(a).getPoster_url();
              String episodeTitleStr = episode_details_output.get(a).getEpisode_title();
              String episodeSeriesNoStr = episode_details_output.get(a).getSeries_number();
              String episodeMovieStreamUniqueIdStr=episode_details_output.get(a).getMovie_stream_uniq_id();
              String episodeThirdParty=episode_details_output.get(a).getThirdparty_url();
              int episodeContenTTypesId=episode_details_output.get(a).getContent_types_id();
              String videodurationStr=episode_details_output.get(a).getVideo_duration();


              itemData.add(new EpisodesListModel(episodeNoStr, episodeStoryStr, episodeDateStr, episodeImageStr, episodeTitleStr, episodeVideoUrlStr, episodeSeriesNoStr, movieUniqueId, episodeMovieStreamUniqueIdStr, episodeThirdParty, episodeContenTTypesId, videodurationStr));

          }

          LogUtil.showLog("MUVI", "episode show...");

          String loggedInStr = preferenceManager.getUseridFromPref();
          if (status == 200) {
              LogUtil.showLog("MUVI", "episode show...1");

              LogUtil.showLog("MUVI", "itemdata==" + itemData);
              if (itemData.size() <= 0) {
                  LogUtil.showLog("MUVI", "episode show...1");
                  if (pDialog != null && pDialog.isShowing()) {
                      pDialog.hide();

                  }

                  //Toast.makeText(ShowWithEpisodesListActivity.this, getResources().getString(R.string.there_no_data_str), Toast.LENGTH_LONG).show();
              } else {

                  LogUtil.showLog("MUVI", "itemData show...1" + itemData.size());
                  LogUtil.showLog("MUVI", "episode show... 2");
                  if (pDialog != null && pDialog.isShowing()) {
                      pDialog.hide();

                  }

                  LogUtil.showLog("MUVI", "data show...");
                  seasontiveLayout.setVisibility(View.VISIBLE);
                  seasontiveLayout.setLayoutManager(mLayoutManager);
                  seasontiveLayout.setItemAnimator(new DefaultItemAnimator());
                  EpisodesListAdapter mAdapter = new EpisodesListAdapter(ShowWithEpisodesActivity.this, R.layout.list_card_multipart, itemData, new EpisodesListAdapter.OnItemClickListener() {
                      @Override
                      public void onItemClick(EpisodesListModel item) {
                          clickItem(item);

                      }
                  });
                  seasontiveLayout.setAdapter(mAdapter);

              }
          }

          if (loggedInStr != null && isFavorite == 1) {

              favorite_view_episode.setImageResource(R.drawable.favorite_red);
          }
      }

  */
    @Override
    public void onGetValidateUserPreExecuteStarted() {
        LogUtil.showLog("PINTU", "validateuser pdlog show");

        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {

        String subscription_Str = validateUserOutput.getIsMemberSubscribed();
        preferenceManager.setIsSubscribedToPref(subscription_Str);
        String validUserStr = validateUserOutput.getValiduser_str();

        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "validate user pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            status = 0;
        }

        if (validateUserOutput == null) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                            startActivity(in);

                        }
                    });
            dlgAlert.create().show();
        } else if (status <= 0) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(ShowWithEpisodesActivity.this, MainActivity.class);
                            startActivity(in);

                        }
                    });
            dlgAlert.create().show();
        }

        if (status > 0) {
            if (status == 427) {


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
                if (message != null && message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                }
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
            } else if (status == 429 || status == 430) {

                if (validUserStr != null) {


                    if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
                        if (NetworkStatus.getInstance().isConnected(this)) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                                ShowPpvPopUp();
                            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
                                Intent intent = new Intent(ShowWithEpisodesActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            } else {
                                ShowPpvPopUp();
                            }
                        }

                    }
                }

            } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                ShowPpvPopUp();
            } else if (PlanId.equals("1") && subscription_Str.equals("0")) {
                Intent intent = new Intent(ShowWithEpisodesActivity.this, SubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else if (Util.dataModel.getIsConverted() == 0) {
                Util.showNoDataAlert(ShowWithEpisodesActivity.this);
                /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();*/
            } else {
                if (NetworkStatus.getInstance().isConnected(this)) {
                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                    getVideoDetailsInput.setAuthToken(authTokenStr);
                    getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                    getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                    asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                }
            }

        }
    }


    @Override
    public void onAddToFavPreExecuteStarted() {
        LogUtil.showLog("PINTU", "addfav pdlog show");

        pDialog.show();


    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {

        if (status == 200) {


            //pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            ShowWithEpisodesActivity.this.sucessMsg = sucessMsg;
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

    @Override
    public void onDeleteFavPreExecuteStarted() {

        LogUtil.showLog("PINTU", "delete fav pdlog show");

        pDialog.show();

    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {

        ShowWithEpisodesActivity.this.sucessMsg = sucessMsg;
        favorite_view_episode.setImageResource(R.drawable.favorite_unselected);
        showToast();
        isFavorite = 0;
        if (pDialog.isShowing() && pDialog != null) {
            LogUtil.showLog("PINTU", "delete fav pdlog hide");
            pDialog.hide();
        }

    }

    @Override
    public void onViewContentRatingPreExecuteStarted() {
        LogUtil.showLog("PINTU", "view content rating pdlog show");

        pDialog.show();
    }

    @Override
    public void onViewContentRatingPostExecuteCompleted(ViewContentRatingOutputModel viewContentRatingOutputModel,
                                                        int status, String message) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "view content pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }

        if (status == 200) {

            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);

            String loggedInStr = preferenceManager.getLoginStatusFromPref();
            // int logI
            LogUtil.showLog("BISHAL", "review data" + reviews);
            LogUtil.showLog("BISHAL", "rating data" + rating);

            if (reviews.equalsIgnoreCase("")) {
                viewRatingTextView.setVisibility(View.GONE);

            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(Float.parseFloat(rating));
            }
            if (rating.equalsIgnoreCase("")) {
                ratingBar.setVisibility(View.GONE);
            } else {
                LogUtil.showLog("BISHAL", "rating ==== " + rating);
                viewRatingTextView.setVisibility(View.VISIBLE);
                if (loggedInStr == null) {
                    viewRatingTextView.setText(languagePreference.getTextofLanguage(ADD_A_REVIEW, DEFAULT_ADD_A_REVIEW));
                    LogUtil.showLog("BISHAL", "rating 0 ==== " + viewContentRatingOutputModel.getShowrating());
                } else {
                    if (viewContentRatingOutputModel.getShowrating() == 1) {
                        viewRatingTextView.setText(languagePreference.getTextofLanguage(ADD_A_REVIEW, DEFAULT_ADD_A_REVIEW));
                        LogUtil.showLog("BISHAL", "rating 1 ==== " + viewContentRatingOutputModel.getShowrating());
                    } else {
                        LogUtil.showLog("BISHAL", "rating 2 ==== " + viewContentRatingOutputModel.getShowrating());
                        viewRatingTextView.setText("reviews (" + reviews + ")");
                        viewRatingTextView.setText(languagePreference.getTextofLanguage(REVIEWS, DEFAULT_REVIEWS) + " (" + reviews + ") ");

                    }

                }

            }

            /***favorite *****/

            if (loggedInStr != null && isFavorite == 0 && Util.favorite_clicked == true) {

                Util.favorite_clicked = false;
                AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                addToFavInputModel.setAuthToken(authTokenStr);
                addToFavInputModel.setMovie_uniq_id(movieUniqueId);
                addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                addToFavInputModel.setIsEpisodeStr(isEpisode);

                asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);
            } else if (loggedInStr != null && isFavorite == 1) {

                favorite_view_episode.setImageResource(R.drawable.favorite_red);
            }
            /***favorite *****/
        } else {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        LogUtil.showLog("PINTU", "videodetails pdlog show");

        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int statusCode, String stus, String message) {
        // _video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // _video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/

        boolean play_video = true;

        if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {

            if (_video_details_output.getStreaming_restriction().trim().equals("0")) {

                play_video = false;
            } else {
                play_video = true;
            }
        } else {
            play_video = true;
        }
        if (!play_video) {

            try {
                if (pDialog.isShowing())
                    pDialog.hide();
            } catch (IllegalArgumentException ex) {
            }

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(message);
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

            return;
        }


        try {
            if (pDialog != null && pDialog.isShowing()) {
                LogUtil.showLog("PINTU", "videodetails pdlog hide");
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
        }


        if (statusCode == 200) {
            playerModel.setIsOffline(_video_details_output.getIs_offline());
            playerModel.setDownloadStatus(_video_details_output.getDownload_status());
            if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {

                /**@bishal
                 * for drm player below condition added
                 * if studio_approved_url is there in api then set the videourl from this other wise goto 2nd one
                 */

                if (_video_details_output.getStudio_approved_url() != null &&
                        !_video_details_output.getStudio_approved_url().isEmpty() &&
                        !_video_details_output.getStudio_approved_url().equals("null") &&
                        !_video_details_output.getStudio_approved_url().matches("")) {
                    LogUtil.showLog("BISHAL", "if called means  studioapproved");
                    playerModel.setVideoUrl(_video_details_output.getStudio_approved_url());
                    LogUtil.showLog("BKS", "studipapprovedurl====" + playerModel.getVideoUrl());


                    if (_video_details_output.getLicenseUrl().trim() != null && !_video_details_output.getLicenseUrl().trim().isEmpty() && !_video_details_output.getLicenseUrl().trim().equals("null") && !_video_details_output.getLicenseUrl().trim().matches("")) {
                        playerModel.setLicenseUrl(_video_details_output.getLicenseUrl());
                    }
                    if (_video_details_output.getVideoUrl().trim() != null && !_video_details_output.getVideoUrl().isEmpty() && !_video_details_output.getVideoUrl().equals("null") && !_video_details_output.getVideoUrl().trim().matches("")) {
                        playerModel.setMpdVideoUrl(_video_details_output.getVideoUrl());

                    } else {
                        playerModel.setMpdVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                    }
                } else {
                    if (_video_details_output.getVideoUrl() != null || !_video_details_output.getVideoUrl().matches("")) {
                        playerModel.setVideoUrl(_video_details_output.getVideoUrl());
                        LogUtil.showLog("BISHAL", "videourl===" + playerModel.getVideoUrl());
                        playerModel.setThirdPartyPlayer(false);
                    } else {
                        //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                        playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                    }
                }
            } else {
                if (_video_details_output.getThirdparty_url() != null || !_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                }
            }

            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if (_video_details_output.getPlayed_length() != null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(_video_details_output.getPlayed_length())));


            //dependency for datamodel

            SubTitleName = _video_details_output.getSubTitleName();
            SubTitleLanguage = _video_details_output.getSubTitleLanguage();

            Util.dataModel.setVideoUrl(_video_details_output.getVideoUrl());
            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());
            Util.dataModel.setThirdPartyUrl(_video_details_output.getThirdparty_url());
            Util.dataModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            Util.dataModel.setChannel_id(_video_details_output.getChannel_id());
            Util.dataModel.setPreRoll(_video_details_output.getPreRoll());
            Util.dataModel.setPostRoll(_video_details_output.getPostRoll());
            Util.dataModel.setMidRoll(_video_details_output.getMidRoll());
            Util.dataModel.setAdDetails(_video_details_output.getAdDetails());


            //player model set
            playerModel.setAdDetails(_video_details_output.getAdDetails());
            playerModel.setMidRoll(_video_details_output.getMidRoll());
            playerModel.setPostRoll(_video_details_output.getPostRoll());
            playerModel.setChannel_id(_video_details_output.getChannel_id());
            playerModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            playerModel.setPreRoll(_video_details_output.getPreRoll());
            playerModel.setSubTitleName(_video_details_output.getSubTitleName());
            playerModel.setSubTitlePath(_video_details_output.getSubTitlePath());
            playerModel.setResolutionFormat(_video_details_output.getResolutionFormat());
            playerModel.setResolutionUrl(_video_details_output.getResolutionUrl());
            playerModel.setFakeSubTitlePath(_video_details_output.getFakeSubTitlePath());
            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            FakeSubTitlePath = _video_details_output.getFakeSubTitlePath();
            playerModel.setSubTitleLanguage(_video_details_output.getSubTitleLanguage());
            playerModel.setOfflineUrl(_video_details_output.getOfflineUrl());
            playerModel.setOfflineLanguage(_video_details_output.getOfflineLanguage());


            if (playerModel.getVideoUrl() == null ||
                    playerModel.getVideoUrl().matches("")) {

                Util.showNoDataAlert(ShowWithEpisodesActivity.this);
               /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();*/
            } else {


                // condition for checking if the response has third party url or not.
                if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {


                    if (mCastSession != null && mCastSession.isConnected()) {
                        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, playerModel.getVideoStory());
                        movieMetadata.putString(MediaMetadata.KEY_TITLE, playerModel.getVideoTitle());
                        movieMetadata.addImage(new WebImage(Uri.parse(playerModel.getPosterImageId())));
                        movieMetadata.addImage(new WebImage(Uri.parse(playerModel.getPosterImageId())));


                        String mediaContentType = "videos/mp4";
                        if (playerModel.getVideoUrl().contains(".mpd")) {
                            mediaContentType = "application/dash+xml";
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject();
                                jsonObj.put("description", playerModel.getVideoTitle());
                                jsonObj.put("licenseUrl", playerModel.getLicenseUrl());

                                //  This Code Is Added For Video Log By Bibhu..

                                jsonObj.put("authToken", authTokenStr);
                                jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                                jsonObj.put("ip_address", ipAddres.trim());
                                jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                                jsonObj.put("episode_id", playerModel.getEpisode_id());
                                jsonObj.put("watch_status", "start");
                                jsonObj.put("device_type", "2");
                                jsonObj.put("log_id", "0");
                                jsonObj.put("active_track_index", "0");

                                if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {
                                    jsonObj.put("restrict_stream_id", "0");
                                    jsonObj.put("is_streaming_restriction", "1");
                                    Log.v("BIBHU4", "restrict_stream_id============1");
                                } else {
                                    jsonObj.put("restrict_stream_id", "0");
                                    jsonObj.put("is_streaming_restriction", "0");
                                    Log.v("BIBHU4", "restrict_stream_id============0");
                                }

                                jsonObj.put("domain_name", BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));
                                jsonObj.put("is_log", "1");

                                //=====================End===================//

                                // This code is changed according to new Video log //

                                jsonObj.put("played_length", "0");
                                jsonObj.put("log_temp_id", "0");
                                jsonObj.put("resume_time", "0");
                                jsonObj.put("seek_status", "");
                                // This  Code Is Added For Drm BufferLog By Bibhu ...

                                jsonObj.put("resolution", "BEST");
                                jsonObj.put("start_time", "0");
                                jsonObj.put("end_time", "0");
                                jsonObj.put("log_unique_id", "0");
                                jsonObj.put("location", "0");
                                jsonObj.put("bandwidth_log_id", "0");
                                jsonObj.put("video_type", "mped_dash");
                                jsonObj.put("drm_bandwidth_by_sender", "0");

                                //====================End=====================//

                            } catch (JSONException e) {
                            }
                            List tracks = new ArrayList();
                            for (int i = 0; i < FakeSubTitlePath.size(); i++) {
                                MediaTrack englishSubtitle = new MediaTrack.Builder(i,
                                        MediaTrack.TYPE_TEXT)
                                        .setName(SubTitleName.get(0))
                                        .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                                        .setContentId(FakeSubTitlePath.get(0))
                                        .setLanguage(SubTitleLanguage.get(0))
                                        .setContentType("text/vtt")
                                        .build();
                                tracks.add(englishSubtitle);
                            }

                            mediaInfo = new MediaInfo.Builder(playerModel.getMpdVideoUrl().trim())
                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                    .setContentType(mediaContentType)
                                    .setMetadata(movieMetadata)
                                    .setCustomData(jsonObj)
                                    .setMediaTracks(tracks)
                                    .build();
                            mSelectedMedia = mediaInfo;


                            togglePlayback();
                        } else {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject();
                                jsonObj.put("description", playerModel.getVideoTitle());

                                //  This Code Is Added For Video Log By Bibhu..

                                jsonObj.put("authToken", authTokenStr);
                                jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                                jsonObj.put("ip_address", ipAddres.trim());
                                jsonObj.put("movie_id", playerModel.getMovieUniqueId());
                                jsonObj.put("episode_id", playerModel.getEpisode_id());
                                jsonObj.put("watch_status", "start");
                                jsonObj.put("device_type", "2");
                                jsonObj.put("log_id", "0");
                                jsonObj.put("active_track_index", "0");
                                jsonObj.put("seek_status", "");

                                jsonObj.put("played_length", "0");
                                jsonObj.put("log_temp_id", "0");
                                jsonObj.put("resume_time", "0");
                                jsonObj.put("seek_status", "");


                                if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {
                                    jsonObj.put("restrict_stream_id", "0");
                                    jsonObj.put("is_streaming_restriction", "1");
                                    Log.v("BIBHU4", "restrict_stream_id============1");
                                } else {
                                    jsonObj.put("restrict_stream_id", "0");
                                    jsonObj.put("is_streaming_restriction", "0");
                                    Log.v("BIBHU4", "restrict_stream_id============0");
                                }

                                jsonObj.put("domain_name", BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));
                                jsonObj.put("is_log", "1");

                                //=====================End===================//


                                // This  Code Is Added For Drm BufferLog By Bibhu ...

                                jsonObj.put("resolution", "BEST");
                                jsonObj.put("start_time", "0");
                                jsonObj.put("end_time", "0");
                                jsonObj.put("log_unique_id", "0");
                                jsonObj.put("location", "0");
                                jsonObj.put("video_type", "");
                                jsonObj.put("totalBandwidth", "0");

                                //====================End=====================//

                            } catch (JSONException e) {
                            }

                            List tracks = new ArrayList();
                            for (int i = 0; i < FakeSubTitlePath.size(); i++) {
                                MediaTrack englishSubtitle = new MediaTrack.Builder(i,
                                        MediaTrack.TYPE_TEXT)
                                        .setName(SubTitleName.get(0))
                                        .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                                        .setContentId(FakeSubTitlePath.get(0))
                                        .setLanguage(SubTitleLanguage.get(0))
                                        .setContentType("text/vtt")
                                        .build();
                                tracks.add(englishSubtitle);
                            }

                            mediaInfo = new MediaInfo.Builder(Util.dataModel.getVideoUrl().trim())
                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                    .setContentType(mediaContentType)
                                    .setMetadata(movieMetadata)
                                    .setStreamDuration(15 * 1000)
                                    .setCustomData(jsonObj)
                                    .setMediaTracks(tracks)
                                    .build();
                            mSelectedMedia = mediaInfo;


                            togglePlayback();
                        }
                    } else {


                        playerModel.setThirdPartyPlayer(false);

                        final Intent playVideoIntent;
                        if (Util.dataModel.getAdNetworkId() == 3) {
                            LogUtil.showLog("responseStr", "playVideoIntent" + Util.dataModel.getAdNetworkId());

                            playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);

                        } else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1) {
                            if (Util.dataModel.getPlayPos() <= 0) {
                                playVideoIntent = new Intent(ShowWithEpisodesActivity.this, AdPlayerActivity.class);
                            } else {
                                playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);

                            }


                        } else {
                            playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
                        }
                        /***ad **/
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (FakeSubTitlePath.size() > 0) {
                                    // This Portion Will Be changed Later.

                                    File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
                                    if (dir.isDirectory()) {
                                        String[] children = dir.list();
                                        for (int i = 0; i < children.length; i++) {
                                            new File(dir, children[i]).delete();
                                        }
                                    }

                                    progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
                                    progressBarHandler.show();
                                    Download_SubTitle(FakeSubTitlePath.get(0).trim());
                                } else {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                    playVideoIntent.putExtra("PlayerModel", playerModel);
                                    startActivity(playVideoIntent);
                                }

                            }
                        });
                    }
                } else {
                    final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    playVideoIntent.putExtra("PlayerModel", playerModel);
                    startActivity(playVideoIntent);
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

            Util.showNoDataAlert(ShowWithEpisodesActivity.this);
        }


    }

        /*subtitle-------------------------------------*/


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


    // Added For The Voucher

    int isVoucher = 0;
    String PurchageType = "";
    String VoucherCode = "";

    TextView content_label, content_name, voucher_success;
    EditText voucher_code;
    Button apply, watch_now;
    boolean watch_status = false;
    String ContentName = "";
    AlertDialog voucher_alert;
    String sucessMsg;

    // voucher ends here //

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
    String ipAddres = "";


    public static ProgressBarHandler progressBarHandler;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    int prevPosition = 0;

    RelativeLayout viewStoryLayout;

    //Add By Bibhu Later.
    TextView videoStoryTextView;
    Button storyViewMoreButton;

    boolean isExpanded = false;
    VideoDetailsAsynctask asynLoadVideoUrls;
    GetValidateUserAsynTask asynValidateUserDetails;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    AddToFavAsync asynFavoriteAdd;
    Toolbar mActionBarToolbar;
    // ProgressBarHandler pDialog;
    int isLogin = 0;
    String movieUniqueId = "";

    String censorRatingStr = "";
    private boolean isThirdPartyTrailer = false;
    GetEpisodeDeatailsAsynTask asynEpisodeDetails;
    int spinnerPosition = 0;
    ImageView moviePoster, favorite_view_episode;
    ImageView playButton;
    ImageButton offlineImageButton;
    Button viewTrailerButton, btnmore, watchTrailerButton;
    TextView videoTitle, videoGenreTextView, videoDurationTextView, videoCensorRatingTextView, videoCensorRatingTextView1, videoReleaseDateTextView, videoCastCrewTitleTextView;
    RatingBar ratingBar;
    SharedPreferences pref;
    Spinner season_spinner;
    ArrayList<String> season;
    EpisodesListModel itemToPlay;
    String videoResolution = "BEST";
    boolean castStr = false;
    int isFavorite;
    String Season_Value = "";
    int loginresultcode = 0;

    AlertDialog alert;
    Player playerModel;

    RecyclerView.LayoutManager mLayoutManager;
    String movieStreamUniqueId, bannerImageId, posterImageId, permalinkStr;
    String movieTrailerUrlStr, isEpisode = "";
    String movieTypeStr = "";
    String videoduration = "";
    String movieReleaseDateStr = "";
    String name, loggedInStr, planid;
    static String _permalink;
    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout;
    LinearLayout story_layout;
    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    RecyclerView seasontiveLayout;
    int status = 0;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    int isSubscribedDataStr = 0;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ArrayList<EpisodesListModel> itemData;
    String movieNameStr;
    // TextView seasonTitleTextView;
    String episodeVideoUrlStr;
    TextView noDataTextView;
    TextView noInternetTextView;
    String isMemberSubscribed;
    PreferenceManager preferenceManager;
    Episode_Details_output content = new Episode_Details_output();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
        setContentView(R.layout.activity_show_with_episodes);
//        SharedPreferences isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(DELETE_ACTION, new IntentFilter("ITEM_STATUS"));

        LogUtil.showLog("MUVI", "onCreate");
        season = new ArrayList<String>();
        Util.goToLibraryplayer = false;
        languagePreference = LanguagePreference.getLanguagePreference(ShowWithEpisodesActivity.this);
        playerModel = new Player();
        playerModel.setIsstreaming_restricted(Util.getStreamingRestriction(languagePreference));
        //playerModel = (Player) getIntent().getSerializableExtra("PlayerModel");
        isLogin = preferenceManager.getLoginFeatureFromPref();
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
        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        btnmore = (Button) findViewById(R.id.btnMore);
        favorite_view_episode = (ImageView) findViewById(R.id.favorite_view_episode);

        FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.regular_fonts), btnmore);

        btnmore.setText(languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE));

        btnmore.setVisibility(View.GONE);
        playButton = (ImageView) findViewById(R.id.playButton);
        watchTrailerButton = (Button) findViewById(R.id.viewTrailerButton);
        FontUtls.loadFont(ShowWithEpisodesActivity.this, getResources().getString(R.string.regular_fonts), watchTrailerButton);
        watchTrailerButton.setText(languagePreference.getTextofLanguage(VIEW_TRAILER, DEFAULT_VIEW_TRAILER));

        playButton.setVisibility(View.GONE);

        offlineImageButton = (ImageButton) findViewById(R.id.offlineImageButton);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoGenreTextView = (TextView) findViewById(R.id.videoGenreTextView);
        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        videoStoryTextView = (TextView) findViewById(R.id.videoStoryTextView);
        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        playButton.setVisibility(View.GONE);

        videoCastCrewTitleTextView.setVisibility(View.GONE);
        ppvmodel = new PPVModel();
        advmodel = new APVModel();
        currencymodel = new CurrencyModel();
        PlanId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();
        season_spinner = (Spinner) findViewById(R.id.seasonSpinner);

        if (Build.VERSION.SDK_INT < 23) {
            season_spinner.setBackgroundResource(R.drawable.lollipop_spinner_theme);
        }

        season_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerPosition = position;
                if (asynEpisodeDetails != null) {
                    asynEpisodeDetails.cancel(true);
                }
                btnmore.setVisibility(View.VISIBLE);


                LogUtil.showLog("MUVI", "episode details  call 1");

                Episode_Details_input episodeDetailsInput = new Episode_Details_input();
                episodeDetailsInput.setAuthtoken(authTokenStr);
                episodeDetailsInput.setPermalink(permalinkStr.trim());
                episodeDetailsInput.setLimit("4");
                episodeDetailsInput.setOffset("1");
                episodeDetailsInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                episodeDetailsInput.setCountry(preferenceManager.getCountryCodeFromPref());

                String data = season.get(spinnerPosition);
                String[] data1 = data.split(" ");

                if (data1.length > 0) {

                    episodeDetailsInput.setSeries_number(data1[1].trim());

                    Season_Value = data1[1].trim();

                } else {

                }

                asynEpisodeDetails = new GetEpisodeDeatailsAsynTask(episodeDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                asynEpisodeDetails.executeOnExecutor(threadPoolExecutor);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
      /*  seasonTitleTextView = (TextView) findViewById(R.id.seasonTitleTextView);
        seasonTitleTextView.setText(languagePreference.getTextofLanguage(SEASON, Util.DEFAULT_SEASON));
*/
        seasontiveLayout = (RecyclerView) findViewById(R.id.featureContent);
        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noDataLayout.setVisibility(View.GONE);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));

        mLayoutManager = new LinearLayoutManager(ShowWithEpisodesActivity.this, LinearLayoutManager.HORIZONTAL, false);
        iconImageRelativeLayout = (RelativeLayout) findViewById(R.id.iconImageRelativeLayout);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        story_layout = (LinearLayout) findViewById(R.id.story_layout);
        seasontiveLayout.setVisibility(View.GONE);
        episodeVideoUrlStr = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        seasontiveLayout.addItemDecoration(new HorizotalSpaceItemDecoration(20));
        videoStoryTextView = (TextView) findViewById(R.id.videoStoryTextView);
        storyViewMoreButton = (Button) findViewById(R.id.storyViewMoreButton);
        // *** rating***////
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setFocusable(false);
        ratingBar.setVisibility(View.GONE);

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewRatingTextView = (TextView) findViewById(R.id.viewRatingTextView);


        // *****rating********///
        viewRatingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(ShowWithEpisodesActivity.this, ReviewActivity.class);
                reviewIntent.putExtra("muviId", movieIdStr.trim());
                startActivityForResult(reviewIntent, 30060);
            }
        });

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

                        DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
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

                        asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                        asynFavoriteAdd.executeOnExecutor(threadPoolExecutor);


                    }
                } else {
                    Util.favorite_clicked = true;
                    final Intent registerActivity = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);
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


        watchTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl("");
                dbModel.setVideoTitle(movieNameStr);
                dbModel.setVideoStory(movieDetailsStr);
                dbModel.setVideoGenre(videoGenreTextView.getText().toString());
                dbModel.setVideoDuration(videoDurationTextView.getText().toString());
                dbModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                dbModel.setCensorRating(censorRatingStr);
                dbModel.setCastCrew(castStr);
                dbModel.setVideoUrl(movieTrailerUrlStr);
                dbModel.setVideoResolution("BEST");
                dbModel.setContentTypesId(contentTypesId);

                Util.dataModel = dbModel;


                if ((movieTrailerUrlStr.matches("")) || (movieTrailerUrlStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA)))) {
                    Util.showNoDataAlert(ShowWithEpisodesActivity.this);
                   /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();*/
                    return;
                } else if (isThirdPartyTrailer == false) {
                    if (mCastSession != null && mCastSession.isConnected()) {


                        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

                        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieReleaseDateStr);
                        movieMetadata.putString(MediaMetadata.KEY_TITLE, movieNameStr + " - Trailer");
                        movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                        movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject();
                            jsonObj.put("description", movieNameStr);


                            //  This Code Is Added For Video Log By Bibhu..

                            jsonObj.put("authToken", authTokenStr.trim());
                            jsonObj.put("user_id", preferenceManager.getUseridFromPref());
                            jsonObj.put("ip_address", ipAddres.trim());
                            jsonObj.put("movie_id", movieUniqueId);
                            jsonObj.put("episode_id", "");


                            jsonObj.put("played_length", "0");


                            jsonObj.put("watch_status", "start");
                            jsonObj.put("device_type", "2");
                            jsonObj.put("log_id", "0");

                            // restrict_stream_id is always set to be "0" , bcoz it's a triler.
                            jsonObj.put("restrict_stream_id", "0");

                            jsonObj.put("domain_name", BuildConfig.SERVICE_BASE_PATH.trim().substring(0, BuildConfig.SERVICE_BASE_PATH.trim().length() - 6));
                            jsonObj.put("is_log", "1");

                            //=====================End===================//


                            // This  Code Is Added For Drm BufferLog By Bibhu ...

                            jsonObj.put("resolution", "BEST");
                            jsonObj.put("start_time", "0");
                            jsonObj.put("end_time", "0");
                            jsonObj.put("log_unique_id", "0");
                            jsonObj.put("location", "0");
                            jsonObj.put("video_type", "");
                            jsonObj.put("totalBandwidth", "0");
                            // This is added only to identify, the videolog is called for trailer
                            jsonObj.put("content_type", "2");

                            //====================End=====================//

                        } catch (JSONException e) {
                        }

                        mediaInfo = new MediaInfo.Builder(movieTrailerUrlStr.trim())
                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                .setContentType("videos/mp4")
                                .setMetadata(movieMetadata)
                                .setStreamDuration(15 * 1000)
                                .setCustomData(jsonObj)
                                .build();
                        mSelectedMedia = mediaInfo;
                        // Util.showQueuePopup(ShowWithEpisodesActivity.this, view, mediaInfo);

                        togglePlayback();

                    } else {
                        final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, TrailerActivity.class);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(playVideoIntent);

                            }
                        });
                    }
                } else {
                    if (movieTrailerUrlStr.contains("://www.youtube") || movieTrailerUrlStr.contains("://www.youtu.be")) {
                        if (movieTrailerUrlStr.contains("live_stream?channel")) {
                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });
                        } else {

                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, YouTubeAPIActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);


                                }
                            });

                        }
                    } else {
                        final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(playVideoIntent);

                            }
                        });
                    }

                }

            }
        });

        //  pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        loggedInStr = preferenceManager.getLoginStatusFromPref();
        planid = preferenceManager.getUseridFromPref();

        if (planid == null)
            planid = "0";

        if (loggedInStr == null)
            loggedInStr = "0";

        try {
            isSubscribedDataStr = Integer.parseInt(preferenceManager.getIsSubscribedFromPref());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        LogUtil.showLog("MUVI", "PER" + getIntent().getStringExtra(PERMALINK_INTENT_KEY));

        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin == 1) {
                    String loggedInStr = preferenceManager.getLoginStatusFromPref();
                    if (loggedInStr == null) {
                        final Intent registerActivity = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                Util.check_for_subscription = 1;
                                registerActivity.putExtra("PlayerModel", playerModel);
                                startActivity(registerActivity);


                            }
                        });

                        //showLoginDialog();
                    } else {
                        if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {

                            if (isFreeContent == 1) {
                                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                getVideoDetailsInput.setAuthToken(authTokenStr);
                                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                            } else {
                                ValidateUserInput validateUserInput = new ValidateUserInput();
                                validateUserInput.setAuthToken(authTokenStr);
                                validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                                validateUserInput.setMuviUniqueId(movieUniqueId.trim());
                                validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                                validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                                validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                                validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                                asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                            }
                        } else {

                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {
                        // MUVIlaxmi

                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(authTokenStr);
                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                        asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                    } else {
                        Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                        // Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }


            }
        });
        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Intent episode = new Intent(ShowWithEpisodesActivity.this, Episode_list_Activity.class);
                episode.putExtra(PERMALINK_INTENT_KEY, _permalink);
                episode.putExtra(GENRE_INTENT_KEY, videoGenreTextView.getText().toString());
                episode.putExtra(STORY_INTENT_KEY, videoStoryTextView.getText().toString());
                episode.putExtra(CENSOR_RATING_INTENT_KEY, censorRatingStr);
                episode.putExtra(CAST_INTENT_KEY, castStr);
                episode.putExtra(SEASON_INTENT_KEY, Season_Value);
                episode.putExtra(VIDEO_TITLE_INTENT_KEY, movieNameStr.trim());
                episode.putExtra("content_types_id", "" + contentTypesId);

                LogUtil.showLog("MUVI", "season intent = " + Season_Value);

                runOnUiThread(new Runnable() {
                    public void run() {
                        episode.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(episode);


                    }
                });

            }
        });
        videoCastCrewTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent episode = new Intent(ShowWithEpisodesActivity.this, CastAndCrewActivity.class);
                episode.putExtra("cast_movie_id", movieUniqueId.trim());
                runOnUiThread(new Runnable() {
                    public void run() {
                        episode.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(episode);


                    }
                });

            }
        });

           /*subtitle-------------------------------------*/


        if (ContextCompat.checkSelfPermission(ShowWithEpisodesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowWithEpisodesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(ShowWithEpisodesActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        111);
            } else {
                ActivityCompat.requestPermissions(ShowWithEpisodesActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);

            }
        } else {
            //Call whatever you want
            if (NetworkStatus.getInstance().isConnected(ShowWithEpisodesActivity.this)) {

                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                contentDetailsInput.setAuthToken(authTokenStr);
                contentDetailsInput.setPermalink(permalinkStr);
                contentDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                contentDetailsInput.setCountry(preferenceManager.getCountryCodeFromPref());
                contentDetailsInput.setLanguage(preferenceManager.getLanguageListFromPref());


                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                // Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }

/***************chromecast**********************/

        mAquery = new AQuery(this);

        //setupControlsCallbacks();
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        boolean shouldStartPlayback = false;
        int startPosition = 0;

         /*   MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieName.getText().toString());
            movieMetadata.putString(MediaMetadata.KEY_TITLE,  movieName.getText().toString());
            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject();
                jsonObj.put("description", movieName.getText().toString());
            } catch (JSONException e) {
                Log.e(TAG, "Failed to add description to the json object", e);
            }

            mediaInfo = new MediaInfo.Builder(castVideoUrl.trim())
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("videos/mp4")
                    .setMetadata(movieMetadata)
                    .setStreamDuration(15 * 1000)
                    .setCustomData(jsonObj)
                    .build();
            mSelectedMedia = mediaInfo;*//*

        // see what we need to play and where
           *//* Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mSelectedMedia = getIntent().getParcelableExtra("media");
                //setupActionBar();
                boolean shouldStartPlayback = bundle.getBoolean("shouldStart");
                int startPosition = bundle.getInt("startPosition", 0);
                // mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
               // Log.d(TAG, "Setting url of the VideoView to: " + mSelectedMedia.getContentId());
                if (shouldStartPlayback) {
                    // this will be the case only if we are coming from the
                    // CastControllerActivity by disconnecting from a device
                    mPlaybackState = PlaybackState.PLAYING;
                    updatePlaybackLocation(PlaybackLocation.LOCAL);
                    updatePlayButton(mPlaybackState);
                    if (startPosition > 0) {
                        // mVideoView.seekTo(startPosition);
                    }
                    // mVideoView.start();
                    //startControllersTimer();
                } else {
                    // we should load the video but pause it
                    // and show the album art.
                    if (mCastSession != null && mCastSession.isConnected()) {
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    } else {
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                    }
                    mPlaybackState = PlaybackState.IDLE;
                    updatePlayButton(mPlaybackState);
                }
            }*/


        if (shouldStartPlayback) {
            // this will be the case only if we are coming from the
            // CastControllerActivity by disconnecting from a device
            mPlaybackState = PlaybackState.PLAYING;
            updatePlaybackLocation(PlaybackLocation.LOCAL);
            updatePlayButton(mPlaybackState);
            if (startPosition > 0) {
                // mVideoView.seekTo(startPosition);
            }
            // mVideoView.start();
            //startControllersTimer();
        } else {
            // we should load the video but pause it
            // and show the album art.
            if (mCastSession != null && mCastSession.isConnected()) {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                updatePlaybackLocation(PlaybackLocation.REMOTE);
            } else {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));

                updatePlaybackLocation(PlaybackLocation.LOCAL);
            }
            mPlaybackState = PlaybackState.IDLE;
            updatePlayButton(mPlaybackState);
        }
/***************chromecast**********************/


    }


    @Override
    public void onBackPressed() {
        if (asynValidateUserDetails != null) {
            asynValidateUserDetails.cancel(true);
        }
        if (asynLoadVideoUrls != null) {
            asynLoadVideoUrls.cancel(true);
        }
        if (asynLoadMovieDetails != null) {
            asynLoadMovieDetails.cancel(true);
        }
        if (asynGetReviewDetails != null) {
            asynGetReviewDetails.cancel(true);
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();


        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    public void clickItem(EpisodesListModel item) {

        itemToPlay = item;

        DataModel dbModel = new DataModel();
        dbModel.setIsFreeContent(isFreeContent);
        dbModel.setIsAPV(isAPV);
        dbModel.setIsPPV(isPPV);
        dbModel.setIsConverted(1);
        dbModel.setMovieUniqueId(movieUniqueId);
        dbModel.setStreamUniqueId(item.getEpisodeStreamUniqueId());
        dbModel.setThirdPartyUrl(item.getEpisodeThirdPartyUrl());
        dbModel.setVideoTitle(movieNameStr);
        dbModel.setVideoStory(item.getEpisodeDescription());
        dbModel.setVideoGenre(videoGenreTextView.getText().toString());
        dbModel.setVideoDuration(item.getEpisodeDuration());
        // dbModel.setVideoReleaseDate(item.getEpisodeTelecastOn());
        dbModel.setVideoReleaseDate("");

        dbModel.setCensorRating(censorRatingStr);
        dbModel.setCastCrew(castStr);
        dbModel.setEpisode_id(item.getEpisodeStreamUniqueId());
        dbModel.setSeason_id(Season_Value);
        dbModel.setPurchase_type("episode");
        dbModel.setPosterImageId(item.getEpisodeThumbnailImageView());
        dbModel.setContentTypesId(contentTypesId);

        dbModel.setEpisode_series_no(item.getEpisodeSeriesNo());
        dbModel.setEpisode_no(item.getEpisodeNumber());
        dbModel.setEpisode_title(item.getEpisodeTitle());

        // dbModel.setParentTitle(movieNameStr);
        dbModel.setContentTypesId(3);


        Util.dataModel = dbModel;
        SubTitleName.clear();
        SubTitlePath.clear();
        ResolutionUrl.clear();
        ResolutionFormat.clear();
        SubTitleLanguage.clear();
      /*  Util.offline_url.clear();
        Util.offline_language.clear();*/


        //edit by bishal
        //set the required data in playermodel
        playerModel.setStreamUniqueId(item.getEpisodeStreamUniqueId());
        playerModel.setMovieUniqueId(item.getEpisodeMuviUniqueId());
        playerModel.setUserId(preferenceManager.getUseridFromPref());
        playerModel.setEmailId(preferenceManager.getEmailIdFromPref());
        playerModel.setAuthTokenStr(authTokenStr.trim());
        playerModel.setRootUrl(BuildConfig.SERVICE_BASE_PATH.trim());
        playerModel.setEpisode_id(item.getEpisodeStreamUniqueId());
        playerModel.setVideoTitle(movieNameStr);
        playerModel.setVideoStory(item.getEpisodeDescription());
        playerModel.setVideoGenre(videoGenreTextView.getText().toString());
        playerModel.setVideoDuration(item.getEpisodeDuration());
        playerModel.setVideoReleaseDate("");
        playerModel.setCensorRating(censorRatingStr);
        playerModel.setContentTypesId(contentTypesId);
        playerModel.setPosterImageId(posterImageId);

        LogUtil.showLog("MUVI", "content typesid = " + contentTypesId);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if (isLogin == 1) {
            if (loggedInStr != null) {
                if (NetworkStatus.getInstance().isConnected(this)) {

                    ValidateUserInput validateUserInput = new ValidateUserInput();
                    validateUserInput.setAuthToken(authTokenStr);
                    validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                    validateUserInput.setMuviUniqueId(movieUniqueId.trim());
                    validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                    validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                    validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                    validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                    asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);

                } else {
                    Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                    //  Toast.makeText(ShowWithEpisodesActivity.this,Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
                }

            } else {

                final Intent register = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);

                runOnUiThread(new Runnable() {
                    public void run() {
                        register.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Util.check_for_subscription = 1;
                        register.putExtra("PlayerModel", playerModel);
                        startActivity(register);


                    }
                });
            }
        } else {
            if (NetworkStatus.getInstance().isConnected(this)) {
                // MUVIlaxmi

                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(authTokenStr);
                getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

            } else {
                Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

                //Toast.makeText(ShowWithEpisodesActivity.this,Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
            }
        }


    }

    /*private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int statusCode;
        // This is added because of change in simultaneous login feature
        String message;
        boolean play_video = true;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(BuildConfig.SERVICE_BASE_PATH.trim() + "loadVideoUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
                httppost.addHeader("internet_speed", MainActivity.internetSpeed.trim());
                httppost.addHeader("user_id", pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
                httppost.addHeader("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();

                            }
                            responseStr = "0";
                            Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                            Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //  Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();

                    }
                    responseStr = "0";
                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                    e.printStackTrace();
                }

              *//*  JSONObject myJson =null;

                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                }
*//*

                *//**** subtitles************//*

                JSONObject myJson = null;
                JSONArray SubtitleJosnArray = null;
                JSONArray ResolutionJosnArray = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
                    ResolutionJosnArray = myJson.optJSONArray("videoDetails");
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    // This is added because of change in simultaneous login feature
                    message = myJson.optString("msg");

                    LogUtil.showLog("BIBHU", "video stream msg" + message);
                    // ================================== End ====================================//
                }
                */

    /**** subtitles************//*

                if (statusCode >= 0) {
                    if (statusCode == 200) {
                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA))) {
                            if ((myJson.has("studio_approved_url")) && myJson.getString("studio_approved_url").trim() != null && !myJson.getString("studio_approved_url").trim().isEmpty() && !myJson.getString("studio_approved_url").trim().equals("null") && !myJson.getString("studio_approved_url").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("studio_approved_url"));
                                if ((myJson.has("licenseUrl")) && myJson.getString("licenseUrl").trim() != null && !myJson.getString("licenseUrl").trim().isEmpty() && !myJson.getString("licenseUrl").trim().equals("null") && !myJson.getString("licenseUrl").trim().matches("")) {
                                    // Util.dataModel.setLicenseUrl(myJson.getString("licenseUrl"));
                                }
                                if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                    //  Util.dataModel.setMpdVideoUrl(myJson.getString("videoUrl"));

                                } else {
                                    // Util.dataModel.setMpdVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                                }
                            }

                           *//* if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
                            }*//*

                            else {
                                if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                    Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));

                                } else {
                                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                                }
                            }
                        } else {
                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));

                            } else {
                                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));

                            }
                        }
                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));

                        }
                        if ((myJson.has("played_length")) && myJson.getString("played_length").trim() != null && !myJson.getString("played_length").trim().isEmpty() && !myJson.getString("played_length").trim().equals("null") && !myJson.getString("played_length").trim().matches("")) {
                            Util.dataModel.setPlayPos(Util.isDouble(myJson.getString("played_length")));
                        }

                        if ((myJson.has("is_offline")) && myJson.getString("is_offline").trim() != null && !myJson.getString("is_offline").trim().isEmpty() && !myJson.getString("is_offline").trim().equals("null") && !myJson.getString("is_offline").trim().matches("")) {

                            //offline = myJson.getString("is_offline");
                            // Util.dataModel.setIsOffline(Util.isOffline=myJson.getString("is_offline"));

                        } else {


                        }

                        if (SubtitleJosnArray != null) {
                            if (SubtitleJosnArray.length() > 0) {
                                for (int i = 0; i < SubtitleJosnArray.length(); i++) {

                                    LogUtil.showLog("BIBHU", "Sutitle name = " + SubtitleJosnArray.getJSONObject(i).optString("language").trim());
                                    LogUtil.showLog("BIBHU", "Sutitle path = " + SubtitleJosnArray.getJSONObject(i).optString("url").trim());

                                    SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
                                    FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
                                    SubTitleLanguage.add(SubtitleJosnArray.getJSONObject(i).optString("code").trim());
                                    //  Util.offline_url.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
                                    // Util.offline_language.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());

                                }
                            }
                        }
                        // This is added because of change in simultaneous login feature
                        if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, Util.DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {

                            LogUtil.showLog("BIBHU", "streaming_restriction============" + myJson.optString("streaming_restriction").toString().trim());

                            if (myJson.optString("streaming_restriction").toString().trim().equals("0")) {
                                play_video = false;
                            } else {
                                play_video = true;
                            }
                        } else {
                            play_video = true;
                        }

                        // ================================== End ====================================//
                    }

                } else {

                    responseStr = "0";
                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                }
            } catch (JSONException e1) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();

                }
                responseStr = "0";
                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                e1.printStackTrace();
            } catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();

                }
                responseStr = "0";
                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

            // This is added because of change in simultaneous login feature //

            if (!play_video) {

                try {
                    if (pDialog.isShowing())
                        pDialog.hide();
                } catch (IllegalArgumentException ex) {
                }

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(message);
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

                return;
            }

            //=====================End========================================//
            if (responseStr == null) {
                responseStr = "0";
                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();

                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
                }
                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
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

                if (Util.dataModel.getVideoUrl() == null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();

                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
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
                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA))) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();

                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
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
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();

                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA))) {


                        if (Util.dataModel.getVideoUrl().contains("rtmp://") || Util.dataModel.getVideoUrl().contains("rtmp://")) {
                            Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(VIDEO_ISSUE, Util.DEFAULT_VIDEO_ISSUE), Toast.LENGTH_SHORT).show();
                        } else {
                            final Intent playVideoIntent;

                            playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);


                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (FakeSubTitlePath.size() > 0) {
                                        // This Portion Will Be changed Later.

                                        File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
                                        if (dir.isDirectory()) {
                                            String[] children = dir.list();
                                            for (int i = 0; i < children.length; i++) {
                                                new File(dir, children[i]).delete();
                                            }
                                        }

                                        progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
                                        progressBarHandler.show();
                                        Download_SubTitle(FakeSubTitlePath.get(0).trim());
                                    } else {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                        playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                        playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                                        playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);
                                        startActivity(playVideoIntent);
                                    }


                                }
                            });
                        }

                    } else {
                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")) {
                            if (Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
                                final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(playVideoIntent);

                                    }
                                });
                            } else {

                                final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, YouTubeAPIActivity.class);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(playVideoIntent);


                                    }
                                });

                            }
                        } else {
                            final Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ThirdPartyPlayer.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);

                                }
                            });
                        }
                    }
                }


            }
        }

        @Override
        protected void onPreExecute() {

            pDialog.show();


        }


    }*/
    private void ShowPpvPopUp() {
        {


            try {
                if (Util.currencyModel.getCurrencySymbol() == null) {
                    Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));

                    // Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));

                // Toast.makeText(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) ShowWithEpisodesActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View convertView = inflater.inflate(R.layout.activity_ppv_popup, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("");

            final RadioButton completeRadioButton = (RadioButton) convertView.findViewById(R.id.completeRadioButton);
            final RadioButton seasonRadioButton = (RadioButton) convertView.findViewById(R.id.seasonRadioButton);
            final RadioButton episodeRadioButton = (RadioButton) convertView.findViewById(R.id.episodeRadioButton);
            TextView episodePriceTextView = (TextView) convertView.findViewById(R.id.episodePriceTextView);
            TextView seasonPriceTextView = (TextView) convertView.findViewById(R.id.seasonPriceTextView);
            TextView completePriceTextView = (TextView) convertView.findViewById(R.id.completePriceTextView);
            Button payNowButton = (Button) convertView.findViewById(R.id.payNowButton);


            if (Util.dataModel.getIsAPV() == 1) {
                if (Util.apvModel.getIsEpisode() == 1) {
                    episodeRadioButton.setVisibility(View.VISIBLE);
                    episodePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    episodeRadioButton.setVisibility(View.GONE);
                    episodePriceTextView.setVisibility(View.GONE);
                }
                if (Util.apvModel.getIsSeason() == 1) {
                    seasonRadioButton.setVisibility(View.VISIBLE);
                    seasonPriceTextView.setVisibility(View.VISIBLE);
                } else {
                    seasonRadioButton.setVisibility(View.GONE);
                    seasonPriceTextView.setVisibility(View.GONE);
                }
                if (Util.apvModel.getIsShow() == 1) {
                    completeRadioButton.setVisibility(View.VISIBLE);
                    completePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    completeRadioButton.setVisibility(View.GONE);
                    completePriceTextView.setVisibility(View.GONE);
                }
            } else {
                if (Util.ppvModel.getIsEpisode() == 1) {
                    episodeRadioButton.setVisibility(View.VISIBLE);
                    episodePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    episodeRadioButton.setVisibility(View.GONE);
                    episodePriceTextView.setVisibility(View.GONE);
                }
                if (Util.ppvModel.getIsSeason() == 1) {
                    seasonRadioButton.setVisibility(View.VISIBLE);
                    seasonPriceTextView.setVisibility(View.VISIBLE);
                } else {
                    seasonRadioButton.setVisibility(View.GONE);
                    seasonPriceTextView.setVisibility(View.GONE);
                }
                if (Util.ppvModel.getIsShow() == 1) {
                    completeRadioButton.setVisibility(View.VISIBLE);
                    completePriceTextView.setVisibility(View.VISIBLE);
                } else {
                    completeRadioButton.setVisibility(View.GONE);
                    completePriceTextView.setVisibility(View.GONE);
                }
            }


            completeRadioButton.setText("  " + Util.dataModel.getVideoTitle().trim() + " Complete Season ");
            seasonRadioButton.setText("  " + Util.dataModel.getVideoTitle().trim() + " Season " + Util.dataModel.getEpisode_series_no().trim() + " ");
            episodeRadioButton.setText("  " + Util.dataModel.getVideoTitle().trim() + " S" + Util.dataModel.getEpisode_series_no().trim() + " E " + Util.dataModel.getEpisode_no().trim() + " ");

            String subscriptionStr = preferenceManager.getIsSubscribedFromPref();

            if (subscriptionStr.trim().equals("1")) {
                if (Util.dataModel.getIsAPV() == 1) {

                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeSubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvSeasonSubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowSubscribedStr());
                } else {
                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeSubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonSubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvShowSubscribedStr());
                }
            } else {
                if (Util.dataModel.getIsAPV() == 1) {

                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeUnsubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvSeasonUnsubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowUnsubscribedStr());
                } else {
                    episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeUnsubscribedStr());
                    seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonUnsubscribedStr());
                    completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvShowUnsubscribedStr());
                }
            }


            alert = alertDialog.show();
            completeRadioButton.setChecked(true);


        /*if (completeRadioButton.isChecked() == true) {
            if (Util.dataModel.getIsAPV() == 1) {
                priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

            } else {
                priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
            }
        }*/


            // Changed later

            if (completeRadioButton.getVisibility() == View.VISIBLE) {

                completeRadioButton.setChecked(true);
                episodeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);

                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
                }


            } else {
                if (seasonRadioButton.getVisibility() == View.VISIBLE) {

                    completeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(true);
                    episodeRadioButton.setChecked(false);

                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvSeasonSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvSeasonSubscribedStr();
                    }


                } else {
                    completeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(false);
                    episodeRadioButton.setChecked(true);

                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvEpisodeSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvEpisodeSubscribedStr();
                    }


                }
            }

            ///////////////////////=====================////////////////////////

            completeRadioButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    episodeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(false);
                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
                    }


                }

            });


            episodeRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeRadioButton.setChecked(false);
                    seasonRadioButton.setChecked(false);

                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvEpisodeSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvEpisodeUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvEpisodeSubscribedStr();
                    }

                }
            });
            seasonRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    episodeRadioButton.setChecked(false);
                    completeRadioButton.setChecked(false);
                    if (Util.dataModel.getIsAPV() == 1) {
                        priceForUnsubscribedStr = Util.apvModel.getApvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.apvModel.getApvSeasonSubscribedStr();

                    } else {
                        priceForUnsubscribedStr = Util.ppvModel.getPpvSeasonUnsubscribedStr();
                        priceFosubscribedStr = Util.ppvModel.getPpvSeasonSubscribedStr();
                    }

                }
            });

            payNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completeRadioButton.isChecked()) {
                        selectedPurchaseType = 1;
                        Util.selected_episode_id = "0";
                        Util.selected_season_id = "0";
                        LogUtil.showLog("MUVI", "called 1");
                    } else if (seasonRadioButton.isChecked()) {
                        selectedPurchaseType = 2;
                        Util.selected_episode_id = "0";
                        Util.selected_season_id = "" + spinnerPosition;
                        LogUtil.showLog("MUVI", "called 2");
                    } else {
                        selectedPurchaseType = 3;
                        Util.selected_episode_id = Util.dataModel.getStreamUniqueId();
                        Util.selected_season_id = Util.dataModel.getEpisode_series_no();
                        LogUtil.showLog("MUVI", "called 3");
                    }


                    LogUtil.showLog("MUVI", "Show withepisode  Activity Season Id =" + Util.selected_season_id);
                    LogUtil.showLog("MUVI", "Show withepisode Activity episode Id =" + Util.selected_episode_id);

                    alert.dismiss();
                    final Intent showPaymentIntent = new Intent(ShowWithEpisodesActivity.this, PPvPaymentInfoActivity.class);
                    showPaymentIntent.putExtra("muviuniqueid", Util.dataModel.getMovieUniqueId().trim());
                    showPaymentIntent.putExtra("episodeStreamId", Util.dataModel.getStreamUniqueId().trim());
                    showPaymentIntent.putExtra("contentTypesId", Util.dataModel.getContentTypesId());
                    showPaymentIntent.putExtra("movieThirdPartyUrl", Util.dataModel.getThirdPartyUrl());
                    showPaymentIntent.putExtra("planUnSubscribedPrice", priceForUnsubscribedStr);
                    showPaymentIntent.putExtra("planSubscribedPrice", priceFosubscribedStr);
                    showPaymentIntent.putExtra("currencyId", Util.currencyModel.getCurrencyId());
                    showPaymentIntent.putExtra("currencyCountryCode", Util.currencyModel.getCurrencyCode());
                    showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
                    showPaymentIntent.putExtra("PlayerModel", playerModel);

                    // showPaymentIntent.putExtra("showName", Util.dataModel.getEpisode_title());


                    if (selectedPurchaseType == 1)
                        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle().trim() + " Complete Season ");
                    if (selectedPurchaseType == 2)
                        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle().trim() + " Season " + Util.dataModel.getEpisode_series_no().trim());
                    if (selectedPurchaseType == 3)
                        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle().trim() + " S" + Util.dataModel.getEpisode_series_no().trim() + " E " + Util.dataModel.getEpisode_no().trim());
                    showPaymentIntent.putExtra("seriesNumber", Util.dataModel.getEpisode_series_no());
                    showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
                    showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
                    showPaymentIntent.putExtra("selectedPurchaseType", selectedPurchaseType);
                    if (Util.dataModel.getIsAPV() == 1) {
                        showPaymentIntent.putExtra("isConverted", 0);
                    } else {
                        showPaymentIntent.putExtra("isConverted", 1);

                    }

                    showPaymentIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(showPaymentIntent);

                }
            });
        }
    }


    public class HorizotalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int horizontalSpaceWidth;

        public HorizotalSpaceItemDecoration(int horizontalSpaceWidth) {
            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.right = horizontalSpaceWidth;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        /***************chromecast**********************/

        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        /***************chromecast**********************/


        MenuItem item, item1, item2, item3, item4, item5, item6;
        item = menu.findItem(R.id.action_filter);
        item.setVisible(false);
        MenuItem item7 = menu.findItem(R.id.menu_item_favorite);
        //  pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        if (preferenceManager.getLanguageListFromPref().equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);


        if (loggedInStr != null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            item5.setVisible(false);

            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            item1.setVisible(true);

            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE).trim()).equals("1")) {
                item7.setVisible(true);
            } else {
                item7.setVisible(false);

            }
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(true);

            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            item3.setVisible(true);


            if ((languagePreference.getTextofLanguage(HAS_FAVORITE, DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                item7.setVisible(true);
            } else {
                item7.setVisible(false);

            }

            item6 = menu.findItem(R.id.action_mydownload);
            item6.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
            if ((languagePreference.getTextofLanguage(IS_OFFLINE, DEFAULT_IS_OFFLINE)
                    .trim()).equals("1")) {
                item6.setVisible(true);
            } else {
                item6.setVisible(false);

            }


        } else if (loggedInStr == null) {
            item4 = menu.findItem(R.id.action_login);
            item4.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5 = menu.findItem(R.id.action_register);
            item5.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            if (isLogin == 1) {
                item4.setVisible(true);
                item5.setVisible(true);

            } else {
                item4.setVisible(false);
                item5.setVisible(false);

            }

            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            item1.setVisible(false);
            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            item3.setVisible(false);
            item6 = menu.findItem(R.id.action_mydownload);
            item6.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
            item6.setVisible(true);
            item7 = menu.findItem(R.id.menu_item_favorite);
            item7.setTitle(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
            item7.setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(ShowWithEpisodesActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(ShowWithEpisodesActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_mydownload:

                Intent mydownload = new Intent(ShowWithEpisodesActivity.this, MyDownloads.class);
                startActivity(mydownload);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(ShowWithEpisodesActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);

                if (Util.languageModel != null && Util.languageModel.size() > 0) {


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
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(ShowWithEpisodesActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(ShowWithEpisodesActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
           /* case R.id.action_mydownload:

                Intent mydownload = new Intent(ShowWithEpisodesActivity.this, MyDownloads.class);
                startActivity(mydownload);
                // Not implemented here
                return false;*/
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
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
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
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
    }


    private BroadcastReceiver DELETE_ACTION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String movieUniqId = intent.getStringExtra("movie_uniq_id").trim();
            LogUtil.showLog("UMA", "movie_id tto be deleted=" + movieUniqId);
            if (movieUniqId.equals(movieUniqueId.trim())) {
                isFavorite = 0;
                favorite_view_episode.setImageResource(R.drawable.favorite_unselected);
            }


        }
    };

    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

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

        languageCustomAdapter = new LanguageCustomAdapter(ShowWithEpisodesActivity.this, Util.languageModel);
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
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(ShowWithEpisodesActivity.this, recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = Util.languageModel.get(position).getLanguageId();


                languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, Util.languageModel.get(position).getLanguageId());
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
                    GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                    asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
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

    @Override
    protected void onResume() {
        super.onResume();
        /**FAVORITE*/
        if (Util.favorite_clicked == true) {

            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
            contentDetailsInput.setAuthToken(authTokenStr);
            contentDetailsInput.setPermalink(permalinkStr);

            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);
        }
        // **************chromecast*********************//
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }

        GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

        /***************chromecast**********************/
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }


        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        /***************chromecast**********************/
        invalidateOptionsMenu();

    }

    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


   /* private class AsynGetLanguageList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.LanguageList.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());


                // Execute HTTP Post Request
                try {


                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
                } catch (Exception e) {
                }
                if (responseStr != null) {
                    JSONObject json = new JSONObject(responseStr);
                    try {
                        status = Integer.parseInt(json.optString("code"));
                        Default_Language = json.optString("default_lang");
                        if (!languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, "").equals("")) {
                            Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE);
                        }

                    } catch (Exception e) {
                        status = 0;
                    }
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (progressBarHandler.isShowing()) {
                progressBarHandler.hide();
                progressBarHandler = null;

            }

            if (responseStr == null) {
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject json = new JSONObject(responseStr);
                        JSONArray jsonArray = json.getJSONArray("lang_list");
                        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String language_id = jsonArray.getJSONObject(i).optString("code").trim();
                            String language_name = jsonArray.getJSONObject(i).optString("language").trim();


                            LanguageModel languageModel = new LanguageModel();
                            languageModel.setLanguageId(language_id);
                            languageModel.setLanguageName(language_name);

                            if (Default_Language.equalsIgnoreCase(language_id)) {
                                languageModel.setIsSelected(true);
                            } else {
                                languageModel.setIsSelected(false);
                            }
                            languageModels.add(languageModel);
                        }

                        Util.languageModel = languageModels;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                 *//*   if(!Default_Language.equals("en")) {
                        //                  Call For Language Translation.
                        AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);

                    }else{

                    }*//*

                } else {
                }
            }
            ShowLanguagePopup();


        }

        protected void onPreExecute() {

            progressBarHandler = new ProgressBarHandler(ShowWithEpisodesActivity.this);
            progressBarHandler.show();

        }
    }*/


    /*****************chromecvast*-------------------------------------*/

    private void updateMetadata(boolean visible) {
        Point displaySize;
        if (!visible) {
            /*mDescriptionView.setVisibility(View.GONE);
            mTitleView.setVisibility(View.GONE);
            mAuthorView.setVisibility(View.GONE);*/
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    displaySize.y + getSupportActionBar().getHeight());
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
        } else {
            //MediaMetadata mm = mSelectedMedia.getMetadata();
          /*  mDescriptionView.setText(mSelectedMedia.getCustomData().optString(
                    VideoProvider.KEY_DESCRIPTION));
            //mTitleView.setText(mm.getString(MediaMetadata.KEY_TITLE));
            //mAuthorView.setText(mm.getString(MediaMetadata.KEY_SUBTITLE));
            mDescriptionView.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.VISIBLE);
            mAuthorView.setVisibility(View.VISIBLE);*/
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    (int) (displaySize.x * mAspectRatio));
            lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
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
                mLocation = PlaybackLocation.REMOTE;
                if (null != mSelectedMedia) {

                    if (mPlaybackState == PlaybackState.PLAYING) {
                       /* mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);*/
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
/*
                    mPlayCircle.setVisibility(View.GONE);
*/

                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlayButton(PlaybackState state) {
           /* boolean isConnected = (mCastSession != null)
                    && (mCastSession.isConnected() || mCastSession.isConnecting());*/
        //mControllers.setVisibility(isConnected ? View.GONE : View.VISIBLE);

        switch (state) {
            case PLAYING:

                //mLoading.setVisibility(View.INVISIBLE);
                // mPlayPause.setVisibility(View.VISIBLE);
                //mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_pause_dark));

                break;
            case IDLE:
                if (mLocation == PlaybackLocation.LOCAL) {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                    }*/

                } else {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                    }*/
                }
                //mCon
                // trollers.setVisibility(View.GONE);
                // mCoverArt.setVisibility(View.VISIBLE);
                // mVideoView.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                //mLoading.setVisibility(View.INVISIBLE);
              /*  mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_play_dark));*/

                break;
            case BUFFERING:
                //mPlayPause.setVisibility(View.INVISIBLE);
                //mLoading.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                //setCoverArtStatus(null);
                //startControllersTimer();
            } else {
                //stopControllersTimer();

                //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            //stopControllersTimer();
            // setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            //updateControllersVisibility(false);
        }
    }


    private void togglePlayback() {
        //stopControllersTimer();
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:



                      /* mVideoView.start();
                        Log.d(TAG, "Playing locally...");
                        mPlaybackState = PlaybackState.PLAYING;
                        startControllersTimer();
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*/
                        break;

                    case REMOTE:

                        loadRemoteMedia(0, true);

                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;

                // mVideoView.pause();
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                        //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));

                        // mPlayCircle.setVisibility(View.GONE);
                       /* mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
                        mVideoView.seekTo(0);
                        mVideoView.start();
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*/
                        break;
                    case REMOTE:
                        // mPlayCircle.setVisibility(View.VISIBLE);
                        if (mCastSession != null && mCastSession.isConnected()) {
                            // watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            loadRemoteMedia(0, true);


                            // Utils.showQueuePopup(this, mPlayCircle, mSelectedMedia);
                        } else {
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        updatePlayButton(mPlaybackState);
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

                Intent intent = new Intent(ShowWithEpisodesActivity.this, ExpandedControlsActivity.class);
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
        remoteMediaClient.setActiveMediaTracks(new long[1]).setResultCallback(new ResultCallback<RemoteMediaClient.MediaChannelResult>() {
            @Override
            public void onResult(@NonNull RemoteMediaClient.MediaChannelResult mediaChannelResult) {
                if (!mediaChannelResult.getStatus().isSuccess()) {
                    LogUtil.showLog("MUVI", "Failed with status code:" +
                            mediaChannelResult.getStatus().getStatusCode());
                }
            }
        });
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
    }

    /***************chromecast**********************/
    /***********Subtitle********/

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
                mediaStorageDir = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/", "");

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
                playerModel.setSubTitlePath(SubTitlePath);


                final Intent playVideoIntent;
                if (Util.dataModel.getAdNetworkId() == 3) {
                    LogUtil.showLog("responseStr", "playVideoIntent" + Util.dataModel.getAdNetworkId());

                    playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);

                } else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1) {
                    if (Util.dataModel.getPlayPos() <= 0) {
                        playVideoIntent = new Intent(ShowWithEpisodesActivity.this, AdPlayerActivity.class);
                    } else {
                        playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);

                    }
                } else {
                    playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);

                }
                /***ad **/
                //Intent playVideoIntent = new Intent(ShowWithEpisodesActivity.this, ExoPlayerActivity.class);
                /*playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                playVideoIntent.putExtra("PlayerModel", playerModel);
                startActivity(playVideoIntent);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want

                        if (NetworkStatus.getInstance().isConnected(this)) {
                            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                            contentDetailsInput.setAuthToken(authTokenStr);
                            contentDetailsInput.setPermalink(permalinkStr);

                            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

                        } else {
                            Util.showToast(ShowWithEpisodesActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));

//                            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } else {
                        finish();
                    }
                } else {
                    finish();
                }

                return;
            }
        }
    }
    /*subtitles***********/


    //Added Later For Voucher


    public void GetVoucherPlan() {
        LogUtil.showLog("BIBHU", "get voucher plan called");

      /*  AsynGetVoucherPlan getVoucherPlan = new AsynGetVoucherPlan();
        getVoucherPlan.executeOnExecutor(threadPoolExecutor);*/
    }

   /* private class AsynGetVoucherPlan extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;

        int status;
        String validUserStr;
        String userMessage;
        String responseStr;
        String loggedInIdStr;

        @Override
        protected Void doInBackground(Void... params) {

            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }



            try {


                String urlRouteList = Util.rootUrl().trim()+Util.GetVoucherPlan.trim();
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("movie_id", movieUniqueId.trim());
                httppost.addHeader("stream_id", Util.dataModel.getStreamUniqueId());
                httppost.addHeader("season", Util.dataModel.getSeason_id());

               *//* String urlRouteList = "http://www.idogic.com/rest/GetVoucherPlan";
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken","b7fbac553a14c99adcf079be6b48fd9f");
                httppost.addHeader("user_id","5146");
                httppost.addHeader("movie_id","5b24dfaf49a996b04ef92c272bde21f0");
                httppost.addHeader("stream_id","de4fcc9ffcc0b7d3ae1468765290685f");
                httppost.addHeader("season","1");*//*



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    LogUtil.showLog("BIBHU" , "Response Of the get voucher plan = "+responseStr);


                } catch (final org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();

                            }
                            status = 0;
                            Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();

                    }
                    status = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }
                else
                {
                    status = 0;
                }
            }
            catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();

                }
                status = 0;
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();

                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }


            if(status == 200)
            {

                String isShow="0",isSeason="0",isEpisode="0";

                try {
                    JSONObject jsonObject = new JSONObject(responseStr);

                    // Checking for Show Purchase Type
                    if(jsonObject.optString("is_show")!=null)
                    {
                        if(!(jsonObject.optString("is_show").equals("")) && !(jsonObject.optString("is_show").equals("null"))
                                && (jsonObject.optString("is_show").trim().equals("1")))
                        {
                            isShow = "1";
                        }
                        else
                        {
                            isShow = "0";
                        }
                    }
                    else
                    {
                        isShow = "0";
                    }

                    // Checking for Season Purchase Type

                    if(jsonObject.optString("is_season")!=null)
                    {
                        if(!(jsonObject.optString("is_season").equals("")) && !(jsonObject.optString("is_season").equals("null"))
                                && (jsonObject.optString("is_season").trim().equals("1")))
                        {
                            isSeason = "1";
                        }
                        else
                        {
                            isSeason = "0";
                        }
                    }
                    else
                    {
                        isSeason = "0";
                    }

                    // Checking for Episode Purchase Type

                    if(jsonObject.optString("is_episode")!=null)
                    {
                        if(!(jsonObject.optString("is_episode").equals("")) && !(jsonObject.optString("is_episode").equals("null"))
                                && (jsonObject.optString("is_episode").trim().equals("1")))
                        {
                            isEpisode = "1";
                        }
                        else
                        {
                            isEpisode = "0";
                        }
                    }
                    else
                    {
                        isEpisode = "0";
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(isShow.equals("0") && isSeason.equals("0"))
                {
                    PurchageType = "episode";
                    ShowVoucherPopUp("  "+movieNameStr.trim() + " S" + Util.dataModel.getSeason_id().trim() + " E" + Util.dataModel.getEpisode_no() + " ");
                }
                else
                {
                    ShowVoucherPurchaseTypePopUp(isShow,isSeason,isEpisode);
                }

            }
            else
            {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SORRY,Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
            pDialog.show();
        }
    }

    public void ShowVoucherPurchaseTypePopUp(String isShow,String isSeason,String isEpisode)
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) ShowWithEpisodesActivity.this.getSystemService(ShowWithEpisodesActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.voucher_plan_popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        final RadioButton completeRadioButton = (RadioButton) convertView.findViewById(R.id.completeRadioButton);
        final RadioButton seasonRadioButton = (RadioButton) convertView.findViewById(R.id.seasonRadioButton);
        final RadioButton episodeRadioButton = (RadioButton) convertView.findViewById(R.id.episodeRadioButton);
        Button payNowButton = (Button) convertView.findViewById(R.id.payNowButton);
        TextView heading = (TextView) convertView.findViewById(R.id.heading);


        // Font implemented Here//

        Typeface typeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        completeRadioButton.setTypeface(typeface);
        seasonRadioButton.setTypeface(typeface);
        episodeRadioButton.setTypeface(typeface);
        payNowButton.setTypeface(typeface);
        heading.setTypeface(typeface);


        // ============end==============//

        // Language Implemented Here //

        completeRadioButton.setText("  " + movieNameStr.trim() +" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.COMPLETE_SEASON,Util.DEFAULT_COMPLETE_SEASON).toString().toLowerCase()+" ");
        seasonRadioButton.setText("  "+movieNameStr.trim() +" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SEASON,Util.DEFAULT_SEASON).toString().toLowerCase()+" "+ Util.dataModel.getSeason_id().trim() + " ");
        episodeRadioButton.setText("  "+movieNameStr.trim() + " S" + Util.dataModel.getSeason_id().trim() + " E" + Util.dataModel.getEpisode_no() + " ");
        heading.setText(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SELECT_PURCHASE_TYPE,Util.DEFAULT_SELECT_PURCHASE_TYPE).toString().toLowerCase());
        payNowButton.setText(Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BTN_NEXT,Util.DEFAULT_BTN_NEXT).toString().toLowerCase());


        //==============End===============//


        if (isEpisode.equals("1")) {
            episodeRadioButton.setVisibility(View.VISIBLE);
        } else {
            episodeRadioButton.setVisibility(View.GONE);
        }
        if (isSeason.equals("1")) {
            seasonRadioButton.setVisibility(View.VISIBLE);
        } else {
            seasonRadioButton.setVisibility(View.GONE);
        }
        if (isShow.equals("1")) {
            completeRadioButton.setVisibility(View.VISIBLE);
        } else {
            completeRadioButton.setVisibility(View.GONE);
        }

        voucher_alert = alertDialog.show();
        completeRadioButton.setChecked(true);

        // Changed later

        if (completeRadioButton.getVisibility() == View.VISIBLE) {

            completeRadioButton.setChecked(true);
            episodeRadioButton.setChecked(false);
            seasonRadioButton.setChecked(false);

            PurchageType = "show";
            ContentName  = completeRadioButton.getText().toString().trim();

        }
        else
        {
            if (seasonRadioButton.getVisibility() == View.VISIBLE) {

                completeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(true);
                episodeRadioButton.setChecked(false);

                PurchageType = "season";
                ContentName  = seasonRadioButton.getText().toString().trim();

            }
            else
            {
                completeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);
                episodeRadioButton.setChecked(true);

                PurchageType = "episode";
                ContentName  = episodeRadioButton.getText().toString().trim();

            }
        }

        ///////////////////////=====================////////////////////////


        episodeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);

                PurchageType = "episode";
                ContentName  = episodeRadioButton.getText().toString().trim();



            }
        });
        seasonRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                episodeRadioButton.setChecked(false);
                completeRadioButton.setChecked(false);

                PurchageType = "season";
                ContentName  = seasonRadioButton.getText().toString().trim();



            }
        });

        completeRadioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                episodeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);

                PurchageType = "show";
                ContentName  = completeRadioButton.getText().toString().trim();


            }

        });

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voucher_alert.dismiss();
                //ShowVoucherPopUp(ContentName);
            }
        });

    }*/

   /* public void ShowVoucherPopUp(String ContentName)
    {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowWithEpisodesActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) ShowWithEpisodesActivity.this.getSystemService(ShowWithEpisodesActivity.this.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.voucher_popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        content_label = (TextView) convertView.findViewById(R.id.content_label);
        content_name = (TextView) convertView.findViewById(R.id.content_name);
        voucher_success = (TextView) convertView.findViewById(R.id.voucher_success);
        voucher_code = (EditText) convertView.findViewById(R.id.voucher_code);
        apply = (Button) convertView.findViewById(R.id.apply);
        watch_now = (Button) convertView.findViewById(R.id.watch_now);

        apply.setEnabled(true);
        apply.setBackgroundResource(R.drawable.button_radious);
        apply.setTextColor(getResources().getColor(R.color.pageTitleColor));


        // Font implemented Here//

        Typeface typeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        content_label.setTypeface(typeface);
        content_name.setTypeface(typeface);
        voucher_success.setTypeface(typeface);
        apply.setTypeface(typeface);
        watch_now.setTypeface(typeface);
        voucher_code.setTypeface(typeface);

        //==============end===============//

        // Language Implemented Here //

        content_label.setText(" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.PURCHASE,Util.DEFAULT_PURCHASE).toString().toLowerCase()+" : ");
        voucher_success.setText(" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.VOUCHER_SUCCESS,Util.DEFAULT_VOUCHER_SUCCESS).toString().toLowerCase()+" ");
        apply.setText(" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.BUTTON_APPLY,Util.DEFAULT_BUTTON_APPLY).toString().toLowerCase()+" ");
        watch_now.setText(" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.WATCH_NOW,Util.DEFAULT_WATCH_NOW).toString().toLowerCase()+" ");
        voucher_code.setHint(" "+ Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.VOUCHER_CODE,Util.DEFAULT_VOUCHER_CODE).toString().toLowerCase()+" ");


        //==============End===============//

        voucher_code.setText("");
        watch_now.setBackgroundResource(R.drawable.voucher_inactive_button);
        watch_now.setTextColor(Color.parseColor("#7f7f7f"));

        voucher_success.setVisibility(View.INVISIBLE);

        content_name.setText(ContentName);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VoucherCode = voucher_code.getText().toString().trim();
                if(!VoucherCode.equals(""))
                {
                    voucher_alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    ValidateVoucher_And_VoucherSubscription();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.VOUCHER_BLANK_MESSAGE,Util.DEFAULT_VOUCHER_BLANK_MESSAGE), Toast.LENGTH_SHORT).show();
                }
            }
        });

        watch_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(watch_status)
                {
                    voucher_alert.dismiss();

                    // Calling Voucher Subscription Api

                    AsynVoucherSubscription asynVoucherSubscription = new AsynVoucherSubscription();
                    asynVoucherSubscription.executeOnExecutor(threadPoolExecutor);

                }
            }
        });
        voucher_alert = alertDialog.show();
        voucher_alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void ValidateVoucher_And_VoucherSubscription()
    {
        // Calling Validate Voucher Api

        AsynValidateVoucher asynValidateVoucher = new AsynValidateVoucher();
        asynValidateVoucher.executeOnExecutor(threadPoolExecutor);
    }*/


    /*private class AsynValidateVoucher extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1;

        int status;
        String responseStr;
        String loggedInIdStr;
        String message = "Invalid Voucher.";



        @Override
        protected Void doInBackground(Void... params) {

            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }

            try {
                String urlRouteList = Util.rootUrl().trim()+Util.ValidateVoucher.trim();
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("movie_id", movieUniqueId.trim());
                httppost.addHeader("stream_id", Util.dataModel.getStreamUniqueId());
                httppost.addHeader("season", Util.dataModel.getSeason_id());
                httppost.addHeader("voucher_code", VoucherCode);
                httppost.addHeader("purchase_type", "episode");

             *//*   if(PurchageType.trim().equals("show")){
                    httppost.addHeader("purchase_type","show");
                }
                else {
                    httppost.addHeader("purchase_type","episode");
                }*//*

                httppost.addHeader("lang_code",Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

               *//* String urlRouteList = "http://www.idogic.com/rest/ValidateVoucher";
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken","b7fbac553a14c99adcf079be6b48fd9f");
                httppost.addHeader("user_id","5146");
                httppost.addHeader("movie_id","5b24dfaf49a996b04ef92c272bde21f0");
                httppost.addHeader("stream_id","de4fcc9ffcc0b7d3ae1468765290685f");
                httppost.addHeader("season","1");
                httppost.addHeader("voucher_code", VoucherCode);
               if(PurchageType.trim().equals("show")){
                    httppost.addHeader("purchase_type","show");
                }
                else {
                    httppost.addHeader("purchase_type","episode");
                }
*//*


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    LogUtil.showLog("BIBHU" , "Response Of validate voucher  = "+responseStr);


                } catch (final org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog1 != null && progressDialog1.isShowing()) {
                                progressDialog1.hide();
                                progressDialog1 = null;
                            }
                            status = 0;
                            Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    if (progressDialog1 != null && progressDialog1.isShowing()) {
                        progressDialog1.hide();
                        progressDialog1 = null;
                    }
                    status = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                }
                else
                {
                    status = 0;
                }
            }
            catch (Exception e) {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
                status = 0;
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }

            if(status == 200)
            {

                voucher_success.setVisibility(View.VISIBLE);
                watch_now.setBackgroundResource(R.drawable.button_radious);
                watch_now.setTextColor(Color.parseColor("#ffffff"));
                watch_status = true;

                apply.setEnabled(false);
                apply.setBackgroundResource(R.drawable.voucher_inactive_button);
                apply.setTextColor(Color.parseColor("#7f7f7f"));

            }
            else
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog1 = new ProgressDialog(ShowWithEpisodesActivity.this,R.style.MyTheme);
            progressDialog1.setCancelable(false);
            progressDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog1.setIndeterminate(false);
            progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_rawable));
            progressDialog1.show();
        }
    }

    private class AsynVoucherSubscription extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1;

        int status;
        String responseStr;
        String loggedInIdStr;
        String message = "";

        @Override
        protected Void doInBackground(Void... params) {

            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }

            try {

                String urlRouteList = Util.rootUrl().trim()+Util.VoucherSubscription.trim();
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("movie_id", movieUniqueId.trim());
                httppost.addHeader("stream_id", Util.dataModel.getStreamUniqueId());
                httppost.addHeader("season", Util.dataModel.getSeason_id());
                httppost.addHeader("voucher_code", VoucherCode);
                httppost.addHeader("lang_code",Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                httppost.addHeader("purchase_type", PurchageType);
               *//* if(PurchageType.trim().equals("show")){
                    httppost.addHeader("purchase_type","show");
                }
                else {
                    httppost.addHeader("purchase_type","episode");
                }*//*
                httppost.addHeader("is_preorder",""+Util.dataModel.getIsAPV());

               *//* String urlRouteList = "http://www.idogic.com/rest/VoucherSubscription";
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken","b7fbac553a14c99adcf079be6b48fd9f");
                httppost.addHeader("user_id","5146");
                httppost.addHeader("movie_id","5b24dfaf49a996b04ef92c272bde21f0");
                httppost.addHeader("stream_id","de4fcc9ffcc0b7d3ae1468765290685f");
                httppost.addHeader("season","de4fcc9ffcc0b7d3ae1468765290685f");
                httppost.addHeader("voucher_code", VoucherCode);
                if(PurchageType.trim().equals("show")){
                    httppost.addHeader("purchase_type","show");
                }
                else {
                    httppost.addHeader("purchase_type","episode");
                }
                httppost.addHeader("is_preorder",""+Util.dataModel.getIsAPV());*//*



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    LogUtil.showLog("BIBHU" , "Response Of VoucherSubscription  = "+responseStr);


                } catch (final org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog1 != null && progressDialog1.isShowing()) {
                                progressDialog1.hide();
                                progressDialog1 = null;
                            }
                            status = 0;
                            Toast.makeText(ShowWithEpisodesActivity.this, Util.getTextofLanguage(ShowWithEpisodesActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    if (progressDialog1 != null && progressDialog1.isShowing()) {
                        progressDialog1.hide();
                        progressDialog1 = null;
                    }
                    status = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                }
                else
                {
                    status = 0;
                }
            }
            catch (Exception e) {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
                status = 0;
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (progressDialog1 != null && progressDialog1.isShowing()) {
                    progressDialog1.hide();
                    progressDialog1 = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }

            if(status == 200)
            {
               *//* voucher_success.setVisibility(View.VISIBLE);
                watch_now.setBackgroundResource(R.drawable.button_radious);
                watch_now.setTextColor(Color.parseColor("#ffffff"));
                watch_status = true;

                apply.setEnabled(false);
                apply.setBackgroundResource(R.drawable.voucher_inactive_button);
                apply.setTextColor(Color.parseColor("#7f7f7f"));*//*

                AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
            }
            else
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog1 = new ProgressDialog(ShowWithEpisodesActivity.this,R.style.MyTheme);
            progressDialog1.setCancelable(false);
            progressDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog1.setIndeterminate(false);
            progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_rawable));
            progressDialog1.show();
        }
    }*/

   /* private class AsynFavoriteAdd extends AsyncTask<String, Void, Void> {


        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String loggedInIdStr;
        String responseStr;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {


        //    pref = getSharedPreferences(Util.LOGIN_PREF, 0);



            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }
            String urlRouteList = Util.rootUrl().trim() + Util.AddtoFavlist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", movieUniqueId);
                httppost.addHeader("content_type", isEpisode);
                httppost.addHeader("user_id", loggedInIdStr);
                LogUtil.showLog("ANU","SHOWTOAST h......isepisode"+isEpisode);
                LogUtil.showLog("ANU","Value......."+movieUniqueId);
                LogUtil.showLog("ANU","login data"+loggedInIdStr.trim());


                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            pref = getSharedPreferences(Util.LOGIN_PREF, 0);
            String loggedInStr = pref.getString("PREFS_LOGGEDIN_KEY", null);

            favorite_view_episode.setImageResource(R.drawable.favorite_red);
            isFavorite=1;

            showToast();
            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }

        }

    }*/

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

    /* private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {


         String contName;
         JSONObject myJson = null;
         int status;
         ProgressBarHandler pDialog;
         String loggedInIdStr;

         String contMessage;
         String responseStr;

 //    @Override
 //    protected void onPreExecute() {
 //        pDialog = new ProgressBarHandler(getActivity().getBaseContext());
 //        pDialog.show();
 //        LogUtil.showLog("NIhar","onpreExecution");
 //    }

         @Override
         protected Void doInBackground(String... params) {

             pref = getSharedPreferences(Util.LOGIN_PREF, 0);

             if (pref != null) {
                 loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
             }

             String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

             try {
                 HttpClient httpclient = new DefaultHttpClient();
                 HttpPost httppost = new HttpPost(urlRouteList);
                 httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                 httppost.addHeader("authToken", Util.authTokenStr.trim());
                 httppost.addHeader("movie_uniq_id", movieUniqueId);
                 httppost.addHeader("content_type", isEpisode);
                 httppost.addHeader("user_id", loggedInIdStr);

                 try {
                     HttpResponse response = httpclient.execute(httppost);
                     responseStr = EntityUtils.toString(response.getEntity());


                 } catch (org.apache.http.conn.ConnectTimeoutException e) {

                 }
             } catch (IOException e) {

                 e.printStackTrace();
             }
             if (responseStr != null) {
                 try {
                     myJson = new JSONObject(responseStr);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 status = Integer.parseInt(myJson.optString("code"));
                 sucessMsg = myJson.optString("msg");
 //                statusmsg = myJson.optString("status");


             }
             return null;
         }

         @Override
         protected void onPostExecute(Void aVoid) {
             favorite_view_episode.setImageResource(R.drawable.favorite_unselected);
             showToast();
             isFavorite = 0;
             if(pDialog.isShowing()&& pDialog!=null)
             {
                 pDialog.hide();
             }






         }
         @Override
         protected void onPreExecute() {

             pDialog = new ProgressBarHandler(ShowWithEpisodesActivity.this);
             pDialog.show();

         }
     }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* loginresultcode = requestCode;
        if (requestCode == 40500) {
            if (resultCode == RESULT_OK) {
                Util.favorite_clicked = false;
                LogUtil.showLog("BKS", "ifclickedddddd");
//            call feb Api
                loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

//            AsynFavoriteAdd asynFavoriteAdd =new AsynFavoriteAdd();
//            asynFavoriteAdd.execute();
                AsynLoadMovieDetails asyncLoadVideos = new AsynLoadMovieDetails();
                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
            } else {
                LogUtil.showLog("BKS", "elseclickedddddd");
                Util.favorite_clicked = false;
//            AsynFavoriteDelete asynFavoriteDelete=new AsynFavoriteDelete();
//            asynFavoriteDelete.execute();
            }

        }
//*/
        if (requestCode == 30060) {
            if (NetworkStatus.getInstance().isConnected(this)) {

                LogUtil.showLog("MUVI", "CODE");
                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                contentDetailsInput.setAuthToken(authTokenStr);
                contentDetailsInput.setPermalink(permalinkStr);

                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }

        }


    }

    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        ipAddres=ipAddressStr;
        return;
    }


    public void GetReviewDetails() {

        ViewContentRatingInputModel viewContentRatingInputModel = new ViewContentRatingInputModel();
        viewContentRatingInputModel.setAuthToken(authTokenStr);
        viewContentRatingInputModel.setUser_id(preferenceManager.getUseridFromPref());
        viewContentRatingInputModel.setContent_id(movieIdStr.trim());
        viewContentRatingInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        asynGetReviewDetails = new ViewContentRatingAsynTask(viewContentRatingInputModel, ShowWithEpisodesActivity.this, ShowWithEpisodesActivity.this);
        asynGetReviewDetails.executeOnExecutor(threadPoolExecutor);

    }
    //Asyntask for getDetails of the csat and crew members.


}
