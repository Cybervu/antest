package com.home.vod.activity;


import android.os.Bundle;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.home.apisdk.apiController.GetIpAddressAsynTask;
import com.home.apisdk.apiController.GetVideoLogsAsynTask;
import com.home.apisdk.apiModel.VideoLogsInputModel;
import com.home.vod.R;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.SensorOrientationChangeNotifier;
import com.home.vod.util.Util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class YouTubeAPIActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, SensorOrientationChangeNotifier.Listener, ActionBarOverlayLayout.ActionBarVisibilityCallback,
        GetIpAddressAsynTask.IpAddress, GetVideoLogsAsynTask.GetVideoLogs {

    Toolbar mActionBarToolbar;

    String ipAddressStr = "";
    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    String userIdStr ="";
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    YouTubePlayerFragment fragmentYoutube;
    GetIpAddressAsynTask asynGetIpAddress;
    GetVideoLogsAsynTask asyncVideoLogDetails;
    private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyDy9dfNlSYnHlUsM28ayyPH7a7dMIfFoYg-0";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_api);
        fragmentYoutube = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);

        fragmentYoutube.initialize(YoutubeDeveloperKey, this);
        preferenceManager = PreferenceManager.getPreferenceManager(this);

        if (Util.dataModel.getVideoUrl().matches("")) {
            onBackPressed();
        }

        asynGetIpAddress = new GetIpAddressAsynTask(this, this);
        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);

    }


    public boolean isValidWord(String w) {
        return w.matches("[?@%#~.$&^*]");

    }

    String replaceString(String string) {
        String tt = string.replaceAll("[?@%#~.$&^*]", ",");
        String[] hh = tt.split(",");
        String id = hh[0];

        String[] tempStr = string.replaceAll("[?@%#~.$&^*]", ",").split(",");
        return tempStr[0];
    }

    @Override
    protected void onUserLeaveHint() {
        if (asynGetIpAddress != null) {
            asynGetIpAddress.cancel(true);
        }
        if (asyncVideoLogDetails != null) {
            asyncVideoLogDetails.cancel(true);
        }
        SensorOrientationChangeNotifier.getInstance(YouTubeAPIActivity.this).remove(this);
        finish();
        overridePendingTransition(0, 0);
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (asynGetIpAddress != null) {
            asynGetIpAddress.cancel(true);
        }
        if (asyncVideoLogDetails != null) {
            asyncVideoLogDetails.cancel(true);
        }
        SensorOrientationChangeNotifier.getInstance(YouTubeAPIActivity.this).remove(this);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {

    }

    @Override
    public void showForSystem() {

    }

    @Override
    public void hideForSystem() {

    }

    @Override
    public void enableContentAnimations(boolean enable) {

    }

    @Override
    public void onContentScrollStarted() {

    }

    @Override
    public void onContentScrollStopped() {

    }

    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        if (!ipAddressStr.matches("")) {
            VideoLogsInputModel videoLogsInputModel = new VideoLogsInputModel();
            videoLogsInputModel.setAuthToken(Util.authTokenStr);
            videoLogsInputModel.setIpAddress(ipAddressStr.trim());
            videoLogsInputModel.setMuviUniqueId(Util.dataModel.getMovieUniqueId().trim());
            videoLogsInputModel.setEpisodeStreamUniqueId(Util.dataModel.getEpisode_id().trim());
            if (preferenceManager!=null){
                userIdStr = preferenceManager.getUseridFromPref();
            }else{
                userIdStr="";

            }
            videoLogsInputModel.setUserId(userIdStr.trim());
            videoLogsInputModel.setPlayedLength("0");
            videoLogsInputModel.setWatchStatus("start");
            videoLogsInputModel.setDeviceType("2");
            videoLogsInputModel.setVideoLogId("0");
            asyncVideoLogDetails = new GetVideoLogsAsynTask(videoLogsInputModel,this,this);
            asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
        } else {
            return;
        }
        return;
    }

    @Override
    public void onGetVideoLogsPreExecuteStarted() {

    }

    @Override
    public void onGetVideoLogsPostExecuteCompleted(int status, String message, String videoLogId) {
        return;
    }

