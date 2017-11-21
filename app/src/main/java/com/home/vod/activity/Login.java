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
import com.home.apisdk.apiController.GetGenreListAsynctask;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetPlanListAsynctask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.GetUserProfileAsynctask;
import com.home.apisdk.apiController.IsRegistrationEnabledAsynTask;
import com.home.apisdk.apiModel.CheckGeoBlockInputModel;
import com.home.apisdk.apiModel.CheckGeoBlockOutputModel;
import com.home.apisdk.apiModel.GenreListInput;
import com.home.apisdk.apiModel.GenreListOutput;
import com.home.apisdk.apiModel.Get_UserProfile_Input;
import com.home.apisdk.apiModel.IsRegistrationEnabledInputModel;
import com.home.apisdk.apiModel.IsRegistrationEnabledOutputModel;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.SubscriptionPlanInputModel;
import com.home.apisdk.apiModel.SubscriptionPlanOutputModel;
import com.home.vod.R;
import com.home.vod.model.LanguageModel;
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

import static android.R.attr.filter;
import static com.google.ads.interactivemedia.v3.b.b.a.m.R;
import static com.home.apisdk.apiController.HeaderConstants.RATING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_EMPTY_FIELD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_A_Z;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_ALPHA_Z_A;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_BY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_LAST_UPLOADED;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORT_RELEASE_DATE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_EMIAL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.ENTER_EMPTY_FIELD;
import static com.home.vod.preferences.LanguagePreference.FILTER_BY;
import static com.home.vod.preferences.LanguagePreference.IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PLAN_ID;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
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
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener {

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
    String ipAddressStr;

    String[] genreArrToSend;
    String[] genreValueArrayToSend;
    private String user_Id = "", email_Id = "", isSubscribed = "0";

    EditText editEmail,editPassword;
    Logintest login;

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

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference((this));


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


        buildingAppText= (TextView) findViewById(R.id.building);

        linearLayout= (LinearLayout) findViewById(R.id.ll);
        progressBar= (ProgressBar) findViewById(R.id.dialogProgressBar);
        pref = getSharedPreferences("logpref", 0);
        language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
        isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE,0);
        isNetwork = Util.checkNetwork(Login.this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email=editEmail.getText().toString();
                password=editPassword.getText().toString();

                if(email.length()!=0 && password.length()!=0){

                    if (isNetwork == true ) {

                        login  = new Logintest();
                        login.execute();
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


//        asynFcmNotificationcount= new AsynFcmNotificationcount();
//                  asynFcmNotificationcount.executeOnExecutor(threadPoolExecutor);

//        AsynGetPlanId asynGetPlanId = new AsynGetPlanId();
//        asynGetPlanId.executeOnExecutor(threadPoolExecutor);
    }
//
//    public void login(View view){
//
//        emil=et.getText().toString();
//        pass=et1.getText().toString();
//
//        if(emil.length()!=0 && pass.length()!=0){
//
//            if (isNetwork == true ) {
//
//                login  = new Logintest();
//                login.execute();
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
//            }else{
//
//                taoast(getResources().getString(R.string.internet));
//
//            }
//
//        }else {
//
//            taoast(getResources().getString(R.string.toastmessage));
//        }
//
//
//    }

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


    private class Logintest extends AsyncTask<Void, Void, Void> {


        String responseStr;
        int status;
        String response;
        String sta;


        @Override
        protected void onPreExecute() {
            linearLayout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            buildingAppText.setVisibility(View.VISIBLE);
//            pDialog = new ProgressDialog(Login.this);
//            pDialog.setCancelable(false);
//            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
//            pDialog.setIndeterminate(false);
//
//            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
//            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = APIUrlConstant.getGetStudioAuthToken();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("email", email);
                httppost.addHeader("password", password);

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {


                } catch (IOException e) {


                    e.printStackTrace();
                }


            } catch (Exception e) {

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            if(responseStr!=null){



                try {
                    jsonObject = new JSONObject(responseStr);
                    response=jsonObject.getString("msg");
                    if (jsonObject.getString("status").equals("OK")){


                        key=jsonObject.getString("authToken");
                        Util.authTokenStr=key;
//                        AsynGetPlanId asynGetPlanId = new AsynGetPlanId();
//                        asynGetPlanId.executeOnExecutor(threadPoolExecutor);

                        GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
                        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

                    }else if (jsonObject.getString("status").equals("Failure")){


                        toastMsg(response);


                        Intent i=new Intent(Login.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }else{


                toastMsg("Network problem try again");
                //showCustomAlert("Network problem try again");
                //Toast.makeText(Login.this,"Network problem try again",Toast.LENGTH_LONG).show();
                Intent i=new Intent(Login.this,Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
                overridePendingTransition(0,0);
            }

        }

    }

    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {
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
                planListInput.setAuthToken(authTokenStr);
                planListInput.setLang(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                GetPlanListAsynctask asynGetPlanid = new GetPlanListAsynctask(planListInput, Login.this, Login.this);
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
        languageListInputModel.setAuthToken(authTokenStr);
        languageListInputModel.setLangCode(defaultLanguage);
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
        genreListInput.setAuthToken(authTokenStr);

        GetGenreListAsynctask asynGetGenreList = new GetGenreListAsynctask(genreListInput, SplashScreen.this, SplashScreen.this);
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

            mIntent = new Intent(Login.this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mIntent);
            finish();
            overridePendingTransition(0, 0);
        }
    }



    //Verify the IP
    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
        String responseStr;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                // Execute HTTP Post Request
                try {
                    URL myurl = new URL(Util.loadIPUrl);
                    HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
                    InputStream ins = con.getInputStream();
                    InputStreamReader isr = new InputStreamReader(ins);
                    BufferedReader in = new BufferedReader(isr);

                    String inputLine;

                    while ((inputLine = in.readLine()) != null)
                    {
                        System.out.println(inputLine);
                        responseStr = inputLine;
                    }

                    in.close();


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    ipAddressStr = "";

                    runOnUiThread(new Runnable() {
                        public void run() {
                            noInternetLayout.setVisibility(View.VISIBLE);
                            geoBlockedLayout.setVisibility(View.GONE);

                        }
                    });
                } catch (UnsupportedEncodingException e) {

                    ipAddressStr = "";
                    runOnUiThread(new Runnable() {
                        public void run() {
                            noInternetLayout.setVisibility(View.VISIBLE);
                            geoBlockedLayout.setVisibility(View.GONE);

                        }
                    });
                }catch (IOException e) {
                    ipAddressStr = "";

                    runOnUiThread(new Runnable() {
                        public void run() {
                            noInternetLayout.setVisibility(View.VISIBLE);
                            geoBlockedLayout.setVisibility(View.GONE);

                        }
                    });
                }
                if(responseStr!=null){
                    Object json = new JSONTokener(responseStr).nextValue();
                    if (json instanceof JSONObject){
                        ipAddressStr = ((JSONObject) json).getString("ip");

                    }

                }

            }
            catch (Exception e) {
                ipAddressStr = "";

                runOnUiThread(new Runnable() {
                    public void run() {
                        noInternetLayout.setVisibility(View.VISIBLE);
                        geoBlockedLayout.setVisibility(View.GONE);

                    }
                });


            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){
                ipAddressStr = "";
                noInternetLayout.setVisibility(View.VISIBLE);
                geoBlockedLayout.setVisibility(View.GONE);
            }else{
                AsynGetCountry asynGetCountry = new AsynGetCountry();
                asynGetCountry.executeOnExecutor(threadPoolExecutor);

            }

        }

        protected void onPreExecute() {

        }
    }
    //Verify the IP
    private class AsynGetCountry extends AsyncTask<Void, Void, Void> {
        String responseStr;
        String countryCode;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList =Util.rootUrl().trim()+Util.loadCountryUrl.trim();

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpGet httppost = new HttpGet(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("ip", ipAddressStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    countryCode = "";
                    runOnUiThread(new Runnable() {
                        public void run() {
                            noInternetLayout.setVisibility(View.GONE);
                            geoBlockedLayout.setVisibility(View.VISIBLE);

                        }
                    });

                } catch (UnsupportedEncodingException e) {

                    countryCode = "";
                    runOnUiThread(new Runnable() {
                        public void run() {
                            noInternetLayout.setVisibility(View.GONE);
                            geoBlockedLayout.setVisibility(View.VISIBLE);

                        }
                    });

                }catch (IOException e) {
                    countryCode = "";
                    runOnUiThread(new Runnable() {
                        public void run() {
                            noInternetLayout.setVisibility(View.GONE);
                            geoBlockedLayout.setVisibility(View.VISIBLE);

                        }
                    });

                }
                if(responseStr!=null){
                    Object json = new JSONTokener(responseStr).nextValue();
                    if (json instanceof JSONObject){
                        String statusStr = ((JSONObject) json).getString("code");
                        status = Integer.parseInt(statusStr);
                        if (status == 200){
                            countryCode = ((JSONObject) json).getString("country");
                        }

                    }

                }

            }
            catch (Exception e) {
                countryCode = "";
                runOnUiThread(new Runnable() {
                    public void run() {
                        noInternetLayout.setVisibility(View.GONE);
                        geoBlockedLayout.setVisibility(View.VISIBLE);

                    }
                });



            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){
                countryCode = "";
                noInternetLayout.setVisibility(View.GONE);
                geoBlockedLayout.setVisibility(View.VISIBLE);
            }else{
                if (status > 0 && status == 200) {
                    if (countryPref != null) {
                        SharedPreferences.Editor countryEditor = countryPref.edit();
                        countryEditor.putString("countryCode", countryCode.trim());

                        countryEditor.commit();
                        AsynGetPlanId asynGetPlanId = new AsynGetPlanId();
                        asynGetPlanId.executeOnExecutor(threadPoolExecutor);
                    }

                }else{
                    noInternetLayout.setVisibility(View.GONE);
                    geoBlockedLayout.setVisibility(View.VISIBLE);
                }
            }

        }

        protected void onPreExecute() {

        }
    }


    private class AsynGetPlanId extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList =Util.rootUrl().trim()+Util.getStudioPlanLists.trim();

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpGet httppost = new HttpGet(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken",Util.authTokenStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());



                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                } catch (UnsupportedEncodingException e) {

                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                }catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                }
                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {
                    if (status == 200) {
                        Util.setLanguageSharedPrefernce(Login.this,Util.PLAN_ID,"1");

                    }
                    else{
                        Util.setLanguageSharedPrefernce(Login.this,Util.PLAN_ID,"0");

                    }
                }

            }
            catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });



            }

            return null;
        }


        protected void onPostExecute(Void result) {

            AsynIsRegistrationEnabled asynIsRegistrationEnabled = new AsynIsRegistrationEnabled();
            asynIsRegistrationEnabled.executeOnExecutor(threadPoolExecutor);
        }

        protected void onPreExecute() {

        }
    }

    //subhashree genre

    private class AsynGetGenreList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        ArrayList<String> genreArrayList = new ArrayList<String>();
        ArrayList<String> genreValueArrayList = new ArrayList<String>();

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList =Util.rootUrl().trim()+Util.getGenreListUrl.trim();

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpGet httppost = new HttpGet(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                } catch (UnsupportedEncodingException e) {

                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                }catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                }
                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("genre_list");

                        int lengthJsonArr = jsonMainNode.length();
                        if (lengthJsonArr > 0){
                            genreArrayList.add(0,Util.getTextofLanguage(Login.this,Util.FILTER_BY,Util.DEFAULT_FILTER_BY));


                            genreValueArrayList.add(0,"");

                        }
                        for(int i=0; i < lengthJsonArr; i++) {
                            genreArrayList.add(jsonMainNode.get(i).toString());
                            genreValueArrayList.add(jsonMainNode.get(i).toString());


                        }

                        if (genreArrayList.size() > 1){

                            genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this,Util.SORT_BY,Util.DEFAULT_SORT_BY));
                            genreValueArrayList.add(genreValueArrayList.size(),"");


                            genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_LAST_UPLOADED, Util.DEFAULT_SORT_LAST_UPLOADED));
                            genreValueArrayList.add(genreValueArrayList.size(),"lastupload");

                            genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_RELEASE_DATE, Util.DEFAULT_SORT_RELEASE_DATE));
                            genreValueArrayList.add(genreValueArrayList.size(),"releasedate");

                            genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_ALPHA_A_Z, Util.DEFAULT_SORT_ALPHA_A_Z));
                            genreValueArrayList.add(genreValueArrayList.size(),"sortasc");

                            genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_ALPHA_Z_A, Util.DEFAULT_SORT_ALPHA_Z_A));
                            genreValueArrayList.add(genreValueArrayList.size(),"sortdesc");






                        }

                    }
                    else{
                        responseStr = "0";

                    }
                }

            }
            catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });



            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){

            }else{
                if (status > 0 && status == 200) {
                    genreArrToSend = new String[genreArrayList.size()];
                    genreArrToSend = genreArrayList.toArray(genreArrToSend);


                    genreValueArrayToSend = new String[genreValueArrayList.size()];
                    genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);




                }else{
                   /* genreArrToSend = new String[0];
                    genreArrToSend = genreArrayList.toArray(genreArrToSend);


                    genreValueArrayToSend = new String[0];
                    genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);*/


                    genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_BY, Util.DEFAULT_SORT_BY));
                    genreValueArrayList.add(genreValueArrayList.size(),"");


                    genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_LAST_UPLOADED, Util.DEFAULT_SORT_LAST_UPLOADED));
                    genreValueArrayList.add(genreValueArrayList.size(),"lastupload");

                    genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_RELEASE_DATE, Util.DEFAULT_SORT_RELEASE_DATE));
                    genreValueArrayList.add(genreValueArrayList.size(),"releasedate");

                    genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_ALPHA_A_Z, Util.DEFAULT_SORT_ALPHA_A_Z));
                    genreValueArrayList.add(genreValueArrayList.size(),"sortasc");

                    genreArrayList.add(genreArrayList.size(),Util.getTextofLanguage(Login.this, Util.SORT_ALPHA_Z_A, Util.DEFAULT_SORT_ALPHA_Z_A));
                    genreValueArrayList.add(genreValueArrayList.size(),"sortdesc");

                    genreArrToSend = new String[genreArrayList.size()];
                    genreArrToSend = genreArrayList.toArray(genreArrToSend);


                    genreValueArrayToSend = new String[genreValueArrayList.size()];
                    genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);


                }

            }

            SharedPreferences.Editor isLoginPrefEditor = isLoginPref.edit();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < genreArrToSend.length; i++) {
                sb.append(genreArrToSend[i]).append(",");
            }
            isLoginPrefEditor.putString(Util.GENRE_ARRAY_PREF_KEY, sb.toString());
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < genreValueArrayToSend.length; i++) {
                sb1.append(genreValueArrayToSend[i]).append(",");
            }
            isLoginPrefEditor.putString(Util.GENRE_VALUES_ARRAY_PREF_KEY, sb1.toString());
            isLoginPrefEditor.commit();


            //============================Added For FCM===========================//


            if(!Util.getTextofLanguage(Login.this,Util.GOOGLE_FCM_TOKEN,Util.DEFAULT_GOOGLE_FCM_TOKEN).equals("0"))
            {



                Intent i = new Intent(Login.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
                overridePendingTransition(0,0);
            }
            else
            {
                GoogleIdGeneraterTimer = new Timer();
                GoogleIdGeneraterTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!Util.getTextofLanguage(Login.this,Util.GOOGLE_FCM_TOKEN,Util.DEFAULT_GOOGLE_FCM_TOKEN).equals("0"))
                        {
                            GoogleIdGeneraterTimer.cancel();
                            GoogleIdGeneraterTimer.purge();



                            Intent i = new Intent(Login.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            finish();
                            overridePendingTransition(0,0);
                        }
                    }
                },0,1000);
            }

            //============================End Added For FCM===========================//

        }
        protected void onPreExecute() {

        }
    }


    private class AsynIsRegistrationEnabled extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int statusCode;
        private int isLogin = 0;
        private String IsMyLibrary = "0";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl()+Util.isRegistrationEnabledurl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }

                    });

                }catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }

                    });
                    e.printStackTrace();
                }


                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));

                }

                if (statusCode > 0) {
                    if (statusCode == 200) {
                        if ((myJson.has("is_login")) && myJson.getString("is_login").trim() != null && !myJson.getString("is_login").trim().isEmpty() && !myJson.getString("is_login").trim().equals("null") && !myJson.getString("is_login").trim().matches("")) {
                            isLogin = Integer.parseInt(myJson.getString("is_login"));


                            //Adder Later By Bibhu
                            //This code is used for the 'My Library Feature'

                            if(isLogin == 1)

                            {
                                if((myJson.optString("isMylibrary")).trim().equals("1"))
                                {
                                    IsMyLibrary = "1";
                                }
                            }
                            else
                            {
                                IsMyLibrary = "0";
                            }

                        } else {
                            isLogin = 0;
                        }

                    }else{
                        isLogin = 0;
                    }
                }
                else {
                    responseStr = "0";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isLogin = 0;


                        }
                    });
                }
            } catch (JSONException e1) {
                try {
                }
                catch(IllegalArgumentException ex)
                {
                    responseStr = "0";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isLogin = 0;


                        }
                    });
                    e1.printStackTrace();
                }
            }

            catch (Exception e)
            {
                try {

                }
                catch(IllegalArgumentException ex)
                {
                    responseStr = "0";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isLogin = 0;


                        }
                    });
                    e.printStackTrace();
                }

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            try {

            }catch (IllegalArgumentException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        isLogin = 0;



                    }
                });
            }

            if(responseStr == null) {
                isLogin = 0;


            }
            if ((responseStr.trim().equalsIgnoreCase("0"))){
                isLogin = 0;

            }

            Util.setLanguageSharedPrefernce(Login.this,Util.IS_MYLIBRARY,IsMyLibrary);
            SharedPreferences.Editor isLoginPrefEditor = isLoginPref.edit();
            isLoginPrefEditor.putInt(Util.IS_LOGIN_PREF_KEY, isLogin);

            isLoginPrefEditor.commit();
            AsynGetLanguageList asynGetLanguageList = new AsynGetLanguageList();
            asynGetLanguageList.executeOnExecutor(threadPoolExecutor);


            /*AsynGetGenreList asynGetGenreList = new AsynGetGenreList();
            asynGetGenreList.executeOnExecutor(threadPoolExecutor);*/


        }

        @Override
        protected void onPreExecute() {

        }


    }
    private class AsynGetLanguageList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

