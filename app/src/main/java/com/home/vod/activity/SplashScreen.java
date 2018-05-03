package com.home.vod.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.CheckGeoBlockCountryAsynTask;
import com.home.apisdk.apiController.GetGenreListAsynctask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetPlanListAsynctask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetUserProfileAsynctask;
import com.home.apisdk.apiController.IsRegistrationEnabledAsynTask;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiModel.CheckGeoBlockInputModel;
import com.home.apisdk.apiModel.CheckGeoBlockOutputModel;
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
import com.home.vod.SplashScreenHandler;
import com.home.vod.model.LanguageModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.AppThreadPoolExecuter;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.LogUtil;
import com.home.vod.util.Util;

import org.json.JSONException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import player.model.ContactModel1;
import player.utils.DBHelper;

import static com.home.apisdk.apiController.HeaderConstants.RATING;
import static com.home.vod.preferences.LanguagePreference.APP_NO_LONGER_ACTIVE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_APP_NO_LONGER_ACTIVE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GEO_BLOCKED_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_RELEASE_DATE;
import static com.home.vod.preferences.LanguagePreference.FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.GEO_BLOCKED_ALERT;
import static com.home.vod.preferences.LanguagePreference.HAS_FAVORITE;
import static com.home.vod.preferences.LanguagePreference.IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.SORT_BY;
import static com.home.vod.preferences.LanguagePreference.SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.SORT_RELEASE_DATE;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.DEFAULT_GOOGLE_FCM_TOKEN;
import static com.home.vod.util.Util.GOOGLE_FCM_TOKEN;
import static com.home.vod.util.Util.decodeSampledBitmapFromResource;
import static player.utils.Util.IS_CHROMECAST;
import static player.utils.Util.IS_OFFLINE;

