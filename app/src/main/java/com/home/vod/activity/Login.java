package com.home.vod.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.CheckGeoBlockCountryAsynTask;
import com.home.apisdk.apiController.FcmNotificationcountAsynTask;
import com.home.apisdk.apiController.GetGenreListAsynctask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetPlanListAsynctask;
import com.home.apisdk.apiController.GetStudioAuthkeyAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetUserProfileAsynctask;
import com.home.apisdk.apiController.IsRegistrationEnabledAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiModel.CheckGeoBlockInputModel;
import com.home.apisdk.apiModel.CheckGeoBlockOutputModel;
import com.home.apisdk.apiModel.FcmNotificationcountInputModel;
import com.home.apisdk.apiModel.FcmNotificationcountOutputModel;
import com.home.apisdk.apiModel.GenreListInput;
import com.home.apisdk.apiModel.GenreListOutput;
import com.home.apisdk.apiModel.GetStudioAuthkeyInputModel;
import com.home.apisdk.apiModel.GetStudioAuthkeyOutputModel;
import com.home.apisdk.apiModel.Get_UserProfile_Input;
import com.home.apisdk.apiModel.Get_UserProfile_Output;
import com.home.apisdk.apiModel.IsRegistrationEnabledInputModel;
import com.home.apisdk.apiModel.IsRegistrationEnabledOutputModel;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.SubscriptionPlanInputModel;
import com.home.apisdk.apiModel.SubscriptionPlanOutputModel;
import com.home.vod.BuildConfig;
import com.home.vod.R;
import com.home.vod.model.LanguageModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import player.utils.Util;

import static android.R.attr.alertDialogTheme;
import static android.R.attr.filter;
import static com.home.apisdk.apiController.HeaderConstants.RATING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_EMPTY_FIELD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GEO_BLOCKED_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_RELEASE_DATE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.ENTER_EMPTY_FIELD;
import static com.home.vod.preferences.LanguagePreference.FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.GEO_BLOCKED_ALERT;
import static com.home.vod.preferences.LanguagePreference.IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.SORT_BY;
import static com.home.vod.preferences.LanguagePreference.SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.SORT_RELEASE_DATE;
import static com.home.vod.preferences.LanguagePreference.TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.TEXT_PASSWORD;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.DEFAULT_GOOGLE_FCM_TOKEN;
import static com.home.vod.util.Util.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.util.Util.GOOGLE_FCM_TOKEN;
import static com.home.vod.util.Util.hideKeyboard;
import static player.utils.Util.HAS_FAVORITE;
import static player.utils.Util.IS_CHROMECAST;
import static player.utils.Util.IS_OFFLINE;

