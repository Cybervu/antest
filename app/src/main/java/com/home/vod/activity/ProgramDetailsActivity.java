package com.home.vod.activity;

/**
 * Created by MUVI on 10/10/2017.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.util.Util;

import org.w3c.dom.Text;

public class ProgramDetailsActivity extends AppCompatActivity {

    ImageView bannerImageView, playButton, share;
    TextView detailsTextView, durationTitleTextView, durationTextView, tutorialTextView, viewAllTextView;
    Button startWorkoutButton, dietPlanButton;
    RecyclerView featureContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        detailsTextView = (TextView) findViewById(R.id.titleTextView);
        startWorkoutButton = (Button) findViewById(R.id.startWorkoutButton);
        dietPlanButton = (Button) findViewById(R.id.dietPlanButton);
        durationTitleTextView = (TextView) findViewById(R.id.durationTitleTextView);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        tutorialTextView = (TextView) findViewById(R.id.tutorialTextView);
        viewAllTextView = (TextView) findViewById(R.id.viewAllTextView);
        featureContent = (RecyclerView) findViewById(R.id.featureContent);
        share = (ImageView) findViewById(R.id.share);


        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.shareIt(ProgramDetailsActivity.this);
            }
        });


        dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgramDetailsActivity.this,DietPlanActivity.class);
                startActivity(intent);
            }
        });
    }
}