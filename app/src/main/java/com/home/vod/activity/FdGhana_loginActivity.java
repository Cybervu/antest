package com.home.vod.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.LoginAsynTask;
import com.home.apisdk.apiController.RegistrationAsynTask;
import com.home.apisdk.apiModel.Login_output;
import com.home.apisdk.apiModel.Registration_output;
import com.home.vod.R;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;


import player.utils.Util;

public class FdGhana_loginActivity extends AppCompatActivity implements LoginAsynTask.LoinDetailsListener,RegistrationAsynTask.RegistrationDetailsListener {

    String regNameStr, regConfirmEmailStr;
    String loginEmailStr = "";
    String loginEmailGet;
    String regEmailStr, regPasswordStr;

    LinearLayout signUpLayout, loginTabLayout, forgotTabLayout;

    TextView forgotpass;

    EditText loginEmail;
    EditText editPhone;
    EditText loginPasswordStr;
    EditText forgotemail;
    EditText loginEmail1Info;
    EditText editEmailStr, editPasswordStr, editConfirmEmail, edituserName;

    boolean navigation = false;

    Button loginTab, SignUpTab;

    ImageButton loginButton;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fd_ghana_login);

        loginTabLayout = (LinearLayout) findViewById(R.id.loginTabLayout);
        forgotTabLayout = (LinearLayout) findViewById(R.id.forgotTabLayout);
        signUpLayout = (LinearLayout) findViewById(R.id.signUpLayout);
        forgotpass = (TextView) findViewById(R.id.forgot_passwd);
        forgotemail = (EditText) findViewById(R.id.editforgotEmailStr);
        loginTabLayout.setVisibility(View.GONE);
        loginEmail = (EditText) findViewById(R.id.editLoginEmailStr);
        loginPasswordStr = (EditText) findViewById(R.id.editLoginPasswordStr);
        editPhone = (EditText) findViewById(R.id.editPhoneStr);
        //editPhone.setVisibility(View.GONE);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        editConfirmEmail = (EditText) findViewById(R.id.editconfirmEmailStr);
        edituserName = (EditText) findViewById(R.id.editUserNameStr);
        editPasswordStr = (EditText) findViewById(R.id.editPasswordStr);
        SignUpTab = (Button) findViewById(R.id.signup);
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        loginTab = (Button) findViewById(R.id.login);

        /*Typeface loginEmaileditEmailStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        loginEmail.setTypeface(loginEmaileditEmailStrTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),loginEmail);
        loginEmail.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));

        forgotemail.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),forgotemail);
        /*Typeface loginEmailStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        forgotemail.setTypeface(loginEmailStrTypeface);*/

        /*Typeface forgotpassStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        forgotpass.setTypeface(forgotpassStrTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),forgotpass);
        SpannableString content = new SpannableString(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.FORGOT_PASSWORD, Util.DEFAULT_FORGOT_PASSWORD));
        content.setSpan(new UnderlineSpan(), 0, Util.getTextofLanguage(FdGhana_loginActivity.this, Util.FORGOT_PASSWORD, Util.DEFAULT_FORGOT_PASSWORD).length(), 0);
        forgotpass.setText(content);

       /* Typeface loginPasswordStrloginEmaileditEmailStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        loginPasswordStr.setTypeface(loginPasswordStrloginEmaileditEmailStrTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),loginPasswordStr);
        loginPasswordStr.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_PASSWORD, Util.DEFAULT_TEXT_PASSWORD));

       /* Typeface editEmailStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),editEmailStr);
        editEmailStr.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));

        /*Typeface editPhoneTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        editPhone.setTypeface(editPhoneTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),editPhone);
        editPhone.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_NUMBER, Util.DEFAULT_TEXT_NUMBER));

        /*Typeface edituserNameTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        edituserName.setTypeface(edituserNameTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),edituserName);
        edituserName.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.NAME_HINT, Util.DEFAULT_NAME_HINT));

        Typeface editConfirmEmailTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        editConfirmEmail.setTypeface(editConfirmEmailTypeface);
        editConfirmEmail.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.CONFIRM_EMAIL, Util.DEFAULT_CONFIRM_EMAIL));

        /*Typeface editPasswordStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        editPasswordStr.setTypeface(editPasswordStrTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),editPasswordStr);
        editPasswordStr.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_PASSWORD, Util.DEFAULT_TEXT_PASSWORD));

       /* Typeface SignUpTabTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        SignUpTab.setTypeface(SignUpTabTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),SignUpTab);
        SignUpTab.setText(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.SIGN_UP_TITLE, Util.DEFAULT_SIGN_UP_TITLE));

       /* Typeface loginTabTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts));
        loginTab.setTypeface(loginTabTypeface);*/
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),loginTab);
        loginTab.setText(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.LOGIN, Util.DEFAULT_LOGIN));

        SignUpTab.setPressed(false);
        loginTab.setPressed(true);
        loginTabLayout.setVisibility(View.VISIBLE);
        signUpLayout.setVisibility(View.GONE);

        loginTab.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                loginTab.setPressed(true);
                SignUpTab.setPressed(false);

                loginTabLayout.setVisibility(View.VISIBLE);
                signUpLayout.setVisibility(View.GONE);
                forgotTabLayout.setVisibility(View.GONE);
                return true;
            }
        });

        SignUpTab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SignUpTab.setPressed(true);
                loginTab.setPressed(false);
                loginTabLayout.setVisibility(View.GONE);
                forgotTabLayout.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.VISIBLE);
                return true;

            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpTab.setPressed(false);
                loginTab.setPressed(false);
                loginTabLayout.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.GONE);
                forgotTabLayout.setVisibility(View.VISIBLE);
                //loginEmail.setVisibility(View.VISIBLE);
                //loginPasswordStr.setVisibility(View.INVISIBLE);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginTab.isPressed() == true) {


                    loginButtonClicked();
                } else if (SignUpTab.isPressed() == true) {
                    registerButtonClicked();
                }else  if (forgotpass.isClickable() == true) {
                    Log.v("SUBHA","SGGS");
                    forgotPasswordButtonClicked();
                }
            }
        });

    }

    public void loginButtonClicked(){

        String regEmailStrInfo = loginEmail.getText().toString().trim();
        regEmailStr = regEmailStrInfo.toLowerCase();
        regPasswordStr = loginPasswordStr.getText().toString().trim();
    }

    public void registerButtonClicked(){

        String regPhoneStr = editPhone.getText().toString().trim();
        regNameStr = edituserName.getText().toString().trim();
        String regEmailStrInfo = editEmailStr.getText().toString();
        regEmailStr = regEmailStrInfo.toLowerCase();
        regPasswordStr = editPasswordStr.getText().toString();
        String regConfirmEmailStrInfo = editConfirmEmail.getText().toString();
        regConfirmEmailStr = regConfirmEmailStrInfo.toLowerCase();

        if (NetworkStatus.getInstance().isConnected(FdGhana_loginActivity.this)){

            if (!regPhoneStr.matches("") && !regNameStr.matches("") && (!regEmailStr.matches("")) &&
                    (!regPasswordStr.matches("")) && !regConfirmEmailStr.equals("")){

                boolean isValidEmail = com.home.vod.util.Util.isValidMail(regEmailStr);
                if (isValidEmail){

                    //if (regPasswordStr.equals(regConfirmPasswordStr)){}
                }
            }

        }
    }

    public void forgotPasswordButtonClicked(){

        String loginEmailStrInfo = forgotemail.getText().toString().trim().toLowerCase();
    }

    @Override
    public void onLoginPreExecuteStarted() {

    }

    @Override
    public void onLoginPostExecuteCompleted(Login_output login_output, int status, String message) {

    }

    @Override
    public void onRegistrationDetailsPreExecuteStarted() {

    }

    @Override
    public void onRegistrationDetailsPostExecuteCompleted(Registration_output registration_output, int status, String message) {

    }
}
