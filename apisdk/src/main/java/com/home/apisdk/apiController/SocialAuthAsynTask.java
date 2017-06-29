package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.SocialAuthInputModel;
import com.home.apisdk.apiModel.SocialAuthOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Muvi on 12/16/2016.
 */
public class SocialAuthAsynTask extends AsyncTask<SocialAuthInputModel, Void, Void> {
    SocialAuthInputModel socialAuthInputModel;

    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface SocialAuth {
        void onSocialAuthPreExecuteStarted();

        void onSocialAuthPostExecuteCompleted(SocialAuthOutputModel socialAuthOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private SocialAuth listener;
    private Context context;
    SocialAuthOutputModel socialAuthOutputModel = new SocialAuthOutputModel();

    public SocialAuthAsynTask(SocialAuthInputModel socialAuthInputModel, SocialAuth listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.socialAuthInputModel = socialAuthInputModel;
        Log.v("SUBHA", "LoginAsynTask");
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);

    }

    @Override
    protected Void doInBackground(SocialAuthInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getSocialauthUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.socialAuthInputModel.getAuthToken());
            httppost.addHeader("email", this.socialAuthInputModel.getEmail());
            httppost.addHeader("password", this.socialAuthInputModel.getPassword());
            httppost.addHeader("name", this.socialAuthInputModel.getName());
            httppost.addHeader("fb_userid", this.socialAuthInputModel.getFb_userid());
            httppost.addHeader("lang_code",this.socialAuthInputModel.getLanguage());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

                status = 0;
                message = "Error";
                Log.v("SUBHA", "ConnectTimeoutException" + e.toString());


            } catch (IOException e) {
                status = 0;
                message = "Error";
                Log.v("SUBHA", "IOException" + e.toString());

            }

            JSONObject mainJson = null;
            if (responseStr != null) {
                mainJson = new JSONObject(responseStr);
                status = Integer.parseInt(mainJson.optString("code"));


                if ((mainJson.has("email")) && mainJson.optString("email").trim() != null && !mainJson.optString("email").trim().isEmpty() && !mainJson.optString("email").trim().equals("null") && !mainJson.optString("email").trim().matches("")) {
                    socialAuthOutputModel.setEmail(mainJson.optString("email"));
                } else {
                    socialAuthOutputModel.setEmail("");

                }
                if ((mainJson.has("display_name")) && mainJson.optString("display_name").trim() != null && !mainJson.optString("display_name").trim().isEmpty() && !mainJson.optString("display_name").trim().equals("null") && !mainJson.optString("display_name").trim().matches("")) {

                    socialAuthOutputModel.setDisplay_name(mainJson.optString("display_name"));


                } else {
                    socialAuthOutputModel.setDisplay_name("");

                }
                if ((mainJson.has("profile_image")) && mainJson.optString("profile_image").trim() != null && !mainJson.optString("profile_image").trim().isEmpty() && !mainJson.optString("profile_image").trim().equals("null") && !mainJson.optString("profile_image").trim().matches("")) {
                    socialAuthOutputModel.setProfile_image(mainJson.optString("profile_image"));


                } else {
                    socialAuthOutputModel.setProfile_image("");

                }
                if ((mainJson.has("isSubscribed")) && mainJson.optString("isSubscribed").trim() != null && !mainJson.optString("isSubscribed").trim().isEmpty() && !mainJson.optString("isSubscribed").trim().equals("null") && !mainJson.optString("isSubscribed").trim().matches("")) {
                    socialAuthOutputModel.setIsSubscribed(mainJson.optString("isSubscribed"));
                } else {
                    socialAuthOutputModel.setIsSubscribed("");

                }
                if ((mainJson.has("nick_name")) && mainJson.optString("nick_name").trim() != null && !mainJson.optString("nick_name").trim().isEmpty() && !mainJson.optString("nick_name").trim().equals("null") && !mainJson.optString("nick_name").trim().matches("")) {
                    socialAuthOutputModel.setNick_name(mainJson.optString("nick_name"));
                } else {
                    socialAuthOutputModel.setNick_name("");

                }

                if ((mainJson.has("studio_id")) && mainJson.optString("studio_id").trim() != null && !mainJson.optString("studio_id").trim().isEmpty() && !mainJson.optString("studio_id").trim().equals("null") && !mainJson.optString("studio_id").trim().matches("")) {
                    socialAuthOutputModel.setStudio_id(mainJson.optString("studio_id"));

                } else {
                    socialAuthOutputModel.setStudio_id("");

                }

                if ((mainJson.has("msg")) && mainJson.optString("msg").trim() != null && !mainJson.optString("msg").trim().isEmpty() && !mainJson.optString("msg").trim().equals("null") && !mainJson.optString("msg").trim().matches("")) {
                    socialAuthOutputModel.setMsg(mainJson.optString("msg"));
                } else {
                    socialAuthOutputModel.setMsg("");

                }
                if ((mainJson.has("login_history_id")) && mainJson.optString("login_history_id").trim() != null && !mainJson.optString("login_history_id").trim().isEmpty() && !mainJson.optString("login_history_id").trim().equals("null") && !mainJson.optString("login_history_id").trim().matches("")) {
                    socialAuthOutputModel.setLogin_history_id(mainJson.optString("login_history_id"));
                } else {
                    socialAuthOutputModel.setLogin_history_id("");

                }
                if ((mainJson.has("id")) && mainJson.optString("id").trim() != null && !mainJson.optString("id").trim().isEmpty() && !mainJson.optString("id").trim().equals("null") && !mainJson.optString("id").trim().matches("")) {
                    socialAuthOutputModel.setId(mainJson.optString("id"));
                } else {
                    socialAuthOutputModel.setId("");

                }


            } else {
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            Log.v("SUBHA", "IOException" + e1.toString());

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
        listener.onSocialAuthPreExecuteStarted();

        status = 0;
    /*    if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onLoginPostExecuteCompleted(socialAuthOutputModel, status, message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onLoginPostExecuteCompleted(socialAuthOutputModel, status, message);
        }*/

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onSocialAuthPostExecuteCompleted(socialAuthOutputModel, status, message);

    }

}
