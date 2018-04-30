package com.home.vod.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;

import static com.home.vod.preferences.LanguagePreference.AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LANDING_TEXT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_START_BROWSING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS_CONDITION;
import static com.home.vod.preferences.LanguagePreference.LANDING_TEXT;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.START_BROWSING;
import static com.home.vod.preferences.LanguagePreference.TERMS;
import static com.home.vod.preferences.LanguagePreference.TERMS_CONDITION;

public class PickboxLandingActivity extends AppCompatActivity {

    Button loginButton;
    TextView registerText, skipText;
    TextView landingText,termsAndCondition , termsAndCondition1, termsAndCondition2;
    LinearLayout registerButtonLayout, skipButtonLayout;
    LanguagePreference languagePreference;
    ImageView backgroundImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        initViews();
        setBackgroundTabMobile();
        setFont();
        setLanguage();

        registerButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCountrySelectActivity("register");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCountrySelectActivity("login");
            }
        });

        skipButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCountrySelectActivity("skip");
            }
        });

        termsAndCondition2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.pickbox_url_tc)));
                startActivity(browser);
            }

        });
    }

    private void setBackgroundTabMobile() {
        if(isTablet(getApplicationContext())){
            backgroundImage.setImageDrawable(getResources().getDrawable(R.drawable.landing_background_tab));
        }else{
            backgroundImage.setImageDrawable(getResources().getDrawable(R.drawable.landing_background_mobile));
        }
    }


    private void intentToCountrySelectActivity(String type) {
        Intent i= new Intent(PickboxLandingActivity.this, PickboxChooseCountry.class);
        i.putExtra("type",type);
        startActivity(i);
        //finish();
    }

    private void initViews() {
        languagePreference = LanguagePreference.getLanguagePreference((this));
        loginButton=(Button) findViewById(R.id.loginButton);
        registerText=(TextView) findViewById(R.id.registerTextView);
        skipText=(TextView) findViewById(R.id.skipTextView);
        registerButtonLayout=(LinearLayout) findViewById(R.id.registerButtonLayout);
        skipButtonLayout=(LinearLayout) findViewById(R.id.skipButtonLayout);
        landingText= (TextView)findViewById(R.id.text);
        termsAndCondition= (TextView) findViewById(R.id.termsAndCondition);
        termsAndCondition1= (TextView) findViewById(R.id.termsAndCondition1);
        termsAndCondition2= (TextView) findViewById(R.id.termsAndCondition2);
        backgroundImage= (ImageView)findViewById(R.id.backgroundImagePickbox);
    }

    private void setFont() {
        FontUtls.loadFont(PickboxLandingActivity.this, getResources().getString(R.string.pickbox_bold_fonts), loginButton);
        FontUtls.loadFont(PickboxLandingActivity.this, getResources().getString(R.string.pickbox_light_fonts), skipText);
        FontUtls.loadFont(PickboxLandingActivity.this, getResources().getString(R.string.pickbox_light_fonts), registerText);
    }

    private void setLanguage() {
        landingText.setText(languagePreference.getTextofLanguage(LANDING_TEXT,DEFAULT_LANDING_TEXT));
        loginButton.setText(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN));
        registerText.setText(languagePreference.getTextofLanguage(BTN_REGISTER , DEFAULT_LOGIN));
        skipText.setText(languagePreference.getTextofLanguage(START_BROWSING, DEFAULT_START_BROWSING));
        termsAndCondition.setText(languagePreference.getTextofLanguage(TERMS_CONDITION, DEFAULT_TERMS_CONDITION));
        termsAndCondition1.setText(languagePreference.getTextofLanguage(TERMS, DEFAULT_TERMS));


    }

    /*
    Check for tablet or mobile
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
