package com.home.vod.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.home.apisdk.apiController.AsyncGmailReg;
import com.home.apisdk.apiController.CheckDeviceAsyncTask;
import com.home.apisdk.apiController.CheckFbUserDetailsAsyn;
import com.home.apisdk.apiController.GetSimultaneousLogoutAsync;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LoginAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.SocialAuthAsynTask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.CheckDeviceInput;
import com.home.apisdk.apiModel.CheckDeviceOutput;
import com.home.apisdk.apiModel.CheckFbUserDetailsInput;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.GmailLoginInput;
import com.home.apisdk.apiModel.GmailLoginOutput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.apisdk.apiModel.Login_input;
import com.home.apisdk.apiModel.Login_output;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.SimultaneousLogoutInput;
import com.home.apisdk.apiModel.SocialAuthInputModel;
import com.home.apisdk.apiModel.SocialAuthOutputModel;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.home.vod.LoginHandler;
import com.home.vod.R;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import player.activity.AdPlayerActivity;
import player.activity.ExoPlayerActivity;
import player.activity.MyLibraryPlayer;
import player.activity.Player;

import static com.home.vod.preferences.LanguagePreference.ANDROID_VERSION;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CANCEL_BUTTON;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEAFULT_CANCEL_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ANDROID_VERSION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_EMAIL_PASSWORD_INVALID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FORGOT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NEW_HERE_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_OOPS_INVALID_EMAIL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_UP_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TRY_AGAIN;
import static com.home.vod.preferences.LanguagePreference.DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.EMAIL_PASSWORD_INVALID;
import static com.home.vod.preferences.LanguagePreference.ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.FORGOT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.NEW_HERE_TITLE;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DETAILS_AVAILABLE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.OOPS_INVALID_EMAIL;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SIGN_UP_TITLE;
import static com.home.vod.preferences.LanguagePreference.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.TEXT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.TRY_AGAIN;
import static com.home.vod.util.Constant.authTokenStr;