public class SplashScreen extends Activity implements GetIpAddressAsynTask.IpAddressListener,
        CheckGeoBlockCountryAsynTask.CheckGeoBlockForCountryListener,
        GetPlanListAsynctask.GetStudioPlanListsListener,
        IsRegistrationEnabledAsynTask.IsRegistrationenabledListener,
        GetLanguageListAsynTask.GetLanguageListListener,
        GetGenreListAsynctask.GenreListListener,
        GetUserProfileAsynctask.Get_UserProfileListener,
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener, SDKInitializer.SDKInitializerListner {

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

    Timer GoogleIdGeneraterTimer;
    Timer apiChcekTimer;

    /*Asynctask on background thread*/
    String ipAddressStr = "";
    private Executor threadPoolExecutor;
    private PreferenceManager preferenceManager;
    private LanguagePreference languagePreference;
    private FeatureHandler featureHandler;
    DBHelper dbHelper;

    SplashScreenHandler splashScreenHandler;


    /**
     * Splashreen Modified to optimize loading time.
     */


   /* Code Details
    0 = initial call
    1 = success
    2 = error form api
    3 = null data
    4 = no internet
    5 = other   */

    boolean sdkInitializerCalled = false ;
    int     sdkInitializerSuccessStatus = 0 ;
    String  sdkInitializerMsg = "";

    boolean ipAdressCalled = false ;
    int     ipAdressSuccessStatus = 0 ;
    String  ipAdressMsg = "";

    boolean geoBloackCalled = false ;
    int     geoBloackSuccessStatus = 0 ;
    String  geoBloackMsg = "";

    boolean isRegistrationEnabledCalled = false ;
    int     isRegistrationEnabledSuccessStatus = 0 ;
    String  isRegistrationEnabledMsg = "";

    boolean planListCalled = false ;
    int     planListSuccessStatus = 0 ;
    String  planListMsg = "";

    boolean languageListCalled = false ;
    int     languageListSuccessStatus = 0 ;
    String  languageListMsg = "";

    boolean languageTranslationCalled = false ;
    int     languageTranslationSuccessStatus = 0 ;
    String  languageTranslationMsg = "";

    boolean genreCalled = false ;
    int     genreSuccessStatus = 0 ;
    String  genreMsg = "";

    boolean profileCalled = false ;
    int     profileSuccessStatus = 0 ;
    String  profileMsg = "";


    // Kushal
    private static final int RC_SETTINGS = 6739;


    private void _init() {


        Util.getDPI(this);
        Util.printMD5Key(this);
        dbHelper = new DBHelper(SplashScreen.this);
        threadPoolExecutor = new AppThreadPoolExecuter().getThreadPoolExecutor();
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        featureHandler = FeatureHandler.getFeaturePreference(SplashScreen.this);

        noInternetLayout = (RelativeLayout) findViewById(R.id.noInternet);
        geoBlockedLayout = (RelativeLayout) findViewById(R.id.geoBlocked);

        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        geoTextView = (TextView) findViewById(R.id.geoBlockedTextView);
        ImageView imageResize = (ImageView) findViewById(R.id.splash_screen);

        Display display = getWindowManager().getDefaultDisplay();
        float dpHeight = display.getHeight();
        float dpWidth = display.getWidth();
        imageResize.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.splash_screen, dpWidth, dpHeight));

        splashScreenHandler.handleSplashscreen(imageResize);


        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        geoTextView.setText(languagePreference.getTextofLanguage(GEO_BLOCKED_ALERT, DEFAULT_GEO_BLOCKED_ALERT));

        // ImageView imageResize = (ImageView) findViewById(R.id.splash_screen);

        noInternetLayout.setVisibility(View.GONE);
        geoBlockedLayout.setVisibility(View.GONE);


    }

    private void apiCall() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            noInternetTextView.setText("The app is not compatible for this OS.");
            noInternetLayout.setVisibility(View.VISIBLE);
            return;
        }

        if (NetworkStatus.getInstance().isConnected(this)) {


            SDKInitializer.getInstance().init(this, this, authTokenStr);


            SDKInitializer.setData(this);
            /*Calling Ip-Address API*/
            GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
            asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

            /*Calling PlanList API*/
            SubscriptionPlanInputModel planListInput = new SubscriptionPlanInputModel();
            planListInput.setAuthToken(authTokenStr);
            planListInput.setLang(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            GetPlanListAsynctask asynGetPlanid = new GetPlanListAsynctask(planListInput, SplashScreen.this, SplashScreen.this);
            asynGetPlanid.executeOnExecutor(threadPoolExecutor);

            /*Calling isRegistrationEnabled API*/
            IsRegistrationEnabledInputModel isRegistrationEnabledInputModel = new IsRegistrationEnabledInputModel();
            isRegistrationEnabledInputModel.setAuthToken(authTokenStr);
            IsRegistrationEnabledAsynTask asynIsRegistrationEnabled = new IsRegistrationEnabledAsynTask(isRegistrationEnabledInputModel, this, this);
            asynIsRegistrationEnabled.executeOnExecutor(threadPoolExecutor);

            /*Calling languageList API*/
            LanguageListInputModel languageListInputModel = new LanguageListInputModel();
            languageListInputModel.setAuthToken(authTokenStr);
            GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
            asynGetLanguageList.executeOnExecutor(threadPoolExecutor);

            /*Calling genre API*/
            GenreListInput genreListInput = new GenreListInput();
            genreListInput.setAuthToken(authTokenStr);
            GetGenreListAsynctask asynGetGenreList = new GetGenreListAsynctask(genreListInput, SplashScreen.this, SplashScreen.this);
            asynGetGenreList.executeOnExecutor(threadPoolExecutor);



        } else {
            // Go to my download page , if the user is pre loggged in and the user has some download content.
            email_Id = preferenceManager.getEmailIdFromPref();
            if (email_Id != null) {
                ArrayList<ContactModel1> Size_Of_Download_Content = dbHelper.getContactt(email_Id, 1);
                if (Size_Of_Download_Content.size() > 0) {

                    Util.hideBcakIcon = true;

                    Intent intent = new Intent(SplashScreen.this, MyDownloads.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                } else {
                    noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                    noInternetLayout.setVisibility(View.VISIBLE);
                    geoBlockedLayout.setVisibility(View.GONE);
                }
            } else {
                noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                noInternetLayout.setVisibility(View.VISIBLE);
                geoBlockedLayout.setVisibility(View.GONE);
            }
        }
    }

    // Kushal

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            showExplanationForPermission();

        } else {
            apiCall();
        }
    }

    private Context getThemedContext() {
        ContextThemeWrapper themedContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            themedContext = new ContextThemeWrapper(SplashScreen.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        } else {
            themedContext = new ContextThemeWrapper(SplashScreen.this, android.R.style.Theme_Light_NoTitleBar);
        }
        return themedContext;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {


        switch (requestCode) {
            case 111: {
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        //denied
                        finish();
                        Log.e("denied", permission);
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            //allowed
                            apiCall();
                            Log.e("allowed", permission);
                        } else {
                            //set to never ask again
                            // Toast.makeText(this, "Set to never ask again", Toast.LENGTH_SHORT).show();
                            goTosettings();
                            Log.e("set to never ask again", permission);
                            //do something here.
                        }
                    }
                }

               /* if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        apiCall();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }*/

            }
        }
    }

    private void goTosettings() {
        new AlertDialog.Builder(getThemedContext()).setTitle(getResources().getString(R.string.permissionsRequired))
                .setMessage(getResources().getString(R.string.permissionsRequiredMessage))
                .setPositiveButton("settings", new DialogInterface.OnClickListener() {
                    @Override
                    @SuppressWarnings("InlinedAPI")
                    public void onClick(DialogInterface dialog, int which) {
                        openSettings();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();

    }

    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        startActivityForResult(intent, RC_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SETTINGS ) {
            askPermission();
        }
        // finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreenHandler = new SplashScreenHandler(this);

        _init();
        // Kushal
         askPermission();

         apiChcekTimer = new Timer();
         apiChcekTimer.schedule(new TimerTask() {
             @Override
             public void run() {

                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if(sdkInitializerCalled && ipAdressCalled && isRegistrationEnabledCalled
                                 && planListCalled && languageListCalled && genreCalled){

                             if(sdkInitializerSuccessStatus == 2){
                                 geoTextView.setText(languagePreference.getTextofLanguage(APP_NO_LONGER_ACTIVE, DEFAULT_APP_NO_LONGER_ACTIVE));
                                 noInternetLayout.setVisibility(View.GONE);
                                 geoBlockedLayout.setVisibility(View.VISIBLE);
                                 stopApicheckTimer();
                                 return;
                             }

                             if(sdkInitializerSuccessStatus == 5){

                                 Log.v("MUVI123","step = 1");

                                 noInternetTextView.setText("Oops something went wrong.Please try again later .");
                                 noInternetLayout.setVisibility(View.VISIBLE);
                                 geoBlockedLayout.setVisibility(View.GONE);
                                 stopApicheckTimer();
                                 return;
                             }

                             if(ipAdressSuccessStatus == 5){

                                 Log.v("MUVI123","step = 2");


                                 noInternetTextView.setText("Could not detect your IP.");
                                 noInternetLayout.setVisibility(View.VISIBLE);
                                 geoBlockedLayout.setVisibility(View.GONE);
                                 stopApicheckTimer();
                                 return;
                             }



                             if(geoBloackCalled){
                                 if(geoBloackSuccessStatus == 3 || geoBloackSuccessStatus == 5){

                                     Log.v("MUVI123","step = 3");


                                     noInternetTextView.setText("Oops something went wrong.Please try again later .");
                                     noInternetLayout.setVisibility(View.VISIBLE);
                                     geoBlockedLayout.setVisibility(View.GONE);
                                     stopApicheckTimer();
                                     return;
                                 }
                                 if(geoBloackSuccessStatus == 2){
                                     noInternetLayout.setVisibility(View.GONE);
                                     geoBlockedLayout.setVisibility(View.VISIBLE);
                                     stopApicheckTimer();
                                     return;
                                 }
                             }else{
                                 return;
                             }


                             if(isRegistrationEnabledSuccessStatus == 5){

                                 Log.v("MUVI123","step = 4");


                                 noInternetTextView.setText("Oops something went wrong.Please try again later .");
                                 noInternetLayout.setVisibility(View.VISIBLE);
                                 geoBlockedLayout.setVisibility(View.GONE);
                                 stopApicheckTimer();
                                 return;
                             }

                             if(!languageTranslationCalled) {
                                 return;
                             }

                             if ((featureHandler.getFeatureStatus(FeatureHandler.SIGNUP_STEP, FeatureHandler.DEFAULT_SIGNUP_STEP))) {
                                 if(!profileCalled) {
                                     return;
                                 }
                             }

                             // This Code Is Done For The One Step Registration.
                             Call_One_Step_Procedure();

                         }
                     }
                 });



             }
         },0,100);


    }

    @Override
    protected void onPause() {

        // TODO Auto-generated method stub
        super.onPause();
        LogUtil.showLog("BKS", "packagenamesplash===" + SDKInitializer.user_Package_Name_At_Api);
        //finish();
        overridePendingTransition(0, 0);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        Log.v("MUVI11", "ipAddressStr=====" + ipAddressStr);
        Log.v("MUVI11", "getIPAddress=====" + getIPAddress(true));

        ipAdressCalled = true;

        if (ipAddressStr.equals("")) {
            ipAdressMsg = "Could not detect your ip" ;
            ipAdressSuccessStatus = 5;

        } else {
            ipAdressSuccessStatus = 1;
            this.ipAddressStr = ipAddressStr;
            CheckGeoBlockInputModel checkGeoBlockInputModel = new CheckGeoBlockInputModel();
            checkGeoBlockInputModel.setAuthToken(authTokenStr);
            checkGeoBlockInputModel.setIp(ipAddressStr);
            CheckGeoBlockCountryAsynTask asynGetCountry = new CheckGeoBlockCountryAsynTask(checkGeoBlockInputModel, this, this);
            asynGetCountry.executeOnExecutor(threadPoolExecutor);
        }
    }

    @Override
    public void onCheckGeoBlockCountryPreExecuteStarted() {

    }

    @Override
    public void onCheckGeoBlockCountryPostExecuteCompleted(CheckGeoBlockOutputModel checkGeoBlockOutputModel, int status, String message) {

       geoBloackCalled = true ;

        if (checkGeoBlockOutputModel == null) {

            geoBloackSuccessStatus = 3;

            } else {
                if (status > 0 && status == 200) {
                    preferenceManager.setCountryCodeToPref(checkGeoBlockOutputModel.getCountrycode().trim());

                    geoBloackSuccessStatus = 1;

                } else if (status == 454) {
                    geoBloackSuccessStatus = 2;

                } else {
                    geoBloackSuccessStatus = 5;

                }
        }

    }

    @Override
    public void onGetPlanListPreExecuteStarted() {

    }

    @Override
    public void onGetPlanListPostExecuteCompleted(ArrayList<SubscriptionPlanOutputModel> planListOutput, int status) {

        planListCalled = true;
        planListSuccessStatus = 1;
        if (status > 0) {
            if (status == 200) {
                languagePreference.setLanguageSharedPrefernce(PLAN_ID, "1");
                LogUtil.showLog("MUVI", "responsestring of plan id = 1");
            } else {
                languagePreference.setLanguageSharedPrefernce(PLAN_ID, "0");
                LogUtil.showLog("MUVI", "responsestring of plan id = 0");
            }
        }

    }

    @Override
    public void onIsRegistrationenabledPreExecuteStarted() {

    }

    @Override
    public void onIsRegistrationenabledPostExecuteCompleted(IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel, int status, String message, String response) {


        isRegistrationEnabledCalled = true ;
        isRegistrationEnabledSuccessStatus = 1;
        try {
            featureHandler.setDefaultFeaturePref(response);
        } catch (Exception e) {}

        /*@BISHAL
         */
        if (status == 200) {
            //Util.login_registration_require=true;
            languagePreference.setLanguageSharedPrefernce(RATING, "" + isRegistrationEnabledOutputModel.getRating());


            languagePreference.setLanguageSharedPrefernce(HAS_FAVORITE, "" + isRegistrationEnabledOutputModel.getHas_favourite());
            languagePreference.setLanguageSharedPrefernce(RATING, "" + isRegistrationEnabledOutputModel.getRating());

            languagePreference.setLanguageSharedPrefernce(IS_RESTRICT_DEVICE, "" + isRegistrationEnabledOutputModel.getIsRestrictDevice());
            languagePreference.setLanguageSharedPrefernce(IS_ONE_STEP_REGISTRATION, "" + isRegistrationEnabledOutputModel.getSignup_step());
            languagePreference.setLanguageSharedPrefernce(IS_MYLIBRARY, "" + isRegistrationEnabledOutputModel.getIsMylibrary());

            languagePreference.setLanguageSharedPrefernce(IS_STREAMING_RESTRICTION, "" + isRegistrationEnabledOutputModel.getIs_streaming_restriction());
            languagePreference.setLanguageSharedPrefernce(IS_OFFLINE, "" + isRegistrationEnabledOutputModel.getIs_offline());
            languagePreference.setLanguageSharedPrefernce(IS_CHROMECAST, "" + isRegistrationEnabledOutputModel.getChromecast());


            preferenceManager.setLoginFeatureToPref(isRegistrationEnabledOutputModel.getIs_login());
            featureHandler.setFeatureFlag(FeatureHandler.IS_LOGIN_REGISTRATION_REQUIRE, "1");


            /**
             * Override feature properties.
             */

            splashScreenHandler.changeFeatureProperties(featureHandler);
            /*@BISHAL
             *Handle 455 status
             */

            callProfileAPI();

        } else if (status == 455) {
            callProfileAPI();

            featureHandler.setFeatureFlag(FeatureHandler.IS_LOGIN_REGISTRATION_REQUIRE, "0");
        }else {
            isRegistrationEnabledMsg = "Oops something went wrong.Please try again later .";
            isRegistrationEnabledSuccessStatus = 5;
        }


    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {

    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

        languageListCalled = true;
        languageListSuccessStatus = 1;

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
        }else {
            preferenceManager.setLanguageListToPref(""+languageModels.size());
        }
        if (languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, "").equalsIgnoreCase("")) {
            languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, defaultLanguage);
        } else {
            defaultLanguage = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
        }

        // Call For Language Translation.

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

        genreCalled = true;
        genreSuccessStatus = 1;

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


    }

    @Override
    public void onGet_UserProfilePreExecuteStarted() {

    }

    @Override
    public void onGet_UserProfilePostExecuteCompleted(Get_UserProfile_Output get_userProfile_output, int code, String message, String status) {

        profileCalled = true ;
        profileSuccessStatus = 1;

        if (status == null) {
            isSubscribed = "0";
        }
        if (code == 200) {
            isSubscribed = get_userProfile_output.getIsSubscribed();
        }

    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {

    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

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

        languageTranslationCalled = true;
        languageTranslationSuccessStatus = 1;


    }

    public void Call_One_Step_Procedure() {


       stopApicheckTimer();


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

    private void stopApicheckTimer() {
        try{
            if(apiChcekTimer != null){
                apiChcekTimer.cancel();
                apiChcekTimer = null;
            }
        }catch (Exception e){}
    }


    /**
     * Jump to next screen by checking condition.
     */
    private void jumpToNextScreen() {
        Intent mIntent;
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if ((featureHandler.getFeatureStatus(FeatureHandler.SIGNUP_STEP, FeatureHandler.DEFAULT_SIGNUP_STEP))) {
            if (loggedInStr != null) {
                if (isSubscribed.trim().equals("1")) {
                    mIntent = new Intent(SplashScreen.this, MainActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(0, 0);
                } else {
                    mIntent = new Intent(SplashScreen.this, SubscriptionActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            } else {
                mIntent = new Intent(SplashScreen.this, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(mIntent);
                finish();
            }

        } else {

            mIntent = new Intent(SplashScreen.this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mIntent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onPreExexuteListner() {

    }

    @Override
    public void onPostExecuteListner(int status) {
        SDKInitializer.setData(this);
        sdkInitializerCalled = true;

        if (status == 200) {
            sdkInitializerSuccessStatus = 1;
        } else if (status == Util.ERROR_CODE_EXPIRED_AUTHTOKEN) {
            sdkInitializerSuccessStatus = 2;
        } else {
            sdkInitializerSuccessStatus = 5;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.showLog("BKS", "packagenamesplash===" + SDKInitializer.user_Package_Name_At_Api);

    }


    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private void showExplanationForPermission(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getThemedContext());
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle(getResources().getString(R.string.storagePermissionNecessary));
        alertBuilder.setMessage(getApplicationName(getApplicationContext()) +" "+ getResources().getString(R.string.storagePermissionToPlayVideo));
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},  111);
            }
        });

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void callProfileAPI(){

        if ((featureHandler.getFeatureStatus(FeatureHandler.SIGNUP_STEP, FeatureHandler.DEFAULT_SIGNUP_STEP))) {

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

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(apiChcekTimer != null){
                apiChcekTimer.cancel();
            }
        }catch (Exception e){}
    }
}