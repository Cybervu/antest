package com.home.api.player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home.apisdk.R;


public class SupportActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        finish();
//
//        EMVideoView emVideoView =(EMVideoView)findViewById(R.id.emVideoView);
//        emVideoView.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
//        emVideoView.start();
    }
}
