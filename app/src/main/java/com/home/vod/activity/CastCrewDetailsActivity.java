package com.home.vod.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.api.api.APIUrlConstant;
import com.home.api.api.apiController.APICallManager;
import com.home.api.api.apiModel.GetCastDetailsModel;
import com.home.vod.R;
import com.home.vod.adapter.FilmographyAdapter;
import com.home.vod.model.GridItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.ResizableCustomView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FILMOGRAPHY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.FILMOGRAPHY;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

public class CastCrewDetailsActivity extends AppCompatActivity implements APICallManager.ApiInterafce {
    ProgressBarHandler pDialog;
    Toolbar mActionBarToolbar;
    TextView castNameTextView, castDescriptionTextView, filmography;
    ImageView castImageView;
    Button btnmore;
    String castpermalinkStr = "";
    RecyclerView filmographyRecyclerView;
    int itemsInServer = 0;
    int videoHeight = 185;
    int videoWidth = 256;
    String videoImageStrToHeight;
    String castNameStr = "";
    String castSummaryStr = "";
    String castImageStr = "";
    private PreferenceManager preferenceManager;

    ArrayList<GridItem> filmogrpahyItems = new ArrayList<GridItem>();
    FilmographyAdapter filmographyAdapter;
    AsynLOADUI loadUI;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    LanguagePreference languagePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_crew_details);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (getIntent().getStringExtra("castPermalink") != null) {
            castpermalinkStr = getIntent().getStringExtra("castPermalink");

        }
        if (getIntent().getStringExtra("castName") != null) {
            castNameStr = getIntent().getStringExtra("castName");
        }
        if (getIntent().getStringExtra("castSummary") != null) {
            castSummaryStr = getIntent().getStringExtra("castSummary");
        }
        if (getIntent().getStringExtra("castImage") != null) {
            castImageStr = getIntent().getStringExtra("castImage");
        }

        castNameTextView = (TextView) findViewById(R.id.castNameTextView);
        filmography = (TextView) findViewById(R.id.filmography);
        filmographyRecyclerView = (RecyclerView) findViewById(R.id.filmographyRecyclerView);
        castImageView = (ImageView) findViewById(R.id.castImageView);
        // Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        // castNameTextView.setTypeface(videoGenreTextViewTypeface);
        castDescriptionTextView = (TextView) findViewById(R.id.castDescriptionTextView);
        // btnmore = (Button) findViewById(R.id.btnMore);
