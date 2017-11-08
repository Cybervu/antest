package com.muvi.muviplayersdk.controller;

/**
 * Created by MUVI on 06-Oct-17.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.muvi.muviplayersdk.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class WebApiController extends AsyncTask<String, Void, String> {


    public interface TaskCompleteListener {
        void onTaskpreExecute(String Requestdata);
        void onTaskPostExecute(String Result, String requestId);
    }

    public TaskCompleteListener listener;
    String response ="";
    String keyList="";
    String valueList="";
    String requestData = "";
    String rootUrl = "";

    public WebApiController(TaskCompleteListener listener, String keyList , String valueList , String requestData ,String rootUrl) {
        this.listener = listener;
        this.keyList = keyList;
        this.valueList = valueList;
        this.requestData = requestData;
        this.rootUrl = rootUrl;
    }

    @Override
    protected String doInBackground(String... params) {

        String inputKeys[] = keyList.split(",");
        String inputValues[] = valueList.split(",");

        String urlRouteList = rootUrl.trim() + inputValues[0].trim();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlRouteList);
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            for(int i=1;i<inputKeys.length;i++)
            {
                httppost.addHeader(inputKeys[i].trim(),inputValues[i].trim());
                Log.v("BIBHU", "key============" + inputKeys[i]+" ,   value================"+inputValues[i]);
            }

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                this.response = EntityUtils.toString(response.getEntity());

                Log.v("BIBHU", "responseStr of videolog============" + this.response);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {}
        return null;
    }

    protected void onPreExecute() {
        listener.onTaskpreExecute(requestData);
    }

    @Override
    protected void onPostExecute(String s) {

      listener.onTaskPostExecute(response,requestData);
        return;
    }
}