//    private class AsynGetIpAddress extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//
//                // Execute HTTP Post Request
//                try {
//                    URL myurl = new URL(Util.loadIPUrl);
//                    HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
//                    InputStream ins = con.getInputStream();
//                    InputStreamReader isr = new InputStreamReader(ins);
//                    BufferedReader in = new BufferedReader(isr);
//
//                    String inputLine;
//
//                    while ((inputLine = in.readLine()) != null)
//                    {
//                        System.out.println(inputLine);
//                        responseStr = inputLine;
//                    }
//
//                    in.close();
//
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    ipAddressStr = "";
//
//                } catch (UnsupportedEncodingException e) {
//
//                    ipAddressStr = "";
//
//                }catch (IOException e) {
//                    ipAddressStr = "";
//
//                }
//                if(responseStr!=null){
//                    Object json = new JSONTokener(responseStr).nextValue();
//                    if (json instanceof JSONObject){
//                        ipAddressStr = ((JSONObject) json).getString("ip");
//
//                    }
//
//                }
//
//            }
//            catch (Exception e) {
//                ipAddressStr = "";
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
//                ipAddressStr = "";
//            }
//            if (!ipAddressStr.matches("")) {
//                asyncVideoLogDetails = new AsyncVideoLogDetails();
//                asyncVideoLogDetails.executeOnExecutor(threadPoolExecutor);
//            }else{
//                return;
//            }
//            return;
//        }
//
//        protected void onPreExecute() {
//
//        }
//    }


//    private class AsyncVideoLogDetails extends AsyncTask<Void, Void, Void> {
//        //  ProgressDialog pDialog;
//        String responseStr;
//        String userIdStr ="";
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String urlRouteList = Util.rootUrl().trim()+Util.videoLogUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                SharedPreferences pref = getSharedPreferences(Util.LOGIN_PREF, 0);
//                if (pref!=null){
//                    userIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
//                }else{
//                    userIdStr="";
//
//                }
//                httppost.addHeader("user_id", userIdStr.trim());
//                httppost.addHeader("ip_address", ipAddressStr.trim());
//                httppost.addHeader("movie_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("episode_id", Util.dataModel.getEpisode_id().trim());
//                httppost.addHeader("played_length", "0");
//                httppost.addHeader("watch_status", "start");
//                httppost.addHeader("device_type", "2");
//                httppost.addHeader("log_id", "0");
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//
//
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//            catch (Exception e) {
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
//            return;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//
//    }


    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubeDeveloperKey, this);
        }
    }*/

  /*  protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeplayerfragment);
    }
*/
    @Override
    public void onInitializationSuccess(Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        YPlayer = player;
 /*
 * Now that this variable YPlayer is global you can access it
 * throughout the activity, and perform all the player actions like
 * play, pause and seeking to a position by code.
 */
       // YPlayer.loadVideo(videoUrlStr);
        if (!wasRestored) {
            //YPlayer.cueVideo(videoUrlStr);
            YPlayer.loadVideo(Util.dataModel.getVideoUrl());


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorOrientationChangeNotifier.getInstance(YouTubeAPIActivity.this).addListener(this);

    }

    @Override
    public void onOrientationChange(int orientation) {

        if (orientation == 90){


            if (YPlayer!=null) {
                YPlayer.setFullscreen(true);
            }
            // Do some landscape stuff
        }
        if (orientation == 270){
            if (YPlayer!=null) {
                YPlayer.setFullscreen(true);
            }

            // Do some landscape stuff
        }  if (orientation == 180){
            if (YPlayer!=null) {
                YPlayer.setFullscreen(false);

            }

            // Do some landscape stuff
        } if (orientation == 0) {

            if (YPlayer!=null) {
                YPlayer.setFullscreen(false);
            }

        }
    }

}