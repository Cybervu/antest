package com.home.vod.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.home.apisdk.apiController.AsyncGmailReg;
import com.home.apisdk.apiController.CheckDeviceAsyncTask;
import com.home.apisdk.apiController.CheckFbUserDetailsAsyn;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.SocialAuthAsynTask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.CheckDeviceInput;
import com.home.apisdk.apiModel.CheckDeviceOutput;
import com.home.apisdk.apiModel.CheckFbUserDetailsInput;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.GmailLoginInput;
import com.home.apisdk.apiModel.GmailLoginOutput;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.SocialAuthInputModel;
import com.home.apisdk.apiModel.SocialAuthOutputModel;
import com.home.vod.R;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.ANDROID_VERSION;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ANDROID_VERSION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_PASSWORD_INVALID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GOOGLE_FCM_TOKEN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.EMAIL_PASSWORD_INVALID;
import static com.home.vod.preferences.LanguagePreference.GOOGLE_FCM_TOKEN;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.util.Constant.authTokenStr;
import static player.utils.Util.DEFAULT_FACEBOOK_STATUS;
import static player.utils.Util.DEFAULT_GOOGLE_STATUS;
import static player.utils.Util.DETAILS_NOT_FOUND_ALERT;
import static player.utils.Util.FACEBOOK_STATUS;
import static player.utils.Util.GOOGLE_STATUS;

public class PreLoginActivity extends AppCompatActivity implements CheckFbUserDetailsAsyn.CheckFbUserDetailsListener,
        SocialAuthAsynTask.SocialAuthListener,
        CheckDeviceAsyncTask.CheckDeviceListener,
        LogoutAsynctask.LogoutListener,
        GoogleApiClient.OnConnectionFailedListener,
        AsyncGmailReg.AsyncGmailListener {

    public Button loginBtn;
    public Button btnFbLogin;
    LoginButton loginButton;

    String fbUserId = "";
    String fbEmail = "";
    String fbName = "";
    String deviceName = "";
    String deviceRestrictionMessage = "";
    String PlanId = "";
    String UniversalIsSubscribed = "";
    String Authname, AuthEmail, AuthId;
    private String AuthImageUrl;

    private LanguagePreference languagePreference;

    private CallbackManager callbackManager;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    CheckFbUserDetailsAsyn asynCheckFbUserDetails;
    SocialAuthAsynTask asynFbRegDetails;

    PreferenceManager preferenceManager;
    Toolbar mActionBarToolbar;

    RelativeLayout google_sign_in_button;

    GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 999;

    ProgressBarHandler pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_pre_login);

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
//        mActionBarToolbar.getBackground().setAlpha(0);
        btnFbLogin= (Button)findViewById(R.id.loginWithFacebookButton);
        btnFbLogin.setText("Login With Facebook");

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference((this));

        loginBtn=(Button)findViewById(R.id.loginBtn);

        google_sign_in_button=(RelativeLayout)findViewById(R.id.google_sign_in_button);

        if(languagePreference.getTextofLanguage(GOOGLE_STATUS,DEFAULT_GOOGLE_STATUS).equals("1")){
            google_sign_in_button.setVisibility(View.VISIBLE);
        }

        if(languagePreference.getTextofLanguage(FACEBOOK_STATUS,DEFAULT_FACEBOOK_STATUS).equals("1")){
            btnFbLogin.setVisibility(View.VISIBLE);
        }

        Log.v("pratik","google login statis="+languagePreference.getTextofLanguage(GOOGLE_STATUS,DEFAULT_GOOGLE_STATUS));
        Log.v("pratik","fb login statis="+languagePreference.getTextofLanguage(FACEBOOK_STATUS,DEFAULT_FACEBOOK_STATUS));
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logIntent=new Intent(PreLoginActivity.this,FdGhana_loginActivity.class);
                startActivity(logIntent);
                finish();
            }
        });


        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();
        PlanId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        callbackManager=CallbackManager.Factory.create();

        loginButton= (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        btnFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

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

                                    String fName = "";
                                    if ((json.has("id")) && json.getString("id").trim() != null && !json.getString("id").trim().isEmpty() && !json.getString("id").trim().equals("null") && !json.getString("id").trim().matches("")) {
                                        fbUserId = json.getString("id");
                                    }
                                    if ((json.has("first_name")) && json.getString("first_name").trim() != null && !json.getString("first_name").trim().isEmpty() && !json.getString("first_name").trim().equals("null") && !json.getString("first_name").trim().matches("")) {
                                        if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                            fbName = json.getString("first_name") + " " + json.getString("last_name");
                                        }
                                        fName =  json.getString("first_name");
                                    } else {

                                        if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                            fbName = json.getString("last_name");
                                            fName =  json.getString("last_name");
                                        }else{
                                            if ((json.has("name")) && json.getString("name").trim() != null && !json.getString("name").trim().isEmpty() && !json.getString("name").trim().equals("null") && !json.getString("name").trim().matches("")) {
                                                fbName = json.getString("name");
                                                fName = json.getString("name").replace(" ","").trim();
                                            }
                                        }

                                    }

                                  /*  if (fbName!=null && fbName.matches("")){
                                        fbName = fbUserId;
                                    }*/
                                    if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                        fbEmail = json.getString("email");
                                    } else {
                                        if (fbName != null && !fbName.matches("")) {
                                            fbEmail = fName + "@facebook.com";
                                        } else {
                                            fbName = fbUserId;
                                            fbEmail = fbUserId + "@facebook.com";
                                        }
                                    }

                                  /*  if ((json.has("name")) && json.getString("name").trim() != null && !json.getString("name").trim().isEmpty() && !json.getString("name").trim().equals("null") && !json.getString("name").trim().matches("")) {

                                        fbName = json.getString("name");
                                        fbName = json.getString("name").replace(" ","").trim();

                                    }
                                    if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                        fbEmail = json.getString("email");
                                    } else {
                                        fbEmail = fbName + "@facebook.com";

                                    }
                                    if ((json.has("id")) && json.optString("id").trim() != null && !json.optString("id").trim().isEmpty() && !json.optString("id").trim().equals("null") && !json.optString("id").trim().matches("")) {
                                        fbUserId = json.optString("id");
                                    }*/