//        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), btnmore);
        //btnmore.setText(languagePreference.getTextofLanguage(VIEW_ALL, DEFAULT_VIEW_ALL));
        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), filmography);
        filmography.setText(languagePreference.getTextofLanguage(FILMOGRAPHY, DEFAULT_FILMOGRAPHY));
        filmography.setVisibility(View.GONE);

        filmographyRecyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(CastCrewDetailsActivity.this, filmographyRecyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {

                GridItem item = filmogrpahyItems.get(position);
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();

                if (moviePermalink.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CastCrewDetailsActivity.this, R.style.MyAlertDialogStyle);
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
                        final Intent movieDetailsIntent = new Intent(CastCrewDetailsActivity.this, MovieDetailsActivity.class);
                        movieDetailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(movieDetailsIntent);
                            }
                        });


                    } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                        final Intent detailsIntent = new Intent(CastCrewDetailsActivity.this, ShowWithEpisodesActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(detailsIntent);
                            }
                        });
                    }
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        //Typeface castDescriptionTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        // castDescriptionTextView.setTypeface(castDescriptionTextViewTypeface);
        // makeTextViewResizable(videoStoryTextView,3,movieDetailsStr,true);
        // castDescriptionTextView.setText("Hello");
        // ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, Util.getTextofLanguage(CastCrewDetailsActivity.this, Util.VIEW_MORE, Util.DEFAULT_VIEW_MORE), true);
        GetCastCrewDetails();


    }

    public void GetCastCrewDetails() {

        final HashMap parameters = new HashMap<>();
        parameters.put("authToken", authTokenStr);
        parameters.put("limit", String.valueOf(4));
        parameters.put("offset", String.valueOf(0));
        parameters.put("permalink", castpermalinkStr);
        parameters.put("country", preferenceManager.getCountryCodeFromPref());
        parameters.put("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

        final APICallManager apiCallManager = new APICallManager(this, APIUrlConstant.GET_CAST_DETAILS, parameters, APIUrlConstant.GET_CAST_DETAILS_REQUEST_ID, APIUrlConstant.BASE_URl);
        apiCallManager.startApiProcessing();
        /*GetCastDetailsInput getCastDetailsInput = new GetCastDetailsInput();
        getCastDetailsInput.setAuthToken(authTokenStr);
        getCastDetailsInput.setLimit(String.valueOf(4));
        getCastDetailsInput.setOffset(String.valueOf(0));
        getCastDetailsInput.setPermalink(castpermalinkStr);
        getCastDetailsInput.setCountry(preferenceManager.getCountryCodeFromPref());
        getCastDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

        GetCastDetailsAsynTask getCastDetailsAsynTask = new GetCastDetailsAsynTask(getCastDetailsInput, CastCrewDetailsActivity.this, CastCrewDetailsActivity.this);
        getCastDetailsAsynTask.executeOnExecutor(threadPoolExecutor);*/

       /* AsynLoadCastCrewDetails asynLoadCastCrewDetails = new AsynLoadCastCrewDetails();
        asynLoadCastCrewDetails.executeOnExecutor(threadPoolExecutor);*/

    }

    /*@Override
    public void onGetCastDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(CastCrewDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onGetCastDetailsPostExecuteCompleted(GetCastDetailsOutputModel getCastDetailsOutputModelArray, int status, int totalItems, String message) {

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;
        }

        String movieGenreStr = "";
        String movieName = "";
        String movieImageStr = "";
        String moviePermalinkStr = "";
        String videoTypeIdStr = "";
        String isEpisodeStr = "";

        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;

        if (status > 0) {
            if (status == 200) {
                for (int i = 0; i < getCastDetailsOutputModelArray.getCastdetails().size(); i++) {
                    movieImageStr = getCastDetailsOutputModelArray.getCastdetails().get(i).getPosterUrl();
                    movieName = getCastDetailsOutputModelArray.getCastdetails().get(i).getName();
                    videoTypeIdStr = getCastDetailsOutputModelArray.getCastdetails().get(i).getContentTypesId();
                    movieGenreStr = getCastDetailsOutputModelArray.getCastdetails().get(i).getGenre();
                    moviePermalinkStr = getCastDetailsOutputModelArray.getCastdetails().get(i).getPermalink();
                    isEpisodeStr = getCastDetailsOutputModelArray.getCastdetails().get(i).getIsEpisode();
                    isConverted = getCastDetailsOutputModelArray.getCastdetails().get(i).getIsConverted();
                    isPPV = getCastDetailsOutputModelArray.getCastdetails().get(i).getIsPPV();
                    isAPV = getCastDetailsOutputModelArray.getCastdetails().get(i).getIsAdvance();
                    filmography.setVisibility(View.VISIBLE);

                    filmogrpahyItems.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV));


                }


                *//*if (totalItems > 4) {
                    btnmore.setVisibility(View.VISIBLE);
                }*//*
                if (getCastDetailsOutputModelArray.getCastImage().trim().matches("")) {
                    castImageView.setImageResource(R.drawable.logo);
                } else {

                    Picasso.with(CastCrewDetailsActivity.this)
                            .load(getCastDetailsOutputModelArray.getCastImage())
                            .error(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .into(castImageView);

                }

                if (filmogrpahyItems.size() <= 0) {


                    if (castNameStr.matches("")) {
                        castNameTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), castNameTextView);
                        castNameTextView.setText(getCastDetailsOutputModelArray.getName());
                        castNameTextView.setVisibility(View.VISIBLE);

                    }
                    if (castSummaryStr.matches("")) {
                        castDescriptionTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.light_fonts), castDescriptionTextView);
                        castDescriptionTextView.setText(getCastDetailsOutputModelArray.getSummary());
                        ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);
                    }
                } else {
                    if (castNameStr.matches("")) {
                        castNameTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), castNameTextView);
                        castNameTextView.setText(getCastDetailsOutputModelArray.getName());
                        castNameTextView.setVisibility(View.VISIBLE);

                    }
                    if (castSummaryStr.matches("")) {
                        castDescriptionTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.light_fonts), castDescriptionTextView);
                        castDescriptionTextView.setText(getCastDetailsOutputModelArray.getSummary());
                        ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);

                    }

                    videoImageStrToHeight = movieImageStr;
                    Picasso.with(CastCrewDetailsActivity.this).load(videoImageStrToHeight
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

                        }
                    });

                }


            }
        } else {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            if (castNameStr.matches("")) {
                castNameTextView.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), castNameTextView);
                castNameTextView.setText(castNameStr);
                castNameTextView.setVisibility(View.VISIBLE);

            }
            if (castSummaryStr.matches("")) {
                castDescriptionTextView.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.light_fonts), castDescriptionTextView);
                castDescriptionTextView.setText(castSummaryStr);
                ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);
            }

        }
    }*/

    @Override
    public void onTaskPreExecute(int requestID) {
        pDialog = new ProgressBarHandler(CastCrewDetailsActivity.this);
        pDialog.show();
    }

    @Override
    public void onTaskPostExecute(Object object, int requestID, String response) {
        if (APIUrlConstant.GET_CAST_DETAILS_REQUEST_ID == requestID) {
            cast_details(object, requestID, response);
        }
    }

    public void cast_details(Object object, int requestID, String response) {

        GetCastDetailsModel getCastDetailsModel = (GetCastDetailsModel) object;

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;
        }

        ArrayList<String> movieGenreStr = null;
        String movieName = "";
        String movieImageStr = "";
        String moviePermalinkStr = "";
        String videoTypeIdStr = "";
        String isEpisodeStr = "";

        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;

        if (getCastDetailsModel.getStatus() > 0) {
            if (getCastDetailsModel.getStatus() == 200) {
                for (int i = 0; i < getCastDetailsModel.getMovieList().size(); i++) {
                    movieImageStr = getCastDetailsModel.getMovieList().get(i).getPosterUrl();
                    movieName = getCastDetailsModel.getMovieList().get(i).getName();
                    videoTypeIdStr = getCastDetailsModel.getMovieList().get(i).getContentTypesId();
                    movieGenreStr = getCastDetailsModel.getMovieList().get(i).getGenre();
                    moviePermalinkStr = getCastDetailsModel.getMovieList().get(i).getPermalink();
                    isEpisodeStr = getCastDetailsModel.getMovieList().get(i).getIsEpisode();
                    isConverted = getCastDetailsModel.getMovieList().get(i).getIsConverted();
                    isPPV = getCastDetailsModel.getMovieList().get(i).getIs_ppv();
                    isAPV = getCastDetailsModel.getMovieList().get(i).getIs_advance();
                    filmography.setVisibility(View.VISIBLE);

                    filmogrpahyItems.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr, isEpisodeStr, "", "", isConverted, isPPV, isAPV));


                }


                /*if (totalItems > 4) {
                    btnmore.setVisibility(View.VISIBLE);
                }*/
                if (getCastDetailsModel.getCastImage().trim().matches("")) {
                    castImageView.setImageResource(R.drawable.logo);
                } else {

                    Picasso.with(CastCrewDetailsActivity.this)
                            .load(getCastDetailsModel.getCastImage())
                            .error(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .into(castImageView);

                }

                if (filmogrpahyItems.size() <= 0) {


                    if (castNameStr.matches("")) {
                        castNameTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), castNameTextView);
                        castNameTextView.setText(getCastDetailsModel.getName());
                        castNameTextView.setVisibility(View.VISIBLE);

                    }
                    if (castSummaryStr.matches("")) {
                        castDescriptionTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.light_fonts), castDescriptionTextView);
                        castDescriptionTextView.setText(getCastDetailsModel.getSummary());
                        ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);
                    }
                } else {
                    if (castNameStr.matches("")) {
                        castNameTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), castNameTextView);
                        castNameTextView.setText(getCastDetailsModel.getName());
                        castNameTextView.setVisibility(View.VISIBLE);

                    }
                    if (castSummaryStr.matches("")) {
                        castDescriptionTextView.setVisibility(View.GONE);
                    } else {
                        FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.light_fonts), castDescriptionTextView);
                        castDescriptionTextView.setText(getCastDetailsModel.getSummary());
                        ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);

                    }

                    videoImageStrToHeight = movieImageStr;
                    Picasso.with(CastCrewDetailsActivity.this).load(videoImageStrToHeight
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

                        }
                    });

                }


            }
        } else {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            if (castNameStr.matches("")) {
                castNameTextView.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.regular_fonts), castNameTextView);
                castNameTextView.setText(castNameStr);
                castNameTextView.setVisibility(View.VISIBLE);

            }
            if (castSummaryStr.matches("")) {
                castDescriptionTextView.setVisibility(View.GONE);
            } else {
                FontUtls.loadFont(CastCrewDetailsActivity.this, getResources().getString(R.string.light_fonts), castDescriptionTextView);
                castDescriptionTextView.setText(castSummaryStr);
                ResizableCustomView.doResizeTextView(CastCrewDetailsActivity.this, castDescriptionTextView, 2, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);
            }

        }
    }


    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            float density = getResources().getDisplayMetrics().density;

            if (videoWidth > videoHeight) {
                if (density >= 3.5 && density <= 4.0) {
                    filmographyAdapter = new FilmographyAdapter(CastCrewDetailsActivity.this, R.layout.nexus_videos_grid_layout_land, filmogrpahyItems);

                } else {
                    filmographyAdapter = new FilmographyAdapter(CastCrewDetailsActivity.this, R.layout.videos_280_grid_layout, filmogrpahyItems);

                }
            } else {
                if (density >= 3.5 && density <= 4.0) {
                    filmographyAdapter = new FilmographyAdapter(CastCrewDetailsActivity.this, R.layout.nexus_videos_grid_layout, filmogrpahyItems);

                } else {
                    filmographyAdapter = new FilmographyAdapter(CastCrewDetailsActivity.this, R.layout.videos_grid_layout, filmogrpahyItems);

                }
            }

            filmographyRecyclerView.setLayoutManager(new LinearLayoutManager(CastCrewDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            //  filmographyRecyclerView.setItemAnimator(new DefaultItemAnimator());
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(CastCrewDetailsActivity.this, R.dimen.recy_margin);
            filmographyRecyclerView.addItemDecoration(itemDecoration);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            filmographyRecyclerView.setAdapter(filmographyAdapter);
        }
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
