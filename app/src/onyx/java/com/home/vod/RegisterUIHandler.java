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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.preferences.LanguagePreference.AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GMAIL_SIGNIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GMAIL_SIGNUP;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS;
import static com.home.vod.preferences.LanguagePreference.ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.GMAIL_SIGNIN;
import static com.home.vod.preferences.LanguagePreference.GMAIL_SIGNUP;
import static com.home.vod.preferences.LanguagePreference.NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.TERMS;

/**
 * Created by BISHAL on 21-08-2017.
 */

public class RegisterUIHandler {
    private Activity context;
    private TextView termsTextView,termsTextView1,gmailTest;
    private EditText editName;
    private LinearLayout btnLogin;
    public  String selected_Language_Id="", selected_Country_Id="",regNameStr,regPhone="";
    private Button loginButton;
    private RelativeLayout googleSignView;
    private LanguagePreference languagePreference;

    public RegisterUIHandler(Activity context){
        this.context=context;
        gmailTest=(TextView) context.findViewById(R.id.textView);
        googleSignView = (RelativeLayout) context.findViewById(R.id.sign_in_button);
        termsTextView = (TextView) context.findViewById(R.id.termsTextView);
        termsTextView1 = (TextView) context.findViewById(R.id.termsTextView1);
        editName = (EditText) context.findViewById(R.id.editNameStr);
        btnLogin = (LinearLayout) context.findViewById(R.id.btnLogin);
        btnLogin.setVisibility(View.GONE);
        languagePreference = LanguagePreference.getLanguagePreference(context);
    }
    public void setCountryList(PreferenceManager preferenceManager){


    }
    public void setTermsTextView(LanguagePreference languagePreference, final PreferenceManager preferenceManager){
        termsTextView1.setText(languagePreference.getTextofLanguage(AGREE_TERMS, DEFAULT_AGREE_TERMS));
        termsTextView.setText(languagePreference.getTextofLanguage(TERMS, DEFAULT_TERMS));
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editName);
        editName.setHint(languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_NAME_HINT));

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(preferenceManager.getPrivacy_policy_url()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(browserIntent);
            }
        });
   }

    public void getRegisterName(){
        regNameStr = editName.getText().toString().trim();
        if (!regNameStr.equals("")) {
            ((RegisterActivity) context).registerButtonClicked(regNameStr,regPhone);
        }else {
            Toast.makeText(context, languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();
        }
    }


    public void callFblogin(final CallbackManager callbackManager, Button loginButton, LanguagePreference languagePreference){

    }

    public void callSignin(LanguagePreference languagePreference){
        gmailTest.setText(languagePreference.getTextofLanguage(GMAIL_SIGNUP, DEFAULT_GMAIL_SIGNUP));
        googleSignView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)context).signIn();
            }
        });

    }

}
