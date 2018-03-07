package com.home.vod.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.CheckDeviceAsyncTask;
import com.home.apisdk.apiController.ForgotpassAsynTask;
import com.home.apisdk.apiController.GetSimultaneousLogoutAsync;
import com.home.apisdk.apiController.LoginAsynTask;
import com.home.apisdk.apiController.RegistrationAsynTask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.CheckDeviceInput;
import com.home.apisdk.apiModel.CheckDeviceOutput;
import com.home.apisdk.apiModel.Forgotpassword_input;
import com.home.apisdk.apiModel.Forgotpassword_output;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Login_input;
import com.home.apisdk.apiModel.Login_output;
import com.home.apisdk.apiModel.Registration_input;
import com.home.apisdk.apiModel.Registration_output;
import com.home.apisdk.apiModel.SimultaneousLogoutInput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.vod.R;
import com.home.vod.RegisterUIHandler;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import player.activity.Player;
import player.utils.Util;

import static com.home.vod.preferences.LanguagePreference.ANDROID_VERSION;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CANCEL_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEAFULT_CANCEL_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ANDROID_VERSION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_DOESNOT_EXISTS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_EXISTS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_PASSWORD_INVALID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ERROR_IN_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GOOGLE_FCM_TOKEN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_OOPS_INVALID_EMAIL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORD_RESET_LINK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TRY_AGAIN;
import static com.home.vod.preferences.LanguagePreference.DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.EMAIL_DOESNOT_EXISTS;
import static com.home.vod.preferences.LanguagePreference.EMAIL_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.EMAIL_EXISTS;
import static com.home.vod.preferences.LanguagePreference.EMAIL_PASSWORD_INVALID;
import static com.home.vod.preferences.LanguagePreference.ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.ERROR_IN_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.GOOGLE_FCM_TOKEN;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.OOPS_INVALID_EMAIL;
import static com.home.vod.preferences.LanguagePreference.PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.PASSWORD_RESET_LINK;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.TRY_AGAIN;
import static com.home.vod.util.Constant.authTokenStr;

public class FdGhana_loginActivity extends AppCompatActivity implements LoginAsynTask.LoinDetailsListener,
        RegistrationAsynTask.RegistrationDetailsListener,
        CheckDeviceAsyncTask.CheckDeviceListener,
        VideoDetailsAsynctask.VideoDetailsListener,
        GetSimultaneousLogoutAsync.SimultaneousLogoutAsyncListener,
        ForgotpassAsynTask.ForgotpassDetailsListener

