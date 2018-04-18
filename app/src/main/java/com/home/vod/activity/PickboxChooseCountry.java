package com.home.vod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.vod.R;
import com.home.vod.model.LanguageModel;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_NEXT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YOUR_LOCATION;
import static com.home.vod.preferences.LanguagePreference.NEXT;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.YOUR_LOCATION;
import static com.home.vod.util.Constant.authTokenStr;

public class PickboxChooseCountry extends AppCompatActivity implements
        GetLanguageListAsynTask.GetLanguageListListener,
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener {
    WheelView wheelView;
    ImageView upArrow, downArrow;
    public static ArrayList<LanguageModel> languageModel = null;
    String Default_Language = "";
    LanguagePreference languagePreference;
    ArrayList<String> languages = new ArrayList<>();
    int defaultLanguageIndex = 0;
    Button submitButton;
    TextView headingText;
    int wheelViewOffset = 1;
    String intentType = null;
    private ProgressBarHandler pDialog = null;
    private RelativeLayout noInternetLayout;
    TextView noInternetTextView;
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    private int CHOOSE_COUNTRY=1001;
    String Response=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_picker);
        initViews();
        setLanguage();
        getPreviousIntentValue();
        checkForArrowVisibility();
        getList();
        setDefaultLanguage();
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollWheelView("up");
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollWheelView("down");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PickboxChooseCountry.this, ""+wheelView.getSeletedIndex()+" "+wheelView.getSeletedItem(), Toast.LENGTH_SHORT).show();
                submitButtonClickMethod();
            }
        });
    }

    private void setLanguage() {
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_NO_DATA, DEFAULT_NO_INTERNET_NO_DATA));
        submitButton.setText(languagePreference.getTextofLanguage(NEXT, DEFAULT_NEXT));
        headingText.setText(languagePreference.getTextofLanguage(YOUR_LOCATION,DEFAULT_YOUR_LOCATION));
    }

    private void checkForArrowVisibility() {
        if (wheelView.getSeletedIndex() == 0) {
            upArrow.setVisibility(View.INVISIBLE);
            downArrow.setVisibility(View.VISIBLE);
        } else if (wheelView.getSeletedIndex() == languages.size() ) {
            downArrow.setVisibility(View.INVISIBLE);
            upArrow.setVisibility(View.VISIBLE);
        }
    }

    private void getPreviousIntentValue() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            intentType = b.getString("type");
        }
    }

    private void submitButtonClickMethod() {
        getWheelViewIndexValue(wheelView.getSeletedIndex());
        LanguageListInputModel languageListInputModel = new LanguageListInputModel();
        languageListInputModel.setLangCode(Default_Language);
        languageListInputModel.setAuthToken(authTokenStr);
        GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel, PickboxChooseCountry.this, PickboxChooseCountry.this);
        asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
    }

    private void scrollWheelView(String type) {
        switch (type) {
            case "up":
                if (wheelView.getSeletedIndex() <= 0) {
                    upArrow.setVisibility(View.INVISIBLE);
                    downArrow.setVisibility(View.VISIBLE);
                } else {
                    wheelView.setSeletion(wheelView.getSeletedIndex() - 1);
                    downArrow.setVisibility(View.VISIBLE);
                }

                //   Toast.makeText(this, ""+wheelView.getSeletedIndex()+" "+wheelView.getSeletedItem(), Toast.LENGTH_SHORT).show();
                break;
            case "down":
                if (wheelView.getSeletedIndex() >= languages.size() -1 ) {
                    downArrow.setVisibility(View.INVISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                } else {
                    wheelView.setSeletion(wheelView.getSeletedIndex() + 1);
                    upArrow.setVisibility(View.VISIBLE);
                }

                // Toast.makeText(this, ""+wheelView.getSeletedIndex()+" "+wheelView.getSeletedItem(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getWheelViewIndexValue(int seletedIndex) {
        Util.itemclicked = true;
        for (int i=0; i<languageModel.size();i++){
            languageModel.get(i).setIsSelected(false);
        }

        languageModel.get(seletedIndex).setIsSelected(true);

        Default_Language = languageModel.get(seletedIndex).getLanguageId();

    }

    private void setDefaultLanguage() {

        Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
    }

    private void wheelViewSetup() {

        wheelView.setOffset(wheelViewOffset);
        wheelView.setAlpha((float) 0.9);
        wheelView.setSeletion(defaultLanguageIndex);
        wheelView.setItems(languages);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                //checkForArrowVisibility();
                if (selectedIndex-wheelViewOffset == 0) {
                    upArrow.setVisibility(View.INVISIBLE);
                    downArrow.setVisibility(View.VISIBLE);
                } else if (selectedIndex-wheelViewOffset == languages.size()-1 ) {
                    downArrow.setVisibility(View.INVISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                }else{
                    downArrow.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                }
                // selectedWheelIndex= selectedIndex;
                Log.d("wheelView", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
    }

    private void initViews() {
        wheelView = (WheelView) findViewById(R.id.loop_view);
        upArrow = (ImageView) findViewById(R.id.upArrow);
        downArrow = (ImageView) findViewById(R.id.downArrow);
        submitButton = (Button) findViewById(R.id.submitButton);
        noInternetLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        languagePreference = LanguagePreference.getLanguagePreference(this);
        noInternetLayout.setVisibility(View.GONE);
        headingText= (TextView)findViewById(R.id.headingText);
    }

    /* public List getList() {
         ArrayList<String> list = new ArrayList<>();

         for (int i = 0; i < 50; i++) {
             list.add("Country "+i);
         }

         return list;
     }*/
    public void getList() {
        if (languageModel != null && languageModel.size() > 0) {
            for (int i = 0; i < languageModel.size(); i++) {
                languages.add(languageModel.get(i).getLanguageName());
                getDefaultLanguageIndex(languageModel);
            }
            wheelViewSetup();

        } else {
            LanguageListInputModel languageListInputModel = new LanguageListInputModel();
            languageListInputModel.setAuthToken(authTokenStr);
            GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
            asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
        }

    }

    private void getDefaultLanguageIndex(ArrayList<LanguageModel> languageModel) {
        for (int i = 0; i < languageModel.size(); i++) {
            if (languageModel.get(i).getIsSelected()) {
                defaultLanguageIndex = i;
            }
        }
    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {
        pDialog = new ProgressBarHandler(PickboxChooseCountry.this);
        pDialog.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;

        }
        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();

        for (int i = 0; i < languageListOutputArray.size(); i++) {
            String language_id = languageListOutputArray.get(i).getLanguageCode();
            String language_name = languageListOutputArray.get(i).getLanguageName();


            LanguageModel languageModel = new LanguageModel();
            languageModel.setLanguageId(language_id);
            languageModel.setLanguageName(language_name);


            if (Default_Language.equalsIgnoreCase(language_id)) {
                languageModel.setIsSelected(true);
            } else {
                languageModel.setIsSelected(false);
            }
            languageModels.add(languageModel);
        }

        languageModel = languageModels;
        for (int i = 0; i < languageModels.size(); i++) {
            languages.add(languageModel.get(i).getLanguageName());
        }
        getDefaultLanguageIndex(languageModel);
        wheelViewSetup();
    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        pDialog = new ProgressBarHandler(PickboxChooseCountry.this);
        pDialog.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;

        }
        if (status > 0 && status == 200) {

            try {
                Response=jsonResponse;
                Util.parseLanguage(languagePreference, jsonResponse, Default_Language);
                languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, Default_Language);
                intentToDesiredScreen(intentType);


            } catch (JSONException e) {
                e.printStackTrace();
                noInternetLayout.setVisibility(View.GONE);
            }
            // Call For Other Methods.


        } else {
            noInternetLayout.setVisibility(View.GONE);
        }

    }

    private void intentToDesiredScreen(String intentType) {
        Intent intent;
        switch (intentType) {
            case "login":
                intent = new Intent(PickboxChooseCountry.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                //finish();
                break;
            case "register":
                intent = new Intent(PickboxChooseCountry.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
              //  finish();
                break;
            case "skip":
                intent = new Intent(PickboxChooseCountry.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
               // finish();
                break;
            case "other":
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",Response);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
        }
    }

}
