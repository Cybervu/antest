package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.VideoBufferLogsInputModel;
import com.home.apisdk.apiModel.VideoBufferLogsOutputModel;

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

public class UpdateVideoBufferLogDetailsAsync extends AsyncTask<VideoBufferLogsInputModel, Void, Void> {
        VideoBufferLogsInputModel videoBufferLogsInputModel;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface UpdateVideoBufferLog {
        void onUpdateVideoBufferLogPreExecuteStarted();

        void onUpdateVideoBufferLogPostExecuteCompleted(VideoBufferLogsOutputModel videoBufferLogsOutputModel, int status, String message);
    }

    public UpdateVideoBufferLogDetailsAsync(VideoBufferLogsInputModel videoBufferLogsInputModel, UpdateVideoBufferLog listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.videoBufferLogsInputModel = videoBufferLogsInputModel;
        Log.v("MUVISDK", "LoginAsynTask");
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);

    }

    private UpdateVideoBufferLog listener;
    private Context context;
    VideoBufferLogsOutputModel videoBufferLogsOutputModel = new VideoBufferLogsOutputModel();
    @Override
    protected Void doInBackground(VideoBufferLogsInputModel... params) {
        try {

            // Execute HTTP Post Request
            try {
                URL url = new URL(APIUrlConstant.getUpdateBufferLogUrl());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("authToken", this.videoBufferLogsInputModel.getAuthToken())
                        .appendQueryParameter("user_id", this.videoBufferLogsInputModel.getUserId())
                        .appendQueryParameter("ip_address", this.videoBufferLogsInputModel.getIpAddress())
                        .appendQueryParameter("movie_id", this.videoBufferLogsInputModel.getMuviUniqueId())
                        .appendQueryParameter("episode_id", this.videoBufferLogsInputModel.getEpisodeStreamUniqueId())
                        .appendQueryParameter("log_id", this.videoBufferLogsInputModel.getBufferLogId())
                        .appendQueryParameter("resolution", this.videoBufferLogsInputModel.getVideoResolution())
                        .appendQueryParameter("device_type", this.videoBufferLogsInputModel.getDeviceType())
                        .appendQueryParameter("start_time", this.videoBufferLogsInputModel.getBufferStartTime())
                        .appendQueryParameter("end_time", this.videoBufferLogsInputModel.getBufferEndTime())
                        .appendQueryParameter("log_unique_id", this.videoBufferLogsInputModel.getBufferLogUniqueId())
                        .appendQueryParameter("location", this.videoBufferLogsInputModel.getLocation());

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

                if (status == 200) {


                    if ((mainJson.has("log_id")) && mainJson.optString("log_id").trim() != null && !mainJson.optString("log_id").trim().isEmpty() && !mainJson.optString("log_id").trim().equals("null") && !mainJson.optString("log_id").trim().matches("")) {
                        videoBufferLogsOutputModel.setBufferLogId(mainJson.optString("log_id"));

                    }
                    if ((mainJson.has("log_unique_id")) && mainJson.optString("log_unique_id").trim() != null && !mainJson.optString("log_unique_id").trim().isEmpty() && !mainJson.optString("log_unique_id").trim().equals("null") && !mainJson.optString("log_unique_id").trim().matches("")) {
                        videoBufferLogsOutputModel.setBufferLogUniqueId(mainJson.optString("log_unique_id"));

                    }
                    if ((mainJson.has("location")) && mainJson.optString("location").trim() != null && !mainJson.optString("location").trim().isEmpty() && !mainJson.optString("location").trim().equals("null") && !mainJson.optString("location").trim().matches("")) {
                        videoBufferLogsOutputModel.setBufferLocation(mainJson.optString("location"));

                    }

                } else {
                    videoBufferLogsInputModel.setBufferLogUniqueId("0");
                    videoBufferLogsOutputModel.setBufferLogId("0");
                    videoBufferLogsOutputModel.setBufferLocation("0");
                }
            }
        } catch (Exception e) {
            videoBufferLogsInputModel.setBufferLogUniqueId("0");
            videoBufferLogsOutputModel.setBufferLogId("0");
            videoBufferLogsOutputModel.setBufferLocation("0");
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onUpdateVideoBufferLogPreExecuteStarted();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onUpdateVideoBufferLogPostExecuteCompleted(videoBufferLogsOutputModel,status,message);
    }
}
