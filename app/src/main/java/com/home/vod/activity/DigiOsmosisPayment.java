package com.home.vod.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.home.apisdk.apiController.HeaderConstants;
import com.home.vod.R;
import com.home.vod.util.ProgressBarHandler;

import java.net.URL;

public class DigiOsmosisPayment extends AppCompatActivity {

    WebView webView;
    boolean returnValue = false;
    ProgressBarHandler progressBarHandler;
    ProgressBar progressBar;

    String authToken = "d2896f37d78aeed68f17a1186cd8db2c";
    String plan_id = "ec308b0a889b64c850dc1ec73da39eef";
    String user_id = "1530";
    String studio_id = "403";
    String curriency_id = "68";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digi_osmosis_payment);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {
                try {

                } catch (Exception e) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                if (progress == 100) {
                    try {
                        progressBar.setVisibility(View.GONE);

                    } catch (Exception e) {
                    }

                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });

        final MyJavaScriptInterface myJavaScriptInterface = new MyJavaScriptInterface(DigiOsmosisPayment.this);
        webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

        webView.setFocusableInTouchMode(true);
        webView.requestFocus();

        String authToken = "d2896f37d78aeed68f17a1186cd8db2c";
        String plan_id = "ec308b0a889b64c850dc1ec73da39eef";
        String user_id = "1530";
        String studio_id = "403";
        String curriency_id = "68";


        try{
            URL url = new URL("https://billing.edocent.com/rest/ccavenueSubscription");
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("authToken", authToken)
                    .appendQueryParameter("plan_id",plan_id)
                    .appendQueryParameter("user_id",user_id)
                    .appendQueryParameter("studio_id",studio_id)
                    .appendQueryParameter("curriency_id",curriency_id);
            String query = (builder.build().getEncodedQuery()).replaceAll("%40","@");
            webView.loadUrl(url+"?"+query);
        }catch (Exception e){}


//        webView.loadUrl("https://billing.edocent.com/rest/ccavenueSubscription?authToken='"+authToken+"'&plan_id='"+plan_id+"'&user_id='"+user_id+"'&studio_id='"+studio_id+"'&curriency_id='"+curriency_id+"'");
//        webView.loadUrl("https://billing.edocent.com/rest/ccavenueSubscription?authToken=d2896f37d78aeed68f17a1186cd8db2c&plan_id=ec308b0a889b64c850dc1ec73da39eef&user_id=1530&studio_id=403&curriency_id=68");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @android.webkit.JavascriptInterface
        public void getResponse(String toast){
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
        }
    }

}
