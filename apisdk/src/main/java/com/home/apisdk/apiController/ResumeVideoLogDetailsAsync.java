package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.ResumeVideoLogDetailsInput;

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
 * Created by MUVI on 7/6/2017.
 */

public class ResumeVideoLogDetailsAsync extends AsyncTask<ResumeVideoLogDetailsInput, Void, Void> {

    ResumeVideoLogDetailsInput resumeVideoLogDetailsInput;

    String responseStr;
    int status;
    String message, PACKAGE_NAME;
    String videoLogId = "";

    public interface ResumeVideoLogDetails {
        void onGetResumeVideoLogDetailsPreExecuteStarted();

        void onGetResumeVideoLogDetailsPostExecuteCompleted(int status, String message, String videoLogId);
    }

    private ResumeVideoLogDetails listener;
    private Context context;

    public ResumeVideoLogDetailsAsync(ResumeVideoLogDetailsInput resumeVideoLogDetailsInput, ResumeVideoLogDetails listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.resumeVideoLogDetailsInput = resumeVideoLogDetailsInput;
        Log.v("MUVISDK", "LoginAsynTask");
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);

    }

    @Override
    protected Void doInBackground(ResumeVideoLogDetailsInput... params) {

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
                        .appendQueryParameter(HeaderConstants.AUTH_TOKEN, this.resumeVideoLogDetailsInput.getAuthToken())
                        .appendQueryParameter(HeaderConstants.USER_ID, this.resumeVideoLogDetailsInput.getUser_id())
                        .appendQueryParameter(HeaderConstants.IP_ADDRESS, this.resumeVideoLogDetailsInput.getIp_address())
                        .appendQueryParameter(HeaderConstants.MOVIE_ID, this.resumeVideoLogDetailsInput.getMovie_id())
                        .appendQueryParameter(HeaderConstants.EPISODE_ID, this.resumeVideoLogDetailsInput.getEpisode_id())
                        .appendQueryParameter(HeaderConstants.PLAYED_LENGTH, this.resumeVideoLogDetailsInput.getPlayed_length())
                        .appendQueryParameter(HeaderConstants.WATCH_STATUS, this.resumeVideoLogDetailsInput.getWatch_status());

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
                    videoLogId = mainJson.optString("log_id");
                } else {
                    videoLogId = "0";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetResumeVideoLogDetailsPreExecuteStarted();
        status = 0;
        if (!PACKAGE_NAME.equals(HeaderConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetResumeVideoLogDetailsPostExecuteCompleted(status,responseStr,videoLogId);
            return;
        }
        if (HeaderConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetResumeVideoLogDetailsPostExecuteCompleted(status,responseStr,videoLogId);
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onGetResumeVideoLogDetailsPostExecuteCompleted(status,responseStr,videoLogId);
    }
}
