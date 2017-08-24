/**
 * SDK initialization, platform and device information classes.
 */


package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.VideoLogsInputModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Muvi on 12/16/2016.
 * Class to get Video Logs details.
 */
public class GetVideoLogsAsynTask extends AsyncTask<VideoLogsInputModel, Void, Void> {

    private VideoLogsInputModel videoLogsInputModel;
    private String responseStr;
    private int status;
    private String message;
    private String PACKAGE_NAME;
    private String videoLogId = "";
    private GetVideoLogsListener listener;
    private Context context;

    /**
     * Interface used to allow the caller of a GetVideoLogsAsynTask to run some code when get
     * responses.
     */

    public interface GetVideoLogsListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */

        void onGetVideoLogsPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param status     Response Code From The Server
         * @param message    On Success Message
         * @param videoLogId For Getting the Video Log Id
         */

        void onGetVideoLogsPostExecuteCompleted(int status, String message, String videoLogId);
    }

    /**
     * Constructor to initialise the private data members.
     *
     * @param videoLogsInputModel A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                            For Example: to use this API we have to set following attributes:
     *                            setAuthToken(),setUserId() etc.
     * @param listener            GetVideoLogs Listener
     * @param context             android.content.Context
     */

    public GetVideoLogsAsynTask(VideoLogsInputModel videoLogsInputModel, GetVideoLogsListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.videoLogsInputModel = videoLogsInputModel;
        Log.v("MUVISDK", "LoginAsynTask");
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);

    }

    /**
     * Background thread to execute.
     *
     * @return null
     * @throws org.apache.http.conn.ConnectTimeoutException,IOException,JSONException
     */

    @Override
    protected Void doInBackground(VideoLogsInputModel... params) {


        try {

            // Execute HTTP Post Request
            try {
                URL url = new URL(APIUrlConstant.getVideoLogsUrl());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter(HeaderConstants.AUTH_TOKEN, this.videoLogsInputModel.getAuthToken())
                        .appendQueryParameter(HeaderConstants.USER_ID, this.videoLogsInputModel.getUserId())
                        .appendQueryParameter(HeaderConstants.IP_ADDRESS, this.videoLogsInputModel.getIpAddress())
                        .appendQueryParameter(HeaderConstants.MOVIE_ID, this.videoLogsInputModel.getMuviUniqueId())
                        .appendQueryParameter(HeaderConstants.EPISODE_ID, this.videoLogsInputModel.getEpisodeStreamUniqueId())
                        .appendQueryParameter(HeaderConstants.PLAYED_LENGTH, this.videoLogsInputModel.getPlayedLength())
                        .appendQueryParameter(HeaderConstants.WATCH_STATUS, this.videoLogsInputModel.getWatchStatus())
                        .appendQueryParameter(HeaderConstants.DEVICE_TYPE, this.videoLogsInputModel.getDeviceType())
                        .appendQueryParameter(HeaderConstants.LOG_ID, this.videoLogsInputModel.getVideoLogId());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                InputStream ins = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    responseStr = inputLine;
                    Log.v("MUVISDK", "responseStr" + responseStr);

                }
                in.close();


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

                status = 0;
                message = "Error";


            } catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject mainJson = null;
            if (responseStr != null) {
                mainJson = new JSONObject(responseStr);
                status = Integer.parseInt(mainJson.optString("code"));


                if ((mainJson.has("log_id")) && mainJson.optString("log_id").trim() != null && !mainJson.optString("log_id").trim().isEmpty() && !mainJson.optString("log_id").trim().equals("null") && !mainJson.optString("log_id").trim().matches("")) {
                    ;
                    videoLogId = mainJson.optString("log_id");
                }

            } else {
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";
        } catch (Exception e) {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetVideoLogsPreExecuteStarted();

        status = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api())) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetVideoLogsPostExecuteCompleted(status, message, videoLogId);
            return;
        }
        if (SDKInitializer.getHashKey().equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetVideoLogsPostExecuteCompleted(status, message, videoLogId);
        }

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetVideoLogsPostExecuteCompleted(status, message, videoLogId);

    }

}