{

    String regNameStr, regConfirmEmailStr;
    String loginEmailStr = "";
    String loginEmailGet;
    String regEmailStr, regPasswordStr;
    String planId = "";

    LinearLayout signUpLayout, loginTabLayout, forgotTabLayout;

    TextView forgotpass;
    TextView logout_text;

    EditText loginEmail;
    EditText editPhone;
    EditText editPassword;
    EditText forgotemail;
    EditText loginEmail1Info;
    EditText editEmailStr, editPasswordStr, editConfirmEmail, edituserName;

    boolean navigation = false;

    Button loginTab, SignUpTab;
    Button ok, cancel;

    ImageButton loginButton;

    PreferenceManager preferenceManager;

    RegistrationAsynTask asyncReg;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    String registrationIdStr;
    String isSubscribedStr = "";
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    RegisterUIHandler registerUIHandler;
    LanguagePreference languagePreference;

    ProgressBarHandler pDialog;
    ProgressDialog progressDialog1;

    String deviceName = "";
    VideoDetailsAsynctask asynLoadVideoUrls;

    AlertDialog logout_alert;

    Player playerModel;

    Toolbar mActionBarToolbar;

    // Kushal ****
        Boolean loginPressed=true, registerPresed= false;
        // End ***


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
        editPassword = (EditText) findViewById(R.id.editLoginPasswordStr);
        editPhone = (EditText) findViewById(R.id.editPhoneStr);
        //editPhone.setVisibility(View.GONE);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        editConfirmEmail = (EditText) findViewById(R.id.editconfirmEmailStr);
        edituserName = (EditText) findViewById(R.id.editUserNameStr);
        editPasswordStr = (EditText) findViewById(R.id.editPasswordStr);
        SignUpTab = (Button) findViewById(R.id.signup);
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        loginTab = (Button) findViewById(R.id.login);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setTitle("");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

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
        FontUtls.loadFont(this,getResources().getString(R.string.fonts),editPassword);
        editPassword.setHint(Util.getTextofLanguage(FdGhana_loginActivity.this, Util.TEXT_PASSWORD, Util.DEFAULT_TEXT_PASSWORD));

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
                // Kushal ***
                loginPressed=true;
                registerPresed = false;
                // End ***
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
                // Kushal ***
                loginPressed=true;
                registerPresed = false;
                //End ***
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

        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

        languagePreference = LanguagePreference.getLanguagePreference(FdGhana_loginActivity.this);
        preferenceManager = PreferenceManager.getPreferenceManager(this);

        planId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();
        playerModel = (Player) getIntent().getSerializableExtra("PlayerModel");

    }

    public void loginButtonClicked(){

        String regEmailStrInfo = loginEmail.getText().toString().trim();
        regEmailStr = regEmailStrInfo.toLowerCase();
        regPasswordStr = editPassword.getText().toString().trim();

        if (NetworkStatus.getInstance().isConnected(this)){
            if ((!regEmailStr.equals("")) && (!regPasswordStr.equals(""))){
                boolean isValidEmail = com.home.vod.util.Util.isValidMail(regEmailStr);
                if (isValidEmail == true){
                    LogUtil.showLog("MUVI", "activity_login valid");
                    Login_input login_input = new Login_input();
                    login_input.setAuthToken(authTokenStr);
                    login_input.setEmail(regEmailStr);
                    login_input.setPassword(regPasswordStr);
                    login_input.setDevice_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    login_input.setGoogle_id(languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));
                    login_input.setDevice_type("1");
                    login_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    LoginAsynTask asyncReg = new LoginAsynTask(login_input, this, this);
                    asyncReg.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(OOPS_INVALID_EMAIL, DEFAULT_OOPS_INVALID_EMAIL), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }


    }

    public void registerButtonClicked(){

        String regPhoneStr = editPhone.getText().toString().trim();
        regNameStr = edituserName.getText().toString().trim();
        String regEmailStrInfo = editEmailStr.getText().toString().trim();
        regEmailStr = regEmailStrInfo.toLowerCase();
        regPasswordStr = editPasswordStr.getText().toString().trim();
        String regConfirmEmailStrInfo = editConfirmEmail.getText().toString().trim();
        regConfirmEmailStr = regConfirmEmailStrInfo.toLowerCase();

        if (NetworkStatus.getInstance().isConnected(FdGhana_loginActivity.this)){

            if (!regPhoneStr.matches("") && !regNameStr.matches("") && (!regEmailStr.matches("")) &&
                    (!regPasswordStr.matches("")) && !regConfirmEmailStr.equals("")){

                boolean isValidEmail = com.home.vod.util.Util.isValidMail(regEmailStr);
                if (isValidEmail){

                    if (regEmailStr.equals(regConfirmEmailStr)){

                        Registration_input registration_input = new Registration_input();
                        registration_input.setAuthToken(authTokenStr);
                        registration_input.setName(regNameStr);
                        registration_input.setCustom_last_name("");
                        registration_input.setEmail(regEmailStr);
                        registration_input.setPhone(regPhoneStr);
                        registration_input.setPassword(regPasswordStr);
//                        registration_input.setCustom_country(registerUIHandler.selected_Country_Id);
//                        registration_input.setCustom_languages(registerUIHandler.selected_Language_Id);
                        registration_input.setDevice_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        registration_input.setGoogle_id(languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));
                        registration_input.setDevice_type("1");
                        registration_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

                        asyncReg = new RegistrationAsynTask(registration_input, this, this);
                        asyncReg.executeOnExecutor(threadPoolExecutor);
                    } else {
                        Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(EMAIL_DO_NOT_MATCH, DEFAULT_EMAIL_DO_NOT_MATCH), Toast.LENGTH_LONG).show();
                    }
                    //if (regPasswordStr.equals(regConfirmPasswordStr)){}
                } else {
                    Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(OOPS_INVALID_EMAIL, DEFAULT_OOPS_INVALID_EMAIL), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();

            }

        }else {
            Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }

    public void forgotPasswordButtonClicked(){

        String loginEmailStrInfo = forgotemail.getText().toString().trim().toLowerCase();

        if (NetworkStatus.getInstance().isConnected(this)){
            boolean isValidEmail = com.home.vod.util.Util.isValidMail(loginEmailStrInfo);
            if (isValidEmail == true){
                Forgotpassword_input forgotpassword_input=new Forgotpassword_input();
                forgotpassword_input.setAuthToken(authTokenStr);
                forgotpassword_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,DEFAULT_SELECTED_LANGUAGE_CODE));
                forgotpassword_input.setEmail(loginEmailStrInfo);
                ForgotpassAsynTask asyncPasswordForgot = new ForgotpassAsynTask(forgotpassword_input,this,this);
                asyncPasswordForgot.executeOnExecutor(threadPoolExecutor);
            }else {
                ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE),
                        languagePreference.getTextofLanguage(OOPS_INVALID_EMAIL,DEFAULT_OOPS_INVALID_EMAIL));

            }

        }


    }

    // Kushal ***

    private void ShowDialog(String Title, String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (navigation) {
                            /*Intent in = new Intent(FdGhana_loginActivity.this, LoginActivity.class);
                            in.putExtra("PlayerModel", playerModel);
                            in.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(in);
                            finish();*/
                            retainSelectedTab();
                            dialog.cancel();
                        } else {
                            retainSelectedTab();
                            dialog.cancel();
                        }

                    }
                });
        dlgAlert.create().show();
    }

    private void retainSelectedTab() {
        if (loginPressed){
            loginTab.setPressed(true);
            SignUpTab.setPressed(false);

            loginTabLayout.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.GONE);
            forgotTabLayout.setVisibility(View.GONE);
        }else if(registerPresed){
            SignUpTab.setPressed(true);
            loginTab.setPressed(false);
            loginTabLayout.setVisibility(View.GONE);
            forgotTabLayout.setVisibility(View.GONE);
            signUpLayout.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Nothing", Toast.LENGTH_SHORT).show();
        }
    }

    // End ***
    @Override
    public void onLoginPreExecuteStarted() {
        pDialog = new ProgressBarHandler(FdGhana_loginActivity.this);
        pDialog.show();
    }

    @Override
    public void onLoginPostExecuteCompleted(Login_output login_output, int status, String message) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        if (status > 0) {

            // SharedPreferences.Editor editor = pref.edit();

            if (status == 200) {
                //playerModel.setEmailId(login_output.getEmail());

                preferenceManager.setLogInStatusToPref("1");
                preferenceManager.setUserIdToPref(login_output.getId());
                preferenceManager.setPwdToPref("");
                preferenceManager.setEmailIdToPref(login_output.getEmail());
                preferenceManager.setDispNameToPref(login_output.getDisplay_name());
                preferenceManager.setLoginProfImgoPref(login_output.getProfile_image());
                preferenceManager.setIsSubscribedToPref(login_output.getIsSubscribed());
                preferenceManager.setLoginHistIdPref(login_output.getLogin_history_id());


                if (NetworkStatus.getInstance().isConnected(this)) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    LogUtil.showLog("MUVI", "isRestrictDevice called===" + (languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE)));

                    if ((languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE)).trim().equals("1")) {

                        LogUtil.showLog("MUVI", "isRestrictDevice called");
                        // Call For Check Api.
                        CheckDeviceInput checkDeviceInput = new CheckDeviceInput();
                        if (preferenceManager != null) {
                            String userIdStr = preferenceManager.getUseridFromPref();
                            checkDeviceInput.setUser_id(userIdStr.trim());
                        }
                        checkDeviceInput.setDevice(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        checkDeviceInput.setGoogle_id(languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));
                        checkDeviceInput.setAuthToken(authTokenStr);
                        checkDeviceInput.setUser_id(preferenceManager.getUseridFromPref());
                        checkDeviceInput.setDevice_type("1");
                        checkDeviceInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        checkDeviceInput.setDevice_info(deviceName + "," + languagePreference.getTextofLanguage(ANDROID_VERSION, DEFAULT_ANDROID_VERSION) + " " + Build.VERSION.RELEASE);
                        CheckDeviceAsyncTask asynCheckDevice = new CheckDeviceAsyncTask(checkDeviceInput, this, this);
                        asynCheckDevice.executeOnExecutor(threadPoolExecutor);
                    } else {

                        if (getIntent().getStringExtra("from") != null) {
                            /** review **/
                            onBackPressed();
                        } else {
                            if (com.home.vod.util.Util.check_for_subscription == 1) {
                                //go to subscription page
                                if (NetworkStatus.getInstance().isConnected(this)) {

                                    setResultAtFinishActivity();


                                } else {
                                    Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                                }

                            } else {
                                if (planId.equals("1") && login_output.getIsSubscribed().equals("0")) {
                                    Intent intent = new Intent(FdGhana_loginActivity.this, SubscriptionActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    Intent in = new Intent(FdGhana_loginActivity.this, MainActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                    finish();
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }
            } else if (status == 300) {
                // Show Popup For the Simultaneous Logout

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;

                }
                show_logout_popup(login_output.getMsg());
            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(EMAIL_PASSWORD_INVALID, DEFAULT_EMAIL_PASSWORD_INVALID));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Kushal ***
                                retainSelectedTab();
                                // End ***
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }
        } else {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {

            }

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Kushal ***
                            retainSelectedTab();
                            // End ***
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }
    }

    @Override
    public void onRegistrationDetailsPreExecuteStarted() {

        pDialog = new ProgressBarHandler(FdGhana_loginActivity.this);
        pDialog.show();
    }

    @Override
    public void onRegistrationDetailsPostExecuteCompleted(Registration_output registration_output, int status, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {
            status = 0;

        }

        if (status == 0) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(ERROR_IN_REGISTRATION, DEFAULT_ERROR_IN_REGISTRATION));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Kushal ***
                            retainSelectedTab();
                            // End ***
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }
        if (status > 0) {

            if (status == 422) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(EMAIL_EXISTS, DEFAULT_EMAIL_EXISTS));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Kushal ***
                                retainSelectedTab();
                                // End ***
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();

            } else if (status == 200) {

                // Take appropiate step here
                // playerModel.setEmailId(registration_output.getEmail());

                isSubscribedStr = registration_output.getIsSubscribed();
                preferenceManager.setLogInStatusToPref("1");
                preferenceManager.setUserIdToPref(registration_output.getId());
                preferenceManager.setPwdToPref(editPassword.getText().toString().trim());
                preferenceManager.setEmailIdToPref(registration_output.getEmail());
                preferenceManager.setDispNameToPref(registration_output.getDisplay_name());
                preferenceManager.setLoginProfImgoPref(registration_output.getProfile_image());
                preferenceManager.setIsSubscribedToPref(isSubscribedStr);
                preferenceManager.setLoginHistIdPref(registration_output.getLogin_history_id());

                Date todayDate = new Date();
                String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
                preferenceManager.setLoginDatePref(todayStr);

                if (languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1")) {
                    // Call For Check Api.
                    CheckDeviceInput checkDeviceInput = new CheckDeviceInput();
                    checkDeviceInput.setAuthToken(authTokenStr);
                    String userIdStr = preferenceManager.getUseridFromPref();
                    checkDeviceInput.setUser_id(userIdStr);
                    checkDeviceInput.setDevice(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    checkDeviceInput.setGoogle_id(languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));
                    checkDeviceInput.setDevice_type("1");
                    checkDeviceInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    checkDeviceInput.setDevice_info(deviceName + ",Android " + Build.VERSION.RELEASE);
                    /*CheckDeviceAsyncTask asynCheckDevice = new CheckDeviceAsyncTask(checkDeviceInput, this, this);
                    asynCheckDevice.executeOnExecutor(threadPoolExecutor);*/
                } else {
                    if (getIntent().getStringExtra("from") != null) {
                        /** review **/
                        onBackPressed();
                    } else {
                        if (com.home.vod.util.Util.check_for_subscription == 1) {
                            // Go for subscription

                            if (NetworkStatus.getInstance().isConnected(FdGhana_loginActivity.this)) {
                                if (com.home.vod.util.Util.dataModel.getIsFreeContent() == 1) {
                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                    getVideoDetailsInput.setAuthToken(authTokenStr);
                                    getVideoDetailsInput.setContent_uniq_id(com.home.vod.util.Util.dataModel.getMovieUniqueId().trim());
                                    getVideoDetailsInput.setStream_uniq_id(com.home.vod.util.Util.dataModel.getStreamUniqueId().trim());
                                    getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                    getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                    getVideoDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                    /*asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, FdGhana_loginActivity.this, FdGhana_loginActivity.this);
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);*/
                                } else {
                                    setResultAtFinishActivity();
                                }
                            } else {
                                Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION, DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }

                        } else {

                            if (planId.equals("1") && isSubscribedStr.equals("0")) {
                                Intent intent = new Intent(FdGhana_loginActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                if (LoginActivity.loginA != null) {
                                    LoginActivity.loginA.finish();
                                }
                                finish();
                            } else {
                                Intent in = new Intent(FdGhana_loginActivity.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                if (LoginActivity.loginA != null) {
                                    LoginActivity.loginA.finish();
                                }
                                finish();
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onCheckDevicePreExecuteStarted() {
        pDialog = new ProgressBarHandler(FdGhana_loginActivity.this);
        pDialog.show();
    }

    @Override
    public void onCheckDevicePostExecuteCompleted(CheckDeviceOutput checkDeviceOutput, int code, String message) {

    }

    public void setResultAtFinishActivity() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {

    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int code, String status, String message) {

    }

    public void show_logout_popup(String msg) {
        {

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) FdGhana_loginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View convertView = inflater.inflate(R.layout.logout_popup, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("");

            logout_text = (TextView) convertView.findViewById(R.id.logout_text);
            ok = (Button) convertView.findViewById(R.id.ok);
            cancel = (Button) convertView.findViewById(R.id.cancel);


            // Font implemented Here//
            FontUtls.loadFont(FdGhana_loginActivity.this, getResources().getString(R.string.regular_fonts), logout_text);
            FontUtls.loadFont(FdGhana_loginActivity.this, getResources().getString(R.string.regular_fonts), ok);
            FontUtls.loadFont(FdGhana_loginActivity.this, getResources().getString(R.string.regular_fonts), cancel);

            //==============end===============//

            // Language Implemented Here //

            logout_text.setText(msg);
            ok.setText(" " + languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK));
            cancel.setText(" " + languagePreference.getTextofLanguage(CANCEL_BUTTON, DEAFULT_CANCEL_BUTTON));

            //==============End===============//


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkStatus.getInstance().isConnected(FdGhana_loginActivity.this)) {

                        // Call Api For Simultaneous Logout
                        SimultaneousLogoutInput simultaneousLogoutInput = new SimultaneousLogoutInput();
                        simultaneousLogoutInput.setAuthToken(authTokenStr);
                        simultaneousLogoutInput.setDevice_type("1");
                        simultaneousLogoutInput.setEmail_id(regEmailStr);
                        GetSimultaneousLogoutAsync asynSimultaneousLogout = new GetSimultaneousLogoutAsync(simultaneousLogoutInput, FdGhana_loginActivity.this, FdGhana_loginActivity.this);
                        asynSimultaneousLogout.executeOnExecutor(threadPoolExecutor);
                    } else {
                        Toast.makeText(FdGhana_loginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    logout_alert.dismiss();
                }
            });
            logout_alert = alertDialog.show();
            logout_alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }
    }

    @Override
    public void onSimultaneousLogoutPreExecuteStarted() {
        progressDialog1 = new ProgressDialog(FdGhana_loginActivity.this, R.style.MyTheme);
        progressDialog1.setCancelable(false);
        progressDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
        progressDialog1.setIndeterminate(false);
        progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_rawable));
        progressDialog1.show();
    }

    @Override
    public void onSimultaneousLogoutPostExecuteCompleted(int code) {
        if (progressDialog1 != null && progressDialog1.isShowing()) {
            progressDialog1.hide();
            progressDialog1 = null;
        }
        if (code == 200) {
            // Allow the user to activity_login
            if (logout_alert.isShowing()) {
                logout_alert.dismiss();
            }

            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, DEFAULT_SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(TRY_AGAIN, DEFAULT_TRY_AGAIN), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onForgotpassDetailsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(FdGhana_loginActivity.this);
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


    // Kushal- commented ***
   /* public void ShowDialog(String Title, String msg)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(FdGhana_loginActivity.this, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (navigation) {
                            *//*Intent in = new Intent(FdGhana_loginActivity.this, LoginActivity.class);
                            in.putExtra("PlayerModel", playerModel);
                            in.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(in);
                            finish();*//*
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
        dlgAlert.create().show();
    }*/
    // end ***
}
