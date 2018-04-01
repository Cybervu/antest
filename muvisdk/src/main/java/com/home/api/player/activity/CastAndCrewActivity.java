package com.home.api.player.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.api.api.APIUrlConstant;
import com.home.api.api.apiController.APICallManager;
import com.home.api.api.apiModel.GetCelebrityModel;
import com.home.api.player.CastAndCrewDetailsIntentHandler;
import com.home.api.player.adapter.CastCrewAdapter;
import com.home.api.player.model.GetCastCrewItem;
import com.home.api.player.network.NetworkStatus;
import com.home.api.player.preferences.LanguagePreference;
import com.home.api.player.util.FontUtls;
import com.home.api.player.util.LogUtil;
import com.home.api.player.util.ProgressBarHandler;
import com.home.apisdk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.home.api.player.preferences.LanguagePreference.BUTTON_OK;
import static com.home.api.player.preferences.LanguagePreference.CAST_CREW_BUTTON_TITLE;
import static com.home.api.player.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.api.player.preferences.LanguagePreference.DEFAULT_CAST_CREW_BUTTON_TITLE;
import static com.home.api.player.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.api.player.preferences.LanguagePreference.DEFAULT_NO_CONTENT;
import static com.home.api.player.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.api.player.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.api.player.preferences.LanguagePreference.FAILURE;
import static com.home.api.player.preferences.LanguagePreference.NO_CONTENT;
import static com.home.api.player.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.api.player.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.api.player.util.Constant.authTokenStr;

public class CastAndCrewActivity extends AppCompatActivity implements APICallManager.ApiInterafce {


    Toolbar mActionBarToolbar;
    TextView castCrewTitleTextView;
    RecyclerView castCrewListRecyclerView;

    ArrayList<GetCastCrewItem> castCrewItems = new ArrayList<GetCastCrewItem>();
    CastCrewAdapter castCrewAdapter;
    GridView cast_crew_crid;

    RelativeLayout noInternetLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    ProgressBarHandler pDialog;
    TextView noInternetTextView;

    LinearLayout primary_layout;
    boolean isNetwork;

    String movie_id, movie_uniq_id;
    LanguagePreference languagePreference;
    CastAndCrewDetailsIntentHandler castAndCrewDetailsIntentHandler;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_and_crew);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noInternetLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));
        castAndCrewDetailsIntentHandler = new CastAndCrewDetailsIntentHandler(this);

        primary_layout = (LinearLayout) findViewById(R.id.primary_layout);
        castCrewTitleTextView = (TextView) findViewById(R.id.castCrewTitleTextView);
        FontUtls.loadFont(CastAndCrewActivity.this, getResources().getString(R.string.regular_fonts), castCrewTitleTextView);
        castCrewTitleTextView.setText(languagePreference.getTextofLanguage(CAST_CREW_BUTTON_TITLE, DEFAULT_CAST_CREW_BUTTON_TITLE));
        cast_crew_crid = (GridView) findViewById(R.id.cast_crew_crid);
        isNetwork = NetworkStatus.getInstance().isConnected(this);


        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            cast_crew_crid.setNumColumns(2);

        } else {
            //"Mobile";
            cast_crew_crid.setNumColumns(1);
        }
        castCrewAdapter = new CastCrewAdapter(CastAndCrewActivity.this, castCrewItems);
        cast_crew_crid.setAdapter(castCrewAdapter);
        GetCsatCrewDetails();
        cast_crew_crid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetCastCrewItem item = castCrewItems.get(position);
                Intent castCrewIntent = new Intent(CastAndCrewActivity.this, CastCrewDetailsActivity.class);
                castCrewIntent.putExtra("castPermalink", item.getCastPermalink());
                LogUtil.showLog("SUBHA", "PERMALINK_INTENT_KEY" + item.getCastPermalink());

                castCrewIntent.putExtra("castName", item.getCastPermalink());
                castCrewIntent.putExtra("castSummary", item.getCelebritySummary());
                castCrewIntent.putExtra("castImage", item.getCastImage());

                castAndCrewDetailsIntentHandler.castandcrewdetailsIntentShowORHide(castCrewIntent);
            }
        });


    }


    public void GetCsatCrewDetails() {
        noInternetLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        primary_layout.setVisibility(View.VISIBLE);

        final HashMap parameters = new HashMap<>();
        parameters.put("authToken", authTokenStr);
        parameters.put("movie_id", getIntent().getStringExtra("cast_movie_id"));
        parameters.put("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        final APICallManager apiCallManager = new APICallManager(this, APIUrlConstant.GET_CELIBRITY_URL, parameters, APIUrlConstant.GET_CELIBRITY_URL_REQUEST_ID, APIUrlConstant.BASE_URl);
        apiCallManager.startApiProcessing();

    }

    public void ShowDialog(String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CastAndCrewActivity.this, R.style.MyAlertDialogStyle);
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

    @Override
    public void onTaskPreExecute(int requestID) {
        pDialog = new ProgressBarHandler(CastAndCrewActivity.this);
        pDialog.show();
    }

    @Override
    public void onTaskPostExecute(Object object, int requestID, String response) {
        if (APIUrlConstant.GET_CELIBRITY_URL_REQUEST_ID == requestID) {
            celebrity(object, requestID, response);
        }
    }

    public void celebrity(Object object, int requestID, String response) {
        GetCelebrityModel getCelebrityModel = (GetCelebrityModel) object;

        if (getCelebrityModel != null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }

            } catch (IllegalArgumentException ex) {
            }
            if (getCelebrityModel.getCode() == 200) {
                //castCrewItems = new ArrayList<GetCastCrewItem>();
                for (int i = 0; i < getCelebrityModel.getCelibrity().size(); i++) {
                    GetCastCrewItem movie = new GetCastCrewItem(getCelebrityModel.getCelibrity().get(i).getName(), getCelebrityModel.getCelibrity().get(i).getCastType()
                            , getCelebrityModel.getCelibrity().get(i).getCelebrityImage()
                            , getCelebrityModel.getCelibrity().get(i).getPermalink(), getCelebrityModel.getCelibrity().get(i).getSummary());

                    castCrewItems.add(movie);
                }

                noDataLayout.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.GONE);
                primary_layout.setVisibility(View.VISIBLE);
                // Set the grid adapter here.
                castCrewAdapter.notifyDataSetChanged();
            } else if (getCelebrityModel.getCode() == 448) {
                ShowDialog(getCelebrityModel.getMsg());
            } else if (getCelebrityModel.getCode() == 0) {
                primary_layout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetLayout.setVisibility(View.GONE);
            }
        }

    }

   /* @Override
    public void onBackPressed()
    {
        finish();
//        overridePendingTransition(0, 0);
        super.onBackPressed();
    }*/


}