//                                    registerButton.setVisibility(View.GONE);
                                    loginButton.setVisibility(View.GONE);
                                    btnFbLogin.setVisibility(View.GONE);
                                   handleFbUserDetails(fbUserId,fbEmail,fbName);
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

//            registerButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            btnFbLogin.setVisibility(View.VISIBLE);
            Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {

//            registerButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            btnFbLogin.setVisibility(View.VISIBLE);
            Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();

            //progressDialog.dismiss();
        }
    };

    public void handleFbUserDetails(String fbUserId, String fbEmail, String fbName) {
        this.fbUserId = fbUserId;
        this.fbEmail = fbEmail;
        this.fbName = fbName;
        CheckFbUserDetailsInput checkFbUserDetailsInput = new CheckFbUserDetailsInput();
        checkFbUserDetailsInput.setAuthToken(authTokenStr);
        checkFbUserDetailsInput.setFb_userid(fbUserId.trim());
        asynCheckFbUserDetails = new CheckFbUserDetailsAsyn(checkFbUserDetailsInput, PreLoginActivity.this, PreLoginActivity.this);
        asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);

    }

    @Override
    public void onCheckFbUserDetailsAsynPreExecuteStarted() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
        pDialog = new ProgressBarHandler(PreLoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onCheckFbUserDetailsAsynPostExecuteCompleted(int code) {

        if (code == 0) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(LanguagePreference.DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }

        if (code == 200) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            SocialAuthInputModel socialAuthInputModel = new SocialAuthInputModel();
            socialAuthInputModel.setAuthToken(authTokenStr);
            socialAuthInputModel.setName(fbName.trim());
            socialAuthInputModel.setEmail(fbEmail.trim());
            socialAuthInputModel.setPassword("");
            socialAuthInputModel.setFb_userid(fbUserId.trim());
            socialAuthInputModel.setDevice_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            socialAuthInputModel.setDevice_type("1");
            socialAuthInputModel.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            asynFbRegDetails = new SocialAuthAsynTask(socialAuthInputModel, this, this);
            asynFbRegDetails.executeOnExecutor(threadPoolExecutor);


        } else {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(LanguagePreference.DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }
    }

    @Override
    public void onSocialAuthPreExecuteStarted() {
        pDialog = new ProgressBarHandler(PreLoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onSocialAuthPostExecuteCompleted(SocialAuthOutputModel socialAuthOutputModel, int status, String message) {

        if (socialAuthOutputModel == null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;

            }
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(LanguagePreference.DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }

        if (status > 0) {

            //    SharedPreferences.Editor editor = pref.edit();

            if (status == 200) {
                String displayNameStr = socialAuthOutputModel.getDisplay_name();
                String emailFromApiStr = socialAuthOutputModel.getEmail();
                String profileImageStr = socialAuthOutputModel.getProfile_image();
                String isSubscribedStr = socialAuthOutputModel.getIsSubscribed();
                String loginHistoryIdStr = socialAuthOutputModel.getLogin_history_id();

                preferenceManager.setLogInStatusToPref("1");
                preferenceManager.setUserIdToPref(socialAuthOutputModel.getId());
                preferenceManager.setPwdToPref("");
                preferenceManager.setEmailIdToPref(emailFromApiStr);
                preferenceManager.setDispNameToPref(displayNameStr);
                preferenceManager.setLoginProfImgoPref(profileImageStr);
                preferenceManager.setIsSubscribedToPref(isSubscribedStr);
                preferenceManager.setLoginHistIdPref(loginHistoryIdStr);


                if (NetworkStatus.getInstance().isConnected(this)) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }


                    if ((languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE)).trim().equals("1")) {

                        LogUtil.showLog("MUVI", "isRestrictDevice called");
                        // Call For Check Api.
                        CheckDeviceInput checkDeviceInput = new CheckDeviceInput();
                        if (preferenceManager != null) {
                            String userIdStr = preferenceManager.getUseridFromPref();
                            checkDeviceInput.setUser_id(userIdStr.trim());
                        }
                        checkDeviceInput.setDevice(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        checkDeviceInput.setGoogle_id(languagePreference.getTextofLanguage(Util.GOOGLE_FCM_TOKEN, Util.DEFAULT_GOOGLE_FCM_TOKEN));
                        checkDeviceInput.setAuthToken(authTokenStr);
                        checkDeviceInput.setUser_id(preferenceManager.getUseridFromPref());
                        checkDeviceInput.setDevice_type("1");
                        checkDeviceInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        checkDeviceInput.setDevice_info(deviceName + "," + languagePreference.getTextofLanguage(ANDROID_VERSION, DEFAULT_ANDROID_VERSION) + " " + Build.VERSION.RELEASE);
                        CheckDeviceAsyncTask asynCheckDevice = new CheckDeviceAsyncTask(checkDeviceInput, this, this);
                        asynCheckDevice.executeOnExecutor(threadPoolExecutor);
                    } else {


                        if (Util.check_for_subscription == 1) {
                            //go to subscription page
                            if (NetworkStatus.getInstance().isConnected(this)) {
                                if (Util.dataModel.getIsFreeContent() == 1) {
                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                    getVideoDetailsInput.setAuthToken(authTokenStr);
                                    getVideoDetailsInput.setContent_uniq_id(getVideoDetailsInput.getContent_uniq_id());
                                    getVideoDetailsInput.setStream_uniq_id(getVideoDetailsInput.getStream_uniq_id());
                                    getVideoDetailsInput.setInternetSpeed(getVideoDetailsInput.getInternetSpeed());
                                    getVideoDetailsInput.setUser_id(getVideoDetailsInput.getUser_id());
                                    getVideoDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                    /*VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, PreLoginActivity.this, PreLoginActivity.this);
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);*/
                                } else {

                                    setResultAtFinishActivity();


                                }
                            } else {
                                Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }

                        } else {

                            Intent in = new Intent(PreLoginActivity.this, MainActivity.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(in);

                            onBackPressed();
                        }
                    }


                } else {
                    Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }


            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;

                }
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(EMAIL_PASSWORD_INVALID, DEFAULT_EMAIL_PASSWORD_INVALID));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(LanguagePreference.DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }
    }

    @Override
    public void onCheckDevicePreExecuteStarted() {
        pDialog = new ProgressBarHandler(PreLoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onCheckDevicePostExecuteCompleted(CheckDeviceOutput checkDeviceOutput, int code, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;

            }
        } catch (IllegalArgumentException ex) {
        }
        deviceRestrictionMessage = message;

        if (code > 0) {
            if (code == 200) {

                // Allow The User To Login
                if (Util.check_for_subscription == 1) {
                    //go to subscription page
                    if (NetworkStatus.getInstance().isConnected(this)) {

                            setResultAtFinishActivity();


                    } else {
                        Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (PlanId.equals("1") && UniversalIsSubscribed.equals("0")) {
                        Intent intent = new Intent(PreLoginActivity.this, SubscriptionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    } else {
                        Intent in = new Intent(PreLoginActivity.this, MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                    }
                }
            } else {
                // Call For Logout
                LogOut();
            }
        } else {
            // Call For Logout
            LogOut();
        }
    }

    public void setResultAtFinishActivity(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void LogOut() {
        String loginHistoryIdStr = preferenceManager.getLoginHistIdFromPref();
        LogUtil.showLog("MUVI3", "logout Called");
        LogoutInput logoutInput = new LogoutInput();
        logoutInput.setAuthToken(authTokenStr);
        logoutInput.setLogin_history_id(loginHistoryIdStr);
        logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, this, this);
        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);
    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(PreLoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;

            }
        } catch (IllegalArgumentException ex) {
            Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (status == null) {
            Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }

        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(deviceRestrictionMessage);
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();

            } else {
                Toast.makeText(PreLoginActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.v("pratik", "gSign in res=" + result.toString());

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        // Log.d(TAG, "handleSignInResult:" + result.isSuccess()+result.getSignInAccount());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Authname = acct.getDisplayName();
            AuthEmail = acct.getEmail();
            AuthId = acct.getId();
            AuthImageUrl = String.valueOf(acct.getPhotoUrl());

            Log.v("pratik","name=="+Authname);

            GmailLoginInput gmailLoginInput = new GmailLoginInput();
            gmailLoginInput.setEmail(AuthEmail);
            gmailLoginInput.setName(Authname);
            gmailLoginInput.setGmail_userid(AuthId);
            gmailLoginInput.setProfile_image(AuthImageUrl);
            gmailLoginInput.setPassword("");
            gmailLoginInput.setAuthToken(authTokenStr);
            AsyncGmailReg asyncGmailReg = new AsyncGmailReg(gmailLoginInput, this, this);
            asyncGmailReg.executeOnExecutor(threadPoolExecutor);
        }
    }

    @Override
    public void onGmailRegPreExecuteStarted() {
        pDialog = new ProgressBarHandler(PreLoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onGmailRegPostExecuteCompleted(GmailLoginOutput gmailLoginOutput, int status, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {
            status = 0;

        }

        if (status == 200){
            preferenceManager.setLogInStatusToPref("1");
            preferenceManager.setUserIdToPref(gmailLoginOutput.getId());
            preferenceManager.setPwdToPref("");
            preferenceManager.setEmailIdToPref(gmailLoginOutput.getEmail());
            preferenceManager.setDispNameToPref(gmailLoginOutput.getDisplay_name());
            preferenceManager.setLoginProfImgoPref(gmailLoginOutput.getProfile_image());
            preferenceManager.setIsSubscribedToPref(Integer.toString(gmailLoginOutput.getIsSubscribed()));
            preferenceManager.setLoginHistIdPref(gmailLoginOutput.getLogin_history_id());

            if (NetworkStatus.getInstance().isConnected(PreLoginActivity.this)) {

                //load video urls according to resolution
                if (languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1")) {

                    Log.v("BIBHU", "isRestrictDevice called");
                    // Call For Check Api.
                    CheckDeviceInput checkDeviceInput = new CheckDeviceInput();
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
                        //** review **//*
                        onBackPressed();
                    } else {
                        if (Util.check_for_subscription == 1) {
                            //go to subscription page
                            if (NetworkStatus.getInstance().isConnected(PreLoginActivity.this)) {
                                setResultAtFinishActivity();

                            } else {
                                Util.showToast(PreLoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                                // Toast.makeText(PreLoginActivity.this, Util.getTextofLanguage(PreLoginActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if (PlanId.equals("1") && preferenceManager.getIsSubscribedFromPref().equals("0")) {
                                Intent intent = new Intent(PreLoginActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                               /* if (RegisterActivity.fa != null) {
                                    RegisterActivity.fa.finish();
                                }
                                if (ForgotPasswordActivity.forgotA != null) {
                                    ForgotPasswordActivity.forgotA.finish();
                                }*/
                                onBackPressed();
                            } else {

                                Intent in = new Intent(PreLoginActivity.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                onBackPressed();
                            }
                        }
                    }
                }

            } else {
                Util.showToast(PreLoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
            }
        }
        else {

            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(PreLoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(LanguagePreference.DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();


        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
