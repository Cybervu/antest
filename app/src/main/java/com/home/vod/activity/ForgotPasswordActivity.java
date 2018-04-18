package com.home.vod.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.apisdk.apiController.ForgotpassAsynTask;
import com.home.apisdk.apiModel.Forgotpassword_input;
import com.home.apisdk.apiModel.Forgotpassword_output;
import com.home.vod.R;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.FontUtls;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import player.activity.Player;

import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.BUTTON_RESET;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_RESET;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_DOESNOT_EXISTS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FORGOT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FORGOT_PASSWORD_CONTENT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORD_RESET_LINK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_RESET_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.EMAIL_DOESNOT_EXISTS;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.FORGOT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.FORGOT_PASSWORD_CONTENT;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PASSWORD_RESET_LINK;
import static com.home.vod.preferences.LanguagePreference.RESET_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.TEXT_EMIAL;
import static com.home.vod.util.Constant.authTokenStr;


public class ForgotPasswordActivity extends AppCompatActivity implements ForgotpassAsynTask.ForgotpassDetailsListener{
    Toolbar mActionBarToolbar;
    ImageView logoImageView;
    EditText editEmailStr;
    TextView logintextView;
    Button submitButton;
    ProgressBarHandler pDialog;
    String loginEmailStr = "";
    boolean navigation=false;
    ForgotpassAsynTask asyncPasswordForgot;
    Player playerModel;
    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    LanguagePreference languagePreference;
    FeatureHandler featureHandler;

    // Kushal
    TextView heading, sampleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setBackgroundDrawableResource(R.drawable.app_background);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        featureHandler = FeatureHandler.getFeaturePreference(this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(languagePreference.getTextofLanguage(FORGOT_PASSWORD,DEFAULT_FORGOT_PASSWORD));
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitleColor));
        setSupportActionBar(mActionBarToolbar);
        playerModel = (Player) getIntent().getSerializableExtra("PlayerModel");

        // Kushal
        heading= (TextView)findViewById(R.id.headingText);
        sampleText=(TextView)findViewById(R.id.forgotPasswordText);
        heading.setText(languagePreference.getTextofLanguage(RESET_PASSWORD,DEFAULT_RESET_PASSWORD));
        sampleText.setText(languagePreference.getTextofLanguage(FORGOT_PASSWORD_CONTENT,DEFAULT_FORGOT_PASSWORD_CONTENT));
/*
        if ((featureHandler.getFeatureStatus(FeatureHandler.SIGNUP_STEP, FeatureHandler.DEFAULT_SIGNUP_STEP))){
            mActionBarToolbar.setNavigationIcon(null);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
        else
        {
        }*/

        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));


        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       // logoImageView = (ImageView) findViewById(R.id.logoImageView);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        logintextView = (TextView) findViewById(R.id.loginTextView);
        submitButton = (Button) findViewById(R.id.submitButton);
      //  FontUtls.loadFont(ForgotPasswordActivity.this, getResources().getString(R.string.regular_fonts),submitButton);
        FontUtls.loadFont(ForgotPasswordActivity.this, getResources().getString(R.string.pickbox_bold_fonts),submitButton);
        FontUtls.loadFont(ForgotPasswordActivity.this, getResources().getString(R.string.light_fonts),editEmailStr);
      //  FontUtls.loadFont(ForgotPasswordActivity.this, getResources().getString(R.string.light_fonts),logintextView);
        FontUtls.loadFont(ForgotPasswordActivity.this, getResources().getString(R.string.pickbox_light_fonts),logintextView);

        editEmailStr.setHint(languagePreference.getTextofLanguage( TEXT_EMIAL, DEFAULT_TEXT_EMIAL));
       // submitButton.setText(languagePreference.getTextofLanguage( BTN_SUBMIT, DEFAULT_BTN_SUBMIT));
        submitButton.setText(languagePreference.getTextofLanguage( BUTTON_RESET, DEFAULT_BUTTON_RESET));
    //    logintextView.setText(languagePreference.getTextofLanguage(LOGIN,DEFAULT_LOGIN));
       /* editEmailStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    editEmailStr.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#458ccc")));
                } else {
                    editEmailStr.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#737373")));
                }

            }
        });*/

        /*******enter key of keyboard *************/
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (source.charAt(i) == '\n') {
                        return " ";
                    }
                }
                return null;
            }
        };
        editEmailStr.setFilters(new InputFilter[]{filter});
        logintextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                detailsIntent.putExtra("PlayerModel", playerModel);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(detailsIntent);
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                forgotPasswordButtonClicked();
            }
        });
    }



    public void forgotPasswordButtonClicked() {

        loginEmailStr = editEmailStr.getText().toString().trim();
            boolean isValidEmail = Util.isValidMail(loginEmailStr);

        if(!loginEmailStr.equals("")){

            if (isValidEmail == true) {
                if (NetworkStatus.getInstance().isConnected(this)) {

                    Forgotpassword_input forgotpassword_input = new Forgotpassword_input();
                    forgotpassword_input.setAuthToken(authTokenStr);
                    forgotpassword_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    forgotpassword_input.setEmail(loginEmailStr);
                    ForgotpassAsynTask asyncPasswordForgot = new ForgotpassAsynTask(forgotpassword_input, this, this);
                    asyncPasswordForgot.executeOnExecutor(threadPoolExecutor);
                }
                else {
                    ShowDialog(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION,DEFAULT_NO_INTERNET_CONNECTION));

                }
            } else {

                ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE), languagePreference.getTextofLanguage(EMAIL_DOESNOT_EXISTS,DEFAULT_EMAIL_DOESNOT_EXISTS));
            }
        }else{
            ShowDialog(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY), languagePreference.getTextofLanguage(TEXT_EMIAL,DEFAULT_TEXT_EMIAL));
        }

    }

    @Override
    public void onForgotpassDetailsPreExecuteStarted() {

        pDialog = new ProgressBarHandler(ForgotPasswordActivity.this);
        pDialog.show();
    }

    @Override
    public void onForgotpassDetailsPostExecuteCompleted(Forgotpassword_output forgotpassword_output, int status, String message) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {
            ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE),
                    languagePreference.getTextofLanguage(EMAIL_DOESNOT_EXISTS,DEFAULT_EMAIL_DOESNOT_EXISTS));

        }

        if (status > 0) {
            if (status == 200) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                navigation=true;
                ShowDialog("",languagePreference.getTextofLanguage(PASSWORD_RESET_LINK,DEFAULT_PASSWORD_RESET_LINK));

            } else {
                ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE),
                        languagePreference.getTextofLanguage(EMAIL_DOESNOT_EXISTS,DEFAULT_EMAIL_DOESNOT_EXISTS));
            }
        }else {
            ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE),
                    languagePreference.getTextofLanguage(EMAIL_DOESNOT_EXISTS,DEFAULT_EMAIL_DOESNOT_EXISTS));
        }

    }


    @Override
    public void onBackPressed() {
        if (asyncPasswordForgot!=null){
            asyncPasswordForgot.cancel(true);
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        final Intent detailsIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        detailsIntent.putExtra("PlayerModel", playerModel);
        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(detailsIntent);
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();

    }

    public void ShowDialog(String Title, String msg)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ForgotPasswordActivity.this, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (navigation) {
                            Intent in = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            in.putExtra("PlayerModel", playerModel);
                            in.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(in);
                            finish();
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
        dlgAlert.create().show();
    }
}
