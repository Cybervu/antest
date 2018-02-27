package com.home.vod;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.preferences.LanguagePreference.AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS;
import static com.home.vod.preferences.LanguagePreference.ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.TERMS;

/**
 * Created by BISHAL on 21-08-2017.
 */

public class RegisterUIHandler {
    private Activity context;
    private TextView termsTextView,termsTextView1;
    private LinearLayout btnLogin;
    private EditText editName;
    // Kushal
    private EditText editNameFirst, editNameLast, editEmail, editPassword, editConfirmPassword;
    //
    public  String selected_Language_Id="", selected_Country_Id="",regNameStr,regPhone="",last_name="";
    public String regFirstNameStr,regLastNameStr;
    private LanguagePreference languagePreference;

    public RegisterUIHandler(Activity context){
        this.context=context;
        termsTextView = (TextView) context.findViewById(R.id.termsTextView);
        termsTextView1 = (TextView) context.findViewById(R.id.termsTextView1);
        btnLogin = (LinearLayout) context.findViewById(R.id.btnLogin);
        btnLogin.setVisibility(View.GONE);
       // editName = (EditText) context.findViewById(R.id.editNameStr);
        // Kushal
        editNameFirst = (EditText) context.findViewById(R.id.editNameStr_first);
        editNameLast= (EditText)context.findViewById(R.id.editNameStr_last);
        //
        languagePreference = LanguagePreference.getLanguagePreference(context);


        FeatureHandler featureHandler = FeatureHandler.getFeaturePreference(context);
        if(featureHandler.getFeatureStatus(FeatureHandler.FACEBOOK,FeatureHandler.DEFAULT_FACEBOOK)) {
            btnLogin.setVisibility(View.VISIBLE);
        }else {
            btnLogin.setVisibility(View.GONE);
        }

    }
    public void setCountryList(PreferenceManager preferenceManager){


    }
    public void setTermsTextView(LanguagePreference languagePreference){
        termsTextView1.setText(languagePreference.getTextofLanguage(AGREE_TERMS, DEFAULT_AGREE_TERMS));
        termsTextView.setText(languagePreference.getTextofLanguage(TERMS, DEFAULT_TERMS));
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editNameFirst);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editNameLast);
        editNameFirst.setHint(languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_FIRST_NAME));
        editNameLast.setHint(languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_LAST_NAME));

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.plusnights.co.uk/privacy-policy"));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(browserIntent);
            }
        });
   }
    public void getRegisterName(){
       // regNameStr = editName.getText().toString().trim();
        //Kushal
        regFirstNameStr= editNameFirst.getText().toString().trim();
        regLastNameStr= editNameLast.getText().toString().trim();
        //
        if (!regFirstNameStr.equals("") && !regLastNameStr.equals("") || !regLastNameStr.isEmpty() && !regFirstNameStr.isEmpty()) {
            ((RegisterActivity) context).registerButtonClicked(regFirstNameStr,regLastNameStr,regPhone);
        }else {
            Toast.makeText(context, languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();
        }
    }
    public void callFblogin(final CallbackManager callbackManager, Button loginButton, LanguagePreference languagePreference){

    }

    public void callSignin(LanguagePreference languagePreference){

    }



}
