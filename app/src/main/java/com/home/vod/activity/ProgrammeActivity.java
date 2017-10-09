package com.home.vod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.vod.R;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.ResizableCustomView;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import static com.home.vod.preferences.LanguagePreference.CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CAST_CREW_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SEASON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.SEASON;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static player.utils.Util.DEFAULT_HAS_FAVORITE;
import static player.utils.Util.HAS_FAVORITE;

/**
 * Created by MUVI on 10/6/2017.
 */

public class ProgrammeActivity extends AppCompatActivity implements GetContentDetailsAsynTask.GetContentDetailsListener {

    TextView detailsTextView, videoStoryTextView, benefitsTitleTextView, benefitsStoryTextView;
    ImageView bannerImageView, playButton, favoriteImageView;
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
    String movieReleaseDateStr = "";
    static String _permalink;
    LanguagePreference languagePreference;

    int isFreeContent = 0, isPPV, isConverted, contentTypesId, isAPV;
    String movieStreamUniqueId, bannerImageId, posterImageId, permalinkStr;
    boolean castStr = false;
    int isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme);
        languagePreference = LanguagePreference.getLanguagePreference(ProgrammeActivity.this);
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        favoriteImageView = (ImageView) findViewById(R.id.favoriteImageView);
        detailsTextView = (TextView) findViewById(R.id.detailsTextView);
        videoStoryTextView = (TextView) findViewById(R.id.videoStoryTextView);
        benefitsTitleTextView = (TextView) findViewById(R.id.benefitsTitleTextView);
        benefitsStoryTextView = (TextView) findViewById(R.id.benefitsStoryTextView);
        startProgramButton = (Button) findViewById(R.id.startProgramButton);
        dietPlanButton = (Button) findViewById(R.id.dietPlanButton);


        startProgramButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), SeasonActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public void onGetContentDetailsPreExecuteStarted() {

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

            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);
            castStr = contentDetailsOutput.getCastStr();
            isFreeContent = Integer.parseInt(contentDetailsOutput.getIsFreeContent());
            movieUniqueId = contentDetailsOutput.getMuviUniqId();
            isEpisode = contentDetailsOutput.getIsEpisode();
            movieStreamUniqueId = contentDetailsOutput.getMovieStreamUniqId();
            movieNameStr = contentDetailsOutput.getName();
            movieTrailerUrlStr = contentDetailsOutput.getTrailerUrl();
            videoduration = contentDetailsOutput.getVideoDuration();
            story=contentDetailsOutput.getStory();
            movieTypeStr = contentDetailsOutput.getGenre();
            isFavorite = contentDetailsOutput.getIs_favorite();
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
                favoriteImageView.setVisibility(View.VISIBLE);
            } else {
                favoriteImageView.setVisibility(View.GONE);
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

            }

            if (movieDetailsStr.matches("") || movieDetailsStr.matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoStoryTextView.setVisibility(View.GONE);

            } else {
                //  videoStoryTextView.setMaxLines(3);
                videoStoryTextView.setVisibility(View.VISIBLE);

                FontUtls.loadFont(ProgrammeActivity.this, getResources().getString(R.string.light_fonts), videoStoryTextView);

            }



    }
}