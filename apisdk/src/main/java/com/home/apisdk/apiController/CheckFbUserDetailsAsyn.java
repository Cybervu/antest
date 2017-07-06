package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.CheckFbUserDetailsInput;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MUVI on 7/4/2017.
 */

public class CheckFbUserDetailsAsyn extends AsyncTask<CheckFbUserDetailsInput,Void ,Void > {

    public CheckFbUserDetailsInput checkFbUserDetailsInput;
    String PACKAGE_NAME, responseStr;
    int isNewUserStr = 1;
    int code;
    JSONObject myJson = null;

    public interface CheckFbUserDetails {
        void onCheckFbUserDetailsAsynPreExecuteStarted();

        void onCheckFbUserDetailsAsynPostExecuteCompleted(int code);
    }

    private CheckFbUserDetails listener;
    private Context context;


    public CheckFbUserDetailsAsyn(CheckFbUserDetailsInput checkFbUserDetailsInput, CheckFbUserDetails listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.checkFbUserDetailsInput = checkFbUserDetailsInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "register user payment");
    }

    @Override
    protected Void doInBackground(CheckFbUserDetailsInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getFbUserExistsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.checkFbUserDetailsInput.getAuthToken());
            httppost.addHeader("fb_userid", this.checkFbUserDetailsInput.getFb_userid());


            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);


            } catch (ClientProtocolException e) {
                code = 0;
                e.printStackTrace();
            } catch (IOException e) {
                code = 0;
                e.printStackTrace();
            }

            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                isNewUserStr = Integer.parseInt(myJson.optString("is_newuser"));
            }

        } catch (JSONException e) {
            code=0;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onCheckFbUserDetailsAsynPreExecuteStarted();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onCheckFbUserDetailsAsynPostExecuteCompleted(code);
    }
}
