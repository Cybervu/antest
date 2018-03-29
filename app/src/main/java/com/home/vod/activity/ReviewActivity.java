package com.home.vod.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.api.api.APIUrlConstant;
import com.home.api.api.apiController.APICallManager;
import com.home.api.api.apiModel.AddContentRatingModel;
import com.home.api.api.apiModel.ViewContentRatingModel;
import com.home.vod.R;
import com.home.vod.adapter.ReviewsAdapter;
import com.home.vod.model.ReviewsItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.BTN_POST_REVIEW;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CLICK_HERE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_POST_REVIEW;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CLICK_HERE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REVIEW_HERE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ERROR_IN_DATA_FETCHING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NEED_LOGIN_TO_REVIEW;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SUBMIT_YOUR_RATING_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TO_LOGIN;
import static com.home.vod.preferences.LanguagePreference.ENTER_REVIEW_HERE;
import static com.home.vod.preferences.LanguagePreference.ERROR_IN_DATA_FETCHING;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.NEED_LOGIN_TO_REVIEW;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SUBMIT_YOUR_RATING_TITLE;
import static com.home.vod.preferences.LanguagePreference.TO_LOGIN;
import static com.home.vod.util.Constant.authTokenStr;


public class ReviewActivity extends AppCompatActivity implements APICallManager.ApiInterafce {

    Toolbar mActionBarToolbar;
    ProgressBarHandler pDialog;
    ArrayList<ReviewsItem> reviewsItem = new ArrayList<ReviewsItem>();
    ReviewsAdapter reviewsAdapter;
    GridView reviewsGridView;


    /* RelativeLayout noInternetLayout;
     RelativeLayout noDataLayout;
     TextView noDataTextView;
     TextView noInternetTextView;*/
    int isLogin = 0;
    PreferenceManager preferenceManager;
    LanguagePreference languagePreference;
    //  LinearLayout primary_layout;
    boolean isNetwork;
    int showRating = 0;
    String movie_id;
    TextView submitTitleTextView;
    EditText submitReviewTextView;
    RelativeLayout submitRatingLayout;
    Button submitButton;
    RatingBar addRatingBar;
    TextView clickHereToLogin;
    String reviewMessage = "";
    String ratingStr = "";
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        isLogin = preferenceManager.getLoginFeatureFromPref();

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitRatingLayout = (RelativeLayout) findViewById(R.id.submitRatingLayout);
        clickHereToLogin = (TextView) findViewById(R.id.clickHereToLogin);
        submitTitleTextView = (TextView) findViewById(R.id.sectionTitle);
        submitReviewTextView = (EditText) findViewById(R.id.reviewEditText);
        submitButton = (Button) findViewById(R.id.submitReviewButton);
        addRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        FontUtls.loadFont(ReviewActivity.this, getResources().getString(R.string.light_fonts), submitButton);
        FontUtls.loadFont(ReviewActivity.this, getResources().getString(R.string.light_fonts), submitTitleTextView);
        submitTitleTextView.setText(languagePreference.getTextofLanguage(SUBMIT_YOUR_RATING_TITLE, DEFAULT_SUBMIT_YOUR_RATING_TITLE));
        submitButton.setText(languagePreference.getTextofLanguage(BTN_POST_REVIEW, DEFAULT_BTN_POST_REVIEW));

        submitReviewTextView.setHint(languagePreference.getTextofLanguage(ENTER_REVIEW_HERE, DEFAULT_ENTER_REVIEW_HERE));
        String clickHereStr = languagePreference.getTextofLanguage(NEED_LOGIN_TO_REVIEW, DEFAULT_NEED_LOGIN_TO_REVIEW) + " " + languagePreference.getTextofLanguage(CLICK_HERE, DEFAULT_CLICK_HERE) + " " + languagePreference.getTextofLanguage(TO_LOGIN, DEFAULT_TO_LOGIN);

