package player.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.home.vod.R;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


public class  ThirdPartyPlayer extends ActionBarActivity {
    WebView mWebView;
    Player playerModel;
   // Toolbar mActionBarToolbar;
    private ProgressBarHandler asyncpDialog;
    String frameVideo = "";
    String ipAddressStr = "";
    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    AsynGetIpAddress asynGetIpAddress;
    AsyncVideoLogDetails asyncVideoLogDetails;

    String VideoUrl;
    String Playble_VideoUrl;
    int count = 0;

    @SuppressLint("JavascriptInterface")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_third_party_player);
        playerModel=new Player();
        playerModel = (Player) getIntent().getSerializableExtra("PlayerModel");
        LogUtil.showLog("BISHALK","ThirdpartyUrl=="+playerModel.getVideoUrl());

        hideSystemUI();


        if (playerModel.getVideoUrl().matches("")){
            finish();
            overridePendingTransition(0, 0);
        }

        VideoUrl = playerModel.getVideoUrl();


        if(VideoUrl.startsWith("//")){
            Playble_VideoUrl = "http:"+VideoUrl;
        }else if(!VideoUrl.startsWith("http")){
            Playble_VideoUrl = "http://"+VideoUrl;
        }else {
            Playble_VideoUrl = VideoUrl;
        }



        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
       // mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {



                if (progress == 100) {
                    if (asyncpDialog != null && asyncpDialog.isShowing()) {
                        asyncpDialog.hide();
                        asyncpDialog = null;
                    }
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                try {
                Log.v("MUVI12","loaded");
                    emulateClick(view);

                    Log.v("MUVI12","clicked=======");


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                if(count <1){
                    count = count + 1;

                    if(VideoUrl.startsWith("//")){
                        Playble_VideoUrl = "https:"+VideoUrl;
                    }else if(!VideoUrl.startsWith("http")){
                        Playble_VideoUrl = "https://"+VideoUrl;
                    }else {
                        Playble_VideoUrl = VideoUrl;
                    }



                    asyncpDialog = new ProgressBarHandler(ThirdPartyPlayer.this);
                    asyncpDialog.show();

                    mWebView.loadUrl(Playble_VideoUrl.trim());

                }else {
                    onBackPressed();
                }



            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

        });
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setBackgroundColor(0);
        mWebView.setBackgroundColor(Color.parseColor("#000000"));

        asyncpDialog = new ProgressBarHandler(ThirdPartyPlayer.this);
        asyncpDialog.show();

        mWebView.loadUrl(Playble_VideoUrl.trim());

        asynGetIpAddress = new AsynGetIpAddress();
        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

    }




    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
        String responseStr;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                // Execute HTTP Post Request
                try {
                    URL myurl = new URL(Player.loadIPUrl);
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

                } catch (UnsupportedEncodingException e) {

                    ipAddressStr = "";

                }catch (IOException e) {
                    ipAddressStr = "";

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

            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){
                ipAddressStr = "";
            }
            if (!ipAddressStr.matches("")) {
                asyncVideoLogDetails = new AsyncVideoLogDetails();
                asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
            }else{
                return;
            }
            return;
        }

        protected void onPreExecute() {

        }
    }


    private class AsyncVideoLogDetails extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog pDialog;
        String responseStr;
        String userIdStr ="";
        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = playerModel.getRootUrl().trim() + "videoLogs";
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", playerModel.getAuthTokenStr().trim());
               /* SharedPreferences pref = getSharedPreferences(Util.LOGIN_PREF, 0);
                if (pref!=null){
                    userIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
                }else{
                    userIdStr="";

                }*/
                if (playerModel.getUserId() != null && playerModel.getUserId().trim().matches("")){
                    userIdStr = playerModel.getUserId();
                }
                httppost.addHeader("user_id", userIdStr.trim());
                httppost.addHeader("ip_address", ipAddressStr.trim());
                httppost.addHeader("movie_id", playerModel.getMovieUniqueId().trim());
                httppost.addHeader("episode_id", playerModel.getEpisode_id().trim());
                httppost.addHeader("played_length", "0");
                httppost.addHeader("watch_status", "start");
                httppost.addHeader("device_type", "2");
                httppost.addHeader("log_id", "0");

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){


                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
            catch (Exception e) {

            }

            return null;
        }


        protected void onPostExecute(Void result) {


            return;

        }

        @Override
        protected void onPreExecute() {

        }


    }


    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }




  /*  public class WebAppInterface {
        Context mContext;

        *//** Instantiate the interface and set the context *//*
        WebAppInterface(Context c) {
            mContext = c;
        }

        *//** Show a toast from the web page *//*
        @JavascriptInterface
        public void webViewFullscreen(){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
        @JavascriptInterface
        public void webViewFullscreenExit() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }


    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.loadUrl("about:blank");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (asyncpDialog != null && asyncpDialog.isShowing()) {
                    asyncpDialog.hide();
                    asyncpDialog = null;
                }
            }
        });
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (asynGetIpAddress!=null){
            asynGetIpAddress.cancel(true);
        }
        if (asyncVideoLogDetails!=null){
            asyncVideoLogDetails.cancel(true);
        }
        mWebView.loadUrl("about:blank");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (asyncpDialog != null && asyncpDialog.isShowing()) {
                    asyncpDialog.hide();
                    asyncpDialog = null;
                }
            }
        });
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onUserLeaveHint()
    {
        mWebView.loadUrl("about:blank");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (asyncpDialog != null && asyncpDialog.isShowing()) {
                    asyncpDialog.hide();
                    asyncpDialog = null;
                }
            }
        });
        if (asynGetIpAddress!=null){
            asynGetIpAddress.cancel(true);
        }
        if (asyncVideoLogDetails!=null){
            asyncVideoLogDetails.cancel(true);
        }
        finish();
        overridePendingTransition(0, 0);
        super.onUserLeaveHint();

    }

    private void emulateClick(final WebView webview) {
        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webview.getLeft() + webview.getWidth()/2; //in the middle of the webview
        float y = webview.getTop() + webview.getHeight()/2;

        final MotionEvent downEvent = MotionEvent.obtain( downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0 );
        // change the position of touch event, otherwise, it'll show the menu.
        final MotionEvent upEvent = MotionEvent.obtain( downTime, downTime+ delta, MotionEvent.ACTION_UP, x+10, y+10, 0 );

        webview.post(new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(downEvent);
                    webview.dispatchTouchEvent(upEvent);
                }
            }
        });
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
