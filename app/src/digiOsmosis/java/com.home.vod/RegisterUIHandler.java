package com.home.vod;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.preferences.LanguagePreference.AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS;
import static com.home.vod.preferences.LanguagePreference.TERMS;

/**
 * Created by BISHAL on 21-08-2017. alok
 */

public class RegisterUIHandler {
    private Activity context;
    private TextView termsTextView,termsTextView1;

    public  String selected_Language_Id="", selected_Country_Id="";

    public RegisterUIHandler(Activity context){
        this.context=context;
        termsTextView = (TextView) context.findViewById(R.id.termsTextView);
        termsTextView1 = (TextView) context.findViewById(R.id.termsTextView1);

    }
    public void setCountryList(PreferenceManager preferenceManager){


    }
    public void setTermsTextView(LanguagePreference languagePreference){
        termsTextView1.setText(languagePreference.getTextofLanguage(AGREE_TERMS, DEFAULT_AGREE_TERMS));
        termsTextView.setText(languagePreference.getTextofLanguage(TERMS, DEFAULT_TERMS));


        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://theshilpashetty.muvi.com/page/terms-privacy-policy"));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(browserIntent);
            }
        });
   }





}