//          String urlRouteList =Util.rootUrl().trim()+Util.LanguageList.trim();
            String urlRouteList = Util.rootUrl().trim()+Util.LanguageList.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);


                // Execute HTTP Post Request
                try {


                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
                } catch (Exception e) {
                }
                if (responseStr != null) {
                    JSONObject json = new JSONObject(responseStr);
                    try {
                        status = Integer.parseInt(json.optString("code"));
                        Default_Language = json.optString("default_lang");
                    } catch (Exception e) {
                        status = 0;
                    }
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        noInternetLayout.setVisibility(View.GONE);
                        geoBlockedLayout.setVisibility(View.VISIBLE);

                    }
                });
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {
                noInternetLayout.setVisibility(View.GONE);
                geoBlockedLayout.setVisibility(View.VISIBLE);
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject json = new JSONObject(responseStr);
                        JSONArray jsonArray = json.getJSONArray("lang_list");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            String language_id = jsonArray.getJSONObject(i).optString("code").trim();
                            String language_name = jsonArray.getJSONObject(i).optString("language").trim();


                            LanguageModel languageModel = new LanguageModel();
                            languageModel.setLanguageId(language_id);
                            languageModel.setLanguageName(language_name);
                            if (Default_Language.equalsIgnoreCase(language_id)){
                                languageModel.setIsSelected(true);

                            }else {
                                languageModel.setIsSelected(false);
                            }

                            languageModels.add(languageModel);
                        }

                        Util.languageModel = languageModels;

                    } catch (JSONException e) {
                        e.printStackTrace();
                        noInternetLayout.setVisibility(View.GONE);
                        geoBlockedLayout.setVisibility(View.VISIBLE);
                    }
                    Util.languageModel = languageModels;

                    if(languageModels.size()==1)
                    {
                        SharedPreferences.Editor countryEditor = language_list_pref.edit();
                        countryEditor.putString("total_language","1");
                        countryEditor.commit();
                    }
                    if (Util.getTextofLanguage(Login.this,Util.SELECTED_LANGUAGE_CODE,"").equalsIgnoreCase("")){
                        Util.setLanguageSharedPrefernce(Login.this,Util.SELECTED_LANGUAGE_CODE,Default_Language);
                    }
                    AsynGetTransalatedLanguage asynGetGenreList = new AsynGetTransalatedLanguage();
                    asynGetGenreList.executeOnExecutor(threadPoolExecutor);
                   /* if(!Default_Language.equals("en")) {
                        //                  Call For Language Translation.
                        AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);

                    }else{
                        AsynGetGenreList asynGetGenreList = new AsynGetGenreList();
                        asynGetGenreList.executeOnExecutor(threadPoolExecutor);
                    }*/

                } else {
                    noInternetLayout.setVisibility(View.GONE);
                    geoBlockedLayout.setVisibility(View.VISIBLE);
                }
            }

        }

        protected void onPreExecute() {

        }
    }



    private class AsynGetTransalatedLanguage extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList =Util.rootUrl().trim()+Util.LanguageTranslation.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken",Util.authTokenStr);
                httppost.addHeader("lang_code",Default_Language);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = (EntityUtils.toString(response.getEntity())).trim();
                } catch (Exception e) {
                }
                if (responseStr != null) {
                    JSONObject json = new JSONObject(responseStr);
                    try {
                        status = Integer.parseInt(json.optString("code"));
                    } catch (Exception e) {
                        status = 0;
                    }
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        noInternetLayout.setVisibility(View.GONE);

                    }
                });
            }

            return null;
        }


        protected void onPostExecute(Void result) {



            if (responseStr == null) {
                noInternetLayout.setVisibility(View.GONE);
            } else {
                if (status > 0 && status == 200) {

                    try {
                        JSONObject parent_json = new JSONObject(responseStr);
                        JSONObject json = parent_json.getJSONObject("translation");


                        Util.setLanguageSharedPrefernce(Login.this,Util.ALREADY_MEMBER,json.optString("already_member").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ACTIAVTE_PLAN_TITLE,json.optString("activate_plan_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_STATUS_ACTIVE,json.optString("transaction_status_active").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ADD_TO_FAV,json.optString("add_to_fav").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ADDED_TO_FAV,json.optString("added_to_fav").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ENTER_EMPTY_FIELD,json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.ADVANCE_PURCHASE,json.optString("advance_purchase").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ALERT,json.optString("alert").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.EPISODE_TITLE,json.optString("episodes_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SORT_ALPHA_A_Z,json.optString("sort_alpha_a_z").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SORT_ALPHA_Z_A,json.optString("sort_alpha_z_a").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.AMOUNT,json.optString("amount").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.COUPON_CANCELLED,json.optString("coupon_cancelled").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.BUTTON_APPLY,json.optString("btn_apply").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SIGN_OUT_WARNING,json.optString("sign_out_warning").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.DISCOUNT_ON_COUPON,json.optString("discount_on_coupon").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.MY_LIBRARY, json.optString("my_library").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CREDIT_CARD_CVV_HINT,json.optString("credit_card_cvv_hint").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CAST,json.optString("cast").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CAST_CREW_BUTTON_TITLE,json.optString("cast_crew_button_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CENSOR_RATING,json.optString("censor_rating").trim());
                        if(json.optString("change_password").trim()==null || json.optString("change_password").trim().equals("")) {
                            Util.setLanguageSharedPrefernce(Login.this, Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD);
                        }
                        else {
                            Util.setLanguageSharedPrefernce(Login.this, Util.CHANGE_PASSWORD, json.optString("change_password").trim());
                        }
                        Util.setLanguageSharedPrefernce(Login.this, Util.CANCEL_BUTTON, json.optString("btn_cancel").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.RESUME_MESSAGE, json.optString("resume_watching").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.CONTINUE_BUTTON, json.optString("continue").trim());


                        Util.setLanguageSharedPrefernce(Login.this,Util.CONFIRM_PASSWORD,json.optString("confirm_password").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CREDIT_CARD_DETAILS,json.optString("credit_card_detail").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.DIRECTOR,json.optString("director").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.DOWNLOAD_BUTTON_TITLE,json.optString("download_button_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.DESCRIPTION,json.optString("description").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.HOME,json.optString("home").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.EMAIL_EXISTS,json.optString("email_exists").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.EMAIL_DOESNOT_EXISTS,json.optString("email_does_not_exist").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.EMAIL_PASSWORD_INVALID,json.optString("email_password_invalid").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.COUPON_CODE_HINT,json.optString("coupon_code_hint").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SEARCH_ALERT,json.optString("search_alert").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.CREDIT_CARD_NUMBER_HINT,json.optString("credit_card_number_hint").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TEXT_EMIAL,json.optString("text_email").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NAME_HINT,json.optString("name_hint").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CREDIT_CARD_NAME_HINT,json.optString("credit_card_name_hint").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TEXT_PASSWORD,json.optString("text_password").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.ERROR_IN_PAYMENT_VALIDATION,json.optString("error_in_payment_validation").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ERROR_IN_REGISTRATION,json.optString("error_in_registration").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_STATUS_EXPIRED,json.optString("transaction_status_expired").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.DETAILS_NOT_FOUND_ALERT,json.optString("details_not_found_alert").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.FAILURE,json.optString("failure").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.FILTER_BY,json.optString("filter_by").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.FORGOT_PASSWORD,json.optString("forgot_password").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.GENRE,json.optString("genre").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.AGREE_TERMS,json.optString("agree_terms").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.INVALID_COUPON,json.optString("invalid_coupon").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.INVOICE,json.optString("invoice").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.LANGUAGE_POPUP_LANGUAGE,json.optString("language_popup_language").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SORT_LAST_UPLOADED,json.optString("sort_last_uploaded").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.LANGUAGE_POPUP_LOGIN,json.optString("language_popup_login").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.LOGIN,json.optString("login").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.LOGOUT,json.optString("logout").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.LOGOUT_SUCCESS,json.optString("logout_success").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.MY_FAVOURITE,json.optString("my_favourite").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.NEW_PASSWORD,json.optString("new_password").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NEW_HERE_TITLE,json.optString("new_here_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NO,json.optString("no").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NO_DATA,json.optString("no_data").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NO_INTERNET_CONNECTION,json.optString("no_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.NO_INTERNET_NO_DATA,json.optString("no_internet_no_data").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NO_DETAILS_AVAILABLE,json.optString("no_details_available").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.BUTTON_OK,json.optString("btn_ok").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.OLD_PASSWORD,json.optString("old_password").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.OOPS_INVALID_EMAIL,json.optString("oops_invalid_email").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.ORDER,json.optString("order").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_DETAILS_ORDER_ID,json.optString("transaction_detail_order_id").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PASSWORD_RESET_LINK,json.optString("password_reset_link").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PASSWORDS_DO_NOT_MATCH,json.optString("password_donot_match").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PAY_BY_PAYPAL,json.optString("pay_by_paypal").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.BTN_PAYNOW,json.optString("btn_paynow").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PAY_WITH_CREDIT_CARD,json.optString("pay_with_credit_card").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PAYMENT_OPTIONS_TITLE,json.optString("payment_options_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PLAN_NAME,json.optString("plan_name").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO,json.optString("activate_subscription_watch_video").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.COUPON_ALERT,json.optString("coupon_alert").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.VALID_CONFIRM_PASSWORD,json.optString("valid_confirm_password").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PROFILE,json.optString("profile").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PROFILE_UPDATED,json.optString("profile_updated").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.PURCHASE,json.optString("purchase").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_DETAIL_PURCHASE_DATE,json.optString("transaction_detail_purchase_date").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.PURCHASE_HISTORY,json.optString("purchase_history").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.BTN_REGISTER,json.optString("btn_register").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SORT_RELEASE_DATE,json.optString("sort_release_date").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.SAVE_THIS_CARD,json.optString("save_this_card").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TEXT_SEARCH_PLACEHOLDER,json.optString("text_search_placeholder").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SEASON,json.optString("season").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SELECT_OPTION_TITLE,json.optString("select_option_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SELECT_PLAN,json.optString("select_plan").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.SIGN_UP_TITLE,json.optString("signup_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SLOW_INTERNET_CONNECTION,json.optString("slow_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SLOW_ISSUE_INTERNET_CONNECTION,json.optString("slow_issue_internet_connection").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SORRY,json.optString("sorry").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.GEO_BLOCKED_ALERT,json.optString("geo_blocked_alert").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.SIGN_OUT_ERROR,json.optString("sign_out_error").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ALREADY_PURCHASE_THIS_CONTENT,json.optString("already_purchase_this_content").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CROSSED_MAXIMUM_LIMIT,json.optString("crossed_max_limit_of_watching").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SORT_BY,json.optString("sort_by").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.STORY_TITLE,json.optString("story_title").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.BTN_SUBMIT,json.optString("btn_submit").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_STATUS,json.optString("transaction_success").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.VIDEO_ISSUE,json.optString("video_issue").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NO_CONTENT,json.optString("no_content").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.NO_VIDEO_AVAILABLE,json.optString("no_video_available").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY,json.optString("content_not_available_in_your_country").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_DATE,json.optString("transaction_date").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANASCTION_DETAIL,json.optString("transaction_detail").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION_STATUS,json.optString("transaction_status").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.TRANSACTION,json.optString("transaction").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.TRY_AGAIN,json.optString("try_again").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.UNPAID,json.optString("unpaid").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.USE_NEW_CARD,json.optString("use_new_card").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.VIEW_MORE,json.optString("view_more").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.VIEW_TRAILER,json.optString("view_trailer").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.WATCH,json.optString("watch").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.WATCH_NOW,json.optString("watch_now").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SIGN_OUT_ALERT,json.optString("sign_out_alert").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.UPDATE_PROFILE_ALERT,json.optString("update_profile_alert").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.YES,json.optString("yes").trim());

                        Util.setLanguageSharedPrefernce(Login.this,Util.PURCHASE_SUCCESS_ALERT,json.optString("purchase_success_alert").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.CARD_WILL_CHARGE,json.optString("card_will_charge").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.SEARCH_HINT,json.optString("search_hint").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.TERMS, json.optString("terms").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.APP_ON, json.optString("app_on").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.MESSAGE, json.optString("text_message").trim());


                        Util.setLanguageSharedPrefernce(Login.this, Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
                        Util.setLanguageSharedPrefernce(Login.this, Util.MESSAGE, json.optString("text_message").trim());


                        Util.setLanguageSharedPrefernce(Login.this,Util.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE,json.optString("logged_out_from_all_devices").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.ANDROID_VERSION,json.optString("android_version").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.MANAGE_DEVICE,json.optString("manage_device").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.YOUR_DEVICE,json.optString("your_device").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.DEREGISTER,json.optString("deregister").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.LOGIN_STATUS_MESSAGE,json.optString("oops_you_have_no_access").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.UPADTE_TITLE,json.optString("update_title").trim());
                        Util.setLanguageSharedPrefernce(Login.this,Util.UPADTE_MESSAGE,json.optString("update_message").trim());
                        Util.getTextofLanguage(Login.this, Util.PURCHASE, Util.DEFAULT_PURCHASE);
                        Util.setLanguageSharedPrefernce(Login.this, Util.SELECTED_LANGUAGE_CODE, Default_Language);

                        //Call For Language PopUp Dialog



                    } catch (JSONException e) {
                        e.printStackTrace();
                        noInternetLayout.setVisibility(View.GONE);
                    }
                    // Call For Other Methods.


                } else {
                    noInternetLayout.setVisibility(View.GONE);
                }
            }
            AsynGetGenreList asynGetGenreList = new AsynGetGenreList();
            asynGetGenreList.executeOnExecutor(threadPoolExecutor);



        }
        protected void onPreExecute() {

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

}
