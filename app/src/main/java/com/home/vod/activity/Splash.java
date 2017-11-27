package com.home.vod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home.vod.preferences.PreferenceManager;
import com.home.vod.R;

import java.util.Timer;
import java.util.TimerTask;



public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    PreferenceManager preferenceManager;
    String regid;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        regid = preferenceManager.getSharedPref();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {



                if(!preferenceManager.getSharedPref().trim().equals("0") && !preferenceManager.getSharedPref().trim().equals(""))
                {



                    timer.cancel();
                    Intent i=new Intent(Splash.this,Login.class);
                    startActivity(i);
                    Splash.this.finish();
                }

            }
        },SPLASH_DISPLAY_LENGTH,200);
    }


    @Override
    public void onResume() {
        super.onResume();

//        NotificationUtils.clearNotifications(getApplicationContext());

    }


}

