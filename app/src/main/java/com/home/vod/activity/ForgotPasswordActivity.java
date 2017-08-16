package com.home.vod.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.BTN_SUBMIT;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_SUBMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_DOESNOT_EXISTS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_OOPS_INVALID_EMAIL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORD_RESET_LINK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.EMAIL_DOESNOT_EXISTS;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.OOPS_INVALID_EMAIL;
import static com.home.vod.preferences.LanguagePreference.PASSWORD_RESET_LINK;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.TEXT_EMIAL;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.DEFAULT_IS_ONE_STEP_REGISTRATION;


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
    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    LanguagePreference languagePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION,DEFAULT_IS_ONE_STEP_REGISTRATION)
                .trim()).equals("1")) {
            mActionBarToolbar.setNavigationIcon(null);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
        else
        {
            mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        }

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        logintextView = (TextView) findViewById(R.id.loginTextView);
        submitButton = (Button) findViewById(R.id.submitButton);

        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        submitButton.setTypeface(submitButtonTypeface);

        Typeface editEmailStrTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);

        Typeface logintextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        logintextView.setTypeface(logintextViewTypeface);


        editEmailStr.setHint(languagePreference.getTextofLanguage( TEXT_EMIAL, DEFAULT_TEXT_EMIAL));
        submitButton.setText(languagePreference.getTextofLanguage( BTN_SUBMIT, DEFAULT_BTN_SUBMIT));
        logintextView.setText(languagePreference.getTextofLanguage(LOGIN,DEFAULT_LOGIN));
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
        logintextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailsIntent);
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordButtonClicked();
            }
        });
    }

    public void forgotPasswordButtonClicked() {

        loginEmailStr = editEmailStr.getText().toString().trim();
        if (NetworkStatus.getInstance().isConnected(this)) {
            boolean isValidEmail = Util.isValidMail(loginEmailStr);
            if (isValidEmail == true) {
                Forgotpassword_input forgotpassword_input=new Forgotpassword_input();
                forgotpassword_input.setAuthToken(authTokenStr);
                forgotpassword_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,DEFAULT_SELECTED_LANGUAGE_CODE));
                forgotpassword_input.setEmail(loginEmailStr);
                ForgotpassAsynTask asyncPasswordForgot = new ForgotpassAsynTask(forgotpassword_input,this,this);
                asyncPasswordForgot.executeOnExecutor(threadPoolExecutor);
            } else {

                ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE), languagePreference.getTextofLanguage(OOPS_INVALID_EMAIL,DEFAULT_OOPS_INVALID_EMAIL));

            }
        } else {
            ShowDialog(languagePreference.getTextofLanguage(SORRY,DEFAULT_SORRY), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION,DEFAULT_NO_INTERNET_CONNECTION));

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
                            startActivity(in);
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
        dlgAlert.create().show();
    }
}