public class LoginActivity extends AppCompatActivity implements LoginAsynTask.LoinDetailsListener, GoogleApiClient.OnConnectionFailedListener,
        GetValidateUserAsynTask.GetValidateUserListener,
        VideoDetailsAsynctask.VideoDetailsListener,
        LogoutAsynctask.LogoutListener, CheckDeviceAsyncTask.CheckDeviceListener,
        GetSimultaneousLogoutAsync.SimultaneousLogoutAsyncListener,
        CheckFbUserDetailsAsyn.CheckFbUserDetailsListener, SocialAuthAsynTask.SocialAuthListener
        ,AsyncGmailReg.AsyncGmailListener{


    /*subtitle-------------------------------------*/

    LoginHandler loginHandler;
    String filename = "";
    static File mediaStorageDir;
    String UniversalErrorMessage = "";
    String UniversalIsSubscribed = "";
    String loggedInIdStr;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ProgressDialog progressDialog1;
    public static ProgressBarHandler progressBarHandler;
    ProgressBarHandler pDialog;
    Player playerModel;
    LanguagePreference languagePreference;


    ////////Google sign in ////start/////
    TextView name, email, id;
    String Authname, AuthEmail, AuthId;
    private static final String TAG = "Nihar";
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    Button signout;
    private String AuthImageUrl;
    /////////////////////end//////////////////

    @Override
    public void onLoginPreExecuteStarted() {
        pDialog = new ProgressBarHandler(LoginActivity.this);
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


                    if (languagePreference.getTextofLanguage (IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1")) {

                        LogUtil.showLog("MUVI", "isRestrictDevice called");
                        // Call For Check Api.
                        CheckDeviceInput checkDeviceInput = new CheckDeviceInput();
                        if (preferenceManager != null) {
                            String userIdStr = preferenceManager.getUseridFromPref();
                            checkDeviceInput.setUser_id(userIdStr.trim());
                        }
                        checkDeviceInput.setDevice(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        checkDeviceInput.setGoogle_id(languagePreference.getTextofLanguage (Util.GOOGLE_FCM_TOKEN, Util.DEFAULT_GOOGLE_FCM_TOKEN));
                        checkDeviceInput.setAuthToken(authTokenStr);
                        checkDeviceInput.setDevice_type("1");
                        checkDeviceInput.setLang_code(languagePreference.getTextofLanguage (SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        checkDeviceInput.setDevice_info(deviceName + "," + languagePreference.getTextofLanguage (ANDROID_VERSION, DEFAULT_ANDROID_VERSION) + " " + Build.VERSION.RELEASE);
                        CheckDeviceAsyncTask asynCheckDevice = new CheckDeviceAsyncTask(checkDeviceInput, this, this);
                        asynCheckDevice.executeOnExecutor(threadPoolExecutor);
                    } else {

                        if (getIntent().getStringExtra("from") != null) {
                            /** review **/
                            onBackPressed();
                        }else {
                            if (Util.check_for_subscription == 1) {
                                //go to subscription page
                                if (NetworkStatus.getInstance().isConnected(this)) {
                                    if (Util.dataModel.getIsFreeContent() == 1) {
                                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                                        getVideoDetailsInput.setAuthToken(authTokenStr);
                                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                                        getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                                        VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, LoginActivity.this, LoginActivity.this);
                                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                    } else {
                                        ValidateUserInput validateUserInput = new ValidateUserInput();
                                        validateUserInput.setAuthToken(authTokenStr);
                                        validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                                        validateUserInput.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
                                        validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                                        validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                                        validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                        validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                                        GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, LoginActivity.this, LoginActivity.this);
                                        asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);


                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                                }

                            } else {
                                if (PlanId.equals("1") && login_output.getIsSubscribed().equals("0")) {
                                    Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                    finish();
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage (NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage (EMAIL_PASSWORD_INVALID, DEFAULT_EMAIL_PASSWORD_INVALID));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage (BUTTON_OK, DEFAULT_BUTTON_OK),
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

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage (SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage (BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }

    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {

        pDialog = new ProgressBarHandler(LoginActivity.this);
        pDialog.show();

    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {

        if (validateUserOutput == null) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage (NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            finish();
                        }
                    });
            dlgAlert.create().show();
        } else if (status <= 0) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            finish();
                        }
                    });
            dlgAlert.create().show();
        }

        if (status > 0) {
            if (status == 427) {

                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                if (message != null && message.equalsIgnoreCase("")) {
                    dlgAlert.setMessage(message);
                } else {
                    dlgAlert.setMessage(languagePreference.getTextofLanguage( CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));

                }
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                onBackPressed();
                            }
                        });
                dlgAlert.create().show();
            } else if (status == 429) {

                if (validateUserOutput.getValiduser_str() != null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }

                    if ((validateUserOutput.getValiduser_str().trim().equalsIgnoreCase("OK")) || (validateUserOutput.getValiduser_str().trim().matches("OK")) || (validateUserOutput.getValiduser_str().trim().equals("OK"))) {
                        if (NetworkStatus.getInstance().isConnected(this)) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setContent_uniq_id(getVideoDetailsInput.getContent_uniq_id());
                            getVideoDetailsInput.setStream_uniq_id(getVideoDetailsInput.getStream_uniq_id());
                            getVideoDetailsInput.setInternetSpeed(getVideoDetailsInput.getInternetSpeed());
                            getVideoDetailsInput.setUser_id(getVideoDetailsInput.getUser_id());
                            VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, this, this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    } else {

                        if ((message.trim().equalsIgnoreCase("Unpaid")) || (message.trim().matches("Unpaid")) || (message.trim().equals("Unpaid"))) {
                            if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                                if (Util.dataModel.getContentTypesId() == 3) {
                                    // Show Popup
                                    ShowPpvPopUp();
                                } else {
                                    // Go to ppv Payment
                                    payment_for_single_part();
                                }
                            } else if (PlanId.equals("1") && validateUserOutput.getIsMemberSubscribed().equals("0")) {
                                Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);
                            } else {
                                if (Util.dataModel.getContentTypesId() == 3) {
                                    // Show Popup
                                    ShowPpvPopUp();
                                } else {
                                    // Go to ppv Payment
                                    payment_for_single_part();
                                }
                            }
                        }

                    }
                }

            } else if (Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1) {
                if (Util.dataModel.getContentTypesId() == 3) {
                    // Show Popup
                    ShowPpvPopUp();
                } else {
                    // Go to ppv Payment
                    payment_for_single_part();
                }
            } else if (PlanId.equals("1") && validateUserOutput.getIsMemberSubscribed().equals("0")) {
                Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            } else if (Util.dataModel.getIsConverted() == 0) {
                Util.showNoDataAlert(LoginActivity.this);
               /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                onBackPressed();
                            }
                        });
                dlgAlert.create().show();*/
            } else {
                if (NetworkStatus.getInstance().isConnected(this)) {

                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                    getVideoDetailsInput.setAuthToken(authTokenStr);
                    getVideoDetailsInput.setContent_uniq_id(getVideoDetailsInput.getContent_uniq_id());
                    getVideoDetailsInput.setStream_uniq_id(getVideoDetailsInput.getStream_uniq_id());
                    getVideoDetailsInput.setInternetSpeed(getVideoDetailsInput.getInternetSpeed());
                    getVideoDetailsInput.setUser_id(getVideoDetailsInput.getUser_id());
                    VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, this, this);
                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        }
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {

        SubTitleName.clear();
        SubTitlePath.clear();
        ResolutionUrl.clear();
        ResolutionFormat.clear();
        pDialog = new ProgressBarHandler(LoginActivity.this);
        pDialog.show();

    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int statusCode, String stus, String message) {
        // _video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // _video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/
        boolean play_video = true;

        if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {

            if (_video_details_output.getStreaming_restriction().trim().equals("0")) {

                play_video = false;
            }
            else
            {
                play_video = true;
            }
        }
        else
        {
            play_video = true;
        }
        if (!play_video) {

            try {
                if (pDialog.isShowing())
                    pDialog.hide();
            } catch (IllegalArgumentException ex) {
            }

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            dlgAlert.create().show();

            return;
        }



        if (statusCode == 200) {
            playerModel.setIsOffline(_video_details_output.getIs_offline());
            playerModel.setDownloadStatus(_video_details_output.getDownload_status());
            if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {

                /**@bishal
                 * for drm player below condition added
                 * if studio_approved_url is there in api then set the videourl from this other wise goto 2nd one
                 */

                if (_video_details_output.getStudio_approved_url() != null &&
                        !_video_details_output.getStudio_approved_url().isEmpty() &&
                        !_video_details_output.getStudio_approved_url().equals("null") &&
                        !_video_details_output.getStudio_approved_url().matches("")) {
                    LogUtil.showLog("BISHAL","if called means  studioapproved");
                    playerModel.setVideoUrl(_video_details_output.getStudio_approved_url());
                    LogUtil.showLog("BS","studipapprovedurl===="+playerModel.getVideoUrl());


                    if ( _video_details_output.getLicenseUrl().trim() != null && !_video_details_output.getLicenseUrl().trim().isEmpty() && !_video_details_output.getLicenseUrl().trim().equals("null") && !_video_details_output.getLicenseUrl().trim().matches("")) {
                        playerModel.setLicenseUrl(_video_details_output.getLicenseUrl());
                    }
                    if ( _video_details_output.getVideoUrl().trim() != null && !_video_details_output.getVideoUrl().isEmpty() && !_video_details_output.getVideoUrl().equals("null") && !_video_details_output.getVideoUrl().trim().matches("")) {
                        playerModel.setMpdVideoUrl(_video_details_output.getVideoUrl());

                    }else {
                        playerModel.setMpdVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                    }
                }

                else {
                    if (_video_details_output.getVideoUrl() != null || !_video_details_output.getVideoUrl().matches("")) {
                        playerModel.setVideoUrl(_video_details_output.getVideoUrl());
                        LogUtil.showLog("BISHAL", "videourl===" + playerModel.getVideoUrl());
                        playerModel.setThirdPartyPlayer(false);
                    } else {
                        //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                        playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                    }
                }
            } else {
                if (_video_details_output.getThirdparty_url() != null || !_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));

                }
            }

            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if(_video_details_output.getPlayed_length()!=null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(_video_details_output.getPlayed_length())));




            //dependency for datamodel
            Util.dataModel.setVideoUrl(_video_details_output.getVideoUrl());
            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());
            Util.dataModel.setThirdPartyUrl(_video_details_output.getThirdparty_url());
            Util.dataModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            Util.dataModel.setChannel_id(_video_details_output.getChannel_id());
            Util.dataModel.setPreRoll(_video_details_output.getPreRoll());
            Util.dataModel.setPostRoll(_video_details_output.getPostRoll());
            Util.dataModel.setMidRoll(_video_details_output.getMidRoll());
            Util.dataModel.setAdDetails(_video_details_output.getAdDetails());


            //player model set
            playerModel.setAdDetails(_video_details_output.getAdDetails());
            playerModel.setMidRoll(_video_details_output.getMidRoll());
            playerModel.setPostRoll(_video_details_output.getPostRoll());
            playerModel.setChannel_id(_video_details_output.getChannel_id());
            playerModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            playerModel.setPreRoll(_video_details_output.getPreRoll());
            playerModel.setSubTitleName(_video_details_output.getSubTitleName());
            playerModel.setSubTitlePath(_video_details_output.getSubTitlePath());
            playerModel.setResolutionFormat(_video_details_output.getResolutionFormat());
            playerModel.setResolutionUrl(_video_details_output.getResolutionUrl());
            playerModel.setFakeSubTitlePath(_video_details_output.getFakeSubTitlePath());
            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            FakeSubTitlePath = _video_details_output.getFakeSubTitlePath();



            if (playerModel.getVideoUrl() == null ||
                    playerModel.getVideoUrl().matches("")) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                }
                Util.showNoDataAlert(LoginActivity.this);
               /* AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();*/
            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                }


                // condition for checking if the response has third party url or not.
                if (_video_details_output.getThirdparty_url()==null ||
                        _video_details_output.getThirdparty_url().matches("")
                        ) {


                    playerModel.setThirdPartyPlayer(false);
                    final Intent playVideoIntent;
                    if (Util.goToLibraryplayer) {
                        playVideoIntent = new Intent(LoginActivity.this, MyLibraryPlayer.class);
                    }  else
                    {
                        if (Util.dataModel.getAdNetworkId() == 3){
                            LogUtil.showLog("responseStr","playVideoIntent"+Util.dataModel.getAdNetworkId());

                            playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);

                        }
                        else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1){
                            if (Util.dataModel.getPlayPos() <= 0) {
                                playVideoIntent = new Intent(LoginActivity.this, AdPlayerActivity.class);
                            }else{
                                playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);

                            }
                        }else{
                            playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);

                        }
                        // playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (FakeSubTitlePath.size() > 0) {
                                // This Portion Will Be changed Later.

                                File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
                                if (dir.isDirectory()) {
                                    String[] children = dir.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(dir, children[i]).delete();
                                    }
                                }

                                progressBarHandler = new ProgressBarHandler(LoginActivity.this);
                                progressBarHandler.show();
                                Download_SubTitle(FakeSubTitlePath.get(0).trim());
                            } else {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                /*playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                                playVideoIntent.putExtra("PlayerModel",playerModel);
                                startActivity(playVideoIntent);
                                finish();
                            }

                        }
                    });
                } else {
                    final Intent playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                /*playVideoIntent.putExtra("SubTitleName", SubTitleName);
                                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                    playVideoIntent.putExtra("PlayerModel",playerModel);
                    startActivity(playVideoIntent);

                    //below part  checked at exoplayer thats why no need of checking here

                   /* playerModel.setThirdPartyPlayer(true);
                    if (playerModel.getVideoUrl().contains("://www.youtube") ||
                            playerModel.getVideoUrl().contains("://www.youtu.be")) {
                        if (playerModel.getVideoUrl().contains("live_stream?channel")) {
                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    playVideoIntent.putExtra("PlayerModel",playerModel);
                                    startActivity(playVideoIntent);

                                }
                            });
                        } else {

                            final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, YouTubeAPIActivity.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    playVideoIntent.putExtra("PlayerModel",playerModel);
                                    startActivity(playVideoIntent);


                                }
                            });

                        }
                    } else {
                        final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, ThirdPartyPlayer.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                playVideoIntent.putExtra("PlayerModel",playerModel);
                                startActivity(playVideoIntent);

                            }
                        });
                    }*/
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                playerModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
                // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }
            playerModel.setVideoUrl(languagePreference.getTextofLanguage( NO_DATA, DEFAULT_NO_DATA));
            //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();*/
            Util.showNoDataAlert(LoginActivity.this);
        }




    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(LoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        if (status == null) {
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(UniversalErrorMessage);
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();

            } else {
                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



        /*subtitle-------------------------------------*/
    /*chromecast-------------------------------------*/


    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    /**
     * List of various states that we can be in
     */
    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;


    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(CastSession session) {
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionEnding(CastSession session) {
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
        }
    }


    MediaInfo mediaInfo;
    /*chromecast-------------------------------------*/

    /*********fb****/

    String fbUserId = "";
    String fbEmail = "";
    String fbName = "";
    private LinearLayout btnLogin;
    private ProgressBarHandler progressDialog;
    private CallbackManager callbackManager;
    CheckFbUserDetailsAsyn asynCheckFbUserDetails;
    SocialAuthAsynTask asynFbRegDetails;

    /*********fb****/
    //AsynLogInDetails asyncReg;
    GetValidateUserAsynTask asynLoadVideoUrls;
    EditText editEmailStr, editPasswordStr;
    TextView forgotPassword, loginNewUser, signUpTextView;
    Button loginButton;
    LoginButton loginWithFacebookButton;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    //SharedPreferences pref;
    String regEmailStr, regPasswordStr;
    Toolbar mActionBarToolbar;

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    // Added For FCM

    TextView logout_text;
    Button ok, cancel;
    AlertDialog logout_alert;
    String PlanId = "";
    AlertDialog alert;
    String priceForUnsubscribedStr, priceFosubscribedStr;
    String deviceName = "";

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*********fb****/
        FacebookSdk.sdkInitialize(getApplicationContext());
        /*********fb****/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_login);

        languagePreference = LanguagePreference.getLanguagePreference((this));
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();


        LogUtil.showLog("MUVI", "Device_Name=" + deviceName);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        playerModel=new Player();
        playerModel.setIsstreaming_restricted(Util.getStreamingRestriction(languagePreference));

        if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                .trim()).equals("1")) {
            mActionBarToolbar.setNavigationIcon(null);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            LogUtil.showLog("MUVI", "Called");
        } else {
            LogUtil.showLog("MUVI", "Called============" + (languagePreference.getTextofLanguage( IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)));
            mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        }

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.light_fonts),editEmailStr);

        editEmailStr.setHint(languagePreference.getTextofLanguage( TEXT_EMIAL, DEFAULT_TEXT_EMIAL));
      /*  editEmailStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    editEmailStr.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#458ccc")));
                } else {
                    editEmailStr.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#737373")));
                }

            }
        });*/

        editPasswordStr = (EditText) findViewById(R.id.editPasswordStr);

       /* editPasswordStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (hasFocus) {

                    editPasswordStr.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#458ccc")));
                } else {
                    editPasswordStr.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#737373")));
                }

            }
        });*/


        /*LogUtil.showLog("MUVI","is Apv = "+ Util.dataModel.getIsAPV());
        LogUtil.showLog("MUVI","is Apv = "+ Util.dataModel.getIsPPV());
        LogUtil.showLog("MUVI","is Apv = "+ Util.dataModel.getContentTypesId());*/

        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.light_fonts),editPasswordStr);


        editPasswordStr.setHint(languagePreference.getTextofLanguage( TEXT_PASSWORD, DEFAULT_TEXT_PASSWORD));
        forgotPassword = (TextView) findViewById(R.id.forgotPasswordTextView);
        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.light_fonts),forgotPassword);
        forgotPassword.setText(languagePreference.getTextofLanguage( FORGOT_PASSWORD, DEFAULT_FORGOT_PASSWORD));
        loginNewUser = (TextView) findViewById(R.id.loginNewUser);
        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.light_fonts),loginNewUser);

        loginNewUser.setText(languagePreference.getTextofLanguage( NEW_HERE_TITLE, DEFAULT_NEW_HERE_TITLE));

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.light_fonts),signUpTextView);

        signUpTextView.setText(languagePreference.getTextofLanguage( SIGN_UP_TITLE, DEFAULT_SIGN_UP_TITLE));


        loginButton = (Button) findViewById(R.id.loginButton);
        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.regular_fonts),loginButton);

        loginButton.setText(languagePreference.getTextofLanguage( LOGIN, DEFAULT_LOGIN));
        loginWithFacebookButton = (LoginButton) findViewById(R.id.loginWithFacebookButton);
        loginWithFacebookButton.setVisibility(View.GONE);



       /* Toolbar mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/


        preferenceManager = PreferenceManager.getPreferenceManager(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                loginButtonClicked();

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(detailsIntent);
                finish();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                /****rating ******/

                if (getIntent().getStringExtra("from")!=null){
                    detailsIntent.putExtra("from", getIntent().getStringExtra("from"));
                }
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailsIntent);
                onBackPressed();
            }
        });

        PlanId = (languagePreference.getTextofLanguage( PLAN_ID, DEFAULT_PLAN_ID)).trim();
      /*  callbackManager=CallbackManager.Factory.create();

        loginWithFacebookButton.setReadPermissions("public_profile", "email", "user_friends");

        btnLogin= (LinearLayout) findViewById(R.id.btnLogin);*/
        TextView fbLoginTextView = (TextView) findViewById(R.id.fbLoginTextView);
        FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.regular_fonts),fbLoginTextView);

     /*   btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressBarHandler(LoginActivity.this);
                progressDialog.show();
              *//* progressDialog = new ProgressDialog(LoginActivity.this);

                progressDialog.setMessage("Loading...");
                progressDialog.show();
*//*
                loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                if (AccessToken.getCurrentAccessToken() ==null) {

                    loginWithFacebookButton.registerCallback(callbackManager, mCallBack);
                }

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });
*/
       /* loginWithFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                if (token != null) {
                }
                onFblogin();
            }
        });*/

      /*  loginNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(LoginActivity.this, PurchaseHistoryActivity.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailsIntent);
            }
        });*/


        callbackManager = CallbackManager.Factory.create();


        loginWithFacebookButton.setReadPermissions("public_profile", "email", "user_friends");

        btnLogin = (LinearLayout) findViewById(R.id.btnLogin);
        btnLogin.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                progressDialog = new ProgressDialog(LoginActivity.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();

                loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                loginWithFacebookButton.registerCallback(callbackManager, mCallBack);

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });



        //-----------------------google signin--------------//

        loginHandler =new LoginHandler(this);
        loginHandler.callSignin();
       /* RelativeLayout GoogleSignView = (RelativeLayout) findViewById(R.id.sign_in_button);
        GoogleSignView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });*/

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_ids))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //-----------------------end--------------------------//


            /*chromecast-------------------------------------*/

        mAquery = new AQuery(this);

        // setupControlsCallbacks();
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        boolean shouldStartPlayback = false;
        int startPosition = 0;

         /*   MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movieName.getText().toString());
            movieMetadata.putString(MediaMetadata.KEY_TITLE,  movieName.getText().toString());
            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
            movieMetadata.addImage(new WebImage(Uri.parse(posterImageId.trim())));
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject();
                jsonObj.put("description", movieName.getText().toString());
            } catch (JSONException e) {
                Log.e(TAG, "Failed to add description to the json object", e);
            }

            mediaInfo = new MediaInfo.Builder(castVideoUrl.trim())
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("videos/mp4")
                    .setMetadata(movieMetadata)
                    .setStreamDuration(15 * 1000)
                    .setCustomData(jsonObj)
                    .build();
            mSelectedMedia = mediaInfo;*/

        // see what we need to play and where
           /* Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mSelectedMedia = getIntent().getParcelableExtra("media");
                //setupActionBar();
                boolean shouldStartPlayback = bundle.getBoolean("shouldStart");
                int startPosition = bundle.getInt("startPosition", 0);
                // mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
               // Log.d(TAG, "Setting url of the VideoView to: " + mSelectedMedia.getContentId());
                if (shouldStartPlayback) {
                    // this will be the case only if we are coming from the
                    // CastControllerActivity by disconnecting from a device
                    mPlaybackState = PlaybackState.PLAYING;
                    updatePlaybackLocation(PlaybackLocation.LOCAL);
                    updatePlayButton(mPlaybackState);
                    if (startPosition > 0) {
                        // mVideoView.seekTo(startPosition);
                    }
                    // mVideoView.start();
                    //startControllersTimer();
                } else {
                    // we should load the video but pause it
                    // and show the album art.
                    if (mCastSession != null && mCastSession.isConnected()) {
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    } else {
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                    }
                    mPlaybackState = PlaybackState.IDLE;
                    updatePlayButton(mPlaybackState);
                }
            }*/


        if (shouldStartPlayback) {
            // this will be the case only if we are coming from the
            // CastControllerActivity by disconnecting from a device
            mPlaybackState = PlaybackState.PLAYING;
            updatePlaybackLocation(PlaybackLocation.LOCAL);
            updatePlayButton(mPlaybackState);
            if (startPosition > 0) {
                // mVideoView.seekTo(startPosition);
            }
            // mVideoView.start();
            //startControllersTimer();
        } else {
            // we should load the video but pause it
            // and show the album art.
            if (mCastSession != null && mCastSession.isConnected()) {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                updatePlaybackLocation(PlaybackLocation.REMOTE);
            } else {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));

                updatePlaybackLocation(PlaybackLocation.LOCAL);
            }
            mPlaybackState = PlaybackState.IDLE;
            updatePlayButton(mPlaybackState);
        }