public class Login extends Activity implements  GetIpAddressAsynTask.IpAddressListener,
        CheckGeoBlockCountryAsynTask.CheckGeoBlockForCountryListener,
        GetPlanListAsynctask.GetStudioPlanListsListener,
        IsRegistrationEnabledAsynTask.IsRegistrationenabledListener,
        GetLanguageListAsynTask.GetLanguageListListener,
        GetGenreListAsynctask.GenreListListener,
        GetUserProfileAsynctask.Get_UserProfileListener,
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener,
        GetStudioAuthkeyAsynTask.GetStudioAuthkeyListener,
        FcmNotificationcountAsynTask.FcmNotificationcountListener,
        SDKInitializer.SDKInitializerListner,
        LogoutAsynctask.LogoutListener {

    TextView noInternetTextView;
    TextView geoTextView;
    TextView buildingAppText;

    Button loginbtn;


    RelativeLayout noInternetLayout ;
    RelativeLayout geoBlockedLayout ;
    LinearLayout linearLayout;

    String email,password;
    String key;
    String Default_Language = "";
    String ipAddressStr,loggedInStr,authToken,loginHistoryIdStr;


    String[] genreArrToSend;
    String[] genreValueArrayToSend;
    private String user_Id = "", email_Id = "", isSubscribed = "0";
    private String default_Language = "";

    public static String auth = "";

    EditText editEmail,editPassword;

    SharedPreferences pref;
    SharedPreferences isLoginPref;
    SharedPreferences countryPref,language_list_pref;
    LanguagePreference languagePreference;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    Timer GoogleIdGeneraterTimer;
    JSONObject jsonObject;

    ArrayList<LanguageModel> languageModels = new ArrayList<>();
    private ArrayList<String> genreArrayList = new ArrayList<String>();
    private ArrayList<String> genreValueArrayList = new ArrayList<String>();



    boolean isNetwork;
    int notificationcont;
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;


    //    AsynFcmNotificationcount asynFcmNotificationcount;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        /** Added to close auto keyborad **/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference((this));
        loggedInStr = preferenceManager.getLoginStatusFromPref();
        authToken = preferenceManager.getAuthToken();
        loginHistoryIdStr = preferenceManager.getLoginHistIdFromPref();

        deleteExistingUser();

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

        editEmail= (EditText) findViewById(R.id.editEmail);
        editEmail.setFilters(new InputFilter[]{filter});
        FontUtls.loadFont(Login.this, getResources().getString(R.string.light_fonts), editEmail);
        editEmail.setHint(languagePreference.getTextofLanguage(TEXT_EMIAL, DEFAULT_TEXT_EMIAL));

        editPassword= (EditText) findViewById(R.id.editPassword);
        editPassword.setFilters(new InputFilter[]{filter});
        FontUtls.loadFont(Login.this, getResources().getString(R.string.light_fonts), editPassword);
        editPassword.setHint(languagePreference.getTextofLanguage(TEXT_PASSWORD, DEFAULT_TEXT_PASSWORD));

        loginbtn= (Button) findViewById(R.id.loginbtn);
        FontUtls.loadFont(Login.this, getResources().getString(R.string.regular_fonts), loginbtn);
        loginbtn.setText(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN));

        noInternetLayout = (RelativeLayout) findViewById(R.id.noInternet);
        geoBlockedLayout = (RelativeLayout) findViewById(R.id.geoBlocked);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        geoTextView = (TextView) findViewById(R.id.geoBlockedTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        geoTextView.setText(languagePreference.getTextofLanguage(GEO_BLOCKED_ALERT, DEFAULT_GEO_BLOCKED_ALERT));
        noInternetLayout.setVisibility(View.GONE);
        geoBlockedLayout.setVisibility(View.GONE);


        buildingAppText= (TextView) findViewById(R.id.building);

        linearLayout= (LinearLayout) findViewById(R.id.ll);
        progressBar= (ProgressBar) findViewById(R.id.dialogProgressBar);
        pref = getSharedPreferences("logpref", 0);
        language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
        isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE,0);
        isNetwork = Util.checkNetwork(Login.this);

        preferenceManager.setExitAppKey(1);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email=editEmail.getText().toString();
                password=editPassword.getText().toString();


                if(email.length()!=0 && password.length()!=0){

                    if (isNetwork == true ) {
                        Log.v("ANU","GetStudioAuthkeyAsynTask called");
                        Log.v("ANU","isNetwork ==="+isNetwork);



                        GetStudioAuthkeyInputModel getStudioAuthkeyInputModel = new GetStudioAuthkeyInputModel();
                        getStudioAuthkeyInputModel.setEmail(email);
                        getStudioAuthkeyInputModel.setPassword(password);
                        Log.v("ANU","email called"+email);
                        Log.v("ANU","password called"+password);
                        Log.v("ANU","email in model"+getStudioAuthkeyInputModel.getEmail());
                        Log.v("ANU","password in model"+getStudioAuthkeyInputModel.getPassword());
                        GetStudioAuthkeyAsynTask getStudioAuthkeyAsynTask = new GetStudioAuthkeyAsynTask(getStudioAuthkeyInputModel, Login.this, Login.this);
                        getStudioAuthkeyAsynTask.executeOnExecutor(threadPoolExecutor);



                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);



                    }else{

                        toastMsg(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                        Intent i=new Intent(Login.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);

                    }

                }else {

                    toastMsg(languagePreference.getTextofLanguage(ENTER_EMPTY_FIELD, DEFAULT_ENTER_EMPTY_FIELD));


                }


            }
        });

    }


    private void toastMsg(String msgs) {

        final Toast toast = Toast.makeText(getBaseContext(), msgs,Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast_boder1);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        //v.setTextColor(getResources().getColor(R.color.muvilogocolor));
        v.setTextColor(Color.WHITE);

        toast.show();

        new CountDownTimer(9000, 1000)
        {

            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}

        }.start();

    }




    @Override
    public void onGetStudioAuthkeyPreExecuteStarted() {
        Log.v("ANU","onGetStudioAuthkeyPreExecuteStarted called");

        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        buildingAppText.setVisibility(View.VISIBLE);

    }


    @Override
    public void onGetStudioAuthkeyPostExecuteCompleted(GetStudioAuthkeyOutputModel getStudioAuthkeyOutputModel,String status, String response, String authToken, String message) {

        Log.v("ANU","onGetStudioAuthkeyPostExecuteCompleted called");
        Log.v("ANU","response ===="+response);
        Log.v("ANU","status ===="+status);
        Log.v("ANU","authToken ===="+authToken);
        Log.v("ANU","message ===="+message);

       if (response != null) {


           if (status.equals("OK")) {
               preferenceManager.setAuthToken(authToken);

               if (NetworkStatus.getInstance().isConnected(Login.this)) {
                   SDKInitializer.getInstance().init(Login.this, Login.this, preferenceManager.getAuthToken().trim());
               } else {
                   noInternetLayout.setVisibility(View.VISIBLE);
                   geoBlockedLayout.setVisibility(View.GONE);
               }


           }
           else {


               toastMsg(message);

               Intent i=new Intent(Login.this,Login.class);
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               startActivity(i);
               finish();
               overridePendingTransition(0,0);

           }




       } else {
           toastMsg("Network problem try again");

           Intent i=new Intent(Login.this,Login.class);
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
           startActivity(i);
           finish();
           overridePendingTransition(0,0);
       }

    }


    @Override
    public void onPreExexuteListner() {

        Log.v("ANU","SDK called");
    }

    @Override
    public void onPostExecuteListner() {
        SDKInitializer.setData(this);
        if (NetworkStatus.getInstance().isConnected(this)) {
            Log.v("ANU","GetIpAddressAsynTask called");

            GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
            asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            geoBlockedLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {
        if (ipAddressStr.equals("")) {
            Log.v("ANU","ipAddressStr==="+ipAddressStr);
            Log.v("ANU","message==="+message);
            Log.v("ANU","statusCode==="+statusCode);
            noInternetLayout.setVisibility(View.VISIBLE);
            geoBlockedLayout.setVisibility(View.GONE);
        } else {
            this.ipAddressStr = ipAddressStr;
            CheckGeoBlockInputModel checkGeoBlockInputModel = new CheckGeoBlockInputModel();
            checkGeoBlockInputModel.setAuthToken(preferenceManager.getAuthToken().trim());
            checkGeoBlockInputModel.setIp(ipAddressStr);
            Log.v("ANU","CheckGeoBlockCountryAsynTask called");
            CheckGeoBlockCountryAsynTask asynGetCountry = new CheckGeoBlockCountryAsynTask(checkGeoBlockInputModel, this, this);
            asynGetCountry.executeOnExecutor(threadPoolExecutor);
        }
        Log.v("ANU","FcmNotificationcountAsynTask called");
        FcmNotificationcountInputModel fcmNotificationcountInputModel = new FcmNotificationcountInputModel();
        fcmNotificationcountInputModel.setAuthToken(preferenceManager.getAuthToken().trim());
        fcmNotificationcountInputModel.setDevice_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        FcmNotificationcountAsynTask fcmNotificationcountAsynTask = new FcmNotificationcountAsynTask(fcmNotificationcountInputModel,this,this);
        fcmNotificationcountAsynTask.executeOnExecutor(threadPoolExecutor);
    }


    @Override
    public void onCheckGeoBlockCountryPreExecuteStarted() {

    }

    @Override
    public void onCheckGeoBlockCountryPostExecuteCompleted(CheckGeoBlockOutputModel checkGeoBlockOutputModel, int status, String message) {
        if (checkGeoBlockOutputModel == null) {
            // countryCode = "";
            noInternetLayout.setVisibility(View.GONE);
            geoBlockedLayout.setVisibility(View.VISIBLE);
        } else {
            if (status > 0 && status == 200) {
                preferenceManager.setCountryCodeToPref(checkGeoBlockOutputModel.getCountrycode().trim());
                SubscriptionPlanInputModel planListInput = new SubscriptionPlanInputModel();
                planListInput.setAuthToken(preferenceManager.getAuthToken().trim());
                planListInput.setLang(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                Log.v("ANU","GetPlanListAsynctask called");
                GetPlanListAsynctask asynGetPlanid = new GetPlanListAsynctask(planListInput, Login.this, Login.this);
                asynGetPlanid.executeOnExecutor(threadPoolExecutor);

            } else {
                noInternetLayout.setVisibility(View.GONE);
                geoBlockedLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onFcmNotificationcountPreExecuteStarted() {


    }

    @Override
    public void onFcmNotificationcountPostExecuteCompleted(FcmNotificationcountOutputModel fcmNotificationcountOutputModel, int count, String msg) {

        preferenceManager.setNOTI_COUNT(count);
        LogUtil.showLog("ANU","COUNT======="+count);
        LogUtil.showLog("ANU","device_id======="+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

    }

    @Override
    public void onGetPlanListPreExecuteStarted() {

    }

    @Override
    public void onGetPlanListPostExecuteCompleted(ArrayList<SubscriptionPlanOutputModel> planListOutput, int status) {
        if (status > 0) {
            if (status == 200) {
                languagePreference.setLanguageSharedPrefernce(PLAN_ID, "1");
                LogUtil.showLog("MUVI", "responsestring of plan id = 1");
            } else {
                languagePreference.setLanguageSharedPrefernce(PLAN_ID, "0");
                LogUtil.showLog("MUVI", "responsestring of plan id = 0");
            }
        }
        Log.v("ANU","IsRegistrationEnabledAsynTask called");

        IsRegistrationEnabledInputModel isRegistrationEnabledInputModel = new IsRegistrationEnabledInputModel();
        isRegistrationEnabledInputModel.setAuthToken(preferenceManager.getAuthToken().trim());
        IsRegistrationEnabledAsynTask asynIsRegistrationEnabled = new IsRegistrationEnabledAsynTask(isRegistrationEnabledInputModel, this, this);
        asynIsRegistrationEnabled.executeOnExecutor(threadPoolExecutor);
    }


    @Override
    public void onIsRegistrationenabledPreExecuteStarted() {

    }

    @Override
    public void onIsRegistrationenabledPostExecuteCompleted(IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel, int status, String message) {

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
        languageListInputModel.setAuthToken(preferenceManager.getAuthToken().trim());
        Log.v("ANU","GetLanguageListAsynTask called");
        GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
        asynGetLanguageList.executeOnExecutor(threadPoolExecutor);

    }


    @Override
    public void onGetLanguageListPreExecuteStarted() {

    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

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

        com.home.vod.util.Util.languageModel = languageModels;

        if (languageModels.size() == 1) {
            preferenceManager.setLanguageListToPref("1");
        }
        if (languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, "").equalsIgnoreCase("")) {
            languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, defaultLanguage);
        }

        else {
            defaultLanguage = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,DEFAULT_SELECTED_LANGUAGE_CODE);
        }
        //                  Call For Language Translation.

        LanguageListInputModel languageListInputModel = new LanguageListInputModel();
        languageListInputModel.setAuthToken(preferenceManager.getAuthToken().trim());
        languageListInputModel.setLangCode(defaultLanguage);
        Log.v("ANU","GetTranslateLanguageAsync called");
        GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel, this, this);
        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);


    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {

    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

        if (status > 0 && status == 200) {


            try {
                com.home.vod.util.Util.parseLanguage(languagePreference, jsonResponse, default_Language);
            } catch (JSONException e) {
                e.printStackTrace();
                noInternetLayout.setVisibility(View.GONE);
            }

        } else {
            noInternetLayout.setVisibility(View.GONE);
        }

        GenreListInput genreListInput = new GenreListInput();
        genreListInput.setAuthToken(preferenceManager.getAuthToken().trim());
        Log.v("ANU","GetGenreListAsynctask called");
        GetGenreListAsynctask asynGetGenreList = new GetGenreListAsynctask(genreListInput, Login.this, Login.this);
        asynGetGenreList.executeOnExecutor(threadPoolExecutor);
    }



    @Override
    public void onGetGenreListPreExecuteStarted() {

    }

    @Override
    public void onGetGenreListPostExecuteCompleted(ArrayList<GenreListOutput> genreListOutput, int code, String status) {

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
                    get_userProfile_input.setAuthToken(preferenceManager.getAuthToken().trim());
                    get_userProfile_input.setEmail(email_Id);
                    get_userProfile_input.setUser_id(user_Id);
                    get_userProfile_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    Log.v("ANU","GetUserProfileAsynctask called");
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
        if (status == null) {
            isSubscribed = "0";
        }
        if (code==200){
            isSubscribed=get_userProfile_output.getIsSubscribed();
        }

        Call_One_Step_Procedure();
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


    private void jumpToNextScreen() {
        Intent mIntent;
        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                .trim()).equals("1")) {
            if (loggedInStr != null) {
                if (isSubscribed.trim().equals("1")) {
                    Log.v("ANU","MainActivity===1");
                    mIntent = new Intent(Login.this, MainActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(0, 0);
                } else {
                    mIntent = new Intent(Login.this, SubscriptionActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            } else {
                mIntent = new Intent(Login.this, RegisterActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(mIntent);
                finish();
            }

        } else {
            Log.v("ANU","MainActivity===2");
            mIntent = new Intent(Login.this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mIntent);
            finish();
            overridePendingTransition(0, 0);
        }
    }




/*
    private class AsynFcmNotificationcount extends AsyncTask<Void, Void, Void> {

        ProgressBarHandler pDialog;

        int status;
        String responseStr;
        String registrationIdStr;
        String isSubscribedStr;
        String loginHistoryIdStr;
        JSONObject myJson = null;

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim()+Util.notificationcount.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("device_id", Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID));
                httppost.addHeader("authToken", Util.authTokenStr.trim());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            status = 0;
                            Toast.makeText(Login.this, Util.getTextofLanguage(Login.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    status = 0;
                    e.printStackTrace();
                }


            }
            catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;



                    if(responseStr!=null){



                        try {
                            jsonObject = new JSONObject(responseStr);
                            //responseStr=jsonObject.getString("msg");
                            if (jsonObject.getString("status").equals("Success")){

                                notificationcont=jsonObject.getInt("count");
//                                key=jsonObject.getString("authToken");
                                Util.noticount=jsonObject.getInt("count");


                            }else if (jsonObject.getString("status").equals("Failure")){


                                //taoast(response);



                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else{



                    }




//                   AsynGetPlanId asynGetPlanId = new AsynGetPlanId();
//                    asynGetPlanId.executeOnExecutor(threadPoolExecutor);

                AsynGetIpAddress asynGetIpAddress = new AsynGetIpAddress();
                asynGetIpAddress.executeOnExecutor(threadPoolExecutor);
                    //Toast.makeText(getApplicationContext(),regId+"\n"+Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID),Toast.LENGTH_LONG).show();

               // }
            } catch (IllegalArgumentException ex) {
                status = 0;

            }

        }

        @Override
        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(Login.this);
//            pDialog.show();
        }


    }
*/



 public void deleteExistingUser() {

     preferenceManager.clearLoginPref();

     if (loggedInStr!=null) {
         LogoutInput logoutInput = new LogoutInput();
         logoutInput.setAuthToken(authToken.trim());
         logoutInput.setLogin_history_id(loginHistoryIdStr);
         logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
         LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, this, this);
         asynLogoutDetails.executeOnExecutor(threadPoolExecutor);
     }

 }

    @Override
    public void onLogoutPreExecuteStarted() {

    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {


    }

}
