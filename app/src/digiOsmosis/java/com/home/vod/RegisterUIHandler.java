package com.home.vod;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.R.id.loginWithFacebookButton;
import static com.home.vod.preferences.LanguagePreference.AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GMAIL_SIGNUP;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN_FACEBOOK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_REGISTER_FACEBOOK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_UP_WITH_EMAIL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS;
import static com.home.vod.preferences.LanguagePreference.GMAIL_SIGNUP;
import static com.home.vod.preferences.LanguagePreference.LOGIN_FACEBOOK;
import static com.home.vod.preferences.LanguagePreference.REGISTER_FACEBOOK;
import static com.home.vod.preferences.LanguagePreference.SIGN_UP_WITH_EMAIL;
import static com.home.vod.preferences.LanguagePreference.TERMS;
import static player.utils.Util.DEFAULT_DETAILS_NOT_FOUND_ALERT;
import static player.utils.Util.DETAILS_NOT_FOUND_ALERT;

/**
 * Created by BISHAL on 21-08-2017.
 */

public class RegisterUIHandler {
    private Activity context;
    private TextView termsTextView,termsTextView1,gmailTest;
    private Button loginButton;
    private LinearLayout btnLogin;
    LoginButton loginWithFacebookButton;
    private LinearLayout googleSignView;
    private LanguagePreference languagePreference;
    public  String selected_Language_Id="", selected_Country_Id="";
    private Button registerButton;
    RelativeLayout  registerPage1,registerPage2;
    Button regtisterEmailButton;
    TextView fbLoginTextView;

    String fbUserId = "";
    String fbEmail = "";
    String fbName = "";

    public RegisterUIHandler(Activity context,LanguagePreference languagePreference){
        this.context=context;
        gmailTest=(TextView) context.findViewById(R.id.textView);
        googleSignView = (LinearLayout) context.findViewById(R.id.sign_in_button);
//        termsTextView = (TextView) context.findViewById(R.id.termsTextView);
//        termsTextView1 = (TextView) context.findViewById(R.id.termsTextView1);
        btnLogin = (LinearLayout) context.findViewById(R.id.btnLogin);
        loginWithFacebookButton = (LoginButton) context.findViewById(R.id.loginWithFacebookButton);
        loginWithFacebookButton.setVisibility(View.GONE);
        fbLoginTextView = (TextView) context.findViewById(R.id.fbLoginTextView);
        registerPage1 = (RelativeLayout) context.findViewById(R.id.registerPage1);
        registerPage2 = (RelativeLayout) context.findViewById(R.id.registerPage2);
        regtisterEmailButton = (Button) context.findViewById(R.id.regtisterEmailButton);


        loginWithFacebookButton.setReadPermissions("public_profile", "email", "user_friends");




    }
    public void setCountryList(PreferenceManager preferenceManager){


    }
   /* public void setTermsTextView(LanguagePreference languagePreference){
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
   }*/


   public void callEmailSignIn(LanguagePreference languagePreference) {


       regtisterEmailButton.setText(languagePreference.getTextofLanguage(SIGN_UP_WITH_EMAIL,DEFAULT_SIGN_UP_WITH_EMAIL));
       registerPage2.setVisibility(View.GONE);
       registerPage1.setVisibility(View.VISIBLE);


       regtisterEmailButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               registerPage1.setVisibility(View.GONE);
               registerPage2.setVisibility(View.VISIBLE);
           }
       });
   }

    public void callFblogin(final CallbackManager callbackManager,Button registerButton,LanguagePreference languagePreference){

        this.registerButton=registerButton;
        this.languagePreference=languagePreference;
        fbLoginTextView.setText(languagePreference.getTextofLanguage(REGISTER_FACEBOOK,DEFAULT_REGISTER_FACEBOOK));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                loginWithFacebookButton.registerCallback(callbackManager, mCallBack);

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            //progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {


                            JSONObject json = response.getJSONObject();
                            try {
                                if (json != null) {


                                    if ((json.has("name")) && json.getString("name").trim() != null && !json.getString("name").trim().isEmpty() && !json.getString("name").trim().equals("null") && !json.getString("name").trim().matches("")) {

                                        fbName = json.getString("name");

                                    }
                                    if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                        fbEmail = json.getString("email");
                                    } else {
                                        fbEmail = fbName + "@facebook.com";

                                    }
                                    if ((json.has("id")) && json.optString("id").trim() != null && !json.optString("id").trim().isEmpty() && !json.optString("id").trim().equals("null") && !json.optString("id").trim().matches("")) {
                                        fbUserId = json.optString("id");
                                    }
                                    registerButton.setVisibility(View.GONE);
                                    loginWithFacebookButton.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.GONE);
                                    ((RegisterActivity)context).handleFbUserDetails(fbUserId,fbEmail,fbName);
//
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

            registerButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(context, languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {

            registerButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(context, languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();

            //progressDialog.dismiss();
        }
    };


    public void callSignin(LanguagePreference languagePreference){
        gmailTest.setText(languagePreference.getTextofLanguage(GMAIL_SIGNUP, DEFAULT_GMAIL_SIGNUP));
        googleSignView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((RegisterActivity)context).signIn();
            }
        });
    }

   /* public void setEmailText(LanguagePreference languagePreference){
        gmailTest.setText(languagePreference.getTextofLanguage(GMAIL_SIGNUP, DEFAULT_GMAIL_SIGNUP));

    }*/

   public void sendBroadCast()
   {
       Intent Sintent = new Intent("LOGIN_SUCCESS");
       Sintent.putExtra("login_status","1");
       LocalBroadcastManager.getInstance((context)).sendBroadcast(Sintent);
   }

}