/***************chromecast**********************/
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        /***************chromecast**********************/
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }


        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        /***************chromecast**********************/


     /*   callbackManager=CallbackManager.Factory.create();

        loginWithFacebookButton.setReadPermissions("public_profile", "email","user_friends");

        btnLogin= (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                if (AccessToken.getCurrentAccessToken() ==null) {
                    LogUtil.showLog("MUVI","Gbb");

                    loginWithFacebookButton.registerCallback(callbackManager, mCallBack);
                }

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });*/
    }

    /* private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
         @Override
         public void onSuccess(LoginResult loginResult) {
             if (progressDialog != null && progressDialog.isShowing()) {
                 progressDialog.hide();
                 progressDialog = null;
             }
            // progressDialog.dismiss();

             // App code
             GraphRequest request = GraphRequest.newMeRequest(
                     loginResult.getAccessToken(),
                     new GraphRequest.GraphJSONObjectCallback() {
                         @Override
                         public void onCompleted(
                                 JSONObject json,
                                 GraphResponse response) {

                             try {
                                 String jsonresult = String.valueOf(json);
                                 System.out.println("JSON Result" + jsonresult);

                                *//* if ((json.has("first_name")) && json.getString("first_name").trim() != null && !json.getString("first_name").trim().isEmpty() && !json.getString("first_name").trim().equals("null") && !json.getString("first_name").trim().matches("")) {
                                    if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                        fbName = json.getString("first_name") + " " + json.getString("last_name");
                                    }
                                } else {
                                    if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                        fbName = json.getString("last_name");
                                    }
                                }*//*
                                if ((json.has("name")) && json.getString("first_name").trim() != null && !json.getString("first_name").trim().isEmpty() && !json.getString("first_name").trim().equals("null") && !json.getString("first_name").trim().matches("")) {
                                        fbName = json.getString("name");

                                }
                                if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                    fbEmail = json.getString("email");
                                } else {
                                    fbEmail = fbName + "@facebook.com";

                                }
                                if ((json.has("id")) && json.getString("id").trim() != null && !json.getString("id").trim().isEmpty() && !json.getString("id").trim().equals("null") && !json.getString("id").trim().matches("")) {
                                    fbUserId = json.getString("id");
                                }
                                //fbName = json.getString("first_name") + " "+ json.getString("last_name");
                                asynCheckFbUserDetails = new AsynCheckFbUserDetails();
                                asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            loginButton.setVisibility(View.GONE);
                            loginWithFacebookButton.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.GONE);
                            LoginManager.getInstance().logOut();
                            asynCheckFbUserDetails = new AsynCheckFbUserDetails();
                            asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
                progressDialog = null;
            }
           // progressDialog.dismiss();
           // LoginManager.getInstance().logOut();
            loginButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);

        }

        @Override
        public void onError(FacebookException e) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
                progressDialog = null;
            }
           // progressDialog.dismiss();
           // LoginManager.getInstance().logOut();
            loginButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    };*/
    public void loginButtonClicked() {

        regEmailStr = editEmailStr.getText().toString().trim();
        regPasswordStr = editPasswordStr.getText().toString().trim();


        if (NetworkStatus.getInstance().isConnected(this)) {
            if ((!regEmailStr.equals("")) && (!regPasswordStr.equals(""))) {
                boolean isValidEmail = Util.isValidMail(regEmailStr);
                if (isValidEmail == true) {

                    LogUtil.showLog("MUVI","login valid");
                    Login_input login_input = new Login_input();
                    login_input.setAuthToken(authTokenStr);
                    login_input.setEmail(regEmailStr);
                    login_input.setPassword(regPasswordStr);
                    LoginAsynTask asyncReg = new LoginAsynTask(login_input, this, this);
                    asyncReg.executeOnExecutor(threadPoolExecutor);
                } else {
                    Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( OOPS_INVALID_EMAIL, DEFAULT_OOPS_INVALID_EMAIL), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }

    }
//    private class AsynLogInDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        int statusCode;
//        String loggedInIdStr;
//        String responseStr;
//        String isSubscribedStr;
//        String loginHistoryIdStr;
//        String logout_limit_message;
//
//        JSONObject myJson = null;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//
//            String urlRouteList = Util.rootUrl().trim()+Util.loginUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("password", regPasswordStr);
//                httppost.addHeader("email", regEmailStr);
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("lang_code",languagePreference.getTextofLanguage(Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//                // Added For FCM
//
//                httppost.addHeader("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
//                httppost.addHeader("google_id", languagePreference.getTextofLanguage(Util.GOOGLE_FCM_TOKEN,Util.DEFAULT_GOOGLE_FCM_TOKEN));
//                httppost.addHeader("device_type","1");
//
//
//
//           /*     try {
//                    httppost.setEntity(new UrlEncodedFormEntity(cred, "UTF-8"));
//                }
//
//                catch (UnsupportedEncodingException e) {
//                    statusCode = 0;
//                    e.printStackTrace();
//                }*/
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                    LogUtil.showLog("MUVI2","google_id="+languagePreference.getTextofLanguage(Util.GOOGLE_FCM_TOKEN,Util.DEFAULT_GOOGLE_FCM_TOKEN));
//                    LogUtil.showLog("MUVI2","responseStr="+responseStr);
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            statusCode = 0;
//                            //Crouton.showText(ShowWithEpisodesListActivity.this, "Slow Internet Connection", Style.INFO);
//                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    statusCode = 0;
//
//                    e.printStackTrace();
//                }
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//
//                    //userIdStr = myJson.optString("status");
//                    loggedInIdStr = myJson.optString("id");
//                    isSubscribedStr = myJson.optString("isSubscribed");
//                    UniversalIsSubscribed = isSubscribedStr;
//                    loginHistoryIdStr = myJson.optString("login_history_id");
//                    logout_limit_message = myJson.optString("msg");
//
//
//                }
//
//            }
//            catch (Exception e) {
//                statusCode = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            if(responseStr == null){
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    statusCode = 0;
//
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setMessage(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//            if (statusCode > 0) {
//
//                SharedPreferences.Editor editor = pref.edit();
//
//                if (statusCode == 200){
//                    String displayNameStr = myJson.optString("display_name");
//                    String emailFromApiStr = myJson.optString("email");
//                    String profileImageStr = myJson.optString("profile_image");
//
//                    editor.putString("PREFS_LOGGEDIN_KEY","1");
//                    editor.putString("PREFS_LOGGEDIN_ID_KEY",loggedInIdStr);
//                    editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY","");
//                    editor.putString("PREFS_LOGIN_EMAIL_ID_KEY", emailFromApiStr);
//                    editor.putString("PREFS_LOGIN_DISPLAY_NAME_KEY", displayNameStr);
//                    editor.putString("PREFS_LOGIN_PROFILE_IMAGE_KEY", profileImageStr);
//                    // editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY",loginPasswordEditText.getText().toString().trim());
//                    editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY",isSubscribedStr);
//                    editor.putString("PREFS_LOGIN_HISTORYID_KEY",loginHistoryIdStr);
//
//
//                    Date todayDate = new Date();
//                    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
//                    editor.putString("date", todayStr.trim());
//                    editor.commit();
//
//
//                LogUtil.showLog("MUVI","planId="+planId+" ,isSubscribedStr="+isSubscribedStr);
//
//                    if (Util.checkNetwork(LoginActivity.this) == true) {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//
//
//                        if(languagePreference.getTextofLanguage(Util.IS_RESTRICT_DEVICE,Util.DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1"))
//                        {
//
//                            LogUtil.showLog("MUVI","isRestrictDevice called");
//                            // Call For Check Api.
//                            AsynCheckDevice asynCheckDevice = new AsynCheckDevice();
//                            asynCheckDevice.executeOnExecutor(threadPoolExecutor);
//                        }
//                        else {
//
//                            if (Util.check_for_subscription == 1) {
//                                //go to subscription page
//                                if (Util.checkNetwork(LoginActivity.this) == true) {
//                                    if (Util.dataModel.getIsFreeContent() == 1) {
//                                        asynLoadVideoUrls = new AsynLoadVideoUrls();
//                                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                                    } else {
//                                        asynValidateUserDetails = new AsynValidateUserDetails();
//                                        asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
//
//                                    }
//                                } else {
//                                    Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                                }
//
//                            } else {
//                                if (planId.equals("1") && isSubscribedStr.equals("0")) {
//                                    Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(0, 0);
//                                } else {
//                                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
//                                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(in);
//                                    finish();
//                                }
//                            }
//                        }
//
//                    } else {
//                        Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                    }
//                }
//                else if(statusCode == 300) {
//                    // Show Popup For the Simultaneous Logout
//
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//
//                    }
//                    show_logout_popup(logout_limit_message);
//                }
//
//                else{
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        statusCode = 0;
//                    }
//
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.EMAIL_PASSWORD_INVALID, Util.DEFAULT_EMAIL_PASSWORD_INVALID));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//            }else{
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//
//                }
//
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//        }
//    }


//    private class AsynValidateUserDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//
//        int status;
//        String validUserStr;
//        String userMessage;
//        String responseStr;
//        String loggedInIdStr;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            if (pref != null) {
//                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
//            }
//
//
//
//            String urlRouteList = Util.rootUrl().trim()+Util.userValidationUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("user_id", loggedInIdStr.trim());
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("movie_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("purchase_type", Util.dataModel.getPurchase_type());
//                httppost.addHeader("season_id", Util.dataModel.getSeason_id());
//                httppost.addHeader("episode_id", Util.dataModel.getEpisode_id());
//                SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
//             /*   if (countryPref != null) {
//                    String countryCodeStr = countryPref.getString("countryCode", null);
//                    httppost.addHeader("country", countryCodeStr);
//                }else{
//                    httppost.addHeader("country", "IN");
//
//                }    */
//
//                httppost.addHeader("lang_code",languagePreference.getTextofLanguage(Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    StringBuilder sb = new StringBuilder();
//
//                    BufferedReader reader =
//                            new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
//                    String line = null;
//
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//
//                    responseStr = sb.toString();
//
//
//                } catch (final org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            status = 0;
//                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    status = 0;
//
//                    e.printStackTrace();
//                }
//                if(responseStr!=null){
//                    JSONObject myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                    validUserStr = myJson.optString("status");
//                    userMessage = myJson.optString("msg");
//
//                }
//
//            }
//            catch (Exception e) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                status = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//
//            String Subscription_Str = pref.getString("PREFS_LOGIN_ISSUBSCRIBED_KEY", "0");
//
//
//            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//            } catch (IllegalArgumentException ex) {
//                status = 0;
//            }
//
//            if (responseStr == null) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
//                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(in);
//                                finish();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else if (status <= 0) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
//                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(in);
//                                finish();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//            if (status > 0) {
//                if (status == 427) {
//
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
//                    if (userMessage != null && userMessage.equalsIgnoreCase("")) {
//                        dlgAlert.setMessage(userMessage);
//                    } else {
//                        dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
//
//                    }
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    onBackPressed();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (status == 429) {
//
//                    if (validUserStr != null) {
//                        try {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                        } catch (IllegalArgumentException ex) {
//                            status = 0;
//                        }
//
//                        if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK")))
//                        {
//                            if (Util.checkNetwork(LoginActivity.this) == true) {
//                                asynLoadVideoUrls = new AsynLoadVideoUrls();
//                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                            } else {
//                                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                                onBackPressed();
//                            }
//                        }
//                        else {
//
//                            if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid")))
//                            {
//                                if(Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1)
//                                {
//                                    if(Util.dataModel.getContentTypesId() == 3)
//                                    {
//                                        // Show Popup
//                                        ShowPpvPopUp();
//                                    }
//                                    else
//                                    {
//                                        // Go to ppv Payment
//                                        payment_for_single_part();
//                                    }
//                                }
//                                else if(planId.equals("1") && Subscription_Str.equals("0"))
//                                {
//                                    Intent intent = new Intent(LoginActivity.this,SubscriptionActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(0,0);
//                                }
//                                else
//                                {
//                                    if(Util.dataModel.getContentTypesId() == 3)
//                                    {
//                                        // Show Popup
//                                        ShowPpvPopUp();
//                                    }
//                                    else
//                                    {
//                                        // Go to ppv Payment
//                                        payment_for_single_part();
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//
//                }
//                else if(Util.dataModel.getIsAPV() == 1 || Util.dataModel.getIsPPV() == 1)
//                {
//                    if(Util.dataModel.getContentTypesId() == 3)
//                    {
//                        // Show Popup
//                        ShowPpvPopUp();
//                    }
//                    else
//                    {
//                        // Go to ppv Payment
//                        payment_for_single_part();
//                    }
//                }
//                else if(planId.equals("1") && Subscription_Str.equals("0"))
//                {
//                    Intent intent = new Intent(LoginActivity.this,SubscriptionActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//                    finish();
//                    overridePendingTransition(0,0);
//                }
//                else if(Util.dataModel.getIsConverted() == 0)
//                {
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    onBackPressed();
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//                else
//                {
//                    if (Util.checkNetwork(LoginActivity.this) == true) {
//                        asynLoadVideoUrls = new AsynLoadVideoUrls();
//                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                    } else {
//                        Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                        onBackPressed();
//                    }
//                }
//
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//
//        }
//
//
//    }

//    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        String responseStr;
//        int statusCode;
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.loadVideoUrl.trim());
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
//                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
//                httppost.addHeader("user_id",pref.getString("PREFS_LOGGEDIN_ID_KEY", null));
//
//                // Execute HTTP Post Request
//                try {
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseStr = "0";
//                            Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                }catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    e.printStackTrace();
//                }
//
//                /**** subtitles************/
//                JSONArray SubtitleJosnArray = null;
//                JSONArray ResolutionJosnArray = null;
//                JSONObject myJson =null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    SubtitleJosnArray = myJson.optJSONArray("subTitle");
//                    ResolutionJosnArray = myJson.optJSONArray("videoDetails");
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//                /**** subtitles************/
//              /*  JSONObject myJson =null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }*/
//
//                if (statusCode >= 0) {
//                    if (statusCode == 200) {
//                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
//
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                            }
//                        }else{
//                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                            }
//                        }
//                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
//                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));
//
//                        }
//                        if ((myJson.has("played_length")) && myJson.getString("played_length").trim() != null && !myJson.getString("played_length").trim().isEmpty() && !myJson.getString("played_length").trim().equals("null") && !myJson.getString("played_length").trim().matches("")) {
//                            Util.dataModel.setPlayPos(Util.isDouble(myJson.getString("played_length")));
//
//
//
//                        }
//                        /**** subtitles************/
//                        if(SubtitleJosnArray!=null)
//                        {
//                            if(SubtitleJosnArray.length()>0)
//                            {
//                                for(int i=0;i<SubtitleJosnArray.length();i++)
//                                {
//                                    SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
//                                    FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());
//
//                                }
//                            }
//                        }
//                        /**** subtitles************/
//                        if(ResolutionJosnArray!=null)
//                        {
//                            if(ResolutionJosnArray.length()>0)
//                            {
//                                for(int i=0;i<ResolutionJosnArray.length();i++)
//                                {authTokenStr
//                                    if((ResolutionJosnArray.getJSONObject(i).optString("resolution").trim()).equals("BEST"))
//                                    {
//                                        ResolutionFormat.add(ResolutionJosnArray.getJSONObject(i).optString("resolution").trim());
//                                    }
//                                    else
//                                    {
//                                        ResolutionFormat.add((ResolutionJosnArray.getJSONObject(i).optString("resolution").trim())+"p");
//                                    }
//
//                                    ResolutionUrl.add(ResolutionJosnArray.getJSONObject(i).optString("url").trim());
//
//                                }
//                            }
//                        }
//
//
//                    }
//authTokenStr
//                }
//                else {
//
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                }
//            } catch (JSONException e1) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
//                e1.printStackTrace();
//            }
//
//            catch (Exception e)
//            {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, Util.DEFAULT_NO_DATA));
//
//                e.printStackTrace();
//
//            }
//            return null;
//
//        }
//
//        protected void onPostExecute(Void result) {
//
//      /*  try{
//            if(pDialog.isShowing())
//                pDialog.dismiss();
//        }
//        catch(IllegalArgumentException ex)
//        {
//            responseStr = "0";
//            movieVideoUrlStr = languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA);
//        }*/
//            if (responseStr == null) {
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA);
//            }
//
//            if ((responseStr.trim().equalsIgnoreCase("0"))) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    // movieThirdPartyUrl = languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA);
//                }
//                Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA);
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                finish();
//                                overridePendingTransition(0,0);
//                            }
//                        });
//                dlgAlert.create().show();
//            } else {
//
//                if (Util.dataModel.getVideoUrl() == null) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    finish();
//                                    overridePendingTransition(0,0);
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    finish();
//                                    overridePendingTransition(0,0);
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(languagePreference.getTextofLanguage( Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                        if (mCastSession != null && mCastSession.isConnected()) {
//
//
//                            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
//
//                            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, Util.dataModel.getVideoStory());
//                            movieMetadata.putString(MediaMetadata.KEY_TITLE, Util.dataModel.getVideoTitle());
//                            movieMetadata.addImage(new WebImage(Uri.parse(Util.dataModel.getPosterImageId())));
//                            movieMetadata.addImage(new WebImage(Uri.parse(Util.dataModel.getPosterImageId())));
//                            JSONObject jsonObj = null;
//                            try {
//                                jsonObj = new JSONObject();
//                                jsonObj.put("description", Util.dataModel.getVideoTitle());
//                            } catch (JSONException e) {
//                            }
//
//                            mediaInfo = new MediaInfo.Builder(Util.dataModel.getVideoUrl().trim())
//                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//                                    .setContentType("videos/mp4")
//                                    .setMetadata(movieMetadata)
//                                    .setStreamDuration(15 * 1000)
//                                    .setCustomData(jsonObj)
//                                    .build();
//                            mSelectedMedia = mediaInfo;
//
//
//                            togglePlayback();
//                            removeFocusFromViews();
//                            finish();
//                            overridePendingTransition(0, 0);
//                        }else {
//
//                            final Intent playVideoIntent;
//
//                            if(Util.goToLibraryplayer)
//                            {
//                                playVideoIntent = new Intent(LoginActivity.this, MyLibraryPlayer.class);
//                            }
//                            else
//                            {
//                                playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);
//                            }
//                            /**subtitle**/
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    if(FakeSubTitlePath.size()>0)
//                                    {
//                                        // This Portion Will Be changed Later.
//
//                                        File dir = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
//                                        if (dir.isDirectory())
//                                        {
//                                            String[] children = dir.list();
//                                            for (int i = 0; i < children.length; i++)
//                                            {
//                                                new File(dir, children[i]).delete();
//                                            }
//                                        }
//
//                                        progressBarHandler = new ProgressBarHandler(LoginActivity.this);
//                                        progressBarHandler.show();
//                                        Download_SubTitle(FakeSubTitlePath.get(0).trim());
//                                    }
//                                    else
//                                    {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        playVideoIntent.putExtra("SubTitleName", SubTitleName);
//                                        playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
//                                        playVideoIntent.putExtra("ResolutionFormat",ResolutionFormat);
//                                        playVideoIntent.putExtra("ResolutionUrl",ResolutionUrl);
//                                        startActivity(playVideoIntent);
//                                        removeFocusFromViews();
//
//                                        finish();
//                                        overridePendingTransition(0, 0);
//                                    }
//
//                                }
//                            });
//                        }
//                          /*
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    removeFocusFromViews();
//                                    startActivity(playVideoIntent);
//                                    finish();
//                                    overridePendingTransition(0, 0);
//
//                                }
//                            });*/
//
//                    }else{
//                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")){
//                            if(Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
//                                final Intent playVideoIntent = new Intent(LoginActivity.this, ThirdPartyPlayer.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        removeFocusFromViews();
//                                        startActivity(playVideoIntent);
//                                        finish();
//                                        overridePendingTransition(0,0);
//
//                                    }
//                                });
//                            }else{
//
//                                final Intent playVideoIntent = new Intent(LoginActivity.this, YouTubeAPIActivity.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        removeFocusFromViews();
//                                        startActivity(playVideoIntent);
//                                        finish();
//                                        overridePendingTransition(0,0);
//
//                                    }
//                                });
//
//                            }
//                        }else{
//                            final Intent playVideoIntent = new Intent(LoginActivity.this, ThirdPartyPlayer.class);
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    removeFocusFromViews();
//                                    startActivity(playVideoIntent);
//                                    finish();
//                                    overridePendingTransition(0,0);
//                                }
//                            });
//                        }
//                    }
//                }
//
//
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            SubTitleName.clear();
//            SubTitlePath.clear();
//            ResolutionUrl.clear();
//            ResolutionFormat.clear();
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//
//        }
//
//
//    }

    @Override
    public void onBackPressed() {
//        if (asynValidateUserDetails!=null){
//            asynValidateUserDetails.cancel(true);
//        }
        if (asynCheckFbUserDetails != null) {
            asynCheckFbUserDetails.cancel(true);
        }
        if (asynFbRegDetails != null) {
            asynFbRegDetails.cancel(true);
        }
        if (asynLoadVideoUrls != null) {
            asynLoadVideoUrls.cancel(true);
        }
//        if (asyncReg!=null){
//            asyncReg.cancel(true);
//        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    public void removeFocusFromViews() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /*private void onFblogin()
    {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            loginButton.setVisibility(View.GONE);
                                            loginWithFacebookButton.setVisibility(View.GONE);
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                if ((json.has("first_name")) && json.getString("first_name").trim() != null && !json.getString("first_name").trim().isEmpty() && !json.getString("first_name").trim().equals("null") && !json.getString("first_name").trim().matches("")) {
                                                    if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                                        fbName = json.getString("first_name") + " " + json.getString("last_name");
                                                    }
                                                } else {
                                                    if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                                        fbName = json.getString("last_name");
                                                    }
                                                }
                                                if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                                    fbEmail = json.getString("email");
                                                } else {
                                                    fbEmail = fbName + "@facebook.com";

                                                }
                                                fbUserId = json.getString("id");
                                                //fbName = json.getString("first_name") + " "+ json.getString("last_name");
                                                AsynCheckFbUserDetails asynCheckFbUserDetails = new AsynCheckFbUserDetails();
                                                asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }*/

  /*  private class AsynCheckFbUserDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;

        int status;
        String responseStr;
        int isNewUserStr = 1;
        JSONObject myJson = null;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.fbUserExistsUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("fb_userid",fbUserId.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){

                    status = 0;

                } catch (IOException e) {
                    status = 0;
                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    isNewUserStr = Integer.parseInt(myJson.optString("is_newuser"));
                }

            }
            catch (Exception e) {
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {

         *//*   if (isNewUserStr == 1) {


            }*//*
            if(responseStr == null){
                status = 0;

            }
            if (status == 0) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_RECORD, Util.DEFAULT_NO_RECORD));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (status == 200){
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                   *//* if (isNewUserStr == 1){

                        AsynFbRegDetails asynFbRegDetails = new AsynFbRegDetails();
                        asynFbRegDetails.executeOnExecutor(threadPoolExecutor);

                    }else {
                        AsynFbRegDetails asynFbRegDetails = new AsynFbRegDetails();
                        asynFbRegDetails.executeOnExecutor(threadPoolExecutor);
                    }*//*
                asynFbRegDetails = new AsynFbRegDetails();
                asynFbRegDetails.executeOnExecutor(threadPoolExecutor);


            }else{
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_RECORD, Util.DEFAULT_NO_RECORD));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }



        }

        @Override
        protected void onPreExecute() {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if(accessToken != null){
                LoginManager.getInstance().logOut();
            }
            pDialog = new ProgressBarHandler(LoginActivity.this);
            pDialog.show();

        }
    }

    private class AsynFbRegDetails extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pDialog;
        ProgressBarHandler pDialog;
        int statusCode;
        String loggedInIdStr;
        String responseStr;
        String isSubscribedStr;
        String loginHistoryIdStr;

        JSONObject myJson = null;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.fbRegUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("name", fbName.trim());
                httppost.addHeader("email", fbEmail.trim());
                httppost.addHeader("password","");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("fb_userid", fbUserId.trim());
                httppost.addHeader("lang_code",languagePreference.getTextofLanguage( Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusCode = 0;
                            //Crouton.showText(ShowWithEpisodesListActivity.this, "Slow Internet Connection", Style.INFO);
                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    statusCode = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    loggedInIdStr = myJson.optString("id");
                    isSubscribedStr = myJson.optString("isSubscribed");
                    if (myJson.has("login_history_id")) {
                        loginHistoryIdStr = myJson.optString("login_history_id");
                    }


                }

            }
            catch (Exception e) {
                statusCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    statusCode = 0;

                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_RECORD, Util.DEFAULT_NO_RECORD));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (statusCode > 0) {

                SharedPreferences.Editor editor = pref.edit();

                if (statusCode == 200){
                    String displayNameStr = myJson.optString("display_name");
                    String emailFromApiStr = myJson.optString("email");
                    String profileImageStr = myJson.optString("profile_image");

                    editor.putString("PREFS_LOGGEDIN_KEY","1");
                    editor.putString("PREFS_LOGGEDIN_ID_KEY",loggedInIdStr);
                    editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY","");
                    editor.putString("PREFS_LOGIN_EMAIL_ID_KEY", emailFromApiStr);
                    editor.putString("PREFS_LOGIN_DISPLAY_NAME_KEY", displayNameStr);
                    editor.putString("PREFS_LOGIN_PROFILE_IMAGE_KEY", profileImageStr);
                    editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY",isSubscribedStr);
                    editor.putString("PREFS_LOGIN_HISTORYID_KEY",loginHistoryIdStr);


                    Date todayDate = new Date();
                    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
                    editor.putString("date", todayStr.trim());
                    editor.commit();



                    if (Util.checkNetwork(LoginActivity.this) == true) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                        //load video urls according to resolution


                        if(Util.check_for_subscription == 1)
                        {
                            //go to subscription page
                            if (Util.checkNetwork(LoginActivity.this) == true) {
                                if (Util.dataModel.getIsFreeContent() == 1) {
                                    asynLoadVideoUrls = new AsynLoadVideoUrls();
                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                } else {
                                    asynValidateUserDetails = new AsynValidateUserDetails();
                                    asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }

                        }
                        else
                        {

                            Intent in=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(in);
                            onBackPressed();
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage(Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }


                }else{
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        statusCode = 0;

                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.EMAIL_PASSWORD_INVALID, Util.DEFAULT_EMAIL_PASSWORD_INVALID));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }
            }else{
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {

                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.NO_RECORD, Util.DEFAULT_NO_RECORD));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(LoginActivity.this);
            pDialog.show();

        }
    }*/

    /*****************chromecvast*-------------------------------------*/

    private void updateMetadata(boolean visible) {
        Point displaySize;
        if (!visible) {
            /*mDescriptionView.setVisibility(View.GONE);
            mTitleView.setVisibility(View.GONE);
            mAuthorView.setVisibility(View.GONE);*/
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    displaySize.y + getSupportActionBar().getHeight());
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
        } else {
            //MediaMetadata mm = mSelectedMedia.getMetadata();
          /*  mDescriptionView.setText(mSelectedMedia.getCustomData().optString(
                    VideoProvider.KEY_DESCRIPTION));
            //mTitleView.setText(mm.getString(MediaMetadata.KEY_TITLE));
            //mAuthorView.setText(mm.getString(MediaMetadata.KEY_SUBTITLE));
            mDescriptionView.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.VISIBLE);
            mAuthorView.setVisibility(View.VISIBLE);*/
            displaySize = Util.getDisplaySize(this);
            RelativeLayout.LayoutParams lp = new
                    RelativeLayout.LayoutParams(displaySize.x,
                    (int) (displaySize.x * mAspectRatio));
            lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
            // mVideoView.setLayoutParams(lp);
            //mVideoView.invalidate();
        }
    }


    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                mLocation = PlaybackLocation.REMOTE;
                if (null != mSelectedMedia) {

                    if (mPlaybackState == PlaybackState.PLAYING) {
                       /* mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);*/
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
/*
                    mPlayCircle.setVisibility(View.GONE);
*/

                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlayButton(PlaybackState state) {
           /* boolean isConnected = (mCastSession != null)
                    && (mCastSession.isConnected() || mCastSession.isConnecting());*/
        //mControllers.setVisibility(isConnected ? View.GONE : View.VISIBLE);

        switch (state) {
            case PLAYING:

                //mLoading.setVisibility(View.INVISIBLE);
                // mPlayPause.setVisibility(View.VISIBLE);
                //mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_pause_dark));

                break;
            case IDLE:
                if (mLocation == PlaybackLocation.LOCAL) {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                    }*/

                } else {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                    }*/
                }
                //mCon
                // trollers.setVisibility(View.GONE);
                // mCoverArt.setVisibility(View.VISIBLE);
                // mVideoView.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                //mLoading.setVisibility(View.INVISIBLE);
              /*  mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_play_dark));*/

                break;
            case BUFFERING:
                //mPlayPause.setVisibility(View.INVISIBLE);
                //mLoading.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                //setCoverArtStatus(null);
                //startControllersTimer();
            } else {
                //stopControllersTimer();

                //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            //stopControllersTimer();
            // setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            //updateControllersVisibility(false);
        }
    }


    private void togglePlayback() {
        //stopControllersTimer();
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:



                      /* mVideoView.start();
                        Log.d(TAG, "Playing locally...");
                        mPlaybackState = PlaybackState.PLAYING;
                        startControllersTimer();
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*/
                        break;

                    case REMOTE:

                        loadRemoteMedia(0, true);

                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;

                //  mVideoView.pause();
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                        //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));

                        // mPlayCircle.setVisibility(View.GONE);
                       /* mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
                        mVideoView.seekTo(0);
                        mVideoView.start();
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*/
                        break;
                    case REMOTE:
                        // mPlayCircle.setVisibility(View.VISIBLE);
                        if (mCastSession != null && mCastSession.isConnected()) {
                            // watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            loadRemoteMedia(0, true);


                            // Utils.showQueuePopup(this, mPlayCircle, mSelectedMedia);
                        } else {
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        updatePlayButton(mPlaybackState);
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {

        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {

            @Override
            public void onStatusUpdated() {

                Intent intent = new Intent(LoginActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
            }

            @Override
            public void onQueueStatusUpdated() {
            }

            @Override
            public void onPreloadStatusUpdated() {
            }

            @Override
            public void onSendingRemoteMediaRequest() {
            }
        });
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
    }

    /***************chromecast**********************/


    /***********Subtitle********/

    public void Download_SubTitle(String Url) {
        new DownloadFileFromURL().execute(Url);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;


            try {
                URL url = new URL(f_url[0]);
                String str = f_url[0];
                filename = str.substring(str.lastIndexOf("/") + 1);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File root = Environment.getExternalStorageDirectory();
                mediaStorageDir = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/", "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        LogUtil.showLog("App", "failed to create directory");
                    }
                }

                SubTitlePath.add(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");
                OutputStream output = new FileOutputStream(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                LogUtil.showLog("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {
            FakeSubTitlePath.remove(0);
            if (FakeSubTitlePath.size() > 0) {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            } else {
                if (progressBarHandler != null && progressBarHandler.isShowing()) {
                    progressBarHandler.hide();
                }
                playerModel.setSubTitlePath(SubTitlePath);
                final Intent playVideoIntent;

                if (Util.goToLibraryplayer) {
                    playVideoIntent = new Intent(LoginActivity.this, MyLibraryPlayer.class);
                } else
                {
                    if (Util.dataModel.getAdNetworkId() == 3){
                        LogUtil.showLog("responseStr","playVideoIntent"+Util.dataModel.getAdNetworkId());

                        playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);

                    }
                    else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1){
                        if (Util.dataModel.getPlayPos() <= 0) {
                            playVideoIntent = new Intent(LoginActivity.this, AdPlayerActivity.class);
                        }else{
                            playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);

                        }
                    }else{
                        playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);

                    }
                    // playVideoIntent = new Intent(LoginActivity.this, ExoPlayerActivity.class);
                }
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               /* playVideoIntent.putExtra("SubTitleName", SubTitleName);
                playVideoIntent.putExtra("SubTitlePath", SubTitlePath);
                playVideoIntent.putExtra("ResolutionFormat", ResolutionFormat);
                playVideoIntent.putExtra("ResolutionUrl", ResolutionUrl);*/
                playVideoIntent.putExtra("PlayerModel",playerModel);
                startActivity(playVideoIntent);
                removeFocusFromViews();
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }



  /*  ************facebook*******--------------/
            *
            *
            */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.v(TAG,""+result.toString());

        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

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
                                    if ((json.has("id")) && json.getString("id").trim() != null && !json.getString("id").trim().isEmpty() && !json.getString("id").trim().equals("null") && !json.getString("id").trim().matches("")) {
                                        fbUserId = json.getString("id");
                                    }
                                    loginButton.setVisibility(View.GONE);
                                    loginWithFacebookButton.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.GONE);

                                    CheckFbUserDetailsInput checkFbUserDetailsInput = new CheckFbUserDetailsInput();
                                    checkFbUserDetailsInput.setAuthToken(authTokenStr);
                                    checkFbUserDetailsInput.setFb_userid(fbUserId.trim());
                                    CheckFbUserDetailsAsyn asynCheckFbUserDetails = new CheckFbUserDetailsAsyn(checkFbUserDetailsInput, LoginActivity.this, LoginActivity.this);
                                    asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);

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
            loginButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            loginButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();

            //progressDialog.dismiss();
        }
    };

    @Override
    public void onCheckFbUserDetailsAsynPreExecuteStarted() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
        pDialog = new ProgressBarHandler(LoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onCheckFbUserDetailsAsynPostExecuteCompleted(int code) {

        if (code == 0) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
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
            SocialAuthInputModel socialAuthInputModel=new SocialAuthInputModel();
            socialAuthInputModel.setAuthToken(authTokenStr);
            socialAuthInputModel.setName(fbName.trim());
            socialAuthInputModel.setEmail(fbEmail.trim());
            socialAuthInputModel.setPassword("");
            socialAuthInputModel.setFb_userid(fbUserId.trim());
            socialAuthInputModel.setLanguage(languagePreference.getTextofLanguage( SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            asynFbRegDetails = new SocialAuthAsynTask(socialAuthInputModel,this,this);
            asynFbRegDetails.executeOnExecutor(threadPoolExecutor);


        } else {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }

    }


//    private class AsynCheckFbUserDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//
//        int status;
//        String responseStr;
//        int isNewUserStr = 1;
//        JSONObject myJson = null;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.fbUserExistsUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("fb_userid", fbUserId.trim());
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//
//                    status = 0;
//
//                } catch (IOException e) {
//                    status = 0;
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                    isNewUserStr = Integer.parseInt(myJson.optString("is_newuser"));
//                }
//
//            } catch (Exception e) {
//                status = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//
//            if (responseStr == null) {
//                status = 0;
//
//            }
//            if (status == 0) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK));
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//            if (status == 200) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//
//                asynFbRegDetails = new AsynFbRegDetails();
//                asynFbRegDetails.executeOnExecutor(threadPoolExecutor);
//
//
//            } else {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK));
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            AccessToken accessToken = AccessToken.getCurrentAccessToken();
//            if (accessToken != null) {
//                LoginManager.getInstance().logOut();
//            }
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//
//        }
//    }

    @Override
    public void onSocialAuthPreExecuteStarted() {

        pDialog = new ProgressBarHandler(LoginActivity.this);
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
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
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
                preferenceManager.setUserIdToPref(loggedInIdStr);
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
                    //load video urls according to resolution


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
                                VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, LoginActivity.this, LoginActivity.this);
                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                            } else {
                                ValidateUserInput validateUserInput = new ValidateUserInput();
                                validateUserInput.setAuthToken(authTokenStr);
                                validateUserInput.setUserId(validateUserInput.getUserId());
                                validateUserInput.setMuviUniqueId(validateUserInput.getMuviUniqueId());
                                validateUserInput.setEpisodeStreamUniqueId(validateUserInput.getEpisodeStreamUniqueId());
                                validateUserInput.setSeasonId(validateUserInput.getSeasonId());
                                validateUserInput.setLanguageCode(validateUserInput.getLanguageCode());
                                validateUserInput.setPurchaseType(validateUserInput.getPurchaseType());
                                GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, LoginActivity.this, LoginActivity.this);
                                asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }

                    } else {

                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(in);

                        onBackPressed();
                    }


                } else {
                    Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage( EMAIL_PASSWORD_INVALID, DEFAULT_EMAIL_PASSWORD_INVALID));
                dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
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
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage( DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        }

    }

