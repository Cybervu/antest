package com.home.vod.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.home.apisdk.apiController.CheckGeoBlockCountryAsynTask;
import com.home.apisdk.apiController.FcmNotificationcountAsynTask;
import com.home.apisdk.apiController.GetGenreListAsynctask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetPlanListAsynctask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetUserProfileAsynctask;
import com.home.apisdk.apiController.IsRegistrationEnabledAsynTask;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiModel.CheckGeoBlockInputModel;
import com.home.apisdk.apiModel.CheckGeoBlockOutputModel;
import com.home.apisdk.apiModel.FcmNotificationcountInputModel;
import com.home.apisdk.apiModel.FcmNotificationcountOutputModel;
import com.home.apisdk.apiModel.GenreListInput;
import com.home.apisdk.apiModel.GenreListOutput;
import com.home.apisdk.apiModel.Get_UserProfile_Input;
import com.home.apisdk.apiModel.Get_UserProfile_Output;
import com.home.apisdk.apiModel.IsRegistrationEnabledInputModel;
import com.home.apisdk.apiModel.IsRegistrationEnabledOutputModel;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.SubscriptionPlanInputModel;
import com.home.apisdk.apiModel.SubscriptionPlanOutputModel;
import com.home.vod.R;
import com.home.vod.model.LanguageModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.AppThreadPoolExecuter;
import com.home.vod.util.Constant;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static com.home.apisdk.apiController.HeaderConstants.RATING;
import static com.home.vod.preferences.LanguagePreference.*;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.DEFAULT_GOOGLE_FCM_TOKEN;
import static com.home.vod.util.Util.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.util.Util.GOOGLE_FCM_TOKEN;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.IS_CHROMECAST;
import static player.utils.Util.IS_OFFLINE;
import static player.utils.Util.timer;