        /*******enter key of keyboard *************/

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (source.charAt(i) == '\n') {
                        return " ";
                    }
                }
                return null;
            }
        };

        submitReviewTextView.setFilters(new InputFilter[]{filter});

        SpannableString mySpannableString = new SpannableString(clickHereStr);
        mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
        clickHereToLogin.setText(mySpannableString);
        clickHereToLogin.setVisibility(View.GONE);
        clickHereToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent registerActivity = new Intent(ReviewActivity.this, LoginActivity.class);
                runOnUiThread(new Runnable() {
                    public void run() {
                        registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        registerActivity.putExtra("from", this.getClass().getName());
                        startActivity(registerActivity);

                    }
                });

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewMessage = submitReviewTextView.getText().toString().trim();
                ratingStr = Float.toString(addRatingBar.getRating());


                final HashMap parameters = new HashMap<>();
                parameters.put("authToken", authTokenStr);
                parameters.put("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                parameters.put("user_id", preferenceManager.getUseridFromPref());
                parameters.put("content_id", getIntent().getStringExtra("muviId"));
                parameters.put("rating", ratingStr);
                parameters.put("review", reviewMessage);
                final APICallManager apiCallManager1 = new APICallManager(ReviewActivity.this, APIUrlConstant.ADD_CONTENT_RATING, parameters, APIUrlConstant.ADD_CONTENT_RATING_REQUEST_ID, APIUrlConstant.BASE_URl);
                apiCallManager1.startApiProcessing();
               /* AddContentRatingInputModel addContentRatingInputModel = new AddContentRatingInputModel();
                addContentRatingInputModel.setUser_id(preferenceManager.getUseridFromPref());
                addContentRatingInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                addContentRatingInputModel.setContent_id(getIntent().getStringExtra("muviId"));
                addContentRatingInputModel.setAuthToken(authTokenStr.trim());
                addContentRatingInputModel.setRating(ratingStr);
                addContentRatingInputModel.setReview(reviewMessage);

                AddContentRatingAsynTask addContentRatingAsynTask = new AddContentRatingAsynTask(addContentRatingInputModel, ReviewActivity.this, ReviewActivity.this);
                addContentRatingAsynTask.executeOnExecutor(threadPoolExecutor);
*/

            }
        });


        //  primary_layout = (LinearLayout)findViewById(R.id.primary_layout);
        reviewsGridView = (GridView) findViewById(R.id.reviewsList);
        isNetwork = NetworkStatus.getInstance().isConnected(this);
        reviewsGridView.setNumColumns(1);




       /* if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            reviewsGridView.setNumColumns(2);

        }else {
            //"Mobile";
            reviewsGridView.setNumColumns(1);
        }*/

        // GetReviewDetails();


    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if(isLogin == 1) {
            if (pref != null) {
                LogUtil.showLog("MUVI","FHFH");
                String loggedInStr = pref.getString("PREFS_LOGGEDIN_KEY", null);
                if (loggedInStr == null) {
                    LogUtil.showLog("MUVI","loggedInStr");

                    clickHereToLogin.setVisibility(View.VISIBLE);
                    submitRatingLayout.setVisibility(View.GONE);
                }else{
                    LogUtil.showLog("MUVI","loggedInStr1");

                    clickHereToLogin.setVisibility(View.GONE);
                    submitRatingLayout.setVisibility(View.VISIBLE);
                }
            }else{
                LogUtil.showLog("MUVI","loggedInStr2");

                clickHereToLogin.setVisibility(View.VISIBLE);
                submitRatingLayout.setVisibility(View.GONE);
            }
        }else{
            LogUtil.showLog("MUVI","loggedInStr3");

            clickHereToLogin.setVisibility(View.GONE);
            submitRatingLayout.setVisibility(View.GONE);

        }*/
        GetReviewDetails();
    }

    public void GetReviewDetails() {
        if (isNetwork) {
            String muviid = getIntent().getStringExtra("muviId");

            final HashMap parameters = new HashMap<>();
            parameters.put("authToken", authTokenStr);
            parameters.put("user_id", preferenceManager.getUseridFromPref());
            parameters.put("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            parameters.put("content_id", muviid);

            final APICallManager apiCallManager = new APICallManager(this, APIUrlConstant.VIEW_CONTENT_RATING, parameters, APIUrlConstant.VIEW_CONTENT_RATING_REQUEST_ID, APIUrlConstant.BASE_URl);
            apiCallManager.startApiProcessing();
           /* ViewContentRatingInputModel viewContentRatingInputModel = new ViewContentRatingInputModel();
            viewContentRatingInputModel.setAuthToken(authTokenStr);
            viewContentRatingInputModel.setContent_id(muviid);
            viewContentRatingInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            viewContentRatingInputModel.setUser_id(preferenceManager.getUseridFromPref());

            ViewContentRatingAsynTask viewContentRatingAsynTask = new ViewContentRatingAsynTask(viewContentRatingInputModel, ReviewActivity.this, ReviewActivity.this);
            viewContentRatingAsynTask.executeOnExecutor(threadPoolExecutor);*/
        } else {
            Util.showToast(ReviewActivity.this, languagePreference.getTextofLanguage(ERROR_IN_DATA_FETCHING, DEFAULT_ERROR_IN_DATA_FETCHING));
        }


    }

 /*   @Override
    public void onViewContentRatingPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ReviewActivity.this);
        pDialog.show();
    }

    @Override
    public void onViewContentRatingPostExecuteCompleted(ViewContentRatingOutputModel viewContentRatingOutputModel, int status, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }

        } catch (IllegalArgumentException ex) {
        }

        if (status > 0) {
            if (status == 200) {

               *//* for (int a = 0; a < viewContentRatingOutputModel.getRatingArray().size(); a++) {

                    if(viewContentRatingOutputModel.getRatingArray().get(a).getStatus().equals("1")){
                        ReviewsItem reviewItem = new ReviewsItem(viewContentRatingOutputModel.getRatingArray().get(a).getReview()
                                , viewContentRatingOutputModel.getRatingArray().get(a).getDisplay_name(),
                                viewContentRatingOutputModel.getRatingArray().get(a).getRating());
                        reviewsItem.add(reviewItem);
                    }
                }*//*
                //LogUtil.showLog("MUVI", "Review activity activity_login featrure ::"+preferenceManager.getLoginFeatureFromPref());
                if (preferenceManager.getLoginFeatureFromPref() == 1) {

                    String loggedInStr = preferenceManager.getLoginStatusFromPref();
                    if (loggedInStr == null) {
                        LogUtil.showLog("MUVI", "loggedInStr");

                        clickHereToLogin.setVisibility(View.VISIBLE);
                        submitRatingLayout.setVisibility(View.GONE);
                    } else {
                        if (viewContentRatingOutputModel.getShowrating() == 0) {
                            submitRatingLayout.setVisibility(View.GONE);
                        } else {
                            submitRatingLayout.setVisibility(View.VISIBLE);

                        }
                        clickHereToLogin.setVisibility(View.GONE);
                        // submitRatingLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    LogUtil.showLog("MUVI", "loggedInStr2");

                    clickHereToLogin.setVisibility(View.VISIBLE);
                    submitRatingLayout.setVisibility(View.GONE);
                }
            }
            reviewsAdapter = new ReviewsAdapter(ReviewActivity.this, viewContentRatingOutputModel.getRatingArray());
            reviewsGridView.setAdapter(reviewsAdapter);

        } else {

        }
        *//*if(preferenceManager.getLoginFeatureFromPref() == 1) {
            if (preferenceManager.getLoginFeatureFromPref() == 1) {

                String loggedInStr = preferenceManager.getUseridFromPref();
                if (loggedInStr == null) {
                    LogUtil.showLog("MUVI","loggedInStr");

                    clickHereToLogin.setVisibility(View.VISIBLE);
                    submitRatingLayout.setVisibility(View.GONE);
                }else{


                    if (viewContentRatingOutputModel.getShowrating() == 0){
                        submitRatingLayout.setVisibility(View.GONE);
                    }else{
                        submitRatingLayout.setVisibility(View.VISIBLE);

                    }
                    clickHereToLogin.setVisibility(View.GONE);
                    // submitRatingLayout.setVisibility(View.VISIBLE);
                }
            }else{
                LogUtil.showLog("MUVI","loggedInStr2");

                clickHereToLogin.setVisibility(View.VISIBLE);
                submitRatingLayout.setVisibility(View.GONE);
            }
        }else{
            LogUtil.showLog("MUVI","loggedInStr3");

            clickHereToLogin.setVisibility(View.GONE);
            submitRatingLayout.setVisibility(View.GONE);

        }

        ;
        reviewsAdapter = new ReviewsAdapter(ReviewActivity.this,viewContentRatingOutputModel.getRatingArray());
        reviewsGridView.setAdapter(reviewsAdapter);*//*

    }*/
    //Asyntask for getDetails of the csat and crew members.


    public void ShowDialog(String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ReviewActivity.this);
        dlgAlert.setTitle(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE));
        dlgAlert.setMessage(msg);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        dlgAlert.create().show();
    }

   /* @Override
    public void onAddContentRatingPreExecuteStarted() {
        pDialog = new ProgressBarHandler(ReviewActivity.this);
        pDialog.show();
    }

    @Override
    public void onAddContentRatingPostExecuteCompleted(AddContentRatingOutputModel addContentRatingOutputModel, int status, String message) {


        if ((status != 200)) {
            ShowDialog(message);
        } else {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

        }


    }*/

    @Override
    public void onTaskPreExecute(int requestID) {

    }

    @Override
    public void onTaskPostExecute(Object object, int requestID, String response) {
        if (APIUrlConstant.ADD_CONTENT_RATING_REQUEST_ID == requestID) {
            add_content_rating(object, requestID, response);
        } else if (APIUrlConstant.VIEW_CONTENT_RATING_REQUEST_ID == requestID) {
            view_content_rating(object, requestID, response);
        }
    }

    public void view_content_rating(Object object, int requestID, String response) {

        ViewContentRatingModel viewContentRatingModel = (ViewContentRatingModel) object;

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }

        } catch (IllegalArgumentException ex) {
        }

        if (viewContentRatingModel.getCode() > 0) {
            if (viewContentRatingModel.getCode() == 200) {

               /* for (int a = 0; a < viewContentRatingOutputModel.getRatingArray().size(); a++) {

                    if(viewContentRatingOutputModel.getRatingArray().get(a).getStatus().equals("1")){
                        ReviewsItem reviewItem = new ReviewsItem(viewContentRatingOutputModel.getRatingArray().get(a).getReview()
                                , viewContentRatingOutputModel.getRatingArray().get(a).getDisplay_name(),
                                viewContentRatingOutputModel.getRatingArray().get(a).getRating());
                        reviewsItem.add(reviewItem);
                    }
                }*/
                //LogUtil.showLog("MUVI", "Review activity activity_login featrure ::"+preferenceManager.getLoginFeatureFromPref());
                if (preferenceManager.getLoginFeatureFromPref() == 1) {

                    String loggedInStr = preferenceManager.getLoginStatusFromPref();
                    if (loggedInStr == null) {
                        LogUtil.showLog("MUVI", "loggedInStr");

                        clickHereToLogin.setVisibility(View.VISIBLE);
                        submitRatingLayout.setVisibility(View.GONE);
                    } else {
                        if (viewContentRatingModel.getShowrating() == 0) {
                            submitRatingLayout.setVisibility(View.GONE);
                        } else {
                            submitRatingLayout.setVisibility(View.VISIBLE);

                        }
                        clickHereToLogin.setVisibility(View.GONE);
                        // submitRatingLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    LogUtil.showLog("MUVI", "loggedInStr2");

                    clickHereToLogin.setVisibility(View.VISIBLE);
                    submitRatingLayout.setVisibility(View.GONE);
                }
            }
            reviewsAdapter = new ReviewsAdapter(ReviewActivity.this, viewContentRatingModel.getRating());
            reviewsGridView.setAdapter(reviewsAdapter);

        } else {

        }
        /*if(preferenceManager.getLoginFeatureFromPref() == 1) {
            if (preferenceManager.getLoginFeatureFromPref() == 1) {

                String loggedInStr = preferenceManager.getUseridFromPref();
                if (loggedInStr == null) {
                    LogUtil.showLog("MUVI","loggedInStr");

                    clickHereToLogin.setVisibility(View.VISIBLE);
                    submitRatingLayout.setVisibility(View.GONE);
                }else{


                    if (viewContentRatingOutputModel.getShowrating() == 0){
                        submitRatingLayout.setVisibility(View.GONE);
                    }else{
                        submitRatingLayout.setVisibility(View.VISIBLE);

                    }
                    clickHereToLogin.setVisibility(View.GONE);
                    // submitRatingLayout.setVisibility(View.VISIBLE);
                }
            }else{
                LogUtil.showLog("MUVI","loggedInStr2");

                clickHereToLogin.setVisibility(View.VISIBLE);
                submitRatingLayout.setVisibility(View.GONE);
            }
        }else{
            LogUtil.showLog("MUVI","loggedInStr3");

            clickHereToLogin.setVisibility(View.GONE);
            submitRatingLayout.setVisibility(View.GONE);

        }

        ;
        reviewsAdapter = new ReviewsAdapter(ReviewActivity.this,viewContentRatingOutputModel.getRatingArray());
        reviewsGridView.setAdapter(reviewsAdapter);*/

    }

    public void add_content_rating(Object object, int requestID, String response) {
        AddContentRatingModel addContentRatingModel = (AddContentRatingModel) object;

        if ((addContentRatingModel.getCode() != 200)) {
            ShowDialog(addContentRatingModel.getMsg());
        } else {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

        }
    }


}