//    private class AsynFbRegDetails extends AsyncTask<Void, Void, Void> {
//        // ProgressDialog pDialog;
//        ProgressBarHandler pDialog;
//        int statusCode;
//        String loggedInIdStr;
//        String responseStr;
//        String isSubscribedStr;
//        String loginHistoryIdStr;
//
//        JSONObject myJson = null;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim() + Util.fbRegUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("name", fbName.trim());
//                httppost.addHeader("email", fbEmail.trim());
//                httppost.addHeader("password", "");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("fb_userid", fbUserId.trim());
//                httppost.addHeader("lang_code", languagePreference.getTextofLanguage( Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            statusCode = 0;
//                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    statusCode = 0;
//
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                    loggedInIdStr = myJson.optString("id");
//                    isSubscribedStr = myJson.optString("isSubscribed");
//                    UniversalIsSubscribed = isSubscribedStr;
//                    if (myJson.has("login_history_id")) {
//                        loginHistoryIdStr = myJson.optString("login_history_id");
//                    }
//
//
//                }
//
//            } catch (Exception e) {
//                statusCode = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            if (responseStr == null) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    statusCode = 0;
//
//                }
//                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK));
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//            if (statusCode > 0) {
//
//                //    SharedPreferences.Editor editor = pref.edit();
//
//                if (statusCode == 200) {
//                    String displayNameStr = myJson.optString("display_name");
//                    String emailFromApiStr = myJson.optString("email");
//                    String profileImageStr = myJson.optString("profile_image");
//
//                    preferenceManager.setLogInStatusToPref("1");
//                    preferenceManager.setUserIdToPref(loggedInIdStr);
//                    preferenceManager.setPwdToPref("");
//                    preferenceManager.setEmailIdToPref(emailFromApiStr);
//                    preferenceManager.setDispNameToPref(displayNameStr);
//                    preferenceManager.setLoginProfImgoPref(profileImageStr);
//                    preferenceManager.setIsSubscribedToPref(isSubscribedStr);
//                    preferenceManager.setLoginHistIdPref(loginHistoryIdStr);
//
//
//                    if (Util.checkNetwork(LoginActivity.this) == true) {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                        //load video urls according to resolution
//
//
//                        if (Util.check_for_subscription == 1) {
//                            //go to subscription page
//                            if (Util.checkNetwork(LoginActivity.this) == true) {
//                                if (Util.dataModel.getIsFreeContent() == 1) {
//                                    GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
//                                    getVideoDetailsInput.setAuthToken(Util.authTokenStr);
//                                    getVideoDetailsInput.setContent_uniq_id(getVideoDetailsInput.getContent_uniq_id());
//                                    getVideoDetailsInput.setStream_uniq_id(getVideoDetailsInput.getStream_uniq_id());
//                                    getVideoDetailsInput.setInternetSpeed(getVideoDetailsInput.getInternetSpeed());
//                                    getVideoDetailsInput.setUser_id(getVideoDetailsInput.getUser_id());
//                                    VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, LoginActivity.this, LoginActivity.this);
//                                    asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                                } else {
//                                    ValidateUserInput validateUserInput = new ValidateUserInput();
//                                    validateUserInput.setAuthToken(Util.authTokenStr);
//                                    validateUserInput.setUserId(validateUserInput.getUserId());
//                                    validateUserInput.setMuviUniqueId(validateUserInput.getMuviUniqueId());
//                                    validateUserInput.setEpisodeStreamUniqueId(validateUserInput.getEpisodeStreamUniqueId());
//                                    validateUserInput.setSeasonId(validateUserInput.getSeasonId());
//                                    validateUserInput.setLanguageCode(validateUserInput.getLanguageCode());
//                                    validateUserInput.setPurchaseType(validateUserInput.getPurchaseType());
//                                    GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, LoginActivity.this, LoginActivity.this);
//                                    asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
//                                }
//                            } else {
//                                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                            }
//
//                        } else {
//
//                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
//                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            startActivity(in);
//
//                            onBackPressed();
//                        }
//
//
//                    } else {
//                        Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                    }
//
//
//                } else {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        statusCode = 0;
//
//                    }
//                    android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.EMAIL_PASSWORD_INVALID, Util.DEFAULT_EMAIL_PASSWORD_INVALID));
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//            } else {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//
//                }
//                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                dlgAlert.setMessage(languagePreference.getTextofLanguage( Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
//                dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage( Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//
//        }
//    }


    // Added For  FCM

    public void show_logout_popup(String msg) {
        {

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View convertView = inflater.inflate(R.layout.logout_popup, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("");

            logout_text = (TextView) convertView.findViewById(R.id.logout_text);
            ok = (Button) convertView.findViewById(R.id.ok);
            cancel = (Button) convertView.findViewById(R.id.cancel);


            // Font implemented Here//
            FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.regular_fonts),logout_text);
            FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.regular_fonts),ok);
            FontUtls.loadFont(LoginActivity.this, getResources().getString(R.string.regular_fonts),cancel);

            //==============end===============//

            // Language Implemented Here //

            logout_text.setText(msg);
            ok.setText(" " + languagePreference.getTextofLanguage( BUTTON_OK, DEFAULT_BUTTON_OK));
            cancel.setText(" " + languagePreference.getTextofLanguage( CANCEL_BUTTON, DEAFULT_CANCEL_BUTTON));

            //==============End===============//


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkStatus.getInstance().isConnected(LoginActivity.this)) {

                        // Call Api For Simultaneous Logout
                        SimultaneousLogoutInput simultaneousLogoutInput = new SimultaneousLogoutInput();
                        simultaneousLogoutInput.setAuthToken(authTokenStr);
                        simultaneousLogoutInput.setDevice_type("1");
                        simultaneousLogoutInput.setEmail_id(regEmailStr);
                        GetSimultaneousLogoutAsync asynSimultaneousLogout = new GetSimultaneousLogoutAsync(simultaneousLogoutInput, LoginActivity.this, LoginActivity.this);
                        asynSimultaneousLogout.executeOnExecutor(threadPoolExecutor);
                    } else {
                        Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
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
        progressDialog1 = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
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
            // Allow the user to login
            if (logout_alert.isShowing()) {
                logout_alert.dismiss();
            }

            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage( SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, DEFAULT_SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage( TRY_AGAIN, DEFAULT_TRY_AGAIN), Toast.LENGTH_LONG).show();
        }
    }

    // This API is called for simultaneous logout