public class SplashScreen extends Activity implements GetIpAddressAsynTask.IpAddressListener,
        CheckGeoBlockCountryAsynTask.CheckGeoBlockForCountryListener,
        GetPlanListAsynctask.GetStudioPlanListsListener,
        IsRegistrationEnabledAsynTask.IsRegistrationenabledListener,
        GetLanguageListAsynTask.GetLanguageListListener,
        GetGenreListAsynctask.GenreListListener,
        GetUserProfileAsynctask.Get_UserProfileListener,
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener, SDKInitializer.SDKInitializerListner,FcmNotificationcountAsynTask.FcmNotificationcountListener {

    private String[] genreArrToSend;
    private String[] genreValueArrayToSend;
    private RelativeLayout noInternetLayout;
    private RelativeLayout geoBlockedLayout;
    private String default_Language = "";
    private ArrayList<LanguageModel> languageModels = new ArrayList<>();
    private TextView noInternetTextView, geoTextView;
    private ArrayList<String> genreArrayList = new ArrayList<String>();
    private ArrayList<String> genreValueArrayList = new ArrayList<String>();
    private String user_Id = "", email_Id = "", isSubscribed = "0";
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    public static ProgressBarHandler internetSpeedDialog;

 /*   private boolean isPlanlistAsyncComleted = false;
    private boolean isRegEnableAsyncComleted = false;
    private boolean isLanguagelistAsyncComleted = false;
    private boolean isTranslateAsyncComleted = false;
    private boolean isGenreAsyncComleted = false;
    private boolean isProfileAsyncComleted = false;*/

    //============================Added For FCM===========================//
    Timer GoogleIdGeneraterTimer;

    /*Asynctask on background thread*/
    String ipAddressStr;
    private Executor threadPoolExecutor;
    private PreferenceManager preferenceManager;
    private LanguagePreference languagePreference;


    private void _init() {

        Log.v("SUBHA","_init  === ");
        Util.getDPI(this);
        Util.printMD5Key(this);
        threadPoolExecutor = new AppThreadPoolExecuter().getThreadPoolExecutor();
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(this);

        noInternetLayout = (RelativeLayout) findViewById(R.id.noInternet);
        geoBlockedLayout = (RelativeLayout) findViewById(R.id.geoBlocked);

        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        geoTextView = (TextView) findViewById(R.id.geoBlockedTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        geoTextView.setText(languagePreference.getTextofLanguage(GEO_BLOCKED_ALERT, DEFAULT_GEO_BLOCKED_ALERT));

        noInternetLayout.setVisibility(View.GONE);
        geoBlockedLayout.setVisibility(View.GONE);

        if (NetworkStatus.getInstance().isConnected(this)) {
            SDKInitializer.getInstance().init(this, this, authTokenStr);
        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            geoBlockedLayout.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        ImageView imageResize = (ImageView) findViewById(R.id.splash_screen);


        float density = getResources().getDisplayMetrics().density;
        if (density >= 3.5 && density <= 4.0) {
            imageResize.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (density <= 1.5) {
            imageResize.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageResize.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.v("nihar_gg", dpHeight + "////////" + dpWidth);

        imageResize.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.splash_screen, dpWidth, dpHeight));
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.release.digiosmosis",  // replace with your unique package name
//                    getResources().getString(R.string.app_name),  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("SUBHA:", Base64.encodeToString(md.digest(), Base64.DEFAULT));


            }
        } catch (PackageManager.NameNotFoundException e) {

            Log.v("ANUU", e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.v("ANUU", e.toString());

        }


        _init();
    }

    @Override
    protected void onPause() {

        // TODO Auto-generated method stub
        super.onPause();
        LogUtil.showLog("BKS", "packagenamesplash===" + SDKInitializer.user_Package_Name_At_Api);
        finish();
        overridePendingTransition(0, 0);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        finish();
        System.exit(0);
    }

    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        Log.v("SUBHA","status ip  === "+statusCode);
        if (ipAddressStr.equals("")) {
            noInternetLayout.setVisibility(View.VISIBLE);
            geoBlockedLayout.setVisibility(View.GONE);
        } else {
            this.ipAddressStr = ipAddressStr;
            CheckGeoBlockInputModel checkGeoBlockInputModel = new CheckGeoBlockInputModel();
            checkGeoBlockInputModel.setAuthToken(authTokenStr);
            checkGeoBlockInputModel.setIp(ipAddressStr);
            CheckGeoBlockCountryAsynTask asynGetCountry = new CheckGeoBlockCountryAsynTask(checkGeoBlockInputModel, this, this);
            asynGetCountry.executeOnExecutor(threadPoolExecutor);
        }
        FcmNotificationcountInputModel fcmNotificationcountInputModel = new FcmNotificationcountInputModel();
        fcmNotificationcountInputModel.setAuthToken(authTokenStr);
        fcmNotificationcountInputModel.setDevice_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        FcmNotificationcountAsynTask fcmNotificationcountAsynTask = new FcmNotificationcountAsynTask(fcmNotificationcountInputModel,this,this);
        fcmNotificationcountAsynTask.executeOnExecutor(threadPoolExecutor);
    }

    @Override
    public void onCheckGeoBlockCountryPreExecuteStarted() {

    }

    @Override
    public void onCheckGeoBlockCountryPostExecuteCompleted(CheckGeoBlockOutputModel checkGeoBlockOutputModel, int status, String message) {

        Log.v("SUBHA","status geo  === "+status);

        if (checkGeoBlockOutputModel == null) {
            // countryCode = "";
            noInternetLayout.setVisibility(View.GONE);
            geoBlockedLayout.setVisibility(View.VISIBLE);
        } else {
            if (status > 0 && status == 200) {
                preferenceManager.setCountryCodeToPref(checkGeoBlockOutputModel.getCountrycode().trim());
                SubscriptionPlanInputModel planListInput = new SubscriptionPlanInputModel();
                planListInput.setAuthToken(authTokenStr);
                planListInput.setLang(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                GetPlanListAsynctask asynGetPlanid = new GetPlanListAsynctask(planListInput, SplashScreen.this, SplashScreen.this);
                asynGetPlanid.executeOnExecutor(threadPoolExecutor);

            } else {
                noInternetLayout.setVisibility(View.GONE);
                geoBlockedLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onGetPlanListPreExecuteStarted() {

    }

    @Override
    public void onGetPlanListPostExecuteCompleted(ArrayList<SubscriptionPlanOutputModel> planListOutput, int status) {

        Log.v("SUBHA","status plan id  === "+status);
        if (status > 0) {
            if (status == 200) {
                languagePreference.setLanguageSharedPrefernce(PLAN_ID, "1");
                LogUtil.showLog("MUVI", "responsestring of plan id = 1");
            } else {
                languagePreference.setLanguageSharedPrefernce(PLAN_ID, "0");
                LogUtil.showLog("MUVI", "responsestring of plan id = 0");
            }
        }

        IsRegistrationEnabledInputModel isRegistrationEnabledInputModel = new IsRegistrationEnabledInputModel();
        isRegistrationEnabledInputModel.setAuthToken(authTokenStr);
        IsRegistrationEnabledAsynTask asynIsRegistrationEnabled = new IsRegistrationEnabledAsynTask(isRegistrationEnabledInputModel, this, this);
        asynIsRegistrationEnabled.executeOnExecutor(threadPoolExecutor);
    }

    @Override
    public void onIsRegistrationenabledPreExecuteStarted() {

    }

    @Override
    public void onIsRegistrationenabledPostExecuteCompleted(IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel, int status, String message) {

        Log.v("SUBHA","status is resgistration  === "+status);

        languagePreference.setLanguageSharedPrefernce(HAS_FAVORITE, "" + isRegistrationEnabledOutputModel.getHas_favourite());
        languagePreference.setLanguageSharedPrefernce(RATING, "" + isRegistrationEnabledOutputModel.getRating());

        languagePreference.setLanguageSharedPrefernce(IS_RESTRICT_DEVICE, "" + isRegistrationEnabledOutputModel.getIsRestrictDevice());
        languagePreference.setLanguageSharedPrefernce(IS_ONE_STEP_REGISTRATION, "" + isRegistrationEnabledOutputModel.getSignup_step());
        languagePreference.setLanguageSharedPrefernce(IS_MYLIBRARY, "" + isRegistrationEnabledOutputModel.getIsMylibrary());

        languagePreference.setLanguageSharedPrefernce(IS_STREAMING_RESTRICTION, "" + isRegistrationEnabledOutputModel.getIs_streaming_restriction());
        languagePreference.setLanguageSharedPrefernce(IS_OFFLINE, "" + isRegistrationEnabledOutputModel.getIs_offline());
        languagePreference.setLanguageSharedPrefernce(IS_CHROMECAST, "" + isRegistrationEnabledOutputModel.getChromecast());


        preferenceManager.setLoginFeatureToPref(isRegistrationEnabledOutputModel.getIs_login());

        LogUtil.showLog("MUVI", "Splash setLoginFeatureToPref ::" + isRegistrationEnabledOutputModel.getIs_login());

        LanguageListInputModel languageListInputModel = new LanguageListInputModel();
        languageListInputModel.setAuthToken(authTokenStr);
        GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
        asynGetLanguageList.executeOnExecutor(threadPoolExecutor);

    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {

    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

        Log.v("SUBHA","status language list  === "+status);
        this.default_Language = defaultLanguage;
        for (int i = 0; i < languageListOutputArray.size(); i++) {

            LanguageModel languageModel = new LanguageModel();
            languageModel.setLanguageId(languageListOutputArray.get(i).getLanguageCode());
            languageModel.setLanguageName(languageListOutputArray.get(i).getLanguageName());
            if (defaultLanguage.equalsIgnoreCase(languageListOutputArray.get(i).getLanguageCode())) {
                languageModel.setIsSelected(true);

            } else {
                languageModel.setIsSelected(false);
            }

            languageModels.add(languageModel);
        }

        Util.languageModel = languageModels;

        if (languageModels.size() == 1) {
            preferenceManager.setLanguageListToPref("1");
        }
        if (languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, "").equalsIgnoreCase("")) {
            languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, defaultLanguage);
        }

        //                  Call For Language Translation.

        LanguageListInputModel languageListInputModel = new LanguageListInputModel();
        languageListInputModel.setAuthToken(authTokenStr);
        languageListInputModel.setLangCode(defaultLanguage);
        GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel, this, this);
        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);


    }

    @Override
    public void onGetGenreListPreExecuteStarted() {

    }

    @Override
    public void onGetGenreListPostExecuteCompleted(ArrayList<GenreListOutput> genreListOutput, int code, String status) {

        Log.v("SUBHA","status genre  === "+status);

        if (code > 0) {

            if (code == 200) {
                int lengthJsonArr = genreListOutput.size();
                if (lengthJsonArr > 0) {
                    genreArrayList.add(0, languagePreference.getTextofLanguage(FILTER_BY, DEFAULT_FILTER_BY));
                    genreValueArrayList.add(0, "");

                }
                for (int i = 0; i < lengthJsonArr; i++) {
                    genreArrayList.add(genreListOutput.get(i).getGenre_name());
                    genreValueArrayList.add(genreListOutput.get(i).getGenre_name());


                }

                if (genreArrayList.size() > 1) {

                    genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_BY, DEFAULT_SORT_BY));
                    genreValueArrayList.add(genreValueArrayList.size(), "");


                    genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_LAST_UPLOADED, DEFAULT_SORT_LAST_UPLOADED));
                    genreValueArrayList.add(genreValueArrayList.size(), "lastupload");

                    genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_RELEASE_DATE, DEFAULT_SORT_RELEASE_DATE));
                    genreValueArrayList.add(genreValueArrayList.size(), "releasedate");

                    genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_ALPHA_A_Z, DEFAULT_SORT_ALPHA_A_Z));
                    genreValueArrayList.add(genreValueArrayList.size(), "sortasc");

                    genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_ALPHA_Z_A, DEFAULT_SORT_ALPHA_Z_A));
                    genreValueArrayList.add(genreValueArrayList.size(), "sortdesc");


                }
                genreArrToSend = new String[genreArrayList.size()];
                genreArrToSend = genreArrayList.toArray(genreArrToSend);


                genreValueArrayToSend = new String[genreValueArrayList.size()];
                genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);
            } else {
                genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_BY, DEFAULT_SORT_BY));
                genreValueArrayList.add(genreValueArrayList.size(), "");


                genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_LAST_UPLOADED, DEFAULT_SORT_LAST_UPLOADED));
                genreValueArrayList.add(genreValueArrayList.size(), "lastupload");

                genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_RELEASE_DATE, DEFAULT_SORT_RELEASE_DATE));
                genreValueArrayList.add(genreValueArrayList.size(), "releasedate");

                genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_ALPHA_A_Z, DEFAULT_SORT_ALPHA_A_Z));
                genreValueArrayList.add(genreValueArrayList.size(), "sortasc");

                genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_ALPHA_Z_A, DEFAULT_SORT_ALPHA_Z_A));
                genreValueArrayList.add(genreValueArrayList.size(), "sortdesc");

                genreArrToSend = new String[genreArrayList.size()];
                genreArrToSend = genreArrayList.toArray(genreArrToSend);


                genreValueArrayToSend = new String[genreValueArrayList.size()];
                genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);

            }
        } else {
            genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_BY, DEFAULT_SORT_BY));
            genreValueArrayList.add(genreValueArrayList.size(), "");


            genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_LAST_UPLOADED, DEFAULT_SORT_LAST_UPLOADED));
            genreValueArrayList.add(genreValueArrayList.size(), "lastupload");

            genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_RELEASE_DATE, DEFAULT_SORT_RELEASE_DATE));
            genreValueArrayList.add(genreValueArrayList.size(), "releasedate");

            genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_ALPHA_A_Z, DEFAULT_SORT_ALPHA_A_Z));
            genreValueArrayList.add(genreValueArrayList.size(), "sortasc");

            genreArrayList.add(genreArrayList.size(), languagePreference.getTextofLanguage(SORT_ALPHA_Z_A, DEFAULT_SORT_ALPHA_Z_A));
            genreValueArrayList.add(genreValueArrayList.size(), "sortdesc");

            genreArrToSend = new String[genreArrayList.size()];
            genreArrToSend = genreArrayList.toArray(genreArrToSend);


            genreValueArrayToSend = new String[genreValueArrayList.size()];
            genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genreArrToSend.length; i++) {
            sb.append(genreArrToSend[i]).append(",");
        }

        preferenceManager.setGenreArrayToPref(sb.toString());

        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < genreValueArrayToSend.length; i++) {
            sb1.append(genreValueArrayToSend[i]).append(",");
        }

        preferenceManager.setGenreValuesArrayToPref(sb1.toString());

        // This Code Is Done For The One Step Registration.


        if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                .trim()).equals("1")) {

            if (preferenceManager != null) {
                user_Id = preferenceManager.getUseridFromPref();
                email_Id = preferenceManager.getEmailIdFromPref();

                if (user_Id != null && email_Id != null) {
                    Get_UserProfile_Input get_userProfile_input = new Get_UserProfile_Input();
                    get_userProfile_input.setAuthToken(authTokenStr);
                    get_userProfile_input.setEmail(email_Id);
                    get_userProfile_input.setUser_id(user_Id);
                    get_userProfile_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    GetUserProfileAsynctask asynLoadProfileDetails = new GetUserProfileAsynctask(get_userProfile_input, this, this);
                    asynLoadProfileDetails.executeOnExecutor(threadPoolExecutor);

                } else {
                    Call_One_Step_Procedure();
                }
            } else {
                Call_One_Step_Procedure();
            }
        } else {
            Call_One_Step_Procedure();
        }
    }

    @Override
    public void onGet_UserProfilePreExecuteStarted() {

    }

    @Override
    public void onGet_UserProfilePostExecuteCompleted(Get_UserProfile_Output get_userProfile_output, int code, String message, String status) {

        Log.v("SUBHA","status update profile  === "+status);

        if (status == null) {
            isSubscribed = "0";
        }
        if (code == 200) {
            isSubscribed = get_userProfile_output.getIsSubscribed();
        }

        Call_One_Step_Procedure();
    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {

    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

        Log.v("SUBHA","status translated  === "+status);

        if (status > 0 && status == 200) {


            try {
                Util.parseLanguage(languagePreference, jsonResponse, default_Language);
            } catch (JSONException e) {
                e.printStackTrace();
                noInternetLayout.setVisibility(View.GONE);
            }

        } else {
            noInternetLayout.setVisibility(View.GONE);
        }

        GenreListInput genreListInput = new GenreListInput();
        genreListInput.setAuthToken(authTokenStr);

        GetGenreListAsynctask asynGetGenreList = new GetGenreListAsynctask(genreListInput, SplashScreen.this, SplashScreen.this);
        asynGetGenreList.executeOnExecutor(threadPoolExecutor);
    }


    @Override
    public void onFcmNotificationcountPreExecuteStarted() {


    }

    @Override
    public void onFcmNotificationcountPostExecuteCompleted(FcmNotificationcountOutputModel fcmNotificationcountOutputModel,int count,String msg) {

        if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
            internetSpeedDialog.hide();
            internetSpeedDialog = null;

        }


        preferenceManager.setNOTI_COUNT(count);
        LogUtil.showLog("ANU","COUNT======="+count);
        LogUtil.showLog("ANU","device_id======="+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

    }


    public void Call_One_Step_Procedure() {

        if (!languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN).equals("0")) {
            LogUtil.showLog("MUVI", "google_id already created =" + languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));

            jumpToNextScreen();

        } else {
            GoogleIdGeneraterTimer = new Timer();
            GoogleIdGeneraterTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    LogUtil.showLog("MUVI", "google_id=" + languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));
                    if (!languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN).equals("0")) {
                        GoogleIdGeneraterTimer.cancel();
                        GoogleIdGeneraterTimer.purge();

                        LogUtil.showLog("MUVI", "google_id=" + languagePreference.getTextofLanguage(GOOGLE_FCM_TOKEN, DEFAULT_GOOGLE_FCM_TOKEN));
                        jumpToNextScreen();

                    }
                }
            }, 0, 1000);
        }

        //============================End Added For FCM===========================//
    }


    /**
     * Jump to next screen by checking condition.
     */
    private void jumpToNextScreen() {
        Intent mIntent;
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                .trim()).equals("1")) {
            if (loggedInStr != null) {
                if (isSubscribed.trim().equals("1")) {



                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            Log.v("ANU","preferenceManager.getSharedPref()==="+preferenceManager.getSharedPref());
                            if (!preferenceManager.getSharedPref().trim().equals("0") && !preferenceManager.getSharedPref().trim().equals("")) {


                                timer.cancel();
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
//                                    i.putExtra("notificationCount",Util.count);
//                                    Log.v("pratik","count=="+Util.count);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                                finish();
                                overridePendingTransition(0, 0);
                            }

                        }
                    },SPLASH_DISPLAY_LENGTH,200);

                    /*mIntent = new Intent(SplashScreen.this, MainActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(0, 0);*/
                } else {
                    mIntent = new Intent(SplashScreen.this, SubscriptionActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            } else {
                mIntent = new Intent(SplashScreen.this, RegisterActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(mIntent);
                finish();
            }

        } else {




            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {


                    if (!preferenceManager.getSharedPref().trim().equals("0") && !preferenceManager.getSharedPref().trim().equals("")) {


                        timer.cancel();
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
//                            i.putExtra("notificationCount",Util.count);
//                            Log.v("pratik","count=="+Util.count);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0, 0);
                        //Toast.makeText(SplashScreen.this, notificationPref.getString("regId", "0"), Toast.LENGTH_SHORT).show();
                    }

                }
            }, SPLASH_DISPLAY_LENGTH, 200);


            /*mIntent = new Intent(SplashScreen.this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mIntent);
            finish();
            overridePendingTransition(0, 0);*/
        }
    }

    @Override
    public void onPreExexuteListner() {

    }

    @Override
    public void onPostExecuteListner() {
        SDKInitializer.setData(this);
        if (NetworkStatus.getInstance().isConnected(this)) {
            GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
            asynGetIpAddress.executeOnExecutor(threadPoolExecutor);
        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            geoBlockedLayout.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.showLog("BKS", "packagenamesplash===" + SDKInitializer.user_Package_Name_At_Api);

    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         float reqWidth, float reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, float reqWidth, float reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}