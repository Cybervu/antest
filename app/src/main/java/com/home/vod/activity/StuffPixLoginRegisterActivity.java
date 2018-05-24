package com.home.vod.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.home.vod.R;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.ProgressBarHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGIN;


public class StuffPixLoginRegisterActivity extends AppCompatActivity {


    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    Toolbar mActionBarToolbar;

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    // Added For FCM

    TextView logout_text;
    Button ok, cancel;
    PreferenceManager preferenceManager;
    LanguagePreference languagePreference;
    FeatureHandler featureHandler;
    ProgressBarHandler pDialog;
    WebView stuff_webView;
    String LoadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.app_background);

        setContentView(R.layout.stuffpix_activity_login);

        stuff_webView = (WebView) findViewById(R.id.stuff_webView);

        pDialog = new ProgressBarHandler(StuffPixLoginRegisterActivity.this);
        languagePreference = LanguagePreference.getLanguagePreference((this));
        featureHandler = FeatureHandler.getFeaturePreference(StuffPixLoginRegisterActivity.this);


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mActionBarToolbar.setTitle(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN));
        mActionBarToolbar.setTitle(getIntent().getStringExtra("titel"));
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitleColor));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stuff_webView.canGoBack()){
                    stuff_webView.goBack();
                }else {
                    finish();
                }

            }
        });


        stuff_webView.getSettings().setJavaScriptEnabled(true);
        stuff_webView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");

        stuff_webView.clearHistory();
        stuff_webView.clearFormData();
        stuff_webView.clearCache(true);
        stuff_webView.clearView();

        stuff_webView.loadUrl(getIntent().getStringExtra("LoadUrl"));
//      stuff_webView.loadUrl("https://player.edocent.com/OpenidConnect/OpenidConnectLogin?openid_device_type=2");

        stuff_webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO show you progress image
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementById('openid_response').innerText);");
            }

        });



        stuff_webView.setWebChromeClient(new WebChromeClient() {
            private ProgressDialog mProgress;

            @Override
            public void onProgressChanged(WebView view, int progress) {


                try{
                    if (mProgress == null) {
                        mProgress = new ProgressDialog(StuffPixLoginRegisterActivity.this);
                        mProgress.show();
                    }
                    mProgress.setMessage("Loading " + String.valueOf(progress) + "%");
                    if (progress == 100) {
                        mProgress.dismiss();
                        mProgress = null;
                    }
                }catch (Exception e){}
            }
        });


    }


    class MyJavaScriptInterface {

        public MyJavaScriptInterface() {
        }

        //API 17 and higher required you to add @JavascriptInterface as mandatory before your method.
        @JavascriptInterface
        public void processContent(String aContent) {
            final String content = aContent;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.putExtra("sutff_response",content);
                    setResult(RESULT_OK,intent);
                    finish();

//                    Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        if(stuff_webView.canGoBack()){
            stuff_webView.goBack();
        }else {
           finish();
        }
    }
}