package com.home.vod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.vod.R;

/**
 * Created by MUVI on 10/6/2017.
 */

public class ProgrammeActivity extends AppCompatActivity {

    TextView detailsTextView, videoStoryTextView, benefitsTitleTextView, benefitsStoryTextView;
    ImageView bannerImageView, playButton, favoriteImageView;
    Button startProgramButton, dietPlanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme);
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


}