//    private class AsynSimultaneousLogout extends AsyncTask<Void, Void, Void> {
//        ProgressDialog progressDialog1;
//        String responseStr;
//        int statusCode = 0;
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.LogoutAll.trim());
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("device_type", "1");
//                httppost.addHeader("email_id", regEmailStr);
//
//                // Execute HTTP Post Request
//                try {
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//                    LogUtil.showLog("MUVI", "Response Of the Simultaneous Logout =" + responseStr);
//
//
//                } catch (Exception e) {
//                    responseStr = "0";
//                }
//
//                JSONObject myJson = null;
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//            } catch (Exception e) {
//                responseStr = "0";
//                e.printStackTrace();
//
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Void result) {
//
//            if (progressDialog1 != null && progressDialog1.isShowing()) {
//                progressDialog1.hide();
//                progressDialog1 = null;
//            }
//            if (statusCode == 200) {
//                // Allow the user to login
//                if (logout_alert.isShowing()) {
//                    logout_alert.dismiss();
//                }
//
//                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage( Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, Util.DEFAULT_SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE), Toast.LENGTH_LONG).show();
//
//            } else {
//                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage( Util.TRY_AGAIN, Util.DEFAULT_TRY_AGAIN), Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog1 = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
//            progressDialog1.setCancelable(false);
//            progressDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
//            progressDialog1.setIndeterminate(false);
//            progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_rawable));
//            progressDialog1.show();
//        }
//
//    }


    public void ShowPpvPopUp() {

        try {
            if (Util.currencyModel.getCurrencySymbol() == null) {
                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
            finish();
        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.activity_ppv_popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        final RadioButton completeRadioButton = (RadioButton) convertView.findViewById(R.id.completeRadioButton);
        final RadioButton seasonRadioButton = (RadioButton) convertView.findViewById(R.id.seasonRadioButton);
        final RadioButton episodeRadioButton = (RadioButton) convertView.findViewById(R.id.episodeRadioButton);
        TextView episodePriceTextView = (TextView) convertView.findViewById(R.id.episodePriceTextView);
        TextView seasonPriceTextView = (TextView) convertView.findViewById(R.id.seasonPriceTextView);
        TextView completePriceTextView = (TextView) convertView.findViewById(R.id.completePriceTextView);
        Button payNowButton = (Button) convertView.findViewById(R.id.payNowButton);


        if (Util.dataModel.getIsAPV() == 1) {
            if (Util.apvModel.getIsEpisode() == 1) {
                episodeRadioButton.setVisibility(View.VISIBLE);
                episodePriceTextView.setVisibility(View.VISIBLE);
            } else {
                episodeRadioButton.setVisibility(View.GONE);
                episodePriceTextView.setVisibility(View.GONE);
            }
            if (Util.apvModel.getIsSeason() == 1) {
                seasonRadioButton.setVisibility(View.VISIBLE);
                seasonPriceTextView.setVisibility(View.VISIBLE);
            } else {
                seasonRadioButton.setVisibility(View.GONE);
                seasonPriceTextView.setVisibility(View.GONE);
            }
            if (Util.apvModel.getIsShow() == 1) {
                completeRadioButton.setVisibility(View.VISIBLE);
                completePriceTextView.setVisibility(View.VISIBLE);
            } else {
                completeRadioButton.setVisibility(View.GONE);
                completePriceTextView.setVisibility(View.GONE);
            }
        } else {
            if (Util.ppvModel.getIsEpisode() == 1) {
                episodeRadioButton.setVisibility(View.VISIBLE);
                episodePriceTextView.setVisibility(View.VISIBLE);
            } else {
                episodeRadioButton.setVisibility(View.GONE);
                episodePriceTextView.setVisibility(View.GONE);
            }
            if (Util.ppvModel.getIsSeason() == 1) {
                seasonRadioButton.setVisibility(View.VISIBLE);
                seasonPriceTextView.setVisibility(View.VISIBLE);
            } else {
                seasonRadioButton.setVisibility(View.GONE);
                seasonPriceTextView.setVisibility(View.GONE);
            }
            if (Util.ppvModel.getIsShow() == 1) {
                completeRadioButton.setVisibility(View.VISIBLE);
                completePriceTextView.setVisibility(View.VISIBLE);
            } else {
                completeRadioButton.setVisibility(View.GONE);
                completePriceTextView.setVisibility(View.GONE);
            }
        }


        completeRadioButton.setText("  " + Util.dataModel.getEpisode_title().trim() + " Complete Season ");
        seasonRadioButton.setText("  " + Util.dataModel.getEpisode_title().trim() + " Season " + Util.dataModel.getEpisode_series_no().trim() + " ");
        episodeRadioButton.setText("  " + Util.dataModel.getEpisode_title().trim() + " S" + Util.dataModel.getEpisode_series_no().trim() + " E " + Util.dataModel.getEpisode_no().trim() + " ");


        String subscriptionStr = preferenceManager.getIsSubscribedFromPref();

        if (subscriptionStr.trim().equals("1")) {
            if (Util.dataModel.getIsAPV() == 1) {

                episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeSubscribedStr());
                seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvSeasonSubscribedStr());
                completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowSubscribedStr());
            } else {
                episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeSubscribedStr());
                seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonSubscribedStr());
                completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvShowSubscribedStr());
            }
        } else {
            if (Util.dataModel.getIsAPV() == 1) {

                episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvEpisodeUnsubscribedStr());
                seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvSeasonUnsubscribedStr());
                completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.apvModel.getApvShowUnsubscribedStr());
            } else {
                episodePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvEpisodeUnsubscribedStr());
                seasonPriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvSeasonUnsubscribedStr());
                completePriceTextView.setText(Util.currencyModel.getCurrencySymbol() + " " + Util.ppvModel.getPpvShowUnsubscribedStr());
            }
        }


        alert = alertDialog.show();
        completeRadioButton.setChecked(true);


        if (completeRadioButton.isChecked() == true) {
            if (Util.dataModel.getIsAPV() == 1) {
                priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

            } else {
                priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
            }
        }


       /*
        completeRadioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                episodeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);
                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
                }

            }

        });*/


        // Changed later

        if (completeRadioButton.getVisibility() == View.VISIBLE) {

            completeRadioButton.setChecked(true);
            episodeRadioButton.setChecked(false);
            seasonRadioButton.setChecked(false);

            if (Util.dataModel.getIsAPV() == 1) {
                priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

            } else {
                priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
            }


        } else {
            if (seasonRadioButton.getVisibility() == View.VISIBLE) {

                completeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(true);
                episodeRadioButton.setChecked(false);

                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvSeasonUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvSeasonSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvSeasonUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvSeasonSubscribedStr();
                }


            } else {
                completeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);
                episodeRadioButton.setChecked(true);

                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvEpisodeUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvEpisodeSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvEpisodeUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvEpisodeSubscribedStr();
                }

            }
        }

        ///////////////////////=====================////////////////////////

        completeRadioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                episodeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);
                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvShowSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvShowUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvShowSubscribedStr();
                }

            }

        });


        episodeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeRadioButton.setChecked(false);
                seasonRadioButton.setChecked(false);

                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvEpisodeUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvEpisodeSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvEpisodeUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvEpisodeSubscribedStr();
                }

            }
        });
        seasonRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                episodeRadioButton.setChecked(false);
                completeRadioButton.setChecked(false);
                if (Util.dataModel.getIsAPV() == 1) {
                    priceForUnsubscribedStr = Util.apvModel.getApvSeasonUnsubscribedStr();
                    priceFosubscribedStr = Util.apvModel.getApvSeasonSubscribedStr();

                } else {
                    priceForUnsubscribedStr = Util.ppvModel.getPpvSeasonUnsubscribedStr();
                    priceFosubscribedStr = Util.ppvModel.getPpvSeasonSubscribedStr();
                }


            }
        });

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (completeRadioButton.isChecked()) {
                    Util.selected_episode_id = "0";
                    Util.selected_season_id = "0";
                } else if (seasonRadioButton.isChecked()) {
                    Util.selected_episode_id = "0";
                } else {
                    Util.selected_episode_id = Util.dataModel.getStreamUniqueId();
                    Util.selected_season_id = Util.dataModel.getEpisode_series_no();
                }


                alert.dismiss();
                final Intent showPaymentIntent = new Intent(LoginActivity.this, PPvPaymentInfoActivity.class);
                showPaymentIntent.putExtra("muviuniqueid", Util.dataModel.getMovieUniqueId().trim());
                showPaymentIntent.putExtra("episodeStreamId", Util.dataModel.getStreamUniqueId().trim());
                showPaymentIntent.putExtra("content_types_id", Util.dataModel.getContentTypesId());
                showPaymentIntent.putExtra("movieThirdPartyUrl", Util.dataModel.getThirdPartyUrl());
                showPaymentIntent.putExtra("planUnSubscribedPrice", priceForUnsubscribedStr);
                showPaymentIntent.putExtra("planSubscribedPrice", priceFosubscribedStr);
                showPaymentIntent.putExtra("currencyId", Util.currencyModel.getCurrencyId());
                showPaymentIntent.putExtra("currencyCountryCode", Util.currencyModel.getCurrencyCode());
                showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
                showPaymentIntent.putExtra("showName", Util.dataModel.getEpisode_title());
                showPaymentIntent.putExtra("seriesNumber", Util.dataModel.getEpisode_series_no());
                showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
                showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
                showPaymentIntent.putExtra("PlayerModel",playerModel);
                if (Util.dataModel.getIsAPV() == 1) {
                    showPaymentIntent.putExtra("isConverted", 0);
                } else {
                    showPaymentIntent.putExtra("isConverted", 1);

                }

                showPaymentIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(showPaymentIntent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
    }

    public void payment_for_single_part() {

        try {
            if (Util.currencyModel.getCurrencySymbol() == null) {
                Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_DETAILS_AVAILABLE, DEFAULT_NO_DETAILS_AVAILABLE), Toast.LENGTH_LONG).show();
            finish();
        }


        if (Util.dataModel.getIsAPV() == 1) {
            priceForUnsubscribedStr = Util.apvModel.getAPVPriceForUnsubscribedStr();
            priceFosubscribedStr = Util.apvModel.getAPVPriceForsubscribedStr();

        } else {
            priceForUnsubscribedStr = Util.ppvModel.getPPVPriceForUnsubscribedStr();
            priceFosubscribedStr = Util.ppvModel.getPPVPriceForsubscribedStr();
        }


        Util.selected_episode_id = "0";
        Util.selected_season_id = "0";

        LogUtil.showLog("MUVI", "priceFosubscribedStr=" + priceFosubscribedStr);
        LogUtil.showLog("MUVI", "priceForUnsubscribedStr=" + priceForUnsubscribedStr);

        final Intent showPaymentIntent = new Intent(LoginActivity.this, PPvPaymentInfoActivity.class);
        showPaymentIntent.putExtra("muviuniqueid", Util.dataModel.getMovieUniqueId().trim());
        showPaymentIntent.putExtra("episodeStreamId", Util.dataModel.getStreamUniqueId().trim());
        showPaymentIntent.putExtra("content_types_id", Util.dataModel.getContentTypesId());
        showPaymentIntent.putExtra("movieThirdPartyUrl", Util.dataModel.getThirdPartyUrl());
        showPaymentIntent.putExtra("planUnSubscribedPrice", priceForUnsubscribedStr);
        showPaymentIntent.putExtra("planSubscribedPrice", priceFosubscribedStr);
        showPaymentIntent.putExtra("currencyId", Util.currencyModel.getCurrencyId());
        showPaymentIntent.putExtra("currencyCountryCode", Util.currencyModel.getCurrencyCode());
        showPaymentIntent.putExtra("currencySymbol", Util.currencyModel.getCurrencySymbol());
        showPaymentIntent.putExtra("showName", Util.dataModel.getVideoTitle());
        showPaymentIntent.putExtra("isPPV", Util.dataModel.getIsPPV());
        showPaymentIntent.putExtra("isAPV", Util.dataModel.getIsAPV());
        if (Util.dataModel.getIsAPV() == 1) {
            showPaymentIntent.putExtra("isConverted", 0);
        } else {
            showPaymentIntent.putExtra("isConverted", 1);

        }

        showPaymentIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(showPaymentIntent);
        finish();
        overridePendingTransition(0, 0);
    }


    // Following Code Has Been Added For The Device Management

    @Override
    public void onCheckDevicePreExecuteStarted() {
        pDialog = new ProgressBarHandler(LoginActivity.this);
        pDialog.show();
    }

    @Override
    public void onCheckDevicePostExecuteCompleted(CheckDeviceOutput checkDeviceOutput, int code, String message) {

        if (code > 0) {
            if (code == 200) {

                // Allow The User To Login
                if (Util.check_for_subscription == 1) {
                    //go to subscription page
                    if (NetworkStatus.getInstance().isConnected(this)) {
                        if (Util.dataModel.getIsFreeContent() == 1) {
                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                            getVideoDetailsInput.setAuthToken(authTokenStr);
                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                            getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, LoginActivity.this, LoginActivity.this);
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                        } else {
                            ValidateUserInput validateUserInput = new ValidateUserInput();
                            validateUserInput.setAuthToken(authTokenStr);
                            validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                            validateUserInput.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
                            validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                            validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                            validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                            GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, LoginActivity.this, LoginActivity.this);
                            asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (PlanId.equals("1") && UniversalIsSubscribed.equals("0")) {
                        Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    } else {
                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
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


//    private class AsynCheckDevice extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//
//        int statusCode;
//        String responseStr;
//        JSONObject myJson = null;
//        String userIdStr;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            if (preferenceManager != null) {
//                userIdStr = preferenceManager.getUseridFromPref();
//            }
//
//            String urlRouteList = Util.rootUrl().trim() + Util.CheckDevice.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("user_id", userIdStr.trim());
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("device", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
//                httppost.addHeader("google_id", languagePreference.getTextofLanguage( Util.GOOGLE_FCM_TOKEN, Util.DEFAULT_GOOGLE_FCM_TOKEN));
//                httppost.addHeader("device_type", "1");
//                httppost.addHeader("lang_code", languagePreference.getTextofLanguage( Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//                httppost.addHeader("device_info", deviceName + "," + languagePreference.getTextofLanguage( Util.ANDROID_VERSION, Util.DEFAULT_ANDROID_VERSION) + " " + Build.VERSION.RELEASE);
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                    LogUtil.showLog("MUVI3", "responseStr of check device=" + responseStr);
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            statusCode = 0;
//                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    statusCode = 0;
//
//                    e.printStackTrace();
//                }
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                    UniversalErrorMessage = myJson.optString("msg");
//                }
//            } catch (Exception e) {
//                statusCode = 0;
//            }
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//            } catch (IllegalArgumentException ex) {
//                statusCode = 0;
//            }
//
//            if (responseStr == null) {
//                // Call For Logout
//                LogOut();
//            }
//
//            if (statusCode > 0) {
//                if (statusCode == 200) {
//
//                    // Allow The User To Login
//                    if (Util.check_for_subscription == 1) {
//                        //go to subscription page
//                        if (Util.checkNetwork(LoginActivity.this) == true) {
//                            if (Util.dataModel.getIsFreeContent() == 1) {
//                                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
//                                getVideoDetailsInput.setAuthToken(Util.authTokenStr);
//                                getVideoDetailsInput.setContent_uniq_id(getVideoDetailsInput.getContent_uniq_id());
//                                getVideoDetailsInput.setStream_uniq_id(getVideoDetailsInput.getStream_uniq_id());
//                                getVideoDetailsInput.setInternetSpeed(getVideoDetailsInput.getInternetSpeed());
//                                getVideoDetailsInput.setUser_id(getVideoDetailsInput.getUser_id());
//                                VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, LoginActivity.this, LoginActivity.this);
//                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//                            } else {
//                                ValidateUserInput validateUserInput = new ValidateUserInput();
//                                validateUserInput.setAuthToken(Util.authTokenStr);
//                                validateUserInput.setUserId(validateUserInput.getUserId());
//                                validateUserInput.setMuviUniqueId(validateUserInput.getMuviUniqueId());
//                                validateUserInput.setEpisodeStreamUniqueId(validateUserInput.getEpisodeStreamUniqueId());
//                                validateUserInput.setSeasonId(validateUserInput.getSeasonId());
//                                validateUserInput.setLanguageCode(validateUserInput.getLanguageCode());
//                                validateUserInput.setPurchaseType(validateUserInput.getPurchaseType());
//                                GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, LoginActivity.this, LoginActivity.this);
//                                asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
//
//                            }
//                        } else {
//                            Toast.makeText(LoginActivity.this, languagePreference.getTextofLanguage( Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        if (planId.equals("1") && UniversalIsSubscribed.equals("0")) {
//                            Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            startActivity(intent);
//                            finish();
//                            overridePendingTransition(0, 0);
//                        } else {
//                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
//                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(in);
//                            finish();
//                        }
//                    }
//                } else {
//                    // Call For Logout
//                    LogOut();
//                }
//            } else {
//                // Call For Logout
//                LogOut();
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//        }
//    }

    public void LogOut() {
        LogUtil.showLog("MUVI3", "logout Called");
        LogoutInput logoutInput = new LogoutInput();
        logoutInput.setAuthToken(authTokenStr);
        logoutInput.setLogin_history_id(logoutInput.getLogin_history_id());
        logoutInput.setLang_code(logoutInput.getLang_code());
        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, this, this);
        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);
    }

//    private class AsynLogoutDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        int responseCode;
//        String loginHistoryIdStr = pref.getString("PREFS_LOGIN_HISTORYID_KEY", null);
//        String responseStr;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            String urlRouteList =Util.rootUrl().trim()+Util.logoutUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("login_history_id",loginHistoryIdStr);
//                httppost.addHeader("lang_code",languagePreference.getTextofLanguage(Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                    LogUtil.showLog("MUVI3","responseStr of logout="+responseStr);
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseCode = 0;
//                            Toast.makeText(LoginActivity.this,languagePreference.getTextofLanguage( Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                }catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    responseCode = 0;
//                    e.printStackTrace();
//                }
//                if(responseStr!=null){
//                    JSONObject myJson = new JSONObject(responseStr);
//                    responseCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//            }
//            catch (Exception e) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseCode = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//
//                }
//            } catch (IllegalArgumentException ex) {
//                Toast.makeText(LoginActivity.this,languagePreference.getTextofLanguage( Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if(responseStr == null){
//                Toast.makeText(LoginActivity.this,languagePreference.getTextofLanguage( Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode == 0) {
//                Toast.makeText(LoginActivity.this,languagePreference.getTextofLanguage( Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//            }
//            if (responseCode > 0) {
//                if (responseCode == 200) {
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.clear();
//                    editor.commit();
//                    SharedPreferences loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
//                    if (loginPref!=null) {
//                        SharedPreferences.Editor countryEditor = loginPref.edit();
//                        countryEditor.clear();
//                        countryEditor.commit();
//                    }
//
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
//                    dlgAlert.setMessage(UniversalErrorMessage);
//                    dlgAlert.setTitle(languagePreference.getTextofLanguage( Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//
//                }
//                else {
//                    Toast.makeText(LoginActivity.this,languagePreference.getTextofLanguage( Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            pDialog = new ProgressBarHandler(LoginActivity.this);
//            pDialog.show();
//        }
//    }



    /////////Google sign in and sign out by nihar/////start//////////
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(LoginActivity.this, "Log out sucessfull", Toast.LENGTH_SHORT).show();
                    }
                });
    }
   /*
   *@onhandleSigninResult call when we recive Auth conformation from google
    * Dont remove implimentation for onDemand just use 9.4.0 not latest v because it ll affect the chromecast . by @nihar.
    */

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess()+result.getSignInAccount());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Authname = acct.getDisplayName();
            AuthEmail =   acct.getEmail();
            AuthId = acct.getId();
            AuthImageUrl = String.valueOf(acct.getPhotoUrl());

            GmailLoginInput gmailLoginInput=new GmailLoginInput();
            gmailLoginInput.setEmail(AuthEmail);
            gmailLoginInput.setName(Authname);
            gmailLoginInput.setGmail_userid(AuthId);
            gmailLoginInput.setProfile_image(AuthImageUrl);
            gmailLoginInput.setPassword("");
            gmailLoginInput.setAuthToken(authTokenStr);
            AsyncGmailReg asyncGmailReg=new AsyncGmailReg(gmailLoginInput,this,this);
            asyncGmailReg.executeOnExecutor(threadPoolExecutor);
        }
    }
    // [END handleSignInResult]

   /* private class  AsynGmailReg extends AsyncTask<Void, Void, Void> {
        int statusCode;
        String loggedInIdStr;
        String responseStr;
        String isSubscribedStr;
        String loginHistoryIdStr;
        ProgressBarHandler pDialog;

        JSONObject myJson = null;

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.fbRegUrl.trim();
//            String urlRouteList = "https://www.muvi.com/rest/socialAuth";
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("name", Authname);
                httppost.addHeader("email", AuthEmail);
                httppost.addHeader("password","");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("fb_userid", AuthId);
                httppost.addHeader("profile_image", AuthImageUrl);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v(TAG,""+responseStr);
                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusCode = 0;

                            //  Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    statusCode = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    loggedInIdStr = myJson.optString("id");
                    isSubscribedStr = myJson.optString("isSubscribed");
                    if (myJson.has("login_history_id")) {
                        loginHistoryIdStr = myJson.optString("login_history_id");
                    }


                }

            }
            catch (Exception e) {
                statusCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            if (responseStr == null) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    statusCode = 0;

                }
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(LoginActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(LoginActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(LoginActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(LoginActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (statusCode > 0) {

                SharedPreferences.Editor editor = pref.edit();

                if (statusCode == 200) {
                    String displayNameStr = myJson.optString("display_name");
                    String emailFromApiStr = myJson.optString("email");
                    String profileImageStr = myJson.optString("profile_image");

                    editor.putString("PREFS_LOGGEDIN_KEY", "1");
                    editor.putString("PREFS_LOGGEDIN_ID_KEY", loggedInIdStr);
                    editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY", "");
                    editor.putString("PREFS_LOGIN_EMAIL_ID_KEY", emailFromApiStr);
                    editor.putString("PREFS_LOGIN_DISPLAY_NAME_KEY", displayNameStr);
                    editor.putString("PREFS_LOGIN_PROFILE_IMAGE_KEY", profileImageStr);
                    editor.putString("PREFS_LOGIN_ISSUBSCRIBED_KEY", isSubscribedStr);
                    editor.putString("PREFS_LOGIN_HISTORYID_KEY", loginHistoryIdStr);

                    preferenceManager.setLogInStatusToPref("1");
                    preferenceManager.setUserIdToPref(login_output.getId());
                    preferenceManager.setPwdToPref("");
                    preferenceManager.setEmailIdToPref(login_output.getEmail());
                    preferenceManager.setDispNameToPref(login_output.getDisplay_name());
                    preferenceManager.setLoginProfImgoPref(login_output.getProfile_image());
                    preferenceManager.setIsSubscribedToPref(login_output.getIsSubscribed());
                    preferenceManager.setLoginHistIdPref(login_output.getLogin_history_id());

                    Date todayDate = new Date();
                    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
                    editor.putString("date", todayStr.trim());
                    editor.commit();


                    if (Util.checkNetwork(LoginActivity.this) == true) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                        //load video urls according to resolution
                        if (Util.getTextofLanguage(LoginActivity.this, Util.IS_RESTRICT_DEVICE, Util.DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1")) {

                            Log.v("BIBHU", "isRestrictDevice called");
                            // Call For Check Api.
                            AsynCheckDevice asynCheckDevice = new AsynCheckDevice();
                            asynCheckDevice.executeOnExecutor(threadPoolExecutor);
                        } else {
                            if (getIntent().getStringExtra("from") != null) {
                                *//** review **//*
                                onBackPressed();
                            } else {
                                if (Util.check_for_subscription == 1) {
                                    //go to subscription page
                                    if (Util.checkNetwork(LoginActivity.this) == true) {
                                        asynValidateUserDetails = new AsynValidateUserDetails();
                                        asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                                    } else {
                                        Util.showToast(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                                        // Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    if (PlanId.equals("1") && isSubscribedStr.equals("0")) {
                                        Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                        if (RegisterActivity.fa != null) {
                                            RegisterActivity.fa.finish();
                                        }
                                        if (ForgotPasswordActivity.forgotA != null) {
                                            ForgotPasswordActivity.forgotA.finish();
                                        }
                                        onBackPressed();
                                    } else {
                                        Log.v("ABC", "FBREGISTRATIONDETAILS_POST");

                                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(in);
                                        onBackPressed();
                                    }
                                }
                            }
                        }

                    } else {
                        Util.showToast(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    }


                } else {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        statusCode = 0;

                    }
                    android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(LoginActivity.this, Util.EMAIL_PASSWORD_INVALID, Util.DEFAULT_EMAIL_PASSWORD_INVALID));
                    dlgAlert.setTitle(Util.getTextofLanguage(LoginActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(LoginActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(LoginActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(LoginActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(LoginActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(LoginActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(LoginActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(LoginActivity.this);
            pDialog.show();
        }
    }*/

    /////////////////////////end/////////////////////////

    @Override
    public void onGmailRegPreExecuteStarted() {
        pDialog = new ProgressBarHandler(LoginActivity.this);
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


        if (status==200){

            preferenceManager.setLogInStatusToPref("1");
            preferenceManager.setUserIdToPref(gmailLoginOutput.getId());
            preferenceManager.setPwdToPref("");
            preferenceManager.setEmailIdToPref(gmailLoginOutput.getEmail());
            preferenceManager.setDispNameToPref(gmailLoginOutput.getDisplay_name());
            preferenceManager.setLoginProfImgoPref( gmailLoginOutput.getProfile_image());
            preferenceManager.setIsSubscribedToPref(Integer.toString(gmailLoginOutput.getIsSubscribed()));
            preferenceManager.setLoginHistIdPref(gmailLoginOutput.getLogin_history_id());

            /*Date todayDate = new Date();
            String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
            editor.putString("date", todayStr.trim());
            editor.commit();*/


            if (NetworkStatus.getInstance().isConnected(LoginActivity.this)) {

                //load video urls according to resolution
                if (languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE,DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1")) {

                    Log.v("BIBHU", "isRestrictDevice called");
                    // Call For Check Api.
                    CheckDeviceInput checkDeviceInput=new CheckDeviceInput();
                    checkDeviceInput.setDevice(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    checkDeviceInput.setGoogle_id(languagePreference.getTextofLanguage (Util.GOOGLE_FCM_TOKEN, Util.DEFAULT_GOOGLE_FCM_TOKEN));
                    checkDeviceInput.setAuthToken(authTokenStr);
                    checkDeviceInput.setDevice_type("1");
                    checkDeviceInput.setLang_code(languagePreference.getTextofLanguage (SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    checkDeviceInput.setDevice_info(deviceName + "," + languagePreference.getTextofLanguage (ANDROID_VERSION, DEFAULT_ANDROID_VERSION) + " " + Build.VERSION.RELEASE);
                    CheckDeviceAsyncTask asynCheckDevice = new CheckDeviceAsyncTask(checkDeviceInput, this, this);
                    asynCheckDevice.executeOnExecutor(threadPoolExecutor);
                } else {
                    if (getIntent().getStringExtra("from") != null) {
                                //** review **//*
                        onBackPressed();
                    } else {
                        if (Util.check_for_subscription == 1) {
                            //go to subscription page
                            if (NetworkStatus.getInstance().isConnected(LoginActivity.this)) {

                                ValidateUserInput validateUserInput = new ValidateUserInput();
                                validateUserInput.setAuthToken(authTokenStr);
                                validateUserInput.setUserId(preferenceManager.getUseridFromPref());
                                validateUserInput.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
                                validateUserInput.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id());
                                validateUserInput.setSeasonId(Util.dataModel.getSeason_id());
                                validateUserInput.setLanguageCode(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                                validateUserInput.setPurchaseType(Util.dataModel.getPurchase_type());
                                GetValidateUserAsynTask asynValidateUserDetails = new GetValidateUserAsynTask(validateUserInput, LoginActivity.this, LoginActivity.this);
                                asynValidateUserDetails.executeOnExecutor(threadPoolExecutor);
                            } else {
                                Util.showToast(LoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                                // Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if (PlanId.equals("1") && preferenceManager.getIsSubscribedFromPref().equals("0")) {
                                Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
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

                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                onBackPressed();
                            }
                        }
                    }
                }

            } else {
                Util.showToast(LoginActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION,DEFAULT_NO_INTERNET_CONNECTION));
            }


        }
        else {

            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setMessage(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK,DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();


        }

    }

}
