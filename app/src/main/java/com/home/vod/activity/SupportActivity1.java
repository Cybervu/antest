package com.home.vod.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.home.vod.R;


public class SupportActivity1 extends AppCompatActivity {

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
