package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.FFVideoLogDetailsInput;

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
 * Created by MUVI on 7/5/2017.
 */

public class GetFFVideoLogDetailsAsync extends AsyncTask<FFVideoLogDetailsInput, Void, Void> {
    public FFVideoLogDetailsInput ffVideoLogDetailsInput;
    String responseStr,message;
    int code;
    String PACKAGE_NAME, videoLogId = "";


    public interface GetFFVideoLogs {
        void onGetFFVideoLogsPreExecuteStarted();

        void onGetFFVideoLogsPostExecuteCompleted(int code, String status,String videoLogId);
    }

    private GetFFVideoLogs listener;
    private Context context;


    public GetFFVideoLogDetailsAsync(FFVideoLogDetailsInput videoBufferLogsInputModel, GetFFVideoLogs listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.ffVideoLogDetailsInput = ffVideoLogDetailsInput;
        Log.v("MUVISDK", "LoginAsynTask");
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);

    }

    @Override
    protected Void doInBackground(FFVideoLogDetailsInput... params) {

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
                        .appendQueryParameter(HeaderConstants.AUTH_TOKEN, this.ffVideoLogDetailsInput.getAuthToken())
                        .appendQueryParameter(HeaderConstants.USER_ID, this.ffVideoLogDetailsInput.getUser_id())
                        .appendQueryParameter(HeaderConstants.IP_ADDRESS, this.ffVideoLogDetailsInput.getIp_address())
                        .appendQueryParameter(HeaderConstants.MOVIE_ID, this.ffVideoLogDetailsInput.getMovie_id())
                        .appendQueryParameter(HeaderConstants.EPISODE_ID, this.ffVideoLogDetailsInput.getEpisode_id())
                        .appendQueryParameter(HeaderConstants.PLAYED_LENGTH, this.ffVideoLogDetailsInput.getPlayed_length())
                        .appendQueryParameter(HeaderConstants.WATCH_STATUS, this.ffVideoLogDetailsInput.getWatch_status())
                        .appendQueryParameter(HeaderConstants.DEVICE_TYPE, this.ffVideoLogDetailsInput.getDevice_type())
                        .appendQueryParameter(HeaderConstants.LOG_ID, this.ffVideoLogDetailsInput.getLog_id());
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

                code = 0;


            } catch (IOException e) {
                code = 0;
            }

            JSONObject mainJson = null;
            if (responseStr != null) {
                mainJson = new JSONObject(responseStr);
                code = Integer.parseInt(mainJson.optString("code"));

                if (code == 200) {
                    if ((mainJson.has("log_id")) && mainJson.optString("log_id").trim() != null && !mainJson.optString("log_id").trim().isEmpty() && !mainJson.optString("log_id").trim().equals("null") && !mainJson.optString("log_id").trim().matches("")) {
                        videoLogId = mainJson.optString("log_id");
                    }

                } else {
                    videoLogId = "0";
                }
            }
        } catch (Exception e) {
            videoLogId = "0";

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetFFVideoLogsPreExecuteStarted();
        code = 0;
        if (!PACKAGE_NAME.equals(HeaderConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetFFVideoLogsPostExecuteCompleted(code,responseStr, videoLogId);
            return;
        }
        if (HeaderConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetFFVideoLogsPostExecuteCompleted(code, responseStr, videoLogId);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onGetFFVideoLogsPostExecuteCompleted(code,responseStr,videoLogId);
    }
}
